package com.bgood.xn.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.UserManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.bean.UserBean;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.squareup.picasso.Picasso;

/**
 * 我的关注适配器
 */
public class FollowAdapter extends KBaseAdapter 
{
    public FollowAdapter(List<?> mList, Activity mActivity) {
		super(mList, mActivity);
	}

	@Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_follow_item, null);
            holder.userIconImgV = (ImageView) convertView.findViewById(R.id.follow_item_imgv_user_icon);
            holder.userNameTv = (TextView) convertView.findViewById(R.id.follow_item_tv_user_name);
            holder.userSexImgV = (ImageView) convertView.findViewById(R.id.follow_item_imgv_sex);
            holder.userIdentityImgV = (ImageView) convertView.findViewById(R.id.follow_item_imgv_identity);
            holder.userInfoTv = (TextView) convertView.findViewById(R.id.follow_item_tv_user_info);
            holder.followConfirmBtn = (Button) convertView.findViewById(R.id.follow_item_btn_confirm);
            holder.followCancelBtn = (Button) convertView.findViewById(R.id.follow_item_btn_cancel);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        
        final UserBean userDTO = (UserBean) mList.get(position);
        if (userDTO != null &&!TextUtils.isEmpty(userDTO.userIcon)){
            Picasso.with(mActivity).load(userDTO.userIcon).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(holder.userIconImgV);
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
        
        if (userDTO.follow == 0)
        {
            holder.followConfirmBtn.setVisibility(View.INVISIBLE);
            holder.followCancelBtn.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.followConfirmBtn.setVisibility(View.VISIBLE);
            holder.followCancelBtn.setVisibility(View.INVISIBLE);
        }
//        
//        // 关注
//        holder.followConfirmBtn.setOnClickListener(new OnClickListener()
//        {
//            
//            @Override
//            public void onClick(View v)
//            {
//                WindowUtil.getInstance().progressDialogShow(m_context, "关注中...");
//                messageManager.registerObserver(FollowAdapter.this);
//                messageManager.follow(userDTO.userId, UserManager.getInstance().m_user.userId, 0);
//                index = 0;
//                m_position = position;
//            }
//        });
//        
//        // 取消关注
//        holder.followCancelBtn.setOnClickListener(new OnClickListener()
//        {
//            
//            @Override
//            public void onClick(View v)
//            {
//                WindowUtil.getInstance().progressDialogShow(m_context, "取消关注中...");
//                messageManager.registerObserver(FollowAdapter.this);
//                messageManager.follow(userDTO.userId, UserManager.getInstance().m_user.userId, 1);
//                index = 1;
//                m_position = position;
//            }
//        });
        
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
