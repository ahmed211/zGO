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


    public Database(Context context) {
        super(context, databaseName, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ tableName +" (USERNAME TEXT PRIMARY KEY , EMAIL TEXT, PASSWORD TEXT, IMAGE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
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
       // Cursor res=dp.rawQuery("select * from "+ tableName, null);
        return res;
    }



}
