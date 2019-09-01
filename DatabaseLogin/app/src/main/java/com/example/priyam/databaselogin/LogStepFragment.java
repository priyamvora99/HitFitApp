package com.example.priyam.databaselogin;

import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Pinakin on 25-02-2018.
 */

public class LogStepFragment extends Fragment implements SensorEventListener{
    private TextView tvc;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        return inflater.inflate(R.layout.log_step_fragment, container, false);

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ImageView mImageViewWalking = (ImageView) getView().findViewById(R.id.walkingAnime);
        ((AnimationDrawable) mImageViewWalking.getBackground()).start();
        tvc= (TextView) getView().findViewById(R.id.steps);
        //changeStep();
    }
    @Override
    public void onStart(){
        super.onStart();
        changeStep();

    }
    public void changeStep(){
        SharedPreferences sharedPreferences;
        sharedPreferences=getActivity().getSharedPreferences("StepCounter",0);
        if(sharedPreferences.contains("count")){
            tvc.setText(sharedPreferences.getInt("count",0)+"");
            Log.e("onS",sharedPreferences.getInt("count",0)+"");
            Log.e("onS",sharedPreferences.getInt("countNotSet",0)+"");


        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.e("inSensorchange","yeah");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //((Animationrawable) mImageViewWalking.getBackground()).start();
}
