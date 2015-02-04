package com.bgood.xn.system;

public class SystemConfig
{	
	
	public enum ServerType{	//服务器类型
		LoginServer,
		BusinessServer,
		FileServer,
		IMServer
	};
	
	/**
	 * 
	 *用服务器(登录服务器)分配管理其它的服务器
	 * */
	public static String HttpServer = "114.215.189.189:5000";		
	//public static String HttpServer = "192.168.1.120";		
	
	public static String SERVERTYPE = "BS";// "BS","AS"
	
	public static int PACKETHEADSIZE = 4; 
	
	public static int SessionID = 0;
	
	public static String protocolType = "TCP";// "HTTP","TCP"
	
	public static String IM_SERVER = null;
	
	public static String FILE_SERVER = null;
	
	public static String BS_SERVER = null;
	
	public static String SERVERIP = "192.168.1.113";
	
	public static int SERVERPORT = 5001;
	
	public static boolean isFirstUse;
	
	public static boolean isOpenEveningDisturb;
	
	public static boolean isOpenSoundPrompt;
	
	public static boolean isOpenVibrationPrompt;
	
	public static boolean isOpenSystemnNotify;
	
	public static boolean isOpenFriendNotify;

}
