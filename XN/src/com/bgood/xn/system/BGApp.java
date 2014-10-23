package com.bgood.xn.system;

import android.app.Application;

import com.iflytek.cloud.SpeechUtility;

public class BGApp extends Application {

	@Override
	public void onCreate()
	{
		super.onCreate();
		//语音使用
		SpeechUtility.createUtility(this, "appid=53aa7931");
	}
}
