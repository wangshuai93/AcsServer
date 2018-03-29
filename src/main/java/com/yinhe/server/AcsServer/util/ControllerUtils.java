package com.yinhe.server.AcsServer.util;

import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.yinhe.server.AcsServer.controller.LoginBean;


public class ControllerUtils {

	/**
	 * @return operator ip address
	 */
	public static String getOperatorIpAddress() {
		HttpServletRequest request = (HttpServletRequest) externalContext().getRequest();
		String ipAddress = request.getRemoteAddr();
		if (ipAddress == null) {
			ipAddress = request.getHeader("X-FORWARDED-FOR");
		}
		System.out.println("[getOperatorIpAddress] getOperatorIpAddress: " + ipAddress);
		return ipAddress;
	}
	
	public static String getOperatorName() {
		Object o = getSessionMap().get(LoginBean.m_loginUserName);
		// System.out.println("[getOperatorName] employeename: " + (String) o);
		return o instanceof String ? (String) o : null;
	}
	private static ExternalContext externalContext() {
		return FacesContext.getCurrentInstance().getExternalContext();
	}
	
	public static Map<String, Object> getSessionMap() {
		return externalContext().getSessionMap();
	}
}
