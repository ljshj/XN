package com.bgood.xn.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;

import com.bgood.xn.db.DBHelper;
import com.bgood.xn.db.PreferenceUtil;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.user.account.LoginActivity;
/**
 * 
 * @todo:Activity基类
 * @date:2014-12-25 上午11:05:00
 * @author:hg_liuzl@163.com
 */
public class BaseActivity extends FragmentActivity {
	public LayoutInflater inflater = null;
	public Activity mActivity = null;
	protected InputMethodManager im = null;
	public static final int PAGE_SIZE_ADD = 20;
	public PreferenceUtil pUitl;
	public DBHelper dbHelper = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mActivity = this;
		BGApp.getInstance().addActivity(this);
		inflater = LayoutInflater.from(this);
		im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		pUitl = new PreferenceUtil(this, PreferenceUtil.PREFERENCE_FILE);
		dbHelper = new DBHelper(mActivity);
	}
	
	
	public void toActivity(Class<?> target, Bundle bundle)
	{
		Intent intent = new Intent(this, target);
		if (bundle != null)
			intent.putExtras(bundle);
		startActivity(intent);
	}

	public void toActivity(String action, Class<?> target, Bundle bundle)
	{
		Intent intent = new Intent(this, target);
		if (bundle != null)
			intent.putExtras(bundle);
		startActivity(intent);
	}

	public void toActivity(Class<?> target)
	{
		Intent intent = new Intent(this, target);
		startActivity(intent);
	}
	
	public void hideSoftInputFromWindow()
	{
		if (getCurrentFocus() == null){
				return;
		}
		
		im.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.SHOW_FORCED);
	}
	/**
	 * 
	 * @todo:判断用户是否登录了
	 * @date:2014-10-29 上午9:36:15
	 * @author:hg_liuzl@163.com
	 * @params:
	 */
	public void judgeLogin(){
		if(!BGApp.isUserLogin){
			Intent intent = new Intent(mActivity, LoginActivity.class);
			mActivity.startActivity(intent);
			return;
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
}
