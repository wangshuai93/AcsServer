package com.yinhe.server.AcsServer.RPCMethodModel;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.yinhe.server.AcsServer.ejb.RPCMethod;
import com.yinhe.server.AcsServer.model.SoapMessageModel;

public class RPCHelper {
	
	private final String SplitStr_V2 = "\\<soap:Envelope";
	private final String CompleteStr_V2 = "<soap:Envelope";
	private final String SplitStr_V1 = "\\<soap-env:Envelope";
	private final String CompleteStr_V1 = "<soap-env:Envelope";	
	
	public int CreateResponse(RPCMethod response_method, OutputStream out) {
		try {
			int responseLen = 0;
			if (response_method != null) {
				response_method.setAcs2CpeEnv(true); // 指明是从ACS-CPE的信包
				response_method.setHoldReqs(false);
				responseLen += response_method.writeTo(out);
			}
			return responseLen;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[RPCHelper.CreateResponse] CreateResponse error!");
		}
		return 0;
	}

	/**
	 * 拆分收到的数据流
	 */
	public SoapMessageModel[] parseInputStream2SoapMsgs(byte[] bs)throws Exception {
		String messageXml = new String(bs);	
		String soapXml = "";
		String startxml = "<?xml version='1.0' encoding='UTF-8'?>";
		try{
			if(messageXml.startsWith(startxml)){
				System.out.println("[RPCHelper.parseInputStream2SoapMsgs] start with xml version");
				soapXml = messageXml.substring(38);
			}else{
				System.out.println("[RPCHelper.parseInputStream2SoapMsgs] not start with xml version");
				soapXml = messageXml;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(soapXml.startsWith("\n" + CompleteStr_V1)){
			System.out.println("[RPCHelper.parseInputStream2SoapMsgs] xml form <soap-env:Envelope>");
			String[] xmls = soapXml.split(SplitStr_V1);

			//解析为SoapMessageModel，xmls[0]要避免，因为它为空
			int length = xmls.length - 1;
			SoapMessageModel[] smms = new SoapMessageModel[length];
			for (int index = 0; index < length; index++) { 
				String xml = CompleteStr_V1 + xmls[index + 1];
				
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				dbf.setNamespaceAware(true);
				DocumentBuilder db = dbf.newDocumentBuilder();
				try{
				Document doc = db.parse(new ByteArrayInputStream(xml.trim().getBytes()));
				smms[index] = new SoapMessageModel(doc);
				}catch(Exception ex){
					ex.getMessage();
					ex.printStackTrace();
				}
			}			
			return smms;
		}else if(soapXml.startsWith(CompleteStr_V2)){
			System.out.println("[RPCHelper.parseInputStream2SoapMsgs] xml form <soap:Envelope>");
			String[] xmls = soapXml.split(SplitStr_V2);

			//解析为SoapMessageModel，xmls[0]要避免，因为它为空
			int length = xmls.length - 1;
			SoapMessageModel[] smms = new SoapMessageModel[length];
			for (int index = 0; index < length; index++) { 
				String xml = CompleteStr_V2 + xmls[index + 1];
				
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				dbf.setNamespaceAware(true);
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(new ByteArrayInputStream(xml.trim().getBytes()));
				smms[index] = new SoapMessageModel(doc);
			}			
			return smms;
		}
		return null;
	}

}
