package com.yinhe.server.AcsServer.struct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.yinhe.server.AcsServer.ejb.RPCMethod;
import com.yinhe.server.AcsServer.model.SoapMessageModel;

public class ParameterList extends AbstractStruct {

    private static final long serialVersionUID = 9119333307832052200L;
    private static final String ParameterListNode = "ParameterList";
    private static final String ParameterValueStructNode = "ParameterValueStruct";
    private static final String NameNode = "Name";
    private static final String ValueNode = "Value";
    // Intert网关必备
    public static final String SpecVesion = "InternetGatewayDevice.DeviceInfo.SpecVersion";   
    public static final String ConnectionRequestURL = "Device.ManagementServer.ConnectionRequestURL";
    public static final String ParameterKey = "InternetGatewayDevice.ManagementServer.ParameterKey";
       
    public static final String HardwareVersion = "Device.DeviceInfo.HardwareVersion";
    public static final String SoftwareVersion = "Device.DeviceInfo.SoftwareVersion";
    public static final String CurrentStbName = "Device.DeviceInfo.CurrentStbName";
    public static final String ProvisioningCode = "InternetGatewayDevice.DeviceInfo.ProvisioningCode";
    public static final String ConnectionRequestUsername = "Device.ManagementServer.ConnectionRequestUsername";
    public static final String ConnectionRequestPassword = "Device.ManagementServer.ConnectionRequestPassword";
    public static final String CMMacAddress = "Device.CM.CMMACAddress";
    public static final String STBMacAddress = "Device.Network.STBMACAddress";

    
    public static final String DecoderManufacturer = "Device.System.DecoderManufacturer";
    public static final String DecoderModel = "Device.System.DecoderModel";
    public static final String RAMSize = "Device.System.RAMSize";
    public static final String FlashSize = "Device.System.FlashSize";
    
    public static final String RAMRest = "Device.Status.RAMRest";
    public static final String FlashRest = "Device.Status.FlashRest";
    public static final String IDLE = "Device.Status.IDLE";
    public static final String SystemRunTime = "Device.Status.SystemRunTime";
    public static final String RAMUsed = "Device.Status.RAMUsed";
    public static final String CPULoad = "Device.Status.CPULoad";
    
    public static final String ExternalIPAddress = "InternetGatewayDevice.WANDevice.{i}WANConnectionDevice.{j}.WAN{***}Connection.{k}.ExternalIPAddress";
    @SuppressWarnings({ "rawtypes" })
    private List<ParameterValueStruct> parameterValueStructs;

    @SuppressWarnings("rawtypes")
	public ParameterList()
    {
        parameterValueStructs = new ArrayList<ParameterValueStruct>();
    }

    @SuppressWarnings({ "rawtypes" })
    public List<ParameterValueStruct> getParameterValueStructs()
    {
        return parameterValueStructs;
    }

    public synchronized void addParamValues(@SuppressWarnings("rawtypes") ParameterValueStruct... paramValues)
    {
        //校验？
        if (paramValues == null)
        {
            return;
        }
        addParamValues(Arrays.asList(paramValues));
    }
    
    public synchronized void addParamValues(@SuppressWarnings("rawtypes") List<ParameterValueStruct> pvsList)
    {
    	if(pvsList != null)
    	{
    		parameterValueStructs.addAll(pvsList);
    	}
    }
    
    public int size()
    {
    	return parameterValueStructs.size();
    }

    public synchronized void clear()
    {
        parameterValueStructs.clear();
    }

    public synchronized void removeParamValues(@SuppressWarnings("rawtypes") ParameterValueStruct paramValue)
    {
        parameterValueStructs.remove(paramValue);
    }

    @SuppressWarnings("rawtypes")
	@Override
    public void addThisToBody(Element body, SoapMessageModel soapMessageModel)
    {
        Element paramListElement = soapMessageModel.createElement(ParameterListNode);
        if (parameterValueStructs.size() > 0)
        {
            RPCMethod.getArrayTypeAttributeToCPE(paramListElement, ParameterValueStructNode, parameterValueStructs.size());
            for (ParameterValueStruct ps : parameterValueStructs)
            {
                Element paramenterValueStruct = soapMessageModel.createElement(ParameterValueStructNode);
                Element nameElement = soapMessageModel.createElement(NameNode);
                nameElement.setTextContent(ps.getName());
                Element valueElement = soapMessageModel.createElement(ValueNode);
                valueElement.setTextContent(String.valueOf(ps.getValue()));
                valueElement.setAttribute("xsi:type", "xsd:" + ps.getValueType());
                paramenterValueStruct.appendChild(nameElement);
                paramenterValueStruct.appendChild(valueElement);
                paramListElement.appendChild(paramenterValueStruct);
            }
        }
        body.appendChild(paramListElement);
    }

    @SuppressWarnings("rawtypes")
	@Override
    public void parseBodyOfThis(Element body, SoapMessageModel soapMessageModel)
    {
        Element paramListElement = getRequestChildElement(soapMessageModel, body, ParameterListNode);
        NodeList nodeList = paramListElement.getElementsByTagName(ParameterValueStructNode);
        for (int index = 0; index < nodeList.getLength(); index++)
        {
            Node item = nodeList.item(index);
            if (item instanceof Element)
            {
                Element nameElement = getRequestChildElement(soapMessageModel, (Element) item, NameNode);
                Element valueElement = getRequestChildElement(soapMessageModel, (Element) item, ValueNode);

                // TODO区分不同类型
                ParameterValueStruct paramStruct = null;
                String valueAttr = valueElement.getAttribute("xsi:type");
                if (valueAttr == null || valueAttr.trim().equals("")){
                    // 默认为Object方式
                    paramStruct = new ParameterValueStructObject(
                        nameElement.getTextContent(), valueElement.getTextContent());
                }else{
                    String valueType = valueAttr.split("xsd:")[1];
                    if (valueType.equals(ParameterValueStruct.Type_String)){
                    	
                    		 paramStruct = new ParameterValueStructStr(nameElement.getTextContent(), valueElement.getTextContent());
                       
                    }else if (valueType.equals(ParameterValueStruct.Type_Int)){
                    	
                    		paramStruct = new ParameterValueStructInt(nameElement.getTextContent(), Integer.valueOf(valueElement.getTextContent()));
                    
                    }else if (valueType.equals(ParameterValueStruct.Type_UnsignedInt)){
                    	
                    	if(valueElement.getTextContent() != null && valueElement.getTextContent().length() != 0){
                    		 try{
                    		      paramStruct = new ParameterValueStructUnsignedInt(nameElement.getTextContent(), Long.valueOf(valueElement.getTextContent()));
                    		 }catch(Exception ex){
                    			System.out.println("ParameterList parseBodyOfThis,type unSignedInt:" + ex.getMessage()); 
                    		 }
                    	}else{
                    		paramStruct = new ParameterValueStructUnsignedInt(nameElement.getTextContent(), 0L);
                    	}  
                    	
                    }else if (valueType.equals(ParameterValueStruct.Type_Boolean)){
                       
                    	paramStruct = new ParameterValueStructBoolean(nameElement.getTextContent(), Boolean.parseBoolean(valueElement.getTextContent()));
                   
                    }else{
                        
                    	paramStruct = new ParameterValueStructObject(nameElement.getTextContent(), valueElement.getTextContent());
                    
                    }
                }
                parameterValueStructs.add(paramStruct);
            }
        }
    }

    @Override
    public String toString()
    {
        StringBuilder sbd = new StringBuilder();
        sbd.append("ParameterValueStructs:" + Arrays.toString(parameterValueStructs.toArray()));
        return sbd.toString();
    }

}
