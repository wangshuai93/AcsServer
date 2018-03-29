package com.yinhe.server.AcsServer.backbean;

import java.util.Date;

public class QueryTaskResultBean extends BaseQueryBean {

	private Date beforeTime;
	private Date afterTime;

	public Date getBeforeTime() {
		return beforeTime;
	}

	public void setBeforeTime(Date beforeTime) {
		this.beforeTime = beforeTime;
	}

	public Date getAfterTime() {
		return afterTime;
	}

	public void setAfterTime(Date afterTime) {
		this.afterTime = afterTime;
	}
}
