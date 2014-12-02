package com.bgood.xn.ui.user.account;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.utils.WindowUtil;
import com.bgood.xn.widget.TitleBar;

/**
 * 
 * @todo:注册页面完成
 * @date:2014-12-2 下午5:42:11
 * @author:hg_liuzl@163.com
 */
public class RegisterDoneActivity extends BaseActivity
{
	private EditText m_passwordEt = null; // 密码
	private EditText m_confirmPasswordEt = null; // 确认密码
	private Button m_loginBtn = null; // 立即登录按钮

	private String m_password = "";
	private String m_confirmPassword = "";
	private String m_phone;
	private String userID;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_register_done);
		(new TitleBar(mActivity)).initTitleBar("请输入登录密码");
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
	 * 检查用户填写信息是否规范与合理
	 */
	private void checkUserInfo()
	{
		// 新密码查询
		String password = m_passwordEt.getText().toString().trim();
		if (password == null || password.equals(""))
		{
			Toast.makeText(RegisterDoneActivity.this, "新密码不能为空！", 0).show();
			return;
		} else if (password.length() < 6)
		{
			Toast.makeText(RegisterDoneActivity.this, "密码不能少于六位数！", 0).show();
			return;
		} else
		{
			m_password = password;
		}

		// 确认密码查询
		String confirmPassword = m_confirmPasswordEt.getText().toString().trim();
		if (confirmPassword == null || confirmPassword.equals(""))
		{
			Toast.makeText(RegisterDoneActivity.this, "确认密码不能为空！", 0).show();
			return;
		} else if (!confirmPassword.equals(m_password))
		{
			Toast.makeText(RegisterDoneActivity.this, "请输入正确的密码进行确认！", 0).show();
			return;
		} else
		{
			m_confirmPassword = confirmPassword;
		}

	}

}
