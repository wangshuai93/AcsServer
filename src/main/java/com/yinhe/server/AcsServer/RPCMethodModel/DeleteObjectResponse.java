package com.yinhe.server.AcsServer.RPCMethodModel;


import org.w3c.dom.Element;

import com.yinhe.server.AcsServer.ejb.RPCMethod;
import com.yinhe.server.AcsServer.model.SoapMessageModel;

/**
 */
public class DeleteObjectResponse extends RPCMethod
{
	private static final long serialVersionUID = 2439753415329414922L;
	private static final String Status = "Status";
	
	private int status;

    public DeleteObjectResponse()
    {
        methodName = "DeleteObjectResponse";
        status = 0;
    }

    @Override
    protected void addField2Body(Element body, SoapMessageModel soapMessageModel)
    {
        Element item = soapMessageModel.createElement(Status,String.valueOf(status));
        body.appendChild(item);    	
    }

    @Override
    protected void parseBody2Filed(Element body, SoapMessageModel soapMessageModel)
    {
        status = Integer.parseInt(getRequestElement(body, Status));
    }

    @Override
    public String toString()
    {
    	StringBuilder sbd = new StringBuilder();
    	sbd.append(" Status:" + status);
    	return super.toString() + sbd.toString();
    }

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}
}
