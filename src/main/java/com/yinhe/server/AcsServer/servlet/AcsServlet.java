package com.yinhe.server.AcsServer.servlet;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.yinhe.server.AcsServer.RPCMethodModel.Inform;
import com.yinhe.server.AcsServer.RPCMethodModel.InformResponse;
import com.yinhe.server.AcsServer.RPCMethodModel.RPCHelper;
import com.yinhe.server.AcsServer.RPCMethodModel.RPCMethodFactory;
import com.yinhe.server.AcsServer.auth.HttpAuthentication;
import com.yinhe.server.AcsServer.ejb.CPECallEJB;
import com.yinhe.server.AcsServer.ejb.CPEManager;
import com.yinhe.server.AcsServer.ejb.CPEProcess;
import com.yinhe.server.AcsServer.ejb.RPCMethod;
import com.yinhe.server.AcsServer.model.SoapMessageModel;
import com.yinhe.server.AcsServer.util.Resources;


@WebServlet(name="AcsServlet",asyncSupported = true,urlPatterns = { "/AcsServlet" })
public class AcsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Inject
	private Logger m_log;
	@Inject
	private CPEManager m_cpeManager;
	@Inject
	private CPECallEJB m_cpeCallEJB;
	private final int MIN_MAX_ENVELOPES = 1;    
	protected static final String LASTHOLDREQ = "lastHold";
	protected static final String CPEPROCESS = "cpeProcess";
	protected static final String MAXENVELOPES = "maxEvelopes";
	protected static final String HasReceivedNullPackage = "hasReceivedNullPackage";
	
    /**
     * 鉴权
     */
	@SuppressWarnings("unused")
	private boolean authenticate(String username, String pwd,HttpServletRequest request, HttpServletResponse response)throws IOException {
		return HttpAuthentication.Authenticate(username, pwd, request, response);
	}
	
	/**
	 * CPE端请求的处理流程
	 */
	
	@SuppressWarnings("static-access")
	protected void processRequest(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/xml;charset=UTF-8");
		//request.setCharacterEncoding("UTF-8");
		ByteArrayOutputStream out = new ByteArrayOutputStream();		
		HttpSession session = request.getSession();
		Object processAttr  = session.getAttribute(CPEPROCESS);
		Object lastHoldReqAttr = session.getAttribute(LASTHOLDREQ);  
		Object maxEnvelopesAttr = session.getAttribute(MAXENVELOPES);		
		CPEProcess cpe_process = null; 
		boolean m_lastHoldReq = false;
		boolean m_hasReceivedNullPackage = false;
	    int countEnvelopes = 0;	
	    int maxEnvelopes = 0;
	    if(processAttr != null){
	    	cpe_process = (CPEProcess)processAttr;
	    }
		if(lastHoldReqAttr != null){
			m_lastHoldReq = (boolean)lastHoldReqAttr;
		}	
		if(maxEnvelopesAttr != null){
			maxEnvelopes = (int)maxEnvelopesAttr;
		}			
        if (maxEnvelopes < MIN_MAX_ENVELOPES) {
            maxEnvelopes = 1;
        }
        
        RPCHelper rpcHelper = new RPCHelper();             
		if (request.getContentLength() == 0) {      //CPE发送内容为空,代表一次会话的开始
			//m_hasReceivedNullPackage = true;
			session.setAttribute(HasReceivedNullPackage, true);
			if(cpe_process != null){								
				cpe_process.sessionBegin();	        //sessionBegin
			}			
		} else {  	
			try {				
				ServletInputStream inputStream = null;
				int inputStream_len = 0;
				byte[] bs = new byte[65535];
				//char[] bc = new char[65535];
				while(true){
				    inputStream = request.getInputStream();					
					if(request.getInputStream().isFinished()){
						break;
					}
					int available = inputStream.available();
					inputStream.read(bs, inputStream_len, available);
					inputStream_len += available;
					
					//InputStreamReader isr = new InputStreamReader(request.getInputStream(),"UTF-8");
					//isr.read(bc, inputStream_len, available);
					//test
					//for(int i =0;i<available;i++){
					//	m_log.info("[pendingRequest] hhhhh = "+ bs[i]);
					//}
					//m_log.info("[pendingRequest] sssss = "+ (new String(bs)));
					//String test_sring =new String(bs);
					//byte[] b_utf8 = test_sring.getBytes("UTF-8");
					//byte[] b_uiso88591 = test_sring.getBytes("ISO8859-1");
					//for(int i=0;i<available;i++)
					//{
					//	System.out.println("[RPCHelper.parseInputStream2SoapMsgs] tttttb_utf8:"+b_utf8[i]);
					//	System.out.println("[RPCHelper.parseInputStream2SoapMsgs] tttttb_uiso88591:"+b_uiso88591[i]);
					//}
					//test
				}	
				m_log.info("[pendingRequest] totalLength = "+ inputStream_len);			
				m_log.info("[pendingRequest] request.getInputStream().isFinished() = "+request.getInputStream().isFinished());
				int soap_length = 0;
				SoapMessageModel[] soapMsgs = rpcHelper.parseInputStream2SoapMsgs(bs);
				if(soapMsgs != null){
					soap_length = soapMsgs.length;		
				}
				
				SoapMessageModel cur_soapMsg = null;					
				for (int index = 0; index < soap_length; index++){ 					
					cur_soapMsg = soapMsgs[index];
					String req_name = RPCMethod.getRequestName(cur_soapMsg);
					m_log.info("[pendingRequest] cpeRequestName = " +req_name);
					RPCMethod cpe2acs_method = null;
					cpe2acs_method = RPCMethodFactory.getInstance().getAbstractMethod(req_name);
					cpe2acs_method.parse(cur_soapMsg);					
					if (req_name.equals("Inform")) { 
						Inform lastInform = (Inform) cpe2acs_method;	
						String serial_number = lastInform.getDeviceId().getSerialNubmer();	
						m_log.info("[pendingRequest] serial_number = " + serial_number);
						
						//test=============
						if(Resources.isNull(serial_number)){
							//serial_number = "123456789";
						}
						
						try{
							cpe_process = m_cpeManager.getCurrentCPEProcess(serial_number);	  //cpe_process为null
							if(cpe_process == null){
								cpe_process = new CPEProcess();
							}
						}catch(Exception ex){
							m_log.info("[pendingRequest] get this CPEProcess error:"  + ex.getMessage());
						}				
						session.setAttribute(MAXENVELOPES, lastInform.getMaxEnvelopes());
						session.setAttribute(LASTHOLDREQ, lastInform.isHoldReqs());
						//Inform回复消息							
						InformResponse m_informResponse = (InformResponse) m_cpeCallEJB.call(cpe_process,lastInform);	
						session.setAttribute(CPEPROCESS, cpe_process);	
						rpcHelper.CreateResponse(m_informResponse,out);						
						countEnvelopes++;						
						maxEnvelopes = lastInform.getMaxEnvelopes();	
						
					} else {      //其他RPC方法      
						RPCMethod response2cpe_method = null;
						if(cpe_process != null){
							response2cpe_method = m_cpeCallEJB.call(cpe_process,cpe2acs_method);
							if(response2cpe_method != null){			
								session.setAttribute(LASTHOLDREQ, response2cpe_method.isHoldReqs());
								rpcHelper.CreateResponse(response2cpe_method,out);	
								countEnvelopes++;
							}	
						}																								
					}
				}				 
			} catch (Exception e) {
				m_log.info("[processRequest] ParseMethodFromCPE exception:" + e.getMessage());
				e.printStackTrace();
				if(cpe_process != null){
					cpe_process.sessionEnd();
				}
			}		
		}
		 
		//若已创建的回复小于CPE可接收的xml封包个数
		while (countEnvelopes < maxEnvelopes ){
		    RPCMethod req2cpe = null;	
		    long tempTime = System.currentTimeMillis();
		    long wait = 200;
		    while(System.currentTimeMillis() - tempTime < wait){
		    	if(cpe_process != null){
			    	req2cpe = cpe_process.pendingRequest();
			    	if(req2cpe != null){
			    		break;
			    	}			    	
			    }		    	
		    }		
		    
			m_log.info("[pendingRequest] " + ((req2cpe == null) ? "null" : req2cpe.getMethodName()));
			if(req2cpe != null){
				session.setAttribute(LASTHOLDREQ, req2cpe.isHoldReqs());
				rpcHelper.CreateResponse(req2cpe, out);
			}			
			countEnvelopes++;
		}
		
		//response	
		response.setContentLength(out.size());
		if (out.size() == 0) {
			m_log.info("[pendingRequest] out.size = 0");
			response.setStatus(response.SC_NO_CONTENT);	
			
			//判断是否结束此次会话
		    lastHoldReqAttr = session.getAttribute(LASTHOLDREQ);
			if(lastHoldReqAttr != null){				
				 m_lastHoldReq = (boolean)lastHoldReqAttr;
			}
			Object receivedNullPackage = session.getAttribute(HasReceivedNullPackage);
			if(receivedNullPackage != null){
				m_hasReceivedNullPackage = (boolean)receivedNullPackage;
			}
			if(!m_lastHoldReq && m_hasReceivedNullPackage){ 
				 if(cpe_process != null){
					 m_log.info("[pendingRequest] session go to end!");	
					 cpe_process.sessionEnd();
				 }	
			 }
	    }else{
	    	response.getOutputStream().print(out.toString());
	    }
		out.close();
		m_log.info("[processRequest] Response to CPE Complete!");		
	} 

	/**
	 * 测试
	 */
	/*
	@SuppressWarnings("static-access")
	protected void processRequest(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/xml;charset=UTF-8");
		//request.setCharacterEncoding("UTF-8");
		ByteArrayOutputStream out = new ByteArrayOutputStream();		
		HttpSession session = request.getSession();
		Object processAttr  = session.getAttribute(CPEPROCESS);
		Object lastHoldReqAttr = session.getAttribute(LASTHOLDREQ);  
		Object maxEnvelopesAttr = session.getAttribute(MAXENVELOPES);		
		CPEProcess cpe_process = null; 
		boolean m_lastHoldReq = false;
		boolean m_hasReceivedNullPackage = false;
	    int countEnvelopes = 0;	
	    int maxEnvelopes = 0;
	    if(processAttr != null){
	    	cpe_process = (CPEProcess)processAttr;
	    }
		if(lastHoldReqAttr != null){
			m_lastHoldReq = (boolean)lastHoldReqAttr;
		}	
		if(maxEnvelopesAttr != null){
			maxEnvelopes = (int)maxEnvelopesAttr;
		}			
        if (maxEnvelopes < MIN_MAX_ENVELOPES) {
            maxEnvelopes = 1;
        }
        
        RPCHelper rpcHelper = new RPCHelper();             
		if (request.getContentLength() == 0) {      //CPE发送内容为空,代表一次会话的开始
			//m_hasReceivedNullPackage = true;
			session.setAttribute(HasReceivedNullPackage, true);
			if(cpe_process != null){								
				cpe_process.sessionBegin();	        //sessionBegin
			}			
		} else { 
			
			try {
				InputStreamReader isr = new InputStreamReader(request.getInputStream(),"UTF-8");

				char bc;
				int b = 0;
				int tmp;
				String results ="";
				while((tmp = isr.read()) != -1 ){
					bc = (char)tmp;
					m_log.info("[pendingRequest] TMP_char = "+ bc);
					results += (char)tmp;	
					m_log.info("[pendingRequest] TMP_int = "+ (int)bc);	
				}	
				isr.close();
				m_log.info("[pendingRequest] Charset = "+ Charset.defaultCharset().name());
				results = new String(results.getBytes(),"UTF-8");
				m_log.info("[pendingRequest] results = "+ results);	
				
				//m_log.info("[pendingRequest] totalLength = "+ inputStream_len);			
				//m_log.info("[pendingRequest] request.getInputStream().isFinished() = "+request.getInputStream().isFinished());
				int soap_length = 0;
				SoapMessageModel[] soapMsgs = rpcHelper.parseInputStream2SoapMsgs(results);
				if(soapMsgs != null){
					soap_length = soapMsgs.length;		
				}
				
				SoapMessageModel cur_soapMsg = null;					
				for (int index = 0; index < soap_length; index++){ 					
					cur_soapMsg = soapMsgs[index];
					String req_name = RPCMethod.getRequestName(cur_soapMsg);
					m_log.info("[pendingRequest] cpeRequestName = " +req_name);
					RPCMethod cpe2acs_method = null;
					cpe2acs_method = RPCMethodFactory.getInstance().getAbstractMethod(req_name);
					cpe2acs_method.parse(cur_soapMsg);					
					if (req_name.equals("Inform")) { 
						Inform lastInform = (Inform) cpe2acs_method;	
						String serial_number = lastInform.getDeviceId().getSerialNubmer();	
						m_log.info("[pendingRequest] serial_number = " + serial_number);
						
						//test=============
						if(Resources.isNull(serial_number)){
							//serial_number = "123456789";
						}
						
						try{
							cpe_process = m_cpeManager.getCurrentCPEProcess(serial_number);	  //cpe_process为null
							if(cpe_process == null){
								cpe_process = new CPEProcess();
							}
						}catch(Exception ex){
							m_log.info("[pendingRequest] get this CPEProcess error:"  + ex.getMessage());
						}				
						session.setAttribute(MAXENVELOPES, lastInform.getMaxEnvelopes());
						session.setAttribute(LASTHOLDREQ, lastInform.isHoldReqs());
						//Inform回复消息							
						InformResponse m_informResponse = (InformResponse) m_cpeCallEJB.call(cpe_process,lastInform);	
						session.setAttribute(CPEPROCESS, cpe_process);	
						rpcHelper.CreateResponse(m_informResponse,out);						
						countEnvelopes++;						
						maxEnvelopes = lastInform.getMaxEnvelopes();	
						
					} else {      //其他RPC方法      
						RPCMethod response2cpe_method = null;
						if(cpe_process != null){
							response2cpe_method = m_cpeCallEJB.call(cpe_process,cpe2acs_method);
							if(response2cpe_method != null){			
								session.setAttribute(LASTHOLDREQ, response2cpe_method.isHoldReqs());
								rpcHelper.CreateResponse(response2cpe_method,out);	
								countEnvelopes++;
							}	
						}																								
					}
				}				 
			} catch (Exception e) {
				m_log.info("[processRequest] ParseMethodFromCPE exception:" + e.getMessage());
				e.printStackTrace();
				if(cpe_process != null){
					cpe_process.sessionEnd();
				}
			}		
		}
		 
		//若已创建的回复小于CPE可接收的xml封包个数
		while (countEnvelopes < maxEnvelopes ){
		    RPCMethod req2cpe = null;	
		    long tempTime = System.currentTimeMillis();
		    long wait = 200;
		    while(System.currentTimeMillis() - tempTime < wait){
		    	if(cpe_process != null){
			    	req2cpe = cpe_process.pendingRequest();
			    	if(req2cpe != null){
			    		break;
			    	}			    	
			    }		    	
		    }		
		    
			m_log.info("[pendingRequest] " + ((req2cpe == null) ? "null" : req2cpe.getMethodName()));
			if(req2cpe != null){
				session.setAttribute(LASTHOLDREQ, req2cpe.isHoldReqs());
				rpcHelper.CreateResponse(req2cpe, out);
			}			
			countEnvelopes++;
		}
		
		//response	
		response.setContentLength(out.size());
		if (out.size() == 0) {
			m_log.info("[pendingRequest] out.size = 0");
			response.setStatus(response.SC_NO_CONTENT);	
			
			//判断是否结束此次会话
		    lastHoldReqAttr = session.getAttribute(LASTHOLDREQ);
			if(lastHoldReqAttr != null){				
				 m_lastHoldReq = (boolean)lastHoldReqAttr;
			}
			Object receivedNullPackage = session.getAttribute(HasReceivedNullPackage);
			if(receivedNullPackage != null){
				m_hasReceivedNullPackage = (boolean)receivedNullPackage;
			}
			if(!m_lastHoldReq && m_hasReceivedNullPackage){ 
				 if(cpe_process != null){
					 m_log.info("[pendingRequest] session go to end!");	
					 cpe_process.sessionEnd();
				 }	
			 }
	    }else{
	    	response.getOutputStream().print(out.toString());
	    }
		out.close();
		m_log.info("[processRequest] Response to CPE Complete!");		
	}
	*/

	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		m_log.info("[doGet] Process doGet");
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		m_log.info("[doPost] Process doPost");
		processRequest(request, response);
	}	
	
	public void pendingRequest(int countEnvelopes,int maxEnvelopes,CPEProcess cpe_process,ByteArrayOutputStream out,
			HttpSession session,RPCHelper rpcHelper){
		//若已创建的回复小于CPE可接收的xml封包个数
		while (countEnvelopes < maxEnvelopes ){
		    RPCMethod req2cpe = null;	
		    long tempTime = System.currentTimeMillis();
		    long wait = 200;
		    while(System.currentTimeMillis() - tempTime < wait){
		    	if(cpe_process != null){
			    	req2cpe = cpe_process.pendingRequest();
			    	if(req2cpe != null){
			    		break;
			    	}			    	
			    }		    	
		    }		
		    
			m_log.info("[pendingRequest] " + ((req2cpe == null) ? "null" : req2cpe.getMethodName()));
			if(req2cpe != null){
				session.setAttribute(LASTHOLDREQ, req2cpe.isHoldReqs());
				rpcHelper.CreateResponse(req2cpe, out);
			}			
			countEnvelopes++;
		}		
	}
	
	//response	
	@SuppressWarnings("static-access")
	public void response(HttpServletResponse response,ByteArrayOutputStream out,Object lastHoldReqAttr,
			HttpSession session,boolean m_lastHoldReq,CPEProcess cpe_process,boolean m_hasReceivedNullPackage) throws IOException{
		response.setContentLength(out.size());
		if (out.size() == 0) {
			m_log.info("[response] out.size == 0");
			response.setStatus(response.SC_NO_CONTENT);	
			
			//判断是否结束此次会话
		    lastHoldReqAttr = session.getAttribute(LASTHOLDREQ);
			if(lastHoldReqAttr != null){				
				 m_lastHoldReq = (boolean)lastHoldReqAttr;
			}
			Object receivedNullPackage = session.getAttribute(HasReceivedNullPackage);
			if(receivedNullPackage != null){
				m_hasReceivedNullPackage = (boolean)receivedNullPackage;
			}
			if(!m_lastHoldReq && m_hasReceivedNullPackage){ 
				 if(cpe_process != null){
					 cpe_process.sessionEnd();
				 }	
			 }
	    }else{
	    	response.getOutputStream().print(out.toString());
	    }
		m_log.info("[response] Response to CPE Complete!");		
	}
}
