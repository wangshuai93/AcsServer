package com.yinhe.server.AcsServer.ejb;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import com.yinhe.server.AcsServer.RPCMethodModel.AddObject;
import com.yinhe.server.AcsServer.RPCMethodModel.DeleteObject;
import com.yinhe.server.AcsServer.RPCMethodModel.Download;
import com.yinhe.server.AcsServer.RPCMethodModel.GetParameterAttributes;
import com.yinhe.server.AcsServer.RPCMethodModel.GetParameterNames;
import com.yinhe.server.AcsServer.RPCMethodModel.GetParameterValues;
import com.yinhe.server.AcsServer.RPCMethodModel.Reboot;
import com.yinhe.server.AcsServer.RPCMethodModel.SetParameterAttributes;
import com.yinhe.server.AcsServer.RPCMethodModel.SetParameterValues;
import com.yinhe.server.AcsServer.auth.HMACSHA1;
import com.yinhe.server.AcsServer.backbean.ParamGetBean;
import com.yinhe.server.AcsServer.backbean.ParamSetBean;
import com.yinhe.server.AcsServer.backbean.RPCParameterBean;
import com.yinhe.server.AcsServer.backbean.TaskParameter;
import com.yinhe.server.AcsServer.backbean.UpgradeParameter;
import com.yinhe.server.AcsServer.enums.TaskResultCode;
import com.yinhe.server.AcsServer.model.Devices;
import com.yinhe.server.AcsServer.model.ParamGet;
import com.yinhe.server.AcsServer.struct.ParameterList;
import com.yinhe.server.AcsServer.struct.ParameterValueStruct;
import com.yinhe.server.AcsServer.struct.ParameterValueStructBoolean;
import com.yinhe.server.AcsServer.struct.ParameterValueStructDateTime;
import com.yinhe.server.AcsServer.struct.ParameterValueStructInt;
import com.yinhe.server.AcsServer.struct.ParameterValueStructObject;
import com.yinhe.server.AcsServer.struct.ParameterValueStructStr;
import com.yinhe.server.AcsServer.struct.ParameterValueStructUnsignedInt;
import com.yinhe.server.AcsServer.struct.SetParameterAttributesStruct;
import com.yinhe.server.AcsServer.util.GenerateRandom;
import com.yinhe.server.AcsServer.util.Resources;


public class CPEProcess {
	
	private Devices devices;
	private List<RPCMethod> m_rpcMethodList = new ArrayList<RPCMethod>();
	//设置参数的结果,
	//0:开启成功，1:TASK_START_PARAMETERS_NOT_YET_APPLIED,
	//-1:超时，
	//2:出错
	//3:主动连接成功，参数是否设置成功未知
	private int m_setParam_status = -1;           
	private boolean m_hasGet_param = false;       //是否已获得参数,8 Diagnostic Complete上报
	private TaskResultCode m_taskResultCode;
	private Object lock = new Object();
	private boolean m_sessionLocked = false;	
	private boolean has_get = false;	
	private String cur_task;
	private String rpc_result;
	private boolean is_SingleOperation = false;
	
	public boolean isIs_SingleOperation() {
		return is_SingleOperation;
	}
	public void setIs_SingleOperation(boolean is_SingleOperation) {
		this.is_SingleOperation = is_SingleOperation;
	}
	public String getCur_task() {
		return cur_task;
	}
	public void setCur_task(String cur_task) {
		this.cur_task = cur_task;
	}
	public List<RPCMethod> getM_rpcMethodList() {
		return m_rpcMethodList;
	}
	public void setM_rpcMethodList(List<RPCMethod> m_rpcMethodList) {
		this.m_rpcMethodList = m_rpcMethodList;
	}
	public boolean isHas_get() {
		return has_get;
	}
	public void setHas_get(boolean has_get) {
		this.has_get = has_get;
	}
	public Devices getDevices() {
		return devices;
	}
	public void setDevices(Devices devices) {
		System.out.println("[setDevices] CPEProcess setDevices,device sn = " + devices.getM_serialNumber());
		this.devices = devices;
	}
	public int getM_setParam_status() {
		return m_setParam_status;
	}
	public void setM_setParam_status(int m_setParam_status) {
		this.m_setParam_status = m_setParam_status;
	}
	public boolean isM_hasGet_param() {
		return m_hasGet_param;
	}
	public void setM_hasGet_param(boolean m_hasGet_param) {
		this.m_hasGet_param = m_hasGet_param;
	}
	public TaskResultCode getM_taskResultCode() {
		return m_taskResultCode;
	}
	public void setM_taskResultCode(TaskResultCode m_taskResultCode) {
		this.m_taskResultCode = m_taskResultCode;
	}
	public boolean isM_sessionLocked() {
		return m_sessionLocked;
	}
	public void setM_sessionLocked(boolean m_sessionLocked) {
		this.m_sessionLocked = m_sessionLocked;
	}		
	public String getRpc_result() {
		return rpc_result;
	}
	public void setRpc_result(String rpc_result) {
		this.rpc_result = rpc_result;
	}
	
	/**
	 * ACS主动发起TCP连接
	 * @param cpe_url RequestConnectionURL
	 */
	public int requestCpeTCPConnection(String cpe_url){
    	int responseCode = 0;
    	
    	HttpClient client = new HttpClient();
    	client.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
    	GetMethod myGet = new GetMethod(cpe_url);  
    	myGet.setRequestHeader("Connection", "keep-alive"); 
    	try {
			responseCode = client.executeMethod(myGet);
			if(responseCode == 401){
				String username = this.devices.getM_connectRequestUsername();     
				String password = this.devices.getM_connectRequestPwd();
				System.out.println("[requestCpeTCPConnection] CPEProcess requestCpeTCPConnection2,username = " + username + ",password = " + password);
				String tok = username + ":" + password;
				@SuppressWarnings("restriction")
				String hash = (new sun.misc.BASE64Encoder()).encode(tok.getBytes());
				System.out.println("[requestCpeTCPConnection] CPEProcess requestCpeTCPConnection,hash = " + hash);
		    	myGet.setRequestHeader("Authorization","Basic " + hash);
		    	responseCode = client.executeMethod(myGet);
			}
		} catch (IOException e) {	
			this.m_rpcMethodList.clear();    //清空RPC方法列表
			System.out.println("[requestCpeTCPConnection] CPEProcess requestCpeTCPConnection,Request CPE Connection Exception:" + e.getMessage());
		}
    	System.out.println("[requestCpeTCPConnection] CPEProcess requestCpeTCPConnection,responseCode = " + responseCode);
        return responseCode;
    }
	
	/**
	 * ACS主动发起UDP连接
	 */
	@SuppressWarnings({ "resource", "unused" })
	public void requestCpeUDPConnection(String cpe_url){
		String[] url_port = cpe_url.split(":");
		String url = url_port[0];
		int port = Integer.parseInt(url_port[1]);
		String host = "Host:" + url + ":" + port + "\r\n";
		System.out.println("[requestCpeUDPConnection] CPEProcess requestCpeUDPConnection,cpe url = " + url + ",cpe port = " + port);
		
		//鉴权，queryString				
		long ts = System.currentTimeMillis();
		long id = 0L;         //随机数，不同的UDP请求不同
		String un = "ac5entry";      //Device.ManagementServer.ConnectionRequestUsername		
		String cn = GenerateRandom.getRandomString(16);   //随机字符串,长度如何确定?		
		String pwd = "testcpe";     //Device.ManagementServer.ConnectionRequestPassword
		StringBuilder data = new StringBuilder();
		data.append(ts);
		data.append(id);
		data.append(un);
		data.append(cn);
		String sig = HMACSHA1.hamcsha1(data.toString().getBytes(), pwd.getBytes());     //签名
		String queryString = "?" + "&ts=" + ts + "&id=" + id + "&un" + un + "&cn=" + cn + "&sig=" + sig;		
		String requestLine = "GET /" + queryString + " HTTP/1.1\r\n";
		
		String requestLine_test = "GET / HTTP/1.1\r\n";
		String[] header = {requestLine_test,host,"\r\n"};
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i < header.length;i++){
			sb.append(header[i]);
		}
		byte[] request = sb.toString().getBytes();
		InetAddress cpeAdd = null;
		try {
			cpeAdd = InetAddress.getByName(url);
		} catch (UnknownHostException e1) {
			this.m_rpcMethodList.clear();    //清空RPC方法列表
			e1.printStackTrace();
		}		
		DatagramPacket sendPKt = new DatagramPacket(request,request.length,cpeAdd,port);
		try {
			DatagramSocket socket = new DatagramSocket(null);	
			socket.setReuseAddress(true);
			socket.bind(new InetSocketAddress(3478));
			System.out.println("[requestCpeUDPConnection] CPEProcess requestCpeUDPConnection,REUSEADDR is enables:" + socket.getReuseAddress());
			System.out.println("[requestCpeUDPConnection] CPEProcess requestCpeUDPConnection,Port:" + socket.getLocalPort());
			socket.send(sendPKt);	
		} catch (IOException e) {
			this.m_rpcMethodList.clear();    //清空RPC方法列表
			e.printStackTrace();			
		}
    }
	
    
	/**
	 *  开启一个任务
	 * @param type  
	 * @param param  
	 * @param ms     等待结果时间
	 * @return  
	 */
	public synchronized TaskResultCode start(String type, TaskParameter param, long ms) {
		return null;
	}		

	/**
	 * 
	 * @param type
	 * @param ms
	 * @return
	 */
	public synchronized TaskResultCode stop(String type, long ms) {
		synchronized(lock){
			if (m_sessionLocked) 
				return TaskResultCode.TASK_RESULT_DEVICE_BUSY;
			
			this.m_setParam_status = -1;                                       
			this.m_taskResultCode = TaskResultCode.TASK_STOP_ERROR;
			
			ParameterList para_list = new ParameterList();
			ParameterValueStructBoolean pvs = null;
		    //设置停止周期任务参数为true????????????????????????
			if(type.equals("ping")){
				pvs = new ParameterValueStructBoolean("Device.LAN.IPPingDiagnostics.Stop",true);
			}else if(type.equals("http")){
				pvs = new ParameterValueStructBoolean("Device.LAN.IPHttpDiagnostics.Stop",true);
			}else if(type.equals("tcp/udp")){
				pvs = new ParameterValueStructBoolean("Device.LAN.IPTCPUDPDiagnostics.Stop",true);
			}else if(type.equals("broadcast")){
				pvs = new ParameterValueStructBoolean("Device.LAN.BroadcastDiagnostics.Stop",true);
			}else if(type.equals("record")){
				pvs = new ParameterValueStructBoolean("Device.LAN.RecordDiagnostics.Stop",true);
			}
		    para_list.addParamValues(pvs);
		    SetParameterValues spv_method = new SetParameterValues(para_list);
		    m_rpcMethodList.add(spv_method); 	
		}		
						 
		//ACS主动发起连接
		TaskResultCode request_result = requestCPEConnection();
		if(request_result != TaskResultCode.TASK_REQUEST_CPE_CONNECTION_OK){
			return request_result;
		}					
		
		//等待任务停止结果
		System.out.println("[stop] CPEProcess stop, begin to wait task stop result!");
		long tempTime = System.currentTimeMillis();
		while(System.currentTimeMillis() - tempTime < ms){
			if(!m_sessionLocked){				
				if(this.m_setParam_status == 0 || this.m_setParam_status == 1){
					return TaskResultCode.TASK_STOP_OK;
				}	
			}			
		}
		System.out.println("[stop] CPEProcess stop, m_sessionLocked = " + m_sessionLocked + ",m_setParam_status = " + m_setParam_status);
		if(!m_sessionLocked){				
			if(this.m_setParam_status == 0 || this.m_setParam_status == 1){
				return TaskResultCode.TASK_STOP_OK;
			}	
		}else{
			if(this.m_setParam_status == -1){
				sessionEnd();
				return TaskResultCode.TASK_STOP_TIMEOUT;
			}
		}
		System.out.println("[stop] CPEProcess stop, taskResultCode = " + m_taskResultCode.toString());
    	return m_taskResultCode;
	}		
	
	public boolean sessionBegin() {
		synchronized(lock){			
			if (m_sessionLocked) 
				return false;
			m_sessionLocked = true;
			System.out.println("[sessionBegin] sessionBegin, m_sessionLocked = " + m_sessionLocked);
		}
		return true;
	}
	
	public void sessionEnd() {
		synchronized(lock){
			if(!m_sessionLocked){
				return;
			}		
			m_sessionLocked = false;
			System.out.println("[sessionEnd] sessionEnd,m_sessionLocked = " + m_sessionLocked);
			this.is_SingleOperation = false;
			this.m_rpcMethodList.clear();   //清空RPC
		}		
	}
	 
	public RPCMethod pendingRequest() {
		RPCMethod req2CPE = null;
		if(m_rpcMethodList.size() > 0){			
			req2CPE = m_rpcMethodList.get(0);
			m_rpcMethodList.remove(0);
			return req2CPE;		
		}
		return null;
	}		
	
	/**
	 * 设备发起boot或bootstrap时设置
	 */
	public void addBootEventRPCMethod(){
		/*//设置stun内容
		ParameterList param_list = new ParameterList();
		ParameterValueStructBoolean pvs1 = new ParameterValueStructBoolean("Device.ManagementServer.STUNEnable",true);
		param_list.addParamValues(pvs1);
		ParameterValueStructStr pvs2 = new ParameterValueStructStr("Device.ManagementServer.STUNServerPort","3478");
		param_list.addParamValues(pvs2);
		ParameterValueStructStr pvs3 = new ParameterValueStructStr("Device.ManagementServer.STUNServerAddress","172.16.218.1");
		param_list.addParamValues(pvs3);
		ParameterValueStructStr pvs4 = new ParameterValueStructStr("Device.ManagementServer.STUNMaximumKeepAlivePeriod","180");
		param_list.addParamValues(pvs4);
		ParameterValueStructStr pvs5 = new ParameterValueStructStr("Device.ManagementServer.STUNMinimumKeepAlivePeriod","180");
		param_list.addParamValues(pvs5);
		SetParameterValues stunEnable_rpc = new SetParameterValues(param_list);
		m_rpcMethodList.add(stunEnable_rpc);
		
		//发现设备是否在NAT后
		List<SetParameterAttributesStruct> attr_list = new ArrayList<SetParameterAttributesStruct>();
		SetParameterAttributes set_param_attr = new SetParameterAttributes();
		SetParameterAttributesStruct spa1 = new SetParameterAttributesStruct();
		spa1.setName("Device.ManagementServer.NATDetected");
		spa1.setNotificationChange(true);
		spa1.setNotification(2);
		spa1.setAccessListChange(false);
		attr_list.add(spa1);
		SetParameterAttributesStruct spa2 = new SetParameterAttributesStruct();
		spa2.setName("Device.ManagementServer.UDPConnectionRequestAddress");
		spa2.setNotificationChange(true);
		spa2.setNotification(2);
		spa2.setAccessListChange(false);
		attr_list.add(spa2);
		set_param_attr.setSetParamAttrList(attr_list);
		m_rpcMethodList.add(set_param_attr);*/		
	}

	/**
	 * 待应用,已测试
	 * @param paramGet_list
	 */
	public void addGetParamRPCMethod(List<ParamGet> paramGet_list){
		if(paramGet_list == null || paramGet_list.size() == 0){
			System.out.println("[addGetParamRPCMethod] CPEProcess addGetParamRPCMethod,paramGet_list == null");
			return;
		}
		GetParameterValues gpv_method = null;
		List<String> param_name_list = new ArrayList<String>();
		for(ParamGet pg : paramGet_list){
			param_name_list.add(pg.getM_nodeTask().getM_node().getNode_path());
			System.out.println("CPEProcess addGetParamRPCMethod," + pg.getM_nodeTask().getM_node().getNode_path());
		}
		gpv_method = new GetParameterValues(param_name_list);
		m_rpcMethodList.add(gpv_method); 
	}
	 
	public synchronized TaskResultCode reboot() {
		synchronized(lock){
			if (m_sessionLocked) 
				return TaskResultCode.TASK_RESULT_DEVICE_BUSY;
			
			m_setParam_status = -1;
			this.m_taskResultCode = TaskResultCode.TASK_REBOOT_ERROR;
		    Reboot rpc_reboot = new Reboot();
		    m_rpcMethodList.add(rpc_reboot);
		}
		
		//ACS主动发起连接
		TaskResultCode request_result = requestCPEConnection();
		if(request_result != TaskResultCode.TASK_REQUEST_CPE_CONNECTION_OK){
			return request_result;
		}				
		
		//等待任务开始结果
		long ms = 1000;
		System.out.println("[reboot] CPEProcess start,begin to wait task reboot result");
		long tempTime = System.currentTimeMillis();
		while(System.currentTimeMillis() - tempTime < ms){
			if(!m_sessionLocked){ 				
				if(m_setParam_status == 0 || m_setParam_status == 1){						
					return TaskResultCode.TASK_REBOOT_OK;
				}
			}				
		}
		System.out.println("[reboot] CPEProcess start,m_sessionLocked = " + m_sessionLocked + ",m_setParam_status = " + m_setParam_status);
		if(!m_sessionLocked){
			if(m_setParam_status == 0 || m_setParam_status == 1){						
				return TaskResultCode.TASK_REBOOT_OK;
			}
		}else{
			if(this.m_setParam_status == -1){	
            	System.out.println("[reboot] CPEProcess reboot, timeout");
				sessionEnd();
				return TaskResultCode.TASK_REBOOT_TIMEOUT;
			}	
		}
		System.out.println("[reboot] CPEProcess reboot,taskResultCode = " + m_taskResultCode.toString());
    	return this.m_taskResultCode;					
	}
	
	public synchronized TaskResultCode upgrade(UpgradeParameter m_upgradeParamter) {
		synchronized(lock){
			if (m_sessionLocked) 
				return TaskResultCode.TASK_RESULT_DEVICE_BUSY;
			
			m_setParam_status = -1;
			this.m_taskResultCode = TaskResultCode.TASK_UPDATE_ERROR;
		    Download rpc_download = new Download(m_upgradeParamter.getCommandKey(),m_upgradeParamter.getUrl(),m_upgradeParamter.getFileType());
		    m_rpcMethodList.add(rpc_download);
		}
		
		//ACS主动发起连接
		TaskResultCode request_result = requestCPEConnection();
		if(request_result != TaskResultCode.TASK_REQUEST_CPE_CONNECTION_OK){
			return request_result;
		}		
		
		//等待任务开始结果
		System.out.println("CPEProcess start,begin to wait task upgrade result!");
		long ms = 1000;
		long tempTime = System.currentTimeMillis();
		while(System.currentTimeMillis() - tempTime < ms){
			if(!m_sessionLocked){ 				
				if(m_setParam_status == 0 || m_setParam_status == 1){						
					return TaskResultCode.TASK_UPDATE_OK;
				}
			}				
		}
		System.out.println("[upgrade] CPEProcess start,m_sessionLocked = " + m_sessionLocked + ",m_setParam_status = " + m_setParam_status);
		if(!m_sessionLocked){
			if(m_setParam_status == 0 || m_setParam_status == 1){						
				return TaskResultCode.TASK_UPDATE_OK;
			}
		}else{
			if(this.m_setParam_status == -1){
            	System.out.println("[upgrade] CPEProcess.start:timeout");
				sessionEnd(); 
				return TaskResultCode.TASK_UPDATE_TIMEOUT;
			}						
		}
		System.out.println("[upgrade] CPEProcess.start:taskResultCode:" + m_taskResultCode.toString());
    	return this.m_taskResultCode;					
	}
	
	public TaskResultCode requestCPEConnection(){
		//ACS主动发起连接
		int responseCode = 0;
		String cpe_url = this.devices.getM_ip();
		if(Resources.isNullOrEmpty(cpe_url)){
			this.m_rpcMethodList.clear();                                     //清空RPC方法列表
			return TaskResultCode.TASK_REQUEST_CPE_URL_NULL;
		}
		
		System.out.println("[requestCPEConnection] CPEProcess requestCPEConnection,isNAT = " + this.devices.getM_inNet());
	    if(!this.devices.getM_inNet()){                                       //不在NAT后，则使用tcp连接	    	
	    	try {
				responseCode = requestCpeTCPConnection(cpe_url);
				if(responseCode == 200){
					return TaskResultCode.TASK_REQUEST_CPE_CONNECTION_OK;
				}else{
					return TaskResultCode.TASK_REQUEST_CPE_CONNECTION_ERROR;
				}
			} catch (Exception e) {
				this.m_rpcMethodList.clear();                                  //清空RPC方法列表
				System.out.println("[requestCPEConnection] CPEProcess requestCPEConnection,requestConn ERROR:" + e.getMessage() );
				return TaskResultCode.TASK_REQUEST_CPE_CONNECTION_ERROR;
			}
	    }else{                                                            //在NAT则udp连接
	    	try {
				requestCpeUDPConnection(cpe_url);
				return TaskResultCode.TASK_REQUEST_CPE_CONNECTION_OK;
			} catch (Exception e) {
				this.m_rpcMethodList.clear();    //清空RPC方法列表
				System.out.println("[requestCPEConnection] CPEProcess requestCPEConnection,requestConn ERROR:" + e.getMessage() );
				return TaskResultCode.TASK_REQUEST_CPE_CONNECTION_ERROR;
			}	    	
	    }
	}
	
	@SuppressWarnings("rawtypes")
	public String startExcuteRPC(RPCParameterBean rpc_parameterBean){
		System.out.println("get into startExcuteRPC");
		long ms = 1000L;
		synchronized(lock){
			if (m_sessionLocked) 
				return "设备正忙！";	
			
			if(rpc_parameterBean == null){
				return "参数为空！";
			}
			rpc_result = "";
			is_SingleOperation = true;
			if(rpc_parameterBean.getMethod_name().equals("GetParameterValues")){ 
				System.out.println("[startExcuteRPC] CPEProcess startExcuteRPC,GetParameterValues parameterName = " + rpc_parameterBean.getParam_name());
				
				GetParameterValues gpv_method = null;
		    	List<String> param_name_list = new ArrayList<String>();
		    	param_name_list.add(rpc_parameterBean.getParam_name());		    		
	    		gpv_method= new GetParameterValues(param_name_list);
	    		m_rpcMethodList.add(gpv_method);
			}else if(rpc_parameterBean.getMethod_name().equals("GetParameterNames")){  
				GetParameterNames gpn_method = null;
				if(rpc_parameterBean.getParam_name() != null){
					gpn_method = new GetParameterNames(rpc_parameterBean.getParam_name(),rpc_parameterBean.getNext_level());
				}else{
					gpn_method = new GetParameterNames();
				}
				m_rpcMethodList.add(gpn_method);
			}else if(rpc_parameterBean.getMethod_name().equals("GetParameterAttributes")){
				GetParameterAttributes gpa_method = new GetParameterAttributes();
				if(rpc_parameterBean.getParam_name() != null){
					gpa_method.addParameterName(rpc_parameterBean.getParam_name());
				}
				m_rpcMethodList.add(gpa_method);
			}else if(rpc_parameterBean.getMethod_name().equals("SetParameterValues")){  //TO-DO??参数形式待修改
				ParameterList pl = new ParameterList();
				ParameterValueStruct pvs = null;
                if(rpc_parameterBean.getParam_name().contains("{")){
					//TO-DO??
				}else{
					switch(rpc_parameterBean.getType()){
					case "string":pvs = new ParameterValueStructStr(rpc_parameterBean.getParam_name(),rpc_parameterBean.getValue_set());
					case "boolean":pvs = new ParameterValueStructBoolean(rpc_parameterBean.getParam_name(),Boolean.parseBoolean(rpc_parameterBean.getValue_set()));
					case "int":pvs = new ParameterValueStructInt(rpc_parameterBean.getParam_name(),Integer.parseInt(rpc_parameterBean.getValue_set()));
					case "unsignedInt":pvs = new ParameterValueStructUnsignedInt(rpc_parameterBean.getParam_name(),Long.parseLong(rpc_parameterBean.getValue_set()));
					case "dateTime":pvs = new ParameterValueStructDateTime(rpc_parameterBean.getParam_name(),rpc_parameterBean.getValue_set());
					case "object":pvs = new ParameterValueStructObject(rpc_parameterBean.getParam_name(),(Object)rpc_parameterBean.getValue_set());
					}
					pl.addParamValues(pvs);
				}
				SetParameterValues spv_method = new SetParameterValues(pl);
				m_rpcMethodList.add(spv_method);
			}else if(rpc_parameterBean.getMethod_name().equals("SetParameterAttributes")){ //TO-DO??
				SetParameterAttributes spa_method = new SetParameterAttributes();
				
				List<SetParameterAttributesStruct> attr_list = new ArrayList<SetParameterAttributesStruct>();
				SetParameterAttributesStruct spa1 = new SetParameterAttributesStruct();
				spa1.setName(rpc_parameterBean.getParam_name());
				spa1.setNotificationChange(rpc_parameterBean.isNotificationChange());
				spa1.setNotification(rpc_parameterBean.getNotification());
				spa1.setAccessListChange(rpc_parameterBean.isAccessListChange());
				attr_list.add(spa1);
				spa_method.setSetParamAttrList(attr_list);
				
				m_rpcMethodList.add(spa_method);
			}else if(rpc_parameterBean.getMethod_name().equals("AddObject")){ //TO-DO??参数形式待修改
				String objectName = "";  //??
				String commandKey = "";  //??
				AddObject addObject_method = new AddObject(objectName,commandKey);
				m_rpcMethodList.add(addObject_method);
			}else if(rpc_parameterBean.getMethod_name().equals("DeleteObject")){ //TO-DO??参数形式待修改
				String objectName = "";  //??
				String commandKey = "";  //??
				DeleteObject deleteObject_method = new DeleteObject(objectName,commandKey);
				m_rpcMethodList.add(deleteObject_method);
			}
		}						
		System.out.println("[startExcuteRPC] CPEProcess startExcuteRPC,get out lock!");

		//ACS主动发起连接
		int responseCode = 0;
		String cpe_url = this.devices.getM_ip();
		if(Resources.isNullOrEmpty(cpe_url)){
			this.m_rpcMethodList.clear();    //清空RPC方法列表
			return "设备URL为空！";
		}
		System.out.println("[startExcuteRPC] CPEProcess startExcuteRPC,isNAT = " + this.devices.getM_inNet());
	    if(!this.devices.getM_inNet()){    //不在NAT后，则使用tcp连接	    	
	    	try {
				responseCode = requestCpeTCPConnection(cpe_url);
		    	if(responseCode != 200){
					return "连接设备出错！";
				}
			} catch (Exception e) {
				this.m_rpcMethodList.clear();    //清空RPC方法列表
				System.out.println("[startExcuteRPC] CPEProcess startExcuteRPC,requestConn ERROR:" + e.getMessage() );
				return "连接设备失败！";
			}
	    }else{    //在NAT则udp连接
	    	try {
				requestCpeUDPConnection(cpe_url);
			} catch (Exception e) {
				this.m_rpcMethodList.clear();    //清空RPC方法列表
				System.out.println("[startExcuteRPC] CPEProcess startExcuteRPC,requestConn ERROR:" + e.getMessage() );
				return "连接设备出错！";
			}	    	
	    }		
		
		long tempTime = System.currentTimeMillis();
		while(System.currentTimeMillis() - tempTime < ms){
			if(!m_sessionLocked){
				if(!Resources.isNullOrEmpty(rpc_result)){
					return rpc_result;
				}
			}
		}
		
		System.out.println("[startExcuteRPC] CPEProcess startExcuteRPC,rpc_result = " + rpc_result);
		if(!m_sessionLocked){
			if(!Resources.isNullOrEmpty(rpc_result)){
				return rpc_result;
			}
		}else{
			rpc_result = "超时！";
			System.out.println("[startExcuteRPC] CPEProcess startExcuteRPC,timeout");
			sessionEnd();
			return rpc_result;
		}
		return rpc_result;
	}
	
	public synchronized TaskResultCode start2(List<ParamSetBean> paramSet_list, long ms) {
		synchronized(lock){
			if (m_sessionLocked) 
				return TaskResultCode.TASK_RESULT_DEVICE_BUSY;	
			
			if(paramSet_list == null){
				return TaskResultCode.TASK_RESULT_PARAMETER_NULL;
			}
			this.is_SingleOperation = false;  //是否为节点树的单个节点的操作
			this.m_setParam_status = -1;    //下发任务的结果，默认为-1
			this.m_hasGet_param = false;    //是否已获取参数
			this.m_taskResultCode = TaskResultCode.TASK_START_ERROR;
			this.cur_task = paramSet_list.get(0).getM_taskName();
			System.out.println("[start2] CPEProcess start2,current task = " + cur_task);
			ParameterList para_list = new ParameterList();  	
			for(ParamSetBean psb : paramSet_list){
				System.out.println(psb.getM_type() + "," + psb.getM_nodePath() + ":" + psb.getM_value());
				switch(psb.getM_type()){
				case "string":ParameterValueStructStr pvsStr = new ParameterValueStructStr(psb.getM_nodePath(),psb.getM_value());
			                  para_list.addParamValues(pvsStr);break;
				case "int":ParameterValueStructInt pvsInt = new ParameterValueStructInt(psb.getM_nodePath(),Integer.parseInt(psb.getM_value()));
                           para_list.addParamValues(pvsInt);break;
				case "unsignedInt":ParameterValueStructUnsignedInt pvsUnsignedInt = new ParameterValueStructUnsignedInt(psb.getM_nodePath(),Long.parseLong(psb.getM_value()));
                                   para_list.addParamValues(pvsUnsignedInt);break;
				case "boolean":ParameterValueStructBoolean pvsBoolean = new ParameterValueStructBoolean(psb.getM_nodePath(),Boolean.parseBoolean(psb.getM_value()));
                               para_list.addParamValues(pvsBoolean);break;
				case "dateTime":ParameterValueStructDateTime pvsDateTime = new ParameterValueStructDateTime(psb.getM_nodePath(),(psb.getM_value()));
                                para_list.addParamValues(pvsDateTime);break;
				case "object":ParameterValueStructObject pvsObject = new ParameterValueStructObject(psb.getM_nodePath(),(psb.getM_value()));
                              para_list.addParamValues(pvsObject);break;
				}	
			}
			 SetParameterValues spv_method = new SetParameterValues(para_list);
			 m_rpcMethodList.add(spv_method);   //添加set方法	 	
		}						
		System.out.println("[start2] CPEProcess start2,get out lock!");

		//ACS主动发起连接
		TaskResultCode request_result = requestCPEConnection();
		if(request_result != TaskResultCode.TASK_REQUEST_CPE_CONNECTION_OK){
			return request_result;
		}	
		
		//等待任务开始结果
		System.out.println("[start2] CPEProcess start,begin to wait task start2 result");
		long tempTime = System.currentTimeMillis();
		while(System.currentTimeMillis() - tempTime < ms){
			if(!m_sessionLocked){ 				
				if(m_setParam_status == 0 || this.m_setParam_status == 1){						
					return TaskResultCode.TASK_START_OK;
				}else if(m_setParam_status == 2){
					return this.m_taskResultCode;
				}
			}				
		}
		
		System.out.println("[start2] CPEProcess start,m_sessionLocked = " + m_sessionLocked + ",m_setParam_status = " + m_setParam_status);
		if(!m_sessionLocked){
			if(m_setParam_status == 0 || this.m_setParam_status == 1){						
				return TaskResultCode.TASK_START_OK;
			}else if(m_setParam_status == 2){
				return this.m_taskResultCode;
			}
		}else{
			if(this.m_setParam_status == -1){	
	        	System.out.println("[start2] CPEProcess start,timeout====");
	        	sessionEnd();  
				return TaskResultCode.TASK_START_TIMEOUT;
			}else if(this.isM_hasGet_param()){
				return TaskResultCode.TASK_START_OK;
			}
		}
		System.out.println("[start2] CPEProcess start,taskResultCode = " + m_taskResultCode.toString());
    	return this.m_taskResultCode;		 
	}	
	
	//GetParameterValue方法
	public synchronized TaskResultCode start3(List<ParamGetBean> paramGet_list, long ms) {
		synchronized(lock){
			if (m_sessionLocked) 
				return TaskResultCode.TASK_RESULT_DEVICE_BUSY;	
			
			if (paramGet_list == null) {
				return TaskResultCode.TASK_RESULT_PARAMETER_NULL;
			}
			this.is_SingleOperation = false;  //是否为节点树的单个节点的操作
			this.m_setParam_status = -1;    //下发任务的结果，默认为-1
			this.m_hasGet_param = false;    //是否已获取参数
			this.m_taskResultCode = TaskResultCode.TASK_START_RECORD_ERROR;
			this.cur_task = paramGet_list.get(0).getTaskName();
			System.out.println("[start3] CPEProcess start3,current task = " + cur_task);
			
			GetParameterValues gpv_method = null;
	    	List<String> param_name_list = new ArrayList<String>();
	    	for (ParamGetBean pg : paramGet_list) {
	    		param_name_list.add(pg.getM_nodePath());		
	    	}
	    	
    		gpv_method= new GetParameterValues(param_name_list);
			m_rpcMethodList.add(gpv_method);   //添加set方法	 	
		}						
		System.out.println("[start3] CPEProcess start3,get out lock!");

		//ACS主动发起连接
		TaskResultCode request_result = requestCPEConnection();
		if (request_result != TaskResultCode.TASK_REQUEST_CPE_CONNECTION_OK) {
			return request_result;
		}	
		
		//等待任务开始结果
		System.out.println("[start3] CPEProcess start,begin to wait task start3 result");
		long tempTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - tempTime < ms) {
			if (!m_sessionLocked) { 				
				if (m_setParam_status == 0 || this.m_setParam_status == 1) {						
					return TaskResultCode.TASK_START_RECORD_OK;
				} else if (m_setParam_status == 2){
					return this.m_taskResultCode;
				}
			}				
		}
		
		System.out.println("[start3] CPEProcess start,m_sessionLocked = " + m_sessionLocked + ",m_setParam_status = " + m_setParam_status);
		if (!m_sessionLocked) { 				
			if (m_setParam_status == 0 || this.m_setParam_status == 1) {						
				return TaskResultCode.TASK_START_RECORD_OK;
			} else if (m_setParam_status == 2){
				return this.m_taskResultCode;
			}
		} else {
			if (this.m_setParam_status == -1) {	
				System.out.println("[start3] CPEProcess start,timeout====");
	        	sessionEnd();  
				return TaskResultCode.TASK_START_RECORD_TIMEOUT;
			} else if (this.isM_hasGet_param()){
				return TaskResultCode.TASK_START_RECORD_OK;
			}
		}
        	
		System.out.println("[start3] CPEProcess start,taskResultCode = " + m_taskResultCode.toString());
    	return this.m_taskResultCode;		 
	}	
	
}
