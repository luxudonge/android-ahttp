package com.alex.http.request;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.alex.http.core.AHttpLog;

/**
 * 
 * get请求
 * 
 * @author Alex.Lu
 *
 */
public class AResourceHttpRequest extends AHttpRequest implements AHandleable{

	private HttpGet mHttpGet;
	
	public static String ENDCODING = "UTF-8";
	
	private List<BasicNameValuePair> mUrlParams;
	
	private final int updateIntervalmillis = 100;
	private String mTempPath;
	private String mTargetPath;
	private byte buffer[] = new byte[8 * 1024];
	private int len = -1;
	private long mHasDownload = 0;
	private long size;
	private String mName;
	private boolean isStop = false;
	
	
	public AResourceHttpRequest(
			String name,
			AResponseHandler responseHandler,
			String url) {
		super(responseHandler);
		setAHandleable(this);
		mUrlParams = new LinkedList<BasicNameValuePair>() ;
		mURL = url;
		mName = name;
	}

	/**
	 * 设置放数据的目录
	 * @param dir
	 */
	public void setResouceDir(String dir){
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(dir);
		sbuf.append(mName);
		mTargetPath = sbuf.toString();
		sbuf.append(".temp");
		mTempPath = sbuf.toString();
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
		
		File tempFile = new File(mTempPath);
		if(tempFile.exists()){
			mHasDownload = tempFile.length();
		}else{
			mHasDownload = 0;
		}
		
		mHttpGet = new HttpGet(mURL);
		StringBuffer sbuf = new StringBuffer();
		sbuf.append("bytes=");
		sbuf.append(mHasDownload);
		sbuf.append("-");
		mHttpGet.addHeader("RANGE", sbuf.toString());
		AHttpLog.print(this, mRequestId,"doResourceRequest:"+mURL);
	}
	
	@Override
	public void doRequest() throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		AHttpLog.print(this, mRequestId,"doResourceRequest:"+mURL);
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

	

	@Override
	public Object handle(int requestId, HttpEntity paramInputStream)
			throws AHttpException, IOException {
		// TODO Auto-generated method stub
		download(paramInputStream);
		return null;
	}
	
	
	private void download(HttpEntity paramInputStream){
		File downloadFile = new File(mTargetPath);
		File tempFile = new File(mTempPath);
		if (!downloadFile.exists()) {
			try {
				if(!tempFile.exists()){
					tempFile.createNewFile();
				}else{
					mHasDownload = tempFile.length();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
		
			mHasDownload = tempFile.length();
		}
		
		RandomAccessFile randomFile;
		try {
			randomFile = new RandomAccessFile(mTempPath,"rw");
			randomFile.seek(mHasDownload);
			size = paramInputStream.getContentLength();
			InputStream is = paramInputStream.getContent();
			long progressTime = System.currentTimeMillis();
			while ((len = is.read(buffer)) != -1) {
				if(isStop)
					break;
				
				randomFile.write(buffer, 0, len);
				mHasDownload += len;
				final long currentTime = System.currentTimeMillis();
				if(currentTime > progressTime){
					progressTime = currentTime + updateIntervalmillis;
					mAResponseHandler.sendUpdateDataMessage(mRequestId, mHasDownload, size);
				}
				
			}
			
			if(mHasDownload>= size){
				mAResponseHandler.sendUpdateDataMessage(mRequestId, mHasDownload, size);
				tempFile.renameTo(downloadFile);
				is.close();
			}
			is.close();
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
