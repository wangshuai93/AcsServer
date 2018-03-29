package com.yinhe.server.AcsServer.backbean;

import java.io.Serializable;
import java.util.Date;


public class ResultQueryBean extends BaseQueryBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5093385802697673638L;
	private Long m_id;
	private Long m_taskId;
	private Long m_nodeId;
	private String m_value;
	private String m_serialNumber;
	private Date m_reportTime;
	private Boolean is_alarm;
	
	private Long m_device_id;
	private Date m_beginTime;
	private Date m_endTime;
	
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
	public String getM_serialNumber() {
		return m_serialNumber;
	}
	public void setM_serialNumber(String m_serialNumber) {
		this.m_serialNumber = m_serialNumber;
	}
	public Date getM_reportTime() {
		return m_reportTime;
	}
	public void setM_reportTime(Date m_reportTime) {
		this.m_reportTime = m_reportTime;
	}
	public Boolean getIs_alarm() {
		return is_alarm;
	}
	public void setIs_alarm(Boolean is_alarm) {
		this.is_alarm = is_alarm;
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
	public Long getM_device_id() {
		return m_device_id;
	}
	public void setM_device_id(Long m_device_id) {
		this.m_device_id = m_device_id;
	}
}
