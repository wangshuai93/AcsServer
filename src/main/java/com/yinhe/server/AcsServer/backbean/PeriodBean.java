package com.yinhe.server.AcsServer.backbean;

import java.io.Serializable;

public class PeriodBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4866194100536858320L;
	private String m_key;
	private Long m_value;
	public PeriodBean(String key,Long value){
		this.m_key = key;
		this.m_value = value;
	}
	public String getM_key() {
		return m_key;
	}
	public void setM_key(String m_key) {
		this.m_key = m_key;
	}
	public Long getM_value() {
		return m_value;
	}
	public void setM_value(Long m_value) {
		this.m_value = m_value;
	}
}
