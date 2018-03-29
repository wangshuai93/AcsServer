package com.yinhe.server.AcsServer.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "node", catalog = "acssrv")
public class Node implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1081899612382758292L;
	private Long m_id;
	private String abbr_name;
	private String type;
	private String node_path;
	private String default_value;
	private String min_value;
	private String max_value;
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

	// to delete
	private NodeTask nodeTask;
	private ThresholdValue thresholdValue;

	public Node() {

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getM_id() {
		return m_id;
	}

	public void setM_id(Long m_id) {
		this.m_id = m_id;
	}

	@Column(name = "abbr_name", nullable = false)
	public String getAbbr_name() {
		return abbr_name;
	}

	public void setAbbr_name(String abbr_name) {
		this.abbr_name = abbr_name;
	}

	@Column(name = "type", nullable = false)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "node_path", nullable = false, unique = true)
	public String getNode_path() {
		return node_path;
	}

	public void setNode_path(String node_path) {
		this.node_path = node_path;
	}

	@Column(name = "default_value")
	public String getDefault_value() {
		return default_value;
	}

	public void setDefault_value(String default_value) {
		this.default_value = default_value;
	}

	@Column(name = "min_value")
	public String getMin_value() {
		return min_value;
	}

	public void setMin_value(String min_value) {
		this.min_value = min_value;
	}

	@Column(name = "max_value")
	public String getMax_value() {
		return max_value;
	}

	public void setMax_value(String max_value) {
		this.max_value = max_value;
	}

	@Column(name = "rw")
	public String getRw() {
		return rw;
	}

	public void setRw(String rw) {
		this.rw = rw;
	}

	@Column(name = "getc")
	public String getGetc() {
		return getc;
	}

	public void setGetc(String getc) {
		this.getc = getc;
	}

	@Column(name = "noc")
	public String getNoc() {
		return noc;
	}

	public void setNoc(String noc) {
		this.noc = noc;
	}

	@Column(name = "nocc")
	public String getNocc() {
		return nocc;
	}

	public void setNocc(String nocc) {
		this.nocc = nocc;
	}

	@Column(name = "il")
	public String getIl() {
		return il;
	}

	public void setIl(String il) {
		this.il = il;
	}

	@Column(name = "acl")
	public String getAcl() {
		return acl;
	}

	public void setAcl(String acl) {
		this.acl = acl;
	}

	@Column(name = "value_type")
	public String getValue_type() {
		return value_type;
	}

	public void setValue_type(String value_type) {
		this.value_type = value_type;
	}

	@Column(name = "other_value")
	public String getOther_value() {
		return other_value;
	}

	public void setOther_value(String other_value) {
		this.other_value = other_value;
	}

	@Column(name = "unit")
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Column(name = "nin")
	public String getNin() {
		return nin;
	}

	public void setNin(String nin) {
		this.nin = nin;
	}

	@OneToOne(mappedBy = "m_node", cascade = CascadeType.REMOVE)
	public NodeTask getNodeTask() {
		return nodeTask;
	}

	public void setNodeTask(NodeTask nodeTask) {
		this.nodeTask = nodeTask;
	}

	@OneToOne(mappedBy = "nodeModel", cascade = CascadeType.REMOVE)
	public ThresholdValue getThresholdValue() {
		return thresholdValue;
	}

	public void setThresholdValue(ThresholdValue thresholdValue) {
		this.thresholdValue = thresholdValue;
	}
	
	@Column(name = "name_zh")
	public String getNameZh() {
		return nameZh;
	}

	public void setNameZh(String nameZh) {
		this.nameZh = nameZh;
	}
	
	@Override
	public String toString() {
		return "Node [m_id=" + m_id + ", abbr_name=" + abbr_name + ", type="
				+ type + ", node_path=" + node_path + ", default_value="
				+ default_value + ", min_value=" + min_value + ", max_value="
				+ max_value + ", rw=" + rw + ", getc=" + getc + ", noc=" + noc
				+ ", nocc=" + nocc + ", il=" + il + ", acl=" + acl
				+ ", value_type=" + value_type + ", other_value=" + other_value
				+ ", unit=" + unit + ", nin=" + nin + ", nodeTask=" + nodeTask
				+ ", thresholdValue=" + thresholdValue + "]";
	}

}
