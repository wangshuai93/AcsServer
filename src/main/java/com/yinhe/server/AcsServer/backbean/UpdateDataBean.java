package com.yinhe.server.AcsServer.backbean;

import java.util.Date;

public class UpdateDataBean {
	private int m_rowId;
	private String m_version;
	private String m_name;
	private Date m_date;
	private Boolean m_isCurrentVersion;

	public int getM_rowId() {
		return m_rowId;
	}

	public void setM_rowId(int m_rowId) {
		this.m_rowId = m_rowId;
	}

	public String getM_version() {
		return m_version;
	}

	public void setM_version(String m_version) {
		this.m_version = m_version;
	}

	public String getM_name() {
		return m_name;
	}

	public void setM_name(String m_name) {
		this.m_name = m_name;
	}

	public Date getM_date() {
		return m_date;
	}

	public void setM_date(Date m_date) {
		this.m_date = m_date;
	}

	public Boolean getM_isCurrentVersion() {
		return m_isCurrentVersion;
	}

	public void setM_isCurrentVersion(Boolean m_isCurrentVersion) {
		this.m_isCurrentVersion = m_isCurrentVersion;
	}

}
