package com.yinhe.server.AcsServer.RPCMethodModel;


import org.w3c.dom.Element;

import com.yinhe.server.AcsServer.ejb.RPCMethod;
import com.yinhe.server.AcsServer.model.SoapMessageModel;
import com.yinhe.server.AcsServer.struct.ParameterList;


public class SetParameterValues extends RPCMethod
{
	private static final long serialVersionUID = -7720852412139686283L;
    private static final String ParameterKey = "ParameterKey";
    
    private ParameterList parameterList;
    private String parameterKey;

    public SetParameterValues()
    {
        methodName = "SetParameterValues";
        parameterList = new ParameterList();
    }
    
    public SetParameterValues(ParameterList parameter_list)
    {
        methodName = "SetParameterValues";
        parameterList = new ParameterList();
        parameterList = parameter_list;
    }


    @Override
    protected void addField2Body(Element body, SoapMessageModel soapMessageModel)
    {
        parameterList.addThisToBody(body, soapMessageModel);
        Element parameter = soapMessageModel.createElement(ParameterKey);
        parameter.setTextContent(parameterKey);
        body.appendChild(parameter);
    }

    @Override
    protected void parseBody2Filed(Element body, SoapMessageModel soapMessageModel)
    {
    	parameterList.parseBodyOfThis(body, soapMessageModel);
    	parameterKey = getRequestElement(body,ParameterKey);
    }
    
	public String getParamterKey()
	{
		return parameterKey;
	}

	public void setParamterKey(String key)
	{
		this.parameterKey = key;
	}

	public ParameterList getParameterList()
	{
		return parameterList;
	}
}
