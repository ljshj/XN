package com.bgood.xn.ui.user.product;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

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
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.view.xlistview.XListView;
import com.bgood.xn.widget.TitleBar;


/**
 * 
 * @todo:商品展示列表
 * @date:2014-11-13 下午4:00:53
 * @author:hg_liuzl@163.com
 */
public class ProductListActivity extends BaseActivity implements OnItemClickListener,TaskListenerWithState{

	private XListView m_listXLv = null;
    private String m_content = "";
    private ProductListAdapter m_adpater = null;
    private List<ProductBean> m_list = new ArrayList<ProductBean>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_product_list);
        (new TitleBar(mActivity)).initTitleBar("产品列表");
        m_content =getIntent().getStringExtra("content");
        m_listXLv = (XListView) findViewById(R.id.product_list_xlv_list);
        m_adpater = new ProductListAdapter(m_list,this);
        m_listXLv.setAdapter(m_adpater);
        m_listXLv.setOnItemClickListener(this);
      //  ProductRequest.getInstance().requestProductSearch(mHttpTaskListener, context, userid, keyword);
       
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View v, int location, long arg3)
    {
       final ProductBean ProductBean = (ProductBean)adapter.getAdapter().getItem(location);
        Intent intent = new Intent(ProductListActivity.this, ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.PRODUCT_BEAN_KEY, ProductBean);
        startActivity(intent);
    }

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			JSONObject body = bNetWork.getBody();
			String strJson = bNetWork.getStrJson();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				
				ProductResponse response = (ProductResponse) JSON.parseArray(strJson, ProductResponse.class);
			
			}
		}
	}
}
