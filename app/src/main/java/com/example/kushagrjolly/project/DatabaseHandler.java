package com.example.kushagrjolly.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kushagr Jolly on 02-Apr-15.
 */public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";
    String CREATE_TABLE;

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";
    private static final String KEY_BODY = "body";
    private static final String KEY_ACC = "account";
    private static final String KEY_TXN = "transacton";
    private static final String KEY_AMT = "amount";
    private static final String KEY_BAL = "balance";
    private static final String KEY_DATE = "date";
    private static final String KEY_PLACE = "place";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_PH_NO + " TEXT," + KEY_BODY +" TEXT," + KEY_ACC + " TEXT," + KEY_TXN + " TEXT," + KEY_AMT + " TEXT," +
                KEY_BAL + " TEXT," + KEY_DATE + " INTEGER," + KEY_PLACE + " TEXT" + ")" ;
        db.execSQL(CREATE_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    void addContact(Acc contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        float[] amount=new float[2];
        ContentValues values = new ContentValues();
        amount=contact.amountBal(contact.getBody());
        //values.put(KEY_NAME, contact.getName()); // Acc Name
        values.put(KEY_PH_NO, contact.getPhoneNumber()); // Acc Phone
        values.put(KEY_BODY,contact.getBody());
        values.put(KEY_ACC,contact.getacc());
        values.put(KEY_AMT,amount[0]);
        values.put(KEY_BAL,amount[1]);
        values.put(KEY_DATE, (contact.getDate()));
        values.put(KEY_PLACE,contact.getTxnName());
        values.put(KEY_TXN,"True");
        values.put(KEY_NAME,contact.getTxnName()+" "+contact.getDate());
        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }



    // Getting All Contacts
    public List<Acc> getAllContacts() {
        List<Acc> contactList = new ArrayList<Acc>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Acc contact = new Acc();
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));
                contact.setBody(cursor.getString(3));
                contact.setacc(cursor.getString(4));
                contact.setAmt(cursor.getFloat(6));
                contact.setBal(cursor.getFloat(7));
                contact.setDate(cursor.getString(8));
                contact.setPlace(cursor.getString(9));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }


    public void delete() {
        SQLiteDatabase db = this.getWritableDatabase(); //get database
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        Log.d("dbms destroyed","projecct");
        // Create tables again
        onCreate(db);
    }
    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }
}

