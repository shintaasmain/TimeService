package com.example.timerservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.timerservice.service.TimerService;

public class MainActivity extends AppCompatActivity {
    private TextView mTextTimer;
    private Button mButtonStart;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI (intent);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.i ("D3MI03","Activity onCreate");

        init();
    }

    private void init() {
        mTextTimer = findViewById(R.id.tv_timer);
        mButtonStart = findViewById(R.id.bt_start);


        if (isMyServiceRunning(TimerService.class)){
            mButtonStart.setText("Restart");
        }else{
            mButtonStart.setText("Start");
        }

        registerReceiver(broadcastReceiver, new IntentFilter(TimerService.BROADCAST_ACTION));

        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(MainActivity.this, TimerService.class);

                if (isMyServiceRunning(TimerService.class)){
                    stopService(serviceIntent);
                }

                startService(serviceIntent);

                mButtonStart.setText("Restart");
            }
        });
    }


    private void updateUI(Intent intent) {
        int mins = intent.getIntExtra("mins",0);
        int secs = intent.getIntExtra("secs",0);

        mTextTimer.setText("" + mins + ":" + String.format("%02d",secs));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i("D3MI03", "Activity onDestroy");
    }
    private boolean isMyServiceRunning (Class<TimerService>serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                Integer.MAX_VALUE)) {
        if (serviceClass.getName().equals(service.service.getClassName())) {
            return true;
        }
    }
      return  false;
    }

}
