package com.example.priyam.databaselogin;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pinakin on 25-02-2018.
 */

public class LogFoodFragment extends Fragment {
    LinearLayout breakfast,brunch,lunch,esnack,dinner;
    TextView breakfastE,brunchE,lunchE,esnackE,dinnerE,tCal;
    Intent i;
    float totalCal=0.0f;
    int count=0;
    Context mContext;
    String date=null;
    SQLiteDatabase db;
    Cursor data,dataItem;
    List<FoodItems> foodItems;
    FoodEatenAdapter adapter;
    ListView list1,list2,list3,list4,list5,listView;
    public LogFoodFragment(){}
    public LogFoodFragment(FoodMonitor context){
        //mContext=context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        foodItems = new ArrayList<FoodItems>();
        View v= inflater.inflate(R.layout.log_food_fragment, container, false);
        mContext=container.getContext();

        //adapter = new FoodEatenAdapter(container.getContext(), R.layout.food_eaten_list, foodItems);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getID();
        setClickListener();
        initialize();
    }
    public void extractData(String time){
        if(date!=null){
            data=UserDatabase.getDataFromDb(db,date,time);
            count=data.getCount();
            Log.e("Count",count+"");
        }
    }
    public void getID(){
        breakfast= (LinearLayout) getView().findViewById(R.id.breakfast);
        brunch= (LinearLayout) getView().findViewById(R.id.brunch);
        lunch= (LinearLayout) getView().findViewById(R.id.lunch);
        esnack= (LinearLayout) getView().findViewById(R.id.esnack);
        dinner= (LinearLayout) getView().findViewById(R.id.dinner);

        breakfastE= (TextView) getView().findViewById(R.id.breakfastE);
        brunchE= (TextView) getView().findViewById(R.id.brunchE);
        lunchE= (TextView) getView().findViewById(R.id.lunchE);
        esnackE= (TextView) getView().findViewById(R.id.esnackE);
        dinnerE= (TextView) getView().findViewById(R.id.dinnerE);
        tCal= (TextView) getActivity().findViewById(R.id.totalCal);

        list1= (ListView) getActivity().findViewById(R.id.listBreakfast);
        list2= (ListView) getActivity().findViewById(R.id.listBrunch);
        list3= (ListView) getActivity().findViewById(R.id.listLunch);
        list4= (ListView) getActivity().findViewById(R.id.listEsnack);
        list5= (ListView) getActivity().findViewById(R.id.listDinner);


    }
    public void setClickListener(){
        breakfast.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {displayData(1);
            }
        });
        brunch.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {displayData(2);
            }
        });
        lunch.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                displayData(3);
            }
        });
        esnack.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
               displayData(4);
            }
        });
        dinner.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {displayData(5);
            }
        });
    }
    public String formatFloat(float s) {
        String n= String.valueOf(s);
        int indexOfDecimal = n.indexOf('.');
        return (n.substring(0, indexOfDecimal + 2));
    }
    public boolean putDateAndDb(String d,SQLiteDatabase dB){
        this.date=d;
        this.db=dB;
        dataItem=null;
        data=null;

        if(this.isVisible())
            return true;
        else return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        initialize();
        unShowLists();

    }

    public void goToAddItem(int type){
        i=new Intent(getActivity(),AddFoodItem.class);
        i.putExtra("Type",type);
        i.putExtra("Date",date);
        startActivity(i);
    }
    public void displayData(int type){
        showLists();
        TextView calSet=null;
        listView=null;
        data=null;
        dataItem=null;
        foodItems.clear();
        subDisplayData(type);
        adapter = new FoodEatenAdapter(mContext, R.layout.food_eaten_list, foodItems);
        listView.setAdapter(adapter);
        switch (listView.getId()){
            case R.id.listBreakfast:
                list2.setVisibility(View.GONE);
                list4.setVisibility(View.GONE);
                list3.setVisibility(View.GONE);
                list5.setVisibility(View.GONE);break;
            case R.id.listBrunch:
                list1.setVisibility(View.GONE);
                list3.setVisibility(View.GONE);
                list4.setVisibility(View.GONE);
                list5.setVisibility(View.GONE);break;
            case R.id.listLunch:
                list1.setVisibility(View.GONE);
                list2.setVisibility(View.GONE);
                list4.setVisibility(View.GONE);
                list5.setVisibility(View.GONE);break;
            case R.id.listEsnack:
                list1.setVisibility(View.GONE);
                list2.setVisibility(View.GONE);
                list3.setVisibility(View.GONE);
                list5.setVisibility(View.GONE);break;
            case R.id.listDinner:
                list1.setVisibility(View.GONE);
                list2.setVisibility(View.GONE);
                list3.setVisibility(View.GONE);
                list4.setVisibility(View.GONE);break;

        }
        setHeight();
    }

    public void initialize(){
        totalCal=0;

        extractData("Breakfast");setListItems();totalCal+=addCal();

        extractData("Brunch");setListItems();totalCal+=addCal();

        extractData("Lunch");setListItems();totalCal+=addCal();

        extractData("Evening Snack");setListItems();totalCal+=addCal();

        extractData("Dinner");setListItems();totalCal+=addCal();

        tCal.setText(formatFloat(totalCal)+"");
        for (int j=1;j<6;j++){
            subDisplayData(j);
        }
        showLists();
        if(list1.getAdapter()!=null) {
            list1.setAdapter(null);list1.setVisibility(View.GONE);
        }
        if(list2.getAdapter()!=null) {
            list2.setAdapter(null);list2.setVisibility(View.GONE);
        }
        if(list3.getAdapter()!=null) {
            list3.setAdapter(null);list3.setVisibility(View.GONE);
        }
        if(list4.getAdapter()!=null) {
            list4.setAdapter(null);list4.setVisibility(View.GONE);
        }
        if(list5.getAdapter()!=null) {
            list5.setAdapter(null);list5.setVisibility(View.GONE);
        }
    }
    public void subDisplayData(int type){
        TextView calSet=null;
        switch(type){
            case 1:extractData("Breakfast");listView=list1;calSet=breakfastE;break;
            case 2:extractData("Brunch");listView=list2;calSet=brunchE;break;
            case 3:extractData("Lunch");listView=list3;calSet=lunchE;break;
            case 4:extractData("Evening Snack");listView=list4;calSet=esnackE;break;
            case 5:extractData("Dinner");listView=list5;calSet=dinnerE;break;
        }
        foodItems.clear();
        setListItems();
        float cal=addCal();
        Log.e("Type",type+" "+cal);
        if(type==1 || type ==3 || type==5)
            calSet.setText(cal+" of 650 Cal");
        else
            calSet.setText(cal+" of 250 Cal");
    }
    public void setListItems(){
        while(data.moveToNext()) {
            dataItem = UserDatabase.getItem(db, data.getString(2));
            while (dataItem.moveToNext()) {
                foodItems.add(new FoodItems(db, dataItem.getString(dataItem.getColumnIndex("ItemName"))));
            }
        }
    }
    public float addCal(){
        FoodItems fi;
        float cal=0.0f;
        for (int i=0;i<count;i++){
            Log.e("count",count+"");
            Log.e("count",foodItems.size()+"");
            if(foodItems.size()>i) {
                Log.e("i",i+"");
                fi = foodItems.get(i);
                cal += fi.getEnergy();
            }
        }
        return cal;
    }
    public void setHeight() {
        FoodEatenAdapter listadp = (FoodEatenAdapter) listView.getAdapter();
        if (listadp != null) {
            int totalHeight = 0;
            if (listadp.getCount() > 0) {
                for (int i = 0; i < listadp.getCount(); i++) {
                    View listItem = listadp.getView(i, null, listView);
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();
                }
                ViewGroup.LayoutParams params = listView.getLayoutParams();

                params.height = totalHeight + (listView.getDividerHeight() * (listadp.getCount() + 1) + 20);
                listView.setLayoutParams(params);
                listView.requestLayout();
            } else {
                Toast.makeText(getActivity(), "No items in this list! ", Toast.LENGTH_LONG).show();
                listView.setVisibility(View.GONE);

            }
        }
        listView=null;
    }
    public void showLists(){
        list1.setVisibility(View.VISIBLE);
        list2.setVisibility(View.VISIBLE);
        list3.setVisibility(View.VISIBLE);
        list4.setVisibility(View.VISIBLE);
        list5.setVisibility(View.VISIBLE);
    }
    public void unShowLists(){

        list1.setVisibility(View.GONE);
        list2.setVisibility(View.GONE);
        list3.setVisibility(View.GONE);
        list4.setVisibility(View.GONE);
        list5.setVisibility(View.GONE);
    }
}