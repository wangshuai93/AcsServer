package com.yinhe.server.AcsServer.ejb;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.yinhe.server.AcsServer.RPCMethodModel.Inform;
import com.yinhe.server.AcsServer.data.CPERespository;
import com.yinhe.server.AcsServer.data.DeviceTaskRespository;
import com.yinhe.server.AcsServer.data.ParamGetRespository;
import com.yinhe.server.AcsServer.data.TaskRespository;
import com.yinhe.server.AcsServer.model.Devices;
import com.yinhe.server.AcsServer.model.DeviceTask;
import com.yinhe.server.AcsServer.model.ParamGet;
import com.yinhe.server.AcsServer.model.Task;
import com.yinhe.server.AcsServer.struct.DeviceId;
import com.yinhe.server.AcsServer.struct.ParameterList;
import com.yinhe.server.AcsServer.struct.ParameterValueStruct;
import com.yinhe.server.AcsServer.util.Resources;

@Stateless
public class InformHandleEJB {
	@Inject 
	private CPERespository m_cpeRespository;
	@Inject
	private TaskRespository m_taskRespository;
	@Inject
	private DeviceTaskRespository m_deviceTaskRespository;
	@Inject 
	private Logger m_log;
	@Inject
	private CPEManager m_cpeManager;
	@Inject 
	private ParamGetRespository m_paramGetRespository;
	@Inject
	private InformHelperEJB m_informHelperEJB;
	
    /**     
     * "0 BOOTSTRAP" 表明会话发起原因是CPE首次安装或ACS的URL发生变化。 
     */
    @SuppressWarnings("rawtypes")
	public void bootStrapEvent(CPEProcess cpeProcess,Inform inform){
    	m_log.info("[bootStrapEvent] get into bootStrapEvent!");
    	Devices device = null;
    	try{
    		DeviceId device_id = new DeviceId();
        	device_id = inform.getDeviceId();
        	if(device_id == null){
        		return;
        	}   	
        	ParameterList param_list = new ParameterList();
        	param_list = inform.getParameterList();
            List<ParameterValueStruct> pvs = param_list.getParameterValueStructs();
            String sn = device_id.getSerialNubmer();
            if(Resources.isNull(sn)){
            	return;
            }
            device = m_cpeRespository.findDeviceBySerialNumber(sn);
        	Date now  = new Date();
        	if(device == null){
        		device = new Devices();
            	device.setM_serialNumber(sn); 
            	device.setM_inNet(false);   //默认false
            	device.setM_status("offline");
            	//等待上报           	
            	device.setM_positionName("未知");
            	device.setM_positionX(0);
            	device.setM_positionY(0);           
            	updateDevices(device_id,pvs,device,now,cpeProcess);
            	m_cpeRespository.addEntity(device);
            	
            	List<Task> taskBean = new ArrayList<Task>();
        		taskBean = m_taskRespository.findAllOrderedByIdDesc();
        		if(null != taskBean && 0 != taskBean.size()){
        			for (Task temp : taskBean) {
        				DeviceTask deviceTask = new DeviceTask();
        				Long period_time = new Long(1000000L);
        				deviceTask.setM_device(device);
        				deviceTask.setM_is_period(true);
        				deviceTask.setM_period_time(period_time);
        				deviceTask.setM_status("stopped");
        				deviceTask.setM_task(temp);
        				try {
        					m_deviceTaskRespository.addEntity(deviceTask);
        				} catch (Exception e) {
        					e.printStackTrace();
        				}
        			}
        		}
            	
            	try{
                	m_cpeManager.addCPEProcessToCache(cpeProcess);   //同时将此设备更新到缓存           	 
            	}catch(Exception e){
            		m_log.info("[bootStrapEvent] 更新缓存异常" + e.getMessage());
            	}
        	}else{  //device已存在，更新
        		updateDevices(device_id,pvs,device,now,cpeProcess);
        		m_cpeRespository.updateEntity(device);
        	}
        	cpeProcess.addBootEventRPCMethod();  //设置参数，判断是否在NAT后，以及主动上传结果
    	}catch(Exception ex){
    		System.out.println(ex.getMessage());
    		m_log.info("[bootStrapEvent] handle 0 bootStrap inform exception:" + ex.toString());
    	}
    	
    }
    
    /**
     * "1 BOOT" 表明会话发起原因是CPE加电或重置，包括系统首次启动，以及因任何原因而引起的重启，包括使用Reboot方法。
     * @param inform
     */
	@SuppressWarnings("rawtypes")
	public void bootEvent(CPEProcess cpeProcess,Inform inform){
		m_log.info("[bootEvent] get into bootEvent!");
    	Devices device = null;
    	try{
    		DeviceId device_id = new DeviceId();
        	device_id = inform.getDeviceId();
        	if(device_id == null){
        		return;
        	}   	
        	ParameterList param_list = new ParameterList();
        	param_list = inform.getParameterList();
            List<ParameterValueStruct> pvs = param_list.getParameterValueStructs();
            String sn = device_id.getSerialNubmer();
            m_log.info("[bootEvent] serialNumber = " + sn);
            if(Resources.isNull(sn)){
            	//test
            	//sn = "123456789";
            	return;
            }
            device = m_cpeRespository.findDeviceBySerialNumber(sn);
        	Date now  = new Date();
        	if(device == null){
        		device = new Devices();
            	device.setM_serialNumber(sn);
            	device.setM_inNet(false);   //默认false
            	device.setM_status("offline");
            	//等待上报
            	device.setM_positionName("未知");
            	device.setM_positionX(0);
            	device.setM_positionY(0);     
            	
            	updateDevices(device_id,pvs,device,now,cpeProcess); 
            	m_cpeRespository.addEntity(device);
            	List<Task> taskBean = new ArrayList<Task>();
        		taskBean = m_taskRespository.findAllOrderedByIdDesc();
        		if(null != taskBean && 0 != taskBean.size()){
        			for (Task temp : taskBean) {
        				DeviceTask deviceTask = new DeviceTask();
        				Long period_time = new Long(1000000L);
        				deviceTask.setM_device(device);
        				deviceTask.setM_is_period(true);
        				deviceTask.setM_period_time(period_time);
        				deviceTask.setM_status("stopped");
        				deviceTask.setM_task(temp);
        				try {
        					m_deviceTaskRespository.addEntity(deviceTask);
        				} catch (Exception e) {
        					e.printStackTrace();
        				}
        			}
        		}
        		
            	try{
                	m_cpeManager.addCPEProcessToCache(cpeProcess);              	 
            	}catch(Exception e){
            		m_log.info("[bootEvent] 添加新设备至缓存异常" + e.getMessage());
            	}
        	}else{  //device已存在，更新
        		updateDevices(device_id,pvs,device,now,cpeProcess);
        		m_cpeRespository.updateEntity(device);
        	}
        	cpeProcess.addBootEventRPCMethod();     //设置参数，判断是否在NAT后，以及主动上传结果
    	}catch(Exception ex){
    		m_log.info("[bootEvent] handle 1 boot inform exception!" );
    		ex.printStackTrace();
    	}
    	
    }
    
    /**
     * "2 PERIODIC" 表明会话发起原因是定期的Inform引起。
     * @param inform
     */
    @SuppressWarnings("rawtypes")
	public void periodicEvent(CPEProcess cpeProcess,Inform inform){
    	m_log.info("[periodicEvent] get into periodicEvent!");
    	//更新lastContact
    	DeviceId device_id = new DeviceId();
    	device_id = inform.getDeviceId();
    	String sn = device_id.getSerialNubmer();
    	if(Resources.isNullOrEmpty(sn)){
    		return;
    	}
    	Devices device = m_cpeRespository.findDeviceBySerialNumber(sn);
    	if(device != null){
    		m_log.info("[periodicEvent] device != null");
    		Date now  = new Date();
        	device.setM_lastContact(now);
        	m_cpeRespository.updateEntity(device);
        	cpeProcess.getDevices().setM_lastContact(now);   //更新缓存
        	
        	//添加Tuner结果
        	ParameterList param_list = new ParameterList();    
			param_list = inform.getParameterList();
        	List<ParameterValueStruct> pvs_list = param_list.getParameterValueStructs();
        	if(pvs_list != null){
        		m_log.info("[periodicEvent] pvs_list != null");
        		m_informHelperEJB.periodicResult(pvs_list, cpeProcess);
        	}
    	}   	
   }
    
    /**
     * "3 SCHEDULED" 表明会话发起原因是调用了ScheduleInform方法。
     * @param inform
     */
    public void scheduleEvent(CPEProcess cpeProcess,Inform inform){
    	
    }
    
    
    /**
     * "4 VALUE CHANGE" 表明会话发起原因是一个或多个参数值的变化。该参数值包括在Inform方法的调用中。例如CPE分配了新的IP地址。
     * @param inform
     */
    @SuppressWarnings("rawtypes")
	public void valueChangeEvent(CPEProcess cpeProcess,Inform inform){
    	m_log.info("[valueChangeEvent] get into valueChangeEvent!");
    	try{
        	DeviceId device_id = new DeviceId();
        	device_id = inform.getDeviceId();
        	ParameterList param_list = new ParameterList();
        	param_list = inform.getParameterList();
            List<ParameterValueStruct> pvs = param_list.getParameterValueStructs();
            Devices device = null;
            String sn = device_id.getSerialNubmer();
            if(Resources.isNull(sn)){
            	return;
            }
            device = m_cpeRespository.findDeviceBySerialNumber(sn);
        	if(device != null){
        		String connectionRequestURL = "";
    			boolean nat_Detected = false;
    			String udpConnectionRequestAddress = "";
        		for(ParameterValueStruct p:pvs){
        			if(p == null){
    					continue;
    				}
        			switch(p.getName()){
        			case "Device.ManagementServer.ConnectionRequestURL":connectionRequestURL=(String)p.getValue();break;
        			case "Device.ManagementServer.NATDetected":nat_Detected=(Boolean)p.getValue();break;
        			case "Device.ManagementServer.UDPConnectionRequestAddress":udpConnectionRequestAddress=(String)p.getValue();break;
        			} 			
        		}
        		m_log.info("[valueChangeEvent] connectionRequestURL's length = " + connectionRequestURL.length());
        		m_log.info("[valueChangeEvent] udpConnectionRequestAddress's length = " + udpConnectionRequestAddress.length());
        		device.setM_inNet(nat_Detected);
        		if(nat_Detected){
        			device.setM_ip(udpConnectionRequestAddress);
        		}else{
        			device.setM_ip(connectionRequestURL);
        		}
        		m_log.info("[valueChangeEvent] isNAT = " + nat_Detected);
        		m_log.info("[valueChangeEvent] device's ip == " + device.getM_ip());
        		m_cpeRespository.updateEntity(device);
        		cpeProcess.setDevices(device);
        	}      	
    	}catch(Exception e){
    		e.getMessage();
    	}    	
    }
    
    /**
     * "5 KICKED" 表明会话发起原因是为了web标识管理（见附录D），及在本会话中将会调用Kicked方法（见A.4.2.1节）。
     * @param inform
     */
    public void kickedEvent(CPEProcess cpeProcess,Inform inform){
    	
    }
    
    /**
     * "6 CONNECTION REQUEST" 表明会话发起原因是3.2节中定义的源自服务器的Connection Request
     * @param inform
     */
    public void connectionRequestEvent(CPEProcess cpeProcess,Inform inform){
    	
    }
    
    /**
     * "7 TRANSFER COMPLETE" 表明会话的发起是为了表明以前请求的下载或上载（不管是否成功）已经结束，在此会话中将要调用一次或多次TransferComplete方法。
     * @param inform
     */
    public void transferCompleteEvent(CPEProcess cpeProcess,Inform inform){
    	
    }
    
    /**
     * "8 DIAGNOSTICS COMPLETE" 当完成由ACS发起的诊断测试结束后，重新与ACS建立连接时使用。如DSL环路诊断（见附录B）。
     * @param inform
     */
    public void diagnosticsCompleteEvent(CPEProcess cpeProcess,String commandKey){
    	m_log.info("[diagnosticsCompleteEvent] commandKey = " + commandKey);
    	Date now = new Date();
    	cpeProcess.getDevices().setM_lastContact(now);
    	cpeProcess.setM_hasGet_param(true);	
    	
    	List<ParamGet> paramGet_list = new ArrayList<ParamGet>();
    	m_log.info("[diagnosticsCompleteEvent] current task = " + cpeProcess.getCur_task());
    	//Task task = m_taskRespository.findByType(cpeProcess.getCur_task());
    	Task task = m_taskRespository.findByType(commandKey);
    	if(task == null){
    		paramGet_list = null;
    	}else{
    		paramGet_list = m_paramGetRespository.findNodesByTask(task);
    	}
    	cpeProcess.addGetParamRPCMethod(paramGet_list);
    }
    
    /**
     *	"M "<method name> 如果这是另一方法的结果，值“M”后紧接着一个空格和方法的名称。例如： “M Reboot” “X “<OUI>
	 * 厂商自定义事件。在“X”后的OUI和空格是组织唯一标识符，表示为6位十六进制值，均使用大写，并包括任何前置零。
	 * 该值必须是在[9]中定义的有效OUI。对<event>的值与解释是厂商自定义的。例如：“X 00D09E MyEvent”
     */
    public void selfDefineEvent(CPEProcess cpeProcess,Inform inform){
    	
    }
    
   @SuppressWarnings("rawtypes")
   public Devices updateDevices(DeviceId device_id,List<ParameterValueStruct> pvs,Devices device,Date now,CPEProcess cpeProcess){
	   m_log.info("[updateDevices] get into updateDevices!");
	   if(device_id != null){
		   device.setM_manuFacturer(device_id.getManufacturer());
	   	   device.setM_oui(device_id.getOui());
	       device.setM_productClass(device_id.getProductClass()); 
	   }
	   if(pvs != null){
		   for(ParameterValueStruct ps:pvs){
			   if(ps == null){
					continue;
				}
			   switch(ps.getName()){         			
			   case ParameterList.ConnectionRequestURL: device.setM_ip((String)ps.getValue());break;
   			   case ParameterList.ConnectionRequestUsername: device.setM_connectRequestUsername((String)ps.getValue());break;
   			   case ParameterList.ConnectionRequestPassword: device.setM_connectRequestPwd((String)ps.getValue());break;            				            			
   			   case ParameterList.HardwareVersion:device.setM_hardwareVersion((String)ps.getValue());break;
   			   case ParameterList.SoftwareVersion:device.setM_softwareVersion((String)ps.getValue());break;
   			   case ParameterList.CurrentStbName:device.setM_deviceName((String)ps.getValue());break;
   			   case ParameterList.STBMacAddress:
   				   if(!Resources.isNullOrEmpty((String)ps.getValue())){
   					   device.setM_macAddress((String)ps.getValue());
   				   }else{
   					   device.setM_macAddress("000000000000");
   				   }
   				   break;
   			   case ParameterList.CMMacAddress:
   				   if(!Resources.isNullOrEmpty((String)ps.getValue())){
   					   device.setM_cmmacAddress((String)ps.getValue());
   				   }else{
   					   device.setM_cmmacAddress("000000000000");
   				   }
   				   break;
   			   case ParameterList.DecoderManufacturer:device.setM_decoderManufacturer((String)ps.getValue());break;
   			   case ParameterList.DecoderModel:device.setM_decoderModel((String)ps.getValue());break;
   			   case ParameterList.RAMSize:device.setM_RAMSize((Long)ps.getValue());break;
   			   case ParameterList.FlashSize:device.setM_flashSize((Long)ps.getValue());break;
   			
   			   case ParameterList.RAMRest:device.setM_RAMRest((Long)ps.getValue());break;
   			   case ParameterList.FlashRest:device.setM_flashRest((Long)ps.getValue());break;
   			   case ParameterList.IDLE:device.setM_IDLE((Long)ps.getValue());break;
   			   case ParameterList.SystemRunTime:device.setM_systemRunTime((Long)ps.getValue());break;
   			   case ParameterList.RAMUsed:device.setM_RAMUsed((Long)ps.getValue());break;
   			   case ParameterList.CPULoad:device.setM_CPULoad((Long)ps.getValue());break;
   			   }  
			  }
		   }  
		   device.setM_lastContact(now);
		   
		   if(cpeProcess != null){
			   cpeProcess.setDevices(device);
		   }
		   return device;
	  }
}
