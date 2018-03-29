package com.yinhe.server.AcsServer.ejb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.yinhe.server.AcsServer.backbean.NodeModelDetailBean;
import com.yinhe.server.AcsServer.data.NodeRespository;
import com.yinhe.server.AcsServer.model.Node;
import com.yinhe.server.AcsServer.util.Document;

@Stateless
public class TreeNodeEJB {
	@Inject
	private NodeRespository m_nodeRespository;
	@Inject
	private Logger log;
	
	public TreeNode createTree(){
		log.info("[createTree] get into createTree");
	    TreeNode root = new DefaultTreeNode("root",null);
		List<Node> list = new ArrayList<Node>();
		list = m_nodeRespository.findAllOrderedByIdDesc();
		int temp = 0;
		for(Node dm : list){
			int len = dm.getNode_path().split("\\.").length;
			if(len > temp){
				temp = len;
			}
		}
		
		Map<String,TreeNode> map = new HashMap<String,TreeNode>();
		for(int i = 1; i < temp + 1; i++){
			for(Node dm : list){	
				if(i == 1){
					if(1 == dm.getNode_path().split("\\.").length){	
					    TreeNode tn = new DefaultTreeNode(dm.getNode_path() + ".",root);
					    map.put(dm.getNode_path(), tn);
					}
				}else{
					if(i == dm.getNode_path().split("\\.").length){
	                    int j = dm.getNode_path().lastIndexOf(".");
	                    if(j > 0){
	                    	 String str = dm.getNode_path().substring(0, j); 
	                         String name = dm.getNode_path().substring(j);
	                         TreeNode t = map.get(str);
	                         if(t != null){
	                        	 if(dm.getType().equals("node")){
	                        		 TreeNode tn = new DefaultTreeNode(name + ".",t);
		                             map.put(dm.getNode_path(), tn);
	                        	 }else{
	                        		 TreeNode tn = new DefaultTreeNode(name,t);
		                             map.put(dm.getNode_path(), tn);
	                        	 }
	                        	 
	                         }                      
	                    }                  
					}
				}
								
			}
		}
		return root;
	}
	
	public NodeModelDetailBean findByPath(String path){	
		log.info("[findByPath] path = " + path);
		Node node = new Node();
		node = m_nodeRespository.findNodebyPath(path);
		if(node == null){
			log.info("[findByPath] node == null");
			return null;
		}else{
			NodeModelDetailBean nm = new NodeModelDetailBean(node);
			return nm;
		}		
	}
	
	public TreeNode createCheckboxTree(){
		log.info("[createCheckboxTree] get into createCheckboxTree");
		TreeNode root = new CheckboxTreeNode(new Document("root", 0L, ""), null);
		List<Node> list = new ArrayList<Node>();
		list = m_nodeRespository.findAllOrderedByIdDesc();
		
		//节点的最大长度
		int temp = 0;
		for(Node dm : list){
			int len = dm.getNode_path().split("\\.").length;
			if(len > temp){
				temp = len;
			}
		}
		
		//生成节点树
		Map<String,TreeNode> map = new HashMap<String,TreeNode>();
		for(int i = 1; i < temp + 1; i++){
			for(Node dm : list){	
				
				if(i == 1){
					if(1 == dm.getNode_path().split("\\.").length){	
					    TreeNode tn = new CheckboxTreeNode(new Document(dm.getAbbr_name(), dm.getM_id(),dm.getType()),root);
					    map.put(dm.getNode_path(), tn);
					}
				}else{
					if(i == dm.getNode_path().split("\\.").length){
	                    int j = dm.getNode_path().lastIndexOf(".");
	                    if(j > 0){
	                    	 String str = dm.getNode_path().substring(0, j); 
	                         TreeNode t = map.get(str);
	                         if(t != null){
	                        	 TreeNode tn = new CheckboxTreeNode(new Document(dm.getAbbr_name(), dm.getM_id(), dm.getType()),t);
	                             map.put(dm.getNode_path(), tn);
	                         }                      
	                    }                  
					}
				}
								
			}
		}
		return root;
	}
}
