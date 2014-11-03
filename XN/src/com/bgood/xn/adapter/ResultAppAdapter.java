package com.bgood.xn.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.AppBean;
import com.squareup.picasso.Picasso;
/**
 * 本地应用程序适配器
 */
public class ResultAppAdapter extends KBaseAdapter
{
    public ResultAppAdapter(List<?> mList, Activity mActivity) {
		super(mList, mActivity);
	}

    @SuppressLint("InflateParams")
	@Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_search_result_app_item, null);
            holder.iconImgV = (ImageView) convertView.findViewById(R.id.result_app_item_imgv_icon);
            holder.nameTv = (TextView) convertView.findViewById(R.id.result_app_item_tv_name);
            holder.sizeTv = (TextView) convertView.findViewById(R.id.result_app_item_tv_size);
            holder.infoTv = (TextView) convertView.findViewById(R.id.result_app_item_tv_info);
            holder.openBtn = (Button) convertView.findViewById(R.id.result_app_item_btn_open);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        
        AppBean appDTO = (AppBean) mList.get(position);
        
        if (!TextUtils.isEmpty(appDTO.img)){
            Picasso.with(mActivity).load(appDTO.img_thum).placeholder(R.drawable.icon_default).error(R.drawable.icon_default).into(holder.iconImgV);
        }
        holder.nameTv.setText(appDTO.title);
        holder.sizeTv.setText(appDTO.size);
        holder.infoTv.setText(appDTO.intro);
        
        return convertView;
    }

    class ViewHolder
    {
        ImageView iconImgV;   // 应用图标
        TextView nameTv;      // 应用名
        TextView sizeTv;      // 应用大小
        TextView infoTv;      // 应用信息
        Button openBtn;       // 打开软件
    }
}
