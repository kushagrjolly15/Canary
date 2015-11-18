package com.example.kushagrjolly.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yushrox on 21-04-2015.
 */
public class CustomAdapter extends ArrayAdapter<AdapterContent> {
    Context context;


    public CustomAdapter(Context context, ArrayList<AdapterContent> users) {

        super(context, 0, users);
        this.context = context;
    }



    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position

        AdapterContent user = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_layout, parent, false);

        }

        // Lookup view for data population
        TextView place = (TextView) convertView.findViewById(R.id.textView);
        TextView amount = (TextView) convertView.findViewById(R.id.textView2);
        TextView date = (TextView) convertView.findViewById(R.id.textView3);



        // Populate the data into the template view using the data object
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm");
        //Date date1 = new Date(user.date);
        //String date_display= simpleDateFormat.format(date1);
        place.setText(user.place);
        amount.setText("Amount: " + user.amount);
        date.setText(user.date);
        // Return the completed view to render on screen

        return convertView;

    }

}

