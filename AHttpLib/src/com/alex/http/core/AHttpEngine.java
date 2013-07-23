package com.alex.http.core;

import java.lang.ref.WeakReference;
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
import com.alex.http.request.AHttpRequest;
import com.alex.http.request.APostHttpRequest;
import com.alex.http.request.AResourceHttpRequest;

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

	private final Map<Context, List<WeakReference<Future<?>>>> mRequestMap;
	
	private AHttpEngine(){
		mRequestMap = new WeakHashMap<Context, List<WeakReference<Future<?>>>>();
	}
	
	public static AHttpEngine getInstance(){
		if(mInstance==null){
			mInstance = new AHttpEngine();
		}
		return mInstance;
	}
	
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

	public void resourceRequest(Context context,AResourceHttpRequest request){
		sendRequest(context, request);
	}
	
	public void getRequest(Context context,AGetHttpRequest request){
		sendRequest(context, request);
	}
	
	public void getRequest(Context context,AGetHttpRequest request, HttpContext httpContext){
		request.setHttpContext(httpContext);
		sendRequest(context, request);
	}
	
	public void postRequest(Context context,APostHttpRequest request){
		sendRequest(context, request);
	}
	
	public void postRequest(Context context,APostHttpRequest request, HttpContext httpContext){
		request.setHttpContext(httpContext);
		sendRequest(context, request);
	}
	
	
	private void sendRequest(Context context,AHttpRequest request){
		request.setDefaultHttpClient(mDefaultHttpClient);
		Future<?> future = mThreadPool.submit(request);
		if(context != null){
			 List<WeakReference<Future<?>>> requestList = mRequestMap.get(context);
	            if(requestList == null) {
	                requestList = new LinkedList<WeakReference<Future<?>>>();
	                mRequestMap.put(context, requestList);
	            }
	            requestList.add(new WeakReference<Future<?>>(future));
		}
	}
	
	public void cancelRequest(Context context,boolean mayInterruptIfRunning){
		List<WeakReference<Future<?>>> requestList = mRequestMap.get(context);
		if(requestList != null){
			for(WeakReference<Future<?>> requestRef : requestList) {
                Future<?> request = requestRef.get();
                if(request != null) {
                    request.cancel(mayInterruptIfRunning);
                }
            }
		}
		mRequestMap.remove(context);
	}
	
	
	
}
