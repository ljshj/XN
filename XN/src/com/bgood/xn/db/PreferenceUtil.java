﻿package com.bgood.xn.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.bgood.xn.bean.UserBean;

public class PreferenceUtil {
	
	public static final  String PREFERENCE_FILE = "tb_preference";
	
	private final SharedPreferences sp;
	private final SharedPreferences.Editor editor;

	@SuppressLint("CommitPrefEdits")
	public PreferenceUtil(Context context, String file) {
		sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
		editor = sp.edit();
	}
	
	/**设置是否展示微墙页**/
	public void setShowWelcomePage(boolean isShow) {
		editor.putBoolean("welcome_show", isShow);
		editor.commit();
	}

	public boolean getShowWelcomePage() {
		return sp.getBoolean("welcome_show", false);
	}

	
	/**设置微墙中所有微墙刷新时间**/
	public void setWeiqiangAllRefreshTime(String refreshTime) {
		editor.putString("weiqiang_all", refreshTime);
		editor.commit();
	}

	public String getWeiqiangAllRefreshTime() {
		return sp.getString("weiqiang_all", "");
	}
	
	/**设置微墙中关注的微墙刷新时间**/
	public void setWeiqiangAttionRefreshTime(String refreshTime) {
		editor.putString("weiqiang_attion", refreshTime);
		editor.commit();
	}

	public String getWeiqiangAttionRefreshTime() {
		return sp.getString("weiqiang_attion", "");
	}
	
	/**设置微墙中关注的微墙刷新时间**/
	public void setWeiqiangDetailRefreshTime(String refreshTime) {
		editor.putString("weiqiang_detail", refreshTime);
		editor.commit();
	}

	public String getWeiqiangDetailRefreshTime() {
		return sp.getString("weiqiang_detail", "");
	}
	
}
