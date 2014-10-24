//package com.bgood.xn.ui.weiqiang;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.location.Location;
//import android.os.Bundle;
//import android.os.UserManager;
//import android.provider.MediaStore;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.EditText;
//import android.widget.GridView;
//import android.widget.Toast;
//
//import com.baidu.mapapi.BMapManager;
//import com.baidu.mapapi.LocationListener;
//import com.baidu.mapapi.MKLocationManager;
//import com.bgood.xn.R;
//import com.bgood.xn.adapter.WeiqiangPublishAdapter;
//import com.bgood.xn.bean.UserBean.UserState;
//import com.bgood.xn.network.BaseNetWork.ReturnCode;
//import com.bgood.xn.ui.BaseActivity;
//
///**
// * 
// * @todo:发微墙
// * @date:2014-10-24 上午10:19:23
// * @author:hg_liuzl@163.com
// */
//
//public class WeiqiangPublishActivity extends BaseActivity implements OnItemClickListener, LocationListener, OnClickListener
//{
//	private GridView gridview_images;
//	private WeiqiangPublishAdapter adapter;
//	private EditText comment_content;
//	
//	private BMapManager mapManager;
//    private MKLocationManager mLocationManager = null;
//    
//    private double m_longitude = 0;    // 经度
//    private double m_latitude = 0;     // 纬度
//    
//    private FDSUtil fdsUtil;
//    private String m_content = "";
//    private int ib_add_image_position;
//    
//    private SubmitInterface submit;
//    
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.layout_weiqiang_publish);
//		initViews();
//		setListeners();
//		initData();
//	}
//
//	@Override
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
//	@Override
//	protected void initData()
//	{
//		super.initData();
//	}
//
//	@Override
//	protected void initViews()
//	{
//		super.initViews();
//		gridview_images = (GridView) findViewById(R.id.gridview_images);
//		
//		adapter = new WeiqiangPublishAdapter(this, selFilepaPathList);
//		gridview_images.setAdapter(adapter);
//		gridview_images.setOnItemClickListener(this);
//		comment_content = (EditText) findViewById(R.id.comment_content);
//		
//		submit = new SubmitPostBar();
//	}
//
//	@Override
//	protected void setListeners()
//	{
//		super.setListeners();
//		findViewById(R.id.weiqiang_publish_submit).setOnClickListener(this);
//	}
//	
//	private void setBaiduData()
//    {
//     // 初始化MapActivity
//        mapManager = new BMapManager(getApplication());
//        // init方法的第一个参数需填入申请的API Key
//        mapManager.init("C66C0501D0280744759A6957C42543AE38F5D540", null);
////        initMapActivity(mapManager);
//
//        mLocationManager = mapManager.getLocationManager();
//        // 注册位置更新事件
//        mLocationManager.requestLocationUpdates(this);
//        // 使用GPS定位
//        mLocationManager.enableProvider((int) MKLocationManager.MK_GPS_PROVIDER);
//    }
//	
//	/**
//     * 当位置发生变化时触发此方法
//     * 
//     * @param location 当前位置
//     */
//    public void onLocationChanged(Location location)
//    {
//        if (location != null) 
//        {
//            // 显示定位结果
//            m_longitude = location.getLongitude();
//            m_latitude = location.getLatitude();
////            Toast.makeText(this, "经度：" +m_longitude + "纬度：" + m_latitude , 0).show();
//        }
//        
//    }
//
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//	{
//		ib_add_image_position = position;
//		toPickPhoto();
//	}
//
//	@Override
//	public void publishWeiqiangCB(Reulst result_state)
//	{
//		super.publishWeiqiangCB(result_state);
//		WindowUtil.getInstance().DismissAllDialog();
//		if (result_state.resultCode == ReturnCode.RETURNCODE_OK)
//		{
//			Toast.makeText(this, "发布成功", Toast.LENGTH_SHORT).show();
//			finish();
//		} else
//		{
//			Toast.makeText(this, "发布失败", Toast.LENGTH_SHORT).show();
//		}
//	}
//
//	@Override
//	public void onClick(View v)
//	{
//		switch (v.getId())
//		{
//		case R.id.weiqiang_publish_submit:
//		{
////			WindowUtil.getInstance().progressDialogShow(this, "正在发布");
////			String content = comment_content.getText().toString();
////			if (content == null || content.trim().length() <= 0)
////			{
////				Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
////				return;
////			}
////			String time = DateManager.FormatTimeYYDDMMHHMMSMillis(System.currentTimeMillis());
//			submit.sendComment();
//			break;
//		}
//		}
//
//	}
//	
//	private boolean checkInfo()
//	{
//		String content = comment_content.getText().toString();
//		if (content == null || content.trim().length() <= 0)
//		{
//			Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
//			return false;
//		}
//		else
//		{
//			m_content = content;
//		}
//		return true;
//	}
//	
//	/**
//	 * 上传微墙微博数据方法
//	 * @param content 内容
//	 * @param imgs 图片
//	 * @param time 时间
//	 * @param longitude 经度
//	 * @param latitude 维度
//	 */
//	private void loadData(String content, String[] imgs, String[] smallImgs, float longitude, float latitude)
//	{
//		String time = DateManager.FormatTimeYYDDMMHHMMSMillis(System.currentTimeMillis());
//		// TODO 微墙发布 差经纬度
//		messageManager.publishWeiqiang(content, imgs, smallImgs, time, longitude, latitude);
//	}
//	
//	private void toPickPhoto() {
//		getFdsUtil().photoPopupIn(WeiqiangPublishActivity.this, R.layout.layout_personal_data_dialog);
//	}
//	
//	private FDSUtil getFdsUtil() {
//        if (fdsUtil == null) {
//            fdsUtil = new FDSUtil(WeiqiangPublishActivity.this, new onPhotoReceiverListeners() {
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
//                    Bitmap bitmap = fdsUtil.loadImgThumbnail(WeiqiangPublishActivity.this, imgName, MediaStore.Images.Thumbnails.MINI_KIND);
////                    m_photoImgV.setImageBitmap(bitmap);
//                    if (ib_add_image_position >= selFilepaPathList.size())
//                    {
//                        selFilepaPathList.add(imagePath);
//                        endFilepaPathList.add(end);
//                    }
//                    else {
//                    	selFilepaPathList.add(ib_add_image_position, imagePath);
//                    	selFilepaPathList.remove(ib_add_image_position + 1);
//                    }
//                    adapter.notifyDataSetChanged();
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
//    private List<String> selFilepaPathList = new ArrayList<String>();
//    private List<String> endFilepaPathList = new ArrayList<String>();
//    private int uploadImageSuccessCount;
//    
//    interface SubmitInterface {
//		void sendComment();
//
//		void sendReply(FDSState state, String ID);
//	}
//
//    
//    class SubmitPostBar implements SubmitInterface {
//
//    	String[] bigImages = null;
//    	String[] smallImages = null;
//    	
//		public SubmitPostBar() {
//		}
//
//		@Override
//		public void sendComment() {
//			hideSoftInputFromWindow();
//			if (UserManager.getInstance().m_userState != UserState.UserState_LOGINED) {
//				WindowUtil.getInstance().dialogLoginShow(WeiqiangPublishActivity.this);
//				return;
//			}
//			 if (checkInfo())
//			 {
//				if (selFilepaPathList != null && selFilepaPathList.size() > 0) {
//					bigImages = new String[selFilepaPathList.size()];
//					smallImages = new String[selFilepaPathList.size()];
//					uploadImageSuccessCount = 0;
//					WindowUtil.getInstance().progressDialogShow(WeiqiangPublishActivity.this, "正在上传图片" + "0 /" + bigImages.length);
//
//					for (int i = 0; i < selFilepaPathList.size(); i++) {
//						final String image = selFilepaPathList.get(i);
//						if (image == null || image.trim().length() <= 0)
//							continue;
//						final int index = i;
//						getFdsUtil().uploadPic(image, "1",
//								PathManager.FILE_UPDATE_TAG_USERINFO, endFilepaPathList.get(i), new onReceiverListeners() {
//
//									@Override
//									public void onReceive(String isSuccess, String bigPath, String smallPath) {
//										if (isSuccess != null && isSuccess.equalsIgnoreCase("true")) {
//											bigImages[index] = bigPath;
//											smallImages[index] = smallPath;
//										}
//										else
//										{
//											Toast.makeText(WeiqiangPublishActivity.this, "上传图片失败！", Toast.LENGTH_LONG).show();
//											WindowUtil.getInstance().DismissAllDialog();
//											return;
//										}
//										uploadImageSuccessCount++;
//										WindowUtil.getInstance().progressDialogShow(WeiqiangPublishActivity.this,
//												"正在上传图片" + uploadImageSuccessCount + "/" + bigImages.length);
//										if (uploadImageSuccessCount >= bigImages.length) {
//											WindowUtil.getInstance().progressDialogShow(WeiqiangPublishActivity.this, "正在评论");
//											send();
//										}
//									}
//
//								});
//					}
//				} else {
//					WindowUtil.getInstance().progressDialogShow(WeiqiangPublishActivity.this, "正在上传数据");
//					send();
//				}
//			}
//		}
//
//		private void send() {
//			
//			loadData(m_content, bigImages, smallImages, 114.1917953491211f, 22.636533737182617f);
////			postBarMessageManager
////					.sendPostComment("comment", postID, null, UserManager.getInstance().m_user.m_userID, comment_send.content, comment_send.images);
//		}
//
//		@Override
//		public void sendReply(FDSState state, String ID) {
//			WindowUtil.getInstance().DismissAllDialog();
//			if (state != null && state.result.equals("OK")) {
//				finish();
//			}
//
//		}
//	}
//    
//    public static String getTheUUID()
//	{
//		String uniqueid;
//		uniqueid = UUID.randomUUID().toString();
//		return uniqueid;
//	}
//}
