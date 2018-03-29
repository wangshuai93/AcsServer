package com.yinhe.server.AcsServer.ejb;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.yinhe.server.AcsServer.backbean.TaskBean;
import com.yinhe.server.AcsServer.backbean.TaskQueryBean;
import com.yinhe.server.AcsServer.data.CPERespository;
import com.yinhe.server.AcsServer.data.DeviceTaskRespository;
import com.yinhe.server.AcsServer.data.TaskRespository;
import com.yinhe.server.AcsServer.enums.SystemOperateResultCode;
import com.yinhe.server.AcsServer.model.DeviceTask;
import com.yinhe.server.AcsServer.model.Devices;
import com.yinhe.server.AcsServer.model.Task;

@Stateless
public class TaskSettingEJB {
	@Inject 
	private Logger m_log;
	@Inject  
	private TaskRespository m_taskRespository;
	@Inject
	private CPERespository m_cpeRespository;
	@Inject
	private DeviceTaskRespository m_deviceTaskRespository;
	
	public List<TaskBean> listTaskRecord(TaskQueryBean m_queryBean){
		m_queryBean.setMaxByCount(m_taskRespository.getTaskSize());
		List<Task> taskList = m_taskRespository.listTask(m_queryBean);
		if(taskList == null || taskList.size() == 0){
			m_log.info("[listTaskRecord] userList.size() == 0");
			return null;
		}else{
			List<TaskBean> taskRecordList = new ArrayList<TaskBean>();
			int rowId = 0;
			for(Task task:taskList){
				TaskBean detailBean = new TaskBean();
				
				detailBean.setM_rowId(++rowId);
				detailBean.setM_id(task.getM_id());
				detailBean.setM_name(task.getM_name());
				/*m_log.info(detailBean.toString());
				m_log.info("rowId= "+rowId);*/
				taskRecordList.add(detailBean);
			}
			return taskRecordList;
		}
	}
	
	public TaskBean findTaskByName(String name) {
		Task task = m_taskRespository.findByType(name);
		if (task != null) {
			TaskBean taskBean = new TaskBean();
			taskBean.setM_id(task.getM_id());
			taskBean.setM_name(task.getM_name());
			return taskBean;
		}
		return null;
	}
	
	public SystemOperateResultCode deleteTask(Long task_id){
		m_log.info("[deleteTask] task_id = " + task_id);
		Task task = m_taskRespository.findTaskByTaskId(task_id);
		if(task != null){
			m_log.info("[deleteTask] task != null ");
			boolean flag = false;
			List<DeviceTask> deviceTask_list = m_deviceTaskRespository.findDeviceTaskByTask(task);
			if(deviceTask_list != null && deviceTask_list.size() > 0){
				for(DeviceTask dt : deviceTask_list){
					if(dt.getM_status().equals("running")){
						flag = true;
						break;
					}
				}
			}
			if(!flag){
				try{
					m_taskRespository.deleteEntity(task);
					return SystemOperateResultCode.DELETE_TASK_SUCCESS;
				}catch(Exception ex){
					ex.printStackTrace();
					return SystemOperateResultCode.DELETE_TASK_FAILED;
				}
			}else{
				return SystemOperateResultCode.DELETE_TASK_RUNNING;
			}
		}else{
			return SystemOperateResultCode.DELETE_TASK_NULL;
		}
	}
	
	public SystemOperateResultCode addTask(String task_name){
		Task task2 = m_taskRespository.findByType(task_name);
		if(task2 != null){
			return SystemOperateResultCode.ADD_TASK_EXIST;
		}
		//添加任务，同时为deviceTask中每个设备添加任务
		Task task = new Task();
		task.setM_name(task_name);
		try{
			m_taskRespository.addEntity(task);
			List<Devices> devicesList = new ArrayList<Devices>();
			devicesList = m_cpeRespository.findAllOrderedByIdDesc();
			if(devicesList != null && devicesList.size() > 0){
				for(Devices device : devicesList){
					DeviceTask dt = new DeviceTask();
					dt.setM_task(task);
					dt.setM_device(device);
					dt.setM_period_time(1000000L);
					dt.setM_status("stopped");
					m_deviceTaskRespository.addEntity(dt);
				}
			}
			
			return SystemOperateResultCode.ADD_TASK_SUCCESS;
		}catch(Exception ex){
			ex.printStackTrace();
			return SystemOperateResultCode.ADD_TASK_FAILED;
		}
	}
	
}
