package com.bgood.xn.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;

import com.bgood.xn.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @todo:图片辅助类
 * @date:2014-10-27 下午4:41:33
 * @author:hg_liuzl@163.com
 */
public class ImgUtils {

	/**设置图片*/
    public static void setPhoto(final String imgUrl,final ImageView iv){
	    DisplayImageOptions options;
		options = new DisplayImageOptions.Builder()
		.showImageOnFail(R.drawable.icon_default)
		.showImageOnLoading(R.drawable.icon_default)
		.showImageForEmptyUri(R.drawable.icon_default)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.bitmapConfig(Bitmap.Config.RGB_565)  
		.build();
		 ImageLoader.getInstance().displayImage(imgUrl,iv, options);
    }
	
	/**
	 * 
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
	
}
