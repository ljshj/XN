package com.bgood.xn.ui.message.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.base.BaseShowDataFragment;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.xlistview.XListView;
/**
 * @todo:交流厅
 * @date:2014-12-12 上午10:01:05
 * @author:hg_liuzl@163.com
 */
public class CommunicateFragment extends BaseShowDataFragment {
	private EditText etContent;
	private ImageButton ibClear;
	private XListView m_groupXLv;
	private List<GroupBean> m_groupList = new ArrayList<GroupBean>();
	private Map<String,GroupBean> m_groupMap = new HashMap<String,GroupBean>();
	private String mKeyWord = "";
	private GroupAdapter groupAdapter;
	private View layout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		layout =  inflater.inflate(R.layout.fragment_group_list, container, false);
		initView();
		doGetGroupData();
		return layout;
	}

	private void initView() {
		etContent = (EditText) layout.findViewById(R.id.query);
		etContent.setHint("搜索交流厅");
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
		m_groupXLv.setPullRefreshEnable(false);
		groupAdapter = new GroupAdapter(m_groupList, getActivity());
		m_groupXLv.setAdapter(groupAdapter);
	}
	
	private void doSearch() {
		mKeyWord = etContent.getText().toString().trim();
		if(TextUtils.isEmpty(mKeyWord)){
			BToast.show(mActivity, "请输入关键字");
			return;
		}else{
			searchGroup(mKeyWord);
		}
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		if (!hidden) {
			doGetGroupData();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!hidden) {
			doGetGroupData();
		}
	}
	
	public void refresh() {
		try {
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					doGetGroupData();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void doGetGroupData(){
		m_groupMap.clear();
		m_groupList.clear();

		m_groupMap = BGApp.getInstance().getTempMap();
		
		Iterator<Entry<String, GroupBean>> iter = m_groupMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, GroupBean> entry = (Entry<String, GroupBean>) iter.next();
			m_groupList.add(entry.getValue());
		}
		groupAdapter.notifyDataSetChanged();		
	}
	
	private void searchGroup(String keyWord) {
		m_groupList.clear();
		Iterator<Entry<String, GroupBean>> iter = m_groupMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, GroupBean> entry = (Entry<String, GroupBean>) iter.next();
			if(entry.getKey().contains(keyWord)){
			m_groupList.add(entry.getValue());
			}
		}
		groupAdapter.notifyDataSetChanged();
	}
}
