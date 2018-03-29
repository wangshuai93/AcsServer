package com.yinhe.server.AcsServer.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "roles",catalog="acssrv")
public class Roles implements java.io.Serializable {

	private static final long serialVersionUID = 7036434186421712474L;

	private Long m_id;
	private String m_rolename;
	private String m_chrolename;
	

	public Roles() {

	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getM_id() {
		return m_id;
	}

	public void setM_id(Long id) {
		this.m_id = id;
	}

	@Column(name = "role_name")
	public String getM_rolename() {
		return m_rolename;
	}

	public void setM_rolename(String rolename) {
		this.m_rolename = rolename;
	}

	public String getM_chrolename() {
		return m_chrolename;
	}

	public void setM_chrolename(String m_chrolename) {
		this.m_chrolename = m_chrolename;
	}
}
