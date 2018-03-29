package com.yinhe.server.AcsServer.RPCMethodModel;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.yinhe.server.AcsServer.ejb.RPCMethod;
import com.yinhe.server.AcsServer.model.SoapMessageModel;

public class GetRPCMethodsResponse extends RPCMethod {

    private static final long serialVersionUID = 2886019393604582937L;	
	private static final String MethodList = "MethodList";
    private static final String STR = "string";

    private List<String> methodList;

    public GetRPCMethodsResponse() 
    {
        methodName = "GetRPCMethodsResponse";
        methodList = new ArrayList<String>();
        methodList.add("Inform");
        methodList.add("TransferComplete");
        methodList.add("GetRPCMethods");        
    }
    
	@Override
	protected void addField2Body(Element body, SoapMessageModel soapMessageModel) {
		 Element methodListElement = soapMessageModel.createElement(MethodList);
	        int size = methodList.size();
	        if (size > 0)
	        {
	            getArrayTypeAttribute(methodListElement, STR, size);
	            for (String method : methodList)
	            {
	                Element item = soapMessageModel.createElement(STR);
	                item.setTextContent(method);
	                methodListElement.appendChild(item);
	            }
	        }
	        body.appendChild(methodListElement);
	}

	@Override
	protected void parseBody2Filed(Element body,SoapMessageModel soapMessageModel) {
		Element methodListElement = getRequestChildElement(body, MethodList);
        NodeList methodElements = methodListElement.getElementsByTagName(STR);
        for (int index = 0; index < methodElements.getLength(); index++)
        {
            Node methodValue = methodElements.item(index);
            methodList.add(methodValue.getTextContent());
        }
	}
	
	public String[] getMethods()
    {
    	String[] methods = new String[methodList.size()];
        return methodList.toArray(methods);
    }
    
    @Override
    public String toString()
    {
    	StringBuilder sbd = new StringBuilder();
    	sbd.append(" MethodList:" + methodList);
    	return super.toString() + sbd.toString();
    }

}
