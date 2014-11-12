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
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bgood.xn.R;
/***
 * @todo:图片适配器
 * @date:2014-11-10 下午6:00:55
 * @author:hg_liuzl@163.com
 */
public class ImageAdapter extends KBaseAdapter {
	
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
		
		if(mList.size() == 0){
			holder.imageImgV.setImageResource(R.drawable.icon_add_photo);
		}
		
		if (position < mList.size()) {
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			try {
				fis = new FileInputStream((File)mList.get(position));
				bis = new BufferedInputStream(fis);
				Bitmap bitmap = BitmapFactory.decodeStream(bis);
				holder.imageImgV.setImageBitmap(bitmap);
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
		} 
		else
		{
			holder.imageImgV.setImageResource(R.drawable.icon_add_photo);
		}
		return convertView;
	}

	class Holder {
		ImageView imageImgV;
		ImageView deleteImgV;
	}
}
