package com.example.aanas.newapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class FourColumn_ListAdapter extends ArrayAdapter<Product> {

    private LayoutInflater mInflater;
    private ArrayList<Product> products;
    private int mViewResourceId;

    public FourColumn_ListAdapter(Context context, int textViewResourceId, ArrayList<Product> products){
        super(context,textViewResourceId,products);
        this.products = products;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parents){
        convertView = mInflater.inflate(mViewResourceId,null);

        Product product = products.get(position);

        if (product != null){
            TextView name = (TextView)convertView.findViewById(R.id.name_id);
            TextView price = (TextView)convertView.findViewById(R.id.price_id);
            TextView amount = (TextView)convertView.findViewById(R.id.amount_id);
            TextView bought = (TextView) convertView.findViewById(R.id.check_id);

            if(name != null) {
                name.setText(product.getName());
            }
            if(price != null){
                price.setText(product.getPrice());
            }
            if(amount != null){
                amount.setText(product.getAmount());
            }
            if(bought != null) {
                bought.setText(String.valueOf(product.isBought()));
            }
        }
        return convertView;
    }
}
