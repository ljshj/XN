package com.bgood.xn.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.bgood.xn.bean.FriendBean;
import com.bgood.xn.bean.GroupBean;
import com.bgood.xn.bean.MemberLoginBean;
import com.bgood.xn.bean.UserInfoBean;
import com.easemob.EMCallBack;
import com.easemob.chat.ChatHXSDKHelper;
import com.iflytek.cloud.SpeechUtility;

public class BGApp extends Application {
	
	public static boolean isUserLogin = false;
	
	/**存储用户登录信息*/
	public static MemberLoginBean mLoginBean;
	
	/**存放用户资料*/
	public static UserInfoBean mUserBean;
	
	/**存放用户编号*/
	public static String mUserId = "";
	
	
	public static Context applicationContext;
	private static BGApp instance;
	// login user name
	public final String PREF_USERNAME = "username";
	
	/**
	 * 当前用户nickname,为了苹果推送不是userid而是昵称
	 */
	public static String currentUserNick = "";
	public static ChatHXSDKHelper hxSDKHelper = new ChatHXSDKHelper();
	
	/**存放好友列表*/
	public Map<String, FriendBean> friendMap = new HashMap<String, FriendBean>();
	/**存放好友列表*/
	public Map<String, GroupBean> groupMap = new HashMap<String, GroupBean>();
	/**存放好友列表*/
	public Map<String, GroupBean> tempMap = new HashMap<String, GroupBean>();
	

	@Override
	public void onCreate()
	{
		super.onCreate();
		//语音使用
		SpeechUtility.createUtility(this, "appid=53aa7931");
		
		/*****************************环信******************************/
        applicationContext = this;
        instance = this;

        /**
         * this function will initialize the HuanXin SDK
         * 
         * @return boolean true if caller can continue to call HuanXin related APIs after calling onInit, otherwise false.
         * 
         * 环信初始化SDK帮助函数
         * 返回true如果正确初始化，否则false，如果返回为false，请在后续的调用中不要调用任何和环信相关的代码
         * 
         * for example:
         * 例子：
         * 
         * public class DemoHXSDKHelper extends HXSDKHelper
         * 
         * HXHelper = new DemoHXSDKHelper();
         * if(HXHelper.onInit(context)){
         *     // do HuanXin related work
         * }
         */
        hxSDKHelper.onInit(applicationContext);
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
    
    /********************************环信配置*************************************/
    

	public static BGApp getInstance() {
		return instance;
	}

	public Map<String, FriendBean> getFriendMap() {
		return friendMap;
	}

	public void setFriendMap(Map<String, FriendBean> friendMap) {
		this.friendMap = friendMap;
	}

	public Map<String, GroupBean> getGroupMap() {
		return groupMap;
	}

	public void setGroupMap(Map<String, GroupBean> groupMap) {
		this.groupMap = groupMap;
	}

	public Map<String, GroupBean> getTempMap() {
		return tempMap;
	}

	public void setTempMap(Map<String, GroupBean> tempMap) {
		this.tempMap = tempMap;
	}

	/**
	 * 获取当前登陆用户名
	 *
	 * @return
	 */
	public String getUserName() {
	    return hxSDKHelper.getHXId();
	}

	/**
	 * 获取密码
	 *
	 * @return
	 */
	public String getPassword() {
		return hxSDKHelper.getPassword();
	}

	/**
	 * 设置用户名
	 *
	 * @param user
	 */
	public void setUserName(String username) {
	    hxSDKHelper.setHXId(username);
	}

	/**
	 * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
	 * 内部的自动登录需要的密码，已经加密存储了
	 *
	 * @param pwd
	 */
	public void setPassword(String pwd) {
	    hxSDKHelper.setPassword(pwd);
	}

	/**
	 * 退出登录,清空数据
	 */
	public void logout(final EMCallBack emCallBack) {
		// 先调用sdk logout，在清理app中自己的数据
	    hxSDKHelper.logout(emCallBack);
	}
	
	/**是否登录聊天服务器*/
	public boolean isLogin(){
		return hxSDKHelper.isLogined();
	}
}
