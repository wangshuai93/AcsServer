package com.yinhe.server.AcsServer.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.yinhe.server.AcsServer.model.Roles;

public class RolesRespository extends BaseRespository<Roles>{

	public Roles findByRoleName(String roleName){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Roles> criteria = cb.createQuery(Roles.class);
		Root<Roles> roles = criteria.from(Roles.class);

		List<Predicate> whereList = new ArrayList<>();
		whereList.add(cb.equal(roles.get("m_rolename"), roleName));
		criteria.select(roles).where(
				whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Roles> listRoles(){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Roles> criteria = cb.createQuery(Roles.class);
		Root<Roles> roles = criteria.from(Roles.class);
		criteria.select(roles);
		try {
			return em.createQuery(criteria).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
}
