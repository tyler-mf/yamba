package com.example.yamba;

import winterwell.jtwitter.Twitter;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class RefreshService extends IntentService {
	static final String TAG = "Refresh service";
	Twitter twitter;

	public RefreshService() {
		super(TAG);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		twitter = new Twitter("student", "password");
		twitter.setAPIRootUrl("http://yamba.marakana.com/api");
		Log.d(TAG, "Created");
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		((YambaApp) getApplication()).pullAndInsert();
		Log.d(TAG, "onHandleIntent");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "Destroyed");
	}
}
