package com.bgood.xn.ui.message;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.bean.FriendBean;
import com.bgood.xn.bean.GroupBean;
import com.bgood.xn.bean.GroupMemberBean;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.IMRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.ui.message.fragment.GroupFragment;
import com.bgood.xn.view.BToast;
import com.bgood.xn.widget.TitleBar;

/**
 * @todo:创建群
 * @date:2014-12-17 下午4:29:45
 * @author:hg_liuzl@163.com
 */
public class CreateGroupActivity extends BaseActivity implements TaskListenerWithState {
	private EditText etGroupName,etGroupIntro,etGroupNotice;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_create_group);
		initView();
	}
	
	private void initView() {
		(new TitleBar(mActivity)).initTitleBar("创建群");
		etGroupName = (EditText) findViewById(R.id.et_group_name);
		etGroupIntro = (EditText) findViewById(R.id.et_group_intro);
		etGroupNotice = (EditText) findViewById(R.id.et_group_notice);
		findViewById(R.id.btn_commit).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				doCommit();
			}
		});
	}
	
	private void doCommit() {
		String groupName = etGroupName.getText().toString().trim();
		String groupIntro = etGroupIntro.getText().toString().trim();
		String groupNotice = etGroupNotice.getText().toString().trim();
		
		if(TextUtils.isEmpty(groupName)){
			BToast.show(mActivity, "请输入群名称");
			return;
		}else if(TextUtils.isEmpty(groupIntro)){
			BToast.show(mActivity, "请输入群简介");
			return;
		}else if(TextUtils.isEmpty(groupNotice)){
			BToast.show(mActivity, "请输入群公告");
			return;
		}else{
			IMRequest.getInstance().requestGroupCreate(CreateGroupActivity.this, CreateGroupActivity.this, groupName, 0, groupIntro,groupNotice);
		}
	}

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			String json = bNetWork.getStrJson();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				GroupBean group = JSON.parseObject(json, GroupBean.class);
				
				//插入群到数据库中
				GroupBean.insertGroupBean(dbHelper,group);
				BGApp.getInstance().getGroupAndHxId().put(group.hxgroupid, group);
				
				//插入群成员到数据库中
				FriendBean fb = FriendBean.copyUserInfo(BGApp.mUserBean);
				GroupMemberBean.insertFriendBean(dbHelper, group.hxgroupid,group.roomid,fb);
				
				//向内存中插入变更的数据
				List<FriendBean> list = new ArrayList<FriendBean>();
				list.add(fb);
				BGApp.getInstance().getGroupMemberAndHxId().put(group.hxgroupid, list);	
				
				GroupFragment.instance.refresh();	//刷新一下数据
				BToast.show(mActivity, "群创建成功");
				finish();
			}
		}
	}
}
