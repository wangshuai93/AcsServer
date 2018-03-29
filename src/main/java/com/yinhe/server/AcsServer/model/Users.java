package com.yinhe.server.AcsServer.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "users",catalog="acssrv")
public class Users implements java.io.Serializable {

	private static final long serialVersionUID = -5202951135211969449L;

	private Long m_id;
	private String m_userName;
	private String m_password;
	private String m_phoneNumber;
	private String m_department;
	private Roles m_roles;

	public Users() {

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
	
	
	@Column(name = "username", nullable = false, length = 20)
	public String getM_userName() {
		return m_userName;
	}

	public void setM_userName(String userName) {
		this.m_userName = userName;
	}

	@Column(name = "password", nullable = false)
	public String getM_password() {
		return m_password;
	}

	public void setM_password(String password) {
		this.m_password = password;
	}

	@Column(name = "phone_number", unique = true, nullable = false, length = 12)
	public String getM_phoneNumber() {
		return m_phoneNumber;
	}

	public void setM_phoneNumber(String phoneNumber) {
		this.m_phoneNumber = phoneNumber;
	}

	@Column(name = "department", length = 20)
	public String getM_department() {
		return m_department;
	}

	public void setM_department(String department) {
		this.m_department = department;
	}

	@ManyToOne(fetch = FetchType.LAZY )
	@JoinColumn(name = "role_id", nullable = false)
	public Roles getM_roles() {
		return m_roles;
	}

	public void setM_roles(Roles roles) {
		this.m_roles = roles;
	}

	
}
