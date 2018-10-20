package com.example.qiaoxian.myservice;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private boolean isBound = false;
    private TextView textViewInfo;
    private int number;
    private Handler handler;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int j = intent.getIntExtra("data",0);
            textViewInfo.setText(String.valueOf(j));
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("TAG","connected message");
            MService.MyBinder mb = (MService.MyBinder) iBinder;
            number = mb.getProcess();
            Log.e("TAG",number+"");
//            Message msg = Message.obtain();
//            msg.what = 1001;
//            msg.arg1 = number;
//            handler.sendMessage(msg);


        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewInfo=(TextView)findViewById(R.id.textView);
//        handler = new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                switch (msg.what){
//                    case 1001:
//                        textViewInfo.setText(msg.arg1);
//                }
//            }
//        };


    }

    public void buttonOperate(View view){
        switch (view.getId()){
            case R.id.buttonStartService:
                Intent intentStartService = new Intent(MainActivity.this,MService.class);
                startService(intentStartService);
                count();
                break;
            case R.id.buttonStopService:
                Intent intentStopService = new Intent(MainActivity.this,MService.class);
                stopService(intentStopService);
                stopCount();
                textViewInfo.setText("STOP");
                break;
            case R.id.buttonBind:
                Intent intentBindService = new Intent(MainActivity.this,MService.class);
                bindService(intentBindService, serviceConnection,BIND_AUTO_CREATE);
                isBound = true;
                break;
            case R.id.buttonUnbind:
                if(isBound){
                    unbindService(serviceConnection);
                    isBound =false;
                }
                break;
        }

    }



    public void stopCount(){
        Intent intentStop = new Intent("com.example.stopIt");
        sendBroadcast(intentStop);
        unregisterReceiver(broadcastReceiver);

    }

    public void count(){
        IntentFilter filter = new IntentFilter("com.example.UpdateUI");
        this.registerReceiver(broadcastReceiver,filter);
    }
}
