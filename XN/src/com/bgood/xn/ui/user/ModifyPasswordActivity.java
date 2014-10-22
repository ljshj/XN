package com.bgood.xn.ui.user;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.UserManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.ui.BaseActivity;

/**
 * 修改密码页面
 */
public class ModifyPasswordActivity extends BaseActivity
{

    private Button   m_backBtn           = null; // 返回
    private EditText m_oldPasswordEt     = null; // 原密码
    private EditText m_newPasswordEt     = null; // 新密码
    private EditText m_confirmPasswordEt = null; // 确认新密码
    private Button m_nextBtn = null; // 下一步按钮

    private String m_oldPassword = "";
    private String m_newPassword = "";
    private String m_confirmPassword = "";
    
    UserCenterMessageManager messageManager = UserCenterMessageManager.getInstance();
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_modify_password);
        
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
     * 控件初始化方法
     */
    private void findView()
    {
        m_backBtn = (Button) findViewById(R.id.modify_password_btn_back);
        m_oldPasswordEt = (EditText) findViewById(R.id.modify_password_et_old_password);
        m_newPasswordEt = (EditText) findViewById(R.id.modify_password_et_new_password);
        m_confirmPasswordEt = (EditText) findViewById(R.id.modify_password_et_confirm_password);
        m_nextBtn = (Button) findViewById(R.id.modify_password_btn_next);
    }
    
    private void setListener()
    {
        // 返回按钮
        m_backBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                ModifyPasswordActivity.this.finish();
            }
        });
        
        // 下一步按钮
        m_nextBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                checkInfo();
//                Intent intent = new Intent(ModifyPasswordActivity.this, ModifyPasswordCodeActivity.class);
//                startActivity(intent);
            }
        });
    }
    
    /**
     * 检查用户信息方法
     */
    private void checkInfo()
    {
        // 原密码
        String oldPassword = m_oldPasswordEt.getText().toString().trim();
        if (oldPassword == null || oldPassword.equals(""))
        {
            Toast.makeText(ModifyPasswordActivity.this, "密码不能为空！", 0).show();
            return;
        }
        else
        {
            m_oldPassword = oldPassword;
        }
        
        // 新密码
        String newPassword = m_newPasswordEt.getText().toString().trim();
        if (newPassword == null || newPassword.equals(""))
        {
            Toast.makeText(ModifyPasswordActivity.this, "密码不能为空！", 0).show();
            return;
        }
        else if (newPassword.length() < 6)
        {
            Toast.makeText(ModifyPasswordActivity.this, "密码不能少于六位数！", 0).show();
            return;
        }
        else
        {
            m_newPassword = newPassword;
        }
        
        // 确认密码查询
        String confirmPassword = m_confirmPasswordEt.getText().toString().trim();
        if (confirmPassword == null || confirmPassword.equals(""))
        {
            Toast.makeText(ModifyPasswordActivity.this, "确认密码不能为空！", 0).show();
            return;
        }
        else if (!confirmPassword.equals(m_newPassword))
        {
            Toast.makeText(ModifyPasswordActivity.this, "请输入正确的密码进行确认！", 0).show();
            return;
        }
        else
        {
            m_confirmPassword = confirmPassword;
        }
        
        // 隐藏软键盘
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(ModifyPasswordActivity.this
                        .getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        
        WindowUtil.getInstance().progressDialogShow(ModifyPasswordActivity.this, "数据请求中...");
        messageManager.modifyPassword(m_oldPassword, m_confirmPassword);
    }

    @Override
    public void modifyPasswordCB(Reulst result_state)
    {
        super.modifyPasswordCB(result_state);
        
        WindowUtil.getInstance().DismissAllDialog();
        
        if (result_state.resultCode == ReturnCode.RETURNCODE_OK)
        {
            Toast.makeText(this, "修改成功！", Toast.LENGTH_LONG).show();
            AppManager.getAppManager().finishActivity();
            AppManager.getAppManager().finishActivity();
            
            SharedPreferences userInfo_prf = getSharedPreferences("userInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = userInfo_prf.edit();
            editor.putBoolean(PreferenceKey.USER_LOGOUT_FLAG, true);
            editor.putString(PreferenceKey.USER_ID, "");
            UserManager.getInstance().m_user.userId = "";
            UserManager.getInstance().m_user.roleID = "";
            editor.commit();
        }
        else
        {
            Toast.makeText(this, "修改失败！", Toast.LENGTH_LONG).show();
        }
    }
    
    
    
}
