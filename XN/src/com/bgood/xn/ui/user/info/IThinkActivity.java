package com.bgood.xn.ui.user.info;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.view.BToast;
import com.bgood.xn.widget.TitleBar;


/**
 * 我想页面
 */
public class IThinkActivity extends BaseActivity
{
    private EditText m_contentEt = null;  // 内容
    private TextView m_wordcountTv = null;  // 字数显示
    private Button m_doneBtn = null;  // 确定按钮

    
    private String m_content = "";
    private UserInfoBean mUserBean;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_layout_i_miss);
        (new TitleBar(mActivity)).initTitleBar("我想");
        mUserBean = (UserInfoBean) getIntent().getSerializableExtra(UserInfoBean.KEY_USER_BEAN);;
        findView();
        setListener();
    }


	
	/**
	 * 控件初始化方法
	 */
    private void findView()
    {
        m_contentEt = (EditText) findViewById(R.id.imiss_edit_content);
        m_wordcountTv = (TextView) findViewById(R.id.imiss_tv_wordcount);
        m_doneBtn = (Button) findViewById(R.id.imiss_btn_done);
        if (!TextUtils.isEmpty(mUserBean.ineed))
        {
        	m_contentEt.setText(mUserBean.ineed);
        	m_contentEt.setSelection(mUserBean.ineed.length());
        }
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
            }
        });
        
        // 确定按钮
        m_doneBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                String content = m_contentEt.getText().toString().trim();
                if (TextUtils.isEmpty(content))
                {
                    BToast.show(mActivity, "内容不能为空!");
                    return;
                }else
                {
                	m_content = content;
                }

            }
        });
    }

}
