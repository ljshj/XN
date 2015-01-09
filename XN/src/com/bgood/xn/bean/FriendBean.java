package com.bgood.xn.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.bgood.xn.db.DBHelper;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.utils.LogUtils;
import com.easemob.chat.Constant;
import com.easemob.util.HanziToPinyin;

/**
 * @todo:好友类
 * @date:2014-12-19 上午11:40:27
 * @author:hg_liuzl@163.com
 */
public class FriendBean implements Parcelable {
	public String hxgroupid;
	public String groupid;
	public String userid;
	public String type;  //0 普通成员，1，管理员，2，群主
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
		fb.name = TextUtils.isEmpty(user.nickn)?user.username:user.nickn;
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
	       Cursor c = mDBHelper.queryAndAll(DBHelper.TB_FRIEND,DBHelper.CLOUMN_CURRENT_USER_ID,BGApp.mUserId);
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
		   Cursor c = mDBHelper.queryAndAll(DBHelper.TB_FRIEND,DBHelper.CLOUMN_CURRENT_USER_ID,BGApp.mUserId);
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
				values.put(DBHelper.CLOUMN_CURRENT_USER_ID, BGApp.mUserId);
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
		values.put(DBHelper.CLOUMN_CURRENT_USER_ID, BGApp.mUserId);
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
		int count = dbHelper.deleteAll(DBHelper.TB_FRIEND, new String[]{DBHelper.CLOUMN_CURRENT_USER_ID,DBHelper.Friend.F_USERID},new String[]{BGApp.mUserId,userid});
		LogUtils.i("-------------删除数据------------"+count);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	
	public static final Parcelable.Creator<FriendBean> CREATOR = new Creator<FriendBean>() {  
	    public FriendBean createFromParcel(Parcel source) {  
	    	FriendBean friend = new FriendBean();  
	    	
	    	friend.hxgroupid = source.readString();  
	    	friend.groupid = source.readString();  
	    	friend.userid = source.readString();  
	    	friend.type = source.readString();  
	    	friend.name = source.readString();  
	    	friend.sex = source.readString();  
	    	friend.level = source.readString();  
	    	friend.photo = source.readString();  
	    	friend.signature = source.readString();  
	    	friend.unreadMsgCount = source.readInt();  
	    	friend.header = source.readString();  
	    	friend.nick = source.readString();  
	        return friend;  
	    }  
	    public FriendBean[] newArray(int size) {  
	        return new FriendBean[size];  
	    }  
	}; 
	
	@Override
	public void writeToParcel(Parcel parcel, int arg1) {
		
		parcel.writeString(hxgroupid);
		parcel.writeString(groupid);
		parcel.writeString(userid);
		parcel.writeString(type);
		parcel.writeString(name);
		parcel.writeString(sex);
		parcel.writeString(level);
		parcel.writeString(photo);
		parcel.writeString(signature);
		parcel.writeInt(unreadMsgCount);
		parcel.writeString(header);
		parcel.writeString(nick);
		
	}
	
	/**
	 * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
	 * @param username
	 * @param user
	 */
	public static void setUserHearder(String name, FriendBean friend) {
		String headerName = null;
		if (!TextUtils.isEmpty(friend.getNick())) {
			headerName = friend.getNick();
		} else {
			headerName = friend.getName();
		}
		
		if(headerName== null)
		{
			return;
		}
		
		
		if (Constant.NEW_FRIENDS_USERNAME.equals(name)) {
			friend.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			friend.setHeader("#");
		} else {
			friend.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1).toUpperCase());
			char header = friend.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				friend.setHeader("#");
			}
		}
	}
}
