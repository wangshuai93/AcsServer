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
import com.yinhe.server.AcsServer.model.CountResult;
import com.yinhe.server.AcsServer.model.Devices;
import com.yinhe.server.AcsServer.util.Resources;

public class CountResultRespository  extends BaseRespository<CountResult>{

	public int resultCount(Devices device){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
		Root<CountResult> root = criteria.from(CountResult.class);
		List<Predicate> whereList = new ArrayList<>();		
		whereList.add(cb.equal(root.get("m_device"), device));
		try {
			criteria.multiselect(cb.count(root)).where(whereList.toArray(new Predicate[whereList.size()]));
			return em.createQuery(criteria).getSingleResult().intValue();
		} catch (Exception e) {
			return 0;
		}
	}
	
	public List<CountResult> listCountResult(CountResultQueryBean m_queryBean){
		String serialNumber = m_queryBean.getM_serialNumber();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CountResult> criteria = cb.createQuery(CountResult.class);
		Root<CountResult> root = criteria.from(CountResult.class);
		List<Predicate> whereList = new ArrayList<>();		
		if(!Resources.isNullOrEmpty(serialNumber)){
			whereList.add(cb.equal(root.get("m_device").get("m_serialNumber"), serialNumber));
		}
		
        Expression<Date> exp = root.get("m_reportTime");
        if(m_queryBean.getM_beginTime() != null && m_queryBean.getM_endTime() != null)
        	whereList.add(cb.between(exp, m_queryBean.getM_beginTime(), new Timestamp(m_queryBean.getM_endTime().getTime())));
        else if(m_queryBean.getM_endTime() != null)
        	whereList.add(cb.lessThanOrEqualTo(exp, new Timestamp(m_queryBean.getM_endTime().getTime())));
        else if(m_queryBean.getM_beginTime() != null)
        	whereList.add(cb.greaterThanOrEqualTo(exp, m_queryBean.getM_beginTime()));
		
		try {
			criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()])).orderBy(cb.desc(root.get("m_id")));
			return em.createQuery(criteria).getResultList();
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<CountResult> listCountResult(ResultQueryBean m_queryBean){
		String serialNumber = m_queryBean.getM_serialNumber();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CountResult> criteria = cb.createQuery(CountResult.class);
		Root<CountResult> root = criteria.from(CountResult.class);
		List<Predicate> whereList = new ArrayList<>();		
		if(!Resources.isNullOrEmpty(serialNumber)){
			whereList.add(cb.equal(root.get("m_device").get("m_serialNumber"), serialNumber));
		}
		
        Expression<Date> exp = root.get("m_reportTime");
        if(m_queryBean.getM_beginTime() != null && m_queryBean.getM_endTime() != null)
        	whereList.add(cb.between(exp, m_queryBean.getM_beginTime(), new Timestamp(m_queryBean.getM_endTime().getTime())));
        else if(m_queryBean.getM_endTime() != null)
        	whereList.add(cb.lessThanOrEqualTo(exp, new Timestamp(m_queryBean.getM_endTime().getTime())));
        else if(m_queryBean.getM_beginTime() != null)
        	whereList.add(cb.greaterThanOrEqualTo(exp, m_queryBean.getM_beginTime()));
        
		try {
			criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()])).orderBy(cb.desc(root.get("m_id")));
			return em.createQuery(criteria).getResultList();
		} catch (Exception e) {
			return null;
		}
	}
}
