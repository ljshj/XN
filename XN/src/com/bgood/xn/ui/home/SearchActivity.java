package com.bgood.xn.ui.home;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.bgood.xn.R;
import com.bgood.xn.adapter.HistoryAdapter;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.view.BToast;

/***
 * 
 * @todo:搜索界面
 * @date:2014-11-21 上午11:41:59
 * @author:hg_liuzl@163.com
 */
public class SearchActivity extends BaseActivity implements OnClickListener, OnItemClickListener,OnEditorActionListener
{
	private EditText m_contentTv = null; // 搜索内容
	private ListView m_listLv = null; // 搜索列表
	private View footView_history = null;	//底部
	private int search_type = 0;
	private HistoryAdapter adapter;
	private List<String> history = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_search);
		search_type = getIntent().getIntExtra(HomeActivity.ACTION_TYPE, 0);
		getHistory();
		findView();
	}

	/**
	 * 控件初始化方法
	 */
	private void findView()
	{
		m_contentTv = (EditText) findViewById(R.id.search_tv_content);
		m_contentTv.setOnEditorActionListener(this);
		findViewById(R.id.btn_cancel).setOnClickListener(this);
		m_listLv = (ListView) findViewById(R.id.search_lv_list);
		
		if (history.size()>0)
		{
			footView_history = inflater.inflate(R.layout.footview_clear_history, m_listLv, false);
			m_listLv.addFooterView(footView_history);
			m_listLv.setOnItemClickListener(this);
			findViewById(R.id.footview_ll_clear_history).setOnClickListener(this);
			adapter = new HistoryAdapter(history,this);
			m_listLv.setAdapter(adapter);
		
		}
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_cancel:
				finish();
				break;
			case R.id.footview_ll_clear_history:
				clearHistory();
				break;
		}
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		doSearch(parent.getAdapter().getItem(position).toString());
	}
	
	private void doSearch(String searchText) {
		Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
		intent.putExtra(HomeActivity.ACTION_TYPE, search_type);
		intent.putExtra("msg",searchText);
		startActivity(intent);
	}
	
	
	/**
	 * 清除历史记录
	 */
	private void clearHistory()
	{
		pUitl.clearSearchHistory(search_type);
		history = null;
		adapter.clear();
		m_listLv.removeFooterView(footView_history);
		adapter.notifyDataSetChanged();
	}

	/**获取历史记录**/
	private void getHistory()
	{
		String longhistory = pUitl.getSearchHistory(search_type);
		if (TextUtils.isEmpty(longhistory)){
			return;
		}
		
		String[] newArrays = longhistory.split(",");
		List<String> localHiStory = Arrays.asList(newArrays);
		history.addAll(localHiStory);
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		
		switch (actionId) {
		case EditorInfo.IME_ACTION_SEARCH:
			String searchText = m_contentTv.getText().toString().trim();
			if (TextUtils.isEmpty(searchText)) {
				BToast.show(mActivity, "请输入您的搜索内容");
			} else {
				pUitl.saveSearchHistory(search_type, searchText);
				doSearch(searchText);
			}
			break;
		default:
			break;
		}
		
		return false;
	}
}
