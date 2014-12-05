package com.bgood.xn.ui.user.info;

import org.json.JSONObject;

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
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.view.BToast;
import com.bgood.xn.widget.TitleBar;


/**
 * 我能页面
 */
public class ICanActivity extends BaseActivity implements TaskListenerWithState
{
    private EditText m_contentEt = null;  // 内容
    private TextView tvWordCount;	//字数显示
    private UserInfoBean mUserBean = null;
    private String mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_layout_i_can);
        (new TitleBar(mActivity)).initTitleBar("我能");
        mUserBean = (UserInfoBean) getIntent().getSerializableExtra(UserInfoBean.KEY_USER_BEAN);;
        findView();
    }
	/**
	 * 控件初始化方法
	 */
    private void findView()
    {
        m_contentEt = (EditText) findViewById(R.id.ican_edit_content);
        tvWordCount = (TextView) findViewById(R.id.ican_tv_wordcount);
        
        if (!TextUtils.isEmpty(mUserBean.ican))
        {
        	m_contentEt.setText(mUserBean.ican);
        	m_contentEt.setSelection(mUserBean.ican.length());
        	tvWordCount.setText(mUserBean.ican.length()+"/30");
        }
        
        
        m_contentEt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				tvWordCount.setText(s.length()+"/30");
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
			}
			
			@Override
			public void afterTextChanged(Editable e) {
				
			}
		});
        
        findViewById(R.id.ican_btn_done).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checkContent();
			}
		});
    }
    
    private void checkContent() {
    	mContent = m_contentEt.getText().toString().trim();
    	if(TextUtils.isEmpty(mContent)){
    		 BToast.show(mActivity, "内容不能为空!");
             return;
    	}
    	UserCenterRequest.getInstance().requestUpdatePerson(this, this, "ican", mContent);
	}
    
	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				BToast.show(mActivity, "修改成功");
				final UserInfoBean ufb = BGApp.mUserBean;
				ufb.ican = mContent;
				BGApp.mUserBean = ufb;
				finish();
			}else{
				BToast.show(mActivity, "修改失败");
			}
		}
	}
}
