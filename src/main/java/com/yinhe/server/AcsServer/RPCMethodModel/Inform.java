package com.yinhe.server.AcsServer.RPCMethodModel;


import org.w3c.dom.Element;

import com.yinhe.server.AcsServer.ejb.RPCMethod;
import com.yinhe.server.AcsServer.model.SoapMessageModel;
import com.yinhe.server.AcsServer.struct.DeviceId;
import com.yinhe.server.AcsServer.struct.Event;
import com.yinhe.server.AcsServer.struct.ParameterList;


public class Inform extends RPCMethod{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1834192088548155370L;
	public static final String MAXENV = "MaxEnvelopes";
    public static final String RETRYCOUNT = "RetryCount";
    public static final String CURRENTTIME = "CurrentTime";
    private DeviceId m_deviceId;
    private Event m_event;
    private int m_retryCount;
    private int m_maxEnvelopes;
    private String m_currentTime;
    private ParameterList m_parameterList;
    
    public Inform() 
    {
    	methodName = "Inform";
        m_deviceId = new DeviceId();
        m_event = new Event();
        m_parameterList = new ParameterList();
    }

    public DeviceId getDeviceId()
	{
		return m_deviceId;
	}

	public Event getEvent()
	{
		return m_event;
	}

    public ParameterList getParameterList()
	{
		return m_parameterList;
	}
    @Override
    public String toString()
    {
    	StringBuilder sbd = new StringBuilder();
    	sbd.append(" DeviceId:" + m_deviceId);
    	sbd.append("\r\nMaxEnvelopes:" + m_maxEnvelopes);
    	sbd.append(" RetryCount:" + m_retryCount);
    	sbd.append(" CurrentTime:" + m_currentTime);
    	sbd.append("\r\nEvent:" + m_event);
    	sbd.append("\r\nParameterList:" + m_parameterList.toString());
    	return super.toString() + sbd.toString();
    }

    public int getRetryCount()
    {
        return m_retryCount;
    }

    public void setRetryCount(int retryCount)
    {
        this.m_retryCount = retryCount;
    }

    public String getCurrentTime()
    {
        return m_currentTime;
    }

    public void setCurrentTime(String currentTime)
    {
        this.m_currentTime = currentTime;
    }

    public int getMaxEnvelopes()
    {
        return m_maxEnvelopes;
    }

    public void setMaxEnvelopes(int maxEnvelopes)
    {
    	if(maxEnvelopes <= 0)
    	{
    		 this.m_maxEnvelopes = 1;
    	}
    	else
    	{
    		this.m_maxEnvelopes = maxEnvelopes;
    	}
    }
	@Override
	protected void addField2Body(Element body, SoapMessageModel soapMessageModel) {
		m_deviceId.addThisToBody(body, soapMessageModel);
		m_event.addThisToBody(body, soapMessageModel);
        Element maxnv = soapMessageModel.createElement(MAXENV);
        maxnv.setTextContent(Integer.toString((m_maxEnvelopes)));
        Element retry = soapMessageModel.createElement(RETRYCOUNT);
        retry.setTextContent(Integer.toString(m_retryCount));
        Element curren = soapMessageModel.createElement(CURRENTTIME);
        curren.setTextContent(m_currentTime);
        body.appendChild(maxnv);
        body.appendChild(retry);
        body.appendChild(curren);
        m_parameterList.addThisToBody(body, soapMessageModel);
		
	}

	@Override
	protected void parseBody2Filed(Element body,
			SoapMessageModel soapMessageModel) {
		m_deviceId.parseBodyOfThis(body, soapMessageModel);
		m_maxEnvelopes = Integer.parseInt(getRequestElement(body, MAXENV));
		m_retryCount = Integer.parseInt(getRequestElement(body, RETRYCOUNT));
		m_currentTime = getRequestElement(body, CURRENTTIME);
		m_event.parseBodyOfThis(body, soapMessageModel);
		m_parameterList.parseBodyOfThis(body, soapMessageModel);
		
	}
}
