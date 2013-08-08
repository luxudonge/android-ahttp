package com.alex.http.request;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.alex.http.core.HttpLog;
import com.alex.http.core.HttpRequest;

/**
 * 
 * get请求
 * 
 * @author Alex.Lu
 *
 */
public class GetHttpRequest extends HttpRequest {

	private HttpGet mHttpGet;
	
	public static String ENDCODING = "UTF-8";
	
	private List<BasicNameValuePair> mUrlParams;
	
	public GetHttpRequest(
			int requestId,
			Handleable handle, 
			ResponseHandler responseHandler,
			String url) {
		super(requestId,handle, responseHandler);
		// TODO Auto-generated constructor stub
		mUrlParams = new LinkedList<BasicNameValuePair>() ;
		mURL = url;
	}
	

	@Override
	public void buildRequest(){
		// TODO Auto-generated method stub
		String paramString = URLEncodedUtils.format(mUrlParams, ENDCODING);
		
		if (mURL.indexOf("?") == -1) {
			mURL += "?" + paramString;
        } else {
        	mURL += "&" + paramString;
        }
		
		mHttpGet = new HttpGet(mURL);
		HttpLog.print(this, mRequestId,"doGetRequest:"+mURL);
	}
	
	@Override
	public void doRequest() throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		HttpLog.print(this, mRequestId,"doGetRequest:"+mURL);
		mHttpResponse = mClient.execute(mHttpGet, mHttpContext);
	}
	
	public void putUrlParam(String key,int value){
		putUrlParam(key, String.valueOf(value));
	}
	
	public void putUrlParam(String key,long value){
		putUrlParam(key, String.valueOf(value));
	}
	
	public void putUrlParam(String key,String value){
		if(key!=null && value != null){
			mUrlParams.add(new BasicNameValuePair(key, value));
		}
	}
}
