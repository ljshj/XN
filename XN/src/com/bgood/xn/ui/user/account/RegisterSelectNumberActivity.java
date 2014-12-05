package com.bgood.xn.ui.user.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bgood.xn.R;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.utils.WindowUtil;
import com.bgood.xn.widget.TitleBar;

/**
 * 
 * @todo:注册，选择能能号
 * @date:2014-12-2 下午6:33:13
 * @author:hg_liuzl@163.com
 */
public class RegisterSelectNumberActivity extends BaseActivity implements OnItemClickListener,TaskListenerWithState
{

	private ListView m_listLv = null; // 列表
	private String phone;
	private String[] ids;
	private SimpleAdapter adapter;
    private TitleBar titleBar = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_register_select_number);
		titleBar = new TitleBar(mActivity);
	    titleBar.initAllBar("选择邦固号", "换一批");
		findView();
	}

	/**
	 * 控件初始化方法
	 */
	private void findView()
	{
		phone = getIntent().getStringExtra("phone");
		ids = getIntent().getStringArrayExtra("ids");
		m_listLv = (ListView) findViewById(R.id.register_select_number_lv_list);
		m_listLv.setOnItemClickListener(this);
		setAdapter();
        titleBar.rightBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            	UserCenterRequest.getInstance().requestChangeBGID(RegisterSelectNumberActivity.this, mActivity, phone);
            }
        });
	}


	private void setAdapter()
	{
		List<Map<String, Object>> contents = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < ids.length; i++)
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", ids[i]);
			contents.add(map);
		}
		adapter = new SimpleAdapter(this, (List<Map<String, Object>>) contents, R.layout.layout_register_select_number_item, new String[]
		{ "id" }, new int[]
		{ R.id.register_select_number_item_tv_name });
		m_listLv.setAdapter(adapter);
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Bundle bundle = new Bundle();
		bundle.putString("phone", phone);
		bundle.putString("userID", ((Map) adapter.getItem(position)).get("id").toString());
		toActivity(RegisterDoneActivity.class, bundle);
	}


	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();

			JSONObject body = bNetWork.getBody();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				try {
					JSONArray jsonArray = body.getJSONArray("ids");
					if (jsonArray != null)
					{
						ids = new String[jsonArray.length()];
						for (int i = 0; i < jsonArray.length(); i++)
						{
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							ids[i] = jsonObject.getString("id");
						}
					}
					setAdapter();
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
