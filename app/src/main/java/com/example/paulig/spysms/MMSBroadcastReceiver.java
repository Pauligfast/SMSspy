package com.example.paulig.spysms;

/**
 * Created by Paulig on 6/1/2017.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MMSBroadcastReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";

    @Override
    public void onReceive(Context context, Intent intent) {
        throw new UnsupportedOperationException("Coming soon!");
    }
}