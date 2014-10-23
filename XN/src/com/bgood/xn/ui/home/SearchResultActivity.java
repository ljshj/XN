package com.bgood.xn.ui.home;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.adapter.ResultMemberAdapter;
import com.bgood.xn.adapter.ResultShowcaseAdapter;
import com.bgood.xn.adapter.ResultWeiQiangAdapter;
import com.bgood.xn.bean.CabinetBean;
import com.bgood.xn.bean.MemberLoginBean;
import com.bgood.xn.bean.UserBean;
import com.bgood.xn.bean.WeiQiangBean;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.HttpRquestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.request.HomeRequest;
import com.bgood.xn.system.SystemConfig;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.view.xlistview.XListView.IXListViewListener;

/**
 * 搜索结果页面
 */
public class SearchResultActivity extends BaseActivity implements OnClickListener, OnItemClickListener,TaskListenerWithState,IXListViewListener
{
	
	private ViewPager mTabPager;//页卡内容
	
	public static final int CHOOSE_MEMBER = 1;	//会员类型
	public static final int CHOOSE_WEI_QIANG = CHOOSE_MEMBER + 1;  //微墙类型
	public static final int CHOOSE_CHU_CHUANG = CHOOSE_WEI_QIANG + 1; //橱窗类型
	private int REQUEST_FLAG = CHOOSE_MEMBER;
	
	private Button m_backBtn = null; // 返回按钮
	private EditText m_contentEt = null; // 输入内容

	private Button m_serachBtn = null;  // 搜索按钮
	
	private PopupWindow popupWindowCheckSearchType;
    private ViewGroup ll_home_search_check_type;
    private int search_type = 0;
    private TextView home_tv_check_search_indecator;

    private RadioGroup radio_group;
	private RadioButton radio_01,radio_02,radio_03;
	
	private ImageView img_01,img_02,img_03,no_data_img;
	
	private ResultMemberAdapter m_memberAdapter = null;
	private ResultWeiQiangAdapter m_weiqiangAdapter = null;
	private ResultShowcaseAdapter m_showcaseAdapter = null;
	
	private XListView m_memberXLv = null;   // 会员
	private XListView m_weiQiangXLv = null;    // 微墙
	private XListView m_showcaseXLv = null;    // 橱窗
	
	ArrayList<UserBean> m_memberList = new ArrayList<UserBean>();
	ArrayList<WeiQiangBean> m_weiQiangList = new ArrayList<WeiQiangBean>();
	ArrayList<CabinetBean> m_showcaseList = new ArrayList<CabinetBean>();

	private String m_msg = "";
	
	private int m_start = 0;
	private int m_memberStart = 0;
	private int m_weiqiangStart = 0;
	private int m_cabinetStart = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_search_result);
		m_msg = getIntent().getStringExtra("msg");
		findView();
		setListeners();
		HomeRequest.getInstance().requestSearch(this, this, search_type, m_msg, 114.1917953491211f, 22.636533737182617f, m_start, m_start + PAGE_SIZE_ADD);
	}

	/**
	 * 控件初始化方法
	 */
	@SuppressLint("InflateParams")
	private void findView()
	{
//		m_backBtn = (Button) findViewById(R.id.search_result_btn_back);
//		m_contentEt = (EditText) findViewById(R.id.search_result_et_content);
//		
//		m_serachBtn = (Button) findViewById(R.id.search_result_btn_search);
		
		home_tv_check_search_indecator = (TextView) findViewById(R.id.home_tv_check_search_indecator);
        ll_home_search_check_type = (ViewGroup) findViewById(R.id.ll_home_search_check_type);
        ll_home_search_check_type.setOnClickListener(this);
		m_contentEt.setText(m_msg);
		
		mTabPager = (ViewPager) findViewById(R.id.account_query_result_panel);
		//设置ViewPager的页面翻滚监听
		mTabPager.setOnPageChangeListener(new myOnPageChangeListener());
		
		radio_group = (RadioGroup) findViewById(R.id.radio_group);
		radio_group.setOnCheckedChangeListener(mOnCheckedChangeListener);
		radio_01 = (RadioButton) findViewById(R.id.radio_01);
		radio_02 = (RadioButton) findViewById(R.id.radio_02);
		radio_03 = (RadioButton) findViewById(R.id.radio_03);
		
		radio_01.setOnClickListener(radio_click);
		radio_02.setOnClickListener(radio_click);
		radio_03.setOnClickListener(radio_click);
		
		img_01 = (ImageView) findViewById(R.id.img_01);
		img_02 = (ImageView) findViewById(R.id.img_02);
		img_03 = (ImageView) findViewById(R.id.img_03);
		
		no_data_img = (ImageView) findViewById(R.id.no_data_img);
		
		//加载布局
		LayoutInflater inflater = LayoutInflater.from(this);
		View view1 = inflater.inflate(R.layout.result_listview, null);
		m_memberXLv = (XListView) view1.findViewById(R.id.xListView);
		View view2 = inflater.inflate(R.layout.result_listview, null);
		m_weiQiangXLv = (XListView) view2.findViewById(R.id.xListView);
		View view3 = inflater.inflate(R.layout.result_listview, null);
		m_showcaseXLv = (XListView) view3.findViewById(R.id.xListView);
		
		
		m_memberXLv.setPullLoadEnable(true);
		m_memberXLv.setPullRefreshEnable(false);
		m_memberXLv.setXListViewListener(this);
		m_weiQiangXLv.setPullLoadEnable(true);
		m_weiQiangXLv.setPullRefreshEnable(false);
		m_weiQiangXLv.setXListViewListener(this);
		m_showcaseXLv.setPullLoadEnable(true);
		m_showcaseXLv.setPullRefreshEnable(false);
		m_showcaseXLv.setXListViewListener(this);
		
		//将布局放入集合
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		
		//放入adapter
		PagerAdapter adapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager)container).removeView(views.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(views.get(position));
				return views.get(position);
			}
		};
		mTabPager.setAdapter(adapter);
	}

	OnClickListener radio_click = new OnClickListener() {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
				switch (view.getId()) {
				case R.id.radio_01:
					setChooseView(CHOOSE_MEMBER);
					break;
				case R.id.radio_02:
					setChooseView(CHOOSE_WEI_QIANG);
					break;
				case R.id.radio_03:
					setChooseView(CHOOSE_CHU_CHUANG);
					break;

				default:
					break;
				
			}
		}
		
	};
	
	
	/**
	 * 设置选中
	 * @param flag
	 */
	private void setChooseView(int flag){
		mTabPager.setVisibility(View.VISIBLE);
		switch (flag) {
		case CHOOSE_MEMBER:
			img_01.setVisibility(View.VISIBLE);
			img_02.setVisibility(View.INVISIBLE);
			img_03.setVisibility(View.INVISIBLE);
			break;
		case CHOOSE_WEI_QIANG:
			img_02.setVisibility(View.VISIBLE);
			img_01.setVisibility(View.INVISIBLE);
			img_03.setVisibility(View.INVISIBLE);
			break;
		case CHOOSE_CHU_CHUANG:
			img_03.setVisibility(View.VISIBLE);
			img_01.setVisibility(View.INVISIBLE);
			img_02.setVisibility(View.INVISIBLE);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 设置选中
	 * @param flag
	 */
	private void getData(int flag){
		switch (flag) {
		case CHOOSE_MEMBER:
			HomeRequest.getInstance().reqeuestMemberList(this, this, search_type, m_msg, m_start, m_start + PAGE_SIZE_ADD);
			break;
		case CHOOSE_WEI_QIANG:
			HomeRequest.getInstance().requestWeiqianList(this, this, search_type, m_msg, m_start, m_start + PAGE_SIZE_ADD);
			break;
		case CHOOSE_CHU_CHUANG:
			HomeRequest.getInstance().requestProductList(this, this, search_type, m_msg, m_start, m_start + PAGE_SIZE_ADD);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 控件事件监听方法
	 */
	private void setListeners()
	{
		m_contentEt.setOnClickListener(this);
		ll_home_search_check_type.setOnClickListener(this);
		m_backBtn.setOnClickListener(this);
		m_serachBtn.setOnClickListener(this);
		m_memberXLv.setOnItemClickListener(this);
		m_weiQiangXLv.setOnItemClickListener(this);
		m_showcaseXLv.setOnItemClickListener(this);
	}
	
	/**
     * 搜索type改变
     * @param view
     */
    private void checkSearchType(View view)
    {
        View home_iv_ican_indicator = view.findViewById(R.id.home_iv_ican_indicator);
        View home_iv_ithink_indicator = view.findViewById(R.id.home_iv_ithink_indicator);
        TextView home_tv_ican_indicator = (TextView) view.findViewById(R.id.home_tv_ican_indicator);
        TextView home_tv_ithink_indicator = (TextView) view.findViewById(R.id.home_tv_ithink_indicator);
        if (search_type == 0)
        {
            home_iv_ican_indicator.setBackgroundResource(R.drawable.img_home_seach_normal);
            home_iv_ithink_indicator.setBackgroundResource(R.drawable.img_home_seach_checked);
            home_tv_ican_indicator.setTextColor(getResources().getColor(R.color.home_check_search_normal));
            home_tv_ithink_indicator.setTextColor(getResources().getColor(R.color.home_check_search_checked));
        } else if (search_type == 1)
        {
            home_iv_ican_indicator.setBackgroundResource(R.drawable.img_home_seach_checked);
            home_iv_ithink_indicator.setBackgroundResource(R.drawable.img_home_seach_normal);
            home_tv_ican_indicator.setTextColor(getResources().getColor(R.color.home_check_search_checked));
            home_tv_ithink_indicator.setTextColor(getResources().getColor(R.color.home_check_search_normal));
        }
    }
    
	/**
	 * 
	 */
	OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			int radioButtonId = group.getCheckedRadioButtonId();
			switch (radioButtonId) {
			case R.id.radio_01:
				REQUEST_FLAG = CHOOSE_MEMBER;
				mTabPager.setCurrentItem(0);
				if(m_memberAdapter == null){
					no_data_img.setVisibility(View.VISIBLE);
					getData(REQUEST_FLAG);
				}else{
					if(m_memberAdapter.getCount() == 0){
						no_data_img.setVisibility(View.VISIBLE);
					}else{
						no_data_img.setVisibility(View.GONE);
					}
				}
				break;
			case R.id.radio_02:
				REQUEST_FLAG = CHOOSE_WEI_QIANG;
				mTabPager.setCurrentItem(1);
				if(m_weiqiangAdapter == null){
					no_data_img.setVisibility(View.VISIBLE);
					getData(REQUEST_FLAG);
				}else{
					if(m_weiqiangAdapter.getCount() == 0){
						no_data_img.setVisibility(View.VISIBLE);
					}else{
						no_data_img.setVisibility(View.GONE);
					}
				}
				break;
			case R.id.radio_03:
				REQUEST_FLAG = CHOOSE_CHU_CHUANG;
				mTabPager.setCurrentItem(2);
				if(m_showcaseAdapter == null){
					no_data_img.setVisibility(View.VISIBLE);
					getData(REQUEST_FLAG);
				}else{
					if(m_showcaseAdapter.getCount() == 0){
						no_data_img.setVisibility(View.VISIBLE);
					}else{
						no_data_img.setVisibility(View.GONE);
					}
				}
				break;
		
			default:
				break;
			}
			setChooseView(REQUEST_FLAG);
		}
	};
	
	public class myOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int i) {
			// TODO Auto-generated method stub
			RadioButton child = (RadioButton) radio_group.getChildAt(i);
			child.setChecked(true);
		}
	}

    /**
     * 设置会员数据
     * @param list
     */
    private void setMemberAdapter(ArrayList<UserBean> list)
    {
        m_memberList.addAll(list);
        
        if (m_memberAdapter == null)
        {
            m_memberAdapter = new ResultMemberAdapter(m_memberList,this);
            m_memberXLv.setAdapter(m_memberAdapter);
        }
        else
        {
            m_memberAdapter.notifyDataSetChanged();
        }
    }
    
    /**
     * 设置微墙数据
     * @param list
     */
    private void setWeiQiangAdapter(ArrayList<WeiQiangBean> list)
    {
        m_weiQiangList.addAll(list);
        if (m_weiqiangAdapter == null)
        {
            m_weiqiangAdapter = new ResultWeiQiangAdapter(m_weiQiangList,this);
            m_weiQiangXLv.setAdapter(m_weiqiangAdapter);
        }
        else
        {
            m_weiqiangAdapter.notifyDataSetChanged();
        }
    }
    
    /**
     * 设置橱窗数据
     * @param list
     */
    private void setShowcaseAdapter(ArrayList<CabinetBean> list)
    {
        m_showcaseList.addAll(list);
        if (m_showcaseAdapter == null)
        {
            m_showcaseAdapter = new ResultShowcaseAdapter(m_showcaseList,this);
            m_showcaseXLv.setAdapter(m_showcaseAdapter);
        }
        else
        {
            m_showcaseAdapter.notifyDataSetChanged();
        }
    }
	
    
    /**
     * 加载完成之后进行时间保存等方法
     */
    @SuppressLint("SimpleDateFormat")
	private void stopLoad(XListView xListView) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = format.format(new Date());
        xListView.stopRefresh();
        xListView.stopLoadMore();
        xListView.setRefreshTime(date);
        xListView.setPullRefreshEnable(false);
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
    {
        switch (adapter.getId())
        {
//            case R.id.search_result_member_xlv_list:
//            {
//                UserBean userDTO = (UserBean) adapter.getAdapter().getItem(position);
//                Intent intent = new Intent(SearchResultActivity.this, UserCardActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("user", userDTO);
//                intent.putExtras(bundle);
//                startActivity(intent);
//                break;
//            }
//            
//            case R.id.search_result_weiqiang_xlv_list:
//            {
//                WeiQiangBean weiQiangDTO = (WeiQiangBean)  adapter.getAdapter().getItem(position);
//                Intent intent = new Intent(SearchResultActivity.this, CommentDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("comment", weiQiangDTO);
//                intent.putExtras(bundle);
//                startActivity(intent);
//                break;
//            }
//            case R.id.search_result_showcase_xlv_list:
//            {
//                CabinetBean cabinetDTO = (CabinetBean) adapter.getAdapter().getItem(position);
//                Intent intent = new Intent(SearchResultActivity.this, ProductDetailActivity.class);
//                intent.putExtra("productId", cabinetDTO.cabintId);
//                startActivity(intent);
//                break;
//            }
            default:
                break;
        }
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (popupWindowCheckSearchType != null)
        {
            popupWindowCheckSearchType.dismiss();
            popupWindowCheckSearchType = null;
        }
    }

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		switch (info.getState()) {
		case STATE_ERROR_SERVER:
			Toast.makeText(mContext, "服务器地址错误", Toast.LENGTH_SHORT).show();
			break;
		case STATE_NO_NETWORK_CONNECT:
			Toast.makeText(mContext, "没有网络，请检查您的网络连接", Toast.LENGTH_SHORT).show();
			break;
		case STATE_TIME_OUT:
			Toast.makeText(mContext, "连接超时", Toast.LENGTH_SHORT).show();
			break;
		case STATE_UNKNOWN:
			Toast.makeText(mContext, "未知错误", Toast.LENGTH_SHORT).show();
			break;
		case STATE_OK:
			BaseNetWork bNetWork = info.getmBaseNetWork();
			JSONObject body = bNetWork.getBody();
			String strJson = bNetWork.getStrJson();
			try {
			switch (bNetWork.getMessageType()) {
			case 840001://搜索结果
				MemberLoginBean login = JSON.parseObject(strJson, MemberLoginBean.class);
				StringBuilder sb = new StringBuilder();
				sb.append("SessionID").append(login.userid).append("\n");
				sb.append("Success").append(login.success).append("\n");
				sb.append("token").append(login.token).append("\n");
				sb.append("BServer").append(login.bserver).append("\n");
				sb.append("FServer").append(login.fserver).append("\n");
				SystemConfig.FILE_SERVER = login.fserver;
				SystemConfig.BS_SERVER = login.bserver;
				SystemConfig.SessionID = login.userid;
				BToast.show(mContext, "消息："+sb.toString());
				break;
			case 840002:	//会员分页请求
				ArrayList<UserBean> list = (ArrayList<UserBean>) JSON.parseArray(strJson, UserBean.class);
		            if (list != null)
		            {
		                setMemberAdapter(list);
		            }
		            stopLoad(m_memberXLv);
		            if (list.size() < PAGE_SIZE_ADD)
		            {
		                m_memberXLv.setPullLoadEnable(false);
		                Toast.makeText(SearchResultActivity.this, "加载完毕！", Toast.LENGTH_LONG).show();
		            }
		            else
		            {
		                m_memberXLv.setPullLoadEnable(true);
		                m_memberStart = m_memberStart + PAGE_SIZE_ADD;
		            }
				break;
			case 840003:	//微墙请求
				ArrayList<WeiQiangBean> listWeiqiang = (ArrayList<WeiQiangBean>) JSON.parseArray(strJson, WeiQiangBean.class);
		            if (listWeiqiang != null)
		            {
		                setWeiQiangAdapter(listWeiqiang);
		            }
		            stopLoad(m_weiQiangXLv);
		            if (listWeiqiang.size() < PAGE_SIZE_ADD)
		            {
		                m_weiQiangXLv.setPullLoadEnable(false);
		                Toast.makeText(SearchResultActivity.this, "加载完毕！", Toast.LENGTH_LONG).show();
		            }
		            else
		            {
		                m_weiQiangXLv.setPullLoadEnable(true);
		                m_weiqiangStart = m_weiqiangStart + PAGE_SIZE_ADD;
		            }
				break;
			case 840004:	//橱窗请求
				ArrayList<CabinetBean> listCabinet = (ArrayList<CabinetBean>) JSON.parseArray(strJson, CabinetBean.class);

		            if (listCabinet != null)
		            {
		                setShowcaseAdapter(listCabinet);
		            }
		            stopLoad(m_showcaseXLv);
		            if (listCabinet.size() < PAGE_SIZE_ADD)
		            {
		                m_showcaseXLv.setPullLoadEnable(false);
		                Toast.makeText(SearchResultActivity.this, "加载完毕！", Toast.LENGTH_LONG).show();
		            }
		            else
		            {
		                m_showcaseXLv.setPullLoadEnable(true);
		                m_cabinetStart = m_cabinetStart + PAGE_SIZE_ADD;
		            }
				break;
			default:

				break;
			}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			break;
		default:
			break;
			}
		}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		/**
		 * @todo:TODO
		 * @date:2014-10-22 下午6:10:24
		 * @author:hg_liuzl@163.com
		 */
		
	}

	@Override
	public void onRefresh() {
		
	}

	@Override
	public void onLoadMore() {
		getData(REQUEST_FLAG);
	}
}
