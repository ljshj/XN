package com.bgood.xn.ui.weiqiang;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.adapter.WeiqiangCommentAdapter;
import com.bgood.xn.bean.WeiQiangBean;
import com.bgood.xn.bean.WeiqiangCommentBean;
import com.bgood.xn.bean.response.WeiqiangDetailResponse;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.WeiqiangRequest;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.utils.SharedUtil;
import com.bgood.xn.utils.ToolUtils;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.widget.TitleBar;
import com.squareup.picasso.Picasso;


/**
 * @todo:微墙详情界面
 * @date:2014-10-24 下午2:22:14
 */
public class WeiqiangDetailActivity extends BaseActivity implements OnClickListener,TaskListenerWithState
{
	/**微墙详情类的key*/
	public static final String BEAN_WEIQIANG_KEY = "bean_weiqiang_key";
	private XListView listview;
	
	private LinearLayout m_replyLl = null;  // 评论
	private EditText m_replyContentEt = null;  // 评论内容
	private Button m_replySendBtn = null;  // 发送评论按钮
	
	public ImageView ivAuthorImg;
	public TextView tvAuthorName;
	public TextView distanceTv;
	public ImageView ivDelete;
	public TextView tvTime;
	public TextView tvOldAuthorName;
	public TextView tvContent;
	public TextView tvZanCount;
	public TextView tvReplyCount;
	public TextView tvTranspontCount;
	public TextView tvShareCount;
	
	private WeiqiangCommentAdapter weiQiangCommentAdapter;
	private List<WeiqiangCommentBean> weiqiangComments = new ArrayList<WeiqiangCommentBean>();
	private WeiQiangBean mWeiQiangBean;
	private String mRefreshDetailWeiqiangTime;
	
	private int comment_start = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_weiqiang_detail);
		mWeiQiangBean = (WeiQiangBean) getIntent().getSerializableExtra(BEAN_WEIQIANG_KEY);
		(new TitleBar(mActivity)).initTitleBar("微墙正文");
		findView();
		setListener();
		setData(mWeiQiangBean);
		WeiqiangRequest.getInstance().requestWeiqiangContent(this, this, mWeiQiangBean.weiboid, String.valueOf(comment_start), String.valueOf(comment_start+PAGE_SIZE_ADD));
		
	}
	

	/**
	 * 控件初始化方法
	 */
	private void findView()
	{
	    m_replyContentEt = (EditText) findViewById(R.id.weiqiang_detail_et_reply_content);  // 评论内容
	    m_replySendBtn = (Button) findViewById(R.id.weiqiang_detail_btn_reply_send);  // 发送评论按钮
	    
		listview = (XListView) findViewById(R.id.weiqiang_detail_xlv);
		listview.setPullLoadEnable(true);
		listview.setPullRefreshEnable(false);
	   
	   	View head_weiqiang_detail = inflater.inflate(R.layout.weiqiang_item_layout, listview, false);
		ivAuthorImg = (ImageView) head_weiqiang_detail.findViewById(R.id.iv_author);
		tvAuthorName = (TextView) head_weiqiang_detail.findViewById(R.id.tv_weiqiang_author);
		distanceTv = (TextView) head_weiqiang_detail.findViewById(R.id.tv_weiqiang_distance);
		ivDelete = (ImageView) head_weiqiang_detail.findViewById(R.id.iv_delete);
		tvTime = (TextView) head_weiqiang_detail.findViewById(R.id.tv_weiqiang_time);
		tvOldAuthorName = (TextView) head_weiqiang_detail.findViewById(R.id.tv_content_fromuser);
		tvContent = (TextView) head_weiqiang_detail.findViewById(R.id.tv_content);
		tvZanCount = (TextView) head_weiqiang_detail.findViewById(R.id.tv_zan_count);
		tvReplyCount = (TextView) head_weiqiang_detail.findViewById(R.id.tv_comment_count);
		tvTranspontCount = (TextView) head_weiqiang_detail.findViewById(R.id.tv_transpont_count);
		tvShareCount = (TextView) head_weiqiang_detail.findViewById(R.id.tv_share_count);
		
		listview.addHeaderView(head_weiqiang_detail);
		weiQiangCommentAdapter = new WeiqiangCommentAdapter(weiqiangComments,this);
		listview.setAdapter(weiQiangCommentAdapter);
	}
	
	/**
	 * 控件时间监听方法
	 */
	private void setListener()
	{
		tvZanCount.setOnClickListener(this);
		tvReplyCount.setOnClickListener(this);
		tvTranspontCount.setOnClickListener(this);
		tvShareCount.setOnClickListener(this);
	    m_replySendBtn.setOnClickListener(this);
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		SharedUtil.initSharedPane(mActivity);
		mRefreshDetailWeiqiangTime = pUitl.getWeiqiangDetailRefreshTime();
		listview.setRefreshTime(mRefreshDetailWeiqiangTime);
		
	}
	
	@Override
	public void onPause() {
		super.onPause();
		pUitl.setWeiqiangDetailRefreshTime(mRefreshDetailWeiqiangTime);
	}
	
	private void setData(WeiQiangBean weiQiangBean)
	{
		if (weiQiangBean != null && !TextUtils.isEmpty(weiQiangBean.photo))
			Picasso.with(this).load(weiQiangBean.photo).placeholder(R.drawable.icon_default).error(R.drawable.icon_default).into(ivAuthorImg);
		
		distanceTv.setText(weiQiangBean.distance);
		distanceTv.setVisibility(View.GONE);
		
		tvTime.setText(ToolUtils.getFormatDate(weiQiangBean.date_time));
		
		if (!TextUtils.isEmpty(weiQiangBean.fromname))
		{
			tvOldAuthorName.setVisibility(View.VISIBLE);
			tvOldAuthorName.setText(weiQiangBean.fromname);
		}
		else
		{
			tvOldAuthorName.setVisibility(View.GONE);
		}
		
		
		tvAuthorName.setText(weiQiangBean.name);
		
		tvTime.setText(ToolUtils.getFormatDate(weiQiangBean.date_time));
    	
		tvContent.setText(weiQiangBean.content);
    	
		tvZanCount.setText(weiQiangBean.like_count);
    	
		tvReplyCount.setText(weiQiangBean.forward_count);
    	
		tvTranspontCount.setText(weiQiangBean.forward_count);
    	
		tvShareCount.setText(weiQiangBean.share_count);
	}

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            // 赞
            case R.id.weiqiang_detail_ll_zan_count:
                zanWeiqiang();
                break;
            // 评论
            case R.id.weiqiang_detail_ll_reply_count:
                m_replyLl.setVisibility(View.VISIBLE);
                commentWeiqiang("");
                break;
            // 转发
            case R.id.weiqiang_detail_ll_transform_send_count:
            	transportWeiqiang();
                break;
            // 分享
            case R.id.weiqiang_detail_ll_share_count:
              SharedUtil.openShare(mActivity, "炫能App", mWeiQiangBean.content, mWeiQiangBean.photo);
                break;
            
            // 发送评论
            case R.id.weiqiang_detail_btn_reply_send:
                String content = m_replyContentEt.getText().toString().trim();
                if (!TextUtils.isEmpty(content))
                {
                	commentWeiqiang(content);
                }else{
                	BToast.show(mActivity, "请输入评论内容");
                	return;
                }
                break;
            
            default:
                break;
        }
    }
    
    /**
     * 点赞
     */
    private void zanWeiqiang()
    {
       WeiqiangRequest.getInstance().requestWeiqiangZan(this, mActivity, mWeiQiangBean.weiboid);
    }
    
    /**
     * 评论
     */
    private void commentWeiqiang(final String content)
    {
    	WeiqiangRequest.getInstance().requestWeiqiangReply(this, mActivity, mWeiQiangBean.weiboid, content);
    }
    
    /**
     * 转发
     */
    private void transportWeiqiang()
    {
    	WeiqiangRequest.getInstance().requestWeiqiangTranspond(this, mActivity, mWeiQiangBean.weiboid);
    }


  
  

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		listview.stopRefresh();
		listview.stopLoadMore();
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			JSONObject body = bNetWork.getBody();
			String strJson = bNetWork.getStrJson();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				switch (bNetWork.getMessageType()) {
				case 860002:
					WeiqiangDetailResponse response = JSON.parseObject(strJson, WeiqiangDetailResponse.class);
					List<WeiqiangCommentBean> comments = response.comments;
					setWeiqiangCommentData(comments);
					break;

				default:
					break;
				}
				
			}
		}
	}
	
	private void setWeiqiangCommentData(List<WeiqiangCommentBean> comments) {
		if (null == comments) {
			return;
		}

		if (0 == comments.size()) {
			listview.setPullLoadEnable(false);
			BToast.show(mActivity, "数据加载完毕");
		}

		if (comments.size() <= PAGE_SIZE_ADD) {
			listview.setPullLoadEnable(false);
			BToast.show(mActivity, "数据加载完毕");
		} else {
			comment_start += PAGE_SIZE_ADD;
			listview.setPullLoadEnable(true);
		}

		this.weiqiangComments.addAll(comments);
		weiQiangCommentAdapter.notifyDataSetChanged();
	}
	
}
