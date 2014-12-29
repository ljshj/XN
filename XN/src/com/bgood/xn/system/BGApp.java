package com.bgood.xn.system;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.bgood.xn.bean.FriendBean;
import com.bgood.xn.bean.GroupBean;
import com.bgood.xn.bean.MemberLoginBean;
import com.bgood.xn.bean.UserInfoBean;
import com.easemob.EMCallBack;
import com.easemob.chat.ChatHXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.activity.ChatActivity;
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
	
	/**存放好友列表 （好友名字,好友实体类）*/
	private Map<String, FriendBean> friendMapByName = new HashMap<String, FriendBean>();
	
	/**存放好友列表 （好友ID,好友实体类）*/
	private Map<String, FriendBean> friendMapById = new HashMap<String, FriendBean>();
	
	/**存放固定群列表   （群ID, 群实体类）*/
	private Map<String, GroupBean> groupMap = new HashMap<String, GroupBean>();
	
	/**存放临时群列表   （群ID, 群实体类）*/
	private Map<String, GroupBean> tempMap = new HashMap<String, GroupBean>();
	

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
        registerReciverMsg();
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

	public Map<String, FriendBean> getFriendMapByName() {
		return friendMapByName;
	}

	public void setFriendMapByName(Map<String, FriendBean> friendMapByName) {
		this.friendMapByName = friendMapByName;
	}

	public Map<String, FriendBean> getFriendMapById() {
		return friendMapById;
	}

	public void setFriendMapById(Map<String, FriendBean> friendMapById) {
		this.friendMapById = friendMapById;
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
	
	private void registerReciverMsg() {
		// 注册一个接收消息的BroadcastReceiver
		IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		registerReceiver(msgReceiver, intentFilter);

	}
	
	/**接受消息的广播*/
	private BroadcastReceiver msgReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// 主页面收到消息后，主要为了提示未读，实际消息内容需要到chat页面查看

			String from = intent.getStringExtra("from");
			// 消息id
			String msgId = intent.getStringExtra("msgid");
			EMMessage message = EMChatManager.getInstance().getMessage(msgId);
			// 2014-10-22 修复在某些机器上，在聊天页面对方发消息过来时不立即显示内容的bug
			if (ChatActivity.activityInstance != null) {
				if (message.getChatType() == ChatType.GroupChat) {
					if (message.getTo().equals("bg"+ChatActivity.activityInstance.getToChatUserId()))
						return;
				} else {
					if (from.equals("bg"+ChatActivity.activityInstance.getToChatUserId()))
						return;
				}
			}
			
			// 注销广播接收者，否则在ChatActivity中会收到这个广播
			abortBroadcast();
			
//			notifyNewMessage(message);

//			// 刷新bottom bar消息未读数
//			updateUnreadLabel();
//			if (currentTabIndex == 0) {
//				// 当前页面如果为聊天历史页面，刷新此页面
//				if (chatHistoryFragment != null) {
//					chatHistoryFragment.refresh();
//				}
//			}
		}
	};
}
