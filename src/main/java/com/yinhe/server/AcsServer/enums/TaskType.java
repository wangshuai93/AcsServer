package com.yinhe.server.AcsServer.enums;

import com.yinhe.server.AcsServer.util.Resources;

public enum TaskType {
	BROADCAST_TASK, 
	HTTP_TASK, 
	PING_TASK, 
	TCP_UDP_TASK, 
	REC_TASK;
	
	public String toLocalString() {
		return Resources.getLocalName(this.name());
	}
}
