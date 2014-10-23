package com.bgood.xn.ui;import java.util.ArrayList;import java.util.List;import android.os.Bundle;import android.support.v4.app.Fragment;import android.support.v4.app.FragmentActivity;import android.support.v4.app.FragmentManager;import android.support.v4.app.FragmentPagerAdapter;import android.support.v4.app.FragmentTabHost;import android.support.v4.view.ViewPager;import android.support.v4.view.ViewPager.OnPageChangeListener;import android.util.DisplayMetrics;import android.widget.RadioButton;import android.widget.RadioGroup;import android.widget.RadioGroup.OnCheckedChangeListener;import android.widget.TabHost.OnTabChangeListener;import android.widget.TabHost.TabSpec;import com.bgood.xn.R;import com.bgood.xn.ui.home.HomeFragment;import com.bgood.xn.ui.message.MessageFragment;import com.bgood.xn.ui.user.UserCenterFragment;import com.bgood.xn.ui.weiqiang.WeiqiangFragment;import com.bgood.xn.ui.xuanneng.XuanNengFragment;public class MainActivity extends FragmentActivity {	private RadioGroup rg;	private RadioButton homeBtn,msgBtn,weiqiangBtn,xuanBtn,userBtn;	private FragmentTabHost mFragmentTabhost;	public static final String SHOW_OF_FIRST_TAG = "first";	public static final String SHOW_OF_SECOND_TAG = "second";	public static final String SHOW_OF_THIRD_TAG = "third";	public static final String SHOW_OF_FOUR_TAG = "four";	public static final String SHOW_OF_FIVE_TAG = "five";	private int SCREEN_WIDTH;	private List<Fragment> list = new ArrayList<Fragment>();	private ViewPager mViewPager;	@Override	protected void onCreate(Bundle savedInstanceState) {		super.onCreate(savedInstanceState);		DisplayMetrics metrics = new DisplayMetrics();		getWindowManager().getDefaultDisplay().getMetrics(metrics);		SCREEN_WIDTH = metrics.widthPixels;		setContentView(R.layout.activity_main);		mFragmentTabhost = (FragmentTabHost) findViewById(android.R.id.tabhost);		rg = (RadioGroup) findViewById(R.id.main_tab_group);		homeBtn = (RadioButton) findViewById(R.id.main_tab_home);		msgBtn = (RadioButton) findViewById(R.id.main_tab_communication);		weiqiangBtn = (RadioButton) findViewById(R.id.main_tab_weiqiang);		xuanBtn = (RadioButton) findViewById(R.id.main_tab_xuanneng);		userBtn = (RadioButton) findViewById(R.id.main_tab_me);		mViewPager = (ViewPager) findViewById(R.id.pager);		mFragmentTabhost.setup(this, getSupportFragmentManager(), R.id.pager);		TabSpec tabSpec0 = mFragmentTabhost.newTabSpec(SHOW_OF_FIRST_TAG).setIndicator("0");		TabSpec tabSpec1 = mFragmentTabhost.newTabSpec(SHOW_OF_SECOND_TAG).setIndicator("1");		TabSpec tabSpec2 = mFragmentTabhost.newTabSpec(SHOW_OF_THIRD_TAG).setIndicator("2");		TabSpec tabSpec3 = mFragmentTabhost.newTabSpec(SHOW_OF_FOUR_TAG).setIndicator("3");		TabSpec tabSpec4 = mFragmentTabhost.newTabSpec(SHOW_OF_FIVE_TAG).setIndicator("4");		mFragmentTabhost.addTab(tabSpec0, HomeFragment.class, null);		mFragmentTabhost.addTab(tabSpec1, MessageFragment.class, null);		mFragmentTabhost.addTab(tabSpec2, WeiqiangFragment.class, null);		mFragmentTabhost.addTab(tabSpec3, XuanNengFragment.class, null);		mFragmentTabhost.addTab(tabSpec4, UserCenterFragment.class, null);		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {			@Override			public void onCheckedChanged(RadioGroup group, int checkedId) {				// TODO Auto-generated method stub				switch (checkedId) {				case R.id.main_tab_home:					mFragmentTabhost.setCurrentTabByTag(SHOW_OF_FIRST_TAG);					break;				case R.id.main_tab_communication:					mFragmentTabhost.setCurrentTabByTag(SHOW_OF_SECOND_TAG);					break;				case R.id.main_tab_weiqiang:					mFragmentTabhost.setCurrentTabByTag(SHOW_OF_THIRD_TAG);					break;				case R.id.main_tab_xuanneng:					mFragmentTabhost.setCurrentTabByTag(SHOW_OF_FOUR_TAG);					break;				case R.id.main_tab_me:					mFragmentTabhost.setCurrentTabByTag(SHOW_OF_FIVE_TAG);					break;				default:					break;				}			}		});		mFragmentTabhost.setOnTabChangedListener(new OnTabChangeListener() {			@Override			public void onTabChanged(String tabId) {				int position = mFragmentTabhost.getCurrentTab();				mViewPager.setCurrentItem(position);			}		});		mFragmentTabhost.setCurrentTab(0);		HomeFragment p1 = new HomeFragment();		MessageFragment p2 = new MessageFragment();		WeiqiangFragment p3 = new WeiqiangFragment();		XuanNengFragment p4 = new XuanNengFragment();		UserCenterFragment p5 = new UserCenterFragment();		list.add(p1);		list.add(p2);		list.add(p3);		list.add(p4);		list.add(p5);		mViewPager.setAdapter(new MenuAdapter(getSupportFragmentManager()));		mViewPager.setOnPageChangeListener(new ViewPagerListener());	}	class MenuAdapter extends FragmentPagerAdapter {		public MenuAdapter(FragmentManager fm) {			super(fm);		}		@Override		public Fragment getItem(int arg0) {			return list.get(arg0);		}		@Override		public int getCount() {			return list.size();		}	}	class ViewPagerListener implements OnPageChangeListener {		@Override		public void onPageScrollStateChanged(int arg0) {		}		@Override		public void onPageScrolled(int arg0, float arg1, int arg2) {		}		@Override		public void onPageSelected(int index) {			if (index == 0) {				homeBtn.setChecked(true);			} else if (index == 1) {				msgBtn.setChecked(true);			} else if (index == 2) {				weiqiangBtn.setChecked(true);			}else if (index == 3) {				xuanBtn.setChecked(true);			}else if (index == 4) {				userBtn.setChecked(true);			}			mFragmentTabhost.setCurrentTab(index);		}	}}