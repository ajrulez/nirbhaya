package com.som_itsolutions.nirbhaya;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class PhoneLocatorService extends Service implements SensorEventListener {

    private static final int NOTIFICATION_ID = 1;
    SMSBroadcastReceiver mBroadcastReceiver;
    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private static final double FREEFALL_THRESHOLD = 2.0;
    public static boolean firstTimeAlert = false;
    public static boolean mediaPlayerOncePlayed = false;
    public static String mAlternateNumber;
    Intent mLocationSendServiceIntent;
    MediaPlayer mediaPlayer;
    AudioManager am;
    private PowerManager.WakeLock mWakeLock;
    private final static long SCREEN_OFF_RECEIVER_DELAY = 800;


    public PhoneLocatorService() {
    }

    // BroadcastReceiver for handling ACTION_SCREEN_OFF.
    public BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // Check action just to be on the safe side.
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Log.i("test","trying re-registration");

                Runnable runnable = new Runnable() {
                    public void run() {
                        sensorManager.unregisterListener(PhoneLocatorService.this);
                        sensorManager.registerListener(PhoneLocatorService.this, sensorAccelerometer,
                                SensorManager.SENSOR_DELAY_NORMAL);
                    }
                };
                new Handler().postDelayed(runnable, SCREEN_OFF_RECEIVER_DELAY);

            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        //setting up the broadcast

        am = (AudioManager)(getApplicationContext().getSystemService(Context.AUDIO_SERVICE));
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.siren);
        mediaPlayer.setLooping(true);

        sensorManager = (SensorManager)getSystemService(getApplicationContext().SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        mBroadcastReceiver = new SMSBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(mBroadcastReceiver, filter);


        IntentFilter newFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, newFilter);


        PowerManager manager =
                (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, PhoneLocatorService.class.getName());

        // Create a notification area notification so the user
        // can get back to the PhoneLocator Service and Close It

        final Intent notificationIntent = new Intent(getApplicationContext(),
                NirbhayaActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        final Notification notification = new Notification.Builder(
                getApplicationContext())
                .setSmallIcon(R.drawable.ic_launcher)
                .setOngoing(true).setContentTitle("Nirbhaya")
                .setContentText("Click to Access Nirbhaya Service")
                .setContentIntent(pendingIntent).build();

        // Put this Service in a foreground state, so it won't
        // readily be killed by the system
        startForeground(NOTIFICATION_ID, notification);

        firstTimeAlert = false;
        mLocationSendServiceIntent = new Intent(NirbhayaActivity.getMainApp(), LocationSendService.class);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {

        //mAlternateNumber = intent.getStringExtra("ALTERNATE_NUMBER");
        mWakeLock.acquire();
        // Don't automatically restart this Service if it is killed
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        unregisterReceiver(mReceiver);
        firstTimeAlert = false;
        if(LocationSendService.mTimer != null){
            LocationSendService.mTimer.cancel();
            LocationSendService.mTimer.purge();
        }
        sensorManager.unregisterListener(this);

        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mWakeLock.release();

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        float x = 0;
        float y = 0;
        float z = 0;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
        }


        if ((Math.sqrt(x*x + y*y + z*z) < FREEFALL_THRESHOLD) && !firstTimeAlert){



            if(am.isMicrophoneMute()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        am.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_UNMUTE, 0);
                }
                else {
                    // Note that this must be the same instance of audioManager that mutes
                    // http://stackoverflow.com/questions/7908962/setstreammute-never-unmutes?rq=1
                    am.setStreamMute(AudioManager.STREAM_ALARM, false);
                }
            }

            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            firstTimeAlert = true;
            mediaPlayerOncePlayed = true;

            this.startService(mLocationSendServiceIntent);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
