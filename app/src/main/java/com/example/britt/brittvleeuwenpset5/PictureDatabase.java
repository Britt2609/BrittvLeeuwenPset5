package com.example.britt.brittvleeuwenpset5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by britt on 30-11-2017.
 */

public class PictureDatabase extends SQLiteOpenHelper {

    private static String TABLE_NAME = "dishes";
    private static PictureDatabase instance;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create = "Create table " + TABLE_NAME + " (_id integer primary key, name text," +
                " category text, description text, image_url text, price float, amount integer)";
        sqLiteDatabase.execSQL(create);
    }

    private PictureDatabase(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void onDrop() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public static PictureDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new PictureDatabase(context);
        }
        return instance;
    }

    public void addItem(String name, String description, String category, Float price, String url) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put("name", name);
        contentValues.put("price", price);
        contentValues.put("description", description);
        contentValues.put("category", category);
        contentValues.put("image_url", url);

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    public Cursor selectAll() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("Select * from " + TABLE_NAME, null);
        return c;
    }

}
