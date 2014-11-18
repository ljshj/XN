package com.bgood.xn.ui.user.product;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSON;
import com.bgood.xn.R;
import com.bgood.xn.adapter.ProductEditAdapter;
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
import com.bgood.xn.view.xlistview.XListView.IXListViewListener;
import com.bgood.xn.widget.SwipeListView;
import com.bgood.xn.widget.TitleBar;

/**
 * @todo:我的产品编辑列表
 * @date:2014-11-13 下午5:27:14
 * @author:hg_liuzl@163.com
 */
public class ProductEditListActivity extends BaseActivity implements OnItemClickListener,OnClickListener,TaskListenerWithState,IXListViewListener
{
    private SwipeListView m_listLv = null;  // 列表
    private List<ProductBean> m_list = new ArrayList<ProductBean>();
    private int m_start_size = 0;
    private ProductEditAdapter adapter = null;
    private String mKeyWord = null;
    private String mUserid = null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_product_edit_list);
        (new TitleBar(mActivity)).initTitleBar("编辑产品");
        mUserid = getIntent().getStringExtra("userid");
        m_listLv = (SwipeListView) findViewById(R.id.product_edit_list_lv_list);
        m_listLv.setOnItemClickListener(this);
        adapter = new ProductEditAdapter(m_list,mActivity,this, m_listLv.getRightViewWidth());
        m_listLv.setAdapter(adapter);
        m_listLv.setOnItemClickListener(this);
        ProductRequest.getInstance().requestProductList(this, this, mUserid, mKeyWord, String.valueOf(m_start_size), String.valueOf(m_start_size+PAGE_SIZE_ADD));
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View v, int location, long arg3)
    {
        final ProductBean productDTO = (ProductBean)adapter.getAdapter().getItem(location);
        Intent intent = new Intent(ProductEditListActivity.this, ProductEditActivity.class);
        intent.putExtra(ProductBean.BEAN_PRODUCT, productDTO);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.product_edit_list_item_tv_delete:
            final ProductBean bean = (ProductBean) v.getTag();
            ProductRequest.getInstance().requestProductDelete(this, mActivity, bean.product_id);
            break;

        default:
            break;
        }
    }

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			String strJson = bNetWork.getStrJson();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				switch (bNetWork.getMessageType()) {
				case 830003:
					final ProductResponse response = JSON.parseObject(strJson, ProductResponse.class);
					setProductData(response.products);
					break;
				case 830005:
					BToast.show(mActivity, "删除成功");
					break;

				default:
					break;
				}
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
		this.m_list.addAll(products);
		adapter.notifyDataSetChanged();
	}
}
