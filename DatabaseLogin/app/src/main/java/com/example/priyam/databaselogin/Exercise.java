package com.example.priyam.databaselogin;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
public class Exercise extends AppCompatActivity {
    RelativeLayout surya,yoga,gym;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //setTitle("Exercises");
        getId();
    }
    public void reminder(View v){
        i=new Intent(getApplicationContext(),ExerciseReminder.class);
        startActivity(i);
    }
    public void getId(){
        surya= (RelativeLayout) findViewById(R.id.suryanamaskarL);
        yoga= (RelativeLayout) findViewById(R.id.yogaL);
        gym= (RelativeLayout) findViewById(R.id.gymL);
        yoga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=new Intent(getApplicationContext(),Yoga.class);
                startActivity(i);
            }
        });
        gym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=new Intent(getApplicationContext(),Gym.class);
                startActivity(i);
            }
        });
        surya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=new Intent(getApplicationContext(),Suryanamaskar.class);
                startActivity(i);
            }
        });
    }
}