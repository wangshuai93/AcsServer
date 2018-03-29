package com.yinhe.server.AcsServer.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.yinhe.server.AcsServer.backbean.LoginUserDetailBean;
import com.yinhe.server.AcsServer.backbean.LoginUserStatusInfo;
import com.yinhe.server.AcsServer.data.UsersRespository;
import com.yinhe.server.AcsServer.ejb.UserManagerEJB;
import com.yinhe.server.AcsServer.enums.PersonalInfoErrorCode;
import com.yinhe.server.AcsServer.model.Users;
import com.yinhe.server.AcsServer.util.ControllerUtils;
import com.yinhe.server.AcsServer.util.Resources;

@Named
@RequestScoped
public class PesonalInfoController implements Serializable{

	private static final long serialVersionUID = 1L;
	@Inject
	private Logger logger;
	private String m_message;
	
	@Inject
	private LoginUserDetailBean detailBean;
	
	@Inject
	private LoginUserStatusInfo m_loginUserStatusInfo;
	
	@Inject
	private UsersRespository m_usersRespository;
	
	@Inject 
	private UserManagerEJB userManagerEJB;
	
	
	
	public void getPersonalInfoDetail() {
		if(m_loginUserStatusInfo == null 
				|| Resources.isNullOrEmpty(m_loginUserStatusInfo.getM_userName())){
			m_message = "未登录";
			this.redirect("/index.jsf");
		}
		Users user = m_usersRespository.findByUsername(m_loginUserStatusInfo.getM_userName());
		if(user == null){
			m_message = "未登录";
			this.redirect("/index.jsf");
		}
		detailBean.setM_userId(user.getM_id());
		detailBean.setM_department(user.getM_department());
		detailBean.setM_phoneNumber(user.getM_phoneNumber());
		detailBean.setM_roleName(m_loginUserStatusInfo.getM_role());
		detailBean.setM_userName(user.getM_userName());
	}

	public String updatePassword(){
		m_message = "";
		logger.info("[updatePassword] "+detailBean.toString());
		if(Resources.isNullOrEmpty(detailBean.getM_userId()+"")){
			getPersonalInfoDetail();
			return "";
		}
		if(Resources.isNull(detailBean.getM_newPassword())){
			m_message = "输入密码为空";
			getPersonalInfoDetail();
			return "";
		}
		PersonalInfoErrorCode errorCode = userManagerEJB.ModifyPassword(detailBean);
		m_message = errorCode.toLocalString();
		logger.info(m_message);
		if (!errorCode.name().contains("SUCCESS")) {
			getPersonalInfoDetail();
			m_message = errorCode.toLocalString();
			return "";
		} else {
			Map<String, Object> sessionMap = ControllerUtils.getSessionMap();
			sessionMap.remove(LoginBean.m_loginUserName);
			m_loginUserStatusInfo.reset();
			this.redirect("/index.jsf");
			return "";
		}
	}
	public String gotoModifyPage(){
		getPersonalInfoDetail();
		logger.info("[gotoModifyPage] "+detailBean.toString());
		switch (m_loginUserStatusInfo.getM_role()) {
		case "common_admin":
		case "device_admin":
		case "system_admin":
			return "/admin/modify_pwd.jsf";
		default:
			return "";

		}
	}
	public String getM_message() {
		return m_message;
	}

	public void setM_message(String m_message) {
		this.m_message = m_message;
	}

	public LoginUserDetailBean getDetailBean() {
		return detailBean;
	}

	public void setDetailBean(LoginUserDetailBean detailBean) {
		this.detailBean = detailBean;
	}
	
	

	private void redirect(String path) {
		// log.info("redirect to :" + path);
		try {
			if ("void".equals(path)) {
				path = "/index.jsf";
			}
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(ec.getRequestContextPath() + path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}
	
	/**========================================================**
	 *  更新session,防止session超时
	 **========================================================**/
	public void updateSession() {
		
	}
	
}
