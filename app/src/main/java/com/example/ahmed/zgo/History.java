package com.example.ahmed.zgo;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import java.util.ArrayList;

import static android.R.id.list;

public class History extends AppCompatActivity {

    ImageView image;
    byte[] dataimageimg;
    Bitmap img;
    Database myDB;
    Bundle bundle;
    String username;
    private ArrayList<Bitmap> imageList;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        myDB = new Database(this);
        image = (ImageView) findViewById(R.id.history_image);
        imageList=new ArrayList<Bitmap>();

        bundle = getIntent().getExtras();
        username = bundle.getString("username");

        Cursor res = myDB.getAllData(username);
        while (res.moveToNext())
        {
            dataimageimg = res.getBlob(2);
            img = getImage(dataimageimg);
            imageList.add(img);
        }

        recyclerView= (RecyclerView)findViewById(R.id.recycle);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        adapter=new RecyclerAdapter(imageList);
        recyclerView.setAdapter(adapter);
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

}



