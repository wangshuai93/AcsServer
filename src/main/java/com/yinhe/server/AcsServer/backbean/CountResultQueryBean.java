package com.yinhe.server.AcsServer.backbean;

import java.io.Serializable;
import java.util.Date;

public class CountResultQueryBean extends BaseQueryBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -281365318207842385L;
	private String m_serialNumber;
	private Long m_taskId;
	private Long m_nodeId;
	private Long m_countResultId;
	
	private Date m_beginTime;
	private Date m_endTime;
	
	public String getM_serialNumber() {
		return m_serialNumber;
	}

	public void setM_serialNumber(String m_serialNumber) {
		this.m_serialNumber = m_serialNumber;
	}

	public Long getM_taskId() {
		return m_taskId;
	}

	public void setM_taskId(Long m_taskId) {
		this.m_taskId = m_taskId;
	}

	public Long getM_nodeId() {
		return m_nodeId;
	}

	public void setM_nodeId(Long m_nodeId) {
		this.m_nodeId = m_nodeId;
	}

	public Long getM_countResultId() {
		return m_countResultId;
	}

	public void setM_countResultId(Long m_countResultId) {
		this.m_countResultId = m_countResultId;
	}

	public Date getM_beginTime() {
		return m_beginTime;
	}

	public void setM_beginTime(Date m_beginTime) {
		this.m_beginTime = m_beginTime;
	}

	public Date getM_endTime() {
		return m_endTime;
	}

	public void setM_endTime(Date m_endTime) {
		this.m_endTime = m_endTime;
	}

}
