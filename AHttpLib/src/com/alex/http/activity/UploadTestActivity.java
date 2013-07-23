package com.alex.http.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class UploadTestActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	
	private TextView text;
	private Button but;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
		finish();
	}


}