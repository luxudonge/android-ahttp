package com.alex.http.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;


public class RssTestActivity extends Activity{
    /** Called when the activity is first created. */
	
	private String TAG = "RssTestActivity";
	private ListView rssListView;
	private ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }
    
    private void initUI(){
	};
	
}