package com.example.timerservice.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;


public class TimerService extends Service {


    public static final String BROADCAST_ACTION = "com.example.timerservice";
    long timeInMillisecond = 0L;
    private Intent intent;
    private Handler handler = new Handler();
    private long initial_time;

    private Runnable sendUpdatesToUI = new Runnable() {
        @Override
        public void run() {
            DisplayLoggingInfo();
            handler.postDelayed(this, 1000); // 1 seconds
        }

        private void DisplayLoggingInfo() {
            timeInMillisecond = SystemClock.uptimeMillis() - initial_time;

            int timer = (int) timeInMillisecond / 1000;
            int mins = timer / 60;
            int secs = timer % 60;
            intent.putExtra("mins", mins);
            intent.putExtra("secs", secs);


            Log.i("D3MI03", "" + mins + ":" + String.format("%02d", secs));
            sendBroadcast(intent);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        initial_time = SystemClock.uptimeMillis();
        intent = new Intent(BROADCAST_ACTION);
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(sendUpdatesToUI);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startService(intent);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}
