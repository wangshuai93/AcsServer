package com.yinhe.server.AcsServer.backbean;

import java.io.Serializable;

import com.yinhe.server.AcsServer.model.NodeTask;

public class ParamSetQueryBean extends BaseQueryBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -339011518331109374L;
	private Integer m_rowId; 
	private Long m_id;
	private NodeTask m_nodeTask;
	private String m_value;
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
	public NodeTask getM_nodeTask() {
		return m_nodeTask;
	}
	public void setM_nodeTask(NodeTask m_nodeTask) {
		this.m_nodeTask = m_nodeTask;
	}
	public String getM_value() {
		return m_value;
	}
	public void setM_value(String m_value) {
		this.m_value = m_value;
	}
	
}
