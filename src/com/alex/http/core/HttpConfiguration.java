package com.alex.http.core;


/**
 * 
 * httpClient 配置文件
 * 
 * @author Alex.Lu
 *
 */
public final class HttpConfiguration {

	static boolean mLoggingEnabled;
	final int mSocketTimeout;
	final int mMaxConnectionsPerRoute;
	final int mMaxTotalConnections;
	final int mSoSocketTimeout;
	final int mConnectionTimeout;
	final int mSocketBufferSize;
	final int mThreadPoolSize;
	
	HttpRequestConfiguration mAHttpConfiguration = null;
	
	private HttpConfiguration(final Builder builder){
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
		
		private HttpRequestConfiguration mAHttpConfiguration = null;
		
		public Builder defaultAHttpRequestConfiguration(HttpRequestConfiguration defaultAHttpRequestConfiguration){
			this.mAHttpConfiguration = defaultAHttpRequestConfiguration;
			return this;
		}
		
		/**
		 * 开启日志打印
		 * @return
		 */
		public Builder enLog(){
			mLoggingEnabled = true;
			return this;
		}
		
		/**
		 * 
		 * @param value
		 * @return
		 */
		public Builder setMaxConectionsPerRounte(int value){
			mMaxConnectionsPerRoute = value;
			return this;
		}
		
		/**
		 * 设置socket缓存大小
		 * @param value
		 * @return
		 */
		public Builder setSocketBufferSize(int value){
			mMaxTotalConnections = value;
			return this;
		}
		
		/**
		 * 等待数据超时
		 * @param value
		 * @return
		 */
		public Builder setSoSocketTimeout(int value){
			mSocketBufferSize = value;
			return this;
		}
		
		/**
		 * 连接超时
		 * @param value
		 * @return
		 */
		public Builder setconnectionTime(int value){
			mSoSocketTimeout = value;
			return this;
		}
		
		/**
		 * 设置最大的线程池  当为-1时
		 * @param value
		 * @return
		 */
		public Builder setThreadPoolSize(int value){
			mThreadPoolSize = value;
			return this;
		}
		
		/**
		 * 
		 * @return
		 */
		public HttpConfiguration build(){
			return new HttpConfiguration(this);
		}
		
		
	}
}
