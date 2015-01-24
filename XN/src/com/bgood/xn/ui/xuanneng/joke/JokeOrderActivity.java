package com.bgood.xn.ui.xuanneng.joke;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.adapter.JokeAdapter;
import com.bgood.xn.bean.JokeBean;
import com.bgood.xn.bean.JokeBean.JokeActionType;
import com.bgood.xn.bean.response.JokeResponse;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.XuannengRequest;
import com.bgood.xn.ui.base.BaseShowDataActivity;
import com.bgood.xn.ui.xuanneng.XuannengActivity;
import com.bgood.xn.utils.ShareUtils;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.dialog.BGDialog;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.view.xlistview.XListView.IXListViewListener;

/**
 * 
 * @todo:幽默秀排序
 * @date:2014-12-25 上午11:21:29
 * @author:hg_liuzl@163.com
 */
public class JokeOrderActivity extends BaseShowDataActivity implements OnItemClickListener,TaskListenerWithState,OnClickListener,IXListViewListener
{
	private XListView m_listXlv = null;
	private JokeAdapter adapter;
	private ArrayList<JokeBean> listJoke = new ArrayList<JokeBean>();
    private JokeBean mActionJoke = null;
    
    private JokeActionType type;
    /**刷新时间*/
    private String mRefreshTime = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		share = new ShareUtils(mActivity);
		setContentView(R.layout.home_layout_result_listview);
		m_listXlv = (XListView) findViewById(R.id.xListView);
		m_listXlv.setPullLoadEnable(true);
		m_listXlv.setPullRefreshEnable(true);
		m_listXlv.setXListViewListener(this);
		m_listXlv.setOnItemClickListener(this);
		adapter = new JokeAdapter(listJoke, mActivity,this);
		m_listXlv.setAdapter(adapter);
		XuannengRequest.getInstance().requestJokeList(this, this, XuannengActivity.XUANNENG_JOKE, JokeBean.JOKE_ORDER, m_start_page, m_start_page+PAGE_SIZE_ADD);
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		
		mRefreshTime = pUitl.getJokeOrderRefreshTime();
		m_listXlv.setRefreshTime(mRefreshTime);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		pUitl.setJokeOrderRefreshTime(mRefreshTime);
	}

	@Override
	public void onClick(View v)
	{
		
		judgeLogin();
		JokeBean jBean = null;
		switch (v.getId())
		{
		case R.id.av_zan:	//赞
			jBean = (JokeBean) v.getTag();
			mActionJoke = jBean;
			XuannengRequest.getInstance().requestXuanZan(this, mActivity, jBean.jokeid);
			break;
		case R.id.av_reply:	//评论
			jBean = (JokeBean) v.getTag();
			mActionJoke = jBean;
			type = JokeActionType.RESPONSE;
			createSendDialog();
			break;
		case R.id.av_transpont:	//转发
			jBean = (JokeBean) v.getTag();
			mActionJoke = jBean;
			type = JokeActionType.TRANSPOND;
			createSendDialog();
			break;
		case R.id.av_share:	//分享
			jBean = (JokeBean) v.getTag();
			mActionJoke = jBean;
			share.setShareContent(jBean.content, jBean.imgs.size() > 0 ? jBean.imgs.get(0).img:null);
			XuannengRequest.getInstance().requestXuanShare(this, mActivity, jBean.jokeid);
			break;
		}
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
					etcontent.setText("");
					if(type == JokeActionType.TRANSPOND){
						mActionJoke.forward_count = String.valueOf(Integer.valueOf(mActionJoke.forward_count)+1);
						adapter.notifyDataSetChanged();
						XuannengRequest.getInstance().requestXuanTransport(JokeOrderActivity.this, mActivity, mActionJoke.jokeid,content);
					}else{
						mActionJoke.comment_count = String.valueOf(Integer.valueOf(mActionJoke.comment_count)+1);
						adapter.notifyDataSetChanged();
						XuannengRequest.getInstance().requestXuanComment(JokeOrderActivity.this, mActivity, mActionJoke.jokeid,content);
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
	
    
    /**
     * 加载完成之后进行时间保存等方法
     */
    @SuppressLint("SimpleDateFormat")
	private void stopLoad(XListView xListView) {
        xListView.stopRefresh();
        xListView.stopLoadMore();
    }
	
	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			String strJson = bNetWork.getStrJson();
				switch (bNetWork.getMessageType()) {
				case 870001:
						if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
							JokeResponse response = JSON.parseObject(strJson, JokeResponse.class);
							setDataAdapter(m_listXlv, adapter, listJoke, response.jokes, isRefreshAction);
						}
					break;
				case 870005://分享
					if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
						mActionJoke.share_count = String.valueOf(Integer.valueOf(mActionJoke.share_count)+1);
						adapter.notifyDataSetChanged();
					}
					break;
				case 870007:	//点赞
					if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
						mActionJoke.like_count = String.valueOf(Integer.valueOf(mActionJoke.like_count)+1);
						adapter.notifyDataSetChanged();
					}else if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
						BToast.show(mActivity, "您已经点过赞！");
					}
					break;
				default:
					if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
						BToast.show(mActivity, "操作成功");
					}else{
						BToast.show(mActivity, "操作失败");
					}
					break;
				}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
		final JokeBean joke = (JokeBean) adapter.getAdapter().getItem(position);
        Intent intent = new Intent(mActivity, JokeDetailActivity.class);
        intent.putExtra(JokeDetailActivity.BEAN_JOKE_KEY, joke);
        startActivity(intent);
	}

	@Override
	public void onRefresh() {
		isRefreshAction = true;
		m_start_page = 0;
		XuannengRequest.getInstance().requestJokeList(this, this, XuannengActivity.XUANNENG_JOKE, JokeBean.JOKE_ORDER, m_start_page, m_start_page + PAGE_SIZE_ADD);
	}
	@Override
	public void onLoadMore() {
		isRefreshAction = false;
		XuannengRequest.getInstance().requestJokeList(this, this, XuannengActivity.XUANNENG_JOKE, JokeBean.JOKE_ORDER, m_start_page, m_start_page + PAGE_SIZE_ADD);
	}
}
