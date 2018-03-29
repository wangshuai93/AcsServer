package com.yinhe.server.AcsServer.enums;

import com.yinhe.server.AcsServer.util.Resources;

public enum LoginErrorCode {

	LOGIN_TIME_ERROR,
	DECODE_ERROR,
	USER_HAS_LOGEDIN,
	USERNAME_IS_NULL,
	PASSWORD_IS_NULL,
	USERNAME_NOT_EXIST,
	USERNAME_PASSWORD_NOT_MATCH,
	USER_ROLE_NOT_MATCH,
	PROVIDER_NOT_EXIST,
	LOGIN_SUCCESSFULLY,
	PROVIDERNAME_NOT_EXIT;

	public String toLocalString() {
		return Resources.getLocalName(this.name());
	}
}
