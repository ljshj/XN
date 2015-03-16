package com.bgood.xn.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.bgood.xn.R;
import com.bgood.xn.ui.user.product.ShowcaseRightFragment;
import com.bgood.xn.view.slidingmenu.lib.SlidingMenu;
import com.bgood.xn.view.slidingmenu.lib.app.SlidingFragmentActivity;
import com.umeng.analytics.MobclickAgent;

public class CBaseSlidingMenu extends SlidingFragmentActivity
{
	public SlidingMenu m_slidingMenu; // 侧边栏菜单
	private ShowcaseRightFragment m_rightFragment; // 右侧菜单Fragment
	protected Fragment mFrag;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initSlidingMenu();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	/**
	 * 初始化侧边栏菜单
	 */
	private void initSlidingMenu()
	{

		m_rightFragment = new ShowcaseRightFragment();// 创建右边菜单Fragment

		m_slidingMenu = getSlidingMenu(); // 由于Activity继承自SlidingFragmentActivity,所以直接获取当前的侧边栏菜单

		m_slidingMenu.setMode(SlidingMenu.RIGHT); // 设置侧边栏菜单为左右模式
		m_slidingMenu.setRightBehindWidth(getWindowManager().getDefaultDisplay().getWidth() / 2);
		// m_slidingMenu.setRightMenuOffset(getWindowManager().getDefaultDisplay().getWidth()
		// / 2);
		m_slidingMenu.setShadowDrawable(R.drawable.shadow); // 设置左菜单的阴影
		m_slidingMenu.setShadowWidth(5); // 设置阴影宽度
		m_slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN); // 设置侧边栏菜单为边缘打开
		// m_slidingMenu.setBehindWidth(getWindowManager().getDefaultDisplay()
		// .getWidth() / 2);
		m_slidingMenu.setFadeDegree(0.7f);// SlidingMenu滑动时的渐变程度
		m_slidingMenu.setFadeEnabled(false);
		setBehindContentView(R.layout.right_menu_layout); // 设置左菜单的默认VIEW
		getSupportFragmentManager().beginTransaction().replace(R.id.rightMenu, m_rightFragment).commit(); // 将左菜单默认VIEW替换为左菜单Fragment

		/*
		 * setBehindContentView(R.layout.menu_frame); FragmentTransaction t =
		 * this.getSupportFragmentManager().beginTransaction(); mFrag = new
		 * MenuFragment(); t.replace(R.id.menu_frame, mFrag); t.commit();
		 * 
		 * m_rightFragment = new RightFragment();// 创建右边菜单Fragment
		 * 
		 * m_slidingMenu = getSlidingMenu(); //
		 * 由于Activity继承自SlidingFragmentActivity,所以直接获取当前的侧边栏菜单
		 * 
		 * m_slidingMenu = getSlidingMenu();
		 * m_slidingMenu.setMode(SlidingMenu.RIGHT);
		 * m_slidingMenu.setBehindWidthRes(R.dimen.right_menu_width); //
		 * 设置左边菜单的宽度,该值为左菜单展开的宽度 m_slidingMenu.setShadowWidth(10);
		 * m_slidingMenu.setShadowDrawable(R.drawable.shadow);
		 * m_slidingMenu.setSecondaryShadowDrawable(R.drawable.right_shadow); //
		 * m_slidingMenu
		 * .setBehindOffset(getWindowManager().getDefaultDisplay().getWidth() /
		 * 6); m_slidingMenu.setFadeEnabled(true);
		 * m_slidingMenu.setFadeDegree(0.5f); //
		 * m_slidingMenu.setBehindScrollScale(0); //
		 * m_slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		 */
	}
}
