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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.adapter.KBaseAdapter;
import com.bgood.xn.bean.FriendBean;
import com.bgood.xn.bean.FriendGroupBean;
import com.bgood.xn.bean.GroupBean;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.IMRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.view.BToast;
import com.bgood.xn.widget.TitleBar;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.activity.AlertDialog;
import com.easemob.chat.activity.ChatActivity;
import com.easemob.chat.activity.ExitGroupDialog;
import com.easemob.chat.widget.ExpandGridView;
import com.easemob.util.NetUtils;

/**
 * 
 * @todo:群详情
 * @date:2014-12-19 上午11:34:40
 * @author:hg_liuzl@163.com
 */
public class GroupDetailsActivity extends BaseActivity implements OnClickListener,TaskListenerWithState 
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

	private RelativeLayout rl_switch_block_groupmsg;
	/**
	 * 屏蔽群消息imageView
	 */
	private ImageView iv_switch_block_groupmsg;
	/**
	 * 关闭屏蔽群消息imageview
	 */
	private ImageView iv_switch_unblock_groupmsg;

	public static GroupDetailsActivity instance;

	// 清空所有聊天记录
	private RelativeLayout clearAllHistory;
	
	private TextView tvNotice,tvGroupInfo;
	
	/**当前用户的身份*/
	private String type = "";  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.activity_group_details);
		(new TitleBar(mActivity)).initTitleBar("群详情");
		// 获取传过来的groupid
		hxgroupId = getIntent().getStringExtra("hxgroupId");
		group = BGApp.getInstance().getGroupMap().get(hxgroupId);
		/**获取群成员*/
		IMRequest.getInstance().requestGroupMembers(GroupDetailsActivity.this, this,group.roomid,true);
	}
	
	@SuppressLint("ClickableViewAccessibility")
	private void initView() {
		userGridview = (ExpandGridView) findViewById(R.id.gridview);
		
		tvGroupInfo = (TextView) findViewById(R.id.tv_group_intro);
		tvGroupInfo.setText(group.intro);
		
		tvNotice = (TextView) findViewById(R.id.tv_group_notice);
		tvNotice.setText(group.notice);
		
		clearAllHistory = (RelativeLayout) findViewById(R.id.clear_all_history);
		exitBtn = (Button) findViewById(R.id.btn_exit_grp);
		deleteBtn = (Button) findViewById(R.id.btn_exitdel_grp);

		rl_switch_block_groupmsg = (RelativeLayout) findViewById(R.id.rl_switch_block_groupmsg);

		iv_switch_block_groupmsg = (ImageView) findViewById(R.id.iv_switch_block_groupmsg);
		iv_switch_unblock_groupmsg = (ImageView) findViewById(R.id.iv_switch_unblock_groupmsg);

		rl_switch_block_groupmsg.setOnClickListener(this);

		Drawable referenceDrawable = getResources().getDrawable(R.drawable.smiley_add_btn);
		referenceWidth = referenceDrawable.getIntrinsicWidth();
		referenceHeight = referenceDrawable.getIntrinsicHeight();
		// 如果自己是群主，显示解散按钮
		if (type.equals("2")) {
			exitBtn.setVisibility(View.GONE);
			deleteBtn.setVisibility(View.VISIBLE);
		}else{
			exitBtn.setVisibility(View.GONE);
			deleteBtn.setVisibility(View.GONE);
		}
		
		
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

		clearAllHistory.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(GroupDetailsActivity.this);
				progressDialog.setMessage("正在添加...");
				progressDialog.setCanceledOnTouchOutside(false);
			}
			switch (requestCode) {
			case REQUEST_CODE_ADD_USER:// 添加群成员
				final ArrayList<FriendBean> friends = data.getParcelableArrayListExtra("newmembers");
				progressDialog.show();
				addMembersToGroup(friends);
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
	 * 
	 * @param groupId
	 */
	private void exitGrop() {
		new Thread(new Runnable() {
			public void run() {
				try {
					EMGroupManager.getInstance().exitFromGroup(hxgroupId);
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							setResult(RESULT_OK);
							finish();
							ChatActivity.activityInstance.finish();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
						}
					});
				}
			}
		}).start();
	}

	/**
	 * 解散群组
	 * 
	 * @param groupId
	 */
	private void deleteGrop() {
		new Thread(new Runnable() {
			public void run() {
				try {
					EMGroupManager.getInstance().exitAndDeleteGroup(hxgroupId);
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							setResult(RESULT_OK);
							finish();
							ChatActivity.activityInstance.finish();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							BToast.show(mActivity, "解散群聊失败");
						}
					});
				}
			}
		}).start();
	}

	/**
	 * 增加群成员
	 * 
	 * @param newmembers
	 */
	private void addMembersToGroup(final List<FriendBean> listFriends) {
		new Thread(new Runnable() {

			public void run() {
				try {
					
					String[] newmembers = new String[listFriends.size()];
					
					for (int i = 0; i < listFriends.size(); i++) {
						FriendBean friend = listFriends.get(i);
						newmembers[i] = "bg"+friend.userid;
					}
					
					// 创建者调用add方法
					if (!type.equals("0")) {	//非普通成员
						EMGroupManager.getInstance().addUsersToGroup(group.hxgroupid, newmembers);
					} else {
						// 一般成员调用invite方法
						EMGroupManager.getInstance().inviteUser(group.hxgroupid, newmembers, null);
					}
					runOnUiThread(new Runnable() {
						public void run() {
							friends.addAll(listFriends);
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
		case R.id.rl_switch_block_groupmsg: // 屏蔽群组
			if (iv_switch_block_groupmsg.getVisibility() == View.VISIBLE) {
				System.out.println("change to unblock group msg");
				try {
					EMGroupManager.getInstance().unblockGroupMessage(hxgroupId);
					iv_switch_block_groupmsg.setVisibility(View.INVISIBLE);
					iv_switch_unblock_groupmsg.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					e.printStackTrace();
					// todo: 显示错误给用户
				}
			} else {
				System.out.println("change to block group msg");
				try {
					EMGroupManager.getInstance().blockGroupMessage(hxgroupId);
					iv_switch_block_groupmsg.setVisibility(View.VISIBLE);
					iv_switch_unblock_groupmsg.setVisibility(View.INVISIBLE);
				} catch (Exception e) {
					e.printStackTrace();
					// todo: 显示错误给用户
				}
			}
			break;

		case R.id.clear_all_history: // 清空聊天记录
			Intent intent = new Intent(GroupDetailsActivity.this, AlertDialog.class);
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
		public View getView(final int position, View convertView, final ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(mActivity).inflate(R.layout.grid,null);
			}
			final Button button = (Button) convertView.findViewById(R.id.button_avatar);
			// 最后一个item，减人按钮
			if (position == mList.size()+2- 1) {
				button.setText("");
				// 设置成删除按钮
				button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.smiley_minus_btn, 0, 0);
				// 如果不是创建者或者没有相应权限，不提供加减人按钮
				if (type.equals("0")) {	//普通成员
					convertView.setVisibility(View.INVISIBLE);
				} else { // 显示删除按钮 非0 则是管理员，或群主
					if (isInDeleteMode) {
						// 正处于删除模式下，隐藏删除按钮
						convertView.setVisibility(View.INVISIBLE);
					} else {
						// 正常模式
						convertView.setVisibility(View.VISIBLE);
						convertView.findViewById(R.id.badge_delete).setVisibility(View.INVISIBLE);
					}
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							isInDeleteMode = true;
							notifyDataSetChanged();
						}
					});
				}
			} else if (position == mList.size()+2 - 2) { // 添加群组成员按钮
				button.setText("");
				button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.smiley_add_btn, 0, 0);
				// 如果不是创建者或者没有相应权限
				if (!type.equals("0")) {
					// if current user is not group admin, hide add/remove btn
					convertView.setVisibility(View.INVISIBLE);
				} else {
					// 正处于删除模式下,隐藏添加按钮
					if (isInDeleteMode) {
						convertView.setVisibility(View.INVISIBLE);
					} else {
						convertView.setVisibility(View.VISIBLE);
						convertView.findViewById(R.id.badge_delete).setVisibility(View.INVISIBLE);
					}
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// 进入选人页面
							startActivityForResult(
									(new Intent(GroupDetailsActivity.this, GroupPickContactsActivity.class).putExtra("hxgroupId", group.hxgroupid)),
									REQUEST_CODE_ADD_USER);
						}
					});
				}
			} else { // 普通item，显示群组成员
				
				final FriendBean friendBean = (FriendBean) mList.get(position);
				if(null == friendBean){
					return null;
				}
				
				final ImageView iv =  (ImageView) convertView.findViewById(R.id.iv_img);
				
				
				button.setText(friendBean.name);
				convertView.setVisibility(View.VISIBLE);
				button.setVisibility(View.VISIBLE);
				
				mImageLoader.displayImage(friendBean.photo,iv, options, null);
				
				//Drawable avatar = getResources().getDrawable(R.drawable.default_avatar);
				Drawable avatar = iv.getDrawable();
				avatar.setBounds(0, 0, referenceWidth, referenceHeight);
				button.setCompoundDrawables(null, avatar, null, null);
				// demo群组成员的头像都用默认头像，需由开发者自己去设置头像
				if (isInDeleteMode) {
					// 如果是删除模式下，显示减人图标
					convertView.findViewById(R.id.badge_delete).setVisibility(View.VISIBLE);
				} else {
					convertView.findViewById(R.id.badge_delete).setVisibility(View.INVISIBLE);
				}
				
				
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (isInDeleteMode) {
							// 如果是删除自己，return
							if (EMChatManager.getInstance().getCurrentUser().equals("bg"+friendBean.userid)) {
								BToast.show(mActivity, "不能删除自己");
								return;
							}
							if (!NetUtils.hasNetwork(getApplicationContext())) {
								BToast.show(mActivity, R.string.network_unavailable);
								return;
							}
							deleteMembersFromGroup("bg"+friendBean.userid);
						} else {
							 //正常情况下点击user，可以进入用户详情或者聊天页面等等
							 startActivity(new Intent(GroupDetailsActivity.this,ChatActivity.class).putExtra("userId",friendBean.userid));

						}
					}});
			}
			return convertView;
		}
	}

	/**
	 * 删除群成员
	 * @param username
	 */
	protected void deleteMembersFromGroup(final String userId) {
		final ProgressDialog deleteDialog = new ProgressDialog(GroupDetailsActivity.this);
		deleteDialog.setMessage("正在移除...");
		deleteDialog.setCanceledOnTouchOutside(false);
		deleteDialog.show();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// 删除被选中的成员
					EMGroupManager.getInstance().removeUserFromGroup(hxgroupId, userId);
					adapter.isInDeleteMode = false;
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							deleteDialog.dismiss();
							adapter.notifyDataSetChanged();
						}
					});
				} catch (final Exception e) {
					deleteDialog.dismiss();
					runOnUiThread(new Runnable() {
						public void run() {
							BToast.show(mActivity, "删除失败");
						}
					});
				}

			}
		}).start();
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
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
			switch (bNetWork.getMessageType()) {
			case 850013:
				FriendGroupBean fgb = JSON.parseObject(json, FriendGroupBean.class);
				friends.addAll(fgb.items);
				for(FriendBean f: friends){
					if(BGApp.mUserId.equals(f.userid)){
						type = f.type;
					}
				}
				sortMemberByType();
				initView();
				break;

			default:
				break;
			
			}
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
