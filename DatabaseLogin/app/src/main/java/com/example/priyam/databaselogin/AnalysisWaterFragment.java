package com.example.priyam.databaselogin;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import java.util.ArrayList;
/**
 * Created by Pinakin on 02-03-2018.
 */
public class AnalysisWaterFragment extends Fragment {
    private SQLiteDatabase db;
    private boolean exists;
    private int count;
    private int max_count=12;
    private int left;
    private String date;
    private PieChart pieChart;
    private String TAG="Analysis Water Fragment";
    private String[] xData = {"Glasses Drank", "Glasses left to complete goal"};
    Description description = new Description();
    public AnalysisWaterFragment(SQLiteDatabase d){
        db=d;
    }
    public AnalysisWaterFragment(){
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        return inflater.inflate(R.layout.analysis_water_fragment, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pieChart = (PieChart) getView().findViewById(R.id.pieChart);
        showPieChart();
    }
    public boolean getData(Boolean e,String d,int c) {
        exists=e;
        date=d;
        count=c;
        if(AnalysisWaterFragment.this.isVisible()){
            showPieChart();
            return true;
        }
        else return false;
    }
    public void showPieChart(){
        count=UserDatabase.getData(db,date);
        left=max_count-count;
        final int[]yDat={count,left};
        pieChart.setRotationEnabled(true);
        //pieChart.setUsePercentValues(true);
        //pieChart.setHoleColor(Color.BLUE);
        //pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Water Pie Chart");
        pieChart.setCenterTextSize(10);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);
        //pieChart.setDrawEntryLabels(true);
        //pieChart.setEntryLabelTextSize(20);
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int pos1 = e.toString().indexOf("y:");
                String sales = e.toString().substring(pos1 + 3);
                float count=Float.parseFloat(sales);
                int c=(int)count;
                for(int i = 0; i < yDat.length; i++){
                    if(yDat[i] == Float.parseFloat(sales)){
                        pos1 = i;
                        break;
                    }
                }
                String employee = xData[pos1];
                Toast.makeText(getActivity(),employee + "\n" + c , Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected() {}
        });
        addDataSet();
    }
    private void addDataSet() {
        final int[]yDat={count,left};
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();
        for(int i = 0; i < yDat.length; i++){
            yEntrys.add(new PieEntry(yDat[i] , i));
        }
        for(int i = 1; i < xData.length; i++){
            xEntrys.add(xData[i]);
        }
        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Glasses consumed");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);
        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(0,172,193));
        colors.add(Color.rgb(126,121,121));
        pieDataSet.setColors(colors);
        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }
}