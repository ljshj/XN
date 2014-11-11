package com.bgood.xn.ui.user.info;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.widget.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/**
 * 我的名片
 */
public class MyCardActivity extends BaseActivity implements OnClickListener,TaskListenerWithState
{
	private UserInfoBean user;
    private ImageView m_userIconImgV = null;  // 用户头像
    private TextView m_userNicteTv = null;  // 用户昵称
    private TextView m_identityTv = null;  // 性别
    private ImageView m_sexImgV = null;  // 性别
    private TextView m_userNumberTv = null;  // 用户账号信息
    private TextView m_icanTv = null;  // 我能
    private TextView m_ineedTv = null;  // 我想
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		user = (UserInfoBean) getIntent().getSerializableExtra(UserInfoBean.KEY_USER_BEAN);
		setContentView(R.layout.layout_user_card);
		(new TitleBar(mActivity)).initTitleBar("我的名片");
		findView();
		setData(user);
	}

	/**
	 * 控件初始化方法
	 */
	private void findView()
	{
	    m_userIconImgV = (ImageView) findViewById(R.id.user_center_imgv_user_icon);
	    m_userNicteTv = (TextView) findViewById(R.id.user_center_tv_user_name);
	    m_sexImgV = (ImageView) findViewById(R.id.iv_sex);
	    m_identityTv = (TextView) findViewById(R.id.tv_identity);
	    m_userNumberTv = (TextView) findViewById(R.id.user_center_tv_account_number);
	   
	    m_icanTv = (TextView) findViewById(R.id.tv_ican);
	    m_ineedTv = (TextView) findViewById(R.id.tv_iwant);
		
		
		
		findViewById(R.id.tv_add_attention).setOnClickListener(this);
		findViewById(R.id.tv_add_friend).setOnClickListener(this);
		findViewById(R.id.tv_call_message).setOnClickListener(this);
		findViewById(R.id.tv_xuanneng).setOnClickListener(this);
		findViewById(R.id.tv_weiqiang).setOnClickListener(this);
		findViewById(R.id.tv_shop).setOnClickListener(this);
	}


	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
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
		ImageLoader mImageLoader;
		DisplayImageOptions options;
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.icon_default)
		.showImageForEmptyUri(R.drawable.icon_default)
		.cacheInMemory()
		.cacheOnDisc()
		.build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(mActivity));
		
        mImageLoader.displayImage(userDTO.photo,m_userIconImgV, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete() {
				Animation anim = AnimationUtils.loadAnimation(mActivity, R.anim.fade_in);
				m_userIconImgV.setAnimation(anim);
				anim.start();
			}
		});
        
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
        
        m_identityTv.setText(mActivity.getResources().getString(R.string.account_vip, userDTO.level));
        m_identityTv.setVisibility(userDTO.level < 1 ? View.GONE:View.VISIBLE);
        
        m_userNumberTv.setText(userDTO.username);
        m_ineedTv.setText(userDTO.ineed); // 我想
        m_icanTv.setText(userDTO.ican); // 我能
    }


	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		
	}

}
