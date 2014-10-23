//package com.bgood.xn.ui.user.product;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.Button;
//import android.widget.ProgressBar;
//
//import com.zhuozhong.bandgood.R;
//import com.zhuozhong.bandgood.activity.BaseActivity;
//import com.zhuozhong.bandgood.adapter.ProductListAdapter;
//import com.zhuozhong.bandgood.bean.ProductDTO;
//import com.zhuozhong.bandgood.bean.Reulst;
//import com.zhuozhong.bandgood.messagemanager.ShowCaseMessageManager;
//import com.zhuozhong.manager.UserManager;
//import com.zhuozhong.view.xlistview.XListView;
//import com.zhuozhong.zzframework.session.Frame.ReturnCode;
//
///**
// * 商品列表和搜索商品列表页面
// */
//public class ProductListActivity extends BaseActivity implements OnItemClickListener
//{
//    private Button m_backBtn = null;
//    private ProgressBar m_progressBar = null;
//    private XListView m_listXLv = null;
//    
//    ShowCaseMessageManager messageManager = ShowCaseMessageManager.getInstance();
//    
//    private String m_content = "";
//    
//    private ProductListAdapter m_adpater = null;
//    private List<ProductDTO> m_list = new ArrayList<ProductDTO>();
//    
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_product_list);
//        
//        getIntentData();
//        findView();
//        setListener();
//        loadData();
//    }
//    
//    @Override
//    public void onResume()
//    {
//        super.onResume();
//        
//        if (messageManager != null)
//        {
//            // 消息注册
//            messageManager.registerObserver(this);
//        }
//    }
//
//    @Override
//    public void onPause()
//    {
//        super.onPause();
//        
//        if (messageManager != null)
//        {
//            // 消息注销
//            messageManager.unregisterObserver(this);
//        }
//    }
//    
//    /**
//     * 得到传值数据方法
//     */
//    private void getIntentData()
//    {
//        Intent intent = getIntent();
//        m_content = intent.getStringExtra("content");
//    }
//
//    /**
//     * 控件初始化方法
//     */
//    private void findView()
//    {
//        m_backBtn = (Button) findViewById(R.id.product_list_btn_back);
//        m_progressBar = (ProgressBar) findViewById(R.id.product_list_progress);
//        m_listXLv = (XListView) findViewById(R.id.product_list_xlv_list);
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
//                ProductListActivity.this.finish();
//            }
//        });
//        
//        m_listXLv.setOnItemClickListener(this);
//    }
//    
//    /**
//     * 加载数据方法
//     */
//    private void loadData()
//    {
//        messageManager.searchProduct(UserManager.getInstance().m_user.userId, m_content);
//    }
//    
//    /**
//     * 设置数据显示方法
//     * @param list
//     */
//    private void setAdapter(List<ProductDTO> list)
//    {
//        m_list = list;
//        m_adpater = new ProductListAdapter(ProductListActivity.this, list);
//        m_listXLv.setAdapter(m_adpater);
//    }
//
//    @Override
//    public void searchProductCB(Reulst result_state, ArrayList<ProductDTO> list)
//    {
//        super.searchProductCB(result_state, list);
//        
//        if (result_state.resultCode == ReturnCode.RETURNCODE_OK)
//        {
//            m_progressBar.setVisibility(View.GONE);
//            m_listXLv.setVisibility(View.VISIBLE);
//            m_listXLv.setPullLoadEnable(false);
//            setAdapter(list);
//        }
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
//    {
//        ProductDTO productDTO = (ProductDTO)arg0.getAdapter().getItem(arg2);
//        Intent intent = new Intent(ProductListActivity.this, ProductDetailActivity.class);
//        intent.putExtra("productId", productDTO.productId);
//        startActivity(intent);
//    }
//    
//    
//}
