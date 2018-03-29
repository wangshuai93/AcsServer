package com.yinhe.server.AcsServer.backbean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QueryCPEBean extends BaseQueryBean {
	private Long m_id;
	private String m_status;
	private String m_ip;
	private Integer m_port;
	private String m_serialNumber;
	private String m_macAddress;
	private String m_manuFacturer;
	private String m_oui;
	private String m_productClass;
	private Date m_lastContact;
	private Boolean m_inNet;
	private Boolean m_isAlarm;

	private List<Long> idList = new ArrayList<>();

	public Long getM_id() {
		return m_id;
	}

	public void setM_id(Long m_id) {
		this.m_id = m_id;
	}

	public String getM_status() {
		return m_status;
	}

	public void setM_status(String m_status) {
		this.m_status = m_status;
	}

	public String getM_ip() {
		return m_ip;
	}

	public void setM_ip(String m_ip) {
		this.m_ip = m_ip;
	}

	public Integer getM_port() {
		return m_port;
	}

	public void setM_port(Integer m_port) {
		this.m_port = m_port;
	}

	public String getM_serialNumber() {
		return m_serialNumber;
	}

	public void setM_serialNumber(String m_serialNumber) {
		this.m_serialNumber = m_serialNumber;
	}

	public String getM_macAddress() {
		return m_macAddress;
	}

	public void setM_macAddress(String m_macAddress) {
		this.m_macAddress = m_macAddress;
	}

	public String getM_manuFacturer() {
		return m_manuFacturer;
	}

	public void setM_manuFacturer(String m_manuFacturer) {
		this.m_manuFacturer = m_manuFacturer;
	}

	public String getM_oui() {
		return m_oui;
	}

	public void setM_oui(String m_oui) {
		this.m_oui = m_oui;
	}

	public String getM_productClass() {
		return m_productClass;
	}

	public void setM_productClass(String m_productClass) {
		this.m_productClass = m_productClass;
	}

	public Date getM_lastContact() {
		return m_lastContact;
	}

	public void setM_lastContact(Date m_lastContact) {
		this.m_lastContact = m_lastContact;
	}

	public Boolean getM_inNet() {
		return m_inNet;
	}

	public void setM_inNet(Boolean m_inNet) {
		this.m_inNet = m_inNet;
	}

	public Boolean getM_isAlarm() {
		return m_isAlarm;
	}

	public void setM_isAlarm(Boolean m_isAlarm) {
		this.m_isAlarm = m_isAlarm;
	}

	public List<Long> getIdList() {
		return idList;
	}

	public void setIdList(List<Long> idList) {
		this.idList = idList;
	}

}
