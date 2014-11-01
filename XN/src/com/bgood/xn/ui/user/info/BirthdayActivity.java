package com.bgood.xn.ui.user.info;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.bean.UserInfoBean;
import com.bgood.xn.network.BaseNetWork.ReturnCode;
import com.bgood.xn.network.HttpRequestAsyncTask.TaskListenerWithState;
import com.bgood.xn.network.HttpResponseInfo.HttpTaskState;
import com.bgood.xn.network.BaseNetWork;
import com.bgood.xn.network.HttpRequestInfo;
import com.bgood.xn.network.HttpResponseInfo;
import com.bgood.xn.network.request.UserCenterRequest;
import com.bgood.xn.system.BGApp;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.view.BToast;
import com.bgood.xn.widget.TitleBar;
import com.bgood.xn.widget.wheelview.OnWheelChangedListener;
import com.bgood.xn.widget.wheelview.WheelView;
import com.bgood.xn.widget.wheelview.adapters.NumericWheelAdapter;

/**
 * 生日编写页面
 */
public class BirthdayActivity extends BaseActivity implements TaskListenerWithState
{
    private TextView m_dayTv = null;  // 生日填写
    
    private static int START_YEAR = 1900, END_YEAR = 2100;
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
                timeDialogShow();
            }
        });
        
        // 确定按钮
        titleBar.rightBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
            	m_birthday = m_dayTv.getText().toString();
                if (TextUtils.isEmpty(m_birthday))
                {
                    loadData();
                }
                else
                {
                    Toast.makeText(BirthdayActivity.this, "生日不能为空！", Toast.LENGTH_LONG).show();
                    return;
                }
                
            }
        });
    }
    
    /**
     * 显示Dialog对话框
     * @param typeNum
     * @param list
     * @param textView
     * @param imageView
     */
    private void timeDialogShow()
    {
        final Dialog dialog_more = new Dialog(BirthdayActivity.this, R.style.birthday_time_dialog);
        dialog_more.setCancelable(true);
        dialog_more.setCanceledOnTouchOutside(true);
        
        LayoutInflater localLayoutInflater = LayoutInflater.from(this);
        View localView1 = localLayoutInflater.inflate(R.layout.layout_birthday_dialog, null);
        dialog_more.setContentView(localView1);
        
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
        String[] months_little = { "4", "6", "9", "11" };

        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);

        // 年
        final WheelView wv_year = (WheelView) localView1.findViewById(R.id.birthday_dialog_wv_year);
        wv_year.setViewAdapter(new NumericWheelAdapter(BirthdayActivity.this, START_YEAR, END_YEAR));// 设置"年"的显示数据
        wv_year.setCyclic(true);// 可循环滚动
        wv_year.setLabel("年");// 添加文字
        wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据

        // 月
        final WheelView wv_month = (WheelView) localView1.findViewById(R.id.birthday_dialog_wv_month);
        wv_month.setViewAdapter(new NumericWheelAdapter(BirthdayActivity.this, 1, 12));
        wv_month.setCyclic(true);
        wv_month.setLabel("月");
        wv_month.setCurrentItem(month);

        // 日
        final WheelView wv_day = (WheelView) localView1.findViewById(R.id.birthday_dialog_wv_day);
        wv_day.setCyclic(true);
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (list_big.contains(String.valueOf(month + 1))) {
            wv_day.setViewAdapter(new NumericWheelAdapter(BirthdayActivity.this, 1, 31));
        } else if (list_little.contains(String.valueOf(month + 1))) {
            wv_day.setViewAdapter(new NumericWheelAdapter(BirthdayActivity.this, 1, 30));
        } else {
            // 闰年
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
                wv_day.setViewAdapter(new NumericWheelAdapter(BirthdayActivity.this, 1, 29));
            else
                wv_day.setViewAdapter(new NumericWheelAdapter(BirthdayActivity.this, 1, 28));
        }
        wv_day.setLabel("日");
        wv_day.setCurrentItem(day - 1);

        // 添加"年"监听
        OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int year_num = newValue + START_YEAR;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big.contains(String
                        .valueOf(wv_month.getCurrentItem() + 1))) {
                    wv_day.setViewAdapter(new NumericWheelAdapter(BirthdayActivity.this, 1, 31));
                } else if (list_little.contains(String.valueOf(wv_month
                        .getCurrentItem() + 1))) {
                    wv_day.setViewAdapter(new NumericWheelAdapter(BirthdayActivity.this, 1, 30));
                } else {
                    if ((year_num % 4 == 0 && year_num % 100 != 0)
                            || year_num % 400 == 0)
                        wv_day.setViewAdapter(new NumericWheelAdapter(BirthdayActivity.this, 1, 29));
                    else
                        wv_day.setViewAdapter(new NumericWheelAdapter(BirthdayActivity.this, 1, 28));
                }
            }
        };
        // 添加"月"监听
        OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int month_num = newValue + 1;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big.contains(String.valueOf(month_num))) {
                    wv_day.setViewAdapter(new NumericWheelAdapter(BirthdayActivity.this, 1, 31));
                } else if (list_little.contains(String.valueOf(month_num))) {
                    wv_day.setViewAdapter(new NumericWheelAdapter(BirthdayActivity.this, 1, 30));
                } else {
                    if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
                            .getCurrentItem() + START_YEAR) % 100 != 0)
                            || (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
                        wv_day.setViewAdapter(new NumericWheelAdapter(BirthdayActivity.this, 1, 29));
                    else
                        wv_day.setViewAdapter(new NumericWheelAdapter(BirthdayActivity.this, 1, 28));
                }
            }
        };
        wv_year.addChangingListener(wheelListener_year);
        wv_month.addChangingListener(wheelListener_month);

//      View vShow = (View) localView1.findViewById(R.id.log_off_v_show);
        TextView confirmTv = (TextView) localView1.findViewById(R.id.birthday_dialog_tv_confirm);
        TextView cancelTv = (TextView) localView1.findViewById(R.id.birthday_dialog_tv_cancel);
        
        confirmTv.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
                int year = wv_year.getCurrentItem() + START_YEAR;
                int month = wv_month.getCurrentItem() + 1;
                int day = wv_day.getCurrentItem() +1;
                m_dayTv.setText(year + "-" + month + "-" + day); 
                dialog_more.dismiss();
            }
        });
        
        cancelTv.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v) 
            {
                dialog_more.dismiss();
            }
        });
        
        WindowManager.LayoutParams lp = dialog_more.getWindow().getAttributes();
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        Window window = dialog_more.getWindow();
        window.setGravity(Gravity.BOTTOM);//在底部弹出
        window.setWindowAnimations(R.style.birthday_time_dialog_animation);
        dialog_more.show();
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
