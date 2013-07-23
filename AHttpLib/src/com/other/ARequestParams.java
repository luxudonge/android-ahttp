package com.other;

import java.util.concurrent.ConcurrentHashMap;

public abstract class ARequestParams {

	public static String ENDCODING = "UTF-8";
	
	private ConcurrentHashMap<String, String> mUrlParams;
	
	public ARequestParams(){
		mUrlParams = new ConcurrentHashMap<String, String>();
	}
	
	public void putUrlParam(String key,int value){
		putUrlParam(key, String.valueOf(value));
	}
	
	public void putUrlParam(String key,long value){
		putUrlParam(key, String.valueOf(value));
	}
	
	public void putUrlParam(String key,String value){
		if(key!=null && value != null){
			mUrlParams.put(key, value);
		}
	}
	
}
