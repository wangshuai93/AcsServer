package com.yinhe.server.AcsServer.backbean;

import java.util.ArrayList;
import java.util.List;

import com.yinhe.server.AcsServer.model.Devices;
import com.yinhe.server.AcsServer.model.Task;

public class DeviceTaskQueryBean  extends BaseQueryBean{
	private Long m_id;
	private boolean m_is_period;
	private Long m_period_time;
	private String m_status;
	private Devices m_device;
	private Task m_task;

	private List<Long> idList = new ArrayList<>();

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

	public Task getM_task() {
		return m_task;
	}

	public void setM_task(Task m_task) {
		this.m_task = m_task;
	}

	public List<Long> getIdList() {
		return idList;
	}

	public void setIdList(List<Long> idList) {
		this.idList = idList;
	}
	
	
}
