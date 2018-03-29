package com.yinhe.server.AcsServer.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@Entity
@Table(name = "devices",catalog="acssrv",uniqueConstraints={@UniqueConstraint(columnNames="serial_number")})
public class Devices implements java.io.Serializable {

	private static final long serialVersionUID = 3650450947869915768L;

	private Long m_id;
	private String m_status;
	private String m_ip;
	private String m_port;
	private String m_macAddress;
	private String m_serialNumber;
	private String m_manuFacturer;
	private String m_oui;
	private String m_productClass;
	private Date m_lastContact;
	private Boolean m_inNet;
	private Boolean m_isAlarm;
	private Integer m_positionX;
	private Integer m_positionY;
	private String m_positionName;
	private String m_connectRequestUsername;
	private String m_connectRequestPwd;
	private String m_hardwareVersion;
	private String m_softwareVersion;
	private String m_deviceName;
	
	//硬件信息
	private String m_cmmacAddress;
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

	private Set<DeviceTask> deviceTask = new HashSet<DeviceTask>();
	private Set<CountResult> countResult = new HashSet<CountResult>();
	private Set<Alarm> alarm = new HashSet<Alarm>();

	public Devices() {

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

	@Column(name = "status", length = 20)
	public String getM_status() {
		return m_status;
	}

	public void setM_status(String m_status) {
		this.m_status = m_status;
	}

	@Column(name = "ip", length = 28)
	public String getM_ip() {
		return m_ip;
	}

	public void setM_ip(String m_ip) {
		this.m_ip = m_ip;
	}

	@Column(name = "port", length = 20)
	public String getM_port() {
		return m_port;
	}

	public void setM_port(String m_port) {
		this.m_port = m_port;
	}

	@Column(name = "mac_address", length = 20)
	public String getM_macAddress() {
		return m_macAddress;
	}

	public void setM_macAddress(String m_macAddress) {
		this.m_macAddress = m_macAddress;
	}

	@Column(name = "serial_number", unique = true,nullable = false)
	public String getM_serialNumber() {
		return m_serialNumber;
	}

	public void setM_serialNumber(String m_serialNumber) {
		this.m_serialNumber = m_serialNumber;
	}

	@Column(name = "manu_facturer", length = 50)
	public String getM_manuFacturer() {
		return m_manuFacturer;
	}

	public void setM_manuFacturer(String m_manuFacturer) {
		this.m_manuFacturer = m_manuFacturer;
	}

	@Column(name = "oui", length = 20)
	public String getM_oui() {
		return m_oui;
	}

	public void setM_oui(String m_oui) {
		this.m_oui = m_oui;
	}

	@Column(name = "product_class", length = 20)
	public String getM_productClass() {
		return m_productClass;
	}

	public void setM_productClass(String m_productClass) {
		this.m_productClass = m_productClass;
	}

	@Column(name = "last_contact", length = 19)
	public Date getM_lastContact() {
		return m_lastContact;
	}

	public void setM_lastContact(Date m_lastContact) {
		this.m_lastContact = m_lastContact;
	}

	@Column(name = "in_net",nullable = false, length = 1)
	public Boolean getM_inNet() {
		return m_inNet;
	}

	public void setM_inNet(Boolean m_inNet) {
		this.m_inNet = m_inNet;
	}

	@Column(name = "is_alarm")
	public Boolean getM_isAlarm() {
		return m_isAlarm;
	}

	public void setM_isAlarm(Boolean m_isAlarm) {
		this.m_isAlarm = m_isAlarm;
	}

	@Column(name = "position_x", nullable = false)
	public Integer getM_positionX() {
		return m_positionX;
	}

	public void setM_positionX(Integer m_positionX) {
		this.m_positionX = m_positionX;
	}

	@Column(name = "position_y", nullable = false)
	public Integer getM_positionY() {
		return m_positionY;
	}

	public void setM_positionY(Integer m_positionY) {
		this.m_positionY = m_positionY;
	}

	@Column(name = "position_name")
	public String getM_positionName() {
		return m_positionName;
	}

	public void setM_positionName(String m_positionName) {
		this.m_positionName = m_positionName;
	}
	
	@Column(name = "conn_requestUsername")
	public String getM_connectRequestUsername() {
		return m_connectRequestUsername;
	}

	public void setM_connectRequestUsername(String m_connectRequestUsername) {
		this.m_connectRequestUsername = m_connectRequestUsername;
	}
	
	@Column(name = "conn_requestPwd")
	public String getM_connectRequestPwd() {
		return m_connectRequestPwd;
	}
	
	public void setM_connectRequestPwd(String m_connectRequestPwd) {
		this.m_connectRequestPwd = m_connectRequestPwd;
	}

	@Column(name = "hardware_version")
	public String getM_hardwareVersion() {
		return m_hardwareVersion;
	}

	public void setM_hardwareVersion(String m_hardwareVersion) {
		this.m_hardwareVersion = m_hardwareVersion;
	}

	@Column(name = "software_version")
	public String getM_softwareVersion() {
		return m_softwareVersion;
	}

	public void setM_softwareVersion(String m_softwareVersion) {
		this.m_softwareVersion = m_softwareVersion;
	}
	
	@Column(name = "cmmac_Address")
	public String getM_cmmacAddress() {
		return m_cmmacAddress;
	}

	public void setM_cmmacAddress(String m_cmmacAddress) {
		this.m_cmmacAddress = m_cmmacAddress;
	}

	@OneToMany(mappedBy = "m_device", cascade = CascadeType.REMOVE)
	public Set<DeviceTask> getDeviceTask() {
		return deviceTask;
	}

	public void setDeviceTask(Set<DeviceTask> deviceTask) {
		this.deviceTask = deviceTask;
	}

	@Column(name = "decoder_manufacturer")
	public String getM_decoderManufacturer() {
		return m_decoderManufacturer;
	}

	public void setM_decoderManufacturer(String m_decoderManufacturer) {
		this.m_decoderManufacturer = m_decoderManufacturer;
	}

	@Column(name = "decoder_model")
	public String getM_decoderModel() {
		return m_decoderModel;
	}

	public void setM_decoderModel(String m_decoderModel) {
		this.m_decoderModel = m_decoderModel;
	}

	@Column(name = "ram_size")
	public Long getM_RAMSize() {
		return m_RAMSize;
	}

	public void setM_RAMSize(Long m_RAMSize) {
		this.m_RAMSize = m_RAMSize;
	}

	@Column(name = "flash_size")
	public Long getM_flashSize() {
		return m_flashSize;
	}

	public void setM_flashSize(Long m_flashSize) {
		this.m_flashSize = m_flashSize;
	}

	@Column(name = "ram_rest")
	public Long getM_RAMRest() {
		return m_RAMRest;
	}

	public void setM_RAMRest(Long m_RAMRest) {
		this.m_RAMRest = m_RAMRest;
	}

	@Column(name = "flash_rest")
	public Long getM_flashRest() {
		return m_flashRest;
	}

	public void setM_flashRest(Long m_flashRest) {
		this.m_flashRest = m_flashRest;
	}

	@Column(name = "idle")
	public Long getM_IDLE() {
		return m_IDLE;
	}

	public void setM_IDLE(Long m_IDLE) {
		this.m_IDLE = m_IDLE;
	}

	@Column(name = "system_runTime")
	public Long getM_systemRunTime() {
		return m_systemRunTime;
	}

	public void setM_systemRunTime(Long m_systemRunTime) {
		this.m_systemRunTime = m_systemRunTime;
	}

	@Column(name = "ram_used")
	public Long getM_RAMUsed() {
		return m_RAMUsed;
	}

	public void setM_RAMUsed(Long m_RAMUsed) {
		this.m_RAMUsed = m_RAMUsed;
	}

	@Column(name = "cpu_load")
	public Long getM_CPULoad() {
		return m_CPULoad;
	}

	public void setM_CPULoad(Long m_CPULoad) {
		this.m_CPULoad = m_CPULoad;
	}
	
	@OneToMany(mappedBy = "m_device", cascade = CascadeType.REMOVE)
	public Set<CountResult> getCountResult() {
		return countResult;
	}

	public void setCountResult(Set<CountResult> countResult) {
		this.countResult = countResult;
	}
	
	@Column(name = "device_name")
	public String getM_deviceName() {
		return m_deviceName;
	}

	public void setM_deviceName(String m_deviceName) {
		this.m_deviceName = m_deviceName;
	}

	@OneToMany(mappedBy = "m_devices", cascade = CascadeType.REMOVE)
	public Set<Alarm> getAlarm() {
		return alarm;
	}

	public void setAlarm(Set<Alarm> alarm) {
		this.alarm = alarm;
	}
	
	@Override
	public String toString() {
		return "Devices [m_id=" + m_id + ", m_status=" + m_status + ", m_ip="
				+ m_ip + ", m_port=" + m_port + ", m_macAddress="
				+ m_macAddress + ", m_serialNumber=" + m_serialNumber
				+ ", m_manuFacturer=" + m_manuFacturer + ", m_oui=" + m_oui
				+ ", m_productClass=" + m_productClass + ", m_lastContact="
				+ m_lastContact + ", m_inNet=" + m_inNet + ", m_isAlarm="
				+ m_isAlarm + ", m_positionX=" + m_positionX + ", m_positionY="
				+ m_positionY + ", m_positionName=" + m_positionName
				+ ", m_hardwareVersion="+ m_hardwareVersion + ", m_softwareVerison="+ m_softwareVersion + ", deviceTask=" + deviceTask 
				+ "]";
	}

}
