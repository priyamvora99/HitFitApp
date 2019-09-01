package com.example.priyam.databaselogin;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public class ExerciseReminder extends AppCompatActivity {
    TextView t1,t2,t3,t4,t5;
    LinearLayout timeL;
    long time1=0,time2=0,time3=0,time5=0,time4=0;
    Switch mExRemind;
    int id=222;
    Date d;
    Boolean b;
    Button set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_reminder);
        setTitle("Exercise Reminder");
        getID();
        SharedPreferences pref=getSharedPreferences("Reminders",MODE_PRIVATE);
        b=pref.getBoolean("ExerciseReminder",false);
        if(b){
            mExRemind.setChecked(b);
            timeL.setVisibility(View.VISIBLE);
            SharedPreferences prefs=getSharedPreferences("Timings",MODE_PRIVATE);
            time1=prefs.getLong("Suryanamaskar",0);
            time2=prefs.getLong("Yoga",0);
            time3=prefs.getLong("Gym",0);
            if(time1!=0)t1.setText(new SimpleDateFormat("HH:mm").format(time1)+"");
            else  t1.setText("Click here to select time");
            if (time2!=0)t2.setText(new SimpleDateFormat("HH:mm").format(time2)+"");
            else t2.setText("Click here to select time");
            if (time3!=0)t3.setText(new SimpleDateFormat("HH:mm").format(time3)+"");
            else t3.setText("Click here to select time");
        }
        else {
            mExRemind.setChecked(b);
            t1.setText("Click here to select time");
            t2.setText("Click here to select time");
            t3.setText("Click here to select time");
        }
        mExRemind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    timeL.setVisibility(View.VISIBLE);
                    if(b)set.setText("Done");
                    else set.setText("Apply Changes");
                }else {
                    timeL.setVisibility(View.GONE);
                    if(b)set.setText("Apply Changes");
                    else set.setText("Done");
                }
            }
        });
    }
    public void selectTime(View v){
        d=new Date();
        switch (v.getId()){
            case R.id.timeSurya:setTime(t1,1);Log.e("select1",time1+"");break;
            case R.id.timeYoga:setTime(t2,2);Log.e("select2",time2+"");break;
            case R.id.timeGym:setTime(t3,3);Log.e("select3",time3+"");break;
        }
    }
    public void setAlarm(int type){
        id+=111;
        SharedPreferences pref=getSharedPreferences("Timings",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        String t=null;
        if (type==1) {
            t = "Suryanamaskar";
            Log.e("alarm1",time1+"");
            alarmToBeSet(time1,id);
            editor.putLong(t,time1);editor.apply();
        }
        if (type==2){
            t="Yoga";
            Log.e("alarm2",time2+"");
            alarmToBeSet(time2,id);
            editor.putLong(t,time2);editor.apply();
        }
        if (type==3){
            t="Gym";
            Log.e("alarm3",time3+"");
            alarmToBeSet(time3,id);
            editor.putLong(t,time3);editor.apply();
        }
    }
    public void alarmToBeSet(long time,int id){
        Intent intent=new Intent(getApplicationContext(),Notification_receiver.class);
        intent.putExtra("Type",4);
        intent.setAction("Remind");
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= 23)
        {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,time , pendingIntent);
        }
        else if (Build.VERSION.SDK_INT >= 19)
        {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,time , pendingIntent);
        }   else{}
        Toast.makeText(getApplicationContext(),"Alarm set at: "+(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(time)),Toast.LENGTH_LONG).show();
    }
    public void setReminder(View v){
        if(mExRemind.isChecked()) {
                SharedPreferences pref=getSharedPreferences("Reminders",MODE_PRIVATE);
                SharedPreferences.Editor editor=pref.edit();
                editor.putBoolean("ExerciseReminder",mExRemind.isChecked());
                editor.apply();
                if(time1!=0)setAlarm(1);
                if(time2!=0)setAlarm(2);
                if(time3!=0)setAlarm(3);
                finish();
        }else{
            Intent intent=new Intent(getApplicationContext(),Notification_receiver.class);
            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),333,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            SharedPreferences pref=getSharedPreferences("Reminders",MODE_PRIVATE);
            SharedPreferences.Editor editor=pref.edit();
            editor.putBoolean("ExerciseReminder",mExRemind.isChecked());
            editor.apply();
            pref=getSharedPreferences("Timings",MODE_PRIVATE);
            editor=pref.edit();
            editor.putLong("Suryanamaskar",0);
            editor.putLong("Yoga",0);
            editor.putLong("Gym",0);
            editor.apply();
        }
    }
    public void setTime(final TextView timeT, final int t){
        final Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ExerciseReminder.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                timeT.setText( ""+selectedHour + ":" + selectedMinute);
                d.setHours(selectedHour);
                d.setMinutes(selectedMinute);
                if (d.getTime()<System.currentTimeMillis()) {
                    d.setDate(d.getDate()+1);
                    Log.e("time", d.getTime() + "");
                }
                else{
                    Log.e("time", d.getTime() + "");
                }
                switch(t){
                    case 1:time1=d.getTime();Log.e("select1",time1+"");break;
                    case 2:time2=d.getTime();Log.e("select1",time2+"");break;
                    case 3:time3=d.getTime();Log.e("select1",time3+"");break;
                }
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
    public void getID() {
        t1= (TextView) findViewById(R.id.timeSurya);
        t2= (TextView) findViewById(R.id.timeYoga);
        t3= (TextView) findViewById(R.id.timeGym);
        mExRemind= (Switch) findViewById(R.id.switchRemind);
        timeL= (LinearLayout) findViewById(R.id.timings);
        set= (Button) findViewById(R.id.exerciseReminderButton);
    }
}