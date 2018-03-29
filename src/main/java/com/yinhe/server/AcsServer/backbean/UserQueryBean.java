package com.yinhe.server.AcsServer.backbean;

import java.util.ArrayList;
import java.util.List;

public class UserQueryBean extends BaseQueryBean{

	private Long m_id;
	private String m_userName;
	private String m_phoneNumber;
	private String m_department;
	private List<Long> idList = new ArrayList<>();
	
	public List<Long> getIdList() {
		return idList;
	}
	public void setIdList(List<Long> idList) {
		this.idList = idList;
	}
	
	public Long getM_id() {
		return m_id;
	}
	public void setM_id(Long m_id) {
		this.m_id = m_id;
	}
	
	public String getM_userName() {
		return m_userName;
	}
	public void setM_userName(String m_userName) {
		this.m_userName = m_userName;
	}
	public String getM_phoneNumber() {
		return m_phoneNumber;
	}
	public void setM_phoneNumber(String m_phoneNumber) {
		this.m_phoneNumber = m_phoneNumber;
	}
	public String getM_department() {
		return m_department;
	}
	public void setM_department(String m_department) {
		this.m_department = m_department;
	}
	
	
}
