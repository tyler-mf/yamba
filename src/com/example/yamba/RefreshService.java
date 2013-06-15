package com.example.yamba;

import java.util.List;

import winterwell.jtwitter.Twitter.Status;
import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
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
		StatusData statusData = ((YambaApp) getApplication()).statusData; 
		try {
			List<Status> timeline = twitter.getPublicTimeline();
			for (Status status : timeline) {
				
				// Insert status into DB
				statusData.insert(status);
				
				Log.d(TAG, String.format("%s: %s",
						status.user.name, status.text));
			}
		} catch (TwitterException e) {
			Log.e(TAG, "Failed to connect twitter API", e);
		}
		Log.d(TAG, "onHandleIntent");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "Destroyed");
	}
}
