package com.bgood.xn.ui.xuanneng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.view.xlistview.XListView;

/**
 * 
 * @todo:炫能排序页
 * @date:2014-11-21 下午5:50:53
 * @author:hg_liuzl@163.com
 */
public class XuannengOrderActivity extends BaseActivity implements OnItemClickListener,TaskListenerWithState,OnClickListener
{
	private XListView m_listXlv = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_xuanneng_humor_order);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		/**
		 * @todo:TODO
		 * @date:2014-11-21 下午6:44:03
		 * @author:hg_liuzl@163.com
		 */
		
	}

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		// TODO Auto-generated method stub
		/**
		 * @todo:TODO
		 * @date:2014-11-21 下午6:44:03
		 * @author:hg_liuzl@163.com
		 */
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		/**
		 * @todo:TODO
		 * @date:2014-11-21 下午6:44:03
		 * @author:hg_liuzl@163.com
		 */
		
	}
	
	
}
