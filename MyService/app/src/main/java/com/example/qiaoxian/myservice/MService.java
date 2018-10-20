package com.example.qiaoxian.myservice;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import static java.lang.Thread.sleep;

public class MService extends Service {
    public MService() {
    }

    private int i;
    private boolean flag =true;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG","service create");
        new Thread(new Runnable() {
            @Override
            public void run() {

                    for(i=0;i<100;i++){
                        try {

                            Intent intentService = new Intent("com.example.UpdateUI");
                            intentService.putExtra("data",i);
                            sendBroadcast(intentService);
                            sleep(1000);


                        } catch (InterruptedException e) {
                            e.printStackTrace();

                    }
                }

            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("TAG","service start");

        IntentFilter intentFilterService = new IntentFilter("com.example.stopIt");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                flag = false;
                stopSelf();
            }
        },intentFilterService);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.e("TAG","service bind");
        return null;
    }

    class MyBinder extends Binder{
        public int getProcess(){
            return i;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("TAG","service unbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("TAG","service destroyed");
    }
}
