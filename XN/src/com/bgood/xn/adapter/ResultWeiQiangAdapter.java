package com.bgood.xn.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.WeiQiangBean;
import com.nostra13.universalimageloader.core.ImageLoader;


public class ResultWeiQiangAdapter extends KBaseAdapter
{

    

    public ResultWeiQiangAdapter(List<?> mList, Activity mActivity) {
		super(mList, mActivity);
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        
        final ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_search_result_weiqiang_item, null);
            holder.iconImgV = (ImageView) convertView.findViewById(R.id.result_weiqiang_item_imgv_icon);
            holder.infoTv = (TextView) convertView.findViewById(R.id.result_weiqiang_item_tv_info);
            holder.sendUserTv = (TextView) convertView.findViewById(R.id.result_weiqiang_item_tv_send_user);
            
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        
       final WeiQiangBean weiqiangDTO = (WeiQiangBean) mList.get(position);
       
       ImageLoader.getInstance().displayImage(weiqiangDTO.photo,holder.iconImgV);
        
        holder.infoTv.setText(weiqiangDTO.content);
        holder.sendUserTv.setText(weiqiangDTO.name);
        
        return convertView;
    }

    class ViewHolder
    {
        ImageView iconImgV;       // 头像
        TextView infoTv;          // 详细信息
        TextView sendUserTv;      // 发送用户
    }
}
