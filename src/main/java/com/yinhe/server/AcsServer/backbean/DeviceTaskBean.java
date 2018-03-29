package com.yinhe.server.AcsServer.backbean;

import java.io.Serializable;

import com.yinhe.server.AcsServer.model.Devices;
import com.yinhe.server.AcsServer.model.Task;

public class DeviceTaskBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3924363351031524949L;
	private Integer m_rowId;
	private Long m_id;
	private String m_status;
	private Boolean m_is_period;
	private Long m_period_time;
	private Devices m_device;
	private Task m_task;

	private Long m_wait_time;

	public DeviceTaskBean() {

	}

	public Integer getM_rowId() {
		return m_rowId;
	}

	public void setM_rowId(Integer m_rowId) {
		this.m_rowId = m_rowId;
	}

	public Long getM_id() {
		return m_id;
	}

	public void setM_id(Long m_id) {
		this.m_id = m_id;
	}

	public Boolean getM_is_period() {
		return m_is_period;
	}

	public void setM_is_period(Boolean m_is_period) {
		this.m_is_period = m_is_period;
	}

	public Long getM_period_time() {
		return m_period_time;
	}

	public void setM_period_time(Long m_period_time) {
		this.m_period_time = m_period_time;
	}

	public String getM_status() {
		return m_status;
	}

	public void setM_status(String m_status) {
		this.m_status = m_status;
	}

	public Devices getM_device() {
		return m_device;
	}

	public void setM_device(Devices m_device) {
		this.m_device = m_device;
	}

	public Long getM_wait_time() {
		return m_wait_time;
	}

	public void setM_wait_time(Long m_wait_time) {
		this.m_wait_time = m_wait_time;
	}

	public Task getM_task() {
		return m_task;
	}

	public void setM_task(Task m_task) {
		this.m_task = m_task;
	}
}
