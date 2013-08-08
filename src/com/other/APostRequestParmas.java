package com.other;

import java.util.concurrent.ConcurrentHashMap;


public class APostRequestParmas extends ARequestParams{

	private ConcurrentHashMap<String, String> mPostParams;
	
	public APostRequestParmas(){
		super();
		mPostParams = new ConcurrentHashMap<String, String>();
	}
	
	public void putPostParam(String key,int value){
		putPostParam(key, String.valueOf(value));
	}
	
	public void putPostParam(String key,long value){
		putPostParam(key, String.valueOf(value));
	}
	
	public void putPostParam(String key,String value){
		if(key!=null && value != null){
			mPostParams.put(key, value);
		}
	}
	
}
