package com.yinhe.server.AcsServer.backbean;

import java.io.Serializable;

import com.yinhe.server.AcsServer.model.Node;

public class NodeModelDetailBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1584719561256081965L;
	private int m_rowId;
	private Long id;
	private String abbr_name;
	private String node_path;
	private String default_value;
	private String min_value;
	private String max_value;
	private String type;
	private String rw;
	private String getc;
	private String noc;
	private String nocc;
	private String il;
	private String acl;
	private String value_type;
	private String other_value;
	private String unit;
	private String nin;
	private String nameZh;
	
	private String paramSetting;

	public NodeModelDetailBean() {

	}

	public NodeModelDetailBean(Node nm) {
		this.id = nm.getM_id();
		this.abbr_name = nm.getAbbr_name();
		this.node_path = nm.getNode_path();
		this.default_value = nm.getDefault_value();
		this.min_value = nm.getMin_value();
		this.max_value = nm.getMax_value();
		this.type = nm.getType();
		this.rw = nm.getRw();
		this.getc = nm.getGetc();
		this.noc = nm.getNoc();
		this.nocc = nm.getNocc();
		this.il = nm.getIl();
		this.acl = nm.getAcl();
		this.value_type = nm.getValue_type();
		this.other_value = nm.getOther_value();
		this.unit = nm.getUnit();
		this.nin = nm.getNin();
		this.nameZh = nm.getNameZh();
	}

	public int getM_rowId() {
		return m_rowId;
	}

	public void setM_rowId(int m_rowId) {
		this.m_rowId = m_rowId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAbbr_name() {
		return abbr_name;
	}

	public void setAbbr_name(String abbr_name) {
		this.abbr_name = abbr_name;
	}

	public String getNode_path() {
		return node_path;
	}

	public void setNode_path(String node_path) {
		this.node_path = node_path;
	}

	public String getDefault_value() {
		return default_value;
	}

	public void setDefault_value(String default_value) {
		this.default_value = default_value;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRw() {
		return rw;
	}

	public void setRw(String rw) {
		this.rw = rw;
	}

	public String getGetc() {
		return getc;
	}

	public void setGetc(String getc) {
		this.getc = getc;
	}

	public String getNoc() {
		return noc;
	}

	public void setNoc(String noc) {
		this.noc = noc;
	}

	public String getNocc() {
		return nocc;
	}

	public void setNocc(String nocc) {
		this.nocc = nocc;
	}

	public String getIl() {
		return il;
	}

	public void setIl(String il) {
		this.il = il;
	}

	public String getAcl() {
		return acl;
	}

	public void setAcl(String acl) {
		this.acl = acl;
	}

	public String getValue_type() {
		return value_type;
	}

	public void setValue_type(String value_type) {
		this.value_type = value_type;
	}

	public String getOther_value() {
		return other_value;
	}

	public void setOther_value(String other_value) {
		this.other_value = other_value;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public String getNin() {
		return nin;
	}

	public void setNin(String nin) {
		this.nin = nin;
	}
	
	public String getNameZh() {
		return nameZh;
	}

	public void setNameZh(String nameZh) {
		this.nameZh = nameZh;
	}

	
	public String getParamSetting() {
		return paramSetting;
	}

	public void setParamSetting(String paramSetting) {
		this.paramSetting = paramSetting;
	}

	@Override
	public String toString() {
		return "NodeModelDetailBean [m_rowId=" + m_rowId + ", id=" + id
				+ ", abbr_name=" + abbr_name + ", node_path=" + node_path
				+ ", default_value=" + default_value + ", min_value="
				+ min_value + ", max_value=" + max_value + ", type=" + type
				+ ", rw=" + rw + ", getc=" + getc + ", noc=" + noc + ", nocc="
				+ nocc + ", il=" + il + ", acl=" + acl + ", value_type="
				+ value_type + ", other_value=" + other_value + ", unit="
				+ unit + ", nin=" + nin + "]";
	}

}
