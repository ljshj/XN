package com.bgood.xn.ui.home;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bgood.xn.R;
import com.bgood.xn.ui.base.BaseActivity;
import com.bgood.xn.utils.JsonParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.umeng.analytics.MobclickAgent;

/**
 * 语音搜索页面
 */
public class SpeechSearchActivity extends BaseActivity
{
    private Button m_backBtn = null; // 返回按钮
    private ImageView m_speechImgV = null; // 返回按钮
    private TextView m_msgTv = null;
    private AnimationDrawable animationDrawable;
    private SpeechRecognizer mIat; // 语音听写对象
    private int ret = 0;// 函数调用返回值
    private String m_msg = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_speech_search);
        findView();
        setListener();
        // 初始化识别对象
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
    }
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
    
    @Override
	public void onResume() 
    {
    	super.onResume();
    	MobclickAgent.onResume(this);
        SpeechUtility.getUtility().checkServiceInstalled();
    }
    
    @Override
    protected void onDestroy() 
    {
        super.onDestroy();
        // 退出时释放连接
        mIat.cancel();
        mIat.destroy();
    }
    /**
     * 控件初始化方法
     */
    private void findView()
    {
        m_backBtn = (Button) findViewById(R.id.speech_search_btn_back);
        m_speechImgV = (ImageView) findViewById(R.id.speech_search_imgv_speech);
        m_msgTv = (TextView) findViewById(R.id.speech_search_tv_msg);
    }

    /**
     * 控件事件监听方法
     */
    private void setListener()
    {
        m_backBtn.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                SpeechSearchActivity.this.finish();
            }
        });
        
        // 说话
        m_speechImgV.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
            	
            	MobclickAgent.onEvent(SpeechSearchActivity.this,"home_speech_click");
            	
                m_msg = "";
                m_msgTv.setText("请点击");
                m_speechImgV.setImageResource(R.drawable.speech_animation_start);  
                animationDrawable = (AnimationDrawable) m_speechImgV.getDrawable();
                animationDrawable.setOneShot(false);
                animationDrawable.start(); 
                // 设置参数
                setParam();
                // 不显示听写对话框
                ret = mIat.startListening(recognizerListener);
                
                if(ret != ErrorCode.SUCCESS)
                {
                    showTip("听写失败,错误码：" + ret);
                }
                else 
                {
                    showTip(getString(R.string.speech_text_begin));
                }
            }
        });
    }
    
    private void init()
    {
       
    }

    /**
     * 参数设置
     * 
     * @param param
     * @return
     */
    public void setParam()
    {
        // 设置语言
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 设置语音前端点
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
        // 设置语音后端点
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
        // 设置标点符号
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");
    }
    
    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() 
    {

        @Override
        public void onInit(int code) 
        {
            if (code == ErrorCode.SUCCESS) 
            {
                m_speechImgV.setEnabled(true);
            }
        }
    };
    
    /**
     * 听写监听器。
     */
    private RecognizerListener recognizerListener=new RecognizerListener()
    {

        @Override
        public void onBeginOfSpeech() 
        { 
            m_msgTv.setText("请说话");
        }

        @Override
        public void onError(SpeechError error) 
        {
            animationDrawable = (AnimationDrawable) m_speechImgV.getDrawable();  
            animationDrawable.stop(); 
            m_speechImgV.setImageResource(R.drawable.speech_animation_pause);
            m_msgTv.setText("请点击");
            showTip(error.getPlainDescription(true));
        }

        @Override
        public void onEndOfSpeech() 
        {
            animationDrawable = (AnimationDrawable) m_speechImgV.getDrawable();  
            animationDrawable.stop(); 
            m_msgTv.setText("识别中");
            
            m_speechImgV.setImageResource(R.drawable.speech_animation_stop);  
            animationDrawable = (AnimationDrawable) m_speechImgV.getDrawable(); 
            animationDrawable.setOneShot(false);
            animationDrawable.start();
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, String msg) 
        {

        }

        @Override
        public void onResult(final RecognizerResult results, final boolean isLast) 
        {  
            String text = JsonParser.parseIatResult(results.getResultString());
            m_msg = m_msg + text;
            animationDrawable = (AnimationDrawable) m_speechImgV.getDrawable();  
            animationDrawable.stop();
            
            m_msgTv.setText("请点击");
            m_speechImgV.setImageResource(R.drawable.speech_animation_pause);
            
            
            if(isLast) 
            {
                
                setIntent(m_msg);
            }
        }

        @Override
        public void onVolumeChanged(int volume) 
        {
//            showTip("当前正在说话，音量大小：" + volume);
        }

    };
    
    private void showTip(final String str)
    {
        runOnUiThread(new Runnable() 
        {
            @Override
            public void run() 
            {
                Toast.makeText(SpeechSearchActivity.this, str, 0).show();
            }
        });
    }
    
    private void setIntent(String msg)
    {
        if (msg != null && !msg.trim().equals(""))
        {
            Intent intent = new Intent(SpeechSearchActivity.this, SearchResultActivity.class);
            intent.putExtra("msg", msg);
            startActivity(intent);
        }
        else
        {
            showTip("请说话");
        }
    }
}
