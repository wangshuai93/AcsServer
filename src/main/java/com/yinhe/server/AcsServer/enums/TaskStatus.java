package com.yinhe.server.AcsServer.enums;

import com.yinhe.server.AcsServer.util.Resources;

public enum TaskStatus {
	RUNNING,
	STOPPED,
	UNKNOWN;
	
	public String toLocalString() {
		return Resources.getLocalName(this.name());
	}
}
