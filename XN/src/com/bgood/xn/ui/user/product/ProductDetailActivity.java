package com.bgood.xn.ui.user.product;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.bean.ProductBean;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.ProductRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.ui.user.info.NameCardActivity;
import com.bgood.xn.utils.ImgUtils;
import com.bgood.xn.widget.TitleBar;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @todo:产品详情页
 * @date:2014-11-13 下午4:59:00
 * @author:hg_liuzl@163.com
 */
public class ProductDetailActivity extends BaseActivity implements OnClickListener,TaskListenerWithState
{
	
    private ImageView m_iconImgV = null;  // 商品图片
    private TextView m_priceTv = null;  // 价格值
    private TextView m_productNameTv = null;  // 商品名
    private TextView m_productInfoTv = null;  // 商品信息介绍
    private RelativeLayout m_lookCommentRl = null; // 查看评论信息
    private LinearLayout m_contactSellerLl = null; // 联系卖家
    private String productId;
    private ArrayList<String> imgList = new ArrayList<String>(); //存储图片查看器的图片地址
    private ProductBean mProductBean;
    
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_product_detail);
        (new TitleBar(mActivity)).initTitleBar("产品详情");
        findView();
        productId =  getIntent().getStringExtra(ProductBean.KEY_PRODUCT_ID);
        if(null!=productId){
        	ProductRequest.getInstance().requestProductDetail(this, mActivity, productId);
        }else{
        	finish();
        }
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
        m_iconImgV.setOnClickListener(this);
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
                 intent = new Intent(ProductDetailActivity.this, ProductCommentShowActivity.class);
                 intent.putExtra(ProductCommentShowActivity.KEY_PRODUCT_ID, productId);
                 startActivity(intent);
                break;
            // 联系卖家
            case R.id.product_detail_ll_contact_seller:
            	NameCardActivity.lookNameCard(mActivity, mProductBean.userid);
                break;
            //查看图片
            case R.id.product_detail_imgv_icon:
            	ImgUtils.imageBrower(0, imgList, mActivity);
            	break;
            default:
                break;
        }
    }
    
    private void setData()
    {
    	imgList.add(mProductBean.img);
    	BGApp.getInstance().setImageSqure(mProductBean.img, m_iconImgV);
    	m_productNameTv.setText(mProductBean.product_name);
    	m_priceTv.setText(mProductBean.getPrice());
    	m_productInfoTv.setText(mProductBean.intro);
    }

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			String strJson = bNetWork.getStrJson();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				mProductBean = JSON.parseObject(strJson, ProductBean.class);
				setData();
			}
		}
	}
}
