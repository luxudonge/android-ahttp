package com.alex.http.core;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.protocol.HttpContext;

import android.os.Bundle;

import com.alex.http.exception.HttpException;
import com.alex.http.request.Handleable;
import com.alex.http.request.ResponseHandler;

/**
 * 
 * 核心请求类
 * 
 * @author Alex.Lu
 * 
 */
public abstract class HttpRequest implements Runnable{
	
	private static int COUNTER = 0;
	
	private int _ID = 0;
	
	private HttpRequestConfiguration mAHttpRequestConfiguration = null;
	
	protected ResponseHandler mAResponseHandler;
	
	protected Handleable mParse;
	
	protected HttpClient mClient;
	
	protected HttpResponse mHttpResponse;
	
	protected HttpContext mHttpContext;
	
	private HttpEngine mHttpEngine;
	
	//请求的id
    protected int mRequestId;
    
    protected String mURL;
    
    private int mCount = 3;
    
    private Bundle mData;
    
    private boolean mIsCancel;
    
	public HttpRequest(
			int requestId,
			Handleable handle,
			ResponseHandler responseHandler
			){
		_ID = COUNTER++;
		mRequestId = requestId;
		mParse = handle;
		mAResponseHandler = responseHandler;
		mIsCancel = false;
	}
	
	public HttpRequest(
			int requestId,
			ResponseHandler responseHandler
			){
		_ID = COUNTER++;
		mRequestId = requestId;
		mAResponseHandler = responseHandler;
		mIsCancel = false;
	}
	
	protected void setAHandleable(Handleable handle){
		mParse = handle;
	}
	
	void setDefaultHttpClient(HttpClient client){
		mClient = client;
	}
	
	void setHttpEngine(HttpEngine engine){
		mHttpEngine = engine;
	}
	
	public void setHttpContext(HttpContext httpContext){
		mHttpContext = httpContext;
	}
	
	public void setBundle(Bundle data){
		mData = new Bundle();
		mData.putAll(data);
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		buildRequest();
		if(mIsCancel){
			//取消
			return;
		}else{
			mAResponseHandler.sendStartRequestMessage(this,mRequestId);
			execute();
			mAResponseHandler.sendFinishRequestMessages(this,mRequestId);
			mHttpEngine.cancelRequest(this, true);
		}
	}
	
	/**
	 * 创建请求
	 */
	public abstract void buildRequest();
	
	/**
	 * 开始请求
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public abstract void doRequest() throws ClientProtocolException, IOException ;
	
	/**
	 * 响应请求
	 * @throws HttpException 
	 * @throws IOException 
	 * @throws ParseException 
	 */
	private void onResponse(){
		HttpEntity httpEntity = null;
		httpEntity = mHttpResponse.getEntity();
		StatusLine statusLine = mHttpResponse.getStatusLine();
		int code = statusLine.getStatusCode();
		//206为断点续传做准备
		if(code < 300){
			
			if(mIsCancel){
				//取消
				return;
			}else{
				//数据解析
				Object data = null;
				try {
					data = prase(httpEntity);
				} catch (HttpException e) {
					mAResponseHandler.sendErrorMessage(this, mRequestId, code, e);
				} catch (IOException e) {
					mAResponseHandler.sendErrorMessage(this, mRequestId, code, e);
				}
				mAResponseHandler.sendSuccessMessage(this,mRequestId, code,data);
			}
		}else{
			if(!repeatRequest()){
				mAResponseHandler.sendErrorMessage(this,mRequestId, code,new HttpResponseException(code, statusLine.getReasonPhrase()));
			}
		}
		
	}

	/**
	 * 是否发生重复请求
	 * @return
	 */
	private boolean repeatRequest(){
		if(mIsCancel){
			//取消
			return true;
		}else{
			if(mCount>1){
				HttpLog.print(this, mRequestId,"repeatRequest mCount:"+mCount);
				mAResponseHandler.sendReqeatRequestMessages(this,mRequestId,mCount);
				mCount--;
				execute();
				return true;
			}else{
				return false;
			}
		}
	}
	
	
	private void execute(){
		
		try {
			doRequest();
			if(mIsCancel){
				//取消
				return;
			}else{
				onResponse();
			}
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			if(!repeatRequest()){
				mAResponseHandler.sendErrorMessage(this,mRequestId, -1,e);
			}
		}
	}
	
	/**
	 * 
	 * @param entity
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	private Object prase(HttpEntity entity) throws HttpException, IOException{
		Object data;
		HttpLog.print(this, mRequestId,"prase");
		data = mParse.handle(mRequestId, entity);
		return data;
	}
	
	/**
	 * 获取请求唯一标识
	 * @return
	 */
	public int getRequestId(){
		return mRequestId;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getID(){
		return _ID;
	}
	
	/**
	 * 打断请求
	 */
	public void cancel(){
		mIsCancel = true;
		mHttpEngine.cancelRequest(this, true);
	}
	
}
