﻿package com.bgood.xn.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

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
	
	/**设置用户名*/
	public void setAccountNumber(String accountNumber){
		editor.putString("user_account_number", accountNumber);
		editor.commit();
	}
	
	public String getAccountNumber(){
		return sp.getString("user_account_number", "");
	}
	
	/**设置密码*/
	public void setAccountPassword(String accountPassword){
		editor.putString("user_account_password", accountPassword);
		editor.commit();
	}
	
	public String getAccountPassword(){
		return sp.getString("user_account_password", "");
	}
	
	/**设置意见反馈的刷新时间**/
	public void setFeedbackRefreshTime(String refreshTime) {
		editor.putString("Feedback", refreshTime);
		editor.commit();
	}

	public String getFeedbackRefreshTime() {
		return sp.getString("Feedback", "");
	}
	
	/**设置个性化模块**/
	public void setSelfTemplat(int indexTemplat) {
		editor.putInt("templat", indexTemplat);
		editor.commit();
	}

	public int getSelfTemplat() {
		return sp.getInt("templat", 0);
	}
	
	/**设置产品评论刷新时间**/
	public void setProductCommentRefreshTime(String productCommentRefreshTime){
		editor.putString("productCommentRefreshTime", productCommentRefreshTime);
		editor.commit();
	}
	
	public String getProductCommentRefreshTime(){
		return sp.getString("productCommentRefreshTime", "");
	}
	
	/**清除搜索历史记录**/
	public void clearSearchHistory(int type) {
		editor.putString(type == 1?"search_i_can":"search_i_think", null);
		editor.commit();
	}
	
	/**保存查询记录*/
	public void saveSearchHistory(int type,String searchText) {
		String localHistory = getSearchHistory(type);
		StringBuilder sb = new StringBuilder();
		
		if(null!=localHistory){
			// 只保留最近的10条的记录
			String[] hisArrays = localHistory.split(",");
			String[] newArrays = null;
			if (hisArrays.length > 10)
			{
				newArrays = new String[10];
				System.arraycopy(hisArrays, 0, newArrays, 0, 10);
			} else
			{
				newArrays = hisArrays;
			}
			for(String s:newArrays){
				sb.append(s).append(",");
			}
			
			if(!localHistory.contains(searchText+",")){
				sb.insert(0, searchText + ",");
				editor.putString(type == 1?"search_i_can":"search_i_think", sb.toString());
				editor.commit();
			}
		}else{
			sb.insert(0, searchText + ",");
			editor.putString(type == 1?"search_i_can":"search_i_think", sb.toString());
			editor.commit();
		}
	}
	
	/**获取我想与我能**/
	public String getSearchHistory(int type) {
		return sp.getString(type == 1?"search_i_can":"search_i_think", "");
	}
	
	
	
	
	
	
	
	
	
	/*************************************幽默秀***********************************************/
	
	
	/**设置幽默秀刷新时间**/
	public void setJokeDetailRefreshTime(String refreshTime) {
		editor.putString("joke_detail", refreshTime);
		editor.commit();
	}

	public String getJokeDetailRefreshTime() {
		return sp.getString("weiqiang_detail", "");
	}
	
	
	
}
