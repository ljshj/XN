//package com.bgood.xn.ui.message.fragment;
//
//import java.util.List;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.ListView;
//
//import com.bgood.xn.R;
//import com.easemob.chat.EMGroup;
//import com.easemob.chat.EMGroupManager;
//import com.easemob.chat.adapter.GroupAdapter;
//
///**
// * @todo:TODO
// * @date:2014-12-12 上午10:01:05
// * @author:hg_liuzl@163.com
// */
//public class GroupFragment extends Fragment {
//	
//	private ListView groupListView;
//	protected List<EMGroup> grouplist;
//	private GroupAdapter groupAdapter;
//	private InputMethodManager inputMethodManager;
//	private View layout;
//	
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
//		layout =  inflater.inflate(R.layout.fragment_groups, container, false);
//		return layout;
//	}
//	
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//		inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//		grouplist =	EMGroupManager.getInstance().getAllGroups();
//		groupListView = (ListView)findViewById(R.id.list);
//		groupAdapter = new GroupAdapter(this, 1, grouplist);
//		groupListView.setAdapter(groupAdapter);
//		groupListView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//					//进入群聊
//					Intent intent = new Intent(GroupsActivity.this, ChatActivity.class);
//					// it is group chat
//					intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
//					intent.putExtra("groupId", groupAdapter.getItem(position - 1).getGroupId());
//					startActivityForResult(intent, 0);
//				}
//			}
//
//		});
//		groupListView.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
//					if (getCurrentFocus() != null)
//						inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
//								InputMethodManager.HIDE_NOT_ALWAYS);
//				}
//				return false;
//			}
//		});
//	}
//	
//	
//	
//	@Override
//	public void onResume() {
//		super.onResume();
//		grouplist = EMGroupManager.getInstance().getAllGroups();
//		groupAdapter = new GroupAdapter(this, 1, grouplist);
//		groupListView.setAdapter(groupAdapter);
//		groupAdapter.notifyDataSetChanged();
//	}
//}
