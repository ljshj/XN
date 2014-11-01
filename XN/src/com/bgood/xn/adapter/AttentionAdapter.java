package com.bgood.xn.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.AttentionBean;
import com.bgood.xn.bean.UserInfoBean;
import com.squareup.picasso.Picasso;

/**
 * 我的关注适配器
 */
public class AttentionAdapter extends KBaseAdapter 
{
	private int mType = 0;
    public AttentionAdapter(List<?> mList, Activity mActivity,int type) {
		super(mList, mActivity);
		this.mType = type;
	}

	@Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.user_layout_attention_item, null);
            holder.userIconImgV = (ImageView) convertView.findViewById(R.id.iv_user);
            holder.userNameTv = (TextView) convertView.findViewById(R.id.tv_username);
            holder.userSexIV = (ImageView) convertView.findViewById(R.id.iv_sex);
            holder.userIdentitytV = (TextView) convertView.findViewById(R.id.tv_identity);
            holder.userSignName = (TextView) convertView.findViewById(R.id.tv_user_signname);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        
        final AttentionBean mAttentionBean = (AttentionBean) mList.get(position);
        if (!TextUtils.isEmpty(mAttentionBean.img)){
            Picasso.with(mActivity).load(mAttentionBean.img).placeholder(R.drawable.default_icon).error(R.drawable.ic_launcher).into(holder.userIconImgV);
        }
        holder.userNameTv.setText(mAttentionBean.name);
        
        holder.userSignName.setText(mAttentionBean.signatrue);
        
        holder.userSexIV.setImageDrawable(mActivity.getResources().getDrawable(mAttentionBean.sex == 1? R.drawable.img_common_sex_male:R.drawable.img_common_sex_female));

        holder.userIdentitytV.setText(String.valueOf(mAttentionBean.level));

        holder.userIdentitytV.setVisibility(mAttentionBean.level<1 ? View.GONE:View.VISIBLE);
        
        return convertView;
    }
    
    private class ViewHolder
    {
        ImageView userIconImgV;        // 用户头像
        TextView userNameTv;           // 用户名
        ImageView userSexIV;        // 用户头像
        TextView userIdentitytV;    // 用户特征
        TextView userSignName;         // 用户签名
        Button btnAttentionType;       // 关注
    }
}
