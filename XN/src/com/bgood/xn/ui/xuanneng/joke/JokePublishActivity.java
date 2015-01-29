package com.bgood.xn.ui.xuanneng.joke;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.adapter.ImageAdapter;
import com.bgood.xn.bean.JokeBean;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.http.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.http.HttpRequestInfo;
import com.bgood.xn.network.http.HttpResponseInfo;
import com.bgood.xn.network.http.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.FileRequest;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.network.request.XuannengRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.ui.user.account.RegisterActivity;
import com.bgood.xn.utils.ImgUtils;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.LoadingProgress;
import com.bgood.xn.view.dialog.BGDialog;
import com.bgood.xn.view.dialog.BottomDialog;
import com.bgood.xn.widget.TitleBar;

/**
 * @todo:有奖投稿
 * @date:2014-11-10 下午4:08:55
 * @author:hg_liuzl@163.com
 */
public class JokePublishActivity extends BaseActivity implements OnItemClickListener,TaskListenerWithState,OnClickListener
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
    
    private JokeBean mJokeBean = null;
    private TextView tvShowRule;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_weiqiang_publish);
		
		mJokeBean = (JokeBean) getIntent().getSerializableExtra(JokeBean.JOKE_BEAN);
		(new TitleBar(mActivity)).initTitleBar(null==mJokeBean ? "有奖投稿":"修改投搞");
		files.add(null);
		bitmaps.add(null);
		initViews();
		setData();
	}

	private void initViews()
	{
		tvShowRule = (TextView) findViewById(R.id.tv_show_rule);
		tvShowRule.setVisibility(View.VISIBLE);
		tvShowRule.setOnClickListener(this);
		gridview_images = (GridView) findViewById(R.id.gridview_images);
		adapter = new ImageAdapter(bitmaps,this,this);
		gridview_images.setAdapter(adapter);
		gridview_images.setOnItemClickListener(this);
		comment_content = (EditText) findViewById(R.id.comment_content);
		findViewById(R.id.weiqiang_publish_submit).setOnClickListener(this);
	}
	
	private void setData() {
		if(null == mJokeBean){
			return;
		}
		
		comment_content.setText(mJokeBean.content);
		gridview_images.setVisibility(View.GONE);
		
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
			BToast.show(mActivity, "请输入投稿内容!");
			return;
		}else{
			XuannengRequest.getInstance().requestXuanPublish(this, mActivity, m_content, imgs, smallImgs);
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
			case 870003:
				LoadingProgress.getInstance().dismiss();
				if(bNetWork.getReturnCode() ==  ReturnCode.RETURNCODE_OK){
					BToast.show(mActivity,"投稿成功,待审核发布");
					finish();
				}else{
					BToast.show(mActivity,"投稿失败");
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.weiqiang_publish_submit:
			files.remove(files.size()-1);	//移除最后一张空白的图片
			if(files.size()>0){	//如果有图片的话
				m_content = comment_content.getText().toString().trim();
				if (TextUtils.isEmpty(m_content))
				{
					BToast.show(mActivity, "请输入投稿内容!");
					return;
				}
				imgs = new String[files.size()];
				smallImgs = new String[files.size()];
				LoadingProgress.getInstance().show(mActivity, "正在投稿");
				FileRequest.getInstance().requestUpLoadFile(this,mActivity,false,files.get(uploadCount), String.valueOf(BGApp.mUserId), "webo", "jpg");
			}else{
				checkInfo();
			}
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
		case R.id.send_weiqiang_imgv_delete:
			int position = (Integer) v.getTag();
			bitmaps.remove(position);
			files.remove(position);
			adapter.notifyDataSetChanged();
			break;
		case R.id.tv_show_rule:
			createProtocolDialog();
			break;
		default:
			break;
		}
	}
	/**
	 * 
	 * @todo:使用协议
	 * @date:2015-1-24 上午10:47:52
	 * @author:hg_liuzl@163.com
	 * @params:
	 */
	private void createProtocolDialog() {
		@SuppressWarnings("deprecation")
		final BottomDialog dialog = new BottomDialog(mActivity,R.style.dialog_no_thing);
		View vProtocol = inflater.inflate(R.layout.dialog_protocol_layout, null);
		
		TextView tvRuleTitle = (TextView) vProtocol.findViewById(R.id.tv_protocol_title);
		tvRuleTitle.setText(getString(R.string.joke_rule_title));
		
		TextView tvRule = (TextView) vProtocol.findViewById(R.id.tv_protocol_content);
		tvRule.setText(getString(R.string.joke_rule));
		
		Button btnKnow = (Button) vProtocol.findViewById(R.id.btn_protocol);
		btnKnow.setText("我知道了");
		btnKnow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		Button btn =  (Button) vProtocol.findViewById(R.id.btn_dis_argee);
		btn.setVisibility(View.GONE);
		
		dialog.setvChild(vProtocol);
		dialog.show();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
