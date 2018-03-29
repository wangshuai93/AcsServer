package com.yinhe.server.AcsServer.backbean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class RecordResultDetailBean  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3172840721358652826L;
	private int rowId;
	private Long id;
	private String nodePath;
	private String taskName;
	private List<String> value;
	private Boolean isAlarm;
	private Date reportTime;
	
	public int getRowId() {
		return rowId;
	}
	public void setRowId(int rowId) {
		this.rowId = rowId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNodePath() {
		return nodePath;
	}
	public void setNodePath(String nodePath) {
		this.nodePath = nodePath;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public List<String> getValue() {
		return value;
	}
	public void setValue(List<String> value) {
		this.value = value;
	}
	public Boolean getIsAlarm() {
		return isAlarm;
	}
	public void setIsAlarm(Boolean isAlarm) {
		this.isAlarm = isAlarm;
	}
	public Date getReportTime() {
		return reportTime;
	}
	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}
}
