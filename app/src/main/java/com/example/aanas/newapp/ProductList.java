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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ProductList extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;

    private DatabaseHelper myDB;
    private ArrayList<Product> products;
    private Product product;
    private ListView listView;
    protected Object mActionMode;
    public int selectedItem = -1;
    private Button button, editBtn, signOutBtn;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int color;
    private float size;
    private ProductProvider pProvider;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private DatabaseReference priceRef;
    private DatabaseReference amountRef;
    private DatabaseReference nameRef;
    private DatabaseReference boughtRef;
    private DatabaseReference fireProducts;
    private FourColumn_ListAdapter adapter;
    private Context context;
    private ArrayList<String> listKeys;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        myDB = new DatabaseHelper(this);
        pProvider = new ProductProvider();
        products = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
        nameRef = dbRef.child("name");
        priceRef = dbRef.child("price");
        amountRef = dbRef.child("amount");
        boughtRef = dbRef.child("bought");
        fireProducts = dbRef.child("products");
        context = this;
        listKeys = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();

//        Cursor data = myDB.getListContents();
//        Cursor data = getContentResolver().query(ProductContract.ProductEntry.CONTENT_URI, null, null, null, null);
//        if (data != null) {
//            int numRows = data.getCount();
////        int numRows = data.getCount();
//            if (numRows == 0) {
//                Toast.makeText(ProductList.this, "There is nothing in this database", Toast.LENGTH_LONG).show();
//            } else {
//                int i = 0;
//                while (data.moveToNext()) {
//                    product = new Product(data.getInt(0), data.getString(1), data.getString(2), data.getString(3), data.getInt(4) == 1);
//                    products.add(i, product);
//                    i++;
//                }
//                adapter = new FourColumn_ListAdapter(this, R.layout.list_adapter_view, products);
//                listView = (ListView) findViewById(R.id.list_view);
//                listView.setAdapter(adapter);
//            }
//            data.close();
//        }

        addChildEventListener();
        adapter = new FourColumn_ListAdapter(this, R.layout.list_adapter_view, products);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        editBtn = (Button) findViewById(R.id.edit_button);
        signOutBtn = (Button)findViewById(R.id.signOutBtnId);

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(ProductList.this, MainActivity.class);
                startActivity(intent);
            }
        });

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

    private void addChildEventListener(){
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ArrayList<String> names = new ArrayList<>();
                ArrayList<String> prices = new ArrayList<>();
                ArrayList<String> amounts = new ArrayList<>();
                boolean bought = false;
                for(DataSnapshot snaps : dataSnapshot.getChildren()){
                    String name = (String)snaps.child("name").getValue();
                    String price = (String)snaps.child("price").getValue();
                    String amount = (String)snaps.child("amount").getValue();
                    Integer isBought = (Integer) snaps.child("bought").getValue();
//                    if(isBought!= null && isBought == 1) {
//                            bought = true;
//                    }
//                        else {
//                            bought = false;
//                        }
//                    }
                    products.add(new Product(name,price,amount,bought));
                    listKeys.add(dataSnapshot.getKey());
                }
                adapter = new FourColumn_ListAdapter(context, R.layout.list_adapter_view, products);
                listView = (ListView) findViewById(R.id.list_view);
                listView.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                int index = listKeys.indexOf(key);

                if(index != -1){
                    products.remove(index);
                    listKeys.remove(index);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        dbRef.addChildEventListener(childEventListener);
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
                dbRef.child(listKeys.get(prod.getId())).removeValue();
//                myDB.delete(prod.getId());
                break;
            case R.id.modify_id:
                Intent intent = new Intent(this, ModifyProduct.class);
                intent.putExtra("id", prod.getId());
                intent.putExtra("name", prod.getName());
                intent.putExtra("price", prod.getPrice());
                intent.putExtra("amount", prod.getAmount());
                int boughtVal = -1;
                if (prod.isBought()) {
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
