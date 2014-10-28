package com.bgood.xn.ui.welcome;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bgood.xn.R;

/**
 * 用户帮助适配器
 * @author ld 20130418
 *
 */
public class NavigatePagerAdapter extends PagerAdapter {

	private LayoutInflater inflater;
	private int[] ids;
	OnClickListener mOnClickListener;

	public NavigatePagerAdapter(Activity context,int[] ids,OnClickListener mOnClickListener) {
		inflater = context.getLayoutInflater();
		this.ids = ids;
		this.mOnClickListener = mOnClickListener;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public void finishUpdate(View container) {
	}

	@Override
	public int getCount() {
//		return images.length;
		return ids.length;
	}

	@Override
	public Object instantiateItem(View view, int position) {
		final FrameLayout imageLayout = (FrameLayout) inflater.inflate(R.layout.item_naviagte_pager_image, null);
		final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
		final ImageButton start_btn = (ImageButton)imageLayout.findViewById(R.id.start_btn);
		if(position == 2){
			start_btn.setVisibility(View.VISIBLE);
			start_btn.setOnClickListener(mOnClickListener);
		}
		imageView.setBackgroundResource(ids[position]);
		((ViewPager) view).addView(imageLayout, 0);
		return imageLayout;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View container) {
	}


}
