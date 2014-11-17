package com.bgood.xn.ui.user.product;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.ui.BaseActivity;
import com.bgood.xn.widget.TitleBar;

/**
 * 
 * @todo:设置个性化模块
 * @date:2014-11-17 下午3:11:11
 * @author:hg_liuzl@163.com
 */
public class IndividuationActivity extends BaseActivity implements OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_individuation);
        (new TitleBar(mActivity)).initTitleBar("个性化模板");
        findViewById(R.id.individuation_tv_blue1).setOnClickListener(this);
        findViewById(R.id.individuation_tv_white2).setOnClickListener(this);
        findViewById(R.id.individuation_tv_purple3).setOnClickListener(this);
        findViewById(R.id.individuation_tv_green4).setOnClickListener(this);
        findViewById(R.id.individuation_tv_lightblue5).setOnClickListener(this);
        findViewById(R.id.individuation_tv_yellow6).setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            // 蓝色
            case R.id.individuation_tv_blue1:
                setColor(1);
                break;
            // 白色
            case R.id.individuation_tv_white2:
                setColor(2);
                break;
            
            // 紫色
            case R.id.individuation_tv_purple3:
                setColor(3);
                break;
            // 绿色
            case R.id.individuation_tv_green4:
                setColor(4);
                break;
            // 淡蓝
            case R.id.individuation_tv_lightblue5:
                setColor(5);
                break;
            // 黄色
            case R.id.individuation_tv_yellow6:
                setColor(6);
                break;
            default:
                break;
        }
    }
    
    /**
     * 设置颜色方法
     */
    private void setColor(int index)
    {
        pUitl.setSelfTemplat(index);
        Toast.makeText(this, "修改成功！", Toast.LENGTH_LONG).show();
        finish();
    }
}
