package com.yinhe.server.AcsServer.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.yinhe.server.AcsServer.model.Users;
import com.yinhe.server.AcsServer.util.Resources;

public class UsersRespository extends BaseRespository<Users> {
	// 根据id查找
	public Users findById(Long id) {
		return super.findById(id);
	}

	// 增加
	public void addUsers(Users users) {
		super.addEntity(users);
	}

	// 更新
	public void updateUsers(Users users) {
		super.updateEntity(users);
	}
	
	public void deleteUsers(Users users){
		super.deleteEntity(users);
	}

	public Long getUsersSize() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
		Root<Users> root = criteria.from(Users.class);
		List<Predicate> whereList = new ArrayList<>();
		Expression<Long> expression = root.get("m_id");
		criteria.multiselect(cb.count(expression)).where(
				whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getSingleResult();
		} catch (Exception e) {
			return 0L;
		}
	}

	// 根据phoneNumber查找
	public Users findByPhoneNumber(String phoneNumber) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Users> criteria = cb.createQuery(Users.class);
		Root<Users> users = criteria.from(Users.class);

		List<Predicate> whereList = new ArrayList<>();
		whereList.add(cb.equal(users.get("m_phoneNumber"), phoneNumber));
		criteria.select(users).where(
				whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	// 根据userName查找
	public Users findByUserName(String userName) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Users> criteria = cb.createQuery(Users.class);
		Root<Users> users = criteria.from(Users.class);

		List<Predicate> whereList = new ArrayList<>();
		whereList.add(cb.equal(users.get("m_userName"), userName));
		criteria.select(users).where(
				whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public boolean Authentication(String phoneNumber, String password) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Users> criteria = cb.createQuery(Users.class);
		Root<Users> users = criteria.from(Users.class);
		// Swap criteria statements if you would like to try out type-safe
		// criteria queries, a new
		// feature in JPA 2.0
		List<Predicate> whereList = new ArrayList<>();
		whereList.add(cb.equal(users.get("m_phoneNumber"), phoneNumber));
		whereList.add(cb.equal(users.get("m_password"), password));
		criteria.select(users).where(
				whereList.toArray(new Predicate[whereList.size()]));
		try {
			Users user = em.createQuery(criteria).getSingleResult();
			return user == null ? false : true;
		} catch (NoResultException e) {
			return false;
		}
	}

	// 获取所有Users，并按名字升序排列
	public List<Users> findAllOrderedByName() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Users> criteria = cb.createQuery(Users.class);
		Root<Users> users = criteria.from(Users.class);
		// Swap criteria statements if you would like to try out type-safe
		// criteria queries, a new
		// feature in JPA 2.0
		// criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));

		List<Predicate> whereList = new ArrayList<>();
		criteria.where(whereList.toArray(new Predicate[whereList.size()]));

		criteria.select(users).orderBy(cb.asc(users.get("m_username")));
		return em.createQuery(criteria).getResultList();
	}

	// 获取所有Users，并按名字升序排列
	public Users findByNameAndPW(String username, String password) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Users> criteria = cb.createQuery(Users.class);
		Root<Users> users = criteria.from(Users.class);
		// Swap criteria statements if you would like to try out type-safe
		// criteria queries, a new
		// feature in JPA 2.0
		// criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
		List<Predicate> whereList = new ArrayList<>();
		whereList.add(cb.equal(users.get("m_username"), username));
		whereList.add(cb.equal(users.get("m_password"), password));
		criteria.select(users).where(
				whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public int getUserCount() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
		Root<Users> root = criteria.from(Users.class);
		Expression<Long> exp = root.get("m_id");

		List<Predicate> whereList = new ArrayList<>();
		criteria.where(whereList.toArray(new Predicate[whereList.size()]));

		criteria.select(cb.count(exp));
		try {
			Long count = em.createQuery(criteria).getSingleResult();
			return count == null ? 0 : count.intValue();
		} catch (Exception e) {
			return 0;
		}
	}

	public Users findByUsername(String username) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Users> criteria = cb.createQuery(Users.class);
		Root<Users> users = criteria.from(Users.class);

		List<Predicate> whereList = new ArrayList<>();
		whereList.add(cb.equal(users.get("m_userName"), username));

		criteria.select(users).where(
				whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getSingleResult();
		} catch (Exception e) {
			log.info("usersRespository findByUsername() = null where username = "
					+ username);
			return null;
		}
	}

	public Users findByUsernameLogin(String username) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Users> criteria = cb.createQuery(Users.class);
		Root<Users> users = criteria.from(Users.class);
		List<Predicate> whereList = new ArrayList<>();
		whereList.add(cb.equal(users.get("m_userName"), username));
		criteria.select(users).where(
				whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getSingleResult();
		} catch (Exception e) {
			log.info("usersRespository findByUsername() = null where username = "
					+ username);
			return null;
		}
	}

	public int countRelevances(String m_userName, String m_phoneNumber,
			String m_department) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
		Root<Users> root = criteria.from(Users.class);
		List<Predicate> whereList = new ArrayList<Predicate>();
		if(!Resources.isNullOrEmpty(m_userName)){
			whereList.add(cb.equal(root.get("m_userName"), m_userName));
		}
		if(!Resources.isNullOrEmpty(m_phoneNumber)){
			whereList.add(cb.equal(root.get("m_phoneNumber"), m_phoneNumber));
		}
		if(!Resources.isNullOrEmpty(m_department)){
			whereList.add(cb.equal(root.get("m_department"), m_department));
		}
		try {
			criteria.multiselect(cb.count(root)).where(whereList.toArray(new Predicate[whereList.size()]));
			return em.createQuery(criteria).getSingleResult().intValue();
		} catch (Exception e) {
			return 0;
		}
	}

	public List<Users> listUsers(String m_userName, String m_phoneNumber,
			String m_department, int startPosition, int maxSize) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Users> criteria = cb.createQuery(Users.class);
		Root<Users> root = criteria.from(Users.class);
		List<Predicate> whereList = new ArrayList<Predicate>();
		
		if(!Resources.isNullOrEmpty(m_userName)){
			whereList.add(cb.equal(root.get("m_userName"), m_userName));
		}
		if(!Resources.isNullOrEmpty(m_phoneNumber)){
			whereList.add(cb.equal(root.get("m_phoneNumber"), m_phoneNumber));
		}
		if(!Resources.isNullOrEmpty(m_department)){
			whereList.add(cb.equal(root.get("m_department"), m_department));
		}
		try {
			return em.createQuery(criteria).setFirstResult(startPosition).setMaxResults(maxSize).getResultList();
		} catch (Exception e) {
			return null;
		}
	}
	
}
