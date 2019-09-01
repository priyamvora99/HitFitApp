package com.example.priyam.databaselogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class FoodItem extends AppCompatActivity {
    ImageView img;
    TextView nameT,caution,energyT,proteinT,fatsT,fiberT,carbsT,sugarT;
    FoodItems fi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item);
        setTitle("");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getID();
        Intent i=getIntent();
        Bundle b = i.getExtras();
        fi =  b.getParcelable("Item");

        setValues();
        ImageView fab = (ImageView) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDatabase.addFoodEaten(AddFoodItem.db,AddFoodItem.date,AddFoodItem.time,fi.getName());
                Intent i=new Intent(getApplicationContext(),FoodMonitor.class);
                startActivity(i);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void getID(){
        img= (ImageView) findViewById(R.id.foodImage);
        nameT= (TextView) findViewById(R.id.itemName);
        energyT= (TextView) findViewById(R.id.itemEnergy);
        proteinT= (TextView) findViewById(R.id.itemProtein);
        fiberT= (TextView) findViewById(R.id.itemFiber);
        sugarT= (TextView) findViewById(R.id.itemSugar);
        carbsT= (TextView) findViewById(R.id.itemCarbs);
        fatsT= (TextView) findViewById(R.id.itemFats);
        caution= (TextView) findViewById(R.id.caution);
    }
    public void setValues(){
        String name = fi.getName();
        float energy = fi.getEnergy();
        img.setImageBitmap(fi.getDownImg());
        if (name.length()>29){
            nameT.setText(name.substring(0,26)+"...");
        }else nameT.setText(name);

        energyT.setText(fi.getEnergy()+" Cal\n Energy");
        proteinT.setText(fi.getProtein()+" g\n Protien");
        fatsT.setText(fi.getFats()+" g\n Fats");
        fiberT.setText(fi.getFiber()+" g\n Fiber");
        carbsT.setText(fi.getCarbs()+" g\n Carbs");
        sugarT.setText(fi.getSugar()+" g\n Sugar");
        if (fi.getSugar()>30){
            caution.setText(caution.getText()+"This item is High on Sugar! Be careful Diabetic Pateints ! ");
        }
        else if (fi.getFats()>40)
            caution.setText(caution.getText()+"This item is High on Fats! ");
        else
            caution.setVisibility(View.GONE);

    }
}
