package com.example.ahmed.zgo;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.nio.ByteBuffer;

public class Login extends AppCompatActivity {

    private Button signup, signin;
    private EditText name, pass;
    private Database myDB;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myDB = new Database(this);

        signup = (Button) findViewById(R.id.login_signup);
        signin = (Button) findViewById(R.id.login_signin);
        name = (EditText) findViewById(R.id.login_username);
        pass = (EditText) findViewById(R.id.login_password);
        image = (ImageView) findViewById(R.id.login_image);

        enter();



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);

            }
        });
    }

    public void enter()
    {
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDB.getData(name.getText().toString());

                if(res.getCount()==0)
                {

                    Toast.makeText(Login.this, "User Not Found", Toast.LENGTH_SHORT).show();;
                    return;
                }
                res.moveToNext();
                {
                    if(res.getString(0).equals(name.getText().toString()) && res.getString(2).equals(pass.getText().toString()))
                    {
                        Bitmap selectedImage= BitmapFactory.decodeFile(res.getString(3));
                        Drawable drawable = new BitmapDrawable(selectedImage);
                        image.setImageDrawable(drawable);



                        Toast.makeText(Login.this, "Login successful \n Welcome : "+ name.getText().toString() , Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, Map.class);
                        intent.putExtra("username", res.getString(0));
                        intent.putExtra("email", res.getString(1));
                        intent.putExtra("photo", res.getString(3));
                        startActivity(intent);

                        return;

                    }
                }
                Toast.makeText(Login.this, "username or password is false", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
