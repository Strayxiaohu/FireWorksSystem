package com.xiaohu.fireworkssystem.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.xiaohu.fireworkssystem.utils.Utils;

/**
 * Created by Administrator on 2016/7/16.
 */
public abstract class BaseActivity extends Activity {
    //RequestQueue是一个请求队列对象，它可以缓存所有的HTTP请求，然后按照一定的算法并发地发出这些请求。RequestQueue内部的设计就是非常合适高并发的，
    //因此我们不必为每一次HTTP请求都创建一个RequestQueue对象，这是非常浪费资源的，基本上在每一个需要和网络交互的Activity中创建一个RequestQueue对象就足够了。
    public RequestQueue mQueue;
    Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //volley网络请求
        mQueue = Volley.newRequestQueue(getApplicationContext());
        utils = new Utils(BaseActivity.this);
        initView();
        initEvent();
    }

    public abstract void initView();

    public abstract void initEvent();

    public void ToastShort(String mes) {
        Toast.makeText(this, mes, Toast.LENGTH_SHORT).show();
    }
}
