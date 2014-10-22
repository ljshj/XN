//package com.bgood.xn.ui.home;
//
//import java.util.ArrayList;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.UserManager;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bgood.xn.R;
//import com.bgood.xn.adapter.ShowcaseAllProductAdapter;
//import com.bgood.xn.adapter.ShowcaseRecommendAdapter;
//import com.bgood.xn.network.BaseNetWork.ReturnCode;
//import com.bgood.xn.ui.BaseActivity;
//import com.bgood.xn.view.xlistview.XListView;
//
///**
// * 我的橱窗页面
// */
//public class TaShowcaseActivity extends BaseActivity implements OnClickListener, OnItemClickListener
//{
//	private ProgressBar m_progressBar = null; // 加载进度条
//	private LinearLayout m_showLl = null;  // 数据显示区域
//	private Button m_backBtn = null; // 返回按钮
//	private Button m_moreBtn = null; // 更多按钮
//	private FrameLayout m_backgroundFl = null; // 背景
//	private EditText m_searchEt = null; // 搜索输入框
//	private Button m_searchBtn = null;  // 搜索按钮
//	private ImageView m_showcaseIconImgV = null; // 橱窗图片
//	private TextView m_showcaseNameTv = null; // 橱窗名
//	private TextView m_commentsTv = null; // 评论值
//	private ImageView m_oneHintImgV = null; // 第一个心
//	private ImageView m_twoHintImgV = null; // 第二个心
//	private ImageView m_threeHintImgV = null; // 第三个心
//	private ImageView m_fourHintImgV = null; // 第四个心
//	private ImageView m_fiveHintImgV = null; // 第五个心
//	private LinearLayout m_recommendLl = null; // 产品推荐
//	private TextView m_recommendHintTv = null; // 推荐提示
//	private TextView m_recommendTv = null; // 产品推荐值
//	private LinearLayout m_allProductLl = null; // 全部产品
//	private TextView m_allProductHintTv = null; // 全部产品提示
//	private TextView m_allProductTv = null; // 全部产品值
//	private LinearLayout m_recommendLayout = null; // 推荐布局
//	private LinearLayout m_allProductLayout = null; // 全部产品
//	private XListView m_recommendXLv = null;  // 推荐产品
//	private ProgressBar m_allProductPb = null;
//	private XListView m_allProductXLv = null;  // 所有产品
//
//	private ShowcaseRecommendAdapter m_recommendAdapter = null;    // 推荐商品适配器
//	private ShowcaseAllProductAdapter m_allProductAdapter = null;  // 所有商品适配器
//	
//	ShowCaseMessageManager messageManager = ShowCaseMessageManager.getInstance();
//	
//	private int m_loadAllProduct = 1;
//	private int m_start = 0;
//	private int m_addStart = 10;
//	
//	private UserDTO user;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.layout_showcase);
//
//		getIntentData();
//		findView();
//		setListener();
//		load();
//
////		setCredibility(3);
//	}
//	
//	@Override
//	public void onResume()
//	{
//		super.onResume();
//		
//		if (messageManager != null)
//		{
//			// 消息注册
//			messageManager.registerObserver(this);
//		}
//	}
//
//	@Override
//	public void onPause()
//	{
//		super.onPause();
//		
//		if (messageManager != null)
//		{
//			// 消息注销
//			messageManager.unregisterObserver(this);
//		}
//	}
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
//	/**
//	 * 控件初始化方法
//	 */
//	private void findView()
//	{
//		m_progressBar = (ProgressBar) findViewById(R.id.showcase_progress);
//		m_showLl = (LinearLayout) findViewById(R.id.showcase_ll_show);
//		m_backBtn = (Button) findViewById(R.id.showcase_btn_back);
//		m_moreBtn = (Button) findViewById(R.id.showcase_btn_more);
//		m_moreBtn.setVisibility(View.GONE);
//		m_backgroundFl = (FrameLayout) findViewById(R.id.showcase_fl_background);
//		m_searchEt = (EditText) findViewById(R.id.showcase_et_search);
//		m_searchBtn = (Button) findViewById(R.id.showcase_btn_search);
//		m_showcaseIconImgV = (ImageView) findViewById(R.id.showcase_imgv_showcase_icon);
//		m_showcaseNameTv = (TextView) findViewById(R.id.showcase_tv_shop_name);
//		m_commentsTv = (TextView) findViewById(R.id.showcase_tv_comments);
//		m_oneHintImgV = (ImageView) findViewById(R.id.showcase_imgv_one);
//		m_twoHintImgV = (ImageView) findViewById(R.id.showcase_imgv_two);
//		m_threeHintImgV = (ImageView) findViewById(R.id.showcase_imgv_three);
//		m_fourHintImgV = (ImageView) findViewById(R.id.showcase_imgv_four);
//		m_fiveHintImgV = (ImageView) findViewById(R.id.showcase_imgv_five);
//		m_recommendLl = (LinearLayout) findViewById(R.id.showcase_ll_recommend);
//		m_recommendHintTv = (TextView) findViewById(R.id.showcase_tv_recommend_hint);
//		m_recommendTv = (TextView) findViewById(R.id.showcase_tv_recommend);
//		m_allProductLl = (LinearLayout) findViewById(R.id.showcase_ll_all_product);
//		m_allProductHintTv = (TextView) findViewById(R.id.showcase_tv_all_product_hint);
//		m_allProductTv = (TextView) findViewById(R.id.showcase_tv_all_product);
//		m_recommendLayout = (LinearLayout) findViewById(R.id.showcase_layout_recommend);
//		m_allProductLayout = (LinearLayout) findViewById(R.id.showcase_layout_all_product);
//		m_recommendXLv = (XListView) findViewById(R.id.showcase_xlv_recommend);
//		m_allProductPb = (ProgressBar) findViewById(R.id.showcase_all_product_progress);
//		m_allProductXLv = (XListView) findViewById(R.id.showcase_xlv_all_product);
//	}
//
//	/**
//	 * 控件事件监听方法
//	 */
//	private void setListener()
//	{
//		m_backBtn.setOnClickListener(this);
//		m_searchBtn.setOnClickListener(this);
//		m_recommendLl.setOnClickListener(this);
//		m_allProductLl.setOnClickListener(this);
//		m_recommendXLv.setOnItemClickListener(this);
//		m_allProductXLv.setOnItemClickListener(this);
//
//	}
//
//	/**
//	 *  加载数据方法
//	 */
//	private void load()
//	{
//		
//		messageManager.getShowcaseInfo(user.userId);
//	}
//	
//	@Override
//	public void onClick(View v)
//	{
//		switch (v.getId())
//		{
//    		// 返回
//    		case R.id.showcase_btn_back:
//    		{
//    			TaShowcaseActivity.this.finish();
//    			break;
//    		}
//    		
//    		// 返回
//            case R.id.showcase_btn_search:
//            {
//                String searchContent = m_searchEt.getText().toString().trim();
//                if (searchContent == null || searchContent.equals(""))
//                {
//                    Toast.makeText(TaShowcaseActivity.this, "请输入搜索内容！", Toast.LENGTH_LONG).show();
//                    return;
//                }
//                else
//                {
//                    Intent intent = new Intent(TaShowcaseActivity.this, ProductListActivity.class);
//                    intent.putExtra("content", searchContent);
//                    startActivity(intent);
//                }
//                break;
//            }
//    
//    		// 推荐产品
//    		case R.id.showcase_ll_recommend:
//    		{
//    			setProduct(0);
//    			break;
//    		}
//    
//    		// 全部产品
//    		case R.id.showcase_ll_all_product:
//    		{
//    			setProduct(1);
//    //			Intent intent = new Intent(ShowcaseActivity.this, ProductDetailActivity.class);
//    //			startActivity(intent);
//    			break;
//    		}
//    		default:
//    			break;
//		}
//	}
//
//	/**
//	 * 设置橱窗信誉度
//	 * 
//	 * @param index
//	 *            信用值
//	 */
//	private void setCredibility(int index)
//	{
//		switch (index)
//		{
//		case 1:
//		{
//			m_oneHintImgV.setVisibility(View.VISIBLE);
//			m_twoHintImgV.setVisibility(View.INVISIBLE);
//			m_threeHintImgV.setVisibility(View.INVISIBLE);
//			m_fourHintImgV.setVisibility(View.INVISIBLE);
//			m_fiveHintImgV.setVisibility(View.INVISIBLE);
//			break;
//		}
//
//		case 2:
//		{
//			m_oneHintImgV.setVisibility(View.VISIBLE);
//			m_twoHintImgV.setVisibility(View.VISIBLE);
//			m_threeHintImgV.setVisibility(View.INVISIBLE);
//			m_fourHintImgV.setVisibility(View.INVISIBLE);
//			m_fiveHintImgV.setVisibility(View.INVISIBLE);
//			break;
//		}
//
//		case 3:
//		{
//			m_oneHintImgV.setVisibility(View.VISIBLE);
//			m_twoHintImgV.setVisibility(View.VISIBLE);
//			m_threeHintImgV.setVisibility(View.VISIBLE);
//			m_fourHintImgV.setVisibility(View.INVISIBLE);
//			m_fiveHintImgV.setVisibility(View.INVISIBLE);
//			break;
//		}
//
//		case 4:
//		{
//			m_oneHintImgV.setVisibility(View.VISIBLE);
//			m_twoHintImgV.setVisibility(View.VISIBLE);
//			m_threeHintImgV.setVisibility(View.VISIBLE);
//			m_fourHintImgV.setVisibility(View.VISIBLE);
//			m_fiveHintImgV.setVisibility(View.INVISIBLE);
//			break;
//		}
//
//		case 5:
//		{
//			m_oneHintImgV.setVisibility(View.VISIBLE);
//			m_twoHintImgV.setVisibility(View.VISIBLE);
//			m_threeHintImgV.setVisibility(View.VISIBLE);
//			m_fourHintImgV.setVisibility(View.VISIBLE);
//			m_fiveHintImgV.setVisibility(View.VISIBLE);
//			break;
//		}
//
//		default:
//		{
//			m_oneHintImgV.setVisibility(View.INVISIBLE);
//			m_twoHintImgV.setVisibility(View.INVISIBLE);
//			m_threeHintImgV.setVisibility(View.INVISIBLE);
//			m_fourHintImgV.setVisibility(View.INVISIBLE);
//			m_fiveHintImgV.setVisibility(View.INVISIBLE);
//			break;
//		}
//		}
//	}
//
//	/**
//	 * 显示产品
//	 * 
//	 * @param index
//	 */
//	private void setProduct(int index)
//	{
//		switch (index)
//		{
//		case 0:
//		{
//			m_recommendHintTv.setTextColor(getResources().getColor(R.color.red));
//			m_recommendTv.setTextColor(getResources().getColor(R.color.red));
//			m_allProductHintTv.setTextColor(getResources().getColor(R.color.black));
//			m_allProductTv.setTextColor(getResources().getColor(R.color.black));
////			m_recommendLayout.setVisibility(View.VISIBLE);
////			m_allProductLayout.setVisibility(View.GONE);
//			m_recommendXLv.setVisibility(View.VISIBLE);
//			m_allProductXLv.setVisibility(View.GONE);
//			break;
//		}
//
//		case 1:
//		{
//			m_recommendHintTv.setTextColor(getResources().getColor(R.color.black));
//			m_recommendTv.setTextColor(getResources().getColor(R.color.black));
//			m_allProductHintTv.setTextColor(getResources().getColor(R.color.red));
//			m_allProductTv.setTextColor(getResources().getColor(R.color.red));
////			m_recommendLayout.setVisibility(View.GONE);
////			m_allProductLayout.setVisibility(View.VISIBLE);
//			m_recommendXLv.setVisibility(View.GONE);
//			m_allProductXLv.setVisibility(View.VISIBLE);
//			
//			if (m_loadAllProduct == 1)
//			{
//			    laodData(UserManager.getInstance().m_user.userId);
//			}
//			
//			break;
//		}
//		default:
//			break;
//		}
//	}
//	
//	/**
//     * 加载数据方法
//     * @param userId 用户Id
//     */
//    private void laodData(String userId)
//    {
//        messageManager.getProductList(userId, m_start, m_start + m_addStart);
//    }
//	
//	/**
//	 * 进行推荐商品数据显示
//	 * @param list 数据源
//	 */
//	private void setRevommendAdapter(ArrayList<ProductDTO> list)
//	{
//		if (m_recommendAdapter == null)
//		{
//			m_recommendAdapter = new ShowcaseRecommendAdapter(TaShowcaseActivity.this, list);
//			m_recommendXLv.setAdapter(m_recommendAdapter);
//		}
//		else
//		{
//			m_recommendAdapter.notifyDataSetChanged();
//		}
//		
//	}
//	
//	/**
//	 * 进行所有商品数据显示
//	 * @param list 数据源
//	 */
//	private void setAllProductAdapter(ArrayList<ProductDTO> list)
//	{
//		if (m_allProductAdapter == null)
//		{
//			m_allProductAdapter = new ShowcaseAllProductAdapter(TaShowcaseActivity.this, list);
//			m_allProductXLv.setAdapter(m_allProductAdapter);
//		}
//		else
//		{
//			m_allProductAdapter.notifyDataSetChanged();
//		}
//		
//	}
//	
//	/**
//	 * 设置数据显示方法
//	 * @param showcaseDTO 说明参数含义
//	 */
//	private void setData(ShowcaseDTO showcaseDTO)
//	{
//		m_progressBar.setVisibility(View.GONE);
//		m_showLl.setVisibility(View.VISIBLE);
//		
//		if (showcaseDTO != null)
//		{
//			if (showcaseDTO.credit != null && !showcaseDTO.credit.equals(""))
//			{
//				setCredibility(Integer.parseInt(showcaseDTO.credit));
//			}
//			else
//			{
//				setCredibility(0);
//			}
//			
//			if (showcaseDTO.comments != null && !showcaseDTO.comments.equals(""))
//			{
//				m_commentsTv.setText(showcaseDTO.comments);
//			}
//			
//			if (showcaseDTO.shopName != null && !showcaseDTO.shopName.equals(""))
//			{
//				m_showcaseNameTv.setText(showcaseDTO.shopName);
//			}
//			
//			if (showcaseDTO.productList != null )
//			{
//			    if (showcaseDTO.productList.size() < 10)
//                {
//                    m_recommendXLv.setPullLoadEnable(false);
//                    Toast.makeText(TaShowcaseActivity.this, "加载完毕！", Toast.LENGTH_LONG).show();
//                }
//                else
//                {
//                    m_recommendXLv.setPullLoadEnable(true);
//                }
//				setRevommendAdapter(showcaseDTO.productList);
//			}
//			else
//			{
//			    m_recommendXLv.setPullLoadEnable(false);
//                Toast.makeText(TaShowcaseActivity.this, "加载完毕！", Toast.LENGTH_LONG).show();
//			}
//		}
//	}
//
//	@Override
//	public void getShowcaseInfoCB(Reulst result_state, ShowcaseDTO showcaseDTO)
//	{
//		if (result_state.resultCode == ReturnCode.RETURNCODE_OK)
//		{
//			setData(showcaseDTO);
//		}
//		else
//		{
//			
//		}
//	}
//
//	@Override
//	public void addProductCB(Reulst result_state)
//	{
//	}
//
//	@Override
//	public void modifyProductCB(Reulst result_state)
//	{
//	}
//
//	@Override
//	public void deleteProductCB(Reulst result_state)
//	{
//	}
//
//	@Override
//	public void searchProductCB(Reulst result_state, ArrayList<ProductDTO> list)
//	{
//	}
//
//	@Override
//	public void getProductInfoCB(Reulst result_state, ProductDTO productDTO)
//	{
//	}
//
//	@Override
//	public void getProductListCB(Reulst result_state, ArrayList<ProductDTO> list)
//	{
//	    if (result_state.resultCode == ReturnCode.RETURNCODE_OK)
//        {
//	        m_loadAllProduct = 2;
//	        m_allProductPb.setVisibility(View.GONE);
//            m_allProductXLv.setVisibility(View.VISIBLE);
//            if (list != null)
//            {
//                if (list.size() < 10)
//                {
//                    m_allProductXLv.setPullLoadEnable(false);
//                    Toast.makeText(TaShowcaseActivity.this, "加载完毕！", Toast.LENGTH_LONG).show();
//                }
//                else
//                {
//                    m_start = m_start + m_addStart;
//                    m_allProductXLv.setPullLoadEnable(true);
//                }
//                setAllProductAdapter(list);
//            }
//        }
//	}
//
//	@Override
//	public void setShowcaseModelCB(Reulst result_state)
//	{
//	}
//
//    @Override
//    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
//    {
//        ProductDTO productDTO = (ProductDTO)arg0.getAdapter().getItem(arg2);
//        Intent intent = new Intent(TaShowcaseActivity.this, ProductDetailActivity.class);
//        intent.putExtra("productId", productDTO.productId);
//        startActivity(intent);
//    }
//	
//	
//}
