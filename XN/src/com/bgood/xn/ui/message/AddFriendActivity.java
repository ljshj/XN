package com.bgood.xn.ui.message;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.ui.base.BaseActivity;
import com.easemob.chat.ChatApplication;
import com.easemob.chat.EMContactManager;


/**
 * 
 * @todo:添加好友
 * @date:2014-12-12 下午2:58:20
 * @author:hg_liuzl@163.com
 */
public class AddFriendActivity extends BaseActivity{
	private EditText editText;
	private LinearLayout searchedUserLayout;
	private TextView nameText;
	private Button searchBtn;
	private ImageView avatar;
	private InputMethodManager inputMethodManager;
	private String toAddUsername;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		
		editText = (EditText) findViewById(R.id.edit_note);
		searchedUserLayout = (LinearLayout) findViewById(R.id.ll_user);
		nameText = (TextView) findViewById(R.id.name);
		searchBtn = (Button) findViewById(R.id.search);
		avatar = (ImageView) findViewById(R.id.avatar);
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	}
	
	
	/**
	 * 查找contact
	 * @param v
	 */
	public void searchContact(View v) {
		final String name = editText.getText().toString();
		String saveText = searchBtn.getText().toString();
		
		if (getString(R.string.button_search).equals(saveText)) {
			toAddUsername = name;
			if(TextUtils.isEmpty(name)) {
//				startActivity(new Intent(this, AlertDialog.class).putExtra("msg", "请输入用户名"));
				return;
			}
			
			// TODO 从服务器获取此contact,如果不存在提示不存在此用户
			
			//服务器存在此用户，显示此用户和添加按钮
			searchedUserLayout.setVisibility(View.VISIBLE);
			nameText.setText(toAddUsername);
			
		} 
	}	
	
	/**
	 *  添加contact
	 * @param view
	 */
	public void addContact(View view){
		if(ChatApplication.getInstance().getUserName().equals(nameText.getText().toString())){
			return;
		}
		
		if(ChatApplication.getInstance().getContactList().containsKey(nameText.getText().toString())){
			return;
		}
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("正在发送请求...");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		
		new Thread(new Runnable() {
			public void run() {
				
				try {
					//demo写死了个reason，实际应该让用户手动填入
					EMContactManager.getInstance().addContact(toAddUsername, "加个好友呗");
//					runOnUiThread(new Runnable() {
//						public void run() {
//							progressDialog.dismiss();
//							Toast.makeText(getApplicationContext(), "发送请求成功,等待对方验证", 1).show();
//						}
//					});
				} catch (final Exception e) {
//					runOnUiThread(new Runnable() {
//						public void run() {
//							progressDialog.dismiss();
//							Toast.makeText(getApplicationContext(), "请求添加好友失败:" + e.getMessage(), 1).show();
//						}
//					});
				}
			}
		}).start();
	}
	
	public void back(View v) {
		finish();
	}
}
