package com.bgood.xn.ui.help;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;

import com.bgood.xn.R;
import com.bgood.xn.bean.JokeBean;
import com.bgood.xn.bean.WeiQiangBean;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.WeiqiangRequest;
import com.bgood.xn.network.request.XuannengRequest;
import com.bgood.xn.view.BToast;

/**
 * @todo:删除微墙，幽默秀
 * @date:2015-2-4 上午11:17:00
 * @author:hg_liuzl@163.com
 */
public class DeleteListener implements OnClickListener,TaskListenerWithState {

	private Object object;
	private Activity mActivity;
	private IDeleteCallback mCallback;
	
	public DeleteListener(Object object,Activity activity,IDeleteCallback callback){
		this.object = object;
		this.mActivity = activity;
		this.mCallback = callback;
	}
	
	@Override
	public void onClick(View v) {
		alertDialog();
//		if(object instanceof WeiQiangBean){	//微墙
//			WeiQiangBean weiqiang = (WeiQiangBean) object;
//			WeiqiangRequest.getInstance().requestWeiqiangDel(DeleteListener.this, mActivity, weiqiang.weiboid);
//		}else if(object instanceof JokeBean){ //炫能
//			JokeBean joke = (JokeBean) object;
//			XuannengRequest.getInstance().requestXuanDel(DeleteListener.this, mActivity, joke.jokeid);
//		}
	}

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			switch (bNetWork.getMessageType()) {
			case 860004:	//删除微墙
			case 870011:	//删除幽默秀
				if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
					mCallback.deleteAction(object);
				}else{
					BToast.show(mActivity, "删除失败");
				}
				break;
				}
			}
	}
	
	
	private void deleteAction() {
		if(object instanceof WeiQiangBean){	//微墙
			WeiQiangBean weiqiang = (WeiQiangBean) object;
			WeiqiangRequest.getInstance().requestWeiqiangDel(DeleteListener.this, mActivity, weiqiang.weiboid);
		}else if(object instanceof JokeBean){ //炫能
			JokeBean joke = (JokeBean) object;
			XuannengRequest.getInstance().requestXuanDel(DeleteListener.this, mActivity, joke.jokeid);
		}

	}
	
	private void alertDialog(){
		final AlertDialog dialog = new AlertDialog.Builder(mActivity, R.style.user_dialog)
    	.setTitle("确定删除这条幽默秀吗?")
    	.setCancelable(true)
    	.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				deleteAction();
			}
		}).setNegativeButton("取消", null)
    	.show();
	}
}