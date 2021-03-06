package com.bgood.xn.ui.message;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.adapter.GroupAdapter;
import com.bgood.xn.bean.GroupBean;
import com.bgood.xn.bean.response.GroupBeanResponse;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.IMRequest;
import com.bgood.xn.ui.base.BaseShowDataActivity;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.view.xlistview.XListView.IXListViewListener;
import com.bgood.xn.widget.TitleBar;

/**
 * @todo:搜索并添加群
 * @date:2014-12-12 下午2:58:20
 * @author:hg_liuzl@163.com
 */
public class AddGroupActivity extends BaseShowDataActivity implements IXListViewListener,TaskListenerWithState{

	private EditText etContent;
	private ImageButton ibClear;
	private XListView m_memberXLv;
	
	private GroupAdapter mGroupAdapter = null;
	private ArrayList<GroupBean> mGroupList = new ArrayList<GroupBean>();
	
	private int m_start = 0;
	private String mKeyWord = "";
	private TitleBar mTitleBar = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		initView();
		doRequest();
	}
	
	private void initView() {
		mTitleBar = new TitleBar(mActivity);
		mTitleBar.initAllBar("添加群", "创建群");
		mTitleBar.rightBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BToast.show(mActivity, "创建群");
				Intent intent = new Intent(AddGroupActivity.this,CreateGroupActivity.class);
				AddGroupActivity.this.startActivity(intent);
			}
		});
		etContent = (EditText) findViewById(R.id.query);
		etContent.setHint("请输入群名称");
		etContent.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionID, KeyEvent arg2) {
				if(actionID == EditorInfo.IME_ACTION_SEARCH){
					doSearch();
				}
				return false;
			}
		});
		ibClear = (ImageButton) findViewById(R.id.search_clear);
		ibClear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				etContent.setText("");
				mKeyWord = "";
			}
		});
		
		m_memberXLv = (XListView) findViewById(R.id.xlv_spaceless);
		m_memberXLv.setPullLoadEnable(true);
		m_memberXLv.setPullRefreshEnable(false);
		m_memberXLv.setFooterDividersEnabled(false);
		m_memberXLv.setXListViewListener(this);
		mGroupAdapter = new GroupAdapter(mGroupList, mActivity);
		m_memberXLv.setAdapter(mGroupAdapter);
		m_memberXLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position,long arg3) {
				
				GroupBean bean = (GroupBean) adapter.getAdapter().getItem(position);
				Intent intent = new Intent(mActivity,GroupCardActivity.class);
				intent.putExtra(GroupBean.BEAN_GROUP, bean);
				startActivity(intent);
			}
		});
	}
	
	private void doSearch() {
		mKeyWord = etContent.getText().toString().trim();
		isRefreshAction = true;
		m_start = 0;
		doRequest();
	}
	
	private void doRequest(){
		IMRequest.getInstance().requestGroupSearch(this, mActivity, mKeyWord, m_start, m_start+PAGE_SIZE_ADD);
	}

	@Override
	public void onRefresh() {
		isRefreshAction = true;
		doRequest();
	}

	@Override
	public void onLoadMore() {
		isRefreshAction = false;
		doRequest();
	}

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			String strJson = bNetWork.getStrJson();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				GroupBeanResponse response = JSON.parseObject(strJson, GroupBeanResponse.class);
				m_start += PAGE_SIZE_ADD;
				setDataAdapter(m_memberXLv, mGroupAdapter, mGroupList, response.items,isRefreshAction);
				}
			}
	}
}
