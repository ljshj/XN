package com.bgood.xn.ui.user.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.view.BToast;
import com.bgood.xn.widget.TitleBar;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @todo:忘记密码---输入验证码
 * @date:2014-12-2 下午5:44:58
 * @author:hg_liuzl@163.com
 */
public class ForgetPasswordCodeActivity extends BaseActivity implements TaskListenerWithState
{
    private TextView m_phoneTv = null;  // 号码显示
    private EditText m_codeEt = null;  // 验证码输入框
    private String m_phone = "";
    private TitleBar titleBar = null;
    private String mVerifyCode = "";	//验证码

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_forget_password_code);
        titleBar = new TitleBar(mActivity);
        titleBar.initAllBar("忘记密码", "下一步");
        m_phone =  getIntent().getStringExtra("phone");
        findView();
    }
    /**
     * 控件初始化方法
     */
    private void findView()
    {
        m_phoneTv = (TextView) findViewById(R.id.forget_password_code_tv_phone);
        m_codeEt = (EditText) findViewById(R.id.forget_password_code_et_code);
        m_phoneTv.setText("验证码已发送到" + m_phone.substring(0,3) + "****" + m_phone.substring(7, m_phone.length()) + "，请注意查收");
        // 下一步按钮
        titleBar.rightBtn.setOnClickListener(new OnClickListener()
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
    	mVerifyCode = m_codeEt.getText().toString().trim();
        if (TextUtils.isEmpty(mVerifyCode))
        {
            BToast.show(mActivity, "验证码不能为空");
            return;
        }else{
        	UserCenterRequest.getInstance().requestVerifyPhoneByForget(this, mActivity, m_phone, mVerifyCode);
        }
    }

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				BToast.show(mActivity, "验证成功");
				Intent intent = new Intent(ForgetPasswordCodeActivity.this, ForgetPasswordNewPassWordActivity.class);
				intent.putExtra("phone", m_phone);
				startActivity(intent);
			}else{
				BToast.show(mActivity, "验证失败");
			}
		}
		
	}
}
