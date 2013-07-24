package com.alex.http.activity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.alex.http.core.AHttpEngine;
import com.alex.http.request.AGetHttpRequest;
import com.alex.http.request.AResponseHandler;
import com.alex.http.request.AStringHandleable;
import com.alex.http.request.ReponseDataListeners;
import com.alex.http.request.StateListeners;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * 测试get的请求方式
 * 
 * @author Alex.Lu
 * 
 */
public class GetsTestActivity extends Activity implements OnClickListener,
		ReponseDataListeners, StateListeners {

	private String TAG = "GetTestActivity";

	private long REQUEST_ID = 0x0001;

	private ProgressDialog progressDialog;

	private TextView mResponseContentTV;

	private EditText mUrlET;

	private Button mSendBT;

	private int mN = 10;

	private long mTime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_getstest);

		progressDialog = new ProgressDialog(this);

		mResponseContentTV = (TextView) findViewById(R.id.response_content);

		mUrlET = (EditText) findViewById(R.id.url);
		mUrlET.setText("http://192.1.1.90");
		mSendBT = (Button) findViewById(R.id.send);
		mSendBT.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.send:
			mTime = System.currentTimeMillis();
			String url = mUrlET.getText().toString();
			if (url != null) {
				progressDialog.show();
				mResponseContentTV.setText("");
				AStringHandleable handle = new AStringHandleable();
				AResponseHandler responseHandler = new AResponseHandler();
				responseHandler.setStateListeners(this);
				responseHandler.setReponseDataListeners(this);
				AGetHttpRequest request = new AGetHttpRequest(handle,responseHandler, url);
				AHttpEngine.getInstance().getRequest(this, request);
				break;

			}
		}

	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		AHttpEngine.getInstance().cancelRequest(this, true);
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
		Log.e("onRepeatRequest", "count:"+count);
	}

	@Override
	public void onSuccessResult(int requestId, int statusCode, Object data) {
		// TODO Auto-generated method stub
		Log.e("onSuccessResult", "data:"+String.valueOf(data));
	}

	@Override
	public void onErrorResult(int requestId, int statusCode, Throwable e) {
		// TODO Auto-generated method stub
		Log.e("onSuccessResult", "error:"+e.getMessage());
	}
}