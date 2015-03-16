package com.bgood.xn.ui.xuanneng.joke;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.adapter.JokeAdapter;
import com.bgood.xn.bean.JokeBean;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.bean.response.JokeResponse;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.XuannengRequest;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.utils.ShareUtils;
import com.bgood.xn.utils.ToolUtils;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.view.xlistview.XListView.IXListViewListener;
import com.bgood.xn.widget.TitleBar;
import com.umeng.analytics.MobclickAgent;

/**
 * @todo:别人的幽默秀,基本复用微墙的样式
 * @date:2014-12-1 上午9:55:09
 * @author:hg_liuzl@163.com
 */
public class JokePersonActivity extends BaseActivity implements OnItemClickListener,TaskListenerWithState,OnClickListener,IXListViewListener
{
	/**我自己的微墙**/
	private XListView m_joke_listview;
	private List<JokeBean> mjokeList = new ArrayList<JokeBean>();
	private JokeAdapter jokeAdapter;
	private int start_size = 0;
	private boolean isRefresh = true;
	private String mRefreshTime;
	private JokeBean mActionjoke = null;
	private String userid;
	private ShareUtils share = null;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		share = new ShareUtils(mActivity);
		userid = getIntent().getStringExtra(UserInfoBean.KEY_USER_ID);
		setContentView(R.layout.layout_weiqiang_person);
		(new TitleBar(mActivity)).initTitleBar("TA的幽默秀");
		initViews();
		doRequest();
	}
	
	private void initViews()
	{
		m_joke_listview = (XListView) findViewById(R.id.weiqiang_person_listview);
		jokeAdapter = new JokeAdapter(mjokeList,mActivity,this);
		m_joke_listview.setXListViewListener(this);
		m_joke_listview.setAdapter(jokeAdapter);
		m_joke_listview.setOnItemClickListener(this);
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View v, int location, long arg3)
	{
		final JokeBean joke = (JokeBean) adapter.getAdapter().getItem(location);
		Intent intent = new Intent(mActivity, JokeDetailActivity.class);
		intent.putExtra(JokeBean.JOKE_BEAN,joke);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		JokeBean joke = null;
		switch (v.getId()) {
		case R.id.av_zan:	//赞
			joke = (JokeBean) v.getTag();
			mActionjoke = joke;
			XuannengRequest.getInstance().requestXuanZan(this, mActivity, joke.jokeid);
			break;
		case R.id.av_reply:	//评论
			joke = (JokeBean) v.getTag();
			BToast.show(mActivity, "评论"+joke.jokeid);
			break;
		case R.id.av_transpont:	//转发
			joke = (JokeBean) v.getTag();
				mActionjoke.forward_count = String.valueOf(Integer.valueOf(mActionjoke.forward_count)+1);
				jokeAdapter.notifyDataSetChanged();
			
			XuannengRequest.getInstance().requestXuanComment(this, mActivity, joke.jokeid,"");;
			break;
		case R.id.av_share:	//分享
			joke = (JokeBean) v.getTag();
			share.setShareContent(joke.content, joke.imgs.size() > 0 ? joke.imgs.get(0).img:null);
			break;
		default:
			break;
		}
	}

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			String strJson = bNetWork.getStrJson();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				switch(bNetWork.getMessageType()){
				case 870002:	//获取微墙详细内容
					break;
				case 870003:
					break;
				case 870004:
					break;
				case 870005:	//转发微墙
					break;
				case 870006:
					break;
				case 870007:	//点赞
					if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
						mActionjoke.like_count = String.valueOf(Integer.valueOf(mActionjoke.like_count)+1);
						jokeAdapter.notifyDataSetChanged();
					}else if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
						BToast.show(mActivity, "您已经点过赞！");
					}
					break;
				case 870008:	//获取微墙列表
					JokeResponse response  = JSON.parseObject(strJson, JokeResponse.class);
					setJokeData(response.jokes);
					break;
				}
				
				}else{
					switch(bNetWork.getMessageType()){
					case 870001:
						m_joke_listview.stopRefresh();
						m_joke_listview.stopLoadMore();
						break;
					case 870002:
						break;
					case 870003:
						break;
					case 870004:
						break;
					case 870005:
						break;
					case 870006:
						break;
					case 870007:
						break;
					case 870008:
						break;
					}
				}
			}else{
				m_joke_listview.stopRefresh();
				m_joke_listview.stopLoadMore();
			}
		}
	
	
	private void setJokeData(List<JokeBean> jokes) {
		if(isRefresh){
			mRefreshTime = ToolUtils.getNowTime();
			m_joke_listview.setRefreshTime(mRefreshTime);
		}
		
		m_joke_listview.stopRefresh();
		m_joke_listview.stopLoadMore();
		if(null == jokes){
			return;
		}
		
		if(0 == jokes.size()){
			m_joke_listview.setPullLoadEnable(false);
            BToast.show(mActivity, "数据加载完毕");
		}
			
		if (isRefresh) {
			mjokeList.clear();
			start_size = 0;
		} 
			
		if (jokes.size() < PAGE_SIZE_ADD) {
			m_joke_listview.setPullLoadEnable(false);
			BToast.show(mActivity, "数据加载完毕");
			} else {
				start_size += PAGE_SIZE_ADD;
				m_joke_listview.setPullLoadEnable(true);
			}
		
		this.mjokeList.addAll(jokes);
		jokeAdapter.notifyDataSetChanged();
	}

	@Override
	public void onRefresh() {
		isRefresh = true;
		start_size = 0;
		doRequest();
	}
	@Override
	public void onLoadMore() {
		isRefresh = false;
		doRequest();
	}
	
	private void doRequest(){
		XuannengRequest.getInstance().requestXuanPublishList(this, mActivity, userid, JokeBean.JOKE_VERIFY, start_size, start_size+PAGE_SIZE_ADD);
	}
}
