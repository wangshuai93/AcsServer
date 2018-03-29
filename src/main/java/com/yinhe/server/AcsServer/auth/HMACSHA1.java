package com.yinhe.server.AcsServer.auth;

import java.security.InvalidKeyException;  
import java.security.NoSuchAlgorithmException;  
import javax.crypto.Mac;  
import javax.crypto.spec.SecretKeySpec; 
public class HMACSHA1 {
	public static String hamcsha1(byte[] data, byte[] key) 
	{
	      try {
	          SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");
	          Mac mac = Mac.getInstance("HmacSHA1");
	          mac.init(signingKey);
	          return byte2hex(mac.doFinal(data));
	      } catch (NoSuchAlgorithmException e) {
	           e.printStackTrace();
	      } catch (InvalidKeyException e) {
	           e.printStackTrace();
	      }
	     return null;
	 }
 
	//二行制转字符串  
	public static String byte2hex(byte[] b) 
	{
	    StringBuilder hs = new StringBuilder();
	    String stmp;
	    for (int n = 0; b!=null && n < b.length; n++) {
	        stmp = Integer.toHexString(b[n] & 0XFF);
	        if (stmp.length() == 1)
	            hs.append('0');
	        hs.append(stmp);
	    }
	    return hs.toString().toUpperCase();
	}
}
