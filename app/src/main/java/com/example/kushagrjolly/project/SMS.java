package com.example.kushagrjolly.project;

import android.content.Intent;

/**
 * Created by Kushagr Jolly on 27-Jan-15.
 */
public class SMS {
    private String _name;
    private String number;
    private String body;
    private String date;
    public SMS(Intent intent){
        String counter = intent.getStringExtra("counter");
        String message=intent.getStringExtra("message");
        date=intent.getStringExtra("xyz");
        setBody(counter);
        setNumber(message);
        setDate(date);
    }
    // Empty constructor
    public SMS(){

    }

    public String getDate(){
        return date;
    }
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setDate(String date) {
        // TODO Auto-generated method stub
        this.date=date;
    }

}
