package com.alex.http.core;

public final class AHttpRequestConfiguration {

	private final int mSendNumber;
	
	private AHttpRequestConfiguration(Builder builder){
		mSendNumber = builder.mSendNumber;
	}

	
	public static class Builder{
		
		private int mSendNumber;
		
		public AHttpRequestConfiguration build(){
			return new AHttpRequestConfiguration(this);
		}
		
		public void setSendNumber(int count){
			mSendNumber = count;
		}
	}
	
	public int getNumber(){
		return mSendNumber;
	}
	
}
