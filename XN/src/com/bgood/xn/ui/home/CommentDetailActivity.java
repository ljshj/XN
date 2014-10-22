//package com.bgood.xn.ui.home;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.bgood.xn.R;
//import com.bgood.xn.adapter.WeiqiangCommentDetailAdapter;
//import com.bgood.xn.network.BaseNetWork.ReturnCode;
//import com.bgood.xn.ui.BaseActivity;
//import com.bgood.xn.view.xlistview.XListView;
//import com.bgood.xn.widget.CMyGridView;
//import com.squareup.picasso.Picasso;
//
///**
// * 评论详情页面
// */
//public class CommentDetailActivity extends BaseActivity implements OnClickListener
//{
//    private Button m_backBtn = null;
//    private ProgressBar m_progressBar = null;
//    private XListView m_listXlv = null;
//    
//    private ImageView m_sendUserIconImgV = null;  // 发送用户头像
//    private TextView m_sendUserNameTv = null;    // 发送用户姓名
//    private TextView m_sendTimeTv = null;        // 发送时间
//    private TextView m_sendContentTv = null;     // 发送内容
//    private ImageView m_oneImageImgV = null;     // 微博图片
//    private LinearLayout m_zanCountLl = null;    // 点赞布局
//    private TextView m_zanCountTv = null;        // 点赞的数量
//    private LinearLayout m_replyCountLl = null;  // 评论布局
//    private TextView m_replyCountTv = null;      // 评论的数量
//    private LinearLayout m_sendCountLl = null;    // 转发布局
//    private TextView m_sendCountTv = null;        // 转发的数量
//    private LinearLayout m_shareCountLl = null;    // 分享布局
//    private TextView m_shareCountTv = null;        // 分享的数量
//    
//    private LinearLayout layout_images;
//    private CMyGridView oneImgV;
//    
//    WeiqiangMessageManager messageManager = WeiqiangMessageManager.getInstance(); 
//    
//    private WeiqiangCommentDetailAdapter adapter;
//    private List<ReplyCommentDTO> m_list = new ArrayList<ReplyCommentDTO>();
//    private CommentsDTO m_commentsDTO;
//    
//    private int comment_start = 0;
//    private int comment_add =10;
//    
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_comment_detail);
//        
//        getIntentData();
//        findView();
//        setListener();
//        initData();
//    }
//
//    @Override
//    public void onResume()
//    {
//        super.onResume();
//        
//        if (messageManager != null)
//        {
//            // 消息注册
//            messageManager.registerObserver(this);
//        }
//    }
//
//    @Override
//    public void onPause()
//    {
//        super.onPause();
//        
//        if (messageManager != null)
//        {
//            // 消息注销
//            messageManager.unregisterObserver(this);
//        }
//    }
//    
//    /**
//     * 得到传值数据方法
//     */
//    private void getIntentData()
//    {
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null)
//        {
//            m_commentsDTO = (CommentsDTO) bundle.getSerializable("comment");
//        }
//    }
//    
//    /**
//     * 控件初始化方法
//     */
//    private void findView()
//    {
//        m_backBtn = (Button) findViewById(R.id.return_btn);
//        m_progressBar = (ProgressBar) findViewById(R.id.common_progress);
//        m_listXlv = (XListView) findViewById(R.id.common_xlv);
//        
//        View head_weiqiang_detail = inflater.inflate(R.layout.head_comment_detail, m_listXlv, false);
//        m_sendUserIconImgV = (ImageView) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_imgv_send_icon);
//        m_sendUserNameTv = (TextView) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_tv_name);
//        m_sendTimeTv = (TextView) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_tv_time);
//        m_sendContentTv = (TextView) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_tv_content);
////        m_oneImageImgV = (ImageView) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_imgv_image_one);
//        m_zanCountLl = (LinearLayout) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_ll_zan_count);
//        m_zanCountTv = (TextView) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_tv_zan_count);
//        
//        m_replyCountLl = (LinearLayout) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_ll_reply_count);
//        m_replyCountTv = (TextView) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_tv_reply_count);
//        
//        m_sendCountLl = (LinearLayout) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_ll_transform_send_count);
//        m_sendCountTv = (TextView) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_tv_transform_send_count);
//        
//        m_shareCountLl = (LinearLayout) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_ll_share_count);
//        m_shareCountTv = (TextView) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_tv_share_count);
//        
//        layout_images = (LinearLayout) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_ll_comment_images);
//        oneImgV = (CMyGridView) head_weiqiang_detail.findViewById(R.id.weiqiang_detail_imgv_image_one);
//        
//        m_listXlv.addHeaderView(head_weiqiang_detail);
//        adapter = new WeiqiangCommentDetailAdapter(this, m_list);
//        m_listXlv.setAdapter(adapter);
//    }
//    
//    /**
//     * 控件时间监听方法
//     */
//    private void setListener()
//    {
//        // 返回按钮
//        m_backBtn.setOnClickListener(new OnClickListener()
//        {
//            
//            @Override
//            public void onClick(View v)
//            {
//                finish();     
//            }
//        });
//        
//        m_zanCountLl.setOnClickListener(this);
//        m_replyCountLl.setOnClickListener(this);
//        m_sendCountLl.setOnClickListener(this);
//        m_shareCountLl.setOnClickListener(this);
//    }
//    
//    @Override
//    protected void initData()
//    {
//        super.initData();
//        messageManager.getWeiqiangContent(m_commentsDTO.commentId, comment_start, comment_start + comment_add);
//    }
//
//    @Override
//    public void getWeiqiangContentCB(CommentsDTO commentsDTO, Reulst result_state)
//    {
//        super.getWeiqiangContentCB(commentsDTO, result_state);
//        if (result_state.resultCode == ReturnCode.RETURNCODE_OK && commentsDTO != null)
//        {
//            m_progressBar.setVisibility(View.GONE);
//            m_listXlv.setVisibility(View.VISIBLE);
//            comment_start = comment_start + comment_add;
//            
//            setData(commentsDTO);
//        }
//    }
//    
//    /**
//     * 设置数据显示方法
//     * @param commentsDTO
//     */
//    private void setData(CommentsDTO commentsDTO)
//    {
//        if (commentsDTO != null && commentsDTO.senderIcon != null && !commentsDTO.senderIcon.equals(""))
//            Picasso.with(this).load(commentsDTO.senderIcon).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(m_sendUserIconImgV);
//        m_sendUserNameTv.setText(commentsDTO.senderName);
//        m_sendTimeTv.setText(commentsDTO.sendTime);
//        m_sendContentTv.setText(commentsDTO.content);
//        m_zanCountTv.setText(commentsDTO.like_count + "");
//        m_replyCountTv.setText(commentsDTO.revertCount + "");
//        m_sendCountTv.setText(commentsDTO.forward_count + "");
//        m_shareCountTv.setText(commentsDTO.share_count  + "");
//        
//        if (commentsDTO.reverts != null)
//        {
//            if (commentsDTO.reverts.size() < 10)
//            {
//                m_listXlv.setPullLoadEnable(false);
//            }
//            
//            m_list.addAll(commentsDTO.reverts);
//            adapter.notifyDataSetChanged();
//        }
//        
//        if (commentsDTO.imageList != null && commentsDTO.imageList.size() > 0)
//        {
//                layout_images.setVisibility(View.VISIBLE);
//                oneImgV.setVisibility(View.VISIBLE);
//                ImageAdapter imageAdapter = new ImageAdapter(this, commentsDTO.imageList);
//                oneImgV.setAdapter(imageAdapter);
//        }
//    }
//
//    @Override
//    public void onClick(View v)
//    {
//    }
//    
//    
//    /**
//     * 我的动态适配器
//     */
//    public class ImageAdapter extends BaseAdapter
//    {
//        private LayoutInflater m_inflater;
//        private Context m_context;
//        private List<ImageDTO> list;
//        
//        public ImageAdapter(Context context, List<ImageDTO> list)
//        {
//            super();
//            this.m_context = context;
//            this.list = list;
//            this.m_inflater = LayoutInflater.from(m_context);
//        }
//
//        @Override
//        public int getCount()
//        {
//            return list.size();
//        }
//
//        @Override
//        public Object getItem(int position)
//        {
//            return list.get(position);
//        }
//
//        @Override
//        public long getItemId(int position)
//        {
//            return position;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent)
//        {
//            ViewHolder holder;
//            if (convertView == null)
//            {
//                holder = new ViewHolder();
//                convertView = m_inflater.inflate(R.layout.layout_comment_detail_item, null);
//                holder.iconImgV = (ImageView) convertView.findViewById(R.id.comment_detail_imgv_pic);
//                convertView.setTag(holder);
//            }
//            else
//            {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            ImageDTO imageDTO = list.get(position);
//            if (imageDTO != null && imageDTO.img_thum != null && !imageDTO.img_thum.equals(""))
//                Picasso.with(m_context).load(imageDTO.img_thum).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(holder.iconImgV);
//            
//            holder.iconImgV.setOnClickListener(new OnClickListener()
//            {
//                
//                @Override
//                public void onClick(View v)
//                {
//                    WindowUtil.getInstance().dialogViewPagerShow(m_context, list,
//                            position);
//                }
//            });
//            
//            
//            return convertView;
//        }
//
//        class ViewHolder
//        {
//            ImageView iconImgV;           // 头像
//        }
//    }
//}
