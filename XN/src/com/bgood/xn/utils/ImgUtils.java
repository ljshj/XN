package com.bgood.xn.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;

import com.bgood.xn.R;
import com.bgood.xn.adapter.NoScrollGridAdapter;
import com.bgood.xn.bean.ImageBean;
import com.bgood.xn.system.Const;
import com.bgood.xn.system.SystemConfig;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.photoview.ImagePagerActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @todo:图片辅助类
 * @date:2014-10-27 下午4:41:33
 * @author:hg_liuzl@163.com
 */
public class ImgUtils {
	/**
	 * @todo:绘制圆角图片
	 * @date:2014-10-27 下午4:42:34
	 * @author:hg_liuzl@163.com
	 * @params:@param bitmap
	 * @params:@return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
	    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);
	    final int color = 0xff424242;
	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	    final RectF rectF = new RectF(rect);
	    final float roundPx = 12;
	 
	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	 
	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paint);
	 
	    return output;
	  }
	
	/**
	 * 照相
	 */
	public static File takePicture(Activity mActivity,File file,int requestCode){
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				file = new File(Const.CID_IMG_STRING_PATH);
				
				if(!file.getParentFile().exists()){
					file.getParentFile().mkdirs();
				}
				Uri u = Uri.fromFile(file);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
				mActivity.startActivityForResult(intent, requestCode);
			} catch (ActivityNotFoundException e) {
			}
		} else {
			BToast.show(mActivity, "没有SD卡");
		}
		return file;
	}
	
	/**
	 * 选择照片
	 */
	public static void selectPicture(Activity mActivity,int requestCode) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		intent.setType("image/*");
		mActivity.startActivityForResult(intent, requestCode);
	}
	
	
	/**
	 * 将文件压缩成图片
	 * */
	public static Bitmap compressImageFromFile(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;//只读边,不读内容
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 800f;//
		float ww = 480f;//
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置采样率
		
		newOpts.inPreferredConfig = Config.ARGB_8888;//该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收
		
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
//		return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
									//其实是无效的,大家尽管尝试
		return bitmap;
	}
	
	/**
	 * 图片压缩成文件,且按质量压缩
	 * @param bmp
	 * @param file
	 */
	public static void compressBmpToFile(Bitmap bmp,File file){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 90;
		bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
		while (baos.toByteArray().length / 1024 > 100) {
			baos.reset();
			options -= 10;
			if(options <= 0){
				break;
			}
			
			bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baos.toByteArray());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @todo:按质量压缩
	 * @date:2015-1-21 下午4:30:01
	 * @author:hg_liuzl@163.com
	 * @params:@return
	 */
	public static byte[] comPressBmpToByte(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 90;
		bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
		while (baos.toByteArray().length / 1024 > 100) {	//把图片压缩到100k以下
			baos.reset();
			options -= 10;
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		return baos.toByteArray();
	}
	
	
	/**
	 * 打开图片查看器
	 * 
	 * @param position
	 * @param urls2
	 */
	private static void imageBrower(int position, List<ImageBean> imgList,Activity mActivity) {
		ArrayList<String> arrays = new ArrayList<String>();
		for(ImageBean img:imgList){
			arrays.add(img.img);
		}
		Intent intent = new Intent(mActivity, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, arrays);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		mActivity.startActivity(intent);
	}
	
	
	/**
	 * 
	 * @todo:九宫格展示图片
	 * @date:2015-1-22 上午10:14:46
	 * @author:hg_liuzl@163.com
	 * @params:@param list
	 * @params:@param gv
	 */
	@SuppressWarnings("null")
	public static void showImgs(final List<ImageBean> list,GridView gv,final Activity mActivity){
		if (list == null || list.size() == 0) { // 没有图片资源就隐藏GridView
			gv.setVisibility(View.GONE);
			return;
		} else {
			gv.setVisibility(View.VISIBLE);
			gv.setAdapter(new NoScrollGridAdapter(list, mActivity));
		}
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
		// 点击回帖九宫格，查看大图
		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				imageBrower(position, list,mActivity);
			}
		});
	}
	
}
