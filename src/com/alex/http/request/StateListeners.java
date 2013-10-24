package com.alex.http.request;

import com.alex.http.core.HttpRequest;

/**
 * 
 * 状态监听
 * 
 * @author Alex.Lu
 *
 */
public interface StateListeners {

	/**
	 * 开始请求
	 * @param requestId
	 */
	public void onStartRequest(HttpRequest request,int requestId);
	
	/**
	 * 结束请求
	 * @param requestId
	 */
	public void onFinishRequest(HttpRequest request,int requestId);
	
	/**
	 * 重复请求
	 * @param requestId
	 * @param count 
	 */
	public void onRepeatRequest(HttpRequest request,int requestId,int count);
	
}
