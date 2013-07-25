package com.alex.http.app;

import com.alex.http.core.AHttpConfiguration;
import com.alex.http.core.AHttpEngine;
import android.app.Application;

/**
 * 
 * @author Alex.Lu
 *
 */
public class AHttpApp extends Application{

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		AHttpConfiguration httpConfiguration = new AHttpConfiguration.Builder()
		.enLog()
		.build();
		
		AHttpEngine.getInstance().init(httpConfiguration);
		
	}
}
