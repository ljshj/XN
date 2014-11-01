package com.bgood.xn.ui.user.info;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.view.BToast;
import com.bgood.xn.widget.TitleBar;

/**
 * 星座选择页面
 */
public class ConstellationActivity extends BaseActivity implements TaskListenerWithState
{
    private RadioGroup m_constellationRg = null;  // 性别分组
    private String m_constellation;
    private UserInfoBean m_userDTO = null;
    private TitleBar titleBar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_constellation);
        titleBar = new TitleBar(mActivity);
        titleBar.initAllBar("星座");
        m_userDTO = (UserInfoBean) getIntent().getSerializableExtra(UserInfoBean.KEY_USER_BEAN);
        m_constellationRg = (RadioGroup) findViewById(R.id.constellation_rg_constellation);
        setListener();
        setData();
    }

    
    /**
     * 控件事件监听方法
     */
    private void setListener()
    {
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
        titleBar.rightBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (!TextUtils.isEmpty(m_constellation))
                {
                    loadData();
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
     */
    private void loadData()
    {
		UserCenterRequest.getInstance().requestUpdatePerson(this, mActivity, "conste", m_constellation);
    }
    


	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				BToast.show(mActivity, "修改成功");
				final UserInfoBean ufb = BGApp.mUserBean;
				ufb.birthday = m_constellation;
				BGApp.mUserBean = ufb;
				finish();
			}else{
				BToast.show(mActivity, "修改失败");
			}
		}
	}
}
