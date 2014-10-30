package com.bgood.xn.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.widget.TitleBar;

/**
 * 账号中心页面
 */
public class AccountCenterActivity extends BaseActivity implements OnClickListener
{
    private TextView m_userPropertyTv = null;  // 用户属性
    private TextView m_experienceTv = null;  // 用户经验值
    private TextView m_totalExperienceTv = null;  // 等级总经验值
    private TextView m_creditsTv = null;  // 信用值
    private TextView m_highOpinionTv = null;  // 好评值
    private TextView m_gradeTv = null;  // 用户等级
    private TextView m_vermicelliTv = null;  // 粉丝数量
    
    private UserInfoBean mUserBean = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_layout_account_center);
        mUserBean = (UserInfoBean) getIntent().getSerializableExtra(UserInfoBean.KEY_USER_BEAN);
        (new TitleBar(mActivity)).initTitleBar("账号中心");
        initView();
        setData();
    }
    /**
     * 控件初始化方法
     */
    private void initView()
    {
        m_userPropertyTv = (TextView) findViewById(R.id.account_center_tv_user_property);
        m_experienceTv = (TextView) findViewById(R.id.account_center_tv_experience);
        m_totalExperienceTv = (TextView) findViewById(R.id.account_center_tv_total_experience);
        m_creditsTv = (TextView) findViewById(R.id.account_center_tv_credits);
        m_highOpinionTv = (TextView) findViewById(R.id.account_center_tv_high_opinion);
        m_gradeTv = (TextView) findViewById(R.id.account_center_tv_grade);
        m_vermicelliTv = (TextView) findViewById(R.id.account_center_tv_vermicelli);
        
        findViewById(R.id.tv_modify_pwd).setOnClickListener(this);
        findViewById(R.id.tv_edit_userinfo).setOnClickListener(this);
        findViewById(R.id.tv_quit_login).setOnClickListener(this);
    }

    /**
     * 设置用户数据
     */
    private void setData()
    {
        m_userPropertyTv.setText(mUserBean.level);
        m_experienceTv.setText(mUserBean.exp);
        m_totalExperienceTv.setText(mUserBean.exp);
        m_creditsTv.setText(mUserBean.credit);
        m_highOpinionTv.setText(mUserBean.favor);
        m_gradeTv.setText(mUserBean.level);
        m_vermicelliTv.setText(mUserBean.fansnumber);
    }
    
    @Override
    public void onClick(View v)
    {
    	Intent intent = null;
        switch(v.getId())
        {
            // 修改密码
            case R.id.tv_modify_pwd:
            	intent = new Intent(AccountCenterActivity.this, ModifyPasswordActivity.class);
                startActivity(intent);
                break;
            // 个人资料
            case R.id.tv_edit_userinfo:
            	intent = new Intent(AccountCenterActivity.this, PersonalDataActivity.class);
                intent.putExtra(UserInfoBean.KEY_USER_BEAN, mUserBean);
                startActivity(intent);
                break;
            // 注销
            case R.id.tv_quit_login:
            	pUitl.setAccountNumber(null);
            	pUitl.setAccountPassword(null);
            	intent = new Intent(mActivity, LoginActivity.class);
            	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            	startActivity(intent);
                break;
            
            default:
                break;
        }
    }
}
