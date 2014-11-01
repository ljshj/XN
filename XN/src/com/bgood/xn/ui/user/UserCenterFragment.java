package com.bgood.xn.ui.user;import org.json.JSONObject;import android.app.Activity;import android.content.Intent;import android.os.Bundle;import android.text.TextUtils;import android.view.LayoutInflater;import android.view.View;import android.view.View.OnClickListener;import android.view.ViewGroup;import android.widget.ImageView;import android.widget.LinearLayout;import android.widget.RelativeLayout;import android.widget.TextView;import com.alibaba.fastjson.JSON;import com.bgood.xn.R;import com.bgood.xn.bean.UserInfoBean;import com.bgood.xn.network.BaseNetWork;import com.bgood.xn.network.BaseNetWork.ReturnCode;import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;import com.bgood.xn.network.HttpRequestInfo;import com.bgood.xn.network.HttpResponseInfo;import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;import com.bgood.xn.network.request.UserCenterRequest;import com.bgood.xn.system.BGApp;import com.bgood.xn.ui.BaseFragment;import com.bgood.xn.ui.user.info.ICanActivity;import com.bgood.xn.ui.user.info.IThinkActivity;import com.bgood.xn.ui.user.more.MoreActivity;import com.bgood.xn.ui.user.product.ShowcaseActivity;import com.bgood.xn.ui.weiqiang.WeiqiangOfMeActivity;import com.bgood.xn.widget.TitleBar;import com.squareup.picasso.Picasso;/** * @todo:个人中心 * @author:hg_liuzl@163.com */public class UserCenterFragment extends BaseFragment implements OnClickListener,TaskListenerWithState {	/**登录操作码**/	private static final int CODE_LOGIN = 100;		public static final String TAG = "UserCenterFragment";		private View layout;	private RelativeLayout m_userInfoRl = null; // 用户信息	private ImageView m_userIconImgV = null; // 用户图片	private TextView m_userNameTv = null; // 用户名	private ImageView m_identityImgV = null; // 用户身份	private TextView m_accountNumberTv = null; // 用户账号	private LinearLayout m_followLl = null; // 我的关注布局	private TextView m_followTv = null; // 我的关注	private LinearLayout m_vermicelliLl = null; // 我的粉丝布局	private TextView m_vermicelliTv = null; // 我的粉丝	private LinearLayout m_dynamicLl = null; // 我的动态布局	private TextView m_dynamicTv = null; // 我的动态	private TextView m_iMissTv = null; // 我想	private TextView m_iCanTv = null; // 我能	private TextView tvShop,tvSytemNotification,tvApplication,tvXuanneng,tvSetting,tvMoire;			@Override	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {				layout = inflater.inflate(R.layout.layout_user_center, container, false);		TitleBar bar = new TitleBar(mActivity, layout);		bar.initTitleBar("个人中心");		bar.setBackBtnVisible(View.GONE);		initViews();		return layout;	}		@Override	public void onResume() {		super.onResume();		if(BGApp.isUserLogin ){			if(null == BGApp.mUserBean){				UserCenterRequest.getInstance().requestPersonInfo(this, mActivity, String.valueOf(BGApp.mLoginBean.userid));			}else{				setData(BGApp.mUserBean);				}		}else{			Intent intent = new Intent(mActivity, LoginActivity.class);			intent.putExtra(LoginActivity.FROM_KEY, TAG);			mActivity.startActivityForResult(intent, CODE_LOGIN);		}	}		private void initViews() {		m_userInfoRl = (RelativeLayout) layout.findViewById(R.id.user_center_rl_user_info);		m_userIconImgV = (ImageView) layout.findViewById(R.id.user_center_imgv_user_icon);		m_userNameTv = (TextView) layout.findViewById(R.id.user_center_tv_user_name);		m_identityImgV = (ImageView) layout.findViewById(R.id.iv_identity);		m_accountNumberTv = (TextView) layout.findViewById(R.id.user_center_tv_account_number);		m_followLl = (LinearLayout) layout.findViewById(R.id.user_center_ll_follow);		m_followTv = (TextView) layout.findViewById(R.id.user_center_tv_follow);		m_vermicelliLl = (LinearLayout) layout.findViewById(R.id.user_center_ll_vermicelli);		m_vermicelliTv = (TextView) layout.findViewById(R.id.user_center_tv_vermicelli);		m_dynamicLl = (LinearLayout) layout.findViewById(R.id.user_center_ll_dynamic);		m_dynamicTv = (TextView) layout.findViewById(R.id.user_center_tv_dynamic);		m_iMissTv = (TextView) layout.findViewById(R.id.user_center_tv_imiss);		m_iCanTv = (TextView) layout.findViewById(R.id.user_center_tv_ican);				layout.findViewById(R.id.tv_shop).setOnClickListener(this);		layout.findViewById(R.id.tv_system_notification).setOnClickListener(this);		layout.findViewById(R.id.tv_application_management).setOnClickListener(this);		layout.findViewById(R.id.tv_xuanneng).setOnClickListener(this);		layout.findViewById(R.id.tv_settings).setOnClickListener(this);		layout.findViewById(R.id.tv_more).setOnClickListener(this);				m_userInfoRl.setOnClickListener(this);		m_followLl.setOnClickListener(this);		m_vermicelliLl.setOnClickListener(this);		m_dynamicLl.setOnClickListener(this);		m_iMissTv.setOnClickListener(this);		m_iCanTv.setOnClickListener(this);	}		@Override	public void onActivityResult(int requestCode, int resultCode, Intent data) {		super.onActivityResult(requestCode, resultCode, data);		if(resultCode == Activity.RESULT_OK && requestCode == CODE_LOGIN){			UserCenterRequest.getInstance().requestPersonInfo(this, mActivity, String.valueOf(BGApp.mLoginBean.userid));		}	}		/**	 * 设置用户数据显示方法	 * @param userDTO 用户信息实体类	 */	private void setData(UserInfoBean userBean)	{				if (!TextUtils.isEmpty(userBean.photo)){			Picasso.with(mActivity).load(userBean.photo).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(m_userIconImgV);		}		// 昵称		m_userNameTv.setText(userBean.nickn);		// 性别		m_accountNumberTv.setCompoundDrawables(null, null, getResources().getDrawable(userBean.sex == 1 ? R.drawable.img_common_sex_male:R.drawable.img_common_sex_female), null);			//	m_identityImgV.setBackgroundResource(resid);				m_accountNumberTv.setText(getString(R.string.account_number, userBean.username));								m_followTv.setText(userBean.guanzhu); // 我的关注		m_vermicelliTv.setText(userBean.fansnumber); // 我的粉丝		m_dynamicTv.setText(userBean.weiqiang); // 我的动态		m_iMissTv.setText(userBean.ineed); // 我想		m_iCanTv.setText(userBean.ican); // 我能	}		@Override	public void onClick(View v) {		Intent intent = null;		switch (v.getId())		{			// 账户信息			case R.id.user_center_rl_user_info:				intent = new Intent(mActivity, AccountCenterActivity.class);				Bundle bundle = new Bundle();				bundle.putSerializable(UserInfoBean.KEY_USER_BEAN, BGApp.mUserBean);				intent.putExtras(bundle);				startActivity(intent);				break;			// 我的关注			case R.id.user_center_ll_follow:				intent = new Intent(mActivity, AttentionActivity.class);				intent.putExtra(AttentionActivity.KEY_ATTENTION, 0);				startActivity(intent);				break;			// 我的粉丝			case R.id.user_center_ll_vermicelli:				intent = new Intent(mActivity, AttentionActivity.class);				intent.putExtra(AttentionActivity.KEY_ATTENTION, 1);				startActivity(intent);				break;			// 我的动态			case R.id.user_center_ll_dynamic:				intent = new Intent(mActivity, WeiqiangOfMeActivity.class);				startActivity(intent);				break;			// 我想			case R.id.user_center_tv_imiss:				intent = new Intent(mActivity, IThinkActivity.class);				intent.putExtra(UserInfoBean.KEY_USER_BEAN, BGApp.mUserBean);				startActivity(intent);				break;			// 我能			case R.id.user_center_tv_ican:				intent = new Intent(mActivity, ICanActivity.class);				intent.putExtra(UserInfoBean.KEY_USER_BEAN, BGApp.mUserBean);				startActivity(intent);				break;			// 橱窗			case R.id.tv_shop:				intent = new Intent(mActivity, ShowcaseActivity.class);				startActivity(intent);				break;//			// 本地应用管理//			case R.id.tv_application_management://				intent = new Intent(mActivity, ApplicationManagementActivity.class);//				startActivity(intent);//				break;			// 我的炫能//			case R.id.tv_xuanneng://				intent = new Intent(mActivity, MyXuanNengActivity.class);//				startActivity(intent);//				break;			// 设置			case R.id.tv_settings:				intent = new Intent(mActivity, SettingsActivity.class);				startActivity(intent);				break;							// 更多			case R.id.tv_more:				intent = new Intent(mActivity, MoreActivity.class);				startActivity(intent);				break;			default:				break;		}	}		@Override	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {		if(info.getState() == HttpTaskState.STATE_OK){			BaseNetWork bNetWork = info.getmBaseNetWork();			JSONObject body = bNetWork.getBody();			String strJson = bNetWork.getStrJson();			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){				UserInfoBean userBean = JSON.parseObject(strJson, UserInfoBean.class);				BGApp.mUserBean = userBean;				setData(userBean);			}			}		}}