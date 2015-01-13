package com.bgood.xn.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;

import com.bgood.xn.db.DBHelper;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.utils.LogUtils;
/**
 * @todo:群组实体类
 * @date:2014-12-18 下午6:40:09
 * @author:hg_liuzl@163.com
 */
public class GroupBean implements Serializable
{
	public static final String BEAN_GROUP = "bean_group";
	
	/**
	 * @todo:TODO
	 * @date:2014-12-18 下午6:40:03
	 * @author:hg_liuzl@163.com
	 */
	private static final long serialVersionUID = 1L;
	public String hxgroupid;
	public String roomid;
	public String name;
	public String intro;
	public String photo;
	public String notice;
	public String grouptype = "0";//群类型 0，固定群；1，临时群
	public int membercount;
	

	
	
	
	/**
	 * @todo:从数据库查询群组
	 * @date:2014-12-22 上午10:25:18
	 * @author:hg_liuzl@163.com
	 * @params:@param mDBHelper
	 * @params:@return
	 */
	public static List<GroupBean> queryGroupBeanByGroupName(DBHelper mDBHelper,int type,String groupName){
		   LogUtils.i("------from database----");
	       Cursor c = mDBHelper.queryAndAllAndLike(DBHelper.TB_GROUP, new String[]{DBHelper.CLOUMN_CURRENT_USER_ID,DBHelper.Group.G_GROUPTYPE,DBHelper.Group.G_NAME}, new String[]{String.valueOf(type),groupName},new String[]{BGApp.mUserId,"",DBHelper.Group.G_NAME},null);
	       List<GroupBean> map = new ArrayList<GroupBean>();
	       c.moveToFirst();
	       do {
	    	   if(c!=null&&c.getCount()>0){
	    		   String roomid = c.getString(c.getColumnIndex(DBHelper.Group.G_ROOMID));
	    		   String hx_groupid = c.getString(c.getColumnIndex(DBHelper.Group.G_HX_GROUPID));
	    		   String photo = c.getString(c.getColumnIndex(DBHelper.Group.G_PHOTO));
	    		   String name = c.getString(c.getColumnIndex(DBHelper.Group.G_NAME));
	    		   String notice = c.getString(c.getColumnIndex(DBHelper.Group.G_NOTICE));
	    		   String intro = c.getString(c.getColumnIndex(DBHelper.Group.G_INTRO));
	    		   String groupType = c.getString(c.getColumnIndex(DBHelper.Group.G_GROUPTYPE));

		           GroupBean group = new GroupBean();
		           group.roomid = roomid;
		           group.hxgroupid = hx_groupid;
		           group.photo = photo;
		           group.name = name;
		           group.intro = intro;
		           group.notice = notice;
		           group.grouptype = groupType;
		           map.add(group);
	          }
		} while (c.moveToNext());
	       
	       if(null!=c)
				c.close();
	       
	     return map;
	}
	
	
	/**
	 * @todo:从数据库查询群组
	 * @date:2014-12-22 上午10:25:18
	 * @author:hg_liuzl@163.com
	 * @params:@param mDBHelper
	 * @params:@return
	 */
	public static Map<String,GroupBean> queryGroupBeanByType(DBHelper mDBHelper,int type){
		   LogUtils.i("------from database----");
	       Cursor c = mDBHelper.queryAndAll(DBHelper.TB_GROUP, new String[]{DBHelper.CLOUMN_CURRENT_USER_ID,DBHelper.Group.G_GROUPTYPE}, new String[]{BGApp.mUserId,String.valueOf(type)});
	       Map<String,GroupBean> map = new HashMap<String,GroupBean>();
	       c.moveToFirst();
	       do {
	    	   if(c!=null&&c.getCount()>0){
	    		   String roomid = c.getString(c.getColumnIndex(DBHelper.Group.G_ROOMID));
	    		   String hx_groupid = c.getString(c.getColumnIndex(DBHelper.Group.G_HX_GROUPID));
	    		   String photo = c.getString(c.getColumnIndex(DBHelper.Group.G_PHOTO));
	    		   String name = c.getString(c.getColumnIndex(DBHelper.Group.G_NAME));
	    		   String notice = c.getString(c.getColumnIndex(DBHelper.Group.G_NOTICE));
	    		   String intro = c.getString(c.getColumnIndex(DBHelper.Group.G_INTRO));
	    		   String groupType = c.getString(c.getColumnIndex(DBHelper.Group.G_GROUPTYPE));

	           GroupBean group = new GroupBean();
	           group.roomid = roomid;
	           group.hxgroupid = hx_groupid;
	           group.photo = photo;
	           group.name = name;
	           group.intro = intro;
	           group.notice = notice;
	           group.grouptype = groupType;
	           map.put(hx_groupid, group);
	          }
		} while (c.moveToNext());
	       
	       if(null!=c)
				c.close();
	       
	     return map;
	}
	/**
	 * 
	 * @todo:将数据批量存储到数据库
	 * @date:2014-12-22 上午10:36:29
	 * @author:hg_liuzl@163.com
	 * @params:
	 */
	public static void storeGroupBean(DBHelper dbHelper,List<GroupBean> listGroup) {
		if(null == listGroup){
			return;
		}
			ArrayList<ContentValues> list = new ArrayList<ContentValues>();
			for(GroupBean g:listGroup){
			
				ContentValues values = new ContentValues();
				values.put(DBHelper.CLOUMN_CURRENT_USER_ID, BGApp.mUserId);
				values.put(DBHelper.Group.G_HX_GROUPID, g.hxgroupid);
				values.put(DBHelper.Group.G_ROOMID, g.roomid);
				values.put(DBHelper.Group.G_PHOTO, g.photo);
				values.put(DBHelper.Group.G_NAME, g.name);
				values.put(DBHelper.Group.G_INTRO, g.intro);
				values.put(DBHelper.Group.G_NOTICE, g.notice);
				values.put(DBHelper.Group.G_GROUPTYPE, g.grouptype);
				list.add(values);
			}
			int count = dbHelper.insert(DBHelper.TB_GROUP, list);
			LogUtils.i("-----------批量插入群----"+count);
	}
	
	
	/**
	 * 
	 * @todo:插入一条数据
	 * @date:2014-12-22 上午10:55:07
	 * @author:hg_liuzl@163.com
	 * @params:@param dbHelper
	 * @params:@param groupBean
	 */
	public static void insertGroupBean(DBHelper dbHelper,GroupBean g) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.CLOUMN_CURRENT_USER_ID, BGApp.mUserId);
		values.put(DBHelper.Group.G_HX_GROUPID, g.hxgroupid);
		values.put(DBHelper.Group.G_ROOMID, g.roomid);
		values.put(DBHelper.Group.G_PHOTO, g.photo);
		values.put(DBHelper.Group.G_NAME, g.name);
		values.put(DBHelper.Group.G_INTRO, g.intro);
		values.put(DBHelper.Group.G_NOTICE, g.notice);
		values.put(DBHelper.Group.G_GROUPTYPE, g.grouptype);
		long count = dbHelper.insert(DBHelper.TB_GROUP, values);
		LogUtils.i("--------插入数据-------"+count);
		
	}
	
	/**
	 * 
	 * @todo:删除一条数据
	 * @date:2014-12-22 上午10:55:51
	 * @author:hg_liuzl@163.com
	 * @params:@param dbHelper
	 * @params:@param groupBean
	 */
	public static void deleteGroupBean(DBHelper dbHelper,String groupId) {
		int count = dbHelper.deleteAll(DBHelper.TB_GROUP, new String[]{DBHelper.CLOUMN_CURRENT_USER_ID,DBHelper.Group.G_ROOMID}, new String[]{BGApp.mUserId,groupId});
		LogUtils.i("-------------删除数据------------"+count);
	}
}
