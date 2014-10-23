/*
 * Copyright (C) 2013 47 Degrees, LLC
 *  http://47deg.com
 *  hello@47deg.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.bgood.xn.view.pulltorefresh;

import java.lang.reflect.Field;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Scroller;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.view.pulltorefresh.listener.IChangeStateListener;
import com.bgood.xn.view.pulltorefresh.listener.IListViewItemListener;
import com.bgood.xn.view.pulltorefresh.listener.IOnDragListener;
import com.bgood.xn.view.pulltorefresh.listener.IPageListViewItemListener;

/**
 * 
 * PageListViewAdapter 支持左右滑动listview 适配器
 * 
 * tanxiaoxing 2013-8-17 下午2:09:29
 * 
 * @version 1.0.0
 * 
 */

public class PageListViewAdapter extends BaseAdapter {

	private static final String TAG = PageListViewAdapter.class.getSimpleName();
	// 记录滑动选项卡Map，key =item index； value= 选项卡 position
	private SparseIntArray mStateMap = new SparseIntArray();
	// 数据源
	private List<Object> mData;
	private Context mContext;
	private LayoutInflater mLf;
	// 最小滑动距离
	private int mDefaultXDistance;
	// 第一页ID
	private int mItemFirstViewId;
	// 第二页ID
	private int mItemSecondViewId;
	// item标题View，不可左右滑动的
	private int mItemTitleViewId;
	// 界面初始化监听
	private IPageListViewItemListener mPageListViewItemListener;
	// LISTVIEW ITEM头部监听
	private IListViewItemListener mListViewItemListener;

	// 选中状态改变监听
	private IChangeStateListener mChangeStateListener;
	// 默认选中状态颜色值
	private int mDefaultSelectColorId = android.R.color.black;
	// 默认未选中颜色
	private int mDefaultUnSelectColorId = android.R.color.darker_gray;
	// ITEM拖曳回调接口
	private IOnDragListener mOnDragListener;

	@SuppressWarnings("static-access")
	public PageListViewAdapter(Context context, List<Object> data,
			int itemFirstViewId, int itemSecondViewId) {
		this.mContext = context;
		this.mData = data;
		this.mItemFirstViewId = itemFirstViewId;
		this.mItemSecondViewId = itemSecondViewId;
		mLf = ((Activity) mContext).getLayoutInflater().from(mContext);
		// ViewConfiguration viewConfiguration = ViewConfiguration
		// .get(this.mContext);
		// mDefaultXDistance = viewConfiguration.getMinimumFlingVelocity();

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);

		int width = wm.getDefaultDisplay().getWidth();
		mDefaultXDistance = width / 3;

	}

	/**
	 * 设置界面监听回调
	 * 
	 * @param pageListViewItemListener
	 */
	public void setPageListViewItemListener(
			IPageListViewItemListener pageListViewItemListener) {
		this.mPageListViewItemListener = pageListViewItemListener;
	}

	/**
	 * setmItemTitleViewId
	 * 
	 * @param mItemTitleViewId
	 */
	public void setItemTitleViewId(int itemTitleViewId) {
		this.mItemTitleViewId = itemTitleViewId;
	}

	/**
	 * setListViewItemListener
	 * 
	 * @param listViewItemListener
	 */
	public void setListViewItemListener(
			IListViewItemListener listViewItemListener) {
		this.mListViewItemListener = listViewItemListener;
	}

	/**
	 * 设置item选中状态改变监听
	 * 
	 * @param changeStateListener
	 */
	public void setChangeStateListener(IChangeStateListener changeStateListener) {
		this.mChangeStateListener = changeStateListener;
	}

	@Override
	public int getCount() {
		if (mData != null) {
			return mData.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		if (mData != null && mData.size() > position) {
			return mData.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private void setViewPagerScrollDuration(ViewPager viewPager) {
		try {
			Field mScroller = viewPager.getClass()
					.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			Scroller scroll = new Scroller(mContext);
			Field scrollDuration = scroll.getClass().getDeclaredField(
					"mDuration");
			scrollDuration.setAccessible(true);
			scrollDuration.set(scroll, 0);
		} catch (Exception e) {
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater li = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(R.layout.base_list_item, parent, false);

			holder = new ViewHolder();
			holder.viewPager = (ViewPager) convertView
					.findViewById(R.id.viewPager);
			setViewPagerScrollDuration(holder.viewPager);
			holder.mLvTitle = (RelativeLayout) convertView
					.findViewById(R.id.lvTitle);
			if (mItemTitleViewId != 0) {
				View titleView = mLf.inflate(mItemTitleViewId, null);
				LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT);
				holder.mLvTitle.addView(titleView, lp);
			}

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Object obj = getItem(position);
		if (obj != null) {
			if (mListViewItemListener != null && mItemTitleViewId != 0) {
				mListViewItemListener.getItemTitleView(position,
						holder.mLvTitle);
			} else {
				holder.mLvTitle.setVisibility(View.VISIBLE);
			}

			holder.viewPager.setAdapter(new MyPagerAdapter(position));
			holder.viewPager.setTag(position);
			holder.viewPager
					.setOnPageChangeListener(new MyOnPageChangeListener(
							holder.viewPager));
			// 设置选项卡设置项
			int pos = mStateMap.get(position);
			holder.viewPager.setCurrentItem(pos != 0 ? pos : 1, false);
		}

		return convertView;
	}

	static class ViewHolder {
		ViewPager viewPager;
		RelativeLayout mLvTitle;
	}

	class MyPagerAdapter extends PagerAdapter {

		private int mPosition;
		// 是否选择
		private boolean mIsSelect;
		// 第一页空白页，用于显示回弹界面
		private View mFirstView;
		// 选中标记界面
		private View mFlagView;

		// 是否初始化
		private boolean isFirstInit = true;;

		private int mSelectColorId;

		public MyPagerAdapter(int position) {
			this.mPosition = position;
		}

		public void setIsSelect(boolean mIsSelect) {
			this.mIsSelect = mIsSelect;
		}

		public void setSelectColorId(int mSelectColorId) {
			this.mSelectColorId = mSelectColorId;
		}

		/**
		 * 获取是否选择状态
		 */
		public boolean isSelect() {
			return mIsSelect;
		}

		public View getFirstView() {
			return mFirstView;
		}

		public View getFlagView() {
			return mFlagView;
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return (view == object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view;
			switch (position) {
			case 0:
				view = mLf.inflate(R.layout.pager_base_item_1, null);
				mFirstView = view;
				// 选中状态
				if (mIsSelect) {
					view.setBackgroundColor(mContext.getResources().getColor(
							mSelectColorId != 0 ? mSelectColorId
									: mDefaultSelectColorId));
				}
				break;
			case 1:
				view = (RelativeLayout) mLf.inflate(R.layout.pager_base_item_2,
						null);
				View mViewFirst = mLf.inflate(mItemFirstViewId, null);
				TextView tvFlagView = (TextView) view
						.findViewById(R.id.tv_flag);

				if (mPageListViewItemListener != null) {
					boolean mIsDefaultSelect = mPageListViewItemListener
							.getFirstView(mPosition, mViewFirst);
					// 初始化赋值
					if (isFirstInit) {
						mIsSelect = mIsDefaultSelect;
					}
					mSelectColorId = mPageListViewItemListener
							.getSelectColorId(mPosition);
				}
				LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.FILL_PARENT);
				lp.addRule(RelativeLayout.RIGHT_OF, R.id.tv_flag);
				((RelativeLayout) view).addView(mViewFirst, lp);
				mFlagView = tvFlagView;
				// 选中状态
				setFlagViewState(mIsSelect, tvFlagView, mSelectColorId);
				isFirstInit = false;
				break;
			default:
				view = mLf.inflate(mItemSecondViewId, null);
				if (mPageListViewItemListener != null) {
					mPageListViewItemListener.getSecondView(mPosition, view);
				}
				break;
			}
			try {
				if (view.getParent() == null) {
					((ViewPager) container).addView(view);
				} else {
					((ViewPager) container).removeView(view);
					((ViewPager) container).addView(view);
				}
			} catch (Exception e) {
			}
			return view;

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

	}

	/**
	 * 设置选中状态，（用于选中展示也，左端小颜色块）
	 * 
	 * @param isSelect
	 * @param flagView
	 * @param selectColorId
	 */
	private void setFlagViewState(boolean isSelect, View flagView,
			int selectColorId) {
		if (flagView == null) {
			return;
		}
		if (isSelect) {
			flagView.setBackgroundColor(mContext.getResources().getColor(
					selectColorId != 0 ? selectColorId : mDefaultSelectColorId));
			flagView.setVisibility(View.VISIBLE);
		} else {
			flagView.setBackgroundColor(mContext.getResources().getColor(
					mDefaultUnSelectColorId));
			flagView.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 
	 * MyOnPageChangeListener viewpager数据适配器
	 * 
	 * tanxiaoxing 2013-8-16 下午9:22:15
	 * 
	 * @version 1.0.0
	 * 
	 */

	public class MyOnPageChangeListener implements OnPageChangeListener {
		// private boolean mIsLeft = false;
		private boolean mIsRight;
		private boolean mIsScrolling;
		private int mLastValue = -1;
		private boolean mIsLastToRight;
		private int mPosition;
		private ViewPager mPager;

		public MyOnPageChangeListener(ViewPager viewPager) {
			mPager = viewPager;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// Log.d(TAG, "---onPageScrollStateChanged- " + arg0);
			if (arg0 == 1) {
				mIsScrolling = true;
			} else {
				mIsScrolling = false;
			}
			if (arg0 == 0) {
				changeState(mPager);
			}

		}

		/**
		 * 监听首端pager滑动 实现反弹效果
		 * 
		 * @param arg0
		 */
		@Override
		public void onPageScrolled(int position, float arg1, int arg2) {
			this.setPosition(position);
			// Log.d(TAG, "---onPageSelected- " + "  mDefaultXDistance=="
			// + mDefaultXDistance + "  " + arg2);
			if (mIsScrolling) {
				if (mLastValue > arg2 && arg2 > mDefaultXDistance) {
					// 递减，向右侧滑动
					mIsRight = true;
					// mIsLeft = false;
				} else if (mLastValue < arg2) {
					// 递减，向右侧滑动
					mIsRight = false;
					// mIsLeft = true;
				} else if (mLastValue == arg2) {
					mIsRight = false;
				}
			}
			mLastValue = arg2;
		}

		@Override
		public void onPageSelected(int position) {
			// Log.d(TAG, "---onPageSelected- " + position);
			this.setPosition(position);
			if (position == 0) {
				mPager.setCurrentItem(1);
			}
			if (mPosition == 2) {
				mIsLastToRight = true;
			}

		}

		/**
		 * setPosition设置选项卡位置
		 * 
		 * @param position
		 */
		public void setPosition(int position) {
			// listview中 item位置
			int itemPos = (Integer) mPager.getTag();
			mStateMap.delete(itemPos);
			mStateMap.put(itemPos, position);
			this.mPosition = position;
		}

		/**
		 * 改变选择状态
		 * 
		 * @param viewPager
		 *            ViewPager
		 */
		private void changeState(ViewPager viewPager) {
			// Log.d(TAG, " mPosition==" + mPosition + " right==" + mIsRight
			// + "  isLastToRight ==" + mIsLastToRight);

			if ((mPosition == 0 || mPosition == 1) && mIsRight) {
				if (mIsLastToRight) {
					mIsLastToRight = false;
					return;
				}
				MyPagerAdapter adpter = (MyPagerAdapter) mPager.getAdapter();
				adpter.setIsSelect(!adpter.isSelect());

				// 选中状态
				int selectColorId = 0;
				if (mChangeStateListener != null) {
					selectColorId = mChangeStateListener.onChangeState(
							(Integer) viewPager.getTag(), adpter.isSelect());
				}

				adpter.setSelectColorId(selectColorId);
				setFlagViewState(adpter.isSelect(), adpter.getFlagView(),
						selectColorId);
				setFlagViewState(adpter.isSelect(), adpter.getFirstView(),
						selectColorId);

			}
		}

	}

	/**
	 * setOnDragListener
	 * 
	 * @param onDragListener
	 */
	public void setOnDragListener(IOnDragListener onDragListener) {
		this.mOnDragListener = onDragListener;
	}

	/***
	 * 拖动更新ListVIiw的方位.
	 * 
	 * @param start
	 *            点击移动的position
	 * @param down
	 *            松开时候的position
	 */
	public void dragUpdate(int start, int down) {
		// Log.d(TAG, "===update=" + start + " to  " + down);
		if (start < mData.size()) {
			if (mOnDragListener != null) {
				mOnDragListener.onDragFinish(start, down);
			}
		}
	}

	/**
	 * getStateMap 获取Item选项卡位置
	 * 
	 * @return
	 */
	public SparseIntArray getStateMap() {
		return mStateMap;
	}

	private Bitmap getBitmap(View view) {
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();

		return bitmap;
	}
}
