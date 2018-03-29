package com.yinhe.server.AcsServer.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.yinhe.server.AcsServer.backbean.NodeModelQueryBean;
import com.yinhe.server.AcsServer.model.Node;
import com.yinhe.server.AcsServer.util.Resources;

public class NodeRespository   extends BaseRespository<Node> {
	// 根据id查找
	public Node findById(Long id) {
		return super.findById(id);
	}

	// 增加
	public void addNode(Node nodeModel) {
		super.addEntity(nodeModel);
	}

	// 更新
	public void updateNode(Node nodeModel) {
		super.updateEntity(nodeModel);
	}
	
	public void deleteNode(Node nodeModel){
		super.deleteEntity(nodeModel);
	}
	
	public Node findNodebyPath(String node_path){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Node> criteria = cb.createQuery(Node.class);
		Root<Node> root = criteria.from(Node.class);
		List<Predicate> whereList = new ArrayList<>();
		if(!Resources.isNullOrEmpty(node_path)){
			whereList.add(cb.equal(root.get("node_path"), node_path));
		}		
		criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	public int getNodeSize() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
		Root<Node> root = criteria.from(Node.class);
		List<Predicate> whereList = new ArrayList<>();
		Expression<Long> expression = root.get("m_id");
		criteria.multiselect(cb.count(expression)).where(whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getSingleResult().intValue();
		} catch (Exception e) {
			return 0;
		}
	}
	
	public List<Node> listAll(){			
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Node> criteria = cb.createQuery(Node.class);
		Root<Node> root = criteria.from(Node.class);
		List<Predicate> whereList = new ArrayList<Predicate>();
		criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
		try {
			return em.createQuery(criteria).getResultList();
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Node> listNodes(NodeModelQueryBean queryBean){
		//int startPosition = queryBean.getStartPosition();
		//int maxSize = queryBean.getSize();		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Node> criteria = cb.createQuery(Node.class);
		Root<Node> root = criteria.from(Node.class);
		List<Predicate> whereList = new ArrayList<Predicate>();
		criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
		try {
			//return em.createQuery(criteria).setFirstResult(startPosition).setMaxResults(maxSize).getResultList();
			return em.createQuery(criteria).getResultList();
		} catch (Exception e) {
			return null;
		}		
	}
	
	public List<String> listAllNodePath(){	
		List<String> list = new ArrayList<String>();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> criteria = cb.createQuery(String.class);
		Root<Node> root = criteria.from(Node.class);
		criteria.multiselect(root.get("node_path"));
		criteria.orderBy(cb.asc(root.get("m_id")));
		try {
			list.addAll(em.createQuery(criteria).getResultList());
		} catch (NoResultException e) {
		}
		return list;
	}	
	
	public List<Node> findNodesByIdList(List<Long> idList){
		if (null == idList || 0 == idList.size()) {
			return new ArrayList<Node>(0);
		}
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Node> criteria = cb.createQuery(Node.class);
		Root<Node> root = criteria.from(Node.class);
		List<Predicate> whereList = new ArrayList<>();
		whereList.add(cb.in(root.get("m_id")).value(idList));
		criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
		return em.createQuery(criteria).getResultList();
	}	
	
	public void flush(){
		em.flush();
	}
}
