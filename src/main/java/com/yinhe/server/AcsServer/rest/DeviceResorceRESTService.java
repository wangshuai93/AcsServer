package com.yinhe.server.AcsServer.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.yinhe.server.AcsServer.backbean.CPEBean;
import com.yinhe.server.AcsServer.backbean.CountResultBean;
import com.yinhe.server.AcsServer.backbean.CountResultQueryBean;
import com.yinhe.server.AcsServer.backbean.QueryCPEBean;
import com.yinhe.server.AcsServer.backbean.QueryTaskResultBean;
import com.yinhe.server.AcsServer.backbean.ResultTaskBean;
import com.yinhe.server.AcsServer.data.CountResultRespository;
import com.yinhe.server.AcsServer.ejb.CPEHandler;
import com.yinhe.server.AcsServer.ejb.ResultManagerEJB;
import com.yinhe.server.AcsServer.rest.type.DevicesDisplay;
import com.yinhe.server.AcsServer.util.Resources;

@Path("/device/")
@RequestScoped
public class DeviceResorceRESTService {
	@Inject
	private Logger log;
	@Inject
	private CPEHandler m_handler;
	@Inject
	private CPEBean m_cpeBean;
	@Inject
	private QueryTaskResultBean query;
	@Inject
	private QueryCPEBean m_queryCPEBean;
	@Inject
	private CountResultQueryBean m_countResultQueryBean;
	@Inject
	private ResultManagerEJB m_resultEJB;
	@Inject CountResultRespository m_countResultRespository;
	
	private List<CountResultBean> m_countResultList;
	
	@GET
	@Path("getCPEBeanList")
	@Produces(MediaType.APPLICATION_JSON)
	public List<DevicesDisplay> getCPEBeanList() {
		List<DevicesDisplay> devicesDisplay = new ArrayList<DevicesDisplay>();
		List<CPEBean> cpeBeanList = new ArrayList<CPEBean>();
		cpeBeanList = m_handler.listAll();
		if (null != cpeBeanList && 0 != cpeBeanList.size()) {
			for (CPEBean bean : cpeBeanList) {
				DevicesDisplay devicesDiplay = new DevicesDisplay();
				devicesDiplay.setCx(bean.getM_positionX());
				devicesDiplay.setCy(bean.getM_positionY());
				log.info("[getCPEBeanList] positionX = " + bean.getM_positionX() + ",positionY = " + bean.getM_positionY());
				devicesDiplay.setName(bean.getM_positionName());
				devicesDiplay.setStatus(bean.getM_status());
				devicesDiplay.setId(bean.getM_id());
				devicesDisplay.add(devicesDiplay);
			}
		}
		return devicesDisplay;
	}
	
	@GET
	@Path("updateCPEPosition")
	@Produces(MediaType.APPLICATION_JSON)
	public String updateCPEPosition(
			@QueryParam("device_id") Long device_id,
			@QueryParam("x") Integer x,
			@QueryParam("y") Integer y) {
		return m_handler.updateDevicePosition(device_id, x, y).toLocalString();
	}

	public QueryCPEBean getM_queryCPEBean() {
		return m_queryCPEBean;
	}

	public void setM_queryCPEBean(QueryCPEBean m_queryCPEBean) {
		this.m_queryCPEBean = m_queryCPEBean;
	}

	@GET
	@Path("getTaskResultList")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ResultTaskBean> getTaskResultList(
			@QueryParam("m_serialNumber") String m_serialNumber,
			@QueryParam("taskId") Long taskId) {
		log.info("[getTaskResultList] getTaskResultList m_serialNumber = " + m_serialNumber + "; taskId = " + taskId);
		List<ResultTaskBean> resultTaskBeanList = new ArrayList<ResultTaskBean>();
		if(!Resources.isNullOrEmpty(m_serialNumber) && null != taskId){
			m_countResultQueryBean.setM_serialNumber(m_serialNumber);
			m_countResultQueryBean.setM_taskId(taskId);
			resultTaskBeanList = m_resultEJB.resultFigureList(m_countResultQueryBean);
		}
		
		return resultTaskBeanList;
	}
	
	//constellation
	@GET
	@Path("getResult")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ResultTaskBean> getResult(
			@QueryParam("taskId") Long taskId,
			@QueryParam("m_countResultId") Long m_countResultId) {
		log.info("[getTaskResult] getTaskResult m_countResultId = " + m_countResultId + "; taskId = " + taskId);
		List<ResultTaskBean> resultTaskBeanList = new ArrayList<ResultTaskBean>();
		if(null != m_countResultId && null != taskId){
			m_countResultQueryBean.setM_countResultId(m_countResultId);
			m_countResultQueryBean.setM_taskId(taskId);
			resultTaskBeanList = m_resultEJB.resultListByCountResultId(m_countResultQueryBean);
		}
		
		return resultTaskBeanList;
	}

	public CPEBean getM_cpeBean() {
		return m_cpeBean;
	}

	public void setM_cpeBean(CPEBean m_cpeBean) {
		this.m_cpeBean = m_cpeBean;
	}

	public QueryTaskResultBean getQuery() {
		return query;
	}

	public void setQuery(QueryTaskResultBean query) {
		this.query = query;
	}

	public List<CountResultBean> getM_countResultList() {
		return m_countResultList;
	}

	public void setM_countResultList(List<CountResultBean> m_countResultList) {
		this.m_countResultList = m_countResultList;
	}

}
