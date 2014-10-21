package com.bgood.xn.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.UserManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.squareup.picasso.Picasso;


/**
 * 我的关注适配器
 */
public class VermicelliAdapter extends BaseAdapter implements UserCenterMessageManagerInterface
{
    private Context m_context;
    private LayoutInflater m_inflater;
    private List<UserDTO> m_list;
    
    private int index = 0;
    private int m_position = 0;
    UserCenterMessageManager messageManager = UserCenterMessageManager.getInstance();
    
    public VermicelliAdapter(Context context, List<UserDTO> list)
    {
        super();
        this.m_context = context;
        this.m_inflater = LayoutInflater.from(m_context);
        this.m_list = list;
    }

    @Override
    public int getCount()
    {
        return m_list.size();
    }

    @Override
    public Object getItem(int position)
    {
        return m_list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = m_inflater.inflate(R.layout.layout_vermicelli_item, null);
            holder.userIconImgV = (ImageView) convertView.findViewById(R.id.vermicelli_item_imgv_user_icon);
            holder.userNameTv = (TextView) convertView.findViewById(R.id.vermicelli_item_tv_user_name);
            holder.userSexImgV = (ImageView) convertView.findViewById(R.id.vermicelli_item_imgv_sex);
            holder.userIdentityImgV = (ImageView) convertView.findViewById(R.id.vermicelli_item_imgv_identity);
            holder.userInfoTv = (TextView) convertView.findViewById(R.id.vermicelli_item_tv_user_info);
            holder.followConfirmBtn = (Button) convertView.findViewById(R.id.vermicelli_item_btn_confirm);
            holder.followCancelBtn = (Button) convertView.findViewById(R.id.vermicelli_item_btn_cancel);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        
        final UserDTO userDTO = m_list.get(position);
        if (userDTO != null && userDTO.userIcon != null && !userDTO.userIcon.equals(""))
            Picasso.with(m_context).load(userDTO.userIcon).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(holder.userIconImgV);
        holder.userNameTv.setText(userDTO.nickn);
        int sex = userDTO.sex;
        if (sex == 1)
        {
            holder.userSexImgV.setImageResource(R.drawable.img_common_sex_male);
        }
        else if (sex == 2)
        {
            holder.userSexImgV.setImageResource(R.drawable.img_common_sex_female);
        }
        else
        {
            holder.userSexImgV.setImageResource(0);
        }
        
        holder.userInfoTv.setText(userDTO.signature);
        
        if (userDTO.guanzhuType == 1)
        {
            holder.followConfirmBtn.setVisibility(View.INVISIBLE);
            holder.followCancelBtn.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.followConfirmBtn.setVisibility(View.VISIBLE);
            holder.followCancelBtn.setVisibility(View.INVISIBLE);
        }
        
        // 关注
        holder.followConfirmBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                WindowUtil.getInstance().progressDialogShow(m_context, "关注中...");
                messageManager.registerObserver(VermicelliAdapter.this);
                messageManager.follow(userDTO.userId, UserManager.getInstance().m_user.userId, 0);
                index = 0;
                m_position = position;
            }
        });
        
        // 取消关注
        holder.followCancelBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                WindowUtil.getInstance().progressDialogShow(m_context, "取消关注中...");
                messageManager.registerObserver(VermicelliAdapter.this);
                messageManager.follow(userDTO.userId, UserManager.getInstance().m_user.userId, 1);
                index = 1;
                m_position = position;
            }
        });
        
        return convertView;
    }
    
    private class ViewHolder
    {
        ImageView userIconImgV;        // 用户头像
        TextView userNameTv;           // 用户名
        ImageView userSexImgV;         // 用户性别
        ImageView userIdentityImgV;    // 用户特征
        TextView userInfoTv;           // 用户信息
        Button followConfirmBtn;       // 关注
        Button followCancelBtn;        // 已关注
    }
    
    @Override
    public void userRegisterRequestCB(Reulst result_state, String verfy)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void verifyPhoneNumberCB(Reulst result_state, boolean verfied, String[] ids)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void getBandgoodCodeCB(Reulst result_state, String[] ids)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void userRegisterReplyCB(UserDTO user, boolean success)
    {
    }

    @Override
    public void userLoginReplyCB(UserDTO user)
    {
    }

    @Override
    public void forgetPwdPhoneCB(Reulst result_state, String verfy)
    {
    }

    @Override
    public void forgetPwdCodeCB(Reulst result_state)
    {
    }

    @Override
    public void forgetPwdNewPwdCB(Reulst result_state, String userid, boolean success, String token, String bserver, String fserver)
    {
    }

    @Override
    public void getPersonalDataCB(Reulst result_state, UserDTO user)
    {
    }

    @Override
    public void modifyPersonalInfoCB(Reulst result_state)
    {
    }

    @Override
    public void followCB(Reulst result_state)
    {
        WindowUtil.getInstance().DismissAllDialog();
        messageManager.unregisterObserver(VermicelliAdapter.this);
        if (result_state.resultCode == ReturnCode.RETURNCODE_OK)
        {
            if (index == 0)
            {
                Toast.makeText(m_context, "关注成功", Toast.LENGTH_SHORT).show();
                m_list.get(m_position).guanzhuType = 1;
            }
            else
            {
                Toast.makeText(m_context, "取消关注成功", Toast.LENGTH_SHORT).show();
                m_list.get(m_position).guanzhuType = 0;
            }
            notifyDataSetChanged();
        } else
        {
            Toast.makeText(m_context, "操作失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getFollowListCB(Reulst result_state, ArrayList<UserDTO> list)
    {
    }
    
    @Override
    public void modifyPasswordCB(Reulst result_state)
    {
    }

    @Override
    public void userLogoutCB(String result)
    {
    }

    @Override
    public void getIMReplyCB(String messageID, String result, String receiverName, String reveiverIcon)
    {
    }

    @Override
    public void getIMCB(ChatMessageDTO chatMessage)
    {
    }

    @Override
    public void addFriendReplyCB(UserDTO friend, String result, String replywords, String time)
    {
    }

    @Override
    public void addFriendRequestCB(UserDTO friend, String accesswords, String time)
    {
    }

    @Override
    public void subFriendReplyCB(String result, UserDTO friend)
    {
    }

    @Override
    public void subedFriendCB(UserDTO friend)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void modifyUserCardReplyCB(StateDTO state)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void searchFriendsReplyCB(Reulst result_state, List<UserDTO> list)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void getUserCardReplyCB(String result, UserDTO user)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void modifyFriendsRemarkNameReplyCB(StateDTO state)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void joinGroupReplyCB(StateDTO state)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void quitGroupReplyCB(StateDTO state)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void getUserFriendsReplyCB(List<UserDTO> list)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void checkUsersMembersByPhoneReplyCB(ArrayList<UserDTO> list, int[] indexs)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void getGroupMembersReplyCB(GroupDTO group)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void modifyPassword01ReplyCB(StateDTO state)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void modifyPassword02ReplyCB(StateDTO state)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void feedbackReplyCB(StateDTO state)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void checkVersionReplyCB(int version, String downloadURL)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void cardInfoModifyCB(Object object)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteGroupMemberReplyCB(StateDTO state)
    {
        // TODO Auto-generated method stub
        
    }
}
