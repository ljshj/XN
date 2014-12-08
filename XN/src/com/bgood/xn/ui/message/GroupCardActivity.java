//package com.bgood.xn.ui.message;
//
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.CompoundButton.OnCheckedChangeListener;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bgood.xn.R;
//import com.bgood.xn.ui.base.BaseActivity;
//
///**
// * @author ChenGuoqing 2014-6-27下午1:27:08
// */
//public class GroupCardActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener
//{
//	private TextView group_card_tv_show_all_member;
//	private CheckBox group_card_cb_sound_prompt;
//	private GroupDTO group;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.layout_group_card);
//		initViews();
//		setListeners();
//		initData();
//	}
//
//	@Override
//	protected void initData()
//	{
//		super.initData();
//	}
//
//	@Override
//	protected void initViews()
//	{
//		super.initViews();
//		group_card_tv_show_all_member = (TextView) findViewById(R.id.group_card_tv_show_all_member);
//		group_card_cb_sound_prompt = (CheckBox) findViewById(R.id.group_card_cb_sound_prompt);
//		Bundle bundle = getIntent().getExtras();
//		if (bundle != null)
//		{
//			group = (GroupDTO) bundle.getSerializable("user");
//		}
//		if (group == null || group.groupID == null || group.groupID.length() <= 0)
//		{
//			Toast.makeText(this, "没有该群", Toast.LENGTH_SHORT).show();
//			finish();
//		}
//
//	}
//
//	@Override
//	protected void setListeners()
//	{
//		super.setListeners();
//		group_card_tv_show_all_member.setOnClickListener(this);
//		group_card_cb_sound_prompt.setOnCheckedChangeListener(this);
//	}
//
//	@Override
//	public void onClick(View v)
//	{
//		switch (v.getId())
//		{
//		case R.id.group_card_tv_show_all_member:
//			toActivity(GroupAllMemberActivity.class, null);
//			break;
//		}
//	}
//
//	@Override
//	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//	{
//	}
//
//}
