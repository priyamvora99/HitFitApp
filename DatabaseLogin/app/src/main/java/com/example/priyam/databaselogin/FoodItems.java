package com.example.priyam.databaselogin;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
/**
 * Created by Kartik on 08-03-2018.
 */
public class FoodItems implements Parcelable {
    String name;
    String bmp;
    float energy;
    float protein;
    float carbs;
    float fats;
    float fiber;
    float sugar;
    float servings;
    String query;
    Bitmap itemimage=null;
    FoodItems(String n, String e, String b,String p,String c,String fat,String fib,String s,String serve,String q) {
        this.name = n;
        this.bmp = b;
        this.query= q;
        this.servings= Float.valueOf(serve);
        this.energy = Float.valueOf(String.format("%.1f",(Float.valueOf(e)/servings))) ;
        this.protein = Float.valueOf(String.format("%.1f",(Float.valueOf(p)/servings))) ;
        this.carbs = Float.valueOf(String.format("%.1f",(Float.valueOf(c)/servings))) ;
        this.fats = Float.valueOf
                (String.format("%.1f",(Float.valueOf(fat)/servings)));
        this.fiber = Float.valueOf(String.format("%.1f",(Float.valueOf(fib)/servings)));
        this.sugar = Float.valueOf(String.format("%.1f",(Float.valueOf(s)/servings))) ;
    }
    FoodItems(String q,String n,float e,float p,float f,float c,float fib, float s,Bitmap i){
        query=q;
        name=n;
        energy=e;
        protein=p;
        fats=f;
        carbs=c;
        fiber=fib;
        sugar=s;
        itemimage=i;
    }
    FoodItems(SQLiteDatabase db,String n){
        Cursor c=UserDatabase.getItem(db,n);
        while (c.moveToNext()){
            query=c.getString(0);
            name=c.getString(1);
            energy=c.getFloat(2);
            protein=c.getFloat(3);
            fats=c.getFloat(4);
            carbs=c.getFloat(5);
            fiber=c.getFloat(6);
            sugar=c.getFloat(7);
            Log.e("q",query);
            byte[] blob = c.getBlob(c.getColumnIndex("Img"));
            // Convert the byte array to Bitmap
            itemimage=BitmapFactory.decodeByteArray(blob, 0, blob.length);
        }
    }
    public static final Creator<FoodItems> CREATOR = new Creator<FoodItems>() {
        @Override
        public FoodItems createFromParcel(Parcel in) {
            return new FoodItems(in);
        }
        @Override
        public FoodItems[] newArray(int size) {
            return new FoodItems[size];
        }
    };
    public Bitmap getImg() {
        try {
            new DownloadImg().execute();
        } catch (Exception e) {
            Log.e("exgg", e.toString());
        }
        return itemimage;
    }
    public Bitmap getDownImg(){
        return  this.itemimage;
    }
    public String getImgUrl(){
        return this.bmp;
    }
    public String getName() {
        return this.name;
    }
    public float getEnergy() {
        return this.energy;
    }
    public float getProtein() {
        return this.protein;
    }
    public float getFats() {
        return this.fats;
    }
    public float getFiber() {
        return this.fiber;
    }
    public float getCarbs() {
        return this.carbs;
    }
    public float getSugar() {
        return this.sugar;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeFloat(energy);
        dest.writeFloat(protein);
        dest.writeFloat(carbs);
        dest.writeFloat(fats);
        dest.writeFloat(sugar);
        dest.writeFloat(fiber);
        dest.writeString(query);
        dest.writeString(bmp);
        dest.writeParcelable(itemimage,flags);
    }
    protected FoodItems(Parcel in) {
        name = in.readString();
        energy = in.readFloat();
        protein = in.readFloat();
        carbs = in.readFloat();
        fats = in.readFloat();
        sugar = in.readFloat();
        fiber = in.readFloat();
        query=in.readString();
        bmp = in.readString();
        itemimage=in.readParcelable(getClass().getClassLoader());
    }
    public class DownloadImg extends AsyncTask<String, Void, String> {
        Bitmap img;
        @Override
        protected void onPreExecute() {
            Bitmap.Config config = null;
            //itemimage= Bitmap.createBitmap(1,1,config);
        }
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                //imgURl = new URL(url);
                //Log.e("url", url);
                InputStream is = new URL(bmp).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                img = BitmapFactory.decodeStream(is);
            } catch (Exception e) {
                Log.e("Exdd", e.toString());
            }
            return "";
        }
        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            itemimage = img;
            Log.e("Done","image");
            addFoodToDB(AddFoodItem.db);
        }
    }
    private void addFoodToDB(SQLiteDatabase db) {
        if(!UserDatabase.checkItem(db,name)) {
            ContentValues val = new ContentValues();
            val.put("Query", query);
            val.put("ItemName", name);
            val.put("Energy", energy);
            val.put("Protein", protein);
            val.put("Fats", fats);
            val.put("Carbs", carbs);
            val.put("Fiber", fiber);
            val.put("Sugar", sugar);
            Log.e("q",query);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            itemimage.compress(Bitmap.CompressFormat.JPEG, 100, out);
            byte[] buffer = out.toByteArray();
            val.put("Img", buffer);
            db.insert("FoodItems", null, val);
            Log.e("Inserted","True");
        }
    }
    public String formatFloat(String s){
        int indexOfDecimal = s.indexOf('.');
        return (s.substring(0,indexOfDecimal+2));
    }
}