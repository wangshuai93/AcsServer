package com.yinhe.server.AcsServer.backbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NodeModelQueryBean extends BaseQueryBean implements Serializable{
	
	private static final long serialVersionUID = 2961184417761961267L;
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
	private List<Long> idList = new ArrayList<>();
	
	public Long getM_id() {
		return m_id;
	}
	public void setM_id(Long m_id) {
		this.m_id = m_id;
	}
	public String getAbbr_name() {
		return abbr_name;
	}
	public void setAbbr_name(String abbr_name) {
		this.abbr_name = abbr_name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public List<Long> getIdList() {
		return idList;
	}
	public void setIdList(List<Long> idList) {
		this.idList = idList;
	}
}
