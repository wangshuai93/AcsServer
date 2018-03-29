package com.yinhe.server.AcsServer.RPCMethodModel;

import org.w3c.dom.Element;

import com.yinhe.server.AcsServer.ejb.RPCMethod;
import com.yinhe.server.AcsServer.model.SoapMessageModel;

public class TransferCompleteResponse extends RPCMethod{

	private static final long serialVersionUID = 477408010724841186L;

	public TransferCompleteResponse()
    {
        methodName = "TransferCompleteResponse";
    }

    @Override
    protected void addField2Body(Element body, SoapMessageModel soapMessageModel)
    {
        // TODO Auto-generated method stub
    }

    @Override
    protected void parseBody2Filed(Element body, SoapMessageModel soapMessageModel)
    {
        // TODO Auto-generated method stub
    }
}
