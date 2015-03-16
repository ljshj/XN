package com.bgood.xn.ui.user;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.ui.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 设置页面
 */
public class SettingsActivity extends BaseActivity implements OnCheckedChangeListener
{

    private Button   m_backBtn          = null; // 返回按钮
    private CheckBox m_searchCB         = null; // 是否可被其它用户搜索
    private CheckBox m_openDataCB       = null; // 是否公开个人资料
    private CheckBox m_followCB         = null; // 是否接受关注
    private CheckBox m_temporaryChatCB  = null; // 是否接受临时聊天
    private CheckBox m_messagePushCB    = null; // 是否接受消息推送
    private CheckBox m_friendsProvingCB = null; // 加我为好友时是否需要验证
    private CheckBox m_soundHintCB      = null; // 消息声音提示

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_settings);
        
        findView();
        setListener();
    }

    /**
     * 控件初始化方法
     */
    private void findView()
    {
        m_backBtn = (Button) findViewById(R.id.settings_btn_back);
        m_searchCB = (CheckBox) findViewById(R.id.settings_cb_search);
        m_openDataCB = (CheckBox) findViewById(R.id.settings_cb_open_data);
        m_followCB = (CheckBox) findViewById(R.id.settings_cb_follow);
        m_temporaryChatCB = (CheckBox) findViewById(R.id.settings_cb_temporary_chat);
        m_messagePushCB = (CheckBox) findViewById(R.id.settings_cb_message_push);
        m_friendsProvingCB = (CheckBox) findViewById(R.id.settings_cb_friends_proving);
        m_soundHintCB = (CheckBox) findViewById(R.id.settings_cb_sound_hint);
    }
    
    /**
     * 控件事件监听方法
     */
    private void setListener()
    {
        m_backBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                SettingsActivity.this.finish();
            }
        });
        
        m_searchCB.setOnCheckedChangeListener(this);
        m_openDataCB.setOnCheckedChangeListener(this);
        m_followCB.setOnCheckedChangeListener(this);
        m_temporaryChatCB.setOnCheckedChangeListener(this);
        m_messagePushCB.setOnCheckedChangeListener(this);
        m_friendsProvingCB.setOnCheckedChangeListener(this);
        m_soundHintCB.setOnCheckedChangeListener(this);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        // 搜索
        if (buttonView.getId() == R.id.settings_cb_search)
        {
            if (isChecked)
            {
                Toast.makeText(SettingsActivity.this, "搜索打开", 0).show();
            }
            else
            {
                Toast.makeText(SettingsActivity.this, "搜索关闭", 0).show();
            }
        }
        
        // 个人资料
        else if (buttonView.getId() == R.id.settings_cb_open_data)
        {
            if (isChecked)
            {
                Toast.makeText(SettingsActivity.this, "个人资料打开", 0).show();
            }
            else
            {
                Toast.makeText(SettingsActivity.this, "个人资料关闭", 0).show();
            }
        } 
        
        // 关注
        else if (buttonView.getId() == R.id.settings_cb_follow)
        {
            if (isChecked)
            {
                Toast.makeText(SettingsActivity.this, "关注打开", 0).show();
            }
            else
            {
                Toast.makeText(SettingsActivity.this, "关注关闭", 0).show();
            }
        } 
        
        // 临时会话
        else if (buttonView.getId() == R.id.settings_cb_temporary_chat)
        {
            if (isChecked)
            {
                Toast.makeText(SettingsActivity.this, "临时会话打开", 0).show();
            }
            else
            {
                Toast.makeText(SettingsActivity.this, "临时会话关闭", 0).show();
            }
        } 
        
        // 消息推送
        else if (buttonView.getId() == R.id.settings_cb_message_push)
        {
            if (isChecked)
            {
                Toast.makeText(SettingsActivity.this, "消息推送打开", 0).show();
            }
            else
            {
                Toast.makeText(SettingsActivity.this, "消息推送关闭", 0).show();
            }
        } 
        
        // 用户验证
        else if (buttonView.getId() == R.id.settings_cb_friends_proving)
        {
            if (isChecked)
            {
                Toast.makeText(SettingsActivity.this, "验证打开", 0).show();
            }
            else
            {
                Toast.makeText(SettingsActivity.this, "验证关闭", 0).show();
            }
        } 
        
        // 声音
        else if (buttonView.getId() == R.id.settings_cb_sound_hint)
        {
            if (isChecked)
            {
                Toast.makeText(SettingsActivity.this, "声音打开", 0).show();
            }
            else
            {
                Toast.makeText(SettingsActivity.this, "声音关闭", 0).show();
            }
        } 
            
    }
}
