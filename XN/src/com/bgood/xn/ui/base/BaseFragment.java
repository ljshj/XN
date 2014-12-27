package com.bgood.xn.ui.base;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bgood.xn.db.DBHelper;
import com.bgood.xn.db.PreferenceUtil;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.user.account.LoginActivity;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.NotificationCompat;
import com.easemob.chat.utils.CommonUtils;
import com.easemob.util.EasyUtils;

/**
 * @todo:所有Fragment基类
 * @date:2014-10-22 下午1:39:05
 * @author:hg_liuzl@163.com
 */
public class BaseFragment extends Fragment {
	
	  private static final int notifiId = 11;
	    protected NotificationManager notificationManager;
	
	public static final int PAGE_SIZE_ADD = BaseActivity.PAGE_SIZE_ADD;
	
	public InputMethodManager inputMethodManager;
	
	public Activity mActivity = null;
	public LayoutInflater inflater = null;
	public View layout;
	public PreferenceUtil pUitl;
	public boolean hidden;
	
	public DBHelper dbHelper = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
		inputMethodManager = (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
		inflater = LayoutInflater.from(mActivity);
		pUitl = new PreferenceUtil(mActivity, PreferenceUtil.PREFERENCE_FILE);
		dbHelper = new DBHelper(mActivity);
		
		notificationManager = (NotificationManager)mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	public void finish() {
		FragmentManager manager = getActivity().getSupportFragmentManager();
		manager.popBackStack();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		 // onresume时，取消notification显示
        EMChatManager.getInstance().activityResumed();
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
	
   
}
