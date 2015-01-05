package com.bgood.xn.ui.message.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.bgood.xn.view.LoadingProgress;
import com.bgood.xn.view.xlistview.XListView;
import com.easemob.chat.activity.ChatActivity;

/**
 * @todo:固定群
 * @date:2014-12-12 上午10:01:05
 * @author:hg_liuzl@163.com
 */
public class GroupFragment extends BaseShowDataFragment {
	
	private EditText etContent;
	private ImageButton ibClear;
	private XListView m_groupXLv;
	private List<GroupBean> m_groupList = new ArrayList<GroupBean>();
	private Map<String,GroupBean> m_groupMap = new HashMap<String,GroupBean>();
	private String mKeyWord = "";
	private GroupAdapter groupAdapter;
	private View layout;
	public static GroupFragment instance = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		instance = this;
		layout =  inflater.inflate(R.layout.fragment_group_list, container, false);
		initView();
		doGetGroupData();
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
		m_groupXLv.setPullRefreshEnable(false);
		groupAdapter = new GroupAdapter(m_groupList, getActivity());
		m_groupXLv.setAdapter(groupAdapter);
		
		
		m_groupXLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
				
				final GroupBean groupBean = (GroupBean) adapter.getAdapter().getItem(position);
				//进入群聊
				Intent intent = new Intent(mActivity, ChatActivity.class);
				// it is group chat
				intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
				intent.putExtra("groupId", groupBean.hxgroupid);
				startActivityForResult(intent, 0);
			}

		});
		m_groupXLv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (mActivity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (mActivity.getCurrentFocus() != null){
						inputMethodManager.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
					}
				}
				return false;
			}
		});
		
	}
	
	private void doSearch() {
		mKeyWord = etContent.getText().toString().trim();
		if(TextUtils.isEmpty(mKeyWord)){
			BToast.show(mActivity, "请输入关键字");
			return;
		}else{
			searchGroupLocal(mKeyWord);
		}
	}
	
	/**获取群数据，如固定群，临时群，群成员列表*/
	private void searchGroupLocal(final String keyWord){
		new AsyncTask<Void, Void, List<GroupBean>>(){
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				LoadingProgress.getInstance().show(mActivity);
			}
			@Override
			protected List<GroupBean> doInBackground(Void... arg0) {
				return GroupBean.queryGroupBeanByGroupName(dbHelper, 0, keyWord);
			}
			@Override
			protected void onPostExecute(List<GroupBean> groups) {
				super.onPostExecute(groups);
				LoadingProgress.getInstance().dismiss();
				m_groupList.clear();
				m_groupList.addAll(groups);
				groupAdapter.notifyDataSetChanged();
			}
		}.execute();
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

		m_groupMap.putAll(BGApp.getInstance().getGroupMap());
		
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
