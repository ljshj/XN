package com.bgood.xn.ui.home;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import android.widget.TextView.OnEditorActionListener;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.adapter.KBaseAdapter;
import com.bgood.xn.adapter.ResultMemberAdapter;
import com.bgood.xn.adapter.ResultShowcaseAdapter;
import com.bgood.xn.adapter.ResultWeiQiangAdapter;
import com.bgood.xn.bean.CabinetResultBean;
import com.bgood.xn.bean.MemberResultBean;
import com.bgood.xn.bean.ProductBean;
import com.bgood.xn.bean.SearchResultBean;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.bean.WeiQiangBean;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.HomeRequest;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.ui.user.info.NameCardActivity;
import com.bgood.xn.ui.user.product.ProductDetailActivity;
import com.bgood.xn.ui.weiqiang.WeiqiangDetailActivity;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.view.xlistview.XListView.IXListViewListener;
import com.bgood.xn.widget.TitleBar;

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
	
	private EditText m_contentEt = null; // 输入内容

	private Button m_serachBtn = null;  // 搜索按钮
	private TextView home_tv_check_search_indecator;
	
	private PopupWindow popupWindowCheckSearchType;
    private ViewGroup ll_home_search_check_type;
    
    private int search_type = 0;

    private RadioGroup radio_group;
	
	private ImageView img_01,img_02,img_03,no_data_img;
	
	private ResultMemberAdapter m_memberAdapter = null;
	private ResultWeiQiangAdapter m_weiqiangAdapter = null;
	private ResultShowcaseAdapter m_showcaseAdapter = null;
	
	private XListView m_memberXLv = null;   // 会员
	private XListView m_weiQiangXLv = null;    // 微墙
	private XListView m_showcaseXLv = null;    // 橱窗
	
	ArrayList<MemberResultBean> m_memberList = new ArrayList<MemberResultBean>();
	ArrayList<WeiQiangBean> m_weiQiangList = new ArrayList<WeiQiangBean>();
	ArrayList<CabinetResultBean> m_showcaseList = new ArrayList<CabinetResultBean>();

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
		new TitleBar(mActivity).initTitleBar("搜索结果");
		search_type = getIntent().getIntExtra(HomeFragment.ACTION_TYPE, 0);
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
		m_contentEt = (EditText) findViewById(R.id.search_result_et_content);
		m_contentEt.setText(m_msg);
		
		m_contentEt.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionID, KeyEvent arg2) {
				if(actionID == EditorInfo.IME_ACTION_SEARCH){
					doSearch();
				}
				return false;
			}
		});
		
		
		
		m_serachBtn = (Button) findViewById(R.id.search_result_btn_search);
		
        ll_home_search_check_type = (ViewGroup) findViewById(R.id.ll_home_search_check_type);
        ll_home_search_check_type.setOnClickListener(this);
		
		home_tv_check_search_indecator = (TextView) findViewById(R.id.home_tv_check_search_indecator);
		
		mTabPager = (ViewPager) findViewById(R.id.account_query_result_panel);
		//设置ViewPager的页面翻滚监听
		mTabPager.setOnPageChangeListener(new myOnPageChangeListener());
		
		radio_group = (RadioGroup) findViewById(R.id.radio_group);
		radio_group.setOnCheckedChangeListener(mOnCheckedChangeListener);
		
		img_01 = (ImageView) findViewById(R.id.img_01);
		img_02 = (ImageView) findViewById(R.id.img_02);
		img_03 = (ImageView) findViewById(R.id.img_03);
		
		no_data_img = (ImageView) findViewById(R.id.no_data_img);
		
		//加载布局
		LayoutInflater inflater = LayoutInflater.from(this);

		View view1 = inflater.inflate(R.layout.home_layout_result_listview, null);
		m_memberXLv = (XListView) view1.findViewById(R.id.xListView);
		m_memberXLv.setPullLoadEnable(true);
		m_memberXLv.setPullRefreshEnable(false);
		m_memberXLv.setXListViewListener(this);
		m_memberAdapter = new ResultMemberAdapter(m_memberList, mActivity);
		m_memberXLv.setAdapter(m_memberAdapter);
		
		View view2 = inflater.inflate(R.layout.home_layout_result_listview, null);
		m_weiQiangXLv = (XListView) view2.findViewById(R.id.xListView);
		m_weiQiangXLv.setPullLoadEnable(true);
		m_weiQiangXLv.setPullRefreshEnable(false);
		m_weiQiangXLv.setXListViewListener(this);
		m_weiqiangAdapter = new ResultWeiQiangAdapter(m_weiQiangList, mActivity);
		m_weiQiangXLv.setAdapter(m_weiqiangAdapter);
		
		View view3 = inflater.inflate(R.layout.home_layout_result_listview, null);
		m_showcaseXLv = (XListView) view3.findViewById(R.id.xListView);
		m_showcaseXLv.setPullLoadEnable(true);
		m_showcaseXLv.setPullRefreshEnable(false);
		m_showcaseXLv.setXListViewListener(this);
		m_showcaseAdapter = new ResultShowcaseAdapter(m_showcaseList, mActivity);
		m_showcaseXLv.setAdapter(m_showcaseAdapter);
		
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
			HomeRequest.getInstance().reqeuestMemberList(this, this, search_type, m_msg, m_memberStart, m_memberStart + PAGE_SIZE_ADD);
			break;
		case CHOOSE_WEI_QIANG:
			HomeRequest.getInstance().requestWeiqianList(this, this, search_type, m_msg, m_weiqiangStart, m_weiqiangStart + PAGE_SIZE_ADD);
			break;
		case CHOOSE_CHU_CHUANG:
			HomeRequest.getInstance().requestProductList(this, this, search_type, m_msg, m_cabinetStart, m_cabinetStart + PAGE_SIZE_ADD);
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
		ll_home_search_check_type.setOnClickListener(this);
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
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void setDataAdapter(XListView xListView,KBaseAdapter adapter,List<?> showList,List resultlist,int start_page)
    {
    	if(null == resultlist || resultlist.size() ==0)
    	{
    		xListView.setPullLoadEnable(false);
            BToast.show(mActivity, "数据加载完毕");
    		return;
    	}
    	
    	 if (resultlist.size() < PAGE_SIZE_ADD)
         {
    		 xListView.setPullLoadEnable(false);
             BToast.show(mActivity, "数据加载完毕");
         }else
         {
        	 xListView.setPullLoadEnable(true);
        	 start_page +=PAGE_SIZE_ADD;
         }
    	 showList.addAll(resultlist);
         adapter.notifyDataSetChanged();
    }
    
    /**
     * 加载完成之后进行时间保存等方法
     */
    @SuppressLint("SimpleDateFormat")
	private void stopLoad(XListView xListView) {
        xListView.stopRefresh();
        xListView.stopLoadMore();
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
    {
    	 Intent intent = null;
        switch (REQUEST_FLAG)
        {
            case CHOOSE_MEMBER:
            	final MemberResultBean userDTO = (MemberResultBean) adapter.getAdapter().getItem(position);
                intent = new Intent(SearchResultActivity.this, NameCardActivity.class);
                intent.putExtra(UserInfoBean.KEY_USER_ID, userDTO.userid);
                startActivity(intent);
                break;
            
            case CHOOSE_WEI_QIANG:
            	final WeiQiangBean weiQiangDTO = (WeiQiangBean)  adapter.getAdapter().getItem(position);
                intent = new Intent(SearchResultActivity.this, WeiqiangDetailActivity.class);
                intent.putExtra(WeiQiangBean.KEY_WEIQIANG_BEAN, weiQiangDTO);
                startActivity(intent);
                break;
            case CHOOSE_CHU_CHUANG:
            	final CabinetResultBean cabinetDTO = (CabinetResultBean) adapter.getAdapter().getItem(position);
                intent = new Intent(SearchResultActivity.this, ProductDetailActivity.class);
                intent.putExtra(ProductBean.KEY_PRODUCT_ID, cabinetDTO.productid);
                startActivity(intent);
                break;
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
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			String strJson = bNetWork.getStrJson();
			stopLoad(m_memberXLv);
			stopLoad(m_weiQiangXLv);
			stopLoad(m_showcaseXLv);
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
			try {
			switch (bNetWork.getMessageType()) {
			case 840001://搜索结果
				SearchResultBean resultBean = JSON.parseObject(strJson, SearchResultBean.class);
				setDataAdapter(m_memberXLv, m_memberAdapter, m_memberList, resultBean.members, m_memberStart);
				setDataAdapter(m_weiQiangXLv, m_weiqiangAdapter, m_weiQiangList, resultBean.weiqiang, m_weiqiangStart);
				setDataAdapter(m_showcaseXLv, m_showcaseAdapter, m_showcaseList, resultBean.cabinet, m_cabinetStart);
				break;
			case 840002:	//会员分页请求
				ArrayList<MemberResultBean> listMember = (ArrayList<MemberResultBean>) JSON.parseArray(strJson, MemberResultBean.class);
				setDataAdapter(m_memberXLv, m_memberAdapter, m_memberList, listMember, m_memberStart);
				break;
			case 840003:	//微墙请求
				ArrayList<WeiQiangBean> listWeiqiang = (ArrayList<WeiQiangBean>) JSON.parseArray(strJson, WeiQiangBean.class);
				setDataAdapter(m_weiQiangXLv, m_weiqiangAdapter, m_weiQiangList, listWeiqiang, m_weiqiangStart);
				break;
			case 840004:	//橱窗请求
				ArrayList<CabinetResultBean> listCabinet = (ArrayList<CabinetResultBean>) JSON.parseArray(strJson, CabinetResultBean.class);
				setDataAdapter(m_showcaseXLv, m_showcaseAdapter, m_showcaseList, listCabinet, m_cabinetStart);
				break;
			default:

				break;
			}
			} catch (Exception e) {
				e.printStackTrace();
			}}}
}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_home_search_check_type:
            View view = inflater.inflate(R.layout.popup_home_check_seach, null);
            checkSearchType(view);
            view.findViewById(R.id.home_ll_ican).setOnClickListener(this);
            view.findViewById(R.id.home_ll_ithink).setOnClickListener(this);
            popupWindowCheckSearchType = null;
            popupWindowCheckSearchType = new PopupWindow(view, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindowCheckSearchType.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_home_check_popup));
            popupWindowCheckSearchType.setOutsideTouchable(true);
            popupWindowCheckSearchType.showAsDropDown(ll_home_search_check_type, 0, 10);
            break;
		  case R.id.home_ll_ican:
	            search_type = 1;
	            home_tv_check_search_indecator.setText("我能");
	            popupWindowCheckSearchType.dismiss();
	            break;
	        case R.id.home_ll_ithink:
	            search_type = 0;
	            home_tv_check_search_indecator.setText("我想");
	            popupWindowCheckSearchType.dismiss();
	            break;
		case R.id.search_result_btn_search:
			doSearch();
			break;

		default:
			break;
		}
	}
	
	private void doSearch(){
		final String msg = m_contentEt.getText().toString().trim();
		    if (!TextUtils.isEmpty(msg)){
		        m_msg = msg;
		        m_start = 0;
		        m_memberStart = 0;
		        m_weiqiangStart = 0;
		        m_cabinetStart = 0;
		        m_memberList.clear();
		        m_weiQiangList.clear();
		        m_showcaseList.clear();
				HomeRequest.getInstance().requestSearch(this, this, search_type, m_msg, 114.1917953491211f, 22.636533737182617f, m_start, m_start + PAGE_SIZE_ADD);
		    }else{
		    		BToast.show(mActivity, "请输入搜索内容");
		        return;
		    }
	}

	@Override
	public void onRefresh() {
		
	}

	@Override
	public void onLoadMore() {
		getData(REQUEST_FLAG);
	}
}
