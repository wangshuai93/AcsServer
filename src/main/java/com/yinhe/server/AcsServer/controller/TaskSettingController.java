package com.yinhe.server.AcsServer.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.yinhe.server.AcsServer.backbean.TaskBean;
import com.yinhe.server.AcsServer.backbean.TaskQueryBean;
import com.yinhe.server.AcsServer.ejb.TaskSettingEJB;
import com.yinhe.server.AcsServer.enums.SystemOperateResultCode;
import com.yinhe.server.AcsServer.util.Resources;

@Named
@RequestScoped
public class TaskSettingController {
	@Inject 
	private Logger m_log;
	private List<TaskBean> m_allTask = new ArrayList<TaskBean>();
	@Inject
	private TaskQueryBean m_queryBean;
	@Inject
	private TaskSettingEJB m_taskSettingEJB;
	private String m_message;
	private String m_taskName;
	
	@PostConstruct
	private void init(){
		m_log.info("TaskSettingController init");
		
		String o = getRequestParamMap().get("load");
		if (o != null && "lazy".equals(o)) {
			m_log.info("TaskSettingController load: lazy!");
		} else {
			queryRecordList();
		}
	}
	
	public String deleteTask(){
		Long task_id = Long.parseLong(getRequestParamMap().get("task_id"));
		m_log.info("[deleteTask] task_id = " + task_id);
		if(task_id != null){
			m_message = m_taskSettingEJB.deleteTask(task_id).toLocalString();
			queryRecordList();
			return "";
		}else{
			m_message = SystemOperateResultCode.DELETE_TASK_FAILED.toLocalString();
			return "";
		}
	}
	
	public 	String addTask(){
		if(Resources.isNullOrEmpty(m_taskName)){
			m_message = SystemOperateResultCode.ADD_TASK_NULL.toLocalString();
			return "";
		}else{
			m_log.info("[addTask] taskName = " + m_taskName);
			m_message = m_taskSettingEJB.addTask(m_taskName).toLocalString();
			queryRecordList();
			return "";
		}
	}
	
	
	private void queryRecordList() {
		m_allTask = m_taskSettingEJB.listTaskRecord(m_queryBean);
	}
	
	public void queryCurrentPage() {
		this.queryRecordList();
	}

	public void queryPreviousPage() {
		m_queryBean.previous();
		this.queryRecordList();
	}

	public void queryNextPage() {
		m_queryBean.next();
		this.queryRecordList();
	}

	private Map<String, String> getRequestParamMap() {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	}
	
	public List<TaskBean> getM_allTask() {
		return m_allTask;
	}

	public void setM_allTask(List<TaskBean> m_allTask) {
		this.m_allTask = m_allTask;
	}

	public TaskQueryBean getM_queryBean() {
		return m_queryBean;
	}

	public void setM_queryBean(TaskQueryBean m_queryBean) {
		this.m_queryBean = m_queryBean;
	}

	public String getM_message() {
		return m_message;
	}

	public void setM_message(String m_message) {
		this.m_message = m_message;
	}

	public String getM_taskName() {
		return m_taskName;
	}

	public void setM_taskName(String m_taskName) {
		this.m_taskName = m_taskName;
	}
	
	@PreDestroy
	private void destroy() {
		m_log.info("system TaskSettingController record will be destoryed!\n");
	}
}
