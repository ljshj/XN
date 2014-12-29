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

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.bean.UserInfoBean;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.db.InviteMessgeDao;
import com.easemob.chat.domain.InviteMessage;
import com.easemob.chat.domain.InviteMessage.InviteMesageStatus;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class NewFriendsMsgAdapter extends ArrayAdapter<InviteMessage> {

	private Context context;
	private InviteMessgeDao messgeDao;
	
	public ImageLoader mImageLoader;
	public DisplayImageOptions options;

	public NewFriendsMsgAdapter(Context context, int textViewResourceId, List<InviteMessage> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		messgeDao = new InviteMessgeDao(context);
		
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.icon_default)
		.showImageForEmptyUri(R.drawable.icon_default)
		.cacheInMemory()
		.cacheOnDisc()
		.build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
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
		mImageLoader.displayImage(msg.getUserPhotoUrl(),holder.avator, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete() {
				Animation anim = AnimationUtils.loadAnimation(context, R.anim.fade_in);
				holder.avator.setAnimation(anim);
				anim.start();
			}
		});
		
		
		if (msg != null) {
			if(msg.getGroupId() != null){ // 显示群聊提示
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
					if(msg.getGroupId() == null) //同意好友请求
						EMChatManager.getInstance().acceptInvitation(msg.getFrom());
					else //同意加群申请
						EMGroupManager.getInstance().acceptApplication(msg.getFrom(), msg.getGroupId());
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
					if(msg.getGroupId() == null) //拒绝好友请求
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

}
