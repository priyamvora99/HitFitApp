package com.example.priyam.databaselogin;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import java.util.ArrayList;
import static android.content.ContentValues.TAG;
/**
 * Created by Pinakin on 25-02-2018.
 */
public class AnalysisFoodFragment extends Fragment {
    private SQLiteDatabase db;
    private String date;
    private RadarChart mChart;
    TextView tv;
    private Cursor day;
    private String itemName;
    private String itemTime;
    private Cursor items;
    private ArrayList<Float> radarData;
    private float calories1=0.0f;
    private float calories2=0f;
    private float calories3=0f;
    private float calories4=0f;
    private float calories5=0f;
    float totPercent=0f;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.analysis_food_fragment, container, false);
    }
    public boolean putDateAndDb(String d,SQLiteDatabase dB){
        this.date=d;
        this.db=dB;
        calories1=calories2=calories3=calories4=calories5 = 0.0f;
        day=UserDatabase.getTimeAndItemName(db,date);
        if(day.getCount()!=0){
            while(day.moveToNext()){
                itemTime=day.getString(0);
                itemName=day.getString(1);
                switch(itemTime){
                    case "Breakfast":
                        items=UserDatabase.getItem(db,itemName);
                        if(items.getCount()!=0){
                            while (items.moveToNext()){
                                calories1+=items.getFloat(2);
                            }
                        }
                        break;
                    case "Brunch":
                        items=UserDatabase.getItem(db,itemName);
                        if(items.getCount()!=0){
                            while (items.moveToNext()){
                                calories2+=items.getFloat(2);
                            }
                        }
                        break;
                    case "Lunch":
                        items=UserDatabase.getItem(db,itemName);
                        if(items.getCount()!=0){
                            while (items.moveToNext()){
                                calories3+=items.getFloat(2);
                            }
                        }
                        break;
                    case "Evening Snack":
                        items=UserDatabase.getItem(db,itemName);
                        if(items.getCount()!=0){
                            while (items.moveToNext()){
                                calories4+=items.getFloat(2);
                            }
                        }
                        break;
                    case "Dinner":
                        items=UserDatabase.getItem(db,itemName);
                        if(items.getCount()!=0){
                            while (items.moveToNext()){
                                calories5+=items.getFloat(2);
                            }
                        }
                        break;
                }
            }
        }
        radarData= new ArrayList<>();
        radarData.add(0,((calories1/650)*100));
        radarData.add(1,((calories2/250)*100));
        radarData.add(2,((calories3/650)*100));
        radarData.add(3,((calories4/250)*100));
        radarData.add(4,((calories5/650)*100));
        //if (this.isVisible())showRadar();
        return this.isVisible();
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        putDateAndDb(this.date,this.db);
        showRadar();
    }
    public void showRadar(){
        mChart = (RadarChart) getView().findViewById(R.id.chart1);
        mChart.setBackgroundColor(Color.rgb(255, 255, 255));
        mChart.getDescription().setEnabled(false);
        mChart.setWebLineWidth(1.5f);
        mChart.setWebColor(Color.BLACK);
        mChart.setWebLineWidthInner(1.5f);
        mChart.setWebColorInner(Color.BLACK);
        mChart.setWebAlpha(100);
        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MarkerView mv = new RadarMarkerView(getContext(), R.layout.radar_markerview);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart
        setData();
        mChart.animateXY(
                1400, 1400,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            private String[] mActivities = new String[]{"Morning", "Brunch", "Lunch", "Evening Snack", "Dinner"};
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }
        });
        xAxis.setTextColor(Color.BLACK);
        YAxis yAxis = mChart.getYAxis();
        yAxis.setLabelCount(5, false);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(80f);
        yAxis.setDrawLabels(false);
        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setTextSize(18f);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.BLACK);
    }
    private void setData() {
        ArrayList<RadarEntry> entries2 = new ArrayList<>();
        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < 5; i++) {
            if(radarData.get(i)<100) {
                Log.e(TAG,radarData.get(i)+"");
                entries2.add(new RadarEntry(radarData.get(i)));
            }else{
                Log.e(TAG,"in more than 100");
                entries2.add(new RadarEntry(99.99f));
            }
        }
        RadarDataSet set2 = new RadarDataSet(entries2,"Calories intake");
        set2.setColor(Color.rgb(219, 77, 45));
        set2.setFillColor(Color.rgb(219, 77, 45));
        set2.setDrawFilled(true);
        set2.setFillAlpha(150);
        set2.setLineWidth(2f);
        set2.setDrawHighlightCircleEnabled(true);
        set2.setDrawHighlightIndicators(false);
        ArrayList<IRadarDataSet> sets = new ArrayList<>();
        sets.add(set2);
        RadarData data = new RadarData(sets);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);
        mChart.invalidate();
    }
}