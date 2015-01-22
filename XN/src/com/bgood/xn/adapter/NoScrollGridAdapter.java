package com.bgood.xn.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap.Config;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bgood.xn.R;
import com.bgood.xn.bean.ImageBean;
import com.bgood.xn.system.BGApp;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * 九宫格展示小图片
 */
public class NoScrollGridAdapter extends KBaseAdapter {
	/** 图片Url集合 */
	private ArrayList<String> imageSmallUrls = new ArrayList<String>();
	public NoScrollGridAdapter(List<ImageBean> mList, Activity mActivity) {
		super(mList, mActivity);
		for(ImageBean img:mList){
			imageSmallUrls.add(img.img_thum);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.item_gridview, null);
		ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_image);
		BGApp.getInstance().setImageSqure(imageSmallUrls.get(position), imageView);
		return convertView;
	}

}
