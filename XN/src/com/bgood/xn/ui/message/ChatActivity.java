//package com.bgood.xn.ui.message;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.net.ConnectivityManager;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnTouchListener;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.zhuozhong.bandgood.R;
//import com.zhuozhong.bandgood.SystemEnum;
//import com.zhuozhong.bandgood.activity.BaseActivity;
//import com.zhuozhong.bandgood.activity.BaseActivity.OnFaceClickListener;
//import com.zhuozhong.bandgood.adapter.ChatAdapter;
//import com.zhuozhong.bandgood.bean.ChatMessageDTO;
//import com.zhuozhong.bandgood.bean.ChatMessageDTO.CHAT_MESSAGE_DATA_TYPE;
//import com.zhuozhong.bandgood.bean.ChatMessageDTO.CHAT_MESSAGE_TYPE;
//import com.zhuozhong.bandgood.bean.ChatMessageDTO.CHAT_SEND_STATE;
//import com.zhuozhong.bandgood.bean.MessageCenterMessageDTO;
//import com.zhuozhong.bandgood.bean.UserDTO;
//import com.zhuozhong.bandgood.messagemanager.UserCenterMessageManager;
//import com.zhuozhong.manager.FDSUserCenterDataManager;
//import com.zhuozhong.manager.PathManager;
//import com.zhuozhong.manager.StateManager;
//import com.zhuozhong.manager.UserManager;
//import com.zhuozhong.util.DateManager;
//import com.zhuozhong.util.SoundMeter;
//import com.zhuozhong.util.Util;
//import com.zhuozhong.util.Util.onPhotoReceiverListener;
//import com.zhuozhong.util.Util.onReceiverListener;
//import com.zhuozhong.view.pulltorefresh.PullToRefreshBase;
//import com.zhuozhong.view.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
//import com.zhuozhong.view.pulltorefresh.PullToRefreshListView;
//import com.zhuozhong.zzframework.SystemRunning;
//import com.zhuozhong.zzframework.message.ZZThreadMessageType;
//import com.zhuozhong.zzframework.session.ZZSessionManager;
//
//public class ChatActivity extends BaseActivity implements OnItemClickListener, OnClickListener, OnRefreshListener2, OnFaceClickListener, TextWatcher, OnTouchListener
//{
//	/* 借用消息中心的消息，来进来聊天界面的参数传入 */
//	MessageCenterMessageDTO message = null;
//	ListView messagesList = null;/* 消息列表 */
//	PullToRefreshListView pullListViewContainer;
//	ChatAdapter chatRoomPageAdapter;
//	private List<ChatMessageDTO> sendingMessge = Collections.synchronizedList(new ArrayList<ChatMessageDTO>());
//	TextView chatRoomName = null;
//	public static final int CHAT_KEYBOARD_MODE_EDIT = 0;
//	public static final int CHAT_KEYBOARD_MODE_RECORD = 1;
//	private int chat_keyborad_mode = 0;
//	Button m_sendButton;/* 发送按钮 */
//	String friendID = null;
//	String title = null;
//	String userID = null;
//	// SystemEnum.FDS_Message_Type messageType;
//	boolean isGroupRoom = false;// 是否为群聊
//	SystemEnum.FDS_Role_Type m_roleType = SystemEnum.FDS_Role_Type.FDS_Role_Type_User;// 群聊类型
//	String m_groupID = null;// 群的ID
//	/* 记录下载最上面的聊天记录ID */
//	int recordID = -1;
//	int count = -1;// 记录table移动的位置
//	private long lastTimeDisplay;
//	private long newTimeDisplay;
//	List<ChatMessageDTO> chatMessages = new ArrayList<ChatMessageDTO>();
//	private Util fdsUtil;
//	private Button chat_b_text_indicator;
//	private Button chat_b_record_indicator;
//	private EditText chat_et_edit;
//	private Button chat_b_record;
//	private ImageButton chat_iv_send_more;
//	private Button chat_b_send;
//	private ViewGroup chat_ll_send_more;
//	private RelativeLayout chat_rl_face_layout;
//	private int voice_flag = 1;
//	private boolean voice_isShort;
//	private static final int POLL_INTERVAL = 300;
//	private SoundMeter mSensor;
//	private long startVoiceT;
//	private String voice_path;
//	Handler handler = new Handler()
//	{
//		public void handleMessage(Message msg)
//		{
//			switch (msg.what)
//			{
//			case ZZThreadMessageType.PullRefresh_chat_activtity:
//				pullListViewContainer.onRefreshComplete(false);
//				loadChatDatas();
//				break;
//			}
//		}
//	};
//	private LinearLayout chat_ll_cancel_voice;
//	private View chat_layout_voice_popup;
//	private LinearLayout chat_ll_voice_rcd_hint_rcding;
//	private LinearLayout chat_ll_voice_rcd_hint_loading;
//	private LinearLayout chat_ll_voice_rcd_hint_tooshort;
//	private ImageView chat_iv_voice_volume;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.layout_chat);
//		registerNetMonitor();
//		chatInit();
//		initViews();
//		setListeners();
//		initData();
//		StateManager.getInstance().updateMessageActivity();
//	}
//
//	@Override
//	protected void onDestroy()
//	{
//		this.unRegisterNetMonitor();
//		super.onDestroy();
//	}
//
//	@Override
//	protected void setListeners()
//	{
//		super.setListeners();
//		m_sendButton.setOnClickListener(this);
//		chat_b_text_indicator.setOnClickListener(this);
//		chat_b_record_indicator.setOnClickListener(this);
//		chat_b_record.setOnTouchListener(this);
//		chat_iv_send_more.setOnClickListener(this);
//		chat_b_send.setOnClickListener(this);
//		findViewById(R.id.chat_ll_send_img_indicator).setOnClickListener(this);
//		findViewById(R.id.chat_ll_send_file_indicator).setOnClickListener(this);
//		findViewById(R.id.chat_iv_send_more).setOnClickListener(this);
//		/* 下拉刷新 */
//		pullListViewContainer.setOnRefreshListener(this);
//	}
//
//	public void addChatMessage(List<ChatMessageDTO> temMessages)
//	{
//		for (ChatMessageDTO message : temMessages)
//		{
//			Long sendTime = Long.valueOf(message.m_sendTime);
//			chatMessages.add(0, message);
//			udpateOldTip(sendTime);
//		}
//	}
//
//	public void chatInit()
//	{
//		message = (MessageCenterMessageDTO) this.getIntent().getSerializableExtra("messageCenterMessage");
//		if (message.m_messageType == MessageCenterMessageDTO.FDSMessageCenterMessage_Type.FDSMessageCenterMessage_Type_CHAT_GROUP)
//		{
//			/* 群聊 */
//			isGroupRoom = true;
//			m_roleType = SystemEnum.FDS_Role_Type.values()[Integer.parseInt(message.m_param1)];
//			m_groupID = message.m_param2;
//			friendID = m_groupID;
//			title = message.m_senderName;
//		} else if (message.m_messageType == MessageCenterMessageDTO.FDSMessageCenterMessage_Type.FDSMessageCenterMessage_Type_CHAT_PERSON)
//		{
//			/* 私聊 */
//			isGroupRoom = false;
//			friendID = message.m_senderID;
//			title = message.m_senderName;
//		}
//		// TODO 取消了通知栏上的消息 正式版 需要
//		// ZZNotificationManager.getInstance().cancelNotification(
//		// Integer.valueOf(friendID));
//	}
//
//	public void loadChatDatas()
//	{
//		List<ChatMessageDTO> temMessages = FDSUserCenterDataManager.getInstance().getChatRecords(userID, this.m_roleType, friendID/* groupID */, recordID, 10);
//		count = temMessages.size();
//		if (temMessages != null && temMessages.size() > 0)
//		{
//			addChatMessage(temMessages);
//			recordID = this.chatMessages.get(0).m_messageID;
//		}
//		this.updateChatRoomViews();
//	}
//
//	private void udpateOldTip(long sendTime)
//	{
//		boolean isDistance3Min = false;
//		if (lastTimeDisplay == 0)
//		{
//			lastTimeDisplay = sendTime;
//			isDistance3Min = true;
//		} else
//			isDistance3Min = DateManager.getDistanceForThreadMin(lastTimeDisplay, sendTime);
//		System.out.println(sendTime + "  " + lastTimeDisplay);
//		System.out.println(isDistance3Min);
//		if (isDistance3Min)
//		{
//			ChatMessageDTO message2 = new ChatMessageDTO();
//			message2.m_messageType = CHAT_MESSAGE_TYPE.CHAT_MESSAGE_TYPE_TIP;
//			String time = DateManager.hmFormat.format(new Date(sendTime));
//			message2.m_content = time;
//			chatMessages.add(0, message2);
//			lastTimeDisplay = sendTime;
//			if (newTimeDisplay == 0)
//				newTimeDisplay = sendTime;
//		}
//	}
//
//	private void udpateNewTip(long sendTime)
//	{
//		boolean isDistance3Min = false;
//		isDistance3Min = DateManager.getDistanceForThreadMin(sendTime, newTimeDisplay);
//		if (isDistance3Min)
//		{
//			ChatMessageDTO message2 = new ChatMessageDTO();
//			message2.m_messageType = CHAT_MESSAGE_TYPE.CHAT_MESSAGE_TYPE_TIP;
//			String time = DateManager.hmFormat.format(new Date(sendTime));
//			message2.m_content = time;
//			chatMessages.add(message2);
//			newTimeDisplay = sendTime;
//		}
//	}
//
//	public void initData()
//	{
//		mSensor = new SoundMeter();
//		ZZSessionManager.getInstance().createKeepAliveConnect();
//		userID = UserManager.getInstance().getNowUser().userId;
//		FDSUserCenterDataManager.getInstance().setChatRecordsDoDeal(userID, friendID, this.m_roleType, this.m_groupID);
//		loadChatDatas();
//	}
//
//	@Override
//	protected void onPause()
//	{
//		if (fdsUtil != null)
//			fdsUtil.photoPopupOut();
//		super.onPause();
//		UserCenterMessageManager.getInstance().unregisterObserver(this);
//	}
//
//	@Override
//	protected void onResume()
//	{
//		super.onResume();
//		UserCenterMessageManager.getInstance().registerObserver(this);
//	}
//
//	@Override
//	public void initViews()
//	{
//		chat_ll_send_more = (ViewGroup) findViewById(R.id.chat_ll_send_more);
//		initKeybord();
//		View chat_message_icon = findViewById(R.id.chat_message_icon);
//		if (message.m_messageType == MessageCenterMessageDTO.FDSMessageCenterMessage_Type.FDSMessageCenterMessage_Type_CHAT_PERSON)
//		{
//			chat_message_icon.setVisibility(View.INVISIBLE);
//		} else
//		{
//			chat_message_icon.setVisibility(View.VISIBLE);
//			chat_message_icon.setOnClickListener(this);
//		}
//
//		// if (message.m_messageType ==
//		// MessageCenterMessageDTO.FDSMessageCenterMessage_Type.FDSMessageCenterMessage_Type_CHAT_GROUP)
//		// {
//		// chat_message_icon.setBackgroundResource(R.drawable.chat_message_icon);
//		// } else
//		// {
//		// chat_message_icon.setBackgroundResource(R.drawable.icon_user_businesscard);
//		// }
//
//		chatRoomName = (TextView) findViewById(R.id.chatRoomName);
//		chatRoomName.setText(title);
//
//		pullListViewContainer = (PullToRefreshListView) findViewById(R.id.chatMessagesList);
//		pullListViewContainer.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
//
//		messagesList = pullListViewContainer.getRefreshableView();/* get list view */
//		chatRoomPageAdapter = new ChatAdapter(ChatActivity.this, chatMessages);
//		pullListViewContainer.setOnRefreshListener(this);
//
//		messagesList.setItemsCanFocus(true);
//
//		messagesList.setAdapter(chatRoomPageAdapter);
//		pullListViewContainer.onRefreshComplete(false);
//
//		/* 点击发送按钮 */
//		m_sendButton = (Button) findViewById(R.id.chat_b_send);
//	}
//
//	/**
//	 * 初始化下面的键盘栏
//	 */
//	private void initKeybord()
//	{
//		chat_b_text_indicator = (Button) findViewById(R.id.chat_b_text_indicator);
//		chat_b_record_indicator = (Button) findViewById(R.id.chat_b_record_indicator);
//		chat_et_edit = (EditText) findViewById(R.id.chat_et_edit);
//		chat_et_edit.addTextChangedListener(this);
//		chat_b_record = (Button) findViewById(R.id.chat_b_record);
//		chat_iv_send_more = (ImageButton) findViewById(R.id.chat_iv_send_more);
//		chat_b_send = (Button) findViewById(R.id.chat_b_send);
//		chat_rl_face_layout = (RelativeLayout) findViewById(R.id.layout_face);
//		chat_ll_cancel_voice = (LinearLayout) this.findViewById(R.id.chat_ll_cancel_voice);
//		chat_layout_voice_popup = this.findViewById(R.id.layout_chat_voice_popup);
//		chat_ll_voice_rcd_hint_rcding = (LinearLayout) this.findViewById(R.id.chat_ll_voice_rcd_hint_rcding);
//		chat_ll_voice_rcd_hint_loading = (LinearLayout) this.findViewById(R.id.chat_ll_voice_rcd_hint_loading);
//		chat_ll_voice_rcd_hint_tooshort = (LinearLayout) this.findViewById(R.id.chat_voice_ll_rcd_hint_tooshort);
//		chat_iv_voice_volume = (ImageView) this.findViewById(R.id.chat_iv_voice_volume);
//		initFace(chat_et_edit, this);
//	}
//
//	/* 刷新聊天室界面 */
//	public void updateChatRoomViews()
//	{
//		chatRoomPageAdapter.notifyDataSetChanged();
//		if (count < 0)
//			messagesList.setSelection(chatRoomPageAdapter.getCount() - 1);
//		else
//		{
//			messagesList.setSelection(count - 1);
//		}
//	}
//
//	@Override
//	public void getIMCB(ChatMessageDTO chatMessage)
//	{
//		count = -1;
//		if ((chatMessage.m_messageType == ChatMessageDTO.CHAT_MESSAGE_TYPE.CHAT_MESSAGE_TYPE_CHAT_PERSON && chatMessage.m_senderID.equalsIgnoreCase(friendID))
//				|| (chatMessage.m_messageType == ChatMessageDTO.CHAT_MESSAGE_TYPE.CHAT_MESSAGE_TYPE_CHAT_GROUP && chatMessage.m_groupID.equals(this.m_groupID)))
//		{
//			long sendTime = Long.valueOf(chatMessage.m_sendTime);
//			udpateNewTip(sendTime);
//			chatMessages.add(chatMessage);
//			updateChatRoomViews();
//			FDSUserCenterDataManager.getInstance().setChatMessageState(chatMessage.m_messageID, SystemEnum.FDS_Message_State.FDS_Message_State_Done);
//		}
//	}
//
//	/* 增加记录到 */
//	public void updateChatRecordToMessageCenter(ChatMessageDTO chatMessage, String receiverName, String receiverIcon)
//	{
//		MessageCenterMessageDTO message = new MessageCenterMessageDTO();
//		message.m_messageClass = MessageCenterMessageDTO.FDSMessageCenterMessage_Class.FDSMessageCenterMessage_Class_SYSTEM;
//		message.m_senderID = chatMessage.m_senderID;// SystemDefine.AddFriend_Check_ID;//friend.m_userID;
//
//		if (chatMessage.m_roleType == SystemEnum.FDS_Role_Type.FDS_Role_Type_User)
//		{
//			message.m_icon = receiverIcon;
//			message.m_senderName = receiverName;
//			message.m_senderID = chatMessage.m_recevierID;
//			message.m_messageClass = MessageCenterMessageDTO.FDSMessageCenterMessage_Class.FDSMessageCenterMessage_Class_USER;
//			message.m_messageType = MessageCenterMessageDTO.FDSMessageCenterMessage_Type.FDSMessageCenterMessage_Type_CHAT_PERSON;
//
//			if (chatMessage.m_imageURL != null && chatMessage.m_content == null)
//			{
//				message.m_content = "[图片]";
//			} else
//			{
//				message.m_content = chatMessage.m_content;
//			}
//		} else
//		{
//			message.m_icon = receiverIcon;
//			message.m_senderName = receiverName;
//			message.m_senderID = chatMessage.m_groupID;
//			message.m_param1 = String.valueOf(chatMessage.m_roleType.ordinal());
//			message.m_param2 = chatMessage.m_groupID;
//			message.m_messageClass = MessageCenterMessageDTO.FDSMessageCenterMessage_Class.FDSMessageCenterMessage_Class_USER;
//			message.m_messageType = MessageCenterMessageDTO.FDSMessageCenterMessage_Type.FDSMessageCenterMessage_Type_CHAT_GROUP;
//
//			if (chatMessage.m_imageURL != null && chatMessage.m_content == null)
//			{
//				message.m_content = chatMessage.m_senderName + ":[图片]";
//			} else
//			{
//				message.m_content = chatMessage.m_senderName + ": " + chatMessage.m_content;
//			}
//		}
//		message.m_sendTime = chatMessage.m_sendTime;
//
//		message.m_state = SystemEnum.FDS_Message_State.FDS_Message_State_Done;
//		FDSUserCenterDataManager.getInstance().addMessageCenterMessage(userID, message);
//		return;
//	}
//
//	@Override
//	public void getIMReplyCB(String messageID, String result, String receiverName, String reveiverIcon)
//	{
//		int id = Integer.valueOf(messageID);
//		if (result != null && result.equals("OK"))
//		{
//			for (ChatMessageDTO chatMessage : sendingMessge)
//			{
//				if (chatMessage.m_messageID == id)
//				{
//					chatMessage.m_send_state = CHAT_SEND_STATE.CHAT_MESSAGE_SEND_STATE_SUCCESS;
//					FDSUserCenterDataManager.getInstance().addChatRecord(userID, chatMessage);
//					sendingMessge.remove(chatMessage);
//					updateChatRecordToMessageCenter(chatMessage, receiverName, reveiverIcon);
//					break;
//				}
//			}
//			chatRoomPageAdapter.notifyDataSetChanged();
//		}
//	}
//
//	@Override
//	public void subedFriendCB(UserDTO friend)
//	{
//		String userID = UserManager.getInstance().getNowUser().userId;
//		FDSUserCenterDataManager.getInstance().subFriend(userID, friend.userId);
//	}
//
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//	{
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data)
//	{
//		if (fdsUtil != null)
//			fdsUtil.onActivityResult(requestCode, resultCode, data);
//		super.onActivityResult(requestCode, resultCode, data);
//	}
//
//	private Util getFdsUtil()
//	{
//		if (fdsUtil == null)
//		{
//			fdsUtil = new Util(ChatActivity.this, new onPhotoReceiverListener()
//			{
//
//				@Override
//				public void onReceive(final File file)
//				{
//					if (file == null || !file.exists())
//						return;
//					String path = file.getAbsolutePath();
//					String end = Util.getFileName(path);
//					getFdsUtil().uploadPic(path, UserManager.getInstance().m_user.userId, PathManager.FILE_UPDATE_TAG_CHAT, end);
//				}
//			}, new onReceiverListener()
//			{
//				@Override
//				public void onReceive(boolean success, String path)
//				{
//					if (success)
//					{
//						ChatMessageDTO chatMessage = new ChatMessageDTO();
//						sendMessage(chatMessage);
//						chatMessage.m_data_type = CHAT_MESSAGE_DATA_TYPE.CHAT_MESSAGE_DATA_IMAGE;
//						chatMessage.m_imageURL = path;
//						UserCenterMessageManager.getInstance().sendImage(chatMessage);
//					} else
//					{
//						Toast.makeText(ChatActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
//					}
//				}
//			});
//
//		}
//		return fdsUtil;
//	}
//
//	public void onClick(View v)
//	{
//		switch (v.getId())
//		{
//		case R.id.chat_ll_send_img_indicator:
//		{
//			getFdsUtil().toPickPhoto();
//			break;
//		}
//		case R.id.chat_ll_send_file_indicator:
//		{
//			break;
//		}
//		case R.id.chat_message_icon:
//			if (message.m_messageType == MessageCenterMessageDTO.FDSMessageCenterMessage_Type.FDSMessageCenterMessage_Type_CHAT_PERSON)
//			{
//				UserDTO user = new UserDTO();
//				user.userId = message.m_senderID;
//				Intent intent = new Intent(ChatActivity.this, UserCardActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putSerializable("user", user);
//				intent.putExtras(bundle);
//				startActivity(intent);
//			} else if (message.m_messageType == MessageCenterMessageDTO.FDSMessageCenterMessage_Type.FDSMessageCenterMessage_Type_CHAT_GROUP)
//			{
//				Bundle bundle = new Bundle();
//				bundle.putSerializable("message", message);
//				toActivity(GroupCardActivity.class, bundle);
//			}
//			break;
//		case R.id.chat_iv_send_more:
//			showLayoutMore();
//			break;
//		case R.id.chat_b_record_indicator:
//			SwitchKeyBoradMode(CHAT_KEYBOARD_MODE_RECORD);
//			break;
//		case R.id.chat_b_text_indicator:
//			SwitchKeyBoradMode(CHAT_KEYBOARD_MODE_EDIT);
//			break;
//		case R.id.chat_b_send:
//			ChatMessageDTO chatMessage = new ChatMessageDTO();
//			chatMessage.m_content = chat_et_edit.getText().toString();
//			if (chatMessage.m_content == null || chatMessage.m_content.length() < 1)
//			{
//				Toast.makeText(ChatActivity.this, "发送内容不能为空！", Toast.LENGTH_LONG).show();
//				return;
//			}
//			chatMessage.m_data_type = CHAT_MESSAGE_DATA_TYPE.CHAT_MESSAGE_DATA_TEXT;
//			sendMessage(chatMessage);
//			UserCenterMessageManager.getInstance().sendIM(chatMessage);
//			break;
//		case R.id.chat_et_edit:
//			SwitchKeyBoradMode(CHAT_KEYBOARD_MODE_EDIT);
//			break;
//		case R.id.face_btn:
//			showLayoutFace();
//			break;
//		}
//	}
//
//	/**
//	 * @param chatMessage
//	 */
//	private void sendMessage(ChatMessageDTO chatMessage)
//	{
//		chatMessage.m_messageID = sendingMessge.size();
//		UserDTO user = UserManager.getInstance().getNowUser();
//		// chatMessage.m_sendTime = String.valueOf(System.currentTimeMillis());
//		chatMessage.m_sendTime = DateManager.FormatTimeYYDDMMHHMMSMillis(System.currentTimeMillis());
//		chatMessage.m_senderID = userID;// "0000009";
//		chatMessage.m_senderName = user.userName;// "0000009";
//		chatMessage.m_senderIcon = user.userIcon;
//		chatMessage.m_recevierID = friendID;// "0000009";
//		// TODO 正式版 需不需要发送中标示
//		chatMessage.m_send_state = CHAT_SEND_STATE.CHAT_MESSAGE_SEND_STATE_SUCCESS;
//		chatMessage.m_roleType = m_roleType;
//		if (isGroupRoom == true)
//		{
//			chatMessage.m_messageType = ChatMessageDTO.CHAT_MESSAGE_TYPE.CHAT_MESSAGE_TYPE_CHAT_GROUP;
//			chatMessage.m_groupID = m_groupID;
//		} else
//		{
//			chatMessage.m_messageType = ChatMessageDTO.CHAT_MESSAGE_TYPE.CHAT_MESSAGE_TYPE_CHAT_PERSON;
//		}
//		chatMessage.m_state = SystemEnum.FDS_Message_State.FDS_Message_State_Done;
//		chat_et_edit.setText("");
//		count = -1;
//		long sendTime = Long.valueOf(System.currentTimeMillis());
//		udpateNewTip(sendTime);
//		chatMessages.add(chatMessage);
//		sendingMessge.add(chatMessage);
//		updateChatRoomViews();
//	}
//
//	/**
//	 * 展示表情面板
//	 */
//	private void showLayoutFace()
//	{
//		chat_ll_send_more.setVisibility(View.GONE);
//		hideSoftInput();
//	}
//
//	/**
//	 * 发送文件,发送图片下面的界面展示出来
//	 */
//	private void showLayoutMore()
//	{
//		chat_ll_send_more.setVisibility(View.VISIBLE);
//		chat_rl_face_layout.setVisibility(View.GONE);
//		hideSoftInput();
//	}
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event)
//	{
//		if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
//		{
//			im.hideSoftInputFromWindow(chat_et_edit.getWindowToken(), 0);
//			return true;
//		}
//		return super.onTouchEvent(event);
//	}
//
//	/**
//	 * 选择键盘栏录音还是文字输入
//	 * 
//	 * @param chatKeyboardMode
//	 */
//	private void SwitchKeyBoradMode(int chatKeyboardMode)
//	{
//		switch (chatKeyboardMode)
//		{
//		case CHAT_KEYBOARD_MODE_EDIT:
//			chat_b_record_indicator.setVisibility(View.VISIBLE);
//			chat_b_text_indicator.setVisibility(View.INVISIBLE);
//			chat_et_edit.setVisibility(View.VISIBLE);
//			chat_b_record.setVisibility(View.INVISIBLE);
//			chat_ll_send_more.setVisibility(View.GONE);
//			chat_rl_face_layout.setVisibility(View.GONE);
//			chat_et_edit.requestFocus();
//			showSoftInput();
//			break;
//		case CHAT_KEYBOARD_MODE_RECORD:
//			hideSoftInput();
//			chat_b_record_indicator.setVisibility(View.INVISIBLE);
//			chat_b_text_indicator.setVisibility(View.VISIBLE);
//			chat_et_edit.setVisibility(View.INVISIBLE);
//			chat_b_record.setVisibility(View.VISIBLE);
//			chat_ll_send_more.setVisibility(View.GONE);
//			chat_rl_face_layout.setVisibility(View.GONE);
//			break;
//		}
//		this.chat_keyborad_mode = chatKeyboardMode;
//	}
//
//	public void hideSoftInput()
//	{
//		im.hideSoftInputFromWindow(chat_et_edit.getWindowToken(), 0);
//	}
//
//	/**
//	 * 键盘展示
//	 */
//	private void showSoftInput()
//	{
//		im.showSoftInput(chat_et_edit, InputMethodManager.SHOW_FORCED);
//	}
//
//	@Override
//	public void onPullDownToRefresh()
//	{
//
//		Log.d("test", "下拉刷新");
//		// 演示延时下拉刷新
//		// 此处启动线程中执行耗时任务
//		new Thread(new Runnable()
//		{
//			@Override
//			public void run()
//			{
//				handler.sendEmptyMessage(ZZThreadMessageType.PullRefresh_chat_activtity);
//			}
//		}).start();
//	}
//
//	@Override
//	public void onPullUpToRefresh()
//	{
//		Log.d("test", "上拉刷新");
//	}
//
//	@Override
//	public void onFaceClick()
//	{
//		showLayoutFace();
//	}
//
//	@Override
//	public void onTextClick()
//	{
//		SwitchKeyBoradMode(CHAT_KEYBOARD_MODE_EDIT);
//	}
//
//	@Override
//	public void beforeTextChanged(CharSequence s, int start, int count, int after)
//	{
//	}
//
//	@Override
//	public void onTextChanged(CharSequence s, int start, int before, int count)
//	{
//		int length = s.toString().length();
//		if (length > 0)
//		{
//			chat_iv_send_more.setVisibility(View.GONE);
//			chat_b_send.setVisibility(View.VISIBLE);
//		} else
//		{
//			chat_iv_send_more.setVisibility(View.VISIBLE);
//			chat_b_send.setVisibility(View.GONE);
//		}
//	}
//
//	@Override
//	public void afterTextChanged(Editable s)
//	{
//	}
//
//	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
//	{
//		@Override
//		public void onReceive(Context context, Intent intent)
//		{
//			ZZSessionManager.getInstance().netStateUpdate();
//		}
//	};
//
//	void registerNetMonitor()
//	{
//		IntentFilter filter = new IntentFilter();
//		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//		SystemRunning.sharedSystemRunning().getApplicationContext().registerReceiver(mBroadcastReceiver, filter);
//	}
//
//	void unRegisterNetMonitor()
//	{
//		IntentFilter filter = new IntentFilter();
//		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//		SystemRunning.sharedSystemRunning().getApplicationContext().unregisterReceiver(mBroadcastReceiver);
//	}
//
//	@Override
//	public boolean onTouch(View v, MotionEvent event)
//	{
//		if (!Environment.getExternalStorageDirectory().exists())
//		{
//			Toast.makeText(this, "请插入sd卡", Toast.LENGTH_LONG).show();
//			return false;
//		}
//		if (chat_keyborad_mode == 1)
//		{
//			System.out.println("1");
//			int[] location = new int[2];
//			chat_b_record.getLocationInWindow(location); // 获取在当前窗口内的绝对坐标
//			int btn_rc_Y = location[1];
//			int btn_rc_X = location[0];
//			int[] del_location = new int[2];
//			chat_ll_cancel_voice.getLocationInWindow(del_location);
//			int del_Y = del_location[1];
//			int del_x = del_location[0];
//			if (event.getAction() == MotionEvent.ACTION_DOWN && voice_flag == 1)
//			{
//				if (!Environment.getExternalStorageDirectory().exists())
//				{
//					Toast.makeText(this, "No SDCard", Toast.LENGTH_LONG).show();
//					return false;
//				}
//				System.out.println("2");
//				if (event.getRawY() > btn_rc_Y && event.getRawX() > btn_rc_X)
//				{// 判断手势按下的位置是否是语音录制按钮的范围内
//					System.out.println("3");
//					chat_b_record.setBackgroundResource(R.drawable.img_chat_voice_pressed);
//					chat_layout_voice_popup.setVisibility(View.VISIBLE);
//					chat_ll_voice_rcd_hint_loading.setVisibility(View.VISIBLE);
//					chat_ll_voice_rcd_hint_rcding.setVisibility(View.GONE);
//					chat_ll_voice_rcd_hint_tooshort.setVisibility(View.GONE);
//					voice_path = PathManager.getInstance().getRecordPath(this).getPath() + "/" + System.currentTimeMillis() + ".amr";
//					start(voice_path);
//					chat_ll_voice_rcd_hint_loading.setVisibility(View.GONE);
//					chat_ll_voice_rcd_hint_rcding.setVisibility(View.VISIBLE);
//					chat_ll_cancel_voice.setVisibility(View.GONE);
//					voice_flag = 2;
//					startVoiceT = System.currentTimeMillis();
//				}
//			} else if (event.getAction() == MotionEvent.ACTION_UP && voice_flag == 2)
//			{// 松开手势时执行录制完成
//				System.out.println("4");
//				chat_b_record.setBackgroundResource(R.drawable.img_chat_text_edit);
//				if (event.getRawY() >= del_Y && event.getRawY() <= del_Y + chat_ll_cancel_voice.getHeight() && event.getRawX() >= del_x && event.getRawX() <= del_x + chat_ll_cancel_voice.getWidth())
//				{
//					chat_layout_voice_popup.setVisibility(View.GONE);
//					chat_ll_cancel_voice.setVisibility(View.GONE);
//					stop();
//					voice_flag = 1;
//					Util.deleteFile(voice_path);
//				} else
//				{
//					chat_ll_voice_rcd_hint_rcding.setVisibility(View.GONE);
//					stop();
//					long endVoiceT = System.currentTimeMillis();
//					voice_flag = 1;
//					int time = (int) ((endVoiceT - startVoiceT) / 1000);
//					if (time < 2)
//					{
//						voice_isShort = true;
//						chat_ll_voice_rcd_hint_loading.setVisibility(View.GONE);
//						chat_ll_voice_rcd_hint_rcding.setVisibility(View.GONE);
//						chat_ll_voice_rcd_hint_tooshort.setVisibility(View.VISIBLE);
//						chat_ll_voice_rcd_hint_tooshort.setVisibility(View.GONE);
//						chat_layout_voice_popup.setVisibility(View.GONE);
//						voice_isShort = false;
//						Util.deleteFile(voice_path);
//						return false;
//					}
//					ChatMessageDTO messageDTO = new ChatMessageDTO();
//					messageDTO.m_record = voice_path;
//					messageDTO.m_data_type = CHAT_MESSAGE_DATA_TYPE.CHAT_MESSAGE_DATA_RECORD;
//					sendMessage(messageDTO);
//					UserCenterMessageManager.getInstance().sendIM(messageDTO);
//					chat_layout_voice_popup.setVisibility(View.GONE);
//				}
//			}
//			if (event.getRawY() < btn_rc_Y)
//			{// 手势按下的位置不在语音录制按钮的范围内
//				System.out.println("5");
//				chat_ll_cancel_voice.setVisibility(View.VISIBLE);
//				chat_ll_cancel_voice.setBackgroundResource(R.drawable.img_chat_voice_rcd_cancel_bg);
//				if (event.getRawY() >= del_Y && event.getRawY() <= del_Y + chat_ll_cancel_voice.getHeight() && event.getRawX() >= del_x && event.getRawX() <= del_x + chat_ll_cancel_voice.getWidth())
//				{
//					chat_ll_cancel_voice.setBackgroundResource(R.drawable.img_chat_voice_rcd_cancel_bg_focused);
//				}
//			} else
//			{
//				chat_ll_cancel_voice.setVisibility(View.GONE);
//				chat_ll_cancel_voice.setBackgroundResource(0);
//			}
//		}
//		return super.onTouchEvent(event);
//	}
//
//	private void start(String path)
//	{
//		mSensor.start(path);
//		getHandler().postDelayed(mPollTask, POLL_INTERVAL);
//	}
//
//	private void stop()
//	{
//		getHandler().removeCallbacks(mSleepTask);
//		getHandler().removeCallbacks(mPollTask);
//		mSensor.stop();
//		chat_iv_voice_volume.setImageResource(R.drawable.img_chat_voice_amp1);
//	}
//
//	private Runnable mSleepTask = new Runnable()
//	{
//		public void run()
//		{
//			stop();
//		}
//	};
//	private Runnable mPollTask = new Runnable()
//	{
//		public void run()
//		{
//			double amp = mSensor.getAmplitude();
//			updateDisplay(amp);
//			getHandler().postDelayed(mPollTask, POLL_INTERVAL);
//		}
//	};
//
//	private void updateDisplay(double signalEMA)
//	{
//		switch ((int) signalEMA)
//		{
//		case 0:
//		case 1:
//			chat_iv_voice_volume.setImageResource(R.drawable.img_chat_voice_amp1);
//			break;
//		case 2:
//		case 3:
//			chat_iv_voice_volume.setImageResource(R.drawable.img_chat_voice_amp2);
//			break;
//		case 4:
//		case 5:
//			chat_iv_voice_volume.setImageResource(R.drawable.img_chat_voice_amp3);
//			break;
//		case 6:
//		case 7:
//			chat_iv_voice_volume.setImageResource(R.drawable.img_chat_voice_amp4);
//			break;
//		case 8:
//		case 9:
//			chat_iv_voice_volume.setImageResource(R.drawable.img_chat_voice_amp5);
//			break;
//		case 10:
//		case 11:
//			chat_iv_voice_volume.setImageResource(R.drawable.img_chat_voice_amp6);
//			break;
//		default:
//			chat_iv_voice_volume.setImageResource(R.drawable.img_chat_voice_amp7);
//			break;
//		}
//	}
//}
