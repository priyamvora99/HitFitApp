package com.example.priyam.databaselogin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.regex.Pattern;

public class GetBasicInfo extends AppCompatActivity {
    Spinner weight, daily_activity;
    EditText name, phone, age, height, we, city;
    String w, activity, wei, n, p, a, h, c, gender;
    RadioGroup rg;
    TextView next,back;
    RadioButton r,m,f;
    Pattern regex = Pattern.compile("[$&+,:;=\\\\?#|/'<>^*()@.%!-]");
    Pattern regex2 = Pattern.compile("[0-9]+");
    SQLiteDatabase d;


    float convertCmToMeters,bmi,weig,cm,convertPoundsToKg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_basic_info);
        setTitle("Basic Information");
        d=openOrCreateDatabase("MyTemp.db", Context.MODE_PRIVATE, null);
        getId();

    }
    private void getId() {
        weight = (Spinner) findViewById(R.id.weightSpin);
        daily_activity = (Spinner) findViewById(R.id.dailyActivitySpin);
        name = (EditText) findViewById(R.id.editTextName);
        phone = (EditText) findViewById(R.id.editTextPhone);
        age = (EditText) findViewById(R.id.editTextAge);
        height = (EditText) findViewById(R.id.editTextHeight);
        we = (EditText) findViewById(R.id.editTextWeight);
        city = (EditText) findViewById(R.id.editTextCity);
        m= (RadioButton) findViewById(R.id.radioMale);
        f= (RadioButton) findViewById(R.id.radioFemale);
        rg = (RadioGroup) findViewById(R.id.radioSex);
        back= (TextView) findViewById(R.id.back);
        next= (TextView) findViewById(R.id.next);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.next, menu);
        return true;
    }

    public void next(View v){

                if(getValue()) {
                    cm = Float.parseFloat(h);
                    convertCmToMeters = cm / 100;
                    weig = Float.parseFloat(wei);
                    if (w.equals("lbs")) {
                        convertPoundsToKg = (float) (weig * 0.453592);
                    } else convertPoundsToKg = weig;

                    bmi = ((convertPoundsToKg) / (convertCmToMeters * convertCmToMeters));
                    validate();
                }
    }
    public void back(View v){
        finish();
    }
    private boolean getValue() {
        int i=0;
        boolean val=true;
        final Snackbar snackbar;
        w = weight.getSelectedItem().toString();//weight unit
        activity = daily_activity.getSelectedItem().toString();
        wei = we.getText().toString();//weight
        n = name.getText().toString();//name
        p = phone.getText().toString();//phone
        a = age.getText().toString();//age
        h = height.getText().toString();//height
        c = city.getText().toString();//city
        int id = rg.getCheckedRadioButtonId();
        r = (RadioButton) findViewById(id);//gender
        gender = r.getText().toString();
        if(n.isEmpty() || p.isEmpty() || a.isEmpty() || wei.isEmpty() || h.isEmpty() || c.isEmpty()) {
            val = false;
            snackbar = Snackbar.make(findViewById(android.R.id.content), "Enter values in all fields !", Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ff0000"));
            snackbar.show();
        } else {
            val=true;
        }
        return val;
    }
    public class SendPostRequest extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(GetBasicInfo.this);
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait...");
            this.dialog.show();
        }
        protected String doInBackground(String... arg0) {
            try {
                String file="insertUserAccount.php";
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("email", UserDatabase.getEmail(d));
                postDataParams.put("name", n);
                postDataParams.put("phone", p);
                postDataParams.put("age", a);
                postDataParams.put("gender", gender);
                postDataParams.put("height", h);
                postDataParams.put("weight", convertPoundsToKg);
                postDataParams.put("bmi", bmi);
                postDataParams.put("city", c);
                postDataParams.put("daily_activity", activity);

                Log.e("params", postDataParams.toString());
                return(SendData.sendData(file,postDataParams));

            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }
        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();

            Intent i = new Intent(getApplicationContext(), BmiDisplay.class);
            i.putExtra("Height", h);
            i.putExtra("Weight", wei);
            startActivity(i);
        }
    }
    public void onRadioButtonClicked(View view) {
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioMale:m.setButtonDrawable(R.drawable.malecolor);
                f.setButtonDrawable(R.drawable.femaleblack);
                break;
            case R.id.radioFemale:f.setButtonDrawable(R.drawable.femalecolor);
                m.setButtonDrawable(R.drawable.malebalck);
                break;
        }
    }


    private void validate() {
        int i = 0;
        final Snackbar snackbar;
        String msg="Enter Valid Value in ";
        try{
            if (regex.matcher(n).find() || regex2.matcher(n).find()) {
                i = 2;
                msg = msg + "Name ";
            }
            if (regex.matcher(p).find() || p.length()>10 || p.length()<10){
                if(msg.length()>21)msg=msg+", Phone No ";
                else msg=msg+"Phone No ";
                Log.e("lengthP",p.length()+"");
                i = 3;
            }
            if (regex.matcher(c).find() || regex2.matcher(c).find()){
                if(msg.length()>21)msg=msg+", City ";
                else msg=msg+"City ";
                i = 4;
            }
            if(Integer.parseInt(a)>110 || Integer.parseInt(a)<=0){
                if(msg.length()>21)msg=msg+", Age ";
                else msg=msg+"Age ";
                i=6;
            }
            if(Integer.parseInt(h)>270 || Integer.parseInt(h)<=0){
                if(msg.length()>21)msg=msg+", Height ";
                else msg=msg+"Height ";
                i=7;
            }
            if((Integer.parseInt(wei)>600 && w.equals("kgs")) || Integer.parseInt(wei)<=0){
                if(msg.length()>21)msg=msg+", Weight ";
                else msg=msg+"Weight ";
                i=7;
            }
            if((Integer.parseInt(wei)>1300 && w.equals("lbs")) || Integer.parseInt(wei)<=0){
                if(msg.length()>21)msg=msg+", Weight ";
                else msg=msg+"Weight ";
                i=7;
            }

                if (i == 0) {
                    new SendPostRequest().execute();
                } else {
                snackbar = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ff0000"));
                snackbar.show();
            }
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
        }
    }