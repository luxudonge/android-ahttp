package com.alex.http.app;

import com.alex.http.core.HttpConfiguration;
import com.alex.http.core.HttpEngine;
import android.app.Application;

/**
 * 
 * 
 * @author Alex.Lu
 *
 */
public class AHttpApp extends Application{

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		HttpConfiguration httpConfiguration = new HttpConfiguration.Builder()
		.enLog()
		.build();
		
		HttpEngine.getInstance().init(httpConfiguration);
		
	}
}
