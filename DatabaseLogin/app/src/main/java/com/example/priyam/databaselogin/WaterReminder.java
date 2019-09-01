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
public class WaterReminder extends AppCompatActivity {
    Switch mWaterRemind;
    TextView time,timeT;
    LinearLayout timeL;
    Date d;
    long timeValue=0;
    Boolean b;
    Button set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_reminder);
        setTitle("Water Reminder");
        getId();
        d=new Date();
        SharedPreferences pref=getSharedPreferences("Reminders",MODE_PRIVATE);
        b=pref.getBoolean("WaterReminder",false);
        if(b){
            mWaterRemind.setChecked(b);
            timeL.setVisibility(View.VISIBLE);
            SharedPreferences prefs=getSharedPreferences("Timings",MODE_PRIVATE);
            time.setText(new SimpleDateFormat("HH:mm").format(prefs.getLong("WaterReminder",0))+"");
        }
        else {
            mWaterRemind.setChecked(b);
            time.setText("Click here to select time");
        }
        mWaterRemind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        final Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(WaterReminder.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                time.setText( ""+selectedHour + ":" + selectedMinute);
                d.setHours(selectedHour);
                d.setMinutes(selectedMinute);
                if (d.getTime()<System.currentTimeMillis()) {
                    d.setDate(d.getDate()+1);
                    Log.e("time", d.getTime() + "");
                }
                else{
                    Log.e("time", d.getTime() + "");
                }
                timeValue=d.getTime();
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
    public void getId() {
        mWaterRemind= (Switch) findViewById(R.id.switchRemind);
        time= (TextView) findViewById(R.id.time);
        timeL= (LinearLayout) findViewById(R.id.timeL);
        set= (Button) findViewById(R.id.waterReminderButton);
    }
    public void setReminder(View v){
        if(mWaterRemind.isChecked()){
            if(timeValue!=0) {
                SharedPreferences p = getSharedPreferences("Reminders", MODE_PRIVATE);
                    SharedPreferences pref = getSharedPreferences("Reminders", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("WaterReminder", mWaterRemind.isChecked());
                    editor.apply();
                    pref = getSharedPreferences("Timings", MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putLong("WaterReminder", timeValue);
                    editor.apply();
                    Intent intent = new Intent(getApplicationContext(), Notification_receiver.class);
                    intent.putExtra("Type", 1);
                    intent.setAction("Remind");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 111, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    if (Build.VERSION.SDK_INT >= 23) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, d.getTime(), pendingIntent);
                    } else if (Build.VERSION.SDK_INT >= 19) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, d.getTime(), pendingIntent);
                    } else {
                    }
                    Toast.makeText(getApplicationContext(), "Alarm set at: " + (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(d.getTime())), Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),"Please Select time",Toast.LENGTH_LONG).show();
            }
        }
        else{
            Intent intent=new Intent(getApplicationContext(),Notification_receiver.class);
            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),111,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            SharedPreferences pref=getSharedPreferences("Reminders",MODE_PRIVATE);
            SharedPreferences.Editor editor=pref.edit();
            editor.putBoolean("WaterReminder",mWaterRemind.isChecked());
            editor.apply();
            pref=getSharedPreferences("Timings",MODE_PRIVATE);
            editor=pref.edit();
            editor.putLong("WaterReminder",0);
            editor.apply();
        }
        finish();
    }
}