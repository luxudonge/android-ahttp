package com.alex.http.request;

public interface ReponseDataListeners {

	public void onSuccessResult(int requestId,int statusCode,Object data);
	
	public void onErrorResult(int requestId,int statusCode,Throwable e);
	
}
