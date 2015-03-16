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
package com.easemob.chat.activity;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.bgood.xn.R;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.widget.TitleBar;
import com.easemob.chat.Constant;
import com.easemob.chat.adapter.NewFriendsMsgAdapter;
import com.easemob.chat.db.InviteMessgeDao;
import com.easemob.chat.domain.InviteMessage;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @todo:申请与通知
 * @date:2014-12-20 上午10:10:21
 * @author:hg_liuzl@163.com
 */
public class NewFriendsMsgActivity extends BaseActivity {
	private ListView listView;

	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_friends_msg);
		(new TitleBar(this)).initTitleBar("申请与通知");

		listView = (ListView) findViewById(R.id.list);
		InviteMessgeDao dao = new InviteMessgeDao(this);
		List<InviteMessage> msgs = dao.getMessagesList();
		//设置adapter
		NewFriendsMsgAdapter adapter = new NewFriendsMsgAdapter(this, 1, msgs); 
		listView.setAdapter(adapter);
		BGApp.getInstance().getFriendMapById().get(Constant.NEW_FRIENDS_USERNAME).setUnreadMsgCount(0);
		
	}

	public void back(View view) {
		finish();
	}
	
	
}
