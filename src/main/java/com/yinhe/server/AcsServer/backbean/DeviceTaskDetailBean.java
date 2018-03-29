package com.yinhe.server.AcsServer.backbean;

import com.yinhe.server.AcsServer.model.DeviceTask;
import com.yinhe.server.AcsServer.model.Devices;

public class DeviceTaskDetailBean {
	private int m_rowId;
	private Long m_id;
	private boolean m_is_period;
	private Long m_period_time;
	private String m_status;
	private Long m_taskId;
	private String m_taskName;
	private Devices m_device;
	
	public DeviceTaskDetailBean(DeviceTask device_task){
		this.m_id = device_task.getM_id();
		this.m_is_period = device_task.isM_is_period();
		this.m_period_time = device_task.getM_period_time();
		this.m_status = device_task.getM_status();
		this.m_device = device_task.getM_device();
		this.m_taskName = device_task.getM_task().getM_name();
		this.m_taskId = device_task.getM_task().getM_id();
	}
	
	public int getM_rowId() {
		return m_rowId;
	}
	public void setM_rowId(int m_rowId) {
		this.m_rowId = m_rowId;
	}
	public Long getM_id() {
		return m_id;
	}
	public void setM_id(Long m_id) {
		this.m_id = m_id;
	}
	public boolean isM_is_period() {
		return m_is_period;
	}
	public void setM_is_period(boolean m_is_period) {
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

	public Long getM_taskId() {
		return m_taskId;
	}

	public void setM_taskId(Long m_taskId) {
		this.m_taskId = m_taskId;
	}

	public String getM_taskName() {
		return m_taskName;
	}

	public void setM_taskName(String m_taskName) {
		this.m_taskName = m_taskName;
	}
}
