package com.yinhe.server.AcsServer.data;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.yinhe.server.AcsServer.backbean.QueryTaskResultBean;

public class BaseRespository<T> {
	public final String deleteExceptionPre = "deleteEntitiesException";
	protected Class<T> clazz;
	@Inject
	protected Logger log;

	@Inject
	protected EntityManager em;
	protected String _modelId = "m_id";
	protected String _modelProvider = "provider";

	@SuppressWarnings("unchecked")
	public BaseRespository() {
		try {
			Type genType = this.getClass().getGenericSuperclass();
			Type[] params = ((ParameterizedType) genType)
					.getActualTypeArguments();
			this.clazz = (Class<T>) params[0];
		} catch (Exception e) {
			log.info("[BaseRespository] BaseRespository set T error: " + e.getMessage());
		}
	}

	/**
	 * 如何不是直接继承的该基类则需要设置该泛型的参数
	 * 
	 * @param clazz
	 */
	protected void setClassT(Class<T> clazz) {
		this.clazz = clazz;
	}

	// 根据id查找
	public T findById(Number id) {
		try {
			// return id == null ? null : em.find(clazz, id);
			if (null == id) {
				return null;
			}

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<T> criteria = cb.createQuery(this.clazz);
			Root<T> root = criteria.from(this.clazz);

			List<Predicate> whereList = new ArrayList<>();

			whereList.add(cb.equal(root.get(_modelId), id));
			criteria.where(whereList.toArray(new Predicate[whereList.size()]));
			List<T> list = em.createQuery(criteria).getResultList();
			if (list.isEmpty())
				return null;
			if (list.size() == 1)
				return list.get(0);
			throw new NonUniqueResultException();

		} catch (Exception e) {
			log.info("[findById]" + this.getClass() + "findById(" + id + ") error:"
					+ e.getMessage());
			return null;
		}
	}

	// 增加
	public void addEntity(T entity) {
		em.persist(entity);
	}

	// 更新
	public void updateEntity(T entity) {
		em.merge(entity);
	}

	public int countEntities(List<Long> idList) {
		if (idList == null || idList.size() == 0) {
			return 0;
		}

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
		Root<T> root = criteria.from(clazz);

		List<Predicate> whereList = new ArrayList<>();
		whereList.add(cb.in(root.get("m_id")).value(idList));

		criteria.where(whereList.toArray(new Predicate[whereList.size()]));
		criteria.select(cb.count(root)).where();

		try {
			Long count = em.createQuery(criteria).getSingleResult();
			return count.intValue();
		} catch (Exception e) {
			log.info("[countEntities] countEntities Exception: " + e.getMessage());
			return 0;
		}
	}

	// 删除
	public void deleteEntity(T entity) {
		em.remove(entity);
	}

	// 删除
	public void deleteEntity(Number id) {
		em.remove(findById(id));
	}

	/**
	 * @Title: findAllOrderedByM_Id
	 * @author: wr@yinhe.com
	 * @Description: 
	 * @param @return
	 * @return List<T>
	 * @throws
	 * @date 2017年2月27日 上午9:40:44
	 */
	public List<T> findAllOrderedByM_Id() {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> criteria = cb.createQuery(clazz);
		Root<T> root = criteria.from(clazz);

		List<Predicate> whereList = new ArrayList<>();
		criteria.where(whereList.toArray(new Predicate[whereList.size()]));

		criteria.select(root).orderBy(cb.asc(root.get("m_id")));
		return em.createQuery(criteria).getResultList();
	}

	/**
	 * @Title: findAllOrderedByIdDesc
	 * @author: wr@yinhe.com
	 * @Description: 
	 * @param @return
	 * @return List<T>
	 * @throws
	 * @date 2017年2月27日 上午9:41:14
	 */
	public List<T> findAllOrderedByIdDesc() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> criteria = cb.createQuery(clazz);
		Root<T> root = criteria.from(clazz);

		List<Predicate> whereList = new ArrayList<>();
		criteria.where(whereList.toArray(new Predicate[whereList.size()]));

		criteria.select(root).orderBy(cb.asc(root.get("m_id")));
		return em.createQuery(criteria).getResultList();
	}

	public List<T> findByDeviceIdList(List<Long> idList) {
		if (null == idList || 0 == idList.size()) {
			return new ArrayList<T>(0);
		}
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> criteria = cb.createQuery(clazz);
		Root<T> root = criteria.from(clazz);
		List<Predicate> whereList = new ArrayList<>();
		whereList.add(cb.in(root.get("m_device").get("m_id")).value(idList));
		criteria.select(root).where(
				whereList.toArray(new Predicate[whereList.size()]));
		return em.createQuery(criteria).getResultList();
	}

	/**
	 * @Title: findBrocastResultByTime
	 * @author: wr@yinhe.com
	 * @Description: 按照id、开始时间和结束时间查询
	 * @param @param id
	 * @param @param query
	 * @param @return
	 * @return List<T>
	 * @throws
	 * @date 2017年3月9日 上午11:42:29
	 */
	public List<T> findTaskResultByIdAndTime(Long id,
			QueryTaskResultBean query) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> criteria = cb.createQuery(clazz);
		Root<T> root = criteria.from(clazz);
		Predicate[] predicates = _getQueryWhere(cb, criteria, root, id, query);
		criteria.select(root).where(predicates);
		try {
			return em.createQuery(criteria).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Predicate[] _getQueryWhere(CriteriaBuilder cb,
			CriteriaQuery<?> criteria, Root<T> root, Long id,
			QueryTaskResultBean query) {
		Date beforeTime = query.getBeforeTime();
		Date afterTime = query.getAfterTime();
		List<Predicate> whereList = new ArrayList<Predicate>();
		if (null != id && !"".equals(id)) {
			whereList.add(cb.equal(root.get("m_device").get("m_id"), id));
		}
		if (null != beforeTime && !"".equals(beforeTime) && null != afterTime
				&& !"".equals(afterTime)) {
			Expression<Date> expTime = root.get("m_reportTime");
			whereList.add(cb.between(expTime, query.getBeforeTime(),
					query.getAfterTime()));
		}
		return whereList.toArray(new Predicate[whereList.size()]);
	}

	/**
	 * 将所有托管状态的bean改变为游离态
	 */
	public void clearEntity() {
		em.clear();
	}
}
