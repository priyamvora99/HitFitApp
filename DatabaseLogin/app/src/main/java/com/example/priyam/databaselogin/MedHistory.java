package com.example.priyam.databaselogin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.Toast;

import org.json.JSONObject;

public class MedHistory extends AppCompatActivity {
    CheckedTextView diabetes,allergy,thyroid,cholestrol,pcos,none;
    SQLiteDatabase db;
    String medHist="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_history);
        setTitle("Medical Information");
        getId();
        db= openOrCreateDatabase("MyTemp.db", Context.MODE_PRIVATE, null);
    }

    private void getId() {
        diabetes= (CheckedTextView) findViewById(R.id.diabetes);
        thyroid= (CheckedTextView) findViewById(R.id.thyroid);
        allergy= (CheckedTextView) findViewById(R.id.allergy);
        cholestrol= (CheckedTextView) findViewById(R.id.cholestrol);
        pcos= (CheckedTextView) findViewById(R.id.pcos);
        none= (CheckedTextView) findViewById(R.id.none);
        none.setChecked(true);
        setCheck(none);
        diabetes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (diabetes.isChecked()) {
                    diabetes.setChecked(false);
                    unsetCheck(diabetes);
                } else {
                    diabetes.setChecked(true);
                    setCheck(diabetes);
                    none.setChecked(false);
                    unsetCheck(none);

                }
            }
        });
        thyroid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thyroid.isChecked()) {
                    thyroid.setChecked(false);
                    unsetCheck(thyroid);
                } else {
                    thyroid.setChecked(true);
                    setCheck(thyroid);
                    none.setChecked(false);
                    unsetCheck(none);

                }
            }
        });
        cholestrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cholestrol.isChecked()) {
                    cholestrol.setChecked(false);
                    unsetCheck(cholestrol);
                } else {
                    cholestrol.setChecked(true);
                    setCheck(cholestrol);
                    none.setChecked(false);
                    unsetCheck(none);

                }
            }
        });
        allergy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allergy.isChecked()) {
                    allergy.setChecked(false);
                    unsetCheck(allergy);
                } else {
                    allergy.setChecked(true);
                    setCheck(allergy);
                    none.setChecked(false);
                    unsetCheck(none);

                }
            }
        });

        pcos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pcos.isChecked()) {
                    pcos.setChecked(false);
                    unsetCheck(pcos);
                } else {
                    pcos.setChecked(true);
                    setCheck(pcos);
                    none.setChecked(false);
                    unsetCheck(none);

                }
            }
        });
        none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (none.isChecked()) {
                    none.setChecked(false);
                    unsetCheck(none);
                } else {
                    none.setChecked(true);
                    setCheck(none);
                    unsetCheck(pcos);
                    unsetCheck(allergy);
                    unsetCheck(cholestrol);
                    unsetCheck(diabetes);
                    unsetCheck(thyroid);
                    pcos.setChecked(false);
                    allergy.setChecked(false);
                    cholestrol.setChecked(false);
                    thyroid.setChecked(false);
                    diabetes.setChecked(false);
                }
            }
        });
    }
    public void next(View v){
        Log.e("yeah","nex");
        if(none.isChecked()){
            medHist="None";
        }

        else{
            if(diabetes.isChecked())medHist="Diabetes";
            if(allergy.isChecked() && medHist.equals(""))medHist="Allergy";
            else if (allergy.isChecked())medHist+=", Allergy";
            if(thyroid.isChecked() && medHist.equals(""))medHist="Thyroid";
            else if(thyroid.isChecked())medHist+=", Thyroid";
            if(pcos.isChecked() && medHist.equals(""))medHist="PCOS";
            else if(pcos.isChecked())medHist+=", PCOS";
            if(cholestrol.isChecked() && medHist.equals(""))medHist="Cholestrol";
            else if(cholestrol.isChecked())medHist+=", Cholestrol";

            Log.e("medHist",medHist);
        }

        new SendPostRequest().execute();
        Intent i=new Intent(getApplicationContext(),Dashboard.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
    public void back(View v){
        finish();
    }
    private void setCheck(CheckedTextView c) {
        c.setCheckMarkDrawable(R.drawable.tickwhite);
    }
    private void unsetCheck(CheckedTextView c) {
        c.setCheckMarkDrawable(null);
    }


    public class SendPostRequest extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(MedHistory.this);

        protected void onPreExecute() {
            this.dialog.setMessage("Fetching Details..");
            this.dialog.show();
        }

        protected String doInBackground(String... arg0) {

            String file="updateMedHistory.php";
            String res="";

            try {
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("email", UserDatabase.getEmail(db));
                postDataParams.put("medhistory",medHist);
                res = SendData.sendData(file, postDataParams);

            }catch(Exception ex){
                Log.e("Exception:" ,"Exception: "+Log.getStackTraceString(ex));
            }
            return(res);
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            new SendPostRequestData().execute();
            //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
        }
    }
    public class SendPostRequestData extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(MedHistory.this);

        protected void onPreExecute() {
            this.dialog.setMessage("Fetching Details..");
            this.dialog.show();
        }

        protected String doInBackground(String... arg0) {

            String file="getUserValues.php";
            String res="";
            try {
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("email", UserDatabase.getEmail(db));
                res = SendData.sendData(file, postDataParams);
                UserDatabase.getUserValues(res,db);
            }catch(Exception ex){
                Log.e("Exception:" ,"Exception: "+Log.getStackTraceString(ex));
            }
            return(new String("Done"));
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
        }
    }
}
