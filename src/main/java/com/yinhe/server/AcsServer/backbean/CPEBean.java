package com.yinhe.server.AcsServer.backbean;

import java.io.Serializable;
import java.util.Date;

public class CPEBean extends BaseQueryBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8730154762708504248L;
	private Integer m_rowId;
	private Long m_id;
	private String m_status;
	private String m_ip;
	private String m_port;
	private String m_serialNumber;
	private String m_macAddress;
	private String m_manuFacturer;
	private String m_oui;
	private String m_productClass;
	private Date m_lastContact;
	private Boolean m_inNet;
	private Boolean m_isAlarm;
	private Integer m_positionX;
	private Integer m_positionY;
	private String m_positionName;
	private String m_hardwareVersion;
	private String m_softwareVerison;
	private String m_cmmacAddress;
	private String m_deviceName;
	
	//硬件信息
	private String m_decoderManufacturer;
	private String m_decoderModel;
	private Long m_RAMSize;
	private Long m_flashSize;
	
	//运行状态
	private Long m_RAMRest;
	private Long m_flashRest;
	private Long m_IDLE;
	private Long m_systemRunTime;
	private Long m_RAMUsed;
	private Long m_CPULoad;	

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

	public String getM_status() {
		return m_status;
	}

	public void setM_status(String m_status) {
		this.m_status = m_status;
	}

	public String getM_ip() {
		return m_ip;
	}

	public void setM_ip(String m_ip) {
		this.m_ip = m_ip;
	}

	public String getM_serialNumber() {
		return m_serialNumber;
	}

	public void setM_serialNumber(String m_serialNumber) {
		this.m_serialNumber = m_serialNumber;
	}

	public String getM_macAddress() {
		return m_macAddress;
	}

	public void setM_macAddress(String m_macAddress) {
		this.m_macAddress = m_macAddress;
	}

	public String getM_manuFacturer() {
		return m_manuFacturer;
	}

	public void setM_manuFacturer(String m_manuFacturer) {
		this.m_manuFacturer = m_manuFacturer;
	}

	public String getM_oui() {
		return m_oui;
	}

	public void setM_oui(String m_oui) {
		this.m_oui = m_oui;
	}

	public String getM_productClass() {
		return m_productClass;
	}

	public void setM_productClass(String m_productClass) {
		this.m_productClass = m_productClass;
	}

	public Date getM_lastContact() {
		return m_lastContact;
	}

	public void setM_lastContact(Date m_lastContact) {
		this.m_lastContact = m_lastContact;
	}

	public Boolean getM_inNet() {
		return m_inNet;
	}

	public void setM_inNet(Boolean m_inNet) {
		this.m_inNet = m_inNet;
	}

	public Boolean getM_isAlarm() {
		return m_isAlarm;
	}

	public void setM_isAlarm(Boolean m_isAlarm) {
		this.m_isAlarm = m_isAlarm;
	}

	public String getM_port() {
		return m_port;
	}

	public void setM_port(String m_port) {
		this.m_port = m_port;
	}

	public Integer getM_positionX() {
		return m_positionX;
	}

	public void setM_positionX(Integer m_positionX) {
		this.m_positionX = m_positionX;
	}

	public Integer getM_positionY() {
		return m_positionY;
	}

	public void setM_positionY(Integer m_positionY) {
		this.m_positionY = m_positionY;
	}

	public String getM_positionName() {
		return m_positionName;
	}

	public void setM_positionName(String m_positionName) {
		this.m_positionName = m_positionName;
	}
	
	public String getM_hardwareVersion() {
		return m_hardwareVersion;
	}

	public void setM_hardwareVersion(String m_hardwareVersion) {
		this.m_hardwareVersion = m_hardwareVersion;
	}

	public String getM_softwareVerison() {
		return m_softwareVerison;
	}

	public void setM_softwareVerison(String m_softwareVerison) {
		this.m_softwareVerison = m_softwareVerison;
	}

	public String getM_cmmacAddress() {
		return m_cmmacAddress;
	}

	public void setM_cmmacAddress(String m_cmmacAddress) {
		this.m_cmmacAddress = m_cmmacAddress;
	}

	
	public String getM_decoderManufacturer() {
		return m_decoderManufacturer;
	}

	public void setM_decoderManufacturer(String m_decoderManufacturer) {
		this.m_decoderManufacturer = m_decoderManufacturer;
	}

	public String getM_decoderModel() {
		return m_decoderModel;
	}

	public void setM_decoderModel(String m_decoderModel) {
		this.m_decoderModel = m_decoderModel;
	}

	public Long getM_RAMSize() {
		return m_RAMSize;
	}

	public void setM_RAMSize(Long m_RAMSize) {
		this.m_RAMSize = m_RAMSize;
	}

	public Long getM_flashSize() {
		return m_flashSize;
	}

	public void setM_flashSize(Long m_flashSize) {
		this.m_flashSize = m_flashSize;
	}

	public Long getM_RAMRest() {
		return m_RAMRest;
	}

	public void setM_RAMRest(Long m_RAMRest) {
		this.m_RAMRest = m_RAMRest;
	}

	public Long getM_flashRest() {
		return m_flashRest;
	}

	public void setM_flashRest(Long m_flashRest) {
		this.m_flashRest = m_flashRest;
	}

	public Long getM_IDLE() {
		return m_IDLE;
	}

	public void setM_IDLE(Long m_IDLE) {
		this.m_IDLE = m_IDLE;
	}

	public Long getM_systemRunTime() {
		return m_systemRunTime;
	}

	public void setM_systemRunTime(Long m_systemRunTime) {
		this.m_systemRunTime = m_systemRunTime;
	}

	public Long getM_RAMUsed() {
		return m_RAMUsed;
	}

	public void setM_RAMUsed(Long m_RAMUsed) {
		this.m_RAMUsed = m_RAMUsed;
	}

	public Long getM_CPULoad() {
		return m_CPULoad;
	}

	public void setM_CPULoad(Long m_CPULoad) {
		this.m_CPULoad = m_CPULoad;
	}
	
	public String getM_deviceName() {
		return m_deviceName;
	}

	public void setM_deviceName(String m_deviceName) {
		this.m_deviceName = m_deviceName;
	}

	@Override
	public String toString() {
		return "CPEBean [m_rowId=" + m_rowId + ", m_id=" + m_id + ", m_status="
				+ m_status + ", m_ip=" + m_ip + ", m_port=" + m_port
				+ ", m_serialNumber=" + m_serialNumber + ", m_macAddress="
				+ m_macAddress + ", m_manuFacturer=" + m_manuFacturer
				+ ", m_oui=" + m_oui + ", m_productClass=" + m_productClass
				+ ", m_lastContact=" + m_lastContact + ", m_inNet=" + m_inNet
				+ ", m_isAlarm=" + m_isAlarm + ", m_positionX=" + m_positionX
				+ ", m_positionY=" + m_positionY + ", m_positionName="
				+ m_positionName + ", m_hardwareVersion="
						+ m_hardwareVersion + ", m_softwareVerison="
								+ m_softwareVerison + "]";
	}
	
}
