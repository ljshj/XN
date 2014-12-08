package com.bgood.xn.ui.user.account;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.bean.MemberLoginBean;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.system.SystemConfig;
import com.bgood.xn.ui.MainActivity;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.ui.user.UserCenterFragment;
import com.bgood.xn.view.BToast;
import com.bgood.xn.widget.TitleBar;

/**
 * 
 * @todo:注册页面完成
 * @date:2014-12-2 下午5:42:11
 * @author:hg_liuzl@163.com
 */
public class RegisterDoneActivity extends BaseActivity implements TaskListenerWithState
{
	private EditText m_passwordEt = null; // 密码
	private EditText m_confirmPasswordEt = null; // 确认密码
	private String newPassword = "";
	private String m_phone;
	private String userID;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_register_done);
		(new TitleBar(mActivity)).initTitleBar("请输入密码");
		findView();
	}


	/**
	 * 控件初始化方法
	 */
	private void findView()
	{
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
		{
		    m_phone = bundle.getString("phone");
			userID = bundle.getString("userID");
		}
		m_passwordEt = (EditText) findViewById(R.id.register_done_et_password);
		m_confirmPasswordEt = (EditText) findViewById(R.id.register_done_et_confirm_password);
		findViewById(R.id.register_done_btn_login).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				checkUserInfo();
			}
		});
	}

	  /**
     * 检查用户信息方法
     */
    private void checkUserInfo()
    {
        // 用户手机号码查询
         newPassword = m_passwordEt.getText().toString().trim();
         String confirmPassword = m_confirmPasswordEt.getText().toString().trim();
        if (TextUtils.isEmpty(newPassword))
        {
            BToast.show(mActivity, "请输入密码");
            return;
        }
        else if (newPassword.length() < 6 || newPassword.length() > 10)
        {
            BToast.show(mActivity, "请输入6-10位字符密码");
            return;
        }else if (!newPassword.equals(confirmPassword))
        {
        	BToast.show(mActivity, "两次输入密码不一致");
            return;
        }
        else
        {
          UserCenterRequest.getInstance().requestRegister(this, mActivity, m_phone,userID,newPassword);
        }
      
    }


	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			JSONObject body = bNetWork.getBody();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				boolean isSuccess = body.opt("success").equals("True");
				if(isSuccess){
					BToast.show(mActivity, "注册成功");
					Intent intent = new Intent(this, LoginActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); 
					startActivity(intent);
					finish();
				}else{
					BToast.show(mActivity, "注册失败");
				}
			}else{
				BToast.show(mActivity, "注册失败");
			}
		}else{
			BToast.show(mActivity, "注册失败");
		}
	}

}
