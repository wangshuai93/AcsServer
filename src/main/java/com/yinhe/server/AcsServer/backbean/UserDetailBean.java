package com.yinhe.server.AcsServer.backbean;

import com.yinhe.server.AcsServer.model.Users;

public class UserDetailBean {

	private int m_rowId;
	private Long m_userId;
	private String m_userName;
	private String m_phoneNumber;
	private String m_department;
	private String m_roleName;
	private String m_password;
	private String m_passwordEx;
	
	
	public UserDetailBean() {
		super();
	}
	
	public UserDetailBean(Users user) {
		this.m_userId = user.getM_id();
		this.m_userName = user.getM_userName();
		this.m_department = user.getM_department();
		this.m_phoneNumber = user.getM_phoneNumber();
		this.m_roleName = user.getM_roles().getM_rolename();
	}
	public int getM_rowId() {
		return m_rowId;
	}
	public void setM_rowId(int m_rowId) {
		this.m_rowId = m_rowId;
	}
	public Long getM_userId() {
		return m_userId;
	}
	public void setM_userId(Long m_userId) {
		this.m_userId = m_userId;
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

	public String getM_roleName() {
		return m_roleName;
	}

	public void setM_roleName(String m_roleName) {
		this.m_roleName = m_roleName;
	}

	
	public String getM_password() {
		return m_password;
	}

	public void setM_password(String m_password) {
		this.m_password = m_password;
	}

	public String getM_passwordEx() {
		return m_passwordEx;
	}

	public void setM_passwordEx(String m_passwordEx) {
		this.m_passwordEx = m_passwordEx;
	}

	@Override
	public String toString() {
		return "UserDetailBean [m_rowId=" + m_rowId + ", m_userId=" + m_userId
				+ ", m_userName=" + m_userName + ", m_phoneNumber="
				+ m_phoneNumber + ", m_department=" + m_department
				+ ", m_roleName=" + m_roleName + ", m_password=" + m_password
				+ ", m_passwordEx=" + m_passwordEx + "]";
	}
	
	
}
