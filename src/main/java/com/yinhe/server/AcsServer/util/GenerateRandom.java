package com.yinhe.server.AcsServer.util;

import java.util.Random;

public class GenerateRandom {

	public static String getRandomString(int length){
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random rand = new Random();
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<length;i++){
			int number = rand.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
}
