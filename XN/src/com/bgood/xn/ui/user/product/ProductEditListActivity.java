//package com.bgood.xn.ui.user.product;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.UserManager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.LinearLayout.LayoutParams;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bgood.xn.R;
//import com.bgood.xn.network.BaseNetWork.ReturnCode;
//import com.bgood.xn.ui.BaseActivity;
//import com.bgood.xn.widget.SwipeListView;
//import com.squareup.picasso.Picasso;
//
///**
// * 产品编辑列表页面
// */
//public class ProductEditListActivity extends BaseActivity implements OnItemClickListener
//{
//    private Button m_backBtn = null;   // 返回按钮
//    private ProgressBar m_progressBar = null;  // 进度条
//    private SwipeListView m_listLv = null;  // 列表
//    
//    private SwipeAdapter m_adpater = null;
//    private List<ProductDTO> m_list = new ArrayList<ProductDTO>();
//    
//    ShowCaseMessageManager messageManager = ShowCaseMessageManager.getInstance();
//    
//    private int start = 0;
//    private int m_delPosition = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_product_edit_list);
//        
//        findView();
//        setListener();
//        laodData(UserManager.getInstance().m_user.userId);
////        data();
////        setAdapter(m_list);
//    }
//    
//    @Override
//	public void onResume()
//	{
//		super.onResume();
//		
//		if (messageManager != null)
//		{
//			// 消息注册
//			messageManager.registerObserver(this);
//		}
//	}
//
//	@Override
//	public void onPause()
//	{
//		super.onPause();
//		
//		if (messageManager != null)
//		{
//			// 消息注销
//			messageManager.unregisterObserver(this);
//		}
//	}
//
//    /**
//     * 控件初始化方法
//     */
//    private void findView()
//    {
//        m_backBtn = (Button) findViewById(R.id.product_edit_list_btn_back);
//        m_progressBar = (ProgressBar) findViewById(R.id.product_edit_list_progress);
//        m_listLv = (SwipeListView) findViewById(R.id.product_edit_list_lv_list);
//    }
//    
//    /**
//     * 控件事件监听方法
//     */
//    private void setListener()
//    {
//        m_backBtn.setOnClickListener(new OnClickListener()
//        {
//            
//            @Override
//            public void onClick(View v)
//            {
//                ProductEditListActivity.this.finish();
//            }
//        });
//        
//        m_listLv.setOnItemClickListener(this);
//    }
//    
//    /**
//     * 加载数据方法
//     * @param userId 用户Id
//     */
//    private void laodData(String userId)
//    {
//    	messageManager.getProductList(userId, start, start + 10);
//    }
//    
//    /**
//     * 设置数据显示方法
//     * @param list
//     */
//    private void setAdapter(List<ProductDTO> list)
//    {
//    	m_list = list;
//        m_adpater = new SwipeAdapter(ProductEditListActivity.this, list, m_listLv.getRightViewWidth());
//        m_listLv.setAdapter(m_adpater);
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
//    {
//    	ProductDTO productDTO = (ProductDTO)arg0.getAdapter().getItem(arg2);
//    	Intent intent = new Intent(ProductEditListActivity.this, ProductEditActivity.class);
//    	Bundle bundle = new Bundle();
//    	bundle.putSerializable("productDTO", productDTO);
//    	intent.putExtras(bundle);
//    	startActivity(intent);
//    }
//
//	@Override
//	public void getProductListCB(Reulst result_state, ArrayList<ProductDTO> list)
//	{
//		super.getProductListCB(result_state, list);
//		
//		if (result_state.resultCode == ReturnCode.RETURNCODE_OK)
//		{
//			m_progressBar.setVisibility(View.GONE);
//			m_listLv.setVisibility(View.VISIBLE);
//			if (list != null)
//			{
//				if (list.size() < 10)
//				{
//					
//				}
//				else
//				{
//				    start = start + 10;
//				}
//				setAdapter(list);
//			}
//		}
//	}
//    
//	private class SwipeAdapter extends BaseAdapter 
//	{
//		/**
//		 * 上下文对象
//		 */
//		private Context m_context = null;
//		private List<ProductDTO> m_list = null;
//
//		private int mRightWidth = 0;
//
//		/**
//		 * @param mainActivity
//		 */
//		public SwipeAdapter(Context context, List<ProductDTO> list, int rightWidth) 
//		{
//			this.m_context = context;
//			this.m_list = list;
//			mRightWidth = rightWidth;
//		}
//
//		@Override
//		public int getCount() 
//		{
//			return m_list.size();
//		}
//
//		@Override
//		public ProductDTO getItem(int position) {
//			return m_list.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(final int position, View convertView, ViewGroup parent) 
//		{
//
//			ViewHolder holder;
//			if (convertView == null) 
//			{
//				convertView = LayoutInflater.from(m_context).inflate(R.layout.layout_product_edit_list_item, parent, false);
//				holder = new ViewHolder();
//				holder.infoRl = (RelativeLayout) convertView.findViewById(R.id.product_edit_list_item_rl_info);
//				holder.deleteRl = (RelativeLayout) convertView.findViewById(R.id.product_edit_list_item_rl_delete);
//				holder.iconImgV = (ImageView) convertView.findViewById(R.id.product_edit_list_item_imgv_icon);
//	            holder.nameTv = (TextView) convertView.findViewById(R.id.product_edit_list_item_tv_name);
//	            holder.priceTv = (TextView) convertView.findViewById(R.id.product_edit_list_item_tv_price);
//	            holder.deleteTv = (TextView) convertView.findViewById(R.id.product_edit_list_item_tv_delete);
//	            
//				convertView.setTag(holder);
//			} 
//			else 
//			{
//				// 有直接获得ViewHolder
//				holder = (ViewHolder) convertView.getTag();
//			}
//
//			LinearLayout.LayoutParams lp1 = new LayoutParams(
//					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//			holder.infoRl.setLayoutParams(lp1);
//			LinearLayout.LayoutParams lp2 = new LayoutParams(mRightWidth,
//					LayoutParams.MATCH_PARENT);
//			holder.deleteRl.setLayoutParams(lp2);
//			ProductDTO productDTO = m_list.get(position);
//
//			if (productDTO != null && productDTO.productSmallIcon != null && !productDTO.productSmallIcon.equals(""))
//				Picasso.with(m_context).load(productDTO.productSmallIcon).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(holder.iconImgV);
//			holder.nameTv.setText(productDTO.productName);
//			holder.priceTv.setText(productDTO.productPrice);
//			
//			// 删除
//			holder.deleteTv.setOnClickListener(new OnClickListener() 
//			{
//				@Override
//				public void onClick(View v) 
//				{
//					m_listLv.hiddenRight(m_listLv.mPreItemView);
//					m_delPosition = position;
//					WindowUtil.getInstance().progressDialogShow(ProductEditListActivity.this, "删除中...");
//					messageManager.deleteProduct(m_list.get(position).productId);
//					/*if (myHandler != null) 
//					{
//						Message msg = myHandler.obtainMessage();
//						msg.arg1 = 0;
//						msg.arg2 = position;
//						myHandler.sendMessageDelayed(msg, 600);
//					}*/
//				}
//			});
//			return convertView;
//		}
//
//	}
//
//	/*Handler myHandler = new Handler() {
//		public void handleMessage(Message msg) {
//			m_list.remove(msg.arg2);
//			m_adpater.notifyDataSetChanged();
//		}
//	};*/
//
//	static class ViewHolder 
//	{
//		RelativeLayout infoRl;
//		RelativeLayout deleteRl;
//
//		ImageView iconImgV;           // 头像
//        TextView nameTv;              // 名字
//        TextView priceTv;             // 价格
//
//		TextView deleteTv;            // 删除
//	}
//
//	@Override
//	public void deleteProductCB(Reulst result_state)
//	{
//		super.deleteProductCB(result_state);
//		WindowUtil.getInstance().DismissAllDialog();
//		if (result_state.resultCode == ReturnCode.RETURNCODE_OK)
//		{
//			Toast.makeText(this, "删除成功！", Toast.LENGTH_LONG).show();
//			m_list.remove(m_delPosition);
//			m_adpater.notifyDataSetChanged();
//		}
//		else
//		{
//			Toast.makeText(this, "删除失败！", Toast.LENGTH_LONG).show();
//		}
//	}
//	
//	
//}
