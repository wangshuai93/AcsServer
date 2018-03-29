package com.yinhe.server.AcsServer.backbean;

import java.util.Date;

public class RecordResultDetailBean2 {
	private int rowId;
	private Long id;
	private String nodePath;
	private String taskName;
	private String value;
	private Boolean isAlarm;
	private Date reportTime;
	
	private Boolean hasDownload;
	private String fileName;

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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
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

	public Boolean getHasDownload() {
		return hasDownload;
	}

	public void setHasDownload(Boolean hasDownload) {
		this.hasDownload = hasDownload;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
