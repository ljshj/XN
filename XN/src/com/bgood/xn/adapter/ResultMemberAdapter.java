package com.bgood.xn.adapter;

import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.UserBean;
import com.squareup.picasso.Picasso;


/**
 * 
 * @todo:会员结果类
 * @date:2014-10-21 下午3:40:58
 * @author:hg_liuzl@163.com
 */
public class ResultMemberAdapter extends KBaseAdapter
{

   
    public ResultMemberAdapter(List<?> mList, Activity mActivity) {
		super(mList, mActivity);
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        
        ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_search_result_member_item, null);
            holder.iconImgV = (ImageView) convertView.findViewById(R.id.result_member_item_imgv_icon);
            holder.nameTv = (TextView) convertView.findViewById(R.id.result_member_item_tv_name);
            holder.sexImgV = (ImageView) convertView.findViewById(R.id.result_member_item_imgv_sex);
            holder.identityImgV = (ImageView) convertView.findViewById(R.id.result_member_item_imgv_identity);
            holder.distanceTv = (TextView) convertView.findViewById(R.id.result_member_item_tv_distance);
            holder.signatureTv = (TextView) convertView.findViewById(R.id.result_member_item_tv_signature);
            holder.icanTv = (TextView) convertView.findViewById(R.id.result_member_item_tv_ican);
            holder.ineedTv = (TextView) convertView.findViewById(R.id.result_member_item_tv_ineed);
            
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        
        UserBean userDTO = (UserBean) mList.get(position);
        
        if (!TextUtils.isEmpty(userDTO.userIcon)){
            Picasso.with(mActivity).load(userDTO.userIcon).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(holder.iconImgV);
        }

        holder.nameTv.setText(userDTO.nickn);
        
        if (userDTO.sex == 1)
        {
            holder.sexImgV.setImageResource(R.drawable.img_common_sex_male);
        }
        else
        {
            holder.sexImgV.setImageResource(R.drawable.img_common_sex_female);
        }
        
        if (userDTO.level.equals("1"))
        {
            holder.identityImgV.setImageResource(R.drawable.img_common_sex_male);
        }
        else
        {
            holder.identityImgV.setImageResource(R.drawable.img_common_sex_female);
        }
        holder.distanceTv.setText(userDTO.distance);
        holder.signatureTv.setText(userDTO.signature);
        holder.icanTv.setText(userDTO.ican);
        holder.ineedTv.setText(userDTO.ineed);
        
        return convertView;
    }

    class ViewHolder
    {
        ImageView iconImgV;       // 头像
        TextView nameTv;          // 姓名
        ImageView sexImgV;        // 性别
        ImageView identityImgV;   // 特征
        TextView distanceTv;      // 距离
        TextView signatureTv;     // 个性签名
        TextView icanTv;          // 我能
        TextView ineedTv;         // 我需
    }
}
