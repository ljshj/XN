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
package com.easemob.chat.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.bean.FriendBean;
import com.bgood.xn.bean.FriendGroupBean;
import com.bgood.xn.bean.GroupBean;
import com.bgood.xn.bean.GroupMemberBean;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.db.DBHelper;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.IMRequest;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.view.BToast;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.db.InviteMessgeDao;
import com.easemob.chat.domain.InviteMessage;
import com.easemob.chat.domain.InviteMessage.InviteMesageStatus;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NewFriendsMsgAdapter extends ArrayAdapter<InviteMessage> implements TaskListenerWithState {

	private Context context;
	private InviteMessgeDao messgeDao;
	private DBHelper dbHelper;
	private InviteMessage actionInviteMessage;

	public NewFriendsMsgAdapter(Context context, int textViewResourceId, List<InviteMessage> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.dbHelper = new DBHelper(context);
		messgeDao = new InviteMessgeDao(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.row_invite_msg, null);
			holder.avator = (ImageView) convertView.findViewById(R.id.avatar);
			holder.reason = (TextView) convertView.findViewById(R.id.message);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.status = (Button) convertView.findViewById(R.id.user_state);
			holder.refuse = (Button) convertView.findViewById(R.id.user_refuse);
			holder.groupContainer = (LinearLayout) convertView.findViewById(R.id.ll_group);
			holder.groupname = (TextView) convertView.findViewById(R.id.tv_groupName);
			// holder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final InviteMessage msg = getItem(position);
		actionInviteMessage = msg;
	
		ImageLoader.getInstance().displayImage(msg.getUserPhotoUrl(),holder.avator);
		
		
		if (msg != null) {
			if(msg.getHxgroupId() != null){ // 显示群聊提示
				holder.groupContainer.setVisibility(View.VISIBLE);
				holder.groupname.setText(msg.getGroupName());
			} else{
				holder.groupContainer.setVisibility(View.GONE);
			}
			
			holder.reason.setText(msg.getReason());
			holder.name.setText(msg.getUserNick());
			// holder.time.setText(DateUtils.getTimestampString(new
			// Date(msg.getTime())));
			if (msg.getStatus() == InviteMesageStatus.BEAGREED) {
				holder.status.setVisibility(View.INVISIBLE);
				holder.reason.setText("已同意你的好友请求");
			} else if (msg.getStatus() == InviteMesageStatus.BEINVITEED || msg.getStatus() == InviteMesageStatus.BEAPPLYED) {
				holder.status.setVisibility(View.VISIBLE);
				holder.status.setText("同意");
				if(msg.getStatus() == InviteMesageStatus.BEINVITEED){
					if (msg.getReason() == null) {
						// 如果没写理由
						holder.reason.setText("请求加你为好友");
					}
				}else{ //入群申请
					if (TextUtils.isEmpty(msg.getReason())) {
						holder.reason.setText("申请加入群：" + msg.getGroupName());
					}
				}
				// 设置点击事件
				holder.status.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 同意别人发的好友请求
						acceptInvitation(holder.status, msg);
					}
				});
			}else if (msg.getStatus() == InviteMesageStatus.BEINVITEED || msg.getStatus() == InviteMesageStatus.BEAPPLYED) {
				holder.refuse.setVisibility(View.VISIBLE);
				holder.refuse.setText("拒绝");
				if(msg.getStatus() == InviteMesageStatus.BEINVITEED){
					if (msg.getReason() == null) {
						// 如果没写理由
						holder.reason.setText("请求加你为好友");
					}
				}else{ //入群申请
					if (TextUtils.isEmpty(msg.getReason())) {
						holder.reason.setText("申请加入群：" + msg.getGroupName());
					}
				}
				// 设置点击事件
				holder.refuse.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 拒绝别人发的好友请求
						refuseInvitation(holder.refuse, msg);
					}
				});
			} else if (msg.getStatus() == InviteMesageStatus.AGREED) {
				holder.status.setText("已同意");
				holder.status.setBackgroundDrawable(null);
				holder.status.setEnabled(false);
			} else if(msg.getStatus() == InviteMesageStatus.REFUSED){
				holder.status.setText("已拒绝");
				holder.status.setBackgroundDrawable(null);
				holder.status.setEnabled(false);
			}

			// 设置用户头像
		}

		return convertView;
	}

	/**
	 * 同意好友请求或者群申请
	 * 
	 * @param button
	 * @param username
	 */
	private void acceptInvitation(final Button button, final InviteMessage msg) {
		final ProgressDialog pd = new ProgressDialog(context);
		pd.setMessage("正在同意...");
		pd.setCanceledOnTouchOutside(false);
		pd.show();

		new Thread(new Runnable() {
			public void run() {
				// 调用sdk的同意方法
				try {
					if(msg.getHxgroupId() == null) //同意好友请求
					{
						EMChatManager.getInstance().acceptInvitation(msg.getFrom());
						
						IMRequest.getInstance().requestFriendADD(NewFriendsMsgAdapter.this, context, BGApp.mUserId, new String[]{msg.getFrom().substring(2)});
					}
					else //同意加群申请
					{
						EMGroupManager.getInstance().acceptApplication(msg.getFrom(), msg.getHxgroupId());
					}
					((Activity) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							pd.dismiss();
							button.setText("已同意");
							msg.setStatus(InviteMesageStatus.AGREED);
							// 更新db
							ContentValues values = new ContentValues();
							values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
							messgeDao.updateMessage(msg.getId(), values);
							button.setBackgroundDrawable(null);
							button.setEnabled(false);

							if(null!=msg.getHxgroupId()){
								/**向这个群添加成员*/
								IMRequest.getInstance().requestGroupMemberJoinOrInvite(NewFriendsMsgAdapter.this, context, new String[]{msg.getFrom().substring(2)}, msg.getGroupId());
							}
						}
					});
				} catch (final Exception e) {
					((Activity) context).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							Toast.makeText(context, "同意失败: " + e.getMessage(), 1).show();
						}
					});

				}
			}
		}).start();
	}
	
	

	/**
	 * 拒绝好友请求或者群申请
	 * 
	 * @param button
	 * @param username
	 */
	private void refuseInvitation(final Button button, final InviteMessage msg) {
		final ProgressDialog pd = new ProgressDialog(context);
		pd.setMessage("正在拒绝...");
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		
		new Thread(new Runnable() {
			public void run() {
				// 调用sdk的拒绝方法
				try {
					if(msg.getHxgroupId() == null) //拒绝好友请求
						EMChatManager.getInstance().refuseInvitation(msg.getFrom());
					else //同意加群申请
					{
						
					}
					//	EMGroupManager.getInstance().acceptApplication(msg.getFrom(), msg.getGroupId());
					
					((Activity) context).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							button.setText("已拒绝");
							msg.setStatus(InviteMesageStatus.REFUSED);
//							// 更新db
//							ContentValues values = new ContentValues();
//							values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
//							messgeDao.updateMessage(msg.getId(), values);
							button.setBackgroundDrawable(null);
							button.setEnabled(false);

						}
					});
				} catch (final Exception e) {
					((Activity) context).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							Toast.makeText(context, "拒绝失败: " + e.getMessage(), 1).show();
						}
					});

				}
			}
		}).start();
	}

	private static class ViewHolder {
		ImageView avator;
		TextView name;
		TextView reason;
		Button status,refuse;
		LinearLayout groupContainer;
		TextView groupname;
		// TextView time;
	}

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if (info.getState() == HttpTaskState.STATE_OK) {
			BaseNetWork bNetWork = info.getmBaseNetWork();
			String json = bNetWork.getStrJson();
			switch (bNetWork.getMessageType()) {
			case 820001:
				if (bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK) {
					/**获取用户资料*/
					UserInfoBean user = JSON.parseObject(json, UserInfoBean.class);
					FriendBean fb = FriendBean.copyUserInfo(user);
					GroupMemberBean.insertFriendBean(dbHelper, actionInviteMessage.getHxgroupId(),actionInviteMessage.getGroupId(),fb);
					
					List<FriendBean> storeList = BGApp.getInstance().getGroupMemberAndHxId().get(actionInviteMessage.getHxgroupId());
					storeList.add(fb);
					BGApp.getInstance().getGroupMemberAndHxId().put(actionInviteMessage.getHxgroupId(), storeList);	
				}else{
					
				}
				break;
			case 850012: // 获取群资料
					if (bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK) {
						//把这个群插入到本地的数据库中
						GroupBean group = JSON.parseObject(json, GroupBean.class);
						GroupBean.insertGroupBean(dbHelper, group);
						BGApp.getInstance().getGroupAndHxId().put(group.hxgroupid, group);
						/**获取接受者的资料*/
						UserCenterRequest.getInstance().requestPersonInfo(NewFriendsMsgAdapter.this, context, actionInviteMessage.getFrom().substring(2),false);
						
					} else {
	
					}
				break;
			case 850025:	//加群成功
				if (bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK) {
					/**获取申请者的资料*/
					UserCenterRequest.getInstance().requestPersonInfo(NewFriendsMsgAdapter.this, context, actionInviteMessage.getFrom().substring(2),false);

//					//加群成功后，再获取这个群的资料
//					IMRequest.getInstance().requestGroupInfo(NewFriendsMsgAdapter.this, context, "0", actionInviteMessage.getHxgroupId(),false);	//获取该群的资料
				} else {
					BToast.show(context, "同意请求失败");
				}
				break;
				
			case 850027://添加好友的接口，返回的好友数据
				if (bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK) {
					/**添加好友成功后*/
					FriendGroupBean fgb = JSON.parseObject(json, FriendGroupBean.class);
					FriendBean.storeFriendBean(dbHelper, fgb.items);
					
					/**更新缓存里的好友列表*/
					List<FriendBean> listFriends = fgb.items;
					if(null == listFriends){
						return;
					}
					for (FriendBean friendBean : listFriends) {
						FriendBean.setUserHearder(friendBean.name, friendBean);
						BGApp.getInstance().getFriendMapById().put(friendBean.userid, friendBean);
					}
					
				} else {
					BToast.show(context, "同意请求失败");
				}
				
			break;

				default:
					break;
				
			}
		}
	}
}
