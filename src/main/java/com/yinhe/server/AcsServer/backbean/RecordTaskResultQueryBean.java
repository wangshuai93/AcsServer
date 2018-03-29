package com.yinhe.server.AcsServer.backbean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecordTaskResultQueryBean extends BaseQueryBean{
	private Long m_id;
	private Boolean m_state;
	private String m_videoUrl;
	private Date m_reportTime;
	private Long m_device_id;
	private Date m_beginTime;
	private Date m_endTime;
	private List<Long> idList = new ArrayList<>();
	public Long getM_id() {
		return m_id;
	}
	public void setM_id(Long m_id) {
		this.m_id = m_id;
	}
	public Boolean getM_state() {
		return m_state;
	}
	public void setM_state(Boolean m_state) {
		this.m_state = m_state;
	}
	public String getM_videoUrl() {
		return m_videoUrl;
	}
	public void setM_videoUrl(String m_videoUrl) {
		this.m_videoUrl = m_videoUrl;
	}
	public Date getM_reportTime() {
		return m_reportTime;
	}
	public void setM_reportTime(Date m_reportTime) {
		this.m_reportTime = m_reportTime;
	}
	public Long getM_device_id() {
		return m_device_id;
	}
	public void setM_device_id(Long m_device_id) {
		this.m_device_id = m_device_id;
	}
	public List<Long> getIdList() {
		return idList;
	}
	public void setIdList(List<Long> idList) {
		this.idList = idList;
	}
	public Date getM_beginTime() {
		return m_beginTime;
	}
	public void setM_beginTime(Date m_beginTime) {
		this.m_beginTime = m_beginTime;
	}
	public Date getM_endTime() {
		return m_endTime;
	}
	public void setM_endTime(Date m_endTime) {
		this.m_endTime = m_endTime;
	}
	
}
