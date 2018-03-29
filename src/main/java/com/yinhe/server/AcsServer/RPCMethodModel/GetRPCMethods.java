package com.yinhe.server.AcsServer.RPCMethodModel;

import org.w3c.dom.Element;

import com.yinhe.server.AcsServer.ejb.RPCMethod;
import com.yinhe.server.AcsServer.model.SoapMessageModel;

public class GetRPCMethods extends RPCMethod{
	private static final long serialVersionUID = 335073989232385528L;

	public GetRPCMethods() 
	{
        methodName = "GetRPCMethods";
    }
    public GetRPCMethods(String id) 
    {
        this();
        m_requestId = id;
    }

    @Override
    protected void addField2Body(Element body, SoapMessageModel soapMessageModel)
    {
        
    }

    @Override
    protected void parseBody2Filed(Element body, SoapMessageModel soapMessageModel)
    {
        
    }

}
