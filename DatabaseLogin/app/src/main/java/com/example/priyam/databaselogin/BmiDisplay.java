package com.example.priyam.databaselogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BmiDisplay extends AppCompatActivity {
    TextView rangeText,bm,bmiStatus;
    int mideal,munder,mover,mobese;
    int myW;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    String name,ag,ph,we,wef,he,ci,da,g,textViewBmi;
    int cm,devicewidth,barWidth;
    float convertCmToMeters,bmi,weight;
    LinearLayout l,obese,underweight,ideal,overweight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_display);
        setTitle("Your BMI");
        devicewidth=getScreenWidth();
        barWidth=devicewidth/4;

        try {
            pref=getSharedPreferences("Width",MODE_PRIVATE);
            editor=pref.edit();
            rangeText= (TextView) findViewById(R.id.range);
            bmiStatus = (TextView) findViewById(R.id.bmiStatus);
            l=(LinearLayout)findViewById(R.id.layoutBmi);
            Intent i = getIntent();
            we = i.getStringExtra("Weight");
            he = i.getStringExtra("Height");
            cm = Integer.parseInt(he);
            convertCmToMeters = (float) cm / 100;
            weight = Float.parseFloat(we);
            bmi = ((weight) / (convertCmToMeters * convertCmToMeters));
            textViewBmi = String.valueOf(bmi);

            setBmi();
            getSmthin((LinearLayout) findViewById(R.id.underweight),1);
            getSmthin((LinearLayout) findViewById(R.id.ideal),2);
            getSmthin((LinearLayout) findViewById(R.id.obese),4);
            getSmthin((LinearLayout) findViewById(R.id.overweight),3);

        } catch (Exception e) {
            Toast.makeText(this, "Exception", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setArrow();
    }

    public void getSmthin(LinearLayout param,int type){
        final LinearLayout finalLayout1 = param;

        ViewTreeObserver vto = finalLayout1.getViewTreeObserver();
        switch (type){
            case 1:vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    finalLayout1.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    int width  = finalLayout1.getMeasuredWidth();
                    //int height = finalLayout1.getMeasuredHeight();
                    editor.putInt("under",width);
                    editor.apply();
                    Log.e(finalLayout1.getId()+"",width+"   ");
                }
            });break;
            case 2:vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    finalLayout1.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    int width  = finalLayout1.getMeasuredWidth();
                    //int height = finalLayout1.getMeasuredHeight();
                    editor.putInt("ideal",width);
                    editor.apply();
                    Log.e(finalLayout1.getId()+"",width+"   ");
                }
            });break;
            case 3:vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    finalLayout1.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    int width  = finalLayout1.getMeasuredWidth();
                    //int height = finalLayout1.getMeasuredHeight();
                    editor.putInt("over",width);
                    editor.apply();
                    Log.e(finalLayout1.getId()+"",width+"   ");
                }
            });break;
            case 4:vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    finalLayout1.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    int width  = finalLayout1.getMeasuredWidth();
                    //int height = finalLayout1.getMeasuredHeight();
                    editor.putInt("obese",width);
                    editor.apply();
                    Log.e(finalLayout1.getId()+"",width+"   ");
                }
            });break;

        }
    }
    private void setBmi() {
        if(bmi<18.5){
            l.setBackgroundResource(R.drawable.roundedgeunderweight);
            bmiStatus.setBackgroundColor(Color.parseColor("#1dbdee"));
            bmiStatus.setText("Based on your information your BMI calculated is: "+bmi+".\nYour are underweight!\nDont worry we got you covered");
        }else if(bmi>=18.5 && bmi<=25){
            l.setBackgroundResource(R.drawable.roundedgeideal);
            bmiStatus.setBackgroundColor(Color.parseColor("#32cd32"));
            bmiStatus.setText("Based on your information your BMI calculated is: "+bmi+".\nYou are going well!\nJoin us and lead a even fitter life");
        }else if(bmi>=25&& bmi<=30){
            l.setBackgroundResource(R.drawable.roundedgeoverweight);
            bmiStatus.setBackgroundColor(Color.parseColor("#e6da07"));
            bmiStatus.setText("Based on your information your BMI calculated is: "+bmi+".\nYou are slightly overweight!\nDont worry we got you covered");
        }else {
            l.setBackgroundResource(R.drawable.roundedgeobese);
            bmiStatus.setBackgroundColor(Color.parseColor("#d50000"));
            bmiStatus.setText("Based on your information your BMI calculated is: "+bmi+".\nYou are in the range of obese!\nDont worry we got you covered");
        }
    }
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public void setArrow(){
        LinearLayout layout;
        LinearLayout.LayoutParams params;
        int percent,margin;
        if(bmi<18.5) {
            percent = (int) ((bmi / 18.5) * 100);
        } else if(bmi>=18.5 && bmi<=25){
            percent=(int)(((bmi-18.5)/(25-18.5))*100);
        }else if(bmi>=25&& bmi<=30){
            percent=(int)(((bmi-25)/(30-25))*100);
        }else {
            percent=(int)(((bmi-30)/(40-30))*100);
        }
        margin=((percent*179)/100)-27;
        if(bmi<18.5){
            margin=0;
        }
        else if(bmi>=18.5 && bmi<=25){
            margin+=pref.getInt("under",0);
            Log.e("calM",margin+"");
        }else if(bmi>=25&& bmi<=30){
            margin+=pref.getInt("under",0);
            margin+=pref.getInt("ideal",0);

            Log.e("calM",margin+"");

        }else {
            margin+=pref.getInt("under",0);
            margin+=pref.getInt("ideal",0);
            margin+=pref.getInt("over",0);

            Log.e("calM",margin+"");
        }
        Log.e("margin",margin+"");
        layout = (LinearLayout)findViewById(R.id.arrowLayout);
        params = (LinearLayout.LayoutParams) layout.getLayoutParams();
        if(margin>=(devicewidth-28))margin=devicewidth-35;
        Log.e("margin",margin+"");
        params.setMargins(margin,0,0,0);
    }

    public void next(View v){
        Intent i=new Intent(getApplicationContext(),UserType.class);
        startActivity(i);
    }
    public void back(View v){
        finish();
    }
}