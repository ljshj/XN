package com.bgood.xn.ui.user.account;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.bgood.xn.R;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.view.BToast;
import com.bgood.xn.widget.TitleBar;

/**
 * 
 * @todo:忘记密码后设置新密码
 * @date:2014-12-2 下午5:08:21
 * @author:hg_liuzl@163.com
 */
public class ForgetPasswordNewPassWordActivity extends BaseActivity implements TaskListenerWithState
{
    private EditText m_newPasswordEt = null;  // 新密码
    private EditText m_confirmPasswordEt = null;  // 确认新密码
    
    private String newPassword = "";
    private String m_phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_forget_password_new_password);
        (new TitleBar(mActivity)).initTitleBar("输入新密码");
        m_phone = getIntent().getStringExtra("phone");
        findView();
    }
	
    /**
     * 控件初始化方法
     */
    private void findView()
    {
        m_newPasswordEt = (EditText) findViewById(R.id.forget_password_new_password_et_new_password);
        m_confirmPasswordEt = (EditText) findViewById(R.id.forget_password_new_password_et_confirm_password);
        findViewById(R.id.forget_password_new_password_btn_done).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkInfo();
            }
        });
    }
    
   
    
    /**
     * 检查用户信息方法
     */
    private void checkInfo()
    {
        // 用户手机号码查询
         newPassword = m_newPasswordEt.getText().toString().trim();
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
          UserCenterRequest.getInstance().requestReSetPwd(this, mActivity, m_phone, newPassword);
        }
      
    }


	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		/**密码重置后，是否需要重新登录*/
		
	}
}
