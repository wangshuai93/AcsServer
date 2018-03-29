package com.yinhe.server.AcsServer.ejb;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.yinhe.server.AcsServer.model.SoapMessageModel;
import com.yinhe.server.AcsServer.struct.FaultStruct;


public abstract class RPCMethod implements Serializable {

	@Inject
	private Logger log;
	private static final long serialVersionUID = -5424823002848936758L;
	// 所有soap-xml命名声明全局变量，TODO cwmp-1-1?
	public static final String URN_CWMP1_0 = "urn:dslforum-org:cwmp-1-0";
	public static final String URN_CWMP1_1 = "urn:dslforum-org:cwmp-1-1";
	public static final String URL_XSD = "http://www.w3.org/2001/XMLSchema";
	public static final String URL_XSI = "http://www.w3.org/2001/XMLSchema-instance";
	public static final String URL_ENCODE = "http://schemas.xmlsoap.org/soap/encoding/";
	public static final String URL_ENVELOPE = "http://schemas.xmlsoap.org/soap/envelope/";
	public static final String CWMP = "cwmp";
	public static final String XSD = "xsd";
	public static final String XSI = "xsi";
	public static final String SOAP_ENC = "soap";	
	public static final String SOAP_ENV = "soap";
	public static final String soap_env= "soap-env";
	public static final String SOAPENC = "SOAP-ENC";
	public static final String SOAPENV = "SOAP-ENV";
	public static final String XMLNS = "xmlns";           // xml命名空间
	public static final String ARRAY_TYPE = "arrayType";
	public static final String FAULT = "Fault";
	private static final String FaultCode = "FaultCode";
	private static final String FaultString = "FaultString";
	// 所有soap-xml头声明全局变量
	private static final String ID = "ID"; // 必填项
	private static final String HoldRequests = "HoldRequests"; // 从cpe回复到acs中不需要,默认0为false,1为true
	private static final String MustUnderstand = "SOAP-ENV:mustUnderstand"; // 标识是否存在
	// 通用属性,头信息
	protected String m_requestId; // 每一次请求的id，由请求方决定
	protected String methodName; // 方法名称
	protected boolean acs2CpeEnv; // 是否是acs端到cpe端的信包
	private boolean holdReqs; // 如果ACS需要更新控制流，可以设置，并且mustunderstand为1，只能使用在acs中
	private boolean requeired; // 是否该方法是必选的
	protected FaultStruct faultStruct;
	private String urnCwmpVersion; // 选用CWMP版本
	protected String id;

    public String getId() {
        if (id == null) {
            id = "ID:intrnl.unset.id." + ((methodName != null) ? methodName : "") + (Calendar.getInstance().getTimeInMillis() + 3600 * 1000) + "." + hashCode();
        } 
        return id;
    }
	/**
	 * 默认代表从ACS到CPE的信包
	 */
	public RPCMethod() {
		iniModel(true);
	}

	/**
	 * @param acs2CpeEnv
	 *            ,为true时代表从ACS到CPE的信包，否则反过来
	 */
	public RPCMethod(boolean acs2CpeEnv) {
		iniModel(acs2CpeEnv);
	}

	private void iniModel(boolean acs2CpeEnv) {
		this.acs2CpeEnv = acs2CpeEnv;
		this.holdReqs = false;
		this.requeired = true;
		this.methodName = "";
		this.m_requestId = "";
		urnCwmpVersion = URN_CWMP1_0; // 默认版本
	}

	/**
	 * @return 为true时代表从ACS到CPE的信包，否则反过来
	 */
	public boolean isAcs2CpeEnv() {
		return acs2CpeEnv;
	}

	/**
	 * @param acs2CpeEnv
	 *            为true时代表从ACS到CPE的信包，否则反过来
	 */
	public void setAcs2CpeEnv(boolean acs2CpeEnv) {
		this.acs2CpeEnv = acs2CpeEnv;
	}

	public boolean isHoldReqs() {
		return holdReqs;
	}

	public void setHoldReqs(boolean holdReqs) {
		this.holdReqs = holdReqs;
	}

	public boolean isRequeired() {
		return requeired;
	}

	public void setRequeired(boolean requeired) {
		this.requeired = requeired;
	}

	public String getRequestId() {
		return m_requestId;
	}
	
	public String getUrnCwmpVersion() {
		return urnCwmpVersion;
	}

	public void setUrnCwmpVersion(String urnCwmpVersion) {
		if (URN_CWMP1_0.equalsIgnoreCase(urnCwmpVersion)) {
			this.urnCwmpVersion = URN_CWMP1_0;
		} else {
			this.urnCwmpVersion = URN_CWMP1_1;
		}
	}
	
	public String getMethodName(){
		return this.methodName;
	}

	
	public FaultStruct getFaultStruct() {
		return faultStruct;
	}
	public void setFaultStruct(FaultStruct faultStruct) {
		this.faultStruct = faultStruct;
	}
	
	protected abstract void addField2Body(Element body,
			SoapMessageModel soapMessageModel);

	protected abstract void parseBody2Filed(Element body,
			SoapMessageModel soapMessageModel);

	/**
	 * 解析出Soap中的方法名称
	 */
	public static String getRequestName(SoapMessageModel soapMessageModel) {
		String name = getRequest(soapMessageModel).getNodeName();
		if (name.startsWith("cwmp:")) {
			name = name.substring(5);
		} else {
			name = FAULT;
			/*try {
				Element faultElement = getFaultElement(soapMessageModel);
				if(faultElement != null) {
					NodeList childNodes = faultElement.getChildNodes();
					int length = childNodes.getLength();
					for (int index = 0; index < length; index++) {
						Node item = childNodes.item(index);
						if (item instanceof Element) {
							String nodeName = item.getNodeName();
							if (!nodeName.equals(FaultCode)
									&& !nodeName.equals(FaultString)
									&& nodeName.contains(FAULT)) {
								//return nodeName.substring(0,(nodeName.length() - 5));
								return nodeName;
							}
						}
					}					
				}
			} catch (Exception e) {
				name = FAULT;
				System.out.println("getRequestName Error!");
			}*/
		}
		return name;
	}

	public static Element getRequest(SoapMessageModel soapMessageModel) {
		Element request = null;
		NodeList iter = soapMessageModel.getSoapBody().getChildNodes();
		for (int index = 0; index < iter.getLength(); index++) {
			Node n = iter.item(index);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				request = (Element) n;
			}
		}
		return request;
	}

	protected static Element getFaultElement(SoapMessageModel soapMessageModel) {
		Element soapFaultElement = getRequestChildElement(soapMessageModel.getSoapBody(), soap_env + ":" + FAULT);
		if(soapFaultElement == null){
			soapFaultElement = getRequestChildElement(soapMessageModel.getSoapBody(), SOAP_ENV + ":" + FAULT);
		}
		Element detailElement = getRequestChildElement(soapFaultElement,"detail");
		Element fElement = getRequestChildElement(detailElement, CWMP + ":"+ FAULT);
		return fElement;
	}

	protected static Element getRequestChildElement(Element req, String name) {
		Element result = null;
		NodeList nodeList = req.getChildNodes();
		for (int index = 0; index < nodeList.getLength(); index++) {
			Node item = nodeList.item(index);
			if (item.getNodeName().equalsIgnoreCase(name)&& item instanceof Element) {
				result = (Element) item;
				break;
			}
		}
		return result;
	}

	protected static String getRequestElement(Element req, String name) {
		return getRequestChildElement(req, name).getTextContent();
	}

	/**
	 * 将SOAPMessage 解析值相应的数据模型值中
	 */
	public void parse(SoapMessageModel soapMessageModel)throws NoSuchElementException {
		if (soapMessageModel.getSoapHeader() != null&& soapMessageModel.getSoapHeader().hasChildNodes()) {
			NodeList headList = soapMessageModel.getSoapHeader().getChildNodes();
			for (int index = 0; index < headList.getLength(); index++) {
				Node headItem = headList.item(index);
				if (headItem.getNodeName().startsWith(CWMP + ":" + ID)) {
					m_requestId = headItem.getTextContent();
				}
			}
		}
		try {
			Element body = getRequestChildElement(soapMessageModel.getSoapBody(), CWMP + ":" + methodName);
			if (!isFault()) {
				parseBody2Filed(body, soapMessageModel);
			} else {
				Element fElement = getFaultElement(soapMessageModel);
				//错误时的处理方式
				parseFault(fElement);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[RPCMethod-parseFault] Parse request error:" + e.getMessage());
		}
	}

	public boolean isFault() {
		return methodName.equals(FAULT);
	}

	protected void parseFault(Element detailEntry) {
		//格式如下： 
		/* <detail>
		<cwmp:Fault>
		<FaultCode>9006</FaultCode>
		<FaultString>Invalid parameter type</FaultString>
		<SetParameterValuesFault>
		<ParameterName>Device.ManagementServer.PeriodicInformInterval</ParameterName>
		<FaultCode>9006</FaultCode>
		<FaultString>Invalid parameter type</FaultString>
		</SetParameterValuesFault>
		</cwmp:Fault>
		</detail>*/
		faultStruct = new FaultStruct(Integer.parseInt(getRequestElement(detailEntry, FaultCode)),
				getRequestElement(detailEntry,FaultString));
		System.out.println("[RPCMethod-parseFault] methodName=" + methodName + " faultStruct="+ faultStruct);
		//parseFaultDetail(detailEntry.getElementsByTagName(methodName + FAULT));
	}
	
	//错误的详细信息
    /*protected void parseFaultDetail(NodeList details) {
		// TODO 转为抽象方法，各个具体错误信息
	}*/

	/**
	 * 将soap消息写入输出流
	 */
	public int writeTo(OutputStream out) throws IOException {
		int length = 0;
		try {		
			SoapMessageModel msg = new SoapMessageModel();
			addAllSoap(msg);
			length = msg.writeTo(out);
		} catch (Exception e) {
			log.info("[RPCMethod-writeTo] reply info write error ");
			e.printStackTrace();
		}
		return length;
	}

	// 添加标准SoapMessage命名空间和头部
	private void addAllSoap(SoapMessageModel soapMessageModel) {
		soapMessageModel.getDocument().setXmlStandalone(true);
		addNameSpaceDeclaretion(soapMessageModel);
		addIdElement2Header(soapMessageModel, m_requestId);
		if (acs2CpeEnv) {
			if (holdReqs) {
				addHoldReqsElement2Header(soapMessageModel, holdReqs);
			}
		}
		Element bodyElement = soapMessageModel.createElementNS(methodName,CWMP, urnCwmpVersion);
		soapMessageModel.getSoapBody().appendChild(bodyElement);
		try {
			addField2Body(bodyElement, soapMessageModel);
		} catch (Exception e) {
			log.info("[RPCMethod-addAllSoap] Add Field2Body error!");
		}
	}
	
	private void addNameSpaceDeclaretion(SoapMessageModel soapMessageModel) {
		// 需添加前缀xmlns
		soapMessageModel.getRootNode().setAttribute(XMLNS + ":" + SOAPENV, URL_ENVELOPE);
        soapMessageModel.getRootNode().setAttribute(XMLNS + ":" + SOAPENC, URL_ENCODE);
        soapMessageModel.getRootNode().setAttribute(XMLNS + ":" + XSD, URL_XSD);
        soapMessageModel.getRootNode().setAttribute(XMLNS + ":" + XSI, URL_XSI);
        soapMessageModel.getRootNode().setAttribute(XMLNS + ":" + CWMP, urnCwmpVersion);
	}

	private void addIdElement2Header(SoapMessageModel soapMessageModel,String idValue) {
		Element cwmpIdE = soapMessageModel.createElementNS(ID, CWMP,urnCwmpVersion);
		cwmpIdE.setTextContent(idValue);    // 设置element值;setTextContent此属性返回此节点及其后代的文本内容;setNodeValue()此节点的值，取决于其类型
		cwmpIdE.setAttribute(MustUnderstand, "1");   // 必须标识为1
		soapMessageModel.getSoapHeader().appendChild(cwmpIdE);
	}

	private void addHoldReqsElement2Header(SoapMessageModel soapMessageModel,boolean request) {
		Element cwmpIdE = soapMessageModel.createElementNS(HoldRequests, CWMP,urnCwmpVersion);
		cwmpIdE.setTextContent(request == true ? "1" : "0"); // 设置element值;setTextContent此属性返回此节点及其后代的文本内容;setNodeValue()此节点的值，取决于其类型
		cwmpIdE.setAttribute(MustUnderstand, "1");           // 必须标识为1
		soapMessageModel.getSoapHeader().appendChild(cwmpIdE);
	}
	
	public static void getArrayTypeAttribute(Element paramAttrElement, String arrayType, int arrayLen)
	{
	    paramAttrElement.setAttribute((XSI + ":type"), (SOAP_ENC + ":Array"));
	    paramAttrElement.setAttribute((SOAP_ENC + ":arrayType"), (CWMP + ":" + arrayType + "[" + String.valueOf(arrayLen) + "]"));
	}
	
	public static void getArrayTypeAttributeToCPE(Element paramAttrElement, String arrayType, int arrayLen)
	{
	    paramAttrElement.setAttribute((XSI + ":type"), (SOAPENC + ":Array"));
	    paramAttrElement.setAttribute((SOAPENC + ":arrayType"), (CWMP + ":" + arrayType + "["+ String.valueOf(arrayLen) + "]"));
	}
}
