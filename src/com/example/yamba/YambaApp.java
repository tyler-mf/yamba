package com.example.yamba;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
import winterwell.jtwitter.TwitterException;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

public class YambaApp extends Application implements
		OnSharedPreferenceChangeListener {
	static final String TAG = "YambaApp";
	public static final String ACTION_NEW_STATUS = "com.example.yamba.NEW_STATUS";
	private Twitter twitter;
	SharedPreferences prefs;
	StatusData statusData;

	@Override
	public void onCreate() {

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		super.onCreate();

		statusData = new StatusData(this);

		Log.d(TAG, "onCreated");
	}

	public Twitter getTwitter() {
		if (twitter == null) {
			String username = prefs.getString("username", "");
			String password = prefs.getString("password", "");
			String server = prefs.getString("server", "");

			twitter = new Twitter(username, password);
			twitter.setAPIRootUrl(server);
		}
		return twitter;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String arg1) {
		this.prefs = prefs;
		twitter = null;
	}

	long lastTimestampSeen = -1;
	public int pullAndInsert() {
		int count = 0;
		try {
			List<Status> timeline = getTwitter().getPublicTimeline();

			for (Status status : timeline) {

				// Insert status into DB
				statusData.insert(status);
				if (status.createdAt.getTime() > lastTimestampSeen) {
					count++;
					lastTimestampSeen = status.createdAt.getTime();
				}

				Log.d(TAG, String.format("%s: %s", status.user.name, status.text));
			}
		} catch (TwitterException e) {
			Log.d(TAG, "Failed to pull timeline", e);
		}
		
		if (count > 0) {
			sendBroadcast( new Intent(ACTION_NEW_STATUS).putExtra("count", count) );
		}
		
		return count;
	};
	

}
