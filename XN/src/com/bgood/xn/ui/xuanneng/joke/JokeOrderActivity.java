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
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.adapter.JokeAdapter;
import com.bgood.xn.bean.JokeBean;
import com.bgood.xn.bean.WeiQiangBean;
import com.bgood.xn.bean.JokeBean.JokeActionType;
import com.bgood.xn.bean.response.JokeResponse;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.XuannengRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.base.BaseShowDataActivity;
import com.bgood.xn.ui.user.account.LoginActivity;
import com.bgood.xn.ui.xuanneng.XuanNengMainActivity;
import com.bgood.xn.utils.ConfigUtil;
import com.bgood.xn.utils.ShareUtils;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.dialog.BGDialog;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.view.xlistview.XListView.IXListViewListener;
import com.umeng.analytics.MobclickAgent;

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
		setContentView(R.layout.layout_joke);
		findViewById(R.id.iv_joke_verify).setOnClickListener(this);
		findViewById(R.id.iv_joke_publish).setOnClickListener(this);
		m_listXlv = (XListView) findViewById(R.id.xlv_sapce);
		m_listXlv.setPullLoadEnable(true);
		m_listXlv.setPullRefreshEnable(true);
		m_listXlv.setXListViewListener(this);
		m_listXlv.setOnItemClickListener(this);
		adapter = new JokeAdapter(listJoke, mActivity,this);
		m_listXlv.setAdapter(adapter);
		showData();
	}
	
	private void showData() {
		if (!ConfigUtil.isConnect(mActivity)) {
			String strJson = pUitl.getStoreJokeOrder();
			JokeResponse response = JSON.parseObject(strJson, JokeResponse.class);
			setDataAdapter(m_listXlv, adapter, listJoke, response.jokes, isRefreshAction);
		}else{
			requestData();
		}
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		
		mRefreshTime = pUitl.getJokeOrderRefreshTime();
		m_listXlv.setRefreshTime(mRefreshTime);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		pUitl.setJokeOrderRefreshTime(mRefreshTime);
	}

	@Override
	public void onClick(View v)
	{
		JokeBean jBean = null;
		if (v.getId() == R.id.iv_joke_verify) { // 审核不需要登录
			MobclickAgent.onEvent(this, "xuanneng_joke_verify_click");
			JokeVerifyActivity.doVerifyJoke(mActivity);
		} else if (v.getId() == R.id.ll_share) {
			MobclickAgent.onEvent(this, "xuanneng_joke_share_click");
			jBean = (JokeBean) v.getTag();
			mActionJoke = jBean;
			jBean.doShare(share);
		} else {
			if (!BGApp.isUserLogin) {
				LoginActivity.doLoginAction(this);
			} else {
				switch (v.getId()) {
				case R.id.iv_joke_publish:	//发布
					JokePublishActivity.doPublishJoke(mActivity);
					break;
				case R.id.av_zan: // 赞
					MobclickAgent.onEvent(this, "xuanneng_joke_zan_click");
					jBean = (JokeBean) v.getTag();
					mActionJoke = jBean;
					XuannengRequest.getInstance().requestXuanZan(this,mActivity, jBean.jokeid);
					break;
				case R.id.av_reply: // 评论
					MobclickAgent.onEvent(this, "xuanneng_joke_reply_click");
					jBean = (JokeBean) v.getTag();
					mActionJoke = jBean;
					type = JokeActionType.RESPONSE;
					createSendDialog();
					break;
				case R.id.av_transpont: // 转发
					MobclickAgent
							.onEvent(this, "xuanneng_joke_transpond_click");
					jBean = (JokeBean) v.getTag();
					mActionJoke = jBean;
					type = JokeActionType.TRANSPOND;
					createSendDialog();
					break;
				/*
				 * case R.id.av_share: //分享
				 * MobclickAgent.onEvent(this,"xuanneng_joke_share_click");
				 * jBean = (JokeBean) v.getTag(); mActionJoke = jBean;
				 * jBean.doShare(share);
				 * XuannengRequest.getInstance().requestXuanShare(this,
				 * mActivity, jBean.jokeid); break; case R.id.ll_share: //分享
				 * MobclickAgent.onEvent(this,"xuanneng_joke_share_click");
				 * jBean = (JokeBean) v.getTag(); mActionJoke = jBean;
				 * jBean.doShare(share); break;
				 */
				}
			}
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
		Button btnSend = (Button) vSend.findViewById(R.id.btn_send);
		btnSend.setText(type == JokeActionType.TRANSPOND?"转发":"评论");
		btnSend.setOnClickListener(new OnClickListener() {
			
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
	
	
	/**
	 * 
	 * @todo:保存数据到本地
	 * @date:2015-3-12 下午7:42:42
	 * @author:hg_liuzl@163.com
	 * @params:@param strJson
	 */
	private void saveDataToLocal(String strJson) {
		pUitl.storeJokeOrder(strJson);
	}
    
	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			String strJson = bNetWork.getStrJson();
				switch (bNetWork.getMessageType()) {
				case 870001:
					if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
						saveDataToLocal(strJson);
						m_start_page += 10;
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
					}else if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_HAS_ZAN){
						BToast.show(mActivity, "您已经点赞了！");
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
		if(null!=joke){
			MobclickAgent.onEvent(this,"xuanneng_joke_detail_click");
	        Intent intent = new Intent(mActivity, JokeDetailActivity.class);
	        intent.putExtra(JokeBean.JOKE_BEAN, joke);
	        startActivity(intent);
		}
	}

	@Override
	public void onRefresh() {
		isRefreshAction = true;
		m_start_page = 0;
		requestData();
	}
	@Override
	public void onLoadMore() {
		isRefreshAction = false;
		requestData();
	}
	
	/**
	 * 
	 * @todo:请求网络数据
	 * @date:2015-3-17 下午3:22:32
	 * @author:hg_liuzl@163.com
	 * @params:
	 */
	private void requestData() {
		XuannengRequest.getInstance().requestJokeList(this, this, XuanNengMainActivity.XUANNENG_JOKE, JokeBean.JOKE_ORDER, m_start_page, m_start_page+PAGE_SIZE_ADD,BGApp.location.longitude, BGApp.location.latitude);
	}
}
