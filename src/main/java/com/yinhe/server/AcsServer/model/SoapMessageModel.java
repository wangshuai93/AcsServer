package com.yinhe.server.AcsServer.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*
 * 构建soap结构的处理结构，主要方便其他节点调用使用的
 */
public class SoapMessageModel
{	
	private Element rootNode;

	private Element soapHeader;

	private Element soapBody;

	private NamedNodeMap soapNameSpace;

	private Document document;
	
	private final String SOAPHeader_V2 = "soap:Header";
	private final String SOAPBody_V2 = "soap:Body";
	private final String SOAPHeader_V1 = "soap-env:Header";
	private final String SOAPBody_V1 = "soap-env:Body";

	public NamedNodeMap getSoapNameSpace()
	{
		return soapNameSpace;
	}

	public Element getRootNode()
	{
		return rootNode;
	}

	public Element getSoapHeader()
	{
		return soapHeader;
	}

	public Element getSoapBody()
	{
		return soapBody;
	}

	public Document getDocument()
	{
		return document;
	}

     public Element createElementNS(String nodeName,String namespaceURI)
    {
       Element item = document.createElementNS(namespaceURI, nodeName);
       return item;
    }

    public Element createElementNS(String localName, String prefix, String namespaceURI)
    {
        String nodeName = prefix + ":" + localName;
        Element item = document.createElementNS(namespaceURI, nodeName);
        return item;
    }

    public Element createElement(String nodeName,String nodeValue)
    {
       Element item = document.createElement(nodeName);
       item.setNodeValue(nodeValue);
       return item;
    }

    public Element createElement(String nodeName)
    {
       Element item = document.createElement(nodeName);
       return item;
    }

    /**
     * 构建一个初始化的数据结构
     */
    public SoapMessageModel()
    {
        try
        {
        	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            Element rootElement = doc.createElement("SOAP-ENV:Envelope");
            doc.appendChild(rootElement);
            rootNode = doc.getDocumentElement();
            soapHeader = doc.createElement("SOAP-ENV:Header");
            soapBody =doc.createElement("SOAP-ENV:Body");
            rootNode.appendChild(soapHeader);
            rootNode.appendChild(soapBody);
            document = doc;
            soapNameSpace = document.getAttributes();
        }
        catch(Exception e)
        {
            System.out.println("[SoapMessageModel.SoapMessageModel] build a init SoapMessageModel error!");
        }
    }
    
    /**
     * 根据对象生成结构
     * @param document
     */
    public SoapMessageModel(Document document)
    {
        this.document = document;
        rootNode = document.getDocumentElement();     //根节点   "SOAP-ENV:Envelope"
        NodeList listNode = rootNode.getChildNodes();
        int length = listNode.getLength();
        soapNameSpace = document.getAttributes();
        for(int index = 0; index < length ;index ++ )
        {
            Node item = listNode.item(index);
            if(item.getNodeName().startsWith("#"))
            {
                continue;
            }
            if(item.getNodeName().equalsIgnoreCase(SOAPHeader_V1) || item.getNodeName().equalsIgnoreCase(SOAPHeader_V2))
            {
                soapHeader = (Element)item;
            }
            if(item.getNodeName().equalsIgnoreCase(SOAPBody_V1) || item.getNodeName().equalsIgnoreCase(SOAPBody_V2))
            {
                soapBody = (Element)item;
            }
        }
    }
    
    public static String getXMLString(Document document) throws TransformerException
    {
    	TransformerFactory tf = TransformerFactory.newInstance();
    	Transformer t = tf.newTransformer();
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	t.transform(new DOMSource(document), new StreamResult(bos));
    	String xmlStr = bos.toString();
    	return xmlStr;
    }

    public int writeTo(OutputStream out) throws DOMException, IOException
    {
    	if(out != null)
    	{
    		String xmlStr = null;
			try
			{
				xmlStr = getXMLString(document);
				
			} catch (TransformerException e)
			{
				System.out.println("[SoapMessageModel.writeTo] TransformerException"+e.getMessage());
				return 0;
			}
			xmlStr = xmlStr.substring(38);
    		byte[] bytes = xmlStr.getBytes();
    		out.write(bytes);
    		System.out.print("[SoapMessageModel.writeTo] write by xml:");
    		System.out.println(xmlStr + " ContextLength:" + bytes.length );							
			return bytes.length;
    	}
        return 0;
    }
    
  /*  public static String getXMLString(Document document) throws TransformerException
    {
    	TransformerFactory tf = TransformerFactory.newInstance();
    	Transformer t = tf.newTransformer();
    	// t.setOutputProperty("encoding","utf-8");	//	解决中文问题
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	t.transform(new DOMSource(document), new StreamResult(bos));
    	String xmlStr = bos.toString();
    	return xmlStr;
    }
      public byte[] writeTo(OutputStream out) throws DOMException, IOException
    {
    	String xmlStr = null;
		try
		{
			xmlStr = getXMLString(document);
		} catch (TransformerException e)
		{
			e.printStackTrace();
			return null;
		}
		byte[] bytes = xmlStr.getBytes();
		System.err.println("Write by xml:");
		System.out.println("\t" + xmlStr + " ContextLength:" + bytes.length + "\r\n---------------------------------------------------------------------\r\n");
		return bytes;
    }*/ 

}
