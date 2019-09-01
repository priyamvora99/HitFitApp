package com.example.priyam.databaselogin;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
public class Yoga extends AppCompatActivity {
    ViewPager viewPager;
    SliderAdapterYoga sliderAdapteryoga;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga);
        setTitle("Exercise");
        viewPager=(ViewPager)findViewById(R.id.viewpager);
        sliderAdapteryoga=new SliderAdapterYoga(this);
        viewPager.setAdapter(sliderAdapteryoga);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);
    }
}