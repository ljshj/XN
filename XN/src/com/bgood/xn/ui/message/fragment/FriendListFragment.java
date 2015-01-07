/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bgood.xn.ui.message.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.bgood.xn.R;
import com.bgood.xn.bean.FriendBean;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.IMRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.base.BaseFragment;
import com.bgood.xn.ui.message.MessageActivity;
import com.bgood.xn.view.BToast;
import com.easemob.chat.Constant;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.activity.ChatActivity;
import com.easemob.chat.activity.NewFriendsMsgActivity;
import com.easemob.chat.adapter.ContactAdapter;
import com.easemob.chat.db.InviteMessgeDao;
import com.easemob.chat.widget.Sidebar;
import com.easemob.exceptions.EaseMobException;

/**
 * 
 * @todo:联系人列表
 * @date:2014-12-12 下午2:45:46
 * @author:hg_liuzl@163.com
 */
public class FriendListFragment extends BaseFragment implements TaskListenerWithState {
	private ContactAdapter adapter;
	private List<FriendBean> contactList;
	private ListView listView;
	private Sidebar sidebar;
	private InputMethodManager inputMethodManager;
	private List<String> blackList;
	/**要操作的实体类*/
	private FriendBean mActionFriendBean = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_contact_list, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		//防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
		if(savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
		    return;
		
		inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		listView = (ListView) getView().findViewById(R.id.list);
		sidebar = (Sidebar) getView().findViewById(R.id.sidebar);
		sidebar.setListView(listView);
		//黑名单列表
		blackList = EMContactManager.getInstance().getBlackListUsernames();
		contactList = new ArrayList<FriendBean>();
		// 获取设置contactlist
		getContactList();
		// 设置adapter
		adapter = new ContactAdapter(getActivity(), R.layout.row_contact, contactList, sidebar);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String username = adapter.getItem(position).getName();
				if (Constant.NEW_FRIENDS_USERNAME.equals(username)) {
					// 进入申请与通知页面
					FriendBean user = BGApp.getInstance().getFriendMapByName().get(Constant.NEW_FRIENDS_USERNAME);
					user.setUnreadMsgCount(0);
					startActivity(new Intent(getActivity(), NewFriendsMsgActivity.class));
				}else {
					// demo中直接进入聊天页面，实际一般是进入用户详情页
					startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("userId", adapter.getItem(position).userid));
				}
			}
		});
		listView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 隐藏软键盘
				if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (getActivity().getCurrentFocus() != null)
						inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				}
				return false;
			}
		});
		
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {

            	final FriendBean friendBean = (FriendBean) adapter.getAdapter().getItem(position);
            	
            	AlertDialog dialog = new AlertDialog.Builder(getActivity())
            	.setTitle("删除联系人")
            	.setCancelable(true)
            	.setPositiveButton("确定", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						mActionFriendBean = friendBean;
						// 删除此联系人
						deleteContact(mActionFriendBean);
						
					}
				})
            	.setNegativeButton("取消", null)
            	.show();
                
                
                return true;
            }
        });
		//registerForContextMenu(listView);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		// 长按前两个不弹menu
		if (((AdapterContextMenuInfo) menuInfo).position > 1) {
			getActivity().getMenuInflater().inflate(R.menu.context_contact_list, menu);
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	


	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.delete_contact) {
			mActionFriendBean = adapter.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
			
			// 删除此联系人
			deleteContact(mActionFriendBean);
			// 删除相关的邀请消息
			InviteMessgeDao dao = new InviteMessgeDao(getActivity());
			dao.deleteMessage("bg"+mActionFriendBean.userid);
			return true;
		}else if(item.getItemId() == R.id.add_to_blacklist){
//			mActionFriendBean = adapter.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
//			moveToBlacklist(user.getName());
			return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		if (!hidden) {
			refresh();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!hidden) {
			refresh();
		}
	}

	/**
	 * 删除联系人
	 * 
	 * @param toDeleteUser
	 */
	public void deleteContact(final FriendBean tobeDeleteUser) {
		IMRequest.getInstance().requestFriendDelete(FriendListFragment.this, mActivity, new String[]{tobeDeleteUser.userid});
	}

	/**
	 * 把user移入到黑名单
	 */
	private void moveToBlacklist(final String username){
		final ProgressDialog pd = new ProgressDialog(getActivity());
		pd.setMessage("正在移入黑名单...");
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		new Thread(new Runnable() {
			public void run() {
				try {
					//加入到黑名单
					EMContactManager.getInstance().addUserToBlackList(username,false);
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							BToast.show(mActivity, "移入黑名单成功");
							refresh();
						}
					});
				} catch (EaseMobException e) {
					e.printStackTrace();
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							BToast.show(mActivity, "移入黑名单成功");
						}
					});
				}
			}
		}).start();
		
	}
	
	// 刷新ui
	public void refresh() {
		try {
			// 可能会在子线程中调到这方法
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					getContactList();
					adapter.notifyDataSetChanged();

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取联系人列表，并过滤掉黑名单和排序
	 */
	private void getContactList() {
		contactList.clear();
		//获取本地好友列表
		Map<String, FriendBean> users = BGApp.getInstance().getFriendMapByName();
		Iterator<Entry<String, FriendBean>> iterator = users.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, FriendBean> entry = iterator.next();
			if (!entry.getKey().equals(Constant.NEW_FRIENDS_USERNAME) && !entry.getKey().equals(Constant.GROUP_USERNAME)
					&& !blackList.contains(entry.getKey()))
				contactList.add(entry.getValue());
		}
		// 排序
		Collections.sort(contactList, new Comparator<FriendBean>() {

			@Override
			public int compare(FriendBean lhs, FriendBean rhs) {
				return lhs.getName().compareTo(rhs.getName());
			}
		});
		// 把"申请与通知"添加到首位
		contactList.add(0, users.get(Constant.NEW_FRIENDS_USERNAME));
	}

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
				switch (bNetWork.getMessageType()) {
				case 850008:
						if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
							
							// 删除相关的邀请消息
							InviteMessgeDao dao = new InviteMessgeDao(getActivity());
							dao.deleteMessage("bg"+mActionFriendBean.userid);
							
							FriendBean.deleteFriendBean(dbHelper, mActionFriendBean.userid);
							
//							MessageActivity.instance.dealIMFriendAndGroup();
							BGApp.getInstance().getFriendMapById().remove(mActionFriendBean);
							BGApp.getInstance().getFriendMapByName().remove(mActionFriendBean);
							
							adapter.remove(mActionFriendBean);
							adapter.notifyDataSetChanged();
							
						}else{
							
						}
					break;
				case 850027:
					break;

				default:
					break;
				}
			}
	}
}
