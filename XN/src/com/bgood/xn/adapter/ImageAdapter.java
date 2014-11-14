package com.bgood.xn.adapter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bgood.xn.R;
import com.bgood.xn.bean.ImageBean;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
/***
 * @todo:图片适配器
 * @date:2014-11-10 下午6:00:55
 * @author:hg_liuzl@163.com
 */
public class ImageAdapter extends KBaseAdapter {
	

	public ImageAdapter(List<?> mList, Activity mActivity,OnClickListener listener) {
		super(mList, mActivity, listener);
	}
	
	public ImageAdapter(List<?> mList, Activity mActivity) {
		super(mList, mActivity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.item_weiqiang_publish,parent, false);
			holder.imageImgV = (ImageView) convertView.findViewById(R.id.send_weiqiang_imgv_image);
			holder.deleteImgV = (ImageView) convertView.findViewById(R.id.send_weiqiang_imgv_delete);
			convertView.setTag(holder);
		} 
		else 
		{
			holder = (Holder) convertView.getTag();
		}
		
		final Object object = mList.get(position);
		
		if(object instanceof ImageBean){
			
			ImageBean bean = (ImageBean) object;
			imgType(position,bean,holder.imageImgV);
			
		}else{
			File file = (File) object;
			fileType(position,file, holder.deleteImgV, holder.imageImgV);
		}
		return convertView;
	}
	
	/**文件方式展示图片*/
	private void fileType(int position,File file,ImageView ivDelete,ImageView ivImg){
		ivDelete.setOnClickListener(mListener);
		ivDelete.setTag(position);
		if (file != null && file.exists()) {
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			try {
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				Bitmap bitmap = BitmapFactory.decodeStream(bis);
				ivImg.setImageBitmap(bitmap);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}finally{
				try {
				if(bis!=null){
						bis.close();
					}
				if(fis!=null){
					fis.close();
				}
				
				} catch (IOException e) {
						e.printStackTrace();
					}
				}
			ivDelete.setVisibility(View.VISIBLE);
		} 
		else
		{
			ivImg.setImageResource(R.drawable.icon_add_photo);
			ivDelete.setVisibility(View.GONE);
		}
		if(position >= 9){
			ivImg.setVisibility(View.GONE);
		}else{
			ivImg.setVisibility(View.VISIBLE);
		}
	}
	
	/**图片的url方式展示图片*/
	private void imgType(int position,ImageBean bean,final ImageView ivImg){
		
		mImageLoader.displayImage(bean.img_thum,ivImg, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete() {
				Animation anim = AnimationUtils.loadAnimation(mActivity, R.anim.fade_in);
				ivImg.setAnimation(anim);
				anim.start();
			}
		});
	}

	class Holder {
		ImageView imageImgV;
		ImageView deleteImgV;
	}
}
