package com.bgood.xn.ui.user.product;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.bgood.xn.R;
import com.bgood.xn.adapter.ProductAdapter;
import com.bgood.xn.bean.ProductBean;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.widget.SwipeListView;

/**
 * 
 * @todo:我的产品
 * @date:2014-11-13 下午5:27:14
 * @author:hg_liuzl@163.com
 */
public class ProductEditListActivity extends BaseActivity implements OnItemClickListener,OnClickListener
{
    private SwipeListView m_listLv = null;  // 列表
    private List<ProductBean> m_list = new ArrayList<ProductBean>();
    private int start = 0;
    private ProductAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_product_edit_list);
        m_listLv = (SwipeListView) findViewById(R.id.product_edit_list_lv_list);
        m_listLv.setOnItemClickListener(this);
        adapter = new ProductAdapter(m_list,mActivity,this, m_listLv.getRightViewWidth());
        m_listLv.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View v, int location, long arg3)
    {
//    	final ProductBean productDTO = (ProductBean)adapter.getAdapter().getItem(location);
//    	Intent intent = new Intent(ProductEditListActivity.this, ProductEditActivity.class);
//    	intent.putExtra(ProductBean.BEAN_PRODUCT, productDTO);
//    	startActivity(intent);
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.product_edit_list_item_tv_delete:
			final ProductBean bean = (ProductBean) v.getTag();
			
			
			break;

		default:
			break;
		}
	}

}
