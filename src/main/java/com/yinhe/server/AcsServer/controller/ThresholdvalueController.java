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

import com.yinhe.server.AcsServer.backbean.NodeModelDetailBean;
import com.yinhe.server.AcsServer.backbean.TaskBean;
import com.yinhe.server.AcsServer.backbean.ThresholdValueBean;
import com.yinhe.server.AcsServer.backbean.ThresholdValueQueryBean;
import com.yinhe.server.AcsServer.ejb.SystemSettingEJB;
import com.yinhe.server.AcsServer.enums.ThresholdValueResultCode;
import com.yinhe.server.AcsServer.model.Node;

@Named
@ViewScoped
public class ThresholdvalueController implements Serializable{

	private static final long serialVersionUID = -28208642916570201L;
	
	@Inject 
	private Logger log;
	@Inject
	private SystemSettingEJB m_systemSettingEJB;
	@Inject
	private ThresholdValueQueryBean m_queryBean;
	@Inject
	private ThresholdValueBean m_thresholdBean;
	
	private String m_message;
	private List<TaskBean> m_allTask;
	private Long task_id;
	private List<NodeModelDetailBean> m_nodeParamsList;
	private List<NodeModelDetailBean> m_nodeParamsChoiceList;
	
	private List<ThresholdValueBean> m_thresholdList;
	private List<ThresholdValueBean> m_thresholdValueList;
	
	@PostConstruct
	private void init(){
		m_nodeParamsChoiceList = new ArrayList<NodeModelDetailBean>();
		m_thresholdList = new ArrayList<ThresholdValueBean>();
		m_thresholdValueList = new ArrayList<ThresholdValueBean>();
		m_allTask = new ArrayList<TaskBean>();
		m_allTask = m_systemSettingEJB.listAllTask();
		
		queryRecordList();
	}
	
	private void queryRecordList(){
		log.info("[queryRecordList] get into queryRecordList");
		m_thresholdValueList = m_systemSettingEJB.getThresholdValueList(m_queryBean);		
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
	
	public void getSettingParam(ValueChangeEvent event){
		Long task_id = (Long) event.getNewValue();
		log.info("[getSettingParam] task_id = " + task_id);
		setM_nodeParamsList(new ArrayList<NodeModelDetailBean>());
		if(null != task_id){
			m_nodeParamsList = m_systemSettingEJB.listNodesByTaskId(task_id);
		}
	}

	public void getNodeChoiceParamList(ValueChangeEvent event){
		Long query_id = (Long) event.getNewValue();
		log.info("[getNodeChoiceParamList] task_id = " + task_id);
		log.info("[getNodeChoiceParamList] query_id = " + query_id);
		if(null != query_id){
			int rowId = 1;
			if(null != m_nodeParamsChoiceList && 0 != m_nodeParamsChoiceList.size()){
				rowId = m_nodeParamsChoiceList.size() + 1;
				for(NodeModelDetailBean bean : m_nodeParamsChoiceList){
					if(bean.getId().equals(query_id)){
						return;
					}
				}
			}
			NodeModelDetailBean bean = m_systemSettingEJB.listNodeByNodeId(query_id, rowId);
			m_nodeParamsChoiceList.add(bean);
		}
	}
	
	public String setTempAlarmThreshold(){
		if(null != m_thresholdBean){
			Long node_id = Long.parseLong(getRequestParamMap().get("window_node_id"));
			log.info("[setTempAlarmThreshold] set temp node_id = " + node_id);
			Node node = m_systemSettingEJB.findNodeByNodeId(node_id);
			int rowId = 1;
			if(null != m_thresholdList && 0 != m_thresholdList.size()){
				rowId = m_thresholdList.size() + 1;
				for(ThresholdValueBean bean : m_thresholdList){
					if(bean.getNodeModel().getM_id().equals(node_id)){
						bean.setMin_value(m_thresholdBean.getMin_value());
						bean.setMax_value(m_thresholdBean.getMax_value());
						bean.setValue_type(node.getValue_type());
						return "";
					}
				}
			}
			
			ThresholdValueBean thresholdBean = new ThresholdValueBean();
			thresholdBean.setM_rowId(rowId);
			thresholdBean.setNodeModel(node);
			thresholdBean.setMin_value(m_thresholdBean.getMin_value());
			thresholdBean.setMax_value(m_thresholdBean.getMax_value());
			thresholdBean.setValue_type(node.getValue_type());
			
			m_thresholdList.add(thresholdBean);
		}
		return "";
	}
	
	public String setThresholdEnum(){
		Long node_id = Long.parseLong(getRequestParamMap().get("node_id"));
		log.info("[setThresholdEnum] set enum node_id = " + node_id);
		Node node = m_systemSettingEJB.findNodeByNodeId(node_id);
		if(null != node){
			int rowId = 1;
			if(null != m_thresholdList && 0 != m_thresholdList.size()){
				rowId = m_thresholdList.size() + 1;
				for(ThresholdValueBean bean : m_thresholdList){
					if(bean.getNodeModel().getM_id().equals(node_id)){
						bean.setValue(node.getOther_value());
						bean.setValue_type(node.getValue_type());
						return "";
					}
				}
			}
			ThresholdValueBean thresholdBean = new ThresholdValueBean();
			thresholdBean.setM_rowId(rowId);
			thresholdBean.setNodeModel(node);
			thresholdBean.setValue(node.getOther_value());
			thresholdBean.setValue_type(node.getValue_type());
			
			m_thresholdList.add(thresholdBean);
		}
		return "";
	}
	
	public void removeThreshold(){
		Long node_id = Long.parseLong(getRequestParamMap().get("node_id"));
		log.info("[removeThreshold] node_id = " + node_id);
		List<ThresholdValueBean> tempList = new ArrayList<ThresholdValueBean>();
		if(null != m_thresholdList && 0 != m_thresholdList.size()){
			for(ThresholdValueBean bean : m_thresholdList){
				if(bean.getNodeModel().getM_id().equals(node_id)){
					tempList.add(bean);
				}
			}
		}
		m_thresholdList.removeAll(tempList);
		
		int row_id = 1;
		if(null != m_thresholdList && 0 != m_thresholdList.size()){
			for(ThresholdValueBean bean : m_thresholdList){
				bean.setM_rowId(row_id++);
			}
		}
	}
	
	public String setAlarmThreshold(){
		if(null != m_thresholdList && 0 != m_thresholdList.size()){
			log.info("[setAlarmThreshold] m_thresholdList.size() = " + m_thresholdList.size());
			ThresholdValueResultCode resultCode = m_systemSettingEJB.setAlarmThreshold(m_thresholdList);
			if(resultCode.toLocalString().equals("SET_THRESHOLDVALUE_SUCCESS")){
				return "my_settings.jsf";
			}
		}
		return "";
	}
	
	public ThresholdValueResultCode deleteThresholdValueSet(){
		Long thresholdvalue_id = Long.parseLong(getRequestParamMap().get("thresholdvalue_id"));
		log.info("[deleteThresholdValueSet] thresholdvalue_id = " + thresholdvalue_id);
		ThresholdValueResultCode result = m_systemSettingEJB.deleteThresholdvalue(thresholdvalue_id);
		queryRecordList();
		return result;
	}
	
	private Map<String, String> getRequestParamMap() {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	}
	
	public List<NodeModelDetailBean> getM_nodeParamsChoiceList() {
		return m_nodeParamsChoiceList;
	}

	public void setM_nodeParamsChoiceList(
			List<NodeModelDetailBean> m_nodeParamsChoiceList) {
		this.m_nodeParamsChoiceList = m_nodeParamsChoiceList;
	}
	
	public ThresholdValueBean getM_thresholdBean() {
		return m_thresholdBean;
	}

	public void setM_thresholdBean(ThresholdValueBean m_thresholdBean) {
		this.m_thresholdBean = m_thresholdBean;
	}

	public Long getTask_id() {
		return task_id;
	}

	public void setTask_id(Long task_id) {
		this.task_id = task_id;
	}

	public List<NodeModelDetailBean> getM_nodeParamsList() {
		return m_nodeParamsList;
	}

	public void setM_nodeParamsList(List<NodeModelDetailBean> m_nodeParamsList) {
		this.m_nodeParamsList = m_nodeParamsList;
	}

	public List<ThresholdValueBean> getM_thresholdList() {
		return m_thresholdList;
	}

	public void setM_thresholdList(List<ThresholdValueBean> m_thresholdList) {
		this.m_thresholdList = m_thresholdList;
	}

	public List<TaskBean> getM_allTask() {
		return m_allTask;
	}

	public void setM_allTask(List<TaskBean> m_allTask) {
		this.m_allTask = m_allTask;
	}

	public String getM_message() {
		return m_message;
	}

	public void setM_message(String m_message) {
		this.m_message = m_message;
	}
	
	public ThresholdValueQueryBean getM_queryBean() {
		return m_queryBean;
	}

	public void setM_queryBean(ThresholdValueQueryBean m_queryBean) {
		this.m_queryBean = m_queryBean;
	}

	public List<ThresholdValueBean> getM_thresholdValueList() {
		return m_thresholdValueList;
	}

	public void setM_thresholdValueList(List<ThresholdValueBean> m_thresholdValueList) {
		this.m_thresholdValueList = m_thresholdValueList;
	}

}
