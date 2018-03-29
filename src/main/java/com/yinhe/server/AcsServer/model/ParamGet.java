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
@Table(name = "param_get",catalog="acssrv")
public class ParamGet implements Serializable{

	private static final long serialVersionUID = 1951474899896266045L;
	
	private Long m_id;
	private NodeTask m_nodeTask;

	public ParamGet() {
		
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
	@JoinColumn(name = "node_task_id",  unique = true, nullable = false)
	public NodeTask getM_nodeTask() {
		return m_nodeTask;
	}

	public void setM_nodeTask(NodeTask m_nodeTask) {
		this.m_nodeTask = m_nodeTask;
	}

}
