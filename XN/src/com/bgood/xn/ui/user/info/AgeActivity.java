package com.bgood.xn.ui.user.info;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.bean.UserBean;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.HttpRquestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.view.BToast;

/**
 * 修改年龄页面
 */
public class AgeActivity extends BaseActivity implements OnClickListener,TaskListenerWithState
{
    private Button m_backBtn = null;  // 返回按钮
    private EditText m_ageEt = null;  // 昵称编辑框
    private Button m_doneBtn = null;  // 确定按钮
    
    
    private String m_age = "";
    private UserBean mUserBean = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_age);
        mUserBean = (UserBean) getIntent().getSerializableExtra(UserBean.KEY_USER_BEAN);
        findView();
    }
  
    /**
     * 控件初始化方法
     */
    private void findView()
    {
        m_backBtn = (Button) findViewById(R.id.age_btn_back);
        m_ageEt = (EditText) findViewById(R.id.age_et_age);
        m_doneBtn = (Button) findViewById(R.id.age_btn_done);
        
        m_ageEt.setText(mUserBean.age);
        m_ageEt.setSelection(mUserBean.age.length());
        
        m_backBtn.setOnClickListener(this);
        m_doneBtn.setOnClickListener(this);
    }
    
    /**
     * 控件事件监听方法
     */
    private void setListener()
    {
        // 返回按钮
        m_backBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                AgeActivity.this.finish();
            }
        });
        
        // 确定按钮
        m_doneBtn.setOnClickListener(new OnClickListener()
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
        String age = m_ageEt.getText().toString().trim();
        if (TextUtils.isEmpty(age))
        {
            BToast.show(mContext, "请输入年龄！");
            return;
        }else
        {
		UserCenterRequest.getInstance().requestUpdatePerson(this, mContext, "age", age);
        }
    }
  

	@Override
	public void onClick(View v) {
	switch (v.getId()) {
	case R.id.age_btn_back:
		finish();
		break;
	case R.id.age_btn_done:
		checkInfo();
		break;
	default:
		break;
	}
		
	}

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		switch (info.getState()) {
		case STATE_ERROR_SERVER:
			Toast.makeText(mContext, "服务器地址错误", Toast.LENGTH_SHORT).show();
			break;
		case STATE_NO_NETWORK_CONNECT:
			Toast.makeText(mContext, "没有网络，请检查您的网络连接", Toast.LENGTH_SHORT).show();
			break;
		case STATE_TIME_OUT:
			Toast.makeText(mContext, "连接超时", Toast.LENGTH_SHORT).show();
			break;
		case STATE_UNKNOWN:
			Toast.makeText(mContext, "未知错误", Toast.LENGTH_SHORT).show();
			break;
		case STATE_OK:
			BaseNetWork bNetWork = info.getmBaseNetWork();
			JSONObject body = bNetWork.getBody();
				BToast.show(mContext, "修改成功");
				Intent intent = getIntent();
	            intent.putExtra("age", m_age);
	            setResult(RESULT_OK, intent);
	            finish();
		default:
			break;
			}
	}

}
