package com.bgood.xn.ui.user.product;


import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.adapter.CommentAdapter;
import com.bgood.xn.bean.CommentBean;
import com.bgood.xn.bean.response.CommentResponse;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.ProductRequest;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.utils.ToolUtils;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.view.xlistview.XListView.IXListViewListener;
import com.bgood.xn.widget.TitleBar;

/**
 * 
 * @todo:意见反馈列表
 * @date:2014-10-24 下午3:50:55
 * @author:hg_liuzl@163.com
 */
public class ProductCommentShowActivity extends BaseActivity implements TaskListenerWithState,OnClickListener,IXListViewListener
{
	/**产品编号**/
	public static final String KEY_PRODUCT_ID = "key_product_id";
	private XListView m_ProductComment;
	private List<CommentBean> mProductCommentList = new ArrayList<CommentBean>();
	private CommentAdapter commentAdapter;
	private String mRefreshTime;
	private TitleBar titleBar = null;
	private int comment_start = 0;
	private String product_id;
	private boolean isRefresh = true;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_product_comment);
		product_id = getIntent().getStringExtra(KEY_PRODUCT_ID);
	    titleBar = new TitleBar(mActivity);
	    titleBar.initAllBar("所有评论","新评论");
		initViews();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mRefreshTime = pUitl.getProductCommentRefreshTime();
		m_ProductComment.setRefreshTime(mRefreshTime);
		isRefresh = true;
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
		m_ProductComment.setXListViewListener(this);
		commentAdapter = new CommentAdapter(mProductCommentList,mActivity);
		m_ProductComment.setAdapter(commentAdapter);
		 // 确定按钮
        titleBar.rightBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
            	Intent intent = new Intent(mActivity, ProductCommentActivity.class);
            	intent.putExtra(KEY_PRODUCT_ID, product_id);
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
			m_ProductComment.stopLoadMore();
			m_ProductComment.stopRefresh();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				CommentResponse response  = JSON.parseObject(strJson, CommentResponse.class);
				setProductCommentData(response.comments);
				}else{
					BToast.show(mActivity, "获取数据失败");
				}
				
			}
		}
	
	private void setProductCommentData(List<CommentBean> productComments) {
			
			if(productComments.size() < PAGE_SIZE_ADD){
				m_ProductComment.setPullLoadEnable(false);
				BToast.show(mActivity, "数据加载完毕");
			}else{
				comment_start += PAGE_SIZE_ADD;
				m_ProductComment.setPullLoadEnable(true);
			}
			
			if(isRefresh)	//如果相等，说明是第一次刷新
			{
				mProductCommentList.clear();
				mRefreshTime = ToolUtils.getNowTime();
				m_ProductComment.setRefreshTime(mRefreshTime);
			}
			isRefresh = false;
			this.mProductCommentList.addAll(productComments);
			commentAdapter.notifyDataSetChanged();
	}

	@Override
	public void onRefresh() {
		comment_start = 0;
		isRefresh = true;
		ProductRequest.getInstance().requestProductCommentList(this, mActivity, product_id, String.valueOf(comment_start), String.valueOf(comment_start+PAGE_SIZE_ADD));
	}

	@Override
	public void onLoadMore() {
		isRefresh = false;
		ProductRequest.getInstance().requestProductCommentList(this, mActivity, product_id, String.valueOf(comment_start), String.valueOf(comment_start+PAGE_SIZE_ADD));
	}
}
