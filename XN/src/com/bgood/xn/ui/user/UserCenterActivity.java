package com.bgood.xn.ui.user;import java.util.ArrayList;import android.content.Intent;import android.os.Bundle;import android.text.TextUtils;import android.view.View;import android.view.View.OnClickListener;import android.widget.LinearLayout;import android.widget.RelativeLayout;import android.widget.TextView;import com.alibaba.fastjson.JSON;import com.bgood.xn.R;import com.bgood.xn.bean.AttentionBean;import com.bgood.xn.bean.UserInfoBean;import com.bgood.xn.network.BaseNetWork;import com.bgood.xn.network.BaseNetWork.ReturnCode;import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;import com.bgood.xn.network.http.HttpRequestInfo;import com.bgood.xn.network.http.HttpResponseInfo;import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;import com.bgood.xn.network.request.UserCenterRequest;import com.bgood.xn.system.BGApp;import com.bgood.xn.ui.base.BaseActivity;import com.bgood.xn.ui.user.info.ICanActivity;import com.bgood.xn.ui.user.info.IThinkActivity;import com.bgood.xn.ui.user.info.SignatureActivity;import com.bgood.xn.ui.user.more.MoreActivity;import com.bgood.xn.ui.user.product.ShowcaseActivity;import com.bgood.xn.ui.weiqiang.WeiqiangPersonActivity;import com.bgood.xn.ui.xuanneng.XuanNengMainActivity;import com.bgood.xn.utils.ImgUtils;import com.bgood.xn.view.RoundImageView;import com.bgood.xn.widget.TitleBar;import com.umeng.analytics.MobclickAgent;/** * @todo:个人中心 * @author:hg_liuzl@163.com */public class UserCenterActivity extends BaseActivity implements OnClickListener,TaskListenerWithState {	public static final String TAG = "UserCenterFragment";	private RelativeLayout m_userInfoRl = null; // 用户信息	private RoundImageView m_userIconImgV = null; // 用户图片	private TextView m_userNameTv = null; // 用户名	private TextView m_identityTV = null; // 用户身份	private TextView m_accountNumberTv = null; // 用户账号	private LinearLayout m_followLl = null; // 我的关注布局	private TextView m_followTv = null; // 我的关注	private LinearLayout m_vermicelliLl = null; // 我的粉丝布局	private TextView m_vermicelliTv = null; // 我的粉丝	private TextView m_iMissTv = null; // 我想	private TextView m_signname = null; // 签名	private TextView m_iCanTv = null; // 我能	private TextView tvXuanneng,tvWeiqiang;	private ArrayList<String> imgList = new ArrayList<String>(); //存储图片查看器的图片地址	 	 	@Override	protected void onCreate(Bundle savedInstanceState) {		super.onCreate(savedInstanceState);		setContentView(R.layout.layout_user_center);		TitleBar bar = new TitleBar(mActivity);		bar.initTitleBar("个人中心");		bar.setBackBtnVisible(View.GONE);		initViews();		loadData();	}	/**加载数据*/	private void loadData() {		if(BGApp.isUserLogin ){			if(null == BGApp.mUserBean){				UserCenterRequest.getInstance().requestPersonInfo(this, mActivity, String.valueOf(BGApp.mUserId),true);			}		}	}		@Override	protected void onPause() {		super.onPause();		MobclickAgent.onPause(this);	}		@Override	public void onResume() {		super.onResume();		MobclickAgent.onResume(this);		if(null != BGApp.mUserBean){			setData(BGApp.mUserBean);			}	}		private void initViews() {		m_userInfoRl = (RelativeLayout) findViewById(R.id.user_center_rl_user_info);		m_userIconImgV = (RoundImageView) findViewById(R.id.user_center_imgv_user_icon);				m_userNameTv = (TextView) findViewById(R.id.user_center_tv_user_name);		m_identityTV = (TextView) findViewById(R.id.tv_identity);		m_accountNumberTv = (TextView) findViewById(R.id.user_center_tv_account_number);		m_followLl = (LinearLayout) findViewById(R.id.user_center_ll_follow);		m_followTv = (TextView) findViewById(R.id.user_center_tv_follow);		m_vermicelliLl = (LinearLayout) findViewById(R.id.user_center_ll_vermicelli);		m_vermicelliTv = (TextView) findViewById(R.id.user_center_tv_vermicelli);		m_iMissTv = (TextView) findViewById(R.id.user_center_tv_imiss);		m_iCanTv = (TextView) findViewById(R.id.user_center_tv_ican);		m_signname = (TextView) findViewById(R.id.tv_sign_name);		//		tvXuanneng = (TextView) findViewById(R.id.tv_xuanneng);//		tvXuanneng.setOnClickListener(this);//		tvXuanneng.setVisibility(View.GONE);//		//		//		tvWeiqiang = (TextView) findViewById(R.id.tv_weiqiang);//		tvWeiqiang.setOnClickListener(this);//		tvWeiqiang.setVisibility(View.GONE);				findViewById(R.id.tv_shop).setOnClickListener(this);//		findViewById(R.id.tv_settings).setOnClickListener(this);		findViewById(R.id.tv_more).setOnClickListener(this);				m_userInfoRl.setOnClickListener(this);		m_followLl.setOnClickListener(this);		m_vermicelliLl.setOnClickListener(this);		m_iMissTv.setOnClickListener(this);		m_iCanTv.setOnClickListener(this);		m_signname.setOnClickListener(this);	}	/**	 * 设置用户数据显示方法	 * @param userDTO 用户信息实体类	 */	private void setData(UserInfoBean userBean)	{		    	if(!TextUtils.isEmpty(userBean.bigPhoto)){    		imgList.add(userBean.bigPhoto);    		m_userIconImgV.setOnClickListener(this);    	}				BGApp.getInstance().setImageSqure(userBean.photo,m_userIconImgV);		// 昵称		m_userNameTv.setText(userBean.nickn);		// 性别		m_accountNumberTv.setCompoundDrawables(null, null, getResources().getDrawable(userBean.sex == 1 ? R.drawable.img_common_sex_male:R.drawable.img_common_sex_female), null);			//	m_identityImgV.setBackgroundResource(resid);				m_accountNumberTv.setText(getString(R.string.account_number, userBean.username));						m_identityTV.setText(getString(R.string.account_vip, userBean.level));		m_identityTV.setVisibility(userBean.level<1 ? View.GONE:View.VISIBLE);						m_signname.setText(userBean.signature);				m_followTv.setText(userBean.guanzhu); // 我的关注		m_vermicelliTv.setText(userBean.fansnumber); // 我的粉丝		m_iMissTv.setText(userBean.ineed); // 我想		m_iCanTv.setText(userBean.ican); // 我能	}		@Override	public void onClick(View v) {		Intent intent = null;		switch (v.getId())		{					case R.id.user_center_imgv_user_icon:	//查看用户图片				ImgUtils.imageBrower(0, imgList, mActivity);				break;			// 账户信息			case R.id.user_center_rl_user_info:				MobclickAgent.onEvent(this,"me_account_center_click");				intent = new Intent(mActivity, AccountCenterActivity.class);				Bundle bundle = new Bundle();				bundle.putSerializable(UserInfoBean.KEY_USER_BEAN, BGApp.mUserBean);				intent.putExtras(bundle);				startActivity(intent);				break;				//个性签名			case R.id.tv_sign_name:				MobclickAgent.onEvent(this,"xuanneng_joke_order_click");				intent = new Intent(mActivity, SignatureActivity.class);                intent.putExtra(UserInfoBean.KEY_USER_BEAN, BGApp.mUserBean);                startActivity(intent);                break;			// 我的关注			case R.id.user_center_ll_follow:				MobclickAgent.onEvent(this,"me_attention_click");				intent = new Intent(mActivity, AttentionActivity.class);				intent.putExtra(AttentionActivity.KEY_ATTENTION, AttentionBean.ATTENTION);				startActivity(intent);				break;			// 我的粉丝			case R.id.user_center_ll_vermicelli:				MobclickAgent.onEvent(this,"me_fans_click");				intent = new Intent(mActivity, AttentionActivity.class);				intent.putExtra(AttentionActivity.KEY_ATTENTION, AttentionBean.FANS);				startActivity(intent);				break;			// 我想			case R.id.user_center_tv_imiss:				intent = new Intent(mActivity, IThinkActivity.class);				intent.putExtra(UserInfoBean.KEY_USER_BEAN, BGApp.mUserBean);				startActivity(intent);				break;			// 我能			case R.id.user_center_tv_ican:				intent = new Intent(mActivity, ICanActivity.class);				intent.putExtra(UserInfoBean.KEY_USER_BEAN, BGApp.mUserBean);				startActivity(intent);				break;			// 我的微墙			case R.id.tv_weiqiang:				MobclickAgent.onEvent(UserCenterActivity.this,"weiqiang_me_click");				intent = new Intent(mActivity, WeiqiangPersonActivity.class);				intent.putExtra(UserInfoBean.KEY_USER_ID, String.valueOf(BGApp.mUserId));				startActivity(intent);				break;							// 橱窗			case R.id.tv_shop:				MobclickAgent.onEvent(this,"xuanneng_joke_order_click");				intent = new Intent(mActivity, ShowcaseActivity.class);				intent.putExtra(ShowcaseActivity.KEY_USER_ID, String.valueOf(BGApp.mLoginBean.userid));				startActivity(intent);				break;//			// 我的炫能//			case R.id.tv_xuanneng://				intent = new Intent(mActivity, XuanNengMainActivity.class);//				intent.putExtra(UserInfoBean.KEY_USER_ID, String.valueOf(BGApp.mLoginBean.userid));//				startActivity(intent);//				break;				//			// 设置//			case R.id.tv_settings://				intent = new Intent(mActivity, SettingsActivity.class);//				startActivity(intent);//				break;							// 更多			case R.id.tv_more:				MobclickAgent.onEvent(this,"me_more_click");				intent = new Intent(mActivity, MoreActivity.class);				startActivity(intent);				break;			default:				break;		}	}		@Override	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {		if(info.getState() == HttpTaskState.STATE_OK){			BaseNetWork bNetWork = info.getmBaseNetWork();			String strJson = bNetWork.getStrJson();			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){				UserInfoBean userBean = JSON.parseObject(strJson, UserInfoBean.class);				BGApp.mUserBean = userBean;				setData(userBean);			}		}	}}