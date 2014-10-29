package com.bgood.xn.ui.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.bgood.xn.R;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.ui.MainActivity;

/**
 * 
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
			R.drawable.help_04,
	};
	private boolean from_index = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_navigate);
		
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		viewpager.setCurrentItem(0);
		
		adapter = new NavigatePagerAdapter(this, ids, mOnClickListener);
		viewpager.setAdapter(adapter);
		
		from_index = getIntent().getBooleanExtra("from_index", false);
		
	}
	
	OnClickListener mOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int id = v.getId();
			switch (id) {
			case R.id.start_btn:
				pUitl.setShowWelcomePage(true);
				if(from_index){
					gotoMain();
				}else{
					finish();
				}
				break;
			default:
				break;
			}
			
		}
	};
	
	/**
	 * 进入首页
	 */
	private void gotoMain(){
		Intent i = new Intent();
		i.setClass(NavigateActivity.this, MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(i);
		finish();
	}
}
