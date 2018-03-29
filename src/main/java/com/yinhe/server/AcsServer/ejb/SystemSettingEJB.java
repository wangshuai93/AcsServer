package com.yinhe.server.AcsServer.ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.yinhe.server.AcsServer.backbean.DeviceTaskBean;
import com.yinhe.server.AcsServer.backbean.NodeModelDetailBean;
import com.yinhe.server.AcsServer.backbean.NodeModelQueryBean;
import com.yinhe.server.AcsServer.backbean.ParamSetBean;
import com.yinhe.server.AcsServer.backbean.TaskBean;
import com.yinhe.server.AcsServer.backbean.ThresholdValueBean;
import com.yinhe.server.AcsServer.backbean.ThresholdValueQueryBean;
import com.yinhe.server.AcsServer.data.DeviceTaskRespository;
import com.yinhe.server.AcsServer.data.NodeRespository;
import com.yinhe.server.AcsServer.data.NodeTaskRespository;
import com.yinhe.server.AcsServer.data.ParamGetRespository;
import com.yinhe.server.AcsServer.data.ParamSetRespository;
import com.yinhe.server.AcsServer.data.TaskRespository;
import com.yinhe.server.AcsServer.data.ThresholdValueRespository;
import com.yinhe.server.AcsServer.enums.SystemOperateResultCode;
import com.yinhe.server.AcsServer.enums.ThresholdValueResultCode;
import com.yinhe.server.AcsServer.model.DeviceTask;
import com.yinhe.server.AcsServer.model.Node;
import com.yinhe.server.AcsServer.model.NodeTask;
import com.yinhe.server.AcsServer.model.ParamGet;
import com.yinhe.server.AcsServer.model.ParamSet;
import com.yinhe.server.AcsServer.model.Task;
import com.yinhe.server.AcsServer.model.ThresholdValue;

@Stateless
public class SystemSettingEJB implements Serializable{
	
	private static final long serialVersionUID = 6659539718660366471L;
	@Inject
	private Logger m_log;
	@Inject
	private NodeRespository m_nodeRespository;
	@Inject
	private TaskRespository m_taskRespository;
	@Inject 
	private NodeTaskRespository m_nodeTaskRespository;
	@Inject
	private ThresholdValueRespository m_thresholdValueRespository;
	@Inject
	private ParamGetRespository m_paramGetRespository;
	@Inject
	private ParamSetRespository m_paramSetRespository;
	@Inject
	private DeviceTaskRespository m_deviceTaskRespository;
	
	public List<NodeModelDetailBean> listNodes(NodeModelQueryBean m_queryBean){
		m_log.info("[listNodes] get into listNodes!");
		m_queryBean.setMaxByCount(m_nodeRespository.getNodeSize());
		List<Node> nodeList = m_nodeRespository.listNodes(m_queryBean);
		if(nodeList == null || nodeList.size() == 0){
			m_log.info("[listNodes] nodeList.size() = 0");
			return null;
		}else{
			List<NodeModelDetailBean> nodeSettingList = new ArrayList<NodeModelDetailBean>();
			int rowId = 0;
			for(Node nm:nodeList){
				NodeModelDetailBean nodeModelDetailBean = new NodeModelDetailBean(nm);			
				nodeModelDetailBean.setM_rowId(++rowId);
				nodeSettingList.add(nodeModelDetailBean);
			}
			return nodeSettingList;
		}
	}
	
	public List<TaskBean> listAllTask(){
		m_log.info("[listAllTask] get into listAllTask!");
		List<Task> m_allTask = m_taskRespository.findAllOrderedByIdDesc();
		if(m_allTask == null || m_allTask.size() == 0){
			m_log.info("[listAllTask] m_allTask.size() = 0");
			return null;
		}else{
			int rowId = 0;
			List<TaskBean> m_taskList = new ArrayList<TaskBean>();
			for(Task t : m_allTask){
				TaskBean tb = new TaskBean();
				tb.setM_id(t.getM_id());
				//m_log.info("[listAllTask] name ="+t.getM_name());
		        if(t.getM_name().compareTo("PING_TASK")==0){
		        	tb.setCh_name("Ping任务");
		        }
		        if(t.getM_name().compareTo("TCP_TASK")==0){
		        	tb.setCh_name("TCP端口检测");
		        	
		        }
		        if(t.getM_name().compareTo("UDP_TASK")==0){
		        	tb.setCh_name("UDP端口检测");
		        	
		        }
		        if(t.getM_name().compareTo("HTTP_TASK")==0){
		        	tb.setCh_name("Http服务检测");
		        	
		        }
		        if(t.getM_name().compareTo("TRACEROUTE_TASK")==0){
		        	tb.setCh_name("Traceroute任务");
		        	
		        }
		        if(t.getM_name().compareTo("CONSTELLATION_TASK")==0){
		        	tb.setCh_name("星座图");
		        }
		        if(t.getM_name().compareTo("FULLBAND_TASK")==0){
		        	tb.setCh_name("全频搜索");
		        }
		        if(t.getM_name().compareTo("TUNER_TASK")==0){
		        	tb.setCh_name("单个频点检测");
		        }
		        if(t.getM_name().compareTo("RECORD_TASK")==0){
		        	tb.setCh_name("录制参数配置");
		        }
		        if(t.getM_name().compareTo("PERIOD_SETTING_TASK")==0){
		        	tb.setCh_name("周期任务时长配置");
		        }
		        if(t.getM_name().compareTo("UPLOAD_DATA")==0){
		        	tb.setCh_name("上传设备数据");
		        }
		        
		        
				tb.setM_name(t.getM_name());
				tb.setM_rowId(++rowId);
				m_taskList.add(tb);
			}
			return m_taskList;
		}
	}
	
	/**
	 *  添加参数与任务的关联关系
	 * @param task_id
	 * @param nodeId_list
	 * @return
	 */
	public SystemOperateResultCode addNodeTask(Long task_id,List<Long> nodeId_list){
		//TO-DO首先确定关联关系是否存在？？
		SystemOperateResultCode result = SystemOperateResultCode.ADD_NODE_TASK_ERROR;
		Task task = m_taskRespository.findById(task_id);
		if(task == null){
			return result;
		}else{		
			List<Node>  node_list = new ArrayList<Node>();
			List<NodeTask> nodeTask_list = new ArrayList<NodeTask>();
			node_list = m_nodeRespository.findNodesByIdList(nodeId_list);
			if(node_list == null || node_list.size() == 0){
				m_log.info("[addNodeTask] node_list.size = 0");
				return result;
			}else{
				int temp = 0;
				boolean flag = false;
				nodeTask_list = m_nodeTaskRespository.findAllOrderedByIdDesc();
				m_log.info("[addNodeTask] nodeTask_list.size = " + nodeTask_list.size());
				for(Node n : node_list){
					//覆盖式的添加任务与参数的关联
					flag = false;
					if(nodeTask_list != null && nodeTask_list.size() > 0){
						for(NodeTask nt : nodeTask_list){
							if(nt.getM_node() == n && nt.getM_task() == task){
								temp ++;
								flag = true;
							}else if(nt.getM_node() == n && nt.getM_task() != task){
								m_nodeTaskRespository.deleteEntity(nt);
							}
						}
					}
					if(!flag){
						m_log.info("[addNodeTask] nodeName =  " + n.getAbbr_name() + ",taskName = " + task.getM_name());
						NodeTask nodeTask = new NodeTask();
						nodeTask.setM_task(task);
						nodeTask.setM_node(n);
						m_nodeTaskRespository.addEntity(nodeTask);
					}
				}	
				
				if(temp > 0){
					result = SystemOperateResultCode.ADD_NODE_TASK_EXIST;
				}else{
					result = SystemOperateResultCode.ADD_NODE_TASK_SUCCESS;
				}
			}
			return result;
		}		
	}
	
	public List<NodeModelDetailBean> listNodesByTaskId(Long task_id){
		m_log.info("[listNodesByTaskId] task id = " + task_id);
		Task task = m_taskRespository.findById(task_id);
		if(task == null){
			return null;
		}
		
		List<NodeTask> nodeList = m_nodeTaskRespository.findNodesByTask(task);
		if(nodeList == null || nodeList.size() == 0){
			m_log.info("[listNodesByTaskId] nodeList.size() = 0");
			return null;
		}else{
			List<ParamGet> paramGet = m_paramGetRespository.findNodesByTask(task);
			List<ParamSet> paramSet = m_paramSetRespository.findNodesByTask(task);
			
			List<NodeModelDetailBean> nodeSettingList = new ArrayList<NodeModelDetailBean>();
			int rowId = 0;
			for(NodeTask nm:nodeList){
				NodeModelDetailBean nodeModelDetailBean = new NodeModelDetailBean(nm.getM_node());			
				nodeModelDetailBean.setM_rowId(++rowId);
				if(paramGet != null && paramGet.size() > 0){
					for(ParamGet pg : paramGet){
						if(pg.getM_nodeTask() == nm){
							nodeModelDetailBean.setParamSetting("上报");
							break;
						}
					}
				}
				if(paramSet != null && paramSet.size() > 0){
					for(ParamSet pg : paramSet){
						if(pg.getM_nodeTask() == nm){
							nodeModelDetailBean.setParamSetting("下发");
							break;
						}
					}
				}
				
				nodeSettingList.add(nodeModelDetailBean);
			}
			return nodeSettingList;
		}
	}
	
	public List<NodeModelDetailBean> listParamSet(Long task_id){
		m_log.info("[listParamSet] task id = " + task_id);
		Task task = m_taskRespository.findById(task_id);
		if(task == null){
			return null;
		}
		
		List<ParamSet> param_set_list = m_paramSetRespository.findNodesByTask(task);
		if(param_set_list == null || param_set_list.size() == 0){
			m_log.info("[listParamSet] param_set_List.size() = 0");
			return null;
		}else{
			List<NodeModelDetailBean> nodeSettingList = new ArrayList<NodeModelDetailBean>();
			int rowId = 0;
			for(ParamSet ps:param_set_list){
				NodeModelDetailBean nodeModelDetailBean = new NodeModelDetailBean(ps.getM_nodeTask().getM_node());			
				nodeModelDetailBean.setM_rowId(++rowId);
				nodeSettingList.add(nodeModelDetailBean);
			}
			return nodeSettingList;
		}
	}
	
	public List<ParamSetBean> listParamSetBean(Long task_id){
		m_log.info("[listParamSetBean] task id = " + task_id);
		Task task = m_taskRespository.findById(task_id);
		if(task == null){
			return null;
		}
		
		List<ParamSet> param_set_list = m_paramSetRespository.findNodesByTask(task);
		if(param_set_list == null || param_set_list.size() == 0){
			m_log.info("[listParamSetBean] param_set_List.size() = 0");
			return null;
		}else{
			m_log.info("[listParamSetBean] param_set_List.size() = " + param_set_list.size());
			List<ParamSetBean> paramSet_list = new ArrayList<ParamSetBean>();
			int rowId = 0;
			for(ParamSet ps:param_set_list){
				ParamSetBean nodeModelDetailBean = new ParamSetBean();	
				nodeModelDetailBean.setM_id(ps.getM_id());
				nodeModelDetailBean.setM_nodePath(ps.getM_nodeTask().getM_node().getNode_path());
				nodeModelDetailBean.setM_taskName(ps.getM_nodeTask().getM_task().getM_name());
				nodeModelDetailBean.setM_type(ps.getM_nodeTask().getM_node().getType());
				nodeModelDetailBean.setM_value(ps.getM_value());
				nodeModelDetailBean.setM_rowId(++rowId);
				nodeModelDetailBean.setM_namezh(ps.getM_nodeTask().getM_node().getNameZh());
				paramSet_list.add(nodeModelDetailBean);
			}
			return paramSet_list;
		}
	}
	
	public List<NodeModelDetailBean> listParamGet(Long task_id){
		m_log.info("[listParamGet] task id = " + task_id);
		Task task = m_taskRespository.findById(task_id);
		if(task == null){
			return null;
		}
		
		List<ParamGet> param_get_list = m_paramGetRespository.findNodesByTask(task);
		if(param_get_list == null || param_get_list.size() == 0){
			m_log.info("[listParamGet] nodeList.size() = 0");
			return null;
		}else{
			List<NodeModelDetailBean> nodeSettingList = new ArrayList<NodeModelDetailBean>();
			int rowId = 0;
			for(ParamGet pg:param_get_list){
				NodeModelDetailBean nodeModelDetailBean = new NodeModelDetailBean(pg.getM_nodeTask().getM_node());			
				nodeModelDetailBean.setM_rowId(++rowId);
				nodeSettingList.add(nodeModelDetailBean);
			}
			return nodeSettingList;
		}
		
	}
	
	public SystemOperateResultCode deleteNodeForTask(Long nodeId,Long taskId){
		SystemOperateResultCode resultCode = SystemOperateResultCode.DELETE_NODE_FOR_TASK_FAILED;
		m_log.info("[deleteNodeForTask] node id = " + nodeId +",task id = " + taskId);
		NodeTask nodetask = m_nodeTaskRespository.findNodesByTaskIdAndNodeId(nodeId,taskId);
		if(nodetask == null){
			m_log.info("[deleteNodeForTask] nodetask = null");
			resultCode = SystemOperateResultCode.DELETE_NODE_FOR_TASK_NULL;
			return resultCode;
		}else{
			try{
	        	m_nodeTaskRespository.deleteEntity(nodetask);			        						
			    resultCode = SystemOperateResultCode.DELETE_NODE_FOR_TASK_SUCCESS;
		    }catch(Exception ex){		    	
		    	resultCode = SystemOperateResultCode.DELETE_NODE_FOR_TASK_FAILED;
		     }
			return resultCode;
		}
	}
	
	public NodeModelDetailBean listNodeByNodeId(Long node_id, int rowId){
		Node node = m_nodeRespository.findById(node_id);
		if(node == null ){
			return null;
		}else{
			NodeModelDetailBean bean = new NodeModelDetailBean(node);
			bean.setM_rowId(rowId);
			return bean;
		}
	}
	
	public Node findNodeByNodeId(Long node_id){
		Node node = m_nodeRespository.findById(node_id);
		if(node == null ){
			return null;
		}else{
			return node;
		}
	}
	
	public ThresholdValueResultCode setAlarmThreshold(List<ThresholdValueBean> m_thresholdList){
		for(ThresholdValueBean bean : m_thresholdList){
			ThresholdValue thresholdValueTemp = m_thresholdValueRespository.findThresholdValueByNodeId(bean.getNodeModel().getM_id());
			if(null == thresholdValueTemp){
				ThresholdValue thresholdValue = new ThresholdValue();
				thresholdValue.setNodeModel(bean.getNodeModel());
				thresholdValue.setMin_value(bean.getMin_value());
				thresholdValue.setMax_value(bean.getMax_value());
				thresholdValue.setValue(bean.getValue());
				m_log.info("[setAlarmThreshold] add thresholdValue = " + thresholdValue.getNodeModel().getM_id());
				try {
					m_thresholdValueRespository.addThresholdValue(thresholdValue);
				} catch (Exception e) {
					e.printStackTrace();
					return  ThresholdValueResultCode.SET_THRESHOLDVALUE_FAILED;
				}
			} else {
				m_log.info("[setAlarmThreshold] update thresholdValueTemp = " + thresholdValueTemp.getNodeModel().getM_id());
				thresholdValueTemp.setNodeModel(bean.getNodeModel());
				thresholdValueTemp.setMin_value(bean.getMin_value());
				thresholdValueTemp.setMax_value(bean.getMax_value());
				thresholdValueTemp.setValue(bean.getValue());
				try {
					m_thresholdValueRespository.updateThresholdValue(thresholdValueTemp);
				} catch (Exception e) {
					e.printStackTrace();
					return  ThresholdValueResultCode.UPDATE_THRESHOLDVALUE_FAILED;
				}
			}
		}
		return ThresholdValueResultCode.SET_THRESHOLDVALUE_SUCCESS;
	}
	
	public List<ThresholdValueBean> getThresholdValueList(ThresholdValueQueryBean m_queryBean){
		m_queryBean.setMaxByCount(m_thresholdValueRespository.getThresholdValueSize());
		List<ThresholdValueBean> thresholdValueBeanList = new ArrayList<ThresholdValueBean>();
		List<ThresholdValue> thresholdValuesList = m_thresholdValueRespository.getAllThresholdValue(m_queryBean);
		if(null == thresholdValuesList){
			return null;
		} else {
			int row_id = 1;
			for(ThresholdValue threshold : thresholdValuesList){
				ThresholdValueBean bean = new ThresholdValueBean();
				bean.setM_rowId(row_id++);
				bean.setM_id(threshold.getM_id());
				bean.setValue(threshold.getValue());
				bean.setValue_type(threshold.getNodeModel().getValue_type());
				bean.setMin_value(threshold.getMin_value());
				bean.setMax_value(threshold.getMax_value());
				bean.setNodeModel(threshold.getNodeModel());
				thresholdValueBeanList.add(bean);
			}
			return thresholdValueBeanList;
		}
	}
	
	public ThresholdValueResultCode deleteThresholdvalue(Long id){
		ThresholdValue thresholdValue = m_thresholdValueRespository.findById(id);
		if(null == thresholdValue){
			return ThresholdValueResultCode.DELETE_THRESHOLDVALUE_FAILED;
		} else{
			try {
				m_thresholdValueRespository.deleteEntity(thresholdValue);
				return ThresholdValueResultCode.DELETE_THRESHOLDVALUE_SUCCESS;
			} catch (Exception e) {
				e.printStackTrace();
				return ThresholdValueResultCode.DELETE_THRESHOLDVALUE_FAILED;
			}
		}			
	}
	
	public List<NodeModelDetailBean> listNodesByTaskAndParamType(Long task_id,String param_type){
		m_log.info("[listNodesByTaskId] task id = " + task_id);
		Task task = m_taskRespository.findById(task_id);
		if(task == null){
			return null;
		}
		
		if(param_type.equals("param_set")){
			List<ParamSet> param_set_list = m_paramSetRespository.findNodesByTask(task);
			if(param_set_list == null || param_set_list.size() == 0){
				m_log.info("[listNodesByTaskId] param_set_List.size() = 0");
				return null;
			}else{
				List<NodeModelDetailBean> nodeSettingList = new ArrayList<NodeModelDetailBean>();
				int rowId = 0;
				for(ParamSet ps:param_set_list){
					NodeModelDetailBean nodeModelDetailBean = new NodeModelDetailBean(ps.getM_nodeTask().getM_node());			
					nodeModelDetailBean.setM_rowId(++rowId);
					nodeSettingList.add(nodeModelDetailBean);
				}
				return nodeSettingList;
			}
			
		}else if(param_type.equals("param_get")){
			List<ParamGet> param_get_list = m_paramGetRespository.findNodesByTask(task);
			if(param_get_list == null || param_get_list.size() == 0){
				m_log.info("[listNodesByTaskId] nodeList.size() = 0");
				return null;
			}else{
				List<NodeModelDetailBean> nodeSettingList = new ArrayList<NodeModelDetailBean>();
				int rowId = 0;
				for(ParamGet pg:param_get_list){
					NodeModelDetailBean nodeModelDetailBean = new NodeModelDetailBean(pg.getM_nodeTask().getM_node());			
					nodeModelDetailBean.setM_rowId(++rowId);
					nodeSettingList.add(nodeModelDetailBean);
				}
				return nodeSettingList;
			}
			
		}else{
			List<NodeTask> nodeList = m_nodeTaskRespository.findNodesByTask(task);
			if(nodeList == null || nodeList.size() == 0){
				m_log.info("[listNodesByTaskId] nodeList.size() = 0");
				return null;
			}else{
				List<NodeModelDetailBean> nodeSettingList = new ArrayList<NodeModelDetailBean>();
				int rowId = 0;
				for(NodeTask nm:nodeList){
					NodeModelDetailBean nodeModelDetailBean = new NodeModelDetailBean(nm.getM_node());			
					nodeModelDetailBean.setM_rowId(++rowId);
					nodeSettingList.add(nodeModelDetailBean);
				}
				return nodeSettingList;
			}
		}
	}
	
	public SystemOperateResultCode addParamSet(Long task_id,List<Long> id_list){
		SystemOperateResultCode resultCode = SystemOperateResultCode.ADD_PARAM_SET_FAILED;
		m_log.info("[addParamSet] task_id = " + task_id);
		List<NodeTask> nodeTask_list = m_nodeTaskRespository.findNodesByTaskAndNodeIdList(task_id, id_list);
		if(nodeTask_list == null || nodeTask_list.size() == 0){
			m_log.info("[addParamSet] nodeTask_list.size() = 0");
			resultCode = SystemOperateResultCode.ADD_PARAM_SET_NULL;
			return resultCode;
		}else{
			int count = 0;
			for(NodeTask nm:nodeTask_list){
				ParamSet ps = m_paramSetRespository.findParamSet(nm);
				if(ps == null){
					ps = new ParamSet();
					ps.setM_nodeTask(nm);
					try{
						m_paramSetRespository.addEntity(ps);
						count ++;
					}catch(Exception ex){
						ex.printStackTrace();
						return SystemOperateResultCode.ADD_PARAM_SET_FAILED;
					}	
				}							
			}
			
			if(count != nodeTask_list.size()){
				resultCode = SystemOperateResultCode.ADD_SOME_PARAM_SET_EXIST;
			}
			else{
				resultCode = SystemOperateResultCode.ADD_PARAM_SET_SUCCESS;
			}
			return resultCode;
		}		
	}
	
	public SystemOperateResultCode addParamGet(Long task_id,List<Long> id_list){
		m_log.info("[addParamSet] task_id = " + task_id);
		SystemOperateResultCode resultCode = SystemOperateResultCode.ADD_PARAM_GET_FAILED;		
		List<NodeTask> nodeTask_list = m_nodeTaskRespository.findNodesByTaskAndNodeIdList(task_id, id_list);
		if(nodeTask_list == null || nodeTask_list.size() == 0){
			m_log.info("[addParamSet] nodeTask_list.size() = 0");
			resultCode = SystemOperateResultCode.ADD_PARAM_GET_NULL;
			return resultCode;
		}else{
			int count = 0;
			for(NodeTask nm:nodeTask_list){
				//判断是否存在
				ParamGet pg = m_paramGetRespository.findParamGet(nm);
				if(pg == null){
					pg = new ParamGet();
					pg.setM_nodeTask(nm);
					try{
						m_paramGetRespository.addEntity(pg);
						count++;
					}catch(Exception ex){
						ex.printStackTrace();
						return SystemOperateResultCode.ADD_PARAM_GET_FAILED;
					}
				}				
			}
			
			if(count != nodeTask_list.size()){
				resultCode = SystemOperateResultCode.ADD_SOME_PARAM_GET_EXIST;
			}else{
				resultCode = SystemOperateResultCode.ADD_PARAM_GET_SUCCESS;
			}
			return resultCode;
		}		
	}
	
	public SystemOperateResultCode deleteParamSet(Long node_id,Long task_id){
		m_log.info("[deleteParamSet] task_id = " + task_id + ",node_id "+ node_id);
		SystemOperateResultCode resultCode = SystemOperateResultCode.DELETE_PARAM_SET_FAILED;
		NodeTask nodetask = m_nodeTaskRespository.findNodesByTaskIdAndNodeId(node_id,task_id);
		if(nodetask == null){
			m_log.info("[deleteParamSet] nodetask = null");
			resultCode = SystemOperateResultCode.DELETE_PARAM_SET_NULL;
			return resultCode;
		}else{
			ParamSet ps = m_paramSetRespository.findParamSet(nodetask);
			if(ps == null){
				m_log.info("[deleteParamSet] ps = null");
				resultCode = SystemOperateResultCode.DELETE_PARAM_SET_NULL;
				return resultCode;
			}else{
				try{
					m_paramSetRespository.deleteEntity(ps);			        						
				    resultCode = SystemOperateResultCode.DELETE_PARAM_SET_SUCCESS;
			    }catch(Exception ex){		    	
			    	resultCode = SystemOperateResultCode.DELETE_PARAM_SET_FAILED;
			     }
				return resultCode;
			}
		}			
	}
	
	public SystemOperateResultCode deleteParamGet(Long node_id,Long task_id){
		m_log.info("[deleteParamSet] task_id = " + task_id + ",node_id "+ node_id);
		SystemOperateResultCode resultCode = SystemOperateResultCode.DELETE_PARAM_GET_FAILED;
		NodeTask nodetask = m_nodeTaskRespository.findNodesByTaskIdAndNodeId(node_id,task_id);
		if(nodetask == null){
			m_log.info("[deleteParamSet] nodetask = null");
			resultCode = SystemOperateResultCode.DELETE_PARAM_GET_NULL;
			return resultCode;
		}else{
			ParamGet pg = m_paramGetRespository.findParamGet(nodetask);
			if(pg == null){
				m_log.info("[deleteParamSet] pg = null");
				resultCode = SystemOperateResultCode.DELETE_PARAM_GET_NULL;
				return resultCode;
			}else{
				try{
					m_paramGetRespository.deleteEntity(pg);			        						
				    resultCode = SystemOperateResultCode.DELETE_PARAM_GET_SUCCESS;
			    }catch(Exception ex){		    	
			    	resultCode = SystemOperateResultCode.DELETE_PARAM_GET_FAILED;
			     }
				return resultCode;
			}
		}			
	}
	
	public ParamSetBean findByParamSetId(Long paramSet_id){
		ParamSet ps = m_paramSetRespository.findById(paramSet_id);
		if(ps != null){
			ParamSetBean psb = new ParamSetBean();
			psb.setM_id(ps.getM_id());
			psb.setM_nodePath(ps.getM_nodeTask().getM_node().getNode_path());
			psb.setM_taskName(ps.getM_nodeTask().getM_task().getM_name());
			psb.setM_type(ps.getM_nodeTask().getM_node().getType());
			psb.setM_value(ps.getM_value());
			return psb;
		}
		return null;
	}
	
	public NodeModelDetailBean findParamById(Long node_id){
		m_log.info("[findParamById] node_id = " + node_id);
		if(node_id != null){
			Node node = m_nodeRespository.findById(node_id);
			if(node != null){
				NodeModelDetailBean nmdb = new NodeModelDetailBean(node);
				return nmdb;
			}
		}
		return null;
	}
	
	public void updateParamSet(ParamSetBean psb){
		NodeTask nodeTask = m_nodeTaskRespository.findNodeTaskByParamPath(psb.getM_nodePath());
		if(nodeTask != null){
			ParamSet ps = new ParamSet();
			ps.setM_id(psb.getM_id());
			ps.setM_nodeTask(nodeTask);
			ps.setM_value(psb.getM_value());
			m_paramSetRespository.updateParamSet(ps);
		}
	}
	
	public SystemOperateResultCode updateParam(NodeModelDetailBean nmdb){
		SystemOperateResultCode resultCode = SystemOperateResultCode.UPDATE_PARAM_FAILED;
		Node node = m_nodeRespository.findById(nmdb.getId());
		if(node != null ){
			node.setValue_type(nmdb.getValue_type());
		    if(nmdb.getValue_type().equals("basic")){
		    	node.setMin_value(nmdb.getMin_value());
		    	node.setMax_value(nmdb.getMax_value());
		    }else if(nmdb.getValue_type().equals("enum")){
		    	node.setOther_value(nmdb.getOther_value());
		    }
		    
			try{
				m_nodeRespository.updateEntity(node);
				resultCode = SystemOperateResultCode.UPDATE_PARAM_SUCCESS;
			}catch(Exception ex){
				ex.toString();
				resultCode = SystemOperateResultCode.UPDATE_PARAM_FAILED;
			}
		}
		return resultCode;
	}
	
	public SystemOperateResultCode updateParamNameZh(NodeModelDetailBean nmdb){
		SystemOperateResultCode resultCode = SystemOperateResultCode.UPDATE_PARAM_FAILED;
		Node node = m_nodeRespository.findById(nmdb.getId());
		if(node != null ){
			m_log.info("[updateParamNameZh] nameZh = " + nmdb.getNameZh());
			node.setNameZh(nmdb.getNameZh());
			try{
				m_nodeRespository.updateEntity(node);
				resultCode = SystemOperateResultCode.UPDATE_PARAM_SUCCESS;
			}catch(Exception ex){
				ex.toString();
				resultCode = SystemOperateResultCode.UPDATE_PARAM_FAILED;
			}
		}
		return resultCode;
	}
	
	public DeviceTaskBean findDeviceTaskBySN(String serialNumber,Long task_id){
		m_log.info("[findDeviceTaskBySN] get into findDeviceTaskBySN!");
		DeviceTask deviceTask = m_deviceTaskRespository.findDeviceTaskBySerialNumberAndTaskId(serialNumber,task_id);
		if(deviceTask != null){
			m_log.info("[findDeviceTaskBySN] deviceTask != null");
			DeviceTaskBean dtb = new DeviceTaskBean();
			dtb.setM_id(deviceTask.getM_id());
	        dtb.setM_device(deviceTask.getM_device());
	        dtb.setM_is_period(deviceTask.isM_is_period());
	        dtb.setM_status(deviceTask.getM_status());
	        dtb.setM_task(deviceTask.getM_task());
	        return dtb;
		}
		return null;
	}
}
