package com.example.aanas.newapp;


import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class AddProduct extends AppCompatActivity {

    private EditText etName, etPrice, etAmount;
    private Button addButton, viewButton;
    private CheckBox checkBox;
    private DatabaseHelper myDB;
    private int bought;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int color;
    private float size;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        etName = (EditText) findViewById(R.id.name_id);
        etAmount = (EditText) findViewById(R.id.amount_id);
        etPrice = (EditText) findViewById(R.id.price_id);
        addButton = (Button) findViewById(R.id.add_product_id);
        viewButton = (Button) findViewById(R.id.view_id);
        checkBox = (CheckBox) findViewById(R.id.check_id);
        myDB = new DatabaseHelper(this);


        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddProduct.this, ProductList.class);
                startActivity(intent);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String price = etPrice.getText().toString();
                String amount = etAmount.getText().toString();
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                    bought = 1;
                } else if (!checkBox.isChecked()) {
                    checkBox.setChecked(true);
                    bought = 0;
                }
                if (name.length() != 0 && price.length() != 0 && amount.length() != 0) {
                    addData(name, price, amount, bought);
                    broadcastInfo(name, price, amount);
                    etName.setText("");
                    etPrice.setText("");
                    etAmount.setText("");
                    checkBox.setChecked(false);
                } else {
                    Toast.makeText(AddProduct.this, "You must put some data", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void broadcastInfo(String name, String price, String amount) {
        Intent broadcastIntent = new Intent(this,BroadcastService.class);
        broadcastIntent.putExtra("name", name);
        broadcastIntent.putExtra("price", price);
        broadcastIntent.putExtra("amount", amount);
        startService(broadcastIntent);
    }

    public void addData(String name, String price, String amount, int bought) {
        boolean insertData = myDB.addData(name, price, amount, bought);

        if (insertData) {
            Toast.makeText(AddProduct.this, "Data inserted correctly", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(AddProduct.this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        color = sharedPreferences.getInt("color", Color.DKGRAY);
        size = sharedPreferences.getFloat("size", 14);


        etName.setTextSize(size);
        etAmount.setTextSize(size);
        etPrice.setTextSize(size);
        addButton.setTextSize(size);
        addButton.setBackgroundColor(color);
        viewButton.setTextSize(size);
        viewButton.setBackgroundColor(color);
    }
}
