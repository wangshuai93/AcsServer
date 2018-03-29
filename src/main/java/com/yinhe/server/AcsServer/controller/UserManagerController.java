package com.yinhe.server.AcsServer.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.yinhe.server.AcsServer.backbean.UserDetailBean;
import com.yinhe.server.AcsServer.backbean.UserQueryBean;
import com.yinhe.server.AcsServer.ejb.RolesManagerEJB;
import com.yinhe.server.AcsServer.ejb.UserManagerEJB;
import com.yinhe.server.AcsServer.enums.PersonalInfoErrorCode;
import com.yinhe.server.AcsServer.enums.UserOperateResultCode;
import com.yinhe.server.AcsServer.model.Roles;
import com.yinhe.server.AcsServer.model.Users;
import com.yinhe.server.AcsServer.util.ControllerUtils;
import com.yinhe.server.AcsServer.util.Resources;
@Named
@RequestScoped
public class UserManagerController {

	@Inject 
	private Logger logger;
	@Inject
	private UserDetailBean m_userDetailBean;
	@Inject
	private UserQueryBean m_queryBean;
	@Inject
	private UserManagerEJB m_ume;
	@Inject
	private RolesManagerEJB rolesManagerEJB;
	
	String [] ch_roleList = {"系统管理员","设备管理员","普通管理员"};
	private List<UserDetailBean> m_userRecordList;
	private List<String> roleList ;
	private List<Roles> rolesList;
	private String m_message;
	
	private Map<String, Object> session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	
	@PostConstruct
	private void init(){
		logger.info("[init] userManagerController init");
		String o = getRequestParamMap().get("load");
		rolesList = rolesManagerEJB.getRoleList();
		roleList = new ArrayList<String>();
		int i =0;
		if(rolesList != null){
			for(Roles role:rolesList){
				//roleList.add(role.getM_rolename());
				
				roleList.add(ch_roleList[i]);
				i++;
				
			}
		}
		if (o != null && "lazy".equals(o)) {
			logger.info("[init] UserManagerController load: lazy!");
		} else {
			if((FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath()).contains("main_view.jsf")){
				m_queryBean.setSize(2);
			}
			queryRecordList();
		}
		//systemManageEJB.initUserCarReleQueryBean(queryBean);
	}
	private void queryRecordList() {
		logger.info("[queryRecordList] m_queryBean.getCurrent() == " + m_queryBean.getCurrent());
		logger.info("[queryRecordList] m_queryBean.getStartPosition() == " + m_queryBean.getStartPosition());
		logger.info("[queryRecordList] m_queryBean.getSize() == " + m_queryBean.getSize());
		m_userRecordList = m_ume.listUserRecords(m_queryBean);
	}
	
	public void queryCurrentPage() {
		this.queryRecordList();
		// return "";
	}

	public void queryPreviousPage() {
		m_queryBean.previous();
		this.queryRecordList();
		// return "";
	}

	public void queryNextPage() {
		m_queryBean.next();
		this.queryRecordList();
		// return "";
	}
	
	public String addUser(){
		//logger.info("手机号输入有误\n");
		if(Resources.isNullOrEmpty(m_userDetailBean.getM_userName())){
			m_message = "输入用户名为空";
			return "";
		}
		
		if(Resources.isNullOrEmpty(m_userDetailBean.getM_password())){
			m_message = "输入密码为空";
			return "";
		}
		
		if(Resources.isNullOrEmpty(m_userDetailBean.getM_passwordEx())){
			m_message = "输入确认密码为空";
			return "";
		}
		
		if(!m_userDetailBean.getM_password().equals(m_userDetailBean.getM_passwordEx())){
			m_message = "密码不匹配";
			return "";
		}
		
		//判断手机号格式
		
		/*if(m_userDetailBean.getM_phoneNumber().equals(null)){
			m_message = "手机号不能为空";
			return "";
		}else if(!isPhoneNumber(m_userDetailBean.getM_phoneNumber())){
			m_message = "手机号输入有误";
			logger.info("手机号输入有误\n");
			return "";
		}
		*/
		logger.info("m_userDetailBean.getM_roleName() = "+m_userDetailBean.getM_roleName());
		if(m_userDetailBean.getM_roleName().equals("系统管理员")){
			m_userDetailBean.setM_roleName("system_admin");
		}
		if(m_userDetailBean.getM_roleName().equals("设备管理员")){
			m_userDetailBean.setM_roleName("device_admin");
		}
		if(m_userDetailBean.getM_roleName().equals("普通管理员")){
			m_userDetailBean.setM_roleName("common_admin");
		}
		UserOperateResultCode code = m_ume.addUser(m_userDetailBean);
		
		if (!code.name().contains("SUCCESS")) {
			m_message = code.toLocalString();
			return "";
		} else {
			m_message= code.toLocalString();
			this.redirect("/admin/all_users.jsf");
			return "";
		}
		
	}
	
	public String deleteUser(){			
		// 获取网页中的 detail_serial_number
		String user_id = getRequestParamMap().get("user_id");
		logger.info("[deleteUser] userId = " + user_id);
		m_userDetailBean.setM_userId(Long.parseLong(user_id));
		UserOperateResultCode userOperateResultCode = m_ume.deleteUser(m_userDetailBean);
		
		if (null == userOperateResultCode) {
			m_message = UserOperateResultCode.DELETE_USER_FAILED.toLocalString();
		} else {
			m_message = userOperateResultCode.toLocalString();
			queryRecordList();
			return "";
		}
		return "";
	}
	
	public String goToUpdateUserPassword(){
		String user_id = getRequestParamMap().get("user_id");
		if(!Resources.isNullOrEmpty(user_id)){
			logger.info("[goToUpdateUserPassword] user_id = " + user_id);
			m_userDetailBean = m_ume.findUserById(Long.parseLong(user_id));
			if(m_userDetailBean != null){
				session.put("userId", m_userDetailBean.getM_userId());
			}
		}
		return "modify_user_pwd.jsf";
	}
	
	public String updatePassword(){
		m_message = "";
		Long userId  = (Long)session.get("userId");
		if(userId != null){
			m_userDetailBean.setM_userId(userId);
		}
		logger.info("[updatePassword] " + m_userDetailBean.toString());
		if(Resources.isNull(m_userDetailBean.getM_password())){
			m_message = PersonalInfoErrorCode.PASSWORD_INPUT_NULL.toLocalString();
			m_userDetailBean = m_ume.findUserById(userId);
			return "";
		}
		PersonalInfoErrorCode errorCode = m_ume.ModifyUserPassword(m_userDetailBean);
		m_userDetailBean = m_ume.findUserById(userId);
		m_message = errorCode.toLocalString();
		return "";
	}
	
	public void updateSession() {
		logger.info("[updateSession] do updateSession()");
	}
	
	private Map<String, String> getRequestParamMap() {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	}
	
	public List<UserDetailBean> getM_userRecordList() {
		return m_userRecordList;
	}
	public void setM_userRecordList(List<UserDetailBean> m_userRecordList) {
		this.m_userRecordList = m_userRecordList;
	}
	public UserDetailBean getM_userDetailBean() {
		return m_userDetailBean;
	}
	public void setM_userDetailBean(UserDetailBean m_userDetailBean) {
		this.m_userDetailBean = m_userDetailBean;
	}
	public UserQueryBean getM_queryBean() {
		return m_queryBean;
	}
	public void setM_queryBean(UserQueryBean m_queryBean) {
		this.m_queryBean = m_queryBean;
	}
	public String getM_message() {
		return m_message;
	}
	public void setM_message(String m_message) {
		this.m_message = m_message;
	}
	
	public List<String> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}
	public List<Roles> getRolesList() {
		return rolesList;
	}
	public void setRolesList(List<Roles> rolesList) {
		this.rolesList = rolesList;
	}
	private void redirect(String path) {
		// log.info("[redirect] redirect to :" + path);
		try {
			if ("void".equals(path)) {
				path = "/index.jsf";
			}
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(ec.getRequestContextPath() + path);
		} catch (IOException e) {
			
		}
	}
	private boolean isPhoneNumber(String phone_number){
		//130~139、145、147、15[0-9]、17[0-1]、173、17[6-8]、18[0-9]
//		String regExp = "^1([3][0-9]{1}|59|58|88|89)[0-9]{8}$";
		
		String regExp = "^(13[0-9]|145|147|15[0-9]|17[013678]|18[0-9])\\d{8}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(phone_number);
		
		return m.find();
		//return true;
	}
	
	@PreDestroy
	private void destroy() {
		logger.info("[destroy] system userManagerController record will be destoryed!\n");
	}
	
}
