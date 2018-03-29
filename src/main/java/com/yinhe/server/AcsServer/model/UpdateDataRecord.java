package com.yinhe.server.AcsServer.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "updatedatarecord", catalog = "acssrv")
public class UpdateDataRecord implements Serializable {

	private static final long serialVersionUID = 5810768863992436242L;
	private Long m_id;
	private String m_fileName;
	private Date m_updateTime;
	private Boolean m_isCurrentVersion;
	
	public UpdateDataRecord(){
		
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

	@Column(name = "file_name", nullable = false, unique=true)
	public String getM_fileName() {
		return m_fileName;
	}

	public void setM_fileName(String m_fileName) {
		this.m_fileName = m_fileName;
	}

	@Column(name = "update_time", nullable = false, length = 19)
	public Date getM_updateTime() {
		return m_updateTime;
	}

	public void setM_updateTime(Date m_updateTime) {
		this.m_updateTime = m_updateTime;
	}
	
	@Column(name = "is_current_version")
	public Boolean getM_isCurrentVersion() {
		return m_isCurrentVersion;
	}

	public void setM_isCurrentVersion(Boolean m_isCurrentVersion) {
		this.m_isCurrentVersion = m_isCurrentVersion;
	}

}
