package com.yinhe.server.AcsServer.enums;

import com.yinhe.server.AcsServer.util.Resources;

public enum ThresholdValueResultCode {
	SET_THRESHOLDVALUE_SUCCESS,
	SET_THRESHOLDVALUE_FAILED,
	UPDATE_THRESHOLDVALUE_SUCCESS,
	UPDATE_THRESHOLDVALUE_FAILED,
	DELETE_THRESHOLDVALUE_SUCCESS,
	DELETE_THRESHOLDVALUE_FAILED;
	
	public String toLocalString() {
		return Resources.getLocalName(this.name());
	}
}
