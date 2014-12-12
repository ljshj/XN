package com.bgood.xn.ui.message;import android.os.Bundle;import android.support.v4.app.Fragment;import android.support.v4.app.FragmentTransaction;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.Button;import android.widget.RadioGroup;import android.widget.RadioGroup.OnCheckedChangeListener;import com.bgood.xn.R;import com.bgood.xn.ui.BaseFragment;import com.bgood.xn.ui.message.fragment.ChatHistoryFragment;import com.bgood.xn.ui.message.fragment.CommunicateFragment;import com.bgood.xn.ui.message.fragment.FriendListFragment;import com.bgood.xn.ui.message.fragment.GroupFragment;/** * @todo:聊天主界面 * @author:hg_liuzl@163.com */public class MessageFragment extends BaseFragment implements OnCheckedChangeListener {		public static final int MESSAGE_MAIN = 0;	public static final int MESSAGE_FRIEND = 1;	public static final int MESSAGE_GROUP = 2;	public static final int MESSAGE_COMMUNICATION_HALL = 3;		private int messageCheckType = 0;	private RadioGroup radioGroup;	private Button btnAdd;			private CommunicateFragment commuFragment = null;	private FriendListFragment friendFragment = null;	private GroupFragment groupFragment = null;	private ChatHistoryFragment histroyFragment = null;	private Fragment[] fragments = null;	private int index;	// 当前fragment的index	private int currentTabIndex;		@Override	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {		layout = inflater.inflate(R.layout.layout_message_main, container, false);		initViews();		return layout;	}	/**	 * 初始化tabhost	 */	private void initViews()	{		commuFragment = new CommunicateFragment();		friendFragment = new FriendListFragment();		groupFragment = new GroupFragment();		histroyFragment = new ChatHistoryFragment();		fragments = new Fragment[]{commuFragment,friendFragment,groupFragment,histroyFragment};		// 添加显示第一个fragment		getActivity().getSupportFragmentManager().beginTransaction()		.add(R.id.fragment_container, histroyFragment)		.add(R.id.fragment_container, groupFragment)		.add(R.id.fragment_container, friendFragment)		.add(R.id.fragment_container, commuFragment)		.hide(commuFragment)		.hide(friendFragment)		.hide(groupFragment)		.show(histroyFragment)		.commit();				btnAdd = (Button) layout.findViewById(R.id.b_message_main_add);		radioGroup = (RadioGroup) layout.findViewById(R.id.rg_message_main_tab);		radioGroup.setOnCheckedChangeListener(this);	}	@Override	public void onCheckedChanged(RadioGroup group, int checkedId)	{		switch (checkedId)		{		case R.id.message_main_tab_messagecenter:			messageCheckType = MESSAGE_MAIN;			btnAdd.setVisibility(View.INVISIBLE);			index = 0;			break;		case R.id.message_main_tab_friend:			messageCheckType = MESSAGE_FRIEND;			btnAdd.setVisibility(View.VISIBLE);			index = 1;			break;		case R.id.message_main_tab_group:			messageCheckType = MESSAGE_GROUP;			btnAdd.setVisibility(View.VISIBLE);			index = 2;			break;		case R.id.message_main_tab_communication_hall:			btnAdd.setVisibility(View.INVISIBLE);			messageCheckType = MESSAGE_COMMUNICATION_HALL;			index = 3;			break;		}		if (currentTabIndex != index) {			FragmentTransaction trx = getActivity().getSupportFragmentManager().beginTransaction();			trx.hide(fragments[currentTabIndex]);			if (!fragments[index].isAdded()) {				trx.replace(R.id.fragment_container, fragments[index]);			}			trx.show(fragments[index]).commit();		}		currentTabIndex = index;	}}