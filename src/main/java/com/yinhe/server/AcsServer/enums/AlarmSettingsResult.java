package com.yinhe.server.AcsServer.enums;

import com.yinhe.server.AcsServer.util.Resources;

public enum AlarmSettingsResult {
	ALARM_SETTINGS_SUCCESS,
	ALARM_SETTINGS_FAILED;
	
	public String toLocalString() {
		return Resources.getLocalName(this.name());
	}
}
