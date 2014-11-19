package com.bgood.xn.ui.user.product;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.adapter.ProductListAdapter;
import com.bgood.xn.bean.ProductBean;
import com.bgood.xn.bean.response.ProductResponse;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.ProductRequest;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.view.xlistview.XListView.IXListViewListener;
import com.bgood.xn.widget.TitleBar;


/**
 * 
 * @todo:商品展示列表
 * @date:2014-11-13 下午4:00:53
 * @author:hg_liuzl@163.com
 */
public class ProductListActivity extends BaseActivity implements OnItemClickListener,TaskListenerWithState,IXListViewListener{
	private XListView m_listXLv = null;
    private ProductListAdapter m_adpater = null;
    private List<ProductBean> m_list = new ArrayList<ProductBean>();
    private String mKeyWord = null;
    private String mUserid = null;
    private int m_start_size = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_product_list);
        (new TitleBar(mActivity)).initTitleBar("我的产品");
        mKeyWord =getIntent().getStringExtra("content");
        
        if(mKeyWord == null){
        	mKeyWord = "";
        }
        
        mUserid = getIntent().getStringExtra("userid");
        m_listXLv = (XListView) findViewById(R.id.product_list_xlv_list);
        m_adpater = new ProductListAdapter(m_list,this);
        m_listXLv.setAdapter(m_adpater);
        m_listXLv.setPullRefreshEnable(false);
        m_listXLv.setOnItemClickListener(this);
        m_listXLv.setXListViewListener(this);
        ProductRequest.getInstance().requestProductList(this, this, mUserid, mKeyWord, String.valueOf(m_start_size), String.valueOf(m_start_size+PAGE_SIZE_ADD));
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View v, int location, long arg3)
    {
       final ProductBean ProductBean = (ProductBean)adapter.getAdapter().getItem(location);
        Intent intent = new Intent(ProductListActivity.this, ProductEditActivity.class);
        intent.putExtra(ProductBean.BEAN_PRODUCT, ProductBean);
        startActivity(intent);
    }

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			String strJson = bNetWork.getStrJson();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				final ProductResponse response = JSON.parseObject(strJson, ProductResponse.class);
				setProductData(response.products);
			}
		}
	}

	@Override
	public void onRefresh() {
	}

	@Override
	public void onLoadMore() {
        ProductRequest.getInstance().requestProductList(this, this, mUserid, mKeyWord, String.valueOf(m_start_size), String.valueOf(m_start_size+PAGE_SIZE_ADD));
	}
	
	private void setProductData(List<ProductBean> products) {
		if (null == products) {
			return;
		}
		m_listXLv.stopLoadMore();
		if (products.size() < PAGE_SIZE_ADD) {
			m_listXLv.setPullLoadEnable(false);
			BToast.show(mActivity, "数据加载完毕");
		} else {
			m_start_size += PAGE_SIZE_ADD;
			m_listXLv.setPullLoadEnable(true);
		}

		this.m_list.addAll(products);
		m_adpater.notifyDataSetChanged();
	}
}
