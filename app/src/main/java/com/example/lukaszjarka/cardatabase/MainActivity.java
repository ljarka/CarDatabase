package com.example.lukaszjarka.cardatabase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MotoDatabaseOpenHelper motoDatabaseOpenHelper = new MotoDatabaseOpenHelper(this);
        motoDatabaseOpenHelper.getWritableDatabase();
    }
}
