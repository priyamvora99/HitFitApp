package com.example.priyam.databaselogin;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import static android.content.ContentValues.TAG;
/**
 * Created by Pinakin on 25-02-2018.
 */
public class AnalysisStepFragment extends Fragment {
    private SQLiteDatabase db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        return inflater.inflate(R.layout.analysis_step_fragment, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showBarChart();
    }
    public void putDb(SQLiteDatabase d){
        this.db=d;
    }
    public class MyAxisValueFormatter implements IAxisValueFormatter {
        private String[] mDates;
        MyAxisValueFormatter(String[] dates ){
            mDates=dates;
        }
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            //Log.e(TAG, "getFormattedValue: "+value );
            return mDates[((int)value)];
        }
    }
    private void showBarChart(){
        Cursor c=null;
        BarChart chart = (BarChart) getView().findViewById(R.id.bar_chart);
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.setFitBars(true);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.setScaleEnabled(false);
        chart.setHighlightFullBarEnabled(false);
        Description description = new Description();
        description.setText("");
        chart.setDescription(description);
        YAxis right = chart.getAxisRight();
        right.setEnabled(false);
        YAxis left=chart.getAxisLeft();
        left.setAxisMinimum(0f);
        chart.animateX(1000, Easing.EasingOption.EaseInOutCubic);
        chart.animateY(2000, Easing.EasingOption.EaseInOutCubic);
        String[] dateArray=new String[6];
        dateArray=UserDatabase.getPreviousDates(db);
        Log.e(TAG, "showBarChart: "+dateArray[0].toString() );
        Log.e(TAG, "showBarChart: "+dateArray[1].toString() );
        Log.e(TAG, "showBarChart: "+dateArray[2].toString() );
        Log.e(TAG, "showBarChart: "+dateArray[3].toString() );
        Log.e(TAG, "showBarChart: "+dateArray[4].toString() );
        Log.e(TAG, "showBarChart: "+dateArray[5].toString() );
        Log.e(TAG, "showBarChart: "+dateArray[6].toString() );
        int i=0;
        ArrayList<Integer> arrayList=new ArrayList<>();
        Log.e(TAG, "showBarChart: "+dateArray.length );
        for(i=0;i<dateArray.length-1;i++){
            Log.e(TAG, "showBarChart: in for loop" );
            c=UserDatabase.getStepData(db,dateArray[i]);
            if(c.getCount()!=0){
                Log.e(TAG, "showBarChart: in if" );
                while(c.moveToNext()){
                    Log.e(TAG, "showBarChart:"+ c.getInt(1) );
                    arrayList.add(c.getInt(1));
                    Log.e(TAG, "onCreate: "+arrayList.toString() );
                }
            }else{
                //UserDatabase.updateStep(db,dateArray[i],0);
                arrayList.add(0);
                Log.e(TAG, "showBarChart: in else" );
            }
        }
        SharedPreferences sharedPreferences;
        sharedPreferences=getActivity().getSharedPreferences("StepCounter",0);
        if(sharedPreferences.contains("count")){
            Log.e("in shared",sharedPreferences.getInt("count",0)+"");
            arrayList.add(sharedPreferences.getInt("count",0));
        }else{
            arrayList.add(0);
        }
        ArrayList<BarEntry> BarEntry = new ArrayList<>();
        for(int j=0;j<dateArray.length;j++){
            BarEntry.add(new BarEntry((float)j, arrayList.get(j)));
        }
        /*BarEntry.add(new BarEntry(1f, arrayList.get(1)));
        BarEntry.add(new BarEntry(2f, arrayList.get(2)));
        BarEntry.add(new BarEntry(3f, arrayList.get(3)));
        BarEntry.add(new BarEntry(4f, arrayList.get(4)));
        BarEntry.add(new BarEntry(5f, arrayList.get(5)));
        BarEntry.add(new BarEntry(6f, arrayList.get(6)));*/
        BarDataSet dataSet = new BarDataSet(BarEntry, "Walk Analysis-Past Seven days");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.5f);
        chart.setData(data);
        XAxis xAxis=chart.getXAxis();
        String[] date={dateArray[0],dateArray[1],dateArray[2],dateArray[3],dateArray[4],dateArray[5],dateArray[6]};
        xAxis.setLabelRotationAngle(40f);
        xAxis.setTextSize(10f);
        xAxis.setValueFormatter(new MyAxisValueFormatter(date));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }
}