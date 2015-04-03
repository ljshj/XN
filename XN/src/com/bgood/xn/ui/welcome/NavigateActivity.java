package com.bgood.xn.ui.welcome;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import com.bgood.xn.R;
import com.bgood.xn.ui.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;
/**
 * @todo:欢迎页
 * @date:2014-10-28 下午6:44:10
 * @author:hg_liuzl@163.com
 */
public class NavigateActivity extends BaseActivity {
	private ViewPager viewpager;
	private NavigatePagerAdapter adapter;
	private int[] ids = {
			R.drawable.help_01,
			R.drawable.help_02,
			R.drawable.help_03,
			R.drawable.help_04
	};
	
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_navigate);
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		viewpager.setCurrentItem(0);
		adapter = new NavigatePagerAdapter(this, ids,load);
		viewpager.setAdapter(adapter);
	}
	
	/**
	 * 向导完成再进入indexActivity中
	 */
	private ILoadCommpletListener load = new ILoadCommpletListener(){
		@Override
		public void loadCommplet() {
			pUitl.setShowWelcomePage(true);	
			setResult(RESULT_OK);
			NavigateActivity.this.finish();
		}
	};
}




