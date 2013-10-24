package com.alex.http.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alex.http.core.HttpEngine;
import com.alex.http.core.HttpRequest;
import com.alex.http.request.PostHttpRequest;
import com.alex.http.request.ResponseHandler;
import com.alex.http.request.StringHandleable;
import com.alex.http.request.ReponseDataListeners;
import com.alex.http.request.StateListeners;

/**
 * 
 * 测试post请求方式
 * 
 * @author Alex.Lu
 * 
 */
public class PostTestActivity extends Activity implements OnClickListener, StateListeners, ReponseDataListeners {
	/** Called when the activity is first created. */

	private String TAG = "PostTestActivity";

	private long REQUEST_ID = 0x0002;

	private ProgressDialog progressDialog;

	private TextView mResponseContentTV;

	private EditText mUrlET;
	
	private EditText mPostDataET;

	private Button mSendBT;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_posttest);
		mResponseContentTV = (TextView) findViewById(R.id.text);

		mUrlET = (EditText) findViewById(R.id.url);
		mUrlET.setText("http://221.181.69.63:8099/appbook/NEWUSER?userid=1970");
		mPostDataET = (EditText)findViewById(R.id.post_data);
		mPostDataET.setText("IMEI=123456789;simSerialNumber=987654321");
		mSendBT = (Button) findViewById(R.id.send);
		mSendBT.setOnClickListener(this);

		progressDialog = new ProgressDialog(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.send:
			String url = mUrlET.getText().toString();
			String postData = mPostDataET.getText().toString();
			if (url != null && postData !=null) {
				mResponseContentTV.setText("");
				
				StringHandleable handle=new StringHandleable();
				ResponseHandler responseHandler = new ResponseHandler();
				responseHandler.setStateListeners(this);
				responseHandler.setReponseDataListeners(this);
				PostHttpRequest request = new PostHttpRequest(0,handle, responseHandler, url);
				
				String[] enterys = postData.split(";");
				for(int i=0;i<enterys.length;i++){
					String[] values = enterys[i].split("=");
					Log.i(TAG, "post参数"+i+":"+values[0]+"="+values[1]);
					request.putPostContentParam(values[0], values[1]);
				}
				
				HttpEngine.getInstance().doRequest(request);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onStartRequest(HttpRequest request,int requestId) {
		// TODO Auto-generated method stub
		progressDialog.show();
	}

	@Override
	public void onFinishRequest(HttpRequest request,int requestId) {
		// TODO Auto-generated method stub
		progressDialog.dismiss();
	}

	@Override
	public void onRepeatRequest(HttpRequest request,int requestId, int count) {
		// TODO Auto-generated method stub
	}
	

	@Override
	public void onSuccessResult(HttpRequest request,int requestId, int statusCode, Object data) {
		// TODO Auto-generated method stub
		Log.e("onSuccessResult", "data:"+String.valueOf(data));
	}

	@Override
	public void onErrorResult(HttpRequest request,int requestId, int statusCode, Throwable e) {
		// TODO Auto-generated method stub
		Log.e("onErrorResult", "requestId:"+requestId+"  statusCode:"+statusCode + "e:" +e.getMessage());
	}
	

}