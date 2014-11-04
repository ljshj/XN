package com.bgood.xn.system;

import java.io.File;

import android.os.Environment;

/**
 * @todo:TODO
 * @date:2014-11-4 下午2:44:21
 * @author:hg_liuzl@163.com
 */
public class Const {

	public static final String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
	public static final String BASE_END = ".png";
	public static final String CID_IMG_STRING_PATH = BASE_PATH + System.currentTimeMillis() + BASE_END;

	
}
