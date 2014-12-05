package com.bgood.xn.ui.user.product;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.ProductRequest;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.view.BToast;
import com.bgood.xn.widget.TitleBar;

/**
 * 
 * @todo:产品评论
 * @date:2014-11-1 下午2:48:49
 * @author:hg_liuzl@163.com
 */
public class ProductCommentActivity extends BaseActivity implements TaskListenerWithState
{
    private EditText m_contentEt = null;  // 内容
    private TextView m_wordcountTv = null;  // 字数显示
    private String mContent;
    private String mCommact;
    private String product_id;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_layout_product_comment);
        (new TitleBar(mActivity)).initTitleBar("新评论");
        product_id = getIntent().getStringExtra(ProductCommentShowActivity.KEY_PRODUCT_ID);
        findView();
    }
	/**
	 * 控件初始化方法
	 */
    private void findView()
    {
    	m_contentEt = (EditText) findViewById(R.id.comment_edit_content);
        m_wordcountTv = (TextView) findViewById(R.id.comment_tv_wordcount);
        
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
        findViewById(R.id.comment_btn_done).setOnClickListener(new OnClickListener()
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
    	if(TextUtils.isEmpty(mContent)){
    		 BToast.show(mActivity, "请输入您的评论!");
             return;
    	}
    	ProductRequest.getInstance().requestProductComment(this, this, product_id, mContent);
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
