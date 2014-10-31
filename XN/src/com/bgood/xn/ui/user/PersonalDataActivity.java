package com.bgood.xn.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.ui.user.info.AgeActivity;
import com.bgood.xn.ui.user.info.BirthdayActivity;
import com.bgood.xn.ui.user.info.BloodGroupActivity;
import com.bgood.xn.ui.user.info.ConstellationActivity;
import com.bgood.xn.ui.user.info.EmailActivity;
import com.bgood.xn.ui.user.info.MyCardActivity;
import com.bgood.xn.ui.user.info.NameActivity;
import com.bgood.xn.ui.user.info.PrivinceActivity;
import com.bgood.xn.ui.user.info.SexActivity;
import com.bgood.xn.ui.user.info.SignatureActivity;
import com.squareup.picasso.Picasso;


/**
 * 个人资料页面
 */
public class PersonalDataActivity extends BaseActivity implements OnClickListener
{
    private RelativeLayout m_iconRl          = null; // 头像布局
    private ImageView      m_iconImgV        = null; // 头像
    private TextView       m_idTv            = null; // ID号
    private RelativeLayout m_phoneRl         = null; // 手机号布局
    private TextView       m_phoneTv         = null; // 手机号
    private RelativeLayout m_nameRl          = null; // 称昵布局
    private TextView       m_nameTv          = null; // 称昵
    private RelativeLayout m_signatureRl     = null; // 个性签名布局
    private TextView       m_signatureTv     = null; // 个性签名
    private RelativeLayout m_sexRl           = null; // 性别布局
    private TextView       m_sexTv           = null; // 性别
    private RelativeLayout m_ageRl           = null; // 年龄布局
    private TextView       m_ageTv           = null; // 年龄
    private RelativeLayout m_birthdayRl      = null; // 生日布局
    private TextView       m_birthdayTv      = null; // 生日
    private RelativeLayout m_constellationRl = null; // 星座布局
    private TextView       m_constellationTv = null; // 星座
    private RelativeLayout m_hometownRl      = null; // 家乡地址布局
    private TextView       m_hometownTv      = null; // 家乡地址
    private RelativeLayout m_locusRl         = null; // 现所在地布局
    private TextView       m_locusTv         = null; // 所在地
    private RelativeLayout m_emailRl         = null; // 邮箱布局
    private TextView       m_emailTv         = null; // 邮箱
    private RelativeLayout m_bloodGroupRl    = null; // 血型布局
    private TextView       m_bloodGroupTv    = null; // 血型
    private Button         m_doneBtn         = null; // 立即预览我的名片按钮
    
    private UserInfoBean mUserBean = null;
    
    public final static int INTENR_PHONE = 100;      // 手机号
    public final static int INTENR_NAME = 101;       // 昵称
    public final static int INTENR_SIGNATURE = 102;  // 个性签名
    public final static int INTENR_SEX = 103;        // 性别
    public final static int INTENR_AGE = 104;        // 年龄
    public final static int INTENR_BIRTHDAY = 105;   // 生日
    public final static int INTENR_CONSTELL = 106;   // 星座
    public final static int INTENR_HOMETOWN = 107;   // 家乡
    public final static int INTENR_LOCUSE = 108;     // 现居住地
    public final static int INTENR_EMAIL = 109;      // 邮箱
    public final static int INTENR_BLOOD = 110;      // 血型

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_personal_data);
        mUserBean = (UserInfoBean) getIntent().getSerializableExtra(UserInfoBean.KEY_USER_BEAN);
        findView();
        setListener();
        setData(mUserBean);
    }

    
    /**
     * 控件初始化方法
     */
    private void findView()
    {
        m_iconRl = (RelativeLayout) findViewById(R.id.presonal_data_rl_icon);
        m_iconImgV = (ImageView) findViewById(R.id.presonal_data_imgv_icon);
        m_idTv = (TextView) findViewById(R.id.presonal_data_tv_id);
        m_phoneRl = (RelativeLayout) findViewById(R.id.presonal_data_rl_phone);
        m_phoneTv = (TextView) findViewById(R.id.presonal_data_tv_phone);
        m_nameRl = (RelativeLayout) findViewById(R.id.presonal_data_rl_name);
        m_nameTv = (TextView) findViewById(R.id.presonal_data_tv_name);
        m_signatureRl = (RelativeLayout) findViewById(R.id.presonal_data_rl_signature);
        m_signatureTv = (TextView) findViewById(R.id.presonal_data_tv_signature);
        m_sexRl = (RelativeLayout) findViewById(R.id.presonal_data_rl_sex);
        m_sexTv = (TextView) findViewById(R.id.presonal_data_tv_sex);
        m_ageRl = (RelativeLayout) findViewById(R.id.presonal_data_rl_age);
        m_ageTv = (TextView) findViewById(R.id.presonal_data_tv_age);
        m_birthdayRl = (RelativeLayout) findViewById(R.id.presonal_data_rl_birthday);
        m_birthdayTv = (TextView) findViewById(R.id.presonal_data_tv_birthday);
        m_constellationRl = (RelativeLayout) findViewById(R.id.presonal_data_rl_constellation);
        m_constellationTv = (TextView) findViewById(R.id.presonal_data_tv_constellation);
        m_hometownRl = (RelativeLayout) findViewById(R.id.presonal_data_rl_hometown);
        m_hometownTv = (TextView) findViewById(R.id.presonal_data_tv_hometown);
        m_locusRl = (RelativeLayout) findViewById(R.id.presonal_data_rl_locus);
        m_locusTv = (TextView) findViewById(R.id.presonal_data_tv_locus);
        m_emailRl = (RelativeLayout) findViewById(R.id.presonal_data_rl_email);
        m_emailTv = (TextView) findViewById(R.id.presonal_data_tv_email);
        m_bloodGroupRl = (RelativeLayout) findViewById(R.id.presonal_data_rl_blood_group);
        m_bloodGroupTv = (TextView) findViewById(R.id.presonal_data_tv_blood_group);
        m_doneBtn = (Button) findViewById(R.id.presonal_data_btn_done);
    }

    /**
     * 控件事件监听方法
     */
    private void setListener()
    {
        m_iconRl.setOnClickListener(this);
        m_nameRl.setOnClickListener(this);
        m_signatureRl.setOnClickListener(this);
        m_sexRl.setOnClickListener(this);
        m_ageRl.setOnClickListener(this);
        m_birthdayRl.setOnClickListener(this);
        m_constellationRl.setOnClickListener(this);
        m_hometownRl.setOnClickListener(this);
        m_locusRl.setOnClickListener(this);
        m_emailRl.setOnClickListener(this);
        m_bloodGroupRl.setOnClickListener(this);
        m_doneBtn.setOnClickListener(this);
    }
    
    /**
     * 设置用户数据显示
     * @param userDTO
     */
    private void setData(UserInfoBean userDTO)
    {
        if (TextUtils.isEmpty(userDTO.photo)){
            Picasso.with(this).load(userDTO.photo).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).into(m_iconImgV);
        }
        m_idTv.setText(userDTO.username);
        m_phoneTv.setText(userDTO.phonenumber);
        m_nameTv.setText(userDTO.nickn);
        m_signatureTv.setText(userDTO.signature);
        
        int sex = userDTO.sex;
        String userSex = "";
        if (sex == 0)
        {
            userSex = "保密";
        }
        else if (sex == 1)
        {
            userSex = "男";
        }
        else if (sex == 2)
        {
            userSex = "女";
        }
        m_sexTv.setText(userSex);
        m_ageTv.setText(userDTO.age);
        m_birthdayTv.setText(userDTO.birthday);
        m_constellationTv.setText(userDTO.conste);
        m_hometownTv.setText(userDTO.hometown);
        m_locusTv.setText(userDTO.loplace);
        m_emailTv.setText(userDTO.email);
        m_bloodGroupTv.setText(userDTO.btype);
    }

    @Override
    public void onClick(View v)
    {
    	
    	Intent intent = null;
    	
        switch (v.getId())
        {
            // 头像
            case R.id.presonal_data_rl_icon:
                break;
            // 称昵
            case R.id.presonal_data_rl_name:
                intent = new Intent(PersonalDataActivity.this, NameActivity.class);
                intent.putExtra(UserInfoBean.KEY_USER_BEAN, mUserBean);
                startActivityForResult(intent, INTENR_NAME);
                break;
            // 个性签名
            case R.id.presonal_data_rl_signature:
                intent = new Intent(PersonalDataActivity.this, SignatureActivity.class);
                intent.putExtra(UserInfoBean.KEY_USER_BEAN, mUserBean);
                startActivityForResult(intent, INTENR_SIGNATURE);
                break;
            // 性别
            case R.id.presonal_data_rl_sex:
                intent = new Intent(PersonalDataActivity.this, SexActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(UserInfoBean.KEY_USER_BEAN, mUserBean);
                intent.putExtras(bundle);
                startActivityForResult(intent, INTENR_SEX);
                break;

            // 年龄
            case R.id.presonal_data_rl_age:
                intent = new Intent(PersonalDataActivity.this, AgeActivity.class);
                intent.putExtra(UserInfoBean.KEY_USER_BEAN, mUserBean);
                startActivityForResult(intent, INTENR_AGE);
                break;

            // 生日
            case R.id.presonal_data_rl_birthday:
                intent = new Intent(PersonalDataActivity.this, BirthdayActivity.class);
                intent.putExtra(UserInfoBean.KEY_USER_BEAN, mUserBean);
                startActivityForResult(intent, INTENR_BIRTHDAY);
                break;

            // 星座
            case R.id.presonal_data_rl_constellation:
                intent = new Intent(PersonalDataActivity.this, ConstellationActivity.class);
                intent.putExtra(UserInfoBean.KEY_USER_BEAN, mUserBean);
                startActivityForResult(intent, INTENR_CONSTELL);
                break;

            // 家乡
            case R.id.presonal_data_rl_hometown:
                intent = new Intent(PersonalDataActivity.this, PrivinceActivity.class);
                intent.putExtra("index", 0);
                startActivity(intent);
                break;

            // 所在地
            case R.id.presonal_data_rl_locus:
                intent = new Intent(PersonalDataActivity.this, PrivinceActivity.class);
                intent.putExtra("index", 1);
                startActivity(intent);
                break;

            // 邮箱
            case R.id.presonal_data_rl_email:
                intent = new Intent(PersonalDataActivity.this, EmailActivity.class);
                intent.putExtra(UserInfoBean.KEY_USER_BEAN, mUserBean);
                startActivityForResult(intent, INTENR_EMAIL);
                break;

            // 血型
            case R.id.presonal_data_rl_blood_group:
                intent = new Intent(PersonalDataActivity.this, BloodGroupActivity.class);
                intent.putExtra(UserInfoBean.KEY_USER_BEAN, mUserBean);
                startActivityForResult(intent, INTENR_BLOOD);
                break;
            // 名片预览按钮
            case R.id.presonal_data_btn_done:
                intent = new Intent(PersonalDataActivity.this, MyCardActivity.class);
                intent.putExtra(UserInfoBean.KEY_USER_BEAN, mUserBean);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
 

    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if(resultCode != RESULT_OK){
        	return;
        }
			switch (requestCode)
			{	
			case INTENR_PHONE:
				 mUserBean.phonenumber = data.getStringExtra("userPhone");
				break;
			case INTENR_NAME:
				 mUserBean.nickn = data.getStringExtra("userName");
				break;
			case INTENR_SIGNATURE:
				 mUserBean.signature = data.getStringExtra("signature");
				break;
			case INTENR_AGE:
				 mUserBean.age = data.getStringExtra("age");
				break;
			case INTENR_SEX:
				  mUserBean.sex = data.getIntExtra("sex", 0);
				break;
			case INTENR_BIRTHDAY:
				 mUserBean.birthday = data.getStringExtra("birthday");
				break;
			case INTENR_CONSTELL:
				 mUserBean.conste = data.getStringExtra("constell");
				break;
			case INTENR_HOMETOWN:
				  mUserBean.hometown = data.getStringExtra("hometown");
				break;
			case INTENR_LOCUSE:
				 mUserBean.loplace = data.getStringExtra("loplace");
				break;
			case INTENR_EMAIL:
				 mUserBean.email = data.getStringExtra("email");
				break;
			case INTENR_BLOOD:
				mUserBean.btype = data.getStringExtra("bloodGroup");
				break;
			    }
			setData(mUserBean);
    	}
}
