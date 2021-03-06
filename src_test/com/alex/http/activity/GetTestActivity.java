package com.alex.http.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alex.http.core.HttpEngine;
import com.alex.http.core.HttpRequest;
import com.alex.http.request.GetHttpRequest;
import com.alex.http.request.ResponseHandler;
import com.alex.http.request.StringHandleable;
import com.alex.http.request.ReponseDataListeners;
import com.alex.http.request.StateListeners;

/**
 * 
 * 测试get的请求方式
 * 
 * @author Alex.Lu
 *
 */
public class GetTestActivity extends Activity implements OnClickListener, StateListeners, ReponseDataListeners, OnCancelListener {

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
		progressDialog.setOnCancelListener(this);

		mResponseContentTV = (TextView) findViewById(R.id.response_content);

		mUrlET = (EditText) findViewById(R.id.url);
		mUrlET.setText("http://wap.cmread.com/iread/wml/p/index.jsp;jsessionid=E0B4DC2619658348910D186716F5C939?pg=106763&cm=M5170021&t1=16024&lab=25884");
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
				mResponseContentTV.setText("");
				StringHandleable handle=new StringHandleable();
				ResponseHandler responseHandler = new ResponseHandler();
				responseHandler.setStateListeners(this);
				responseHandler.setReponseDataListeners(this);
				request = new GetHttpRequest(0,handle, responseHandler, url);
				HttpEngine.getInstance().doRequest( request);
			}
			break;

		default:
			break;
		}
	}
	GetHttpRequest request;

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
		mResponseContentTV.setText(String.valueOf(data));
	}

	@Override
	public void onErrorResult(HttpRequest request,int requestId, int statusCode, Throwable e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancel(DialogInterface arg0) {
		// TODO Auto-generated method stub
		HttpEngine.getInstance().cancelRequest(request, true);
	}


}