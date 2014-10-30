package com.bgood.xn.ui.user.info;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.ui.BaseActivity;
import com.squareup.picasso.Picasso;
import com.tencent.connect.UserInfo;

/**
 * 我的名片
 */
public class MyCardActivity extends BaseActivity implements OnClickListener
{
    private Button m_backBtn = null;  // 返回按钮
    private ProgressBar m_progressBar = null;  // 进度条
    private ScrollView m_showSl = null;  // 显示内容
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
	    m_backBtn = (Button) findViewById(R.id.user_card_btn_back);
	    m_progressBar = (ProgressBar) findViewById(R.id.user_card_progress);
	    m_showSl = (ScrollView) findViewById(R.id.user_card_sl);
	    m_infoRl = (RelativeLayout) findViewById(R.id.user_card_rl_user_info);
	    m_userIconImgV = (ImageView) findViewById(R.id.user_card_imgv_user_icon);
	    m_userNicteTv = (TextView) findViewById(R.id.user_card_tv_user_name);
	    m_sexImgV = (ImageView) findViewById(R.id.user_card_imgv_sex);
	    m_userNumberTv = (TextView) findViewById(R.id.user_card_tv_account_number);
	    m_followLl = (LinearLayout) findViewById(R.id.user_card_ll_follow);
	    m_addFriendLl = (LinearLayout) findViewById(R.id.user_card_ll_add_friend);
	    m_messageLl = (LinearLayout) findViewById(R.id.user_card_ll_message);
	    m_icanTv = (TextView) findViewById(R.id.user_card_tv_ican);
	    m_ineedTv = (TextView) findViewById(R.id.user_card_tv_imiss);
	}

	/**
	 * 控件事件监听方法
	 */
	private void setListener()
	{
	    m_backBtn.setOnClickListener(this);
	    m_infoRl.setOnClickListener(this);
	    m_followLl.setOnClickListener(this);
		m_addFriendLl.setOnClickListener(this);
		m_messageLl.setOnClickListener(this);
	}
	
	/**
     * 加载数据方法
     * @param userId 用户Id号
     */
    private void loadData(String userId)
    {
        messageManager.getPersonalData(userId);
    }

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		    // 返回按钮
            case R.id.user_card_btn_back:
            {
                finish();
                break;
            }
		    
		    // 账户信息
            case R.id.user_card_rl_user_info:
            {
//                Intent intent = new Intent(MyCardActivity.this, UserDataActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("userDTO", user);
//                intent.putExtras(bundle);
//                startActivity(intent);
                break;
            }
            
            // 关注
            case R.id.user_card_ll_follow:
            {
                Toast.makeText(MyCardActivity.this, "不能关注自己", Toast.LENGTH_SHORT).show();
//                WindowUtil.getInstance().progressDialogShow(this, "关注中...");
//                messageManager.follow(user.userId, UserManager.getInstance().m_user.userId, 0);
                break;
            }
		    
    		case R.id.user_card_ll_add_friend:
    		{
    		    Toast.makeText(MyCardActivity.this, "不能加自己为好友", Toast.LENGTH_SHORT).show();
//    			Bundle bundle = new Bundle();
//    			bundle.putSerializable("user", user);
//    			toActivity(AddFriendActivity.class, bundle);
    			break;
    		}	
    			
    		case R.id.user_card_ll_message:
    		{
    		    Toast.makeText(MyCardActivity.this, "不能与自己发起临时会话", Toast.LENGTH_SHORT).show();
//    			MessageCenterMessageDTO message = new MessageCenterMessageDTO();
//    			message.m_senderID = user.userId;
//    			message.m_senderName = user.userName;
//    			message.m_messageClass = MessageCenterMessageDTO.FDSMessageCenterMessage_Class.FDSMessageCenterMessage_Class_USER;
//    			message.m_messageType = MessageCenterMessageDTO.FDSMessageCenterMessage_Type.FDSMessageCenterMessage_Type_CHAT_PERSON;
//    			Bundle bundle = new Bundle();
//    			bundle.putSerializable("messageCenterMessage", message);
//    			toActivity(ChatActivity.class, bundle);
    			break;
    		}
		}
	}
	
	/**
     * 设置用户数据显示方法
     * @param userDTO 用户信息实体类
     */
    private void setData(UserInfoBean userDTO)
    {
        m_progressBar.setVisibility(View.GONE);
        m_showSl.setVisibility(View.VISIBLE);
        
        if (userDTO != null && userDTO.userIcon != null && !userDTO.userIcon.equals(""))
            Picasso.with(this).load(userDTO.userIcon).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(m_userIconImgV);
        
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
        
        m_userNumberTv.setText(userDTO.userAccount);
        m_ineedTv.setText(userDTO.ineed); // 我想
        m_icanTv.setText(userDTO.ican); // 我能
    }

	
	@Override
    public void getPersonalDataCB(Reulst result_state, UserDTO userDTO)
    {
        super.getPersonalDataCB(result_state, userDTO);
        
        if (result_state.resultCode == ReturnCode.RETURNCODE_OK)
        {
//            user.copy(userDTO);
            setData(userDTO);
        }
    }
	
	@Override
    public void followCB(Reulst result_state)
    {
        WindowUtil.getInstance().DismissAllDialog();
        messageManager.unregisterObserver(MyCardActivity.this);
        if (result_state.resultCode == ReturnCode.RETURNCODE_OK)
        {
                Toast.makeText(MyCardActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
        } else
        {
            Toast.makeText(MyCardActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
        }
    }

}
