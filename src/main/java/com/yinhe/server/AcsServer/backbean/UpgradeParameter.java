package com.yinhe.server.AcsServer.backbean;

public class UpgradeParameter {
	private String commandKey;
    private String fileType;
    private String url;
    private String userName;
    private String passWord;
    private String targetFileName;
    private String successURL;
    private String failureURL;
    private Long fileSize;
    private Integer delaySeconds;
    
	public String getCommandKey() {
		return commandKey;
	}
	public void setCommandKey(String commandKey) {
		this.commandKey = commandKey;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getTargetFileName() {
		return targetFileName;
	}
	public void setTargetFileName(String targetFileName) {
		this.targetFileName = targetFileName;
	}
	public String getSuccessURL() {
		return successURL;
	}
	public void setSuccessURL(String successURL) {
		this.successURL = successURL;
	}
	public String getFailureURL() {
		return failureURL;
	}
	public void setFailureURL(String failureURL) {
		this.failureURL = failureURL;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	public Integer getDelaySeconds() {
		return delaySeconds;
	}
	public void setDelaySeconds(Integer delaySeconds) {
		this.delaySeconds = delaySeconds;
	}
    
    
}
