package com.example.priyam.databaselogin;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FoodMonitor extends AppCompatActivity {
    LogFoodFragment foodLog;
    AnalysisFoodFragment foodAna;
    TextView logT,anaT,d;
    ImageView remind;
    FrameLayout mFrame;
    String mdate="";
    private int mYear,mMonth,mDay;
    private String date,day,month;
    SQLiteDatabase db;
    Boolean check=false,check1=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_monitor);
        db=openOrCreateDatabase("MyTemp.db", Context.MODE_PRIVATE,null);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getID();
    }


    public void showLog(View v){
        logT.setTextColor(Color.WHITE);
        anaT.setTextColor(getResources().getColor(R.color.translucent));
        //foodLog.getData(exists,date,count);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();// begin  FragmentTransaction
        ft.replace(R.id.frameLayout,foodLog);
        ft.commit();
    }
    public void showAnalysis(View v){
        //foodAna.getData(exists,date,count);
        anaT.setTextColor(Color.WHITE);
        logT.setTextColor(getResources().getColor(R.color.translucent));

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();// begin  FragmentTransaction
        ft.replace(R.id.frameLayout,foodAna);

        ft.commit();
    }
    public void goToAddItem(View v){
        switch(v.getId()){
            case R.id.buttonBreakfast:foodLog.goToAddItem(1);break;
            case R.id.buttonBrunch:foodLog.goToAddItem(2);break;
            case R.id.buttonLunch:foodLog.goToAddItem(3);break;
            case R.id.buttonEsnack:foodLog.goToAddItem(4);break;
            case R.id.buttonDinner:foodLog.goToAddItem(5);break;
        }
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
                        Date date= new Date();
                        date.setDate(dayOfMonth);
                        date.setMonth(monthOfYear);
                        date.setYear(year-1900);

                        SimpleDateFormat dt=new SimpleDateFormat("dd-MM-yyyy");
                        mdate=dt.format(date);
                        if(foodLog.putDateAndDb(mdate,db))
                        foodLog.initialize();
                        if(foodAna.putDateAndDb(mdate,db))
                            foodAna.showRadar();
                        d.setText(mdate);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
    public void getID() {
        remind= (ImageView) findViewById(R.id.reminder);
        mFrame= (FrameLayout) findViewById(R.id.frameLayout);
        //d=(TextView)findViewById(R.id.date);
        logT= (TextView) findViewById(R.id.log);
        anaT= (TextView) findViewById(R.id.analysis);
        d= (TextView) findViewById(R.id.date);
        addInitialFrame();
    }
    public void addInitialFrame() {
        foodLog = new LogFoodFragment(this);
        foodAna = new AnalysisFoodFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();// begin  FragmentTransaction
        ft.add(R.id.frameLayout, foodLog);
        ft.commit();
        logT.setTextColor(Color.WHITE);
        anaT.setTextColor(getResources().getColor(R.color.translucent));
        //set default today date
        Date d=Calendar.getInstance().getTime();
        SimpleDateFormat dt=new SimpleDateFormat("dd-MM-yyyy");
        Log.e("da", String.valueOf(d));
        mdate=dt.format(d);
        foodLog.putDateAndDb(mdate,db);
        foodAna.putDateAndDb(mdate,db);
    }
    public void reminder(View v){
        Intent i=new Intent(getApplicationContext(),FoodReminder.class);
        startActivity(i);
    }
}