package com.bgood.xn.utils;

import android.util.Log;
/**
 * @todo   日志工具类，
 */
public class LogUtils {
	
	private static final boolean DEBUG = true;			//控制日志的开关，当为true时，打印日志，false时不打印日志
	private static final String TAG = "framework";

	public static void e(String msg) {
		if(DEBUG) Log.e(TAG, "-------------------------:"+msg);
	}

	public static void e(String tag, String msg) {
		if(DEBUG) Log.e(tag, "-------------------------:"+msg);
	}

	public static void e(String tag, String msg, Throwable throwable) {
		if(DEBUG) Log.e(tag, "-------------------------:"+msg,throwable);
	}

	public static void i(String msg) {
		if(DEBUG) Log.i(TAG, "-------------------------:"+msg);
	}

	public static void i(String tag, String msg) {
		if(DEBUG) Log.i(tag, "-------------------------:"+msg);
	}

	public static void i(String tag, String msg, Throwable throwable) {
		if(DEBUG) Log.i(tag, "-------------------------:"+msg,throwable);
	}
	
	public static void d(String tag,String msg) {
		if(DEBUG) Log.d(tag, "-------------------------:"+msg);
	}
	
	public static void d(String msg) {
		if(DEBUG) Log.d(TAG, "-------------------------:"+msg);
	}

	public static void v(String msg) {
		if(DEBUG) Log.v(TAG, "-------------------------:"+msg);
	}

	public static void v(String tag, String msg) {
		if(DEBUG) Log.v(tag, "-------------------------]:"+msg);
	}

	public static void w(Throwable throwable) {
		if(DEBUG) Log.w(TAG, "-------------------------:"+throwable);
	}
	
	public static void w(String tag,Throwable throwable) {
		if(DEBUG) Log.w(tag, "-------------------------:"+throwable);
	}
}
