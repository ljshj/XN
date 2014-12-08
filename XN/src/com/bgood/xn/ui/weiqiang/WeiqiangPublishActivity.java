package com.bgood.xn.ui.weiqiang;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;

import com.bgood.xn.R;
import com.bgood.xn.adapter.ImageAdapter;
import com.bgood.xn.bean.ImageBean;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.FileRequest;
import com.bgood.xn.network.request.WeiqiangRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.system.Const;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.utils.WindowUtil;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.LoadingProgress;
import com.bgood.xn.view.dialog.BottomDialog;
import com.bgood.xn.widget.TitleBar;

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
    
    private double m_longitude = 0;    // 经度
    private double m_latitude = 0;     // 纬度
    
    private String m_content = null;
    
    private List<File> files = new ArrayList<File>();	//文件集合
    private List<ImageBean> listImg = new ArrayList<ImageBean>();
    
    private String[] imgs;
    private String[] smallImgs;
    
    private int uploadCount = 0;
    

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_weiqiang_publish);
		(new TitleBar(mActivity)).initTitleBar("发微墙");
		files.add(null);
		initViews();
	}

	private void initViews()
	{
		gridview_images = (GridView) findViewById(R.id.gridview_images);
		adapter = new ImageAdapter(files,this,this);
		gridview_images.setAdapter(adapter);
		gridview_images.setOnItemClickListener(this);
		comment_content = (EditText) findViewById(R.id.comment_content);
		findViewById(R.id.weiqiang_publish_submit).setOnClickListener(this);
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		if(position == files.size()-1){
			if(files.size() <= MAX_SIZE){
				showPicDialog();
			}
		}
		
//		else{
//			//查看图片
//			WindowUtil.getInstance().dialogViewPagerShow(mActivity, listImg, position);
//		}
	}
	
	private void checkInfo()
	{
		m_content = comment_content.getText().toString().trim();
		if (TextUtils.isEmpty(m_content))
		{
			BToast.show(mActivity, "请输入微墙内容!");
			return;
		}else{
			WeiqiangRequest.getInstance().requestWeiqiangSend(this, mActivity, m_content, imgs, smallImgs, "", String.valueOf(m_longitude), String.valueOf(m_latitude));
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
    
	/**
	 * 
	 * @todo 照相
	 * @author liuzenglong163@gmail.com
	 */
	private void doTakeCamera() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				tempFile = new File(Const.CID_IMG_STRING_PATH);
				Uri u = Uri.fromFile(tempFile);
				intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
				startActivityForResult(intent, FLAG_CHOOSE_FROM_CAMERA);
			} catch (ActivityNotFoundException e) {
			}
		} else {
			BToast.show(mActivity, "没有SD卡");
		}
	}
    
	/**
	 * 
	 * @todo 从相册选择照片
	 * @author liuzenglong163@gmail.com
	 */
	private void doSelectPhoto() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, FLAG_CHOOSE_FROM_IMGS);
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
		//filePath.add(path);
		tempFile = new File(path);
		files.add(files.size()-1,tempFile);
		listImg.add(new ImageBean());
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			switch (bNetWork.getMessageType()) {
			case 860003:
				LoadingProgress.getInstance().dismiss();
				if(bNetWork.getReturnCode() ==  ReturnCode.RETURNCODE_OK){
					BToast.show(mActivity,"微墙发送成功");
					finish();
				}else{
					BToast.show(mActivity,"微墙发送失败");
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
						FileRequest.getInstance().requestUpLoadFile(this,mActivity,false,files.get(uploadCount), String.valueOf(BGApp.mLoginBean.userid), "webo", "jpg");
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.weiqiang_publish_submit:
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
				FileRequest.getInstance().requestUpLoadFile(this,mActivity,false,files.get(uploadCount), String.valueOf(BGApp.mLoginBean.userid), "webo", "jpg");
			}else{
				checkInfo();
			}
			break;
		case R.id.btn_take_photo:
			dialog.dismiss();
			doTakeCamera();
			break;
		case R.id.btn_select_photo:
			dialog.dismiss();
			doSelectPhoto();
			break;
		case R.id.btn_cancel:
			dialog.dismiss();
			break;
		case R.id.send_weiqiang_imgv_delete:
			int position = (Integer) v.getTag();
			files.remove(position);
			adapter.notifyDataSetChanged();
			break;

		default:
			break;
		}
		
	}
}
