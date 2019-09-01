package com.example.priyam.databaselogin;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class Gym extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym);
        setTitle("Exercise");
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        SliderAdapterGym sliderAdapterGym=new SliderAdapterGym(this);
        viewPager.setAdapter(sliderAdapterGym);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);
    }
}
