package com.yinhe.server.AcsServer.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.yinhe.server.AcsServer.backbean.CPEBean;
import com.yinhe.server.AcsServer.backbean.QueryCPEBean;
import com.yinhe.server.AcsServer.model.Devices;

public class CPERespository extends BaseRespository<Devices>{
	
	public void addDevice(Devices device){
		super.addEntity(device);
	}
	/**
	 * @Title: findDeviceBySerialNumber
	 * @author: wr@yinhe.com
	 * @Description: find device by serialNumber
	 * @param @param serialNumber
	 * @param @return
	 * @return Devices
	 * @throws  
	 * @date  2017年2月24日 下午1:34:12
	 */
	public Devices findDeviceBySerialNumber(String serialNumber) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Devices> criteria = cb.createQuery(Devices.class);
		Root<Devices> devices = criteria.from(Devices.class);
		List<Predicate> whereList = new ArrayList<>();
		whereList.add(cb.equal(devices.get("m_serialNumber"), serialNumber));
		criteria.select(devices).where(
				whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
	
	
	/**
	 * @Title: findDeviceById
	 * @author: wr@yinhe.com
	 * @Description: find device by deviceId
	 * @param @param m_id
	 * @param @return
	 * @return Devices
	 * @throws  
	 * @date  2017年2月27日 下午8:55:41
	 */
	public Devices findDeviceById(Long m_id) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Devices> criteria = cb.createQuery(Devices.class);
		Root<Devices> devices = criteria.from(Devices.class);

		List<Predicate> whereList = new ArrayList<>();
		whereList.add(cb.equal(devices.get("m_id"), m_id));
		criteria.select(devices).where(
				whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
	/**
	 * @Title: findDeviceByMacAddress
	 * @author: wr@yinhe.com
	 * @Description: find device by macAddress
	 * @param @param macAddress
	 * @param @return
	 * @return Devices
	 * @throws  
	 * @date  2017年2月24日 下午1:38:13
	 */
	public Devices findDeviceByMacAddress(String macAddress) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Devices> criteria = cb.createQuery(Devices.class);
		Root<Devices> devices = criteria.from(Devices.class);

		List<Predicate> whereList = new ArrayList<>();
		whereList.add(cb.equal(devices.get("m_macAddress"), macAddress));
		criteria.select(devices).where(
				whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/**
	 * @Title: getAllCpeList
	 * @author: wr@yinhe.com
	 * @Description: get all CPE 
	 * @param @return
	 * @return List<CPEBean>
	 * @throws  
	 * @date  2017年2月25日 下午12:51:55
	 */
	public List<CPEBean> getAllCpeList() {
		List<Devices> allDevicesList = findAllOrderedByM_Id();
		List<CPEBean> returnList = new ArrayList<CPEBean>();
		int count = 0;
		for (Devices device : allDevicesList) {
			if (null == device) {
				continue;
			}
			CPEBean bean = new CPEBean();
			bean.setM_rowId(++count);
			bean.setM_serialNumber(device.getM_serialNumber());
			bean.setM_id(device.getM_id());
			bean.setM_manuFacturer(device.getM_manuFacturer());
			bean.setM_positionName(device.getM_positionName());
			bean.setM_positionX(device.getM_positionX());
			bean.setM_positionY(device.getM_positionY());
			bean.setM_status(device.getM_status());
			//离线、在线
			if(device.getM_lastContact() == null){
				bean.setM_status(device.getM_status());
			}else{
				Date now = new Date();
				long beginTime = now.getTime();
				long endTime = device.getM_lastContact().getTime();
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
			bean.setM_inNet(device.getM_inNet());
			bean.setM_ip(device.getM_ip());
			bean.setM_isAlarm(device.getM_isAlarm());
			bean.setM_lastContact(device.getM_lastContact());
			bean.setM_oui(device.getM_oui());
			bean.setM_port(device.getM_port());
			bean.setM_deviceName(device.getM_deviceName());
			returnList.add(bean);
		}
		return returnList;
	}
	
	
	
	public List<CPEBean> getAllDevicesList(QueryCPEBean m_queryCPEBean) {
		List<Devices> allDevicesList = findAllDevicesByM_Id(m_queryCPEBean);
		List<CPEBean> returnList = new ArrayList<CPEBean>();
		int count = 0;
		for (Devices device : allDevicesList) {
			if (null == device) {
				continue;
			}
			CPEBean bean = new CPEBean();
			bean.setM_rowId(++count);
			bean.setM_serialNumber(device.getM_serialNumber());
			bean.setM_id(device.getM_id());
			bean.setM_manuFacturer(device.getM_manuFacturer());
			bean.setM_positionName(device.getM_positionName());
			bean.setM_positionX(device.getM_positionX());
			bean.setM_positionY(device.getM_positionY());
			bean.setM_status(device.getM_status());
			//离线、在线
			if(device.getM_lastContact() == null){
				bean.setM_status(device.getM_status());
			}else{
				Date now = new Date();
				long beginTime = now.getTime();
				long endTime = device.getM_lastContact().getTime();
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
			bean.setM_inNet(device.getM_inNet());
			bean.setM_ip(device.getM_ip());
			bean.setM_isAlarm(device.getM_isAlarm());
			bean.setM_lastContact(device.getM_lastContact());
			bean.setM_oui(device.getM_oui());
			bean.setM_port(device.getM_port());
			bean.setM_deviceName(device.getM_deviceName());
			returnList.add(bean);
		}
		return returnList;
	}
	
	public List<Devices> findAllDevicesByM_Id(QueryCPEBean m_queryCPEBean) {
		int startPosition = m_queryCPEBean.getStartPosition();
		int maxSize = m_queryCPEBean.getSize();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Devices> criteria = cb.createQuery(Devices.class);
		Root<Devices> root = criteria.from(Devices.class);

		List<Predicate> whereList = new ArrayList<>();
		criteria.where(whereList.toArray(new Predicate[whereList.size()]));

		criteria.select(root).orderBy(cb.asc(root.get("m_id")));
//		return em.createQuery(criteria).getResultList();
		return em.createQuery(criteria).setFirstResult(startPosition).setMaxResults(maxSize).getResultList();
	}
	
	/**
	 * @Title: findDevicesBySerialNumber
	 * @author: wr@yinhe.com
	 * @Description: findDevicesBySerialNumber like
	 * @param @param serialNumber
	 * @param @return
	 * @return List<Devices>
	 * @throws  
	 * @date  2017年2月25日 下午4:24:24
	 */
	public List<Devices> findDevicesBySerialNumber(String serialNumber) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Devices> criteria = cb.createQuery(Devices.class);
		Root<Devices> devices = criteria.from(Devices.class);
		Predicate predicates = cb.like(devices.<String> get("m_serialNumber"),
				"%" + serialNumber + "%");
		criteria.where(predicates);
		criteria.select(devices).orderBy(cb.desc(devices.get("m_id")));
		try {
			return em.createQuery(criteria).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/**
	 * @Title: findByQueryCPEBean
	 * @author: wr@yinhe.com
	 * @Description: find Device by QueryCPEBean
	 * @param @param query
	 * @param @return
	 * @return List<Devices>
	 * @throws  
	 * @date  2017年2月28日 下午3:20:01
	 */
	public List<Devices> findByQueryCPEBean(QueryCPEBean query){
		List<Devices> devicesList = new ArrayList<Devices>();
		if(null != query.getM_serialNumber()){
			devicesList = findDevicesBySerialNumber(query.getM_serialNumber());
			return devicesList;
		}
		
		Devices devices = new Devices();
		if (null != query.getIdList() || query.getIdList().size() != 0) {
			for(Long id : query.getIdList()){
				devices = findDeviceById(id);
				devicesList.add(devices);
			}
		}
		return devicesList;
	}
	
	public int countDevices(QueryCPEBean m_queryCPEBean){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
		Root<Devices> root = criteria.from(Devices.class);
		Expression<Long> expression = root.get("m_id");
		List<Predicate> whereList = new ArrayList<>();
		criteria.where(whereList.toArray(new Predicate[whereList.size()]));
		criteria.multiselect(cb.count(expression));
		try {
			long result = em.createQuery(criteria).getSingleResult();
			log.info("getMaxCount(): " + result);
			return (int) result;
		} catch (Exception e) {
			log.info(e.getMessage());
			return 0;
		}
	}
	
}
