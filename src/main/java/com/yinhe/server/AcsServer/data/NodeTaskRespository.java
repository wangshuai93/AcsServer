package com.yinhe.server.AcsServer.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.yinhe.server.AcsServer.model.NodeTask;
import com.yinhe.server.AcsServer.model.Task;
import com.yinhe.server.AcsServer.util.Resources;

public class NodeTaskRespository extends BaseRespository<NodeTask>{
	// 根据id查找
		public NodeTask findById(Long id) {
			return super.findById(id);
		}

		// 增加
		public void addNode(NodeTask nodeTask) {
			super.addEntity(nodeTask);
		}

		// 更新
		public void updateNode(NodeTask nodeTask) {
			super.updateEntity(nodeTask);
		}
		
		public void deleteNode(NodeTask nodeTask){
			super.deleteEntity(nodeTask);
		}
		
		public List<Long> listAllNodeId(){			
			List<Long> list = new ArrayList<Long>();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
			Root<NodeTask> root = criteria.from(NodeTask.class);
			criteria.multiselect(root.get("m_node").get("m_id"));
			criteria.orderBy(cb.asc(root.get("m_id")));
			try {
				list.addAll(em.createQuery(criteria).getResultList());
			} catch (NoResultException e) {
			}
			return list;
		}
		
		public List<NodeTask> findNodesByTask(Task task){
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<NodeTask> criteria = cb.createQuery(NodeTask.class);
			Root<NodeTask> root = criteria.from(NodeTask.class);
			List<Predicate> whereList = new ArrayList<>();
			whereList.add(cb.equal(root.get("m_task"), task));
			criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
			try {
				return em.createQuery(criteria).getResultList();
			} catch (NoResultException e) {
				return null;
			}			
		}
		
		public List<NodeTask> findNodesByTaskName(String task_name){
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<NodeTask> criteria = cb.createQuery(NodeTask.class);
			Root<NodeTask> root = criteria.from(NodeTask.class);
			List<Predicate> whereList = new ArrayList<>();
			if(!Resources.isNullOrEmpty(task_name)){
				whereList.add(cb.equal(root.get("m_task").get("m_name"), task_name));
			}
			//criteria.select(root.get("m_node").get("node_path")).where(whereList.toArray(new Predicate[whereList.size()]));
			criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
			try {
				return em.createQuery(criteria).getResultList();
			} catch (NoResultException e) {
				return null;
			}			
		}
		
		public List<NodeTask> findNodesByTaskId(Long task_id){
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<NodeTask> criteria = cb.createQuery(NodeTask.class);
			Root<NodeTask> root = criteria.from(NodeTask.class);
			List<Predicate> whereList = new ArrayList<>();
			if(task_id != null){
				whereList.add(cb.equal(root.get("m_task").get("m_id"), task_id));
			}
			criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
			try {
				return em.createQuery(criteria).getResultList();
			} catch (NoResultException e) {
				return null;
			}			
		}
		
		public NodeTask findNodesByTaskIdAndNodeId(Long node_id,Long task_id){
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<NodeTask> criteria = cb.createQuery(NodeTask.class);
			Root<NodeTask> root = criteria.from(NodeTask.class);
			List<Predicate> whereList = new ArrayList<>();
			if(node_id != null){
				whereList.add(cb.equal(root.get("m_node").get("m_id"), node_id));
			}
			if(task_id != null){
				whereList.add(cb.equal(root.get("m_task").get("m_id"), task_id));
			}
			criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
			try {
				return em.createQuery(criteria).getSingleResult();
			} catch (NoResultException e) {
				return null;
			}			
		}
		
		public List<NodeTask> findNodesByTaskAndNodeIdList(Long task_id,List<Long> id_list){
			if (null == id_list || 0 == id_list.size()) {
				return new ArrayList<NodeTask>(0);
			}
			
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<NodeTask> criteria = cb.createQuery(NodeTask.class);
			Root<NodeTask> root = criteria.from(NodeTask.class);
			List<Predicate> whereList = new ArrayList<>();
			if(task_id != null){
				whereList.add(cb.equal(root.get("m_task").get("m_id"), task_id));
			}
			whereList.add(cb.in(root.get("m_node").get("m_id")).value(id_list));
			criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
			try {
				return em.createQuery(criteria).getResultList();
			} catch (NoResultException e) {
				return null;
			}			
		}
		
		
		public NodeTask findNodeTaskByNodeId(Long node_id){
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<NodeTask> criteria = cb.createQuery(NodeTask.class);
			Root<NodeTask> root = criteria.from(NodeTask.class);
			List<Predicate> whereList = new ArrayList<>();
			if(node_id != null){
				whereList.add(cb.equal(root.get("m_node").get("m_id"), node_id));
			}
			criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
			try {
				return em.createQuery(criteria).getSingleResult();
			} catch (NoResultException e) {
				return null;
			}			
		}
		
		public NodeTask findNodeTaskByParamPath(String param_path){
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<NodeTask> criteria = cb.createQuery(NodeTask.class);
			Root<NodeTask> root = criteria.from(NodeTask.class);
			List<Predicate> whereList = new ArrayList<>();
			if(!Resources.isNullOrEmpty(param_path)){
				whereList.add(cb.equal(root.get("m_node").get("node_path"), param_path));
			}
			criteria.select(root).where(whereList.toArray(new Predicate[whereList.size()]));
			try {
				return em.createQuery(criteria).getSingleResult();
			} catch (NoResultException e) {
				return null;
			}			
		}

}
