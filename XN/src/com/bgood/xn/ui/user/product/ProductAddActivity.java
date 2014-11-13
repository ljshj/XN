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
//
///**
// * 添加产品页面
// */
//public class ProductAddActivity extends BaseActivity
//{
//    private Button    m_backBtn       = null; // 返回按钮
//    private ImageView m_photoImgV     = null; // 图片
//    private EditText  m_productNameEt = null; // 添加姓名输入框
//    private EditText  m_productPriceEt = null; // 添加产品价格
//    private CheckBox  m_recommendCb   = null; // 是否推荐选择
//    private EditText  m_infoEt        = null; // 产品信息输入框
//    private Button    m_doneBtn       = null; // 确认添加按钮
//
//    ShowCaseMessageManager messageManager = ShowCaseMessageManager.getInstance();
//    
//    private FDSUtil fdsUtil;
//    
//    private int m_recommend = 1;
//    ProductDTO m_productDTO = null;
//    private SubmitInterface submit;
//    
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_add_product);
//
//        findView();
//        setListener();
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
//     * 控件初始化方法
//     */
//    private void findView()
//    {
//        m_backBtn = (Button) findViewById(R.id.add_product_btn_back);
//        m_photoImgV = (ImageView) findViewById(R.id.add_product_imgv_photo);
//        m_productNameEt = (EditText) findViewById(R.id.add_product_et_name);
//        m_productPriceEt = (EditText) findViewById(R.id.add_product_et_price);
//        m_recommendCb = (CheckBox) findViewById(R.id.add_product_cb_recommend);
//        m_infoEt = (EditText) findViewById(R.id.add_product_et_info);
//        m_doneBtn = (Button) findViewById(R.id.add_product_btn_done);
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
//                AddProductActivity.this.finish();
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
//                getFdsUtil().photoPopupIn(AddProductActivity.this, R.layout.layout_personal_data_dialog);
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
//            	submit.sendComment();
//            }
//        });
//    }
//    
//    private FDSUtil getFdsUtil() {
//        if (fdsUtil == null) {
//            fdsUtil = new FDSUtil(AddProductActivity.this, new onPhotoReceiverListeners() {
//
//                @Override
//                public void onReceive(File file) {
//                    fdsUtil.progressDialogDismiss();
//                    String imagePath = file.getAbsolutePath();
//                    String end = FDSUtil.getFileName(imagePath);
//                    String imgName = null;
//                    try
//                    {
//                        imgName = fdsUtil.getFileName(imagePath);
//                    }
//                    catch (Exception e)
//                    {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                    Bitmap bitmap = fdsUtil.loadImgThumbnail(AddProductActivity.this, imgName, MediaStore.Images.Thumbnails.MINI_KIND);
//                    m_photoImgV.setImageBitmap(bitmap);
////                    if (ib_add_image_position >= listImges.size())
//                        selFilepaPathList.add(imagePath);
//                        endFilepaPathList.add(end);
////                    else {
////                        listImges.add(ib_add_image_position, imagePath);
////                        listImges.remove(ib_add_image_position + 1);
////                    }
////                    adapter.notifyDataSetChanged();
//                }
//            });
//        }
//        return fdsUtil;
//    }
//    
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (fdsUtil != null)
//            fdsUtil.onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//    
//    /**
//     * 检查信息
//     */
//    private boolean chenkInfo()
//    {
//    	m_productDTO = new ProductDTO();
//        String productName = m_productNameEt.getText().toString().trim();
//        if (productName == null || productName.equals(""))
//        {
//            Toast.makeText(this, "产品名不能为空！", 0).show();
//            return false;
//        }
//        else
//        {
//        	m_productDTO.productName = productName;
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
//        	m_productDTO.productPrice = productPrice;
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
//        	m_productDTO.productInfo = productInfo;
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
//        WindowUtil.getInstance().progressDialogShow(AddProductActivity.this, "添加产品中...");
//        messageManager.addProduct(productDTO, brecom + "");
//    }
//
//    @Override
//    public void addProductCB(Reulst result_state)
//    {
//        super.addProductCB(result_state);
//        
//        WindowUtil.getInstance().DismissAllDialog();
//        
//        if (result_state.resultCode == ReturnCode.RETURNCODE_OK)
//        {
//            Toast.makeText(this, "添加成功！", Toast.LENGTH_LONG).show();
//            finish();
//        }
//        else
//        {
//            Toast.makeText(this, "添加失败！", Toast.LENGTH_LONG).show();
//        }
//    }
//  
//}
