package com.yinhe.server.AcsServer.backbean;

import java.util.List;

public class SetParameterAttributesBean {
	private String name;					// 为空代表顶级路径
    private int notification;				// [0:2]
    private boolean notificationChange;
    private boolean accessListChange;
    private List<String> accesslist;
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNotification() {
		return notification;
	}
	public void setNotification(int notification) {
		this.notification = notification;
	}
	public boolean isNotificationChange() {
		return notificationChange;
	}
	public void setNotificationChange(boolean notificationChange) {
		this.notificationChange = notificationChange;
	}
	public boolean isAccessListChange() {
		return accessListChange;
	}
	public void setAccessListChange(boolean accessListChange) {
		this.accessListChange = accessListChange;
	}
	public List<String> getAccesslist() {
		return accesslist;
	}
	public void setAccesslist(List<String> accesslist) {
		this.accesslist = accesslist;
	}
    
}
