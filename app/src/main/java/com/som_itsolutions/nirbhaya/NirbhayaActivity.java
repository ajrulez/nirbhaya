package com.som_itsolutions.nirbhaya;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.Timer;

public class NirbhayaActivity extends AppCompatActivity {
     Button mStopNirbhayaService;
	 private static final long INTERVAL = 300000;
	 private Timer mTimer;
	 public static String phoneNumberStr = "";
	 public static  String smsMessageStr = "";
	 private double lat = 0.0;
	 private double lon = 0.0;
	 static String premise = "";
	 static String locality = "";
	 static String postalCode = "" ;
	 static String countryStr = "";
	 static String thorouNirbhayaActivity = "";
	 static String subThoroughfare = "";
	 static String subLocality = "";
	 //private String finalSMSString;
	 public static final String settingPreferences= "settings";
	 private LocationManager lm;
	 private static NirbhayaActivity mainApp;
	 Intent locatorService = null;
	 //private Callback c;
	 List<Address> address;
     //public static MediaPlayer mediaPlayer;
     Intent phoneLocatorService;
      private boolean isFirstTimeServiceStarted = false;


    public static NirbhayaActivity getMainApp(){
		 return mainApp;
	 }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_layout);

        mStopNirbhayaService = (Button)findViewById(R.id.buttonStopService);
        mainApp = this;
        /*mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.siren);
        mediaPlayer.setLooping(true);*/
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        SharedPreferences sp = getSharedPreferences(settingPreferences, Context.MODE_PRIVATE);
        if(!(sp == null) ){
	        phoneNumberStr = sp.getString("phonenumber", "");
	        smsMessageStr = sp.getString("message", "");
            phoneLocatorService = new Intent(getApplicationContext(), PhoneLocatorService.class);
            startService(phoneLocatorService);
            //isFirstTimeServiceStarted = true;
            //finish();
        }
        
        if((phoneNumberStr == "") || (smsMessageStr == "")){
        	Intent i = new Intent();
        	i.setClassName("com.som_itsolutions.nirbhaya", "com.som_itsolutions.nirbhaya.NirbhayaSettings");
        	startActivity(i);
            //return;
        	finish();
        }

        mStopNirbhayaService.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
               /* if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }*/

                getApplicationContext().stopService(phoneLocatorService);
                //isFirstTimeServiceStarted = false;

               /* Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/
            }
        });

        /*if(!isFirstTimeServiceStarted){
            phoneLocatorService = new Intent(getApplicationContext(), PhoneLocatorService.class);
            startService(phoneLocatorService);
            isFirstTimeServiceStarted = true;
        }*/

    }  
        

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_sms_scheduler, menu);
        return true;
    }
    
    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onStop() {

        super.onStop();
    }
    
    /*@Override
    public void onBackPressed() {

         stopService(phoneLocatorService);
         mediaPlayer.stop();
         mediaPlayer.release();
    	 Intent intent = new Intent(Intent.ACTION_MAIN);
	     intent.addCategory(Intent.CATEGORY_HOME);
	     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	     startActivity(intent);
    }
*/

}
