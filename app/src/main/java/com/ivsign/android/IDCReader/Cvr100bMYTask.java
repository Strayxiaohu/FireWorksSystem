package com.ivsign.android.IDCReader;

import android.content.Context;

/**
 * Created by Administrator on 2015/11/30.
 */
public class Cvr100bMYTask {
    private Cvr100bTask mCvr100bTask;

    public void startCvr100bTask(Context context, Cvr100bTask.Cvr100bListener listener) {
        mCvr100bTask = new Cvr100bTask(context);
        mCvr100bTask.setCvr100bListener(listener,context);
        mCvr100bTask.start();
        DelayTask delay = new DelayTask(12 * 1000);
        delay.setOnDelayTaskListener(new DelayTask.OnDelayTaskListener() {
            @Override
            public void onTimeUp() {
                if (mCvr100bTask != null) {
                    mCvr100bTask.stopRun();
                }
            }
        });
    }
}
