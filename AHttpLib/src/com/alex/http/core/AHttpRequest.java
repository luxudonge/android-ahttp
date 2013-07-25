package com.alex.http.core;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

import com.alex.http.request.AHandleable;
import com.alex.http.request.AHttpException;
import com.alex.http.request.AResponseHandler;

/**
 * 
 * @author Alex.Lu
 * 
 */
public abstract class AHttpRequest implements Runnable{
	
	private static int COUNTER = 0;
	
	private int _ID = 0;
	
	private AHttpRequestConfiguration mAHttpRequestConfiguration = null;
	
	protected AResponseHandler mAResponseHandler;
	
	protected AHandleable mParse;
	
	protected DefaultHttpClient mClient;
	
	protected HttpResponse mHttpResponse;
	
	protected HttpContext mHttpContext;
	
	private AHttpEngine mHttpEngine;
	
	//请求的id
    protected int mRequestId;
    
    protected String mURL;
    
    private int mCount = 3;
    
    
    
	public AHttpRequest(
			AHandleable handle,
			AResponseHandler responseHandler
			){
		_ID = COUNTER++;
		
		mParse = handle;
		mAResponseHandler = responseHandler;
	}
	
	public AHttpRequest(
			AResponseHandler responseHandler
			){
		_ID = COUNTER++;
		
		mAResponseHandler = responseHandler;
	}
	
	protected void setAHandleable(AHandleable handle){
		mParse = handle;
	}
	
	void setDefaultHttpClient(DefaultHttpClient client){
		mClient = client;
	}
	
	void setHttpEngine(AHttpEngine engine){
		mHttpEngine = engine;
	}
	
	public void setHttpContext(HttpContext httpContext){
		mHttpContext = httpContext;
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		buildRequest();
		mAResponseHandler.sendStartRequestMessage(mRequestId);
		execute();
		mAResponseHandler.sendFinishRequestMessages(mRequestId);
		mHttpEngine.cancelRequest(this, true);
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
	 * @throws AHttpException 
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
			Object data = null;
			try {
				data = prase(httpEntity);
				
			} catch (AHttpException e) {
				mAResponseHandler.sendErrorMessage(mRequestId, code,e);
			} catch (IOException e) {
				mAResponseHandler.sendErrorMessage(mRequestId, code,e);
			}
			mAResponseHandler.sendSuccessMessage(mRequestId, code,data);
		}else{
			if(!repeatRequest()){
				mAResponseHandler.sendErrorMessage(mRequestId, code,new HttpResponseException(code, statusLine.getReasonPhrase()));
			}
		}
		
	}

	/**
	 * 是否发生重复请求
	 * @return
	 */
	private boolean repeatRequest(){
		if(mCount>1){
			AHttpLog.print(this, mRequestId,"repeatRequest mCount:"+mCount);
			mAResponseHandler.sendReqeatRequestMessages(mRequestId,mCount);
			mCount--;
			execute();
			return true;
		}
		return false;
	}
	
	
	private void execute(){
		
		try {
			doRequest();
			onResponse();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			if(!repeatRequest()){
				mAResponseHandler.sendErrorMessage(mRequestId, -1,e);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if(!repeatRequest()){
				mAResponseHandler.sendErrorMessage(mRequestId, -1,e);
			}
		}
	}
	
	/**
	 * 
	 * @param entity
	 * @return
	 * @throws AHttpException
	 * @throws IOException
	 */
	private Object prase(HttpEntity entity) throws AHttpException, IOException{
		Object data;
		AHttpLog.print(this, mRequestId,"prase");
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
	
	
}
