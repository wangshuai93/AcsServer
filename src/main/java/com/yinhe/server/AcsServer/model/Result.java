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
@Table(name = "result",catalog="acssrv")
public class Result implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7257110684418999912L;
	private Long m_id;
	private NodeTask m_nodeTask;
	private String m_value;
	private Boolean m_alarm;
	private CountResult m_countResult;

	public Result() {

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "node_task_id", nullable = false)
	public NodeTask getM_nodeTask() {
		return m_nodeTask;
	}

	public void setM_nodeTask(NodeTask m_nodeTask) {
		this.m_nodeTask = m_nodeTask;
	}

	@Column(name = "value", length = 1024)
	public String getM_value() {
		return m_value;
	}

	public void setM_value(String m_value) {
		this.m_value = m_value;
	}

	@Column(name = "is_alarm")
	public Boolean getM_alarm() {
		return m_alarm;
	}

	public void setM_alarm(Boolean m_alarm) {
		this.m_alarm = m_alarm;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "count_result_id", nullable = false)
	public CountResult getM_countResult() {
		return m_countResult;
	}

	public void setM_countResult(CountResult m_countResult) {
		this.m_countResult = m_countResult;
	}
	
	@Override
	public String toString() {
		return m_nodeTask.getM_node().getAbbr_name() + ":" + m_value;
	}
}
