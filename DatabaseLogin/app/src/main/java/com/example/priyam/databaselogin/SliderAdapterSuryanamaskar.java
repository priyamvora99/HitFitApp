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


public class SliderAdapterSuryanamaskar extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    public int[] lst_images={
            R.drawable.mountain,
            R.drawable.handsupjpg,
            R.drawable.down,
            R.drawable.lunge,
            R.drawable.plank,
            R.drawable.stick,
            R.drawable.upward,
            R.drawable.downward,
            R.drawable.lunge,
            R.drawable.down,
            R.drawable.handsupjpg,
            R.drawable.mountain,

    };
    public String[] list_descr1={
            "Pranamasana",
            "Hastauttanasana",
            "Padasana",
            "Sanchalanasana",
            "Dandasana",
            "Ashtanga Namaskara",
            "Bhujangasana",
            "Parvatasana",
            "Sanchalanasana",
            "Hasta Padasana",
            "Hastauttanasana",
            "Tadasana",
    };
    public String[] list_descr={
            "1st step of Suryanamaskar !",
            "2nd step of Suryanamaskar !",
            "3nd step of Suryanamaskar !",
            "4th step of Suryanamaskar !",
            "5th step of Suryanamaskar !",
            "6th step of Suryanamaskar !",
            "7th step of Suryanamaskar !",
            "8th step of Suryanamaskar !",
            "9th step of Suryanamaskar !",
            "10th step of Suryanamaskar !",
            "11th step of Suryanamaskar !",
            "12th step in Suryanamaskar !",
    };
    public int[] list_color={
            Color.rgb(255,255,255),
            Color.rgb(255,255, 255),
            Color.rgb(255,255,255),
            Color.rgb(255,255,255),
            Color.rgb(255,255,255),
            Color.rgb(255,255,255),
            Color.rgb(255,255,255),
            Color.rgb(255,255,255),
            Color.rgb(255,255,255),
            Color.rgb(255,255,255),
            Color.rgb(255,255,255),
            Color.rgb(255,255,255),


    };
    public int[] images= {
            R.drawable.hal,
            R.drawable.mala,
            R.drawable.sira,
            R.drawable.ustra,
            R.drawable.tri,
    };

    public String[] list_descr2={
            "Halasana",
            "Malasana",
            "Sirasana",
            "Ustranasana",
            "Trikonasana",
    };

    public int[] list_color1= {
            Color.rgb(255, 255, 255),
            Color.rgb(255, 255, 255),
            Color.rgb(255, 255, 255),
            Color.rgb(255, 255, 255),
            Color.rgb(255, 255, 255),
            Color.rgb(255, 255, 255),
            Color.rgb(255, 255, 255),
    };

    @Override
    public int getCount() {
        return list_descr.length;
    }
    public SliderAdapterSuryanamaskar(Context context){
        this.context=context;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slide_surya,container,false);
        LinearLayout linearLayout=(LinearLayout)view.findViewById(R.id.slide);
        ImageView imageView=(ImageView)view.findViewById(R.id.imageview);
        TextView des=(TextView)view.findViewById(R.id.textDescription);
        TextView des1=(TextView)view.findViewById(R.id.textDescription1);
        linearLayout.setBackgroundColor(list_color[position]);
        imageView.setImageResource(lst_images[position]);
        des.setText(list_descr[position]);
        des1.setText(list_descr1[position]);
        container.addView(view);

        return view;

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(LinearLayout)object);
    }
}
