package com.yinhe.server.AcsServer.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.yinhe.server.AcsServer.model.UpdateDataRecord;
import com.yinhe.server.AcsServer.util.Resources;

public class UpdateDataRecordRespository extends BaseRespository<UpdateDataRecord>{

	public UpdateDataRecord findByFileName(String fileName){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<UpdateDataRecord> criteria = cb.createQuery(UpdateDataRecord.class);
		Root<UpdateDataRecord> root = criteria.from(UpdateDataRecord.class);
		List<Predicate> whereList = new ArrayList<>();
		if(!Resources.isNullOrEmpty(fileName)){
			whereList.add(cb.equal(root.get("m_fileName"), fileName));
		}		
		criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}	
	}
}
