package com.bgood.xn.ui.user.product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhuozhong.bandgood.R;
import com.zhuozhong.bandgood.activity.BaseActivity;
import com.zhuozhong.bandgood.bean.ProductDTO;
import com.zhuozhong.bandgood.bean.Reulst;
import com.zhuozhong.bandgood.messagemanager.ShowCaseMessageManager;
import com.zhuozhong.zzframework.session.Frame.ReturnCode;

/**
 * 产品详情页面
 */
public class ProductDetailActivity extends BaseActivity implements OnClickListener
{
	private ProgressBar m_progressBar = null;
	private RelativeLayout m_showRl = null;
    private Button m_backBtn = null;   // 返回按钮
    private Button m_editBtn = null;   // 编辑按钮
    private ImageView m_iconImgV = null;  // 商品图片
    private TextView m_priceTv = null;  // 价格值
    private TextView m_productNameTv = null;  // 商品名
    private TextView m_productInfoTv = null;  // 商品信息介绍
    private RelativeLayout m_lookCommentRl = null; // 查看评论信息
    private LinearLayout m_contactSellerLl = null; // 联系卖家

    ShowCaseMessageManager messageManager = ShowCaseMessageManager.getInstance();
    private String productId = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_product_detail);
        
        getIntentData();
        findView();
        setListener();
        load(productId);
    }
    
    @Override
	public void onResume()
	{
		super.onResume();
		
		if (messageManager != null)
		{
			// 消息注册
			messageManager.registerObserver(this);
		}
	}

	@Override
	public void onPause()
	{
		super.onPause();
		
		if (messageManager != null)
		{
			// 消息注销
			messageManager.unregisterObserver(this);
		}
	}
	
	/**
	 * 得到传值数据方法
	 */
	private void getIntentData()
	{
		Intent intent = getIntent();
		productId = intent.getStringExtra("productId");
	}

    /**
     * 控件初始化方法
     */
    private void findView()
    {
    	m_progressBar = (ProgressBar) findViewById(R.id.product_detail_progress);
    	m_showRl = (RelativeLayout) findViewById(R.id.product_detail_rl_show);
        m_backBtn = (Button) findViewById(R.id.product_detail_btn_back);
        m_editBtn = (Button) findViewById(R.id.product_detail_btn_edit);
        m_iconImgV = (ImageView) findViewById(R.id.product_detail_imgv_icon);
        m_priceTv = (TextView) findViewById(R.id.product_detail_tv_price);
        m_productNameTv = (TextView) findViewById(R.id.product_detail_tv_name);
        m_productInfoTv = (TextView) findViewById(R.id.product_detail_tv_product_info);
        m_lookCommentRl = (RelativeLayout) findViewById(R.id.product_detail_rl_look_comment);
        m_contactSellerLl = (LinearLayout) findViewById(R.id.product_detail_ll_contact_seller);
    }
    
    /**
     * 控件事件监听方法
     */
    private void setListener()
    {
        m_backBtn.setOnClickListener(this);
        m_editBtn.setOnClickListener(this);
        m_lookCommentRl.setOnClickListener(this);
        m_contactSellerLl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            // 返回
            case R.id.product_detail_btn_back:
            {
                ProductDetailActivity.this.finish();
                break;
            }
            
            // 编辑商品
            case R.id.product_detail_btn_edit:
            {
                Intent intent = new Intent(ProductDetailActivity.this, ProductEditListActivity.class);
                startActivity(intent);
                break;
            }
            
            // 查看评论
            case R.id.product_detail_rl_look_comment:
            {
                Intent intent = new Intent(ProductDetailActivity.this, ProductCommentActivity.class);
                startActivity(intent);
                break;
            }
            
            // 联系卖家
            case R.id.product_detail_ll_contact_seller:
            {
                break;
            }
            
            default:
            {
                break;
            }
        }
    }
    
    /**
     * 加载商品详情方法
     * @param productId 商品Id
     */
    private void load(String productId)
    {
    	messageManager.getProductInfo(productId);
    }
    
    private void setData(ProductDTO productDTO)
    {
    	if (productDTO.productSmallIcon != null && !productDTO.productSmallIcon.equals(""))
			Picasso.with(this).load(productDTO.productSmallIcon).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(m_iconImgV);
    	
    	m_productNameTv.setText(productDTO.productName);
    	m_priceTv.setText("￥" + productDTO.productPrice);
    	m_productInfoTv.setText(productDTO.productInfo);
    }

	@Override
	public void getProductInfoCB(Reulst result_state, ProductDTO productDTO)
	{
		super.getProductInfoCB(result_state, productDTO);
		
		if (result_state.resultCode == ReturnCode.RETURNCODE_OK)
		{
			m_progressBar.setVisibility(View.GONE);
			m_showRl.setVisibility(View.VISIBLE);
			m_editBtn.setVisibility(View.VISIBLE);
			
			setData(productDTO);
		}
	}
    
    
}
