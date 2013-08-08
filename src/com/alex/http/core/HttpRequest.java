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

import com.alex.http.exception.HttpException;
import com.alex.http.request.Handleable;
import com.alex.http.request.ResponseHandler;

/**
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
    
    
    
	public HttpRequest(
			int requestId,
			Handleable handle,
			ResponseHandler responseHandler
			){
		_ID = COUNTER++;
		mRequestId = requestId;
		mParse = handle;
		mAResponseHandler = responseHandler;
	}
	
	public HttpRequest(
			int requestId,
			ResponseHandler responseHandler
			){
		_ID = COUNTER++;
		mRequestId = requestId;
		mAResponseHandler = responseHandler;
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
			Object data = null;
			try {
				data = prase(httpEntity);
				
			} catch (HttpException e) {
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
			HttpLog.print(this, mRequestId,"repeatRequest mCount:"+mCount);
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
	
	
}
