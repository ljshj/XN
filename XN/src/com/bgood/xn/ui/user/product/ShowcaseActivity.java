package com.bgood.xn.ui.user.product;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.adapter.ShowcaseAllProductAdapter;
import com.bgood.xn.adapter.ShowcaseRecommendAdapter;
import com.bgood.xn.bean.ProductBean;
import com.bgood.xn.bean.ShowcaseBean;
import com.bgood.xn.bean.response.ProductResponse;
import com.bgood.xn.db.PreferenceUtil;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.ProductRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.CBaseSlidingMenu;
import com.bgood.xn.view.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.bgood.xn.view.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.bgood.xn.view.xlistview.XListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/**
 * 
 * @todo:我的橱窗界面
 * @date:2014-11-17 上午10:06:49
 * @author:hg_liuzl@163.com
 */
public class ShowcaseActivity extends CBaseSlidingMenu implements OnClickListener, OnItemClickListener,TaskListenerWithState
{
	/**传入的用户编号**/
	public static final String KEY_USER_ID = "key_user_id";
	private Button m_backBtn = null; // 返回按钮
	private Button m_moreBtn = null; // 更多按钮
	private TextView tvTitle = null;	//橱窗标题
	private FrameLayout m_backgroundFl = null; // 背景
	private EditText m_searchEt = null; // 搜索输入框
	private ImageView m_showcaseIconImgV = null; // 橱窗图片
	private TextView m_showcaseNameTv = null; // 橱窗名
	private TextView m_commentsTv = null; // 评论值
	private ImageView m_oneHintImgV = null; // 第一个心
	private ImageView m_twoHintImgV = null; // 第二个心
	private ImageView m_threeHintImgV = null; // 第三个心
	private ImageView m_fourHintImgV = null; // 第四个心
	private ImageView m_fiveHintImgV = null; // 第五个心
	private LinearLayout m_recommendLl = null; // 产品推荐
	private TextView m_recommendHintTv = null; // 推荐提示
	private TextView m_recommendTv = null; // 产品推荐值
	private LinearLayout m_allProductLl = null; // 全部产品
	private TextView m_allProductHintTv = null; // 全部产品提示
	private TextView m_allProductTv = null; // 全部产品值
	private XListView m_recommendXLv = null;  // 推荐产品
	private XListView m_allProductXLv = null;  // 所有产品
	private boolean m_menuShowing; // 右菜单是否显示
	
	private ShowcaseRecommendAdapter m_recommendAdapter = null;    // 推荐商品适配器
	private ShowcaseAllProductAdapter m_allProductAdapter = null;  // 所有商品适配器
	
	
	private String mUserId = null;
	
	private int m_start_page = 0;
	private int m_add_pagesize = BaseActivity.PAGE_SIZE_ADD;
	
	/**是不是自己*/
	private boolean isSelf = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_showcase);
		mUserId = getIntent().getStringExtra(KEY_USER_ID);
		isSelf = mUserId.equals(BGApp.mUserId)?true:false;
		findView();
		setListener();
		setRightMenu();
		ProductRequest.getInstance().requestShowCase(this, this, mUserId);
	}
	/**
	 * 控件初始化方法
	 */
	private void findView()
	{
		m_backBtn = (Button) findViewById(R.id.showcase_btn_back);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText(isSelf?"我的橱窗":"TA的橱窗");
		m_moreBtn = (Button) findViewById(R.id.showcase_btn_more);
		m_moreBtn.setVisibility(isSelf?View.VISIBLE:View.GONE);
		
		m_backgroundFl = (FrameLayout) findViewById(R.id.showcase_fl_background);
		m_searchEt = (EditText) findViewById(R.id.showcase_et_search);
		m_showcaseIconImgV = (ImageView) findViewById(R.id.showcase_imgv_showcase_icon);
		m_showcaseNameTv = (TextView) findViewById(R.id.showcase_tv_shop_name);
		m_commentsTv = (TextView) findViewById(R.id.showcase_tv_comments);
		m_oneHintImgV = (ImageView) findViewById(R.id.showcase_imgv_one);
		m_twoHintImgV = (ImageView) findViewById(R.id.showcase_imgv_two);
		m_threeHintImgV = (ImageView) findViewById(R.id.showcase_imgv_three);
		m_fourHintImgV = (ImageView) findViewById(R.id.showcase_imgv_four);
		m_fiveHintImgV = (ImageView) findViewById(R.id.showcase_imgv_five);
		m_recommendLl = (LinearLayout) findViewById(R.id.showcase_ll_recommend);
		m_recommendHintTv = (TextView) findViewById(R.id.showcase_tv_recommend_hint);
		m_recommendTv = (TextView) findViewById(R.id.showcase_tv_recommend);
		m_allProductLl = (LinearLayout) findViewById(R.id.showcase_ll_all_product);
		m_allProductHintTv = (TextView) findViewById(R.id.showcase_tv_all_product_hint);
		m_allProductTv = (TextView) findViewById(R.id.showcase_tv_all_product);
		
		m_recommendXLv = (XListView) findViewById(R.id.showcase_xlv_recommend);
		m_recommendXLv.setOnItemClickListener(this);
		m_recommendXLv.setPullRefreshEnable(false);
		m_recommendXLv.setPullLoadEnable(false);
		
		m_allProductXLv = (XListView) findViewById(R.id.showcase_xlv_all_product);
		m_allProductXLv.setPullRefreshEnable(false);
		m_allProductXLv.setOnItemClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setBackgroundColor();
	}

	/**
	 * 控件事件监听方法
	 */
	private void setListener()
	{
		m_backBtn.setOnClickListener(this);
		m_recommendLl.setOnClickListener(this);
		m_allProductLl.setOnClickListener(this);

		m_moreBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (m_menuShowing)
				{
					m_slidingMenu.showContent();
				} else
				{
					m_slidingMenu.showSecondaryMenu();
				}
			}
		});
		
		m_searchEt.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView tv, int actionId, KeyEvent arg2) {
				if(actionId == EditorInfo.IME_ACTION_SEARCH){
					String searchContent = m_searchEt.getText().toString().trim();
                    Intent intent = new Intent(ShowcaseActivity.this, ProductListActivity.class);
                    intent.putExtra("content", searchContent);
                    intent.putExtra("userid", String.valueOf(BGApp.mLoginBean.userid));
                    startActivity(intent);
				}
				return false;
			}
		});
	}

	/**
	 * 设置颜色方法
	 */
	private void setBackgroundColor()
	{
		PreferenceUtil pUtil =  new PreferenceUtil(this, PreferenceUtil.PREFERENCE_FILE);
		
		
	    switch (pUtil.getSelfTemplat())
        {
            case 1:
                m_backgroundFl.setBackgroundColor(getResources().getColor(R.color.individuation_blue1));
                break;
            case 2:
                m_backgroundFl.setBackgroundColor(getResources().getColor(R.color.individuation_white2));
                break;
            case 3:
                m_backgroundFl.setBackgroundColor(getResources().getColor(R.color.individuation_purple3));
                break;
            case 4:
                m_backgroundFl.setBackgroundColor(getResources().getColor(R.color.individuation_green4));
                break;
            case 5:
                m_backgroundFl.setBackgroundColor(getResources().getColor(R.color.individuation_lightblue5));
                break;
            case 6:
                m_backgroundFl.setBackgroundColor(getResources().getColor(R.color.individuation_yellow6));
                break;
            default:
                break;
        }
	}
	
	/**
	 * 设置右菜单
	 **/
	private void setRightMenu()
	{
		m_slidingMenu.setSecondaryMenu(R.layout.right_frame);
		m_slidingMenu.setOnOpenedListener(new OnOpenedListener()
		{

			@Override
			public void onOpened()
			{
				m_menuShowing = true;
			}
		});
		m_slidingMenu.setOnClosedListener(new OnClosedListener()
		{

			@Override
			public void onClosed()
			{
				m_menuShowing = false;
			}
		});

	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
    		// 返回
    		case R.id.showcase_btn_back:
    			ShowcaseActivity.this.finish();
    			break;
    		// 推荐产品
    		case R.id.showcase_ll_recommend:
    			setProduct(0);
    			break;
    		// 全部产品
    		case R.id.showcase_ll_all_product:
    			if(null == m_allProductAdapter){	//如果m_allProductAdapter 为null,说明还没有做初始化工作
    				ProductRequest.getInstance().requestProductList(this, this, mUserId, "", String.valueOf(m_start_page), String.valueOf(m_start_page+m_add_pagesize));
    			}
    			setProduct(1);
    			break;
    		default:
    			break;
		}
	}

	/**
	 * 设置橱窗信誉度
	 * 
	 * @param index
	 *            信用值
	 */
	private void setCredibility(int index)
	{
		switch (index)
		{
			case 1:
				m_oneHintImgV.setVisibility(View.VISIBLE);
				m_twoHintImgV.setVisibility(View.INVISIBLE);
				m_threeHintImgV.setVisibility(View.INVISIBLE);
				m_fourHintImgV.setVisibility(View.INVISIBLE);
				m_fiveHintImgV.setVisibility(View.INVISIBLE);
				break;
			case 2:
				m_oneHintImgV.setVisibility(View.VISIBLE);
				m_twoHintImgV.setVisibility(View.VISIBLE);
				m_threeHintImgV.setVisibility(View.INVISIBLE);
				m_fourHintImgV.setVisibility(View.INVISIBLE);
				m_fiveHintImgV.setVisibility(View.INVISIBLE);
				break;
			case 3:
				m_oneHintImgV.setVisibility(View.VISIBLE);
				m_twoHintImgV.setVisibility(View.VISIBLE);
				m_threeHintImgV.setVisibility(View.VISIBLE);
				m_fourHintImgV.setVisibility(View.INVISIBLE);
				m_fiveHintImgV.setVisibility(View.INVISIBLE);
				break;
			case 4:
				m_oneHintImgV.setVisibility(View.VISIBLE);
				m_twoHintImgV.setVisibility(View.VISIBLE);
				m_threeHintImgV.setVisibility(View.VISIBLE);
				m_fourHintImgV.setVisibility(View.VISIBLE);
				m_fiveHintImgV.setVisibility(View.INVISIBLE);
				break;
			case 5:
				m_oneHintImgV.setVisibility(View.VISIBLE);
				m_twoHintImgV.setVisibility(View.VISIBLE);
				m_threeHintImgV.setVisibility(View.VISIBLE);
				m_fourHintImgV.setVisibility(View.VISIBLE);
				m_fiveHintImgV.setVisibility(View.VISIBLE);
				break;
	
			default:
				m_oneHintImgV.setVisibility(View.INVISIBLE);
				m_twoHintImgV.setVisibility(View.INVISIBLE);
				m_threeHintImgV.setVisibility(View.INVISIBLE);
				m_fourHintImgV.setVisibility(View.INVISIBLE);
				m_fiveHintImgV.setVisibility(View.INVISIBLE);
				break;
		}
	}

	/**
	 * 显示产品
	 * 
	 * @param index
	 */
	private void setProduct(int index)
	{
		switch (index)
		{
		case 0:
			m_recommendHintTv.setTextColor(getResources().getColor(R.color.red));
			m_recommendTv.setTextColor(getResources().getColor(R.color.red));
			m_allProductHintTv.setTextColor(getResources().getColor(R.color.black));
			m_allProductTv.setTextColor(getResources().getColor(R.color.black));
			m_recommendXLv.setVisibility(View.VISIBLE);
			m_allProductXLv.setVisibility(View.GONE);
			break;
		case 1:
			m_recommendHintTv.setTextColor(getResources().getColor(R.color.black));
			m_recommendTv.setTextColor(getResources().getColor(R.color.black));
			m_allProductHintTv.setTextColor(getResources().getColor(R.color.red));
			m_allProductTv.setTextColor(getResources().getColor(R.color.red));
			m_recommendXLv.setVisibility(View.GONE);
			m_allProductXLv.setVisibility(View.VISIBLE);
			
			break;
		default:
			break;
		}
	}
	
	/**
	 * 进行推荐商品数据显示
	 * @param list 数据源
	 */
	private void setRecommendAdapter(List<ProductBean> list)
	{
		m_recommendXLv.stopLoadMore();
		m_recommendXLv.stopRefresh();
		
		if(null == list){
			return;
		}
		
		if (m_recommendAdapter == null)
		{
			m_recommendAdapter = new ShowcaseRecommendAdapter(list,ShowcaseActivity.this);
			m_recommendXLv.setAdapter(m_recommendAdapter);
		}
		else
		{
			m_recommendAdapter.notifyDataSetChanged();
		}
		
	}
	
	/**
	 * 进行所有商品数据显示
	 * @param list 数据源
	 */
	private void setAllProductAdapter(List<ProductBean> list)
	{
		
		m_allProductXLv.stopLoadMore();
		m_allProductXLv.stopRefresh();		
		
		if(null == list){
			return;
		}
		
		if(list.size() < m_add_pagesize){
			BToast.show(this, "数据加载完毕");
			m_allProductXLv.setPullLoadEnable(false);
		}else{
			m_start_page += m_add_pagesize;
			m_allProductXLv.setPullLoadEnable(true);
		}
		
		if (m_allProductAdapter == null)
		{
			m_allProductAdapter = new ShowcaseAllProductAdapter(list,ShowcaseActivity.this);
			m_allProductXLv.setAdapter(m_allProductAdapter);
		}
		else
		{
			m_allProductAdapter.notifyDataSetChanged();
		}
		
	}
	
	/**
	 * 设置数据显示方法
	 * @param showcaseDTO 说明参数含义
	 */
	private void setData(ShowcaseBean showcaseDTO)
	{
			setCredibility(!TextUtils.isEmpty(showcaseDTO.credit)?Integer.parseInt(showcaseDTO.credit):0);
			
			ImageLoader mImageLoader;
			DisplayImageOptions options;
			options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.icon_default)
			.showImageForEmptyUri(R.drawable.icon_default)
			.cacheInMemory()
			.cacheOnDisc()
			.build();
			mImageLoader = ImageLoader.getInstance();
			mImageLoader.init(ImageLoaderConfiguration.createDefault(this));
			
	        mImageLoader.displayImage(showcaseDTO.logo,m_showcaseIconImgV, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingComplete() {
					Animation anim = AnimationUtils.loadAnimation(ShowcaseActivity.this, R.anim.fade_in);
					m_showcaseIconImgV.setAnimation(anim);
					anim.start();
				}
			});
			
			m_recommendTv.setText("("+(showcaseDTO.recommend_list == null?"0":String.valueOf(showcaseDTO.recommend_list.size()))+")");
			
			m_allProductTv.setText("("+showcaseDTO.product_count+")");
			m_commentsTv.setText(showcaseDTO.good_comments);
			
			m_showcaseNameTv.setText(showcaseDTO.shop_name);
			setRecommendAdapter(showcaseDTO.recommend_list);
			
	}
    @Override
    public void onItemClick(AdapterView<?> adapter, View v, int location, long arg3)
    {
        ProductBean productDTO = (ProductBean)adapter.getAdapter().getItem(location);
        Intent intent = new Intent(ShowcaseActivity.this, ProductDetailActivity.class);
        intent.putExtra(ProductBean.KEY_PRODUCT_ID,productDTO.product_id);
        startActivity(intent);
    }
	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			String strJson = bNetWork.getStrJson();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				switch (bNetWork.getMessageType()) {
				case 830001:
					final ShowcaseBean mShowcaseBean = JSON.parseObject(strJson, ShowcaseBean.class);
					setData(mShowcaseBean);
					break;
				case 830008:
					final ProductResponse response = JSON.parseObject(strJson, ProductResponse.class);
					setAllProductAdapter(response.products);
					break;
				default:
					break;
				}
			}
		}
	}
	
}
