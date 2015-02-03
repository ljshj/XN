package com.bgood.xn.ui.xuanneng;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.bgood.xn.R;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.ui.xuanneng.joke.JokeMeActivity;
import com.bgood.xn.ui.xuanneng.joke.JokePersonActivity;
import com.bgood.xn.widget.TitleBar;

/**
 * 
 * @author king 
 * 炫能主页列表，展示各种技能入口
 */

public class XuanNengMainActivity extends BaseActivity implements OnClickListener
{
	private String mUserId = "";
	private boolean isSelf = true;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mUserId = getIntent().getStringExtra(UserInfoBean.KEY_USER_ID);
        
        isSelf = mUserId.equals(BGApp.mUserId)?true:false;
        setContentView(R.layout.layout_my_xuanneng);
        (new TitleBar(mActivity)).initTitleBar(isSelf?"我的炫能":"TA的炫能");
        findViewById(R.id.my_xuanneng_btn_humor).setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.my_xuanneng_btn_humor:
			Intent intent = new Intent(mActivity, isSelf?JokeMeActivity.class:JokePersonActivity.class);
			intent.putExtra(UserInfoBean.KEY_USER_ID, mUserId);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
