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
//import com.bgood.xn.ui.base.BaseActivity;
//
///**
// * @author ChenGuoqing 2014-6-26下午3:18:54
// */
//public class MessageGroupActivity extends BaseActivity implements
//		OnItemClickListener {
//	private ListView lv_message_group;
//	private List<GroupDTO> list = new ArrayList<GroupDTO>();
//	private GroupAdapter adapter;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.layout_message_group_main);
//		initViews();
//	}
//
//	@Override
//	protected void initViews() {
//		lv_message_group = (ListView) findViewById(R.id.lv_message_group);
//		// View headView = inflater.inflate(R.layout.head_message_main, null);
//		// lv_message_group.addHeaderView(headView);
//		adapter = new GroupAdapter(this, list);
//		lv_message_group.setAdapter(adapter);
//		lv_message_group.setOnItemClickListener(this);
//	}
//
//	@Override
//	protected void initData() {
//
//	}
//
//	@Override
//	protected void setListeners() {
//	}
//
//	@Override
//	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//		toActivity(GroupCardActivity.class);
//	}
//}
