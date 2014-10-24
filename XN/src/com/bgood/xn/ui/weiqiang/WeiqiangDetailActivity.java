//package com.bgood.xn.ui.weiqiang;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.view.ViewGroup.LayoutParams;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bgood.xn.R;
//import com.bgood.xn.adapter.WeiqiangCommentAdapter;
//import com.bgood.xn.bean.WeiQiangBean;
//import com.bgood.xn.bean.WeiqiangCommentBean;
//import com.bgood.xn.network.BaseNetWork.ReturnCode;
//import com.bgood.xn.network.BaseNetWork;
//import com.bgood.xn.network.HttpRequestInfo;
//import com.bgood.xn.network.HttpResponseInfo;
//import com.bgood.xn.network.HttpRquestAsyncTask.TaskListenerWithState;
//import com.bgood.xn.network.request.HomeRequest;
//import com.bgood.xn.network.request.WeiqiangRequest;
//import com.bgood.xn.system.SystemConfig;
//import com.bgood.xn.ui.BaseActivity;
//import com.bgood.xn.view.BToast;
//import com.bgood.xn.view.LoadingProgress;
//import com.bgood.xn.view.xlistview.XListView;
//import com.bgood.xn.widget.CMyGridView;
//import com.squareup.picasso.Picasso;
//
//
///**
// * @todo:微墙详情界面
// * @date:2014-10-24 下午2:22:14
// */
//public class WeiqiangDetailActivity extends BaseActivity implements OnClickListener,TaskListenerWithState
//{
//	/**微墙详情类的key*/
//	public static final String BEAN_WEIQIANG_KEY = "bean_weiqiang_key";
//	private XListView listview;
//	
//	private LinearLayout m_replyLl = null;  // 评论
//	private EditText m_replyContentEt = null;  // 评论内容
//	private Button m_replySendBtn = null;  // 发送评论按钮
//	
//	private ImageView m_sendUserIconImgV = null;  // 发送用户头像
//	private TextView m_sendUserNameTv = null;    // 发送用户姓名
//	private TextView m_sendTimeTv = null;        // 发送时间
//	private TextView m_sendContentTv = null;     // 发送内容
//	private LinearLayout m_zanCountLl = null;    // 点赞布局
//	private TextView m_zanCountTv = null;        // 点赞的数量
//	private LinearLayout m_replyCountLl = null;  // 评论布局
//	private TextView m_replyCountTv = null;      // 评论的数量
//	private LinearLayout m_sendCountLl = null;    // 转发布局
//	private TextView m_sendCountTv = null;        // 转发的数量
//	private LinearLayout m_shareCountLl = null;    // 分享布局
//	private TextView m_shareCountTv = null;        // 分享的数量
//	private LinearLayout layout_images;
//	private CMyGridView oneImgV;
//	
//	private WeiqiangCommentAdapter adapter;
//	private List<WeiqiangCommentBean> m_list = new ArrayList<WeiqiangCommentBean>();
//	private WeiQiangBean mWeiQiangBean;
//	
//	private int comment_start = 0;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.layout_weiqiang_detail);
//		mWeiQiangBean = (WeiQiangBean) getIntent().getSerializableExtra(BEAN_WEIQIANG_KEY);
//		findView();
//		setListener();
//		WeiqiangRequest.getInstance().requestWeiqiangContent(this, this, mWeiQiangBean.userid, String.valueOf(comment_start), String.valueOf(comment_start+PAGE_SIZE_ADD));
//	}
//	
//
//	/**
//	 * 控件初始化方法
//	 */
//	private void findView()
//	{
//	    m_replyLl = (LinearLayout) findViewById(R.id.weiqiang_detail_ll_reply);  // 评论
//	    m_replyContentEt = (EditText) findViewById(R.id.weiqiang_detail_et_reply_content);  // 评论内容
//	    m_replySendBtn = (Button) findViewById(R.id.weiqiang_detail_btn_reply_send);  // 发送评论按钮
//	    
//		listview = (XListView) findViewById(R.id.weiqiang_detail_xlv);
//		View head_weiqiang_detail = inflater.inflate(R.layout.head_weiqiang_detail, listview, false);
//		m_sendUserIconImgV = (ImageView) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_imgv_send_icon);
//		m_sendUserNameTv = (TextView) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_tv_name);
//		m_sendTimeTv = (TextView) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_tv_time);
//		m_sendContentTv = (TextView) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_tv_content);
//		m_zanCountLl = (LinearLayout) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_ll_zan_count);
//		m_zanCountTv = (TextView) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_tv_zan_count);
//		
//		m_replyCountLl = (LinearLayout) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_ll_reply_count);
//		m_replyCountTv = (TextView) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_tv_reply_count);
//		
//		m_sendCountLl = (LinearLayout) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_ll_transform_send_count);
//		m_sendCountTv = (TextView) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_tv_transform_send_count);
//		
//		m_shareCountLl = (LinearLayout) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_ll_share_count);
//		m_shareCountTv = (TextView) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_tv_share_count);
//		
//		layout_images = (LinearLayout) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_ll_comment_images);
//		
//		listview.addHeaderView(head_weiqiang_detail);
//		adapter = new WeiqiangCommentAdapter(m_list,this);
//		listview.setAdapter(adapter);
//	}
//	
//	/**
//	 * 控件时间监听方法
//	 */
//	private void setListener()
//	{
//	    m_zanCountLl.setOnClickListener(this);
//	    m_replyCountLl.setOnClickListener(this);
//	    m_sendCountLl.setOnClickListener(this);
//	    m_shareCountLl.setOnClickListener(this);
//	    m_replySendBtn.setOnClickListener(this);
//	}
//	
//	private void setData(WeiQiangBean weiQiangBean)
//	{
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
//    	
//    	if (weiQiangBean.reverts != null)
//        {
//            if (weiQiangBean.reverts.size() < 10)
//            {
//                listview.setPullLoadEnable(false);
//            }
//            
//            m_list.addAll(weiQiangBean.reverts);
//            adapter.notifyDataSetChanged();
//        }
//    	
//    	if (weiQiangBean.imageList != null && weiQiangBean.imageList.size() > 0)
//        {
//    	    
//    	    layout_images.setVisibility(View.VISIBLE);
//            oneImgV.setVisibility(View.VISIBLE);
//            ImageAdapter imageAdapter = new ImageAdapter(this, weiQiangBean.imageList);
//            oneImgV.setAdapter(imageAdapter);
//    	  
//        }
//	}
//
//
//	@Override
//	public void getWeiqiangContentCB(CommentsDTO commentsDTO, Reulst result_state)
//	{
//		super.getWeiqiangContentCB(commentsDTO, result_state);
//		if (result_state.resultCode == ReturnCode.RETURNCODE_OK && commentsDTO != null)
//		{
//			comment_start = comment_start + comment_add;
//			
//			m_progressBar.setVisibility(View.GONE);
//			listview.setVisibility(View.VISIBLE);
//			
//			setData(commentsDTO);
//		}
//	}
//
//    @Override
//    public void onClick(View v)
//    {
//        switch (v.getId())
//        {
//            // 赞
//            case R.id.weiqiang_detail_ll_zan_count:
//                zanWeiqiang();
//                break;
//            // 评论
//            case R.id.weiqiang_detail_ll_reply_count:
//                m_replyLl.setVisibility(View.VISIBLE);
//                break;
//            // 转发
//            case R.id.weiqiang_detail_ll_transform_send_count:
//                photoDialogShow();
//                break;
//            // 分享
//            case R.id.weiqiang_detail_ll_share_count:
//              
//                break;
//            
//            // 发送评论
//            case R.id.weiqiang_detail_btn_reply_send:
//                String content = m_replyContentEt.getText().toString().trim();
//                if (!TextUtils.isEmpty(content))
//                {
//                    replyWeiqiang(content);
//                }else{
//                	BToast.show(mActivity, "请输入评论内容");
//                	return;
//                }
//                break;
//            
//            default:
//                break;
//        }
//    }
//    
//    /**
//     * 点赞
//     */
//    private void zanWeiqiang()
//    {
//       WeiqiangRequest.getInstance().requestWeiqiangZan(this, mActivity, mWeiQiangBean.weiboid);
//    }
//    
//    /**
//     * 评论
//     */
//    private void commentWeiqiang(final String content)
//    {
//    	WeiqiangRequest.getInstance().requestWeiqiangReply(this, mActivity, mWeiQiangBean.weiboid, content);
//    }
//    
//    /**
//     * 转发
//     */
//    private void transportWeiqiang()
//    {
//        
//    	WeiqiangRequest.getInstance().requestWeiqiangTranspond(this, mActivity, mWeiQiangBean.weiboid);
//    	
//    }
//
//
//
//    
//    @Override
//    public void zanWeiqiangCB(Reulst result_state)
//    {
//        super.zanWeiqiangCB(result_state);
//        
//        WindowUtil.getInstance().DismissAllDialog();
//        if (result_state.resultCode == ReturnCode.RETURNCODE_OK)
//        {
//            int count = Integer.parseInt(m_zanCountTv.getText().toString().trim());
//            m_zanCountTv.setText((count + 1) + "");
//        } else
//        {
//            Toast.makeText(WeiqiangCommentDetailActivity.this, "点赞失败", Toast.LENGTH_SHORT).show();
//        }
//    }
//    
//    
//    @Override
//    public void commentaryWeiqiangCB(Reulst result_state)
//    {
//        // TODO Auto-generated method stub
//        super.commentaryWeiqiangCB(result_state);
//        
//        WindowUtil.getInstance().DismissAllDialog();
//        if (result_state.resultCode == ReturnCode.RETURNCODE_OK)
//        {
//            int count = Integer.parseInt(m_replyCountTv.getText().toString().trim());
//            m_replyCountTv.setText((count + 1) + "");
//            m_replyLl.setVisibility(View.GONE);
//        } else
//        {
//            Toast.makeText(mActivity, "评论失败", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public void transformWeiqiangCB(Reulst result_state)
//    {
//        super.zanWeiqiangCB(result_state);
//        
//        WindowUtil.getInstance().DismissAllDialog();
//        if (result_state.resultCode == ReturnCode.RETURNCODE_OK)
//        {
//            int count = Integer.parseInt(m_sendCountTv.getText().toString().trim());
//            m_sendCountTv.setText((count + 1) + "");
//        } else
//        {
//            Toast.makeText(mActivity, "转发失败", Toast.LENGTH_SHORT).show();
//        }
//    }
//  
//
//	@Override
//	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
//		LoadingProgress.getInstance().dismiss();
//		switch (info.getState()) {
//		case STATE_ERROR_SERVER:
//			Toast.makeText(mActivity, "服务器地址错误", Toast.LENGTH_SHORT).show();
//			break;
//		case STATE_NO_NETWORK_CONNECT:
//			Toast.makeText(mActivity, "没有网络，请检查您的网络连接", Toast.LENGTH_SHORT).show();
//			break;
//		case STATE_TIME_OUT:
//			Toast.makeText(mActivity, "连接超时", Toast.LENGTH_SHORT).show();
//			break;
//		case STATE_UNKNOWN:
//			Toast.makeText(mActivity, "未知错误", Toast.LENGTH_SHORT).show();
//			break;
//		case STATE_OK:
//			BaseNetWork bNetWork = info.getmBaseNetWork();
//			JSONObject body = bNetWork.getBody();
//			
//		default:
//			break;
//			}
//	}
//
//}
