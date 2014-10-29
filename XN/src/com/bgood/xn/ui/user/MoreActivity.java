package com.bgood.xn.ui.user;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.widget.TitleBar;
import com.tencent.connect.UserInfo;

/**
 * 
 * @todo:更多界面
 * @date:2014-10-29 下午2:54:47
 * @author:hg_liuzl@163.com
 */
public class MoreActivity extends BaseActivity implements OnClickListener {

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
		switch (v.getId()) {
		case R.id.tv_feedback:
			
			break;
		
		case R.id.tv_comment_score:
			score();
			break;

		case R.id.tv_update_version:
			
			break;
		case R.id.tv_about_xuanneng:
			
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
}
