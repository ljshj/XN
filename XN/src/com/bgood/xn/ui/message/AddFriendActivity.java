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
import com.bgood.xn.adapter.ResultMemberAdapter;
import com.bgood.xn.bean.MemberResultBean;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.bean.response.MemberResultResponse;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.IMRequest;
import com.bgood.xn.ui.base.BaseShowDataActivity;
import com.bgood.xn.ui.user.info.NameCardActivity;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.view.xlistview.XListView.IXListViewListener;
import com.bgood.xn.widget.TitleBar;

/**
 * @todo:搜索并添加好友
 * @date:2014-12-12 下午2:58:20
 * @author:hg_liuzl@163.com
 */
public class AddFriendActivity extends BaseShowDataActivity implements IXListViewListener,TaskListenerWithState{

	private EditText etContent;
	private ImageButton ibClear;
	private String mKeyWord = "";
	private XListView m_memberXLv;
	
	private ResultMemberAdapter m_memberAdapter = null;
	private ArrayList<MemberResultBean> m_memberList = new ArrayList<MemberResultBean>();
	
	private int m_start = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		initView();
		doRequest();
	}
	
	private void initView() {
		(new TitleBar(mActivity)).initTitleBar("添加好友");
		etContent = (EditText) findViewById(R.id.query);
		etContent.setHint("请输入好友的手机号");
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
		m_memberXLv.setXListViewListener(this);
		m_memberAdapter = new ResultMemberAdapter(m_memberList, mActivity);
		m_memberXLv.setAdapter(m_memberAdapter);
		m_memberXLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int location,long arg3) {
				final MemberResultBean mrb = (MemberResultBean) adapter.getAdapter().getItem(location);
				NameCardActivity.lookNameCard(mActivity, mrb.userid);
			}
		});
	}
	
	private void doSearch() {
		mKeyWord = etContent.getText().toString().trim();
		if(TextUtils.isEmpty(mKeyWord)){
			BToast.show(mActivity, "请输入关键字");
			return;
		}else{
			isRefreshAction = true;
			doRequest();
		}
	}
	
	private void doRequest(){
		IMRequest.getInstance().requestFriendSearch(this, mActivity, mKeyWord, m_start, m_start+PAGE_SIZE_ADD);
	}

	@Override
	public void onRefresh() {
		isRefreshAction = true;
		doRequest();
	}

	@Override
	public void onLoadMore() {
		doRequest();
	}

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			String strJson = bNetWork.getStrJson();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				MemberResultResponse response = JSON.parseObject(strJson, MemberResultResponse.class);
		    	 setDataAdapter(m_memberXLv, m_memberAdapter, m_memberList, response.items,isRefreshAction);
				}
			}
	}
}
