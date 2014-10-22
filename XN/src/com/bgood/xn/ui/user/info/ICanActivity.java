package com.bgood.xn.ui.user.info;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuozhong.bandgood.R;
import com.zhuozhong.bandgood.activity.BaseActivity;
import com.zhuozhong.bandgood.bean.Reulst;
import com.zhuozhong.bandgood.bean.UserDTO;
import com.zhuozhong.bandgood.messagemanager.UserCenterMessageManager;
import com.zhuozhong.manager.UserManager;
import com.zhuozhong.util.WindowUtil;
import com.zhuozhong.zzframework.session.Frame.ReturnCode;


/**
 * 我能页面
 */
public class ICanActivity extends BaseActivity
{
    private Button m_backBtn = null;  // 返回按钮
    private EditText m_contentEt = null;  // 内容
    private TextView m_wordcountTv = null;  // 字数显示
    private Button m_doneBtn = null;  // 确定按钮
    
    UserCenterMessageManager messageManager = UserCenterMessageManager.getInstance();
    
    private String m_content = "";
    
    private UserDTO m_userDTO = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_i_can);
        
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
	 * 得到上层传值数据方法
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
        m_backBtn = (Button) findViewById(R.id.ican_btn_back);
        m_contentEt = (EditText) findViewById(R.id.ican_edit_content);
        m_wordcountTv = (TextView) findViewById(R.id.ican_tv_wordcount);
        m_doneBtn = (Button) findViewById(R.id.ican_btn_done);
        m_contentEt.setText(m_userDTO.ican);
        m_contentEt.setSelection(m_userDTO.ican.length());
    }
    
    /**
     * 控件监听事件方法
     */
    private void setListener()
    {
        // 取消按钮
        m_backBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                ICanActivity.this.finish();
            }
        });
        
        m_contentEt.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable paramEditable)
            {
            }

            public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
            {
            }

            public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
            {
                m_wordcountTv.setText(calculateLength(ICanActivity.this.m_contentEt.getText().toString()) + "/" + 30);
            }
        });
        
        // 确定按钮
        m_doneBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                String content = m_contentEt.getText().toString().trim();
                if (content == null || content.equals(""))
                {
                    Toast.makeText(ICanActivity.this, "内容不能为空！", 0).show();
                    return;
                }
                else
                {
                	m_content = content;
                }
                
                // 隐藏软键盘
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ICanActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				WindowUtil.getInstance().progressDialogShow(ICanActivity.this, "数据请求中...");
				messageManager.modifyPersonalInfo("ican", m_content);
            }
        });
    }
    
    /** 
     * 计算分享内容的字数，一个汉字=两个英文字母，一个中文标点=两个英文标点 注意：
     *      该函数的不适用于对单个字符进行计算，因为单个字符四舍五入后都是1 
     */  
    private long calculateLength(CharSequence c) 
    {  
        double len = 0;  
        for (int i = 0; i < c.length(); i++) 
        {  
            
            if (c.toString().substring(i, i+1).equals(" "))
            {
                len += 0.5;
            }
            int tmp = (int) c.charAt(i);  
            if (tmp > 0 && tmp < 127) 
            {  
                len += 0.5;  
            }
            else 
            {  
                len++;  
            }  
        }  
        return Math.round(len);  
    }

	@Override
	public void modifyPersonalInfoCB(Reulst result_state)
	{
		super.modifyPersonalInfoCB(result_state);
		WindowUtil.getInstance().DismissAllDialog();
		
		if (result_state.resultCode == ReturnCode.RETURNCODE_OK)
		{
			Toast.makeText(this, "修改成功！", Toast.LENGTH_LONG).show();
			m_userDTO.ican = m_content;
			UserManager.getInstance().m_user.copy(m_userDTO);
			finish();
		}
		else
		{
			Toast.makeText(this, "修改失败！", Toast.LENGTH_LONG).show();
		}
	}
    
}
