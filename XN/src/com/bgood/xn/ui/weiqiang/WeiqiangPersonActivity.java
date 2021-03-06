package com.bgood.xn.ui.weiqiang;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.adapter.WeiqiangAdapter;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.bean.WeiQiangBean;
import com.bgood.xn.bean.response.WeiqiangResponse;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.WeiqiangRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.utils.ToolUtils;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.view.xlistview.XListView.IXListViewListener;
import com.bgood.xn.widget.TitleBar;

/**
 * 
 * @todo:我的微墙
 * @date:2014-10-24 下午3:50:55
 * @author:hg_liuzl@163.com
 */
public class WeiqiangPersonActivity extends BaseActivity implements OnItemClickListener,TaskListenerWithState,OnClickListener,IXListViewListener
{
	/**我自己的微墙**/
	private XListView m_weiqiang_listview;
	private List<WeiQiangBean> mWeiqiangList = new ArrayList<WeiQiangBean>();
	private WeiqiangAdapter weiqiangAdapter;
	private int start_size = 0;
	private boolean isRefresh = true;
	private String mRefreshTime;
	private WeiQiangBean mActionWeiqiang = null;
	private String userid;
	private boolean isSelf = false;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		userid = getIntent().getStringExtra(UserInfoBean.KEY_USER_ID);
		setContentView(R.layout.layout_weiqiang_person);
		isSelf = userid.equals(BGApp.mUserId);
		(new TitleBar(mActivity)).initTitleBar((isSelf?"我":"TA")+"的微墙");
		initViews();
		WeiqiangRequest.getInstance().requestWeiqiangList(this, mActivity, String.valueOf(WeiQiangBean.WEIQIANG_FIND),userid,String.valueOf(start_size), String.valueOf(start_size+PAGE_SIZE_ADD));
	}
	
	private void initViews()
	{
		m_weiqiang_listview = (XListView) findViewById(R.id.weiqiang_person_listview);
		weiqiangAdapter = new WeiqiangAdapter(mWeiqiangList,mActivity,this);
		m_weiqiang_listview.setXListViewListener(this);
		m_weiqiang_listview.setAdapter(weiqiangAdapter);
		m_weiqiang_listview.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View v, int location, long arg3)
	{
		final WeiQiangBean weiqiang = (WeiQiangBean) adapter.getAdapter().getItem(location);
		Intent intent = new Intent(mActivity, WeiqiangDetailActivity.class);
		intent.putExtra(WeiQiangBean.KEY_WEIQIANG_BEAN,weiqiang);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		WeiQiangBean wqb = (WeiQiangBean) v.getTag();
		mActionWeiqiang = wqb;
		switch (v.getId()) {
		case R.id.av_zan:	//赞
			WeiqiangRequest.getInstance().requestWeiqiangZan(this, mActivity, wqb.weiboid);
			break;
		case R.id.av_reply:	//评论
			BToast.show(mActivity, "评论"+wqb.weiboid);
			break;
		case R.id.av_transpont:	//转发
			mActionWeiqiang.forward_count = String.valueOf(Integer.valueOf(mActionWeiqiang.forward_count)+1);
			weiqiangAdapter.notifyDataSetChanged();
			WeiqiangRequest.getInstance().requestWeiqiangTranspond(this, mActivity, wqb.weiboid,"");
			break;
		case R.id.av_share:	//分享
			break;
		default:
			break;
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
						m_weiqiang_listview.stopRefresh();
						m_weiqiang_listview.stopLoadMore();
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
				case 860006:
					break;
				case 860007:
					break;
				case 860008:	//赞微墙
					if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
						mActionWeiqiang.like_count = String.valueOf(Integer.valueOf(mActionWeiqiang.like_count)+1);
						weiqiangAdapter.notifyDataSetChanged();
					}else if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_HAS_ZAN){
						BToast.show(mActivity, "不要重复点赞");
					}
					break;
				}
			}
		}
	private void setWeiqiangData(List<WeiQiangBean> weiqiangs) {
		if(isRefresh){
			mRefreshTime = ToolUtils.getNowTime();
			m_weiqiang_listview.setRefreshTime(mRefreshTime);
		}
		
		m_weiqiang_listview.stopRefresh();
		m_weiqiang_listview.stopLoadMore();
		if(null == weiqiangs){
			return;
		}
		
		if(0 == weiqiangs.size()){
				m_weiqiang_listview.setPullLoadEnable(false);
	                 BToast.show(mActivity, "数据加载完毕");
		}
			
		if (isRefresh) {
			mWeiqiangList.clear();
			start_size = 0;
		} 
			
		if (weiqiangs.size() < PAGE_SIZE_ADD) {
			m_weiqiang_listview.setPullLoadEnable(false);
				BToast.show(mActivity, "数据加载完毕");
			} else {
				start_size += PAGE_SIZE_ADD;
				m_weiqiang_listview.setPullLoadEnable(true);
			}
		
		this.mWeiqiangList.addAll(weiqiangs);
		weiqiangAdapter.notifyDataSetChanged();
	}

	@Override
	public void onRefresh() {
		isRefresh = true;
			start_size = 0;
			WeiqiangRequest.getInstance().requestWeiqiangList(this, mActivity, String.valueOf(WeiQiangBean.WEIQIANG_ALL),userid,String.valueOf(start_size), String.valueOf(start_size+PAGE_SIZE_ADD));
	}
	@Override
	public void onLoadMore() {
		isRefresh = false;
			WeiqiangRequest.getInstance().requestWeiqiangList(this, mActivity, String.valueOf(WeiQiangBean.WEIQIANG_ALL),userid,String.valueOf(start_size), String.valueOf(start_size+PAGE_SIZE_ADD));
	}
}
