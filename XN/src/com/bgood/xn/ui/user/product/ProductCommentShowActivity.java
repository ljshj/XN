package com.bgood.xn.ui.user.product;


import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.adapter.ProductCommentAdapter;
import com.bgood.xn.bean.ProductCommentBean;
import com.bgood.xn.bean.response.ProductCommentResponse;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.ProductRequest;
import com.bgood.xn.ui.BaseActivity;
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
public class ProductCommentShowActivity extends BaseActivity implements TaskListenerWithState,OnClickListener
{
	private XListView m_ProductComment;
	private List<ProductCommentBean> mProductCommentList = new ArrayList<ProductCommentBean>();
	private ProductCommentAdapter ProductCommentAdapter;
	private String mRefreshTime;
	private TitleBar titleBar = null;
	private int comment_start = 0;
	private String product_id;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_product_comment);
	    titleBar = new TitleBar(mActivity);
	    titleBar.initAllBar("所有评论","新评论");
		initViews();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mRefreshTime = pUitl.getProductCommentRefreshTime();
		m_ProductComment.setRefreshTime(mRefreshTime);
		comment_start = 0;
		ProductRequest.getInstance().requestProductCommentList(this, mActivity, product_id, String.valueOf(comment_start), String.valueOf(comment_start+PAGE_SIZE_ADD));
	}
	
	@Override
	public void onPause() {
		super.onPause();
		pUitl.setProductCommentRefreshTime(mRefreshTime);
	}
	
	private void initViews()
	{
		m_ProductComment = (XListView) findViewById(R.id.lv_product_comment);
		m_ProductComment.setPullRefreshEnable(false);
		ProductCommentAdapter = new ProductCommentAdapter(mProductCommentList,mActivity);
		m_ProductComment.setAdapter(ProductCommentAdapter);
		 // 确定按钮
        titleBar.rightBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
            	Intent intent = new Intent(mActivity, ProductCommentActivity.class);
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
				ProductCommentResponse response  = JSON.parseObject(strJson, ProductCommentResponse.class);
				setProductCommentData(response.comments);
				}else{
					BToast.show(mActivity, "获取数据失败");
				}
			}
		}
	
	private void setProductCommentData(List<ProductCommentBean> ProductComments) {
			mRefreshTime = ToolUtils.getNowTime();
			m_ProductComment.setRefreshTime(mRefreshTime);
			this.mProductCommentList.addAll(ProductComments);
			ProductCommentAdapter.notifyDataSetChanged();
	}
}
