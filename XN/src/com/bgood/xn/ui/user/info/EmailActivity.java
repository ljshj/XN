package com.bgood.xn.ui.user.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.ui.BaseActivity;

/**
 * 修改邮箱页面
 */
public class EmailActivity extends BaseActivity
{
    private Button m_backBtn = null;  // 返回按钮
    private CEditText m_emailEt = null;  // 昵称编辑框
    private Button m_doneBtn = null;  // 确定按钮
    
    UserCenterMessageManager messageManager = UserCenterMessageManager.getInstance();

    private String m_email = "";
    private UserDTO m_userDTO = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_email);
        
        getIntentData();
        findView();
        setListener();
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
        m_backBtn = (Button) findViewById(R.id.email_btn_back);
        m_emailEt = (CEditText) findViewById(R.id.email_et_email);
        m_doneBtn = (Button) findViewById(R.id.email_btn_done);
        
        m_emailEt.setText(m_userDTO.email);
        m_emailEt.setSelection(m_userDTO.email.length());
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
                EmailActivity.this.finish();
            }
        });
        
        // 确定按钮
        m_doneBtn.setOnClickListener(new OnClickListener()
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
        String email = m_emailEt.getText().toString().trim();
        if (email == null || email.equals(""))
        {
            Toast.makeText(EmailActivity.this, "邮箱不能为空！", 0).show();
            return;
        }
        else if (!RegularExpression.isEmail(email))
        {
            Toast.makeText(EmailActivity.this, "邮箱格式不正确！", 0).show();
            return;
        }
        else
        {
            m_email = email;
        }
        
        // 隐藏软键盘
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(EmailActivity.this
                        .getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        
        loadData(m_email);
    }
    
    /**
     * 加载数据方法
     * @param value 修改内容
     */
    private void loadData(String value)
    {
    	WindowUtil.getInstance().progressDialogShow(EmailActivity.this, "数据请求中...");
		messageManager.modifyPersonalInfo("email", value);
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
            intent.putExtra("email", m_email);
            setResult(RESULT_OK, intent);
			finish();
		}
		else
		{
			Toast.makeText(this, "修改失败！", Toast.LENGTH_LONG).show();
		}
	}

}
