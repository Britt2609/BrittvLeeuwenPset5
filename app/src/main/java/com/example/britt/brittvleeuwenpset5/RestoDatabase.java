package com.example.britt.brittvleeuwenpset5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;

import javax.xml.transform.Result;

/**
 * Created by britt on 27-11-2017.
 */

public class RestoDatabase extends SQLiteOpenHelper {
    private static String TABLE_NAME = "orders";
    private static RestoDatabase instance;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create = "Create table " + TABLE_NAME + " (_id integer primary key, name text, price float, amount integer)";
        sqLiteDatabase.execSQL(create);
    }


    private RestoDatabase(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public static RestoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new RestoDatabase(context);
        }
        return instance;
    }

    public void addItem(String name, Float price) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Cursor c = sqLiteDatabase.rawQuery("Select * from " + TABLE_NAME + " where name = '"+ name + "';", null);

        if (c.getCount() > 0) {
            String create = "Update " + TABLE_NAME + " Set amount = amount+1 where name='" + name + "';";
            sqLiteDatabase.execSQL(create);
        }
        else if (c.getCount() == 0){
            contentValues.put("name", name);
            contentValues.put("price", price);
            contentValues.put("amount", 1);
            sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        }

    }

    public Cursor selectAll() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("Select * from " + TABLE_NAME, null);
        return c;
    }

    public String totalPrice() {

        int pricecol = 2;
        int amountcol = 3;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        float total = 0;
        while (c.moveToNext()) {
            total = total + c.getFloat(pricecol)*c.getInt(amountcol);
        }
        return "$" + total;
    }

    public void Clear(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String del = "Delete from " + TABLE_NAME + ";";
        sqLiteDatabase.execSQL(del);
    }

    public void deleteItem(String name) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor c = sqLiteDatabase.rawQuery("Select amount from " + TABLE_NAME + " where name = '"+ name + "';", null);
        if (c.getCount() == 1) {
            String del = "Delete from " + TABLE_NAME + " where name= '" + name + "';";
            sqLiteDatabase.execSQL(del);
        }

        else if (c.getCount() > 1) {
                String create = "Update " + TABLE_NAME + " Set amount = amount-1 where name='" + name + "';";
                sqLiteDatabase.execSQL(create);
        }
    }
}