package com.example.priyam.databaselogin;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
public class AddFoodItem extends AppCompatActivity {
    private SearchView searchFoodItem;
    private ProgressBar progressBar;
    private ListView listView;
    private List<FoodItems> foodItems;
    private String q="";
    private String ss;
    static String time,date;
    private TextView tplease;
    static SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_item);
        db = openOrCreateDatabase("MyTemp.db", Context.MODE_PRIVATE, null);
        setTitle("Search Food Items");
        getType();
        getID();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        searchFoodItem.setSubmitButtonEnabled(true);
        searchFoodItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                q = "";
                ss="";
                q = searchFoodItem.getQuery().toString();
                if(!UserDatabase.searchQuery(db,q)) {
                    ss=q;
                    q = q.replaceAll(" ", "%20");
                    if ((!q.equals("")) && q.length() > 2) {
                        new HttpGetRequest().execute();
                    }
                }
                else{
                    foodItems = new ArrayList<>();
                    getItemsFromDb(db,q);
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                q =newText;
                foodItems = new ArrayList<>();
                getItemsFromDb(db,q);
                return false;
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        if (!(searchFoodItem.getQuery().toString()).equals("")) {
            searchFoodItem.setIconified(false);
            searchFoodItem.setFocusable(false);
            searchFoodItem.clearFocus();
        }
    }
    private void getID() {
        searchFoodItem = (SearchView) findViewById(R.id.foodSearch);
        listView = (ListView) findViewById(R.id.list);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        tplease = (TextView) findViewById(R.id.please);
    }
    private void getType() {
        Intent i = getIntent();
        int type = i.getIntExtra("Type", 0);
        date=i.getStringExtra("Date");
        switch (type) {
            case 1:time="Breakfast";
                Toast.makeText(getApplicationContext(),"Breakfast", Toast.LENGTH_LONG).show();
                break;
            case 2:time="Brunch";
                Toast.makeText(getApplicationContext(),"Morning Snack", Toast.LENGTH_LONG).show();
                break;
            case 3:time="Lunch";
                Toast.makeText(getApplicationContext(),"Lunch", Toast.LENGTH_LONG).show();
                break;
            case 4:time="Evening Snack";
                Toast.makeText(getApplicationContext(),"Evening Snack", Toast.LENGTH_LONG).show();
                break;
            case 5:time="Dinner";
                Toast.makeText(getApplicationContext(),"Dinner", Toast.LENGTH_LONG).show();
                break;
        }
    }
    public class HttpGetRequest extends AsyncTask<String, Void, String> {
        String file2 = "https://api.edamam.com/search?q=" + q + "&app_id=85cbcdf0&app_key=fed18c2f489b3d0c92eceaa75d508f82";
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            tplease.setVisibility(View.VISIBLE);
            foodItems = new ArrayList<>();
        }
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                result = SendData.searchItems(file2);
            } catch (Exception e) {
                Log.e("Ex", e.toString());
                result=null;
            }
            return result;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null &&s.contains("recipe")){
                Log.e("fhjl", "onPostExecute: " );
                parseJSON(s);
                UserDatabase.addQuery(ss, db);
            }else{
                Toast.makeText(getApplicationContext(),"Network Problem,try again!",Toast.LENGTH_LONG).show();
            }
            progressBar.setVisibility(View.GONE);
            tplease.setVisibility(View.GONE);
        }
    }
    private void parseJSON(String res) {
        JSONObject jsonResponse;
        JSONObject nutrients;
        JSONObject nutrientsData;
        JSONObject recipeData;
        JSONObject recipe;
        String data = "", n = "", e = "", url = "", servings = "", protein = "", fats = "", fiber = "", carbs = "", sugar = "";
        try {
            jsonResponse = new JSONObject(res);
            Log.e("Parse", String.valueOf(jsonResponse));
            JSONArray recipes = jsonResponse.getJSONArray("hits");
            Log.e("Parse", String.valueOf(recipes));
            int i;
            for (i = 0; i < recipes.length(); i++) {
                n = "";
                e = "";
                protein = "";
                fats = "";
                fiber = "";
                carbs = "";
                sugar = "";
                servings = "";
                recipe = recipes.getJSONObject(i);
                recipeData = recipe.getJSONObject("recipe");
                n = String.valueOf(recipeData.get("label"));
                url = String.valueOf(recipeData.get("image"));
                servings = String.valueOf(recipeData.get("yield"));
                nutrients = recipeData.getJSONObject("totalNutrients");
                nutrientsData = nutrients.getJSONObject("ENERC_KCAL");
                e += formatFloat(String.valueOf(nutrientsData.get("quantity")));
                nutrientsData = nutrients.getJSONObject("FAT");
                fats += formatFloat(String.valueOf(nutrientsData.get("quantity")));
                nutrientsData = nutrients.getJSONObject("PROCNT");
                protein += formatFloat(String.valueOf(nutrientsData.get("quantity")));
                nutrientsData = nutrients.getJSONObject("SUGAR");
                sugar += formatFloat(String.valueOf(nutrientsData.get("quantity")));
                nutrientsData = nutrients.getJSONObject("FIBTG");
                fiber += formatFloat(String.valueOf(nutrientsData.get("quantity")));
                nutrientsData = nutrients.getJSONObject("CHOCDF");
                carbs += formatFloat(String.valueOf(nutrientsData.get("quantity")));
                foodItems.add(new FoodItems(n, e, url, protein, carbs, fats, fiber, sugar, servings, ss));
            }
            FoodItemsAdapter adapter = new FoodItemsAdapter(this, R.layout.food_item_list, foodItems);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent intent = new Intent(getApplicationContext(), FoodItem.class);
                    intent.putExtra("Item", foodItems.get(position));
                    startActivity(intent);
                }
            });
        } catch (JSONException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
    }
    private String formatFloat(String s) {
        int indexOfDecimal = s.indexOf('.');
        return (s.substring(0, indexOfDecimal + 2));
    }
    private void getItemsFromDb(SQLiteDatabase db, String q){
        Cursor c=UserDatabase.getItems(db,q);
        Bitmap img;
        while (c.moveToNext()){
            byte[] blob = c.getBlob(c.getColumnIndex("Img"));
            // Convert the byte array to Bitmap
            img=BitmapFactory.decodeByteArray(blob, 0, blob.length);
            foodItems.add(new FoodItems(ss,c.getString(1), c.getFloat(2),c.getFloat(3),c.getFloat(4),c.getFloat(5),c.getFloat(6),c.getFloat(7),img));
        }
        FoodItemsAdapter adapter = new FoodItemsAdapter(this, R.layout.food_item_list, foodItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(getApplicationContext(), FoodItem.class);
                intent.putExtra("Item", foodItems.get(position));
                startActivity(intent);
            }
        });
        progressBar.setVisibility(View.GONE);
        //dialog.dismiss();
        tplease.setVisibility(View.GONE);
    }
}