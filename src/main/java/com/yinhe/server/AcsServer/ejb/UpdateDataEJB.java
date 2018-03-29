package com.yinhe.server.AcsServer.ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.yinhe.server.AcsServer.backbean.NodeModelDetailBean;
import com.yinhe.server.AcsServer.data.AlarmRespository;
import com.yinhe.server.AcsServer.data.NodeRespository;
import com.yinhe.server.AcsServer.data.NodeTaskRespository;
import com.yinhe.server.AcsServer.data.ThresholdValueRespository;
import com.yinhe.server.AcsServer.data.UpdateDataRecordRespository;
import com.yinhe.server.AcsServer.enums.ImportXmlResult;
import com.yinhe.server.AcsServer.model.Alarm;
import com.yinhe.server.AcsServer.model.Node;
import com.yinhe.server.AcsServer.model.NodeTask;
import com.yinhe.server.AcsServer.model.ThresholdValue;
import com.yinhe.server.AcsServer.model.UpdateDataRecord;
import com.yinhe.server.AcsServer.util.Resources;

@Stateless
public class UpdateDataEJB implements Serializable{
	
	private static final long serialVersionUID = -3658546476134737771L;
	
	@Inject
	private Logger m_log;
	@Inject
	private NodeRespository m_nodeRespository;
	@Inject
	private UpdateDataRecordRespository m_updateDataRecordRespository;
	@Inject
	private ThresholdValueRespository m_thresholdValueRespository;
	@Inject 
	private NodeTaskRespository m_nodeTaskRespository;
	@Inject
	private AlarmRespository m_alarmRespository;

	public void updateDetail(String fileName){
		List<UpdateDataRecord> recordList = new ArrayList<UpdateDataRecord>();
		if(!Resources.isNullOrEmpty(fileName)){
			//更改记录
			recordList = m_updateDataRecordRespository.findAllOrderedByIdDesc();
			boolean isExist = false;
			if(recordList != null && recordList.size() != 0){
				for(UpdateDataRecord record : recordList){
					if(record.getM_fileName().equals(fileName)){
						isExist = true;
						record.setM_isCurrentVersion(true);
						Date date = new Date();
						record.setM_updateTime(date);
					}else{
						record.setM_isCurrentVersion(false);
					}
					try {
						m_updateDataRecordRespository.updateEntity(record);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			//表中没有,增加记录
			if(!isExist){
				UpdateDataRecord updateDataRecord = new UpdateDataRecord();
				updateDataRecord.setM_fileName(fileName);
				updateDataRecord.setM_isCurrentVersion(true);
				Date date = new Date();
				updateDataRecord.setM_updateTime(date);
				try {
					m_updateDataRecordRespository.addEntity(updateDataRecord);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public UpdateDataRecord findByFileName(String fileName){
		if(!Resources.isNullOrEmpty(fileName)){
			UpdateDataRecord updateDataRecord = m_updateDataRecordRespository.findByFileName(fileName);
			return updateDataRecord;
		}
		return null;
	}
	
	public String importXml(List<NodeModelDetailBean> nodelist){
		if(null == nodelist || 0 == nodelist.size()){
			return ImportXmlResult.XML_IS_NULL.toLocalString();
		}
		List<Node> allTempNodeList = m_nodeRespository.listAll();
		m_log.info("[importXml] nodelist.size() = " + nodelist.size());
		m_log.info("[importXml] allTempNodeList.size() = " + allTempNodeList.size());
		if(null != allTempNodeList && 0 != allTempNodeList.size()){
			return doUpdate(nodelist, allTempNodeList);
		}else{
			m_log.info("[importXml] Node is null, add all");
			for(NodeModelDetailBean bean : nodelist){
				Node node = new Node();
				setParam(node, bean);
				try {
					m_nodeRespository.addNode(node);
				} catch (Exception e) {
					e.printStackTrace();
					return ImportXmlResult.IMPORT_XML_FAILED.toLocalString();
				}
			}
			return ImportXmlResult.IMPORT_XML_SUCCESS.toLocalString();
		}
	}
	
	public void setParam(Node node, NodeModelDetailBean bean){
		node.setAbbr_name(bean.getAbbr_name());
		node.setNode_path(bean.getNode_path());
		node.setDefault_value(bean.getDefault_value());
		node.setMin_value(bean.getMin_value());
		node.setMax_value(bean.getMax_value());
		node.setAcl(bean.getAcl());
		node.setNoc(bean.getNoc());
		node.setNocc(bean.getNocc());
		node.setRw(bean.getRw());
		node.setType(bean.getType());
		node.setIl(bean.getIl());
		node.setGetc(bean.getGetc());
		node.setOther_value(bean.getOther_value());
		node.setUnit(bean.getUnit());
		node.setValue_type(bean.getValue_type());
		node.setNin(bean.getNin());
		node.setNameZh(bean.getNameZh());
	}
	
	public boolean isNodeChanged(NodeModelDetailBean bean, Node node){
		if(bean.getNode_path().equals(node.getNode_path())){
			if(stringEquals(bean.getAbbr_name(), node.getAbbr_name())
					&& stringEquals(bean.getAcl(), node.getAcl())
					&& stringEquals(bean.getDefault_value(), node.getDefault_value())
					&& stringEquals(bean.getGetc(), node.getGetc())
					&& stringEquals(bean.getIl(), node.getIl())
					&& stringEquals(bean.getMax_value(), node.getMax_value())
					&& stringEquals(bean.getMin_value(), node.getMin_value())
					&& stringEquals(bean.getNoc(), node.getNoc())
					&& stringEquals(bean.getNocc(), node.getNocc())
					&& stringEquals(bean.getOther_value(), node.getOther_value())
					&& stringEquals(bean.getRw(), node.getRw())
					&& stringEquals(bean.getType(), node.getType())
					&& stringEquals(bean.getUnit(), node.getUnit())
					&& stringEquals(bean.getValue_type(), node.getValue_type())
					&& stringEquals(bean.getNin(), node.getNin())
					&& stringEquals(bean.getNameZh(), node.getNameZh())){
				return false;
			}else{
				return true;
			}
		}
		return false;
	}
	
	public boolean stringEquals(String str1, String str2) {
    	//str1 = null   str2 = ""
    	if (Resources.isNull(str1) && Resources.isNull(str2)) {
    		return true;
    	} else {
    		if(null != str1){
    			//str1 = null 空指针
    			return str1.equals(str2);
    		}else{
    			return str2.equals(str1);
    		}
		}
	}
	
	public String doUpdate(List<NodeModelDetailBean> nodelist, List<Node> allTempNodeList){
		List<NodeModelDetailBean> tempToAddList = new ArrayList<NodeModelDetailBean>();
		List<Node> tempToDeleteList = new ArrayList<Node>();
		List<NodeModelDetailBean> tempToUpdateList = new ArrayList<NodeModelDetailBean>();
		
		boolean doUpdate = false;
		boolean doAdd = false;
		boolean doDelete = false;
		
		boolean deleteNeedUpdateSettings = false;
		boolean updateNeedUpdateSettings = false;
		
		//add and update
		for(NodeModelDetailBean bean : nodelist){
			boolean flag = false;
			for(Node node : allTempNodeList){
				if(bean.getNode_path().equals(node.getNode_path())){
					flag = true;
					if(isNodeChanged(bean, node)){
						tempToUpdateList.add(bean);
					}
				}
			}
			if(!flag){
				m_log.info("[doUpdate]: need add bean.getNode_path() = " + bean.getNode_path());
				tempToAddList.add(bean);
			}
		}
		
		//delete
		for(Node node : allTempNodeList){
			boolean flag = false;
			for(NodeModelDetailBean bean : nodelist){
				if(bean.getNode_path().equals(node.getNode_path())){
					flag = true;
				}
			}
			if(!flag){
				tempToDeleteList.add(node);
			}
		}
		
		m_log.info("[doUpdate] tempToUpdateList = " + tempToUpdateList.size());
		m_log.info("[doUpdate] tempToAddList = " + tempToAddList.size());
		m_log.info("[doUpdate] tempToDeleteList = " + tempToDeleteList.size());
		
		//do Update
		if(null != tempToUpdateList && 0 != tempToUpdateList.size()){
			for(NodeModelDetailBean bean : tempToUpdateList){
				m_log.info("[doUpdate] need to update : " + bean.getNode_path());
				Node node = m_nodeRespository.findNodebyPath(bean.getNode_path());
				setParam(node, bean);
				doUpdate = true;
				
				ThresholdValue thresholdValue = m_thresholdValueRespository.findThresholdValueByNodeId(node.getM_id());
				NodeTask nodeTask = m_nodeTaskRespository.findNodeTaskByNodeId(node.getM_id());
				Alarm alarm = null;
				if(null != nodeTask){
					alarm = m_alarmRespository.findById(nodeTask.getM_id());
				}
				if(null != nodeTask || null != thresholdValue || null != alarm){
					updateNeedUpdateSettings = true;
				} 
				
				try {
					m_nodeRespository.updateNode(node);
				} catch (Exception e) {
					e.printStackTrace();
					return ImportXmlResult.IMPORT_XML_UPDATE_NODE_FAILED.toLocalString();
				}
			}
		}
		
		//do Add
		if(null != tempToAddList && 0 != tempToAddList.size()){
			for(NodeModelDetailBean bean : tempToAddList){
				m_log.info("[doUpdate] need to add  : " + bean.getNode_path());
				Node node = new Node();
				setParam(node, bean);
				doAdd = true;
				try {
					m_nodeRespository.addNode(node);
				} catch (Exception e) {
					e.printStackTrace();
					return ImportXmlResult.IMPORT_XML_ADD_NODE_FAILED.toLocalString();
				}
			}
		}
		
		//do Delete
		if(null != tempToDeleteList && 0 != tempToDeleteList.size()){
			for(Node node : tempToDeleteList){
				m_log.info("[doUpdate] need to delete  : " + node.getNode_path());
				doDelete = true;
				
				ThresholdValue thresholdValue = m_thresholdValueRespository.findThresholdValueByNodeId(node.getM_id());
				NodeTask nodeTask = m_nodeTaskRespository.findNodeTaskByNodeId(node.getM_id());
				Alarm alarm = null;
				if(null != nodeTask){
					alarm = m_alarmRespository.findById(nodeTask.getM_id());
				}
				if(null != nodeTask || null != thresholdValue || null != alarm){
					deleteNeedUpdateSettings = true;
				} 
				//删除与该节点相关的设置和告警
				try {
					m_nodeRespository.deleteNode(node);
				} catch (Exception e) {
					e.printStackTrace();
					return ImportXmlResult.IMPORT_XML_DELETE_NODE_FAILED.toLocalString();
				}
			}
		}
		
		boolean noNeedUpdateSetting = false;
		noNeedUpdateSetting = doUpdate || doAdd || doDelete;
		if(noNeedUpdateSetting){
			//更新和删除节点，涉及到设置的节点
			if(updateNeedUpdateSettings && !deleteNeedUpdateSettings){
				return ImportXmlResult.IMPORT_XML_UPDATE_NODE_NEED_UPDATE_SETTINGS_SUCCESS.toLocalString();
			}else if(!updateNeedUpdateSettings && deleteNeedUpdateSettings){
				return ImportXmlResult.IMPORT_XML_DELETE_NODE_NEED_UPDATE_SETTINGS_SUCCESS.toLocalString();
			}else if(updateNeedUpdateSettings && deleteNeedUpdateSettings){
				return ImportXmlResult.IMPORT_XML_DELETE_AND_UPDATE_NODE_NEED_UPDATE_SETTINGS_SUCCESS.toLocalString();
			}else{
				return ImportXmlResult.IMPORT_XML_UPDATE_NODE_SUCCESS.toLocalString();
			}
		}else{
			//只更新、添加、删除节点，没涉及到设置的节点
			return ImportXmlResult.NO_NEED_TO_UPDATE.toLocalString();
		}
	}
}
