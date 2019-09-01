package com.example.priyam.databaselogin;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class Suryanamaskar extends AppCompatActivity {
    ViewPager viewPager;
    SliderAdapterSuryanamaskar slideAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suryanamaskar);
        setTitle("Exercise");
        viewPager=(ViewPager)findViewById(R.id.viewpager);
        slideAdapter=new SliderAdapterSuryanamaskar(this);
        viewPager.setAdapter(slideAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);
    }
}
