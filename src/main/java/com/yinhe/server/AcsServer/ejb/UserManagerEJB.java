package com.yinhe.server.AcsServer.ejb;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.yinhe.server.AcsServer.backbean.LoginUserDetailBean;
import com.yinhe.server.AcsServer.backbean.LoginUserStatusInfo;
import com.yinhe.server.AcsServer.backbean.UserDetailBean;
import com.yinhe.server.AcsServer.backbean.UserQueryBean;
import com.yinhe.server.AcsServer.data.RolesRespository;
import com.yinhe.server.AcsServer.data.UsersRespository;
import com.yinhe.server.AcsServer.enums.LoginErrorCode;
import com.yinhe.server.AcsServer.enums.PersonalInfoErrorCode;
import com.yinhe.server.AcsServer.enums.UserOperateResultCode;
import com.yinhe.server.AcsServer.model.Roles;
import com.yinhe.server.AcsServer.model.Users;
import com.yinhe.server.AcsServer.util.AESUtils;
import com.yinhe.server.AcsServer.util.Base64Utils;
import com.yinhe.server.AcsServer.util.Resources;

@Stateless
public class UserManagerEJB {

	@Inject
	private UsersRespository usersRespository;
	@Inject
	private RolesManagerEJB rolesManagerEJB;
	@Inject
	private Logger log;
	
	@Inject
	private RolesRespository rolesRespository;

	
	public LoginErrorCode operateLogin(String userName,String password, LoginUserStatusInfo loginUserStatusInfo){
		String passwordBase64 = "";
		try {
			// 16进制字符串Base64编码压缩
			byte[] bytes = AESUtils.hex2Byte(password);
			passwordBase64 = Base64Utils.encode(bytes);
		} catch (Exception e) {
			log.info("[operateLogin] password to base64 error:" + e.getStackTrace()[0]);
		}
		
		Users user = usersRespository.findByUsername(userName);
		
		if(user == null){
			return LoginErrorCode.USERNAME_NOT_EXIST;
		}
		log.info("[operateLogin] passwordBase64:" +passwordBase64);
		log.info("[operateLogin] pass:" +user.getM_password());
		if(!passwordBase64.equals(user.getM_password())){
			return LoginErrorCode.USERNAME_PASSWORD_NOT_MATCH;
		}else{
			loginUserStatusInfo.setM_userId(user.getM_id());
			loginUserStatusInfo.setM_role(user.getM_roles().getM_rolename());
			loginUserStatusInfo.setM_userName(userName);
			return LoginErrorCode.LOGIN_SUCCESSFULLY;
		}
		
	}
	
	public PersonalInfoErrorCode ModifyPassword(LoginUserDetailBean detailBean){
		
		if(!Resources.isNullOrEmpty(detailBean.getM_userName())){
			return PersonalInfoErrorCode.NOT_LOGIN; //未登录
		}
		if (!Resources.isNullOrEmpty(detailBean.getM_newPassword()) && 
				!detailBean.getM_newPassword().equals(detailBean.getM_newPasswordEx())) {
			return PersonalInfoErrorCode.PASSWORD_NOT_MATCH; // 密码不匹配
		}
		if (!Resources.isNullOrEmpty(detailBean.getM_newPasswordEx()) && 
				!detailBean.getM_newPasswordEx().equals(detailBean.getM_newPassword())) {
			return PersonalInfoErrorCode.PASSWORD_NOT_MATCH; // 密码不匹配
		}
		Users user = usersRespository.findById(detailBean.getM_userId());

		if(user == null || Resources.isNullOrEmpty(user.getM_userName())){
			return PersonalInfoErrorCode.USER_NOT_EXIST; //未登录
		}
		
		String encodePassword = Resources.encode(detailBean.getM_newPassword());
		user.setM_password(encodePassword);
		
		try {
			usersRespository.updateEntity(user);
			return PersonalInfoErrorCode.MODIFY_PERSONAL_INFO_SUCCESS;
		} catch (Exception e) {
			return PersonalInfoErrorCode.MODIFY_PERSONAL_INFO_FAILED;
		} 
		
	}
	
	public void operatorLogout(String username, String clientIp, String computerName) {
		Users user = usersRespository.findByUsername(username);
		if (null != user && null != user.getM_id()) {
		
		} else {
			log.info("[operatorLogout] Logout error: username [" + username + "] not exist");
		}
	}
	
	public List<UserDetailBean> listUserRecords(UserQueryBean queryBean){
		
		queryBean.setMaxByCount(usersRespository.countRelevances(queryBean.getM_userName(),
				queryBean.getM_phoneNumber(), queryBean.getM_department()));
		List<Users> userList = usersRespository.listUsers(queryBean.getM_userName(),
				queryBean.getM_phoneNumber(), queryBean.getM_department(),queryBean.getStartPosition(),queryBean.getSize());
		if(userList == null || userList.size() == 0){
			log.info("[operatorLogout] userList.size() == 0");
			return null;
		}else{
			List<UserDetailBean> userRecordList = new ArrayList<UserDetailBean>();
			int rowId = 0;
			for(Users user:userList){
				UserDetailBean detailBean = new UserDetailBean(user);
				
				detailBean.setM_rowId(++rowId);
				userRecordList.add(detailBean);
			}
			return userRecordList;
		}
	}

	public UserOperateResultCode addUser(UserDetailBean m_userDetailBean) {
		Users user = usersRespository.findByUserName(m_userDetailBean.getM_userName());
		if(user != null){
			return UserOperateResultCode.USER_EXISTED;
		}
		
		Roles role = rolesRespository.findByRoleName(m_userDetailBean.getM_roleName());
		
		if(role == null){
			return UserOperateResultCode.ROLE_NOEXIST;
		}
		
		Users adduser = new Users();

		adduser.setM_userName(m_userDetailBean.getM_userName());
		adduser.setM_phoneNumber(m_userDetailBean.getM_phoneNumber());
		String encodePassword = Resources.encode(m_userDetailBean.getM_password());
		adduser.setM_password(encodePassword);
		adduser.setM_roles(role);
		adduser.setM_department(m_userDetailBean.getM_department());
		try {
			usersRespository.addUsers(adduser);
			return UserOperateResultCode.ADD_USER_SUCCESS;
		} catch (Exception e) {
			return UserOperateResultCode.ADD_USER_FAILED;
		} 
	}
	
	public UserOperateResultCode deleteUser(UserDetailBean m_userDetailBean){		
		Users user = usersRespository.findById(m_userDetailBean.getM_userId());				
		try {
			usersRespository.deleteUsers(user);
			return UserOperateResultCode.DELETE_USER_SUCCESS;
		} catch (Exception e) {
			return UserOperateResultCode.DELETE_USER_FAILED;
		} 
	}
	
	public UserDetailBean findUserById(Long userId){
		if(userId != null){
			UserDetailBean userDetailBean = new UserDetailBean();
			Users user = usersRespository.findById(userId);
			if(user != null){
				userDetailBean.setM_department(user.getM_department());
//				userDetailBean.setM_password(user.getM_password());
				userDetailBean.setM_phoneNumber(user.getM_phoneNumber());
				Roles role = rolesManagerEJB.findRoleById(user.getM_roles().getM_id());
				if(role != null){
					userDetailBean.setM_roleName(role.getM_rolename());
				}
				userDetailBean.setM_userId(user.getM_id());
				userDetailBean.setM_userName(user.getM_userName());
				return userDetailBean;
			}
		}
		return null;
	}
	
	public Users findUser(Long userId){
		if(userId != null){
			Users user = usersRespository.findById(userId);
			return user;
		}
		return null;
	}
	
	public PersonalInfoErrorCode ModifyUserPassword(UserDetailBean userDetailBean){
		if (!Resources.isNullOrEmpty(userDetailBean.getM_password()) && 
				!userDetailBean.getM_password().equals(userDetailBean.getM_passwordEx())) {
			findUserById(userDetailBean.getM_userId());
			return PersonalInfoErrorCode.PASSWORD_NOT_MATCH; // 密码不匹配
		}
		if (!Resources.isNullOrEmpty(userDetailBean.getM_passwordEx()) && 
				!userDetailBean.getM_passwordEx().equals(userDetailBean.getM_password())) {
			findUserById(userDetailBean.getM_userId());
			return PersonalInfoErrorCode.PASSWORD_NOT_MATCH; // 密码不匹配
		}
		Users user = findUser(userDetailBean.getM_userId());
		String encodePassword = Resources.encode(userDetailBean.getM_password());
		user.setM_password(encodePassword);
		try {
			usersRespository.updateEntity(user);
			return PersonalInfoErrorCode.MODIFY_PERSONAL_INFO_SUCCESS;
		} catch (Exception e) {
			return PersonalInfoErrorCode.MODIFY_PERSONAL_INFO_FAILED;
		} 
	}

}
