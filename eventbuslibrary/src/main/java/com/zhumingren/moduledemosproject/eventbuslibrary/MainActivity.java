package com.zhumingren.moduledemosproject.eventbuslibrary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        findViewById(R.id.tv_hello).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PostActivity.class));
            }
        });
    }
    
    // 子线程和主线程都能接收到, 后台界面的UI可以得到刷新
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(final MessageEvent event) {
        Log.i("zmr", "event:" + event.getMessage());
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                toast(event.getMessage());
                TextView textView = findViewById(R.id.tv_hello);
                textView.setText(event.getMessage());
                Log.i("zmr", "textView msg:" + textView.getText().toString());
            }
        });
    }
    
    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
