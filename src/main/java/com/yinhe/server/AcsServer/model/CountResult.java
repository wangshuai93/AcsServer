package com.yinhe.server.AcsServer.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "count_result",catalog="acssrv")
public class CountResult  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2651163596484782260L;
	private Long m_id;
	private Devices m_device;
	private Date m_reportTime;
	private Set<Result> result = new HashSet<Result>();

	
	public CountResult(){
		
	}
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getM_id() {
		return m_id;
	}

	public void setM_id(Long m_id) {
		this.m_id = m_id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "device_id", nullable = false)
	public Devices getM_device() {
		return m_device;
	}

	public void setM_device(Devices m_device) {
		this.m_device = m_device;
	}
	
	@Column(name = "m_reportTime", nullable = false, length = 19)
	public Date getM_reportTime() {
		return m_reportTime;
	}

	public void setM_reportTime(Date m_reportTime) {
		this.m_reportTime = m_reportTime;
	}
	
	@OneToMany(mappedBy = "m_countResult", cascade = CascadeType.REMOVE)
	public Set<Result> getResult() {
		return result;
	}
	public void setResult(Set<Result> result) {
		this.result = result;
	}
}
