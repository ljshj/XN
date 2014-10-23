//package com.bgood.xn.ui.user.product;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.RelativeLayout;
//
//import com.zhuozhong.bandgood.R;
//import com.zhuozhong.view.CBaseSlidingMenu;
//import com.zhuozhong.view.slidingmenu.lib.SlidingMenu;
//
///**
// * 侧边栏右边页面
// */
//public class ShowcaseRightFragment extends Fragment implements OnClickListener
//{
//
//	private View m_rightLayout = null;
//	private RelativeLayout m_addProductRl = null; // 添加产品
//	private RelativeLayout m_editProductRl = null; // 编辑产品
//	private RelativeLayout m_individuationRl = null; // 个性化模块
//
//	private ShowcaseActivity m_showcaseActivity = null;
//	private SlidingMenu m_slidingMenu = null;
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
//	{
//		m_rightLayout = inflater.inflate(R.layout.layout_right_framnent_menu, null);
//		m_showcaseActivity = (ShowcaseActivity) ShowcaseRightFragment.this.getActivity();
//		m_slidingMenu = ((CBaseSlidingMenu) ShowcaseRightFragment.this.getActivity()).m_slidingMenu;
//
//		findView();
//		setListener();
//
//		return m_rightLayout;
//	}
//
//	/**
//	 * 控件初始化方法
//	 */
//	private void findView()
//	{
//		m_addProductRl = (RelativeLayout) m_rightLayout.findViewById(R.id.showcase_right_rl_add_product);
//		m_editProductRl = (RelativeLayout) m_rightLayout.findViewById(R.id.showcase_right_rl_edit_product);
//		m_individuationRl = (RelativeLayout) m_rightLayout.findViewById(R.id.showcase_right_rl_individuation);
//	}
//
//	private void setListener()
//	{
//		m_addProductRl.setOnClickListener(this);
//		m_editProductRl.setOnClickListener(this);
//		m_individuationRl.setOnClickListener(this);
//	}
//
//	@Override
//	public void onClick(View v)
//	{
//		switch (v.getId())
//		{
//		case R.id.showcase_right_rl_add_product:
//		{
//			Intent intent = new Intent(m_showcaseActivity, AddProductActivity.class);
//			startActivity(intent);
//			if (!m_slidingMenu.isMenuShowing())
//			{
//				m_slidingMenu.showMenu();
//			} else
//			{
//				m_slidingMenu.showContent();
//			}
//			break;
//		}
//		case R.id.showcase_right_rl_edit_product:
//		{
//			Intent intent = new Intent(m_showcaseActivity, ProductEditListActivity.class);
//			startActivity(intent);
//			if (!m_slidingMenu.isMenuShowing())
//			{
//				m_slidingMenu.showMenu();
//			} else
//			{
//				m_slidingMenu.showContent();
//			}
//			break;
//		}
//		case R.id.showcase_right_rl_individuation:
//		{
//			Intent intent = new Intent(m_showcaseActivity, IndividuationActivity.class);
//			startActivity(intent);
//			if (!m_slidingMenu.isMenuShowing())
//			{
//				m_slidingMenu.showMenu();
//			} else
//			{
//				m_slidingMenu.showContent();
//			}
//			break;
//		}
//		default:
//			break;
//		}
//	}
//}
