package com.bgood.xn.ui.user.more;


import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.adapter.FeedbackAdapter;
import com.bgood.xn.bean.FeedbackBean;
import com.bgood.xn.bean.response.FeedbackResponse;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.utils.ToolUtils;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.widget.TitleBar;

/**
 * 
 * @todo:意见反馈列表
 * @date:2014-10-24 下午3:50:55
 * @author:hg_liuzl@163.com
 */
public class FeedbackShowActivity extends BaseActivity implements TaskListenerWithState,OnClickListener
{
	private XListView m_feedback;
	private List<FeedbackBean> mFeedbackList = new ArrayList<FeedbackBean>();
	private FeedbackAdapter feedbackAdapter;
	private String mRefreshTime;
	 private TitleBar titleBar = null;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_layout_feedback_show);
	    titleBar = new TitleBar(mActivity);
	    titleBar.initAllBar("意见反馈","新反馈");
		initViews();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mRefreshTime = pUitl.getFeedbackRefreshTime();
		m_feedback.setRefreshTime(mRefreshTime);
		UserCenterRequest.getInstance().requestFeedbackList(this, mActivity);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		pUitl.setFeedbackRefreshTime(mRefreshTime);
	}
	
	private void initViews()
	{
		m_feedback = (XListView) findViewById(R.id.lv_feedback);
		m_feedback.setPullLoadEnable(false);
		m_feedback.setPullRefreshEnable(false);
		feedbackAdapter = new FeedbackAdapter(mFeedbackList,mActivity);
		m_feedback.setAdapter(feedbackAdapter);
		 // 确定按钮
        titleBar.rightBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
            	Intent intent = new Intent(mActivity, FeedbackActivity.class);
            	startActivity(intent);
            }
        });
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
				FeedbackResponse response  = JSON.parseObject(strJson, FeedbackResponse.class);
				setFeedbackData(response.items);
				}else{
					BToast.show(mActivity, "获取数据失败");
				}
			}
		}
	
	private void setFeedbackData(List<FeedbackBean> feedbacks) {
			mRefreshTime = ToolUtils.getNowTime();
			m_feedback.setRefreshTime(mRefreshTime);
			this.mFeedbackList.addAll(feedbacks);
			feedbackAdapter.notifyDataSetChanged();
	}
}
