package com.example.kushagrjolly.project;

import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.listener.OnEntryClickListener;
import com.db.chart.model.BarSet;
import com.db.chart.view.BarChartView;
import com.db.chart.view.XController;
import com.db.chart.view.YController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GraphActivity extends ActionBarActivity {

    float[] values;
    String[] months;
    BarChartView chartView;
    BarSet barSet;
    private TextView mBarTooltip;
    private final TimeInterpolator enterInterpolator = new DecelerateInterpolator(1.5f);
    private final TimeInterpolator exitInterpolator = new AccelerateInterpolator();
    List<Acc> accList;
    ArrayList<AdapterContent> contentArrayList;
    ArrayList<ArrayList<AdapterContent>> monthwiselist;
    ListView listView;
    CustomAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        monthwiselist = new ArrayList<ArrayList<AdapterContent>>();
        contentArrayList= new ArrayList<AdapterContent>();

        accList = new DatabaseHandler(this).getAllContacts();
        for(Acc acc: accList){
            float[] amount=new float[2];
            amount=acc.amountBal(acc.getBody());
            String dateString = acc.getDate();
            Date date;
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm");
            try {
                date = sdf.parse(dateString);
                c.setTime(date);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            contentArrayList.add(new AdapterContent(amount[0],
                    acc.getTxnName(),
                    acc.getDate(),
                    c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH)+
                            c.get(Calendar.YEAR)));
            Log.d("Check Graph Data",
                    amount[0] + " " +
                            acc.getTxnName() + " " +
                            acc.getDate() + " " +
                            c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH)+
                            c.get(Calendar.YEAR));
        }
        int count = 0;
        int counter = 0;
        values = new float[6];
        months = new String[6];
        while (count<6){
            AdapterContent adaptercontent = contentArrayList.get(counter);

            float sum=0;
            String month= adaptercontent.month;
            ArrayList<AdapterContent> listcontent = new ArrayList<AdapterContent>();
            int flag=0;
                while(flag==0) {
                    AdapterContent content = contentArrayList.get(counter);

                            if (content.month.equalsIgnoreCase(month)) {
                                sum = sum + content.amount;
                                Log.d("Adding",content.amount+" "+month);
                                listcontent.add(content);
                                counter++;
                            } else {
                                flag++;

                            }
                }
                monthwiselist.add(listcontent);
                values[count]=sum;
                months[count]=month;
                Log.d("Added",values[count]+" "+months[count]);
                count++;
            }

        barSet = new BarSet();
        chartView = (BarChartView) findViewById(R.id.linechart);
        chartView.setXAxis(true);
        chartView.setLabelColor(Color.WHITE);
        chartView.setSetSpacing(Tools.fromDpToPx(3));
        chartView.setBarSpacing(Tools.fromDpToPx(14));

        chartView.setBorderSpacing(0)
                .setYAxis(false)
                .setXLabels(XController.LabelPosition.OUTSIDE)
                .setYLabels(YController.LabelPosition.NONE);
        barSet.addBars(months, values);
        barSet.setColor(Color.LTGRAY);
        chartView.addData(barSet);
        chartView.setOnClickListener(barClickListener);
        chartView.setOnEntryClickListener(barEntryListener);
        chartView.show();
        listView = (ListView) findViewById(R.id.listView2);
    }

    @SuppressLint("NewApi")
    private void showBarTooltip(int setIndex, int entryIndex, Rect rect){

        mBarTooltip = (TextView) getLayoutInflater().inflate(R.layout.bar_tooltip, null);
        mBarTooltip.setText("INR\n"+Integer.toString((int) values[entryIndex]));

        LayoutParams layoutParams = new LayoutParams(rect.width(), rect.height());
        layoutParams.leftMargin = rect.left;
        layoutParams.topMargin = rect.top;
        mBarTooltip.setLayoutParams(layoutParams);

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1){
            mBarTooltip.setAlpha(0);
            mBarTooltip.setScaleY(0);
            mBarTooltip.animate()
                    .setDuration(200)
                    .alpha(1)
                    .scaleY(1)
                    .setInterpolator(enterInterpolator);
        }

        chartView.showTooltip(mBarTooltip);
    }

    private final OnEntryClickListener barEntryListener = new OnEntryClickListener(){
        @Override
        public void onClick(int setIndex, int entryIndex, Rect rect) {
            listView.setVisibility(View.VISIBLE);
            customAdapter = new CustomAdapter(getApplicationContext(),monthwiselist.get(entryIndex));
            listView.setAdapter(customAdapter);
            if(mBarTooltip == null)
                showBarTooltip(setIndex, entryIndex, rect);
            else
                dismissBarTooltip(setIndex, entryIndex, rect);
        }
    };

    private final View.OnClickListener barClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            listView.setVisibility(View.INVISIBLE);
            if(mBarTooltip != null)
                dismissBarTooltip(-1, -1, null);
        }
    };

    @SuppressLint("NewApi")
    private void dismissBarTooltip(final int setIndex, final int entryIndex, final Rect rect){

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            mBarTooltip.animate()
                    .setDuration(100)
                    .scaleY(0)
                    .alpha(0)
                    .setInterpolator(exitInterpolator).withEndAction(new Runnable(){
                @Override
                public void run() {
                    chartView.removeView(mBarTooltip);
                    mBarTooltip = null;
                    if(entryIndex != -1)
                        showBarTooltip(setIndex, entryIndex, rect);
                }
            });
        }else{
            chartView.dismissTooltip(mBarTooltip);
            mBarTooltip = null;
            if(entryIndex != -1)
                showBarTooltip(setIndex, entryIndex, rect);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_graph, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
