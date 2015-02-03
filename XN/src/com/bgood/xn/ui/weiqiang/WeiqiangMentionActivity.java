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
import com.bgood.xn.adapter.WeiqiangCorrelationAdapter;
import com.bgood.xn.bean.WeiQiangBean;
import com.bgood.xn.bean.WeiqiangCorattionBean;
import com.bgood.xn.bean.response.WeiqiangCorattionResponse;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.WeiqiangRequest;
import com.bgood.xn.ui.base.BaseShowDataActivity;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.view.xlistview.XListView.IXListViewListener;
import com.bgood.xn.widget.TitleBar;

/**
 * @todo: 与我相关的微墙
 * @date:2014-10-28 上午9:52:12
 * @author:hg_liuzl@163.com
 */
public class WeiqiangMentionActivity extends BaseShowDataActivity implements TaskListenerWithState,OnClickListener,OnItemClickListener,IXListViewListener {
	private List<WeiqiangCorattionBean> corationBeans = new ArrayList<WeiqiangCorattionBean>();
	private WeiqiangCorrelationAdapter adapter;
	
	private XListView m_mentionXLv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_correlation);
		(new TitleBar(mActivity)).initTitleBar("与我相关");
		m_mentionXLv = (XListView) findViewById(R.id.xlv_correlation);
		m_mentionXLv.setPullLoadEnable(true);
		m_mentionXLv.setPullRefreshEnable(true);
		m_mentionXLv.setXListViewListener(this);
		m_mentionXLv.setOnItemClickListener(this);
		adapter = new WeiqiangCorrelationAdapter(corationBeans, mActivity,this);
		m_mentionXLv.setAdapter(adapter);
		doRequest();
	}


	@Override
	public void onClick(View v) {
		
	}
	
	
	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			String strJson = bNetWork.getStrJson();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				WeiqiangCorattionResponse response = JSON.parseObject(strJson, WeiqiangCorattionResponse.class);
				setDataAdapter(m_mentionXLv, adapter, corationBeans, response.merelated,isRefreshAction);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View v, int location, long arg3) {
		final WeiqiangCorattionBean bean = (WeiqiangCorattionBean) adapter.getAdapter().getItem(location);
		Intent intent = new Intent(WeiqiangMentionActivity.this,WeiqiangDetailActivity.class);
		WeiQiangBean wqBean = WeiQiangBean.copy(bean);
		intent.putExtra(WeiQiangBean.KEY_WEIQIANG_BEAN, wqBean);
		startActivity(intent);
	}


	@Override
	public void onRefresh() {
		m_start_page = 0;
		isRefreshAction = true;
		doRequest();
	}

	@Override
	public void onLoadMore() {
		isRefreshAction = false;
		doRequest();
	}
	
	private void doRequest(){
		WeiqiangRequest.getInstance().requestWeiqiangWithMe(this, mActivity, m_start_page, m_start_page+PAGE_SIZE_ADD);
	}
}
