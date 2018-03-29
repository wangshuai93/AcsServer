package com.yinhe.server.AcsServer.backbean;

import java.io.Serializable;

import com.yinhe.server.AcsServer.model.Task;

public class TaskBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2659988462097891216L;
	private Integer m_rowId;
	private Long m_id;
	private String m_name;
	private String ch_name;
	
	public TaskBean(){
		
	}
	
	public TaskBean(Task task){
		this.m_id = task.getM_id();
		this.m_name = task.getM_name();
	}

	public Long getM_id() {
		return m_id;
	}

	public void setM_id(Long m_id) {
		this.m_id = m_id;
	}

	public String getM_name() {
		return m_name;
	}

	public void setM_name(String m_name) {
		this.m_name = m_name;
	}

	public Integer getM_rowId() {
		return m_rowId;
	}

	public void setM_rowId(Integer m_rowId) {
		this.m_rowId = m_rowId;
	}

	public String getCh_name() {
		return ch_name;
	}

	public void setCh_name(String ch_name) {
		this.ch_name = ch_name;
	}

}
