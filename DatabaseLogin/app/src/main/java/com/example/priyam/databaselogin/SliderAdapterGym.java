package com.example.priyam.databaselogin;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


class SliderAdapterGym extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private int[] image= {
            R.drawable.walking,
            R.drawable.jog,
            R.drawable.cycle,
            R.drawable.pushup,
            R.drawable.skip,
    };

    private String[] list_descr4={
            "Walking",
            "Jogging",
            "Cycling",
            "Push-Ups",
            "Skipping",
    };


    private int[] list_color2= {
            Color.rgb(255, 255, 255),
            Color.rgb(255, 255, 255),
            Color.rgb(255, 255, 255),
            Color.rgb(255, 255, 255),
            Color.rgb(255, 255, 255),

    };

    @Override
    public int getCount() {
        return list_descr4.length;
    }
    public SliderAdapterGym(Context context){
        this.context=context;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slide_gym,container,false);
        LinearLayout linearLayout=(LinearLayout)view.findViewById(R.id.yoga_text);
        ImageView imageView=(ImageView)view.findViewById(R.id.imageview);
        TextView des3=(TextView)view.findViewById(R.id.textDescription1);
        linearLayout.setBackgroundColor(list_color2[position]);
        imageView.setImageResource(image[position]);
        des3.setText(list_descr4[position]);
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view== object);
    }
}
