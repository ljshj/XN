package com.bgood.xn.ui.weiqiang;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.adapter.WeiqiangAdapter;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.bean.WeiQiangBean;
import com.bgood.xn.bean.WeiQiangBean.WeiqiangActionType;
import com.bgood.xn.bean.response.WeiqiangResponse;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.WeiqiangRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.ui.user.account.LoginActivity;
import com.bgood.xn.ui.user.info.NameCardActivity;
import com.bgood.xn.utils.LogUtils;
import com.bgood.xn.utils.ShareUtils;
import com.bgood.xn.utils.ToolUtils;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.dialog.BGDialog;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.view.xlistview.XListView.IXListViewListener;

/**
 * 我的微墙主页
 * @author ChenGuoqing 2014-8-20下午3:46:57
 * @date:2014-10-24 下午2:21:22
 */
public class WeiqiangActivity extends BaseActivity implements OnItemClickListener,TaskListenerWithState,OnClickListener,OnPageChangeListener,IXListViewListener
{
	/**请示去查看微墙详情**/
	public static final int REQUEST_WEIQIANG_DETAIL = 100;
	
	private TextView tv_weiqiang_type_left_select;
	private TextView tv_weiqiang_type_right_select;
	private View v_weiqiang_type_select_left_underline;
	private View v_weiqiang_type_select_right_underline;
	private ViewPager vp_weiqiang_type_select;
	private Button weiqiang_main_b_more_operate;
	private PopupWindow popupWindow_more;
	
	private XListView m_followFriendsXLv = null;
	private XListView m_allFriendsXLv = null;
	
	private View m_followFriendsLayout = null; // 关注好友
	private View m_allFriendsLayout = null; // 全部好友
	private List<View> m_pagerList = null; // ViewPager页面集合
	
	private List<WeiQiangBean> m_followFriendsList = new ArrayList<WeiQiangBean>();
	private List<WeiQiangBean> m_allFriendsList = new ArrayList<WeiQiangBean>();
	
	private WeiqiangAdapter m_followFriendsAdapter;
	private WeiqiangAdapter m_allFriendsAdapter;
	
	public String mAttionWeiqiangRefreshTime = null;
	public String mAllWeiqiangRefreshTime = null;
	
	private int m_type = WeiQiangBean.WEIQIANG_ALL;
    
    private int weiqiang_all_start = 0;	//关注
    private int weiqiang_attion_start = 0;	//非关注（全部）
    
    private boolean isRefresh = true;
	
    private WeiQiangBean mActionWeiqiang = null;
    private WeiqiangActionType type;
    
	private ShareUtils share = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		share = new ShareUtils(mActivity);
		setContentView(R.layout.weiqiang_layout_main);
		initViews();
		setListeners();
		setAdapter();
	}
	
	 @Override
	public void onResume() {
		super.onResume();
		mAllWeiqiangRefreshTime = pUitl.getWeiqiangAllRefreshTime();
		m_allFriendsXLv.setRefreshTime(mAllWeiqiangRefreshTime);
		
		mAttionWeiqiangRefreshTime = pUitl.getWeiqiangAllRefreshTime();
		m_allFriendsXLv.setRefreshTime(mAllWeiqiangRefreshTime);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		 //相当于Fragment的onPause
		pUitl.setWeiqiangAllRefreshTime(mAllWeiqiangRefreshTime);
		pUitl.setWeiqiangAttionRefreshTime(mAttionWeiqiangRefreshTime);
	}
	
	@SuppressLint("InflateParams")
	private void initViews()
	{
		weiqiang_main_b_more_operate = (Button) findViewById(R.id.weiqiang_main_b_more_operate);
		tv_weiqiang_type_left_select = (TextView) findViewById(R.id.tv_weiqiang_type_left_select);
		tv_weiqiang_type_right_select = (TextView) findViewById(R.id.tv_weiqiang_type_right_select);
		v_weiqiang_type_select_left_underline = findViewById(R.id.v_weiqiang_type_select_left_underline);
		v_weiqiang_type_select_right_underline = findViewById(R.id.v_weiqiang_type_select_right_underline);
		vp_weiqiang_type_select = (ViewPager) findViewById(R.id.vp_weiqiang_type_select);
		checkType(0);
		
		m_followFriendsLayout = inflater.inflate(R.layout.listview_space_bar, null);
		m_allFriendsLayout = inflater.inflate(R.layout.listview_space_bar, null);
		m_pagerList = new ArrayList<View>();
		m_pagerList.add(m_allFriendsLayout);
		m_pagerList.add(m_followFriendsLayout);
		
		m_followFriendsXLv = (XListView) m_followFriendsLayout.findViewById(R.id.xlv_sapce);
		
		m_allFriendsXLv = (XListView) m_allFriendsLayout.findViewById(R.id.xlv_sapce); 
		
		m_followFriendsAdapter = new WeiqiangAdapter(m_followFriendsList,mActivity,this);
		m_followFriendsXLv.setAdapter(m_followFriendsAdapter);
		m_followFriendsXLv.setFooterDividersEnabled(false);
		
		m_allFriendsAdapter = new WeiqiangAdapter(m_allFriendsList,mActivity,this);
		m_allFriendsXLv.setAdapter(m_allFriendsAdapter);
	}
	
	/**
	 * 设置数据列表显示
	 */
	private void setAdapter()
	{
		vp_weiqiang_type_select.setAdapter(new TableVPAdapter());
		vp_weiqiang_type_select.setOnPageChangeListener(this);
	}

	@SuppressLint("InflateParams")
	private void showPopupMore()
	{
		weiqiang_main_b_more_operate.setBackgroundResource(R.drawable.img_weiqiang_more_operate_show);
		if (popupWindow_more == null)
		{
			View contentView = inflater.inflate(R.layout.popup_weiqiang_main_more_operate, null);
			contentView.findViewById(R.id.tv_weiqiang_publish).setOnClickListener(this);
			contentView.findViewById(R.id.tv_weiqiang_me).setOnClickListener(this);
			contentView.findViewById(R.id.tv_weiqiang_mention).setOnClickListener(this);
			popupWindow_more = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			popupWindow_more.setFocusable(true);
			popupWindow_more.setOnDismissListener(new OnDismissListener()
			{

				@Override
				public void onDismiss()
				{
					weiqiang_main_b_more_operate.setBackgroundResource(R.drawable.img_weiqiang_more_operate_normal);
				}
			});
			popupWindow_more.setOutsideTouchable(true);
			popupWindow_more.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_weiqiang_more_operate_popup_bg));
		}
		popupWindow_more.showAsDropDown(weiqiang_main_b_more_operate);
	}

	private void dissmissPopupMore()
	{
		if (popupWindow_more != null && popupWindow_more.isShowing())
			popupWindow_more.dismiss();
	}

	private void setListeners()
	{
		weiqiang_main_b_more_operate.setOnClickListener(this);
		
		findViewById(R.id.layout_weiqiang_type_right_select).setOnClickListener(this);
		findViewById(R.id.layout_weiqiang_type_left_select).setOnClickListener(this);
		
		m_followFriendsXLv.setXListViewListener(this);
		m_allFriendsXLv.setXListViewListener(this);
		
		m_followFriendsXLv.setOnItemClickListener(this);
		m_allFriendsXLv.setOnItemClickListener(this);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
	{

	}

	@Override
	public void onPageSelected(int position)
	{
		checkType(position);
	}

	@Override
	public void onPageScrollStateChanged(int state)
	{

	}

	@Override
	public void onClick(View v)
	{
		
		if(!BGApp.isUserLogin){
			LoginActivity.doLoginAction(this);
		}else{
		
		Intent intent = null;
		WeiQiangBean wqb = null;
		switch (v.getId())
		{
		case R.id.layout_weiqiang_type_left_select:
			vp_weiqiang_type_select.setCurrentItem(0);
			break;
		case R.id.layout_weiqiang_type_right_select:
			vp_weiqiang_type_select.setCurrentItem(1);
			break;
		case R.id.weiqiang_main_b_more_operate:
			if (popupWindow_more != null && popupWindow_more.isShowing())
			{
				dissmissPopupMore();
			} else
			{
				showPopupMore();
			}
			break;
		case R.id.tv_weiqiang_me:
			dissmissPopupMore();
			intent = new Intent(mActivity, WeiqiangPersonActivity.class);
			intent.putExtra(UserInfoBean.KEY_USER_ID, String.valueOf(BGApp.mUserId));
			mActivity.startActivity(intent);
			break;
		case R.id.tv_weiqiang_publish:
			dissmissPopupMore();
			intent = new Intent(mActivity, WeiqiangPublishActivity.class);
			mActivity.startActivity(intent);
			break;
		case R.id.tv_weiqiang_mention:
			dissmissPopupMore();
			intent = new Intent(mActivity, WeiqiangMentionActivity.class);
			mActivity.startActivity(intent);
			break;
		case R.id.av_zan:	//赞
			wqb = (WeiQiangBean) v.getTag();
			mActionWeiqiang = wqb;
			WeiqiangRequest.getInstance().requestWeiqiangZan(this, mActivity, wqb.weiboid);
			break;
		case R.id.av_reply:	//评论
			wqb = (WeiQiangBean) v.getTag();
			mActionWeiqiang = wqb;
			type = WeiqiangActionType.RESPONSE;
//			selectPosition(wqb);
			createSendDialog();
			break;
		case R.id.av_transpont:	//转发
			wqb = (WeiQiangBean) v.getTag();
			mActionWeiqiang = wqb;
			type = WeiqiangActionType.TRANSPOND;
//			selectPosition(wqb);
			createSendDialog();
			break;
		case R.id.av_share:	//分享
			wqb = (WeiQiangBean) v.getTag();
			mActionWeiqiang = wqb;
			share.setShareContent(wqb.content, wqb.imgs.size() > 0 ? wqb.imgs.get(0).img:null);
			WeiqiangRequest.getInstance().requestWeiqiangShare(this, this, wqb.weiboid);
			break;
		}}
	}
	
	
//	private void selectPosition(WeiQiangBean wqb) {
//		int position = m_allFriendsList.indexOf(wqb);
//		m_allFriendsXLv.setSelection(position);
//	}
	
	/**
	 * 
	 * @todo 时间选择器
	 * @author lzlong@zwmob.com
	 * @time 2014-3-26 下午2:09:30
	 */
	private void createSendDialog() {

		int screenWidth = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth()); // 屏幕宽
		int screenHeight = (int) (mActivity.getWindowManager().getDefaultDisplay().getHeight()*0.3); // 屏幕高

		final BGDialog dialog = new BGDialog(mActivity,R.style.dialog_no_thing,screenWidth, screenHeight);

		View vSend = inflater.inflate(R.layout.dialog_send, null);

		vSend.requestFocus();

		final EditText etcontent = (EditText) vSend.findViewById(R.id.et_content);
		Button btnSend = (Button) vSend.findViewById(R.id.btn_send);
		btnSend.setText(type == WeiqiangActionType.TRANSPOND?"转发":"评论");
		btnSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				String content = etcontent.getText().toString();
				if(TextUtils.isEmpty(content))
				{
					return;
				}else{
					etcontent.setText("");
					if(type == WeiqiangActionType.TRANSPOND){
						if(m_type == WeiQiangBean.WEIQIANG_ALL){
							mActionWeiqiang.forward_count = String.valueOf(Integer.valueOf(mActionWeiqiang.forward_count)+1);
							m_allFriendsAdapter.notifyDataSetChanged();
						}else{
							mActionWeiqiang.forward_count = String.valueOf(Integer.valueOf(mActionWeiqiang.forward_count)+1);
							m_followFriendsAdapter.notifyDataSetChanged();
						}
						WeiqiangRequest.getInstance().requestWeiqiangTranspond(WeiqiangActivity.this, mActivity, mActionWeiqiang.weiboid,content);
					}else{
						if(m_type == WeiQiangBean.WEIQIANG_ALL){
							mActionWeiqiang.comment_count = String.valueOf(Integer.valueOf(mActionWeiqiang.comment_count)+1);
							m_allFriendsAdapter.notifyDataSetChanged();
						}else{
							mActionWeiqiang.comment_count = String.valueOf(Integer.valueOf(mActionWeiqiang.comment_count)+1);
							m_followFriendsAdapter.notifyDataSetChanged();
						}
						WeiqiangRequest.getInstance().requestWeiqiangReply(WeiqiangActivity.this, mActivity, mActionWeiqiang.weiboid,content);
					}
					
				}
			}
		});
		
		//弹出键盘
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				((InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
			}
		},100);
		
		dialog.setCancelable(true);
		dialog.showDialog(vSend);
	}
	
	class TableVPAdapter extends PagerAdapter
	{

		@Override
		public boolean isViewFromObject(View arg0, Object arg1)
		{

			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2)
		{
			((ViewPager) arg0).removeView(m_pagerList.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0)
		{
		}

		@Override
		public int getCount()
		{
			return m_pagerList.size();
		}

		public Object instantiateItem(View arg0, int arg1)
		{
			((ViewPager) arg0).addView(m_pagerList.get(arg1));
			return m_pagerList.get(arg1);
		}
	}
	

	@Override
	public void onItemClick(AdapterView<?> adapter, View v, int location, long arg3) {
	 	final WeiQiangBean weiqiang = (WeiQiangBean) adapter.getAdapter().getItem(location);
        Intent intent = new Intent(mActivity, WeiqiangDetailActivity.class);
        intent.putExtra(WeiQiangBean.KEY_WEIQIANG_BEAN, weiqiang);
        startActivity(intent);
	}
	
	@Override
	public void onRefresh() {
		isRefresh = true;
		if(m_type == WeiQiangBean.WEIQIANG_ALL){
			weiqiang_all_start = 0;
			WeiqiangRequest.getInstance().requestWeiqiangList(this, mActivity, String.valueOf(m_type),BGApp.mUserId, String.valueOf(weiqiang_all_start), String.valueOf(weiqiang_all_start+PAGE_SIZE_ADD));
		}else{
			weiqiang_attion_start = 0;
			WeiqiangRequest.getInstance().requestWeiqiangList(this, mActivity, String.valueOf(m_type),BGApp.mUserId, String.valueOf(weiqiang_attion_start), String.valueOf(weiqiang_attion_start+PAGE_SIZE_ADD));
		}
	}
	@Override
	public void onLoadMore() {
		isRefresh = false;
		if(m_type == WeiQiangBean.WEIQIANG_ALL){
			WeiqiangRequest.getInstance().requestWeiqiangList(this, mActivity, String.valueOf(m_type),BGApp.mUserId, String.valueOf(weiqiang_all_start), String.valueOf(weiqiang_all_start+PAGE_SIZE_ADD));
		}else{
			WeiqiangRequest.getInstance().requestWeiqiangList(this, mActivity, String.valueOf(m_type),BGApp.mUserId, String.valueOf(weiqiang_attion_start), String.valueOf(weiqiang_attion_start+PAGE_SIZE_ADD));
		}
	}
	
	private void setWeiqiangData(List<WeiQiangBean> weiqiangs) {
		
		if(isRefresh){
			if(m_type == WeiQiangBean.WEIQIANG_ALL){
				mAllWeiqiangRefreshTime = ToolUtils.getNowTime();
				m_allFriendsXLv.setRefreshTime(mAllWeiqiangRefreshTime);
			}else{
				mAttionWeiqiangRefreshTime = ToolUtils.getNowTime();
				m_followFriendsXLv.setRefreshTime(mAttionWeiqiangRefreshTime);
			}
		}
		
		m_allFriendsXLv.stopRefresh();
		m_allFriendsXLv.stopLoadMore();
		m_followFriendsXLv.stopRefresh();
		m_followFriendsXLv.stopLoadMore();
		if(null == weiqiangs){
			return;
		}
		
		if(0 == weiqiangs.size()){
			if(m_type == WeiQiangBean.WEIQIANG_ALL){	//全部微墙
				  m_allFriendsXLv.setPullLoadEnable(false);
	                 BToast.show(mActivity, "数据加载完毕");
			}else{
				  m_followFriendsXLv.setPullLoadEnable(false);
	                 BToast.show(mActivity, "数据加载完毕");
			}
		}
		
		
		if(m_type == WeiQiangBean.WEIQIANG_ALL){	//全部微墙
			
			if (isRefresh) {
				m_allFriendsList.clear();
				weiqiang_all_start = 0;
			} 
				
			if (weiqiangs.size() < PAGE_SIZE_ADD) {
					m_allFriendsXLv.setPullLoadEnable(false);
					BToast.show(mActivity, "数据加载完毕");
				} else {
					weiqiang_all_start += PAGE_SIZE_ADD;
					m_allFriendsXLv.setPullLoadEnable(true);
				}
			
			this.m_allFriendsList.addAll(weiqiangs);
			m_allFriendsAdapter.notifyDataSetChanged();

		}else if(m_type == WeiQiangBean.WEIQIANG_ATTENTION){//关注的微墙
			
			if(isRefresh){
				m_followFriendsList.clear();
				weiqiang_attion_start = 0;
			}
			
			 if (weiqiangs.size() <= PAGE_SIZE_ADD)
             {
                 m_followFriendsXLv.setPullLoadEnable(false);
                 BToast.show(mActivity, "数据加载完毕");
             } else
             {
            	 weiqiang_attion_start += PAGE_SIZE_ADD;
            	 m_followFriendsXLv.setPullLoadEnable(true);
             }
             this.m_followFriendsList.addAll(weiqiangs);
             m_followFriendsAdapter.notifyDataSetChanged();
		}
	}
	
	/**
	 * 选择我想,我能时 上面的导航条变化
	 * @param index
	 * 
	 */
	private void checkType(int index)
	{
		switch (index)
		{
		case 0:
			tv_weiqiang_type_left_select.setTextColor(getResources().getColor(R.color.home_type_text_select));
			tv_weiqiang_type_right_select.setTextColor(getResources().getColor(R.color.text_color_normal));
			v_weiqiang_type_select_left_underline.setVisibility(View.VISIBLE);
			v_weiqiang_type_select_right_underline.setVisibility(View.INVISIBLE);
			m_type = WeiQiangBean.WEIQIANG_ALL;
			if(m_allFriendsList.size() == 0){
				WeiqiangRequest.getInstance().requestWeiqiangList(this, mActivity, String.valueOf(m_type),BGApp.mUserId, String.valueOf(weiqiang_all_start), String.valueOf(weiqiang_all_start+PAGE_SIZE_ADD));
			}

			break;
		case 1:
			tv_weiqiang_type_left_select.setTextColor(getResources().getColor(R.color.text_color_normal));
			tv_weiqiang_type_right_select.setTextColor(getResources().getColor(R.color.home_type_text_select));
			v_weiqiang_type_select_left_underline.setVisibility(View.INVISIBLE);
			v_weiqiang_type_select_right_underline.setVisibility(View.VISIBLE);
			m_type = WeiQiangBean.WEIQIANG_ATTENTION;
			if(m_followFriendsList.size()==0){
				WeiqiangRequest.getInstance().requestWeiqiangList(this, mActivity, String.valueOf(m_type),BGApp.mUserId, String.valueOf(weiqiang_attion_start), String.valueOf(weiqiang_attion_start+PAGE_SIZE_ADD));
			}
		}
	}
	
	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			String strJson = bNetWork.getStrJson();
				switch(bNetWork.getMessageType()){
				case 860001:	//获取微墙列表
					if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
						WeiqiangResponse response  = JSON.parseObject(strJson, WeiqiangResponse.class);
						setWeiqiangData(response.items);
					}else{
						m_allFriendsXLv.stopRefresh();
						m_allFriendsXLv.stopLoadMore();
						m_followFriendsXLv.stopRefresh();
						m_followFriendsXLv.stopLoadMore();
					}
					break;
				case 860002:	//获取微墙详细内容
					break;
				case 860003:
					break;
				case 860004:
					break;
				case 860005:	//转发微墙
					break;
				case 860006: 	//分享
					if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
						if(m_type == WeiQiangBean.WEIQIANG_ALL){
							mActionWeiqiang.share_count = String.valueOf(Integer.valueOf(mActionWeiqiang.share_count)+1);
							m_allFriendsAdapter.notifyDataSetChanged();
						}else{
							mActionWeiqiang.share_count = String.valueOf(Integer.valueOf(mActionWeiqiang.share_count)+1);
							m_followFriendsAdapter.notifyDataSetChanged();
						}
					}
					break;
				case 860007:
					break;
				case 860008:	//赞微墙
					if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
						if(m_type == WeiQiangBean.WEIQIANG_ALL){
							mActionWeiqiang.like_count = String.valueOf(Integer.valueOf(mActionWeiqiang.like_count)+1);
							m_allFriendsAdapter.notifyDataSetChanged();
						}else{
							mActionWeiqiang.like_count = String.valueOf(Integer.valueOf(mActionWeiqiang.like_count)+1);
							m_followFriendsAdapter.notifyDataSetChanged();
						}
					}else if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_HAS_ZAN){
						BToast.show(mActivity, "不要重复点赞");
					}
					break;
				default:
					break;
				}
			}
		}
}
