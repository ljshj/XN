package com.bgood.xn.ui.user.info;

import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
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
import com.bgood.xn.bean.UserBean;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.widget.TitleBar;


/**
 * 我能页面
 */
public class ICanActivity extends BaseActivity
{
    private EditText m_contentEt = null;  // 内容
    private Button m_doneBtn = null;  // 确定按钮
    private TextView tvWordCount;	//字数显示
    private String mContent;
    
    private UserBean mUserBean = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_layout_i_can);
        (new TitleBar(mActivity)).initTitleBar("我能");
        mUserBean = (UserBean) getIntent().getSerializableExtra(UserBean.KEY_USER_BEAN);;
        findView();
    }
	/**
	 * 控件初始化方法
	 */
    private void findView()
    {
        m_contentEt = (EditText) findViewById(R.id.ican_edit_content);
        tvWordCount = (TextView) findViewById(R.id.ican_tv_wordcount);
        m_doneBtn = (Button) findViewById(R.id.ican_btn_done);
        m_contentEt.setText(mUserBean.ican);
        m_contentEt.setSelection(mUserBean.ican.length());
    }

}
