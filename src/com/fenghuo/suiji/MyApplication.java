package com.fenghuo.suiji;



import java.util.Timer;
import java.util.TimerTask;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;


public class MyApplication extends Application {

	public int index;
	

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		
		 	SharedPreferences preferences = getSharedPreferences("user",
				Context.MODE_PRIVATE);

		setIndex(preferences.getInt("index", 0));
		
		
		
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
