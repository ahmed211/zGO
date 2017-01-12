package com.example.ahmed.zgo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.nio.ByteBuffer;


public class Register extends AppCompatActivity {

    private Database myDB;
    private EditText username, pass, email;
    private ImageButton image ;
    private Button register;
    private Bitmap selectedImage;
    private byte[] array;
    private static final int pick_image = 1 ;
    private String filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        myDB = new Database(this);

        username = (EditText) findViewById(R.id.register_username);
        pass = (EditText) findViewById(R.id.register_password);
        email = (EditText) findViewById(R.id.register_email);
        image  = (ImageButton) findViewById(R.id.register_image);
        register = (Button) findViewById(R.id.register_register);


        AddData();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galary = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galary, pick_image);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == pick_image )
        {
            Uri uri = data.getData();
            String [] projection = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(projection[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();

            selectedImage = BitmapFactory.decodeFile(filePath);
            Drawable drawable = new BitmapDrawable(selectedImage);
            image.setImageDrawable(drawable);
        }
    }

    public void AddData()
    {
        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                if(username.getText().toString().equals("") ||
                        email.getText().toString().equals("")    ||
                        pass.getText().toString().equals("")     ||
                        filePath.equals(""))
                {
                    Toast.makeText(Register.this, "Please Complete Your Data", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    boolean isInserted = myDB.insert(username.getText().toString(), email.getText().toString(),
                            pass.getText().toString(), filePath);

                    if (isInserted == true)
                    {
                        Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_LONG).show();
                        Intent login= new Intent(Register.this, Login.class);
                        startActivity(login);
                    }
                    else
                        Toast.makeText(Register.this, "Registration Failed", Toast.LENGTH_LONG).show();
                }

            }
        });
    }



}
