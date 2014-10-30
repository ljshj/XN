package com.bgood.xn.ui.user.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.zhuozhong.bandgood.R;
import com.zhuozhong.bandgood.activity.BaseActivity;
import com.zhuozhong.bandgood.bean.Reulst;
import com.zhuozhong.bandgood.bean.UserDTO;
import com.zhuozhong.bandgood.messagemanager.UserCenterMessageManager;
import com.zhuozhong.util.WindowUtil;
import com.zhuozhong.zzframework.session.Frame.ReturnCode;

/**
 * 星座选择页面
 */
public class ConstellationActivity extends BaseActivity
{
    private Button m_backBtn = null;  // 返回
    private RadioGroup m_constellationRg = null;  // 性别分组
    private Button m_confirmBtn = null; // 确定按钮
    
    UserCenterMessageManager messageManager = UserCenterMessageManager.getInstance();

    private String m_constellation = "";
    private UserDTO m_userDTO = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_constellation);
        
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
        m_backBtn = (Button) findViewById(R.id.constellation_btn_back);
        m_constellationRg = (RadioGroup) findViewById(R.id.constellation_rg_constellation);
        m_confirmBtn = (Button) findViewById(R.id.constellation_btn_confirm);
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
                ConstellationActivity.this.finish();
            }
        });
        
        // 星座选择
        m_constellationRg.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if (checkedId == R.id.constellation_rb_bai_yang)
                {
                    m_constellation = "白羊座";
                }
                else if (checkedId == R.id.constellation_rb_jin_niu)
                {
                    m_constellation = "金牛座";
                }
                else if (checkedId == R.id.constellation_rb_shuang_zi)
                {
                    m_constellation = "双子座";
                }
                else if (checkedId == R.id.constellation_rb_ju_xie)
                {
                    m_constellation = "巨蟹座";
                }
                else if (checkedId == R.id.constellation_rb_shi_zi)
                {
                    m_constellation = "狮子座";
                }
                else if (checkedId == R.id.constellation_rb_chu_nv)
                {
                    m_constellation = "处女座";
                }
                else if (checkedId == R.id.constellation_rb_tian_cheng)
                {
                    m_constellation = "天秤座";
                }
                else if (checkedId == R.id.constellation_rb_tian_xie)
                {
                    m_constellation = "天蝎座";
                }
                else if (checkedId == R.id.constellation_rb_she_shou)
                {
                    m_constellation = "射手座";
                }
                else if (checkedId == R.id.constellation_rb_mo_xie)
                {
                    m_constellation = "魔蝎座";
                }
                
                else if (checkedId == R.id.constellation_rb_shui_ping)
                {
                    m_constellation = "水瓶座";
                }
                else if (checkedId == R.id.constellation_rb_shuang_yu)
                {
                    m_constellation = "双鱼座";
                }
            }
        });
        
        // 确定按钮
        m_confirmBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (m_constellation != null && !m_constellation.equals(""))
                {
                    loadData(m_constellation);
                }
                else
                {
                    Toast.makeText(ConstellationActivity.this, "请选择星座！", Toast.LENGTH_LONG).show();
                    return;
                }
                
            }
        });
    }
    
    private void setData()
    {
        if (m_userDTO.conste.equals("白羊座"))
        {
            m_constellationRg.check(R.id.constellation_rb_bai_yang);
        }
        else if (m_userDTO.conste.equals("金牛座"))
        {
            m_constellationRg.check(R.id.constellation_rb_jin_niu);
        }
        else if (m_userDTO.conste.equals("双子座"))
        {
            m_constellationRg.check(R.id.constellation_rb_shuang_zi);
        }
        else if (m_userDTO.conste.equals("巨蟹座"))
        {
            m_constellationRg.check(R.id.constellation_rb_ju_xie);
        }
        else if (m_userDTO.conste.equals("狮子座"))
        {
            m_constellationRg.check(R.id.constellation_rb_shi_zi);
        }
        else if (m_userDTO.conste.equals("处女座"))
        {
            m_constellationRg.check(R.id.constellation_rb_chu_nv);
        }
        else if (m_userDTO.conste.equals("天秤座"))
        {
            m_constellationRg.check(R.id.constellation_rb_tian_cheng);
        }
        else if (m_userDTO.conste.equals("天蝎座"))
        {
            m_constellationRg.check(R.id.constellation_rb_tian_xie);
        }
        else if (m_userDTO.conste.equals("射手座"))
        {
            m_constellationRg.check(R.id.constellation_rb_she_shou);
        }
        else if (m_userDTO.conste.equals("魔蝎座"))
        {
            m_constellationRg.check(R.id.constellation_rb_mo_xie);
        }
        else if (m_userDTO.conste.equals("水瓶座"))
        {
            m_constellationRg.check(R.id.constellation_rb_shui_ping);
        }
        else if (m_userDTO.conste.equals("双鱼座"))
        {
            m_constellationRg.check(R.id.constellation_rb_shuang_yu);
        }
    
    }
    /**
     * 加载数据方法
     * @param value 修改内容
     */
    private void loadData(String value)
    {
    	WindowUtil.getInstance().progressDialogShow(ConstellationActivity.this, "数据请求中...");
		messageManager.modifyPersonalInfo("conste", value);
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
            intent.putExtra("constell", m_constellation);
            setResult(RESULT_OK, intent);
			finish();
		}
		else
		{
			Toast.makeText(this, "修改失败！", Toast.LENGTH_LONG).show();
		}
	}
}
