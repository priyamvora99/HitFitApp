package com.example.priyam.databaselogin;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
public class Reminders extends AppCompatActivity {
    CardView food,water,walk,exer;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        setTitle("Reminders");
        food= (CardView) findViewById(R.id.food);
        water= (CardView) findViewById(R.id.water);
        walk= (CardView) findViewById(R.id.walk);
        exer= (CardView) findViewById(R.id.exercise);
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=new Intent(getApplicationContext(),FoodReminder.class);
                startActivity(i);
            }
        });
        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=new Intent(getApplicationContext(),WalkReminder.class);
                startActivity(i);
            }
        });
        exer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=new Intent(getApplicationContext(),ExerciseReminder.class);
                startActivity(i);
            }
        });
        water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=new Intent(getApplicationContext(),WaterReminder.class);
                startActivity(i);
            }
        });
    }
}