package com.yinhe.server.AcsServer.backbean;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@SessionScoped
public class LoginUserStatusInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7044614953408292299L;

	@Inject
	private Logger m_log;
	
	private String m_role;
	
	private String m_userName;
	
	private Long m_userId;
	
	private String m_roleNameZh;
	
	public final static int defaultSessionTimeout = 3;
	
	private boolean m_autoLogout = true;
	
	private int m_muniteForLogout = defaultSessionTimeout;
	
	public void reset(){
		m_role = null;
		m_userName = null;
		m_autoLogout = true;
		m_muniteForLogout = defaultSessionTimeout;
	}

	public String getM_role() {
		return m_role;
	}

	public void setM_role(String m_role) {
		this.m_role = m_role;
	}

	
	public String getM_roleNameZh() {
		m_roleNameZh = getM_roleNameZhByRole();
		return m_roleNameZh;
	}

	private String getM_roleNameZhByRole() {
		// TODO Auto-generated method stub
		switch (m_role) {
		case "system_admin":
			return "系统管理员";
		case "device_admin":
			return "设备管理员";
		case "common_admin":
			return "普通管理员";
		default:
			break;
		}
		return "";
	}

	public void setmRoleNameZh(String mRoleNameZh) {
		this.m_roleNameZh = mRoleNameZh;
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

	public boolean isM_autoLogout() {
		return m_autoLogout;
	}

	public void setM_autoLogout(boolean m_autoLogout) {
		this.m_autoLogout = m_autoLogout;
	}

	public int getM_muniteForLogout() {
		return m_muniteForLogout;
	}

	public void setM_muniteForLogout(int m_muniteForLogout) {
		this.m_muniteForLogout = m_muniteForLogout;
	}

	@Override
	public String toString() {
		return "LoginUserStatusInfo [m_log=" + m_log + ", m_role=" + m_role
				+ ", m_userName=" + m_userName + ", m_userId=" + m_userId
				+ ", m_autoLogout=" + m_autoLogout + ", m_muniteForLogout="
				+ m_muniteForLogout + "]";
	}
	
}
