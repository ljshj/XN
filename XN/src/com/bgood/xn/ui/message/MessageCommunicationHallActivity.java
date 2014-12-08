//package com.bgood.xn.ui.message;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ListView;
//
//import com.bgood.xn.R;
//import com.bgood.xn.adapter.CommunicationHallAdapter;
//import com.bgood.xn.ui.base.BaseActivity;
//
///**
// * @author ChenGuoqing 2014-6-26下午3:37:13
// */
//public class MessageCommunicationHallActivity extends BaseActivity implements OnItemClickListener
//{
//	private ListView lv_message_communication_hall_main;
//	private CommunicationHallAdapter adapter;
//	private List<GroupDTO> list = new ArrayList<GroupDTO>();
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.layout_message_communication_hall_main);
//		initViews();
//		setListeners();
//		initData();
//	}
//
//	@Override
//	protected void initViews()
//	{
//		lv_message_communication_hall_main = (ListView) findViewById(R.id.lv_message_communication_hall_main);
////		View headView = inflater.inflate(R.layout.head_message_main, null);
////		lv_message_communication_hall_main.addHeaderView(headView);
//		adapter = new CommunicationHallAdapter(this, list);
//		lv_message_communication_hall_main.setAdapter(adapter);
//		lv_message_communication_hall_main.setOnItemClickListener(this);
//	}
//
//	@Override
//	protected void initData()
//	{
//	}
//
//	@Override
//	protected void setListeners()
//	{
//	}
//
//	@Override
//	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
//	{
//		toActivity(CommunicationHallCardActivity.class);
//	}
//}
