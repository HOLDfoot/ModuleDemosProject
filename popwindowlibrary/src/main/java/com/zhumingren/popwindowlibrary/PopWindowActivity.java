package com.zhumingren.popwindowlibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.zhouwei.library.CustomPopWindow;

public class PopWindowActivity extends AppCompatActivity {
    
    private TextView textView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_window);
        textView = findViewById(R.id.tv_hello);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow();
            }
        });
    }
    
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        showPopWindow();
    }
    
    private void showPopWindow() {
        // 获取一个控件的位置信息，本demo用不到
        int[] loc = new int[2];
        textView.getLocationOnScreen(loc);
        int width = textView.getWidth();
        int height = textView.getHeight();
        // 显示popWindow在相对某个控件的位置
        CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(R.layout.pop_layout1)//显示的布局，还可以通过设置一个View
                //     .size(600,400) //设置显示的大小，不设置就默认包裹内容
                .setFocusable(true)//是否获取焦点，默认为ture
                .setOutsideTouchable(true)//是否PopupWindow 以外触摸dissmiss
                .create()//创建PopupWindow
                .showAsDropDown(textView,-100,10);//显示PopupWindow
    }
}
