package com.github.xlh.adpter;

import java.util.ArrayList;
import java.util.List;

import android.widget.BaseAdapter;

public abstract class AbstractAdapter<T> extends BaseAdapter {
	List<T> mData;

	public void setData(List<T> data) {
		mData = data;
		notifyDataSetChanged();
	}

	public void setData(T[] array) {
		if (array == null) {
			mData = null;
		} else {
			mData = new ArrayList<T>();
			for (T item : array) {
				mData.add(item);
			}
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (mData == null) {
			return 0;
		}
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	public T getDataAt(int position) {
		return mData.get(position);
	}

	public List<T> getData() {
		return mData;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
