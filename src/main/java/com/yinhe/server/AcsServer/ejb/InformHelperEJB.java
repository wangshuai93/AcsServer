package com.yinhe.server.AcsServer.ejb;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.yinhe.server.AcsServer.data.AlarmRespository;
import com.yinhe.server.AcsServer.data.CountResultRespository;
import com.yinhe.server.AcsServer.data.NodeTaskRespository;
import com.yinhe.server.AcsServer.data.ResultRespository;
import com.yinhe.server.AcsServer.data.ThresholdValueRespository;
import com.yinhe.server.AcsServer.model.Alarm;
import com.yinhe.server.AcsServer.model.CountResult;
import com.yinhe.server.AcsServer.model.NodeTask;
import com.yinhe.server.AcsServer.model.Result;
import com.yinhe.server.AcsServer.model.ThresholdValue;
import com.yinhe.server.AcsServer.struct.ParameterValueStruct;
import com.yinhe.server.AcsServer.util.Resources;

@Stateless
public class InformHelperEJB {
	@Inject 
	private Logger m_log;
	@Inject 
	private ThresholdValueRespository m_thresholdValueRespository;
	@Inject
	private AlarmRespository m_alarmRespository;
	@Inject 
	private NodeTaskRespository m_nodeTaskRespository;
	@Inject 
	private CountResultRespository m_countResultRespository;
	@Inject 
	private ResultRespository m_resultRespository;

	
	@SuppressWarnings("rawtypes")
	public void periodicResult(List<ParameterValueStruct> pvs_list,CPEProcess cpeProcess){
		m_log.info("[periodicResult] get into periodicResult!");
		if(pvs_list.size() < 0){
			return;
		}
		Date report_time = new Date();
		m_log.info("[periodicResult] pvs_list.size() = " + pvs_list.size());
		CountResult countResult = new CountResult();
		countResult.setM_device(cpeProcess.getDevices());
		countResult.setM_reportTime(report_time);
		m_countResultRespository.addEntity(countResult);
		
		for(ParameterValueStruct pvs : pvs_list){
			if(pvs != null){
				m_log.info("[periodicResult] pvs.getName() = " + pvs.getName());
				NodeTask node_task = m_nodeTaskRespository.findNodeTaskByParamPath(pvs.getName());
				if(node_task != null){
					m_log.info("[periodicResult] node_task != null");
					Result result = new Result();
					result.setM_value(pvs.getValue().toString());
					result.setM_nodeTask(node_task);
					result.setM_countResult(countResult);
					m_resultRespository.addEntity(result);
					
					//是否告警
					ThresholdValue tv = m_thresholdValueRespository.findThresholdValueByNodeId(node_task.getM_node().getM_id());
					if(tv != null){
						m_log.info("[periodicResult] tv != null");
						alaramSet(pvs,tv,node_task,cpeProcess,report_time);
					}
				}
			}
		}
	}
	
    public NodeTask getNodeTaskbyParamName(String name,List<NodeTask> nodeTask_list){
    	if(nodeTask_list != null){
    		for(NodeTask nt : nodeTask_list){
  				if(nt.getM_node().getAbbr_name().equals(name)){
  					return nt;
  				}
  			}
    	}
  		return null;
  	 }
  	  
  	@SuppressWarnings("rawtypes")
	public void alaramSet(ParameterValueStruct pvs ,ThresholdValue tv,NodeTask nodeTask,CPEProcess cpeProcess,Date reportTime){
		String value_type = nodeTask.getM_node().getValue_type();
		if(Resources.isNullOrEmpty(value_type)){
			m_log.info("[alaramSet] value_type is null");
			return;
		}
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
