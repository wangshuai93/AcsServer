package com.yinhe.server.AcsServer.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.yinhe.server.AcsServer.backbean.TaskQueryBean;
import com.yinhe.server.AcsServer.model.Task;
import com.yinhe.server.AcsServer.util.Resources;

public class TaskRespository extends BaseRespository<Task>{
	
	/*public List<Task> findTaskByDeviceIdList(List<Long> idList) {
		if (null == idList || 0 == idList.size()) {
			return new ArrayList<Task>(0);
		}
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Task> criteria = cb.createQuery(Task.class);
		Root<Task> root = criteria.from(Task.class);
		List<Predicate> whereList = new ArrayList<>();
		whereList.add(cb.in(root.get("m_device").get("m_id")).value(idList));
		criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
		return em.createQuery(criteria).getResultList();
	}*/
	
	public Task findTaskByIdAndName(Long id, String name){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Task> criteria = cb.createQuery(Task.class);
		Root<Task> root = criteria.from(Task.class);
		List<Predicate> whereList = new ArrayList<>();
		whereList.add(cb.equal(root.get("m_device").get("m_id"), id));
		whereList.add(cb.equal(root.get("m_name"), name));
		criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	public Task findByType(String type){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Task> criteria = cb.createQuery(Task.class);
		Root<Task> root = criteria.from(Task.class);
		List<Predicate> whereList = new ArrayList<>();
		if(!Resources.isNullOrEmpty(type)){
			whereList.add(cb.equal(root.get("m_name"), type));
		}		
		criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	public int getTaskSize() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
		Root<Task> root = criteria.from(Task.class);
		List<Predicate> whereList = new ArrayList<>();
		Expression<Long> expression = root.get("m_id");
		criteria.multiselect(cb.count(expression)).where(whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getSingleResult().intValue();
		} catch (Exception e) {
			return 0;
		}
	}
	
	public List<Task> listTask(TaskQueryBean queryBean){
		int startPosition = queryBean.getStartPosition();
		int maxSize = queryBean.getSize();		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Task> criteria = cb.createQuery(Task.class);
		Root<Task> root = criteria.from(Task.class);
		List<Predicate> whereList = new ArrayList<Predicate>();
		
		try {
			return em.createQuery(criteria).setFirstResult(startPosition).setMaxResults(maxSize).getResultList();
		} catch (Exception e) {
			return null;
		}		
	}
	
	public Task findTaskByTaskId(Long task_id){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Task> criteria = cb.createQuery(Task.class);
		Root<Task> root = criteria.from(Task.class);
		List<Predicate> whereList = new ArrayList<>();
		if(task_id != null){
			whereList.add(cb.equal(root.get("m_id"), task_id));
		}
		criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}			
	}
	
}
