package com.yinhe.server.AcsServer.backbean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;


public class ParamSetBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1663947684407938459L;
	private Integer m_rowId; 
	private Long m_id;
	private String m_taskName;
	private String m_nodePath;
	private String m_type;
	//private NodeTask m_nodeTask;
	private String m_value;
	private String m_namezh;
	
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
	public String getM_taskName() {
		return m_taskName;
	}
	public void setM_taskName(String m_taskName) {
		this.m_taskName = m_taskName;
	}
	public String getM_nodePath() {
		return m_nodePath;
	}
	public void setM_nodePath(String m_nodePath) {
		this.m_nodePath = m_nodePath;
	}
	public String getM_value() {
		return m_value;
	}
	public void setM_value(String m_value) {
		this.m_value = m_value;
	}
	public String getM_type() {
		return m_type;
	}
	public void setM_type(String m_type) {
		this.m_type = m_type;
	}

	public String getM_namezh() {
		return m_namezh;
	}
	public void setM_namezh(String m_namezh) {
		this.m_namezh = m_namezh;
	}
}
