package com.yinhe.server.AcsServer.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.yinhe.server.AcsServer.backbean.LoginUserStatusInfo;
import com.yinhe.server.AcsServer.data.UsersRespository;
import com.yinhe.server.AcsServer.ejb.UserManagerEJB;
import com.yinhe.server.AcsServer.enums.LoginErrorCode;
import com.yinhe.server.AcsServer.model.Users;
import com.yinhe.server.AcsServer.util.ControllerUtils;
import com.yinhe.server.AcsServer.util.Resources;

@Named
@RequestScoped
public class LoginBean implements Serializable {

	private static final long serialVersionUID = -205316518232172596L;

	@Inject
	private LoginUserStatusInfo m_loginUserStatusInfo;

	@Inject
	private UsersRespository m_usersRespository;

	@Inject
	private UserManagerEJB m_um;

	@Inject
	private Logger logger;

	@Inject
	private Logger m_log;

	private String m_roleName;

	private String m_userName;

	private String m_password;

	private String m_message;

	private String m_defaultPath;

	public static final String m_loginUserStatusInfoInSession = "loginUserStatusInfo";

	public static final String m_loginUserName = "loginUserName";
	public static final String m_loginIP = "loginIP";

	@PostConstruct
	private void init() {

		m_roleName = m_loginUserStatusInfo.getM_role();
		if (m_roleName == null || m_roleName.isEmpty()) {
			m_roleName = new String("");
		}
	}

	public void operatorLogin() {
		m_log.info("[operatorLogin] UserName = " + m_userName + ",password = "+ m_password);
		if (!validateInput()) {
			return;
		}
		
		if (!validateUserPass()) {
			m_log.info("[operatorLogin] validateUserPass() = false");
		} else {
			m_log.info("[operatorLogin] validateUserPass() = true");
			//判断是否重复登录
			String ip = ControllerUtils.getOperatorIpAddress();
			
			if(hasLogin(m_userName,ip)){
				m_log.info("[operatorLogin] hasLogin!");
				setM_message(LoginErrorCode.USER_HAS_LOGEDIN.toLocalString());
				return;
			}
			
			String fowardPage = operatorWhere2Go(m_defaultPath, m_userName);
			Map<String, Object> sessionMap = ControllerUtils.getSessionMap();
			sessionMap.put(LoginBean.m_loginUserName, m_userName);
			sessionMap.put(LoginBean.m_loginIP, ip);
			this.redirect(fowardPage);
		}

	}

	public void logout() {
		Map<String, Object> sessionMap = ControllerUtils.getSessionMap();
		sessionMap.remove(LoginBean.m_loginUserName);
		sessionMap.remove(LoginBean.m_loginIP);
		m_loginUserStatusInfo.reset();
		this.redirect("/index.jsf");
	}

	/**
	 * 用户名密码验证
	 * 
	 * @return
	 */
	private boolean validateUserPass() {
		// TODO Auto-generated method stub
		// hasLogin(username);
		LoginErrorCode loginErrorCode = m_um.operateLogin(m_userName,m_password, m_loginUserStatusInfo);
		setM_message(loginErrorCode.toLocalString());
		if (loginErrorCode.equals(LoginErrorCode.LOGIN_SUCCESSFULLY)) {
			return true;
		}
		return false;
	}
	
	private boolean hasLogin(String userName,String ip){
		Object employeeName = FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get(LoginBean.m_loginUserName + userName);
		if (employeeName != null) {
			m_log.info("[hasLogin] employeeName != null");
			Object loginIP = FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get(LoginBean.m_loginIP + ip);
			if(loginIP != null){
				m_log.info("[hasLogin] loginIP != null");
				return false;
			}else{
				return true;
			}
		}else{
			return  false;
		}
	}

	/**
	 * 输入验证
	 * 
	 * @return
	 */
	private boolean validateInput() {
		boolean result = true;
		if (Resources.isNull(m_userName)) {
			setM_message("用户名不能为空！");
			result = false;
		}

		if (Resources.isNull(m_password)) {
			setM_message("密码不能为空！");
			result = false;
		}

		if (Resources.isNull(m_userName) && Resources.isNull(m_password)) {
			setM_message("用户名和密码不能为空！");
			result = false;
		}
		return result;
	}

	private String operatorWhere2Go(String defaultPaths, String username) {
		m_roleName = m_loginUserStatusInfo.getM_role();
		logger.info("[operatorWhere2Go] m_roleName = " + m_roleName);
		Users user = m_usersRespository.findByUsername(username);
		if (user != null) {
			return "/admin/main_view.jsf";
		}
		return "void";
	}

	private void redirect(String path) {
		// log.info("redirect to :" + path);
		try {
			if ("void".equals(path)) {
				path = "/index.jsf";
			}
			ExternalContext ec = FacesContext.getCurrentInstance()
					.getExternalContext();
			ec.redirect(ec.getRequestContextPath() + path);
			m_log.info("[redirect] login redirect to :" + ec.getRequestContextPath()
					+ path);
		} catch (IOException e) {
			m_log.info("[redirect] redirect error!");
		}
	}

	public String getM_userName() {
		return m_userName;
	}

	public void setM_userName(String m_userName) {
		this.m_userName = m_userName;
	}

	public String getM_password() {
		return m_password;
	}

	public void setM_password(String m_password) {
		this.m_password = m_password;
	}

	public String getM_message() {
		return m_message;
	}

	public void setM_message(String m_message) {
		this.m_message = m_message;
	}

	public static String getmLoginusername() {
		return m_loginUserName;
	}
	

	public String getM_roleName() {
		return m_roleName;
	}

	public void setM_roleName(String m_roleName) {
		this.m_roleName = m_roleName;
	}

}
