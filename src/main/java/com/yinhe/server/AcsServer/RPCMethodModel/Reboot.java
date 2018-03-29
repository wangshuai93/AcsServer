package com.yinhe.server.AcsServer.RPCMethodModel;

import org.w3c.dom.Element;

import com.yinhe.server.AcsServer.ejb.RPCMethod;
import com.yinhe.server.AcsServer.model.SoapMessageModel;

public class Reboot extends RPCMethod{

    private static final long serialVersionUID = 4803776789054848842L;
    private static final String CommandKey = "CommandKey";
    private String commandKey;

    public Reboot()
    {
        methodName = "Reboot";
        this.commandKey = "";
        this.acs2CpeEnv = false;		// 默认是CPE（Required）,ACS（Optinal）
    }

    public Reboot(String commandKey)
    {
        this();
        this.commandKey = commandKey;
    }

    public String getCommandKey()
    {
        return commandKey;
    }

    public void setCommandKey(String commandKey)
    {
        this.commandKey = commandKey;
    }

	@Override
	protected void addField2Body(Element body, SoapMessageModel soapMessageModel) {
		Element commandItem = soapMessageModel.createElement(CommandKey);
        commandItem.setTextContent(commandKey);
        body.appendChild(commandItem);
		
	}

	@Override
	protected void parseBody2Filed(Element body,SoapMessageModel soapMessageModel) {
		commandKey = getRequestChildElement(body, CommandKey).getTextContent();		
	}
}
