package com.bgood.xn.ui.user.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.ui.BaseActivity;

/**
 * 性别修改页面
 */
public class SexActivity extends BaseActivity
{
    private Button m_backBtn = null;  // 返回
    private RadioGroup m_sexRg = null;  // 性别分组
    private RadioButton m_maleRb = null;  // 男
    private RadioButton m_femaleRb = null;  // 女 
    private RadioButton m_secrecyRb = null;  // 保密
    private Button m_confirmBtn = null; // 确定按钮
    
    UserCenterMessageManager messageManager = UserCenterMessageManager.getInstance();
    
    private int m_sex = -1;
    private UserInfoBean m_userDTO = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sex);
        
        getIntentData();
        findView();
        setListener();
        setData(m_userDTO.sex);
    }
    
    @Override
	public void onResume()
	{
		super.onResume();
		
		if (messageManager != null)
		{
			// 消息注册
			messageManager.registerObserver(this);
		}
	}

	@Override
	public void onPause()
	{
		super.onPause();
		
		if (messageManager != null)
		{
			// 消息注销
			messageManager.unregisterObserver(this);
		}
	}
	
	/**
	 * 得带传值数据方法
	 */
	private void getIntentData()
	{
	    Intent intent = getIntent();
        m_userDTO = (UserDTO) intent.getSerializableExtra("userDTO");
	}

    /**
     * 控件初始化方法
     */
    private void findView()
    {
        m_backBtn = (Button) findViewById(R.id.sex_btn_back);
        m_sexRg = (RadioGroup) findViewById(R.id.sex_rg_sex);
        m_maleRb = (RadioButton) findViewById(R.id.sex_rb_male);
        m_femaleRb = (RadioButton) findViewById(R.id.sex_rb_female);
        m_secrecyRb = (RadioButton) findViewById(R.id.sex_rb_secrecy);
        m_confirmBtn = (Button) findViewById(R.id.sex_btn_confirm);
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
        // 返回按钮
        m_backBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                SexActivity.this.finish();
            }
        });
        
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
        m_confirmBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (m_sex != -1)
                {
                    loadData(m_sex);
                }
                else
                {
                    Toast.makeText(SexActivity.this, "请选择性别！", Toast.LENGTH_LONG).show();
                    return;
                }
                
            }
        });
    }
    
    /**
     * 加载数据方法
     */
    private void loadData(int sex)
    {
    	WindowUtil.getInstance().progressDialogShow(SexActivity.this, "数据请求中...");
		messageManager.modifyPersonalInfo("sex", sex + "");
    }
    
    @Override
	public void modifyPersonalInfoCB(Reulst result_state)
	{
		super.modifyPersonalInfoCB(result_state);
		WindowUtil.getInstance().DismissAllDialog();
		
		if (result_state.resultCode == ReturnCode.RETURNCODE_OK)
		{
			Toast.makeText(this, "修改成功！", Toast.LENGTH_LONG).show();
			Intent intent = getIntent();
			intent.putExtra("sex", m_sex+"");
            setResult(RESULT_OK, intent);
			finish();
		}
		else
		{
			Toast.makeText(this, "修改失败！", Toast.LENGTH_LONG).show();
		}
	}
}
