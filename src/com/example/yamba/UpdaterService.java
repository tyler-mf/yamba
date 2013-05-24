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
	static final int DELAY = 30;
	Twitter twitter;
	boolean running = false;

	@Override
	public void onCreate() {
		super.onCreate();
		twitter = new Twitter("student", "password");
		twitter.setAPIRootUrl("http://yamba.marakana.com/api");
		Log.d(TAG, "Created");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		running = true;
		// final int delay = ((YambaApp)getApplication()).prefs.getInt("delay", DELAY);
		new Thread() {
			public void run() {
				try {
					while (running) {
						List<Status> timeline = twitter.getPublicTimeline();
						for (Status status : timeline) {
							Log.d(TAG, String.format("%s: %s",
									status.user.name, status.text));
						}
						int delay = Integer.parseInt( 
								((YambaApp)getApplication()).prefs.getString("delay", "30") );
						Thread.sleep(delay*1000);
					}
				} catch (TwitterException e) {
					Log.e(TAG, "faild because internet connection");
				} catch (InterruptedException e) {
					Log.d(TAG, "Updater interrupted");
				}
			}
		}.start();

		Log.d(TAG, "Started");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		running = false;
		Log.d(TAG, "Destroyed");
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
