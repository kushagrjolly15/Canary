package com.example.kushagrjolly.project;

/**
 * Created by Kushagr Jolly on 07-Apr-15.
 */

import android.content.Context;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Kushagr Jolly on 27-Jan-15.
 */
public class Acc extends SMS{

    private Context context;

    //private variables
    String _body;
    String _name;
    int _id;
    private String date;
    String _phone_number;
    String _acc;
    String _txn;
    float _amt;
    String y;
    float _bal;
    String _place;


    public Acc(String name, String phoneNumber, String body, String acc, float f1,float f2, String date, String txnName){
        this._body=body;
        this._phone_number=phoneNumber;
        this._name=name;
        this._acc=acc;
        this._amt=f1;
        this._bal=f2;
        this.date= date;
        this._place=txnName;
    }

   public Acc(){

    }
    public Acc(Context context){
        this.context = context;
    }
    public Acc(SMS s) {
        this._body= s.getBody();
        this._phone_number = s.getNumber();
        this.date= s.getDate();
        String x="(X+)*[0-9][0-9][0-9][0-9]";
        Pattern pattern = Pattern.compile(x);
        Matcher matcher = pattern.matcher(_body);
        if(matcher.find()){
            this._acc=matcher.group(0);
            //Log.d("account:",matcher.group(0));
        }

    }



    public float[] amountBal(String body){
        String msg= body;
        String pattern;
        pattern = "INR";
        float amount[]=new float[2];
        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);
        int i=0;
        int start1;
        // Now create matcher object.
        Matcher m = r.matcher(msg);

        while (m.find( )) {
            start1 = m.start();
           // Log.d("Found value at: ", " " + m.start());
            int x = m.start()+4;
            String y=new String();
            while (!y.equalsIgnoreCase(" ")&&x<msg.length()-1){
                y=Character.toString(msg.charAt(x));
                x++;
            }

            String amt = msg.substring(m.start() + 3, x - 1);
            //String amnt = amt.substring(0,amt.indexOf(","))+amt.substring(amt.indexOf(","));
            amt=StringUtils.remove(amt,",");
            amt=StringUtils.remove(amt," ");
            amt=StringUtils.remove(amt,"/-");
            //amt=StringUtils.remove(amt,".");
            //
            // Log.d("amount:", amt+" "+i);
            amount[i] =Math.round(Float.parseFloat(amt));
            //Log.d("amountfun:",amount[i]+" "+i+" ");
            i++;

        }
        return amount;
    }


    public String getDate(){
        return date;
    }
    public void setDate(String date) {
        // TODO Auto-generated method stub
        this.date=date;
    }

    // getting phone number
    public String getPhoneNumber(){
        return this._phone_number;
    }

    // getting name
    public String getName(){
        return this._name;
    }

    // setting name
    public void setName(String name){
        this._name = name;
    }
    //getting body of msg
    public String getBody(){
        return this._body;
    }


    //setting body of msg
    public void setBody(String body){this._body= body;}

    // setting phone number
    public void setPhoneNumber(String phone_number){
        this._phone_number = phone_number;
    }

    public String getTxnName(){
        String bod = this._body;
        int idx=bod.indexOf("Info.");
        int x= idx+5;
        if(idx!=-1){
        String y=new String();
        while (!y.equalsIgnoreCase(".")&&x<bod.length()-1){
            y=Character.toString(bod.charAt(x));
            x++;
        }
        this._place = bod.substring(idx+5,x-1);
        return this._place;
        //Log.d("Name",this._place);
        }
        else{
            this._place = "Withdrawal";
            return this._place;
        }

    }

    public String getacc(){
        return this._acc;
    }

    public String getAmt(){
        return String.valueOf(this._amt);
    }


    public String getBal(){
        return String.valueOf(this._bal);
    }
    public void setacc(String acc){
        this._acc=acc;
    }
  

    // setting acc
    public void setAmt(float amt){
        this._amt = amt;
    }

    // setting bal
    public void setBal(float bal){
        this._bal = bal;
    }

    public void setPlace(String place){
        this._place = place;
    }

}
