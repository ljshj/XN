//package com.bgood.xn.ui.message;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ListView;
//
//import com.bgood.xn.R;
//import com.bgood.xn.ui.base.BaseActivity;
//
///**
// * @author ChenGuoqing 2014-6-26下午2:51:35
// */
//public class MessageCenterActivity extends BaseActivity implements OnItemClickListener
//{
//
//	private ListView lv_message_usercenter;
//	private MessageCenterAdapter adapter;
//	private List<MessageCenterMessageDTO> list = new ArrayList<MessageCenterMessageDTO>();
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.layout_message_usercenter);
//		initViews();
//		initData();
//		setListeners();
//	}
//
//	@Override
//	protected void setListeners()
//	{
//		// TODO Auto-generated method stub
//		super.setListeners();
//	}
//
//	@Override
//	protected void initData()
//	{
//		// TODO Auto-generated method stub
//		super.initData();
//	}
//
//	@Override
//	protected void initViews()
//	{
//		lv_message_usercenter = (ListView) findViewById(R.id.lv_message_usercenter);
//		lv_message_usercenter.setOnItemClickListener(this);
//		adapter = new MessageCenterAdapter(this, list);
//		lv_message_usercenter.setAdapter(adapter);
//	}
//
//	/**
//	 * FDS复制过来 需要检测有无问题
//	 */
//	public void updateMessageView()
//	{
//
//	}
//
//	@Override
//	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
//	{
//		MessageCenterMessageDTO message = new MessageCenterMessageDTO();
//		message.m_senderID = "005";
//		message.m_senderName = "徐景鹏";
//		message.m_messageClass = MessageCenterMessageDTO.FDSMessageCenterMessage_Class.FDSMessageCenterMessage_Class_USER;
//		message.m_messageType = MessageCenterMessageDTO.FDSMessageCenterMessage_Type.FDSMessageCenterMessage_Type_CHAT_PERSON;
//		Bundle bundle = new Bundle();
//		bundle.putSerializable("messageCenterMessage", message);
//		toActivity(ChatActivity.class, bundle);
//		// startActivity(ChatActivity.class);
//	}
//}
