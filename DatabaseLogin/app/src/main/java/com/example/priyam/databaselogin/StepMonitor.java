package com.example.priyam.databaselogin;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class StepMonitor extends AppCompatActivity implements SensorEventListener{
    ImageView mImageViewWalking;
    FrameLayout mFrame;
    LogStepFragment stepLog;
    AnalysisStepFragment stepAna;
    TextView logT,anaT;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_monitor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db=openOrCreateDatabase("MyTemp.db", Context.MODE_PRIVATE,null);
        startService(new Intent(this,StepCounter.class));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getId();
        addInitialFrame();
    }

    public void reminder(View v){
        Intent i=new Intent(getApplicationContext(),WalkReminder.class);
        startActivity(i);
    }

    public void showLog(View v){
        logT.setTextColor(Color.WHITE);
        anaT.setTextColor(getResources().getColor(R.color.translucent));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();// begin  FragmentTransaction
        ft.replace(R.id.frameLayout,stepLog);
        ft.commit();
    }
    public void showAnalysis(View v){
        anaT.setTextColor(Color.WHITE);
        logT.setTextColor(getResources().getColor(R.color.translucent));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();// begin  FragmentTransaction
        ft.replace(R.id.frameLayout,stepAna);
        ft.commit();
    }
    public void getId(){
        logT= (TextView) findViewById(R.id.log);
        anaT= (TextView) findViewById(R.id.analysis);
        mFrame= (FrameLayout) findViewById(R.id.frameLayout);
    }
    public void addInitialFrame(){
        stepLog=new LogStepFragment();
        stepAna=new AnalysisStepFragment();
        logT.setTextColor(Color.WHITE);
        stepAna.putDb(db);
        anaT.setTextColor(getResources().getColor(R.color.translucent));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();// begin  FragmentTransaction
        ft.add(R.id.frameLayout, stepLog );
        ft.commit();
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        stepLog.changeStep();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
