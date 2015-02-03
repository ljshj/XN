/**
 * 版权所有：版权所有(C) 2013，用友
 * 文件名称：MenuFragment.java 
 * 内容摘要：左侧菜单 
 * 其它说明：
 * 创建作者：应志强
 * 创建日期：2013-8-12下午02:50:42
 */
package com.bgood.xn.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MenuFragment extends Fragment
{

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        TextView textView = new TextView(getActivity());
        textView.setBackgroundColor(0x4400ff00);
        return textView;
    }

    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

    }

}
