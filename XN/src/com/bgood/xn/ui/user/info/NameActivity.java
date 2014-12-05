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
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.view.BToast;
import com.bgood.xn.widget.CEditText;
import com.bgood.xn.widget.TitleBar;

/**
 * 修改姓名页面
 */
public class NameActivity extends BaseActivity implements TaskListenerWithState
{
    private CEditText m_nameEt = null;  // 昵称编辑框
    private String m_name;
    private UserInfoBean m_userDTO = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_name);
        (new TitleBar(mActivity)).initTitleBar("修改姓名");
        m_userDTO = (UserInfoBean) getIntent().getSerializableExtra(UserInfoBean.KEY_USER_BEAN);
        findView();
    }
    
    /**
     * 控件初始化方法
     */
    private void findView()
    {
        m_nameEt = (CEditText) findViewById(R.id.name_et_name);
        m_nameEt.setText(m_userDTO.nickn);
        m_nameEt.setSelection(m_userDTO.nickn.length());
       findViewById(R.id.name_btn_done).setOnClickListener(new OnClickListener() {
		
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
    	m_name = m_nameEt.getText().toString().trim();
        if (TextUtils.isEmpty(m_name))
        {
            BToast.show(mActivity, "请输入您的昵称");
            return;
        }else if (m_name.length() <3 && m_name.length() >8)
        {
        	 BToast.show(mActivity, "请输入3-8位长度昵称");
            return;
        }else
        {
           UserCenterRequest.getInstance().requestUpdatePerson(this, mActivity, "nickname", m_name);
        }
    }

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				BToast.show(mActivity, "修改成功");
				final UserInfoBean ufb = BGApp.mUserBean;
				ufb.nickn = m_name;
				BGApp.mUserBean = ufb;
				finish();
			}else{
				BToast.show(mActivity, "修改失败");
			}
		}
	}
}
