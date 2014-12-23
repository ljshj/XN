package com.bgood.xn.ui.user.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.bgood.xn.R;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.view.CBaseSlidingMenu;
import com.bgood.xn.view.slidingmenu.lib.SlidingMenu;

/**
 * 侧边栏右边页面
 */
public class ShowcaseRightFragment extends Fragment implements OnClickListener
{

	private View m_rightLayout = null;
	private ShowcaseActivity m_showcaseActivity = null;
	private SlidingMenu m_slidingMenu = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		m_rightLayout = inflater.inflate(R.layout.layout_right_framnent_menu, null);
		m_showcaseActivity = (ShowcaseActivity) ShowcaseRightFragment.this.getActivity();
		m_slidingMenu = ((CBaseSlidingMenu) ShowcaseRightFragment.this.getActivity()).m_slidingMenu;
		m_rightLayout.findViewById(R.id.tv_add_product).setOnClickListener(this);
		m_rightLayout.findViewById(R.id.tv_list_product).setOnClickListener(this);
		m_rightLayout.findViewById(R.id.tv_individuation).setOnClickListener(this);
		return m_rightLayout;
	}

	@Override
	public void onClick(View v)
	{
		Intent intent = null;
		switch (v.getId())
		{
		case R.id.tv_add_product:
			intent = new Intent(m_showcaseActivity, ProductAddActivity.class);
			startActivity(intent);
			if (!m_slidingMenu.isMenuShowing())
			{
				m_slidingMenu.showMenu();
			} else
			{
				m_slidingMenu.showContent();
			}
			break;
		case R.id.tv_list_product:
			intent = new Intent(m_showcaseActivity, ProductEditListActivity.class);
			intent.putExtra("userid", String.valueOf(BGApp.mUserId));
			startActivity(intent);
			if (!m_slidingMenu.isMenuShowing())
			{
				m_slidingMenu.showMenu();
			} else
			{
				m_slidingMenu.showContent();
			}
			break;
		case R.id.tv_individuation:
			intent = new Intent(m_showcaseActivity, IndividuationActivity.class);
			startActivity(intent);
			if (!m_slidingMenu.isMenuShowing())
			{
				m_slidingMenu.showMenu();
			} else
			{
				m_slidingMenu.showContent();
			}
			break;
		default:
			break;
		}
	}
}
