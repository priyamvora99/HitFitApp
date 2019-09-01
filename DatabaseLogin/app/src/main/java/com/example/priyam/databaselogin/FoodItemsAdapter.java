package com.example.priyam.databaselogin;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Pinakin on 08-03-2018.
 */
public class FoodItemsAdapter extends ArrayAdapter<FoodItems> {

    int resource;
    String response;
    Context context;
    ImageView imageView;
    int count=0;

    public FoodItemsAdapter(AddFoodItem context, int resource, List<FoodItems> items) {
        super(context, resource, items);
        this.resource=resource;
        count=items.size()*2;
        Log.e("Count",count+"");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LinearLayout foodItemsView;
        FoodItems foodItems = getItem(position);
        if(convertView==null) {
            foodItemsView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi;
            vi = (LayoutInflater)getContext().getSystemService(inflater);
            vi.inflate(resource, foodItemsView, true);
        } else {
            foodItemsView = (LinearLayout) convertView;
        }
        TextView itemName =(TextView)foodItemsView.findViewById(R.id.itemName);
        itemName.setTypeface(null, Typeface.BOLD);
        itemName.setTextSize(TypedValue.COMPLEX_UNIT_PX,33);
        TextView itemEnergy = (TextView)foodItemsView.findViewById(R.id.itemEnergy);
        itemEnergy.setTextSize(TypedValue.COMPLEX_UNIT_PX,30);
        if (foodItems.getName().length()>26)
            itemName.setText(foodItems.getName().substring(0,23)+"...");
        else
            itemName.setText(foodItems.getName());
        itemEnergy.setText(foodItems.getEnergy()+" Cal");
        imageView= (ImageView) foodItemsView.findViewById(R.id.foodImg);
        if(foodItems.getDownImg()==null) {
            imageView.setImageBitmap(foodItems.getImg());
        }else {
            imageView.setImageBitmap(foodItems.getDownImg());
        }
        return foodItemsView;
    }

}
