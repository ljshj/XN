package com.bgood.xn.ui.user.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import com.bgood.xn.R;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.view.BToast;
import com.bgood.xn.widget.TitleBar;

/**
 * 修改密码页面
 */
public class ModifyPasswordActivity extends BaseActivity implements TaskListenerWithState
{
    private EditText m_oldPasswordEt     = null; // 原密码
    private EditText m_newPasswordEt     = null; // 新密码
    private EditText m_confirmPasswordEt = null; // 确认新密码
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_modify_password);
        (new TitleBar(mActivity)).initTitleBar("修改密码");
        findView();
    }
 

    /**
     * 控件初始化方法
     */
    private void findView()
    {
        m_oldPasswordEt = (EditText) findViewById(R.id.modify_password_et_old_password);
        m_newPasswordEt = (EditText) findViewById(R.id.modify_password_et_new_password);
        m_confirmPasswordEt = (EditText) findViewById(R.id.modify_password_et_confirm_password);
        findViewById(R.id.modify_password_btn_next).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checkInfo();
			}
		});
    }
    /**
     * 检查用户信息方法
     */
    private void checkInfo()
    {
        // 原密码
    	String oldPassword = m_oldPasswordEt.getText().toString().trim();
    	String newPassword = m_newPasswordEt.getText().toString().trim();
        String confirmPassword = m_confirmPasswordEt.getText().toString().trim();
        if (TextUtils.isEmpty(oldPassword))
        {
            BToast.show(mActivity, "请输入原密码");
            return;
        }else if(TextUtils.isEmpty(newPassword)){
        	 BToast.show(mActivity, "请输入新密码");
             return;
        }else if(newPassword.length() < 6 && newPassword.length() > 10){
        	 BToast.show(mActivity, "新密码长度必须为6-10位");
             return;
        }else if(!newPassword.equals(confirmPassword)){
        	 BToast.show(mActivity, "两次密码输入不一致");
        	 return;
        }else{
        	UserCenterRequest.getInstance().requestModifyPWD(this, this, oldPassword, newPassword);
        }
    }

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				BToast.show(mActivity, "密码成功失败");
				pUitl.setAccountNumber(null);
				pUitl.setAccountPassword(null);
				Intent intent = new Intent(mActivity, LoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				mActivity.startActivity(intent);
			}else{
				BToast.show(mActivity, "密码修改失败");
			}
		}
	}
}
