package com.alex.http.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.alex.http.core.HttpEngine;
import com.alex.http.core.HttpRequest;
import com.alex.http.request.GetHttpRequest;
import com.alex.http.request.ResponseHandler;
import com.alex.http.request.ReponseDataListeners;
import com.alex.http.request.StateListeners;
import com.alex.rss.RssItemAdapter;
import com.alex.rss.RssHandler;
import com.alex.rss.RssItem;


public class RssTestActivity extends Activity implements StateListeners, ReponseDataListeners{
    /** Called when the activity is first created. */
	
	private String TAG = "RssTestActivity";
	private ListView rssListView;
	private RssItemAdapter mAdapter;
	private ProgressDialog progressDialog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        loadRss();
    }
    
    private void initUI(){
    	this.setContentView(R.layout.activity_rsstest);
		this.rssListView = (ListView)findViewById(R.id.rssListView);
		progressDialog = new ProgressDialog(this);
		
		rssListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(mAdapter.getData().get(position).getLink()));
				it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
				startActivity(it);
			}
		});
		mAdapter = new RssItemAdapter(this.getLayoutInflater());
		rssListView.setAdapter(mAdapter);
	};
	
	private void loadRss(){
		String url = "http://www.leiphone.com/feed";
		RssHandler handle=new RssHandler();
		ResponseHandler responseHandler = new ResponseHandler();
		responseHandler.setStateListeners(this);
		responseHandler.setReponseDataListeners(this);
		GetHttpRequest request = new GetHttpRequest(0,handle, responseHandler, url);
		HttpEngine.getInstance().doRequest( request);
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
		@SuppressWarnings("unchecked")
		ArrayList<RssItem> rssItemList =  (ArrayList<RssItem>)data;
		mAdapter.setData(rssItemList);
	}

	@Override
	public void onErrorResult(HttpRequest request,int requestId, int statusCode, Throwable e) {
		// TODO Auto-generated method stub
		
	}
}