package com.yinhe.server.AcsServer.enums;

import com.yinhe.server.AcsServer.util.Resources;

public enum DownloadEnum {
	//设备名和IP
	DOWNLOAD_SUCCESS,
	DOWNLOAD_FAILED,
	DOWNLOAD_FILE_NOT_EXIST,
	DOWNLOAD_CONNECT_FAILED;
		
	public String toLocalString() {
		return Resources.getLocalName(this.name());
	}
}
