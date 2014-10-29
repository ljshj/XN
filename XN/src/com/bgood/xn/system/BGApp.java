package com.bgood.xn.system;

import android.app.Application;

import com.bgood.xn.bean.MemberLoginBean;
import com.bgood.xn.bean.UserBean;
import com.iflytek.cloud.SpeechUtility;

public class BGApp extends Application {
	
	public static boolean isUserLogin = false;
	
	/**存储用户登录信息*/
	public static MemberLoginBean mLoginBean;
	
	/**存放用户资料*/
	public static UserBean mUserBean;
	

	@Override
	public void onCreate()
	{
		super.onCreate();
		//语音使用
		SpeechUtility.createUtility(this, "appid=53aa7931");
	}
}
