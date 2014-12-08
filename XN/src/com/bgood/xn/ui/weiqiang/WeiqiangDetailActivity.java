package com.bgood.xn.ui.weiqiang;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.bean.WeiQiangBean;
import com.bgood.xn.bean.WeiQiangBean.WeiqiangActionType;
import com.bgood.xn.bean.response.CommentResponse;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.WeiqiangRequest;
import com.bgood.xn.network.request.XuannengRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.ui.user.info.NameCardActivity;
import com.bgood.xn.utils.ToolUtils;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.dialog.BGDialog;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.view.xlistview.XListView.IXListViewListener;
import com.bgood.xn.widget.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;


/**
 * @todo:微墙详情界面
 * @date:2014-10-24 下午2:22:14
 */
public class WeiqiangDetailActivity extends BaseActivity implements OnClickListener,TaskListenerWithState,IXListViewListener,OnItemClickListener
{
	/**微墙详情类的key*/
	private XListView listview;
	public LinearLayout llTransArea;
	public ImageView ivAuthorImg;
	public TextView tvAuthorName;
	public TextView tvTime;
	public TextView tvOldAuthorName;
	public TextView tvComments;
	public TextView tvContent;
	public GridView gridView,oldGridview;
	public TextView tvZanCount;
	public TextView tvReplyCount;
	public TextView tvTranspontCount;
	public TextView tvShareCount;
	
	private CommentAdapter weiQiangCommentAdapter;
	private List<CommentBean> weiqiangComments = new ArrayList<CommentBean>();
	private String mRefreshDetailWeiqiangTime;
	
	private int comment_start = 0;
	private String mContent;
	private WeiqiangActionType type;
	private TitleBar titleBar;
	private String weiqiangId;
	
	private WeiQiangBean weiqiangBean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_weiqiang_detail);
		weiqiangBean = (WeiQiangBean) getIntent().getSerializableExtra(WeiQiangBean.KEY_WEIQIANG_BEAN);
		weiqiangId = weiqiangBean.weiboid;
		titleBar = new TitleBar(mActivity);
		titleBar.initTitleBar("微墙详情");
		findView();
		setData();
		WeiqiangRequest.getInstance().requestWeiqiangContent(this, this, weiqiangBean.weiboid, String.valueOf(comment_start), String.valueOf(comment_start+PAGE_SIZE_ADD));
	}
	

	/**
	 * 控件初始化方法
	 */
	private void findView()
	{
		listview = (XListView) findViewById(R.id.weiqiang_detail_xlv);
		listview.setPullLoadEnable(true);
		listview.setPullRefreshEnable(false);
		listview.setXListViewListener(this);
		listview.setOnItemClickListener(this);
	   
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
		
		LinearLayout ll_action = (LinearLayout) head_weiqiang_detail.findViewById(R.id.ll_action);
		ll_action.setVisibility(View.GONE);
		
		
		tvZanCount = (TextView) findViewById(R.id.tv_zan_count);
		tvReplyCount = (TextView) findViewById(R.id.tv_comment_count);
		tvTranspontCount = (TextView) findViewById(R.id.tv_transpont_count);
		tvShareCount = (TextView) findViewById(R.id.tv_share_count);
		
		ivAuthorImg.setOnClickListener(this);
		tvAuthorName.setOnClickListener(this);
		tvOldAuthorName.setOnClickListener(this);
		tvZanCount.setOnClickListener(this);
		tvReplyCount.setOnClickListener(this);
		tvTranspontCount.setOnClickListener(this);
		tvShareCount.setOnClickListener(this);
		
		listview.addHeaderView(head_weiqiang_detail);
		weiQiangCommentAdapter = new CommentAdapter(weiqiangComments,this);
		listview.setAdapter(weiQiangCommentAdapter);
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		mRefreshDetailWeiqiangTime = pUitl.getWeiqiangDetailRefreshTime();
		listview.setRefreshTime(mRefreshDetailWeiqiangTime);
		
	}
	
	@Override
	public void onPause() {
		super.onPause();
		pUitl.setWeiqiangDetailRefreshTime(mRefreshDetailWeiqiangTime);
	}
	
	private void setData()
	{
		ImageLoader mImageLoader;
		DisplayImageOptions options;
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.icon_default)
		.showImageForEmptyUri(R.drawable.icon_default)
		.cacheInMemory()
		.cacheOnDisc()
		.build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(mActivity));
		
        mImageLoader.displayImage(weiqiangBean.photo,ivAuthorImg, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete() {
				Animation anim = AnimationUtils.loadAnimation(mActivity, R.anim.fade_in);
				ivAuthorImg.setAnimation(anim);
				anim.start();
			}
		});
		
		tvTime.setText(ToolUtils.getFormatDate(weiqiangBean.date_time));
		
		tvAuthorName.setText(weiqiangBean.name);
		
		tvTime.setText(ToolUtils.getFormatDate(weiqiangBean.date_time));
    	
		if(!TextUtils.isEmpty(weiqiangBean.fromname)){	//如果转发人存在
			llTransArea.setVisibility(View.VISIBLE);
			tvOldAuthorName.setText(weiqiangBean.fromname);
			tvContent.setText(weiqiangBean.content);
			tvComments.setText(weiqiangBean.Comments);
			showImgs(weiqiangBean.imgs,oldGridview);
			
		}else{
			llTransArea.setVisibility(View.GONE);
			tvComments.setText(weiqiangBean.content);
			showImgs(weiqiangBean.imgs,gridView);
		}
		tvZanCount.setText(weiqiangBean.like_count);
    	
		tvReplyCount.setText(weiqiangBean.comment_count);
    	
		tvTranspontCount.setText(weiqiangBean.forward_count);
    	
		tvShareCount.setText(weiqiangBean.share_count);
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
    	 Intent intent = null;
        switch (v.getId())
        {
            // 赞
            case R.id.tv_zan_count:
            	weiqiangBean.like_count = String.valueOf(Integer.valueOf(weiqiangBean.like_count)+1);
            	tvZanCount.setText(weiqiangBean.like_count);
                zanWeiqiang();
                break;
            // 评论
            case R.id.tv_comment_count:
            	type = WeiqiangActionType.RESPONSE;
                createSendDialog("");
                break;
            // 转发
            case R.id.tv_transpont_count:
            	type = WeiqiangActionType.TRANSPOND;
            	createSendDialog("");
                break;
            // 分享
            case R.id.tv_share_count:
                break;
            case R.id.iv_img:
    		case R.id.tv_nick:
                intent = new Intent(mActivity, NameCardActivity.class);
                intent.putExtra(UserInfoBean.KEY_USER_ID, String.valueOf(weiqiangBean.userid));
                startActivity(intent);
    			break;
    		case R.id.tv_old_user:
    			intent = new Intent(mActivity, NameCardActivity.class);
    			intent.putExtra(UserInfoBean.KEY_USER_ID, String.valueOf(weiqiangBean.fromuserid));
    			startActivity(intent);
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
       WeiqiangRequest.getInstance().requestWeiqiangZan(this, mActivity, weiqiangId);
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
				case 860002:
					CommentResponse response = JSON.parseObject(strJson, CommentResponse.class);
					setCommentData(response.comments);
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

		this.weiqiangComments.addAll(comments);
		weiQiangCommentAdapter.notifyDataSetChanged();
	}
	
	
	/**
	 * 
	 * @todo 时间选择器
	 * @author lzlong@zwmob.com
	 * @time 2014-3-26 下午2:09:30
	 */
	private void createSendDialog(String tag) {

		int screenWidth = (int) (mActivity.getWindowManager().getDefaultDisplay().getWidth()); // 屏幕宽
		int screenHeight = (int) (mActivity.getWindowManager().getDefaultDisplay().getHeight()*0.3); // 屏幕高

		final BGDialog dialog = new BGDialog(mActivity,R.style.dialog_no_thing,screenWidth, screenHeight);

		View vSend = inflater.inflate(R.layout.dialog_send, null);

		vSend.requestFocus();

		final EditText etcontent = (EditText) vSend.findViewById(R.id.et_content);
		etcontent.setText(tag);
		etcontent.setSelection(tag.length());
		vSend.findViewById(R.id.btn_send).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				String content = etcontent.getText().toString();
				if(TextUtils.isEmpty(content))
				{
					return;
				}else{
					etcontent.setText("");
					if(type == WeiqiangActionType.TRANSPOND){
						weiqiangBean.forward_count = String.valueOf(Integer.valueOf(weiqiangBean.forward_count)+1);
							tvTranspontCount.setText(weiqiangBean.forward_count);
							WeiqiangRequest.getInstance().requestWeiqiangTranspond(WeiqiangDetailActivity.this, mActivity, weiqiangId,mContent);
					}else{
						
						CommentBean wcb = new CommentBean();
						wcb.userid = String.valueOf(BGApp.mLoginBean.userid);
						wcb.name = BGApp.mUserBean.nickn;
						wcb.photo = BGApp.mUserBean.photo;
						wcb.content = content;
						wcb.commenttime = ToolUtils.getStandardTime();
						weiqiangComments.add(0, wcb);
						weiQiangCommentAdapter.notifyDataSetChanged();
						
						weiqiangBean.comment_count = String.valueOf(Integer.valueOf(weiqiangBean.comment_count)+1);
						tvReplyCount.setText(weiqiangBean.comment_count);
						WeiqiangRequest.getInstance().requestWeiqiangReply(WeiqiangDetailActivity.this, mActivity, weiqiangId,content);
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
		XuannengRequest.getInstance().requestJokeContent(this, this, weiqiangId, String.valueOf(comment_start), String.valueOf(comment_start+PAGE_SIZE_ADD));
	}


	@Override
	public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
		final CommentBean bean = (CommentBean) adapter.getAdapter().getItem(position-1);
		if(null != bean){
			createSendDialog("@"+bean.name+"  ");
		}
	}
	
}
