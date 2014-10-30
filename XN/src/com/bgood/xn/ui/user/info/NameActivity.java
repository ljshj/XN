package com.bgood.xn.ui.user.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.widget.CEditText;

/**
 * 修改姓名页面
 */
public class NameActivity extends BaseActivity
{
    private Button m_backBtn = null;  // 返回按钮
    private CEditText m_nameEt = null;  // 昵称编辑框
    private Button m_doneBtn = null;  // 确定按钮
    
    
    private String m_name = "";
    private UserInfoBean m_userDTO = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_name);
        
        mUserBean = (UserInfoBean) getIntent().getSerializableExtra(UserInfoBean.KEY_USER_BEAN);
        findView();
        setListener();
    }
    
    /**
     * 控件初始化方法
     */
    private void findView()
    {
        m_backBtn = (Button) findViewById(R.id.name_btn_back);
        m_nameEt = (CEditText) findViewById(R.id.name_et_name);
        m_doneBtn = (Button) findViewById(R.id.name_btn_done);
        
        m_nameEt.setText(m_userDTO.nickn);
        m_nameEt.setSelection(m_userDTO.nickn.length());
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
                NameActivity.this.finish();
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
        String name = m_nameEt.getText().toString().trim();
        if (name == null || name.equals(""))
        {
            Toast.makeText(NameActivity.this, "昵称不能为空！", 0).show();
            return;
        }
        
        else if (name.length() > 8)
        {
            Toast.makeText(NameActivity.this, "昵称不能过长！", 0).show();
            return;
        }
        
        else
        {
            m_name = name;
        }
        
        // 隐藏软键盘
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(NameActivity.this
                        .getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        
        WindowUtil.getInstance().progressDialogShow(NameActivity.this, "数据请求中...");
		messageManager.modifyPersonalInfo("nickname", m_name);
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
            intent.putExtra("userName", m_name);
            setResult(RESULT_OK, intent);
			finish();
		}
		else
		{
			Toast.makeText(this, "修改失败！", Toast.LENGTH_LONG).show();
		}
	}
}
