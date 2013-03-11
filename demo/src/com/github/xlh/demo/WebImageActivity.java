package com.github.xlh.demo;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView.ScaleType;

import com.github.xlh.adpter.AbstractAdapter;
import com.github.xlh.app.BaseActivity;
import com.github.xlh.utils.XLog;
import com.github.xlh.webimage.WebImageView;

public class WebImageActivity extends BaseActivity {
	private GridView mGridView;
	private Adapter mAdapter;
	private static String[] mUrls = {
			"http://img.baidu.com/img/image/liulanimage/w_bizhi.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_dongmanyouxi.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_lvyoufengjing.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_meinv.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_mengchong.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_qiche.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_quweigaoxiao.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_sheying.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_yulemingxing.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_bizhi.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_dongmanyouxi.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_lvyoufengjing.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_meinv.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_mengchong.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_qiche.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_quweigaoxiao.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_sheying.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_yulemingxing.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_bizhi.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_dongmanyouxi.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_lvyoufengjing.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_meinv.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_mengchong.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_qiche.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_quweigaoxiao.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_sheying.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_yulemingxing.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_bizhi.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_dongmanyouxi.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_lvyoufengjing.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_meinv.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_mengchong.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_qiche.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_quweigaoxiao.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_sheying.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_yulemingxing.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_bizhi.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_dongmanyouxi.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_lvyoufengjing.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_meinv.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_mengchong.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_qiche.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_quweigaoxiao.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_sheying.jpg",
			"http://img.baidu.com/img/image/liulanimage/w_yulemingxing.jpg", };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webimage);
		mGridView = (GridView) findViewById(R.id.grid);
		mAdapter = new Adapter();
		mAdapter.setData(mUrls);
		mGridView.setAdapter(mAdapter);
	}

	private static class Adapter extends AbstractAdapter<String> {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			XLog.d("getView" + position);
			WebImageView view = (WebImageView) convertView;
			if (view == null) {
				view = new WebImageView(parent.getContext());
				view.setScaleType(ScaleType.CENTER_CROP);
			}
			view.setWebImageUrl(getDataAt(position));
			return view;
		}
	}
}
