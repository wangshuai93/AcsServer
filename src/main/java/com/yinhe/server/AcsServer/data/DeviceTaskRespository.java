package com.yinhe.server.AcsServer.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.yinhe.server.AcsServer.model.DeviceTask;
import com.yinhe.server.AcsServer.model.Task;
import com.yinhe.server.AcsServer.util.Resources;


public class DeviceTaskRespository extends BaseRespository<DeviceTask>{

	public DeviceTask findDeviceTaskByIdAndType(Long id, String type){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<DeviceTask> criteria = cb.createQuery(DeviceTask.class);
		Root<DeviceTask> root = criteria.from(DeviceTask.class);
		List<Predicate> whereList = new ArrayList<>();
		whereList.add(cb.equal(root.get("m_device").get("m_id"), id));
		whereList.add(cb.equal(root.get("m_task").get("m_name"), type));
		criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<DeviceTask> findDeviceTaskBySerialNumber(String serialNumber){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<DeviceTask> criteria = cb.createQuery(DeviceTask.class);
		Root<DeviceTask> root = criteria.from(DeviceTask.class);
		List<Predicate> whereList = new ArrayList<>();
		whereList.add(cb.equal(root.get("m_device").get("m_serialNumber"), serialNumber));
		criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getResultList();
		} catch (Exception e) {
			return null;
		}
	
	}
	
	public DeviceTask findDeviceTaskBySerialNumberAndTaskId(String serialNumber,Long task_id){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<DeviceTask> criteria = cb.createQuery(DeviceTask.class);
		Root<DeviceTask> root = criteria.from(DeviceTask.class);
		List<Predicate> whereList = new ArrayList<>();
		if(!Resources.isNullOrEmpty(serialNumber)){
			whereList.add(cb.equal(root.get("m_device").get("m_serialNumber"), serialNumber));
		}
		if(task_id != null){
			whereList.add(cb.equal(root.get("m_task").get("m_id"), task_id));
		}
		criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	
	}
	
	public List<DeviceTask> findDeviceTaskByTask(Task task){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<DeviceTask> criteria = cb.createQuery(DeviceTask.class);
		Root<DeviceTask> root = criteria.from(DeviceTask.class);
		List<Predicate> whereList = new ArrayList<>();
		whereList.add(cb.equal(root.get("m_task"), task));
		criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getResultList();
		} catch (Exception e) {
			return null;
		}
	
	}
}
