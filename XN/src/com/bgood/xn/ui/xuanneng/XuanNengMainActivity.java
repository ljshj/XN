package com.bgood.xn.ui.xuanneng;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.bgood.xn.R;
import com.bgood.xn.adapter.MyXuanNengAdapter;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.ui.BaseActivity;

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
//			Intent intent = new Intent(mActivity,XuanNengPersonActivity.class);
//			intent.putExtra(UserInfoBean.KEY_USER_ID, mUserId);
//			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
