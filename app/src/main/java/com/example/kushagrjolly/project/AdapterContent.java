package com.example.kushagrjolly.project;

/**
 * Created by yushrox on 21-04-2015.
 */
public class AdapterContent {
    float amount;
    String place;
    String date;
    String month;


    public AdapterContent(float amount, String place, String date, String month){
        this.amount=amount;
        this.date=date;
        this.place=place;
        this.month= month;
    }

    public AdapterContent(float amount, String place, String date){
        this.amount=amount;
        this.date=date;
        this.place=place;

    }

}
