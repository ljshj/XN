package com.bgood.xn.ui.user;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.request.FileRequest;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.system.Const;
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
import com.bgood.xn.utils.pic.CropImageActivity;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.dialog.BottomDialog;
import com.bgood.xn.widget.TitleBar;
import com.squareup.picasso.Picasso;


/**
 * 个人资料页面
 */
public class PersonalDataActivity extends BaseActivity implements OnClickListener,TaskListenerWithState
{
	
	/** 从相册选择照片 **/
	private static final int FLAG_CHOOSE_FROM_IMGS = 100;
	/** 从手机获取照片 **/
	private static final int FLAG_CHOOSE_FROM_CAMERA = FLAG_CHOOSE_FROM_IMGS + 1;
	/** 选择完过后 **/
	private static final int FLAG_MODIFY_FINISH = FLAG_CHOOSE_FROM_CAMERA + 1;

	private File tempFile = null; // 文件
	private Bitmap mBitmapUploaded;
	
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_personal_data);
        (new TitleBar(mActivity)).initTitleBar("编辑个人资料");
        findView();
        setListener();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	mUserBean = BGApp.mUserBean;
    	setData(BGApp.mUserBean);
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
            Picasso.with(this).load(userDTO.photo).placeholder(R.drawable.icon_default).error(R.drawable.icon_default).into(m_iconImgV);
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
            	showPicDialog();
                break;
            // 称昵
            case R.id.presonal_data_rl_name:
                intent = new Intent(PersonalDataActivity.this, NameActivity.class);
                intent.putExtra(UserInfoBean.KEY_USER_BEAN, mUserBean);
                startActivity(intent);
                break;
            // 个性签名
            case R.id.presonal_data_rl_signature:
                intent = new Intent(PersonalDataActivity.this, SignatureActivity.class);
                intent.putExtra(UserInfoBean.KEY_USER_BEAN, mUserBean);
                startActivity(intent);
                break;
            // 性别
            case R.id.presonal_data_rl_sex:
                intent = new Intent(PersonalDataActivity.this, SexActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(UserInfoBean.KEY_USER_BEAN, mUserBean);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            // 年龄
            case R.id.presonal_data_rl_age:
                intent = new Intent(PersonalDataActivity.this, AgeActivity.class);
                intent.putExtra(UserInfoBean.KEY_USER_BEAN, mUserBean);
                startActivity(intent);
                break;

            // 生日
            case R.id.presonal_data_rl_birthday:
                intent = new Intent(PersonalDataActivity.this, BirthdayActivity.class);
                intent.putExtra(UserInfoBean.KEY_USER_BEAN, mUserBean);
                startActivity(intent);
                break;

            // 星座
            case R.id.presonal_data_rl_constellation:
                intent = new Intent(PersonalDataActivity.this, ConstellationActivity.class);
                intent.putExtra(UserInfoBean.KEY_USER_BEAN, mUserBean);
                startActivity(intent);
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
                startActivity(intent);
                break;

            // 血型
            case R.id.presonal_data_rl_blood_group:
                intent = new Intent(PersonalDataActivity.this, BloodGroupActivity.class);
                intent.putExtra(UserInfoBean.KEY_USER_BEAN, mUserBean);
                startActivity(intent);
                break;
            // 名片预览按钮
            case R.id.presonal_data_btn_done:
                intent = new Intent(PersonalDataActivity.this, MyCardActivity.class);
                intent.putExtra(UserInfoBean.KEY_USER_BEAN, mUserBean);
                startActivity(intent);
                break;
            case R.id.btn_take_photo:
            	dialog.dismiss();
            	doTakeCamera();
            	break;
            case R.id.btn_select_photo:
            	dialog.dismiss();
            	doSelectPhoto();
            	break;
            case R.id.btn_cancel:
            	dialog.dismiss();
            	break;
            default:
                break;
        }
    }
    
    private BottomDialog dialog = null;
    private void showPicDialog() {
		if(null == dialog){
			dialog = new BottomDialog(mActivity);
		}
		View v = inflater.inflate(R.layout.item_get_pic, null);
		v.findViewById(R.id.btn_take_photo).setOnClickListener(this);
		v.findViewById(R.id.btn_select_photo).setOnClickListener(this);
		v.findViewById(R.id.btn_cancel).setOnClickListener(this);
		dialog.setvChild(v);
		dialog.show();
	}    
    
	/**
	 * 
	 * @todo 照相
	 * @author liuzenglong163@gmail.com
	 */
	private void doTakeCamera() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				tempFile = new File(Const.CID_IMG_STRING_PATH);
				Uri u = Uri.fromFile(tempFile);
				intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
				startActivityForResult(intent, FLAG_CHOOSE_FROM_CAMERA);
			} catch (ActivityNotFoundException e) {
			}
		} else {
			BToast.show(mActivity, "没有SD卡");
		}
	}
    
	/**
	 * 
	 * @todo 从相册选择照片
	 * @author liuzenglong163@gmail.com
	 */
	private void doSelectPhoto() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, FLAG_CHOOSE_FROM_IMGS);
	}
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK)
			return;
		if (requestCode == FLAG_CHOOSE_FROM_IMGS) {
			if (data != null) {
				Uri uri = data.getData();
				if (!TextUtils.isEmpty(uri.getAuthority())) {
					Cursor cursor = getContentResolver().query(uri,new String[] { MediaStore.Images.Media.DATA },null, null, null);
					if (null == cursor) {
						BToast.show(mActivity, "图片没有找到");
						return;
					}
					cursor.moveToFirst();
					String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
					cursor.close();
					Intent intent = new Intent(this, CropImageActivity.class);
					intent.putExtra("path", path);
					startActivityForResult(intent, FLAG_MODIFY_FINISH);
				} else {
					Intent intent = new Intent(this, CropImageActivity.class);
					intent.putExtra("path", uri.getPath());
					startActivityForResult(intent, FLAG_MODIFY_FINISH);
				}
			}
		} else if (requestCode == FLAG_CHOOSE_FROM_CAMERA) {
			if (tempFile == null) {
				BToast.show(mActivity, "拍照出错，请重拍");
				return;
			}
			Intent intent = new Intent(this, CropImageActivity.class);
			intent.putExtra("path", tempFile.getAbsolutePath());
			startActivityForResult(intent, FLAG_MODIFY_FINISH);
		} else if (requestCode == FLAG_MODIFY_FINISH) {
			if (data != null) {
				final String path = data.getStringExtra("path");
				tempFile = new File(path);
				mBitmapUploaded = BitmapFactory.decodeFile(path);
				m_iconImgV.setImageBitmap(mBitmapUploaded);
				FileRequest.getInstance().requestUpLoadFile(PersonalDataActivity.this,mActivity, tempFile, String.valueOf(BGApp.mLoginBean.userid), "userInfo", "png");
			}
		}
	}

	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			switch (bNetWork.getMessageType()) {
			case 820002:
				BToast.show(mActivity, bNetWork.getReturnCode() ==  ReturnCode.RETURNCODE_OK?"头像修改成功":"头像修改失败");
				break;					
			default:	//因为上传图片 没有设置messageType
				JSONObject object = bNetWork.getBody();
				String status = object.optString("success");
				if(status.equalsIgnoreCase("true")){
					mUserBean.photo = object.optString("smallurl");
					BGApp.mUserBean = mUserBean;
					UserCenterRequest.getInstance().requestUpdatePerson(this, mActivity, "photo", mUserBean.photo);
					//BToast.show(mActivity, "图片上传成功");
				}else{
					BToast.show(mActivity, "图片上传失败");
				}
				break;
			}}
			
		
	}

}
