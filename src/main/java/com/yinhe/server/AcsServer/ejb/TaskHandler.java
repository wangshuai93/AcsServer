package com.yinhe.server.AcsServer.ejb;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.inject.Inject;

import com.yinhe.server.AcsServer.backbean.CPEBean;
import com.yinhe.server.AcsServer.backbean.DeviceTaskDetailBean;
import com.yinhe.server.AcsServer.backbean.NodeModelDetailBean;
import com.yinhe.server.AcsServer.backbean.ParamGetBean;
import com.yinhe.server.AcsServer.backbean.ParamSetBean;
import com.yinhe.server.AcsServer.backbean.RPCParameterBean;
import com.yinhe.server.AcsServer.backbean.TaskParameter;
import com.yinhe.server.AcsServer.backbean.UpgradeParameter;
import com.yinhe.server.AcsServer.data.DeviceTaskRespository;
import com.yinhe.server.AcsServer.data.ParamSetRespository;
import com.yinhe.server.AcsServer.data.TaskRespository;
import com.yinhe.server.AcsServer.enums.TaskResultCode;
import com.yinhe.server.AcsServer.enums.TaskStatus;
import com.yinhe.server.AcsServer.model.DeviceTask;
import com.yinhe.server.AcsServer.model.ParamSet;
import com.yinhe.server.AcsServer.model.Task;
import com.yinhe.server.AcsServer.util.Resources;

@Stateful
public class TaskHandler {
	@Inject
	private CPEManager m_cpeManager;
	@Inject
	private CPEBean m_cpeBean;
	@Inject
	private Logger m_log;
	@Inject
	private DeviceTaskRespository m_deviceTaskRespository;
	@Inject 
	private ParamSetRespository m_paramSetRespository;
	@Inject 
	private TaskRespository m_taskRespository;
	
	public TaskStatus getStatus(CPEBean device, String type) {
		return m_cpeManager.getStatus(device, type);
	}
	
	public void changeTaskStatus(CPEBean device, String type){
		m_cpeManager.changeTaskStatus(device, type);
	}
	
	public void changeTaskStatusToRunning(String serialNumber, Long task_id){
		if (!Resources.isNullOrEmpty(serialNumber)) {
			DeviceTask deviceTask = m_deviceTaskRespository.findDeviceTaskBySerialNumberAndTaskId(serialNumber, task_id);
			if(null != deviceTask){
				m_log.info("[changeTaskStatusToRunning] status before = " + deviceTask.getM_status());
				if(deviceTask.getM_status().equals("running")){
					return;
				} else {
					deviceTask.setM_status("running");
					m_deviceTaskRespository.updateEntity(deviceTask);
				}
				m_log.info("[changeTaskStatusToRunning] status after = " + deviceTask.getM_status());
			}
		} else {
			m_log.info("[changeTaskStatusToRunning] device = null!");
		}
	}
	
	/*
	 * return message string, sync
	 */
	public TaskResultCode start(CPEBean device, String type, TaskParameter param, long ms) {
		m_log.info("[start] taskHandler start!");
		return m_cpeManager.start(device, type, param, ms);
	}
	
	public TaskResultCode start2(CPEBean device, List<ParamSetBean> paramSet_list, long ms){
		m_log.info("[start2] taskHandler start!");
		return m_cpeManager.start2(device,paramSet_list, ms);
	}
	
	public TaskResultCode startRecordTask (CPEBean device, List<ParamGetBean> paramGet_list, long ms) {
		return m_cpeManager.startRecordTask(device,paramGet_list, ms);
	}
	

	public TaskResultCode startAsync(CPEBean device, String type, TaskParameter param) {
		return TaskResultCode.TASK_START_OK;
	}

	public TaskResultCode stop(CPEBean device, String type, long ms) {
		return m_cpeManager.stop(device, type, ms);
	}
	
	public TaskResultCode reboot(CPEBean device) {
		return m_cpeManager.reboot(device);
	}
	
	public TaskResultCode upgrade(CPEBean device,UpgradeParameter m_upgradeParamter) {
		return m_cpeManager.upgrade(device,m_upgradeParamter);
	}

	public TaskResultCode stopAsync(CPEBean device, String type) {
		return TaskResultCode.TASK_STOP_OK;
	}

	public CPEBean setCPEBean(String serialNumber) {
		this.m_cpeBean = m_cpeManager.setCPEBean(serialNumber);
		return m_cpeBean;
	}
	
	public CPEBean setCPEBeanById(Long device_id){
		this.m_cpeBean = m_cpeManager.setCPEBeanById(device_id);
		return m_cpeBean;
	}
	
	public List<DeviceTaskDetailBean> queryTaskByDeviceSN(String serial_number){
		m_log.info("[queryTaskByDeviceSN] get into queryTaskByDeviceSN!");
		List<DeviceTask> deviceTask_List = m_deviceTaskRespository.findDeviceTaskBySerialNumber(serial_number);
		if(deviceTask_List == null || deviceTask_List.size() == 0){
			m_log.info("[queryTaskByDeviceSN] deviceTask_List.size() == 0");
			return null;
		}else{
			List<DeviceTaskDetailBean> result_list = new ArrayList<DeviceTaskDetailBean>();
			int rowId = 0;
			for(DeviceTask dt : deviceTask_List){
				DeviceTaskDetailBean dtdb = new DeviceTaskDetailBean(dt);
				dtdb.setM_rowId(++rowId);
				result_list.add(dtdb);
			}
			return result_list;
		}			
	}
	
	public List<NodeModelDetailBean> getParamListByType(String type){
		m_log.info("[getParamListByType] type = " + type);
		Task task = m_taskRespository.findByType(type);
		if(task == null){
			return null;
		}else{
			List<ParamSet> paramSet_list = m_paramSetRespository.findNodesByTask(task);
			if(paramSet_list == null || paramSet_list.size() == 0){
				m_log.info("[getParamListByType] paramSet_list == null");
				return null;
			}else{
			    List<NodeModelDetailBean> node_list = new ArrayList<NodeModelDetailBean>();
			    for(ParamSet ps : paramSet_list){
			    	NodeModelDetailBean ndb = new NodeModelDetailBean(ps.getM_nodeTask().getM_node());
			    	node_list.add(ndb);
			    }
			    return node_list;
			}
		}		
	}
	
	public String executeRPCMethod(CPEBean device,RPCParameterBean rpc_parameterBean){
		return m_cpeManager.executeRPC(device, rpc_parameterBean);
	}
}
