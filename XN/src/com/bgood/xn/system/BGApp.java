package com.bgood.xn.system;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

import com.iflytek.cloud.SpeechUtility;

/**
 * @todo:TODO
 * @date:2014-10-22 下午3:29:45
 * @author:hg_liuzl@163.com
 */
public class BGApp extends Application {
	public static double dp;
	public static BGApp Appself;
	public static final int WHAT_NEWMESSAGE = 0xff223;
	
	private List<Activity> activityList=new LinkedList<Activity>();
   private static BGApp instance;
   
   private BGApp(){
	   
   }
   public static BGApp getInstance(){
	   if (instance==null) {
		instance=new BGApp();
	}
	   return instance;
   }
   public void addActivity(Activity activity){
	   activityList.add(activity);
   }
   public void exit(){
	   for(Activity activity:activityList){
		     if(!activity.isFinishing()){
		    	  activity.finish();
		     }
	   }
	   int id=android.os.Process.myPid();
	   if(id!=0){
		   android.os.Process
			.killProcess(id);
	   }
   }


	@Override
	public void onCreate()
	{
		super.onCreate();
		//语音使用
		SpeechUtility.createUtility(this, "appid=53aa7931");
	}
}
