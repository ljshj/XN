package com.bgood.xn.ui.xuanneng.joke;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.adapter.CommentAdapter;
import com.bgood.xn.adapter.ImageAdapter;
import com.bgood.xn.bean.CommentBean;
import com.bgood.xn.bean.ImageBean;
import com.bgood.xn.bean.JokeBean;
import com.bgood.xn.bean.JokeBean.JokeActionType;
import com.bgood.xn.bean.response.CommentResponse;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.XuannengRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.utils.ImgUtils;
import com.bgood.xn.utils.ShareUtils;
import com.bgood.xn.utils.ToolUtils;
import com.bgood.xn.view.ActionView;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.dialog.BGDialog;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.view.xlistview.XListView.IXListViewListener;
import com.bgood.xn.widget.TitleBar;

/**
 * 
 * @todo:炫能详情
 * @date:2014-11-21 下午5:30:37
 * @author:hg_liuzl@163.com
 */
public class JokeDetailActivity extends BaseActivity implements OnClickListener,TaskListenerWithState,IXListViewListener
{
	/**炫能详情类的key*/
	public static final String BEAN_JOKE_KEY = "bean_joke_key";
	private XListView listview;
	
	public LinearLayout llTransArea;
	public ImageView ivAuthorImg;
	public TextView tvAuthorName;
	public TextView tvTime;
	public TextView tvOldAuthorName;
	public TextView tvComments;
	public TextView tvContent;
	public GridView gridView,oldGridview;
	public ActionView avZan,avReply,avTranstpont,avShare;
	
	private CommentAdapter commentAdapter;
	private List<CommentBean> comments = new ArrayList<CommentBean>();
	private JokeBean mJokeBean;
	private String mRefreshJokeTime;
	
	private int comment_start = 0;
	private JokeActionType type;
	private TitleBar titleBar;
	private ShareUtils share;


	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		share = new ShareUtils(mActivity);
		setContentView(R.layout.layout_weiqiang_detail);
		mJokeBean = (JokeBean) getIntent().getSerializableExtra(BEAN_JOKE_KEY);
		titleBar = new TitleBar(mActivity);
		titleBar.initTitleBar("详情");
		findView();
		setData(mJokeBean);
		
		XuannengRequest.getInstance().requestJokeContent(this, this, mJokeBean.jokeid, String.valueOf(comment_start), String.valueOf(comment_start+PAGE_SIZE_ADD));
	}

	

	/**
	 * 控件初始化方法
	 */
	private void findView()
	{
		listview = (XListView) findViewById(R.id.xlv_sapce);
		listview.setPullLoadEnable(true);
		listview.setPullRefreshEnable(false);
		listview.setXListViewListener(this);
	   
	   	View head_weiqiang_detail = inflater.inflate(R.layout.weiqiang_item_layout, listview, false);
		ivAuthorImg = (ImageView) head_weiqiang_detail.findViewById(R.id.iv_img);
		tvAuthorName = (TextView) head_weiqiang_detail.findViewById(R.id.tv_nick);
		tvTime = (TextView) head_weiqiang_detail.findViewById(R.id.tv_time);
		tvComments = (TextView) head_weiqiang_detail.findViewById(R.id.tv_comments);
		gridView = (GridView) head_weiqiang_detail.findViewById(R.id.gv_show_img);
		
		
		tvOldAuthorName = (TextView) head_weiqiang_detail.findViewById(R.id.tv_old_user);
		tvContent = (TextView) head_weiqiang_detail.findViewById(R.id.tv_content);
		llTransArea = (LinearLayout) head_weiqiang_detail.findViewById(R.id.ll_old_area);
		oldGridview = (GridView) head_weiqiang_detail.findViewById(R.id.gv_old_show_img);
		
		avZan = (ActionView) head_weiqiang_detail.findViewById(R.id.av_zan);
		avReply = (ActionView) head_weiqiang_detail.findViewById(R.id.av_reply);
		avTranstpont = (ActionView) head_weiqiang_detail.findViewById(R.id.av_transpont);
		avShare = (ActionView) head_weiqiang_detail.findViewById(R.id.av_share);
		
		avZan.setOnClickListener(this);
		avReply.setOnClickListener(this);
		avTranstpont.setOnClickListener(this);
		avShare.setOnClickListener(this);
		
		listview.addHeaderView(head_weiqiang_detail);
		commentAdapter = new CommentAdapter(comments,this);
		listview.setAdapter(commentAdapter);
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		mRefreshJokeTime = pUitl.getJokeDetailRefreshTime();
		listview.setRefreshTime(mRefreshJokeTime);
		
	}
	
	@Override
	public void onPause() {
		super.onPause();
		pUitl.setJokeDetailRefreshTime(mRefreshJokeTime);
	}
	
	private void setData(JokeBean jBean)
	{
        ImgUtils.setPhoto(jBean.photo,ivAuthorImg);
		
		tvTime.setText(ToolUtils.getFormatDate(jBean.date_time));
		
		tvAuthorName.setText(jBean.username);
		
		tvTime.setText(ToolUtils.getFormatDate(jBean.date_time));
    	
		if(!TextUtils.isEmpty(jBean.fromname)){	//如果转发人存在
			llTransArea.setVisibility(View.VISIBLE);
			tvOldAuthorName.setText(jBean.fromname);
			tvContent.setText(jBean.content);
			tvComments.setText(jBean.Comments);
			showImgs(jBean.imgs,oldGridview);
			
		}else{
			llTransArea.setVisibility(View.GONE);
			tvComments.setText(jBean.content);
			showImgs(jBean.imgs,gridView);
		}
		
		avZan.setCount(jBean.like_count);
    	
		avReply.setCount(jBean.comment_count);
    	
		avTranstpont.setCount(jBean.forward_count);
    	
		avShare.setCount(jBean.share_count);
		
	}

	/**处理九宫格图片**/
	@SuppressWarnings("null")
	private void showImgs(List<ImageBean> list,GridView gv){
		if(null==list && list.size()==0){	//如果没有图片
			gv.setVisibility(View.GONE);
		}else{
			gv.setVisibility(View.VISIBLE);
			ImageAdapter adapter = new ImageAdapter(list, mActivity);
			gv.setAdapter(adapter);
		}
	}
	
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            // 赞
            case R.id.av_zan:
            	mJokeBean.like_count = String.valueOf(Integer.valueOf(mJokeBean.like_count)+1);
            	
            	avZan.setCount(mJokeBean.like_count);
            	
            	XuannengRequest.getInstance().requestXuanZan(this, mActivity, mJokeBean.jokeid);
                break;
            // 评论
            case R.id.av_reply:
            	type = JokeActionType.RESPONSE;
                createSendDialog();
                break;
            // 转发
            case R.id.av_transpont:
            	type = JokeActionType.TRANSPOND;
            	createSendDialog();
                break;
            // 分享
            case R.id.av_share:
            	mJokeBean.share_count = String.valueOf(Integer.valueOf(mJokeBean.share_count)+1);
    			XuannengRequest.getInstance().requestXuanZan(this, mActivity, mJokeBean.jokeid);
    			share.setShareContent(mJokeBean.content, mJokeBean.imgs.size() > 0 ? mJokeBean.imgs.get(0).img:null);
                break;
            default:
                break;
        }
    }

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		listview.stopRefresh();
		listview.stopLoadMore();
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			String strJson = bNetWork.getStrJson();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				switch (bNetWork.getMessageType()) {
				case 870009:
					CommentResponse response = JSON.parseObject(strJson, CommentResponse.class);
					List<CommentBean> comments = response.comments;
					setCommentData(comments);
					break;

				default:
					break;
				}
				
			}
		}
	}
	
	private void setCommentData(List<CommentBean> comments) {
		if (null == comments) {
			listview.setPullLoadEnable(false);
			BToast.show(mActivity, "数据加载完毕");
			return;
		}
		if (comments.size() < PAGE_SIZE_ADD) {
			listview.setPullLoadEnable(false);
			BToast.show(mActivity, "数据加载完毕");
		} else {
			listview.setPullLoadEnable(true);
			comment_start+=PAGE_SIZE_ADD;
		}

		this.comments.addAll(comments);
		commentAdapter.notifyDataSetChanged();
	}
	
	
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
		vSend.findViewById(R.id.btn_send).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				String content = etcontent.getText().toString();
				if(TextUtils.isEmpty(content))
				{
					return;
				}else{
					if(type == JokeActionType.TRANSPOND){
						mJokeBean.forward_count = String.valueOf(Integer.valueOf(mJokeBean.forward_count)+1);
						avTranstpont.setCount(mJokeBean.forward_count);
						XuannengRequest.getInstance().requestXuanTransport(JokeDetailActivity.this, mActivity, mJokeBean.jokeid,content);
					}else{
						CommentBean wcb = new CommentBean();
						wcb.userid = String.valueOf(BGApp.mUserId);
						wcb.name = BGApp.mUserBean.nickn;
						wcb.photo = BGApp.mUserBean.photo;
						wcb.content = content;
						wcb.commenttime = ToolUtils.getStandardTime();
						comments.add(0, wcb);
						commentAdapter.notifyDataSetChanged();
						mJokeBean.comment_count = String.valueOf(Integer.valueOf(mJokeBean.comment_count)+1);
						avReply.setCount(mJokeBean.comment_count);
						XuannengRequest.getInstance().requestXuanComment(JokeDetailActivity.this, mActivity, mJokeBean.jokeid,content);
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
	@Override
	public void onRefresh() {
		
	}
	@Override
	public void onLoadMore() {
		XuannengRequest.getInstance().requestJokeContent(this, this, mJokeBean.jokeid, String.valueOf(comment_start), String.valueOf(comment_start+PAGE_SIZE_ADD));
	}
	
}
