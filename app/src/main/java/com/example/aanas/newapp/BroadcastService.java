package com.example.aanas.newapp;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.Nullable;

public class BroadcastService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BroadcastService(String name) {
        super(name);
    }

    public BroadcastService() {
        super("BroadcastService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent!=null) {
            intent.setAction("pl.aanasz.PRODUCT_ADDED");
            intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            intent.setComponent(new ComponentName("com.example.aanas.broadcastapp", "com.example.aanas.broadcastapp.ProductReceiver"));
            sendBroadcast(intent,"com.example.aanas.newapp.PRODUCT_PERMISSION");
        }
    }
}
