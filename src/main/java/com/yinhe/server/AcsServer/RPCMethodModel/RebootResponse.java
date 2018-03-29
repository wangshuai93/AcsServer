package com.yinhe.server.AcsServer.RPCMethodModel;

import org.w3c.dom.Element;

import com.yinhe.server.AcsServer.ejb.RPCMethod;
import com.yinhe.server.AcsServer.model.SoapMessageModel;

public class RebootResponse extends RPCMethod{
	private static final long serialVersionUID = 5343616840954981656L;

	public RebootResponse()
    {
        methodName = "RebootResponse";
    }

    @Override
    protected void addField2Body(Element body, SoapMessageModel soapMessageModel)
    {
        // TODO 无应答参数

    }

    @Override
    protected void parseBody2Filed(Element body, SoapMessageModel soapMessageModel)
    {
        // TODO 无应答参数

    }
}
