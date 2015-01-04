package com.bgood.xn.ui.message;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.view.BToast;
import com.bgood.xn.widget.TitleBar;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupInfo;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;

public class GroupCardActivity extends BaseActivity {
	private Button btn_add_group;
	private TextView tv_name;
	private TextView tv_introduction;
	private EMGroup group;
	private String groupid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_group_card);
		(new TitleBar(mActivity)).initTitleBar("群名片");
		tv_name = (TextView) findViewById(R.id.name);
		btn_add_group = (Button) findViewById(R.id.btn_add_to_group);
		tv_introduction = (TextView) findViewById(R.id.tv_introduction);

		EMGroupInfo groupInfo = (EMGroupInfo) getIntent().getSerializableExtra("groupinfo");
		String groupname = groupInfo.getGroupName();
		groupid = groupInfo.getGroupId();
		tv_name.setText(groupname);
	}
	
	//加入群聊
	public void addToGroup(View view){
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setMessage("正在发送请求...");
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		new Thread(new Runnable() {
			public void run() {
				try {
					//如果是membersOnly的群，需要申请加入，不能直接join
					if(group.isMembersOnly()){
						EMGroupManager.getInstance().applyJoinToGroup(groupid, "求加入");
					}else{
						EMGroupManager.getInstance().joinGroup(groupid);
					}
					runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							if(group.isMembersOnly()){
								BToast.show(mActivity, "发送请求成功，等待群主同意");
							}else{
								BToast.show(mActivity, "加入群聊成功");
							}
							btn_add_group.setEnabled(false);
						}
					});
				} catch (final EaseMobException e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							BToast.show(mActivity, "加入群聊失败："+e.getMessage());
						}
					});
				}
			}
		}).start();
	}
	
	public void back(View view){
		finish();
	}
}
