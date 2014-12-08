//package com.bgood.xn.ui.message;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ExpandableListView;
//import android.widget.ExpandableListView.OnChildClickListener;
//import android.widget.ExpandableListView.OnGroupClickListener;
//import android.widget.Toast;
//
//import com.bgood.xn.R;
//import com.bgood.xn.ui.base.BaseActivity;
//
///**
// * @author ChenGuoqing 2014-6-26下午3:00:32
// */
//public class MessageFriendActivity extends BaseActivity implements OnChildClickListener, OnGroupClickListener
//{
//	private ExpandableListView elv_message_friend_main;
//	private FriendListAdapter adapter;
//	private List<UserDTO> list = new ArrayList<UserDTO>();
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.layout_message_friend_main);
////		initViews();
//		setListeners();
//		initData();
//	}
//
//	@Override
//	protected void onDestroy()
//	{
//		super.onDestroy();
//	}
//
//	@Override
//	protected void initData()
//	{
//		super.initData();
//		UserCenterMessageManager.getInstance().registerObserver(this);
//	}
//
//	@Override
//	protected void onStart()
//	{
//		super.onStart();
//		UserCenterMessageManager.getInstance().registerObserver(this);
//		UserCenterMessageManager.getInstance().getUserFriends("");
//	}
//
//	@Override
//	protected void onStop()
//	{
//		super.onStop();
//		UserCenterMessageManager.getInstance().unregisterObserver(this);
//	}
//
//	@Override
//	protected void initViews()
//	{
//		elv_message_friend_main = (ExpandableListView) findViewById(R.id.elv_message_friend_main);
//		View headView = LayoutInflater.from(this).inflate(R.layout.head_message_main, null);
//		elv_message_friend_main.addHeaderView(headView);
//		adapter = new FriendListAdapter(this, list);
//		elv_message_friend_main.setAdapter(adapter);
//		elv_message_friend_main.setOnChildClickListener(this);
//		elv_message_friend_main.setOnGroupClickListener(this);
//	}
//
//	@Override
//	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
//	{
//		String groupName = (String) elv_message_friend_main.getExpandableListAdapter().getGroup(groupPosition);
//		UserDTO user = (UserDTO) elv_message_friend_main.getExpandableListAdapter().getChild(groupPosition, childPosition);
//		Bundle bundle = new Bundle();
//		bundle.putSerializable("user", user);
//		toActivity(UserCardActivity.class, bundle);
//		return false;
//	}
//
//	public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
//	{
//		String groupName = (String) elv_message_friend_main.getExpandableListAdapter().getGroup(groupPosition);
//		Toast.makeText(this, groupName, Toast.LENGTH_SHORT).show();
//		return false;
//	}
//
//	@Override
//	public void getUserFriendsReplyCB(List<UserDTO> list)
//	{
//		super.getUserFriendsReplyCB(list);
//		this.list.clear();
//		this.list.addAll(list);
//		adapter.notifyDataSetChanged();
//	}
//}
