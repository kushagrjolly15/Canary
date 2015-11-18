package com.example.kushagrjolly.project;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;

public class MyService extends Service {


    public static final String BROADCAST_ACTION = "variable";
    DatabaseHandler db = new DatabaseHandler(this);

    Intent intent1;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(receiver, filter);
        intent1 = new Intent(BROADCAST_ACTION);

        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
    /*
    *   @param hasbas
    *   @return dsdhckj
    *
    *
    */


    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle bundle = intent.getExtras();
            if(action.equals("android.provider.Telephony.SMS_RECEIVED")){
                Log.d("SMS", "Received");
                Object[] pdus = (Object[])bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                }
                String date= String.valueOf(messages[0].getTimestampMillis());
                intent1.putExtra("counter", messages[0].getMessageBody());
                intent1.putExtra("xyz",date);
                intent1.putExtra("message",messages[0].getDisplayOriginatingAddress());
                sendBroadcast(intent1);
            }
        }
    };




}

