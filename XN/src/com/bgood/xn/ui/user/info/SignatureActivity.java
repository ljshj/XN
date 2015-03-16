package com.bgood.xn.ui.user.info;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.view.BToast;
import com.bgood.xn.widget.TitleBar;
import com.umeng.analytics.MobclickAgent;

/**
 * 个性签名页面
 */
public class SignatureActivity extends BaseActivity implements TaskListenerWithState
{
    private EditText m_contentEt = null;  // 内容
    private TextView m_wordcountTv = null;  // 字数显示
    private String m_content = "";
    private UserInfoBean m_userDTO = null;
    private TitleBar mTitleBar;
    
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signature);
        
        mTitleBar = new TitleBar(mActivity);
        mTitleBar.initAllBar("编辑产品", "确定");
        mTitleBar.rightBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				checkInfo();
			}
		});
        
        m_userDTO = (UserInfoBean) getIntent().getSerializableExtra(UserInfoBean.KEY_USER_BEAN);
        findView();
    }

	/**
	 * 控件初始化方法
	 */
    private void findView()
    {
        m_contentEt = (EditText) findViewById(R.id.signature_edit_content);
        m_wordcountTv = (TextView) findViewById(R.id.signature_tv_wordcount);
        
        if (!TextUtils.isEmpty(m_userDTO.signature))
        {
        	m_contentEt.setText(m_userDTO.signature);
        	m_contentEt.setSelection(m_userDTO.signature.length());
        	m_wordcountTv.setText(m_userDTO.signature.length()+"/30");
        }
        
        
        m_contentEt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				m_wordcountTv.setText(s.length()+"/30");
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
			}
			
			@Override
			public void afterTextChanged(Editable e) {
				
			}
		});
        
        m_contentEt.setText(m_userDTO.signature); 
        m_contentEt.setSelection(m_userDTO.signature.length()); 
   }
    


    private void checkInfo() {
    		m_content = m_contentEt.getText().toString().trim();
          if (TextUtils.isEmpty(m_content))
          {
              BToast.show(mActivity, "请输入您的个性签名");
              return;
          }
          else
          {
        	  UserCenterRequest.getInstance().requestUpdatePerson(this, mActivity, "signature", m_content);
          }
	}
    

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				BToast.show(mActivity, "修改成功");
				final UserInfoBean ufb = BGApp.mUserBean;
				ufb.signature = m_content;
				BGApp.mUserBean = ufb;
				finish();
			}else{
				BToast.show(mActivity, "修改失败");
			}
		}
	}
}
