package com.bgood.xn.ui.user.info;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.bgood.xn.R;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.view.BToast;
import com.bgood.xn.widget.TitleBar;

/**
 * 修改年龄页面
 */
public class AgeActivity extends BaseActivity implements OnClickListener,TaskListenerWithState
{
    private EditText m_ageEt = null;  // 昵称编辑框
    private String m_age = "";
    private UserInfoBean mUserBean = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_age);
        (new TitleBar(mActivity)).initTitleBar("设置年龄");
        mUserBean = (UserInfoBean) getIntent().getSerializableExtra(UserInfoBean.KEY_USER_BEAN);
        findView();
    }
  
    /**
     * 控件初始化方法
     */
    private void findView()
    {
        m_ageEt = (EditText) findViewById(R.id.age_et_age);
        
        m_ageEt.setText(mUserBean.age);
        m_ageEt.setSelection(mUserBean.age.length());
        
        findViewById(R.id.age_btn_done).setOnClickListener(new OnClickListener()
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
       m_age = m_ageEt.getText().toString().trim();
        if (TextUtils.isEmpty(m_age))
        {
            BToast.show(mActivity, "请输入年龄！");
            return;
        }else
        {
		UserCenterRequest.getInstance().requestUpdatePerson(this, mActivity, "age", m_age);
        }
    }
  

	@Override
	public void onClick(View v) {
	switch (v.getId()) {
	case R.id.age_btn_done:
		checkInfo();
		break;
	default:
		break;
	}
		
	}

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				BToast.show(mActivity, "修改成功");
				final UserInfoBean ufb = BGApp.mUserBean;
				ufb.age = m_age;
				BGApp.mUserBean = ufb;
				finish();
			}else{
				BToast.show(mActivity, "修改失败");
			}
		}
	}
}
