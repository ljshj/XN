package com.bgood.xn.ui.message.fragment;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.FriendBean;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.system.Const;
import com.bgood.xn.ui.base.BaseFragment;
import com.bgood.xn.ui.message.MessageActivity;
import com.bgood.xn.view.BToast;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.activity.ChatActivity;
import com.easemob.chat.adapter.ChatAllHistoryAdapter;
import com.easemob.chat.db.InviteMessgeDao;

/**
 * 
 * @todo:聊天记录界面
 * @date:2014-12-12 下午2:45:19
 * @author:hg_liuzl@163.com
 */
public class ChatHistoryFragment extends BaseFragment {

	private InputMethodManager inputMethodManager;
	private ListView listView;
	private ChatAllHistoryAdapter adapter;
	public RelativeLayout errorItem;
	public TextView errorText;
	private boolean hidden;
	private List<EMGroup> groups;
	private List<EMConversation> conversationList;
	public static ChatHistoryFragment instance;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		instance = this;
		return inflater.inflate(R.layout.fragment_conversation_history, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
		inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		errorItem = (RelativeLayout) getView().findViewById(R.id.rl_error_item);
		errorText = (TextView) errorItem.findViewById(R.id.tv_connect_errormsg);
		
		conversationList = loadConversationsWithRecentChat();
		listView = (ListView) getView().findViewById(R.id.list);
		adapter = new ChatAllHistoryAdapter(getActivity(), 1, conversationList);
		// 设置adapter
		listView.setAdapter(adapter);
				
		groups = EMGroupManager.getInstance().getAllGroups();

		
//		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//	            @Override
//	            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//	            	new AlertDialog.Builder(getActivity())
//	            	.setTitle("删除联系人")
//	            	.setPositiveButton("确定", null)
//	            	.setNegativeButton("取消", null)
//	            	.show();
//	                
//	                
//	                return true;
//	            }
//	        });

		
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				EMConversation conversation = adapter.getItem(position);
				String username = conversation.getUserName();
				if (username.equals(BGApp.getInstance().getUserName())){
					BToast.show(mActivity, "不能和自己聊天");
				}else {
					// 进入聊天页面
					Intent intent = new Intent(getActivity(), ChatActivity.class);
					EMContact emContact = null;
					groups = EMGroupManager.getInstance().getAllGroups();
					for (EMGroup group : groups) {
						if (group.getGroupId().equals(username)) {
							emContact = group;
							break;
						}
					}
					if (emContact != null && emContact instanceof EMGroup) {
						
						EMGroup emGroup = (EMGroup) emContact;
						// it is group chat
						intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
						intent.putExtra(Const.CHAT_HXGROUPID, emGroup.getGroupId()); //这里的username 表示
					} else {
						// it is single chat
						intent.putExtra("userId", username.substring(2)); //去掉bg 就是这个用户的userId
					}
					startActivity(intent);
				}
			}
		});
//		// 注册上下文菜单
//		registerForContextMenu(listView);
		
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
            	final EMConversation tobeDeleteCons = (EMConversation) adapter.getAdapter().getItem(position);
            	final AlertDialog dialog = new AlertDialog.Builder(getActivity())
            	.setTitle("删除消息")
            	.setCancelable(true)
            	.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						deleteConve(tobeDeleteCons);
					}
				})
            	.show();
                return true;
            }
        });
		
		
		
		

		listView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 隐藏软键盘
				hideSoftKeyboard();
				return false;
			}

		});
	}

	void hideSoftKeyboard() {
		if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getActivity().getCurrentFocus() != null)
				inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
			getActivity().getMenuInflater().inflate(R.menu.delete_message, menu);
	}
	
	
	private void deleteConve(EMConversation tobeDeleteCons) {
		// 删除此会话
		EMChatManager.getInstance().deleteConversation(tobeDeleteCons.getUserName(), tobeDeleteCons.isGroup());
		InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
		inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
		adapter.remove(tobeDeleteCons);
		adapter.notifyDataSetChanged();

//		// 更新消息未读数
		((MessageActivity) getActivity()).updateUnreadLabel();

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.delete_message) {
			return true;
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * 刷新页面
	 */
	public void refresh() {
		conversationList.clear();
		conversationList.addAll(loadConversationsWithRecentChat());
		adapter.notifyDataSetChanged();
	}

	/**
	 * 获取所有会话
	 * 
	 * @param context
	 * @return
	 */
	private List<EMConversation> loadConversationsWithRecentChat() {
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
		List<EMConversation> list = new ArrayList<EMConversation>();
		// 过滤掉messages seize为0的conversation
		for (EMConversation conversation : conversations.values()) {
			if (conversation.getAllMessages().size() != 0)
				list.add(conversation);
		}
		// 排序
		sortConversationByLastChatTime(list);
		return list;
	}

	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortConversationByLastChatTime(List<EMConversation> conversationList) {
		Collections.sort(conversationList, new Comparator<EMConversation>() {
			@Override
			public int compare(final EMConversation con1, final EMConversation con2) {

				EMMessage con2LastMessage = con2.getLastMessage();
				EMMessage con1LastMessage = con1.getLastMessage();
				if (con2LastMessage.getMsgTime() == con1LastMessage.getMsgTime()) {
					return 0;
				} else if (con2LastMessage.getMsgTime() > con1LastMessage.getMsgTime()) {
					return 1;
				} else {
					return -1;
				}
			}

		});
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		if (!hidden) {
			refresh();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!hidden && ! ((MessageActivity)getActivity()).isConflict) {
			refresh();
		}
	}

	@Override
    public void onSaveInstanceState(Bundle outState) {
        if(((MessageActivity)getActivity()).isConflict)
            outState.putBoolean("isConflict", true);
        super.onSaveInstanceState(outState);
        
    }

}
