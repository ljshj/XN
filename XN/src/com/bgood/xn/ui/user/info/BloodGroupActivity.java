package com.bgood.xn.ui.user.info;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.view.BToast;

/**
 * 
 * @todo:血型选择
 * @date:2014-10-23 上午9:35:02
 * @author:hg_liuzl@163.com
 */
public class BloodGroupActivity extends BaseActivity implements TaskListenerWithState
{
    private Button m_backBtn = null;  // 返回
    private RadioGroup m_bloodGroupRg = null;  // 血型分组
    private Button m_confirmBtn = null; // 确定按钮
    private String m_bloodGroup = "";
    private UserInfoBean mUserBean = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_blood_group);
        mUserBean =  (UserInfoBean) getIntent().getSerializableExtra(UserInfoBean.KEY_USER_BEAN);
        findView();
        setListener();
        setData();
    }
 

    /**
     * 控件初始化方法
     */
    private void findView()
    {
        m_backBtn = (Button) findViewById(R.id.blood_group_btn_back);
        m_bloodGroupRg = (RadioGroup) findViewById(R.id.blood_group_rg_blood_group);
        m_confirmBtn = (Button) findViewById(R.id.blood_group_btn_confirm);
    }
    
    private void setData()
    {
        if (mUserBean.btype.equals("O型"))
        {
            m_bloodGroupRg.check(R.id.blood_group_rb_o);
        }
        else if (mUserBean.btype.equals("A型"))
        {
            m_bloodGroupRg.check(R.id.blood_group_rb_a);
        }
        else if (mUserBean.btype.equals("B型"))
        {
            m_bloodGroupRg.check(R.id.blood_group_rb_b);
        }
        else if (mUserBean.btype.equals("AB型"))
        {
            m_bloodGroupRg.check(R.id.blood_group_rb_ab);
        }
        else
        {
            m_bloodGroupRg.check(R.id.blood_group_rb_no);
        }
    }
    
    /**
     * 控件事件监听方法
     */
    private void setListener()
    {
        // 返回按钮
        m_backBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                BloodGroupActivity.this.finish();
            }
        });
        
        // 血型选择
        m_bloodGroupRg.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if (checkedId == R.id.blood_group_rb_o)
                {
                    m_bloodGroup = "O型";
                }
                else if (checkedId == R.id.blood_group_rb_a)
                {
                    m_bloodGroup = "A型";
                }
                else if (checkedId == R.id.blood_group_rb_b)
                {
                    m_bloodGroup = "B型";
                }
                else if (checkedId == R.id.blood_group_rb_ab)
                {
                    m_bloodGroup = "AB型";
                }
                else
                {
                    m_bloodGroup = "未知";
                }
            }
        });
        
        // 确定按钮
        m_confirmBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (!TextUtils.isEmpty(m_bloodGroup))
                {
                    UserCenterRequest.getInstance().requestUpdatePerson(BloodGroupActivity.this, mActivity, "btype", m_bloodGroup);
                }
                else
                {
                    Toast.makeText(BloodGroupActivity.this, "请选择你的血型！", Toast.LENGTH_LONG).show();
                    return;
                }
                
            }
        });
    }

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				BToast.show(mActivity, "修改成功");
				Intent intent = getIntent();
	            intent.putExtra("bloodGroup", m_bloodGroup);
	            setResult(RESULT_OK, intent);
				finish();
			}else{
				BToast.show(mActivity, "修改失败");
			}
		}
	}
}
