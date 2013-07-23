package com.alex.http.activity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

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
public class GetsTestActivity extends Activity implements OnClickListener {

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
		mUrlET.setText("http://wap.qidian.cn/wap2/top/index.do?&fromindex=lm4");
		mSendBT = (Button) findViewById(R.id.send);
		mSendBT.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.send:
			mTime = System.currentTimeMillis();
			/*
			 * String url = mUrlET.getText().toString(); if (url != null) { mN =
			 * 10; progressDialog.show(); mResponseContentTV.setText("");
			 * AGetRequest request = new AGetRequest(REQUEST_ID, url, this,
			 * this, null); AHttpEngine.getInstance().request(request); }
			 */
			sendRequest();
			break;

		default:
			break;
		}
	}


	private void sendRequest() {
		URL url = null;
		try {
			url = new URL(
					"http://wap.qidian.cn/wap2/top/index.do?&fromindex=lm4");

			HttpURLConnection conn = null;
			conn = (HttpURLConnection) url.openConnection();

			conn.setConnectTimeout(6 * 1000);
			conn.setDoOutput(true);// 允许输出
			conn.setUseCaches(false);// 不使用Cache
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			DataOutputStream outStream = null;
			outStream = new DataOutputStream(conn.getOutputStream());
			outStream.flush();
			if (conn.getResponseCode() == 200) {
				Log.e("", "成功");
				if (mN > 0) {
					mN--;
					conn.disconnect();
					sendRequest();
				} else {
					long t = (System.currentTimeMillis() - mTime) / 1000;
					Log.e("", "t:" + t);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}