package com.bgood.xn.ui.user.account;import org.json.JSONArray;import org.json.JSONException;import org.json.JSONObject;import android.content.Intent;import android.os.Bundle;import android.text.TextUtils;import android.view.View;import android.view.View.OnClickListener;import android.view.inputmethod.InputMethodManager;import android.widget.Button;import android.widget.CheckBox;import android.widget.CompoundButton;import android.widget.CompoundButton.OnCheckedChangeListener;import android.widget.EditText;import android.widget.LinearLayout;import android.widget.TextView;import android.widget.Toast;import com.bgood.xn.R;import com.bgood.xn.network.BaseNetWork.ReturnCode;import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;import com.bgood.xn.network.BaseNetWork;import com.bgood.xn.network.HttpRequestInfo;import com.bgood.xn.network.HttpResponseInfo;import com.bgood.xn.network.request.UserCenterRequest;import com.bgood.xn.ui.base.BaseActivity;import com.bgood.xn.utils.ToolUtils;import com.bgood.xn.utils.WindowUtil;import com.bgood.xn.view.BToast;import com.bgood.xn.widget.TitleBar;/*** *  * @todo:注册界面 * @date:2014-11-28 下午5:37:19 * @author:hg_liuzl@163.com */public class RegisterActivity extends BaseActivity implements TaskListenerWithState{	private EditText m_phoneEt = null; // 手机号码	private CheckBox m_checkCb = null;	private TextView m_ruleTv = null;	private LinearLayout m_doneLl = null; // 完成	private String phone = "";	@Override	protected void onCreate(Bundle savedInstanceState)	{		super.onCreate(savedInstanceState);		setContentView(R.layout.layout_register);		(new TitleBar(mActivity)).initTitleBar("注册");		findView();	}	/**	 * 控件初始化方法	 */	private void findView()	{		m_phoneEt = (EditText) findViewById(R.id.register_et_phone);		m_checkCb = (CheckBox) findViewById(R.id.register_cb_check);		m_ruleTv = (TextView) findViewById(R.id.register_tv_rule);		findViewById(R.id.register_ll_done).setOnClickListener(new OnClickListener()		{			@Override			public void onClick(View v)			{				checkInfo();			}		});	}	/**	 * 检查用户信息方法	 */	private void checkInfo()	{		// 用户手机号码查询		phone = m_phoneEt.getText().toString().trim();		if (TextUtils.isEmpty(phone))		{			BToast.show(mActivity, "请输入手机号码");			return;		}else if (!ToolUtils.isTelephone(phone)){			BToast.show(mActivity, "请输入正确的手机号码");			return;		} else if(!m_checkCb.isChecked()){			BToast.show(mActivity, "请阅读产品协议");			return;		}else{			UserCenterRequest.getInstance().requestVerifyCode(this, mActivity, phone);		}	}	@Override	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {		if(info.getState() == HttpTaskState.STATE_OK){			BaseNetWork bNetWork = info.getmBaseNetWork();			JSONObject body = bNetWork.getBody();			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){								String verfy = body.optString("verfy");				Intent intent = new Intent(this, RegisterCodeActivity.class);				intent.putExtra("verfy", verfy);				intent.putExtra("phone", phone);				startActivity(intent);			}else{				BToast.show(mActivity, "获取验证码失败");			}		}	}}