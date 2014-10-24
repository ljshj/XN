package com.bgood.xn.adapter;

import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.CabinetResultBean;
import com.squareup.picasso.Picasso;

/**
 * 
 * @todo:橱窗适配器
 * @date:2014-10-21 下午3:37:51
 * @author:hg_liuzl@163.com
 */
public class ResultShowcaseAdapter extends KBaseAdapter
{


    public ResultShowcaseAdapter(List<?> mList, Activity mActivity) {
		super(mList, mActivity);
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        
        ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_search_result_showcase_item, null);
            holder.iconImgV = (ImageView) convertView.findViewById(R.id.result_showcase_item_imgv_icon);
            holder.nameTv = (TextView) convertView.findViewById(R.id.result_showcase_item_tv_name);
            holder.priceTv = (TextView) convertView.findViewById(R.id.result_showcase_item_tv_price);
            
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        
        final CabinetResultBean cabinetDTO = (CabinetResultBean) mList.get(position);
        
        if (!TextUtils.isEmpty(cabinetDTO.img_thum)){
            Picasso.with(mActivity).load(cabinetDTO.img_thum).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(holder.iconImgV);
        }
        
        holder.nameTv.setText(cabinetDTO.title);
        holder.priceTv.setText(cabinetDTO.price);
        
        return convertView;
    }

    class ViewHolder
    {
        ImageView iconImgV;       // 头像
        TextView nameTv;          // 姓名
        TextView priceTv;         // 价格信息
    }
}
