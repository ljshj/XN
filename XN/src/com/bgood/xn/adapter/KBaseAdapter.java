package com.bgood.xn.adapter;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @todo adapter的基类
 * @author liuzenglong163@gmail.com
 * @param <T>
 */

public class KBaseAdapter extends BaseAdapter {
	
	public List<?> mList;
	public LayoutInflater mInflater;
	public Activity mActivity;
	public int mLayout;
	public OnClickListener mListener;
	
	public KBaseAdapter(List<?> mList, Activity mActivity) {
		super();
		this.mList = mList;
		this.mActivity = mActivity;
		this.mInflater = LayoutInflater.from(mActivity);
	}

	public KBaseAdapter(List<?> mList,Activity mActivity,int resLayout) {
		this.mList = mList;
		this.mActivity = mActivity;
		this.mInflater = LayoutInflater.from(mActivity);
		this.mLayout = resLayout;
	}
	
	public KBaseAdapter(List<?> mList,Activity mActivity,OnClickListener listener) {
		this.mList = mList;
		this.mActivity = mActivity;
		this.mInflater = LayoutInflater.from(mActivity);
		this.mListener = listener;
	}
	
	public KBaseAdapter(List<?> mList,Activity mActivity,int resLayout,OnClickListener listener) {
		this.mList = mList;
		this.mActivity = mActivity;
		this.mInflater = LayoutInflater.from(mActivity);
		this.mLayout = resLayout;
		this.mListener = listener;
	}

	@Override
	public int getCount() {
		if (null!=mList) {
			return mList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (null!=mList) {
			return mList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		if (null!=mList) {
			return position;
		}
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}
}
