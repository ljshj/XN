package com.bgood.xn.ui.xuanneng.joke;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

import com.bgood.xn.R;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.user.account.LoginActivity;

/**
 * @todo:炫能主页
 * @date:2014-11-21 下午6:05:43
 * @author:hg_liuzl@163.com
 */
public class JokeMainActivity extends TabActivity implements OnCheckedChangeListener, OnClickListener
{
	private TabHost tabHost;
	private RadioGroup radioGroup;
	private ImageView joke_main_b_more_operate;
	private PopupWindow popupWindow_more;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_xuanneng_humor);
		initViews();
		joke_main_b_more_operate = (ImageView) findViewById(R.id.joke_main_b_more_operate);
		joke_main_b_more_operate.setOnClickListener(this);
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
		
		intent = new Intent().setClass(this, JokeOrderActivity.class);
		spec = tabHost.newTabSpec("最新").setIndicator("最新").setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, JokeRandomActivity.class);
		spec = tabHost.newTabSpec("随机").setIndicator("随机").setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent().setClass(this, JokeRankActivity.class);
		spec = tabHost.newTabSpec("排行").setIndicator("排行").setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent().setClass(this, JokeRecordActivity.class);
		spec = tabHost.newTabSpec("榜单").setIndicator("榜单").setContent(intent);
		tabHost.addTab(spec);

		radioGroup = (RadioGroup) this.findViewById(R.id.xuanneng_humor_rg_tab);
		radioGroup.setOnCheckedChangeListener(this);
		
		findViewById(R.id.return_btn).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				JokeMainActivity.this.finish();
			}
		});
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId)
	{
		switch (checkedId)
		{
		
		case R.id.xuanneng_humor_tab_order:
			tabHost.setCurrentTabByTag("最新");
			break;
		case R.id.xuanneng_humor_tab_random:
			tabHost.setCurrentTabByTag("随机");
			break;
		case R.id.xuanneng_humor_tab_ranking:
			tabHost.setCurrentTabByTag("排行");
			break;
		case R.id.xuanneng_humor_tab_record:
			tabHost.setCurrentTabByTag("榜单");
			break;
		}

	}

	@Override
	public void onClick(View v)
	{
		Intent intent = null;
		if(!BGApp.isUserLogin){
			LoginActivity.doLoginAction(this);
		}else{
		
		switch (v.getId())
		{
		
		case R.id.joke_main_b_more_operate:	//展开笑话
				if (popupWindow_more != null && popupWindow_more.isShowing()){
					dissmissPopupMore();
				} else{
					showPopupMore();
			}
			break;
		case R.id.tv_joke_publish:	/**投稿**/
			dissmissPopupMore();
			intent = new Intent(this, JokePublishActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_joke_mention:/**提到我的*/
			dissmissPopupMore();
			intent = new Intent(this, JokeMentionActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_joke_me:/**我的幽默秀*/
			dissmissPopupMore();
			intent = new Intent(this, JokeMeActivity.class);
			intent.putExtra(UserInfoBean.KEY_USER_ID, BGApp.mUserId);
			startActivity(intent);
			break;
		}
	}
}
	
	
	@SuppressLint("InflateParams")
	private void showPopupMore()
	{
		if (popupWindow_more == null)
		{
			View contentView = LayoutInflater.from(this).inflate(R.layout.popup_joke_main_more_operate, null);
			contentView.findViewById(R.id.tv_joke_publish).setOnClickListener(this);
			contentView.findViewById(R.id.tv_joke_me).setOnClickListener(this);
			contentView.findViewById(R.id.tv_joke_mention).setOnClickListener(this);
			popupWindow_more = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			popupWindow_more.setFocusable(true);
			popupWindow_more.setOutsideTouchable(true);
			popupWindow_more.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_weiqiang_more_operate_popup_bg));
		}
		popupWindow_more.showAsDropDown(joke_main_b_more_operate);
	}

	private void dissmissPopupMore()
	{
		if (popupWindow_more != null && popupWindow_more.isShowing())
			popupWindow_more.dismiss();
	}
}
