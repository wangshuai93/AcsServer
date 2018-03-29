package com.yinhe.server.AcsServer.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "thresholdvalue", catalog = "acssrv")
public class ThresholdValue implements Serializable{
	
	private static final long serialVersionUID = 2094346478321397439L;
	
	private Long m_id;
	private String value;
	private String min_value;
	private String max_value;
	private Node nodeModel;
	
	public ThresholdValue() {
		
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	public Long getM_id() {
		return m_id;
	}

	public void setM_id(Long m_id) {
		this.m_id = m_id;
	}

	@Column(name="value")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name="min_value")
	public String getMin_value() {
		return min_value;
	}

	public void setMin_value(String min_value) {
		this.min_value = min_value;
	}

	@Column(name="max_value")
	public String getMax_value() {
		return max_value;
	}

	public void setMax_value(String max_value) {
		this.max_value = max_value;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "node_id", nullable = false)
	public Node getNodeModel() {
		return nodeModel;
	}

	public void setNodeModel(Node nodeModel) {
		this.nodeModel = nodeModel;
	}

}
