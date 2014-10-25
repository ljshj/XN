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

import com.bgood.xn.R;
import com.bgood.xn.adapter.WeiqiangCommentAdapter;
import com.bgood.xn.bean.WeiQiangBean;
import com.bgood.xn.bean.WeiqiangCommentBean;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.WeiqiangRequest;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.widget.CMyGridView;


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
	
	private ImageView m_sendUserIconImgV = null;  // 发送用户头像
	private TextView m_sendUserNameTv = null;    // 发送用户姓名
	private TextView m_sendTimeTv = null;        // 发送时间
	private TextView m_sendContentTv = null;     // 发送内容
	private LinearLayout m_zanCountLl = null;    // 点赞布局
	private TextView m_zanCountTv = null;        // 点赞的数量
	private LinearLayout m_replyCountLl = null;  // 评论布局
	private TextView m_replyCountTv = null;      // 评论的数量
	private LinearLayout m_sendCountLl = null;    // 转发布局
	private TextView m_sendCountTv = null;        // 转发的数量
	private LinearLayout m_shareCountLl = null;    // 分享布局
	private TextView m_shareCountTv = null;        // 分享的数量
	private LinearLayout layout_images;
	private CMyGridView oneImgV;
	
	private WeiqiangCommentAdapter adapter;
	private List<WeiqiangCommentBean> m_list = new ArrayList<WeiqiangCommentBean>();
	private WeiQiangBean mWeiQiangBean;
	
	private int comment_start = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_weiqiang_detail);
		mWeiQiangBean = (WeiQiangBean) getIntent().getSerializableExtra(BEAN_WEIQIANG_KEY);
		findView();
		setListener();
		WeiqiangRequest.getInstance().requestWeiqiangContent(this, this, mWeiQiangBean.userid, String.valueOf(comment_start), String.valueOf(comment_start+PAGE_SIZE_ADD));
	}
	

	/**
	 * 控件初始化方法
	 */
	private void findView()
	{
	    m_replyLl = (LinearLayout) findViewById(R.id.weiqiang_detail_ll_reply);  // 评论
	    m_replyContentEt = (EditText) findViewById(R.id.weiqiang_detail_et_reply_content);  // 评论内容
	    m_replySendBtn = (Button) findViewById(R.id.weiqiang_detail_btn_reply_send);  // 发送评论按钮
	    
		listview = (XListView) findViewById(R.id.weiqiang_detail_xlv);
		View head_weiqiang_detail = inflater.inflate(R.layout.head_weiqiang_detail, listview, false);
		m_sendUserIconImgV = (ImageView) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_imgv_send_icon);
		m_sendUserNameTv = (TextView) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_tv_name);
		m_sendTimeTv = (TextView) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_tv_time);
		m_sendContentTv = (TextView) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_tv_content);
		m_zanCountLl = (LinearLayout) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_ll_zan_count);
		m_zanCountTv = (TextView) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_tv_zan_count);
		
		m_replyCountLl = (LinearLayout) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_ll_reply_count);
		m_replyCountTv = (TextView) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_tv_reply_count);
		
		m_sendCountLl = (LinearLayout) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_ll_transform_send_count);
		m_sendCountTv = (TextView) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_tv_transform_send_count);
		
		m_shareCountLl = (LinearLayout) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_ll_share_count);
		m_shareCountTv = (TextView) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_tv_share_count);
		
		layout_images = (LinearLayout) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_ll_comment_images);
		
		listview.addHeaderView(head_weiqiang_detail);
		adapter = new WeiqiangCommentAdapter(m_list,this);
		listview.setAdapter(adapter);
	}
	
	/**
	 * 控件时间监听方法
	 */
	private void setListener()
	{
	    m_zanCountLl.setOnClickListener(this);
	    m_replyCountLl.setOnClickListener(this);
	    m_sendCountLl.setOnClickListener(this);
	    m_shareCountLl.setOnClickListener(this);
	    m_replySendBtn.setOnClickListener(this);
	}
	
	private void setData(WeiQiangBean weiQiangBean)
	{
//		if (weiQiangBean != null && weiQiangBean.senderIcon != null && !weiQiangBean.senderIcon.equals(""))
//			Picasso.with(this).load(weiQiangBean.senderIcon).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(m_sendUserIconImgV);
//    	
//		m_sendUserNameTv.setText(weiQiangBean.senderName);
//    	m_sendTimeTv.setText(weiQiangBean.sendTime);
//    	m_sendContentTv.setText(weiQiangBean.content);
//    	m_zanCountTv.setText(weiQiangBean.like_count + "");
//    	m_replyCountTv.setText(weiQiangBean.revertCount + "");
//    	m_sendCountTv.setText(weiQiangBean.forward_count + "");
//    	m_shareCountTv.setText(weiQiangBean.share_count  + "");
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
                break;
            // 转发
            case R.id.weiqiang_detail_ll_transform_send_count:
            	transportWeiqiang();
                break;
            // 分享
            case R.id.weiqiang_detail_ll_share_count:
              
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
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			JSONObject body = bNetWork.getBody();
			String strJson = bNetWork.getStrJson();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){}}
			
	}
}
