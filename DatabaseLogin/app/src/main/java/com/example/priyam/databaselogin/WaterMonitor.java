package com.example.priyam.databaselogin;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WaterMonitor extends AppCompatActivity {

    int count=0,a;
    FrameLayout mFrame;
    ImageView remind;
    LogWaterFragment waterLog;
    AnalysisWaterFragment waterAna;
    TextView d,logT,anaT;
    SQLiteDatabase db;

    private int mYear,mMonth,mDay;
    private String date,day,month;
    private static String TAG="WaterMonitor";
    private boolean exists,check=false,check1=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_monitor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db=openOrCreateDatabase("MyTemp.db", Context.MODE_PRIVATE,null);
        count=0;
        getID();
        setStatusBarColor(findViewById(R.id.statusBarBackground),R.drawable.water_gradient);
    }
    public void setStatusBarColor(View statusBar,int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //status bar height

            statusBar.setBackgroundColor(color);
        }
    }

    public void reminder(View v){
        Intent i=new Intent(getApplicationContext(),WaterReminder.class);
        startActivity(i);
    }

    public void showLog(View v){
        logT.setTextColor(Color.WHITE);
        anaT.setTextColor(getResources().getColor(R.color.translucent));
        waterLog.getData(exists,date,count);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();// begin  FragmentTransaction
        ft.replace(R.id.frameLayout,waterLog);
        ft.commit();
    }
    public void showAnalysis(View v){
        waterAna.getData(exists,date,count);
        anaT.setTextColor(Color.WHITE);
        logT.setTextColor(getResources().getColor(R.color.translucent));

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();// begin  FragmentTransaction
        ft.replace(R.id.frameLayout,waterAna);
        ft.commit();
    }

    public void getID() {
        remind= (ImageView) findViewById(R.id.reminder);
        mFrame= (FrameLayout) findViewById(R.id.frameLayout);
        d=(TextView)findViewById(R.id.date);
        logT= (TextView) findViewById(R.id.log);
        anaT= (TextView) findViewById(R.id.analysis);
        addInitialFrame();
    }
    public void selectDate(View v){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        if(dayOfMonth<=9){
                            day="0";
                            day=day+String.valueOf(dayOfMonth);
                            check=true;
                        }if(monthOfYear<9){
                            month="0";
                            month=month+String.valueOf(monthOfYear+1);
                            check1=true;
                        }
                        if(check&&check1){
                            date=day + "-" + (month) + "-" + year;
                            d.setText(date);
                        }else if(check1) {
                            date = dayOfMonth + "-" + month + "-" + year;
                            d.setText(date);
                        }else if(check){
                            date = day + "-" + month + "-" + year;
                            d.setText(date);
                        }
                        else{
                            date = dayOfMonth + "-" + (monthOfYear+1) + "-" + year;
                            d.setText(date);
                        }
                        check=false;
                        check1=false;

                        if(UserDatabase.getIfDateExists(db,date)){
                            count=UserDatabase.getData(db,date);
                            exists=true;
                        }else{
                            exists=false;
                            count=0;
                        }
                        if(waterLog.getData(exists,date,count)){}
                        if(waterAna.getData(exists,date,count))waterAna.showPieChart();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
    public void addInitialFrame(){
        waterLog=new LogWaterFragment(db);
        waterAna=new AnalysisWaterFragment(db);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();// begin  FragmentTransaction
        ft.add(R.id.frameLayout, waterLog );
        ft.commit();
        logT.setTextColor(Color.WHITE);
        anaT.setTextColor(getResources().getColor(R.color.translucent));
        //set default today date
        Date d=Calendar.getInstance().getTime();
        SimpleDateFormat dt=new SimpleDateFormat("dd-MM-yyyy");
        date=dt.format(d);
        Log.e(TAG, "addInitialFrame: "+date);
        if(UserDatabase.getIfDateExists(db,date)){
            Log.e(TAG, "onDateSet: success");
            count=UserDatabase.getData(db,date);
            exists=true;
            if(waterLog.getData(exists,date,count)){}
            if(waterAna.getData(exists,date,count))waterAna.showPieChart();
        }else{
            Log.e(TAG, "onDateSet: failure");
            exists=false;
            count=0;
            if(waterLog.getData(exists,date,count)){}
            if(waterAna.getData(exists,date,count))waterAna.showPieChart();
        }
    }
}