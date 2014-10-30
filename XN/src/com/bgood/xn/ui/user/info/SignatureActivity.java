package com.bgood.xn.ui.user.info;

import android.content.Intent;
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

import com.bgood.xn.R;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.widget.TitleBar;

/**
 * 个性签名页面
 */
public class SignatureActivity extends BaseActivity
{
    private EditText m_contentEt = null;  // 内容
    private TextView m_wordcountTv = null;  // 字数显示
    private Button m_doneBtn = null;  // 确定按钮
    private String m_content = "";
    private UserInfoBean m_userDTO = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signature);
        (new TitleBar(mActivity)).initTitleBar("个性签名");
        m_userDTO = (UserInfoBean) getIntent().getSerializableExtra(UserInfoBean.KEY_USER_BEAN);
        findView();
        setListener();
    }

	/**
	 * 控件初始化方法
	 */
    private void findView()
    {
        m_contentEt = (EditText) findViewById(R.id.signature_edit_content);
        m_wordcountTv = (TextView) findViewById(R.id.signature_tv_wordcount);
        m_doneBtn = (Button) findViewById(R.id.signature_btn_done);
        
        m_contentEt.setText(m_userDTO.signature); 
        m_contentEt.setSelection(m_userDTO.signature.length()); 
    }
    
    /**
     * 控件监听事件方法
     */
    private void setListener()
    {
      
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
                m_wordcountTv.setText(calculateLength(SignatureActivity.this.m_contentEt.getText().toString()) + "/" + 30);
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
                    Toast.makeText(SignatureActivity.this, "个性签名不能为空！", 0).show();
                    return;
                }
                else
                {
                	m_content = content;
                }
                
                // 隐藏软键盘
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SignatureActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				WindowUtil.getInstance().progressDialogShow(SignatureActivity.this, "数据请求中...");
				messageManager.modifyPersonalInfo("signature", m_content);
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
			Intent intent = getIntent();
            intent.putExtra("signature", m_content);
            setResult(RESULT_OK, intent);
			finish();
		}
		else
		{
			Toast.makeText(this, "修改失败！", Toast.LENGTH_LONG).show();
		}
	}
}
