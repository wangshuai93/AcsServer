package com.yinhe.server.AcsServer.backbean;

import java.io.Serializable;

public class ParamGetBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6572776805957998687L;
	private Integer m_rowId; 
	private Long m_id;
	private String m_nodePath;
	private String taskName;
	
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
	public String getM_nodePath() {
		return m_nodePath;
	}
	public void setM_nodePath(String m_nodePath) {
		this.m_nodePath = m_nodePath;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
}
