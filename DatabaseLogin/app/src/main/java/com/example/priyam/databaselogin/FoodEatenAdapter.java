package com.example.priyam.databaselogin;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
/**
 * Created by Pinakin on 08-03-2018.
 */
public class FoodEatenAdapter extends ArrayAdapter<FoodItems> {
    int resource;
    String response;
    Context context;
    int count=0;
    public FoodEatenAdapter(Context context, int resource, List<FoodItems> items) {
        super(context, resource, items);
        this.resource=resource;
        count=items.size();
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
        TextView itemName =(TextView)foodItemsView.findViewById(R.id.itemN);
        itemName.setTextSize(TypedValue.COMPLEX_UNIT_PX,35);
        TextView itemEnergy = (TextView)foodItemsView.findViewById(R.id.itemE);
        itemEnergy.setTextSize(TypedValue.COMPLEX_UNIT_PX,30);
        if (foodItems.getName().length()>33)
            itemEnergy.setText(foodItems.getName().substring(0,30)+"...");
        else
            itemName.setText(foodItems.getName());
        Log.e("nAme",foodItems.getName());
        itemEnergy.setText(foodItems.getEnergy()+" Cal");
        return foodItemsView;
    }
}