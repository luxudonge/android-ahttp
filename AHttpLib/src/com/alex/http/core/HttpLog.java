package com.alex.http.core;

import android.util.Log;

public class HttpLog {
	
	public static AHttpLogListener mAHttpLogListener;
	
	public static void print(Object o,String msg){
		if(!HttpConfiguration.mLoggingEnabled)
			return ;
		Log.i(o.getClass().getSimpleName(), msg);
	}
	
	public static void print(Object o,int requestId,String msg){
		if(!HttpConfiguration.mLoggingEnabled)
			return ;
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(o.getClass().getSimpleName());
		sbuf.append("/");
		sbuf.append(requestId);
		Log.i(sbuf.toString(), msg);
	}
	
	public static void print(String tag,String msg){
		if(!HttpConfiguration.mLoggingEnabled)
			return ;
		Log.i(tag, msg);
		if(mAHttpLogListener != null){
			mAHttpLogListener.onListeners(tag, msg);
		}
	}
	
	public interface AHttpLogListener{
		void onListeners(String tag,String msg);
	}
}
