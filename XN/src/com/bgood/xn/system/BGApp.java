package com.bgood.xn.system;

import java.util.Stack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;

import com.bgood.xn.bean.MemberLoginBean;
import com.bgood.xn.bean.UserInfoBean;
import com.iflytek.cloud.SpeechUtility;

public class BGApp extends Application {
	
	public static boolean isUserLogin = false;
	
	/**存储用户登录信息*/
	public static MemberLoginBean mLoginBean;
	
	/**存放用户资料*/
	public static UserInfoBean mUserBean;
	

	@Override
	public void onCreate()
	{
		super.onCreate();
		//语音使用
		SpeechUtility.createUtility(this, "appid=53aa7931");
	}
	
	
	 private static Stack<Activity> m_activityStack; // activity栈

    /**
     * 添加Activity到堆栈
     * @param Activity 需要加入的Activity
     * @return 
     */
    public static void addActivity(Activity activity)
    {
        if (m_activityStack == null)
        {
            m_activityStack = new Stack<Activity>();
        }
        m_activityStack.add(activity);
    }

    /**
     * 结束所有Activity
     * @param 
     * @return 
     */
    @SuppressLint("NewApi")
	public static void finishAllActivity()
    {
        for (int i = 0, size = m_activityStack.size(); i < size; i++)
        {
        	Activity acitivty = m_activityStack.get(i);
        	
            if (null != acitivty && !acitivty.isDestroyed())
            {
                m_activityStack.get(i).finish();
            }
        }
        m_activityStack.clear();
    }
}
