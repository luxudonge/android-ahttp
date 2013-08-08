package com.alex.http.request;

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
	public void onStartRequest(int requestId);
	
	/**
	 * 结束请求
	 * @param requestId
	 */
	public void onFinishRequest(int requestId);
	
	/**
	 * 重复请求
	 * @param requestId
	 * @param count 
	 */
	public void onRepeatRequest(int requestId,int count);
	
}
