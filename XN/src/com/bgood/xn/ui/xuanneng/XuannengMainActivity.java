package com.bgood.xn.ui.xuanneng;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

import com.bgood.xn.R;

/**
 * @todo:炫能主页
 * @date:2014-11-21 下午6:05:43
 * @author:hg_liuzl@163.com
 */
public class XuannengMainActivity extends TabActivity implements OnCheckedChangeListener, OnClickListener
{
	private TabHost tabHost;
	private RadioGroup radioGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_xuanneng_humor);
		initViews();
		findViewById(R.id.xuanneng_b_response).setOnClickListener(this);
		findViewById(R.id.return_btn).setOnClickListener(this);
	}
	/**
	 * 初始化tabhost
	 */
	@SuppressWarnings("deprecation")
	private void initViews()
	{
		tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;
		intent = new Intent().setClass(this, XuannengRankingActivity.class);
		spec = tabHost.newTabSpec("排行榜").setIndicator("排行帮").setContent(intent);
		tabHost.addTab(spec);
		intent = new Intent().setClass(this, XuannengOrderActivity.class);
		spec = tabHost.newTabSpec("顺序").setIndicator("顺序").setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, XuannengRandomActivity.class);
		spec = tabHost.newTabSpec("随机").setIndicator("随机").setContent(intent);
		tabHost.addTab(spec);

		radioGroup = (RadioGroup) this.findViewById(R.id.xuanneng_humor_rg_tab);
		radioGroup.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId)
	{
		switch (checkedId)
		{
		case R.id.xuanneng_humor_tab_ranking:
			tabHost.setCurrentTabByTag("排行榜");
			break;
		case R.id.xuanneng_humor_tab_order:
			tabHost.setCurrentTabByTag("顺序");
			break;
		case R.id.xuanneng_humor_tab_random:
			tabHost.setCurrentTabByTag("随机");
			break;
		}

	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.xuanneng_b_response:
			Intent intent = new Intent(this, XuannengPublishActivity.class);
			startActivity(intent);
			break;
		case R.id.return_btn:
			onBackPressed();
			break;
		}
	}

}
