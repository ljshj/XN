//package com.bgood.xn.ui.message;
//
//import java.util.List;
//
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.BaseAdapter;
//import android.widget.EditText;
//
//import com.bgood.xn.R;
//import com.bgood.xn.network.BaseNetWork.ReturnCode;
//import com.bgood.xn.ui.base.BaseActivity;
//import com.bgood.xn.view.xlistview.XListView;
//import com.bgood.xn.view.xlistview.XListView.IXListViewListener;
//
///**
// * @author ChenGuoqing 2014-6-30下午4:12:55
// */
//public class SearchFriendOrGroupActivity extends BaseActivity implements OnItemClickListener, OnClickListener, IXListViewListener
//{
//	private XListView lv_add_friend_or_group;
//	private BaseAdapter adapter;
//	private int messageCheck = 1;
//	private List list;
//	private EditText et_add_friend_or_group_search;
//	private int start = 0;
//	private String keyword;
//	private boolean click_search_flag = false;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.search_friend_or_group);
//		initViews();
//		setListeners();
//		initData();
//	}
//
//	@Override
//	protected void initData()
//	{
//		super.initData();
//	}
//
//	@Override
//	protected void initViews()
//	{
//		super.initViews();
//		et_add_friend_or_group_search = (EditText) findViewById(R.id.et_add_friend_or_group_search);
//		lv_add_friend_or_group = (XListView) findViewById(R.id.lv_add_friend_or_group);
//		lv_add_friend_or_group.setPullRefreshEnable(false);
//		lv_add_friend_or_group.setPullLoadEnable(false);
//		lv_add_friend_or_group.setXListViewListener(this);
//		Bundle bundle = getIntent().getExtras();
//		if (bundle != null)
//			messageCheck = bundle.getInt("messageCheck");
//		switch (messageCheck)
//		{
//		case MessageMainActivity.MessageCheck.Message_friend:
//			adapter = new FriendSearchAdapter(this, list);
//			break;
//		case MessageMainActivity.MessageCheck.Message_group:
//			adapter = new GroupAdapter(this, list);
//			break;
//		}
//		lv_add_friend_or_group.setAdapter(adapter);
//		lv_add_friend_or_group.setOnItemClickListener(this);
//	}
//
//	@Override
//	protected void setListeners()
//	{
//		super.setListeners();
//		findViewById(R.id.search_friend_b_search).setOnClickListener(this);
//	}
//
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//	{
//		switch (messageCheck)
//		{
//		case MessageMainActivity.MessageCheck.Message_friend:
//		{
//			UserDTO user = (UserDTO) adapter.getItem(position);
//			Bundle bundle = new Bundle();
//			bundle.putSerializable("user", user);
//			toActivity(UserCardActivity.class, bundle);
//			break;
//		}
//		case MessageMainActivity.MessageCheck.Message_group:
//		{
//			GroupDTO group = (GroupDTO) adapter.getItem(position);
//			Bundle bundle = new Bundle();
//			bundle.putSerializable("group", group);
//			toActivity(GroupCardActivity.class, null);
//			break;
//		}
//		}
//	}
//
//	@Override
//	public void searchFriendsReplyCB(Reulst result_state, List<UserDTO> list)
//	{
//		super.searchFriendsReplyCB(result_state, list);
//		findViewById(R.id.search_friend_pb_searching).setVisibility(View.VISIBLE);
//		lv_add_friend_or_group.setVisibility(View.GONE);
//		if (result_state.resultCode == ReturnCode.RETURNCODE_OK)
//		{
//			if (list == null)
//			{
//				lv_add_friend_or_group.setPullLoadEnable(false);
//				return;
//			}
//			if (this.list == null)
//				this.list = list;
//			else
//			{
//				if (click_search_flag)
//				{
//					this.list.clear();
//					this.list.addAll(list);
//				} else
//				{
//					this.list.addAll(list);
//				}
//			}
//			if (list.size() < 10)
//			{
//				lv_add_friend_or_group.setPullLoadEnable(false);
//			} else
//			{
//				lv_add_friend_or_group.setPullLoadEnable(true);
//			}
//		}
//	}
//
//	@Override
//	public void onClick(View v)
//	{
//		switch (v.getId())
//		{
//		case R.id.search_friend_b_search:
//			keyword = et_add_friend_or_group_search.getText().toString();
//			if (keyword != null && keyword.length() > 0)
//			{
//				start = 0;
//				click_search_flag = true;
//				findViewById(R.id.search_friend_pb_searching).setVisibility(View.VISIBLE);
//				lv_add_friend_or_group.setVisibility(View.GONE);
//				UserCenterMessageManager.getInstance().searchFriends(keyword, start, start = start + 10);
//			}
//			break;
//		}
//	}
//
//	@Override
//	public void onRefresh()
//	{
//	}
//
//	@Override
//	public void onLoadMore()
//	{
//		UserCenterMessageManager.getInstance().searchFriends(keyword, start, start = start + 10);
//	}
//}
