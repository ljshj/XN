package com.bgood.xn.ui.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;

import com.bgood.xn.R;
import com.bgood.xn.bean.FriendBean;
import com.bgood.xn.system.BGApp;
import com.easemob.chat.Constant;
import com.easemob.chat.activity.BaseActivity;
import com.easemob.chat.adapter.ContactAdapter;
import com.easemob.chat.widget.Sidebar;

/**
 * 选择添加群成员 
 */
public class GroupPickContactsActivity extends BaseActivity {
	private ListView listView;
	/** 是否为一个新建的群组 */
	protected boolean isCreatingNewGroup;
	/** 是否为单选 */
	private boolean isSignleChecked;
	private PickContactAdapter contactAdapter;
	/** group中一开始就有的成员 */
	private List<FriendBean> exitingMembers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_pick_contacts);
		String hxgroupId = getIntent().getStringExtra("hxgroupId");
		if (hxgroupId == null) {// 创建群组
			isCreatingNewGroup = true;
		} else {
			// 获取此群组的成员列表
			exitingMembers = BGApp.getInstance().getGroupMemberAndHxId().get(hxgroupId);
			
		}
		if(exitingMembers == null)
			exitingMembers = new ArrayList<FriendBean>();
		// 获取好友列表
		final List<FriendBean> alluserList = new ArrayList<FriendBean>();
		
		Map<String, FriendBean> friendMapByName = BGApp.getInstance().getFriendMapByName();
		
		for(Map.Entry<String, FriendBean> entry:friendMapByName.entrySet()){    
		     alluserList.add(entry.getValue());
		}   
		
		
		
		for (FriendBean user : alluserList) {
			if (!user.name.equals(Constant.NEW_FRIENDS_USERNAME) & !user.name.equals(Constant.GROUP_USERNAME))
				alluserList.add(user);
		}
		// 对list进行排序
		Collections.sort(alluserList, new Comparator<FriendBean>() {
			@Override
			public int compare(FriendBean lhs, FriendBean rhs) {
				return (lhs.name.compareTo(rhs.name));

			}
		});

		listView = (ListView) findViewById(R.id.list);
		contactAdapter = new PickContactAdapter(this, R.layout.row_contact_with_checkbox, alluserList);
		listView.setAdapter(contactAdapter);
		((Sidebar) findViewById(R.id.sidebar)).setListView(listView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
				checkBox.toggle();

			}
		});
	}

	/**
	 * 确认选择的members
	 * 
	 * @param v
	 */
	public void save(View v) {
		setResult(RESULT_OK, new Intent().putParcelableArrayListExtra("newmembers", getToBeAddMembers()));
		finish();
	}

	/**
	 * 获取要被添加的成员
	 * 
	 * @return
	 */
	private ArrayList<FriendBean> getToBeAddMembers() {
		ArrayList<FriendBean> members = new ArrayList<FriendBean>();
		int length = contactAdapter.isCheckedArray.length;
		for (int i = 0; i < length; i++) {
			FriendBean friend = contactAdapter.getItem(i);
			if (contactAdapter.isCheckedArray[i] && !exitingMembers.contains(friend)) {
				members.add(friend);
			}
		}

		return members;
	}

	/**
	 * adapter
	 */
	private class PickContactAdapter extends ContactAdapter {

		private boolean[] isCheckedArray;

		public PickContactAdapter(Context context, int resource, List<FriendBean> users) {
			super(context, resource, users, null);
			isCheckedArray = new boolean[users.size()];
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			if (position > 0) {
				final FriendBean friend = getItem(position);
				// 选择框checkbox
				final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
				if(exitingMembers != null && exitingMembers.contains(friend)){
					checkBox.setButtonDrawable(R.drawable.checkbox_bg_gray_selector);
				}else{
					checkBox.setButtonDrawable(R.drawable.checkbox_bg_selector);
				}
				if (checkBox != null) {

					checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							// 群组中原来的成员一直设为选中状态
							if (exitingMembers.contains(friend)) {
									isChecked = true;
									checkBox.setChecked(true);
							}
							isCheckedArray[position - 1] = isChecked;
							//如果是单选模式
							if (isSignleChecked && isChecked) {
								for (int i = 0; i < isCheckedArray.length; i++) {
									if (i != position - 1) {
										isCheckedArray[i] = false;
									}
								}
								contactAdapter.notifyDataSetChanged();
							}
							
						}
					});
					// 群组中原来的成员一直设为选中状态
					if (exitingMembers.contains(friend)) {
							checkBox.setChecked(true);
							isCheckedArray[position - 1] = true;
					} else {
						checkBox.setChecked(isCheckedArray[position - 1]);
					}
				}
			}
			return view;
		}
	}

	public void back(View view){
		finish();
	}
	
}
