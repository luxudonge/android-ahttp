package com.alex.http.core;

/**
 * 
 * httpClient 配置文件
 * 
 * @author e_worm
 *
 */
public final class AHttpConfiguration {

	static boolean mLoggingEnabled;
	final int mSocketTimeout;
	final int mMaxConnectionsPerRoute;
	final int mMaxTotalConnections;
	final int mSoSocketTimeout;
	final int mConnectionTimeout;
	final int mSocketBufferSize;
	final int mThreadPoolSize;
	
	AHttpRequestConfiguration mAHttpConfiguration = null;
	
	private AHttpConfiguration(final Builder builder){
		mAHttpConfiguration = builder.mAHttpConfiguration;
		mLoggingEnabled = builder.mLoggingEnabled;
		mSocketTimeout = builder.mSocketTimeout;
		mMaxConnectionsPerRoute = builder.mMaxConnectionsPerRoute;
		mMaxTotalConnections = builder.mMaxTotalConnections;
		mSoSocketTimeout = builder.mSoSocketTimeout;
		mConnectionTimeout = builder.mConnectionTimeout;
		mSocketBufferSize = builder.mSocketBufferSize;
		mThreadPoolSize = builder.mThreadPoolSize;
	}
	
	
	
	public static class Builder{
		
		private static final int DEFAULT_MAX_CONNECTIONS = 10;
		
		private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
		
		private static final int DEFAULT_HTTP_CONNECT_TIMEOUT = 5 * 1000;
		
		private static final int DEFAULT_HTTP_READ_TIMEOUT = 20 * 1000;
		
		private static final int DEFAULT_THREAD_POOL_SIZE = 0;
		
		
		private boolean mLoggingEnabled = false;
		
		private int mSocketTimeout = DEFAULT_HTTP_CONNECT_TIMEOUT;
		
		private int mMaxConnectionsPerRoute = DEFAULT_MAX_CONNECTIONS;
		
		private int mMaxTotalConnections = DEFAULT_MAX_CONNECTIONS;
		
		private int mSocketBufferSize = DEFAULT_SOCKET_BUFFER_SIZE;
		
		private int mSoSocketTimeout = DEFAULT_HTTP_READ_TIMEOUT;
		
		private int mConnectionTimeout = DEFAULT_HTTP_CONNECT_TIMEOUT;
		
		private int mThreadPoolSize = DEFAULT_THREAD_POOL_SIZE;
		
		private AHttpRequestConfiguration mAHttpConfiguration = null;
		
		public Builder defaultAHttpRequestConfiguration(AHttpRequestConfiguration defaultAHttpRequestConfiguration){
			this.mAHttpConfiguration = defaultAHttpRequestConfiguration;
			return this;
		}
		public Builder enLog(){
			mLoggingEnabled = true;
			return this;
		}
		
		public AHttpConfiguration build(){
			return new AHttpConfiguration(this);
		}
		
		
	}
}
