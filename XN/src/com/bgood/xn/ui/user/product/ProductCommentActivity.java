package com.bgood.xn.ui.user.product;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.bgood.xn.R;
import com.bgood.xn.adapter.ProductCommentAdapter;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.widget.TitleBar;


/**
 * 
 * @todo:商品评论界面
 * @date:2014-11-13 下午4:51:34
 * @author:hg_liuzl@163.com
 */
public class ProductCommentActivity extends BaseActivity implements OnItemClickListener
{
    private ListView m_listLv = null; // 列表
    
    private ProductCommentAdapter m_adpater = null;
    private List<String> m_list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_product_comment);
        (new TitleBar(mActivity)).initTitleBar("商品评论");
        m_listLv = (ListView) findViewById(R.id.product_comment_lv_list);
        m_listLv.setOnItemClickListener(this);
        m_adpater = new ProductCommentAdapter(ProductCommentActivity.this, m_list);
        m_listLv.setAdapter(m_adpater);
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
        
    }

}
