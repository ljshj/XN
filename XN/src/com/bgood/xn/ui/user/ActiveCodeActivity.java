package com.bgood.xn.ui.user;
//package com.bgood.xn.ui.user;
//
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bgood.xn.R;
//import com.bgood.xn.ui.BaseActivity;
//
//
///**
// * 修改密码——验证码确认页面
// */
//public class ModifyPasswordCodeActivity extends BaseActivity
//{
//    private Button m_backBtn = null;  // 返回按钮
//    private TextView m_phoneTv = null;  // 手机号提示
//    private EditText m_codeEt = null;  // 验证码
//    private Button m_doneBtn = null;  // 完成按钮
//    
//    private String m_code = "";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_modify_password_code);
//        
//        findView();
//        setListener();
//    }
//
//    /**
//     * 控件初始化方法
//     */
//    private void findView()
//    {
//        m_backBtn = (Button) findViewById(R.id.modify_password_code_btn_back);
//        m_phoneTv = (TextView) findViewById(R.id.modify_password_code_tv_phone);
//        m_codeEt = (EditText) findViewById(R.id.modify_password_code_et_code);
//        m_doneBtn = (Button) findViewById(R.id.modify_password_code_btn_done);
//    }
//    
//    /**
//     * 控件事件监听方法
//     */
//    private void setListener()
//    {
//        // 返回
//        m_backBtn.setOnClickListener(new OnClickListener()
//        {
//            
//            @Override
//            public void onClick(View v)
//            {
//                ModifyPasswordCodeActivity.this.finish();
//            }
//        });
//        
//        // 完成
//        m_doneBtn.setOnClickListener(new OnClickListener()
//        {
//            
//            @Override
//            public void onClick(View v)
//            {
//                checkInfo();
//            }
//        });
//    }
//    
//    /**
//     * 检查用户信息方法
//     */
//    private void checkInfo()
//    {
//        // 用户手机号码查询
//        String code = m_codeEt.getText().toString().trim();
//        if (code == null || code.equals(""))
//        {
//            Toast.makeText(ModifyPasswordCodeActivity.this, "验证码不能为空！", 0).show();
//            return;
//        }
//        else
//        {
//            m_code = code;
//        }
//        
//        // 隐藏软键盘
//        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
//                .hideSoftInputFromWindow(ModifyPasswordCodeActivity.this
//                        .getCurrentFocus().getWindowToken(),
//                        InputMethodManager.HIDE_NOT_ALWAYS);
//    }
//}
