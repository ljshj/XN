package com.bgood.xn.system;

import android.os.Environment;

/**
 * @todo:常量类
 * @date:2014-11-4 下午2:44:21
 * @author:hg_liuzl@163.com
 */
public class Const {

	public static final String BASE_BEGIN = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xuanneng/";
	public static final String BASE_END = ".jpg";
	public static final String CID_IMG_STRING_PATH = BASE_BEGIN + System.currentTimeMillis() + BASE_END;

	
}
