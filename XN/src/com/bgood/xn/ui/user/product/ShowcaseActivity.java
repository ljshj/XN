package com.bgood.xn.ui.user.product;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.adapter.ShowcaseAllProductAdapter;
import com.bgood.xn.adapter.ShowcaseRecommendAdapter;
import com.bgood.xn.bean.ProductBean;
import com.bgood.xn.bean.ShowcaseBean;
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
 * 我的橱窗页面
 */
public class ShowcaseActivity extends CBaseSlidingMenu implements OnClickListener, OnItemClickListener
{
	private ProgressBar m_progressBar = null; // 加载进度条
	private LinearLayout m_showLl = null;  // 数据显示区域
	private Button m_backBtn = null; // 返回按钮
	private Button m_moreBtn = null; // 更多按钮
	private FrameLayout m_backgroundFl = null; // 背景
	private EditText m_searchEt = null; // 搜索输入框
	private Button m_searchBtn = null;  // 搜索按钮
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
	private LinearLayout m_recommendLayout = null; // 推荐布局
	private LinearLayout m_allProductLayout = null; // 全部产品
	private XListView m_recommendXLv = null;  // 推荐产品
	private ProgressBar m_allProductPb = null;
	private XListView m_allProductXLv = null;  // 所有产品

	private ShowcaseRightFragment m_rightFragment; // 右侧菜单Fragment

	private boolean m_menuShowing; // 右菜单是否显示
	
	private ShowcaseRecommendAdapter m_recommendAdapter = null;    // 推荐商品适配器
	private ShowcaseAllProductAdapter m_allProductAdapter = null;  // 所有商品适配器
	
	
	private int m_loadAllProduct = 1;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_showcase);

		findView();
		setListener();
		setRightMenu();
		load();
	}
	/**
	 * 控件初始化方法
	 */
	private void findView()
	{
		m_progressBar = (ProgressBar) findViewById(R.id.showcase_progress);
		m_showLl = (LinearLayout) findViewById(R.id.showcase_ll_show);
		m_backBtn = (Button) findViewById(R.id.showcase_btn_back);
		m_moreBtn = (Button) findViewById(R.id.showcase_btn_more);
		m_backgroundFl = (FrameLayout) findViewById(R.id.showcase_fl_background);
		m_searchEt = (EditText) findViewById(R.id.showcase_et_search);
		m_searchBtn = (Button) findViewById(R.id.showcase_btn_search);
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
		m_recommendLayout = (LinearLayout) findViewById(R.id.showcase_layout_recommend);
		m_allProductLayout = (LinearLayout) findViewById(R.id.showcase_layout_all_product);
		m_recommendXLv = (XListView) findViewById(R.id.showcase_xlv_recommend);
		m_allProductPb = (ProgressBar) findViewById(R.id.showcase_all_product_progress);
		m_allProductXLv = (XListView) findViewById(R.id.showcase_xlv_all_product);
	}

	/**
	 * 控件事件监听方法
	 */
	private void setListener()
	{
		m_backBtn.setOnClickListener(this);
		m_searchBtn.setOnClickListener(this);
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
	}

	/**
	 * 设置颜色方法
	 */
	private void setBackgroundColor()
	{
	    SharedPreferences share = ShowcaseActivity.this.getSharedPreferences("IndividuationColor", MODE_PRIVATE);
	    switch (share.getInt("colorNum", 1))
        {
            case 1:
            {
                m_backgroundFl.setBackgroundColor(getResources().getColor(R.color.individuation_blue1));
                break;
            }
            
            case 2:
            {
                m_backgroundFl.setBackgroundColor(getResources().getColor(R.color.individuation_white2));
                break;
            }
            
            case 3:
            {
                m_backgroundFl.setBackgroundColor(getResources().getColor(R.color.individuation_purple3));
                break;
            }
            
            case 4:
            {
                m_backgroundFl.setBackgroundColor(getResources().getColor(R.color.individuation_green4));
                break;
            }
            
            case 5:
            {
                m_backgroundFl.setBackgroundColor(getResources().getColor(R.color.individuation_lightblue5));
                break;
            }
            case 6:
            {
                m_backgroundFl.setBackgroundColor(getResources().getColor(R.color.individuation_yellow6));
                break;
            }
            default:
                break;
        }
	}
	
	/**
	 *  加载数据方法
	 */
	private void load()
	{
		
		//messageManager.getShowcaseInfo(UserManager.getInstance().m_user.userId);
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
    		// 返回
            case R.id.showcase_btn_search:
                String searchContent = m_searchEt.getText().toString().trim();
                if (TextUtils.isEmpty(searchContent))
                {
                    BToast.show(ShowcaseActivity.this,  "请输入搜索内容！");
                    return;
                }
                else
                {
                    Intent intent = new Intent(ShowcaseActivity.this, ProductListActivity.class);
                    intent.putExtra("content", searchContent);
                    startActivity(intent);
                }
                break;
    		// 推荐产品
    		case R.id.showcase_ll_recommend:
    			setProduct(0);
    			break;
    		// 全部产品
    		case R.id.showcase_ll_all_product:
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
		{
			m_oneHintImgV.setVisibility(View.VISIBLE);
			m_twoHintImgV.setVisibility(View.INVISIBLE);
			m_threeHintImgV.setVisibility(View.INVISIBLE);
			m_fourHintImgV.setVisibility(View.INVISIBLE);
			m_fiveHintImgV.setVisibility(View.INVISIBLE);
			break;
		}

		case 2:
		{
			m_oneHintImgV.setVisibility(View.VISIBLE);
			m_twoHintImgV.setVisibility(View.VISIBLE);
			m_threeHintImgV.setVisibility(View.INVISIBLE);
			m_fourHintImgV.setVisibility(View.INVISIBLE);
			m_fiveHintImgV.setVisibility(View.INVISIBLE);
			break;
		}

		case 3:
		{
			m_oneHintImgV.setVisibility(View.VISIBLE);
			m_twoHintImgV.setVisibility(View.VISIBLE);
			m_threeHintImgV.setVisibility(View.VISIBLE);
			m_fourHintImgV.setVisibility(View.INVISIBLE);
			m_fiveHintImgV.setVisibility(View.INVISIBLE);
			break;
		}

		case 4:
		{
			m_oneHintImgV.setVisibility(View.VISIBLE);
			m_twoHintImgV.setVisibility(View.VISIBLE);
			m_threeHintImgV.setVisibility(View.VISIBLE);
			m_fourHintImgV.setVisibility(View.VISIBLE);
			m_fiveHintImgV.setVisibility(View.INVISIBLE);
			break;
		}

		case 5:
		{
			m_oneHintImgV.setVisibility(View.VISIBLE);
			m_twoHintImgV.setVisibility(View.VISIBLE);
			m_threeHintImgV.setVisibility(View.VISIBLE);
			m_fourHintImgV.setVisibility(View.VISIBLE);
			m_fiveHintImgV.setVisibility(View.VISIBLE);
			break;
		}

		default:
		{
			m_oneHintImgV.setVisibility(View.INVISIBLE);
			m_twoHintImgV.setVisibility(View.INVISIBLE);
			m_threeHintImgV.setVisibility(View.INVISIBLE);
			m_fourHintImgV.setVisibility(View.INVISIBLE);
			m_fiveHintImgV.setVisibility(View.INVISIBLE);
			break;
		}
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
		{
			m_recommendHintTv.setTextColor(getResources().getColor(R.color.red));
			m_recommendTv.setTextColor(getResources().getColor(R.color.red));
			m_allProductHintTv.setTextColor(getResources().getColor(R.color.black));
			m_allProductTv.setTextColor(getResources().getColor(R.color.black));
//			m_recommendLayout.setVisibility(View.VISIBLE);
//			m_allProductLayout.setVisibility(View.GONE);
			m_recommendXLv.setVisibility(View.VISIBLE);
			m_allProductXLv.setVisibility(View.GONE);
			break;
		}

		case 1:
		{
			m_recommendHintTv.setTextColor(getResources().getColor(R.color.black));
			m_recommendTv.setTextColor(getResources().getColor(R.color.black));
			m_allProductHintTv.setTextColor(getResources().getColor(R.color.red));
			m_allProductTv.setTextColor(getResources().getColor(R.color.red));
//			m_recommendLayout.setVisibility(View.GONE);
//			m_allProductLayout.setVisibility(View.VISIBLE);
			m_recommendXLv.setVisibility(View.GONE);
			m_allProductXLv.setVisibility(View.VISIBLE);
			
			if (m_loadAllProduct == 1)
			{
		//	    laodData(UserManager.getInstance().m_user.userId);
			}
			
			break;
		}
		default:
			break;
		}
	}
	
	/**
     * 加载数据方法
     * @param userId 用户Id
     */
    private void laodData(String userId)
    {
    //    messageManager.getProductList(userId, m_start, m_start + m_addStart);
        
        
    }
	
	/**
	 * 进行推荐商品数据显示
	 * @param list 数据源
	 */
	private void setRevommendAdapter(ArrayList<ProductBean> list)
	{
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
	private void setAllProductAdapter(ArrayList<ProductBean> list)
	{
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
		m_progressBar.setVisibility(View.GONE);
		m_showLl.setVisibility(View.VISIBLE);
		
		if (showcaseDTO != null)
		{
			if (showcaseDTO.credit != null && !showcaseDTO.credit.equals(""))
			{
				setCredibility(Integer.parseInt(showcaseDTO.credit));
			}
			else
			{
				setCredibility(0);
			}
			
			
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
			
	        mImageLoader.displayImage(showcaseDTO.shopLogo,m_showcaseIconImgV, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingComplete() {
					Animation anim = AnimationUtils.loadAnimation(ShowcaseActivity.this, R.anim.fade_in);
					m_showcaseIconImgV.setAnimation(anim);
					anim.start();
				}
			});
			
			
			
			m_recommendTv.setText("(" + showcaseDTO.goodCount + ")");
			m_allProductTv.setText("(" + showcaseDTO.productCount + ")");
			
//			if (showcaseDTO.comments != null && !showcaseDTO.comments.equals(""))
//			{
				m_commentsTv.setText(showcaseDTO.comments);
//			}
			
//			if (showcaseDTO.shopName != null && !showcaseDTO.shopName.equals(""))
//			{
				m_showcaseNameTv.setText(showcaseDTO.shopName);
//			}
			
			if (showcaseDTO.productList != null )
			{
			    if (showcaseDTO.productList.size() < 10)
                {
                    m_recommendXLv.setPullLoadEnable(false);
                    Toast.makeText(ShowcaseActivity.this, "加载完毕！", Toast.LENGTH_LONG).show();
                }
                else
                {
                    m_recommendXLv.setPullLoadEnable(true);
                }
				setRevommendAdapter(showcaseDTO.productList);
			}
			else
			{
			    m_recommendXLv.setPullLoadEnable(false);
                Toast.makeText(ShowcaseActivity.this, "加载完毕！", Toast.LENGTH_LONG).show();
			}
		}
	}
//
//	@Override
//	public void getShowcaseInfoCB(Reulst result_state, S showcaseDTO)
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
//	public void getProductListCB(Reulst result_state, ArrayList<ProductBean> list)
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
//                    Toast.makeText(ShowcaseActivity.this, "加载完毕！", Toast.LENGTH_LONG).show();
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

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
//        ProductBean productDTO = (ProductBean)arg0.getAdapter().getItem(arg2);
//        Intent intent = new Intent(ShowcaseActivity.this, ProductDetailActivity.class);
//        intent.putExtra("productId", productDTO.productId);
//        startActivity(intent);
    }
	
	
}
