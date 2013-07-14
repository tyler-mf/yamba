package com.example.yamba;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class TimelineActivity extends ListActivity {
	static final String TAG = "TimelineActivity";
	static final String[] FROM = { StatusData.C_USER, StatusData.C_TEXT, StatusData.C_CREATED_AT };
	static final int[] TO = { R.id.text_user, R.id.text_text, R.id.text_created_at };
	Cursor cursor;
	SimpleCursorAdapter adapter;
	TimelineReceiver receiver;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		cursor = ((YambaApp)getApplication()).statusData.query();
		adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, FROM, TO);
		adapter.setViewBinder(viewBinder);
		setTitle(R.string.timeline);
		setListAdapter(adapter);
	}
	

	
	@Override
	protected void onResume() {
		super.onResume();
		if(receiver == null) receiver = new TimelineReceiver();
		registerReceiver(receiver, new IntentFilter(YambaApp.ACTION_NEW_STATUS));
	}



	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}



	static final ViewBinder viewBinder = new ViewBinder() {
		
		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			if (view.getId() != R.id.text_created_at) return false;
			long time = cursor.getLong(cursor.getColumnIndex(StatusData.C_CREATED_AT));
			CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(time);
			((TextView)view).setText(relativeTime);
			return true;
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		Intent intentUpdate = new Intent(this, UpdaterService.class);
		Intent intentRefresh = new Intent(this, RefreshService.class);
		Intent intentPrefs = new Intent(this, PrefsActivity.class);
		Intent intentSetStatus = new Intent(this, StatusActivity.class);

		
		switch(item.getItemId()) {
		case R.id.item_set_status:
			startActivity(intentSetStatus);
			return true;
		case R.id.item_start_service:
			startService(intentUpdate);
			return true;
		case R.id.item_stop_service:
			stopService(intentUpdate);
			return true;
		case R.id.item_refresh:
			startService(intentRefresh);
			return true;
		case R.id.item_prefs:
			startActivity(intentPrefs);
			return true;
		default:
			return false;
		}
	}
	
	class TimelineReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			cursor = ((YambaApp)getApplication()).statusData.query();
			adapter.changeCursor(cursor);
			Log.d(TAG, "TimelineReceiver onReceive changeCursor");
		}
		
	}
}
