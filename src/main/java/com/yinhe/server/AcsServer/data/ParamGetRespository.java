package com.yinhe.server.AcsServer.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.yinhe.server.AcsServer.model.NodeTask;
import com.yinhe.server.AcsServer.model.ParamGet;
import com.yinhe.server.AcsServer.model.Task;

public class ParamGetRespository extends BaseRespository<ParamGet>{
	// 根据id查找
	public ParamGet findById(Long id) {
		return super.findById(id);
	}

	// 增加
	public void addParamGet(ParamGet paramGet) {
		super.addEntity(paramGet);
	}

	// 更新
	public void updateParamGet(ParamGet paramGet) {
		super.updateEntity(paramGet);
	}
	
	public void deleteParamGet(ParamGet paramGet){
		super.deleteEntity(paramGet);
	}		
	
	public List<ParamGet> findNodesByTask(Task task){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ParamGet> criteria = cb.createQuery(ParamGet.class);
		Root<ParamGet> root = criteria.from(ParamGet.class);
		List<Predicate> whereList = new ArrayList<>();
		if(task != null){
			whereList.add(cb.equal(root.get("m_nodeTask").get("m_task"), task));
		}		
		criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getResultList();
		} catch (NoResultException e) {
			return null;
		}			
	}
	
	public ParamGet findParamGet(NodeTask nt){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ParamGet> criteria = cb.createQuery(ParamGet.class);
		Root<ParamGet> root = criteria.from(ParamGet.class);
		List<Predicate> whereList = new ArrayList<>();
		if(nt != null){
			whereList.add(cb.equal(root.get("m_nodeTask"), nt));
		}		
		criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}	
	}
	
	public List<ParamGet> findNodesByTaskId(Long task_id){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ParamGet> criteria = cb.createQuery(ParamGet.class);
		Root<ParamGet> root = criteria.from(ParamGet.class);
		List<Predicate> whereList = new ArrayList<>();
		if(task_id != null){
			whereList.add(cb.equal(root.get("m_nodeTask").get("m_task").get("m_id"), task_id));
		}		
		criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getResultList();
		} catch (NoResultException e) {
			return null;
		}			
	}
}
