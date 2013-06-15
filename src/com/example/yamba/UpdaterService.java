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
<<<<<<< HEAD
	
	static final String TAG = "UpdaterService";
	Twitter twitter;
=======
	static final String TAG = "UpdaterService";
	static final int DELAY = 30;
	Twitter twitter;
	boolean running = false;
>>>>>>> f99bdcb8adbaf706f93be88d5fad11b5b5c15bf2

	@Override
	public void onCreate() {
		super.onCreate();
		twitter = new Twitter("student", "password");
		twitter.setAPIRootUrl("http://yamba.marakana.com/api");
<<<<<<< HEAD
		Log.d(TAG, "onCreated");
=======
		Log.d(TAG, "Created");
>>>>>>> f99bdcb8adbaf706f93be88d5fad11b5b5c15bf2
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
<<<<<<< HEAD
		new Thread() {
			public void run() {
				try {
			
					List<Status> timeline = twitter.getPublicTimeline();
			
					for(Status status: timeline) {
						Log.d(TAG, String.format("%s: %s", status.user.name, status.text));
					}
				} catch (TwitterException e) {
					e.printStackTrace();
				}
			}
		}.start();
		
		
		Log.d(TAG, "onStarted");
=======
		running = true;
		new Thread() {
			public void run() {
				try {
					while (running) {
						List<Status> timeline = twitter.getPublicTimeline();
						for (Status status : timeline) {
							((YambaApp)getApplication()).statusData.insert(status);
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
>>>>>>> f99bdcb8adbaf706f93be88d5fad11b5b5c15bf2
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
<<<<<<< HEAD
		Log.d(TAG, "onDestroyed");
	}
	
=======
		running = false;
		Log.d(TAG, "Destroyed");
	}

>>>>>>> f99bdcb8adbaf706f93be88d5fad11b5b5c15bf2
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
<<<<<<< HEAD
=======

>>>>>>> f99bdcb8adbaf706f93be88d5fad11b5b5c15bf2
}
