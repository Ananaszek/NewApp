package com.example.aanas.newapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProductList extends AppCompatActivity {

    private DatabaseHelper myDB;
    private ArrayList<Product> products;
    private Product product;
    private ListView listView;
    protected Object mActionMode;
    public int selectedItem = -1;
    private Button button, editBtn;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int color;
    private float size;
    private ProductProvider pProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        myDB = new DatabaseHelper(this);
        pProvider = new ProductProvider();
        products = new ArrayList<>();
//        Cursor data = myDB.getListContents();
        Cursor data = getContentResolver().query(ProductContract.ProductEntry.CONTENT_URI, null, null, null, null);
        if (data != null) {
            int numRows = data.getCount();
//        int numRows = data.getCount();
            if (numRows == 0) {
                Toast.makeText(ProductList.this, "There is nothing in this database", Toast.LENGTH_LONG).show();
            } else {
                int i = 0;
                while (data.moveToNext()) {
                    product = new Product(data.getInt(0), data.getString(1), data.getString(2), data.getString(3), data.getInt(4) == 1);
                    products.add(i, product);
                    i++;
                }
                FourColumn_ListAdapter adapter = new FourColumn_ListAdapter(this, R.layout.list_adapter_view, products);
                listView = (ListView) findViewById(R.id.list_view);
                listView.setAdapter(adapter);
            }
            data.close();
        }
        editBtn = (Button) findViewById(R.id.edit_button);

        Intent receivedIntent = getIntent();
        color = receivedIntent.getIntExtra("color", -1);
        size = receivedIntent.getFloatExtra("size", -1);
        editBtn.setBackgroundColor(color);
        editBtn.setTextSize(size);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEdit();
            }
        });

        button = (Button) findViewById(R.id.add_button_id);

        color = receivedIntent.getIntExtra("color", -1);
        size = receivedIntent.getFloatExtra("size", -1);
        button.setBackgroundColor(color);
        button.setTextSize(size);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openList();
            }
        });

        if (listView != null) {
            registerForContextMenu(listView);
        }
    }

    public void openEdit() {
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }

    private void openList() {
        Intent intent = new Intent(this, AddProduct.class);
        startActivity(intent);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Product prod = (Product) listView.getItemAtPosition(info.position);
        switch (item.getItemId()) {
            case R.id.delete_id:
                myDB.delete(prod.getId());
                break;
            case R.id.modify_id:
                Intent intent = new Intent(this, ModifyProduct.class);
                intent.putExtra("id", prod.getId());
                intent.putExtra("name", prod.getName());
                intent.putExtra("price", prod.getPrice());
                intent.putExtra("amount", prod.getAmount());
                int boughtVal = -1;
                if (prod.isBought() == true) {
                    boughtVal = 1;
                } else {
                    boughtVal = 0;
                }

                intent.putExtra("bought", boughtVal);
                startActivity(intent);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        color = sharedPreferences.getInt("color", Color.DKGRAY);
        size = sharedPreferences.getFloat("size", 14);

        editBtn.setTextSize(size);
        editBtn.setBackgroundColor(color);
        button.setTextSize(size);
        button.setBackgroundColor(color);
    }
}
