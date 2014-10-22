package com.bgood.xn.ui.user.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.ui.BaseActivity;

/**
 * 血型选择页面
 */
public class BloodGroupActivity extends BaseActivity
{
    private Button m_backBtn = null;  // 返回
    private RadioGroup m_bloodGroupRg = null;  // 血型分组
    private Button m_confirmBtn = null; // 确定按钮
    
    UserCenterMessageManager messageManager = UserCenterMessageManager.getInstance();
    
    private String m_bloodGroup = "";
    private UserDTO m_userDTO = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_blood_group);
        
        getIntentData();
        findView();
        setListener();
        setData();
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
        m_backBtn = (Button) findViewById(R.id.blood_group_btn_back);
        m_bloodGroupRg = (RadioGroup) findViewById(R.id.blood_group_rg_blood_group);
        m_confirmBtn = (Button) findViewById(R.id.blood_group_btn_confirm);
    }
    
    private void setData()
    {
        if (m_userDTO.btype.equals("O型"))
        {
            m_bloodGroupRg.check(R.id.blood_group_rb_o);
        }
        else if (m_userDTO.btype.equals("A型"))
        {
            m_bloodGroupRg.check(R.id.blood_group_rb_a);
        }
        else if (m_userDTO.btype.equals("B型"))
        {
            m_bloodGroupRg.check(R.id.blood_group_rb_b);
        }
        else if (m_userDTO.btype.equals("AB型"))
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
                if (m_bloodGroup != null && !m_bloodGroup.equals(""))
                {
                    loadData(m_bloodGroup);
                }
                else
                {
                    Toast.makeText(BloodGroupActivity.this, "请选择你的血型！", Toast.LENGTH_LONG).show();
                    return;
                }
                
            }
        });
    }
    
    /**
     * 加载数据方法
     * @param value 修改内容
     */
    private void loadData(String value)
    {
    	WindowUtil.getInstance().progressDialogShow(BloodGroupActivity.this, "数据请求中...");
		messageManager.modifyPersonalInfo("btype", value);
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
            intent.putExtra("bloodGroup", m_bloodGroup);
            setResult(RESULT_OK, intent);
			finish();
		}
		else
		{
			Toast.makeText(this, "修改失败！", Toast.LENGTH_LONG).show();
		}
	}

}
