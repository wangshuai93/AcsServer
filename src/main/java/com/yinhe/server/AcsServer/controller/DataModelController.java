package com.yinhe.server.AcsServer.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.TreeNode;

import com.yinhe.server.AcsServer.backbean.CPEBean;
import com.yinhe.server.AcsServer.backbean.NodeModelDetailBean;
import com.yinhe.server.AcsServer.backbean.NodeModelQueryBean;
import com.yinhe.server.AcsServer.backbean.RPCParameterBean;
import com.yinhe.server.AcsServer.backbean.SetParameterAttributesBean;
import com.yinhe.server.AcsServer.backbean.TaskBean;
import com.yinhe.server.AcsServer.ejb.SystemSettingEJB;
import com.yinhe.server.AcsServer.ejb.TaskHandler;
import com.yinhe.server.AcsServer.ejb.TreeNodeEJB;
import com.yinhe.server.AcsServer.enums.SystemOperateResultCode;
import com.yinhe.server.AcsServer.util.Document;
import com.yinhe.server.AcsServer.util.Resources;

@Named
@RequestScoped
public class DataModelController{
	@Inject
	private Logger log;
	@Inject
	private CPEBean m_cpeBean;
	private Map<String, Object> session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	private String m_dataModelMessage;
	private TreeNode root;
	private TreeNode selectedNode;
	@Inject 
	private TreeNodeEJB m_treeNodeEJB;
	private NodeModelDetailBean m_nodeDetailBean;
	private List<RPCParameterBean> param_list;
	@Inject
    private	RPCParameterBean rpc_parameterBean;
	@Inject
	private SetParameterAttributesBean m_setParameterAttributesBean;
	@Inject
	private TaskHandler m_taskHandler;
	private String rpc_result;
	//new add
	private String m_message;
	private Long m_taskId;
	@Inject
	private NodeModelQueryBean m_queryBean;
	@Inject
	private SystemSettingEJB m_systemSettingEJB;
	
	//checkbox tree
	private TreeNode root2;
	private TreeNode[] selectedNodes2;
	private List<TaskBean> m_allTask;
	
	@PostConstruct
	public void init(){	
		param_list = new ArrayList<RPCParameterBean>();
		m_allTask = new ArrayList<TaskBean>();
		String serialNumber = getSerialNumber();
		if(!Resources.isNullOrEmpty(serialNumber)) {
			log.info("init DataModelController,serial_number = " + serialNumber);
		} 
		
		m_allTask = m_systemSettingEJB.listAllTask();
		root = m_treeNodeEJB.createTree();	
		root2 = m_treeNodeEJB.createCheckboxTree();
		
		String o = getRequestParamMap().get("load");
		String t = getRequestParamMap().get("load_tree");
		if (t != null && "lazy".equals(t)) {			
			log.info("[init] DataModelController load_tree: lazy!");			
		} else{
			log.info("[init] DataModelController load_tree != lazy!");			
		}		
		if (o != null && "lazy".equals(o)) {			
			log.info("[init] DataModelController load: lazy!");			
		} else {
			getNodeDetailBean();
			log.info("[init]DataModelController load != lazy!");
		}
	}
	public void getNodeDetailBean(){
		Object path = session.get("path");
		String pp = "";
		if(path != null) {
			pp = (String)path;
			log.info("[getNodeDetailBean] path = " + pp);
		} 
		m_nodeDetailBean = m_treeNodeEJB.findByPath(pp);	
	}
   
	public void onNodeExpand(NodeExpandEvent event){
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,"Expanded",event.getTreeNode().toString());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public void onNodeCollapse(NodeCollapseEvent event){
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,"Collapsed",event.getTreeNode().toString());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public void onNodeSelect(NodeSelectEvent event){
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,"Selected",event.getTreeNode().toString());
		FacesContext.getCurrentInstance().addMessage(null, message);	
		String path = "";
		path = event.getTreeNode().toString();
		TreeNode node = event.getTreeNode();
		while(node.getParent() != null){
			node = node.getParent();
			path = node + path;
		}
		path = path.substring(4);	
		String[] temp = path.split("\\..");
		String path2 = "";
		int len = temp.length;
		for(int i = 0; i < len - 1; i++){
			path2 += temp[i] + ".";			
		}
		int index = temp[len - 1].lastIndexOf(".");
		if(index > 0){
			path2 += temp[len - 1].substring(0,index);
		}else{
			path2 += temp[len - 1];
		}
		log.info("[onNodeSelect] path = " + path2 );
		session.put("path", path2);
		m_nodeDetailBean = m_treeNodeEJB.findByPath(path2);	
		session.put("type", m_nodeDetailBean.getType());
	}
	
	public void onNodeUnselect(NodeUnselectEvent event){
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,"Unselected",event.getTreeNode().toString());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}			
		
	public void excuteRPC(){
		log.info("[excuteRPC] get into excuteRPC");
		String serialNumber = this.getSerialNumber();
		this.setM_cpeBean(m_taskHandler.setCPEBean(serialNumber));
		Object path = session.get("path");
		String pp = "";
		if(path != null) {
			pp = (String)path;
			Object type = session.get("type");
			String tt = "";
			if(tt != null) {
				tt = (String)type;
				rpc_parameterBean.setType(tt);
				if(tt.equals("node")){
					pp += ".";
				}
			}
			rpc_parameterBean.setParam_name(pp);
			log.info("[excuteRPC] method_name = " + rpc_parameterBean.getMethod_name() + ",param_name = " + rpc_parameterBean.getParam_name());
			rpc_result = m_taskHandler.executeRPCMethod(m_cpeBean,rpc_parameterBean);
		}else{
			rpc_result = "参数路径为空！";
		}
	}
	
	public void addParam(){
		Object path_obj = session.get("path");
		String path = "";
		if(path_obj != null) {
			path = (String)path_obj;
			log.info("[addParam] path = " + path);
		} 
		log.info("[addParam] value_set = " + rpc_parameterBean.getValue_set());		
		rpc_parameterBean.setParam_name(path);		
		param_list.add(rpc_parameterBean);
	}
	
	public String getSerialNumber(){
		Object taskId = session.get("serial_number");
		String serialNumber = "";
		if(taskId != null) {
			serialNumber = (String)taskId;
			log.info("[getSerialNumber] serial_number = " + serialNumber);			
		} 
		return serialNumber;
	}
	
	public void displaySelectedMultiple(TreeNode[] nodes){
		if(nodes != null && nodes.length > 0) {
			List<Long> idList = new ArrayList<Long>();
			try {
				for(TreeNode node : nodes) {
	            	Document doc = (Document)node.getData();
	            	log.info("[displaySelectedMultiple] doc.getName = " + doc.getName());
	            	log.info("[displaySelectedMultiple] doc.getId = " + doc.getId());
	            	log.info("[displaySelectedMultiple] doc.getType = " + doc.getType());
	            	if(!doc.getType().equals("node")){
	            		idList.add(doc.getId());
	            	}
	            }
			} catch (Exception e) {
				m_message = SystemOperateResultCode.ADD_NODE_TASK_NULL.toLocalString();
				showMessage(m_message);
				return;
			}
            m_queryBean.setIdList(idList);
            SystemOperateResultCode result_code = m_systemSettingEJB.addNodeTask(m_taskId,idList);

    		if (null == result_code) {
    			m_message = SystemOperateResultCode.ADD_NODE_TASK_ERROR.toLocalString();
    		} else {
    			m_message = result_code.toLocalString();
    		}		
            log.info("[displaySelectedMultiple] m_message = " + m_message);
        }else{
        	 m_message = SystemOperateResultCode.ADD_NODE_TASK_NULL.toLocalString();
        }
		showMessage(m_message);
	}
	
	public String displaySelectedOne(TreeNode[] nodes){
		if(nodes != null && nodes.length > 0){
			if(nodes.length > 1){
				m_message = SystemOperateResultCode.UPDATE_PARAM_ONE.toLocalString();
				showMessage(m_message);
				return "";
			}else{
				Long node_id = null;
				for(TreeNode node : nodes) {
	            	Document doc  = (Document)node.getData();
	            	node_id = doc.getId();
	            	log.info("[displaySelectedOne] doc.getName = " + doc.getName());
	            	log.info("[displaySelectedOne] doc.getId = " + doc.getId());
	            }
				session.put("modify_node_id", node_id);	
				m_nodeDetailBean = m_systemSettingEJB.findParamById(node_id);
				this.redirect("/admin/modify_node.jsf");
				return "";
			}
		}else{
			m_message = SystemOperateResultCode.UPDATE_PARAM_NULL.toLocalString();
			showMessage(m_message);
			return "";
		}
	}
	
	public void showMessage(String msgs) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msgs, "--------------------------提示--------------------------");
	    RequestContext.getCurrentInstance().showMessageInDialog(message);
	}
	
	public void viewCarsCustomized() {
		Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("width", 640);
        options.put("height", 340);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        options.put("headerElement", "customheader");
         
        RequestContext.getCurrentInstance().openDialog("viewCars", options, null);
	}
	
	private void redirect(String path) {
		try {
			if ("void".equals(path)) {
				path = "/index.jsf";
			}
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(ec.getRequestContextPath() + path);
			log.info("[redirect] path = " + ec.getRequestContextPath()+ path);
		} catch (IOException e) {
			log.info("[redirect] redirect error!");
		}
	}
	
	public void checkRelevanceAdded(){
		this.redirect("/admin/task_parameter.jsf");
	}
	
	public String returnDeviceManage(){
		return "device_manage.jsf";
	}
	
	public String getM_dataModelMessage() {
		return m_dataModelMessage;
	}

	public void setM_dataModelMessage(String m_dataModelMessage) {
		this.m_dataModelMessage = m_dataModelMessage;
	}
	public TreeNode getRoot() {
		return root;
	}
	public void setRoot(TreeNode root) {
		this.root = root;
	}
	public TreeNode getSelectedNode() {
		return selectedNode;
	}
	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}		
	public NodeModelDetailBean getM_nodeDetailBean() {
		return m_nodeDetailBean;
	}
	public void setM_nodeDetailBean(NodeModelDetailBean m_nodeDetailBean) {
		this.m_nodeDetailBean = m_nodeDetailBean;
	}
	public RPCParameterBean getRpc_parameterBean() {
		return rpc_parameterBean;
	}
	public void setRpc_parameterBean(RPCParameterBean rpc_parameterBean) {
		this.rpc_parameterBean = rpc_parameterBean;
	}  
	
	public List<RPCParameterBean> getParam_list() {
		return param_list;
	}

	public void setParam_list(List<RPCParameterBean> param_list) {
		this.param_list = param_list;
	}
	
	private Map<String, String> getRequestParamMap() {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	}	
	
	public SetParameterAttributesBean getM_setParameterAttributesBean() {
		return m_setParameterAttributesBean;
	}

	public void setM_setParameterAttributesBean(
			SetParameterAttributesBean m_setParameterAttributesBean) {
		this.m_setParameterAttributesBean = m_setParameterAttributesBean;
	}	
	
	public CPEBean getM_cpeBean() {
		return m_cpeBean;
	}
	public void setM_cpeBean(CPEBean m_cpeBean) {
		this.m_cpeBean = m_cpeBean;
	}
	
	public String getRpc_result() {
		return rpc_result;
	}
	public void setRpc_result(String rpc_result) {
		this.rpc_result = rpc_result;
	}
	
	public TreeNode getRoot2() {
		return root2;
	}
	public void setRoot2(TreeNode root2) {
		this.root2 = root2;
	}
	public TreeNode[] getSelectedNodes2() {
		return selectedNodes2;
	}
	public void setSelectedNodes2(TreeNode[] selectedNodes2) {
		this.selectedNodes2 = selectedNodes2;
	}
	public String getM_message() {
		return m_message;
	}
	public void setM_message(String m_message) {
		this.m_message = m_message;
	}
	public Long getM_taskId() {
		return m_taskId;
	}
	public void setM_taskId(Long m_taskId) {
		this.m_taskId = m_taskId;
	}
	
	public NodeModelQueryBean getM_queryBean() {
		return m_queryBean;
	}
	public void setM_queryBean(NodeModelQueryBean m_queryBean) {
		this.m_queryBean = m_queryBean;
	}
	
	public List<TaskBean> getM_allTask() {
		return m_allTask;
	}
	public void setM_allTask(List<TaskBean> m_allTask) {
		this.m_allTask = m_allTask;
	}
	@PreDestroy
	private void destroy() {
		log.info("DataModelController record will be destoryed!\n");
	}
	
}
