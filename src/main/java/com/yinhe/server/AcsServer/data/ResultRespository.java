package com.yinhe.server.AcsServer.data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.yinhe.server.AcsServer.backbean.CountResultQueryBean;
import com.yinhe.server.AcsServer.backbean.ResultQueryBean;
import com.yinhe.server.AcsServer.model.Result;
import com.yinhe.server.AcsServer.util.Resources;

public class ResultRespository extends BaseRespository<Result>{
	public int resultCount(ResultQueryBean m_queryBean){
		Long task_id = m_queryBean.getM_taskId();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
		Root<Result> root = criteria.from(Result.class);
		List<Predicate> whereList = new ArrayList<>();
		whereList.add(cb.equal(root.get("m_countResult").get("m_device").get("m_serialNumber"), m_queryBean.getM_serialNumber()));
		if(task_id != null){
			whereList.add(cb.equal(root.get("m_nodeTask").get("m_task").get("m_id"), task_id));
		}
		try {
			criteria.multiselect(cb.count(root)).where(whereList.toArray(new Predicate[whereList.size()]));
			return em.createQuery(criteria).getSingleResult().intValue();
		} catch (Exception e) {
			return 0;
		}
	}
	
	public List<Result> listResult(ResultQueryBean m_queryBean){
		Long task_id = m_queryBean.getM_taskId();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Result> criteria = cb.createQuery(Result.class);
		Root<Result> root = criteria.from(Result.class);
		List<Predicate> whereList = new ArrayList<Predicate>();
		whereList.add(cb.equal(root.get("m_countResult").get("m_device").get("m_serialNumber"), m_queryBean.getM_serialNumber()));
		if(task_id != null){
			whereList.add(cb.equal(root.get("m_nodeTask").get("m_task").get("m_id"), task_id));
		}
		criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getResultList();
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Result> findByCountResultId(Long countResult_id){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Result> criteria = cb.createQuery(Result.class);
		Root<Result> root = criteria.from(Result.class);
		List<Predicate> whereList = new ArrayList<Predicate>();
		if(countResult_id != null){
			whereList.add(cb.equal(root.get("m_countResult").get("m_id"), countResult_id));
		}
		criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getResultList();
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Result> listResultByCountResultAndTaskId(Long countResult_id,CountResultQueryBean m_queryBean){
		Long task_id = m_queryBean.getM_taskId();
		Long node_id = m_queryBean.getM_nodeId();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Result> criteria = cb.createQuery(Result.class);
		Root<Result> root = criteria.from(Result.class);
		List<Predicate> whereList = new ArrayList<Predicate>();
		if(node_id != null){
			whereList.add(cb.equal(root.get("m_nodeTask").get("m_node").get("m_id"), node_id));
		}
		if(countResult_id != null){
			whereList.add(cb.equal(root.get("m_countResult").get("m_id"), countResult_id));
		}
		if(task_id != null){
			whereList.add(cb.equal(root.get("m_nodeTask").get("m_task").get("m_id"), task_id));
		}
		criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getResultList();
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Result> listResultByCountResultAndTaskId(Long countResult_id,ResultQueryBean m_queryBean){
		Long task_id = m_queryBean.getM_taskId();
		Long node_id = m_queryBean.getM_nodeId();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Result> criteria = cb.createQuery(Result.class);
		Root<Result> root = criteria.from(Result.class);
		List<Predicate> whereList = new ArrayList<Predicate>();
		if(node_id != null){
			whereList.add(cb.equal(root.get("m_nodeTask").get("m_node").get("m_id"), node_id));
		}
		if(countResult_id != null){
			whereList.add(cb.equal(root.get("m_countResult").get("m_id"), countResult_id));
		}
		if(task_id != null){
			whereList.add(cb.equal(root.get("m_nodeTask").get("m_task").get("m_id"), task_id));
		}

       		
		criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getResultList();
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Result> listReferenceResult(List<Long> idList,CountResultQueryBean m_queryBean) {
		if (idList == null || idList.size() == 0) {
			return null;
		}
		Long task_id = m_queryBean.getM_taskId();

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Result> criteria = cb.createQuery(Result.class);
		Root<Result> root = criteria.from(Result.class);

		List<Predicate> whereList = new ArrayList<>();
		whereList.add(cb.in(root.get("count_result_id")).value(idList));
		if(task_id != null){
			whereList.add(cb.equal(root.get("m_nodeTask").get("m_task").get("m_id"), task_id));
		}
		criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));

		try {
			return em.createQuery(criteria).getResultList();
		} catch (Exception e) {
			log.info("countEntities Exception: " + e.getMessage());
			return null;
		}
	}
	
	public List<Result> findByNodePath(String node_path) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Result> criteria = cb.createQuery(Result.class);
		Root<Result> root = criteria.from(Result.class);
		List<Predicate> whereList = new ArrayList<Predicate>();
		if(!Resources.isNullOrEmpty(node_path)){
			whereList.add(cb.equal(root.get("m_nodeTask").get("m_node").get("node_path"), node_path));
		}
		criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getResultList();
		} catch (Exception e) {
			return null;
		}
	}
}
