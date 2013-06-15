package com.example.yamba;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class TimelineActivity extends ListActivity {
	static final String[] FROM = { StatusData.C_USER, StatusData.C_TEXT, StatusData.C_CREATED_AT };
	static final int[] TO = { R.id.text_user, R.id.text_text, R.id.text_created_at };
	Cursor cursor;
	SimpleCursorAdapter adapter;
	
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

}
