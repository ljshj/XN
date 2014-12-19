package com.bgood.xn.ui.message.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.bgood.xn.R;
import com.bgood.xn.adapter.GroupAdapter;
import com.bgood.xn.bean.GroupBean;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.IMRequest;
import com.bgood.xn.ui.base.BaseShowDataFragment;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.view.xlistview.XListView.IXListViewListener;

/**
 * @todo:固定群
 * @date:2014-12-12 上午10:01:05
 * @author:hg_liuzl@163.com
 */
public class GroupFragment extends BaseShowDataFragment implements IXListViewListener,TaskListenerWithState {
	
	private EditText etContent;
	private ImageButton ibClear;
	private XListView m_groupXLv;
	private ArrayList<GroupBean> m_groupList = new ArrayList<GroupBean>();
	private String mKeyWord = "";
	private GroupAdapter groupAdapter;
	private View layout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		layout =  inflater.inflate(R.layout.fragment_group_list, container, false);
		initView();
		doRequest();
		return layout;
	}

	private void initView() {
		etContent = (EditText) layout.findViewById(R.id.query);
		etContent.setHint("搜索群");
		etContent.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionID, KeyEvent arg2) {
				if(actionID == EditorInfo.IME_ACTION_SEARCH){
					doSearch();
				}
				return false;
			}
		});
		ibClear = (ImageButton) layout.findViewById(R.id.search_clear);
		ibClear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				etContent.setText("");
				mKeyWord = "";
			}
		});
		
		m_groupXLv = (XListView) layout.findViewById(R.id.common_xlv);
		m_groupXLv.setPullLoadEnable(false);
		m_groupXLv.setPullRefreshEnable(true);
		m_groupXLv.setXListViewListener(this);
		groupAdapter = new GroupAdapter(m_groupList, getActivity());
		m_groupXLv.setAdapter(groupAdapter);
	}
	
	private void doSearch() {
		mKeyWord = etContent.getText().toString().trim();
		if(TextUtils.isEmpty(mKeyWord)){
			BToast.show(mActivity, "请输入关键字");
			return;
		}else{
			doRequest();
		}
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		if (!hidden) {
			doRequest();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!hidden) {
			doRequest();
		}
	}
	
	
	
	private void doRequest(){
		IMRequest.getInstance().requestContactsList(this, mActivity);
	}

	@Override
	public void onRefresh() {
		doRequest();
	}

	@Override
	public void onLoadMore() {
	}

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			String strJson = bNetWork.getStrJson();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				}
			}
		}
}
