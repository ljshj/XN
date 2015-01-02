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
 * @todo:好友类
 * @date:2014-12-19 上午11:40:27
 * @author:hg_liuzl@163.com
 */
public class FriendBean {
	public String groupid;
	public String userid;
	public String type;
	public String name;
	public String sex;
	public String level;
	public String photo;
	public String signature;
	
	private int unreadMsgCount;
	private String header;
	private String nick;

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public int getUnreadMsgCount() {
		return unreadMsgCount;
	}

	public void setUnreadMsgCount(int unreadMsgCount) {
		this.unreadMsgCount = unreadMsgCount;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	/**复制FriendBean*/
	public static FriendBean copyUserInfo(final UserInfoBean user) {
		FriendBean fb = new FriendBean();
		fb.userid = user.userid;
		fb.type = "0";
		fb.name = user.username;
		fb.sex = String.valueOf(user.sex);
		fb.level = String.valueOf(user.level);
		fb.photo = user.photo;
		fb.signature = user.signature;
		return fb;
	}
	
	
	/**
	 * @todo:从数据库查询群组
	 * @date:2014-12-22 上午10:25:18
	 * @author:hg_liuzl@163.com
	 * @params:@param mDBHelper
	 * @params:@return
	 */
	public static List<FriendBean> queryFriendBean(DBHelper mDBHelper){
		   LogUtils.i("------from database----");
	       Cursor c = mDBHelper.queryAll(DBHelper.TB_FRIEND);
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
	public static Map<String,FriendBean> queryFriendBeanAndID(DBHelper mDBHelper){
		   LogUtils.i("------from database----");
	       Cursor c = mDBHelper.queryAll(DBHelper.TB_FRIEND);
	       Map<String,FriendBean>list = new HashMap<String,FriendBean>();
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
		           
		           list.put(userId, friend);
	           }
		} while (c.moveToNext());
	       
	       if(null!=c)
				c.close();
	       
	     return list;
	}
	
	/**
	 * 
	 * @todo:将数据批量存储到数据库
	 * @date:2014-12-22 上午10:36:29
	 * @author:hg_liuzl@163.com
	 * @params:
	 */
	public static void storeFriendBean(DBHelper dbHelper,List<FriendBean> listFriend) {
			if(null==listFriend)
				return;
			
			ArrayList<ContentValues> list = new ArrayList<ContentValues>();
			for(FriendBean f:listFriend){
				ContentValues values = new ContentValues();
				values.put(DBHelper.Friend.F_USERID, f.userid);
				values.put(DBHelper.Friend.F_TYPE, f.type);
				values.put(DBHelper.Friend.F_SIGNATURE, f.signature);
				values.put(DBHelper.Friend.F_SEX, f.sex);
				values.put(DBHelper.Friend.F_PHOTO, f.photo);
				values.put(DBHelper.Friend.F_NAME, f.name);
				values.put(DBHelper.Friend.F_LEVEL, f.level);
				list.add(values);
			}
			int count = dbHelper.insert(DBHelper.TB_FRIEND, list);
			LogUtils.i("-----------批量插入群----"+count);
	}
	
	
	/**
	 * 
	 * @todo:插入一条数据
	 * @date:2014-12-22 上午10:55:07
	 * @author:hg_liuzl@163.com
	 * @params:@param dbHelper
	 * @params:@param FriendBean
	 */
	public static void insertFriendBean(DBHelper dbHelper,FriendBean f) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.Friend.F_USERID, f.userid);
		values.put(DBHelper.Friend.F_TYPE, f.type);
		values.put(DBHelper.Friend.F_SIGNATURE, f.signature);
		values.put(DBHelper.Friend.F_SEX, f.sex);
		values.put(DBHelper.Friend.F_PHOTO, f.photo);
		values.put(DBHelper.Friend.F_NAME, f.name);
		values.put(DBHelper.Friend.F_LEVEL, f.level);
		long count = dbHelper.insert(DBHelper.TB_FRIEND, values);
		LogUtils.i("--------插入数据-------"+count);
		
	}
	
	/**
	 * 
	 * @todo:删除一条数据
	 * @date:2014-12-22 上午10:55:51
	 * @author:hg_liuzl@163.com
	 * @params:@param dbHelper
	 * @params:@param FriendBean
	 */
	public static void deleteFriendBean(DBHelper dbHelper,String userid) {
		int count = dbHelper.deleteAll(DBHelper.TB_FRIEND, DBHelper.Friend.F_USERID, userid);
		LogUtils.i("-------------删除数据------------"+count);
	}
}
