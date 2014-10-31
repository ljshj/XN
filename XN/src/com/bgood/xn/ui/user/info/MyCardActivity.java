package com.bgood.xn.ui.user.info;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.widget.TitleBar;
import com.squareup.picasso.Picasso;

/**
 * 我的名片
 */
public class MyCardActivity extends BaseActivity implements OnClickListener,TaskListenerWithState
{
    private RelativeLayout m_infoRl = null;  // 个人信息
    private ImageView m_userIconImgV = null;  // 用户头像
    private TextView m_userNicteTv = null;  // 用户昵称
    private ImageView m_sexImgV = null;  // 性别
    private TextView m_userNumberTv = null;  // 用户账号信息
    private LinearLayout m_followLl = null;  // 立即关注
    private LinearLayout m_addFriendLl = null;  // 加为好友
    private LinearLayout m_messageLl = null;  // 聊天
    private TextView m_icanTv = null;  // 我能
    private TextView m_ineedTv = null;  // 我想
    
	private UserInfoBean user;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_user_card);
		(new TitleBar(mActivity)).initTitleBar("我的名片");
		user = (UserInfoBean) getIntent().getSerializableExtra(UserInfoBean.KEY_USER_BEAN);
		findView();
		setListener();
		setData(user);
	}

	/**
	 * 控件初始化方法
	 */
	private void findView()
	{
//	    m_infoRl = (RelativeLayout) findViewById(R.id.user_card_rl_user_info);
//	    m_userIconImgV = (ImageView) findViewById(R.id.user_card_imgv_user_icon);
//	    m_userNicteTv = (TextView) findViewById(R.id.user_card_tv_user_name);
//	    m_sexImgV = (ImageView) findViewById(R.id.user_card_imgv_sex);
//	    m_userNumberTv = (TextView) findViewById(R.id.user_card_tv_account_number);
//	    m_followLl = (LinearLayout) findViewById(R.id.user_card_ll_follow);
//	    m_addFriendLl = (LinearLayout) findViewById(R.id.user_card_ll_add_friend);
//	    m_messageLl = (LinearLayout) findViewById(R.id.user_card_ll_message);
//	    m_icanTv = (TextView) findViewById(R.id.user_card_tv_ican);
//	    m_ineedTv = (TextView) findViewById(R.id.user_card_tv_imiss);
	}

	/**
	 * 控件事件监听方法
	 */
	private void setListener()
	{
	    m_infoRl.setOnClickListener(this);
	    m_followLl.setOnClickListener(this);
		m_addFriendLl.setOnClickListener(this);
		m_messageLl.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
//		    // 账户信息
//            case R.id.user_card_rl_user_info:
//                break;
            // 关注
            case R.id.tv_add_attention:
                Toast.makeText(MyCardActivity.this, "不能关注自己", Toast.LENGTH_SHORT).show();
                break;
    		case R.id.tv_add_friend:
    		    Toast.makeText(MyCardActivity.this, "不能加自己为好友", Toast.LENGTH_SHORT).show();
    			break;
    		case R.id.tv_call_message:
    		    Toast.makeText(MyCardActivity.this, "不能与自己发起临时会话", Toast.LENGTH_SHORT).show();
    			break;
		}
	}
	
	/**
     * 设置用户数据显示方法
     * @param userDTO 用户信息实体类
     */
    private void setData(UserInfoBean userDTO)
    {
        
        if (TextUtils.isEmpty(userDTO.photo)){
            Picasso.with(this).load(userDTO.photo).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(m_userIconImgV);
        }
        
        // 昵称
        m_userNicteTv.setText(userDTO.nickn);
        
        // 性别
        if (userDTO.sex == 1)
        {
            m_sexImgV.setImageResource(R.drawable.img_common_sex_male);
        }
        else if (userDTO.sex == 2)
        {
            m_sexImgV.setImageResource(R.drawable.img_common_sex_female);
        }
        else
        {
            m_sexImgV.setVisibility(View.GONE);
        }
        
        m_userNumberTv.setText(userDTO.username);
        m_ineedTv.setText(userDTO.ineed); // 我想
        m_icanTv.setText(userDTO.ican); // 我能
    }


	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		
	}

}
