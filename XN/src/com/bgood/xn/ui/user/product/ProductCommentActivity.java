//package com.bgood.xn.ui.user.product;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.Button;
//import android.widget.ListView;
//
//import com.zhuozhong.bandgood.R;
//import com.zhuozhong.bandgood.activity.BaseActivity;
//import com.zhuozhong.bandgood.adapter.ProductCommentAdapter;
//
///**
// * 商品评论页面
// */
//public class ProductCommentActivity extends BaseActivity implements OnItemClickListener
//{
//    private Button m_backBtn = null; // 返回按钮
//    private ListView m_listLv = null; // 列表
//    
//    private ProductCommentAdapter m_adpater = null;
//    private List<String> m_list = new ArrayList<String>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_product_comment);
//        
//        findView();
//        setListener();
//        setAdapter(m_list);
//    }
//    
//    /**
//     * 控件初始化方法
//     */
//    private void findView()
//    {
//        m_backBtn = (Button) findViewById(R.id.product_comment_btn_back);
//        m_listLv = (ListView) findViewById(R.id.product_comment_lv_list);
//    }
//    
//    /**
//     * 控件事件监听方法
//     */
//    private void setListener()
//    {
//        m_backBtn.setOnClickListener(new OnClickListener()
//        {
//            
//            @Override
//            public void onClick(View v)
//            {
//                ProductCommentActivity.this.finish();
//            }
//        });
//        
//        m_listLv.setOnItemClickListener(this);
//    }
//    
//    /**
//     * 设置数据显示方法
//     * @param list
//     */
//    private void setAdapter(List<String> list)
//    {
//        m_adpater = new ProductCommentAdapter(ProductCommentActivity.this, list);
//        m_listLv.setAdapter(m_adpater);
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
//    {
//        
//    }
//
//}
