package com.bgood.xn.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * @author lzlong@zwmob.com
 * @todo  封装过后的数据库
 * @time 2013-11-1 上午10:26:28
 */
public class DBHelper extends CommonDB {

	private final static String DATABASE_NAME = "pad.db";  //数据库名
	private final static int DATABASE_VERSION = 4;         //数据库版本
	
	/**网站信息表**/
	public final static String TB_WEBSITE_INFO = "website_info"; 
	
	/**用户表**/
	public final static String TB_USER_INFO = "user_info"; 
	
	/** 下载表 */
	public final static String TB_DOWNLOAD = "download";
	
	/** 应用栏目表 */
	public final static String TB_COLUMN_INFO = "tb_column";
	
	/** 应用表 */
	public final static String TB_APP_NAV = "tab_app";
	
	
	
	public DBHelper(Context context) {
		super(context,DATABASE_NAME,DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Download.newCreateTableString());  //创建下载信息表
		db.execSQL(WebSite.newCreateTableString());  //创建网站信息表
		db.execSQL(User.newCreateTableString());  //创建网站信息表
		db.execSQL(App.newCreateTableString());  //创建网站信息表
		db.execSQL(Column.newCreateTableString());  //创建网站信息表
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(Download.newDeleteTableString());
		db.execSQL(WebSite.newDeleteTableString());
		db.execSQL(User.newDeleteTableString());
		db.execSQL(App.newDeleteTableString());
		db.execSQL(Column.newDeleteTableString());
		onCreate(db);
	}
	
	/**
	 * 网站表
	 */
	public static class WebSite{
		
		/** id */
		public final static String WEB_ID = "id";
		/** 图片地址 */
		public final static String IMG_URL = "imgUrl";
		/** 网站地址 */
		public final static String WEB_URL = "webUrl";
		/** 网站名称 */
		public final static String WEB_NAME = "webName";
		/** 打开时间 */
		public final static String WEB_OPENTIME = "web_opentime";
		
		public static String newCreateTableString() {
			StringBuffer buffer = new StringBuffer(512);
			buffer.append("create table ")
					.append(TB_WEBSITE_INFO).append(" (")
					.append(FD_ID).append(" ").append("integer primary key autoincrement").append(",")
					.append(WEB_ID).append(" ").append("integer").append(",")
					.append(IMG_URL).append(" ").append("varchar").append(",")
					.append(WEB_URL).append(" ").append("varchar").append(",")
					.append(WEB_NAME).append(" ").append("varchar").append(",")
					.append(WEB_OPENTIME).append(" ").append("longtext").append(")");
			return buffer.toString();
		}
		
		public static String newDeleteTableString() {
			StringBuffer buffer = new StringBuffer(64);
			buffer.append("DROP TABLE IF EXISTS ")
				.append(TB_WEBSITE_INFO);
			return buffer.toString();
		}
	}
    /**
     * 用户表
     */
    public static class User{
    	  /** 用户id */
        public final static String USER_ID = "user_id";
        /** 用户名 */
        public final static String USER_NAME = "user_name";
        /** 用户邮箱 */
        public final static String USER_EMAIL = "user_email";
       
        
        public static String newCreateTableString() {
            StringBuffer buffer = new StringBuffer(512);
            buffer.append("create table ")
                    .append(TB_USER_INFO).append(" (")
                    .append(FD_ID).append(" ").append("integer primary key autoincrement").append(",")
                    .append(USER_ID).append(" ").append("integer").append(",")
                    .append(USER_NAME).append(" ").append("varchar").append(",")
                    .append(USER_EMAIL).append(" ").append("long").append(")");
            return buffer.toString();
        }
        
        public static String newDeleteTableString() {
            StringBuffer buffer = new StringBuffer(64);
            buffer.append("DROP TABLE IF EXISTS ")
                .append(TB_USER_INFO);
            return buffer.toString();
        }
    	
    }
    

    /**
     * 应用详细信息表字段
     */
    public static class App {
      
    	/** 关联的栏目ID */
        public final static String APP_COLUMN_ID = "user_coumn_id";
        /** 应用的包名 */
        public final static String APP_PACKAGE_NAME = "app_package_name";

        public static String newCreateTableString() {
            StringBuffer buffer = new StringBuffer(512);
            buffer.append("create table ")
                    .append(TB_APP_NAV).append(" (")
                    .append(FD_ID).append(" ").append("integer primary key autoincrement").append(",")
                    .append(APP_COLUMN_ID).append(" ").append("integer").append(",")
                    .append(APP_PACKAGE_NAME).append(" ").append("varchar").append(")");
            return buffer.toString();
        }

        public static String newDeleteTableString() {
            StringBuffer buffer = new StringBuffer(64);
            buffer.append("DROP TABLE IF EXISTS ")
                    .append(TB_APP_NAV);
            return buffer.toString();
        }
    }


	/**
	 * 应用栏目表
	 */
	public static class Column{
		
		/** id */
		public final static String COLUMN_ID = "id";
		/** 图片地址 */
		public final static String COLUMN_NAME = "name";
		
		public static String newCreateTableString() {
			StringBuffer buffer = new StringBuffer(512);
			buffer.append("create table ")
					.append(TB_COLUMN_INFO).append(" (")
					.append(FD_ID).append(" ").append("integer primary key autoincrement").append(",")
					.append(COLUMN_ID).append(" ").append("integer").append(",")
					.append(COLUMN_NAME).append(" ").append("longtext").append(")");
			return buffer.toString();
		}
		
		public static String newDeleteTableString() {
			StringBuffer buffer = new StringBuffer(64);
			buffer.append("DROP TABLE IF EXISTS ")
				.append(TB_COLUMN_INFO);
			return buffer.toString();
		}
	}
	
	

	/**
     * 下载表
     */
    public static class Download{
    	
    	/**全路径*/
    	public final static String FULL_PATH_NAME = "full_path_name";
    	/**文件名*/
    	public final static String FILENAME = "filename";
        /** url */
        public final static String URL = "url";
        /** 断点 */
        public final static String BREAKPOINT = "breakpoint";
        /**文件大小*/
        public final static String TOTAL_SIZE = "totalsize";
        /** 保存路径 */
        public final static String SAVE_PATH = "savePath";
        /**下载状态 0，未完成下载; 1，已经下载完成,默认是未下载完成**/
        public final static String STATE = "state";
        /**下载完成*/
        public final static int DOLOAD_COMPLETE = 1;
        /**下载未成功*/
        public final static int DOLOAD_UN_COMPLETE = 0;
        
        public static String newCreateTableString() {
            StringBuffer buffer = new StringBuffer(512);
            buffer.append("create table ")
                    .append(TB_DOWNLOAD).append(" (")
                    .append(FD_ID).append(" ").append("integer primary key autoincrement").append(",")
                    .append(FULL_PATH_NAME).append(" ").append("varchar").append(",")
                    .append(FILENAME).append(" ").append("varchar").append(",")
                    .append(URL).append(" ").append("varchar").append(",")
                    .append(SAVE_PATH).append(" ").append("varchar").append(",")
                    .append(TOTAL_SIZE).append(" ").append("long").append(",")
                    .append(STATE).append(" ").append("integer").append(",")
                    .append(BREAKPOINT).append(" ").append("long").append(")");
            return buffer.toString();
        }
        
        public static String newDeleteTableString() {
            StringBuffer buffer = new StringBuffer(64);
            buffer.append("DROP TABLE IF EXISTS ")
                .append(TB_DOWNLOAD);
            return buffer.toString();
        }
    }
	
	
}

