package com.yinhe.server.AcsServer.RPCMethodModel;

import org.w3c.dom.Element;

import com.yinhe.server.AcsServer.ejb.RPCMethod;
import com.yinhe.server.AcsServer.model.SoapMessageModel;

public class GetParameterNames extends RPCMethod
{
	private static final long serialVersionUID = -4543243305187171983L;
	private static final String ParameterPath = "ParameterPath";
    private static final String NextLevel = "NextLevel";
    
    private String parameterPath;
    private boolean nextLevel;
    
    /**
     * nextLevel true:CPE返回的消息包括所有ParameterPath参数中指定字符串相关参数的完全路径名以及它的子节点的路径名。
     * false:CPE返回的消息包括所有ParameterPath参数中指定字符串开始参数的完全路径名。
     */
    public GetParameterNames() 
    {
        methodName = "GetParameterNames";
        parameterPath = ".";
        nextLevel = false; 
    }
    public GetParameterNames(String parameterPath, boolean nextLevel) 
    {
        this ();
        this.parameterPath = parameterPath;
        this.nextLevel = nextLevel;
    }

    @Override
    protected void addField2Body(Element body, SoapMessageModel soapMessageModel)
    {
        Element parameterItem = soapMessageModel.createElement(ParameterPath);
        parameterItem.setTextContent(parameterPath);
        Element nextlevelItem = soapMessageModel.createElement(NextLevel);
        nextlevelItem.setTextContent(Boolean.toString(nextLevel));
        body.appendChild(parameterItem);
        body.appendChild(nextlevelItem);   
    }

    @Override
    protected void parseBody2Filed(Element body, SoapMessageModel soapMessageModel)
    {
    	parameterPath = getRequestChildElement(body, ParameterPath).getTextContent();
    	nextLevel =  Boolean.parseBoolean((getRequestChildElement(body, NextLevel).getTextContent()));
    }
	public String getParameterPath()
	{
		return parameterPath;
	}
	public void setParameterPathAndNextLevel(String parameterPath,boolean nextLevel)
	{
		this.parameterPath = parameterPath;
		this.nextLevel = nextLevel;
	}
	public boolean isNextLevel()
	{
		return nextLevel;
	}
	
    @Override
    public String toString()
    {
    	StringBuilder sbd = new StringBuilder();
    	sbd.append(" ParameterPath:" + parameterPath);
    	sbd.append(",NextLevel:" + nextLevel);
    	return super.toString() + sbd.toString();
    }
}
