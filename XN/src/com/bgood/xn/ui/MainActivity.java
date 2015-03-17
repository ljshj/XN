package com.bgood.xn.ui;import android.app.TabActivity;import android.content.Intent;import android.os.Bundle;import android.text.TextUtils;import android.view.KeyEvent;import android.view.View;import android.widget.RadioGroup;import android.widget.RadioGroup.OnCheckedChangeListener;import android.widget.RadioButton;import android.widget.TabHost;import android.widget.Toast;import com.alibaba.fastjson.JSON;import com.bgood.xn.R;import com.bgood.xn.bean.ApkBean;import com.bgood.xn.bean.MemberLoginBean;import com.bgood.xn.bean.UserInfoBean;import com.bgood.xn.db.PreferenceUtil;import com.bgood.xn.network.BaseNetWork;import com.bgood.xn.network.BaseNetWork.ReturnCode;import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;import com.bgood.xn.network.http.HttpRequestInfo;import com.bgood.xn.network.http.HttpResponseInfo;import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;import com.bgood.xn.network.request.UserCenterRequest;import com.bgood.xn.system.BGApp;import com.bgood.xn.system.SystemConfig;import com.bgood.xn.ui.home.HomeActivity;import com.bgood.xn.ui.message.MessageActivity;import com.bgood.xn.ui.user.UserCenterActivity;import com.bgood.xn.ui.user.account.LoginActivity;import com.bgood.xn.ui.weiqiang.WeiqiangActivity;import com.bgood.xn.ui.xuanneng.XuanNengMainActivity;import com.bgood.xn.utils.update.UpdateManager;import com.bgood.xn.view.BToast;import com.bgood.xn.view.LoadingProgress;import com.bgood.xn.view.MarkImgRadioButton;import com.easemob.EMCallBack;import com.easemob.chat.Constant;import com.easemob.chat.EMChatManager;import com.umeng.analytics.MobclickAgent;/** *  * @todo:主界面 * @date:2014-12-4 下午5:10:25 * @author:hg_liuzl@163.com */@SuppressWarnings("deprecation")public class MainActivity extends TabActivity implements TaskListenerWithState{	//private RadioGroup rg;	private TabHost mTabHost;	public static final String SHOW_OF_FIRST_TAG = "home";	public static final String SHOW_OF_SECOND_TAG = "message";	public static final String SHOW_OF_THIRD_TAG = "weiqiang";	public static final String SHOW_OF_FOUR_TAG = "xuanneng";	public static final String SHOW_OF_FIVE_TAG = "usercenter";	public String mCurrentTab = SHOW_OF_FIRST_TAG;		public static MainActivity instance = null;		public PreferenceUtil pUitl;	/**是否已经检测更新了*/	private boolean hasCheck = false;		private MarkImgRadioButton[] mirbs;		@Override	protected void onCreate(Bundle savedInstanceState) {		super.onCreate(savedInstanceState);		setContentView(R.layout.activity_main);		BGApp.getInstance().addActivity(this);		pUitl = new PreferenceUtil(this, PreferenceUtil.PREFERENCE_FILE);		String mAccountNumber = pUitl.getAccountNumber();		String mPassWord = pUitl.getAccountPassword();		instance = this;				if(!TextUtils.isEmpty(mAccountNumber) && !TextUtils.isEmpty(mPassWord)){			LoadingProgress.getInstance().show(this,"正在登录...");			UserCenterRequest.getInstance().requestLogin(this, this, mAccountNumber, mPassWord,false);		}else{			if(!hasCheck){				MobclickAgent.onEvent(MainActivity.this,"sys_update");				UserCenterRequest.getInstance().requestCheckVesion(this, this);			}		}		initView();		updateUnRead();	}			@Override	protected void onResume() {		super.onResume();		Intent i_home = new Intent(this, HomeActivity.class);		Intent i_weiqiang = new Intent(this, WeiqiangActivity.class);		Intent i_xuanneng = new Intent(this, XuanNengMainActivity.class);				if(pUitl.isLogin()){	//如果登录了，就把底部栏，换一下			Intent i_message = new Intent(this, MessageActivity.class);			Intent i_user = new Intent(this, UserCenterActivity.class);			mTabHost.addTab(mTabHost.newTabSpec(SHOW_OF_FIRST_TAG).setIndicator(SHOW_OF_FIRST_TAG).setContent(i_home));			mTabHost.addTab(mTabHost.newTabSpec(SHOW_OF_SECOND_TAG).setIndicator(SHOW_OF_SECOND_TAG).setContent(i_message));			mTabHost.addTab(mTabHost.newTabSpec(SHOW_OF_THIRD_TAG).setIndicator(SHOW_OF_THIRD_TAG).setContent(i_weiqiang));			mTabHost.addTab(mTabHost.newTabSpec(SHOW_OF_FOUR_TAG).setIndicator(SHOW_OF_FOUR_TAG).setContent(i_xuanneng));			mTabHost.addTab(mTabHost.newTabSpec(SHOW_OF_FIVE_TAG).setIndicator(SHOW_OF_FIVE_TAG).setContent(i_user));		}else{			Intent i_login = new Intent(this,LoginActivity.class);			mTabHost.addTab(mTabHost.newTabSpec(SHOW_OF_FIRST_TAG).setIndicator(SHOW_OF_FIRST_TAG).setContent(i_home));			mTabHost.addTab(mTabHost.newTabSpec(SHOW_OF_SECOND_TAG).setIndicator(SHOW_OF_SECOND_TAG).setContent(i_login));			mTabHost.addTab(mTabHost.newTabSpec(SHOW_OF_THIRD_TAG).setIndicator(SHOW_OF_THIRD_TAG).setContent(i_weiqiang));			mTabHost.addTab(mTabHost.newTabSpec(SHOW_OF_FOUR_TAG).setIndicator(SHOW_OF_FOUR_TAG).setContent(i_xuanneng));			mTabHost.addTab(mTabHost.newTabSpec(SHOW_OF_FIVE_TAG).setIndicator(SHOW_OF_FIVE_TAG).setContent(i_login));		}		mTabHost.setCurrentTabByTag(mCurrentTab);	}		/**	 * 	 * @todo:TODO 设置当前选中的Tab	 * @date:2015-3-16 下午6:24:26	 * @author:hg_liuzl@163.com	 * @params:@param mTab	 */	private void selectCurrentTab(int mTab) {		for (int i = 0; i < mirbs.length; i++) {			if (mTab==i) {				mirbs[i].setSelected(true);			}else{				mirbs[i].setSelected(false);			}		}	}			/**	 * 	 * @todo:选择Tab	 * @date:2015-3-16 下午4:25:41	 * @author:hg_liuzl@163.com	 * @params:@param view	 */	public void onTabClicked(View view) {		int mCurrentIndex = 0;		switch (view.getId()) {		case R.id.main_tab_home:			MobclickAgent.onEvent(MainActivity.this,"home_click");			mCurrentTab = SHOW_OF_FIRST_TAG;			mCurrentIndex = 0;			break;		case R.id.main_tab_communication:			MobclickAgent.onEvent(MainActivity.this,"chat_click");			mCurrentTab = SHOW_OF_SECOND_TAG;			mCurrentIndex = 1;			break;		case R.id.main_tab_weiqiang:			MobclickAgent.onEvent(MainActivity.this,"weiqiang_click");			mCurrentTab = SHOW_OF_THIRD_TAG;			mCurrentIndex = 2;			break;		case R.id.main_tab_xuanneng:			MobclickAgent.onEvent(MainActivity.this,"xuanneng_click");			mCurrentTab = SHOW_OF_FOUR_TAG;			mCurrentIndex = 3;			break;		case R.id.main_tab_me:			MobclickAgent.onEvent(MainActivity.this,"usercenter_click");			mCurrentTab = SHOW_OF_FIVE_TAG;			mCurrentIndex = 4;			break;		default:			mCurrentTab = SHOW_OF_FIRST_TAG;			break;		}		selectCurrentTab(mCurrentIndex);		mTabHost.setCurrentTabByTag(mCurrentTab);	}			private void initView() {			//rg = (RadioGroup) findViewById(R.id.main_tab_group);			mTabHost = getTabHost();						mirbs = new MarkImgRadioButton[5];			mirbs[0] = (MarkImgRadioButton) findViewById(R.id.main_tab_home);			mirbs[1] = (MarkImgRadioButton) findViewById(R.id.main_tab_communication);			mirbs[2] = (MarkImgRadioButton) findViewById(R.id.main_tab_weiqiang);			mirbs[3] = (MarkImgRadioButton) findViewById(R.id.main_tab_xuanneng);			mirbs[4] = (MarkImgRadioButton) findViewById(R.id.main_tab_me);			selectCurrentTab(0);						//			Intent i_home = new Intent(this, HomeActivity.class);//			//Intent i_message = new Intent(this, MessageActivity.class);//			Intent i_weiqiang = new Intent(this, WeiqiangActivity.class);//			Intent i_xuanneng = new Intent(this, XuannengActivity.class);//			//Intent i_user = new Intent(this, UserCenterActivity.class);//			Intent i_login = new Intent(this,LoginActivity.class);////			mTabHost.addTab(mTabHost.newTabSpec(SHOW_OF_FIRST_TAG).setIndicator(SHOW_OF_FIRST_TAG).setContent(i_home));//			mTabHost.addTab(mTabHost.newTabSpec(SHOW_OF_SECOND_TAG).setIndicator(SHOW_OF_SECOND_TAG).setContent(i_login));//			mTabHost.addTab(mTabHost.newTabSpec(SHOW_OF_THIRD_TAG).setIndicator(SHOW_OF_THIRD_TAG).setContent(i_weiqiang));//			mTabHost.addTab(mTabHost.newTabSpec(SHOW_OF_FOUR_TAG).setIndicator(SHOW_OF_FOUR_TAG).setContent(i_xuanneng));//			mTabHost.addTab(mTabHost.newTabSpec(SHOW_OF_FIVE_TAG).setIndicator(SHOW_OF_FIVE_TAG).setContent(i_login));//			rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {//						public void onCheckedChanged(RadioGroup group, int checkedId) {//							switch (checkedId) {//							case R.id.main_tab_home://								MobclickAgent.onEvent(MainActivity.this,"home_click");//								mCurrentTab = SHOW_OF_FIRST_TAG;//								break;////							case R.id.main_tab_communication://								MobclickAgent.onEvent(MainActivity.this,"chat_click");//								mCurrentTab = SHOW_OF_SECOND_TAG;//								break;////							case R.id.main_tab_weiqiang://								MobclickAgent.onEvent(MainActivity.this,"weiqiang_click");//								mCurrentTab = SHOW_OF_THIRD_TAG;//								break;////							case R.id.main_tab_xuanneng://								MobclickAgent.onEvent(MainActivity.this,"xuanneng_click");//								mCurrentTab = SHOW_OF_FOUR_TAG;//								break;////							case R.id.main_tab_me://								MobclickAgent.onEvent(MainActivity.this,"usercenter_click");//								mCurrentTab = SHOW_OF_FIVE_TAG;//								break;//							default://								mCurrentTab = SHOW_OF_FIRST_TAG;//								break;//							}//							mTabHost.setCurrentTabByTag(mCurrentTab);//						}//					});			}		private long exitTime = 0;// 监听用户按返回键        @Override    public boolean dispatchKeyEvent(KeyEvent event) {    	if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK){    		if ((System.currentTimeMillis() - exitTime) > 2000) {				Toast.makeText(MainActivity.this, "再按一次退出程序",Toast.LENGTH_SHORT).show();				exitTime = System.currentTimeMillis();				return true;			} else {				BGApp.finishAllActivity();				finish();			}    		return false;    	}    	return super.dispatchKeyEvent(event);    }        @Override	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {		if(info.getState() == HttpTaskState.STATE_OK){			BaseNetWork bNetWork = info.getmBaseNetWork();			String strJson = bNetWork.getStrJson();			switch (bNetWork.getMessageType()) {			case 880001: //版本升级				if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){					/**版本升级处理*/					final ApkBean apk = JSON.parseObject(strJson, ApkBean.class);					UpdateManager manager = new UpdateManager(MainActivity.this, apk);					manager.checkUpdateInfo(false);				}				break;			case 810005:	//会员登录				LoadingProgress.getInstance().dismiss();				if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){					MemberLoginBean login = JSON.parseObject(strJson, MemberLoginBean.class);					BGApp.isUserLogin = login.success.equalsIgnoreCase("True")?true:false;					if(BGApp.isUserLogin){						SystemConfig.FILE_SERVER = login.fserver;						SystemConfig.BS_SERVER = login.bserver;						SystemConfig.SessionID = login.userid;						BGApp.mLoginBean = login;						BGApp.mUserId = String.valueOf(login.userid);												pUitl.setFileServerUrl(login.fserver);						pUitl.setBSServerUrl(login.bserver);												UserCenterRequest.getInstance().requestPersonInfo(MainActivity.this, MainActivity.this, BGApp.mUserId, false);						LoadingProgress.getInstance().show(MainActivity.this, "正在获取用户资料...");					}else{						BToast.show(MainActivity.this, "登录失败");					}				}else if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_ERROR_PASSWORD){					BToast.show(MainActivity.this, "密码错误");				}else if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_USER_NOTFOUND){					BToast.show(MainActivity.this, "手机号或能能号不存在");				}else if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_USER_KILL){					BToast.show(MainActivity.this, "您已经被管理员禁用");				}else{					BToast.show(MainActivity.this, "登录失败");				}				break;			case 820001:	//获取用户的个人资料				LoadingProgress.getInstance().dismiss();				if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){					UserInfoBean userBean = JSON.parseObject(strJson, UserInfoBean.class);					BGApp.mUserBean = userBean;					doIMLogin();				}else{					BToast.show(MainActivity.this, "获取个人资料失败");				}				break;							default:				break;			}		}	}        private void doIMLogin() {		final String username = "bg"+BGApp.mUserId;		final String password = "banggood123";		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {			LoadingProgress.getInstance().show(this,"正在登录聊天服务器");			// 调用sdk登陆方法登陆聊天服务器			EMChatManager.getInstance().login(username, password, new EMCallBack() {				@Override				public void onSuccess() {					// 登陆成功，保存用户名密码					BGApp.getInstance().setUserName(username);					BGApp.getInstance().setPassword(password);					runOnUiThread(new Runnable() {						public void run() {							//LoadingProgress.getInstance().show(MainActivity.this,"登录聊天服务器成功...");							LoadingProgress.getInstance().dismiss();							if(!hasCheck){								hasCheck = true;								UserCenterRequest.getInstance().requestCheckVesion(MainActivity.this, MainActivity.this);							}						}					});					//更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick					//boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(username+"#########");					if(null!=BGApp.mUserBean){						boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(BGApp.mUserBean.nickn);					}//					if (!updatenick) {//						EMLog.e("LoginActivity", "update current user nick fail");//					}				}				@Override				public void onProgress(int progress, String status) {				}				@Override				public void onError(final int code, final String message) {					runOnUiThread(new Runnable() {						public void run() {							LoadingProgress.getInstance().dismiss();							BToast.show(MainActivity.this, "登录聊天服务器失败");							UserCenterRequest.getInstance().requestCheckVesion(MainActivity.this, MainActivity.this);						}					});				}			});		}	}    	/**	 * 获取未读申请与通知消息	 * 	 * @return	 */	public int getUnreadAddressCountTotal() {		int unreadAddressCountTotal = 0;		if (BGApp.getInstance().getFriendMapById().get(Constant.NEW_FRIENDS_USERNAME) != null)			unreadAddressCountTotal = BGApp.getInstance().getFriendMapById().get(Constant.NEW_FRIENDS_USERNAME).getUnreadMsgCount();		return unreadAddressCountTotal;	}	/**	 * 获取未读消息数	 * 	 * @return	 */	public int getUnreadMsgCountTotal() {		int unreadMsgCountTotal = 0;		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();		return unreadMsgCountTotal;	}		/**	 * 	 * @todo:展示未读消息	 * @date:2015-3-16 下午2:53:31	 * @author:hg_liuzl@163.com	 * @params:	 */	public void updateUnRead() {		int count = getUnreadAddressCountTotal()+getUnreadMsgCountTotal();		mirbs[1].setRBTipValue(count);	}    }