package com.bgood.xn.adapter;
import java.util.List;

import com.bgood.xn.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Activity;
import android.graphics.Bitmap;
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
	public DisplayImageOptions options;

	private void initImgHelp(){
		options = new DisplayImageOptions.Builder()
		.showImageOnFail(R.drawable.icon_default)
		.showImageOnLoading(R.drawable.icon_default)
		.showImageForEmptyUri(R.drawable.icon_default)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.bitmapConfig(Bitmap.Config.RGB_565)  
		.build();
	}
	
	public KBaseAdapter(List<?> mList, Activity mActivity) {
		super();
		this.mList = mList;
		this.mActivity = mActivity;
		this.mInflater = LayoutInflater.from(mActivity);
		initImgHelp();
	}

	public KBaseAdapter(List<?> mList,Activity mActivity,int resLayout) {
		this.mList = mList;
		this.mActivity = mActivity;
		this.mInflater = LayoutInflater.from(mActivity);
		this.mLayout = resLayout;
		initImgHelp();
	}
	
	public KBaseAdapter(List<?> mList,Activity mActivity,OnClickListener listener) {
		this.mList = mList;
		this.mActivity = mActivity;
		this.mInflater = LayoutInflater.from(mActivity);
		this.mListener = listener;
		initImgHelp();
	}
	
	public KBaseAdapter(List<?> mList,Activity mActivity,int resLayout,OnClickListener listener) {
		this.mList = mList;
		this.mActivity = mActivity;
		this.mInflater = LayoutInflater.from(mActivity);
		this.mLayout = resLayout;
		this.mListener = listener;
		initImgHelp();
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
