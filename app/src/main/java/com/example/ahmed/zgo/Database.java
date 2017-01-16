package com.example.ahmed.zgo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Blob;

/**
 * Created by ahmed on 1/10/2017.
 */

public class Database extends SQLiteOpenHelper {

    public static final String databaseName = "zgo.dp";
    public static final String tableName = "USER";
    public static final String COL_1 = "USERNAME";
    public static final String COL_2 = "EMAIL";
    public static final String COL_3 = "PASSWORD";
    public static final String COL_4 = "IMAGE";



    public static final String image_table = "image_table";
    public static final String Image_COL_1 = "ID";
    public static final String Image_COL_2 = "USERNAME";
    public static final String Image_COL_3 = "IMAGE";




    public Database(Context context) {
        super(context, databaseName, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ tableName +" (USERNAME TEXT PRIMARY KEY , EMAIL TEXT, PASSWORD TEXT, IMAGE TEXT)");

        db.execSQL("create table " + image_table + "( ID INTEGER PRIMARY KEY AUTOINCREMENT,USERNAME TEXT ,IMAGE BLOB )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);

        db.execSQL("DROP TABLE IF EXISTS "+ image_table);
        onCreate(db);

    }

    public boolean insert(String username, String email, String pass, String iamge)
    {
        SQLiteDatabase dp=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_1, username);
        contentValues.put(COL_2, email);
        contentValues.put(COL_3, pass);
        contentValues.put(COL_4, iamge);
        Long result =  dp.insert(tableName, null, contentValues);
        if(result==-1)
            return  false;
        else
            return true;
    }

    public Cursor getData(String username)
    {
        SQLiteDatabase dp=this.getWritableDatabase();
        Cursor res=dp.query(tableName, null, "username = ?", new String[]{username}, null, null, null);
        return res;
    }


    public boolean imageInsertData(String name , byte[] image)
    {
        SQLiteDatabase dp=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Image_COL_2, name);
        contentValues.put(Image_COL_3, image);
        Long result =  dp.insert(image_table, null, contentValues);
        if(result==-1)
            return  false;
        else
            return true;
    }

    public Cursor getAllData(String username)
    {
        SQLiteDatabase dp=this.getWritableDatabase();
        Cursor res=dp.query(image_table, null, "username = ?", new String[]{username}, null, null, null);
        return res;
    }




}
