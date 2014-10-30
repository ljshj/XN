package com.bgood.xn.adapter;

import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.UserInfoBean;
import com.squareup.picasso.Picasso;

/**
 * 
 * @todo:我的关注适配器
 * @date:2014-10-22 上午9:39:29
 * @author:hg_liuzl@163.com
 */

public class FansAdapter extends KBaseAdapter 
{
   

    public FansAdapter(List<?> mList, Activity mActivity) {
		super(mList, mActivity);
	}

	@Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_vermicelli_item, null);
            holder.userIconImgV = (ImageView) convertView.findViewById(R.id.vermicelli_item_imgv_user_icon);
            holder.userNameTv = (TextView) convertView.findViewById(R.id.vermicelli_item_tv_user_name);
            holder.userSexImgV = (ImageView) convertView.findViewById(R.id.vermicelli_item_imgv_sex);
            holder.userIdentityImgV = (ImageView) convertView.findViewById(R.id.vermicelli_item_imgv_identity);
            holder.userInfoTv = (TextView) convertView.findViewById(R.id.vermicelli_item_tv_user_info);
            holder.followConfirmBtn = (Button) convertView.findViewById(R.id.vermicelli_item_btn_confirm);
            holder.followCancelBtn = (Button) convertView.findViewById(R.id.vermicelli_item_btn_cancel);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        
        final UserInfoBean userDTO = (UserInfoBean) mList.get(position);
        if (userDTO != null &&TextUtils.isEmpty(userDTO.photo)){
            Picasso.with(mActivity).load(userDTO.photo).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(holder.userIconImgV);
        }
        holder.userNameTv.setText(userDTO.nickn);
        int sex = userDTO.sex;
        if (sex == 1)
        {
            holder.userSexImgV.setImageResource(R.drawable.img_common_sex_male);
        }
        else if (sex == 2)
        {
            holder.userSexImgV.setImageResource(R.drawable.img_common_sex_female);
        }
        else
        {
            holder.userSexImgV.setImageResource(0);
        }
        
        holder.userInfoTv.setText(userDTO.signature);
        
//        if (userDTO.guanzhuType == 1)
//        {
//            holder.followConfirmBtn.setVisibility(View.INVISIBLE);
//            holder.followCancelBtn.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            holder.followConfirmBtn.setVisibility(View.VISIBLE);
//            holder.followCancelBtn.setVisibility(View.INVISIBLE);
//        }
        return convertView;
    }
    
    private class ViewHolder
    {
        ImageView userIconImgV;        // 用户头像
        TextView userNameTv;           // 用户名
        ImageView userSexImgV;         // 用户性别
        ImageView userIdentityImgV;    // 用户特征
        TextView userInfoTv;           // 用户信息
        Button followConfirmBtn;       // 关注
        Button followCancelBtn;        // 已关注
    }
}
