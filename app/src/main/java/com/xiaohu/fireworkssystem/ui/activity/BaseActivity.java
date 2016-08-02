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
    public RequestQueue mQueue;
    Utils utils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(getApplicationContext());
        utils=new Utils(BaseActivity.this);
        initView();
        initEvent();
    }
    public abstract void initView();
    public abstract void initEvent();
    public void ToastShort(String mes) {
        Toast.makeText(this, mes, Toast.LENGTH_SHORT).show();
    }
}
