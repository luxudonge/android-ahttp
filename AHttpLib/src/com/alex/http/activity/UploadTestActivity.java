package com.alex.http.activity;

import com.alex.http.core.AHttpEngine;
import com.alex.http.request.AResponseHandler;
import com.alex.http.request.AStringHandleable;
import com.alex.http.request.AUploadHttpRequest;
import com.alex.http.request.ReponseDataListeners;
import com.alex.http.request.StateListeners;
import com.other.PostFile;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * 上传文件测试
 * 
 * @author Alex.Lu
 *
 */
public class UploadTestActivity extends Activity implements OnClickListener, StateListeners, ReponseDataListeners{
    /** Called when the activity is first created. */
	
	private TextView text;
	private Button but;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadtest);
        but = (Button)findViewById(R.id.upload);
        but.setOnClickListener(this);
    }

    
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		System.gc();
		super.onDestroy();
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.upload:
			String url = "http://www.rongshuxia.com/bookapi/postheadpic/data-json-fid-5-auth-72e327a0-uid-101072657.html";
			AResponseHandler responseHandler = new AResponseHandler();
			responseHandler.setReponseDataListeners(this);
			responseHandler.setStateListeners(this);
			AUploadHttpRequest request = new AUploadHttpRequest(new AStringHandleable(), responseHandler,url);
			PostFile file = new PostFile("/sdcard/com.sd.activity/books/P85.png", "headpic");
			request.setUploadFilePath(file);
			AHttpEngine.getInstance().uploadRequest(this, request);
			break;

		default:
			break;
		}
	}


	@Override
	public void onSuccessResult(int requestId, int statusCode, Object data) {
		// TODO Auto-generated method stub
		Log.d("onSuccessResult", String.valueOf(data));
	}


	@Override
	public void onErrorResult(int requestId, int statusCode, Throwable e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStartRequest(int requestId) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onFinishRequest(int requestId) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onRepeatRequest(int requestId, int count) {
		// TODO Auto-generated method stub
		
	}


}