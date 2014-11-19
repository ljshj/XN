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
import com.bgood.xn.bean.ProductCommentBean;
import com.bgood.xn.utils.ToolUtils;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/**
 * 
 * @todo:产品评论适配器
 * @date:2014-11-18 下午5:22:49
 * @author:hg_liuzl@163.com
 */
public class ProductCommentAdapter extends KBaseAdapter
{
    public ProductCommentAdapter(List<?> mList, Activity mActivity) {
		super(mList, mActivity);
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
       final ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_product_comment_item, null);
            holder.iconImgV = (ImageView) convertView.findViewById(R.id.iv_img);
            holder.nameTv = (TextView) convertView.findViewById(R.id.tv_nick);
            holder.timeTv = (TextView) convertView.findViewById(R.id.tv_time);
            holder.contentTv = (TextView) convertView.findViewById(R.id.product_comment_item_tv_content);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        
        final ProductCommentBean pcb = (ProductCommentBean) mList.get(position);
        
        mImageLoader.displayImage(pcb.photo,holder.iconImgV, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete() {
				Animation anim = AnimationUtils.loadAnimation(mActivity, R.anim.fade_in);
				holder.iconImgV.setAnimation(anim);
				anim.start();
			}
		});
        
        holder.nameTv.setText(pcb.name);
        holder.timeTv.setText(ToolUtils.getFormatDate(pcb.commenttime));
        holder.contentTv.setText(pcb.content);
        return convertView;
    }

   final class ViewHolder
    {
        ImageView iconImgV;           // 头像
        TextView nameTv;              // 名字
        TextView timeTv;              // 发表时间
        TextView contentTv;           // 评论数
    }
}
