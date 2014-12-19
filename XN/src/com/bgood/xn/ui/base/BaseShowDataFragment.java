package com.bgood.xn.ui.base;

import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.bgood.xn.adapter.KBaseAdapter;
import com.bgood.xn.utils.ToolUtils;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.xlistview.XListView;

/**
 * @todo:刷新数据的fragment
 * @date:2014-12-19 上午10:53:22
 * @author:hg_liuzl@163.com
 */
public class BaseShowDataFragment extends BaseFragment {
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	/*******通用的数据刷新与设置数据方法***************************************************/
	public int m_start_page = 0;
	public boolean isRefreshAction = true;
	public String mRefreshTime = "";
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void setDataAdapter(XListView xListView,KBaseAdapter adapter,List<?> showList,List resultlist)
    {
    	mRefreshTime = ToolUtils.getNowTime();
    	xListView.setRefreshTime(mRefreshTime);
    	stopLoad(xListView);
    	
    	if(null == resultlist || resultlist.size() ==0)
    	{
    		xListView.setPullLoadEnable(false);
            BToast.show(mActivity, "数据加载完毕");
    		return;
    	}
    	
    	 if (resultlist.size() < PAGE_SIZE_ADD)
         {
    		 xListView.setPullLoadEnable(false);
             BToast.show(mActivity, "数据加载完毕");
         }else
         {
        	 xListView.setPullLoadEnable(true);
         }
    	 
    	 if(isRefreshAction){
    		 showList.clear();
    		 isRefreshAction = false;
    	 }
    	 
    	 showList.addAll(resultlist);
         adapter.notifyDataSetChanged();
    }
    /**
     * 加载完成之后进行时间保存等方法
     */
    @SuppressLint("SimpleDateFormat")
	private void stopLoad(XListView xListView) {
        xListView.stopRefresh();
        xListView.stopLoadMore();
    }
}
