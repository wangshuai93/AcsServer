package com.yinhe.server.AcsServer.backbean;

import java.util.ArrayList;
import java.util.List;

public class TaskQueryBean extends BaseQueryBean{
	private Long m_id;
	private String m_name;
	private List<Long> m_idList = new ArrayList<>();
	public Long getM_id() {
		return m_id;
	}
	public void setM_id(Long m_id) {
		this.m_id = m_id;
	}
	public String getM_name() {
		return m_name;
	}
	public void setM_name(String m_name) {
		this.m_name = m_name;
	}
	public List<Long> getM_idList() {
		return m_idList;
	}
	public void setM_idList(List<Long> m_idList) {
		this.m_idList = m_idList;
	}
}
