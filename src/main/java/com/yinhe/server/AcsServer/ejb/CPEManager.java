package com.yinhe.server.AcsServer.ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import com.yinhe.server.AcsServer.backbean.CPEBean;
import com.yinhe.server.AcsServer.backbean.ParamGetBean;
import com.yinhe.server.AcsServer.backbean.ParamSetBean;
import com.yinhe.server.AcsServer.backbean.QueryCPEBean;
import com.yinhe.server.AcsServer.backbean.RPCParameterBean;
import com.yinhe.server.AcsServer.backbean.TaskParameter;
import com.yinhe.server.AcsServer.backbean.UpgradeParameter;
import com.yinhe.server.AcsServer.data.CPERespository;
import com.yinhe.server.AcsServer.data.DeviceTaskRespository;
import com.yinhe.server.AcsServer.data.TaskRespository;
import com.yinhe.server.AcsServer.enums.CPEResultCode;
import com.yinhe.server.AcsServer.enums.TaskResultCode;
import com.yinhe.server.AcsServer.enums.TaskStatus;
import com.yinhe.server.AcsServer.model.DeviceTask;
import com.yinhe.server.AcsServer.model.Devices;
import com.yinhe.server.AcsServer.model.Task;
import com.yinhe.server.AcsServer.util.Resources;

@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class CPEManager {
	@Inject
	private Devices m_device;
	@Inject
	private Logger log;
	@Inject
	private CPERespository m_cpeRespository;
	@Inject
	private TaskRespository m_taskRespository;
	@Inject
	private DeviceTaskRespository m_deviceTaskRespository;

	private List<CPEProcess> m_cPEProcessList = new ArrayList<CPEProcess>();

	@PostConstruct
	public void getAllDevices() {
		List<Devices> m_devices = new ArrayList<Devices>();
		m_devices = listAllDevice();
		
		for (Devices devices : m_devices) {			
			CPEProcess cpeProcess = new CPEProcess();
			cpeProcess.setDevices(devices);
			m_cPEProcessList.add(cpeProcess);
		}
	}

	/**
	 * @Title: listAllDevice
	 * @author: wr@yinhe.com
	 * @Description: 获取所有设备 存于缓存中
	 * @param @return
	 * @return List<Devices>
	 * @throws
	 * @date 2017年3月2日 下午1:59:10
	 */	
	private List<Devices> listAllDevice() {
		return m_cpeRespository.findAllOrderedByM_Id();
	}

	/**
	 * @Title: addDevice
	 * @author: wr@yinhe.com
	 * @Description: 添加设备 同时添加5个任务
	 * @param @param cpeBean
	 * @param @return
	 * @return CPEResultCode
	 * @throws
	 * @date 2017年3月2日 下午1:57:32
	 */
	public synchronized CPEResultCode addDevice(CPEBean cpeBean) {
		CPEResultCode cpeResultCode = null;
		if (null != cpeBean) {
			if (0 == m_cPEProcessList.size()) {
				cpeResultCode = add(cpeBean);
				return cpeResultCode;
			} else {
				boolean flag = false;
				for (CPEProcess cpeProcess : m_cPEProcessList) {
					if(!Resources.isNullOrEmpty(cpeProcess.getDevices().getM_macAddress())){
						if (cpeProcess.getDevices().getM_serialNumber().equals(cpeBean.getM_serialNumber())
								|| cpeProcess.getDevices().getM_macAddress().equalsIgnoreCase(cpeBean.getM_macAddress())) {
							flag = true;
						}
					}
				}

				if (!flag) {
					cpeResultCode = add(cpeBean);
					return cpeResultCode;
				} else {
					return CPEResultCode.CPE_ADD_DEVICE_EXIST;
				}
			}
		} else {
			return CPEResultCode.CPE_ADD_UNKNOWN_ERROR;
		}
	}

	/**
	 * @Title: add
	 * @author: wr@yinhe.com
	 * @Description: 抽离添加方法
	 * @param @param cpeBean
	 * @param @return
	 * @return CPEResultCode
	 * @throws
	 * @date 2017年3月2日 下午1:57:08
	 */
	private CPEResultCode add(CPEBean cpeBean) {
		m_device = new Devices();
		m_device.setM_inNet(false);  //默认为false，待从页面修改==========
		m_device.setM_serialNumber(cpeBean.getM_serialNumber());
		m_device.setM_macAddress(cpeBean.getM_macAddress());
		m_device.setM_positionX(cpeBean.getM_positionX());
		m_device.setM_positionY(cpeBean.getM_positionY());
		m_device.setM_positionName(cpeBean.getM_positionName());
		m_device.setM_manuFacturer(cpeBean.getM_manuFacturer());
		m_device.setM_status(cpeBean.getM_status());
		try {
			m_cpeRespository.addEntity(m_device);
		} catch (Exception e) {
			e.printStackTrace();
			return CPEResultCode.CPE_ADD_UNKNOWN_ERROR;
		}
		
		List<Task> taskBean = new ArrayList<Task>();
		taskBean = m_taskRespository.findAllOrderedByIdDesc();
		if(null != taskBean && 0 != taskBean.size()){
			for (Task temp : taskBean) {
				DeviceTask deviceTask = new DeviceTask();
				Long period_time = new Long(1000000L);
				deviceTask.setM_device(m_device);
				deviceTask.setM_is_period(true);
				deviceTask.setM_period_time(period_time);
				deviceTask.setM_status("stopped");
				deviceTask.setM_task(temp);
				try {
					m_deviceTaskRespository.addEntity(deviceTask);
				} catch (Exception e) {
					e.printStackTrace();
					return CPEResultCode.CPE_ADD_UNKNOWN_ERROR;
				}
			}
		}
		log.info("[add] cacheDevicesSize Add Before= " + m_cPEProcessList.size());
		CPEProcess cpeProcess = new CPEProcess();
		cpeProcess.setDevices(m_device);
		m_cPEProcessList.add(cpeProcess);
		log.info("[add] cacheDevicesSize Add After = " + m_cPEProcessList.size());
		return CPEResultCode.CPE_ADD_SUCCESS;
	}
	
	public synchronized void addCPEProcessToCache(CPEProcess cpeProcess){
		m_cPEProcessList.add(cpeProcess);
	}

	/**
	 * @Title: delDevice
	 * @author: wr@yinhe.com
	 * @Description: 删除设备，同时删除该设备的任务和任务结果，有任务正在进行，不能删除
	 * @param @param device
	 * @param @return
	 * @return CPEResultCode
	 * @throws
	 * @date 2017年3月2日 上午8:40:30
	 */
	public synchronized CPEResultCode delDevice(List<CPEBean> cpeBeans) {
		List<Long> deviceIdList = new ArrayList<Long>(cpeBeans.size());
		List<DeviceTask> deviceTaskList = new ArrayList<DeviceTask>();
		if (null != cpeBeans && 0 != cpeBeans.size()) {
			for (CPEBean cpeBean : cpeBeans) {
				if (null != cpeBean) {
					deviceIdList.add(cpeBean.getM_id());
				}
			}
		} else {
			return CPEResultCode.CPE_DELETE_CHECKED_IS_NULL;
		}
		deviceTaskList = m_deviceTaskRespository.findByDeviceIdList(deviceIdList);
		if (0 != deviceTaskList.size()) {
			for (DeviceTask deviceTask : deviceTaskList) {
				if (deviceTask.getM_status().equals("running")) {
					log.info("[delDevice] M_status() = " + deviceTask.getM_status());
					return CPEResultCode.CPE_DELETE_TASK_RUNNING;
				} else {
					int count = 0;
					for (Long id : deviceIdList) {
						for (CPEProcess cacheCPEProcess : m_cPEProcessList) {
							if (cacheCPEProcess.getDevices().getM_id() == id) {
								Devices device = m_cpeRespository
										.findDeviceById(id);
								try {
									m_cpeRespository.deleteEntity(device);
									log.info("[delDevice] cacheDevicesSize Delete Before= "
											+ m_cPEProcessList.size());
									m_cPEProcessList.remove(cacheCPEProcess);
									log.info("[delDevice] cacheDevicesSize Delete After = "
											+ m_cPEProcessList.size());
									count++;
									break;
								} catch (Exception e) {
									e.printStackTrace();
									return CPEResultCode.CPE_DELETE_UNKNOWN_ERROR;
								}
							}
						}
					}
					if (count != deviceIdList.size()) {
						return CPEResultCode.CPE_DELETE_UNKNOWN_ERROR;
					} else {
						return CPEResultCode.CPE_DELETE_CHECKED_SUCCESS;
					}
				}
			}
		}else{
			//有设备，但任务不存在，此情况为非正常添加设备
			int count = 0;
			for (Long id : deviceIdList) {
				for (CPEProcess cacheCPEProcess : m_cPEProcessList) {
					if (cacheCPEProcess.getDevices().getM_id() == id) {
						Devices device = m_cpeRespository
								.findDeviceById(id);
						try {
							m_cpeRespository.deleteEntity(device);
							log.info("[delDevice] cacheDevicesSize Delete Before= "
									+ m_cPEProcessList.size());
							m_cPEProcessList.remove(cacheCPEProcess);
							log.info("[delDevice] cacheDevicesSize Delete After = "
									+ m_cPEProcessList.size());
							count++;
							break;
						} catch (Exception e) {
							e.printStackTrace();
							return CPEResultCode.CPE_DELETE_UNKNOWN_ERROR;
						}
					}
				}
				if (count != deviceIdList.size()) {
					return CPEResultCode.CPE_DELETE_UNKNOWN_ERROR;
				} else {
					return CPEResultCode.CPE_DELETE_CHECKED_SUCCESS;
				}
			}
		}
		return CPEResultCode.CPE_DELETE_UNKNOWN_ERROR;
	}

	/**
	 * @Title: update
	 * @author: wr@yinhe.com
	 * @Description: 更新设备
	 * @param @param cpeBean
	 * @param @return
	 * @return CPEResultCode
	 * @throws
	 * @date 2017年3月2日 下午7:25:19
	 */
	/*public synchronized CPEResultCode updateDevice(CPEBean cpeBean) {
		if (null != cpeBean && m_cPEProcessList != null) {
			for (CPEProcess cacheCPEProcess : m_cPEProcessList) {
				if (cpeBean.getM_serialNumber().equals(cacheCPEProcess.getDevices().getM_serialNumber())) {
					Devices deviceEdit = m_cpeRespository.findDeviceBySerialNumber(cpeBean.getM_serialNumber());
					if (null != computerRoom) {
						deviceEdit.setM_status(cpeBean.getM_status());
						deviceEdit
								.setM_macAddress(deviceEdit.getM_macAddress());
						// 更新数据库
						try {
							m_cpeRespository.updateEntity(deviceEdit);
							log.info("update database");
						} catch (Exception e) {
							e.printStackTrace();
							return CPEResultCode.CPE_UPDATE_UNKNOWN_ERROR;
						}
						// 更新缓存
						cacheCPEProcess.getDevices().setM_computerRoom(computerRoom);
						cacheCPEProcess.getDevices().setM_status(cpeBean.getM_status());
					}
				}
			}
			return CPEResultCode.CPE_UPDATE_CHECKED_SUCCESS;
		} else {
			return CPEResultCode.CPE_UPDATE_UNKNOWN_ERROR;
		}
	}*/

	/**
	 * @Title: listAll
	 * @author: wr@yinhe.com
	 * @Description: 获取所有设备列表 页面显示,地图显示全部
	 * @param @return
	 * @return List<CPEBean>
	 * @throws
	 * @date 2017年3月2日 下午7:25:55
	 */
	public synchronized List<CPEBean> listAll() {
		List<CPEBean> m_cpeList = new ArrayList<CPEBean>();
		m_cpeList = m_cpeRespository.getAllCpeList();
		return m_cpeList;
	}
	
	/**
	 * @Title: listAllDevices
	 * @author: wr@yinhe.com
	 * @Description: 分页查询设备
	 * @param @param m_queryCPEBean
	 * @param @return
	 * @return List<CPEBean>
	 * @throws  
	 * @date  2017年5月23日 上午9:45:37
	 */
	public synchronized List<CPEBean> listAllDevices(QueryCPEBean m_queryCPEBean){
		List<CPEBean> m_cpeList = new ArrayList<CPEBean>();
		m_queryCPEBean.setMaxByCount(m_cpeRespository.countDevices(m_queryCPEBean));
		m_cpeList = m_cpeRespository.getAllDevicesList(m_queryCPEBean);
		return m_cpeList;
	}

	/**
	 * @Title: select
	 * @author: wr@yinhe.com
	 * @Description: 先用序列号来查询 如果序列号不存在，用idList来查询,删除的时候可传入idList来查找所需删除列表
	 * @param @param query
	 * @param @return
	 * @return List<CPEBean>
	 * @throws
	 * @date 2017年3月2日 下午8:52:57
	 */
	public synchronized List<CPEBean> select(QueryCPEBean query) {
		List<CPEBean> m_cpeList = new ArrayList<CPEBean>();
		if (null != query) {
			List<Devices> devicesList = null;
			int count = 0;
			// 按序列号来筛选
			if (null != query.getM_serialNumber()) {
				for (CPEProcess cacheCPEProcess : m_cPEProcessList) {
					if (cacheCPEProcess.getDevices().getM_serialNumber()
							.contains(query.getM_serialNumber())) {
						devicesList = new ArrayList<Devices>();
						devicesList.add(cacheCPEProcess.getDevices());
						for (Devices devices : devicesList) {
							CPEBean bean = new CPEBean();
							bean.setM_rowId(++count);
							bean.setM_serialNumber(devices.getM_serialNumber());
							bean.setM_id(devices.getM_id());
							bean.setM_manuFacturer(devices.getM_manuFacturer());
							bean.setM_inNet(devices.getM_inNet());
							bean.setM_ip(devices.getM_ip());
							bean.setM_isAlarm(devices.getM_isAlarm());
							bean.setM_lastContact(devices.getM_lastContact());
							bean.setM_macAddress(devices.getM_macAddress());
							bean.setM_oui(devices.getM_oui());
							bean.setM_port(devices.getM_port());
							bean.setM_positionName(devices.getM_positionName());
							bean.setM_positionX(devices.getM_positionX());
							bean.setM_positionY(devices.getM_positionY());
							bean.setM_productClass(devices.getM_productClass());
							bean.setM_status(devices.getM_status());
							bean.setM_deviceName(devices.getM_deviceName());
							
							m_cpeList.add(bean);
						}
					}
				}
				return m_cpeList;
			}

			// 按所选idList来筛选
			if (null != query.getIdList() || query.getIdList().size() != 0) {
				for (Long id : query.getIdList()) {
					for (CPEProcess cacheCPEProcess : m_cPEProcessList) {
						if (cacheCPEProcess.getDevices().getM_id().equals(id)) {
							devicesList = new ArrayList<Devices>();
							devicesList.add(cacheCPEProcess.getDevices());
							for (Devices devices : devicesList) {
								CPEBean bean = new CPEBean();
								bean.setM_rowId(++count);
								bean.setM_serialNumber(devices
										.getM_serialNumber());
								bean.setM_id(devices.getM_id());
								bean.setM_manuFacturer(devices.getM_manuFacturer());
								bean.setM_inNet(devices.getM_inNet());
								bean.setM_ip(devices.getM_ip());
								bean.setM_isAlarm(devices.getM_isAlarm());
								bean.setM_lastContact(devices.getM_lastContact());
								bean.setM_macAddress(devices.getM_macAddress());
								bean.setM_oui(devices.getM_oui());
								bean.setM_port(devices.getM_port());
								bean.setM_positionName(devices.getM_positionName());
								bean.setM_positionX(devices.getM_positionX());
								bean.setM_positionY(devices.getM_positionY());
								bean.setM_productClass(devices.getM_productClass());
								bean.setM_status(devices.getM_status());
								bean.setM_deviceName(devices.getM_deviceName());
								
								m_cpeList.add(bean);
							}
						}
					}
				}
				return m_cpeList;
			}
		}
		return m_cpeList;
	}

	/**
	 * @Title: getStatus
	 * @author: wr@yinhe.com
	 * @Description: 获取设备任务类型的任务状态
	 * @param @param device
	 * @param @param type
	 * @param @return
	 * @return TaskStatus
	 * @throws
	 * @date 2017年3月2日 上午9:31:07
	 */
	public TaskStatus getStatus(CPEBean device, String type) {
		if (null != device && !type.equals("")) {
			TaskStatus taskStatus = null;
			for (CPEProcess cacheCPEProcess : m_cPEProcessList) {
				if (cacheCPEProcess.getDevices().getM_id()
						.equals(device.getM_id())) {
					DeviceTask deviceTask = m_deviceTaskRespository.findDeviceTaskByIdAndType(
							device.getM_id(), type);
					if (null != deviceTask) {
						if (deviceTask.getM_status().equals("running")) {
							taskStatus = TaskStatus.RUNNING;
						} else if (deviceTask.getM_status().equals("stopped")) {
							taskStatus = TaskStatus.STOPPED;
						} else {
							taskStatus = TaskStatus.UNKNOWN;
						}
					}
				}
			}
			return taskStatus;
		} else {
			return TaskStatus.UNKNOWN;
		}
	}
	
	//TO-DO??是否插入数据库，是否加锁
	public void changeTaskStatus(CPEBean device, String type){
		if (null != device) {
			for (CPEProcess cacheCPEProcess : m_cPEProcessList) {
				if (cacheCPEProcess.getDevices().getM_id()
						.equals(device.getM_id())) {
					DeviceTask deviceTask = m_deviceTaskRespository.findDeviceTaskByIdAndType(
							device.getM_id(), type);
					if(null != deviceTask){
						log.info("[changeTaskStatus] status before = " + deviceTask.getM_status());
						if(deviceTask.getM_status().equals("stopped")){
							deviceTask.setM_status("running");
						} else {
							deviceTask.setM_status("stopped");
						}
						log.info("[changeTaskStatus] status after = " + deviceTask.getM_status());
					}
				}
			}
		} else {
			log.info("[changeTaskStatus]: device = null");
		}
	}
	
	/**
	 * @Title: start
	 * @author: wr@yinhe.com
	 * @Description: 开启任务
	 * @param @param device
	 * @param @param type
	 * @param @param param
	 * @param @param ms
	 * @param @return
	 * @return TaskResultCode
	 * @throws
	 * @date 2017年3月13日 上午9:47:49
	 */
	public TaskResultCode start(CPEBean device, String type,
			TaskParameter param, long ms) {
		log.info("[TaskResultCode] get in cpeManager start");
		if (null != device) {
			try{
				for (CPEProcess cacheCPEProcess : m_cPEProcessList) {
					if (cacheCPEProcess.getDevices().getM_id().equals(device.getM_id())) {
						log.info("[start] cpeManager start");
						return cacheCPEProcess.start(type, param, ms);
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		} else {
			log.info("[start] device = null");
		}
		return null;
	}
	
	public TaskResultCode start2(CPEBean device,List<ParamSetBean> paramSet_list, long ms) {
		if (null != device && paramSet_list.size() != 0) {
			try{
				for (CPEProcess cacheCPEProcess : m_cPEProcessList) {
					if (cacheCPEProcess.getDevices().getM_id().equals(device.getM_id())) {
						log.info("[start2] cpeManager start2");
						return cacheCPEProcess.start2(paramSet_list, ms);
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		} else {
			log.info("[start2] device = null or paramSet_list == null");
			return TaskResultCode.TASK_RESULT_PARAMETER_NULL;
		}
		return null;
	}
	
	public TaskResultCode startRecordTask(CPEBean device, List<ParamGetBean> paramGet_list, long ms) {
		if (null != device && paramGet_list.size() != 0) {
			try{
				for (CPEProcess cacheCPEProcess : m_cPEProcessList) {
					if (cacheCPEProcess.getDevices().getM_id().equals(device.getM_id())) {
						return cacheCPEProcess.start3(paramGet_list, ms);
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		} else {
			return TaskResultCode.TASK_RESULT_PARAMETER_NULL;
		}
		return null;
	}

	/**
	 * @Title: stop
	 * @author: wr@yinhe.com
	 * @Description: 停止任务
	 * @param @param device
	 * @param @param type
	 * @param @param ms
	 * @param @return
	 * @return TaskResultCode
	 * @throws
	 * @date 2017年3月13日 上午9:48:25
	 */
	public TaskResultCode stop(CPEBean device, String type, long ms) {
		if (null != device) {
			for (CPEProcess cacheCPEProcess : m_cPEProcessList) {
				if (cacheCPEProcess.getDevices().getM_id()
						.equals(device.getM_id())) {
					return cacheCPEProcess.stop(type, ms);
				}
			}
		}
		return null;
	}

	/**
	 * @Title: getDevice
	 * @author: wr@yinhe.com
	 * @Description: get device by serialNumber
	 * @param @param serialNumber
	 * @param @return
	 * @return Devices
	 * @throws
	 * @date 2017年2月25日 下午12:08:27
	 */
	/*
	 * public Devices getDevice(String serialNumber) { m_device =
	 * m_cpeRespository.findDeviceBySerialNumber(serialNumber); return m_device;
	 * }
	 */

	public CPEProcess getDevice(String serialNumber) {
		if (0 != m_cPEProcessList.size()) {
			for (CPEProcess cPEProcess : m_cPEProcessList) {
				log.info("[getDevice] cPEProcess = "
						+ cPEProcess.getDevices().getM_serialNumber());
				log.info("[getDevice] serialNumber = " + serialNumber);
				if (cPEProcess.getDevices().getM_serialNumber()
						.equals(serialNumber)) {
					return cPEProcess;
				}
			}
		}
		return null;
	}

	//TO-DO??位置是否正确，是否加锁
	/**
	 * @Title: setCPEBean
	 * @author: wr@yinhe.com
	 * @Description: 设置页面跳转参数
	 * @param @param serialNumber
	 * @param @return
	 * @return CPEBean
	 * @throws
	 * @date 2017年3月2日 上午8:49:17
	 */
	public CPEBean setCPEBean(String serialNumber) {
		log.info("[setCPEBean] serialNumber = " + serialNumber);
		Devices devices = m_cpeRespository
				.findDeviceBySerialNumber(serialNumber);
		if (null != devices) {
			CPEBean bean = new CPEBean();
			bean.setM_id(devices.getM_id());
			bean.setM_inNet(devices.getM_inNet());
			bean.setM_ip(devices.getM_ip());
			bean.setM_isAlarm(devices.getM_isAlarm());
			bean.setM_lastContact(devices.getM_lastContact());
			bean.setM_manuFacturer(devices.getM_manuFacturer());
			bean.setM_macAddress(devices.getM_macAddress());
			bean.setM_oui(devices.getM_oui());
			bean.setM_port(devices.getM_port());
			bean.setM_positionName(devices.getM_positionName());
			bean.setM_productClass(devices.getM_productClass());
			bean.setM_hardwareVersion(devices.getM_hardwareVersion());
			bean.setM_softwareVerison(devices.getM_softwareVersion());
			bean.setM_cmmacAddress(devices.getM_cmmacAddress());
			bean.setM_deviceName(devices.getM_deviceName());
			
			//硬件信息
			bean.setM_decoderManufacturer(devices.getM_decoderManufacturer());
			bean.setM_decoderModel(devices.getM_decoderModel());
			bean.setM_RAMSize(devices.getM_RAMSize());
			bean.setM_flashSize(devices.getM_flashSize());
			
			bean.setM_RAMRest(devices.getM_RAMRest());
			bean.setM_flashRest(devices.getM_flashRest());
			bean.setM_IDLE(devices.getM_IDLE());
			bean.setM_systemRunTime(devices.getM_systemRunTime());
			bean.setM_RAMUsed(devices.getM_RAMUsed());
			bean.setM_CPULoad(devices.getM_CPULoad());
			if(devices.getM_lastContact() == null){
				bean.setM_status(devices.getM_status());
			}else{
				Date now = new Date();
				long beginTime = now.getTime();
				long endTime = devices.getM_lastContact().getTime();
				long diff = beginTime - endTime;
                long day = diff/(24*1000*60*60);
				long hour = diff/(1000*60*60) - day * 24;
				if(day > 0){
					bean.setM_status("offline");
				}else{
					if(hour > 0){
						bean.setM_status("offline");
					}
					else{
						bean.setM_status("online");
					}	
				}
			}
			bean.setM_serialNumber(devices.getM_serialNumber());
			return bean;
		} else {
			return null;
		}
	}
	
	public CPEBean setCPEBeanById(Long device_id) {
		Devices devices = m_cpeRespository.findDeviceById(device_id);
		if (null != devices) {
			CPEBean bean = new CPEBean();
			bean.setM_id(devices.getM_id());
			bean.setM_inNet(devices.getM_inNet());
			bean.setM_ip(devices.getM_ip());
			bean.setM_isAlarm(devices.getM_isAlarm());
			bean.setM_lastContact(devices.getM_lastContact());
			bean.setM_manuFacturer(devices.getM_manuFacturer());
			bean.setM_macAddress(devices.getM_macAddress());
			bean.setM_oui(devices.getM_oui());
			bean.setM_port(devices.getM_port());
			bean.setM_positionName(devices.getM_positionName());
			bean.setM_productClass(devices.getM_productClass());
			bean.setM_deviceName(devices.getM_deviceName());
			if(devices.getM_lastContact() == null){
				bean.setM_status(devices.getM_status());
			}else{
				Date now = new Date();
				long beginTime = now.getTime();
				long endTime = devices.getM_lastContact().getTime();
				long diff = beginTime - endTime;
                long day = diff/(24*1000*60*60);
				long hour = diff/(1000*60*60) - day * 24;
				if(day > 0){
					bean.setM_status("offline");
				}else{
					if(hour > 0){
						bean.setM_status("offline");
					}else{
						bean.setM_status("online");
					}					
				}
			}
			bean.setM_serialNumber(devices.getM_serialNumber());
			return bean;
		} else {
			return null;
		}
	}

	public Devices getM_device() {
		return m_device;
	}

	public void setM_device(Devices m_device) {
		this.m_device = m_device;
	}

	public List<CPEProcess> getM_cPEProcessList() {
		return m_cPEProcessList;
	}

	public void setM_cPEProcessList(List<CPEProcess> m_cPEProcessList) {
		this.m_cPEProcessList = m_cPEProcessList;
	}

	public CPEProcess getCurrentCPEProcess(String serial_number){
		if(serial_number == null){
			return null;
		}
		for(CPEProcess cp : m_cPEProcessList){
			if(cp.getDevices().getM_serialNumber().equals(serial_number)){
				cp.setHas_get(true);
				return cp;
			}
		}
		return null;
	}
	
	public TaskResultCode reboot(CPEBean device){
		if (null != device) {
			for (CPEProcess cacheCPEProcess : m_cPEProcessList) {
				if (cacheCPEProcess.getDevices().getM_id().equals(device.getM_id())) {
					return cacheCPEProcess.reboot();
				}
			}
	} else {
		log.info("[reboot] device = null");
	}
		return null;
	}
	
	public TaskResultCode upgrade(CPEBean device,UpgradeParameter m_upgradeParamter){
		if (null != device) {
			for (CPEProcess cacheCPEProcess : m_cPEProcessList) {
				if (cacheCPEProcess.getDevices().getM_id().equals(device.getM_id())) {
					return cacheCPEProcess.upgrade(m_upgradeParamter);
				}
			}
	} else {
		log.info("[upgrade]: device = null");
	}
		return null;
	}
	
	public String executeRPC(CPEBean device,RPCParameterBean rpc_parameterBean){
		if (null != device) {
			for (CPEProcess cacheCPEProcess : m_cPEProcessList) {
				if (cacheCPEProcess.getDevices().getM_id().equals(device.getM_id())) {
					return cacheCPEProcess.startExcuteRPC(rpc_parameterBean);
				}
			}
		} else {
			log.info("[executeRPC] device = null");
		}
		return "";
	}
	
	public synchronized CPEResultCode updateDevice(CPEBean cpeBean){
		if (null != cpeBean && m_cPEProcessList != null) {
			for (CPEProcess cacheCPEProcess : m_cPEProcessList) {
				if (cpeBean.getM_serialNumber().equals(cacheCPEProcess.getDevices().getM_serialNumber())) {
					Devices deviceEdit = m_cpeRespository.findDeviceBySerialNumber(cpeBean.getM_serialNumber());
					deviceEdit.setM_positionName(cpeBean.getM_positionName());
					deviceEdit.setM_ip(cpeBean.getM_ip());
					// 更新数据库
					try {
						m_cpeRespository.updateEntity(deviceEdit);
						log.info("[updateDevice] update database");
					} catch (Exception e) {
						e.printStackTrace();
						return CPEResultCode.CPE_UPDATE_UNKNOWN_ERROR;
					}
					// 更新缓存
					cacheCPEProcess.getDevices().setM_positionName(cpeBean.getM_positionName());
					cacheCPEProcess.getDevices().setM_ip(cpeBean.getM_ip());
					}
			}
			return CPEResultCode.CPE_UPDATE_CHECKED_SUCCESS;
		} else {
			return CPEResultCode.CPE_UPDATE_UNKNOWN_ERROR;
		}
	}
	
	/**
	 * @Title: updateDevicePosition
	 * @author: wm@yinhe.com
	 * @Description: 修改设备坐标
	 * @param @param id
	 * @param @param x
	 * @param @param y
	 * @return CPEResultCode
	 * @throws
	 * @date 2018年1月29日 上午13:26:35
	 */
	public CPEResultCode updateDevicePosition(Long id, Integer x, Integer y) {
		Devices deviceEdit = m_cpeRespository.findById(id);
		if(deviceEdit == null)
			return CPEResultCode.CPE_UPDATE_FAILED;
		
		deviceEdit.setM_positionX(x);
		deviceEdit.setM_positionY(y);
		
		try {
			m_cpeRespository.updateEntity(deviceEdit);
			log.info("[updateDevicePosition] update database");
			return CPEResultCode.CPE_UPDATE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return CPEResultCode.CPE_UPDATE_UNKNOWN_ERROR;
		}
	}
}
