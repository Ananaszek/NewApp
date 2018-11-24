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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class ModifyProduct extends AppCompatActivity {

    private static final String TAG = "EditDataActivity";
    private String selectedName;
    private String selectedPrice;
    private String selectedAmount;
    private boolean checked;
    private Button modifyBtn, viewBtn;
    private EditText etNamed, etPriced, etAmounted;
    private CheckBox checkBoxed;
    private DatabaseHelper myDB;
    int bought, id;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int color;
    private float size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_product);

        etNamed = (EditText)findViewById(R.id.name_id);
        etAmounted = (EditText)findViewById(R.id.amount_id);
        etPriced = (EditText)findViewById(R.id.price_id);
        modifyBtn = (Button)findViewById(R.id.save_id);
        viewBtn = (Button)findViewById(R.id.view_id);
        checkBoxed = (CheckBox)findViewById(R.id.check_id);
        myDB = new DatabaseHelper(this);

        Intent receivedIntent = getIntent();

        id = receivedIntent.getIntExtra("id", -1);
        selectedName = receivedIntent.getStringExtra("name");
        selectedPrice = receivedIntent.getStringExtra("price");
        selectedAmount = receivedIntent.getStringExtra("amount");
        bought = receivedIntent.getIntExtra("bought",-1);

        if (bought == 0){
            checked = false;
        }else if(bought == 1){
            checked = true;
        }

        etNamed.setText(selectedName);
        etAmounted.setText(selectedAmount);
        etPriced.setText(selectedPrice);
        checkBoxed.setChecked(checked);

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModifyProduct.this,ProductList.class);
                startActivity(intent);
            }
        });

        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etNamed.getText().toString();
                String price = etPriced.getText().toString();
                String amount = etAmounted.getText().toString();
                if(checkBoxed.isChecked()){
                    checkBoxed.setChecked(false);
                    bought = 1;
                }else if(!checkBoxed.isChecked()){
                    checkBoxed.setChecked(true);
                    bought = 0;
                }
                if(name.length() != 0 && price.length() != 0 && amount.length() != 0){
                    myDB.updateData(id,name, price, amount, bought);
                    etNamed.setText("");
                    etPriced.setText("");
                    etAmounted.setText("");
                    checkBoxed.setChecked(false);
                }else{
                    Toast.makeText(ModifyProduct.this, "You must put some data", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        color = sharedPreferences.getInt("color", Color.DKGRAY);
        size = sharedPreferences.getFloat("size",14);

        etNamed.setTextSize(size);
        etAmounted.setTextSize(size);
        etPriced.setTextSize(size);
        modifyBtn.setTextSize(size);
        modifyBtn.setBackgroundColor(color);
        viewBtn.setTextSize(size);
        viewBtn.setBackgroundColor(color);
    }
}
