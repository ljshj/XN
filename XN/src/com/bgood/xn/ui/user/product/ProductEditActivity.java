//package com.bgood.xn.ui.user.product;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.os.UserManager;
//import android.provider.MediaStore;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.CompoundButton.OnCheckedChangeListener;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.bgood.xn.R;
//import com.bgood.xn.bean.UserBean.UserState;
//import com.bgood.xn.network.BaseNetWork.ReturnCode;
//import com.bgood.xn.ui.BaseActivity;
//import com.squareup.picasso.Picasso;
//
///**
// * 产品编辑页面
// */
//public class ProductEditActivity extends BaseActivity
//{
//    private ImageView m_photoImgV     = null; // 图片
//    private EditText  m_productNameEt = null; // 添加姓名输入框
//    private EditText  m_productPriceEt = null; // 添加产品价格
//    private CheckBox  m_recommendCb   = null; // 是否推荐选择
//    private EditText  m_infoEt        = null; // 产品信息输入框
//    private Button    m_doneBtn       = null; // 确认添加按钮
//    
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_product_edit);
//        
//        getIntentData();
//        findView();
//        setListener();
//        setData(productDTOIntent);
//    }
//
//    
//    /**
//     * 设置数据显示方法
//     */
//    private void setData(ProductDTO productDTO)
//    {
//        if (productDTO.productSmallIcon != null && !productDTO.productSmallIcon.equals(""))
//            Picasso.with(this).load(productDTO.productSmallIcon).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(m_photoImgV);
//        
//        m_productNameEt.setText(productDTO.productName);
//        m_productPriceEt.setText(productDTO.productPrice);
//        m_infoEt.setText(productDTO.productInfo);
//    }
//    
//    /**
//     * 控件初始化方法
//     */
//    private void findView()
//    {
//        m_backBtn = (Button) findViewById(R.id.product_edit_btn_back);
//        m_photoImgV = (ImageView) findViewById(R.id.product_edit_imgv_photo);
//        m_productNameEt = (EditText) findViewById(R.id.product_edit_et_name);
//        m_productPriceEt = (EditText) findViewById(R.id.product_edit_et_price);
//        m_recommendCb = (CheckBox) findViewById(R.id.product_edit_cb_recommend);
//        m_infoEt = (EditText) findViewById(R.id.product_edit_et_info);
//        m_doneBtn = (Button) findViewById(R.id.product_edit_btn_done);
//        
//        submit = new SubmitPostBar();
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
//                ProductEditActivity.this.finish();
//            }
//        });
//        
//        // 图片按钮
//        m_photoImgV.setOnClickListener(new OnClickListener()
//        {
//
//            @Override
//            public void onClick(View v)
//            {
//                getFdsUtil().photoPopupIn(ProductEditActivity.this, R.layout.layout_personal_data_dialog);
//            }
//        });
//        
//        m_recommendCb.setOnCheckedChangeListener(new OnCheckedChangeListener()
//        {
//            
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//            {
//                if (isChecked)
//                {
//                    m_recommend = 1;
//                }
//                else
//                {
//                    m_recommend = 0;
//                }
//            }
//        });
//
//        // 确定添加
//        m_doneBtn.setOnClickListener(new OnClickListener()
//        {
//
//            @Override
//            public void onClick(View v)
//            {
//                if (m_loadIndex == 0)
//                {
//                    if (chenkInfo())
//                    {
//                        m_productDTO.productBigIcon = productDTOIntent.productBigIcon;
//                        m_productDTO.productSmallIcon = productDTOIntent.productSmallIcon;
//                        load(m_productDTO, m_recommend);
//                    }
//                }
//                else
//                {
//                    submit.sendComment();
//                }
//                
//            }
//        });
//    }
//    
//    
//    /**
//     * 检查信息
//     */
//    private boolean chenkInfo()
//    {
//        String productName = m_productNameEt.getText().toString().trim();
//        if (productName == null || productName.equals(""))
//        {
//            Toast.makeText(this, "产品名不能为空！", 0).show();
//            return false;
//        }
//        else
//        {
//            m_productDTO.productName = productName;
//        }
//        
//        String productPrice = m_productPriceEt.getText().toString().trim();
//        if (productPrice == null || productPrice.equals(""))
//        {
//            Toast.makeText(this, "产品价格不能为空！", 0).show();
//            return false;
//        }
//        else
//        {
//            m_productDTO.productPrice = productPrice;
//        }
//        
//        String productInfo = m_infoEt.getText().toString().trim();
//        if (productInfo == null || productInfo.equals(""))
//        {
//            Toast.makeText(this, "产品信息不能为空！", 0).show();
//            return false;
//        }
//        else
//        {
//            m_productDTO.productInfo = productInfo;
//        }
//        
//        if (m_recommend == 1)
//        {
////            if ()
//        }
//        
//        return true;
//    }
//    
//    /**
//     * 添加产品方法
//     * @param productDTO
//     * @param brecom
//     */
//    private void load(ProductDTO productDTO, int brecom)
//    {
//        WindowUtil.getInstance().progressDialogShow(ProductEditActivity.this, "修改产品中...");
//        messageManager.modifyProduct(productDTO, brecom + "");
//    }
//
//    @Override
//    public void modifyProductCB(Reulst result_state)
//    {
//        super.modifyProductCB(result_state);
//        
//        WindowUtil.getInstance().DismissAllDialog();
//        
//        if (result_state.resultCode == ReturnCode.RETURNCODE_OK)
//        {
//            Toast.makeText(this, "修改成功！", Toast.LENGTH_LONG).show();
//            finish();
//        }
//        else
//        {
//            Toast.makeText(this, "修改失败！", Toast.LENGTH_LONG).show();
//        }
//    }
//
//}
