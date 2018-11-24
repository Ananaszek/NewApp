package com.example.aanas.newapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private Button but1;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int color;
    private float size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        but1 = findViewById(R.id.button_id); //initialize button
        but1.setBackgroundColor(color);
        but1.setTextSize(size);
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openList();
            }
        });
    }

    public void openList(){
        Intent intent = new Intent(this, ProductList.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        color = sharedPreferences.getInt("color", Color.DKGRAY);
        size = sharedPreferences.getFloat("size",14);
        but1.setBackgroundColor(color);
        but1.setTextSize(size);
    }
}
