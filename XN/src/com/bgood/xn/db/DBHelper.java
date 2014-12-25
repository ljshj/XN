package com.bgood.xn.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * @todo:封装数据库
 * @date:2014-12-20 下午3:52:54
 * @author:hg_liuzl@163.com
 */
public class DBHelper extends CommonDB {

	private final static String DATABASE_NAME = "bg.db";  //数据库名
	private final static int DATABASE_VERSION = 1;         //数据库版本
	/** 好友 表 */
	public final static String TB_FRIEND = "tab_friend";
	
	/** 群组表 */
	public final static String TB_GROUP = "tab_group";
	
	public DBHelper(Context context) {
		super(context,DATABASE_NAME,DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Friend.newCreateTableString());  //创建好友信息表
		db.execSQL(Group.newCreateTableString());  //创建群组信息表
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(Friend.newDeleteTableString());
		db.execSQL(Group.newDeleteTableString());
		onCreate(db);
	}

    
    /**
     * 
     * @todo:好友表
     * @date:2014-12-20 下午3:12:04
     * @author:hg_liuzl@163.com
     */
    public static class Friend{
    	
    	/**用户ID*/
    	public final static String F_USERID = "userid";
    	/**用户类型，是普通用户，还是管理员*/
    	public final static String F_TYPE = "type";
        /** 用户昵称 */
        public final static String F_NAME = "name";
        /** 用户性别*/
        public final static String F_SEX = "sex";
        /** 用户等级*/
        public final static String F_LEVEL = "level";
        /** 用户图片 */
        public final static String F_PHOTO = "photo";
        /** 用户签名*/
        public final static String F_SIGNATURE = "signature";
     
        
        
        
        
        
        public static String newCreateTableString() {
            StringBuffer buffer = new StringBuffer(512);
              buffer.append("create table ")
			        .append(TB_FRIEND).append(" (")
			        .append(FD_ID).append(" ").append("integer primary key autoincrement").append(",")
			        .append(F_USERID).append(" ").append("varchar").append(",")
			        .append(F_TYPE).append(" ").append("varchar").append(",")
			        .append(F_NAME).append(" ").append("varchar").append(",")
			        .append(F_SEX).append(" ").append("varchar").append(",")
			        .append(F_LEVEL).append(" ").append("varchar").append(",")
			        .append(F_PHOTO).append(" ").append("varchar").append(",")
			        .append(F_SIGNATURE).append(" ").append("varchar").append(")");
            return buffer.toString();
        }
        
        public static String newDeleteTableString() {
            StringBuffer buffer = new StringBuffer(64);
            buffer.append("DROP TABLE IF EXISTS ")
                .append(TB_FRIEND);
            return buffer.toString();
        }
    }
    
    /**
     * 
     * @todo:群组实体类
     * @date:2014-12-20 下午3:08:35
     * @author:hg_liuzl@163.com
     */
    public static class Group{
    	
    	/**环信groupID*/
    	public final static String G_HX_GROUPID = "hx_groupid";
    	/**群ID*/
    	public final static String G_ROOMID = "roomid";
    	/**群名称*/
    	public final static String G_NAME = "name";
        /** 群简介 */
        public final static String G_INTRO = "intro";
        /** 群图片*/
        public final static String G_PHOTO = "photo";
        /** //群类型 0，固定群；1，临时群*/
        public final static String G_GROUPTYPE = "grouptype";
        
        public static String newCreateTableString() {
            StringBuffer buffer = new StringBuffer(512);
              buffer.append("create table ")
			        .append(TB_GROUP).append(" (")
			        .append(FD_ID).append(" ").append("integer primary key autoincrement").append(",")
			        .append(G_HX_GROUPID).append(" ").append("varchar").append(",")
			        .append(G_ROOMID).append(" ").append("varchar").append(",")
			        .append(G_NAME).append(" ").append("varchar").append(",")
			        .append(G_INTRO).append(" ").append("varchar").append(",")
			        .append(G_PHOTO).append(" ").append("varchar").append(",")
			        .append(G_GROUPTYPE).append(" ").append("varchar").append(")");
            return buffer.toString();
        }
        
        public static String newDeleteTableString() {
            StringBuffer buffer = new StringBuffer(64);
            buffer.append("DROP TABLE IF EXISTS ")
                .append(TB_GROUP);
            return buffer.toString();
        }
    }
	
}

