package com.bgood.xn.ui.user.product;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.squareup.picasso.Picasso;
import com.zhuozhong.bandgood.R;
import com.zhuozhong.bandgood.activity.BaseActivity;
import com.zhuozhong.bandgood.activity.usercenter.AddProductActivity.SubmitInterface;
import com.zhuozhong.bandgood.activity.usercenter.AddProductActivity.SubmitPostBar;
import com.zhuozhong.bandgood.bean.ProductDTO;
import com.zhuozhong.bandgood.bean.Reulst;
import com.zhuozhong.bandgood.bean.UserDTO.UserState;
import com.zhuozhong.bandgood.messagemanager.ShowCaseMessageManager;
import com.zhuozhong.manager.PathManager;
import com.zhuozhong.manager.UserManager;
import com.zhuozhong.util.FDSState;
import com.zhuozhong.util.FDSUtil;
import com.zhuozhong.util.WindowUtil;
import com.zhuozhong.util.FDSUtil.onPhotoReceiverListeners;
import com.zhuozhong.util.FDSUtil.onReceiverListeners;
import com.zhuozhong.zzframework.session.Frame.ReturnCode;

/**
 * 产品编辑页面
 */
public class ProductEditActivity extends BaseActivity
{
    private Button    m_backBtn       = null; // 返回按钮
    private ImageView m_photoImgV     = null; // 图片
    private EditText  m_productNameEt = null; // 添加姓名输入框
    private EditText  m_productPriceEt = null; // 添加产品价格
    private CheckBox  m_recommendCb   = null; // 是否推荐选择
    private EditText  m_infoEt        = null; // 产品信息输入框
    private Button    m_doneBtn       = null; // 确认添加按钮
    
    ShowCaseMessageManager messageManager = ShowCaseMessageManager.getInstance();
    
    private FDSUtil fdsUtil;
    
    private int m_recommend = 1;
    ProductDTO m_productDTO = new ProductDTO();;
    ProductDTO productDTOIntent = null;
    private SubmitInterface submit;
    
    private int m_loadIndex = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_product_edit);
        
        getIntentData();
        findView();
        setListener();
        setData(productDTOIntent);
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
        
        if (messageManager != null)
        {
            // 消息注册
            messageManager.registerObserver(this);
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        
        if (messageManager != null)
        {
            // 消息注销
            messageManager.unregisterObserver(this);
        }
    }

    /**
     * 得到传值数据方法
     */
    private void getIntentData()
    {
        Intent intent = getIntent();
        productDTOIntent = (ProductDTO)intent.getSerializableExtra("productDTO");
        m_productDTO.productId = productDTOIntent.productId;
    }
    
    /**
     * 设置数据显示方法
     */
    private void setData(ProductDTO productDTO)
    {
        if (productDTO.productSmallIcon != null && !productDTO.productSmallIcon.equals(""))
            Picasso.with(this).load(productDTO.productSmallIcon).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(m_photoImgV);
        
        m_productNameEt.setText(productDTO.productName);
        m_productPriceEt.setText(productDTO.productPrice);
        m_infoEt.setText(productDTO.productInfo);
    }
    
    /**
     * 控件初始化方法
     */
    private void findView()
    {
        m_backBtn = (Button) findViewById(R.id.product_edit_btn_back);
        m_photoImgV = (ImageView) findViewById(R.id.product_edit_imgv_photo);
        m_productNameEt = (EditText) findViewById(R.id.product_edit_et_name);
        m_productPriceEt = (EditText) findViewById(R.id.product_edit_et_price);
        m_recommendCb = (CheckBox) findViewById(R.id.product_edit_cb_recommend);
        m_infoEt = (EditText) findViewById(R.id.product_edit_et_info);
        m_doneBtn = (Button) findViewById(R.id.product_edit_btn_done);
        
        submit = new SubmitPostBar();
    }

    /**
     * 控件事件监听方法
     */
    private void setListener()
    {
        m_backBtn.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                ProductEditActivity.this.finish();
            }
        });
        
        // 图片按钮
        m_photoImgV.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                getFdsUtil().photoPopupIn(ProductEditActivity.this, R.layout.layout_personal_data_dialog);
            }
        });
        
        m_recommendCb.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    m_recommend = 1;
                }
                else
                {
                    m_recommend = 0;
                }
            }
        });

        // 确定添加
        m_doneBtn.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (m_loadIndex == 0)
                {
                    if (chenkInfo())
                    {
                        m_productDTO.productBigIcon = productDTOIntent.productBigIcon;
                        m_productDTO.productSmallIcon = productDTOIntent.productSmallIcon;
                        load(m_productDTO, m_recommend);
                    }
                }
                else
                {
                    submit.sendComment();
                }
                
            }
        });
    }
    
    private FDSUtil getFdsUtil() {
        if (fdsUtil == null) {
            fdsUtil = new FDSUtil(ProductEditActivity.this, new onPhotoReceiverListeners() {

                @Override
                public void onReceive(File file) {
                    fdsUtil.progressDialogDismiss();
                    String imagePath = file.getAbsolutePath();
                    String end = FDSUtil.getFileName(imagePath);
                    String imgName = null;
                    try
                    {
                        imgName = fdsUtil.getFileName(imagePath);
                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Bitmap bitmap = fdsUtil.loadImgThumbnail(ProductEditActivity.this, imgName, MediaStore.Images.Thumbnails.MINI_KIND);
                    m_photoImgV.setImageBitmap(bitmap);
                    m_loadIndex = 1;
//                    if (ib_add_image_position >= listImges.size())
                        selFilepaPathList.add(imagePath);
                        endFilepaPathList.add(end);
//                    else {
//                        listImges.add(ib_add_image_position, imagePath);
//                        listImges.remove(ib_add_image_position + 1);
//                    }
//                    adapter.notifyDataSetChanged();
                }
            });
        }
        return fdsUtil;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (fdsUtil != null)
            fdsUtil.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    /**
     * 检查信息
     */
    private boolean chenkInfo()
    {
        String productName = m_productNameEt.getText().toString().trim();
        if (productName == null || productName.equals(""))
        {
            Toast.makeText(this, "产品名不能为空！", 0).show();
            return false;
        }
        else
        {
            m_productDTO.productName = productName;
        }
        
        String productPrice = m_productPriceEt.getText().toString().trim();
        if (productPrice == null || productPrice.equals(""))
        {
            Toast.makeText(this, "产品价格不能为空！", 0).show();
            return false;
        }
        else
        {
            m_productDTO.productPrice = productPrice;
        }
        
        String productInfo = m_infoEt.getText().toString().trim();
        if (productInfo == null || productInfo.equals(""))
        {
            Toast.makeText(this, "产品信息不能为空！", 0).show();
            return false;
        }
        else
        {
            m_productDTO.productInfo = productInfo;
        }
        
        if (m_recommend == 1)
        {
//            if ()
        }
        
        return true;
    }
    
    /**
     * 添加产品方法
     * @param productDTO
     * @param brecom
     */
    private void load(ProductDTO productDTO, int brecom)
    {
        WindowUtil.getInstance().progressDialogShow(ProductEditActivity.this, "修改产品中...");
        messageManager.modifyProduct(productDTO, brecom + "");
    }

    @Override
    public void modifyProductCB(Reulst result_state)
    {
        super.modifyProductCB(result_state);
        
        WindowUtil.getInstance().DismissAllDialog();
        
        if (result_state.resultCode == ReturnCode.RETURNCODE_OK)
        {
            Toast.makeText(this, "修改成功！", Toast.LENGTH_LONG).show();
            finish();
        }
        else
        {
            Toast.makeText(this, "修改失败！", Toast.LENGTH_LONG).show();
        }
    }
    
    private List<String> selFilepaPathList = new ArrayList<String>();
    private List<String> endFilepaPathList = new ArrayList<String>();
    private int uploadImageSuccessCount;
    
    interface SubmitInterface {
        void sendComment();

        void sendReply(FDSState state, String ID);
    }

    
    class SubmitPostBar implements SubmitInterface {

        public SubmitPostBar() {
        }

        @Override
        public void sendComment() {
            hideSoftInputFromWindow();
            if (UserManager.getInstance().m_userState != UserState.UserState_LOGINED) {
                WindowUtil.getInstance().dialogLoginShow(ProductEditActivity.this);
                return;
            }
             if (chenkInfo())
             {
                if (selFilepaPathList != null && selFilepaPathList.size() > 0) {
                    final String[] images = new String[selFilepaPathList.size()];
                    uploadImageSuccessCount = 0;
                    WindowUtil.getInstance().progressDialogShow(ProductEditActivity.this, "正在上传图片" + "0 /" + images.length);

                    for (int i = 0; i < selFilepaPathList.size(); i++) {
                        final String image = selFilepaPathList.get(i);
                        if (image == null || image.trim().length() <= 0)
                            continue;
                        final int index = i;
                        getFdsUtil().uploadPic(image, "1",
                                PathManager.FILE_UPDATE_TAG_USERINFO, endFilepaPathList.get(i), new onReceiverListeners() {

                                    @Override
                                    public void onReceive(String isSuccess, String bigPath, String smallPath) {
                                        if (isSuccess != null && isSuccess.equalsIgnoreCase("true")) {
                                            images[index] = bigPath;
                                            m_productDTO.productBigIcon = bigPath;
                                            m_productDTO.productSmallIcon = smallPath;
                                        }
                                        else
                                        {
                                            Toast.makeText(ProductEditActivity.this, "上传图片失败！", Toast.LENGTH_LONG).show();
                                            WindowUtil.getInstance().DismissAllDialog();
                                            return;
                                        }
                                        uploadImageSuccessCount++;
                                        WindowUtil.getInstance().progressDialogShow(ProductEditActivity.this,
                                                "正在上传图片" + uploadImageSuccessCount + "/" + images.length);
                                        if (uploadImageSuccessCount >= images.length) {
                                            WindowUtil.getInstance().progressDialogShow(ProductEditActivity.this, "正在评论");
                                            send();
                                        }
                                    }

                                });
                    }
                } else {
                    WindowUtil.getInstance().progressDialogShow(ProductEditActivity.this, "正在上传数据");
                    send();
                }
            }
        }

        private void send() {
            
            load(m_productDTO, m_recommend);
//          postBarMessageManager
//                  .sendPostComment("comment", postID, null, UserManager.getInstance().m_user.m_userID, comment_send.content, comment_send.images);
        }

        @Override
        public void sendReply(FDSState state, String ID) {
            WindowUtil.getInstance().DismissAllDialog();
            if (state != null && state.result.equals("OK")) {
                finish();
            }

        }
    }
    
    public static String getTheUUID()
    {
        String uniqueid;
        uniqueid = UUID.randomUUID().toString();
        return uniqueid;
    }
}
