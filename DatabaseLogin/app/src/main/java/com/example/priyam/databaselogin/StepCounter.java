package com.example.priyam.databaselogin;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.Date;

public class StepCounter extends Service implements SensorEventListener{
    private final LocalBinder mBinder = new LocalBinder();
    protected Handler handler;
    private boolean mInitialized; // used for initializing sensor only once
    private final float NOISE = (float) 2.0;
    private SensorManager mSensorManager;
    private int stepsCount;
    SQLiteDatabase db;
    private double mLastX,mLastY,mLastZ;
    private Sensor mAccelerometer;
    private double preTime=0,time;

    @Override
    public void onSensorChanged(SensorEvent event) {
        // event object contains values of acceleration, read those
        double x = event.values[0];
        double y = event.values[1];
        double z = event.values[2];
        double mx,my,mz;
        mx=x;
        my=y;
        mz=z;

        time=System.currentTimeMillis();
        final double alpha = 0.8; // constant for our filter below

        double[] gravity = {0,0,0};

        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

// Remove the gravity contribution with the high-pass filter.
        x = event.values[0] - gravity[0];
        y = event.values[1] - gravity[1];
        z = event.values[2] - gravity[2];

        if (!mInitialized) {
            // sensor is used for the first time, initialize the last read values
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            preTime=time;
            Log.e("init", String.valueOf(preTime));
            mInitialized = true;
        } else {
            // sensor is already initialized, and we have previously read values.
            // take difference of past and current values and decide which
            // axis acceleration was detected by comparing values

            double deltaX = Math.abs(mLastX - x);
            double deltaY = Math.abs(mLastY - y);
            double deltaZ = Math.abs(mLastZ - z);
            if (deltaX < NOISE)
                deltaX = (float) 0.0;
            if (deltaY < NOISE)
                deltaY = (float) 0.0;
            if (deltaZ < NOISE)
                deltaZ = (float) 0.0;
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            if (deltaX > deltaY) {
                if (stepsCount >= 0 && (time-preTime>499) ){//&&(mz>-5) && (mz<5) ) {
                    stepsCount = stepsCount + 1;
                    Log.e("Steps X",""+stepsCount);
                    Log.e("Time Diff X",String.valueOf(time-preTime));
                    Log.e("Displacement X ",String.valueOf(mz));
                    preTime=time;
                    addStep(stepsCount);

                }else {
                    preTime=time;
                    Log.e("No Step X ",String.valueOf("OnlyShake"));
                    Log.e("Time Diff X",String.valueOf(time-preTime));
                    Log.e("Displacement X ",String.valueOf(mz));
                }
                // Horizontal shake
                // do something here if you like
                //Log.e("Displacement x ",String.valueOf(mx));
                //Log.e("Displacement z ",String.valueOf(mz));

            } else if (deltaY > deltaX) {
                // Vertical shake
                // do something here if you like
                //Log.e("Displacement y ",String.valueOf(my));
                //Log.e("Displacement z ",String.valueOf(mz));


            } else if ((deltaZ > deltaX) && (deltaZ > deltaY) ) {
                // Z shake
                //Log.e("Displacement x ",String.valueOf(mx));

                if (stepsCount >= 0 && (time-preTime>499) ){//&&(mz>-5) && (mz<5) ) {
                    stepsCount = stepsCount + 1;
                    Log.e("Steps",""+stepsCount);
                    Log.e("Time Diff",String.valueOf(time-preTime));
                    Log.e("Displacement z ",String.valueOf(mz));
                    preTime=time;
                    addStep(stepsCount);

                }else {
                    preTime=time;
                    Log.e("No Step ",String.valueOf("OnlyShake"));
                    Log.e("Time Diff",String.valueOf(time-preTime));
                    Log.e("Displacement z ",String.valueOf(mz));
                }


// Just for indication purpose, I have added vibrate function
                // whenever our count moves past multiple of 10
                if ((stepsCount % 10) == 0) {
                    //Util.Vibrate(this, 100);
                }
            } else {
                // no shake detected
            }
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public class LocalBinder extends Binder {
        public StepCounter getService() {
            return StepCounter .this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db=openOrCreateDatabase("MyTemp.db", Context.MODE_PRIVATE,null);
        mInitialized = false;
        Log.e("Created","Done");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void startSensor() {
        mSensorManager.registerListener(this, mAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler();
        Log.e("Started","Done");
        SharedPreferences sharedPreferences;
        sharedPreferences=getSharedPreferences("StepCounter",0);
        if(sharedPreferences.contains("count")){
            Log.e("Count",sharedPreferences.getInt("count",0)+"");
        }
        stepsCount=sharedPreferences.getInt("count",0);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        startSensor();
        startDailyAlarm();
        handler.post(new Runnable() {
            @Override
            public void run() {

// write your code to post content on server
            }
        });
        return Service.START_STICKY;
    }
    public void addStep(int stepCount){
        SharedPreferences sharedPreferences;
        sharedPreferences=getSharedPreferences("StepCounter",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("count",stepCount);
        editor.apply();
        if(sharedPreferences.contains("count")){
            Log.e("Count",sharedPreferences.getInt("count",0)+"");
        }
    }
    public void startDailyAlarm(){

        Intent intent=new Intent(getApplicationContext(),Notification_receiver.class);
        intent.putExtra("Type",5);
        intent.setAction("Sync");
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),555,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
        long time=0;
        Date tdae=new Date();
        tdae.setDate(tdae.getDate()+1);
        tdae.setMinutes(0);
        tdae.setSeconds(0);
        tdae.setHours(0);
        time=tdae.getTime();
        if (Build.VERSION.SDK_INT >= 23)
        {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,time , pendingIntent);
        }
        else if (Build.VERSION.SDK_INT >= 19)
        {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,time, pendingIntent);
        }   else{}
    }
}
