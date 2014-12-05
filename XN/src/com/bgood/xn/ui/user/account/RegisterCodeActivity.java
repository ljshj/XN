package com.bgood.xn.ui.user.account;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.view.BToast;
import com.bgood.xn.widget.TitleBar;

/**
 * 
 * @todo:注册---填写验证码
 * @date:2014-12-2 下午5:28:21
 * @author:hg_liuzl@163.com
 */
public class RegisterCodeActivity extends BaseActivity implements TaskListenerWithState
{
	private TextView m_phoneTv = null; // 填写手机号
	private EditText m_codeEt = null; // 验证码
	private LinearLayout m_doneLl = null; // 完成按钮
	private String code = "";
	private String m_phone = "";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_register_code);
		(new TitleBar(mActivity)).initTitleBar("注册");
		m_phone = getIntent().getStringExtra("phone"); 
		findView();
	}
	

	private void findView()
	{
		m_phoneTv = (TextView) findViewById(R.id.register_code_tv_phone);
		m_codeEt = (EditText) findViewById(R.id.register_code_et_code);
		m_phoneTv.setText("验证码已发送到" + m_phone.substring(0,3) + "****" + m_phone.substring(7, m_phone.length()) + "，请注意查收");
		findViewById(R.id.register_code_ll_done).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				checkInfo();
			}
		});
	}

	

	/**
	 * 检查用户信息方法
	 */
	private void checkInfo()
	{
		// 用户手机号码查询
		code = m_codeEt.getText().toString().trim();
		if (TextUtils.isEmpty(code))
		{
			Toast.makeText(RegisterCodeActivity.this, "验证码不能为空！", Toast.LENGTH_LONG).show();
			BToast.show(mActivity, "请输入验证码");
			return;
		} else 
		{
			UserCenterRequest.getInstance().requestVerifyPhoneAndGetBgID(this, mActivity, m_phone, code);
		}
	}

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();

			JSONObject body = bNetWork.getBody();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				try {
					boolean verfied = body.getBoolean("verfied");
					JSONArray jsonArray = body.getJSONArray("ids");
					String[] ids = null;
					if (jsonArray != null)
					{
						ids = new String[jsonArray.length()];
						for (int i = 0; i < jsonArray.length(); i++)
						{
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							ids[i] = jsonObject.getString("id");
						}
					}
					
					Intent intent = new Intent(RegisterCodeActivity.this, RegisterSelectNumberActivity.class);
					intent.putExtra("ids", ids);
					intent.putExtra("phone", m_phone);
					startActivity(intent);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
