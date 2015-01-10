package com.bgood.xn.utils;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.ImageBean;
import com.bgood.xn.view.photoview.PhotoView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;

public class FDSPagerAdapter extends PagerAdapter implements OnPageChangeListener
{
    private List<ImageBean> m_list;
	private TextView viewpager_index;
	private LayoutInflater inflater;
	private PhotoView photoView;

	public FDSPagerAdapter(Context context, List<ImageBean> list, TextView textView)
	{
		this.m_list = list;
		this.viewpager_index = textView;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position)
	{
		View view = inflater.inflate(R.layout.layout_image_progress, null);
		PhotoView imageView = (PhotoView) view.findViewById(R.id.image);
		final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
		container.addView(view);
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnFail(R.drawable.icon_default)
		.showImageOnLoading(R.drawable.icon_default)
		.showImageForEmptyUri(R.drawable.icon_default)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.bitmapConfig(Bitmap.Config.RGB_565)  
		.build();

		ImageLoader.getInstance().displayImage(m_list.get(position).img, imageView, options, new com.nostra13.universalimageloader.core.listener.ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				progressBar.setVisibility(View.VISIBLE);	
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				progressBar.setVisibility(View.GONE);				
			}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				progressBar.setVisibility(View.GONE);				
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				progressBar.setVisibility(View.GONE);				
			}
		});
		
		return view;
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object)
	{
		System.out.println("setPrimaryItem");
		super.setPrimaryItem(container, position, object);
		RelativeLayout layout = (RelativeLayout) object;
		photoView = (PhotoView) layout.findViewById(R.id.image);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object)
	{
		container.removeView((View) object);
	}

	@Override
	public int getCount()
	{
		return m_list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1)
	{
		return arg0 == arg1;
	}

	@Override
	public void onPageScrollStateChanged(int arg0)
	{

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2)
	{

	}

	@Override
	public void onPageSelected(int arg0)
	{
		if (photoView != null)
			photoView.setScale(1f);
		viewpager_index.setText(arg0 + 1 + "/" + m_list.size());
	}
}
