//package com.bgood.xn.ui.message;
//
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.bgood.xn.R;
//import com.bgood.xn.ui.base.BaseActivity;
//
///**
// * 
// * @todo:添加好友 
// * @date:2014-12-8 下午5:36:45
// * @author:hg_liuzl@163.com
// */
//public class AddFriendActivity extends BaseActivity implements OnClickListener
//{
//	private UserDTO user;
//	private EditText add_friend_or_group_accesswords;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.add_friend_or_group);
//		initViews();
//		setListeners();
//		initData();
//	}
//
//	@Override
//	protected void initViews()
//	{
//		super.initViews();
//		Bundle bundle = getIntent().getExtras();
//		if (bundle != null)
//		{
//			user = (UserDTO) bundle.getSerializable("user");
//		}
//		add_friend_or_group_accesswords = (EditText) findViewById(R.id.add_friend_or_group_accesswords);
//
//	}
//
//	@Override
//	protected void initData()
//	{
//		super.initData();
//		UserCenterMessageManager.getInstance().registerObserver(this);
//	}
//
//	@Override
//	protected void onStop()
//	{
//		super.onStop();
//		UserCenterMessageManager.getInstance().unregisterObserver(this);
//	}
//
//	@Override
//	protected void onStart()
//	{
//		super.onStart();
//		UserCenterMessageManager.getInstance().registerObserver(this);
//	}
//
//	@Override
//	protected void setListeners()
//	{
//		super.setListeners();
//		findViewById(R.id.add_friend_or_group_submit).setOnClickListener(this);
//	}
//
//	@Override
//	public void onClick(View v)
//	{
//		switch (v.getId())
//		{
//		case R.id.add_friend_or_group_submit:
//			String accesswords = add_friend_or_group_accesswords.getText().toString();
//			UserCenterMessageManager.getInstance().addFriend(user.userId, accesswords);
//			Toast.makeText(this, "等待好友验证", Toast.LENGTH_SHORT).show();
//			finish();
//			break;
//		}
//	}
//}
