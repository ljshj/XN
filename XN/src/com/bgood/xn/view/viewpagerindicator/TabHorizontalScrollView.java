package com.bgood.xn.view.viewpagerindicator;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bgood.xn.R;

/**
 * This widget implements the dynamic action bar tab behavior that can change across different configurations or circumstances.
 */
public class TabHorizontalScrollView extends HorizontalScrollView {
	/** Title text used when no title is provided by the adapter. */
	private static final String EMPTY_TITLE = "";

	/**
	 * Interface for a callback when the selected tab has been reselected.
	 */
	public interface OnTabReselectedListener {
		/**
		 * Callback when the selected tab has been reselected.
		 * 
		 * @param position Position of the current center item.
		 */
		void onTabReselected(int position);
	}

	private Runnable mTabSelector;

	private final OnClickListener mTabClickListener = new OnClickListener() {
		public void onClick(View view) {
			TabView tabView = (TabView) view;
			final int newSelected = tabView.getIndex();
			if (mSelectedTabIndex == newSelected)
				return;
			setCurrentItem(newSelected);
			if (mListener != null) {
				mListener.onPageSelected(newSelected);
			}
		}
	};

	private final IcsLinearLayout mTabLayout;

	private ViewPager.OnPageChangeListener mListener;

	private int mMaxTabWidth;
	private int mSelectedTabIndex;

	private String[] titles;

	public TabHorizontalScrollView(Context context) {
		this(context, null);
	}

	public TabHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setHorizontalScrollBarEnabled(false);

		mTabLayout = new IcsLinearLayout(context, R.attr.vpiTabPageIndicatorStyle);
		LinearLayout viewGroup = new LinearLayout(context);
		viewGroup.setOrientation(LinearLayout.VERTICAL);
		View top = new View(context);
		top.setBackgroundResource(R.drawable.img_underline_vpi);
		View bottom = new View(context);
		bottom.setBackgroundResource(R.drawable.img_underline_vpi);
		viewGroup.addView(top, new ViewGroup.LayoutParams(MATCH_PARENT, 1));
		viewGroup.addView(mTabLayout, new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
		viewGroup.addView(bottom, new ViewGroup.LayoutParams(MATCH_PARENT, 1));
		addView(viewGroup, new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
		setFillViewport(lockedExpanded);

		final int childCount = mTabLayout.getChildCount();
		if (childCount > 1 && (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
			if (childCount > 2) {
				mMaxTabWidth = (int) (MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
			} else {
				mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
			}
		} else {
			mMaxTabWidth = -1;
		}

		final int oldWidth = getMeasuredWidth();
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int newWidth = getMeasuredWidth();

		if (lockedExpanded && oldWidth != newWidth) {
			// Recenter the tab display if we're at a new (scrollable) size.
			setCurrentItem(getSelectedTabIndex());
		}
	}

	private void animateToTab(final int position) {
		final View tabView = mTabLayout.getChildAt(position);
		if (mTabSelector != null) {
			removeCallbacks(mTabSelector);
		}
		mTabSelector = new Runnable() {
			public void run() {
				final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
				smoothScrollTo(scrollPos, 0);
				mTabSelector = null;
			}
		};
		post(mTabSelector);
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (mTabSelector != null) {
			// Re-post the selector we saved
			post(mTabSelector);
		}
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mTabSelector != null) {
			removeCallbacks(mTabSelector);
		}
	}

	private void addTab(int index, CharSequence text, int iconResId) {
		final TabView tabView = new TabView(getContext());
		tabView.mIndex = index;
		tabView.setFocusable(true);
		tabView.setOnClickListener(mTabClickListener);
		tabView.setText(text);

		if (iconResId != 0) {
			tabView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
		}

		mTabLayout.addView(tabView, new LinearLayout.LayoutParams(0, MATCH_PARENT, 1));
	}

	public void onPageScrollStateChanged(int arg0) {
		if (mListener != null) {
			mListener.onPageScrollStateChanged(arg0);
		}
	}

	public void onPageScrolled(int arg0, float arg1, int arg2) {
		if (mListener != null) {
			mListener.onPageScrolled(arg0, arg1, arg2);
		}
	}

	public void onPageSelected(int arg0) {
		setCurrentItem(arg0);
		if (mListener != null) {
			mListener.onPageSelected(arg0);
		}
	}

	public void setCurrentItem(int item) {
		setSelectedTabIndex(item);

		final int tabCount = mTabLayout.getChildCount();
		for (int i = 0; i < tabCount; i++) {
			final View child = mTabLayout.getChildAt(i);
			final boolean isSelected = (i == item);
			child.setSelected(isSelected);
			if (isSelected) {
				animateToTab(item);
			}
		}
	}

	public void setOnPageChangeListener(OnPageChangeListener listener) {
		mListener = listener;
	}

	private class TabView extends TextView {
		private int mIndex;

		public TabView(Context context) {
			super(context, null, R.attr.vpiTabPageIndicatorStyle);
		}

		@Override
		public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);

			// Re-measure if we went beyond our maximum size.
			if (mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
				super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
			}
		}

		public int getIndex() {
			return mIndex;
		}
	}

	public void setTitle(String[] titles) {
		this.titles = titles;
		notifyDataSetChanged();
	}

	public void notifyDataSetChanged() {
		mTabLayout.removeAllViews();
		for (int i = 0; i < titles.length; i++) {
			String title = titles[i];
			if (title == null) {
				title = EMPTY_TITLE;
			}
			int iconResId = 0;
			addTab(i, title, iconResId);
		}
		if (getSelectedTabIndex() > titles.length) {
			setSelectedTabIndex(titles.length - 1);
		}
		setCurrentItem(getSelectedTabIndex());
		requestLayout();
	}

	public int getSelectedTabIndex() {
		return mSelectedTabIndex;
	}

	private void setSelectedTabIndex(int mSelectedTabIndex) {
		this.mSelectedTabIndex = mSelectedTabIndex;
	}
}
