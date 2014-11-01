package com.bgood.xn.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;

import com.bgood.xn.db.PreferenceUtil;


public class BaseActivity extends Activity {
	
	public LayoutInflater inflater = null;
	public Activity mActivity = null;
	protected InputMethodManager im = null;
	public static final int PAGE_SIZE_ADD = 50;
	public PreferenceUtil pUitl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflater = LayoutInflater.from(this);
		im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		pUitl = new PreferenceUtil(this, PreferenceUtil.PREFERENCE_FILE);
		this.mActivity = this;
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

}
