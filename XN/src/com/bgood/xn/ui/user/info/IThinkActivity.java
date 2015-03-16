package com.bgood.xn.ui.user.info;

import org.json.JSONObject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.view.BToast;
import com.bgood.xn.widget.TitleBar;
import com.umeng.analytics.MobclickAgent;


/**
 * 我想页面
 */
public class IThinkActivity extends BaseActivity implements TaskListenerWithState
{
    private EditText m_contentEt = null;  // 内容
    private TextView m_wordcountTv = null;  // 字数显示
    private String mContent;
    private UserInfoBean mUserBean;
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
        setContentView(R.layout.user_layout_i_miss);
        mTitleBar = new TitleBar(mActivity);
        mTitleBar.initAllBar("我想", "确定");
        mTitleBar.rightBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checkContent();
			}
		});
        
        mUserBean = (UserInfoBean) getIntent().getSerializableExtra(UserInfoBean.KEY_USER_BEAN);;
        findView();
    }


	
	/**
	 * 控件初始化方法
	 */
    private void findView()
    {
        m_contentEt = (EditText) findViewById(R.id.imiss_edit_content);
        m_wordcountTv = (TextView) findViewById(R.id.imiss_tv_wordcount);
        if (!TextUtils.isEmpty(mUserBean.ineed))
        {
        	m_contentEt.setText(mUserBean.ineed);
        	m_contentEt.setSelection(mUserBean.ineed.length());
        	m_wordcountTv.setText(mUserBean.ineed.length()+"/30");
        }
        
        m_contentEt.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable paramEditable)
            {
            }

            public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
            {
            }

            public void onTextChanged(CharSequence s, int paramInt1, int paramInt2, int paramInt3)
            {
            	m_wordcountTv.setText(s.length()+"/30");
            }
        });
    }
    
    
    private void checkContent() {
    	mContent = m_contentEt.getText().toString().trim();
    	if(TextUtils.isEmpty(mContent)){
    		 BToast.show(mActivity, "内容不能为空!");
             return;
    	}
    	UserCenterRequest.getInstance().requestUpdatePerson(this, this, "ineed", mContent);
	}
   
	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				BToast.show(mActivity, "修改成功");
				final UserInfoBean ufb = BGApp.mUserBean;
				ufb.ineed = mContent;
				BGApp.mUserBean = ufb;
				finish();
			}else{
				BToast.show(mActivity, "修改失败");
			}
		}
	}
}
