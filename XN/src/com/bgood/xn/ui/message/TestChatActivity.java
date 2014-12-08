package com.bgood.xn.ui.message;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.bgood.xn.R;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.ui.base.BaseActivity;

/**
 * @todo:TODO
 * @date:2014-12-8 下午5:48:43
 * @author:hg_liuzl@163.com
 */
public class TestChatActivity extends BaseActivity implements TaskListenerWithState {

	private EditText edText;
	private Button btnSend;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_chat_test);
		findViewById(R.id.et_content);
		findViewById(R.id.btn_send);
	}
	
	
	
	
	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
	}
	
}
