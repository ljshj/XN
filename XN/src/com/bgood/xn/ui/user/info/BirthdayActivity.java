//package com.bgood.xn.ui.user.info;
//
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.List;
//
//import android.app.Dialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup.LayoutParams;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bgood.xn.R;
//import com.bgood.xn.bean.UserBean;
//import com.bgood.xn.network.BaseNetWork.ReturnCode;
//import com.bgood.xn.ui.BaseActivity;
//
///**
// * 生日编写页面
// */
//public class BirthdayActivity extends BaseActivity implements OnClickListener
//{
//    private Button m_backBtn = null;  // 返回
//    private TextView m_dayTv = null;  // 生日填写
//    private Button m_confirmBtn = null; // 确定按钮
//    
//    private static int START_YEAR = 1900, END_YEAR = 2100;
//    private UserBean mUserBean = null;
//    private String m_birthday = "";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_birthday);
//        mUserBean = (UserBean) getIntent().getSerializableExtra(UserBean.KEY_USER_BEAN);
//        findView();
//        setListener();
//    }
//  
//
//    /**
//     * 控件初始化方法
//     */
//    private void findView()
//    {
//        m_backBtn = (Button) findViewById(R.id.birthday_btn_back);
//        m_dayTv = (TextView) findViewById(R.id.birthday_tv_day);
//        m_confirmBtn = (Button) findViewById(R.id.birthday_btn_confirm);
//        m_dayTv.setText(mUserBean.birthday);
//    }
//    
//    /**
//     * 控件事件监听方法
//     */
//    private void setListener()
//    {
//        m_backBtn.setOnClickListener(new OnClickListener()
//        {
//            
//            @Override
//            public void onClick(View v)
//            {
//                BirthdayActivity.this.finish();
//            }
//        });
//        
//        m_dayTv.setOnClickListener(new OnClickListener()
//        {
//            
//            @Override
//            public void onClick(View v)
//            {
//                timeDialogShow();
//            }
//        });
//        
//        // 确定按钮
//        m_confirmBtn.setOnClickListener(new OnClickListener()
//        {
//            
//            @Override
//            public void onClick(View v)
//            {
//                String birthday = m_dayTv.getText().toString();
//                if (birthday != null && !birthday.equals(""))
//                {
//                    m_birthday = birthday;
//                    loadData(birthday);
//                }
//                else
//                {
//                    Toast.makeText(BirthdayActivity.this, "生日不能为空！", Toast.LENGTH_LONG).show();
//                    return;
//                }
//                
//            }
//        });
//    }
//    
//    /**
//     * 显示Dialog对话框
//     * @param typeNum
//     * @param list
//     * @param textView
//     * @param imageView
//     */
//    private void timeDialogShow()
//    {
//        final Dialog dialog_more = new Dialog(BirthdayActivity.this, R.style.birthday_time_dialog);
//        dialog_more.setCancelable(true);
//        dialog_more.setCanceledOnTouchOutside(true);
//        
//        LayoutInflater localLayoutInflater = LayoutInflater.from(this);
//        View localView1 = localLayoutInflater.inflate(R.layout.layout_birthday_dialog, null);
//        dialog_more.setContentView(localView1);
//        
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DATE);
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);
//
//        // 添加大小月月份并将其转换为list,方便之后的判断
//        String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
//        String[] months_little = { "4", "6", "9", "11" };
//
//        final List<String> list_big = Arrays.asList(months_big);
//        final List<String> list_little = Arrays.asList(months_little);
//
//        // 年
//        final WheelView wv_year = (WheelView) localView1.findViewById(R.id.birthday_dialog_wv_year);
//        wv_year.setViewAdapter(new NumericWheelAdapter(BirthdayActivity.this, START_YEAR, END_YEAR));// 设置"年"的显示数据
//        wv_year.setCyclic(true);// 可循环滚动
//        wv_year.setLabel("年");// 添加文字
//        wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据
//
//        // 月
//        final WheelView wv_month = (WheelView) localView1.findViewById(R.id.birthday_dialog_wv_month);
//        wv_month.setViewAdapter(new NumericWheelAdapter(BirthdayActivity.this, 1, 12));
//        wv_month.setCyclic(true);
//        wv_month.setLabel("月");
//        wv_month.setCurrentItem(month);
//
//        // 日
//        final WheelView wv_day = (WheelView) localView1.findViewById(R.id.birthday_dialog_wv_day);
//        wv_day.setCyclic(true);
//        // 判断大小月及是否闰年,用来确定"日"的数据
//        if (list_big.contains(String.valueOf(month + 1))) {
//            wv_day.setViewAdapter(new NumericWheelAdapter(BirthdayActivity.this, 1, 31));
//        } else if (list_little.contains(String.valueOf(month + 1))) {
//            wv_day.setViewAdapter(new NumericWheelAdapter(BirthdayActivity.this, 1, 30));
//        } else {
//            // 闰年
//            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
//                wv_day.setViewAdapter(new NumericWheelAdapter(BirthdayActivity.this, 1, 29));
//            else
//                wv_day.setViewAdapter(new NumericWheelAdapter(BirthdayActivity.this, 1, 28));
//        }
//        wv_day.setLabel("日");
//        wv_day.setCurrentItem(day - 1);
//
//        // 添加"年"监听
//        OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
//            public void onChanged(WheelView wheel, int oldValue, int newValue) {
//                int year_num = newValue + START_YEAR;
//                // 判断大小月及是否闰年,用来确定"日"的数据
//                if (list_big.contains(String
//                        .valueOf(wv_month.getCurrentItem() + 1))) {
//                    wv_day.setViewAdapter(new NumericWheelAdapter(BirthdayActivity.this, 1, 31));
//                } else if (list_little.contains(String.valueOf(wv_month
//                        .getCurrentItem() + 1))) {
//                    wv_day.setViewAdapter(new NumericWheelAdapter(BirthdayActivity.this, 1, 30));
//                } else {
//                    if ((year_num % 4 == 0 && year_num % 100 != 0)
//                            || year_num % 400 == 0)
//                        wv_day.setViewAdapter(new NumericWheelAdapter(BirthdayActivity.this, 1, 29));
//                    else
//                        wv_day.setViewAdapter(new NumericWheelAdapter(BirthdayActivity.this, 1, 28));
//                }
//            }
//        };
//        // 添加"月"监听
//        OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
//            public void onChanged(WheelView wheel, int oldValue, int newValue) {
//                int month_num = newValue + 1;
//                // 判断大小月及是否闰年,用来确定"日"的数据
//                if (list_big.contains(String.valueOf(month_num))) {
//                    wv_day.setViewAdapter(new NumericWheelAdapter(BirthdayActivity.this, 1, 31));
//                } else if (list_little.contains(String.valueOf(month_num))) {
//                    wv_day.setViewAdapter(new NumericWheelAdapter(BirthdayActivity.this, 1, 30));
//                } else {
//                    if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
//                            .getCurrentItem() + START_YEAR) % 100 != 0)
//                            || (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
//                        wv_day.setViewAdapter(new NumericWheelAdapter(BirthdayActivity.this, 1, 29));
//                    else
//                        wv_day.setViewAdapter(new NumericWheelAdapter(BirthdayActivity.this, 1, 28));
//                }
//            }
//        };
//        wv_year.addChangingListener(wheelListener_year);
//        wv_month.addChangingListener(wheelListener_month);
//
////      View vShow = (View) localView1.findViewById(R.id.log_off_v_show);
//        TextView confirmTv = (TextView) localView1.findViewById(R.id.birthday_dialog_tv_confirm);
//        TextView cancelTv = (TextView) localView1.findViewById(R.id.birthday_dialog_tv_cancel);
//        
//        confirmTv.setOnClickListener(new View.OnClickListener() 
//        {
//            @Override
//            public void onClick(View v) 
//            {
//                int year = wv_year.getCurrentItem() + START_YEAR;
//                int month = wv_month.getCurrentItem() + 1;
//                int day = wv_day.getCurrentItem() +1;
//                m_dayTv.setText(year + "-" + month + "-" + day); 
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
//    /**
//     * 加载数据方法
//     * @param value 修改内容
//     */
//    private void loadData(String value)
//    {
//    	WindowUtil.getInstance().progressDialogShow(BirthdayActivity.this, "数据请求中...");
//		messageManager.modifyPersonalInfo("birthday", value);
//    }
//    
//    @Override
//	public void modifyPersonalInfoCB(Reulst result_state)
//	{
//		super.modifyPersonalInfoCB(result_state);
//		WindowUtil.getInstance().DismissAllDialog();
//		
//		if (result_state.resultCode == ReturnCode.RETURNCODE_OK)
//		{
//			Toast.makeText(this, "修改成功！", Toast.LENGTH_LONG).show();
//			Intent intent = getIntent();
//            intent.putExtra("birthday", m_birthday);
//            setResult(RESULT_OK, intent);
//			finish();
//		}
//		else
//		{
//			Toast.makeText(this, "修改失败！", Toast.LENGTH_LONG).show();
//		}
//	}
//
//
//	@Override
//	public void onClick(View arg0) {
//		// TODO Auto-generated method stub
//		/**
//		 * @todo:TODO
//		 * @author:hg_liuzl@163.com
//		 */
//		
//	}
//}
