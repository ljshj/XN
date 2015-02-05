package com.bgood.xn.ui.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.bean.FriendBean;
import com.bgood.xn.bean.FriendGroupBean;
import com.bgood.xn.bean.GroupBean;
import com.bgood.xn.bean.GroupMemberBean;
import com.bgood.xn.bean.response.FriendAndGroupResponse;
import com.bgood.xn.db.DBHelper;
import com.bgood.xn.db.PreferenceUtil;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.request.IMRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.utils.LogUtils;
import com.easemob.chat.Constant;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * @todo:同步数据，主要是同步联系人列表，群组，以及，交流厅数据
 * @date:2015-1-31 上午11:25:39
 * @author:hg_liuzl@163.com
 */
public class SyncIMDataService extends Service implements TaskListenerWithState {

	public static final String MY_SERVICE = "com.bgood.xn.ui.message.SYNCDATA";//服务指定的动作
	
	public DBHelper dbHelper = null;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		dbHelper = new DBHelper(this);
		LogUtils.i("----------------------开始同步数据----------------------------");
		IMRequest.getInstance().requestContactsList(this,this,false);
		
	}
	private String groupId = null;
	private String hxGroupId = null;
	private List<GroupBean> listGroupBean = null;
	private int count =-1;
	
	private void doSearchGroupMember(){
		if(null != listGroupBean && listGroupBean.size()>0 && (count-1>=0)){
			final GroupBean group = listGroupBean.get(--count);
			hxGroupId = group.hxgroupid;
			groupId = group.roomid;
			IMRequest.getInstance().requestGroupMembers(this,this, groupId,false);
		}else{
			dealIMData();   //数据加载完后，获取内存中的数据
		}
	}
	
	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			String json = bNetWork.getStrJson();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				switch (bNetWork.getMessageType()) {
				case 850006:	
						/**获取好友列表*/
						FriendAndGroupResponse response = JSON.parseObject(json, FriendAndGroupResponse.class);
						if(response.groups.size() > 0 && response.groups.get(0).items.size() > 0){
							dbHelper.deleteAll(DBHelper.TB_FRIEND);
							FriendBean.storeFriendBean(dbHelper, response.groups.get(0).items);
						}
						listGroupBean = response.fixrooms;
						dbHelper.deleteAll(DBHelper.TB_GROUP);
						dbHelper.deleteAll(DBHelper.TB_GROUP_MEMBER);
						GroupBean.storeGroupBean(dbHelper, listGroupBean);
						count = listGroupBean.size();  
						doSearchGroupMember();
						break;
				case 850013:/**获取群成员并插入数据库*/
					FriendGroupBean groupMember = JSON.parseObject(json, FriendGroupBean.class);
					GroupMemberBean.storeGroupMemberBean(dbHelper, hxGroupId,groupId, groupMember.items);
					doSearchGroupMember();
					break;
					}
				}
			}
		}
	
	private void dealIMData() {
		List<FriendBean> friends = FriendBean.queryFriendBean(dbHelper);
		
		Map<String, FriendBean> userAndIdMap = new HashMap<String, FriendBean>();
		
		for (FriendBean fb : friends) {
			FriendBean.setUserHearder(fb.name, fb);
			userAndIdMap.put(fb.userid, fb);
		}
		
		// 添加user"申请与通知"
		FriendBean newFriends = new FriendBean();
		newFriends.setName(Constant.NEW_FRIENDS_USERNAME);
		newFriends.setNick("申请与通知");
		newFriends.setHeader("");
		userAndIdMap.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
		
		// 添加小秘书
		FriendBean adminFriends = new FriendBean();
		adminFriends.setName(Constant.FRIEND_ADMIN);
		adminFriends.setNick("炫能小秘书");
		adminFriends.setHeader("");
		userAndIdMap.put(Constant.FRIEND_ADMIN_ID, adminFriends);
		
		// 好友存入内存
		BGApp.getInstance().setFriendMapById(userAndIdMap);
		//群存入内存中
		BGApp.getInstance().setGroupAndHxId(GroupBean.queryGroupBeanByType(dbHelper));
		BGApp.getInstance().setGroupMemberAndHxId(GroupMemberBean.queryGroupMembersAndHXGroupId(dbHelper));
		stopSelf();
		LogUtils.i("----------------------数据同步完成----------------------------");
	}
}
