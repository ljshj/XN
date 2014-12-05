package com.bgood.xn.ui.user.info;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bgood.xn.R;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.utils.ConfigUtil;
import com.bgood.xn.utils.ToolUtils;
import com.bgood.xn.view.BToast;
import com.bgood.xn.view.dialog.BottomDialog;
import com.bgood.xn.widget.TitleBar;
import com.bgood.xn.widget.wheel.MyDateTimePickerDialog;
import com.bgood.xn.widget.wheel.MyDateTimePickerDialog.OnDateTimeSetListener;
import com.bgood.xn.widget.wheel.NumericWheelAdapter;
import com.bgood.xn.widget.wheel.OnWheelChangedListener;
import com.bgood.xn.widget.wheel.WheelView;

/**
 * 生日编写页面
 */
public class BirthdayActivity extends BaseActivity implements TaskListenerWithState
{
    private TextView m_dayTv = null;  // 生日填写
    
    private static int START_YEAR = 1949,END_YEAR = 2014;
    private UserInfoBean mUserBean = null;
    private String m_birthday = "";
    private TitleBar titleBar = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_birthday);
        titleBar = new TitleBar(mActivity);
        titleBar.initAllBar("生日");
        mUserBean = (UserInfoBean) getIntent().getSerializableExtra(UserInfoBean.KEY_USER_BEAN);
        m_dayTv = (TextView) findViewById(R.id.birthday_tv_day);
        m_dayTv.setText(mUserBean.birthday);
        setListener();
    }
    
    /**
     * 控件事件监听方法
     */
    private void setListener()
    {
        m_dayTv.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
            	new MyDateTimePickerDialog(mActivity,  TextUtils.isEmpty(m_dayTv.getText().toString())?ConfigUtil.getDate(new Date()):m_dayTv.getText().toString(), new OnDateTimeSetListener() {
					@Override
					public void onDateTimeSet(int year, int monthOfYear, int dayOfMonth) {
						m_dayTv.setText(year+"-"+ToolUtils.formateTenNum(monthOfYear)+"-"+ToolUtils.formateTenNum(dayOfMonth));	
					}
				}).show();
            }
        });
        
        // 确定按钮
        titleBar.rightBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
            	m_birthday = m_dayTv.getText().toString();
                if (!TextUtils.isEmpty(m_birthday))
                {
                    loadData();
                }
                else
                {
                   BToast.show(mActivity, "请输入您的生日");
                    return;
                }
                
            }
        });
    }
    
    /**
     * 加载数据方法
     * @param value 修改内容
     */
    private void loadData()
    {
		UserCenterRequest.getInstance().requestUpdatePerson(this, mActivity, "birthday", m_birthday);
    }


	@Override
	public void onTaskOver(HttpRequestInfo request, HttpResponseInfo info) {
		if(info.getState() == HttpTaskState.STATE_OK){
			BaseNetWork bNetWork = info.getmBaseNetWork();
			if(bNetWork.getReturnCode() == ReturnCode.RETURNCODE_OK){
				BToast.show(mActivity, "修改成功");
				final UserInfoBean ufb = BGApp.mUserBean;
				ufb.birthday = m_birthday;
				BGApp.mUserBean = ufb;
				finish();
			}else{
				BToast.show(mActivity, "修改失败");
			}
		}
	}
}
