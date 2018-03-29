package com.yinhe.server.AcsServer.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "node_task", catalog = "acssrv")
public class NodeTask implements Serializable {

	private static final long serialVersionUID = 8215661523149417439L;

	private Long m_id;
	private Node m_node;
	private Task m_task;

	private ParamSet paramSet;
	private ParamGet paramGet;
	private Set<Result> result = new HashSet<Result>();
	
	public NodeTask() {

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

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "node_id", nullable = false)
	public Node getM_node() {
		return m_node;
	}

	public void setM_node(Node m_node) {
		this.m_node = m_node;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "task_id", nullable = false)
	public Task getM_task() {
		return m_task;
	}

	public void setM_task(Task m_task) {
		this.m_task = m_task;
	}

	@OneToOne(mappedBy = "m_nodeTask", cascade = CascadeType.REMOVE)
	public ParamSet getParamSet() {
		return paramSet;
	}

	public void setParamSet(ParamSet paramSet) {
		this.paramSet = paramSet;
	}

	@OneToOne(mappedBy = "m_nodeTask", cascade = CascadeType.REMOVE)
	public ParamGet getParamGet() {
		return paramGet;
	}

	public void setParamGet(ParamGet paramGet) {
		this.paramGet = paramGet;
	}

	@OneToMany(mappedBy = "m_nodeTask", cascade = CascadeType.REMOVE)
	public Set<Result> getResult() {
		return result;
	}

	public void setResult(Set<Result> result) {
		this.result = result;
	}
	
	@Override
	public String toString() {
		return "NodeTask[m_id = " + m_id + "m_node.m_id = " + m_node.getM_id()
				+ "m_task.m_id = " + m_task.getM_id() + "]";
	}

	/*@OneToMany(mappedBy = "m_nodeTask", cascade = CascadeType.REMOVE)
	public Set<Alarm> getAlarms() {
		return alarms;
	}

	public void setAlarms(Set<Alarm> alarms) {
		this.alarms = alarms;
	}*/
}
