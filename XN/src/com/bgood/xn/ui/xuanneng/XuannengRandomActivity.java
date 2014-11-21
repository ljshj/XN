package com.bgood.xn.ui.xuanneng;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.bgood.xn.R;
import com.bgood.xn.adapter.JokeAdapter;
import com.bgood.xn.bean.JokeBean;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.view.xlistview.XListView.IXListViewListener;

/**
 * @todo:炫能随机页
 * @date:2014-11-21 下午5:50:08
 * @author:hg_liuzl@163.com
 */
public class XuannengRandomActivity extends BaseActivity implements OnItemClickListener, IXListViewListener,TaskListenerWithState,OnClickListener
{
	private XListView m_listXlv = null;
	private List<JokeBean> mJokeList = new ArrayList<JokeBean>();
	private JokeAdapter jokeAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_weiqiang_xlistview);
		m_listXlv = (XListView) findViewById(R.id.common_xlv);
		jokeAdapter = new JokeAdapter(mJokeList, mActivity, this);
	}
	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		
	}
	@Override
	public void onRefresh() {
		
	}
	@Override
	public void onLoadMore() {
		
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		/**
		 * @todo:TODO
		 * @date:2014-11-21 下午6:45:57
		 * @author:hg_liuzl@163.com
		 */
		
	}


}
