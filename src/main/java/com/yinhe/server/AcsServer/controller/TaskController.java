package com.yinhe.server.AcsServer.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.yinhe.server.AcsServer.backbean.CPEBean;
import com.yinhe.server.AcsServer.backbean.DeviceTaskDetailBean;
import com.yinhe.server.AcsServer.backbean.NodeModelDetailBean;
import com.yinhe.server.AcsServer.backbean.ParamGetBean;
import com.yinhe.server.AcsServer.backbean.ParamSetBean;
import com.yinhe.server.AcsServer.backbean.QueryTaskResultBean;
import com.yinhe.server.AcsServer.backbean.TaskBean;
import com.yinhe.server.AcsServer.backbean.UpgradeParameter;
import com.yinhe.server.AcsServer.ejb.TaskEJB;
import com.yinhe.server.AcsServer.ejb.TaskHandler;
import com.yinhe.server.AcsServer.enums.TaskResultCode;
import com.yinhe.server.AcsServer.enums.TaskStatus;
import com.yinhe.server.AcsServer.tcpClient.TcpClient;
import com.yinhe.server.AcsServer.util.Resources;

@Named
@RequestScoped
public class TaskController {
	@Inject
	private TaskHandler m_handler;
	@Inject
	private CPEBean m_cpeBean;
	@Inject
	private TaskBean m_taskBean;
	@Inject
	private UpgradeParameter m_upgradeParamter;
	@Inject
	private TaskEJB taskEJB;
	
	private List<DeviceTaskDetailBean> m_deviceTaskList;
	private List<NodeModelDetailBean> m_paramSet_list;

	@Inject
	private Logger log;

	private Map<String, String> requestParamMap;

	private String m_taskMessage;

	QueryTaskResultBean m_broadcastQuery;
	private Map<String, Object> session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	
	String m_roleName;
	private static final String TASK_NAME = "RECORD_TASK";
	
	@PostConstruct
	public void init(){	
		log.info("[init] init taskController!");
		m_deviceTaskList = new ArrayList<DeviceTaskDetailBean>();
		Object taskId = session.get("serial_number");
		String serialNumber = "";
		if(taskId != null) {
			serialNumber = (String)taskId;
			log.info("[init] init taskController,serial_number = " + serialNumber);
		} 		
		setM_cpeBean(m_handler.setCPEBean(serialNumber));	
		m_deviceTaskList = m_handler.queryTaskByDeviceSN(serialNumber);	
	}
	
	public String gotoDevicesDetailPage() {
		// 获取网页中的 detail_serial_number
		// device_manage.xhtml
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		String[] idArray = context.getRequestParameterValuesMap().get("detail_serial_number");
		if (idArray == null || idArray.length == 0) {
			return null;
		}
		String serialNumber = "";
		try {
			serialNumber = idArray[0];
			log.info("[gotoDevicesDetailPage] detail_serial_number == " + serialNumber);
		} catch (Exception e) {
			return null;
		}		
		session.put("serial_number", serialNumber);	
		setM_cpeBean(m_handler.setCPEBean(serialNumber));	
		m_deviceTaskList = m_handler.queryTaskByDeviceSN(serialNumber);	
		return "device_info.jsf";
	}
	
	public String gotoDataModelPage(){
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		String[] idArray = context.getRequestParameterValuesMap().get("detail_serial_number");
		if (idArray == null || idArray.length == 0) {
			return null;
		}
		String serialNumber = "";
		try {
			serialNumber = idArray[0];
			log.info("[gotoDataModelPage] detail_serial_number == " + serialNumber);
		} catch (Exception e) {
			return null;
		}		
		session.put("serial_number", serialNumber);	
		setM_cpeBean(m_handler.setCPEBean(serialNumber));	
		return "datamodel.jsf";
	}
	
	public String gotoTaskManagePage(){
		return "task_manage.jsf";
	}
	
	public String gotoResultPage(){
		return "result_page.jsf";
	}
	
	//device_manage.xhtml
	public String returnAllDevices() {
		return "device_manage.jsf";
	}
	
	public String returnMainView(){
		return "main_view.jsf";
	}
	
	public String gotoRecordTask() {
		return "record_task.jsf";
	}
	
	//从start task页面返回
	public String returnDeviceInfo(){
		String serialNumber = getSerialNumber();
		log.info("[returnDeviceInfo] serialNumber = " + serialNumber);
		setM_cpeBean(m_handler.setCPEBean(serialNumber));
		return "device_info.jsf";
	}
	
	/**
	 * @Title: getStatus
	 * @author: wr@yinhe.com
	 * @Description: 获取设备某一任务的状态
	 * @param @param device
	 * @param @param type
	 * @param @return
	 * @return TaskStatus
	 * @throws
	 * @date 2017年3月3日 下午2:13:45
	 */
	public TaskStatus getStatus(String type) {
		String serialNumber = "";
		if(null != m_cpeBean){
			log.info("m_cpeBean = "  + m_cpeBean.toString());
			serialNumber = m_cpeBean.getM_serialNumber();
			setM_cpeBean(m_handler.setCPEBean(serialNumber));
		} else {
			log.info("m_cpeBean == null");
		}
		
		// 从详细页面跳进
		/*String serialNumber = getRequestParamMap().get("detail_serial_number");
		//log.info("serialNumber = " + serialNumber);
		if(null == serialNumber){
			//启动任务成功后返回详情页面，判断状态
			serialNumber = getRequestParamMap().get("task_serial_number");
		}
		if(null == serialNumber){
			//从任务结果中返回，获取状态
			serialNumber = m_cpeBean.getM_serialNumber();
		}*/
		//setM_cpeBean(m_handler.setCPEBean(serialNumber));
		TaskStatus taskStatus = null;
		if (null != m_cpeBean && !type.equals("")) {
			taskStatus = m_handler.getStatus(m_cpeBean, type);
			return taskStatus;
		} else {
			return TaskStatus.UNKNOWN;
		}
	}
	
	/**
	 * @Title: changeTaskStatus
	 * @author: wr@yinhe.com
	 * @Description: 启动任务成功后，往数据库中塞值，且切换任务状态
	 * @param @param serialNumber
	 * @param @param type
	 * @return void
	 * @throws  
	 * @date  2017年3月27日 下午4:39:44
	 */
	public void changeTaskStatus(String serialNumber, String type){
		setM_cpeBean(m_handler.setCPEBean(serialNumber));
		m_handler.changeTaskStatus(m_cpeBean, type);
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
	
	@SuppressWarnings("unused")
	private Map<String, String> getRequestParamMap() {
		if (requestParamMap == null) {
			requestParamMap = FacesContext.getCurrentInstance()
					.getExternalContext().getRequestParameterMap();
		}
		return requestParamMap;
	}

	public void startAsync(CPEBean device, String type) {

	}
	
	public void reboot(){
		String serialNumber = getSerialNumber();
		log.info("[reboot] serialNumber = " + serialNumber);
		setM_cpeBean(m_handler.setCPEBean(serialNumber));
		if (null == m_cpeBean) {
			m_taskMessage = TaskResultCode.TASK_REBOOT_ERROR.toLocalString();
			return;
		} else {
			m_taskMessage = m_handler.reboot(m_cpeBean).toLocalString();
		}
	}
	
	public String gotoUpgrade(){
		String serialNumber = getSerialNumber();
		log.info("[gotoUpgrade] serialNumber = " + serialNumber);
		setM_cpeBean(m_handler.setCPEBean(serialNumber));
		return "up_grade.jsf";
	}
	
	public void upgrade(){
		String serialNumber = getSerialNumber();
		log.info("[upgrade] serialNumber = " + serialNumber);
		setM_cpeBean(m_handler.setCPEBean(serialNumber));
		if (null == m_cpeBean) {
			m_taskMessage = TaskResultCode.TASK_UPDATE_ERROR.toLocalString();
			return;
		} else {
			m_taskMessage = m_handler.upgrade(m_cpeBean,m_upgradeParamter).toLocalString();
		}
	}
	
	public void stopAsync(CPEBean device, String type) {

	}

	public void getResult(CPEBean device, String type) {

	}
	
	//启动获取录制列表的任务
	public void startRecordTask() {
		//todo获取下发任务参数
		Object sn = session.get("serial_number");
		String serialNumber = "";
		if(sn != null) {
			serialNumber = (String)sn;
			log.info("[startRecordTask] serialNumber = " + serialNumber);
			setM_cpeBean(m_handler.setCPEBean(serialNumber));
			
			List<ParamGetBean> paramGet_list = taskEJB.findByTaskName(TASK_NAME);
			if (m_cpeBean != null) {
				Long ms = 1000L;
				m_taskMessage = m_handler.startRecordTask(m_cpeBean, paramGet_list, ms).toLocalString();
			} else {
				m_taskMessage = TaskResultCode.TASK_START_RECORD_ERROR.toLocalString();
			}
		} 	
	}
	
	//test
	public void downloadTest(String fileName) {
		System.out.println("downloadTest==================");
		if (!Resources.isNullOrEmpty(fileName)) {
			String path = "";
			TcpClient tcpClient = new TcpClient();
			tcpClient.download("172.16.218.5", path, fileName);
		}
	}

	public CPEBean getM_cpeBean() {
		return m_cpeBean;
	}

	public void setM_cpeBean(CPEBean m_cpeBean) {
		this.m_cpeBean = m_cpeBean;
	}

	public TaskBean getM_taskBean() {
		return m_taskBean;
	}

	public void setM_taskBean(TaskBean m_taskBean) {
		this.m_taskBean = m_taskBean;
	}

	public String getM_taskMessage() {
		return m_taskMessage;
	}

	public void setM_taskMessage(String m_taskMessage) {
		this.m_taskMessage = m_taskMessage;
	}	

	public UpgradeParameter getM_upgradeParamter() {
		return m_upgradeParamter;
	}

	public void setM_upgradeParamter(UpgradeParameter m_upgradeParamter) {
		this.m_upgradeParamter = m_upgradeParamter;
	}

	public List<DeviceTaskDetailBean> getM_deviceTaskList() {
		return m_deviceTaskList;
	}

	public void setM_deviceTaskList(List<DeviceTaskDetailBean> m_deviceTaskList) {
		this.m_deviceTaskList = m_deviceTaskList;
	}

	public List<NodeModelDetailBean> getM_paramSet_list() {
		return m_paramSet_list;
	}

	public void setM_paramSet_list(List<NodeModelDetailBean> m_paramSet_list) {
		this.m_paramSet_list = m_paramSet_list;
	}	
	
	@PreDestroy
	private void destroy() {
		log.info("[destroy] TaskController record will be destoryed!\n");
	}
}
