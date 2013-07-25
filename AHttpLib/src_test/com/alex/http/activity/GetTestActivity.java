package com.alex.http.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alex.http.core.AHttpEngine;
import com.alex.http.request.AGetHttpRequest;
import com.alex.http.request.AResponseHandler;
import com.alex.http.request.AStringHandleable;
import com.alex.http.request.ReponseDataListeners;
import com.alex.http.request.StateListeners;

/**
 * 
 * 测试get的请求方式
 * 
 * @author Alex.Lu
 *
 */
public class GetTestActivity extends Activity implements OnClickListener, StateListeners, ReponseDataListeners {

	private String TAG = "GetTestActivity";

	private long REQUEST_ID = 0x0001;

	private ProgressDialog progressDialog;

	private TextView mResponseContentTV;

	private EditText mUrlET;

	private Button mSendBT;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gettest);

		progressDialog = new ProgressDialog(this);

		mResponseContentTV = (TextView) findViewById(R.id.response_content);

		mUrlET = (EditText) findViewById(R.id.url);

		mSendBT = (Button) findViewById(R.id.send);
		mSendBT.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.send:
			String url = mUrlET.getText().toString();
			if(url !=null){
				progressDialog.show();
				mResponseContentTV.setText("");
				AStringHandleable handle=new AStringHandleable();
				AResponseHandler responseHandler = new AResponseHandler();
				responseHandler.setStateListeners(this);
				responseHandler.setReponseDataListeners(this);
				AGetHttpRequest request = new AGetHttpRequest(handle, responseHandler, url);
				AHttpEngine.getInstance().getRequest(this, request);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onStartRequest(int requestId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinishRequest(int requestId) {
		// TODO Auto-generated method stub
		progressDialog.dismiss();
	}

	@Override
	public void onRepeatRequest(int requestId, int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSuccessResult(int requestId, int statusCode, Object data) {
		// TODO Auto-generated method stub
		mResponseContentTV.setText(String.valueOf(data));
	}

	@Override
	public void onErrorResult(int requestId, int statusCode, Throwable e) {
		// TODO Auto-generated method stub
		
	}


}