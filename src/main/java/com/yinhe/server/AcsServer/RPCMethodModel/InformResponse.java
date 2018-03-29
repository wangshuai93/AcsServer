package com.yinhe.server.AcsServer.RPCMethodModel;

import org.w3c.dom.Element;

import com.yinhe.server.AcsServer.ejb.RPCMethod;
import com.yinhe.server.AcsServer.model.SoapMessageModel;


public class InformResponse  extends RPCMethod{

	private static final long serialVersionUID = 6681115948730320709L;
	public static final String MAXENV = "MaxEnvelopes";
    private int maxEnvelopes;
    
    public InformResponse()
    {
        methodName = "InformResponse";
    }

    public InformResponse(int maxEvn)
    {
        methodName = "InformResponse";
        maxEnvelopes = maxEvn;
    }
    
	@Override
	public void addField2Body(Element body, SoapMessageModel soapMessageModel) {
		 Element maxItem = soapMessageModel.createElement(MAXENV);
         maxItem.setTextContent(String.valueOf(maxEnvelopes));
         body.appendChild(maxItem);		
	}
	@Override
	public void parseBody2Filed(Element body, SoapMessageModel soapMessageModel) {
		maxEnvelopes = Integer.parseInt(getRequestChildElement(body,MAXENV).getTextContent());
	}
	
	 /**
     * 为0时，不限制
     * @return
     */
	public int getMaxEnvelopes()
	{
		return maxEnvelopes;
	}

	public void setMaxEnvelopes(int maxEnvelopes)
	{
		if(maxEnvelopes < 0)
		{
			this.maxEnvelopes = 0;
		}
		else
		{
			this.maxEnvelopes = maxEnvelopes;
		}
	}
	
	@Override
	public String toString()
	{
		return super.toString() + " MaxEnvelopes:" + maxEnvelopes;
	}


}
