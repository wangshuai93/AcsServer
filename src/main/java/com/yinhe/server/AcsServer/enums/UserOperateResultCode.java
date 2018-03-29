package com.yinhe.server.AcsServer.enums;

import com.yinhe.server.AcsServer.util.Resources;

public enum UserOperateResultCode {

	USER_EXISTED,
	USER_NOTEXIST,
	ADD_USER_SUCCESS,
	ADD_USER_FAILED,
	DELETE_USER_SUCCESS,
	DELETE_USER_FAILED,
	UPDATE_USER_SUCCESS,
	UPDATE_USER_FAILED,
	ROLE_NOEXIST;
	public String toLocalString() {
		return Resources.getLocalName("UserOperateResultCode." + this.name());
	}
}
