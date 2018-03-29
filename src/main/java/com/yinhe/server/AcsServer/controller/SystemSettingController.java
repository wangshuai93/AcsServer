package com.yinhe.server.AcsServer.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import com.yinhe.server.AcsServer.backbean.NodeModelDetailBean;
import com.yinhe.server.AcsServer.backbean.NodeModelQueryBean;
import com.yinhe.server.AcsServer.backbean.TaskBean;
import com.yinhe.server.AcsServer.ejb.SystemSettingEJB;
import com.yinhe.server.AcsServer.enums.SystemOperateResultCode;
import com.yinhe.server.AcsServer.util.Resources;

@Named
@RequestScoped
public class SystemSettingController {
	@Inject 
	private Logger log;
	@Inject
	private NodeModelDetailBean m_param;
	private List<NodeModelDetailBean> m_nodeModelList;
	private List<NodeModelDetailBean> m_nodeListForTask;
	private List<NodeModelDetailBean> m_paramSet;
	private List<NodeModelDetailBean> m_paramGet;
	private List<TaskBean> m_allTask;
	@Inject
	private SystemSettingEJB m_systemSettingEJB;
	@Inject
	private NodeModelQueryBean m_queryBean;
	private String m_message;
	private static final String m_filePath = System.getProperty("jboss.home.dir")+"/welcome-content/datamodel/";
	private Long task_id;
	private Map<String, Object> session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	
	private String m_importMessage;
	
	@PostConstruct
	private void init(){
		log.info("[init] SystemSettingController init");
		//log.info("m_filePath = " + m_filePath);
		m_nodeModelList = new ArrayList<NodeModelDetailBean>();	
		m_allTask = new ArrayList<TaskBean>();
		m_nodeListForTask = new ArrayList<NodeModelDetailBean>();	
		m_paramSet = new ArrayList<NodeModelDetailBean>();
		m_paramGet = new ArrayList<NodeModelDetailBean>();
		m_allTask = m_systemSettingEJB.listAllTask();
		
		Long node_id = (Long)session.get("modify_node_id");
		if(node_id != null){
			m_param = m_systemSettingEJB.findParamById(node_id);
		}
		
		String o = getRequestParamMap().get("load");
		if (o != null && "lazy".equals(o)) {
			log.info("[init] SystemSettingController load: lazy!");
		} else {	
			if(m_allTask != null && m_allTask.size() > 0){
				m_nodeListForTask = m_systemSettingEJB.listNodesByTaskId(m_allTask.get(0).getM_id());
				m_paramSet = m_systemSettingEJB.listParamSet(m_allTask.get(0).getM_id());
				m_paramGet = m_systemSettingEJB.listParamGet(m_allTask.get(0).getM_id());
			}
			queryRecordList();
		}
	}
	
	private void queryRecordList(){
		log.info("[queryRecordList] get into queryRecordList");
		m_nodeModelList = m_systemSettingEJB.listNodes(m_queryBean);		
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
	
	/**
	 * 添加参数和任务的关联关系
	 */
	public void addRelevance(){
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		String[] strIdArray = context.getRequestParameterValuesMap().get("select_item");
		if (null == strIdArray || strIdArray.length == 0) {
			m_importMessage = SystemOperateResultCode.ADD_NODE_TASK_NULL.toLocalString();
			return;
		}
		List<Long> idList = new ArrayList<Long>();
		try {
			for (String str : strIdArray) {
				idList.add(Long.parseLong(str));
			}
		} catch (Exception e) {
			m_importMessage = SystemOperateResultCode.ADD_NODE_TASK_NULL.toLocalString();
			return;
		}
		m_queryBean.setIdList(idList);
		SystemOperateResultCode result_code = m_systemSettingEJB.addNodeTask(task_id,idList);

		if (null == result_code) {
			m_importMessage = SystemOperateResultCode.ADD_NODE_TASK_ERROR.toLocalString();
		} else {
			m_importMessage = result_code.toLocalString();
		}		
	}
	
	/**
	 * 下拉框值改变事件
	 * @param event
	 */
	public void findParamByTask(ValueChangeEvent event){
		log.info("[findParamByTask] get into findParamByTask");
		Long id = (Long)event.getNewValue();
		session.put("task_id", id);	
		log.info("[findParamByTask] task id = " + id);
		m_nodeListForTask = m_systemSettingEJB.listNodesByTaskId(id);
		m_paramSet = m_systemSettingEJB.listParamSet(id);
		m_paramGet = m_systemSettingEJB.listParamGet(id);
	}		
	
	/**
	 * 删除任务参数关联关系
	 * @return
	 */
	public String deleteTaskNode(){
		Long node_id = Long.parseLong(getRequestParamMap().get("node_id"));
		log.info("[deleteTaskNode] node_id = " + node_id);
		SystemOperateResultCode resultCode = m_systemSettingEJB.deleteNodeForTask(node_id,task_id);
		log.info("[deleteTaskNode] resultCode = " + resultCode);
		
		if (null == resultCode) {
			m_message = SystemOperateResultCode.DELETE_NODE_FOR_TASK_FAILED.toLocalString();
		} else {
			m_message = resultCode.toLocalString();
			m_nodeListForTask = m_systemSettingEJB.listNodesByTaskId(task_id);
			m_paramSet = m_systemSettingEJB.listParamSet(task_id);
			m_paramGet = m_systemSettingEJB.listParamGet(task_id);
			return "";
		}
		return "";
	}
	
	/**
	 * 添加任务下发参数
	 */
	public void addParamSet(){
		log.info("[addParamSet] get into addParamSet");
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		String[] strIdArray = context.getRequestParameterValuesMap().get("select_item");
		if (null == strIdArray || strIdArray.length == 0) {
			m_message = SystemOperateResultCode.ADD_PARAM_SET_NULL.toLocalString();
			return;
		}
		List<Long> idList = new ArrayList<Long>();
		try {
			for (String str : strIdArray) {
				idList.add(Long.parseLong(str));
			}
		} catch (Exception e) {
			m_message = SystemOperateResultCode.ADD_PARAM_SET_NULL.toLocalString();
			return;
		}
		m_queryBean.setIdList(idList);
		SystemOperateResultCode result_code = m_systemSettingEJB.addParamSet(task_id,idList);

		if (null == result_code) {
			m_message = SystemOperateResultCode.ADD_PARAM_SET_NULL.toLocalString();
		} else {
			m_message = result_code.toLocalString();
			m_paramSet = m_systemSettingEJB.listParamSet(task_id);
		}		
	}
	
	/**
	 * 添加任务上报参数
	 */
	public void addParamGet(){
		log.info("[addParamGet] get into addParamGet");
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		String[] strIdArray = context.getRequestParameterValuesMap().get("select_item");
		if (null == strIdArray || strIdArray.length == 0) {
			m_message = SystemOperateResultCode.ADD_PARAM_GET_NULL.toLocalString();
			return;
		}
		List<Long> idList = new ArrayList<Long>();
		try {
			for (String str : strIdArray) {
				idList.add(Long.parseLong(str));
			}
		} catch (Exception e) {
			m_message = SystemOperateResultCode.ADD_PARAM_GET_NULL.toLocalString();
			return;
		}
		m_queryBean.setIdList(idList);
		SystemOperateResultCode result_code = m_systemSettingEJB.addParamGet(task_id,idList);

		if (null == result_code) {
			m_message = SystemOperateResultCode.ADD_PARAM_GET_NULL.toLocalString();
		} else {
			m_message = result_code.toLocalString();
			m_paramGet = m_systemSettingEJB.listParamGet(task_id);
		}	
	}
	
	/**
	 * 删除任务下发参数
	 */
	public void deleteParamSet(){
		Long node_id = Long.parseLong(getRequestParamMap().get("node_id"));
		Long task_id = getTaskId();
		SystemOperateResultCode resultCode = m_systemSettingEJB.deleteParamSet(node_id,task_id);
		
		if (null == resultCode) {
			m_message = SystemOperateResultCode.DELETE_PARAM_SET_FAILED.toLocalString();
		} else {
			m_message = resultCode.toLocalString();
			m_paramSet = m_systemSettingEJB.listParamSet(task_id);
		}
	}
	
	/**
	 * 删除任务上报参数
	 */
	public void deleteParamGet(){
		Long node_id = Long.parseLong(getRequestParamMap().get("node_id"));
		Long task_id = getTaskId();
        SystemOperateResultCode resultCode = m_systemSettingEJB.deleteParamGet(node_id,task_id);
		
		if (null == resultCode) {
			m_message = SystemOperateResultCode.DELETE_PARAM_GET_FAILED.toLocalString();
		} else {
			m_message = resultCode.toLocalString();
			m_paramGet = m_systemSettingEJB.listParamGet(task_id);
		}
	}
	
	public void uploadListener(FileUploadEvent event){
		log.info("[uploadListener] get into uploadListener!");
        UploadedFile uploadedFile = event.getUploadedFile();    
        File destinationFile = null;
        InputStream uploadFileStream = null;       
        try {
            uploadFileStream = uploadedFile.getInputStream();
            File tmpDirectory = new File(m_filePath);        
            // 新建暂存目录
            if (!tmpDirectory.exists()) {
                tmpDirectory.mkdirs();
            }        
            //inputStream写入文件
            destinationFile = new File(tmpDirectory, new String(uploadedFile.getName().getBytes("ISO_8859_1"), "UTF-8"));
            log.info("[uploadListener] destinationFile name: " + new String(uploadedFile.getName().getBytes("ISO_8859_1"), "UTF-8"));
            log.info("[uploadListener] destinationFile path: " + destinationFile.getAbsolutePath());
            Files.copy(uploadFileStream, destinationFile.toPath(),StandardCopyOption.REPLACE_EXISTING);  
        } catch (IOException e) {
        	log.info("[uploadListener] uploadFile Excepiton!"); 
        } finally {           
            try {
                uploadFileStream.close();               
            } catch (IOException e) {
            	log.info("[uploadListener] close uploadFileStream Exception!");
            }     
        }		
	
	}
	
	public String gotoModifyParam(){
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		String[] idArray = context.getRequestParameterValuesMap().get("node_id");
		if (idArray == null || idArray.length == 0) {
			return null;
		}
		Long node_id;
		try {
			node_id = Long.parseLong(idArray[0]);
			log.info("[gotoModifyParam] node_id = " + node_id);
		} catch (Exception e) {
			return null;
		}		
		session.put("modify_node_id", node_id);	
		m_param = m_systemSettingEJB.findParamById(node_id);
		return "modify_node.jsf";
	}
	
	public String modifyParam(){
		m_message = "";
		if(Resources.isNullOrEmpty(m_param.getValue_type())){
			m_message = SystemOperateResultCode.PARAM_VALUE_TYPE_NULL.toLocalString();
			return "";
		}
		if(m_param.getValue_type().equals("basic")){
			if(Resources.isNullOrEmpty(m_param.getMax_value()) || Resources.isNullOrEmpty(m_param.getMin_value())){
				m_message = SystemOperateResultCode.PARAM_MAXORMIN_NULL.toLocalString();
				return "";
			}
			Long max_value = Long.parseLong(m_param.getMax_value());
			Long min_value = Long.parseLong(m_param.getMin_value());
			log.info("[modifyParam] max = " + max_value + ",min = " + min_value );
			if(max_value < min_value){
				m_message = SystemOperateResultCode.PARAM_MAX_MIN_ERROR.toLocalString();
				return "";
			}
		}else if(m_param.getValue_type().equals("enum")){
			if(Resources.isNullOrEmpty(m_param.getOther_value())){
				m_message = SystemOperateResultCode.PARAM_OTHER_VALUE_NULL.toLocalString();
				return "";
			}
		}
		Long node_id = (Long)session.get("modify_node_id");
		if(node_id != null){
			m_param.setId(node_id);
			SystemOperateResultCode resultCode = m_systemSettingEJB.updateParam(m_param);
			m_message = resultCode.toLocalString();
		}
		return "";
	}
	
	public String modifyParamNameZh(){
		m_message = "";
		if(Resources.isNullOrEmpty(m_param.getNameZh())){
			m_message = SystemOperateResultCode.UPDATE_PARAM_NULL.toLocalString();
			return "";
		}
		Long node_id = (Long)session.get("modify_node_id");
		if(node_id != null){
			m_param.setId(node_id);
			SystemOperateResultCode resultCode = m_systemSettingEJB.updateParamNameZh(m_param);
			m_message = resultCode.toLocalString();
		}
		return "";
	}
	
	public String gotoAllParam(){
		return "node_setting2.jsf";
	}

	public List<NodeModelDetailBean> getM_nodeModelList() {
		return m_nodeModelList;
	}

	public void setM_nodeModelList(List<NodeModelDetailBean> m_nodeModelList) {
		this.m_nodeModelList = m_nodeModelList;
	}	

	public NodeModelQueryBean getM_queryBean() {
		return m_queryBean;
	}

	public void setM_queryBean(NodeModelQueryBean m_queryBean) {
		this.m_queryBean = m_queryBean;
	}

	public String getM_message() {
		return m_message;
	}

	public void setM_message(String m_message) {
		this.m_message = m_message;
	}

	public List<TaskBean> getM_allTask() {
		return m_allTask;
	}

	public void setM_allTask(List<TaskBean> m_allTask) {
		this.m_allTask = m_allTask;
	}

	public Long getTask_id() {
		return task_id;
	}

	public void setTask_id(Long task_id) {
		this.task_id = task_id;
	}

	public List<NodeModelDetailBean> getM_nodeListForTask() {
		return m_nodeListForTask;
	}

	public void setM_nodeListForTask(List<NodeModelDetailBean> m_nodeListForTask) {
		this.m_nodeListForTask = m_nodeListForTask;
	}
	
	public List<NodeModelDetailBean> getM_paramSet() {
		return m_paramSet;
	}

	public void setM_paramSet(List<NodeModelDetailBean> m_paramSet) {
		this.m_paramSet = m_paramSet;
	}

	public List<NodeModelDetailBean> getM_paramGet() {
		return m_paramGet;
	}

	public void setM_paramGet(List<NodeModelDetailBean> m_paramGet) {
		this.m_paramGet = m_paramGet;
	}

	private Map<String, String> getRequestParamMap() {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	}
	
	private Long getTaskId(){
		Object taskId = session.get("task_id");
		Long task_id;
		if(taskId != null) {
			task_id = (Long)taskId;
			log.info("[getTaskId] task_id = " + task_id);
			return task_id;			
		} 	
		return null;
	}
	
	public NodeModelDetailBean getM_param() {
		return m_param;
	}
	public void setM_param(NodeModelDetailBean m_param) {
		this.m_param = m_param;
	}

	@PreDestroy
	private void destroy() {
		log.info("[destroy] SystemSettingController record will be destoryed!\n");
	}

	public String getM_importMessage() {
		return m_importMessage;
	}

	public void setM_importMessage(String m_importMessage) {
		this.m_importMessage = m_importMessage;
	}
}
