package com.example.yamba;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StatusActivity extends Activity implements OnClickListener {
	static final String TAG = "StatusActivity";
	Button buttonUpdate;
	EditText editStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Debug.startMethodTracing("Yamba.trace");
		setContentView(R.layout.activity_status);
		
		buttonUpdate = (Button) findViewById(R.id.update_button);
		editStatus = (EditText) findViewById(R.id.text_status);
		
		buttonUpdate.setOnClickListener(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		Debug.stopMethodTracing();
	}

	@Override
	public void onClick(View v) {
		final String statusText = editStatus.getText().toString();
		
		new PostToTwitter().execute(statusText);
		
		Log.d("StatusActivity", "onClicked with text: " + statusText);
		
	}
	
	class PostToTwitter extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				Twitter twitter = new Twitter("student","password");
				twitter.setAPIRootUrl("http://yamba.marakana.com/api");
				twitter.setStatus(params[0]);
				return "Susseccfully posted " + params[0];
			} catch (TwitterException e) {
				Log.e("Activity", "Died", e);
				return "Failed to post" + params[0];
			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Toast.makeText(StatusActivity.this,
					result,
					Toast.LENGTH_SHORT).show();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		Intent intent = new Intent(this, UpdaterService.class);
		switch(item.getItemId()) {
		case R.id.item_start_service:
			startService(intent);
			return true;
		case R.id.item_stop_service:
			stopService(intent);
			return true;
		default:
			return false;
		}
	}
}
