package com.global.defines;

import java.util.UUID;

public class UUIDUtil {
	
	public static String getUUID(){
		String UUID = java.util.UUID.randomUUID().toString();
		
		return UUID;
	}
	public static String getUUID(int length){
		String UUID = java.util.UUID.randomUUID().toString();
		if(length>0&&length<36)
			return UUID.substring(0,length);
		else
			return UUID;
		
	}
	public static void main(String[] args){
		System.out.println(UUIDUtil.getUUID(45));
		
	}

}

