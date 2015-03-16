package com.bgood.xn.ui.user.info;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.utils.ToolUtils;
import com.bgood.xn.view.BToast;
import com.bgood.xn.widget.CEditText;
import com.bgood.xn.widget.TitleBar;
import com.umeng.analytics.MobclickAgent;

/**
 * 修改邮箱页面
 */
public class EmailActivity extends BaseActivity implements TaskListenerWithState
{
    private CEditText m_emailEt = null;  // 昵称编辑框
    private String m_email = "";
    private UserInfoBean m_userDTO = null;
    private TitleBar mTitleBar;
    
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
        setContentView(R.layout.layout_email);
        
        mTitleBar = new TitleBar(mActivity);
        mTitleBar.initAllBar("修改邮箱", "确定");
        mTitleBar.rightBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 checkInfo();
			}
		});
        
        
        m_userDTO = (UserInfoBean) getIntent().getSerializableExtra(UserInfoBean.KEY_USER_BEAN);
        findView();
    }
  
    
    /**
     * 控件初始化方法
     */
    private void findView()
    {
        m_emailEt = (CEditText) findViewById(R.id.email_et_email);
        m_emailEt.setText(m_userDTO.email);
        m_emailEt.setSelection(m_userDTO.email.length());
    }
    
    /**
     * 检查用户信息方法
     */
    private void checkInfo()
    {
    	m_email	 = m_emailEt.getText().toString().trim();
        if (TextUtils.isEmpty(m_email))
        {
            BToast.show(mActivity, "请输入您的邮箱");
            return;
        }
        else if (!ToolUtils.isEmail(m_email))
        {
            BToast.show(mActivity, "邮箱格式不正确");
            return;
        }
        else
        {
            UserCenterRequest.getInstance().requestUpdatePerson(this, mActivity, "email", m_email);
        }
    }

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				BToast.show(mActivity, "修改成功");
				final UserInfoBean ufb = BGApp.mUserBean;
				ufb.email = m_email;
				BGApp.mUserBean = ufb;
				finish();
			}else{
				BToast.show(mActivity, "修改失败");
			}
		}
	}

}
