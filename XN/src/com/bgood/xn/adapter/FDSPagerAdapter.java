package com.bgood.xn.adapter;

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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class FDSPagerAdapter extends PagerAdapter implements OnPageChangeListener
{
    private List<ImageDTO> m_list;
	private TextView viewpager_index;
	private Context context;
	private LayoutInflater inflater;
	private PhotoView photoView;

	public FDSPagerAdapter(Context context, List<ImageDTO> list, TextView textView)
	{
		this.context = context;
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
		// imageView.setBackgroundResource(R.drawable.test10);
		// imageView.setMinScale(0.5f);
		// imageView.setMaxScale(1.5f);

		DisplayImageOptions imageOptions = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.loading_pictrue_item).imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.showImageOnFail(R.drawable.loading_pictrue_item).cacheOnDisc().build();

		ImageLoader.getInstance().displayImage(m_list.get(position).img, imageView, imageOptions, new ImageLoadingListener()
		{

			@Override
			public void onLoadingStarted(String imageUri, View view)
			{
				progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason)
			{
				progressBar.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
			{
				progressBar.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view)
			{

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
