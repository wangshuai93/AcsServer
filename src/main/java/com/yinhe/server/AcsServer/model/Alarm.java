package com.yinhe.server.AcsServer.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "alarm", catalog = "acssrv")
public class Alarm implements Serializable {

	private static final long serialVersionUID = -779156634191972786L;

	private Long m_id;
	private String m_description;
	private Date m_alarmTime;

	private Devices m_devices;
	private String m_paramPath;
	private String m_taskName;

	public Alarm() {
		
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	public Long getM_id() {
		return m_id;
	}

	public void setM_id(Long m_id) {
		this.m_id = m_id;
	}

	@Column(name = "description")
	public String getM_description() {
		return m_description;
	}

	public void setM_description(String m_description) {
		this.m_description = m_description;
	}

	@Column(name = "alarm_time", nullable = false, length = 19)
	public Date getM_alarmTime() {
		return m_alarmTime;
	}

	public void setM_alarmTime(Date m_alarmTime) {
		this.m_alarmTime = m_alarmTime;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "device_id", nullable = false)
	public Devices getM_devices() {
		return m_devices;
	}

	public void setM_devices(Devices m_devices) {
		this.m_devices = m_devices;
	}
    
	@Column(name = "param_path")
	public String getM_paramPath() {
		return m_paramPath;
	}

	public void setM_paramPath(String m_paramPath) {
		this.m_paramPath = m_paramPath;
	}
    
	@Column(name = "task_name")
	public String getM_taskName() {
		return m_taskName;
	}

	public void setM_taskName(String m_taskName) {
		this.m_taskName = m_taskName;
	}
}
