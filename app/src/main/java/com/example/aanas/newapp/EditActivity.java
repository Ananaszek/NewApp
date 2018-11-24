package com.example.aanas.newapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EditActivity extends AppCompatActivity {

    private Button redBtn, greenBtn, greyBtn, smallBtn, mediumBtn, bigBtn;
    private int color;
    private float size;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Intent iAdd, iMain, iModify, iList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        redBtn = findViewById(R.id.red);
        greenBtn = findViewById(R.id.green);
        greyBtn = findViewById(R.id.grey);
        smallBtn = findViewById(R.id.small_button);
        mediumBtn = findViewById(R.id.medium_button);
        bigBtn = findViewById(R.id.big_button);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();

        redBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color = Color.RED;
                editor.putInt("color", color);
                editor.commit();

            }
        });

        greenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color = Color.GREEN;
                editor.putInt("color", color);
                editor.commit();
            }
        });

        greyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color = Color.LTGRAY;
                editor.putInt("color", color);
                editor.commit();
            }
        });

        smallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                size = 10;
                editor.putFloat("size", size);
                editor.commit();
            }
        });

        mediumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                size = 14;
                editor.putFloat("size", size);
                editor.commit();
            }
        });

        bigBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                size = 20;
                editor.putFloat("size", size);
                editor.commit();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void openList(){
        Intent intent = new Intent(this, ProductList.class);
        startActivity(intent);
    }
}
