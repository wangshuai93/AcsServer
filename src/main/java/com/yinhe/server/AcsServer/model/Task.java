package com.yinhe.server.AcsServer.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "task",catalog="acssrv")
public class Task implements Serializable {

	private static final long serialVersionUID = 2735994423320086736L;

	private Long m_id;
	private String m_name;
	private Set<NodeTask> nodeTask = new HashSet<>();
	private Set<DeviceTask> deviceTask = new HashSet<DeviceTask>();
	
	public Task() {
		
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getM_id() {
		return m_id;
	}

	public void setM_id(Long m_id) {
		this.m_id = m_id;
	}

	@Column(name = "name", unique = true, nullable = false)
	public String getM_name() {
		return m_name;
	}

	public void setM_name(String m_name) {
		this.m_name = m_name;
	}

	@OneToMany(mappedBy = "m_task", cascade = CascadeType.REMOVE)
	public Set<NodeTask> getNodeTask() {
		return nodeTask;
	}

	public void setNodeTask(Set<NodeTask> nodeTask) {
		this.nodeTask = nodeTask;
	}

	@OneToMany(mappedBy = "m_task", cascade = CascadeType.REMOVE)
	public Set<DeviceTask> getDeviceTask() {
		return deviceTask;
	}

	public void setDeviceTask(Set<DeviceTask> deviceTask) {
		this.deviceTask = deviceTask;
	}
	
}
