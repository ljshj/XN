package com.bgood.xn.ui.user.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

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
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.view.BToast;
import com.bgood.xn.widget.TitleBar;

/**
 * 性别修改页面
 */
public class SexActivity extends BaseActivity implements TaskListenerWithState
{
    private RadioGroup m_sexRg = null;  // 性别分组
    private RadioButton m_maleRb = null;  // 男
    private RadioButton m_femaleRb = null;  // 女 
    private RadioButton m_secrecyRb = null;  // 保密
    private int m_sex = -1;
    private UserInfoBean m_userDTO = null;
    private TitleBar titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sex);
        titleBar = new TitleBar(mActivity);
        titleBar.initAllBar("年龄");
        m_userDTO = (UserInfoBean) getIntent().getSerializableExtra(UserInfoBean.KEY_USER_BEAN);
        findView();
        setListener();
        setData(m_userDTO.sex);
    }


    /**
     * 控件初始化方法
     */
    private void findView()
    {
        m_sexRg = (RadioGroup) findViewById(R.id.sex_rg_sex);
        m_maleRb = (RadioButton) findViewById(R.id.sex_rb_male);
        m_femaleRb = (RadioButton) findViewById(R.id.sex_rb_female);
        m_secrecyRb = (RadioButton) findViewById(R.id.sex_rb_secrecy);
    }
    
    /**
     * 设置数据
     * @param index
     */
    private void setData(int index)
    {
        if (index == 1)
        {
            m_maleRb.setChecked(true);
        } 
        else if (index == 2)
        {
            m_femaleRb.setChecked(true);
        }
        else
        {
            m_secrecyRb.setChecked(true);
        }
    }
    
    /**
     * 控件事件监听方法
     */
    private void setListener()
    {
        // 性别选择
        m_sexRg.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if (checkedId == R.id.sex_rb_male)
                {
                    m_sex = 1;
                }
                else if (checkedId == R.id.sex_rb_female)
                {
                    m_sex = 2;
                }
                else
                {
                    m_sex = 0;
                }
            }
        });
        
        // 确定按钮
        titleBar.rightBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (m_sex != -1)
                {
                    loadData();
                }
                else
                {
                    BToast.show(mActivity, "请选择性别");
                    return;
                }
                
            }
        });
    }
    
    /**
     * 加载数据方法
     */
    private void loadData()
    {
		UserCenterRequest.getInstance().requestUpdatePerson(this, mActivity, "sex", String.valueOf(m_sex));
    }
    


	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				BToast.show(mActivity, "修改成功");
				final UserInfoBean ufb = BGApp.mUserBean;
				ufb.sex = m_sex;
				BGApp.mUserBean = ufb;
				finish();
			}else{
				BToast.show(mActivity, "修改失败");
			}
		}
	}
}
