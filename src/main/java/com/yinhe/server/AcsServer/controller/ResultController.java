package com.yinhe.server.AcsServer.controller;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.yinhe.server.AcsServer.backbean.CPEBean;
import com.yinhe.server.AcsServer.backbean.ColumnNameBean;
import com.yinhe.server.AcsServer.backbean.CountResultQueryBean;
import com.yinhe.server.AcsServer.backbean.ManagedResultBean;
import com.yinhe.server.AcsServer.backbean.ResultDetailBean;
import com.yinhe.server.AcsServer.backbean.ResultQueryBean;
import com.yinhe.server.AcsServer.backbean.TaskBean;
import com.yinhe.server.AcsServer.ejb.CPEHandler;
import com.yinhe.server.AcsServer.ejb.ResultManagerEJB;
import com.yinhe.server.AcsServer.ejb.SystemSettingEJB;
import com.yinhe.server.AcsServer.tcpClient.TcpClient;
import com.yinhe.server.AcsServer.util.Resources;

@Named
@ViewScoped
public class ResultController implements Serializable{

	private static final long serialVersionUID = -1050747088997999344L;
	@Inject 
	private Logger log;
	private List<ResultDetailBean> m_resultList = new ArrayList<ResultDetailBean>();
	@Inject
	private CountResultQueryBean m_countResultQueryBean;
	private String m_message;
	private Map<String, Object> session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	@Inject
	private ResultManagerEJB m_resultEJB;
	private List<TaskBean> m_taskList;
	@Inject
	private SystemSettingEJB m_systemSettingEJB;
	private List<ManagedResultBean> m_managedResultBean;
	private List<String> m_paramGet;
	private List<ColumnNameBean> m_columnNameList;
	@Inject
	private CPEHandler m_handler;
	@Inject
	private ResultQueryBean m_resultQueryBean;
		
	@PostConstruct
	public void init(){
		log.info("[init] ResultController record init");
		m_taskList = new ArrayList<TaskBean>();
		m_managedResultBean = new ArrayList<ManagedResultBean>();
		m_paramGet = new ArrayList<String>();
		m_columnNameList = new ArrayList<ColumnNameBean>();
		m_taskList = m_systemSettingEJB.listAllTask();
		
		String o = getRequestParamMap().get("load");
		if (o != null && "lazy".equals(o)) {
			log.info("[init] ResultController load: lazy!");
		} else {
			m_countResultQueryBean.setCount(0);
			queryRecordList();
		}
	}
	
	public boolean isConstellationTask(){
		if(m_countResultQueryBean.getM_taskId()!=null){
			if(m_countResultQueryBean.getM_taskId() == 13L)
				return true;
			else
				return false;
		}	
		else
			return false;
	}
	
	public boolean isFullTask(){
		if(m_countResultQueryBean.getM_taskId()!=null){
			if(m_countResultQueryBean.getM_taskId() == 14L)
				return true;
			else
				return false;
		}	
		else
			return false;
	}
	
	public boolean isConstellationTasktest(){
		if(m_countResultQueryBean.getM_taskId()!=null){
			if(m_countResultQueryBean.getM_taskId() == 13L)
				return true;
			else
				return false;
		}	
		else
			return false;
	}
	
	private Map<String, String> getRequestParamMap() {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	}
	
	private void queryRecordList() {
		String serialNumber = this.getSerialNumber();
		if(serialNumber != null){
			m_countResultQueryBean.setM_serialNumber(serialNumber);
			log.info("[queryRecordList] m_countResultQueryBean.getMax() = " + m_countResultQueryBean.getMax());
			log.info("[queryRecordList] m_countResultQueryBean.getCurrent() = " + m_countResultQueryBean.getCurrent());
			m_columnNameList = m_resultEJB.listColumnName(m_countResultQueryBean);
			m_paramGet = m_resultEJB.listParamGet(m_countResultQueryBean);
			m_managedResultBean = m_resultEJB.listResultOrderedByName(m_countResultQueryBean);
			log.info("[queryRecordList] m_countResultQueryBean.getMax() = " + m_countResultQueryBean.getMax());
			log.info("[queryRecordList] m_countResultQueryBean.getCurrent() = " + m_countResultQueryBean.getCurrent());
		}
	}
	
	public String returnDeviceInfo(){
		return "device_info.jsf";
	}
	
	public void queryFirstPage(){	
			m_countResultQueryBean.setCurrent(1);
		queryRecordList();
	}
	
	public void queryCurrentPage() {
		this.queryRecordList();
	}
	
	public void queryPreviousPage() {
		m_countResultQueryBean.previous();
		this.queryRecordList();
	}

	public void queryNextPage() {		
		m_countResultQueryBean.next();
		this.queryRecordList();
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
			log.info("[getTaskId] task_id = " + task_id);
			return task_id;			
		} 	
		return null;
	}
	
	public void queryByTask(ValueChangeEvent event){
		m_managedResultBean = new ArrayList<ManagedResultBean>();
		m_paramGet = new ArrayList<String>();
		m_columnNameList = new ArrayList<ColumnNameBean>();
		m_countResultQueryBean.setM_nodeId(null);
		Long task_id = (Long) event.getNewValue();
		log.info("[queryByTask] task_id = " + task_id);
		if(null != task_id){
			session.put("task_id", task_id);
			
			String serialNumber = this.getSerialNumber();
			if(serialNumber != null){
				
				CPEBean device = m_handler.setCPEBean(serialNumber);
				if(device != null){
					m_resultQueryBean.setM_device_id(device.getM_id());
					log.info("ResultController device_id = " + m_resultQueryBean.getM_device_id());
				}
				
				m_countResultQueryBean.setM_serialNumber(serialNumber);
				m_countResultQueryBean.setM_taskId(task_id);
				m_countResultQueryBean.setCurrent(1);
				
				m_columnNameList = m_resultEJB.listColumnName(m_countResultQueryBean);
				m_paramGet = m_resultEJB.listParamGet(m_countResultQueryBean);
				m_managedResultBean = m_resultEJB.listResultOrderedByName(m_countResultQueryBean);
				
				//add by hdt 2017-11-15
				m_resultQueryBean.setM_serialNumber(serialNumber);
				m_resultQueryBean.setM_taskId(task_id);
				//
			}
		}
	}
	
	public void queryTask(){
		m_managedResultBean = new ArrayList<ManagedResultBean>();
		m_paramGet = new ArrayList<String>();
		m_columnNameList = new ArrayList<ColumnNameBean>();
		m_countResultQueryBean.setM_nodeId(null);
		//Long task_id = (Long) event.getNewValue();
		Long task_id = m_countResultQueryBean.getM_taskId();
		log.info("[queryTask] task_id = " + task_id);
		if(null != task_id){
			session.put("task_id", task_id);
			
			String serialNumber = this.getSerialNumber();
			if(serialNumber != null){
				
				CPEBean device = m_handler.setCPEBean(serialNumber);
				if(device != null){
					m_resultQueryBean.setM_device_id(device.getM_id());
					log.info("ResultController device_id = " + m_resultQueryBean.getM_device_id());
				}
				
				m_countResultQueryBean.setM_serialNumber(serialNumber);
				m_countResultQueryBean.setM_taskId(task_id);
				m_countResultQueryBean.setCurrent(1);
				
				//m_columnNameList = m_resultEJB.listColumnName(m_resultQueryBean);
				//m_paramGet = m_resultEJB.listParamGet(m_resultQueryBean);
				//m_managedResultBean = m_resultEJB.listResultOrderedByName(m_resultQueryBean);
				m_countResultQueryBean.setM_beginTime(m_resultQueryBean.getM_beginTime());
				m_countResultQueryBean.setM_endTime(m_resultQueryBean.getM_endTime());
				
				m_columnNameList = m_resultEJB.listColumnName(m_countResultQueryBean);
				m_paramGet = m_resultEJB.listParamGet(m_countResultQueryBean);
				m_managedResultBean = m_resultEJB.listResultOrderedByName(m_countResultQueryBean);
			}
		}

	}
	
	public void queryByTaskAndNode(ValueChangeEvent event){
		Long node_id = (Long) event.getNewValue();
		log.info("[queryByTaskAndNode] node_id = " + node_id);
		Long task_id = this.getTaskId();
		if(task_id != null){
			String serialNumber = this.getSerialNumber();
			m_countResultQueryBean.setM_serialNumber(serialNumber);
			m_countResultQueryBean.setM_taskId(task_id);
			if(node_id != null){
				m_countResultQueryBean.setM_nodeId(node_id);
			}
		}
	}
	
	public String getTaskName(Long taskId) {
		if (m_taskList != null) {
			for (TaskBean taskBean : m_taskList) {
				if (taskId == taskBean.getM_id()) {
					return taskBean.getM_name();
				}
			}
		}
		return null;
	}

	public List<ResultDetailBean> getM_resultList() {
		return m_resultList;
	}

	public void setM_resultList(List<ResultDetailBean> m_resultList) {
		this.m_resultList = m_resultList;
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

	public void setM_taskList(List<TaskBean> m_taskList) {
		this.m_taskList = m_taskList;
	}

	public CountResultQueryBean getM_countResultQueryBean() {
		return m_countResultQueryBean;
	}

	public void setM_countResultQueryBean(
			CountResultQueryBean m_countResultQueryBean) {
		this.m_countResultQueryBean = m_countResultQueryBean;
	}

	public List<ManagedResultBean> getM_managedResultBean() {
		return m_managedResultBean;
	}

	public void setM_managedResultBean(List<ManagedResultBean> m_managedResultBean) {
		this.m_managedResultBean = m_managedResultBean;
	}

	public List<String> getM_paramGet() {
		return m_paramGet;
	}

	public void setM_paramGet(List<String> m_paramGet) {
		this.m_paramGet = m_paramGet;
	}
	
	public List<ColumnNameBean> getM_columnNameList() {
		return m_columnNameList;
	}

	public void setM_columnNameList(List<ColumnNameBean> m_columnNameList) {
		this.m_columnNameList = m_columnNameList;
	}
	
	public ResultQueryBean getM_resultQueryBean() {
		return m_resultQueryBean;
	}

	public void setM_resultQueryBean(ResultQueryBean m_resultQueryBean) {
		this.m_resultQueryBean = m_resultQueryBean;
	}

	@PreDestroy
	private void destroy() {
		log.info("[destroy] system ResultController record will be destoryed!\n");
	}
	
}
