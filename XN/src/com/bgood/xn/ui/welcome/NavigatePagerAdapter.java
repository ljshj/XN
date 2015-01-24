package com.bgood.xn.ui.welcome;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bgood.xn.R;

/**
 * 
 * @todo:欢迎页适配器
 * @date:2015-1-22 下午7:09:09
 * @author:hg_liuzl@163.com
 */
public class NavigatePagerAdapter extends PagerAdapter {

	private LayoutInflater inflater;
	private int[] ids;
	private Activity mActivity;
	private ILoadCommpletListener mLoad;
	public NavigatePagerAdapter(Activity activity,int[] ids,ILoadCommpletListener load) {
		this.mActivity = activity;
		this.inflater = mActivity.getLayoutInflater();
		this.ids = ids;
		this.mLoad = load;
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

	private ImageView imageView;
	private ImageButton start_btn;
	private FrameLayout left_view,right_view;
	private FrameLayout start_view;
	FrameLayout imageLayout;
	
	@Override
	public Object instantiateItem(View view, int position) {
		imageLayout = (FrameLayout) inflater.inflate(R.layout.item_naviagte_pager_image, null);
		imageView = (ImageView) imageLayout.findViewById(R.id.image);
		start_btn = (ImageButton)imageLayout.findViewById(R.id.start_btn);
		start_view = (FrameLayout) imageLayout.findViewById(R.id.start_view);
		left_view = (FrameLayout) imageLayout.findViewById(R.id.left_view);
		right_view = (FrameLayout) imageLayout.findViewById(R.id.right_view);
		if(position == 3){
			start_btn.setVisibility(View.VISIBLE);
			start_btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					doOpenDoor();
					
					//gotoMain();
				}
			});
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
	
	/**
	 * 开门效果
	 */
	@SuppressLint("ResourceAsColor")
	private void doOpenDoor(){  
		start_view.setVisibility(View.GONE);
        Animation leftOutAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.translate_left);  
        Animation rightOutAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.translate_right);  
        left_view.setAnimation(leftOutAnimation);  
        right_view.setAnimation(rightOutAnimation); 
        left_view.startAnimation(leftOutAnimation);
        right_view.startAnimation(rightOutAnimation);
        leftOutAnimation.setAnimationListener(new AnimationListener() {  
            @Override  
            public void onAnimationStart(Animation animation) { 
            }  
            @Override  
            public void onAnimationRepeat(Animation animation) {  
            }  
            @Override  
            public void onAnimationEnd(Animation animation) {  
            	left_view.setVisibility(View.GONE);  
            	right_view.setVisibility(View.GONE);  
            	mLoad.loadCommplet();
            }  
        });  
    }  
}
