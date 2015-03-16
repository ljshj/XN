package com.bgood.xn.ui.user.info;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

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
import com.bgood.xn.view.BToast;
import com.bgood.xn.widget.TitleBar;
import com.bgood.xn.widget.wheel.AgePikcerDialog;
import com.bgood.xn.widget.wheel.AgePikcerDialog.OnDateAgeSetListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 生日编写页面
 */
public class AgeActivity extends BaseActivity implements TaskListenerWithState
{
    private TextView m_dayTv = null;  // 生日填写
    private UserInfoBean mUserBean = null;
    private String mAge;
    private TitleBar titleBar = null;
    
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
        setContentView(R.layout.layout_birthday);
        titleBar = new TitleBar(mActivity);
        titleBar.initAllBar("设置年龄");
        mUserBean = (UserInfoBean) getIntent().getSerializableExtra(UserInfoBean.KEY_USER_BEAN);
        m_dayTv = (TextView) findViewById(R.id.birthday_tv_day);
        m_dayTv.setText(mUserBean.age);
        setListener();
    }
    
    /**
     * 控件事件监听方法
     */
    private void setListener()
    {
        m_dayTv.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            	new AgePikcerDialog(mActivity, TextUtils.isEmpty(m_dayTv.getText().toString())?"1":m_dayTv.getText().toString(), new OnDateAgeSetListener() {
					@Override
					public void onDateAgeSet(int age) {
						m_dayTv.setText(String.valueOf(age));	
					}
				}).show();
            }
        });
        
        // 确定按钮
        titleBar.rightBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
            	mAge = m_dayTv.getText().toString();
                if (!TextUtils.isEmpty(mAge))
                {
                    loadData();
                }
                else
                {
                   BToast.show(mActivity, "请输入您的生日");
                    return;
                }
                
            }
        });
    }
    
    /**
     * 加载数据方法
     * @param value 修改内容
     */
    private void loadData()
    {
		UserCenterRequest.getInstance().requestUpdatePerson(this, mActivity, "age", mAge);
    }

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				BToast.show(mActivity, "修改成功");
				final UserInfoBean ufb = BGApp.mUserBean;
				ufb.age = mAge;
				BGApp.mUserBean = ufb;
				finish();
			}else{
				BToast.show(mActivity, "修改失败");
			}
		}
	}
}
