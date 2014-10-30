package com.bgood.xn.ui.user.info;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.view.BToast;
import com.bgood.xn.widget.TitleBar;

/**
 * 个性签名页面
 */
public class SignatureActivity extends BaseActivity implements TaskListenerWithState
{
    private EditText m_contentEt = null;  // 内容
    private TextView m_wordcountTv = null;  // 字数显示
    private String m_content = "";
    private UserInfoBean m_userDTO = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signature);
        (new TitleBar(mActivity)).initTitleBar("个性签名");
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
        m_contentEt.setText(m_userDTO.signature); 
        m_contentEt.setSelection(m_userDTO.signature.length()); 
       findViewById(R.id.signature_btn_done).setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			checkInfo();
		}
	});
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
				Intent intent = getIntent();
				intent.putExtra("signature", m_content);
	            setResult(RESULT_OK, intent);
				finish();
			}else{
				BToast.show(mActivity, "修改失败");
			}
		}
	}
}
