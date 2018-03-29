package com.yinhe.server.AcsServer.backbean;

import java.io.Serializable;

public class ColumnNameBean implements Serializable {

	private static final long serialVersionUID = 1225918640930019443L;
	private String m_name;
	private String m_unit;

	public String getM_name() {
		return m_name;
	}

	public void setM_name(String m_name) {
		this.m_name = m_name;
	}

	public String getM_unit() {
		return m_unit;
	}

	public void setM_unit(String m_unit) {
		this.m_unit = m_unit;
	}
}
