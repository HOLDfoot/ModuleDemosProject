package com.zhumingren.moduledemosproject.eventbuslibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

public class PostActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_post);
       
        findViewById(R.id.tv_hello).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent("Post message"));
            }
        });
        findViewById(R.id.tv_hello).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SecondEvent secondEvent = new SecondEvent("secondEvent");
                secondEvent.code = 1;
                EventBus.getDefault().post(secondEvent);
                return false;
            }
        });
    }
    
}
