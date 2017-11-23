package com.som_itsolutions.nirbhaya;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by som on 19/6/16.
 */
public class SMSBroadcastReceiver extends BroadcastReceiver{

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SMSBroadcastReceiver";

    public static double lat;
    public static double lon;
    String smsMessage;
    String smsFromNumber;

    Intent mLocationSendServiceIntent;
    //private static boolean mediaPlayerOncePlayed = false;

    public SMSBroadcastReceiver() {
        mLocationSendServiceIntent = new Intent(NirbhayaActivity.getMainApp(), LocationSendService.class);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Intent recieved: " + intent.getAction());

        if (intent.getAction() == SMS_RECEIVED) {


            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[])bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String format = bundle.getString("format");
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                    }
                    else{
                        messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                    }

                }
                if (messages.length > -1) {
                    Log.i(TAG, "Message recieved: " + messages[0].getMessageBody());
                    smsMessage = messages[0].getMessageBody();
                    smsFromNumber = messages[0].getDisplayOriginatingAddress();


                    if(smsMessage.contains("%!")){
                        //Display the Google Map
                        //String str = NirbhayaActivity.smsMessageStr;
                        String stringToBeParsed = smsMessage;
                        int firstAtTheRatePosition = stringToBeParsed.indexOf("@");
                        int firstHashPosition = stringToBeParsed.indexOf("#");
                        String str = stringToBeParsed.substring(0,firstAtTheRatePosition);


                        String latSubstring = stringToBeParsed.substring(firstAtTheRatePosition + 1,firstHashPosition);
                        String lonSubstring = stringToBeParsed.substring(firstHashPosition + 1);

                        lat = Double.parseDouble(latSubstring);
                        lon = Double.parseDouble(lonSubstring);

                        Log.i("SMSReceiver Latitude", Double.toString(lat));
                        Log.i("SMSReceiver Longitude", Double.toString(lon));

                       //Now display the Map with pin having this lat & lon
                        Intent iMapActivity = new Intent(context,NirbhayaMapsActivity.class);
                        //iMapActivity.setClassName("com.som_itsolutions.nirbhaya", "com.som_itsolutions.nirbhaya.NirbhayaMapsActivity");
                        iMapActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        iMapActivity.putExtra("lat", lat);
                        iMapActivity.putExtra("lon", lon);
                        context.startActivity(iMapActivity);


                    }
                }
            }
        }
    }


}
