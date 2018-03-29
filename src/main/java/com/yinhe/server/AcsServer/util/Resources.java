/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yinhe.server.AcsServer.util;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.codec.digest.DigestUtils;
import org.jboss.security.auth.spi.Util;

/**
 * This class uses CDI to alias Java EE resources, such as the persistence context, to CDI beans
 * 
 * <p>
 * Example injection on a managed bean field:
 * </p>
 * 
 * <pre>
 * &#064;Inject
 * private EntityManager em;
 * </pre>
 */
public class Resources {

	private static ResourceBundle bundle;
	
    @Produces
    @PersistenceContext
    private EntityManager em;

    @Produces
    public Logger produceLog(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

    @Produces
    @RequestScoped
    public FacesContext produceFacesContext() {
        return FacesContext.getCurrentInstance();
    }
    
    private synchronized static void messageBundleInit() {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			bundle = FacesContext
					.getCurrentInstance()
					.getApplication()
					.getResourceBundle(context, "msgs");
		} catch (Exception e) {
			//System.out.println("[Resources] [messageBundleInit] error: " + e.getStackTrace()[0]);
		}
	}

    /**
	 * get property from messagezh.properties
	 * 
	 * @param key:String
	 * @return localString
	 */
	public static String getLocalName(String key) {
		try {
			if (null == bundle) {
				messageBundleInit();
			}
			return bundle.getString(key);
		} catch (Exception e) {
			// System.out.println("get getLocalName Exception e: " +
			// e.getMessage());
			return key;
		}
	}

	/**
	 * 对用户账户密码进行加密
	 * @param str (can't be null);
	 * @return
	 */
	public static String encode(String password) {
		password = DigestUtils.md5Hex(password);
		return Util.createPasswordHash("SHA-256", Util.BASE64_ENCODING, null, null, password.toLowerCase(Locale.ENGLISH));
	}
    public static boolean isNull(String str){
    	if("".equals(str) || str == null || str.trim().length() == 0){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    public static boolean isNullOrEmpty(String s) {
		return s == null ? true : "".equals(s.trim());
	}

}
