package com.github.xlh.demo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.github.xlh.adpter.AbstractAdapter;
import com.github.xlh.app.BaseActivity;
import com.github.xlh.utils.MetricsUtil;

public class MainActivity extends BaseActivity {
	private ListView mLv = null;
	private Adapter mAdapter = null;

	private String[] mTitles = { "WebImageView" };
	private Class[] mActivities = { WebImageActivity.class };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mLv = (ListView) findViewById(R.id.list);

		mAdapter = new Adapter();
		mAdapter.setData(mTitles);
		mLv.setAdapter(mAdapter);
		mLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(MainActivity.this, mActivities[arg2]);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private static class Adapter extends AbstractAdapter<String> {

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			TextView tv = (TextView) arg1;
			if (tv == null) {
				tv = new TextView(arg2.getContext());
				tv.setTextColor(Color.WHITE);
				int height = MetricsUtil.dp2px(arg2.getContext(), 40);
				LayoutParams lp = new LayoutParams(
						AbsListView.LayoutParams.MATCH_PARENT, height);
				tv.setLayoutParams(lp);
			}
			tv.setText(getDataAt(arg0));
			return tv;
		}
	}
}
