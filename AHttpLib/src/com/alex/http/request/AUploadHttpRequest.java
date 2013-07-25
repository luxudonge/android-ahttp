package com.alex.http.request;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicNameValuePair;

import com.alex.http.core.AHttpRequest;
import com.other.PostFile;

/**
 * 
 * 上传文件
 * 
 * @author Alex.Lu
 *
 */
public class AUploadHttpRequest extends AHttpRequest {

	public static String ENDCODING = "UTF-8";
	
	private HttpPost mHttpPost;
	
	private List<BasicNameValuePair> mUrlParams;
	
	private ByteArrayEntity mEntity;
	
	private ArrayList<PostFile> mArrayList;
	
	public AUploadHttpRequest(AHandleable handle, AResponseHandler responseHandler,String url) {
		super(handle, responseHandler);
		mUrlParams = new LinkedList<BasicNameValuePair>();
		mArrayList = new ArrayList<PostFile>();
		mURL = url;
	}
	
	public void setUploadFilePath(PostFile fileName){
		mArrayList.clear();
		mArrayList.add(fileName);
	}
	
	public void setUploadFilesPath(String[] fileNames){
		
	}
	
	
	@Override
	public void buildRequest() {
		// TODO Auto-generated method stub
		PostFile fileName = mArrayList.get(0);
		String end ="\r\n";
    	String twoHyphens ="--";
    	String boundary ="*****";
    	
		ByteArrayOutputStream  outStream  = new ByteArrayOutputStream(); 
		try {
		outStream.write((twoHyphens + boundary + end).getBytes()); 
		outStream.write(("Content-Disposition: form-data; "+
                      "name=\""+fileName.getParameterName()+"\";filename=\""+
                      fileName.getFileName() +"\""+ end).getBytes());
		outStream.write(end.getBytes());  
		
		FileInputStream fStream =new FileInputStream(fileName.getFilePath());
        int bufferSize =1024 * 4;
        byte[] buffer =new byte[bufferSize];
        int length =-1;
        /* 从文件读取数据至缓冲区 */
        int currentSize = 0;;
        int allSize = (int)fileName.getFileSize();
        while((length = fStream.read(buffer)) !=-1)
        {
        	currentSize += length;
        	/* 将资料写入DataOutputStream中 */
        	outStream.write(buffer, 0, length);
        	mAResponseHandler.sendUpdateUploadDataMessaget(mRequestId, 1, 0, currentSize, allSize);
        }
        outStream.write(end.getBytes());
        outStream.write((twoHyphens + boundary + twoHyphens + end).getBytes());
        /* close streams */
        fStream.close();
        outStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mEntity = new ByteArrayEntity(outStream.toByteArray());
		mEntity.setContentType("binary/octet-stream");
		
		String paramString = URLEncodedUtils.format(mUrlParams, ENDCODING);
		
		if (mURL.indexOf("?") == -1) {
			mURL += "?" + paramString;
        } else {
        	mURL += "&" + paramString;
        }
		
		mHttpPost = new HttpPost(mURL);
		if(mEntity != null){
			mHttpPost.setEntity(mEntity);
			mHttpPost.addHeader("Connection", "Keep-Alive");
			mHttpPost.addHeader("Charset", "UTF-8");  
			mHttpPost.addHeader("Content-Type", "multipart/form-data;boundary="+"*****");
		}else{
			//没有post数据
		}
		
	}

	@Override
	public void doRequest() throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		mHttpResponse = mClient.execute(mHttpPost);
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
