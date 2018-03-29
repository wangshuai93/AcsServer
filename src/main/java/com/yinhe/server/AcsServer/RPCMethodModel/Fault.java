package com.yinhe.server.AcsServer.RPCMethodModel;

import org.w3c.dom.Element;

import com.yinhe.server.AcsServer.ejb.RPCMethod;
import com.yinhe.server.AcsServer.model.SoapMessageModel;

public class Fault extends RPCMethod {

	/**
	 * 
	 */
	private static final long serialVersionUID = 365185546572049216L;
	private String faultDetail;
	
	public Fault()
	{
		methodName ="Fault";
		faultDetail = "";
	}	

	public String getFaultDetail() {
		return faultDetail;
	}

	public void setFaultDetail(String faultDetail) {
		this.faultDetail = faultDetail;
	}

    @Override
    protected void addField2Body(Element body, SoapMessageModel soapMessageModel)
    {
    	
    }

    @Override
    protected void parseBody2Filed(Element body, SoapMessageModel soapMessageModel)
    {
    	
    }
    
    @Override
    public String toString()
    {
    	StringBuilder sbd = new StringBuilder();
    	sbd.append("methodName" + methodName);
    	sbd.append("faultDetail" + faultDetail);
    	return super.toString() + sbd.toString();
    }


}
