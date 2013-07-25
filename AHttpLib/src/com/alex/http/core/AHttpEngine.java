package com.alex.http.core;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.http.HttpVersion;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;

import android.content.Context;

import com.alex.http.request.AGetHttpRequest;
import com.alex.http.request.APostHttpRequest;
import com.alex.http.request.AResourceHttpRequest;
import com.alex.http.request.AUploadHttpRequest;

/**
 * 
 * http请求引擎
 * 
 * @author Alex.Lu
 *
 */
public class AHttpEngine {

	private static AHttpEngine mInstance;
	
	private ThreadPoolExecutor mThreadPool;
	
	private DefaultHttpClient mDefaultHttpClient;

	private final Map<Integer, WeakReference<Future<?>>> mRequestMap;
	
	private final Map<Integer, AHttpRequest> mRequest;
	
	private AHttpEngine(){
		mRequestMap = new WeakHashMap<Integer, WeakReference<Future<?>>>();
		mRequest = new HashMap<Integer, AHttpRequest>();
	}
	
	public static AHttpEngine getInstance(){
		if(mInstance==null){
			mInstance = new AHttpEngine();
		}
		return mInstance;
	}
	
	/**
	 * 初始化
	 * @param httpConfiguration
	 */
	public void init(AHttpConfiguration httpConfiguration){
		BasicHttpParams httpParams = new BasicHttpParams();
		
		ConnManagerParams.setTimeout(httpParams, httpConfiguration.mSocketTimeout);
		ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(httpConfiguration.mMaxConnectionsPerRoute));
		ConnManagerParams.setMaxTotalConnections(httpParams, httpConfiguration.mMaxConnectionsPerRoute);
		
		HttpConnectionParams.setSoTimeout(httpParams, httpConfiguration.mSoSocketTimeout);
        HttpConnectionParams.setConnectionTimeout(httpParams, httpConfiguration.mConnectionTimeout);
        HttpConnectionParams.setTcpNoDelay(httpParams, true);
        HttpConnectionParams.setSocketBufferSize(httpParams, httpConfiguration.mSocketBufferSize);

        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
        
		mDefaultHttpClient = new DefaultHttpClient(cm,httpParams);
		if(httpConfiguration.mThreadPoolSize >0){
			mThreadPool  = (ThreadPoolExecutor)Executors.newFixedThreadPool(httpConfiguration.mThreadPoolSize);
		}else{
			mThreadPool  = (ThreadPoolExecutor)Executors.newCachedThreadPool();
		}
	}

	
	public void doRequest(AHttpRequest request){
		sendRequest(request);
	}
	
	
	public void request(AHttpRequest request, HttpContext httpContext){
		request.setHttpContext(httpContext);
		sendRequest(request);
	}
	
	
	private void sendRequest(AHttpRequest request){
		if(mRequest.containsKey(request.getRequestId())){
			return ;
		}else{
			mRequest.put(request.getID(), request);
			request.setDefaultHttpClient(mDefaultHttpClient);
			request.setHttpEngine(this);
			Future<?> future = mThreadPool.submit(request);
			mRequestMap.put(request.getID(), new WeakReference<Future<?>>(future));
		}
	}
	
	
	public void cancelRequest(AHttpRequest request,boolean mayInterruptIfRunning){
		WeakReference<Future<?>> requestRef = mRequestMap.get(request.getID());
        Future<?> future = requestRef.get();
        if(request != null) {
        	future.cancel(mayInterruptIfRunning);
        }
		mRequest.remove(request.getRequestId());
		mRequestMap.remove(request.getID());
	}
	
}
