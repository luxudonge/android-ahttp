package com.alex.http.core;

public final class HttpRequestConfiguration {

	private final int mSendNumber;
	
	private HttpRequestConfiguration(Builder builder){
		mSendNumber = builder.mSendNumber;
	}

	
	public static class Builder{
		
		private int mSendNumber;
		
		public HttpRequestConfiguration build(){
			return new HttpRequestConfiguration(this);
		}
		
		public void setSendNumber(int count){
			mSendNumber = count;
		}
	}
	
	public int getNumber(){
		return mSendNumber;
	}
	
}
