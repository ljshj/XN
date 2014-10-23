/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *******************************************************************************/
package com.bgood.xn.view.pulltorefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.bgood.xn.R;

/**
 * 上下拉动刷新ListView控件
 * 
 * 
 * @author tan.xx
 * @since 2012-8-14
 * @version 2012-8-14，谭晓星，重构文本、注释等<br>
 *          2012-09-29 谭晓星 增加下拉刷新时无数据时的处理<br>
 *          2012-10/31 tan.xx 增加下拉刷新内容列表事件分发处理<br>
 *          2012-11-09 谭晓星 重载setHeaderVisible方法，优化频繁使用此方法时显示异常问题<br>
 * 
 */
public class PullToRefreshListView extends PullToRefreshAdapterViewBase<ListView>
{

	// listview headerview是否有显示过
	private boolean mListViewHasShow;
	private HeaderLoadingLayout mHeaderLoadingView;
	private FooterLoadingLayout mFooterLoadingView;

	private FrameLayout mLvFooterLoadingFrame;

	// 设置可支持的拖拽view 资源id,在setAdpater前面使用，不然无效
	private int mDragViewId = -1;

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            当前布局文件的上下文
	 */
	public PullToRefreshListView(Context context)
	{
		super(context);
		setDisableScrollingWhileRefreshing(false);
	}

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            当前布局文件的上下文
	 * @param attrs
	 *            属性数组
	 */
	public PullToRefreshListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setDisableScrollingWhileRefreshing(false);
	}

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            当前布局文件的上下文
	 * @param mode
	 *            拉动的样式
	 */
	public PullToRefreshListView(Context context, Mode mode)
	{
		super(context, mode);
		setDisableScrollingWhileRefreshing(false);
	}

	@Override
	public ContextMenuInfo getContextMenuInfo()
	{
		return ((InternalListView) getRefreshableView()).getContextMenuInfo();
	}

	@Override
	public void setHeaderLabel(CharSequence label)
	{
		super.setHeaderLabel(label);

		if (null != mHeaderLoadingView && getMode().canPullDown())
		{
			mHeaderLoadingView.setSubHeaderText(label);
		}

		refreshLoadingViewsHeight();
	};

	/**
	 * 设置下拉时的文本信息
	 * 
	 * @param pullLabel
	 *            下拉时的文本信息
	 * @param mode
	 *            上下拉动的模式
	 */
	@Override
	public void setPullLabel(String pullLabel, Mode mode)
	{
		super.setPullLabel(pullLabel, mode);

		if (null != mHeaderLoadingView && mode.canPullDown())
		{
			mHeaderLoadingView.setPullLabel(pullLabel);
		}
		// if (null != mFooterLoadingView && mode.canPullUp()) {
		// // mFooterLoadingView.setPullLabel(pullLabel);
		// }
	}

	/**
	 * 设置正在刷新的文本信息
	 * 
	 * @param refreshingLabel
	 *            正在刷新的文本信息
	 * @param mode
	 *            上下拉动的模式
	 */
	@Override
	public void setRefreshingLabel(String refreshingLabel, Mode mode)
	{
		super.setRefreshingLabel(refreshingLabel, mode);

		if (null != mHeaderLoadingView && mode.canPullDown())
		{
			mHeaderLoadingView.setRefreshingLabel(refreshingLabel);
		}
		if (null != mFooterLoadingView && mode.canPullUp())
		{
			mFooterLoadingView.setRefreshingLabel(refreshingLabel);
		}
	}

	/**
	 * 设置手势释放时的文本信息
	 * 
	 * @param releaseLabel
	 *            手势释放时的文本信息
	 * @param mode
	 *            上下拉动的模式
	 */
	@Override
	public void setReleaseLabel(String releaseLabel, Mode mode)
	{
		super.setReleaseLabel(releaseLabel, mode);

		if (null != mHeaderLoadingView && mode.canPullDown())
		{
			mHeaderLoadingView.setReleaseLabel(releaseLabel);
		}
		if (null != mFooterLoadingView && mode.canPullUp())
		{
			mFooterLoadingView.setReleaseLabel(releaseLabel);
		}
	}

	@Override
	protected final ListView createRefreshableView(Context context, AttributeSet attrs)
	{
		ListView lv = new InternalListView(context, attrs);

		// Get Styles from attrs
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PullToRefresh);

		// Create Loading Views ready for use later
		FrameLayout frame = new FrameLayout(context);
		mHeaderLoadingView = new HeaderLoadingLayout(context, Mode.PULL_DOWN_TO_REFRESH, a);
		frame.addView(mHeaderLoadingView, android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		mHeaderLoadingView.setVisibility(View.GONE);
		lv.addHeaderView(frame, null, false);

		mLvFooterLoadingFrame = new FrameLayout(context);
		mFooterLoadingView = new FooterLoadingLayout(context, Mode.PULL_UP_TO_REFRESH, a);
		mLvFooterLoadingFrame.addView(mFooterLoadingView, android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		mFooterLoadingView.setVisibility(View.GONE);

		a.recycle();

		// Set it to this so it can be used in ListActivity/ListFragment
		lv.setId(android.R.id.list);
		return lv;
	}

	@Override
	protected int getNumberInternalFooterViews()
	{
		return null != mFooterLoadingView ? 1 : 0;
	}

	@Override
	protected int getNumberInternalHeaderViews()
	{
		return null != mHeaderLoadingView ? 1 : 0;
	}

	@Override
	protected void resetHeader()
	{

		// If we're not showing the Refreshing view, or the list is empty, then
		// the header/footer views won't show so we use the
		// normal method
		ListAdapter adapter = mRefreshableView.getAdapter();
		if (!mListViewHasShow)
		{
			// headerView未显示过
			if (!getShowViewWhileRefreshing() || null == adapter || adapter.isEmpty())
			{
				super.resetHeader();
				return;
			}
		}

		HeaderLoadingLayout originalLoadingLayout = null;
		HeaderLoadingLayout listViewLoadingLayout = null;
		FooterLoadingLayout originalFooterLoadingLayout = null;
		FooterLoadingLayout listViewFooterLoadingLayout = null;

		int scrollToHeight = getHeaderHeight();
		int selection;
		boolean scroll;

		switch (getCurrentMode())
		{
		case PULL_UP_TO_REFRESH:
			originalFooterLoadingLayout = getFooterLayout();
			listViewFooterLoadingLayout = mFooterLoadingView;
			selection = mRefreshableView.getCount() - 1;
			scroll = mRefreshableView.getLastVisiblePosition() == selection;
			break;
		case PULL_DOWN_TO_REFRESH:
		default:
			originalLoadingLayout = getHeaderLayout();
			listViewLoadingLayout = mHeaderLoadingView;
			scrollToHeight *= -1;
			selection = 0;
			scroll = mRefreshableView.getFirstVisiblePosition() == selection;
			break;
		}

		// Set our Original View to Visible
		if (originalLoadingLayout != null)
		{
			originalLoadingLayout.setVisibility(View.VISIBLE);
		} else
		{
			originalFooterLoadingLayout.setVisibility(View.VISIBLE);
		}

		/**
		 * Scroll so the View is at the same Y as the ListView header/footer,
		 * but only scroll if we've pulled to refresh and it's positioned
		 * correctly
		 */
		if (scroll && getState() != MANUAL_REFRESHING)
		{
			mRefreshableView.setSelection(selection);
			setHeaderScroll(scrollToHeight);
		}

		// Hide the ListView Header/Footer
		if (listViewLoadingLayout != null)
		{
			listViewLoadingLayout.setVisibility(View.GONE);
		} else
		{
			listViewFooterLoadingLayout.setVisibility(View.GONE);
		}

		super.resetHeader();
	}

	@Override
	protected void setRefreshingInternal(boolean doScroll)
	{
		// If we're not showing the Refreshing view, or the list is empty, then
		// the header/footer views won't show so we use the
		// normal method
		ListAdapter adapter = mRefreshableView.getAdapter();
		if (!getShowViewWhileRefreshing() || null == adapter || adapter.isEmpty())
		{
			super.setRefreshingInternal(doScroll);
			return;
		}
		super.setRefreshingInternal(false);
		HeaderLoadingLayout originalLoadingLayout = null;
		HeaderLoadingLayout listViewLoadingLayout = null;
		FooterLoadingLayout originalFooterLoadingLayout = null;
		FooterLoadingLayout listViewFooterLoadingLayout = null;
		final int selection;
		final int scrollToY;
		switch (getCurrentMode())
		{
		case PULL_UP_TO_REFRESH:
			originalFooterLoadingLayout = getFooterLayout();
			listViewFooterLoadingLayout = mFooterLoadingView;
			selection = mRefreshableView.getCount() - 1;
			scrollToY = getScrollY() - getFooterHeight();
			break;
		case PULL_DOWN_TO_REFRESH:
		default:
			originalLoadingLayout = getHeaderLayout();
			listViewLoadingLayout = mHeaderLoadingView;
			selection = 0;
			scrollToY = getScrollY() + getHeaderHeight();
			break;
		}
		if (doScroll)
		{
			// We scroll slightly so that the ListView's header/footer is at the
			// same Y position as our normal header/footer
			setHeaderScroll(scrollToY);
		}
		// 还有更多数据
		if (originalFooterLoadingLayout != null && originalFooterLoadingLayout.HasMoreData)
		{
			// Set our Original View to Visible
			if (originalLoadingLayout != null)
			{
				originalLoadingLayout.setVisibility(View.INVISIBLE);
			} else
			{
				originalFooterLoadingLayout.setVisibility(View.INVISIBLE);
			}
			// Hide the ListView Header/Footer
			if (listViewLoadingLayout != null)
			{
				listViewLoadingLayout.setVisibility(View.VISIBLE);
				mListViewHasShow = true;
				listViewLoadingLayout.refreshing();
			} else
			{
				listViewFooterLoadingLayout.setVisibility(View.VISIBLE);
				listViewFooterLoadingLayout.refreshing();
			}
		} else
		{
			// 无更多数据 Set our Original View to Visible
			if (originalLoadingLayout != null)
			{
				originalLoadingLayout.setVisibility(View.INVISIBLE);
			} else
			{
				// 显示父类的FooterLoadingLayout
				originalFooterLoadingLayout.setVisibility(View.VISIBLE);
			}
			// Hide the ListView Header/Footer
			if (listViewLoadingLayout != null)
			{
				mListViewHasShow = true;
				listViewLoadingLayout.setVisibility(View.VISIBLE);
				listViewLoadingLayout.refreshing();
			} else
			{
				// 隐藏 listViewFooter 用父类的FooterLoadingLayout
				listViewFooterLoadingLayout.setVisibility(View.GONE);
				originalFooterLoadingLayout.refreshing();
			}
		}
		if (doScroll)
		{
			// Make sure the ListView is scrolled to show the loading
			// header/footer
			mRefreshableView.setSelection(selection);
			// Smooth scroll as normal
			smoothScrollTo(0);
		}
	}

	@Override
	protected void setHeaderVisible()
	{

		if (mIsFisrtHeaderShowing || getItemCount() <= 0)
		{
			super.setHeaderVisible();
		} else
		{
			// 列表有数据时
			// 解决重复调用显示头部刷新，头部布局不随listview滚动问题
			HeaderLoadingLayout originalLoadingLayout = null;
			HeaderLoadingLayout listViewLoadingLayout = null;
			originalLoadingLayout = getHeaderLayout();
			listViewLoadingLayout = mHeaderLoadingView;
			// Scroll 到顶部 显示为listview的headerview
			setHeaderScroll(0);
			// Set our Original View to Visible
			if (originalLoadingLayout != null)
			{
				originalLoadingLayout.setVisibility(View.INVISIBLE);
			}
			// 显示lisetview headerview
			if (listViewLoadingLayout != null)
			{
				mListViewHasShow = true;
				listViewLoadingLayout.setVisibility(View.VISIBLE);
				listViewLoadingLayout.refreshing();
			}
		}
	}

	/**
	 * setDragViewId设置可支持的拖拽view 资源id,在setAdpater前面使用，不然无效
	 * 
	 * @param dragViewId
	 */
	public void setDragViewId(int dragViewId)
	{
		this.mDragViewId = dragViewId;
	}

	class InternalListView extends DragListView implements EmptyViewMethodAccessor
	{

		private int mDefaultXDistance;
		private int mDefaultYDistance;
		private boolean mAddedLvFooter;
		private int mStartY;
		private int mStartX;
		private float mRate = 1;

		@SuppressWarnings("static-access")
		public InternalListView(Context context, AttributeSet attrs)
		{
			super(context, attrs);
			ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
			mDefaultXDistance = viewConfiguration.getMinimumFlingVelocity();
			mDefaultYDistance = mDefaultXDistance * 4;

			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

			int width = wm.getDefaultDisplay().getWidth();
			mDefaultXDistance = width / 4;
		}

		@Override
		public boolean onInterceptTouchEvent(MotionEvent ev)
		{
			switch (ev.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				mStartX = (int) ev.getX();
				mStartY = (int) ev.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				int stopX = (int) ev.getX();
				int stopY = (int) ev.getY();
				// 若根据自定义规则判断当前是水平滑动，则将事件交给下层控件处理
				// 非拖曳时
				if (isMovingHorizontal(mStartX, mStartY, stopX, stopY))
				{
					return false;
				}
			default:
				break;
			}
			return super.onInterceptTouchEvent(ev);
		}

		private boolean isMovingHorizontal(int startX, int startY, int stopX, int stopY)
		{
			int xDiff = stopX - startX;
			int yDiff = Math.abs(stopY - startY);
			boolean conditionOne = xDiff >= mDefaultXDistance && yDiff < mDefaultYDistance;
			boolean conditionTwo = yDiff >= mDefaultYDistance && xDiff < -mDefaultXDistance && Math.abs(xDiff) / yDiff > mRate;
			if (conditionOne)
			{
				return true;
			}
			return false;
		}

		@Override
		public void draw(Canvas canvas)
		{
			/**
			 * This is a bit hacky, but ListView has got a bug in it when using
			 * Header/Footer Views and the list is empty. This masks the issue
			 * so that it doesn't cause an FC. See Issue #66.
			 */
			try
			{
				super.draw(canvas);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		@Override
		public ContextMenuInfo getContextMenuInfo()
		{
			return super.getContextMenuInfo();
		}

		@Override
		public void setAdapter(ListAdapter adapter)
		{

			if (mDragViewId != -1)
			{
				setDragViewId(mDragViewId);
			}
			if (adapter instanceof PageListViewAdapter)
			{
				// 设置记录转换前的adapter
				setPageListViewAdapter((PageListViewAdapter) adapter);
			}

			// Add the Footer View at the last possible moment
			if (!mAddedLvFooter)
			{
				addFooterView(mLvFooterLoadingFrame, null, false);
				mAddedLvFooter = true;
			}
			if (mIsShowHeaderFresh)
			{
				setHeaderVisible(false);
			}
			super.setAdapter(adapter);
		}

		@Override
		public void setEmptyView(View emptyView)
		{
			PullToRefreshListView.this.setEmptyView(emptyView);
		}

		@Override
		public void setEmptyViewInternal(View emptyView)
		{
			super.setEmptyView(emptyView);
		}
	}

}
