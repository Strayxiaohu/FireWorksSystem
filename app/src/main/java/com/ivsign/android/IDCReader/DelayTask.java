package com.ivsign.android.IDCReader;

import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

public class DelayTask {
	
	private Timer m_oTimer;		
	private TimerTask m_oTimerTask;
	private long m_delay = 200;

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			if(onDelayTaskListener != null)
				onDelayTaskListener.onTimeUp();
		}
	};
	
	private OnDelayTaskListener onDelayTaskListener;
	public interface OnDelayTaskListener{
		public void onTimeUp();
	}
	public void setOnDelayTaskListener(OnDelayTaskListener listener)
	{
		onDelayTaskListener = listener;
	}
	
	public DelayTask(long delay)
	{
		m_delay = delay;
	}
	
	public void setDelay(long delay)
	{
		m_delay = delay;
	}
	
	public void start()
	{	
		cancel();
		
		m_oTimer = new Timer();
		m_oTimerTask = new TimerTask() 
        {
            @Override
            public void run() 
            {         	   
            	handler.sendEmptyMessage(1);
            }
       };       
       m_oTimer.schedule(m_oTimerTask, m_delay);
	}
	
	
	public void cancel()
	{
		if (m_oTimerTask != null)
		{
			m_oTimerTask.cancel();
			m_oTimerTask = null;
		}
		
		if (m_oTimer != null)
		{
			m_oTimer.cancel();
			m_oTimer = null;
		}
	}
}
