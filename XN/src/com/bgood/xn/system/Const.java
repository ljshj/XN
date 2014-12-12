package com.bgood.xn.system;

import android.os.Environment;

/**
 * @todo:常量类
 * @date:2014-11-4 下午2:44:21
 * @author:hg_liuzl@163.com
 */
public class Const {

	/**登录到环信聊天服务器的密码**/
	public static final String IM_PWD = "banggood123";
	
	public static final String BASE_BEGIN = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xuanneng/";
	public static final String BASE_END = ".jpg";
	public static final String CID_IMG_STRING_PATH = BASE_BEGIN + System.currentTimeMillis() + BASE_END;
	

		
	// 默认timeout 时间 60s
	public final static int SOCKET_TIMOUT = 60 * 1000;
	
	public final static int SOCKET_READ_TIMOUT = 15 * 1000;
	
	//如果没有连接无服务器。读线程的sleep时间
	public final static int SOCKET_SLEEP_SECOND = 3 ;
	
	//心跳包发送间隔时间
	public final static int SOCKET_HEART_SECOND =3 ;
	
	public final static String BC = "BC";
}
