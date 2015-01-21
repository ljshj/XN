package com.bgood.xn.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bgood.xn.R;
import com.bgood.xn.bean.ImageBean;
import com.bgood.xn.system.BGApp;
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
			Bitmap bitmap = (Bitmap) object;
			bitmapType(position,bitmap, holder.deleteImgV, holder.imageImgV);
		}
		return convertView;
	}
	
	/**文件方式展示图片*/
	private void bitmapType(int position,Bitmap bitmap,ImageView ivDelete,ImageView ivImg){
		ivDelete.setOnClickListener(mListener);
		ivDelete.setTag(position);
		if (null!=bitmap) {
			ivImg.setImageBitmap(bitmap);
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
		BGApp.getInstance().setImage(bean.img_thum, ivImg);
	}

	class Holder {
		ImageView imageImgV;
		ImageView deleteImgV;
	}
}
