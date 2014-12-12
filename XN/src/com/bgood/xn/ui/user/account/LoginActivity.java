package com.bgood.xn.ui.user.account;import android.app.Activity;import android.content.Intent;import android.graphics.Paint;import android.os.Bundle;import android.text.TextUtils;import android.view.KeyEvent;import android.view.View;import android.view.View.OnClickListener;import android.widget.Button;import android.widget.EditText;import android.widget.TextView;import com.alibaba.fastjson.JSON;import com.bgood.xn.R;import com.bgood.xn.bean.MemberLoginBean;import com.bgood.xn.network.BaseNetWork;import com.bgood.xn.network.BaseNetWork.ReturnCode;import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;import com.bgood.xn.network.http.HttpRequestInfo;import com.bgood.xn.network.http.HttpResponseInfo;import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;import com.bgood.xn.network.request.UserCenterRequest;import com.bgood.xn.system.BGApp;import com.bgood.xn.system.SystemConfig;import com.bgood.xn.ui.base.BaseActivity;import com.bgood.xn.ui.user.UserCenterFragment;import com.bgood.xn.view.BToast;import com.bgood.xn.widget.TitleBar;/** * @todo:登录界面 * @author:hg_liuzl@163.com */public class LoginActivity extends BaseActivity implements OnClickListener,TaskListenerWithState {	/**来自那进而**/	public static final String FROM_KEY = "from_key";		private EditText m_accountNumberEt;	private EditText m_passwordEt;	private TextView m_forgetPasswordTv;	private Button m_loginBtn,btnBack;	private Button m_registerBtn;	private String fromKey;	private String mAccountNumber;	private String mPassWord;		@Override	protected void onCreate(Bundle savedInstanceState) {		super.onCreate(savedInstanceState);		setContentView(R.layout.layout_login_fragment);		fromKey = getIntent().getStringExtra(FROM_KEY);		(new TitleBar(mActivity)).initTitleBar("登录");		initView();			}		@Override	public boolean onKeyDown(int keyCode, KeyEvent event) {		if(keyCode == KeyEvent.KEYCODE_BACK){			finish();		}		return super.onKeyDown(keyCode, event);	}		private void initView()	{		mAccountNumber = pUitl.getAccountNumber();		mPassWord = pUitl.getAccountPassword();				btnBack = (Button) findViewById(R.id.btn_back);		btnBack.setVisibility(View.GONE);				m_accountNumberEt = (EditText) findViewById(R.id.login_et_account_number);		m_accountNumberEt.setText(mAccountNumber);				m_passwordEt = (EditText) findViewById(R.id.login_et_password);		m_passwordEt.setText(mPassWord);				m_forgetPasswordTv = (TextView) findViewById(R.id.login_tv_forget_password);		m_loginBtn = (Button) findViewById(R.id.login_btn_login);		m_registerBtn = (Button) findViewById(R.id.login_btn_register);		m_forgetPasswordTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 添加下划线		m_forgetPasswordTv.setOnClickListener(this);		m_loginBtn.setOnClickListener(this);		m_registerBtn.setOnClickListener(this);	}	@Override	public void onClick(View v) {		switch (v.getId())		{		case R.id.login_tv_forget_password:// 忘记密码			Intent intent = new Intent(mActivity, ForgetPasswordActivity.class);			startActivity(intent);			break;		case R.id.login_btn_login:// 登录			mAccountNumber = m_accountNumberEt.getText().toString();			mPassWord = m_passwordEt.getText().toString();			if (TextUtils.isEmpty(mAccountNumber))			{				BToast.show(mActivity, "请输入用户名");				return;			}else if(TextUtils.isEmpty(mPassWord)){				BToast.show(mActivity, "请输入密码");				return;			}else{				UserCenterRequest.getInstance().requestLogin(this, mActivity, mAccountNumber, mPassWord);			}			break;		// 注册		case R.id.login_btn_register:			Intent intent2 = new Intent(mActivity, RegisterActivity.class);			startActivity(intent2);			break;		default:			break;				}	}	@Override	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {		if(info.getState() == HttpTaskState.STATE_OK){			BaseNetWork bNetWork = info.getmBaseNetWork();			String strJson = bNetWork.getStrJson();			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){				MemberLoginBean login = JSON.parseObject(strJson, MemberLoginBean.class);				BGApp.isUserLogin = login.success.equalsIgnoreCase("True")?true:false;								if(BGApp.isUserLogin){					SystemConfig.FILE_SERVER = login.fserver;					SystemConfig.BS_SERVER = login.bserver;					SystemConfig.SessionID = login.userid;					BGApp.mLoginBean = login;					BGApp.mUserId = String.valueOf(login.userid);										pUitl.setAccountNumber(mAccountNumber);					pUitl.setAccountPassword(mPassWord);					BToast.show(mActivity, "登录成功");										doIMLogin();										if(UserCenterFragment.TAG.equals(fromKey)){						Intent intent = new Intent();						setResult(Activity.RESULT_OK, intent);						finish();					}										LoginActivity.this.finish();				}else{					BToast.show(mActivity, "登录失败");				}			}else{				BToast.show(mActivity, "登录失败");			}		}else{			BToast.show(mActivity, "登录失败");		}	}		private void doIMLogin() {	}}