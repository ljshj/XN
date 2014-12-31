package com.bgood.xn.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;

import com.bgood.xn.db.DBHelper;
import com.bgood.xn.utils.LogUtils;

/**
 * @todo:群成员
 * @date:2014-12-31 下午4:21:20
 * @author:hg_liuzl@163.com
 */
public class GroupMemberBean {
	
	public String groupid;
	public List<FriendBean> list;
	
	public GroupMemberBean(String groupid, List<FriendBean> list) {
		super();
		this.groupid = groupid;
		this.list = list;
	}
	

	/**
	 * @todo:从数据库查询群组
	 * @date:2014-12-22 上午10:25:18
	 * @author:hg_liuzl@163.com
	 * @params:@param mDBHelper
	 * @params:@return
	 */
	public static List<FriendBean> queryGroupMembersByGroupId(DBHelper mDBHelper,String groupId){
		   LogUtils.i("------from database----");
	       Cursor c = mDBHelper.queryAndAll(DBHelper.TB_GROUP_MEMBER,DBHelper.GroupMember.GM_GROUPID,groupId);
	       List<FriendBean>list = new ArrayList<FriendBean>();
	       do {
	    	   if(c!=null&&c.getCount()>0){
	    		   String userId = c.getString(c.getColumnIndex(DBHelper.Friend.F_USERID));
	    		   String type = c.getString(c.getColumnIndex(DBHelper.Friend.F_TYPE));
	    		   String singture = c.getString(c.getColumnIndex(DBHelper.Friend.F_SIGNATURE));
	    		   String sex = c.getString(c.getColumnIndex(DBHelper.Friend.F_SEX));
	    		   String photo = c.getString(c.getColumnIndex(DBHelper.Friend.F_PHOTO));
	    		   String name = c.getString(c.getColumnIndex(DBHelper.Friend.F_NAME));
	    		   String level = c.getString(c.getColumnIndex(DBHelper.Friend.F_LEVEL));

		           FriendBean friend = new FriendBean();
		           friend.userid = userId;
		           friend.type = type;
		           friend.signature = singture;
		           friend.sex = sex;
		           friend.photo = photo;
		           friend.name = name;
		           friend.level = level;
		           
	           list.add(friend);
	           }
		} while (c.moveToNext());
	       
	       if(null!=c)
				c.close();
	       
	     return list;
	}
	
	/**
	 * @todo:从数据库查询群组
	 * @date:2014-12-22 上午10:25:18
	 * @author:hg_liuzl@163.com
	 * @params:@param mDBHelper
	 * @params:@return
	 */
	public static Map<String,List<FriendBean>> queryGroupMembers(DBHelper mDBHelper){
		   LogUtils.i("------from database----");
	       Cursor c = mDBHelper.queryAll(DBHelper.TB_GROUP_MEMBER);
	       Map<String,List<FriendBean>> mapGroup = new HashMap<String,List<FriendBean>>();
	       List<FriendBean> listFriends = new ArrayList<FriendBean>();
	       do {
	    	   if(c!=null&&c.getCount()>0){
	    		   
	    		   	   String groupId = c.getString(c.getColumnIndex(DBHelper.GroupMember.GM_GROUPID));
		    		   String userId = c.getString(c.getColumnIndex(DBHelper.GroupMember.GM_USERID));
		    		   String type = c.getString(c.getColumnIndex(DBHelper.GroupMember.GM_TYPE));
		    		   String singture = c.getString(c.getColumnIndex(DBHelper.GroupMember.GM_SIGNATURE));
		    		   String sex = c.getString(c.getColumnIndex(DBHelper.GroupMember.GM_SEX));
		    		   String photo = c.getString(c.getColumnIndex(DBHelper.GroupMember.GM_PHOTO));
		    		   String name = c.getString(c.getColumnIndex(DBHelper.GroupMember.GM_NAME));
		    		   String level = c.getString(c.getColumnIndex(DBHelper.GroupMember.GM_LEVEL));

			           FriendBean friend = new FriendBean();
			           friend.groupid = groupId;
			           friend.userid = userId;
			           friend.type = type;
			           friend.signature = singture;
			           friend.sex = sex;
			           friend.photo = photo;
			           friend.name = name;
			           friend.level = level;
			           listFriends.add(friend);
		           
	           }
		} while (c.moveToNext());
	       
	       if(null!=c)
				c.close();
	       
	       
	       for(FriendBean fb:listFriends){
	    	   String groupId = fb.groupid;
	    	   if(mapGroup.containsKey(groupId))
	    	   {
	    		   
	    	   }else{
	    		   
	    	   }
	       }
	       
	       
	     return mapGroup;
	}
	
	/**
	 * 
	 * @todo:批量插入群成员
	 * @date:2014-12-22 上午10:36:29
	 * @author:hg_liuzl@163.com
	 * @params:
	 */
	public static void storeGroupMemberBean(DBHelper dbHelper,String groupId,List<FriendBean> lists) {
			ArrayList<ContentValues> list = new ArrayList<ContentValues>();
			for(FriendBean f:lists){
				ContentValues values = new ContentValues();
				values.put(DBHelper.GroupMember.GM_GROUPID, groupId);
				values.put(DBHelper.GroupMember.GM_USERID, f.userid);
				values.put(DBHelper.GroupMember.GM_TYPE, f.type);
				values.put(DBHelper.GroupMember.GM_SIGNATURE, f.signature);
				values.put(DBHelper.GroupMember.GM_SEX, f.sex);
				values.put(DBHelper.GroupMember.GM_PHOTO, f.photo);
				values.put(DBHelper.GroupMember.GM_NAME, f.name);
				values.put(DBHelper.GroupMember.GM_LEVEL, f.level);
				list.add(values);
			}
			int count = dbHelper.insert(DBHelper.TB_GROUP_MEMBER, list);
			LogUtils.i("-----------批量插入群----"+count);
	}
	
	
	/**
	 * 
	 * @todo:插入一个群成员
	 * @date:2014-12-22 上午10:55:07
	 * @author:hg_liuzl@163.com
	 * @params:@param dbHelper
	 * @params:@param FriendBean
	 */
	public static void insertFriendBean(DBHelper dbHelper,String groupId,FriendBean f) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.GroupMember.GM_GROUPID, groupId);
		values.put(DBHelper.GroupMember.GM_USERID, f.userid);
		values.put(DBHelper.GroupMember.GM_TYPE, f.type);
		values.put(DBHelper.GroupMember.GM_SIGNATURE, f.signature);
		values.put(DBHelper.GroupMember.GM_SEX, f.sex);
		values.put(DBHelper.GroupMember.GM_PHOTO, f.photo);
		values.put(DBHelper.GroupMember.GM_NAME, f.name);
		values.put(DBHelper.GroupMember.GM_LEVEL, f.level);
		long count = dbHelper.insert(DBHelper.TB_GROUP_MEMBER, values);
		LogUtils.i("--------插入群成员-------"+count);
		
	}
	
	/**
	 * 
	 * @todo:删除一个群成员
	 * @date:2014-12-22 上午10:55:51
	 * @author:hg_liuzl@163.com
	 * @params:@param dbHelper
	 * @params:@param FriendBean
	 */
	public static void deleteGroupMemberBean(DBHelper dbHelper,String groupId,String userid) {
		int count = dbHelper.deleteAll(DBHelper.TB_GROUP_MEMBER, new String[]{DBHelper.GroupMember.GM_GROUPID,DBHelper.GroupMember.GM_USERID}, new String[]{groupId,userid});
		LogUtils.i("--删除-----"+count+"个群成员");
	}
}
