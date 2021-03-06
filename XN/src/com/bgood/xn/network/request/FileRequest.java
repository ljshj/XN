package com.bgood.xn.network.request;

import java.io.File;

import android.content.Context;

import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.http.HttpRequestAsyncTask;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.system.SystemConfig.ServerType;

/**
 * @todo:上传文件
 * @date:2014-11-5 上午10:55:08
 * @author:hg_liuzl@163.com
 */
public class FileRequest extends BaseNetWork {
	private static FileRequest instance = null;
	public  synchronized static FileRequest getInstance(){
		if(null == instance)
			instance = new FileRequest();
		
		return instance;
	}
    
 /**
	 * 
	 * @date:2014-10-20 下午6:21:41
	 * @author:hg_liuzl@163.com
	 * @params:@param mHttpTaskListener
	 * @params:@param context
	 * @params:@param phone
	 */
	 public void requestUpLoadFile(TaskListenerWithState mHttpTaskListener,Context context,boolean showDialog,File files,String userid,String tag,String extra){
			String url = "?userid=" + userid + "&tag=" + tag + "&extra=" + extra;
			setConnUrl(url);
			setFile(files);
			new HttpRequestAsyncTask(showDialog,ServerType.FileServer,this, mHttpTaskListener, context).execute();
	 }
}
