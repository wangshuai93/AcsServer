package com.yinhe.server.AcsServer.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "param_set",catalog="acssrv")
public class ParamSet implements Serializable {

	private static final long serialVersionUID = -2493701676370038721L;

	private Long m_id;
	private NodeTask m_nodeTask;
	private String m_value;

	public ParamSet() {
		
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getM_id() {
		return m_id;
	}

	public void setM_id(Long m_id) {
		this.m_id = m_id;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "node_task_id", unique = true,nullable = false)
	public NodeTask getM_nodeTask() {
		return m_nodeTask;
	}

	public void setM_nodeTask(NodeTask m_nodeTask) {
		this.m_nodeTask = m_nodeTask;
	}

	@Column(name = "value")
	public String getM_value() {
		return m_value;
	}

	public void setM_value(String m_value) {
		this.m_value = m_value;
	}
	
}
