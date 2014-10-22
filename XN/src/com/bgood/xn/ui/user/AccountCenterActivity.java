package com.bgood.xn.ui.user;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.UserManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.bean.UserBean;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.ui.MainActivity;

/**
 * 账号中心页面
 */
public class AccountCenterActivity extends BaseActivity implements OnClickListener
{
    private Button m_backBtn = null; // 返回按钮
    private TextView m_userPropertyTv = null;  // 用户属性
    private TextView m_experienceTv = null;  // 用户经验值
    private TextView m_totalExperienceTv = null;  // 等级总经验值
    private TextView m_creditsTv = null;  // 信用值
    private TextView m_highOpinionTv = null;  // 好评值
    private TextView m_gradeTv = null;  // 用户等级
    private TextView m_vermicelliTv = null;  // 粉丝数量
    private RelativeLayout m_modifyPasswordRl = null;  // 密码修改
    private RelativeLayout m_editPersonalDataRl = null;  // 编辑个人资料
    private RelativeLayout m_logoutRl = null;  // 退出登录
    
    private UserBean mUserBean = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_account_center);
        
        mUserBean = (UserBean) getIntent().getSerializableExtra("userDTO");
        initView();
        setData();
    }
    /**
     * 控件初始化方法
     */
    private void initView()
    {
        m_backBtn = (Button) findViewById(R.id.account_center_btn_back);
        m_userPropertyTv = (TextView) findViewById(R.id.account_center_tv_user_property);
        m_experienceTv = (TextView) findViewById(R.id.account_center_tv_experience);
        m_totalExperienceTv = (TextView) findViewById(R.id.account_center_tv_total_experience);
        m_creditsTv = (TextView) findViewById(R.id.account_center_tv_credits);
        m_highOpinionTv = (TextView) findViewById(R.id.account_center_tv_high_opinion);
        m_gradeTv = (TextView) findViewById(R.id.account_center_tv_grade);
        m_vermicelliTv = (TextView) findViewById(R.id.account_center_tv_vermicelli);
        m_modifyPasswordRl = (RelativeLayout) findViewById(R.id.account_center_rl_modify_password);
        m_editPersonalDataRl = (RelativeLayout) findViewById(R.id.account_center_rl_edit_personal_data);
        m_logoutRl = (RelativeLayout) findViewById(R.id.account_center_rl_logout);
        m_backBtn.setOnClickListener(this);
        m_modifyPasswordRl.setOnClickListener(this);
        m_editPersonalDataRl.setOnClickListener(this);
        m_logoutRl.setOnClickListener(this);
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
        switch(v.getId())
        {
            case R.id.account_center_btn_back:
                AccountCenterActivity.this.finish();
                break;
            // 修改密码
            case R.id.account_center_rl_modify_password:
                Intent intent1 = new Intent(AccountCenterActivity.this, ModifyPasswordActivity.class);
                startActivity(intent1);
                break;
            // 个人资料
            case R.id.account_center_rl_edit_personal_data:
                Intent intent2 = new Intent(AccountCenterActivity.this, PersonalDataActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("userDTO", mUserBean);
                intent2.putExtras(bundle);
                startActivity(intent2);
                break;
            
            // 注销
            case R.id.account_center_rl_logout:
                break;
            
            default:
                break;
        }
    }
}
