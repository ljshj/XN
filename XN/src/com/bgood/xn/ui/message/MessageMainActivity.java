//package com.bgood.xn.ui.message;
//
//import android.app.TabActivity;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.net.ConnectivityManager;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.RadioGroup;
//import android.widget.RadioGroup.OnCheckedChangeListener;
//import android.widget.TabHost;
//
//import com.bgood.xn.R;
//
///**
// * 
// * @todo:
// * @date:2014-12-8 下午6:01:24
// * @author:hg_liuzl@163.com
// */
//public class MessageMainActivity extends TabActivity implements OnCheckedChangeListener, OnClickListener
//{
//
//	private TabHost tabHost;
//	private RadioGroup radioGroup;
//	private int messageCechk = 0;
//	private View b_message_main_add;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.layout_message_main);
//		initViews();
//		setListener();
//	}
//
//	@Override
//	protected void onStart()
//	{
//		super.onStart();
//		registerNetMonitor();
//	}
//
//	@Override
//	protected void onStop()
//	{
//		super.onStop();
//		unRegisterNetMonitor();
//	}
//
//	/**
//	 * 设置监听事件
//	 */
//	private void setListener()
//	{
//		findViewById(R.id.b_message_main_add).setOnClickListener(this);
//	}
//
//	/**
//	 * 初始化数据
//	 */
//	private void initData()
//	{
//		ZZSessionManager.getInstance().createKeepAliveConnect();
//	}
//
//	/**
//	 * 初始化view
//	 */
//	private void initViews()
//	{
//		initRadio();
//		b_message_main_add = findViewById(R.id.b_message_main_add);
//	}
//
//	/**
//	 * 初始化tabhost
//	 */
//	private void initRadio()
//	{
//		tabHost = getTabHost();
//		TabHost.TabSpec spec;
//		Intent intent;
//		intent = new Intent().setClass(this, MessageCenterActivity.class);
//		spec = tabHost.newTabSpec("消息").setIndicator("消息").setContent(intent);
//		tabHost.addTab(spec);
//		intent = new Intent().setClass(this, MessageFriendActivity.class);
//		spec = tabHost.newTabSpec("好友").setIndicator("好友").setContent(intent);
//		tabHost.addTab(spec);
//
//		intent = new Intent().setClass(this, MessageGroupActivity.class);
//		spec = tabHost.newTabSpec("固定群").setIndicator("固定群").setContent(intent);
//		tabHost.addTab(spec);
//
//		intent = new Intent().setClass(this, MessageCommunicationHallActivity.class);
//		spec = tabHost.newTabSpec("交流厅").setIndicator("交流厅").setContent(intent);
//		tabHost.addTab(spec);
//
//		radioGroup = (RadioGroup) this.findViewById(R.id.rg_message_main_tab);
//		radioGroup.setOnCheckedChangeListener(this);
//	}
//
//	@Override
//	public void onCheckedChanged(RadioGroup group, int checkedId)
//	{
//		switch (checkedId)
//		{
//		case R.id.message_main_tab_messagecenter:
//			tabHost.setCurrentTabByTag("消息");
//			messageCechk = 0;
//			b_message_main_add.setVisibility(View.INVISIBLE);
//			break;
//		case R.id.message_main_tab_friend:
//			tabHost.setCurrentTabByTag("好友");
//			messageCechk = 1;
//			b_message_main_add.setVisibility(View.VISIBLE);
//			break;
//		case R.id.message_main_tab_group:
//			tabHost.setCurrentTabByTag("固定群");
//			messageCechk = 2;
//			b_message_main_add.setVisibility(View.VISIBLE);
//			break;
//		case R.id.message_main_tab_communication_hall:
//			tabHost.setCurrentTabByTag("交流厅");
//			b_message_main_add.setVisibility(View.INVISIBLE);
//			messageCechk = 3;
//			break;
//		}
//
//	}
//
//	public class MessageCheck
//	{
//		public static final int Message_main = 0;
//		public static final int Message_friend = 1;
//		public static final int Message_group = 2;
//		public static final int Message_communication_hall = 3;
//	}
//
//	@Override
//	public void onClick(View v)
//	{
//		switch (v.getId())
//		{
//		case R.id.b_message_main_add:
//			Intent intent = new Intent(MessageMainActivity.this, SearchFriendOrGroupActivity.class);
//			Bundle bundle = new Bundle();
//			bundle.putInt("messageCheck", messageCechk);
//			intent.putExtras(bundle);
//			startActivity(intent);
//			break;
//		}
//	}
//
//	void registerNetMonitor()
//	{
//		IntentFilter filter = new IntentFilter();
//		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//		SystemRunning.sharedSystemRunning().getApplicationContext().registerReceiver(mBroadcastReceiver, filter);
//	}
//
//	void unRegisterNetMonitor()
//	{
//		IntentFilter filter = new IntentFilter();
//		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//		SystemRunning.sharedSystemRunning().getApplicationContext().unregisterReceiver(mBroadcastReceiver);
//	}
//
//	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
//	{
//		@Override
//		public void onReceive(Context context, Intent intent)
//		{
//			ZZSessionManager.getInstance().netStateUpdate();
//		}
//	};
//
//}
