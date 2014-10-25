package com.bgood.xn.ui.weiqiang;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.bgood.xn.R;
import com.bgood.xn.adapter.WeiqiangAdapter;
import com.bgood.xn.bean.WeiQiangBean;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.WeiqiangRequest;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.widget.TitleBar;

/**
 * 
 * @todo:我的微墙
 * @date:2014-10-24 下午3:50:55
 * @author:hg_liuzl@163.com
 */
public class WeiqiangOfMeActivity extends BaseActivity implements OnItemClickListener,TaskListenerWithState,OnClickListener
{
	/**我自己的微墙**/
	public static final int WEI_QIANG_ME = 0;
	private XListView m_weiqiang_person_listview;
	private List<WeiQiangBean> m_list = new ArrayList<WeiQiangBean>();
	private WeiqiangAdapter adapter;
	private int start_size = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_weiqiang_person);
		initTitle();
		initViews();
		WeiqiangRequest.getInstance().requestWeiqiangList(this, mActivity, String.valueOf(WEI_QIANG_ME), String.valueOf(start_size), String.valueOf(start_size+PAGE_SIZE_ADD));
	}
	
	private void initTitle() {
		(new TitleBar(mActivity)).initTitleBar("我的微墙");
	}
	
	private void initViews()
	{
		m_weiqiang_person_listview = (XListView) findViewById(R.id.weiqiang_person_listview);
		adapter = new WeiqiangAdapter(m_list,mActivity);
		m_weiqiang_person_listview.setAdapter(adapter);
		m_weiqiang_person_listview.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View v, int location, long arg3)
	{
//		final WeiQiangBean weiqiang = (WeiQiangBean) adapter.getAdapter().getItem(location);
//		Intent intent = new Intent(mActivity, WeiqiangDetailActivity.class);
//		intent.putExtra(WeiqiangDetailActivity.BEAN_WEIQIANG_KEY,weiqiang);
//		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			JSONObject body = bNetWork.getBody();
			String strJson = bNetWork.getStrJson();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
			}
			}
	}
}
