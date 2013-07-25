package com.alex.http.request;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;

import com.alex.http.core.AHttpRequest;
import com.other.AGetRequestParams;

/**
 * 
 * post请求
 * 
 * @author Alex.Lu
 *
 */
public class APostHttpRequest extends AHttpRequest {

	private HttpPost mHttpPost;
	
	public static String ENDCODING = "UTF-8";
	
	private List<BasicNameValuePair> mUrlParams;
	
	private List<BasicNameValuePair> mPostConentParams;
	
	private HttpEntity mEntity;
	
	public APostHttpRequest(
			AHandleable handle, 
			AResponseHandler responseHandler,
			String url) {
		super(handle, responseHandler);
		// TODO Auto-generated constructor stub
		mUrlParams = new LinkedList<BasicNameValuePair>() ;
		mPostConentParams = new LinkedList<BasicNameValuePair>();
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
		
		mHttpPost = new HttpPost(mURL);
		mHttpPost.setEntity(mEntity);
	}
	
	@Override
	public void doRequest() throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		
		mHttpResponse = mClient.execute(mHttpPost, mHttpContext);
	}
	
	public void putPostBtys(byte[] postData){
		mEntity = new ByteArrayEntity(postData);
	}
	
	public void putPostContentParam(String key,int value){
		putPostContentParam(key, String.valueOf(value));
	}
	
	public void putPostContentParam(String key,long value){
		putPostContentParam(key, String.valueOf(value));
	}
	
	public void putPostContentParam(String key,String value){
		if(mEntity == null){
			try {
				mEntity = new UrlEncodedFormEntity(mPostConentParams,ENDCODING);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(key!=null && value != null){
			mPostConentParams.add(new BasicNameValuePair(key, value));
		}
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
