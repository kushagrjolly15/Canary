package com.example.kushagrjolly.project;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    ArrayList<AdapterContent> ab = new ArrayList<AdapterContent>();
    float[] fl = new float[2];
    ListView listView;
    CustomAdapter custom;
    DatabaseHandler db = new DatabaseHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        OldSMS();
        startService(new Intent(getBaseContext(), MyService.class));
        List<Acc> contacts = db.getAllContacts();
        /*for (Acc cn : contacts) {
            String log = "Name:" + cn.getName() + " Phone: " + cn.getPhoneNumber() + " ,Body: " + cn.getBody() + "Account:" + cn.getacc() + "amount" + cn.getAmt() + "Balance" + cn.getBal() +  "place" + cn.getTxnName() + ",Date:" + cn.getDate();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }*/
        custom = new CustomAdapter(this, ab);
        listView= (ListView) this.findViewById(R.id.listView);
        if (listView != null) {
            listView.setAdapter(custom);
        }
    }

    public void OldSMS() {
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor c = getContentResolver().query(uri, null, null, null, null);
        if (c.moveToFirst()) {
            for (int i = 0; i < c.getCount(); i++) {
                SMS sms = new SMS();
                sms.setBody(c.getString(c.getColumnIndexOrThrow("body")).toString());
                String date =  c.getString(c.getColumnIndex("date"));
                sms.setDate(date);
                Long timestamp = Long.parseLong(date);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(timestamp);
                Date finaldate = calendar.getTime();
                String smsDate = finaldate.toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm");
                //Date date1 = new Date(user.date);
                String date_display= simpleDateFormat.format(finaldate);


                sms.setNumber(c.getString(c.getColumnIndexOrThrow("address")).toString());
                if((sms.getBody().contains("purchase")&&sms.getBody().contains("INR")&&sms.getBody().contains("Debit"))||sms.getBody().contains("NFS*CASH")) {
                    Acc ac = new Acc(sms);
                    fl = ac.amountBal(sms.getBody());

                    db.addContact(new Acc(ac.getName(), ac.getPhoneNumber(), ac.getBody(), ac.getacc(), fl[0], fl[1], date_display, ac.getTxnName()));
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM");
                    String month= sdf.format(finaldate);
                    ab.add(new AdapterContent(fl[0], ac.getTxnName(), date_display, month));

                    /*for (Acc cn : contacts) {
                        // Create a Card
                        Card card = new Card(this);
                        // Create a CardHeader
                        CardHeader header = new CardHeader(this);
                        header.setTitle(smsDate);
                        card.setTitle(String.valueOf(fl[1]));
                        card.addCardHeader(header);
                        cards.add(card);

                }*/

                }
                c.moveToNext();
            }
        }
        c.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(MyService.BROADCAST_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private android.content.BroadcastReceiver broadcastReceiver = new android.content.BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("broadcast","broad");
            float[] fl = new float[2];
            SMS s = new SMS(intent);
            if((s.getBody().contains("purchase")&&s.getBody().contains("INR")&&s.getBody().contains("Debit"))||s.getBody().contains("NFS*CASH")) {
                Acc ac = new Acc(s);
                String date = s.getDate();
                Long timestamp = Long.parseLong(date);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(timestamp);
                Date finaldate = calendar.getTime();
                String smsDate = finaldate.toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm");
                //Date date1 = new Date(user.date);
                String date_display = simpleDateFormat.format(finaldate);
                Log.d("date", date_display);
                fl = ac.amountBal(s.getBody());
                db.addContact(new Acc(ac.getName(), ac.getPhoneNumber(), ac.getBody(), ac.getacc(), fl[0], fl[1], date_display, ac.getTxnName()));
                SimpleDateFormat sdf = new SimpleDateFormat("MMM");
                String month = sdf.format(finaldate);
                ab.add(new AdapterContent(fl[0], ac.getTxnName(), date_display, month));

                custom = new CustomAdapter(getApplicationContext(), ab);

                listView.setAdapter(custom);
            }
        }

    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.delete();
    }

    @Override
    protected void onStop() {
        super.onStop();
       // db.delete();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.graphs:
                startActivity(new Intent(this, GraphActivity.class));
                return true;
            case R.id.table:
                startActivity(new Intent(this, AndroidDatabaseManager.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
