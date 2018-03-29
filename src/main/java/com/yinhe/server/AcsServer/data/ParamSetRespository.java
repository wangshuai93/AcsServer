package com.yinhe.server.AcsServer.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.yinhe.server.AcsServer.model.NodeTask;
import com.yinhe.server.AcsServer.model.ParamSet;
import com.yinhe.server.AcsServer.model.Task;

public class ParamSetRespository  extends BaseRespository<ParamSet>{
	// 根据id查找
	public ParamSet findById(Long id) {
		return super.findById(id);
	}

	// 增加
	public void addParamSet(ParamSet paramSet) {
		super.addEntity(paramSet);
	}

	// 更新
	public void updateParamSet(ParamSet paramSet) {
		super.updateEntity(paramSet);
	}
	
	public void deleteParamSet(ParamSet paramSet){
		super.deleteEntity(paramSet);
	}		
	
	public List<ParamSet> findNodesByTask(Task task){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ParamSet> criteria = cb.createQuery(ParamSet.class);
		Root<ParamSet> root = criteria.from(ParamSet.class);
		List<Predicate> whereList = new ArrayList<>();
		whereList.add(cb.equal(root.get("m_nodeTask").get("m_task"), task));
		criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getResultList();
		} catch (NoResultException e) {
			return null;
		}			
	}	
	
	public ParamSet findParamSet(NodeTask nt){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ParamSet> criteria = cb.createQuery(ParamSet.class);
		Root<ParamSet> root = criteria.from(ParamSet.class);
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
}
