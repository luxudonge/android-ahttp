package com.alex.http.request;

public interface StateListeners {

	public void onStartRequest(int requestId);
	
	public void onFinishRequest(int requestId);
	
	public void onRepeatRequest(int requestId,int count);
	
}
