package com.yinhe.server.AcsServer.backbean;

public class RPCParameterBean {
	private String method_name;
	private String param_name;
	private String type;
	
	//SetParameterValue
	private String value_set;
	private String value_instance;
	
	//GetParameterName
	private Boolean next_level;
	
	//SetParameterAttributes
    private int notification;				// [0:2]
    private boolean notificationChange;
    private boolean accessListChange;
	private String accessList;
	
	public String getMethod_name() {
		return method_name;
	}
	public void setMethod_name(String method_name) {
		this.method_name = method_name;
	}
	public String getParam_name() {
		return param_name;
	}
	public void setParam_name(String param_name) {
		this.param_name = param_name;
	}
	public String getValue_set() {
		return value_set;
	}
	public void setValue_set(String value_set) {
		this.value_set = value_set;
	}
	public String getValue_instance() {
		return value_instance;
	}
	public void setValue_instance(String value_instance) {
		this.value_instance = value_instance;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAccessList() {
		return accessList;
	}
	public void setAccessList(String accessList) {
		this.accessList = accessList;
	}
	public Boolean getNext_level() {
		return next_level;
	}
	public void setNext_level(Boolean next_level) {
		this.next_level = next_level;
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
}
