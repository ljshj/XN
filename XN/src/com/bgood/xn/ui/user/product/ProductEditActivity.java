package com.bgood.xn.ui.user.product;

import java.io.File;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.bgood.xn.R;
import com.bgood.xn.bean.ProductBean;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.FileRequest;
import com.bgood.xn.network.request.ProductRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.system.Const;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.utils.ImgUtils;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.dialog.BottomDialog;
import com.bgood.xn.widget.TitleBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @todo:产品编辑界面
 * @date:2014-11-17 下午3:14:58
 * @author:hg_liuzl@163.com
 */
public class ProductEditActivity extends BaseActivity implements OnClickListener,TaskListenerWithState
{
	/** 从相册选择照片 **/
	private static final int FLAG_CHOOSE_FROM_IMGS = 100;
	/** 从手机获取照片 **/
	private static final int FLAG_CHOOSE_FROM_CAMERA = FLAG_CHOOSE_FROM_IMGS + 1;
	
	private File tempFile = null; // 文件
	
    private ImageView m_photoImgV     = null; // 图片
    private EditText  m_productNameEt = null; // 添加姓名输入框
    private EditText  m_productPriceEt = null; // 添加产品价格
    private CheckBox  m_recommendCb   = null; // 是否推荐选择
    private EditText  m_infoEt        = null; // 产品信息输入框
    
    private int m_recommend = 1;
    private ProductBean m_ProductBean = null;
    private String img,img_thumb;
    private TitleBar mTitleBar;
    
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_product_edit);
        m_ProductBean = (ProductBean) getIntent().getSerializableExtra(ProductBean.KEY_PRODUCT_BEAN);
        
        mTitleBar = new TitleBar(mActivity);
        mTitleBar.initAllBar("编辑产品", "确定");
        mTitleBar.rightBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				chenkInfo();
			}
		});
        
        findView();
        setData();
    }
    /**
     * 控件初始化方法
     */
    private void findView()
    {
        m_photoImgV = (ImageView) findViewById(R.id.product_edit_imgv_photo);
        m_photoImgV.setOnClickListener(this);
        
        m_productNameEt = (EditText) findViewById(R.id.product_edit_et_name);
        m_recommendCb = (CheckBox) findViewById(R.id.product_edit_cb_recommend);
        m_infoEt = (EditText) findViewById(R.id.product_edit_et_info);
        m_recommendCb.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
            	m_recommend = isChecked?1:0;
            }
        });
        m_productPriceEt = (EditText) findViewById(R.id.product_edit_et_price);
        m_productPriceEt.addTextChangedListener(new TextWatcher() 
        {
            public void afterTextChanged(Editable edt) 
            {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2)
                {
                    edt.delete(posDot + 3, posDot + 4);
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
        });
        
    }
    
    
    /**
     * 设置数据显示方法
     */
    private void setData()
    {
    	img = m_ProductBean.img;
    	img_thumb = m_ProductBean.img_thum;
    	m_recommend = m_ProductBean.brecom;
   	    
    	BGApp.getInstance().setImageSqure(m_ProductBean.img, m_photoImgV);
		 
        m_recommendCb.setChecked(m_recommend==1?true:false);
        m_productNameEt.setText(m_ProductBean.product_name);
        m_productPriceEt.setText(m_ProductBean.price);
        m_infoEt.setText(m_ProductBean.intro);
    }
    
    /**
     * 检查信息
     */
    private void chenkInfo()
    {
        String productName = m_productNameEt.getText().toString().trim();
        String productPrice = m_productPriceEt.getText().toString().trim();
        String productInfo = m_infoEt.getText().toString().trim();
        if(TextUtils.isEmpty(img) || TextUtils.isEmpty(img_thumb))
        {
        	BToast.show(mActivity, "请上传产品图片");
        	return;
        }else if (TextUtils.isEmpty(productName))
        {
            BToast.show(mActivity, "请输入产品名称");
            return;
        }else if (TextUtils.isEmpty(productPrice))
        {
            BToast.show(mActivity, "请输入产品价格");
            return;
        }else if (TextUtils.isEmpty(productInfo))
        {
            BToast.show(mActivity, "请输入产品资料");
            return;
        }else if (TextUtils.isEmpty(productName))
        {
            BToast.show(mActivity, "请输入产品名称");
            return;
        }else{
        	MobclickAgent.onEvent(this,"me_edit_product_click");
        	m_ProductBean.img = img;
        	m_ProductBean.img_thum = img_thumb;
        	m_ProductBean.product_name = productName;
        	m_ProductBean.price = productPrice;
        	m_ProductBean.intro = productInfo;
        	m_ProductBean.brecom = m_recommend;
        	ProductRequest.getInstance().requestProductModify(this, this, m_ProductBean);
        }
    }

	
	
	   
    private BottomDialog dialog = null;
    @SuppressLint("InflateParams")
	private void showPicDialog() {
		if(null == dialog){
			dialog = new BottomDialog(mActivity);
		}
		View v = inflater.inflate(R.layout.item_get_pic, null);
		v.findViewById(R.id.btn_take_photo).setOnClickListener(this);
		v.findViewById(R.id.btn_select_photo).setOnClickListener(this);
		v.findViewById(R.id.btn_cancel).setOnClickListener(this);
		dialog.setvChild(v);
		dialog.show();
	}    

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK)
			return;
		if (requestCode == FLAG_CHOOSE_FROM_IMGS) {
			if (data != null) {
				Uri uri = data.getData();
				if (!TextUtils.isEmpty(uri.getAuthority())) {
					Cursor cursor = getContentResolver().query(uri,new String[] { MediaStore.Images.Media.DATA },null, null, null);
					if (null == cursor) {
						BToast.show(mActivity, "图片没有找到");
						return;
					}
					cursor.moveToFirst();
					String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
					cursor.close();
					afterGetFile(path);
				} else {
					afterGetFile(uri.getPath());
				}
			}
		} else if (requestCode == FLAG_CHOOSE_FROM_CAMERA) {
			if (tempFile == null) {
				BToast.show(mActivity, "拍照出错，请重拍");
				return;
			}
			afterGetFile(tempFile.getAbsolutePath());
		}
	}
	
	
	/**
	 * @todo:取得图片路径后的操作
	 * @date:2014-11-10 下午6:16:51
	 * @author:hg_liuzl@163.com
	 * @params:@param path
	 */
	private void afterGetFile(String path) {
		tempFile = new File(path);
		tempFile = new File(path);
		Bitmap mBitmapUploaded = ImgUtils.compressImageFromFile(path);
		
		m_photoImgV.setImageBitmap(mBitmapUploaded);
		
		ImgUtils.compressBmpToFile(mBitmapUploaded, tempFile);
		
		FileRequest.getInstance().requestUpLoadFile(this,mActivity,true,tempFile, String.valueOf(BGApp.mUserId), "userInfo", "png");
	}

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			switch (bNetWork.getMessageType()) {
			case 830004:
				if(bNetWork.getReturnCode() ==  ReturnCode.RETURNCODE_OK){
					BToast.show(mActivity,"产品修改成功");
					finish();
				}else{
					BToast.show(mActivity,"产品修改失败");
				}
				break;					
			default:	//因为上传图片 没有设置messageType
				JSONObject object = bNetWork.getBody();
				String status = object.optString("success");
				if(status.equalsIgnoreCase("true")){
					img_thumb = object.optString("smallurl");
					img = object.optString("url");
					m_ProductBean.img_thum = img_thumb;
					m_ProductBean.img = img;
				}else{
					BToast.show(mActivity, "图片上传失败");
				}
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.product_edit_imgv_photo:
				showPicDialog();
				break;
			case R.id.btn_take_photo:
				dialog.dismiss();
				tempFile = ImgUtils.takePicture(mActivity, tempFile, FLAG_CHOOSE_FROM_CAMERA);
				break;
			case R.id.btn_select_photo:
				dialog.dismiss();
				ImgUtils.selectPicture(mActivity, FLAG_CHOOSE_FROM_IMGS);
				break;
			case R.id.btn_cancel:
				dialog.dismiss();
				break;
			default:
				break;
		}
	}

}
