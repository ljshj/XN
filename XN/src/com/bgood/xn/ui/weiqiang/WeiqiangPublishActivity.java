package com.bgood.xn.ui.weiqiang;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.bgood.xn.R;
import com.bgood.xn.adapter.ImageAdapter;
import com.bgood.xn.bean.WeiQiangBean;
import com.bgood.xn.location.ILocationCallback;
import com.bgood.xn.location.ILocationManager;
import com.bgood.xn.location.LocationManagerFactory;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.FileRequest;
import com.bgood.xn.network.request.WeiqiangRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.utils.ImgUtils;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.LoadingProgress;
import com.bgood.xn.view.dialog.BottomDialog;
import com.bgood.xn.widget.TitleBar;
import com.umeng.analytics.MobclickAgent;

/**
 * @todo:发布微墙
 * @date:2014-11-10 下午4:08:55
 * @author:hg_liuzl@163.com
 */
public class WeiqiangPublishActivity extends BaseActivity implements OnItemClickListener,TaskListenerWithState,OnClickListener
{
	/** 从相册选择照片 **/
	private static final int FLAG_CHOOSE_FROM_IMGS = 100;
	/** 从手机获取照片 **/
	private static final int FLAG_CHOOSE_FROM_CAMERA = FLAG_CHOOSE_FROM_IMGS + 1;
	
	private File tempFile = null; // 文件
	
	/**最大图片数*/
	public static final int MAX_SIZE = 9;

	
	private GridView gridview_images;
	private ImageAdapter adapter;
	private EditText comment_content;
    
    private String m_content = null;
    
    private List<File> files = new ArrayList<File>();	//文件集合
    private List<Bitmap> bitmaps = new ArrayList<Bitmap>(); //图片集合
    
    private String[] imgs;
    private String[] smallImgs;
    
    private int uploadCount = 0;
    
    private TitleBar mTitleBar;
    private WeiQiangBean mWeiqiangBean = null;

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
		setContentView(R.layout.layout_weiqiang_publish);
		mWeiqiangBean = (WeiQiangBean) getIntent().getSerializableExtra(WeiQiangBean.KEY_WEIQIANG_BEAN);
		mTitleBar = new TitleBar(mActivity);
		mTitleBar.initAllBar(null == mWeiqiangBean ?"发微墙":"编辑微墙", null == mWeiqiangBean?"发表":"编辑");
		mTitleBar.rightBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doSubmit();
			}
		});
		initViews();
		setData();
	}

	private void initViews()
	{
		gridview_images = (GridView) findViewById(R.id.gridview_images);
		comment_content = (EditText) findViewById(R.id.comment_content);
	}
	
	/**
	 * 
	 * @todo:设置数据
	 * @date:2015-2-4 下午3:55:16
	 * @author:hg_liuzl@163.com
	 * @params:
	 */
	private void setData() {
		if(null == mWeiqiangBean){
			adapter = new ImageAdapter(bitmaps,this,this);
			gridview_images.setAdapter(adapter);
			gridview_images.setOnItemClickListener(this);
			files.add(null);
			bitmaps.add(null);
		}else{
			if(mWeiqiangBean.imgs.size()>0){
				adapter = new ImageAdapter(mWeiqiangBean.imgs,this,this);
				gridview_images.setAdapter(adapter);
				gridview_images.setVisibility(View.VISIBLE);
			}else{
				gridview_images.setVisibility(View.GONE);
			}
			comment_content.setText(mWeiqiangBean.content);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		if(position == bitmaps.size()-1){
			if(bitmaps.size() <= MAX_SIZE){
				showPicDialog();
			}
		}
	}
	
	private void checkInfo()
	{
		m_content = comment_content.getText().toString().trim();
		if (TextUtils.isEmpty(m_content))
		{
			BToast.show(mActivity, "请输入微墙内容!");
			return;
		}else{
			if(null != mWeiqiangBean ){	//如果是修改微墙
				WeiqiangRequest.getInstance().requestWeiqiangUpdate(this, mActivity, m_content,mWeiqiangBean.weiboid);
			}else{ //添加微墙
				WeiqiangRequest.getInstance().requestWeiqiangSend(this, mActivity, m_content, imgs, smallImgs, "", String.valueOf(BGApp.location.longitude), String.valueOf(BGApp.location.latitude));
			}
		}
	}
	
	
	   
    private BottomDialog dialog = null;
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
		Bitmap bitmap = ImgUtils.compressImageFromFile(path);
		bitmaps.add(bitmaps.size()-1, bitmap);
		ImgUtils.compressBmpToFile(bitmap, tempFile);	//图片压缩一下
		files.add(files.size()-1,tempFile);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			switch (bNetWork.getMessageType()) {
			case 860003:	//发微墙
				LoadingProgress.getInstance().dismiss();
				if(bNetWork.getReturnCode() ==  ReturnCode.RETURNCODE_OK){
					BToast.show(mActivity,"微墙发送成功");
					finish();
				}else{
					BToast.show(mActivity,"微墙发送失败");
				}
				break;		
			case 860011: //修改微墙
				if(bNetWork.getReturnCode() ==  ReturnCode.RETURNCODE_OK){
					BToast.show(mActivity," 修改成功");
					finish();
				}else{
					BToast.show(mActivity,"修改失败");
				}
				break;
			default:	//因为上传图片 没有设置messageType
				JSONObject object = bNetWork.getBody();
				String status = object.optString("success");
				if(status.equalsIgnoreCase("true")){
					smallImgs[uploadCount] = object.optString("smallurl");
					imgs[uploadCount] = object.optString("url");
					uploadCount++;
					if(uploadCount < files.size()){	//上传图片
						ImgUtils.compressBmpToFile(BitmapFactory.decodeFile(files.get(uploadCount).getAbsolutePath()), files.get(uploadCount));
						FileRequest.getInstance().requestUpLoadFile(this,mActivity,false,files.get(uploadCount), String.valueOf(BGApp.mUserId), "webo", "jpg");
					}else{	//图片上传完毕
						checkInfo();
					}
				}else{
					BToast.show(mActivity, "图片上传失败");
				}
				break;
			}
		}
	}
	
	/**
	 * 
	 * @todo:提交
	 * @date:2015-2-4 下午4:07:12
	 * @author:hg_liuzl@163.com
	 * @params:
	 */
	private void doSubmit() {
		MobclickAgent.onEvent(WeiqiangPublishActivity.this,"weiqiang_publish_click");
		if(null!=mWeiqiangBean){
			checkInfo();
		}else{
			files.remove(files.size()-1);	//移除最后一张空白的图片
			if(files.size()>0){	//如果有图片的话
				m_content = comment_content.getText().toString().trim();
				if (TextUtils.isEmpty(m_content))
				{
					BToast.show(mActivity, "请输入微墙内容!");
					return;
				}
				imgs = new String[files.size()];
				smallImgs = new String[files.size()];
				LoadingProgress.getInstance().show(mActivity, "正在发送微墙");
				ImgUtils.compressBmpToFile(BitmapFactory.decodeFile(files.get(uploadCount).getAbsolutePath()), files.get(uploadCount));
	
				FileRequest.getInstance().requestUpLoadFile(this,mActivity,false,files.get(uploadCount), String.valueOf(BGApp.mUserId), "webo", "jpg");
			}else{
				checkInfo();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
		case R.id.send_weiqiang_imgv_delete:
			int position = (Integer) v.getTag();
			bitmaps.remove(position);
			files.remove(position);
			adapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
		
	}
}
