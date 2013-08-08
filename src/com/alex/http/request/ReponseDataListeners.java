package com.alex.http.request;

/**
 * 
 * 数据返回监听
 * 
 * @author Alex.Lu
 *
 */
public interface ReponseDataListeners {


	/**
	 * 数据成功返回
	 * @param requestId 请求id
	 * @param statusCode 状态码
	 * @param data 数据
	 */
	public void onSuccessResult(int requestId,int statusCode,Object data);
	
	/**
	 * 错误返回
	 * @param requestId 请求id
	 * @param statusCode 状态码
	 * @param e 错误
	 */
	public void onErrorResult(int requestId,int statusCode,Throwable e);
	
}
