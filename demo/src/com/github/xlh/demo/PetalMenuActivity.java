package com.github.xlh.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.xlh.app.BaseActivity;
import com.github.xlh.utils.MetricsUtil;
import com.github.xlh.utils.XLog;
import com.github.xlh.widget.PetalMenuView;
import com.github.xlh.widget.PetalMenuView.PetalMenuListener;

public class PetalMenuActivity extends BaseActivity {
	private TextView mControl;
	private PetalMenuView mMenus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petalmenu);
		mControl = (TextView) findViewById(R.id.control);
		mMenus = (PetalMenuView) findViewById(R.id.petal);
		mMenus.setMenus(new int[] { R.drawable.petal_menu_bg,
				R.drawable.petal_menu_bg, R.drawable.petal_menu_bg,
				R.drawable.petal_menu_bg });
		mMenus.setEdgeDetectionEnable(true);
		mMenus.setPetalMenuListener(new PetalMenuListener() {
			@Override
			public void onShowComplete(PetalMenuView view) {
				XLog.d("petal menu show!");
			}

			@Override
			public void onHideComplete(PetalMenuView view) {
				XLog.d("petal menu hide!");
			}

			@Override
			public void onClick(PetalMenuView view, int itemIndex) {
				XLog.d("petal menu click: " + itemIndex);
			}
		});
		final int screenWidth = MetricsUtil.getScreenWidth(this);
		final int screenHeight = MetricsUtil.getScreenHeight(this);
		mControl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (mMenus.isShow()) {
					mMenus.hide();
				} else {
					int randomX = (int) (Math.random() * screenWidth);
					int randomY = (int) (Math.random() * screenHeight);
					mMenus.showAtLocation(randomX, randomY);
				}
			}
		});
	}
}
