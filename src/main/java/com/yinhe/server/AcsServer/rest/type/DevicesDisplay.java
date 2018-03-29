package com.yinhe.server.AcsServer.rest.type;

public class DevicesDisplay{
	Long id;
	String name;
	String status;
	int cx;
	int cy;
	
	public DevicesDisplay(){
		
	}
	
	public DevicesDisplay(Long id, String name, String status, int cx, int cy) {
		super();
		this.id = id;
		this.name = name;
		this.status = status;
		this.cx = cx;
		this.cy = cy;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getCx() {
		return cx;
	}
	public void setCx(int cx) {
		this.cx = cx;
	}
	public int getCy() {
		return cy;
	}
	public void setCy(int cy) {
		this.cy = cy;
	}
	@Override
	public String toString() {
		return "TestResult [name=" + name + ", status=" + status + ", cx="
				+ cx + ", cy=" + cy + "]";
	}
	
}