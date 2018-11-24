package com.example.aanas.newapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ProductProvider extends ContentProvider {

    private DatabaseHelper myDB;
    public static final int CODE_PRODUCT = 100;
    public static final int CODE_PRODUCT_WITH_ID = 101;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ProductContract.CONTENT_AUTHORITY;

        matcher.addURI(authority,ProductContract.ProductEntry.TABLE_NAME,CODE_PRODUCT);
        matcher.addURI(authority,ProductContract.ProductEntry.TABLE_NAME + "/#",CODE_PRODUCT_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        myDB = new DatabaseHelper(getContext());
        return myDB != null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        switch (uriMatcher.match(uri)) {

            case CODE_PRODUCT_WITH_ID: {

                String _ID = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{_ID};

                cursor = myDB.getReadableDatabase().query(
                        ProductContract.ProductEntry.TABLE_NAME,
                        projection,
                        ProductContract.ProductEntry._ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_PRODUCT: {
                cursor = myDB.getReadableDatabase().query(
                        ProductContract.ProductEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = myDB.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case CODE_PRODUCT:

                long _id = db.insert(ProductContract.ProductEntry.TABLE_NAME, null, values);

                if (_id != -1) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return ProductContract.ProductEntry.buildProductUriWithId(_id);

            default:
                return null;
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
