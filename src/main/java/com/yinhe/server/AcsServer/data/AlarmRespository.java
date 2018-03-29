package com.yinhe.server.AcsServer.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.yinhe.server.AcsServer.backbean.AlarmQueryBean;
import com.yinhe.server.AcsServer.model.Alarm;

public class AlarmRespository extends BaseRespository<Alarm>{

	public List<Alarm> getAlarmList(AlarmQueryBean m_queryBean){
		int startPosition = m_queryBean.getStartPosition();
		int maxSize = m_queryBean.getSize();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Alarm> criteria = cb.createQuery(Alarm.class);
		Root<Alarm> root = criteria.from(Alarm.class);
		List<Predicate> whereList = new ArrayList<>();
		criteria.where(whereList.toArray(new Predicate[whereList.size()]));

		criteria.select(root).orderBy(cb.desc(root.get("m_id")));
		try {
			return em.createQuery(criteria).setFirstResult(startPosition).setMaxResults(maxSize).getResultList();
		} catch (Exception e) {
			return null;
		}
	}
	
	public int getAlarmingSize() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
		Root<Alarm> root = criteria.from(Alarm.class);
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
}