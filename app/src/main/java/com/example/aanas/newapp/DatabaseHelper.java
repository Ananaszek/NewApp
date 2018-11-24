package com.example.aanas.newapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "products.db";
    public static final String TABLE_NAME = "products_data";
    public static final String COL1 = "ID";
    public static final String COL2 = "NAME";
    public static final String COL3 = "PRICE";
    public static final String COL4 = "AMOUNT";
    public static final String COL5 = "BOUGHT";

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("EEELOO");
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT, PRICE TEXT, AMOUNT TEXT, BOUGHT INTEGER DEFAULT 0)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String name, String price, String amount, int bought) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, name);
        contentValues.put(COL3, price);
        contentValues.put(COL4, amount);
        contentValues.put(COL5, bought);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //query for 1 week repeats
    public Cursor getListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }

    public void delete(int anInt) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME,COL1+"=?",new String[]{String.valueOf(anInt)});
        db.close();
    }

    public void updateData(int id, String name, String price, String amount, int bought){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 + " = '" + name + "' , " +
                COL3 + " = '" + price + "' , "+ COL4 + " = '" + amount + "' , "+
                COL5 + " = '" + bought + "' WHERE " + COL1 + " = " + id;
        db.execSQL(query);
    }
}
