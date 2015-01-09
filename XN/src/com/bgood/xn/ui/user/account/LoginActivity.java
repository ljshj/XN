package com.bgood.xn.ui.user.account;import android.app.Activity;import android.content.Intent;import android.graphics.Paint;import android.os.Bundle;import android.text.TextUtils;import android.view.KeyEvent;import android.view.View;import android.view.View.OnClickListener;import android.widget.Button;import android.widget.EditText;import android.widget.TextView;import com.alibaba.fastjson.JSON;import com.bgood.xn.R;import com.bgood.xn.bean.MemberLoginBean;import com.bgood.xn.bean.UserInfoBean;import com.bgood.xn.network.BaseNetWork;import com.bgood.xn.network.BaseNetWork.ReturnCode;import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;import com.bgood.xn.network.http.HttpRequestInfo;import com.bgood.xn.network.http.HttpResponseInfo;import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;import com.bgood.xn.network.request.UserCenterRequest;import com.bgood.xn.system.BGApp;import com.bgood.xn.system.SystemConfig;import com.bgood.xn.ui.base.BaseActivity;import com.bgood.xn.utils.LogUtils;import com.bgood.xn.view.BToast;import com.bgood.xn.view.LoadingProgress;import com.bgood.xn.widget.TitleBar;import com.easemob.EMCallBack;import com.easemob.chat.Constant;import com.easemob.chat.EMChatManager;import com.easemob.chat.EMGroupManager;import com.easemob.chat.domain.User;import com.easemob.exceptions.EaseMobException;import com.easemob.util.EMLog;import com.easemob.util.HanziToPinyin;/** * @todo:登录界面 * @author:hg_liuzl@163.com */public class LoginActivity extends BaseActivity implements OnClickListener,TaskListenerWithState {	private EditText m_accountNumberEt;	private EditText m_passwordEt;	private TextView m_forgetPasswordTv;	private Button m_loginBtn;	private Button m_registerBtn;	private String mAccountNumber;	private String mPassWord;	private TitleBar titleBar = null;		private String fromKey = null;		public static final String FROM_KEY = "from_key";	public static final String FROM_KEY_OTHER = "from_key_other";		@Override	protected void onCreate(Bundle savedInstanceState) {		super.onCreate(savedInstanceState);		setContentView(R.layout.layout_login_fragment);		fromKey = getIntent().getStringExtra(FROM_KEY);		titleBar = new TitleBar(mActivity);		titleBar.initTitleBar("登录");		titleBar.backBtn.setOnClickListener(new OnClickListener() {			@Override			public void onClick(View arg0) {				cancelOrFinish();			}		});		initView();	}		@Override	protected void onDestroy() {		super.onDestroy();		LoadingProgress.getInstance().dismiss();	}		private void cancelOrFinish() {		if(FROM_KEY_OTHER.equals(fromKey)){			Intent intent = new Intent();			setResult(Activity.RESULT_CANCELED, intent);		}		LoginActivity.this.finish();	}		@Override	public boolean onKeyDown(int keyCode, KeyEvent event) {		if(keyCode == KeyEvent.KEYCODE_BACK){			cancelOrFinish();		}		return super.onKeyDown(keyCode, event);	}		private void initView()	{		mAccountNumber = pUitl.getAccountNumber();		mPassWord = pUitl.getAccountPassword();				m_accountNumberEt = (EditText) findViewById(R.id.login_et_account_number);		m_accountNumberEt.setText(mAccountNumber);				m_passwordEt = (EditText) findViewById(R.id.login_et_password);		m_passwordEt.setText(mPassWord);				m_forgetPasswordTv = (TextView) findViewById(R.id.login_tv_forget_password);		m_loginBtn = (Button) findViewById(R.id.login_btn_login);		m_registerBtn = (Button) findViewById(R.id.login_btn_register);		m_forgetPasswordTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 添加下划线		m_forgetPasswordTv.setOnClickListener(this);		m_loginBtn.setOnClickListener(this);		m_registerBtn.setOnClickListener(this);	}			@Override	public void onClick(View v) {		switch (v.getId())		{		case R.id.login_tv_forget_password:// 忘记密码			Intent intent = new Intent(mActivity, ForgetPasswordActivity.class);			startActivity(intent);			break;		case R.id.login_btn_login:// 登录			mAccountNumber = m_accountNumberEt.getText().toString();			mPassWord = m_passwordEt.getText().toString();			if (TextUtils.isEmpty(mAccountNumber))			{				BToast.show(mActivity, "请输入用户名");				return;			}else if(TextUtils.isEmpty(mPassWord)){				BToast.show(mActivity, "请输入密码");				return;			}else{				UserCenterRequest.getInstance().requestLogin(this, mActivity, mAccountNumber, mPassWord);			}			break;		// 注册		case R.id.login_btn_register:			Intent intent2 = new Intent(mActivity, RegisterActivity.class);			startActivity(intent2);			break;		default:			break;				}	}	@Override	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {		if(info.getState() == HttpTaskState.STATE_OK){			BaseNetWork bNetWork = info.getmBaseNetWork();			String strJson = bNetWork.getStrJson();			switch (bNetWork.getMessageType()) {			case 810005:				if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){					MemberLoginBean login = JSON.parseObject(strJson, MemberLoginBean.class);					BGApp.isUserLogin = login.success.equalsIgnoreCase("True")?true:false;					if(BGApp.isUserLogin){						SystemConfig.FILE_SERVER = login.fserver;						SystemConfig.BS_SERVER = login.bserver;						SystemConfig.SessionID = login.userid;						BGApp.mLoginBean = login;						BGApp.mUserId = String.valueOf(login.userid);												pUitl.setFileServerUrl(login.fserver);						pUitl.setBSServerUrl(login.bserver);												pUitl.setAccountNumber(mAccountNumber);						pUitl.setAccountPassword(mPassWord);												UserCenterRequest.getInstance().requestPersonInfo(LoginActivity.this, LoginActivity.this, BGApp.mUserId, true);//						doIMLogin();						//						if(FROM_KEY_OTHER.equals(fromKey)){//							Intent intent = new Intent();//							setResult(Activity.RESULT_OK, intent);//						}//						finish();					}else{						BToast.show(mActivity, "登录失败");					}				}else{					BToast.show(mActivity, "登录失败");				}				break;			case 820001:	//获取用户的个人资料				UserInfoBean userBean = JSON.parseObject(strJson, UserInfoBean.class);				BGApp.mUserBean = userBean;				doIMLogin();				break;			default:				break;			}					}	}			       private void doIMLogin() {		final String username = "bg"+BGApp.mUserId;		final String password = "banggood123";		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {			LoadingProgress.getInstance().show(this,"正在登录聊天服务器");			// 调用sdk登陆方法登陆聊天服务器			EMChatManager.getInstance().login(username, password, new EMCallBack() {				@Override				public void onSuccess() {					// 登陆成功，保存用户名密码					BGApp.getInstance().setUserName(username);					BGApp.getInstance().setPassword(password);					runOnUiThread(new Runnable() {						public void run() {							LoadingProgress.getInstance().show(mActivity,"登录聊天服务器成功...");						}					});					//更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick					boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(username+"#########");					if (!updatenick) {						EMLog.e("LoginActivity", "update current user nick fail");					}					LoadingProgress.getInstance().dismiss();										if(FROM_KEY_OTHER.equals(fromKey)){						Intent intent = new Intent();						setResult(Activity.RESULT_OK, intent);					}					finish();				}				@Override				public void onProgress(int progress, String status) {				}				@Override				public void onError(final int code, final String message) {					runOnUiThread(new Runnable() {						public void run() {							LoadingProgress.getInstance().dismiss();							BToast.show(mActivity, "登录聊天服务器失败");						}					});				}			});		}	}		/**	 * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人	 * @param username	 * @param user	 */	protected void setUserHearder(String username, User user) {		String headerName = null;		if (!TextUtils.isEmpty(user.getNick())) {			headerName = user.getNick();		} else {			headerName = user.getUsername();		}		if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {			user.setHeader("");		} else if (Character.isDigit(headerName.charAt(0))) {			user.setHeader("#");		} else {			user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1).toUpperCase());			char header = user.getHeader().toLowerCase().charAt(0);			if (header < 'a' || header > 'z') {				user.setHeader("#");			}		}	}}