package com.yinhe.server.AcsServer.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.yinhe.server.AcsServer.backbean.CPEBean;
import com.yinhe.server.AcsServer.backbean.DeviceTaskBean;
import com.yinhe.server.AcsServer.backbean.NodeModelDetailBean;
import com.yinhe.server.AcsServer.backbean.ParamSetBean;
import com.yinhe.server.AcsServer.backbean.ParamSetQueryBean;
import com.yinhe.server.AcsServer.backbean.PeriodBean;
import com.yinhe.server.AcsServer.backbean.TaskBean;
import com.yinhe.server.AcsServer.backbean.TaskParameterBean;
import com.yinhe.server.AcsServer.ejb.SystemSettingEJB;
import com.yinhe.server.AcsServer.ejb.TaskHandler;
import com.yinhe.server.AcsServer.enums.TaskResultCode;

@Named
@ViewScoped
public class TaskManageController  implements Serializable{
	
	private static final long serialVersionUID = 8016772590993132990L;
	public static final String m_periodicEnable = "Device.Task.PeriodicEnable";
	public static final String m_period = "Device.Task.Period";
	@Inject
	private Logger log;
	private Map<String, Object> session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	private String m_message;
	private List<TaskBean> m_taskList;
	private List<ParamSetBean> m_paramSetList;
	private List<NodeModelDetailBean> m_nodeParamsList;  //下拉框参数，与任务相关
    @Inject  
    private TaskHandler m_handler;
    private Long m_taskId;
    @Inject
	private SystemSettingEJB m_systemSettingEJB;
    @Inject
    private ParamSetBean m_paramSetBean;
    @Inject
    private ParamSetQueryBean m_queryBean;
    @Inject
	private CPEBean m_cpeBean;
    @Inject
    private DeviceTaskBean m_deviceTaskBean;
    @Inject 
    private TaskParameterBean m_taskParameterBean;
    private List<PeriodBean> m_periodList; 
    private boolean IsView;
    
	
	@PostConstruct
	public void init(){	
		log.info("[init] init TaskManageController");
		m_taskList = new ArrayList<TaskBean>(); 
		m_paramSetList = new ArrayList<ParamSetBean>();
		m_nodeParamsList = new ArrayList<NodeModelDetailBean>();
		m_periodList = new ArrayList<PeriodBean>();
		
		m_taskList = m_systemSettingEJB.listAllTask();	
		IsView = false;
	}
	
	public boolean isConstellationTask(){
		if(m_taskId!=null){
			if(m_taskId == 13L)
				return true;
			else
				return false;
		}	
		else
			return false;
	}
	
	public void getParamSetAndAll(ValueChangeEvent event){
		Long task_id = (Long) event.getNewValue();
		log.info("[getParamSetAndAll] getParam task_id = " + task_id);
		m_paramSetList = new ArrayList<ParamSetBean>();
		if(null != task_id){
			session.put("task_id", task_id);
			String serialNumber = this.getSerialNumber();	
			m_deviceTaskBean = m_systemSettingEJB.findDeviceTaskBySN(serialNumber, task_id);
			m_nodeParamsList = m_systemSettingEJB.listNodesByTaskId(task_id);
			m_paramSetList = m_systemSettingEJB.listParamSetBean(task_id);
			
//			if(m_taskId!=null){
//				if(m_taskId == 13L)
//					setIsView(true);
//				else
//					setIsView(false);
//			}	
//			else
//				setIsView(false);

		}
	}
	
	public boolean isShowDelete(String param_name){
		if(param_name.equals(m_periodicEnable) || param_name.equals(m_period)){
			return true;
		}
		return false;
	}
	
	public boolean isShowChange(String param_name){
		if(param_name.equals(m_periodicEnable) || param_name.equals(m_period)){
			return false;
		}
		return true;
	}
	
	/**
	 * 更改下发参数的默认值
	 * @return
	 */
	public String changeParamSetValue(){
		m_message="";
		log.info("[changeParamSetValue] get into changeParamSetValue");
		if(null != m_paramSetBean){
			Long paramSet_id = Long.parseLong(getRequestParamMap().get("window_node_id"));
			log.info("[changeParamSetValue] set temp paramSet_id = " + paramSet_id);
			ParamSetBean psb = m_systemSettingEJB.findByParamSetId(paramSet_id);
			int rowId = 1;
			if(null != m_paramSetList && 0 != m_paramSetList.size()){
				rowId = m_paramSetList.size() + 1;
				for(ParamSetBean bean : m_paramSetList){
					if(bean.getM_id() == paramSet_id){
						log.info("[changeParamSetValue] bean.getM_id() == paramSet_id");
						bean.setM_value(m_paramSetBean.getM_value());
						m_systemSettingEJB.updateParamSet(bean);
						return "";
					}
				}
			}
			
			ParamSetBean paramSetBean = new ParamSetBean();
			paramSetBean.setM_rowId(rowId);
			paramSetBean.setM_nodePath(psb.getM_nodePath());
			paramSetBean.setM_taskName(psb.getM_taskName());
			paramSetBean.setM_type(psb.getM_type());
			paramSetBean.setM_value(m_paramSetBean.getM_value());
			m_paramSetList.add(paramSetBean);
		}
		return "";
	}
	
	public String changeParamSetValue2(){
		log.info("[changeParamSetValue2] get into changeParamSetValue2");
		m_paramSetList = new ArrayList<ParamSetBean>();
		if(null != m_paramSetBean){
			Long paramSet_id = Long.parseLong(getRequestParamMap().get("window_node_id"));
			log.info("[changeParamSetValue2] set temp paramSet_id = " + paramSet_id);
			Long task_id = getTaskId();
			m_paramSetList = m_systemSettingEJB.listParamSetBean(task_id);
			if(null != m_paramSetList && 0 != m_paramSetList.size()){
				for(ParamSetBean bean : m_paramSetList){
					if(bean.getM_id() == paramSet_id){
						log.info("[changeParamSetValue2] bean.getM_id() == paramSet_id");
						bean.setM_value(m_paramSetBean.getM_value());
						m_systemSettingEJB.updateParamSet(bean);
						return "";
					}
				}
			}
		}
		return "";
	}
	
	public String addPeriodEnable(ValueChangeEvent event){
		Boolean is_period = (Boolean) event.getNewValue();
		log.info("[addPeriodEnable] is_period = " + is_period);
		if(is_period == null){
			return "";
		}
		if(is_period){
			addPeriodList();  //级联period
			int rowId = 1;
			if(null != m_paramSetList && 0 != m_paramSetList.size()){
				log.info("m_paramSetList.size() = " + m_paramSetList.size());
				rowId = m_paramSetList.size() + 1;
				for(ParamSetBean bean : m_paramSetList){
					if(bean.getM_nodePath().equals("Device.Task.PeriodicEnable")){
						bean.setM_value("true");
						return "";
					}
				}
			}
			ParamSetBean paramSetBean = new ParamSetBean();
			paramSetBean.setM_rowId(rowId);
			paramSetBean.setM_nodePath(m_periodicEnable);
			paramSetBean.setM_type("boolean");
			paramSetBean.setM_value("true");
			m_paramSetList.add(paramSetBean);
			log.info("[addPeriodEnable] m_paramSetList.size() = " + m_paramSetList.size());
		}else{
			List<ParamSetBean> tempList = new ArrayList<ParamSetBean>();
			if(null != m_paramSetList && 0 != m_paramSetList.size()){
				for(ParamSetBean bean : m_paramSetList){
					if(bean.getM_nodePath().equals(m_periodicEnable) || bean.getM_nodePath().equals(m_period)){
						tempList.add(bean);
					}
				}	
				m_paramSetList.removeAll(tempList);
			}
			
			int row_id = 1;
			if(null != m_paramSetList && 0 != m_paramSetList.size()){
				for(ParamSetBean bean : m_paramSetList){
					bean.setM_rowId(row_id++);
				}
			}
			
			m_periodList.clear();
		}
		return "";
	}
	
	public String addPeriod(ValueChangeEvent event){
		log.info("[addPeriod] get into addPeriod");
		Long period = (Long) event.getNewValue();
		log.info("[addPeriod] period = " + period);
		if(null != period){
			int rowId = 1;
			log.info("[addPeriod] m_paramSetList.size() = " + m_paramSetList.size());
			if(null != m_paramSetList && 0 != m_paramSetList.size()){
				rowId = m_paramSetList.size() + 1;
				for(ParamSetBean bean : m_paramSetList){
					if(bean.getM_nodePath() == m_period){
						bean.setM_value(period.toString());
						return "";
					}
				}
			}
			ParamSetBean paramSetBean = new ParamSetBean();
			paramSetBean.setM_rowId(rowId++);
			paramSetBean.setM_nodePath(m_period);
			paramSetBean.setM_type("unsignedInt");
			paramSetBean.setM_value(period.toString());
			m_paramSetList.add(paramSetBean);
			log.info("[addPeriod] m_paramSetList.size() = " + m_paramSetList.size());
		}else{
			if(null != m_paramSetList && 0 != m_paramSetList.size()){
				for(ParamSetBean bean : m_paramSetList){
					if(bean.getM_nodePath() == m_period){
						m_paramSetList.remove(bean);
						return "";
					}
				}
			}
		}
		return "";
	}
	
	public void deletePeriod(){
		log.info("[deletePeriod] get into deletePeriod");
		if(null != m_paramSetList && 0 != m_paramSetList.size()){
			for(ParamSetBean bean : m_paramSetList){
				if(bean.getM_nodePath() == m_period){
					log.info("[deletePeriod] period exist!");
					m_paramSetList.remove(bean);
				}
			}
		}
	}
	
	/**
	 * 启动任务
	 */
	public void start(){
		log.info("[start] get into start111 ");
		String serialNumber = getSerialNumber();
		Long task_id = getTaskId();
		log.info("[start] serialNumber= " + serialNumber);
		setM_cpeBean(m_handler.setCPEBean(serialNumber));
		if(m_cpeBean == null){
			m_message = TaskResultCode.TASK_START_ERROR.toLocalString();
		
			log.info("[start] error");
			return;
		}else{
			Long wait_time = 1500L;
			if(m_paramSetList == null){
				m_message = TaskResultCode.TASK_RESULT_PARAMETER_NULL.toLocalString();
				log.info("[start] m_message= "+m_message);
				return;
			}else{
				m_message = m_handler.start2(m_cpeBean, m_paramSetList, wait_time).toLocalString();
				log.info("[start] m_message= "+m_message+" ****"+TaskResultCode.TASK_START_OK.toLocalString());
				if(m_message.equals(TaskResultCode.TASK_START_OK.toLocalString())){
			
					m_deviceTaskBean.setM_status("running");
					for(ParamSetBean ps : m_paramSetList){
						if(ps.getM_nodePath().equals("Device.Task.PeriodicEnable")){
							if(ps.getM_value().equals("true")){
								//变为running
								log.info("[start] changeTaskStatusToRunning");
								changeTaskStatusToRunning(serialNumber,task_id);
							}
						}
					}
				}
			}
		}
		
	}
	
	//改变任务状态
	public void changeTaskStatusToRunning(String serialNumber,Long task_id){
		m_handler.changeTaskStatusToRunning(serialNumber, task_id);
	}
	
	public void deleteParamSet(){
		log.info("[deleteParamSet] get into deleteParamSet");
		String param_path = (String)(getRequestParamMap().get("node_id"));
		log.info("[deleteParamSet] param_path = " + param_path);
		List<ParamSetBean> tempList = new ArrayList<ParamSetBean>();
		if(param_path != null){
			if(null != m_paramSetList && 0 != m_paramSetList.size()){
				for(ParamSetBean bean : m_paramSetList){
					if(bean.getM_nodePath().equals(param_path)){
						tempList.add(bean);
					}
				}
			}
			m_paramSetList.removeAll(tempList);
			
			int row_id = 1;
			if(null != m_paramSetList && 0 != m_paramSetList.size()){
				for(ParamSetBean bean : m_paramSetList){
					bean.setM_rowId(row_id++);
				}
			}
		}
	}
	
	public void addPeriodList(){
		m_periodList.add(new PeriodBean("5秒",5000L));
		m_periodList.add(new PeriodBean("5分钟",300000L));
		m_periodList.add(new PeriodBean("30分钟",1800000L));
		m_periodList.add(new PeriodBean("1小时",3600000L));
		m_periodList.add(new PeriodBean("6小时",21600000L));
		m_periodList.add(new PeriodBean("12小时",43200000L));
		m_periodList.add(new PeriodBean("1天",86200000L));
		log.info("[addPeriodList] m_periodList.size() = " + m_periodList.size());
	}
	
	public String returnDeviceInfo(){
		return "device_info.jsf";
	}
	
	private String getSerialNumber(){
		Object taskId = session.get("serial_number");
		String serialNumber = "";
		if(taskId != null) {
			serialNumber = (String)taskId;
			log.info("[getSerialNumber] serial_number = " + serialNumber);
			return serialNumber;			
		} 	
		return null;
	}
	
	private Long getTaskId(){
		Object taskId = session.get("task_id");
		Long task_id = null;
		if(taskId != null) {
			task_id = (Long)taskId;
			log.info("task_id = " + task_id);
			return task_id;			
		} 	
		return null;
	}
	
	private Map<String, String> getRequestParamMap() {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	}

	public String getM_message() {
		return m_message;
	}

	public void setM_message(String m_message) {
		this.m_message = m_message;
	}

	public List<TaskBean> getM_taskList() {
		return m_taskList;
	}

	public void setM_taskList(List<TaskBean> m_taskLsit) {
		this.m_taskList = m_taskLsit;
	}
	
	public List<ParamSetBean> getM_paramSetList() {
		return m_paramSetList;
	}

	public void setM_paramSetList(List<ParamSetBean> m_paramSetList) {
		this.m_paramSetList = m_paramSetList;
	}

	public Long getM_taskId() {
		return m_taskId;
	}

	public void setM_taskId(Long m_taskId) {
		this.m_taskId = m_taskId;
	}

	public List<NodeModelDetailBean> getM_nodeParamsList() {
		return m_nodeParamsList;
	}

	public void setM_nodeParamsList(List<NodeModelDetailBean> m_nodeParamsList) {
		this.m_nodeParamsList = m_nodeParamsList;
	}

	public ParamSetBean getM_paramSetBean() {
		return m_paramSetBean;
	}

	public void setM_paramSetBean(ParamSetBean m_paramSetBean) {
		this.m_paramSetBean = m_paramSetBean;
	}

	public ParamSetQueryBean getM_queryBean() {
		return m_queryBean;
	}

	public void setM_queryBean(ParamSetQueryBean m_queryBean) {
		this.m_queryBean = m_queryBean;
	}

	public CPEBean getM_cpeBean() {
		return m_cpeBean;
	}

	public void setM_cpeBean(CPEBean m_cpeBean) {
		this.m_cpeBean = m_cpeBean;
	}

	public DeviceTaskBean getM_deviceTaskBean() {
		return m_deviceTaskBean;
	}

	public void setM_deviceTaskBean(DeviceTaskBean m_deviceTaskBean) {
		this.m_deviceTaskBean = m_deviceTaskBean;
	}

	public TaskParameterBean getM_taskParameterBean() {
		return m_taskParameterBean;
	}

	public void setM_taskParameterBean(TaskParameterBean m_taskParameterBean) {
		this.m_taskParameterBean = m_taskParameterBean;
	}

	public List<PeriodBean> getM_periodList() {
		return m_periodList;
	}

	public void setM_periodList(List<PeriodBean> m_periodList) {
		this.m_periodList = m_periodList;
	}

	public boolean isIsView() {
		return IsView;
	}

	public void setIsView(boolean isView) {
		IsView = isView;
	}
}
