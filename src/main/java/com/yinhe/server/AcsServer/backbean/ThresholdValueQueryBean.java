package com.yinhe.server.AcsServer.backbean;

import java.io.Serializable;

import com.yinhe.server.AcsServer.model.Node;

public class ThresholdValueQueryBean extends BaseQueryBean implements Serializable {

	private static final long serialVersionUID = -1555644936221663946L;

	private int m_rowId;
	private Long m_id;
	private String value;
	private String min_value;
	private String max_value;
	private Node nodeModel;

	public ThresholdValueQueryBean() {

	}

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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getMin_value() {
		return min_value;
	}

	public void setMin_value(String min_value) {
		this.min_value = min_value;
	}

	public String getMax_value() {
		return max_value;
	}

	public void setMax_value(String max_value) {
		this.max_value = max_value;
	}

	public Node getNodeModel() {
		return nodeModel;
	}

	public void setNodeModel(Node nodeModel) {
		this.nodeModel = nodeModel;
	}

}
