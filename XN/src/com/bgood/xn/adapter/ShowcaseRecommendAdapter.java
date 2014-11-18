package com.bgood.xn.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.ProductBean;
import com.bgood.xn.utils.ToolUtils;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/**
 * 
 * @todo: 橱窗中产品推荐页面的适配器
 * @date:2014-10-22 上午9:37:14
 * @author:hg_liuzl@163.com
 */
public class ShowcaseRecommendAdapter extends KBaseAdapter
{
	public ShowcaseRecommendAdapter(List<?> mList, Activity mActivity) {
		super(mList, mActivity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		final ViewHolder holder;
		
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.layout_showcase_recommend_item, null);
			holder.timeTv = (TextView) convertView.findViewById(R.id.showcase_recommend_item_tv_time);
			holder.iconImgV = (ImageView) convertView.findViewById(R.id.showcase_recommend_item_imgv_icon);
			holder.nameTv = (TextView) convertView.findViewById(R.id.showcase_recommend_item_tv_name);
			holder.priceTv = (TextView) convertView.findViewById(R.id.showcase_recommend_item_tv_price);
			convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
		
		final ProductBean productDTO = (ProductBean) mList.get(position);
		holder.timeTv.setText(ToolUtils.getFormatDate(productDTO.date_time));
		
        mImageLoader.displayImage(productDTO.img_thum,holder.iconImgV, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete() {
				Animation anim = AnimationUtils.loadAnimation(mActivity, R.anim.fade_in);
				holder.iconImgV.setAnimation(anim);
				anim.start();
			}
		});
		
		holder.nameTv.setText(productDTO.product_name);
		holder.priceTv.setText(productDTO.getPrice());
		return convertView;
	}

	class ViewHolder
	{
		TextView timeTv;     // 时间
		ImageView iconImgV;  // 商品图片
		TextView nameTv;     // 商品名
		TextView priceTv;    // 商品价格
	}
}
