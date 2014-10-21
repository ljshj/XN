package com.bgood.xn.adapter;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build.VERSION;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bgood.xn.R;

public class WeiqiangPublishAdapter extends BaseAdapter {
	private Activity context;
	private List<String> imgList;
	private LayoutInflater inflater;

	public WeiqiangPublishAdapter(Activity context, List<String> strings) {
		this.context = context;
		this.imgList = strings;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return 9;
	}

	@Override
	public Object getItem(int position) {
		return imgList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.item_weiqiang_publish,parent, false);
			holder.imageImgV = (ImageView) convertView.findViewById(R.id.send_weiqiang_imgv_image);
			holder.deleteImgV = (ImageView) convertView.findViewById(R.id.send_weiqiang_imgv_delete);
			convertView.setTag(holder);
		} 
		else 
		{
			holder = (Holder) convertView.getTag();
		}
		
		holder.imageImgV.setVisibility(View.VISIBLE);
		
		if (position < imgList.size()) {
			String imageName = null;
			try {
				imageName = getFileName(imgList.get(position));
			} catch (Exception e) {
				e.printStackTrace();
			}
			Bitmap bitmap = loadImgThumbnail(context, imageName, MediaStore.Images.Thumbnails.MINI_KIND);
			holder.imageImgV.setImageBitmap(bitmap);
		} 
		else if (position == imgList.size())
		{
			holder.imageImgV.setImageResource(R.drawable.img_weiqiang_publish_add_indicator);
		}
		else 
		{
			holder.imageImgV.setVisibility(View.GONE);
			holder.deleteImgV.setVisibility(View.GONE);
		}
		return convertView;
	}

	class Holder {
		ImageView imageImgV;
		ImageView deleteImgV;
	}
	
	private Bitmap loadImgThumbnail(Activity  activity, String imgName, int kind) 
	{
		Bitmap bitmap = null;

		String[] proj = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME };

		Cursor cursor = activity.managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj, MediaStore.Images.Media.DISPLAY_NAME + "='" + imgName + "'",
				null, null);

		if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
			try {
				ContentResolver crThumb = activity.getContentResolver();
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 1;
				options.inPreferredConfig = Bitmap.Config.RGB_565;
				options.inPurgeable = true;
				options.inInputShareable = true;
				bitmap = MediaStore.Images.Thumbnails.getThumbnail(crThumb, cursor.getInt(0), kind, options);
			} finally {
				if (VERSION.SDK_INT < 14)
					cursor.close();
			}
		}
		return bitmap;
	}

	private String getFileName(String filePath) throws Exception {
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
	}

}
