package com.example.priyam.databaselogin;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;
/**
 * Created by Priyam on 04-02-2018.
 */
public class Notification_receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            //Do a Log or, less likely, a Toast or start your application here.
            Log.e("boot","yeahh");
            SharedPreferences pref=context.getSharedPreferences("Reminders",MODE_PRIVATE);
            SharedPreferences.Editor editor=pref.edit();
            editor.putBoolean("FoodReminder",false);
            editor.putBoolean("WaterReminder",false);
            editor.putBoolean("WalkReminder",false);
            editor.putBoolean("ExerciseReminder",false);
            editor.apply();
            pref=context.getSharedPreferences("Timings",MODE_PRIVATE);
            editor=pref.edit();
            editor.putLong("Breakfast",0);
            editor.putLong("Brunch",0);
            editor.putLong("Lunch",0);
            editor.putLong("Esnack",0);
            editor.putLong("Dinner",0);
            editor.putLong("WaterReminder",0);
            editor.putLong("WalkReminder",0);
            editor.putLong("Suryanamaskar",0);
            editor.putLong("Yoga",0);
            editor.putLong("Gym",0);
            editor.apply();
            Intent intent1=new Intent(context,Notification_receiver.class);
            intent1.putExtra("Type",5);
            intent1.setAction("Sync");
            PendingIntent pendingIntent=PendingIntent.getBroadcast(context,555,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager= (AlarmManager) context.getSystemService(ALARM_SERVICE);
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
        int i=intent.getIntExtra("Type",0);
        Log.e("in Receiver",i+"");
        if(i==1)setWaterReminder(context,intent);
        if(i==2)setWalkReminder(context,intent);
        if(i==3)setEatReminder(context,intent);
        if (i==4)setExerciseReminder(context,intent);
        if (i==5)setStepRecount(context,intent);
    }
    public void setWaterReminder(Context context ,Intent intent){
        intent.putExtra("Type",1);
        intent.setAction("Remind");
        Log.e("i",intent.getIntExtra("Type",0)+"");
        Intent intent1=new Intent(context,WaterMonitor.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,111,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification noti = new Notification.Builder(context)
                .setContentTitle("Time to drink Water !")
                .setStyle(new Notification.BigTextStyle().bigText("Its time you gulped a glass of water! Stay hydrated,stay fit!"))
                .setSmallIcon(R.drawable.water)
                .setContentIntent(pendingIntent)
                .setSound(alarmSound)
                .build();
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(1, noti);
        PendingIntent pendingIntent1=PendingIntent.getBroadcast(context.getApplicationContext(),111,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager= (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Log.e("Time newWater",System.currentTimeMillis()+7200000+"");
        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+7200000 , pendingIntent1);
        }
        else if (Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+7200000, pendingIntent1);
        }
        else{}
    }
    public void setWalkReminder(Context context ,Intent intent){
        intent.putExtra("Type",2);
        intent.setAction("Remind");
        Log.e("i",intent.getIntExtra("Type",0)+"");
        Intent intent1=new Intent(context,StepMonitor.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,222,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification noti = new Notification.Builder(context)
                .setContentTitle("Time to take Walk !")
                .setStyle(new Notification.BigTextStyle().bigText("Its time to take some break and have some walk! Stay active,stay fit!"))
                .setSmallIcon(R.drawable.walker)
                .setContentIntent(pendingIntent)
                .setSound(alarmSound)
                .build();
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(2, noti);
        PendingIntent pendingIntent1=PendingIntent.getBroadcast(context.getApplicationContext(),222,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager= (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Log.e("Time newWalk",System.currentTimeMillis()+86400000+"");
        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+86400000 , pendingIntent1);
        }
        else if (Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+86400000, pendingIntent1);
        }
        else{}
    }
    public void setEatReminder(Context context ,Intent intent){
        intent.putExtra("Type",3);
        intent.setAction("Remind");
        Intent intent1=new Intent(context,FoodMonitor.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Random r=new Random();
        int i=r.nextInt();
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,i,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification noti = new Notification.Builder(context)
                .setContentTitle("Time to Eat !")
                .setStyle(new Notification.BigTextStyle().bigText("Now take some time to feed up our body! Stay fit,stay healthy!"))
                .setSmallIcon(R.drawable.food)
                .setContentIntent(pendingIntent)
                .setSound(alarmSound)
                .build();
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(3, noti);
        PendingIntent pendingIntent1=PendingIntent.getBroadcast(context.getApplicationContext(),i,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager= (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Log.e("Time new",System.currentTimeMillis()+86400000+"");
        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+86400000 , pendingIntent1);
        }
        else if (Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+86400000, pendingIntent1);
        }
        else{}
    }
    public void setExerciseReminder(Context context,Intent intent){
        intent.putExtra("Type",4);
        intent.setAction("Remind");
        Intent intent1=new Intent(context,Exercise.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Random r=new Random();
        int i=r.nextInt();
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,i,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification noti = new Notification.Builder(context)
                .setContentTitle("Time to Exercise !")
                .setSmallIcon(R.drawable.execsize)
                .setStyle(new Notification.BigTextStyle().bigText("Now take some time to shake your body and feel active! Stay fit,stay healthy!"))
                .setContentIntent(pendingIntent)
                .setSound(alarmSound)
                .build();
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(4, noti);
        PendingIntent pendingIntent1=PendingIntent.getBroadcast(context.getApplicationContext(),i,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager= (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Log.e("Time new",System.currentTimeMillis()+86400000+"");
        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+86400000 , pendingIntent1);
        }
        else if (Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+86400000, pendingIntent1);
        }
        else{}
    }
    public void setStepRecount(Context context,Intent intent){
        SQLiteDatabase db=context.openOrCreateDatabase("MyTemp.db", MODE_PRIVATE ,null);
        SharedPreferences sharedPreferences;
        sharedPreferences=context.getSharedPreferences("StepCounter",0);
        int stepCount=0;
        if(sharedPreferences.contains("count")){
            Log.e("onS",sharedPreferences.getInt("count",0)+"");
            stepCount=sharedPreferences.getInt("count",0);
        }
        Date d=new Date();
        d.setDate(d.getDate()-1);
        SimpleDateFormat dt=new SimpleDateFormat("yyyy-MM-dd");
        String date=dt.format(d);
        Log.e("DAte",date);
        UserDatabase.updateStep(db,date,stepCount);
        sharedPreferences=context.getSharedPreferences("StepCounter",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        //editor.putInt("countNotSet",stepCount);
        editor.putInt("count",0);
        editor.apply();
        if(sharedPreferences.contains("count")){
            Log.e("Count",sharedPreferences.getInt("count",0)+"");
            //Log.e("Count",sharedPreferences.getInt("countNotSet",0)+"");
        }
        intent.putExtra("Type",5);
        intent.setAction("Sync");
        PendingIntent pendingIntent=PendingIntent.getBroadcast(context,555,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager= (AlarmManager) context.getSystemService(ALARM_SERVICE);
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