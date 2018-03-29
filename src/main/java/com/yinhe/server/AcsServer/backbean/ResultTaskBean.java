package com.yinhe.server.AcsServer.backbean;

import java.util.Date;

public class ResultTaskBean {

	private String m_paramName;
	private String m_paramValue;
	private Date m_reportTime;
	
	private Long m_id;

	public String getM_paramName() {
		return m_paramName;
	}

	public void setM_paramName(String m_paramName) {
		this.m_paramName = m_paramName;
	}

	public String getM_paramValue() {
		return m_paramValue;
	}

	public void setM_paramValue(String m_paramValue) {
		this.m_paramValue = m_paramValue;
	}

	public Date getM_reportTime() {
		return m_reportTime;
	}

	public void setM_reportTime(Date m_reportTime) {
		this.m_reportTime = m_reportTime;
	}

	
	@Override
	public String toString() {
		return "ResultTaskBean [m_paramName=" + m_paramName + ", m_paramValue="
				+ m_paramValue + ", m_reportTime=" + m_reportTime + "]";
	}

	public Long getM_id() {
		return m_id;
	}

	public void setM_id(Long m_id) {
		this.m_id = m_id;
	}
}
