package com.bgood.xn.ui.user.more;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.bean.ApkBean;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.utils.ConfigUtil;
import com.bgood.xn.utils.update.UpdateManager;
import com.bgood.xn.widget.TitleBar;

/**
 * 
 * @todo:更多界面
 * @date:2014-10-29 下午2:54:47
 * @author:hg_liuzl@163.com
 */
public class MoreActivity extends BaseActivity implements OnClickListener,TaskListenerWithState {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_layout_more);
		new TitleBar(MoreActivity.this).initTitleBar("更多");
		findViewById(R.id.tv_feedback).setOnClickListener(this);
		findViewById(R.id.tv_comment_score).setOnClickListener(this);
		findViewById(R.id.tv_update_version).setOnClickListener(this);
		findViewById(R.id.tv_about_xuanneng).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.tv_feedback:
			intent = new Intent(mActivity, FeedbackShowActivity.class);
			startActivity(intent);
			break;
		
		case R.id.tv_comment_score:
			score();
			break;

		case R.id.tv_update_version:
			UserCenterRequest.getInstance().requestCheckVesion(this, mActivity);
			break;
		case R.id.tv_about_xuanneng:
			intent = new Intent(mActivity, AboutUsActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	
	/**
	 * 
	 * @todo:评分
	 * @date:2014-10-29 下午8:00:39
	 * @author:hg_liuzl@163.com
	 * @params:
	 */
	private void score() {
		Uri uri = Uri.parse("market://details?id=" + getPackageName());
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			String strJson = bNetWork.getStrJson();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				/**版本升级处理*/
				final ApkBean apk = JSON.parseObject(strJson, ApkBean.class);
				UpdateManager manager = new UpdateManager(mActivity, apk);
				manager.checkUpdateInfo();
			}
		}
	}
}
