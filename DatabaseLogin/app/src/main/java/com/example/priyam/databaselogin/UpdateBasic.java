package com.example.priyam.databaselogin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.regex.Pattern;

public class UpdateBasic extends AppCompatActivity  implements View.OnClickListener {
    EditText n,p,a,h,w,ci,da;
    TextView b,update;
    SQLiteDatabase db;
    String he,we,spinnerWeight,updatedName,updatedAge,updatedPhone,updatedHeight,updatedWeight,updatedBmi,updatedCity,email;
    Float height,weight,bmi,convertCmToMeters;
    Spinner weightS;
    Pattern regex = Pattern.compile("[$&+,:;=\\\\?#|/'<>^*()@.%!-]");
    Pattern regex2 = Pattern.compile("[0-9]+");


    Cursor c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_basic);
        setTitle("Basic Information");
        getId();
        db=openOrCreateDatabase("MyTemp.db", Context.MODE_PRIVATE,null);
        setValues();
    }

    private void setValues() {

        c=UserDatabase.selectLogin(db);
        if(c.getCount()!=0){
            while(c.moveToNext()){

                n.setText(c.getString(1));

                p.setText(c.getString(3));
                a.setText(String.valueOf(c.getInt(2)));
                h.setText(String.valueOf(c.getFloat(5)));
                w.setText(String.valueOf(c.getFloat(6)));
                ci.setText(c.getString(9));
                b.setText(String.valueOf(c.getFloat(7)));
            }
        }
        h.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                he=h.getText().toString();
                we=w.getText().toString();
                if(he.equals("")){
                    height=0.0f;

                }else{
                    height=Float.parseFloat(he);
                }
                if(we.equals("")){
                    weight=0.0f;
                }else {
                    weight = Float.parseFloat(we);
                }
                convertCmToMeters = (float) height / 100;
                bmi=((weight) / (convertCmToMeters*convertCmToMeters));
                Log.e("bmi",bmi.toString());
                if(bmi.toString().equals("Infinity")) {
                    b.setText("-");

                }else{
                    b.setText(String.valueOf(bmi));
                }

            }
        });
        w.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                he=h.getText().toString();
                we=w.getText().toString();
                if(he.equals("")){
                    height=0.0f;
                }else {
                    height = Float.parseFloat(he);
                }
                if(we.equals("")){
                    weight=0.0f;
                }else{
                    weight=Float.parseFloat(we);
                }



                convertCmToMeters = (float) height / 100;
                bmi=((weight) / (convertCmToMeters*convertCmToMeters));
                if(bmi.toString().equals("Infinity")) {
                    b.setText("-");

                }else{
                    b.setText(String.valueOf(bmi));
                }
            }
        });
    }

    public void getId() {
        n=(EditText)findViewById(R.id.editTextName);
        p=(EditText)findViewById(R.id.editTextPhone);
        a=(EditText)findViewById(R.id.editTextAge);
        h=(EditText)findViewById(R.id.editTextHeight);
        w=(EditText)findViewById(R.id.editTextWeight);
        Log.e("check",w.getText().toString());
        ci=(EditText)findViewById(R.id.editTextCity);
        b=(TextView) findViewById(R.id.TextViewBmi);

        update=(TextView)findViewById(R.id.update);
        update.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        updatedName=n.getText().toString();
        updatedPhone=p.getText().toString();
        updatedAge=a.getText().toString();
        updatedCity=ci.getText().toString();
        updatedHeight=h.getText().toString();
        updatedWeight=w.getText().toString();
        updatedBmi=b.getText().toString();
        email=UserDatabase.getEmail(db);
        validate();



    }
    public class SendPostRequest extends AsyncTask<String, Void, String> {

        ProgressDialog dialog = new ProgressDialog(UpdateBasic.this);

        protected void onPreExecute(){
            this.dialog.setMessage("Please wait while we update your information");
            this.dialog.show();
        }
        protected String doInBackground(String... arg0) {

            try {
                String file="updateBasicInfo.php";
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("email", email);
                postDataParams.put("name",updatedName);
                postDataParams.put("age", Integer.parseInt(updatedAge));
                postDataParams.put("phone",updatedPhone);
                postDataParams.put("height",Float.parseFloat(updatedHeight));
                postDataParams.put("weight",Float.parseFloat(updatedWeight));
                postDataParams.put("bmi",Float.parseFloat(updatedBmi));
                postDataParams.put("city",updatedCity);


                return (SendData.sendData(file,postDataParams));
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }
        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();
            if(result.equals("Updated successfully") ){
                UserDatabase.updateBasic(db,email,updatedName,updatedAge,updatedPhone,updatedHeight,updatedWeight,updatedBmi,updatedCity);
                Intent i= new Intent(getApplicationContext(),Dashboard.class);
                startActivity(i);
            }else{
                Toast.makeText(getApplicationContext(),"Could not update at this moment please try again",Toast.LENGTH_LONG).show();
            }

        }
    }
    private void validate() {
        int i = 0;
        final Snackbar snackbar;
        String msg = "Enter Valid Value in ";
        try {
            if (regex.matcher(updatedName).find() || regex2.matcher(updatedName).find()) {
                i = 2;
                msg = msg + "Name ";
            }
            if (regex.matcher(updatedPhone).find() || p.length() > 10 || p.length() < 10) {
                if (msg.length() > 21) msg = msg + ", Phone No ";
                else msg = msg + "Phone No ";
                i = 3;
            }
            if (regex.matcher(updatedCity).find() || regex2.matcher(updatedCity).find()) {
                if (msg.length() > 21) msg = msg + ", City ";
                else msg = msg + "City ";
                i = 4;
            }
            if (Integer.parseInt(updatedAge) > 110 || Integer.parseInt(updatedAge) <= 0) {
                if (msg.length() > 21) msg = msg + ", Age ";
                else msg = msg + "Age ";
                i = 6;
            }
            if (Float.parseFloat(updatedHeight) > 270 || Float.parseFloat(updatedHeight) <= 0) {
                if (msg.length() > 21) msg = msg + ", Height ";
                else msg = msg + "Height ";
                i = 7;
            }
            if ((Float.parseFloat(updatedWeight) > 600 && w.equals("kgs")) || Float.parseFloat(updatedWeight) <= 0) {
                if (msg.length() > 21) msg = msg + ", Weight ";
                else msg = msg + "Weight ";
                i = 7;
            }
            if ((Float.parseFloat(updatedWeight) > 1300 && w.equals("lbs")) || Float.parseFloat(updatedWeight) <= 0) {
                if (msg.length() > 21) msg = msg + ", Weight ";
                else msg = msg + "Weight ";
                i = 7;
            }

            if (i == 0) {
                new SendPostRequest().execute();
            } else {
                snackbar = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ff0000"));
                snackbar.show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
