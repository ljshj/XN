package com.bgood.xn.adapter;

import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.FeedbackBean;
import com.bgood.xn.utils.ToolUtils;

/**
 * @todo:意见反馈适配器
 * @date:2014-11-6 下午2:12:47
 * @author:hg_liuzl@163.com
 */
public class FeedbackAdapter extends KBaseAdapter {

	public FeedbackAdapter(List<?> mList, Activity mActivity) {
		super(mList, mActivity);
	}
	
	ViewHolder viewHolder;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(null == convertView){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.user_item_feedback, null);
			viewHolder.tvFbTime = (TextView) convertView.findViewById(R.id.tv_feedback_time);
			viewHolder.tvFbContent = (TextView) convertView.findViewById(R.id.tv_feedback_content);
			viewHolder.tvResponser = (TextView) convertView.findViewById(R.id.tv_response_kefu);
			viewHolder.tvResponseTime = (TextView) convertView.findViewById(R.id.tv_response_time);
			viewHolder.tvResponseContent = (TextView) convertView.findViewById(R.id.tv_response_content);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		final FeedbackBean fb = (FeedbackBean) mList.get(position);
			viewHolder.tvFbTime.setText(ToolUtils.getFormatDate(fb.messagetime));
			viewHolder.tvFbContent.setText("提问:"+fb.message);
			viewHolder.tvResponseTime.setText(ToolUtils.getFormatDate(fb.backtime));
			viewHolder.tvResponseContent.setText("回复:"+fb.messageback);
			
			if(TextUtils.isEmpty(fb.messageback)){
				viewHolder.tvResponser.setVisibility(View.GONE);
				viewHolder.tvResponseTime.setVisibility(View.GONE);
				viewHolder.tvResponseContent.setVisibility(View.GONE);
			}else{
				viewHolder.tvResponser.setVisibility(View.VISIBLE);
				viewHolder.tvResponseTime.setVisibility(View.VISIBLE);
				viewHolder.tvResponseContent.setVisibility(View.VISIBLE);
				
			}
			
		return convertView;
	}
	
	
	final class ViewHolder{
		TextView tvFbTime,tvFbContent,tvResponser,tvResponseTime,tvResponseContent;
	}
}
