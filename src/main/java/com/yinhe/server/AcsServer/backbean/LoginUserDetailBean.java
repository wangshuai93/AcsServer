package com.yinhe.server.AcsServer.backbean;

public class LoginUserDetailBean {

	private Long m_userId; 
	private String m_userName;
	private String m_newPassword;
	private String m_newPasswordEx;
	private String m_phoneNumber;
	private String m_department;
	private String m_roleName;
	
	
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
	public String getM_newPassword() {
		return m_newPassword;
	}
	public void setM_newPassword(String m_newPassword) {
		this.m_newPassword = m_newPassword;
	}
	
	public String getM_newPasswordEx() {
		return m_newPasswordEx;
	}
	public void setM_newPasswordEx(String m_newPasswordEx) {
		this.m_newPasswordEx = m_newPasswordEx;
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
	@Override
	public String toString() {
		return "LoginUserDetailBean [m_userId=" + m_userId + ", m_userName="
				+ m_userName + ", m_newPasswordEx=" + m_newPasswordEx
				+ ", m_newPassword=" + m_newPassword + ", m_phoneNumber="
				+ m_phoneNumber + ", m_department=" + m_department
				+ ", m_roleName=" + m_roleName + "]";
	}
	
}
