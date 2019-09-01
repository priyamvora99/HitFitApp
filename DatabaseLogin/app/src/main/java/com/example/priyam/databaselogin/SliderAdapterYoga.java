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


class SliderAdapterYoga extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private int[] images= {
            R.drawable.hal,
            R.drawable.mala,
            R.drawable.sira,
            R.drawable.ustra,
            R.drawable.tri,
    };

    private String[] list_descr2={
            "Halasana",
            "Malasana",
            "Sirasana",
            "Ustranasana",
            "Trikonasana",
    };


    private int[] list_color1= {
            Color.rgb(255, 255, 255),
            Color.rgb(255, 255, 255),
            Color.rgb(255, 255, 255),
            Color.rgb(255, 255, 255),
            Color.rgb(255, 255, 255),

    };

    @Override
    public int getCount() {
        return list_descr2.length;
    }
    public SliderAdapterYoga(Context context){
        this.context=context;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slide_yoga,container,false);
        LinearLayout linearLayout=(LinearLayout)view.findViewById(R.id.yoga_text);
        ImageView imageView=(ImageView)view.findViewById(R.id.imageview);
        TextView des3=(TextView)view.findViewById(R.id.textDescription1);
        linearLayout.setBackgroundColor(list_color1[position]);
        imageView.setImageResource(images[position]);
        des3.setText(list_descr2[position]);
        container.addView(view);

        return view;

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view== object);
    }
}
