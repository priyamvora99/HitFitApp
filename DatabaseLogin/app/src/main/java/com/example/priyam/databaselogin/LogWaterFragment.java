package com.example.priyam.databaselogin;


import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import me.itangqi.waveloadingview.WaveLoadingView;

/**
 * Created by Pinakin on 02-03-2018.
 */

public class LogWaterFragment extends Fragment {
    WaveLoadingView waveLoadingView;
    ImageButton add,delete;
    SharedPreferences sharedPreferences;
    SQLiteDatabase db;
    TextView countWater;
    String date=null;
    Boolean exists;
    private static int count,max_count=12,a;
    private static String TAG="LogActivity";

    public LogWaterFragment(SQLiteDatabase d){
        db=d;
        Log.e("Db",db.toString());
    }
    public LogWaterFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        return inflater.inflate(R.layout.log_water_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        add= (ImageButton) getView().findViewById(R.id.add);

        countWater= (TextView) getView().findViewById(R.id.countWater);
        delete= (ImageButton) getView().findViewById(R.id.delete);
        waveLoadingView= (WaveLoadingView) getView().findViewById(R.id.waveLoadingView);
        setOnView();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a = 0;
                if (count == 0 && count < max_count) {
                    if (date != null) {
                        if (!exists) {
                            count++;
                            UserDatabase.insertTable(db, count, date);
                            exists = true;
                        } else {
                            count = UserDatabase.getData(db, date);
                            count++;
                            UserDatabase.updateTable(db, count, date);
                        }
                        waveLoadingView.setProgressValue(count*8);
                        //Toast.makeText(getActivity(), "Inserted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Please select date", Toast.LENGTH_LONG).show();
                    }
                    countWater.setText(String.valueOf(count));
                } else {
                    if (count >= max_count) {
                        Toast.makeText(getActivity(), "Its full, but you can keep going!", Toast.LENGTH_LONG).show();
                    } else {
                        if (exists) {
                            count = UserDatabase.getData(db, date);
                            count++;
                            waveLoadingView.setProgressValue(count*8);
                            UserDatabase.updateTable(db, count, date);
                        }
                        //Toast.makeText(getActivity(), "Inserted", Toast.LENGTH_LONG).show();
                    }
                    countWater.setText(String.valueOf(count));
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a=0;
                if(count<=0){
                    Toast.makeText(getActivity(),"Nothing to Delete",Toast.LENGTH_LONG).show();
                }else {
                    if (UserDatabase.getIfDateExists(db, date)) {
                        count = UserDatabase.getData(db, date);
                        count--;
                        waveLoadingView.setProgressValue(count*8);
                        UserDatabase.updateTable(db, count, date);
                    }
                }
                countWater.setText(String.valueOf(count));
            }
        });

    }
    public void setOnView(){
        count = UserDatabase.getData(db, date);
        waveLoadingView.setProgressValue(count*8);
        countWater.setText(String.valueOf(count));
    }
    public boolean getData(Boolean e, String d, int c) {
        exists=e;
        date=d;
        count=c;
       if(LogWaterFragment.this.isVisible())
        {
            waveLoadingView.setProgressValue(count*8);
            countWater.setText(String.valueOf(count));
            return true;
        }
        else return false;
    }

}
