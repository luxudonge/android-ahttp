package com.alex.http.core;

import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
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

import com.alex.http.exception.HttpException;

/**
 * 
 * http请求引擎
 * 
 * @author Alex.Lu
 *
 */
public class HttpEngine {

	private static HttpEngine mInstance;
	
	private ThreadPoolExecutor mThreadPool;
	
	private DefaultHttpClient mDefaultHttpClient;

	private final Map<Integer, WeakReference<Future<?>>> mRequestMap;
	
	private final Hashtable<Integer, HttpRequest> mRequest;
	
	private HttpEngine(){
		mRequestMap = new WeakHashMap<Integer, WeakReference<Future<?>>>();
		mRequest = new Hashtable<Integer, HttpRequest>();
	}
	
	public static HttpEngine getInstance(){
		if(mInstance==null){
			mInstance = new HttpEngine();
		}
		return mInstance;
	}
	
	/**
	 * 初始化
	 * @param httpConfiguration
	 */
	public void init(HttpConfiguration httpConfiguration){
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

	/**
	 * 
	 * @param request
	 */
	public void doRequest(HttpRequest request){
		request.setHttpEngine(this);
		request.setDefaultHttpClient(mDefaultHttpClient);
		sendRequest(request);
	}
	
	/**
	 * 
	 * @param request
	 * @param httpContext
	 */
	public void doRequest(HttpRequest request, HttpContext httpContext){
		request.setHttpContext(httpContext);
		request.setDefaultHttpClient(mDefaultHttpClient);
		request.setHttpEngine(this);
		sendRequest(request);
	}
	
	/**
	 * 
	 * @param request
	 * @param httpContext
	 * @param httpClient
	 * @throws HttpException 
	 */
	public void doRequest(HttpRequest request, HttpContext httpContext,HttpClient httpClient) throws HttpException{
		if(httpClient == null){
			throw new HttpException("httpClient is null");
		}
		request.setDefaultHttpClient(httpClient);
		request.setHttpContext(httpContext);
		request.setHttpEngine(this);
		sendRequest(request);
	}
	
	
	private void sendRequest(HttpRequest request){
		if(mRequest.containsKey(request.getRequestId())){
			return ;
		}else{
			mRequest.put(request.getID(), request);
			Future<?> future = mThreadPool.submit(request);
			mRequestMap.put(request.getID(), new WeakReference<Future<?>>(future));
		}
	}
	
	
	public void cancelRequest(HttpRequest request,boolean mayInterruptIfRunning){
		WeakReference<Future<?>> requestRef = mRequestMap.get(request.getID());
        Future<?> future = requestRef.get();
        if(request != null) {
        	future.cancel(mayInterruptIfRunning);
        }
		mRequest.remove(request.getRequestId());
		mRequestMap.remove(request.getID());
	}
	
}
