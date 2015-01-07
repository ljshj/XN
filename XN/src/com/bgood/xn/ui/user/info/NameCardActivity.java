package com.bgood.xn.ui.user.info;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.IMRequest;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.ui.user.product.ShowcaseActivity;
import com.bgood.xn.ui.weiqiang.WeiqiangPersonActivity;
import com.bgood.xn.ui.xuanneng.XuanNengMainActivity;
import com.bgood.xn.utils.LogUtils;
import com.bgood.xn.view.BToast;
import com.bgood.xn.widget.TitleBar;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.activity.ChatActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/***
 * 
 * @todo:名片中心
 * @date:2014-11-25 上午10:28:09
 * @author:hg_liuzl@163.com
 */
public class NameCardActivity extends BaseActivity implements OnClickListener,TaskListenerWithState
{
	private String userId;
	private UserInfoBean user;
    private ImageView m_userIconImgV = null;  // 用户头像
    private TextView m_userNicteTv = null;  // 用户昵称
    private TextView m_identityTv = null;  // 性别
    private ImageView m_sexImgV = null;  // 性别
    private TextView m_userNumberTv = null;  // 用户账号信息
    private TextView m_icanTv = null;  // 我能
    private TextView m_ineedTv = null;  // 我想
    private TextView m_signName;	//个性签名
    private boolean isSelf = false;	//是否是自己本人
    
    private TextView tvWeiqiang;
    private TextView tvXuanneg;
    private TextView tvChuchuang;
	
    private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_user_card);
		(new TitleBar(mActivity)).initTitleBar("我的名片");
		findView();
		userId = getIntent().getStringExtra(UserInfoBean.KEY_USER_ID);
		if(null != userId){
			isSelf = userId.equals(String.valueOf(BGApp.mUserId));
			UserCenterRequest.getInstance().requestPersonInfo(this, mActivity, userId,true);
		}else{
			finish();
		}
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
	   
	    m_signName = (TextView) findViewById(R.id.tv_sign_name);
	    m_icanTv = (TextView) findViewById(R.id.tv_ican);
	    m_ineedTv = (TextView) findViewById(R.id.tv_iwant);
		
		findViewById(R.id.tv_add_attention).setOnClickListener(this);
		findViewById(R.id.tv_add_friend).setOnClickListener(this);
		findViewById(R.id.tv_call_message).setOnClickListener(this);
		
		tvWeiqiang = (TextView) findViewById(R.id.tv_weiqiang);
		tvWeiqiang.setOnClickListener(this);
		
		tvXuanneg = (TextView) findViewById(R.id.tv_xuanneng);
		tvXuanneg.setOnClickListener(this);
		
		tvChuchuang = (TextView) findViewById(R.id.tv_shop);
		tvChuchuang.setOnClickListener(this);
	}


	@Override
	public void onClick(View v)
	{
		Intent intent = null;
		switch (v.getId()) {
		 // 关注
        case R.id.tv_add_attention:
        	if(isSelf){
        		BToast.show(mActivity, "不能关注自己");
        	}else{
        		UserCenterRequest.getInstance().requestAttention(NameCardActivity.this, mActivity,userId,BGApp.mUserId,String.valueOf(0));
        	}
            break;
		case R.id.tv_add_friend:
			addContact();
			break;
		case R.id.tv_call_message:
			doChat();
			break;
		case R.id.tv_xuanneng:
			intent = new Intent(mActivity, XuanNengMainActivity.class);
			intent.putExtra(UserInfoBean.KEY_USER_ID, userId);
			startActivity(intent);
			
			break;
		case R.id.tv_weiqiang:
			intent = new Intent(mActivity, WeiqiangPersonActivity.class);
			intent.putExtra(UserInfoBean.KEY_USER_ID, userId);
			startActivity(intent);
			break;
		case R.id.tv_shop:
			intent = new Intent(mActivity, ShowcaseActivity.class);
			intent.putExtra(ShowcaseActivity.KEY_USER_ID, userId);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 
	 * @todo:添加聊天
	 * @date:2014-12-19 下午3:41:10
	 * @author:hg_liuzl@163.com
	 * @params:
	 */
	private void doChat() {
		if(BGApp.mUserId.equals(userId)){
			BToast.show(mActivity, "不能与自己聊天");
			return;
		}else if(!BGApp.getInstance().getFriendMapById().containsKey(userId)){
			BToast.show(mActivity, "请先成为好友再聊天");
			return;
		}else if(BGApp.getInstance().getFriendMapById().containsKey(userId)){
			Intent intent = new Intent(mActivity,ChatActivity.class);
			intent.putExtra("userId", userId);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		}
	}
	/**
	 * 
	 * @todo:添加好友 
	 * @date:2014-12-19 下午3:39:34
	 * @author:hg_liuzl@163.com
	 * @params:
	 */
	public void addContact(){
		if(BGApp.mUserId.equals(userId)){
			BToast.show(mActivity, "不能添加自己");
			return;
		}else if(BGApp.getInstance().getFriendMapById().containsKey(userId)){
			BToast.show(mActivity, "此用户已是你的好友");
			return;
		}else{
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在发送请求...");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
			new Thread(new Runnable() {
				public void run() {
					
					try {
						//demo写死了个reason，实际应该让用户手动填入
						EMContactManager.getInstance().addContact("bg"+userId, "bg"+BGApp.mUserId+"加个好友呗");
						LogUtils.print("------------------编号是-----------------------"+"bg"+userId);
						runOnUiThread(new Runnable() {
							public void run() {
								progressDialog.dismiss();
								BToast.show(mActivity, "请求添加好友成功");
							}
						});
					} catch (final Exception e) {
						runOnUiThread(new Runnable() {
							public void run() {
								progressDialog.dismiss();
								BToast.show(mActivity, "请求添加好友失败");
							}
						});
					}
				}
			}).start();
			
//			IMRequest.getInstance().requestFriendADD(NameCardActivity.this, mActivity,BGApp.mUserId, userId, "true");
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
        
        m_userNumberTv.setText("能能号:"+userDTO.username);
        m_signName.setText(userDTO.signature);
        m_ineedTv.setText(userDTO.ineed); // 我想
        m_icanTv.setText(userDTO.ican); // 我能
        tvWeiqiang.setText((isSelf?"我":"TA")+"的微墙");
        tvXuanneg.setText((isSelf?"我":"TA")+"的炫能");
        tvChuchuang.setText((isSelf?"我":"TA")+"的橱窗");
    }


	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			String strJson = bNetWork.getStrJson();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				switch (bNetWork.getMessageType()) {
				case 820001:
					user = JSON.parseObject(strJson, UserInfoBean.class);
					setData(user);
					if(!isSelf){
						(new TitleBar(mActivity)).initTitleBar(user.nickn+"的名片");
					}
				break;
				case 820004:
					BToast.show(mActivity, "关注成功");
				break;
//				case 850027:
//					BToast.show(mActivity, "加好友请求已经成功，等待对应回应");
//				break;

				default:
					break;
				}
			}
		}
	}
}
