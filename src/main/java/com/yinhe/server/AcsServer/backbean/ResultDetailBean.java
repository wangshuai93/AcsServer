package com.yinhe.server.AcsServer.backbean;

import java.io.Serializable;

import com.yinhe.server.AcsServer.model.Result;

public class ResultDetailBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1040169160489584210L;

	private int m_rowId;
	
	private Long m_id;
	private String m_nodePath;
	private String m_taskName;
	//private NodeTask m_nodeTask;
	private String m_value;
	private Boolean is_alarm;
	
	public ResultDetailBean(){
		
	}
	public ResultDetailBean(Result result){
		this.m_id = result.getM_id();
		this.m_value = result.getM_value();
		this.is_alarm = result.getM_alarm();
	}
	
	public int getM_rowId() {
		return m_rowId;
	}
	public void setM_rowId(int m_rowId) {
		this.m_rowId = m_rowId;
	}
	public Long getM_id() {
		return m_id;
	}
	public void setM_id(Long m_id) {
		this.m_id = m_id;
	}
	public String getM_value() {
		return m_value;
	}
	public void setM_value(String m_value) {
		this.m_value = m_value;
	}
	public Boolean getIs_alarm() {
		return is_alarm;
	}
	public void setIs_alarm(Boolean is_alarm) {
		this.is_alarm = is_alarm;
	}

	public String getM_nodePath() {
		return m_nodePath;
	}

	public void setM_nodePath(String m_nodePath) {
		this.m_nodePath = m_nodePath;
	}

	public String getM_taskName() {
		return m_taskName;
	}

	public void setM_taskName(String m_taskName) {
		this.m_taskName = m_taskName;
	}
}
