//package com.bgood.xn.ui.xuanneng;
//
//import java.util.ArrayList;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.util.AttributeSet;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TabHost;
//import android.widget.TabWidget;
//
//import com.bgood.xn.R;
//
///**
// * 
// * @todo:TODO
// * @date:2014-11-21 下午5:49:48
// * @author:hg_liuzl@163.com
// */
//public class XuannengRankingActivity extends FragmentActivity
//{
//	TabHost mTabHost;
//	ViewPager mViewPager;
//	TabsAdapter mTabsAdapter;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.layout_xuanneng_humor_ranking);
//		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
//		mTabHost.setup();
//
//		mViewPager = (ViewPager) findViewById(R.id.pager);
//		mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
//		Bundle bundle_day = new Bundle();
//		bundle_day.putInt("rankingtype", RankingFragment.RANKING_TYPE_DAY);
//		mTabsAdapter.addTab(mTabHost.newTabSpec("日排行").setIndicator("日排行"), RankingFragment.class, bundle_day);
//		Bundle bundle_week = new Bundle();
//		bundle_week.putInt("rankingtype", RankingFragment.RANKING_TYPE_WEEK);
//		mTabsAdapter.addTab(mTabHost.newTabSpec("周排行").setIndicator("周排行"), RankingFragment.class, bundle_week);
//		Bundle bundle_month = new Bundle();
//		bundle_month.putInt("rankingtype", RankingFragment.RANKING_TYPE_MONTH);
//		mTabsAdapter.addTab(mTabHost.newTabSpec("月排行").setIndicator("月排行"), RankingFragment.class, bundle_month);
//		Bundle bundle_year = new Bundle();
//		bundle_year.putInt("rankingtype", RankingFragment.RANKING_TYPE_YEAR);
//		mTabsAdapter.addTab(mTabHost.newTabSpec("年排行").setIndicator("年排行"), RankingFragment.class, bundle_year);
//		if (savedInstanceState != null)
//		{
//			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
//		}
//	}
//	
//	
//
//	@Override
//    public View onCreateView(String name, Context context, AttributeSet attrs)
//    {
//        return super.onCreateView(name, context, attrs);
//    }
//
//
//
//    @Override
//	protected void onSaveInstanceState(Bundle outState)
//	{
//		super.onSaveInstanceState(outState);
//		outState.putString("tab", mTabHost.getCurrentTabTag());
//	}
//
//	/**
//	 * This is a helper class that implements the management of tabs and all
//	 * details of connecting a ViewPager with associated TabHost. It relies on a
//	 * trick. Normally a tab host has a simple API for supplying a View or
//	 * Intent that each tab will show. This is not sufficient for switching
//	 * between pages. So instead we make the content part of the tab host 0dp
//	 * high (it is not shown) and the TabsAdapter supplies its own dummy view to
//	 * show as the tab content. It listens to changes in tabs, and takes care of
//	 * switch to the correct paged in the ViewPager whenever the selected tab
//	 * changes.
//	 */
//	public static class TabsAdapter extends FragmentPagerAdapter implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener
//	{
//		private final Context mContext;
//		private final TabHost mTabHost;
//		private final ViewPager mViewPager;
//		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
//
//		static final class TabInfo
//		{
//			private final String tag;
//			private final Class<?> clss;
//			private final Bundle args;
//
//			TabInfo(String _tag, Class<?> _class, Bundle _args)
//			{
//				tag = _tag;
//				clss = _class;
//				args = _args;
//			}
//		}
//
//		static class DummyTabFactory implements TabHost.TabContentFactory
//		{
//			private final Context mContext;
//
//			public DummyTabFactory(Context context)
//			{
//				mContext = context;
//			}
//
//			@Override
//			public View createTabContent(String tag)
//			{
//				View v = new View(mContext);
//				v.setMinimumWidth(0);
//				v.setMinimumHeight(0);
//				return v;
//			}
//		}
//
//		public TabsAdapter(FragmentActivity activity, TabHost tabHost, ViewPager pager)
//		{
//			super(activity.getSupportFragmentManager());
//			mContext = activity;
//			mTabHost = tabHost;
//			mViewPager = pager;
//			mTabHost.setOnTabChangedListener(this);
//			mViewPager.setAdapter(this);
//			mViewPager.setOnPageChangeListener(this);
//		}
//
//		public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args)
//		{
//			tabSpec.setContent(new DummyTabFactory(mContext));
//			String tag = tabSpec.getTag();
//
//			TabInfo info = new TabInfo(tag, clss, args);
//			mTabs.add(info);
//			mTabHost.addTab(tabSpec);
//			notifyDataSetChanged();
//		}
//
//		@Override
//		public int getCount()
//		{
//			return mTabs.size();
//		}
//
//		@Override
//		public Fragment getItem(int position)
//		{
//			TabInfo info = mTabs.get(position);
//			return Fragment.instantiate(mContext, info.clss.getName(), info.args);
//		}
//
//		@Override
//		public void onTabChanged(String tabId)
//		{
//			int position = mTabHost.getCurrentTab();
//			mViewPager.setCurrentItem(position);
//		}
//
//		@Override
//		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
//		{
//		}
//
//		@Override
//		public void onPageSelected(int position)
//		{
//			// Unfortunately when TabHost changes the current tab, it kindly
//			// also takes care of putting focus on it when not in touch mode.
//			// The jerk.
//			// This hack tries to prevent this from pulling focus out of our
//			// ViewPager.
//			TabWidget widget = mTabHost.getTabWidget();
//			int oldFocusability = widget.getDescendantFocusability();
//			widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
//			mTabHost.setCurrentTab(position);
//			widget.setDescendantFocusability(oldFocusability);
//		}
//
//		@Override
//		public void onPageScrollStateChanged(int state)
//		{
//		}
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object)
//        {
//            super.destroyItem(container, position, object);
//            
//        }
//		
//		
//	}
//}
