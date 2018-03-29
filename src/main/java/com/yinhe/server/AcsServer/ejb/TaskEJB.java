package com.yinhe.server.AcsServer.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.yinhe.server.AcsServer.backbean.ParamGetBean;
import com.yinhe.server.AcsServer.data.ParamGetRespository;
import com.yinhe.server.AcsServer.data.TaskRespository;
import com.yinhe.server.AcsServer.model.ParamGet;
import com.yinhe.server.AcsServer.model.Task;

@Stateless
public class TaskEJB {
	@Inject
	private TaskRespository m_taskRespository;
	@Inject
	private ParamGetRespository m_paramGetRespository;
	
	public List<ParamGetBean> findByTaskName(String taskName) {
		Task task = m_taskRespository.findByType(taskName);
		if (task != null) {
			List<ParamGet> paramGet_list = new ArrayList<ParamGet>();
	    	paramGet_list = m_paramGetRespository.findNodesByTask(task);
	    	if (paramGet_list != null && paramGet_list.size() > 0) {
	    		List<ParamGetBean> detailBeanList = new ArrayList<ParamGetBean>();
	    		int rowId = 0;
	    		for (ParamGet pg : paramGet_list) {
	    			ParamGetBean bean = new ParamGetBean();
	    			bean.setM_rowId(++rowId);
	    			bean.setM_id(pg.getM_id());
	    			bean.setM_nodePath(pg.getM_nodeTask().getM_node().getNode_path());
	    			bean.setTaskName(pg.getM_nodeTask().getM_task().getM_name());
	    			
	    			detailBeanList.add(bean);
	    		}
	    		return detailBeanList;
	    	}
		}
		return null;
	}
	
}
