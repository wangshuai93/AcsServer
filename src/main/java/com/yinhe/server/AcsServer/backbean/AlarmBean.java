package com.yinhe.server.AcsServer.backbean;

import java.util.Date;

import com.yinhe.server.AcsServer.model.Devices;

public class AlarmBean {

	private Integer m_rowId;
	private Long m_id;
	private Date m_alarmTime;
	private String m_description;
	private Devices m_devices;
	private String m_paramPath;
	private String m_taskName;
	
	public AlarmBean() {

	}

	public Long getM_id() {
		return m_id;
	}

	public void setM_id(Long m_id) {
		this.m_id = m_id;
	}

	public String getM_description() {
		return m_description;
	}

	public void setM_description(String m_description) {
		this.m_description = m_description;
	}

	public Devices getM_devices() {
		return m_devices;
	}

	public void setM_devices(Devices m_devices) {
		this.m_devices = m_devices;
	}
	
	public String getM_paramPath() {
		return m_paramPath;
	}

	public void setM_paramPath(String m_paramPath) {
		this.m_paramPath = m_paramPath;
	}

	public String getM_taskName() {
		return m_taskName;
	}

	public void setM_taskName(String m_taskName) {
		this.m_taskName = m_taskName;
	}

	public Integer getM_rowId() {
		return m_rowId;
	}

	public void setM_rowId(Integer m_rowId) {
		this.m_rowId = m_rowId;
	}

	public Date getM_alarmTime() {
		return m_alarmTime;
	}

	public void setM_alarmTime(Date m_alarmTime) {
		this.m_alarmTime = m_alarmTime;
	}
}
