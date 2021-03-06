package com.bgood.xn.ui.user.more;
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

/**
 * 
 * @todo:意见反馈
 * @date:2014-11-1 下午2:48:49
 * @author:hg_liuzl@163.com
 */
public class FeedbackActivity extends BaseActivity implements TaskListenerWithState
{
    private EditText m_contentEt = null;  // 内容
    private EditText m_commact;//联系方式
    private TextView m_wordcountTv = null;  // 字数显示
    private String mContent;
    private String mCommact;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_layout_feedback);
        (new TitleBar(mActivity)).initTitleBar("意见反馈");
        findView();
    }
	/**
	 * 控件初始化方法
	 */
    private void findView()
    {
    	m_contentEt = (EditText) findViewById(R.id.feedback_edit_content);
    	m_commact = (EditText) findViewById(R.id.feedback_edit_commact);
        m_wordcountTv = (TextView) findViewById(R.id.feedback_tv_wordcount);
        
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
            	m_wordcountTv.setText(s.length()+"/200");
            }
        });
        
        // 确定按钮
        findViewById(R.id.feedback_btn_done).setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
            	checkContent();

            }
        });
    }
    
    
    private void checkContent() {
    	mContent = m_contentEt.getText().toString().trim();
    	mCommact = m_commact.getText().toString().trim();
    	if(TextUtils.isEmpty(mContent)){
    		 BToast.show(mActivity, "请输入您的反馈意见!");
             return;
    	}else if(TextUtils.isEmpty(mCommact)){
    		BToast.show(mActivity, "请输入您的联系方式!");
    		return;
    	}
    	UserCenterRequest.getInstance().requestFeedbackInsert(this, this, mContent, mCommact);
	}
   
	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				BToast.show(mActivity, "反馈成功");
				finish();
			}else{
				BToast.show(mActivity, "反馈失败");
			}
		}
	}
}
