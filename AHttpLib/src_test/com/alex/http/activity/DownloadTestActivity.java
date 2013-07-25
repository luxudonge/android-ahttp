package com.alex.http.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.alex.http.core.AHttpEngine;
import com.alex.http.request.AResourceHttpRequest;
import com.alex.http.request.AResponseHandler;
import com.alex.http.request.ReponseUpdateDataListeners;
import com.alex.http.request.StateListeners;

/**
 * 
 * 文件下载
 * @author Alex.Lu
 *
 */
public class DownloadTestActivity extends Activity implements StateListeners, OnClickListener, ReponseUpdateDataListeners {
    /** Called when the activity is first created. */
	
	private String TAG = "ResourceDownloadActivity";
	private TextView text;
	private Button start;
	private Button stop;
	private Button pause;
	private Button begin;
	
       
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        start = (Button)findViewById(R.id.start);
        start.setOnClickListener(this);
        pause = (Button)findViewById(R.id.pause);
        pause.setOnClickListener(this);
        stop = (Button)findViewById(R.id.stop);
        stop.setOnClickListener(this);
        begin = (Button)findViewById(R.id.begin);
        begin.setOnClickListener(this);
        text = (TextView)findViewById(R.id.text);
        text.setMovementMethod(ScrollingMovementMethod.getInstance());
        s=0;
    }

    
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.e(TAG, "onDestroy");
		super.onDestroy();
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int v_id = v.getId();
		switch (v_id) {
		case R.id.stop:
			break;
		case R.id.begin:
			AResponseHandler responseHandler = new AResponseHandler();
			responseHandler.setStateListeners(this);
			responseHandler.setReponseUpdateDataListeners(this);
			AResourceHttpRequest request = new AResourceHttpRequest("jj.jpg", responseHandler, "http://5.gaosu.com/download/pic/000/322/389b3983fd9191c8fc71bece50671612.jpg");
			
			request.setResouceDir("/sdcard/");
			AHttpEngine.getInstance().doRequest(request);
		      
			break;
		default:
			break;
		}
	}

	StringBuffer sbuf = new StringBuffer();
	long s=0;
	


	@Override
	public void onStartRequest(int requestId) {
		// TODO Auto-generated method stub
		Log.e("onStartRequest", "requestId:"+requestId);
	}


	@Override
	public void onFinishRequest(int requestId) {
		// TODO Auto-generated method stub
		Log.e("onFinishRequest", "requestId:"+requestId);
	}


	@Override
	public void onRepeatRequest(int requestId,int count) {
		// TODO Auto-generated method stub
		Log.e("onRepeatRequest", "requestId:"+requestId+"  count:"+count);
	}


	@Override
	public void updateDownloadData(int requestId,long curSize, long allSize) {
		// TODO Auto-generated method stub
		Log.e("onRepeatRequest", "requestId:"+requestId+"  curSize:"+curSize +"  allSize:"+allSize);
		float f = (float)curSize/(float)allSize;
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(curSize);
		sbuf.append("/");
		sbuf.append(allSize);
		sbuf.append("    ");
		sbuf.append(f);
		text.setText(sbuf.toString());
	}

}