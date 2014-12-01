package com.bgood.xn.ui.xuanneng;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.bgood.xn.R;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.ui.xuanneng.joke.JokePersonActivity;

/**
 * 
 * @author king 
 * 炫能主页列表，展示各种技能入口
 */

public class XuanNengMainActivity extends BaseActivity implements OnClickListener
{
	private String mUserId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mUserId = getIntent().getStringExtra(UserInfoBean.KEY_USER_ID);
        setContentView(R.layout.layout_my_xuanneng);
        findViewById(R.id.my_xuanneng_btn_humor).setOnClickListener(this);
    }
  


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.my_xuanneng_btn_humor:
			Intent intent = new Intent(mActivity,JokePersonActivity.class);
			intent.putExtra(UserInfoBean.KEY_USER_ID, mUserId);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
