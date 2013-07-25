package com.alex.http.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 
 * 测试主界面
 * 
 * @author Alex.Lu
 * 
 */
public class MainActivity extends Activity implements OnClickListener {

	private final static String TAG = "TestMainActivity";
	
	private Button mGetRequestBT;
	private Button mGetsRequestBT;
	private Button mPostRequestBT;
	private Button mDownloadRequestBT;
	private Button mUploadRequestBT;
	private Button mRssRequestBT;
	private Button mExitBT;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_testmain);
	
		mGetRequestBT = (Button)findViewById(R.id.get_request_btn);
        mGetRequestBT.setOnClickListener(this);
        
        mGetsRequestBT = (Button)findViewById(R.id.gets_request_btn);
        mGetsRequestBT.setOnClickListener(this);
        
        mPostRequestBT = (Button)findViewById(R.id.post_request_btn);
        mPostRequestBT.setOnClickListener(this);
        
        mDownloadRequestBT = (Button)findViewById(R.id.download_request_btn);
        mDownloadRequestBT.setOnClickListener(this);
        
        mUploadRequestBT = (Button)findViewById(R.id.upload_request_btn);
        mUploadRequestBT.setOnClickListener(this);
        
        mRssRequestBT = (Button)findViewById(R.id.rss_request_btn);
        mRssRequestBT.setOnClickListener(this);
        
        mExitBT = (Button)findViewById(R.id.exit);
        mExitBT.setOnClickListener(this);
        
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		int v_id = v.getId();
		switch (v_id) {
		case R.id.get_request_btn:
			Log.i(TAG, "进入get请求demo");
			intent.setClass(this, GetTestActivity.class);
			break;
		case R.id.gets_request_btn:
			Log.i(TAG, "进入get请求demo");
			intent.setClass(this, GetsTestActivity.class);
			break;
		case R.id.post_request_btn:
			Log.i(TAG, "进入psot请求demo");
			intent.setClass(this, PostTestActivity.class);
			break;
		case R.id.download_request_btn:
			Log.i(TAG, "进入下载demo");
			intent.setClass(this, DownloadTestActivity.class);
			break;
		case R.id.upload_request_btn:
			Log.i(TAG, "进入上传demo");
			intent.setClass(this, UploadTestActivity.class);
			break;
		case R.id.rss_request_btn:
			Log.i(TAG, "进入rss解析demo");
			intent.setClass(this, RssTestActivity.class);
			break;
		case R.id.exit:
			System.exit(0);
		default:
			break;
		}
		
		startActivity(intent);
	}

}
