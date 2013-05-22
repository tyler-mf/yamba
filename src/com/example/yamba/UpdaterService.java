package com.example.yamba;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
import winterwell.jtwitter.TwitterException;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdaterService extends Service {
	static final String TAG = "UpdaterService";

	@Override
	public void onCreate() {
		super.onCreate();
		Twitter twitter = new Twitter("student", "password");
		twitter.setAPIRootUrl("http://yamba.marakana.com/api");
		Log.d(TAG, "Created");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		new Thread() {
			public void run() {
				try {
					Twitter twitter = new Twitter("student", "password");
					twitter.setAPIRootUrl("http://yamba.marakana.com/api");
					List<Status> timeline = twitter.getPublicTimeline();
					for (Status status : timeline) {
						Log.d(TAG, String.format("%s: %s", status.user.name,
								status.text));
					}
				} catch (TwitterException e) {
					e.printStackTrace();
				}
			}
		}.start();
		
		Log.d(TAG, "Started");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "Destroyed");
	}


	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
