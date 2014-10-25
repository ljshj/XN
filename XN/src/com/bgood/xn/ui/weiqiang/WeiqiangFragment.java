package com.bgood.xn.ui.weiqiang;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import cn.jpush.android.util.ad;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.adapter.WeiqiangAdapter;
import com.bgood.xn.bean.WeiQiangBean;
import com.bgood.xn.bean.response.WeiqiangResponse;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.request.WeiqiangRequest;
import com.bgood.xn.ui.BaseFragment;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.view.xlistview.XListView.IXListViewListener;

/**
 * 我的微墙主页
 * @author ChenGuoqing 2014-8-20下午3:46:57
 * @date:2014-10-24 下午2:21:22
 */
public class WeiqiangFragment extends BaseFragment implements OnItemClickListener,TaskListenerWithState,OnClickListener,OnPageChangeListener,IXListViewListener
{
	
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

	
	public static final int WEIQIANG_MY = 0;
	public static final int WEIQIANG_ATTENTION = 1;
	public static final int WEIQIANG_ALL = 2;
	
	private int m_type = WEIQIANG_ALL;
	
    
    
    private int size_weiqiang_all = 0;	//关注
    private int size_weiqiang_attion = 0;	//非关注（全部）
    
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		layout = inflater.inflate(R.layout.layout_weiqiang_main, container, false);
		initViews();
		setListeners();
		setAdapter();
		WeiqiangRequest.getInstance().requestWeiqiangList(this, mActivity, String.valueOf(m_type), String.valueOf(size_weiqiang_all), String.valueOf(size_weiqiang_all+PAGE_SIZE_ADD));
		return layout;
	}
	@SuppressLint("InflateParams")
	private void initViews()
	{
		weiqiang_main_b_more_operate = (Button) layout.findViewById(R.id.weiqiang_main_b_more_operate);
		tv_weiqiang_type_left_select = (TextView) layout.findViewById(R.id.tv_weiqiang_type_left_select);
		tv_weiqiang_type_right_select = (TextView) layout.findViewById(R.id.tv_weiqiang_type_right_select);
		v_weiqiang_type_select_left_underline = layout.findViewById(R.id.v_weiqiang_type_select_left_underline);
		v_weiqiang_type_select_right_underline = layout.findViewById(R.id.v_weiqiang_type_select_right_underline);
		vp_weiqiang_type_select = (ViewPager) layout.findViewById(R.id.vp_weiqiang_type_select);
		checkType(0);
		
		m_followFriendsLayout = inflater.inflate(R.layout.item_comment_xlistview, null);
		m_allFriendsLayout = inflater.inflate(R.layout.item_comment_xlistview, null);
		m_pagerList = new ArrayList<View>();
		m_pagerList.add(m_allFriendsLayout);
		m_pagerList.add(m_followFriendsLayout);
		
		m_followFriendsXLv = (XListView) m_followFriendsLayout.findViewById(R.id.common_xlv);
		
		m_allFriendsXLv = (XListView) m_allFriendsLayout.findViewById(R.id.common_xlv); 
		
		m_followFriendsAdapter = new WeiqiangAdapter(m_followFriendsList,mActivity);
		m_followFriendsXLv.setAdapter(m_followFriendsAdapter);
		
		m_allFriendsAdapter = new WeiqiangAdapter(m_allFriendsList,mActivity);
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
			contentView.findViewById(R.id.weiqiang_main_ll_publish).setOnClickListener(this);
			contentView.findViewById(R.id.weiqiang_main_ll_my_weiqiang).setOnClickListener(this);
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
		
		layout.findViewById(R.id.layout_weiqiang_type_right_select).setOnClickListener(this);
		layout.findViewById(R.id.layout_weiqiang_type_left_select).setOnClickListener(this);
		
		m_followFriendsXLv.setXListViewListener(this);
		m_allFriendsXLv.setXListViewListener(this);
		
		m_followFriendsXLv.setOnItemClickListener(this);
		m_allFriendsXLv.setOnItemClickListener(this);
	}

	/**
	 * 选择我想,我能时 上面的导航条变化
	 * 
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
			m_type = WEIQIANG_ALL;
			WeiqiangRequest.getInstance().requestWeiqiangList(this, mActivity, String.valueOf(m_type), String.valueOf(size_weiqiang_all), String.valueOf(size_weiqiang_all+PAGE_SIZE_ADD));

			break;
		case 1:
			tv_weiqiang_type_left_select.setTextColor(getResources().getColor(R.color.text_color_normal));
			tv_weiqiang_type_right_select.setTextColor(getResources().getColor(R.color.home_type_text_select));
			v_weiqiang_type_select_left_underline.setVisibility(View.INVISIBLE);
			v_weiqiang_type_select_right_underline.setVisibility(View.VISIBLE);
			m_type = WEIQIANG_ATTENTION;
			WeiqiangRequest.getInstance().requestWeiqiangList(this, mActivity, String.valueOf(m_type), String.valueOf(size_weiqiang_attion), String.valueOf(size_weiqiang_attion+PAGE_SIZE_ADD));

		}
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
		Intent intent = null;
		switch (v.getId())
		{
		case R.id.layout_weiqiang_type_left_select:
			vp_weiqiang_type_select.setCurrentItem(0);
			checkType(0);
			break;
		case R.id.layout_weiqiang_type_right_select:
			vp_weiqiang_type_select.setCurrentItem(1);
			checkType(1);
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
		case R.id.weiqiang_main_ll_my_weiqiang:
			dissmissPopupMore();
			intent = new Intent(mActivity, WeiqiangOfMeActivity.class);
			mActivity.startActivity(intent);
			break;
		case R.id.weiqiang_main_ll_publish:
			dissmissPopupMore();
			intent = new Intent(mActivity, WeiqiangPublishActivity.class);
			mActivity.startActivity(intent);
			break;
		}
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
		
//		if(m_type == WEIQIANG_ATTENTION){
//			
//		}else if(m_type = WEIQIANG_ALL){
//			
//		}
		
		 	final WeiQiangBean weiqiang = (WeiQiangBean) adapter.getAdapter().getItem(location);
	        Intent intent = new Intent(mActivity, WeiqiangDetailActivity.class);
	        intent.putExtra(WeiqiangDetailActivity.BEAN_WEIQIANG_KEY, weiqiang);
	        startActivity(intent);
	}
	@Override
	public void onRefresh() {
		
	}
	@Override
	public void onLoadMore() {
		
	}
	
	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			JSONObject body = bNetWork.getBody();
			String strJson = bNetWork.getStrJson();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
			WeiqiangResponse response  = JSON.parseObject(strJson, WeiqiangResponse.class);
			setWeiqiangData(response.items);}}
		}
	
	private void setWeiqiangData(List<WeiQiangBean> weiqiangs) {
		if(null == weiqiangs || weiqiangs.size() == 0){
			
			m_allFriendsXLv.stopRefresh();
			m_allFriendsXLv.stopLoadMore();
			m_followFriendsXLv.stopRefresh();
			m_followFriendsXLv.stopLoadMore();
			return;
		}
		
		
		if(m_type == WEIQIANG_ALL){	//全部微墙
			 if (weiqiangs.size() <= PAGE_SIZE_ADD)
             {
                 m_allFriendsXLv.setPullLoadEnable(false);
                 BToast.show(mActivity, "数据加载完毕");
             } else
             {
            	 size_weiqiang_all += PAGE_SIZE_ADD;
                 m_allFriendsXLv.setPullLoadEnable(true);
             }
             this.m_allFriendsList.addAll(weiqiangs);
             m_allFriendsAdapter.notifyDataSetChanged();
			
			
		}else if(m_type == WEIQIANG_ATTENTION){//关注的微墙
			 if (weiqiangs.size() <= PAGE_SIZE_ADD)
             {
                 m_followFriendsXLv.setPullLoadEnable(false);
                 BToast.show(mActivity, "数据加载完毕");
             } else
             {
            	 size_weiqiang_attion += PAGE_SIZE_ADD;
            	 m_followFriendsXLv.setPullLoadEnable(true);
             }
             this.m_followFriendsList.addAll(weiqiangs);
             m_followFriendsAdapter.notifyDataSetChanged();
		}
	}
}
