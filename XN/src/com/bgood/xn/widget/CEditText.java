/**
 * 版权所有：版权所有(C) 2013，用友
 * 文件名称：CEditText.java 
 * 内容摘要：自定义文本框类
 * 其它说明：
 * 创建作者：曾兵兵
 * 创建日期：2013-7-31上午11:11:51
 */
package com.bgood.xn.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * 自定义文本框类
 */
public class CEditText extends EditText
{
    private Drawable dRight;
    private Rect rBounds;

    public CEditText(Context paramContext)
    {
        super(paramContext);
        initEditText();
    }

    public CEditText(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
        initEditText();
    }

    public CEditText(Context paramContext, AttributeSet paramAttributeSet,
            int paramInt)
    {
        super(paramContext, paramAttributeSet, paramInt);
        initEditText();
    }

    private void initEditText()
    {
        setEditTextDrawable();
        addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable paramEditable)
            {
            }

            public void beforeTextChanged(CharSequence paramCharSequence,
                    int paramInt1, int paramInt2, int paramInt3)
            {
            }

            public void onTextChanged(CharSequence paramCharSequence,
                    int paramInt1, int paramInt2, int paramInt3)
            {
                CEditText.this.setEditTextDrawable();
            }
        });
    }

    private void setEditTextDrawable()
    {
        if (getText().toString().length() == 0)
        {
            setCompoundDrawables(null, null, null, null);
            return;
        }
        setCompoundDrawables(null, null, this.dRight, null);
    }

    protected void finalize() throws Throwable
    {
        this.dRight = null;
        this.rBounds = null;
        super.finalize();
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent)
    {
        Log.d("MyEditText", "onTouchEvent==========x===" + paramMotionEvent.getX() + "====y====" + paramMotionEvent.getY());
        if ((this.dRight != null) && (paramMotionEvent.getAction() == 1))
        {
            this.rBounds = this.dRight.getBounds();
            int i = (int)paramMotionEvent.getX();
//            (int)paramMotionEvent.getY();
            Log.d("MyEditText", "(this.getRight()-rBounds.width())=========" + (getRight() - 3 * this.rBounds.width()));
            if (i > getRight() - 3 * this.rBounds.width())
            {
                Log.d("MyEditText", "click=======xxxxxxxxxxxxxxxxxxxxxxx");
                setText("");
                paramMotionEvent.setAction(3);
            }
        }
        return super.onTouchEvent(paramMotionEvent);
    }

    public void setCompoundDrawables(Drawable paramDrawable1, Drawable paramDrawable2, Drawable paramDrawable3,
            Drawable paramDrawable4)
    {
        if (paramDrawable3 != null)
        {
            this.dRight = paramDrawable3;
        }
        super.setCompoundDrawables(paramDrawable1, paramDrawable2,
                paramDrawable3, paramDrawable4);
    }
}
