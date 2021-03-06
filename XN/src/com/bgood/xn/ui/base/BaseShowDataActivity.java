package com.bgood.xn.ui.base;

import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.bgood.xn.adapter.KBaseAdapter;
import com.bgood.xn.utils.ShareUtils;
import com.bgood.xn.utils.ToolUtils;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.xlistview.XListView;

/**
 * @todo:需要列表展示数据的activity 基类
 * @date:2014-12-5 上午10:56:12
 * @author:hg_liuzl@163.com
 */
public class BaseShowDataActivity extends BaseActivity {
	
	public ShareUtils share = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		share = new ShareUtils(mActivity);
	}
	
	/*******通用的数据刷新与设置数据方法***************************************************/
	public int m_start_page = 0;
	
	public boolean isRefreshAction = true;
	
	public String mRefreshTime = "";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setDataAdapter(XListView xListView,KBaseAdapter adapter,List<?> showList,List resultlist,boolean isRefresh)
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
    	 
    	 if(isRefresh){
    		 showList.clear();
    		 isRefresh = false;
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
