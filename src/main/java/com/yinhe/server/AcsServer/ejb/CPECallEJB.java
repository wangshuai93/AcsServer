package com.yinhe.server.AcsServer.ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.yinhe.server.AcsServer.RPCMethodModel.GetParameterAttributesResponse;
import com.yinhe.server.AcsServer.RPCMethodModel.GetParameterNamesResponse;
import com.yinhe.server.AcsServer.RPCMethodModel.GetParameterValuesResponse;
import com.yinhe.server.AcsServer.RPCMethodModel.GetRPCMethodsResponse;
import com.yinhe.server.AcsServer.RPCMethodModel.Inform;
import com.yinhe.server.AcsServer.RPCMethodModel.InformResponse;
import com.yinhe.server.AcsServer.RPCMethodModel.SetParameterValuesResponse;
import com.yinhe.server.AcsServer.RPCMethodModel.TransferCompleteResponse;
import com.yinhe.server.AcsServer.data.AlarmRespository;
import com.yinhe.server.AcsServer.data.CPERespository;
import com.yinhe.server.AcsServer.data.CountResultRespository;
import com.yinhe.server.AcsServer.data.NodeTaskRespository;
import com.yinhe.server.AcsServer.data.ResultRespository;
import com.yinhe.server.AcsServer.data.ThresholdValueRespository;
import com.yinhe.server.AcsServer.enums.TaskResultCode;
import com.yinhe.server.AcsServer.model.Alarm;
import com.yinhe.server.AcsServer.model.CountResult;
import com.yinhe.server.AcsServer.model.NodeTask;
import com.yinhe.server.AcsServer.model.Result;
import com.yinhe.server.AcsServer.model.ThresholdValue;
import com.yinhe.server.AcsServer.struct.FaultStruct;
import com.yinhe.server.AcsServer.struct.ParameterAttributeStruct;
import com.yinhe.server.AcsServer.struct.ParameterInfoStruct;
import com.yinhe.server.AcsServer.struct.ParameterList;
import com.yinhe.server.AcsServer.struct.ParameterValueStruct;
import com.yinhe.server.AcsServer.util.Resources;

@Stateless
public class CPECallEJB {
	@Inject
	private InformParse informParse;
	@Inject
	private CPERespository m_cpeRespository;
	@Inject
	private Logger m_log;
	@Inject 
	private NodeTaskRespository m_nodeTaskRespository;
	private List<NodeTask> nodeTask_list;
	@Inject 
	private ThresholdValueRespository m_thresholdValueRespository;
	@Inject
	private AlarmRespository m_alarmRespository;
	@Inject 
	private ResultRespository m_resultRespository;
	@Inject 
	private CountResultRespository m_countResultRespository;

	private final int MY_MAX_ENVELOPES = 1;       //ACS端设置的可接收的封包个数，默认为1
	
	@SuppressWarnings("rawtypes")
	public RPCMethod call(CPEProcess cpeProcess,RPCMethod method) {
		Date now = new Date();   	//更新设备的最后上报时间
		if(cpeProcess.getDevices() != null){
			cpeProcess.getDevices().setM_lastContact(now);
			m_cpeRespository.updateEntity(cpeProcess.getDevices());
		}
		
		String name = method.getMethodName();
		if(name.equals("Inform")){
			m_log.info("[call] handle Inform!");
			try{
				informParse.parseInform(cpeProcess,(Inform)method);
			}catch(Exception ex){
				m_log.info("[call] Parse Error" + ex.getMessage());
			}			 		
			InformResponse imformResponse = new InformResponse(MY_MAX_ENVELOPES);
			return imformResponse;
		}else if(name.equals("GetRPCMethods")){
			m_log.info("[call] handle GetRPCMethods!");
			
			GetRPCMethodsResponse getRPCMethodsResponse = new GetRPCMethodsResponse();
			return getRPCMethodsResponse;
		}else if(name.equals("TransferComplete")){
			m_log.info("[call] handle TransferComplete!");
			
			TransferCompleteResponse transferCompleteResponse = new TransferCompleteResponse();
			return transferCompleteResponse;			
		}else if(name.equals("GetParameterNamesResponse")){
			m_log.info("[call] handle GetParameterNamesResponse!");
			
			List<ParameterInfoStruct> param_List= new ArrayList<ParameterInfoStruct>();
			param_List = ((GetParameterNamesResponse) method).getParameterList();
			//单个节点操作的回复
            if(cpeProcess.isIs_SingleOperation()){
            	m_log.info("[call] Single Node Operation!");
            	String single_result = "";
				for(ParameterInfoStruct pis : param_List){
					single_result += pis.toString() + "\r\n";
				}
				cpeProcess.setRpc_result(single_result);
			}
			return null;
		}else if(name.equals("GetParameterAttributesResponse")){
			m_log.info("[call] handle GetParameterAttributesResponse!");

			List<ParameterAttributeStruct> param_list = new ArrayList<ParameterAttributeStruct>();
			param_list = ((GetParameterAttributesResponse) method).getParamAttrList();
			//单个节点操作的回复
            if(cpeProcess.isIs_SingleOperation()){
            	m_log.info("[call] Single Node Operation!");
            	String single_result = "";
            	m_log.info("[call] param_list.size = " + param_list.size());
            	for(ParameterAttributeStruct pas : param_list){
					single_result += pas.toString() + "\r\n";
				}
				cpeProcess.setRpc_result(single_result);
			}
		    return null;
		}else if(name.equals("GetParameterValuesResponse")){
			m_log.info("[call] handle GetParameterValuesResponse!");						
			cpeProcess.setM_setParam_status(0);
			cpeProcess.setM_hasGet_param(true);	
			
			ParameterList param_list = new ParameterList();    
			param_list = ((GetParameterValuesResponse) method).getParameterList();
			List<ParameterValueStruct> pvs_list = param_list.getParameterValueStructs();			
			//单个节点操作的回复
            if(cpeProcess.isIs_SingleOperation()){
            	m_log.info("[call] Single Node Operation!");
            	String single_result = "";
				for(ParameterValueStruct pvs : pvs_list){
					single_result += pvs.toString() + "  ";
				}
				cpeProcess.setRpc_result(single_result);
			}else{
				m_log.info("[call] Not Single Node Operation!");
				try{
					GetParameterResponse(pvs_list, cpeProcess);  //将数据插入数据库
	            }catch(Exception ex){
	            	ex.printStackTrace();
	            	m_log.info("[call] Exception : " + ex.toString());
	            	return null;
	            }
			}
			return null;			
		}else if(name.equals("SetParameterValuesResponse")){					
			m_log.info("[call] handle SetParameterValuesResponse!");
			if(!cpeProcess.isM_sessionLocked()){         //session已关闭则不处理
				return null;
			}
			cpeProcess.setM_setParam_status(((SetParameterValuesResponse) method).getStatus());  //设置参数的结果（即任务下发的结果  ）
			m_log.info("[call] handle SetParameterValuesResponse,status = " + ((SetParameterValuesResponse) method).getStatus());
			//单个节点操作的回复
            if(cpeProcess.isIs_SingleOperation()){
            	m_log.info("[call] Single Node Operation!");
            	String single_result = "设置参数结果：" + ((SetParameterValuesResponse) method).getStatus();
				cpeProcess.setRpc_result(single_result);
			}
			return null;		
		}else if(name.equals("SetParameterAttributesResponse")){
			m_log.info("[call] handle SetParameterAttributesResponse!");
			
			//单个节点操作的回复
            if(cpeProcess.isIs_SingleOperation()){
            	m_log.info("[call] Single Node Operation!");
            	String single_result = "设置参数属性成功！：" ;
				cpeProcess.setRpc_result(single_result);
			}
			return null;		
		}else if(name.equals("Fault")){           //结果返回给start 
			m_log.info("[call] handle Fault!");
			cpeProcess.setM_setParam_status(2);
			
			FaultStruct faultStruct = method.getFaultStruct();
			 if(cpeProcess.isIs_SingleOperation()){
				 m_log.info("[call] Single Node Operation!");
					cpeProcess.setRpc_result(faultStruct.toString());	
			}else{
				 FaultHandle(faultStruct,cpeProcess);
			}
			return null;
		}else if(name.equals("RebootResponse")){
			m_log.info("[call] handle RebootResponse!");
			cpeProcess.setM_setParam_status(0);
			return null;
		}else if(name.equals("DownloadResponse")){
			m_log.info("[call] handle DownloadResponse!");
			cpeProcess.setM_setParam_status(0);
			return null;
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public void GetParameterResponse(List<ParameterValueStruct> pvs_list,CPEProcess cpeProcess){
		m_log.info("[GetParameterResponse] get into GetParameterResponse!");
		if(pvs_list.size() < 0){
			return;
		}
		Date report_time = new Date();
		m_log.info("[GetParameterResponse] pvs_list.size() = " + pvs_list.size());
		CountResult countResult = new CountResult();
		countResult.setM_device(cpeProcess.getDevices());
		countResult.setM_reportTime(report_time);
		m_countResultRespository.addEntity(countResult);
		
		for(ParameterValueStruct pvs : pvs_list){
			if(pvs == null){
				continue;
			}
			m_log.info("[GetParameterResponse] pvs.getName() = " + pvs.getName());
			NodeTask node_task = m_nodeTaskRespository.findNodeTaskByParamPath(pvs.getName());
			if(node_task != null){
				m_log.info("[GetParameterResponse] node_task != null");
				Result result = new Result();
				result.setM_value(pvs.getValue().toString());
				result.setM_nodeTask(node_task);
				result.setM_countResult(countResult);
				m_resultRespository.addEntity(result);
				
				//是否告警
				ThresholdValue tv = m_thresholdValueRespository.findThresholdValueByNodeId(node_task.getM_node().getM_id());
				if(tv != null){
					m_log.info("[GetParameterResponse] tv != null");
					alaramSet(pvs,tv,node_task,cpeProcess,report_time);
				}
			}
		}
	}

	public void FaultHandle(FaultStruct faultStruct,CPEProcess cpeProcess){
		switch(faultStruct.getFaultCode()){
		   case 9000:cpeProcess.setM_taskResultCode(TaskResultCode.TASK_RESULTCODE_9000);break;
		   case 9001:cpeProcess.setM_taskResultCode(TaskResultCode.TASK_RESULTCODE_9001);break;
		   case 9002:cpeProcess.setM_taskResultCode(TaskResultCode.TASK_RESULTCODE_9002);break;
		   case 9003:cpeProcess.setM_taskResultCode(TaskResultCode.TASK_RESULTCODE_9003);break;
		   case 9004:cpeProcess.setM_taskResultCode(TaskResultCode.TASK_RESULTCODE_9004);break;
		   case 9005:cpeProcess.setM_taskResultCode(TaskResultCode.TASK_RESULTCODE_9005);break;
		   case 9006:cpeProcess.setM_taskResultCode(TaskResultCode.TASK_RESULTCODE_9006);break;
		   case 9007:cpeProcess.setM_taskResultCode(TaskResultCode.TASK_RESULTCODE_9007);break;
		   case 9008:cpeProcess.setM_taskResultCode(TaskResultCode.TASK_RESULTCODE_9008);break;
		   case 9009:cpeProcess.setM_taskResultCode(TaskResultCode.TASK_RESULTCODE_9009);break;
		   case 9010:cpeProcess.setM_taskResultCode(TaskResultCode.TASK_RESULTCODE_9010);break;
		   case 9011:cpeProcess.setM_taskResultCode(TaskResultCode.TASK_RESULTCODE_9011);break;
		   case 9012:cpeProcess.setM_taskResultCode(TaskResultCode.TASK_RESULTCODE_9012);break;
		   case 9013:cpeProcess.setM_taskResultCode(TaskResultCode.TASK_RESULTCODE_9013);break;
		}
		m_log.info("[call] FaultCode = " + faultStruct.getFaultCode() + ",FaultString = " + faultStruct.getFaultString());
	}
	
	
	public NodeTask getNodeTaskbyParamName(String name){
		if(nodeTask_list != null){
			for(NodeTask nt : nodeTask_list){
				m_log.info("[getNodeTaskbyParamName] nodeName = " + nt.getM_node().getAbbr_name());
				if(nt.getM_node().getAbbr_name().equals(name)){
					return nt;
				}
			}
		}else{
			m_log.info("[getNodeTaskbyParamName] nodeTask_list == null!");
		}
		return null;
	}
	
	/**
	 * 生成告警记录
	 * @param pvs
	 * @param tv
	 * @param nodeTask
	 */
	@SuppressWarnings("rawtypes")
	public void alaramSet(ParameterValueStruct pvs ,ThresholdValue tv,NodeTask nodeTask,CPEProcess cpeProcess,Date reportTime){
		m_log.info("[alaramSet] get into alaramSet!");
		String value_type = nodeTask.getM_node().getValue_type();
		if(Resources.isNullOrEmpty(value_type)){
			m_log.info("[alaramSet] value_type is null!");
			return;
		}
		m_log.info("[alaramSet] valueType = " + pvs.getValueType());
		switch(pvs.getValueType()){
		case "string": 
			m_log.info("[alaramSet] case string!");
			if(value_type.equals("basic")){	
				//do-nothing
			}else if(value_type.equals("enum")){
				m_log.info("[alaramSet] enum value = " + tv.getValue());
				String[] data_enum = tv.getValue().split("\\,");
				boolean flag = false;
				for(String str : data_enum){
					if(((String)pvs.getValue()).equals(str)){
						flag = true;
						break;
					}
				}
				if(!flag){
					Alarm alarm = new Alarm();
					alarm.setM_alarmTime(reportTime);
					alarm.setM_devices(cpeProcess.getDevices());
					alarm.setM_description("不在枚举范围内！");
					alarm.setM_paramPath(nodeTask.getM_node().getNode_path());
					alarm.setM_taskName(nodeTask.getM_task().getM_name());
					m_alarmRespository.addEntity(alarm);
				}
			}	
			break; 		
		case "int": 
			m_log.info("[alaramSet] case int!");
			if(value_type.equals("basic")){	
				int value = (int)pvs.getValue();
				if(value >= Integer.parseInt(tv.getMin_value()) && value <= Integer.parseInt(tv.getMax_value())){
					break;
				}else{
					Alarm alarm = new Alarm();
					alarm.setM_alarmTime(reportTime);
					alarm.setM_devices(cpeProcess.getDevices());
					alarm.setM_description("不在阀值范围内！");
					alarm.setM_paramPath(nodeTask.getM_node().getNode_path());
					alarm.setM_taskName(nodeTask.getM_task().getM_name());
					m_alarmRespository.addEntity(alarm);
				}
			}else if(value_type.equals("enum")){
				String[] data_enum = tv.getValue().split("\\,");
				boolean flag = false;
				for(String str : data_enum){
					if((int)pvs.getValue() == Integer.parseInt(str)){
						flag = true;
						break;
					}
				}
				if(!flag){
					Alarm alarm = new Alarm();
					alarm.setM_alarmTime(reportTime);
					alarm.setM_devices(cpeProcess.getDevices());
					alarm.setM_description("不在枚举范围内！");
					alarm.setM_paramPath(nodeTask.getM_node().getNode_path());
					alarm.setM_taskName(nodeTask.getM_task().getM_name());
					m_alarmRespository.addEntity(alarm);
				}
			}	
			break;            
		case "unsignedInt":
			m_log.info("[alaramSet] case unsignedInt!");
			if(value_type.equals("basic")){	
				Long value = (Long)pvs.getValue();
				if(value >= Long.parseLong(tv.getMin_value()) && value <= Long.parseLong(tv.getMax_value())){
					m_log.info("[alaramSet] 在阀值范围内！");
					break;
				}else{
					m_log.info("[alaramSet] 不在阀值范围内！");
					Alarm alarm = new Alarm();
					alarm.setM_alarmTime(reportTime);
					alarm.setM_devices(cpeProcess.getDevices());
					alarm.setM_description("不在阀值范围内！");
					alarm.setM_paramPath(nodeTask.getM_node().getNode_path());
					alarm.setM_taskName(nodeTask.getM_task().getM_name());
					m_alarmRespository.addEntity(alarm);
				}
			}else if(value_type.equals("enum")){
				String[] data_enum = tv.getValue().split("\\,");
				boolean flag = false;
				for(String str : data_enum){
					if((Long)pvs.getValue() == Long.parseLong(str)){
						flag = true;
						break;
					}
				}
				if(!flag){
					Alarm alarm = new Alarm();
					alarm.setM_alarmTime(reportTime);
					alarm.setM_devices(cpeProcess.getDevices());
					alarm.setM_description("不在枚举范围内！");
					alarm.setM_paramPath(nodeTask.getM_node().getNode_path());
					alarm.setM_taskName(nodeTask.getM_task().getM_name());
					m_alarmRespository.addEntity(alarm);
				}
			}	
			break; 		
		case "boolean":break;
		case "object":break;
		case "any":break;
		case "dateTime":break;
		}				
	}
}
