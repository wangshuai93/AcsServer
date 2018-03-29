package com.yinhe.server.AcsServer.backbean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ManagedResultBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3460461905410570538L;
	private Long m_rowId;
	private String m_abbrName;
	private List<ResultDetailBean> m_resultDetailList;
	private List<String> m_sortedValue;
	private Date m_reportTime;
	
	private Long m_countResultId;
	
	public Long getM_rowId() {
		return m_rowId;
	}
	public void setM_rowId(Long m_rowId) {
		this.m_rowId = m_rowId;
	}
	public Date getM_reportTime() {
		return m_reportTime;
	}
	public void setM_reportTime(Date m_reportTime) {
		this.m_reportTime = m_reportTime;
	}
	public String getM_abbrName() {
		return m_abbrName;
	}
	public void setM_abbrName(String m_abbrName) {
		this.m_abbrName = m_abbrName;
	}
	public List<ResultDetailBean> getM_resultDetailList() {
		return m_resultDetailList;
	}
	public void setM_resultDetailList(List<ResultDetailBean> m_resultDetailList) {
		this.m_resultDetailList = m_resultDetailList;
	}
	public List<String> getM_sortedValue() {
		return m_sortedValue;
	}
	public void setM_sortedValue(List<String> m_sortedValue) {
		this.m_sortedValue = m_sortedValue;
	}
	@Override
	public String toString() {
		return "ManagedResultBean [m_rowId=" + m_rowId + ", m_abbrName="
				+ m_abbrName + ", m_resultDetailList=" + m_resultDetailList
				+ ", m_sortedValue=" + m_sortedValue + ", m_reportTime="
				+ m_reportTime + "]";
	}
	public Long getM_countResultId() {
		return m_countResultId;
	}
	public void setM_countResultId(Long m_countResultId) {
		this.m_countResultId = m_countResultId;
	}
}
