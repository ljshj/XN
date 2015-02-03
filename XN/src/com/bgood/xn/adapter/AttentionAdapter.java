package com.bgood.xn.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.AttentionBean;
import com.bgood.xn.system.BGApp;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 我的关注适配器
 */
public class AttentionAdapter extends KBaseAdapter 
{
	public AttentionAdapter(List<?> mList, Activity mActivity,
			OnClickListener listener) {
		super(mList, mActivity, listener);
	}

	@Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
       final ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.user_layout_attention_item, null);
            holder.userIconImgV = (ImageView) convertView.findViewById(R.id.iv_user);
            holder.userNameTv = (TextView) convertView.findViewById(R.id.tv_username);
            holder.userSexIV = (ImageView) convertView.findViewById(R.id.iv_sex);
            holder.userIdentitytV = (TextView) convertView.findViewById(R.id.tv_identity);
            holder.userSignName = (TextView) convertView.findViewById(R.id.tv_user_signname);
            holder.tvAttentionType = (TextView) convertView.findViewById(R.id.tv_attention_type);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        
        final AttentionBean mAttentionBean = (AttentionBean) mList.get(position);
        
        BGApp.getInstance().setImage(mAttentionBean.img, holder.userIconImgV);
        
        holder.userIconImgV.setOnClickListener(mListener);
		holder.userIconImgV.setTag(mAttentionBean);
		
		holder.userNameTv.setText(mAttentionBean.name);
		holder.userNameTv.setOnClickListener(mListener);
		holder.userNameTv.setTag(mAttentionBean);
        
        holder.userSignName.setText(mAttentionBean.signatrue);
        
        holder.userSexIV.setImageDrawable(mActivity.getResources().getDrawable(mAttentionBean.sex == 1? R.drawable.img_common_sex_male:R.drawable.img_common_sex_female));

        holder.userIdentitytV.setText(mActivity.getResources().getString(R.string.account_vip,mAttentionBean.level));

        holder.userIdentitytV.setVisibility(mAttentionBean.level<1 ? View.GONE:View.VISIBLE);
        
        if(mAttentionBean.searchtype == AttentionBean.ATTENTION){
        	holder.tvAttentionType.setText("取消关注");
        	holder.tvAttentionType.setBackgroundResource(R.drawable.bg_attention_yes);
        	holder.tvAttentionType.setCompoundDrawablesWithIntrinsicBounds( mActivity.getResources().getDrawable(R.drawable.icon_attention_ok),null, null, null);

        }else{
        	holder.tvAttentionType.setText(mAttentionBean.guanzhutype == 0 ?"关注":"相互关注");
        	holder.tvAttentionType.setBackgroundResource(mAttentionBean.guanzhutype == 0 ?R.drawable.bg_attention_no:R.drawable.bg_attention_yes);
        	holder.tvAttentionType.setCompoundDrawablesWithIntrinsicBounds( mActivity.getResources().getDrawable(mAttentionBean.guanzhutype == 0 ?R.drawable.icon_attention_add:R.drawable.icon_attention_each), null, null,null);
        }
        
        holder.tvAttentionType.setOnClickListener(mListener);
        
        holder.tvAttentionType.setTag(mAttentionBean);
        
        return convertView;
    }
    
    private class ViewHolder
    {
        ImageView userIconImgV;        // 用户头像
        TextView userNameTv;           // 用户名
        ImageView userSexIV;        // 用户头像
        TextView userIdentitytV;    // 用户特征
        TextView userSignName;         // 用户签名
        TextView tvAttentionType;       // 关注
    }
}
