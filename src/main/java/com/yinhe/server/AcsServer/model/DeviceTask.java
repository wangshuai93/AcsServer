package com.yinhe.server.AcsServer.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "device_task",catalog="acssrv")
public class DeviceTask implements Serializable {

	private static final long serialVersionUID = 2735994423320086736L;

	private Long m_id;
	private boolean m_is_period;
	private Long m_period_time;
	private String m_status;
	private Devices m_device;
	private Task m_task;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getM_id() {
		return m_id;
	}

	public void setM_id(Long m_id) {
		this.m_id = m_id;
	}

	@Column(name = "is_period")
	public boolean isM_is_period() {
		return m_is_period;
	}

	public void setM_is_period(boolean m_is_period) {
		this.m_is_period = m_is_period;
	}

	@Column(name = "period_time")
	public Long getM_period_time() {
		return m_period_time;
	}

	public void setM_period_time(Long m_period_time) {
		this.m_period_time = m_period_time;
	}

	@Column(name = "status", length = 255)
	public String getM_status() {
		return m_status;
	}

	public void setM_status(String m_status) {
		this.m_status = m_status;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "device_id", nullable = false)
	public Devices getM_device() {
		return m_device;
	}

	public void setM_device(Devices m_device) {
		this.m_device = m_device;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task_id", nullable = false)
	public Task getM_task() {
		return m_task;
	}

	public void setM_task(Task m_task) {
		this.m_task = m_task;
	}	
}
