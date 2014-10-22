package com.bgood.xn.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.adapter.HistoryAdapter;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.ui.home.SpeechFragment.onSpeechFragmentListener;

/**
 * 搜索页面
 */
public class SearchActivity extends BaseActivity implements OnClickListener, OnItemClickListener
{
	private TextView m_contentTv = null; // 搜索内容
	private Button m_searchBtn = null; // 搜索按钮
	private ListView m_listLv = null; // 搜索列表
	private PopupWindow popupWindowCheckSearchType;
	private ViewGroup ll_home_search_check_type;
	private int search_type = 0;
	private TextView home_tv_check_search_indecator;
	private HistoryAdapter adapter;
	private String[] history;
	private View footView_history;
	public static final int SEARCH_TYPE_TEXT = 0;
	public static final int SEARCH_TYPE_SPEECH = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_search);
		findView();
		ll_home_search_check_type.setOnClickListener(this);
		m_searchBtn.setOnClickListener(this);
	}

	/**
	 * 控件初始化方法
	 */
	private void findView()
	{
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
			search_type = bundle.getInt("searchType");

		ll_home_search_check_type = (ViewGroup) findViewById(R.id.ll_home_search_check_type);
		m_contentTv = (TextView) findViewById(R.id.search_tv_content);
		m_contentTv.requestFocus();
		m_searchBtn = (Button) findViewById(R.id.search_btn_search);
		m_listLv = (ListView) findViewById(R.id.search_lv_list);
		history = getHistory();
		if (history != null)
		{
			footView_history = inflater.inflate(R.layout.footview_clear_history, m_listLv, false);
			m_listLv.addFooterView(footView_history);
			m_listLv.setOnItemClickListener(this);
			findViewById(R.id.footview_ll_clear_history).setOnClickListener(this);
			adapter = new HistoryAdapter(this, history);
			m_listLv.setAdapter(adapter);
		}
		home_tv_check_search_indecator = (TextView) findViewById(R.id.home_tv_check_search_indecator);
		if (search_type == 0)
		{
			im.showSoftInputFromInputMethod(m_contentTv.getWindowToken(), 0);
		} else
		{
			m_listLv.setVisibility(View.GONE);
			SpeechFragment speechFragment = new SpeechFragment();
			speechFragment.setOnSpeechFragmentListener(new onSpeechFragmentListener()
			{

				@Override
				public void oResult(String msg)
				{
					m_contentTv.setText(msg);
				}
			});
		}
	}

	/**
	 * 搜索type改变
	 * 
	 * @param view
	 */
	private void checkSearchType(View view)
	{
		View home_iv_ican_indicator = view.findViewById(R.id.home_iv_ican_indicator);
		View home_iv_ithink_indicator = view.findViewById(R.id.home_iv_ithink_indicator);
		TextView home_tv_ican_indicator = (TextView) view.findViewById(R.id.home_tv_ican_indicator);
		TextView home_tv_ithink_indicator = (TextView) view.findViewById(R.id.home_tv_ithink_indicator);
		if (search_type == 0)
		{
			home_iv_ican_indicator.setBackgroundResource(R.drawable.img_home_seach_normal);
			home_iv_ithink_indicator.setBackgroundResource(R.drawable.img_home_seach_checked);
			home_tv_ican_indicator.setTextColor(getResources().getColor(R.color.home_check_search_normal));
			home_tv_ithink_indicator.setTextColor(getResources().getColor(R.color.home_check_search_checked));
		} else if (search_type == 1)
		{
			home_iv_ican_indicator.setBackgroundResource(R.drawable.img_home_seach_checked);
			home_iv_ithink_indicator.setBackgroundResource(R.drawable.img_home_seach_normal);
			home_tv_ican_indicator.setTextColor(getResources().getColor(R.color.home_check_search_checked));
			home_tv_ithink_indicator.setTextColor(getResources().getColor(R.color.home_check_search_normal));
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.ll_home_search_check_type:
			View view = inflater.inflate(R.layout.popup_home_check_seach, null);
			checkSearchType(view);
			view.findViewById(R.id.home_ll_ican).setOnClickListener(this);
			view.findViewById(R.id.home_ll_ithink).setOnClickListener(this);
			popupWindowCheckSearchType = null;
			popupWindowCheckSearchType = new PopupWindow(view, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			popupWindowCheckSearchType.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_home_check_popup));
			popupWindowCheckSearchType.setOutsideTouchable(true);
			popupWindowCheckSearchType.showAsDropDown(ll_home_search_check_type, 0, 10);
			break;
		case R.id.home_ll_ican:
			search_type = 1;
			home_tv_check_search_indecator.setText("我能");
			popupWindowCheckSearchType.dismiss();
			break;
		case R.id.home_ll_ithink:
			search_type = 0;
			home_tv_check_search_indecator.setText("我想");
			popupWindowCheckSearchType.dismiss();
			break;
		case R.id.search_btn_search:
			saveHistory(m_contentTv.getText().toString());
			String content = m_contentTv.getText().toString().trim();
			if (content != null && !content.equals(""))
			{
				Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
				intent.putExtra("msg", content);
				startActivity(intent);
			}
			else
			{
				Toast.makeText(SearchActivity.this, "搜索内容不能为空！", Toast.LENGTH_LONG).show();
				return;
			}
			
			
			break;
		case R.id.footview_ll_clear_history:
			clearHistory();
			break;
		}
	}

	/**
	 * 清除历史记录
	 */
	private void clearHistory()
	{
		SharedPreferences sp = getSharedPreferences("search_history", 0);
		boolean isClear = sp.edit().clear().commit();
		if (isClear)
		{
			history = null;
			adapter.clear();
			m_listLv.removeFooterView(footView_history);
			adapter.notifyDataSetChanged();
		}
	}

	private String[] getHistory()
	{
		SharedPreferences sp = getSharedPreferences("search_history", 0);
		String longhistory = sp.getString("history", null);
		if (longhistory == null)
			return null;
		String[] hisArrays = longhistory.split(",");
		// 只保留最近的10条的记录
		String[] newArrays = null;
		if (hisArrays.length > 10)
		{
			newArrays = new String[10];
			System.arraycopy(hisArrays, 0, newArrays, 0, 10);
		} else
		{
			newArrays = hisArrays;
		}
		return newArrays;
	}

	private void saveHistory(String text)
	{
		SharedPreferences sp = getSharedPreferences("search_history", 0);
		String longhistory = sp.getString("history", "");
		if (!longhistory.contains(text + ","))
		{
			StringBuilder sb = new StringBuilder(longhistory);
			sb.insert(0, text + ",");
			sp.edit().putString("history", sb.toString()).commit();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
		intent.putExtra("msg", parent.getAdapter().getItem(position).toString());
		startActivity(intent);
	}
	
	@Override
    protected void onDestroy()
    {
        super.onDestroy();
        
        if (popupWindowCheckSearchType != null)
        {
            popupWindowCheckSearchType.dismiss();
            popupWindowCheckSearchType = null;
        }
        
    }
}
