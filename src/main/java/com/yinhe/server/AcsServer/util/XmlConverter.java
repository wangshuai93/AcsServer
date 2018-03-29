package com.yinhe.server.AcsServer.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.yinhe.server.AcsServer.backbean.NodeModelDetailBean;

public class XmlConverter{ 
	
	private Document document;
	private Element root_node;
	List<NodeModelDetailBean> nodelist = new ArrayList<NodeModelDetailBean>();
	private String node_path;
	private boolean flag = true;	
	
	public void parseXML(String fileName) throws Exception{
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
	    document = db.parse(fileName);	
	    root_node = document.getDocumentElement();
	    
	    node_path = root_node.getAttribute("name");
	    NodeModelDetailBean nodeModel = new NodeModelDetailBean();
	    nodeModel.setAbbr_name(root_node.getAttribute("name"));
		nodeModel.setNode_path(node_path);
		nodeModel.setType(root_node.getAttribute("type"));
		nodeModel.setRw(root_node.getAttribute("rw"));
		nodeModel.setIl(root_node.getAttribute("il"));
		//add 
		nodeModel.setAcl(root_node.getAttribute("acl"));
		nodeModel.setGetc(root_node.getAttribute("getc"));
		nodeModel.setMax_value(root_node.getAttribute("max_value"));
		nodeModel.setMin_value(root_node.getAttribute("min_value"));
		nodeModel.setNoc(root_node.getAttribute("noc"));
		nodeModel.setNocc(root_node.getAttribute("nocc"));
		nodeModel.setOther_value(root_node.getAttribute("other_value"));
		nodeModel.setUnit(root_node.getAttribute("unit"));
		nodeModel.setValue_type(root_node.getAttribute("value_type"));
		
		nodelist.add(nodeModel);
		
	    NodeList nodeList = root_node.getChildNodes();
	    parse(nodeList);	
	}
	
	public void parse(NodeList nodeList) throws Exception{
		for(int i = 0; i < nodeList.getLength(); i++){		
	    	Node node = nodeList.item(i);
	    	Element element = null;
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				element = (Element)node;
				NodeList node2= element.getChildNodes();
				node_path += "." + element.getAttribute("name");
				// add  
				if(node2.getLength() < 2){
					NodeModelDetailBean nodeModel = new NodeModelDetailBean();
					nodeModel.setAbbr_name(element.getAttribute("name"));
					nodeModel.setNode_path(node_path);
					nodeModel.setType(element.getAttribute("type"));
					nodeModel.setRw(element.getAttribute("rw"));
					nodeModel.setGetc(element.getAttribute("getc"));
					nodeModel.setNoc(element.getAttribute("noc"));
					nodeModel.setNocc(element.getAttribute("nocc"));
					nodeModel.setAcl(element.getAttribute("acl"));
					nodeModel.setMax_value(element.getAttribute("max_value"));
					nodeModel.setMin_value(element.getAttribute("min_value"));
					nodeModel.setNocc(element.getAttribute("nocc"));
					nodeModel.setOther_value(element.getAttribute("other_value"));
					nodeModel.setUnit(element.getAttribute("unit"));
					nodeModel.setValue_type(element.getAttribute("value_type"));
					nodeModel.setIl(element.getAttribute("il"));
				    flag =true;
					try{
						node2.item(0).getTextContent();
					}catch(Exception ex){
						flag = false;
					}
					if(flag){
						nodeModel.setDefault_value(node2.item(0).getTextContent());
					}		
					nodelist.add(nodeModel);
				}else{
					NodeModelDetailBean nodeModel = new NodeModelDetailBean();
					nodeModel.setAbbr_name(element.getAttribute("name"));
					nodeModel.setNode_path(node_path);
					nodeModel.setType(element.getAttribute("type"));
					nodeModel.setRw(element.getAttribute("rw"));
					nodeModel.setGetc(element.getAttribute("getc"));
					nodeModel.setNoc(element.getAttribute("noc"));
					nodeModel.setNocc(element.getAttribute("nocc"));
					nodeModel.setAcl(element.getAttribute("acl"));
					nodeModel.setMax_value(element.getAttribute("max_value"));
					nodeModel.setMin_value(element.getAttribute("min_value"));
					nodeModel.setNocc(element.getAttribute("nocc"));
					nodeModel.setOther_value(element.getAttribute("other_value"));
					nodeModel.setUnit(element.getAttribute("unit"));
					nodeModel.setValue_type(element.getAttribute("value_type"));
					nodeModel.setIl(element.getAttribute("il"));
					nodelist.add(nodeModel);
					parse(node2);
				}		
				
				int index = node_path.lastIndexOf(".");
				if(index > 0){
					node_path = node_path.substring(0,index);
				}
			}		
	    }
	}	
	
	public List<NodeModelDetailBean> getNodelist() {
		return nodelist;
	}

	public void setNodelist(List<NodeModelDetailBean> nodelist) {
		this.nodelist = nodelist;
	}

	public static void main(String[] args) {
		XmlConverter xmlConverter = null;
		try {
		     xmlConverter = new XmlConverter();
		     String fileName = System.getProperty("user.dir")+"/src/main/webapp/WEB-INF/tr.xml";
		     xmlConverter.parseXML(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
