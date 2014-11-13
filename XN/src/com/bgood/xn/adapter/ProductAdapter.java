package com.bgood.xn.adapter;
import java.util.List;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.ProductBean;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/**
 *     
 * @todo:我的产品适配器
 * @date:2014-11-13 下午5:45:53
 * @author:hg_liuzl@163.com
 */
public class ProductAdapter extends KBaseAdapter 
{
	private int mRightWidth = 0;
	public ProductAdapter(List<?> mList, Activity mActivity,OnClickListener listener,int width) {
		super(mList, mActivity, listener);
		this.mRightWidth = width;
	}


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{

		final ViewHolder holder;
		if (convertView == null) 
		{
			convertView = mInflater.inflate(R.layout.layout_product_edit_list_item, parent, false);
			holder = new ViewHolder();
			holder.infoRl = (RelativeLayout) convertView.findViewById(R.id.product_edit_list_item_rl_info);
			holder.deleteRl = (RelativeLayout) convertView.findViewById(R.id.product_edit_list_item_rl_delete);
			holder.iconImgV = (ImageView) convertView.findViewById(R.id.product_edit_list_item_imgv_icon);
            holder.nameTv = (TextView) convertView.findViewById(R.id.product_edit_list_item_tv_name);
            holder.priceTv = (TextView) convertView.findViewById(R.id.product_edit_list_item_tv_price);
            holder.deleteTv = (TextView) convertView.findViewById(R.id.product_edit_list_item_tv_delete);
			convertView.setTag(holder);
		} 
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}

		LinearLayout.LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		holder.infoRl.setLayoutParams(lp1);
		LinearLayout.LayoutParams lp2 = new LayoutParams(mRightWidth,LayoutParams.MATCH_PARENT);
		holder.deleteRl.setLayoutParams(lp2);
		
		final ProductBean productDTO = (ProductBean) mList.get(position);

		mImageLoader.displayImage(productDTO.img_thum,holder.iconImgV, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete() {
				Animation anim = AnimationUtils.loadAnimation(mActivity, R.anim.fade_in);
				holder.iconImgV.setAnimation(anim);
				anim.start();
			}
		});
		holder.nameTv.setText(productDTO.product_name);
		holder.priceTv.setText(productDTO.price);
		holder.deleteTv.setOnClickListener(mListener);
		holder.deleteTv.setTag(productDTO);
		return convertView;
	}

}

final class ViewHolder 
{
	RelativeLayout infoRl;
	RelativeLayout deleteRl;
	ImageView iconImgV;           // 头像
    TextView nameTv;              // 名字
    TextView priceTv;             // 价格
	TextView deleteTv;            // 删除
}