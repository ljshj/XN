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
package com.bgood.xn.ui.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.adapter.KBaseAdapter;
import com.bgood.xn.bean.FriendBean;
import com.bgood.xn.bean.FriendGroupBean;
import com.bgood.xn.bean.GroupBean;
import com.bgood.xn.bean.GroupMemberBean;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.IMRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.system.Const;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.ui.message.fragment.CommunicateFragment;
import com.bgood.xn.ui.message.fragment.GroupFragment;
import com.bgood.xn.ui.user.info.NameCardActivity;
import com.bgood.xn.view.BToast;
import com.bgood.xn.widget.TitleBar;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.activity.AlertDialog;
import com.easemob.chat.activity.ChatActivity;
import com.easemob.chat.activity.ExitGroupDialog;
import com.easemob.chat.widget.ExpandGridView;
import com.easemob.util.NetUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @todo:交流厅详情
 * @date:2014-12-19 上午11:34:40
 * @author:hg_liuzl@163.com
 */
public class CommunicateDetailActivity extends BaseActivity implements OnClickListener,TaskListenerWithState 
{
	private static final int REQUEST_CODE_ADD_USER = 0;
	private static final int REQUEST_CODE_EXIT = 1;
	private static final int REQUEST_CODE_EXIT_DELETE = 2;
	private static final int REQUEST_CODE_CLEAR_ALL_HISTORY = 3;
	
	String longClickUsername = null;
	private ExpandGridView userGridview;
	private String hxgroupId;
	
	private Button exitBtn;
	private Button deleteBtn;
	private GroupBean group;
	private GridAdapter adapter;
	private int referenceWidth;
	private int referenceHeight;
	private ProgressDialog progressDialog;
	private List<FriendBean> friends = new ArrayList<FriendBean>();
	/**添加群成员*/
	private List<FriendBean> addFriends = new ArrayList<FriendBean>();

	public static CommunicateDetailActivity instance;

	// 清空所有聊天记录
	
	private TextView tvGroupName;
	
	/**要操作的FriendBean*/
	private FriendBean actionFriendBean = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.activity_communicate_details);
		(new TitleBar(mActivity)).initTitleBar("交流厅详情");
		hxgroupId = getIntent().getStringExtra(Const.CHAT_HXGROUPID);
		group = BGApp.getInstance().getGroupAndHxId().get(hxgroupId);
		initView();
		/**获取群成员*/
		IMRequest.getInstance().requestGroupMembers(CommunicateDetailActivity.this, this,group.roomid,true);
	}
	
	@SuppressLint("ClickableViewAccessibility")
	private void initView() {
		userGridview = (ExpandGridView) findViewById(R.id.gridview);
		
		tvGroupName = (TextView) findViewById(R.id.tv_communcation_name);
		tvGroupName.setText(group.name);
		
		findViewById(R.id.clear_all_history).setOnClickListener(this);
		exitBtn = (Button) findViewById(R.id.btn_exit_grp);
		deleteBtn = (Button) findViewById(R.id.btn_exitdel_grp);

		Drawable referenceDrawable = getResources().getDrawable(R.drawable.smiley_add_btn);
		referenceWidth = referenceDrawable.getIntrinsicWidth();
		referenceHeight = referenceDrawable.getIntrinsicHeight();
		adapter = new GridAdapter(friends,this);
		userGridview.setAdapter(adapter);

		// 设置OnTouchListener
		userGridview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (adapter.isInDeleteMode) {
						adapter.isInDeleteMode = false;
						adapter.notifyDataSetChanged();
						return true;
					}
					break;
				default:
					break;
				}
				return false;
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(CommunicateDetailActivity.this);
				progressDialog.setMessage("正在添加...");
				progressDialog.setCanceledOnTouchOutside(false);
			}
			switch (requestCode) {
			case REQUEST_CODE_ADD_USER:// 添加群成员
				addFriends = data.getParcelableArrayListExtra("newmembers");
				progressDialog.show();
				addMembersToGroup();
				break;
			case REQUEST_CODE_EXIT: // 退出群
				progressDialog.setMessage("正在退出群聊...");
				progressDialog.show();
				exitGrop();
				break;
			case REQUEST_CODE_EXIT_DELETE: // 解散群
				progressDialog.setMessage("正在解散群聊...");
				progressDialog.show();
				deleteGrop();
				break;
			case REQUEST_CODE_CLEAR_ALL_HISTORY:
				// 清空此群聊的聊天记录
				progressDialog.setMessage("正在清空群消息...");
				progressDialog.show();
				clearGroupHistory();
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 点击退出群组按钮
	 * @param view
	 */
	public void exitGroup(View view) {
		startActivityForResult(new Intent(this, ExitGroupDialog.class), REQUEST_CODE_EXIT);
	}

	/**
	 * 点击解散群组按钮
	 * @param view
	 */
	public void exitDeleteGroup(View view) {
		startActivityForResult(new Intent(this, ExitGroupDialog.class).putExtra("deleteToast", getString(R.string.dissolution_group_hint)),
				REQUEST_CODE_EXIT_DELETE);

	}

	/**
	 * 清空群聊天记录
	 */
	public void clearGroupHistory() {
		EMChatManager.getInstance().clearConversation(group.hxgroupid);
		progressDialog.dismiss();
	}

	/**
	 * 退出群组
	 */
	private void exitGrop() {
		IMRequest.getInstance().requestGroupQuit(CommunicateDetailActivity.this, mActivity, BGApp.mUserId, group.roomid);
	}

	/**
	 * 解散群组
	 */
	private void deleteGrop() {
		IMRequest.getInstance().requestGroupDisMiss(CommunicateDetailActivity.this, mActivity, group.roomid);
	}

	/**
	 * 增加群成员
	 * 
	 * @param newmembers
	 */
	private void addMembersToGroup() {
		new Thread(new Runnable() {

			public void run() {
				try {
					
					String[] newmembers = new String[addFriends.size()];
					final String[] invites = new String[addFriends.size()];
					
					for (int i = 0; i < addFriends.size(); i++) {
						FriendBean friend = addFriends.get(i);
						newmembers[i] = "bg"+friend.userid;
						invites[i] = friend.userid;
					}
					
					runOnUiThread(new Runnable() {
						public void run() {
							IMRequest.getInstance().requestGroupMemberJoinOrInvite(CommunicateDetailActivity.this, mActivity, invites, group.roomid);
							friends.addAll(addFriends);
							adapter.notifyDataSetChanged();
							progressDialog.dismiss();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							BToast.show(mActivity, "添加成员失败");
						}
					});
				}
			}
		}).start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.clear_all_history: // 清空聊天记录
			Intent intent = new Intent(CommunicateDetailActivity.this, AlertDialog.class);
			intent.putExtra("cancel", true);
			intent.putExtra("titleIsCancel", true);
			intent.putExtra("msg", "确定清空此群的聊天记录吗？");
			startActivityForResult(intent, REQUEST_CODE_CLEAR_ALL_HISTORY);
			break;
		default:
			break;
		}

	}
	
	/**
	 * 群组成员gridadapter
	 */
	class GridAdapter extends KBaseAdapter {
		public boolean isInDeleteMode;
		public GridAdapter(List<?> mList, Activity mActivity) {
			super(mList, mActivity);
		}
		
		@Override
		public int getCount() {
			return super.getCount();
		}


		@Override
		public View getView(final int position, View convertView, final ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(mActivity).inflate(R.layout.grid,null);
			}
			final Button button = (Button) convertView.findViewById(R.id.button_avatar);
				
				final FriendBean friendBean = (FriendBean) mList.get(position);
				if(null == friendBean){
					return null;
				}
				
				final ImageView iv =  (ImageView) convertView.findViewById(R.id.iv_img);
				
				
				button.setText(friendBean.name);
				convertView.setVisibility(View.VISIBLE);
				button.setVisibility(View.VISIBLE);
				
				BGApp.getInstance().setImage(friendBean.photo,iv);
				
				Drawable avatar = iv.getDrawable();
				if(null==avatar){
					avatar = getResources().getDrawable(R.drawable.default_avatar);
				}
				
				avatar.setBounds(0, 0, referenceWidth, referenceHeight);
				button.setCompoundDrawables(null, avatar, null, null);
				
				if (isInDeleteMode) {
					// 如果是删除模式下，显示减人图标
					convertView.findViewById(R.id.badge_delete).setVisibility(View.VISIBLE);
				} else {
					convertView.findViewById(R.id.badge_delete).setVisibility(View.GONE);
				}
				
				
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						actionFriendBean = friendBean;
						if (isInDeleteMode) {
							// 如果是删除自己，return
							if (EMChatManager.getInstance().getCurrentUser().equals("bg"+actionFriendBean.userid)) {
								BToast.show(mActivity, "不能删除自己");
								return;
							}
							if (!NetUtils.hasNetwork(getApplicationContext())) {
								BToast.show(mActivity, R.string.network_unavailable);
								return;
							}
							deleteMembersFromGroup();
						} else {
							 //正常情况下点击user，可以进入用户详情
							 NameCardActivity.lookNameCard(mActivity, actionFriendBean.userid);
						}
					}});
			return convertView;
		}
	}

	/**
	 * 删除群成员
	 * @param username
	 */
	protected void deleteMembersFromGroup() {
		IMRequest.getInstance().requestGroupMemberRemove(CommunicateDetailActivity.this, CommunicateDetailActivity.this, actionFriendBean.userid, group.roomid);
}
	

	public void back(View view) {
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		instance = null;
	}



	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			String json = bNetWork.getStrJson();
				switch (bNetWork.getMessageType()) {
				case 850013: //获取群成员列表	
						if (bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK) {
							FriendGroupBean fgb = JSON.parseObject(json,FriendGroupBean.class);
							friends.addAll(fgb.items);
							adapter.notifyDataSetChanged();
							//initView();
							//每次同步一下数据
							GroupMemberBean.deleteGroupMemberBean(dbHelper, group.roomid);
							GroupMemberBean.storeGroupMemberBean(dbHelper, group.hxgroupid, group.roomid, friends);
							List<FriendBean> storeFriend = BGApp.getInstance().getGroupMemberAndHxId().get(group.hxgroupid);
							storeFriend.addAll(addFriends);
							BGApp.getInstance().getGroupMemberAndHxId().put(group.hxgroupid, storeFriend);
						}
					break;
				case 850018: //退出群 和解散群共用
				case 850016: // 解散群聊
						if (bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK) {
							progressDialog.dismiss();
							if(null!=CommunicateFragment.instance){
								CommunicateFragment.instance.refresh();
							}
							if(null!=ChatActivity.activityInstance){
								ChatActivity.activityInstance.finish();
							}
							BGApp.getInstance().deleteGroup(dbHelper, group);
							
							
							EMChatManager.getInstance().deleteConversation(group.hxgroupid, true);
							
							setResult(RESULT_OK);
							finish();
							
						} else {
							progressDialog.dismiss();
							BToast.show(mActivity, "解散群聊失败");
						}
						break;
				case 850024://删除群成员
					if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
						
						friends.remove(actionFriendBean);
						adapter.isInDeleteMode = false;
						adapter.notifyDataSetChanged();
					}
					GroupMemberBean.deleteGroupMemberBean(dbHelper, group.roomid, actionFriendBean.userid);
				break;
				case 850025:	//添加或邀请群成员
					if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
						BGApp.getInstance().getGroupMemberAndHxId().get(group.hxgroupid).addAll(addFriends);
						GroupMemberBean.storeGroupMemberBean(dbHelper, group.hxgroupid, group.roomid, addFriends);
					}
					break;
				default:
					break;
				
				}
		
	}	
}
	
	/**身份的type比较，群创建者在前，其次是管理员，最后是普通成员*/
	private void sortMemberByType() {
		Collections.sort(friends, new Comparator<FriendBean>() {
			@Override
			public int compare(final FriendBean f1, final FriendBean f2) {
				if (Integer.valueOf(f1.type) == Integer.valueOf(f2.type)) {
					return 0;
				} else if (Integer.valueOf(f1.type) < Integer.valueOf(f2.type)) {
					return 1;
				} else {
					return -1;
				}
			}

		});
	}
}
