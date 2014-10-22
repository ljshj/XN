//package com.bgood.xn.ui.home;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ProgressBar;
//
//import com.bgood.xn.R;
//import com.bgood.xn.adapter.WeiqiangAdapter;
//import com.bgood.xn.network.BaseNetWork.ReturnCode;
//import com.bgood.xn.ui.BaseActivity;
//import com.bgood.xn.view.xlistview.XListView;
//
///**
// * 我的微墙页面
// */
//public class TaWeiqiangActivity extends BaseActivity implements OnItemClickListener
//{
//	private XListView m_weiqiang_person_listview;
//	private ProgressBar m_progressBar;
//	private List<CommentsDTO> m_list = new ArrayList<CommentsDTO>();
//	private WeiqiangAdapter adapter;
//	private int start = 0;
//	ShareManager   m_shareManager;
//	
//	private UserDTO user;
//	
//	WeiqiangMessageManager messageManager = WeiqiangMessageManager.getInstance();
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.layout_weiqiang_person);
//		
//		getIntentData();
//		initViews();
//		setListeners();
//		initData();
//		
//		
//	}
//	
//	@Override
//    public void onResume()
//    {
//        super.onResume();
//        
//        if (messageManager != null)
//        {
//            // 消息注册
//            messageManager.registerObserver(this);
//        }
//    }
//
//    @Override
//    public void onPause()
//    {
//        super.onPause();
//        
//        if (messageManager != null)
//        {
//            // 消息注销
//            messageManager.unregisterObserver(this);
//        }
//    }
//	
//	/**
//     * 得到传值数据方法
//     */
//    private void getIntentData()
//    {
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null)
//        {
//            user = (UserDTO) bundle.getSerializable("user");
//        }
//    }
//
//	@Override
//	protected void initData()
//	{
//		super.initData();
//		messageManager.getWeiqiangList(0, start, start + 10);
//	}
//
//	@Override
//	public void getWeiqiangListCB(List<CommentsDTO> list, Reulst result_state, int tpye)
//	{
//		super.getWeiqiangListCB(list, result_state, tpye);
//		if (result_state.resultCode == ReturnCode.RETURNCODE_OK)
//		{
//			m_progressBar.setVisibility(View.GONE);
//			m_weiqiang_person_listview.setVisibility(View.VISIBLE);
//			start = start + 10;
//			
//			if (list != null)
//			{
//				if (list.size() < 10)
//				{
//					m_weiqiang_person_listview.setPullLoadEnable(false);
//				}
//				else
//				{
//					m_weiqiang_person_listview.setPullLoadEnable(true);
//				}
//				// TODO 加载更多 上啦刷新
//				this.m_list.addAll(list);
//			}
//			else
//			{
//				m_weiqiang_person_listview.setPullLoadEnable(false);
//			}
//			adapter.notifyDataSetChanged();
//		}
//	}
//
//	// 如果有使用任一平台的SSO授权或者集成了facebook平台, 则必须在对应的activity中实现onActivityResult方法,
//    // 并添加如下代码
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (data != null)
//        {
//            m_shareManager.onActivityResult(requestCode, resultCode, data);
//        }
//    }
//	
//	@Override
//	protected void initViews()
//	{
//		super.initViews();
//		m_weiqiang_person_listview = (XListView) findViewById(R.id.weiqiang_person_listview);
//		m_progressBar = (ProgressBar) findViewById(R.id.weiqiang_person_progress);
//		adapter = new WeiqiangAdapter(this, m_list);
//		m_weiqiang_person_listview.setAdapter(adapter);
//		
//		m_weiqiang_person_listview.setOnItemClickListener(this);
//	}
//
//	@Override
//	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
//	{
//		CommentsDTO commentsDTO = (CommentsDTO) arg0.getAdapter().getItem(arg2);
//		Intent intent = new Intent(TaWeiqiangActivity.this, WeiqiangCommentDetailActivity.class);
//		Bundle bundle = new Bundle();
//		bundle.putSerializable("comment", commentsDTO);
//		intent.putExtras(bundle);
//		startActivity(intent);
//	}
//}
