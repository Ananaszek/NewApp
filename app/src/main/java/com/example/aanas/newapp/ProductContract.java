package com.example.aanas.newapp;

import android.net.Uri;
import android.provider.BaseColumns;

public class ProductContract {

    static final String CONTENT_AUTHORITY = "com.example.aanas.newapp";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private ProductContract(){

    }
    public static final class ProductEntry implements BaseColumns{
        public static final String TABLE_NAME = "products_data";
        public static final String COL1 = "ID";
        public static final String COL2 = "NAME";
        public static final String COL3 = "PRICE";
        public static final String COL4 = "AMOUNT";
        public static final String COL5 = "BOUGHT";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_NAME)
                .build();

        public static Uri buildProductUriWithId(long id){
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }
}
