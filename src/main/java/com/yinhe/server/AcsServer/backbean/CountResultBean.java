package com.yinhe.server.AcsServer.backbean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CountResultBean implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1495174868450688364L;
	private int m_rowId;
	private Long m_id;
	private String m_result;
	private List<ResultDetailBean> result_list;
	private Date m_reportTime;
	
	public int getM_rowId() {
		return m_rowId;
	}
	public void setM_rowId(int m_rowId) {
		this.m_rowId = m_rowId;
	}
	public Long getM_id() {
		return m_id;
	}
	public void setM_id(Long m_id) {
		this.m_id = m_id;
	}
	public String getM_result() {
		return m_result;
	}
	public void setM_result(String m_result) {
		this.m_result = m_result;
	}
	public Date getM_reportTime() {
		return m_reportTime;
	}
	public void setM_reportTime(Date m_reportTime) {
		this.m_reportTime = m_reportTime;
	}
	public List<ResultDetailBean> getResult_list() {
		return result_list;
	}
	public void setResult_list(List<ResultDetailBean> result_list) {
		this.result_list = result_list;
	}
}
