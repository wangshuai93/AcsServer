package com.yinhe.server.AcsServer.ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.yinhe.server.AcsServer.backbean.CPEBean;
import com.yinhe.server.AcsServer.backbean.ColumnNameBean;
import com.yinhe.server.AcsServer.backbean.CountResultBean;
import com.yinhe.server.AcsServer.backbean.CountResultQueryBean;
import com.yinhe.server.AcsServer.backbean.ManagedResultBean;
import com.yinhe.server.AcsServer.backbean.ParamGetBean;
import com.yinhe.server.AcsServer.backbean.RecordResultDetailBean;
import com.yinhe.server.AcsServer.backbean.RecordResultDetailBean2;
import com.yinhe.server.AcsServer.backbean.ResultDetailBean;
import com.yinhe.server.AcsServer.backbean.ResultQueryBean;
import com.yinhe.server.AcsServer.backbean.ResultTaskBean;
import com.yinhe.server.AcsServer.data.CPERespository;
import com.yinhe.server.AcsServer.data.CountResultRespository;
import com.yinhe.server.AcsServer.data.NodeRespository;
import com.yinhe.server.AcsServer.data.ParamGetRespository;
import com.yinhe.server.AcsServer.data.ResultRespository;
import com.yinhe.server.AcsServer.model.CountResult;
import com.yinhe.server.AcsServer.model.Devices;
import com.yinhe.server.AcsServer.model.ParamGet;
import com.yinhe.server.AcsServer.model.Result;
import com.yinhe.server.AcsServer.util.Resources;

@Stateless
public class ResultManagerEJB  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1637315069463403412L;
	@Inject
	private Logger log;
	@Inject
	private ResultRespository m_resultRespository;
	@Inject
	private CPERespository m_cpeRespository;
	@Inject
	private CountResultRespository m_countResultRespository;
	@Inject
	private ParamGetRespository m_paramGetRespository;
	@Inject
	private NodeRespository m_nodeRespository;
	
	public List<ResultDetailBean> listResultAll(ResultQueryBean m_queryBean){
		Devices device = m_cpeRespository.findDeviceBySerialNumber(m_queryBean.getM_serialNumber());
		if(device == null){
			return null;
		}
		m_queryBean.setMaxByCount(m_resultRespository.resultCount(m_queryBean));
		List<Result> result_list = m_resultRespository.listResult(m_queryBean);		
		if(result_list == null || result_list.size() == 0){
			log.info("[listResultAll] result_list.size() == 0");
			return null;
		}else{
			List<ResultDetailBean> resultList = new ArrayList<ResultDetailBean>();
			int rowId = 0;
			for(Result r : result_list){
				ResultDetailBean detailBean = new ResultDetailBean(r);				
				detailBean.setM_rowId(++rowId);
				detailBean.setM_nodePath(r.getM_nodeTask().getM_node().getNode_path());
				detailBean.setM_taskName(r.getM_nodeTask().getM_task().getM_name());
				resultList.add(detailBean);
			}
			return resultList;
		}
	}
	
	public List<CountResultBean> listCountResultAll(CountResultQueryBean m_queryBean){
		log.info("[listCountResultAll] get into listCountResultAll");
		Devices device = m_cpeRespository.findDeviceBySerialNumber(m_queryBean.getM_serialNumber());
		if(device == null){
			return null;
		}
		m_queryBean.setMaxByCount(m_countResultRespository.resultCount(device));
		List<CountResult> countResult_list = new ArrayList<CountResult>();
		countResult_list = m_countResultRespository.listCountResult(m_queryBean);
		if(countResult_list != null && countResult_list.size() > 0){
			log.info("[listCountResultAll] countResult_list.size > 0");
			List<CountResultBean> countResultBean_list = new ArrayList<CountResultBean>();
			int rowId = 0;
			for(CountResult cr : countResult_list){
				
				List<Result> result_list = new ArrayList<Result>();
				result_list = m_resultRespository.findByCountResultId(cr.getM_id());
				List<ResultDetailBean> resultList = new ArrayList<ResultDetailBean>();
				if(result_list != null && result_list.size() > 0){
					log.info("[listCountResultAll] resultList.size > 0");
					
					CountResultBean crb = new CountResultBean();
					crb.setM_rowId(++rowId);
					crb.setM_id(cr.getM_id());
					crb.setM_reportTime(cr.getM_reportTime());
					String result = "";
					for(Result r : result_list){
						result += r.toString() + "\r\n";
						ResultDetailBean detailBean = new ResultDetailBean(r);
						detailBean.setM_nodePath(r.getM_nodeTask().getM_node().getNode_path());
						detailBean.setM_taskName(r.getM_nodeTask().getM_task().getM_name());
						resultList.add(detailBean);
					}
					crb.setResult_list(resultList);
					crb.setM_result(result);
					countResultBean_list.add(crb);
				}
				
			}
			return countResultBean_list;
		}
		return null;
	}
	
	public List<CountResultBean> listCountResultByTask(CountResultQueryBean m_queryBean){
		log.info("[listCountResultByTask] task_id = " + m_queryBean.getM_taskId());
		Devices device = m_cpeRespository.findDeviceBySerialNumber(m_queryBean.getM_serialNumber());
		if(device == null){
			return null;
		}
		List<CountResult> countResult_list = new ArrayList<CountResult>();
		countResult_list = m_countResultRespository.listCountResult(m_queryBean);
		if(countResult_list != null && countResult_list.size() > 0){
			log.info("[listCountResultByTask] countResult_list.size > 0");
			
			List<CountResultBean> countResultBean_list = new ArrayList<CountResultBean>();
			int rowId = 0;
			for(CountResult cr : countResult_list){
				List<Result> result_list = new ArrayList<Result>();
				result_list = m_resultRespository.listResultByCountResultAndTaskId(cr.getM_id(),m_queryBean);
				if(result_list != null && result_list.size() > 0){
					log.info("[listCountResultByTask] resultList.size > 0");
					CountResultBean crb = new CountResultBean();
					crb.setM_rowId(++rowId);
					crb.setM_id(cr.getM_id());
					crb.setM_reportTime(cr.getM_reportTime());
					String result = "";
					List<ResultDetailBean> resultList = new ArrayList<ResultDetailBean>();
					for(Result r : result_list){
						result += r.toString() + "\r\n";
						ResultDetailBean detailBean = new ResultDetailBean(r);
						detailBean.setM_nodePath(r.getM_nodeTask().getM_node().getNode_path());
						detailBean.setM_taskName(r.getM_nodeTask().getM_task().getM_name());
						resultList.add(detailBean);
					}
					crb.setResult_list(resultList);
					crb.setM_result(result);
					countResultBean_list.add(crb);
				}
			}
			m_queryBean.setMaxByCount(rowId);
			return countResultBean_list;
		}
		return null;
	}
	
	public List<String> listParamGet(CountResultQueryBean m_queryBean){
		log.info("[listParamGet] task_id = " + m_queryBean.getM_taskId());
		Long task_id = m_queryBean.getM_taskId();
		if(task_id == null){
			return null;
		}
		Devices device = m_cpeRespository.findDeviceBySerialNumber(m_queryBean.getM_serialNumber());
		if(device == null){
			return null;
		}
		
		List<String> paramName_list = new ArrayList<String>();
		List<ParamGet> paramGet_list = new ArrayList<ParamGet>();
		paramGet_list = m_paramGetRespository.findNodesByTaskId(task_id);
		if(paramGet_list != null && paramGet_list.size() > 0){
			log.info("[listParamGet] paramGet_list.size > 0");
			
			for(ParamGet pg : paramGet_list){
				if(!Resources.isNullOrEmpty(pg.getM_nodeTask().getM_node().getNameZh())){
					log.info("[listParamGet] nameZh = " + pg.getM_nodeTask().getM_node().getNameZh());
					paramName_list.add(pg.getM_nodeTask().getM_node().getNameZh());
				}else{
					paramName_list.add(pg.getM_nodeTask().getM_node().getAbbr_name());
				}
			}
			return paramName_list;
		}
		return null;
	}
	
	public List<String> listParamGet(ResultQueryBean m_queryBean){
		log.info("[listParamGet] task_id = " + m_queryBean.getM_taskId());
		Long task_id = m_queryBean.getM_taskId();
		if(task_id == null){
			return null;
		}
		Devices device = m_cpeRespository.findDeviceBySerialNumber(m_queryBean.getM_serialNumber());
		if(device == null){
			return null;
		}
		
		List<String> paramName_list = new ArrayList<String>();
		List<ParamGet> paramGet_list = new ArrayList<ParamGet>();
		paramGet_list = m_paramGetRespository.findNodesByTaskId(task_id);
		if(paramGet_list != null && paramGet_list.size() > 0){
			log.info("[listParamGet] paramGet_list.size > 0");
			
			for(ParamGet pg : paramGet_list){
				if(!Resources.isNullOrEmpty(pg.getM_nodeTask().getM_node().getNameZh())){
					log.info("[listParamGet] nameZh = " + pg.getM_nodeTask().getM_node().getNameZh());
					paramName_list.add(pg.getM_nodeTask().getM_node().getNameZh());
				}else{
					paramName_list.add(pg.getM_nodeTask().getM_node().getAbbr_name());
				}
			}
			return paramName_list;
		}
		return null;
	}
	
	public List<ColumnNameBean> listColumnName(CountResultQueryBean m_queryBean){
		log.info("[listColumnName] task_id = " + m_queryBean.getM_taskId());
		Long task_id = m_queryBean.getM_taskId();
		if(task_id == null){
			return null;
		}
		
		Devices device = m_cpeRespository.findDeviceBySerialNumber(m_queryBean.getM_serialNumber());
		if(device == null){
			return null;
		}
		
		List<ParamGet> paramGet_list = new ArrayList<ParamGet>();
		paramGet_list = m_paramGetRespository.findNodesByTaskId(task_id);
		if(paramGet_list != null && paramGet_list.size() > 0){
			List<ColumnNameBean> columnName_list = new ArrayList<ColumnNameBean>();
			log.info("[listColumnName] paramGet_list.size > 0");
			
			for(ParamGet pg : paramGet_list){
				ColumnNameBean columnName = new ColumnNameBean();
				if(!Resources.isNullOrEmpty(pg.getM_nodeTask().getM_node().getNameZh())){
					log.info("[listColumnName] nameZh = " + pg.getM_nodeTask().getM_node().getNameZh());
					columnName.setM_name(pg.getM_nodeTask().getM_node().getNameZh());
				}else{
					columnName.setM_name(pg.getM_nodeTask().getM_node().getAbbr_name());
				}
				
				if(!Resources.isNullOrEmpty(pg.getM_nodeTask().getM_node().getUnit())){
					columnName.setM_unit(pg.getM_nodeTask().getM_node().getUnit());
				}else{
					columnName.setM_unit("");
				}
								
				if(!"星座图数据0".equals(pg.getM_nodeTask().getM_node().getNameZh()) 
						&& !"星座图数据1".equals(pg.getM_nodeTask().getM_node().getNameZh()) 
						&& !"星座图数据2".equals(pg.getM_nodeTask().getM_node().getNameZh()) 
						&& !"星座图数据3".equals(pg.getM_nodeTask().getM_node().getNameZh()) 
						&& !"星座图数据4".equals(pg.getM_nodeTask().getM_node().getNameZh()) 
						&& !"星座图数据5".equals(pg.getM_nodeTask().getM_node().getNameZh()) 
						&& !"星座图数据6".equals(pg.getM_nodeTask().getM_node().getNameZh()) 
						&& !"星座图数据7".equals(pg.getM_nodeTask().getM_node().getNameZh()))
				{	
						if( (!"FULLBAND_TASK".equals(pg.getM_nodeTask().getM_task().getM_name()))
							||("FULLBAND_TASK".equals(pg.getM_nodeTask().getM_task().getM_name()) 
								&& "Device.FullBandScan.DateTime".equals(pg.getM_nodeTask().getM_node().getNode_path())))
						columnName_list.add(columnName);
				}		
			}
			return columnName_list;
		}
		return null;
	}
	
	public List<ColumnNameBean> listColumnName(ResultQueryBean m_queryBean){
		log.info("[listColumnName] task_id = " + m_queryBean.getM_taskId());
		Long task_id = m_queryBean.getM_taskId();
		if(task_id == null){
			return null;
		}
		Devices device = m_cpeRespository.findDeviceBySerialNumber(m_queryBean.getM_serialNumber());
		if(device == null){
			return null;
		}
		
		List<ParamGet> paramGet_list = new ArrayList<ParamGet>();
		paramGet_list = m_paramGetRespository.findNodesByTaskId(task_id);
		if(paramGet_list != null && paramGet_list.size() > 0){
			List<ColumnNameBean> columnName_list = new ArrayList<ColumnNameBean>();
			log.info("[listColumnName] paramGet_list.size > 0");
			
			for(ParamGet pg : paramGet_list){
				ColumnNameBean columnName = new ColumnNameBean();
				if(!Resources.isNullOrEmpty(pg.getM_nodeTask().getM_node().getNameZh())){
					log.info("[listColumnName] nameZh = " + pg.getM_nodeTask().getM_node().getNameZh());
					columnName.setM_name(pg.getM_nodeTask().getM_node().getNameZh());
				}else{
					columnName.setM_name(pg.getM_nodeTask().getM_node().getAbbr_name());
				}
				
				if(!Resources.isNullOrEmpty(pg.getM_nodeTask().getM_node().getUnit())){
					columnName.setM_unit(pg.getM_nodeTask().getM_node().getUnit());
				}else{
					columnName.setM_unit("");
				}
				if(!pg.getM_nodeTask().getM_node().getNameZh().equals("星座图数据0") 
						&& !pg.getM_nodeTask().getM_node().getNameZh().equals("星座图数据2") 
						&& !pg.getM_nodeTask().getM_node().getNameZh().equals("星座图数据3") 
						&& !pg.getM_nodeTask().getM_node().getNameZh().equals("星座图数据4") 
						&& !pg.getM_nodeTask().getM_node().getNameZh().equals("星座图数据5") 
						&& !pg.getM_nodeTask().getM_node().getNameZh().equals("星座图数据6") 
						&& !pg.getM_nodeTask().getM_node().getNameZh().equals("星座图数据7") 
						&& !pg.getM_nodeTask().getM_node().getNameZh().equals("星座图数据1"))					
				columnName_list.add(columnName);
			}
			return columnName_list;
		}
		return null;
	}
	
	public List<ManagedResultBean> listResultOrderedByName(CountResultQueryBean m_queryBean){
		log.info("[listResultOrderedByName] task_id = " + m_queryBean.getM_taskId());
		Long task_id = m_queryBean.getM_taskId();
		if(task_id == null){
			return null;
		}
		Devices device = m_cpeRespository.findDeviceBySerialNumber(m_queryBean.getM_serialNumber());
		if(device == null){
			return null;
		}
		int startPosition = m_queryBean.getStartPosition();
		int size = m_queryBean.getSize();
		int temp = 0;
		
		List<CountResult> countResult_list = new ArrayList<CountResult>();
		countResult_list = m_countResultRespository.listCountResult(m_queryBean);
		if(countResult_list != null && countResult_list.size() > 0){
			log.info("[listResultOrderedByName] countResult_list.size > 0");
			//总页数
			int total = 0;
			for(CountResult cr : countResult_list){
				List<Result> result_list = m_resultRespository.listResultByCountResultAndTaskId(cr.getM_id(),m_queryBean);
				if(result_list != null && result_list.size() > 0){
					total++;
				}
			}
			m_queryBean.setMaxByCount(total);
			
			//上报参数
			List<ManagedResultBean> managedResult_list = new ArrayList<ManagedResultBean>();
			List<ParamGet> paramGet_list = new ArrayList<ParamGet>();
			paramGet_list = m_paramGetRespository.findNodesByTaskId(task_id);
			
			//填充值
			Long row_id = 0L;
		    if(paramGet_list != null && paramGet_list.size() > 0){
		    	for(CountResult cr : countResult_list){
		    		log.info("[listResultOrderedByName] countResult id = " + cr.getM_id().toString());
		    		
					List<Result> result_list2 = new ArrayList<Result>();
					result_list2 = m_resultRespository.listResultByCountResultAndTaskId(cr.getM_id(),m_queryBean);
					if(result_list2 != null && result_list2.size() > 0){
						log.info("[listResultOrderedByName] resultList.size > 0");
						//实现分页功能
						if(temp >= startPosition){
							ManagedResultBean mrb = new ManagedResultBean();
							//名称匹配
							mrb.setM_rowId(++row_id);
							mrb.setM_reportTime(cr.getM_reportTime());
							mrb.setM_countResultId(cr.getM_id());
							List<String> sorted_value = new ArrayList<String>();
							for(int i = 0; i < paramGet_list.size(); i++){
								String abbr_name1 = paramGet_list.get(i).getM_nodeTask().getM_node().getAbbr_name();
								boolean flag = false;
								boolean flag_planisphere = false;
								boolean flag_fulltask = false;
								for(Result r : result_list2){
										if(!"星座图数据0".equals(r.getM_nodeTask().getM_node().getNameZh()) 
												&& !"星座图数据1".equals(r.getM_nodeTask().getM_node().getNameZh()) 
												&& !"星座图数据2".equals(r.getM_nodeTask().getM_node().getNameZh()) 
												&& !"星座图数据3".equals(r.getM_nodeTask().getM_node().getNameZh()) 
												&& !"星座图数据4".equals(r.getM_nodeTask().getM_node().getNameZh()) 
												&& !"星座图数据5".equals(r.getM_nodeTask().getM_node().getNameZh()) 
												&& !"星座图数据6".equals(r.getM_nodeTask().getM_node().getNameZh()) 
												&& !"星座图数据7".equals(r.getM_nodeTask().getM_node().getNameZh())){
											flag_planisphere = true;
										if( (!"FULLBAND_TASK".equals(r.getM_nodeTask().getM_task().getM_name()))
												||("FULLBAND_TASK".equals(r.getM_nodeTask().getM_task().getM_name()) 
													&& "Device.FullBandScan.DateTime".equals(r.getM_nodeTask().getM_node().getNode_path())))
										{
											flag_fulltask = true;
											String abbr_name2 = r.getM_nodeTask().getM_node().getAbbr_name();
											if(abbr_name1.equals(abbr_name2)){
												flag = true;
												sorted_value.add(r.getM_value());
											}
										}
									}
								}
								
								
								if(!flag && !flag_planisphere && !flag_fulltask){
									sorted_value.add("");
								}
							}
							mrb.setM_sortedValue(sorted_value);
							managedResult_list.add(mrb);
						}
						temp++;
					}
					if(row_id >= size){
						break;
					}
				}
			}
			return managedResult_list;
		}
		return null;
	}
	
	public List<ManagedResultBean> listResultOrderedByName(ResultQueryBean m_queryBean){
		log.info("[listResultOrderedByName] task_id = " + m_queryBean.getM_taskId());
		Long task_id = m_queryBean.getM_taskId();
		if(task_id == null){
			return null;
		}
		Devices device = m_cpeRespository.findDeviceBySerialNumber(m_queryBean.getM_serialNumber());
		if(device == null){
			return null;
		}
		int startPosition = m_queryBean.getStartPosition();
		int size = m_queryBean.getSize();
		int temp = 0;
		
		List<CountResult> countResult_list = new ArrayList<CountResult>();
		countResult_list = m_countResultRespository.listCountResult(m_queryBean);
		if(countResult_list != null && countResult_list.size() > 0){
			log.info("[listResultOrderedByName] countResult_list.size > 0");
			//总页数
			int total = 0;
			for(CountResult cr : countResult_list){
				List<Result> result_list = m_resultRespository.listResultByCountResultAndTaskId(cr.getM_id(),m_queryBean);
				if(result_list != null && result_list.size() > 0){
					total++;
				}
			}
			m_queryBean.setMaxByCount(total);
			
			//上报参数
			List<ManagedResultBean> managedResult_list = new ArrayList<ManagedResultBean>();
			List<ParamGet> paramGet_list = new ArrayList<ParamGet>();
			paramGet_list = m_paramGetRespository.findNodesByTaskId(task_id);
			
			//填充值
			Long row_id = 0L;
		    if(paramGet_list != null && paramGet_list.size() > 0){
		    	for(CountResult cr : countResult_list){
		    		log.info("[listResultOrderedByName] countResult id = " + cr.getM_id().toString());
		    		
					List<Result> result_list2 = new ArrayList<Result>();
					result_list2 = m_resultRespository.listResultByCountResultAndTaskId(cr.getM_id(),m_queryBean);
					if(result_list2 != null && result_list2.size() > 0){
						log.info("[listResultOrderedByName] resultList.size > 0");
						//实现分页功能
						if(temp >= startPosition){
							ManagedResultBean mrb = new ManagedResultBean();
							//名称匹配
							mrb.setM_rowId(++row_id);
							mrb.setM_reportTime(cr.getM_reportTime());
							mrb.setM_countResultId(cr.getM_id());
							//Long a = cr.getM_id();
							//mrb.setM_countResultId(a);
							List<String> sorted_value = new ArrayList<String>();
							for(int i = 0; i < paramGet_list.size(); i++){
								String abbr_name1 = paramGet_list.get(i).getM_nodeTask().getM_node().getAbbr_name();
								boolean flag = false;
								boolean flag_planisphere = false;
								for(Result r : result_list2){
									if(!r.getM_nodeTask().getM_node().getNameZh().equals("星座图数据0") 
											&& !r.getM_nodeTask().getM_node().getNameZh().equals("星座图数据1") 
											&& !r.getM_nodeTask().getM_node().getNameZh().equals("星座图数据2") 
											&& !r.getM_nodeTask().getM_node().getNameZh().equals("星座图数据3") 
											&& !r.getM_nodeTask().getM_node().getNameZh().equals("星座图数据4") 
											&& !r.getM_nodeTask().getM_node().getNameZh().equals("星座图数据5") 
											&& !r.getM_nodeTask().getM_node().getNameZh().equals("星座图数据6") 
											&& !r.getM_nodeTask().getM_node().getNameZh().equals("星座图数据7")){
									flag_planisphere = true;
									String abbr_name2 = r.getM_nodeTask().getM_node().getAbbr_name();
									if(abbr_name1.equals(abbr_name2)){
										flag = true;
										sorted_value.add(r.getM_value());
									}
								}
								}
								if(!flag && !flag_planisphere){
									sorted_value.add("");
								}
							}
							mrb.setM_sortedValue(sorted_value);
							managedResult_list.add(mrb);
						}
						temp++;
					}
					if(row_id >= size){
						break;
					}
				}
			}
			return managedResult_list;
		}
		return null;
	}
	
	public List<ResultTaskBean> resultFigureList(CountResultQueryBean m_queryBean){
		List<ResultTaskBean> resultTaskBeanList = new ArrayList<ResultTaskBean>();
		
		Devices device = m_cpeRespository.findDeviceBySerialNumber(m_queryBean.getM_serialNumber());
		if(device == null){
			return null;
		}
		
		List<ParamGet> paramGet_list = new ArrayList<ParamGet>();
		paramGet_list = m_paramGetRespository.findNodesByTaskId(m_queryBean.getM_taskId());
		
		//获取节点名
		List<String> paramNameList = new ArrayList<String>();
 		if(paramGet_list != null && paramGet_list.size() > 0){
			for(ParamGet paramGet : paramGet_list){
				paramNameList.add(paramGet.getM_nodeTask().getM_node().getAbbr_name());
			}
		}
		List<CountResult> countResultList = new ArrayList<CountResult>();
		countResultList = m_countResultRespository.listCountResult(m_queryBean);
		
		if(paramNameList != null && paramNameList.size() > 0){
			for(String name : paramNameList){
				if(countResultList != null && countResultList.size() > 0){
					for(CountResult countResult : countResultList){
						List<Result> resultList = new ArrayList<Result>();
						resultList = m_resultRespository.listResultByCountResultAndTaskId(countResult.getM_id(), m_queryBean);
						if(resultList != null && resultList.size() > 0){
							for(Result result : resultList){
								if(name.equals(result.getM_nodeTask().getM_node().getAbbr_name())){
									ResultTaskBean resultTaskBean = new ResultTaskBean();
									resultTaskBean.setM_paramName(name);
									resultTaskBean.setM_paramValue(result.getM_value());
									resultTaskBean.setM_reportTime(countResult.getM_reportTime());
									resultTaskBeanList.add(resultTaskBean);
								}
							}
						}
					}
				}
			}
		}
		if(resultTaskBeanList != null && resultTaskBeanList.size() > 0){
			for(ResultTaskBean bean : resultTaskBeanList){
				log.info("resultFigureList  name = " + bean.getM_paramName() + "; value = " + bean.getM_paramValue() + "; reportTime = " + bean.getM_reportTime());
			}
		}
		return resultTaskBeanList;
	}
	
	
	public List<ResultTaskBean> resultListByCountResultId(CountResultQueryBean m_queryBean) {
		List<ResultTaskBean> resultTaskBeanList = new ArrayList<ResultTaskBean>();
		
		List<ParamGet> paramGet_list = new ArrayList<ParamGet>();
		paramGet_list = m_paramGetRespository.findNodesByTaskId(m_queryBean.getM_taskId());
		
		//获取节点名
		List<String> paramNameList = new ArrayList<String>();
 		if(paramGet_list != null && paramGet_list.size() > 0){
			for(ParamGet paramGet : paramGet_list){
				paramNameList.add(paramGet.getM_nodeTask().getM_node().getAbbr_name());
			}
		}
 		
		CountResult countResult = new CountResult();
		countResult = m_countResultRespository.findById(m_queryBean.getM_countResultId());
		
		if(paramNameList != null && paramNameList.size() > 0){
			for(String name : paramNameList){
				if(countResult != null){

					List<Result> resultList = new ArrayList<Result>();
					resultList = m_resultRespository.listResultByCountResultAndTaskId(countResult.getM_id(), m_queryBean);
					if(resultList != null && resultList.size() > 0){
						for(Result result : resultList){
							if(name.equals(result.getM_nodeTask().getM_node().getAbbr_name())){
								ResultTaskBean resultTaskBean = new ResultTaskBean();
								resultTaskBean.setM_id(countResult.getM_id());
								resultTaskBean.setM_paramName(name);
								resultTaskBean.setM_paramValue(result.getM_value());
								resultTaskBean.setM_reportTime(countResult.getM_reportTime());
								resultTaskBeanList.add(resultTaskBean);
							}
						}
					}
					
				}
			}
		}
		/*
		if(resultTaskBeanList != null && resultTaskBeanList.size() > 0){
			for(ResultTaskBean bean : resultTaskBeanList){
				log.info("resultListByCountResultId"+ bean.getM_id() +";  name = " + bean.getM_paramName() + "; value = " + bean.getM_paramValue() + "; reportTime = " + bean.getM_reportTime());
			}
		}
		*/
		return resultTaskBeanList;
	}
	/*public List<String> listParamGet(CountResultQueryBean m_queryBean){
		log.info("get into listResultByName,task_id = " + m_queryBean.getM_taskId());
		Devices device = m_cpeRespository.findDeviceBySerialNumber(m_queryBean.getM_serialNumber());
		if(device == null){
			return null;
		}
		
		List<String> paramName_list = new ArrayList<String>();
		List<ParamGet> paramGet_list = new ArrayList<ParamGet>();
		paramGet_list = m_paramGetRespository.findNodesByTaskId(m_queryBean.getM_taskId());
		if(paramGet_list != null && paramGet_list.size() > 0){
			log.info("paramGet_list.size > 0");
			
			for(ParamGet pg : paramGet_list){
				paramName_list.add(pg.getM_nodeTask().getM_node().getAbbr_name());
			}
			return paramName_list;
		}
		return null;
	}*/
	
	public List<RecordResultDetailBean> listRecordList(ResultQueryBean queryBean) {
		log.info("[listRecordList] task_id = " + queryBean.getM_taskId());
		Long task_id = queryBean.getM_taskId();
		if(task_id == null){
			return null;
		}
		//分页问题
		int start = queryBean.getStartPosition();
		int size = queryBean.getSize();
		int tempStart = 0;
		int tempSize = 0;
		
		queryBean.setMaxByCount(m_resultRespository.resultCount(queryBean));
		List<Result> result_list = m_resultRespository.listResult(queryBean);
		if (result_list != null && result_list.size() > 0) {
			List<RecordResultDetailBean> recordResult_list = new ArrayList<RecordResultDetailBean>();
			int rowId = 0;
			for (Result r : result_list) {
				String value = r.getM_value();    //value
				List<String> value_list = new ArrayList<String>();
				if (value.contains("ts")) {
					if (tempStart >= start) {
						if (tempSize >= size) {
							break;
						}
						log.info("value = " + value);
						RecordResultDetailBean detailBean = new RecordResultDetailBean();
						detailBean.setRowId(++rowId);
						detailBean.setNodePath(r.getM_nodeTask().getM_node().getNameZh());
						detailBean.setIsAlarm(r.getM_alarm());
						detailBean.setReportTime(r.getM_countResult().getM_reportTime());
						detailBean.setTaskName(r.getM_nodeTask().getM_task().getM_name());
						String[] str = value.split(",");
						for (String s : str) {
							value_list.add(s);
						}
						detailBean.setValue(value_list);
						recordResult_list.add(detailBean);
						tempSize++;
					}
					tempStart++;
				}
			}
			return recordResult_list;
		}
		return null;
	}
	
	public List<RecordResultDetailBean2> listRecordList2(ResultQueryBean queryBean) {
		log.info("[listRecordList] task_id = " + queryBean.getM_taskId());
		Long task_id = queryBean.getM_taskId();
		if(task_id == null){
			return null;
		}
		//分页问题
		int start = queryBean.getStartPosition();
		int size = queryBean.getSize();
		int tempStart = 0;
		int tempSize = 0;
		
		List<Result> result_list = m_resultRespository.listResult(queryBean);
		if (result_list != null && result_list.size() > 0) {
			queryBean.setMaxByCount(this.getRecordMaxCount(result_list));
			
			List<RecordResultDetailBean2> recordResult_list = new ArrayList<RecordResultDetailBean2>();
			int rowId = 0;
			for (Result r : result_list) {
				String value = r.getM_value();    //value
				if (value.contains("ts")) {
					log.info("value = " + value);
					String[] str = value.split(",");
					for (String s : str) {
						if (tempStart >= start) {
							if (tempSize >= size) {
								break;
							}
							RecordResultDetailBean2 detailBean = new RecordResultDetailBean2();
							detailBean.setRowId(++rowId);
							detailBean.setReportTime(r.getM_countResult().getM_reportTime());
							detailBean.setValue(s);
							String fileName = "";
							int index = s.lastIndexOf("/");
							index++;
							fileName = s.substring(index, s.length());
							detailBean.setFileName(fileName);
							
							recordResult_list.add(detailBean);
							tempSize++;
						}
						tempStart++;
					}
				}
			}
			return recordResult_list;
		}
		return null;
	}
	
	public int getRecordMaxCount(List<Result> result_list) {
		int maxCount = 0;
		for (Result r : result_list) {
			String value = r.getM_value();    //value
			if (value.contains("ts")) {
				String[] str = value.split(",");
				maxCount = maxCount + str.length;
			}
		}	
		return maxCount;
	}
	
	public void deleteExistRecordResult(List<ParamGetBean> paramGet_list) {
		for (ParamGetBean pg : paramGet_list) {
			List<Result> result_list = m_resultRespository.findByNodePath(pg.getM_nodePath());
			if (result_list != null && result_list.size() > 0) {
				for (Result r : result_list) {
					try {
						m_resultRespository.deleteEntity(r);
					} catch (Exception e) {
						log.info("[deleteExistRecordResult] throws " + e.toString());
					}
				}
			}
		}
	}


	
	
}
