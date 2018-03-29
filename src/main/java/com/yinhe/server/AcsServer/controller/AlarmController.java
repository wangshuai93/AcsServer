package com.yinhe.server.AcsServer.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.yinhe.server.AcsServer.backbean.AlarmBean;
import com.yinhe.server.AcsServer.backbean.AlarmQueryBean;
import com.yinhe.server.AcsServer.ejb.AlarmEJB;

@Named
@RequestScoped
public class AlarmController {

	@Inject
	private AlarmEJB m_alarmEJB;
	@Inject
	private AlarmQueryBean m_queryBean;
//	@Inject
//	private Logger log;

	private List<AlarmBean> m_alarmBeanList;

	@PostConstruct
	public void init() {
		m_alarmBeanList = new ArrayList<AlarmBean>();

		/*String o = getRequestParamMap().get("load");
		if (o != null && "lazy".equals(o)) {
			log.info("[init] SystemSettingController load: lazy!");
		} else {
			queryRecordList();
		}*/
		queryRecordList();
	}

	private void queryRecordList() {
		m_alarmBeanList = m_alarmEJB.getAllAlarmings(m_queryBean);
//		log.info("[queryRecordList] alarmBeanList.size = " + m_alarmBeanList.size());
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

	@SuppressWarnings("unused")
	private Map<String, String> getRequestParamMap() {
		return FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap();
	}

	public List<AlarmBean> getM_alarmBeanList() {
		return m_alarmBeanList;
	}

	public void setM_alarmBeanList(List<AlarmBean> m_alarmBeanList) {
		this.m_alarmBeanList = m_alarmBeanList;
	}

	public AlarmQueryBean getM_queryBean() {
		return m_queryBean;
	}

	public void setM_queryBean(AlarmQueryBean m_queryBean) {
		this.m_queryBean = m_queryBean;
	}
}
