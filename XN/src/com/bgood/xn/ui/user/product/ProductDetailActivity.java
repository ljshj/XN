package com.bgood.xn.ui.user.product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.ProductBean;
import com.bgood.xn.ui.BaseActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/**
 * 
 * @todo:产品详情页
 * @date:2014-11-13 下午4:59:00
 * @author:hg_liuzl@163.com
 */
public class ProductDetailActivity extends BaseActivity implements OnClickListener
{
	/**产品实体类key**/
	public static final String PRODUCT_BEAN_KEY = "product_bean_key";
	
    private Button m_backBtn = null;   // 返回按钮
    private Button m_editBtn = null;   // 编辑按钮
    private ImageView m_iconImgV = null;  // 商品图片
    private TextView m_priceTv = null;  // 价格值
    private TextView m_productNameTv = null;  // 商品名
    private TextView m_productInfoTv = null;  // 商品信息介绍
    private RelativeLayout m_lookCommentRl = null; // 查看评论信息
    private LinearLayout m_contactSellerLl = null; // 联系卖家
    private ProductBean productBean;	//商品实体类
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_product_detail);
        
        productBean =  (ProductBean) getIntent().getSerializableExtra(PRODUCT_BEAN_KEY);
        findView();
        setListener();
        setData();
    }

    /**
     * 控件初始化方法
     */
    private void findView()
    {
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
    	 Intent intent = null;
        switch(v.getId())
        {
            // 查看评论
            case R.id.product_detail_rl_look_comment:
                 intent = new Intent(ProductDetailActivity.this, ProductCommentActivity.class);
                startActivity(intent);
                break;
            
            // 联系卖家
            case R.id.product_detail_ll_contact_seller:
                break;
            
            default:
                break;
        }
    }
    
    private void setData()
    {
    	ImageLoader mImageLoader;
		DisplayImageOptions options;
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.icon_default)
		.showImageForEmptyUri(R.drawable.icon_default)
		.cacheInMemory()
		.cacheOnDisc()
		.build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(mActivity));
		
        mImageLoader.displayImage(productBean.img_thum,m_iconImgV, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete() {
				Animation anim = AnimationUtils.loadAnimation(mActivity, R.anim.fade_in);
				m_iconImgV.setAnimation(anim);
				anim.start();
			}
		});
    	m_productNameTv.setText(productBean.product_name);
    	m_priceTv.setText("￥" + productBean.price);
    	m_productInfoTv.setText(productBean.intro);
    }
}
