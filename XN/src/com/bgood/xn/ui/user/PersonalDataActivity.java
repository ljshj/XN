//package com.bgood.xn.ui.user;
//
//import java.io.File;
//import java.util.UUID;
//
//import android.app.Dialog;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.Window; 
//import android.view.WindowManager;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bgood.xn.bean.UserBean;
//import com.bgood.xn.ui.BaseActivity;
//import com.squareup.picasso.Picasso;
//
//
///**
// * 个人资料页面
// */
//public class PersonalDataActivity extends BaseActivity implements OnClickListener
//{
//    private Button         m_backBtn         = null; // 返回按钮
//    private RelativeLayout m_iconRl          = null; // 头像布局
//    private ImageView      m_iconImgV        = null; // 头像
//    private TextView       m_idTv            = null; // ID号
//    private RelativeLayout m_phoneRl         = null; // 手机号布局
//    private TextView       m_phoneTv         = null; // 手机号
//    private RelativeLayout m_nameRl          = null; // 称昵布局
//    private TextView       m_nameTv          = null; // 称昵
//    private RelativeLayout m_signatureRl     = null; // 个性签名布局
//    private TextView       m_signatureTv     = null; // 个性签名
//    private RelativeLayout m_sexRl           = null; // 性别布局
//    private TextView       m_sexTv           = null; // 性别
//    private RelativeLayout m_ageRl           = null; // 年龄布局
//    private TextView       m_ageTv           = null; // 年龄
//    private RelativeLayout m_birthdayRl      = null; // 生日布局
//    private TextView       m_birthdayTv      = null; // 生日
//    private RelativeLayout m_constellationRl = null; // 星座布局
//    private TextView       m_constellationTv = null; // 星座
//    private RelativeLayout m_hometownRl      = null; // 家乡地址布局
//    private TextView       m_hometownTv      = null; // 家乡地址
//    private RelativeLayout m_locusRl         = null; // 现所在地布局
//    private TextView       m_locusTv         = null; // 所在地
//    private RelativeLayout m_emailRl         = null; // 邮箱布局
//    private TextView       m_emailTv         = null; // 邮箱
//    private RelativeLayout m_bloodGroupRl    = null; // 血型布局
//    private TextView       m_bloodGroupTv    = null; // 血型
//    private Button         m_doneBtn         = null; // 立即预览我的名片按钮
//    
//    UserCenterMessageManager messageManager = UserCenterMessageManager.getInstance();
//    
//    private FDSUtil fdsUtil;
//    private SubmitInterface submit;
//    
//    private UserBean mUserBean = null;
//    
//    public final static int INTENR_PHONE = 100;      // 手机号
//    public final static int INTENR_NAME = 101;       // 昵称
//    public final static int INTENR_SIGNATURE = 102;  // 个性签名
//    public final static int INTENR_SEX = 103;        // 性别
//    public final static int INTENR_AGE = 104;        // 年龄
//    public final static int INTENR_BIRTHDAY = 105;   // 生日
//    public final static int INTENR_CONSTELL = 106;   // 星座
//    public final static int INTENR_HOMETOWN = 107;   // 家乡
//    public final static int INTENR_LOCUSE = 108;     // 现居住地
//    public final static int INTENR_EMAIL = 109;      // 邮箱
//    public final static int INTENR_BLOOD = 110;      // 血型
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_personal_data);
//        
//        mUserBean = (UserBean) getIntent().getSerializableExtra(UserBean.KEY_USER_BEAN);
//        
//        findView();
//        setListener();
//        setHomePreference();
//        setLocPreference();
//        setData(m_userDTO);
//        submit = new SubmitPostBar();
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
//        
//        String resultHome = getHomePreferencr();
//        if (resultHome != null && !resultHome.equals("")) 
//        {
//            m_userDTO.hometown = resultHome;
//        }
//        
//        String resultLoc = getLocPreferencr();
//        if (resultLoc != null && !resultLoc.equals("")) 
//        {
//            m_userDTO.loplace = resultLoc;
//        }
//        UserManager.getInstance().m_user.copy(m_userDTO);
//        setData(m_userDTO);
//        
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
//    private void setHomePreference() {
//        SharedPreferences sharedPreferences = PersonalDataActivity.this
//                .getSharedPreferences("HomeAddress", MODE_PRIVATE);
//        Editor editor = sharedPreferences.edit();
//        editor.putString("address", m_userDTO.hometown);
//        editor.commit();
//    }
//    
//    private void setLocPreference() {
//        SharedPreferences sharedPreferences = PersonalDataActivity.this
//                .getSharedPreferences("LocAddress", MODE_PRIVATE);
//        Editor editor = sharedPreferences.edit();
//        editor.putString("address", m_userDTO.loplace);
//        editor.commit();
//    }
//    
//    /**
//     * 返回传值
//     * 
//     * @return
//     */
//    private String getHomePreferencr() {
//        String result = null;
//        SharedPreferences share = PersonalDataActivity.this.getSharedPreferences(
//                "HomeAddress", MODE_PRIVATE);
//        result = share.getString("address", "");
//        return result;
//    }
//    /**
//     * 返回传值
//     * 
//     * @return
//     */
//    private String getLocPreferencr() {
//        String result = null;
//        SharedPreferences share = PersonalDataActivity.this.getSharedPreferences(
//                "LocAddress", MODE_PRIVATE);
//        result = share.getString("address", "");
//        return result;
//    }
//
//    
//    /**
//     * 控件初始化方法
//     */
//    private void findView()
//    {
//        m_backBtn = (Button) findViewById(R.id.presonal_data_btn_back);
//        m_iconRl = (RelativeLayout) findViewById(R.id.presonal_data_rl_icon);
//        m_iconImgV = (ImageView) findViewById(R.id.presonal_data_imgv_icon);
//        m_idTv = (TextView) findViewById(R.id.presonal_data_tv_id);
//        m_phoneRl = (RelativeLayout) findViewById(R.id.presonal_data_rl_phone);
//        m_phoneTv = (TextView) findViewById(R.id.presonal_data_tv_phone);
//        m_nameRl = (RelativeLayout) findViewById(R.id.presonal_data_rl_name);
//        m_nameTv = (TextView) findViewById(R.id.presonal_data_tv_name);
//        m_signatureRl = (RelativeLayout) findViewById(R.id.presonal_data_rl_signature);
//        m_signatureTv = (TextView) findViewById(R.id.presonal_data_tv_signature);
//        m_sexRl = (RelativeLayout) findViewById(R.id.presonal_data_rl_sex);
//        m_sexTv = (TextView) findViewById(R.id.presonal_data_tv_sex);
//        m_ageRl = (RelativeLayout) findViewById(R.id.presonal_data_rl_age);
//        m_ageTv = (TextView) findViewById(R.id.presonal_data_tv_age);
//        m_birthdayRl = (RelativeLayout) findViewById(R.id.presonal_data_rl_birthday);
//        m_birthdayTv = (TextView) findViewById(R.id.presonal_data_tv_birthday);
//        m_constellationRl = (RelativeLayout) findViewById(R.id.presonal_data_rl_constellation);
//        m_constellationTv = (TextView) findViewById(R.id.presonal_data_tv_constellation);
//        m_hometownRl = (RelativeLayout) findViewById(R.id.presonal_data_rl_hometown);
//        m_hometownTv = (TextView) findViewById(R.id.presonal_data_tv_hometown);
//        m_locusRl = (RelativeLayout) findViewById(R.id.presonal_data_rl_locus);
//        m_locusTv = (TextView) findViewById(R.id.presonal_data_tv_locus);
//        m_emailRl = (RelativeLayout) findViewById(R.id.presonal_data_rl_email);
//        m_emailTv = (TextView) findViewById(R.id.presonal_data_tv_email);
//        m_bloodGroupRl = (RelativeLayout) findViewById(R.id.presonal_data_rl_blood_group);
//        m_bloodGroupTv = (TextView) findViewById(R.id.presonal_data_tv_blood_group);
//        m_doneBtn = (Button) findViewById(R.id.presonal_data_btn_done);
//    }
//
//    /**
//     * 控件事件监听方法
//     */
//    private void setListener()
//    {
//        m_backBtn.setOnClickListener(this);
//        m_iconRl.setOnClickListener(this);
////        m_phoneRl.setOnClickListener(this);
//        m_nameRl.setOnClickListener(this);
//        m_signatureRl.setOnClickListener(this);
//        m_sexRl.setOnClickListener(this);
//        m_ageRl.setOnClickListener(this);
//        m_birthdayRl.setOnClickListener(this);
//        m_constellationRl.setOnClickListener(this);
//        m_hometownRl.setOnClickListener(this);
//        m_locusRl.setOnClickListener(this);
//        m_emailRl.setOnClickListener(this);
//        m_bloodGroupRl.setOnClickListener(this);
//        m_doneBtn.setOnClickListener(this);
//    }
//    
//    /**
//     * 设置用户数据显示
//     * @param userDTO
//     */
//    private void setData(UserDTO userDTO)
//    {
//        if (userDTO != null && userDTO.userIcon != null && !userDTO.userIcon.equals(""))
//            Picasso.with(this).load(userDTO.userIcon).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(m_iconImgV);
//        m_idTv.setText(userDTO.userName);
//        m_phoneTv.setText(userDTO.userPhone);
//        m_nameTv.setText(userDTO.nickn);
//        m_signatureTv.setText(userDTO.signature);
//        
//        int sex = userDTO.sex;
//        String userSex = "";
//        if (sex == 0)
//        {
//            userSex = "保密";
//        }
//        else if (sex == 1)
//        {
//            userSex = "男";
//        }
//        else if (sex == 2)
//        {
//            userSex = "女";
//        }
//        m_sexTv.setText(userSex);
//        m_ageTv.setText(userDTO.age);
//        m_birthdayTv.setText(userDTO.birthday);
//        m_constellationTv.setText(userDTO.conste);
//        m_hometownTv.setText(userDTO.hometown);
//        m_locusTv.setText(userDTO.loplace);
//        m_emailTv.setText(userDTO.email);
//        m_bloodGroupTv.setText(userDTO.btype);
//    }
//
//    @Override
//    public void onClick(View v)
//    {
//        switch (v.getId())
//        {
//            // 返回按钮
//            case R.id.presonal_data_btn_back:
//            {
//                PersonalDataActivity.this.finish();
//                break;
//            }
//
//            // 头像
//            case R.id.presonal_data_rl_icon:
//            {
////                photoDialogShow();photoPopupIn(this, R.layout.layout_myself_photo_modify_popup);
//                getFdsUtil().photoPopupIn(this, R.layout.layout_personal_data_dialog);
//                break;
//            }
//
//            // 手机号
//            case R.id.presonal_data_rl_phone:
//            {
//                Intent intent = new Intent(PersonalDataActivity.this, ModifyPhoneActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("userDTO", m_userDTO);
//                intent.putExtras(bundle);
//                startActivityForResult(intent, INTENR_PHONE);
//                break;
//            }
//
//            // 称昵
//            case R.id.presonal_data_rl_name:
//            {
//                Intent intent = new Intent(PersonalDataActivity.this, NameActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("userDTO", m_userDTO);
//                intent.putExtras(bundle);
//                startActivityForResult(intent, INTENR_NAME);
//                break;
//            }
//
//            // 个性签名
//            case R.id.presonal_data_rl_signature:
//            {
//                Intent intent = new Intent(PersonalDataActivity.this, SignatureActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("userDTO", m_userDTO);
//                intent.putExtras(bundle);
//                startActivityForResult(intent, INTENR_SIGNATURE);
//                break;
//            }
//
//            // 性别
//            case R.id.presonal_data_rl_sex:
//            {
//                Intent intent = new Intent(PersonalDataActivity.this, SexActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("userDTO", m_userDTO);
//                intent.putExtras(bundle);
//                startActivityForResult(intent, INTENR_SEX);
//                break;
//            }
//
//            // 年龄
//            case R.id.presonal_data_rl_age:
//            {
//                Intent intent = new Intent(PersonalDataActivity.this, AgeActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("userDTO", m_userDTO);
//                intent.putExtras(bundle);
//                startActivityForResult(intent, INTENR_AGE);
//                break;
//            }
//
//            // 生日
//            case R.id.presonal_data_rl_birthday:
//            {
//                Intent intent = new Intent(PersonalDataActivity.this, BirthdayActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("userDTO", m_userDTO);
//                intent.putExtras(bundle);
//                startActivityForResult(intent, INTENR_BIRTHDAY);
//                break;
//            }
//
//            // 星座
//            case R.id.presonal_data_rl_constellation:
//            {
//                Intent intent = new Intent(PersonalDataActivity.this, ConstellationActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("userDTO", m_userDTO);
//                intent.putExtras(bundle);
//                startActivityForResult(intent, INTENR_CONSTELL);
//                break;
//            }
//
//            // 家乡
//            case R.id.presonal_data_rl_hometown:
//            {
//                Intent intent = new Intent(PersonalDataActivity.this, PrivinceActivity.class);
//                intent.putExtra("index", 0);
//                startActivity(intent);
////                Bundle bundle = new Bundle();
////                bundle.putSerializable("userDTO", m_userDTO);
////                intent.putExtras(bundle);
////                startActivityForResult(intent, INTENR_HOMETOWN);
//                break;
//            }
//
//            // 所在地
//            case R.id.presonal_data_rl_locus:
//            {
//                Intent intent = new Intent(PersonalDataActivity.this, PrivinceActivity.class);
//                intent.putExtra("index", 1);
//                startActivity(intent);
////                Bundle bundle = new Bundle();
////                bundle.putSerializable("userDTO", m_userDTO);
////                intent.putExtras(bundle);
////                startActivityForResult(intent, INTENR_LOCUSE);
//                break;
//            }
//
//            // 邮箱
//            case R.id.presonal_data_rl_email:
//            {
//                Intent intent = new Intent(PersonalDataActivity.this, EmailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("userDTO", m_userDTO);
//                intent.putExtras(bundle);
//                startActivityForResult(intent, INTENR_EMAIL);
//                break;
//            }
//
//            // 血型
//            case R.id.presonal_data_rl_blood_group:
//            {
//                Intent intent = new Intent(PersonalDataActivity.this, BloodGroupActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("userDTO", m_userDTO);
//                intent.putExtras(bundle);
//                startActivityForResult(intent, INTENR_BLOOD);
//                break;
//            }
//
//            // 名片预览按钮
//            case R.id.presonal_data_btn_done:
//            {
//                Intent intent = new Intent(PersonalDataActivity.this, MyCardActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("userDTO", m_userDTO);
//                intent.putExtras(bundle);
//                startActivity(intent);
//                break;
//            }
//
//            default:
//            {
//                break;
//            }
//        }
//    }
//    
//    /**
//     * 显示Dialog对话框
//     * @param typeNum
//     * @param list
//     * @param textView
//     * @param imageView
//     */
//    private void photoDialogShow()
//    {
//        final Dialog dialog_more = new Dialog(PersonalDataActivity.this, R.style.birthday_time_dialog);
//        dialog_more.setCancelable(true);
//        dialog_more.setCanceledOnTouchOutside(true);
//        
//        LayoutInflater localLayoutInflater = LayoutInflater.from(this);
//        View localView1 = localLayoutInflater.inflate(R.layout.layout_personal_data_dialog, null);
//        // dialog_more.getWindow().setWindowAnimations(R.style.in_down_out_up);
//        dialog_more.setContentView(localView1);
//        
////      View vShow = (View) localView1.findViewById(R.id.log_off_v_show);
//        TextView photographTv = (TextView) localView1.findViewById(R.id.personal_data_dialog_tv_photograph);
//        TextView albumTv = (TextView) localView1.findViewById(R.id.personal_data_dialog_tv_album);
//        TextView cancelTv = (TextView) localView1.findViewById(R.id.personal_data_dialog_tv_cancel);
//        
//        photographTv.setOnClickListener(new View.OnClickListener() 
//        {
//            @Override
//            public void onClick(View v) 
//            {
//                dialog_more.dismiss();
//            }
//        });
//        
//        albumTv.setOnClickListener(new View.OnClickListener() 
//        {
//            @Override
//            public void onClick(View v) 
//            {
//                dialog_more.dismiss();
//            }
//        });
//        
//        cancelTv.setOnClickListener(new View.OnClickListener() 
//        {
//            @Override
//            public void onClick(View v) 
//            {
//                dialog_more.dismiss();
//            }
//        });
//        
//        WindowManager.LayoutParams lp = dialog_more.getWindow().getAttributes();
//        lp.width = LayoutParams.MATCH_PARENT;
//        lp.height = LayoutParams.WRAP_CONTENT;
//        lp.gravity = Gravity.BOTTOM;
//        Window window = dialog_more.getWindow();
//        window.setGravity(Gravity.BOTTOM);//在底部弹出
//        window.setWindowAnimations(R.style.birthday_time_dialog_animation);
//        dialog_more.show();
//    }
//
//    
//    String path = "";
//    String end = "";
//    String imgPath = "";
//    String imgThumPath = "";
//    private FDSUtil getFdsUtil() {
//        if (fdsUtil == null) {
//            fdsUtil = new FDSUtil(PersonalDataActivity.this, new onPhotoReceiverListeners() {
//
//                @Override
//                public void onReceive(File file) {
//                    fdsUtil.progressDialogDismiss();
//                    String imagePath = file.getAbsolutePath();
//                    String imgName = null;
//                    
//                    path = file.getAbsolutePath();
//                    end = Util.getFileName(path);
//                    
//                    try
//                    {
//                        imgName = fdsUtil.getFileName(imagePath);
//                    }
//                    catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//                    Bitmap bitmap = fdsUtil.loadImgThumbnail(PersonalDataActivity.this, imgName, MediaStore.Images.Thumbnails.MINI_KIND);
//                    m_iconImgV.setImageBitmap(bitmap);
//                    submit.sendComment();
////                    if (ib_add_image_position >= listImges.size())
////                        listImges.add(imagePath);
////                    else {
////                        listImges.add(ib_add_image_position, imagePath);
////                        listImges.remove(ib_add_image_position + 1);
////                    }
////                    adapter.notifyDataSetChanged();
//                }
//            });
//        }
//        return fdsUtil;
//    }
//    
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (fdsUtil != null)
//            fdsUtil.onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//        
//        switch (resultCode)
//        {
//            case RESULT_OK:
//            {
//                if(requestCode == INTENR_PHONE)
//                {
//                    m_userDTO.userPhone = data.getStringExtra("userPhone");
//                }
//                else if (requestCode == INTENR_NAME)
//                {
//                    m_userDTO.nickn = data.getStringExtra("userName");
//                }
//                else if (requestCode == INTENR_SIGNATURE)
//                {
//                    m_userDTO.signature = data.getStringExtra("signature");
//                }
//                else if (requestCode == INTENR_SEX)
//                {
//                    m_userDTO.sex = data.getIntExtra("sex", 0);
//                }
//                else if (requestCode == INTENR_AGE)
//                {
//                    m_userDTO.age = data.getStringExtra("age");
//                }
//                else if (requestCode == INTENR_BIRTHDAY)
//                {
//                    m_userDTO.birthday = data.getStringExtra("birthday");
//                }
//                else if (requestCode == INTENR_CONSTELL)
//                {
//                    m_userDTO.conste = data.getStringExtra("constell");
//                }
//                else if (requestCode == INTENR_HOMETOWN)
//                {
//                    m_userDTO.hometown = data.getStringExtra("hometown");
//                }
//                else if (requestCode == INTENR_LOCUSE)
//                {
//                    m_userDTO.loplace = data.getStringExtra("loplace");
//                }
//                else if (requestCode == INTENR_EMAIL)
//                {
//                    m_userDTO.email = data.getStringExtra("email");
//                }
//                else if (requestCode == INTENR_BLOOD)
//                {
//                    m_userDTO.btype = data.getStringExtra("bloodGroup");
//                }
//                UserManager.getInstance().m_user.copy(m_userDTO);
//                setData(m_userDTO);
//            }
//        }
//        
//    }
//    
//    class SubmitPostBar implements SubmitInterface
//    {
//
//        public SubmitPostBar()
//        {
//        }
//
//        @Override
//        public void sendComment()
//        {
//            hideSoftInputFromWindow();
//            if (UserManager.getInstance().m_userState != UserState.UserState_LOGINED)
//            {
//                WindowUtil.getInstance().dialogLoginShow(PersonalDataActivity.this);
//                return;
//            }
//            WindowUtil.getInstance().progressDialogShow(PersonalDataActivity.this, "正在上传图片");
//
//            getFdsUtil().uploadPic(path, UserManager.getInstance().m_user.userId, PathManager.FILE_UPDATE_TAG_USERINFO, end, new onReceiverListeners()
//            {
//
//                @Override
//                public void onReceive(String isSuccess, String bigPath, String smallPath)
//                {
//                    if (isSuccess != null && isSuccess.equalsIgnoreCase("true"))
//                    {
//                        imgPath = bigPath;
//                        imgThumPath = smallPath;
//                    }
//                    else
//                    {
//                        Toast.makeText(PersonalDataActivity.this, "上传图片失败！", Toast.LENGTH_LONG).show();
//                        WindowUtil.getInstance().DismissAllDialog();
//                        return;
//                    }
//                    WindowUtil.getInstance().progressDialogShow(PersonalDataActivity.this, "正在上传图片");
//                    send();
//                }
//
//            });
//            WindowUtil.getInstance().progressDialogShow(PersonalDataActivity.this, "正在上传头像");
//            send();
//        }
//
//        private void send()
//        {
//
//            messageManager.modifyPersonalInfo("photo", imgThumPath);
//        }
//
//        @Override
//        public void sendReply(FDSState state, String ID)
//        {
//            WindowUtil.getInstance().DismissAllDialog();
//            if (state != null && state.result.equals("OK"))
//            {
//                finish();
//            }
//
//        }
//    }
//    
//    public static String getTheUUID()
//    {
//        String uniqueid;
//        uniqueid = UUID.randomUUID().toString();
//        return uniqueid;
//    }
//    
//    @Override
//    public void modifyPersonalInfoCB(Reulst result_state)
//    {
//        super.modifyPersonalInfoCB(result_state);
//        WindowUtil.getInstance().DismissAllDialog();
//        
//        if (result_state.resultCode == ReturnCode.RETURNCODE_OK)
//        {
//            Toast.makeText(this, "修改成功！", Toast.LENGTH_LONG).show();
//            m_userDTO.userIcon = imgThumPath;
//            UserManager.getInstance().m_user.copy(m_userDTO);
//            setData(m_userDTO);
//        }
//        else
//        {
//            Toast.makeText(this, "修改失败！", Toast.LENGTH_LONG).show();
//        }
//    }
//}
