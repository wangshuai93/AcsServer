package com.yinhe.server.AcsServer.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.yinhe.server.AcsServer.backbean.ThresholdValueQueryBean;
import com.yinhe.server.AcsServer.model.ThresholdValue;

public class ThresholdValueRespository extends BaseRespository<ThresholdValue>{
	// 根据id查找
	public ThresholdValue findById(Long id) {
		return super.findById(id);
	}

	// 增加
	public void addThresholdValue(ThresholdValue thresholdValue) {
		super.addEntity(thresholdValue);
	}

	// 更新
	public void updateThresholdValue(ThresholdValue thresholdValue) {
		super.updateEntity(thresholdValue);
	}
	
	public void deleteThresholdValue(ThresholdValue thresholdValue){
		super.deleteEntity(thresholdValue);
	}
		
	public ThresholdValue findThresholdValueByNodeId(Long id){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ThresholdValue> criteria = cb.createQuery(ThresholdValue.class);
		Root<ThresholdValue> root = criteria.from(ThresholdValue.class);
		List<Predicate> whereList = new ArrayList<>();
		if(null != id){
			whereList.add(cb.equal(root.get("nodeModel").get("m_id"), id));
		}
		criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public int getThresholdValueSize(){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
		Root<ThresholdValue> root = criteria.from(ThresholdValue.class);
		List<Predicate> whereList = new ArrayList<>();
		Expression<Long> expression = root.get("m_id");
		criteria.multiselect(cb.count(expression)).where(
		whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getSingleResult().intValue();
		} catch (Exception e) {
			return 0;
		}
	}
	
	public List<ThresholdValue> getAllThresholdValue(ThresholdValueQueryBean m_queryBean){
		int startPosition = m_queryBean.getStartPosition();
		int maxSize = m_queryBean.getSize();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ThresholdValue> criteria = cb.createQuery(ThresholdValue.class);
		Root<ThresholdValue> root = criteria.from(ThresholdValue.class);

		List<Predicate> whereList = new ArrayList<>();
		criteria.where(whereList.toArray(new Predicate[whereList.size()]));

		criteria.select(root).orderBy(cb.asc(root.get("m_id")));
		return em.createQuery(criteria).setFirstResult(startPosition).setMaxResults(maxSize).getResultList();
	}
	
}
