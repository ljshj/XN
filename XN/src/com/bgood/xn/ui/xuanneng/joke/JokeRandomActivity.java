package com.bgood.xn.ui.xuanneng.joke;

import java.util.ArrayList;
import java.util.List;
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
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.adapter.JokeAdapter;
import com.bgood.xn.adapter.KBaseAdapter;
import com.bgood.xn.bean.JokeBean;
import com.bgood.xn.bean.JokeBean.JokeActionType;
import com.bgood.xn.bean.response.JokeResponse;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.request.WeiqiangRequest;
import com.bgood.xn.network.request.XuannengRequest;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.ui.xuanneng.XuannengFragment;
import com.bgood.xn.utils.ShareUtils;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.dialog.BGDialog;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.view.xlistview.XListView.IXListViewListener;

/**
 * 
 * @todo:炫能随机展示页
 * @date:2014-11-21 下午5:50:53
 * @author:hg_liuzl@163.com
 */
public class JokeRandomActivity extends BaseActivity implements OnItemClickListener,TaskListenerWithState,OnClickListener,IXListViewListener
{
	private XListView m_listXlv = null;
	private JokeAdapter adapter;
	private ArrayList<JokeBean> listJoke = new ArrayList<JokeBean>();
	
	private int m_start_size = 0;
	
	private ShareUtils share = null;
	
    private JokeBean mActionJoke = null;
    
    private JokeActionType type;
    
    /**是否刷新*/
    private boolean isRefresh = true;
	
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
		XuannengRequest.getInstance().requestJokeList(this, this, XuannengFragment.XUANNENG_JOKE, JokeBean.JOKE_RADOM, m_start_size, m_start_size+PAGE_SIZE_ADD);
	}

	@Override
	public void onClick(View v)
	{
		
		judgeLogin();
		JokeBean jBean = null;
		switch (v.getId())
		{
		case R.id.tv_zan_count:	//赞
			jBean = (JokeBean) v.getTag();
			mActionJoke = jBean;
			WeiqiangRequest.getInstance().requestWeiqiangZan(this, mActivity, jBean.jokeid);
			break;
		case R.id.tv_comment_count:	//评论
			jBean = (JokeBean) v.getTag();
			mActionJoke = jBean;
			type = JokeActionType.RESPONSE;
			createSendDialog();
			break;
		case R.id.tv_transpont_count:	//转发
			jBean = (JokeBean) v.getTag();
			mActionJoke = jBean;
			type = JokeActionType.TRANSPOND;
			createSendDialog();
			break;
		case R.id.tv_share_count:	//分享
			jBean = (JokeBean) v.getTag();
			share.doShare();
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
//					if(type == JokeActionType.TRANSPOND){
//						if(m_type == WEIQIANG_ALL){
//							mActionWeiqiang.forward_count = String.valueOf(Integer.valueOf(mActionWeiqiang.forward_count)+1);
//							m_allFriendsAdapter.notifyDataSetChanged();
//						}else{
//							mActionWeiqiang.forward_count = String.valueOf(Integer.valueOf(mActionWeiqiang.forward_count)+1);
//							m_followFriendsAdapter.notifyDataSetChanged();
//						}
//						WeiqiangRequest.getInstance().requestWeiqiangTranspond(WeiqiangFragment.this, mActivity, mActionWeiqiang.weiboid,content);
//					}else{
//						if(m_type == WEIQIANG_ALL){
//							mActionWeiqiang.comment_count = String.valueOf(Integer.valueOf(mActionWeiqiang.comment_count)+1);
//							m_allFriendsAdapter.notifyDataSetChanged();
//						}else{
//							mActionWeiqiang.comment_count = String.valueOf(Integer.valueOf(mActionWeiqiang.comment_count)+1);
//							m_followFriendsAdapter.notifyDataSetChanged();
//						}
//						WeiqiangRequest.getInstance().requestWeiqiangReply(WeiqiangFragment.this, mActivity, mActionWeiqiang.weiboid,content);
//					}
					
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
     * 设置会员数据
     * @param list
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void setDataAdapter(XListView xListView,KBaseAdapter adapter,List<?> showList,List resultlist,int start_page)
    {
    	if(null == resultlist || resultlist.size() ==0)
    	{
    		xListView.setPullLoadEnable(false);
            BToast.show(mActivity, "数据加载完毕");
    		return;
    	}
    	
    	 if (resultlist.size() < PAGE_SIZE_ADD)
         {
    		 xListView.setPullLoadEnable(false);
             BToast.show(mActivity, "数据加载完毕");
         }else
         {
        	 xListView.setPullLoadEnable(true);
        	 start_page +=PAGE_SIZE_ADD;
         }
    	 if(isRefresh){
    		 showList.clear();
    	 }
    	 showList.addAll(resultlist);
         adapter.notifyDataSetChanged();
    }
	
	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			String strJson = bNetWork.getStrJson();
			stopLoad(m_listXlv);
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				JokeResponse response = JSON.parseObject(strJson, JokeResponse.class);
				setDataAdapter(m_listXlv, adapter, listJoke, response.jokes, m_start_size);
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
		isRefresh = true;
		m_start_size = 0;
		XuannengRequest.getInstance().requestJokeList(this, this, XuannengFragment.XUANNENG_JOKE, JokeBean.JOKE_RADOM, m_start_size, m_start_size+PAGE_SIZE_ADD);
	}
	@Override
	public void onLoadMore() {
		isRefresh = false;
		XuannengRequest.getInstance().requestJokeList(this, this, XuannengFragment.XUANNENG_JOKE, JokeBean.JOKE_RADOM, m_start_size, m_start_size+PAGE_SIZE_ADD);
	}
}
