package com.yinhe.server.AcsServer.backbean;

import com.yinhe.server.AcsServer.model.Devices;
import com.yinhe.server.AcsServer.model.NodeTask;

public class AlarmQueryBean extends BaseQueryBean {

	private Integer m_rowId;
	private Long m_id;
	private String m_description;
	private Devices m_devices;
	private NodeTask m_nodeTask;

	public AlarmQueryBean() {
		
	}

	public Integer getM_rowId() {
		return m_rowId;
	}

	public void setM_rowId(Integer m_rowId) {
		this.m_rowId = m_rowId;
	}

	public Long getM_id() {
		return m_id;
	}

	public void setM_id(Long m_id) {
		this.m_id = m_id;
	}

	public String getM_description() {
		return m_description;
	}

	public void setM_description(String m_description) {
		this.m_description = m_description;
	}

	public Devices getM_devices() {
		return m_devices;
	}

	public void setM_devices(Devices m_devices) {
		this.m_devices = m_devices;
	}

	public NodeTask getM_nodeTask() {
		return m_nodeTask;
	}

	public void setM_nodeTask(NodeTask m_nodeTask) {
		this.m_nodeTask = m_nodeTask;
	}
}
