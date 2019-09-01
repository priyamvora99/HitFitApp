package com.example.priyam.databaselogin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;

public class UserType extends AppCompatActivity {

    SQLiteDatabase db;
    Button button;
    CheckBox c1,c2,c3;
    RadioGroup r1,r2;
    RadioButton rb1,rb2,rb3,rb4;
    Spinner s1,s2;
    LinearLayout alcoholL,smokeL;
    TextView next;
    boolean veg,nonVeg,vegan;
    int id,id1;
    String radioAlcohol,radioSmoking,howOftenAlcohol,howOftenSmoking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);
        getId();
        db=openOrCreateDatabase("MyTemp.db", Context.MODE_PRIVATE, null);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValues();
            }
        });
    }
    public void back(View v){
        finish();
    }
    public void getId(){
        //button =(Button) findViewById(R.id.proceed_btn);
        next= (TextView) findViewById(R.id.proceed_btn);
        c1 =(CheckBox) findViewById(R.id.veg_check_box);
        c2 =(CheckBox) findViewById(R.id.vegan_check_box);
        c3 =(CheckBox) findViewById(R.id.non_veg_check_box);

        s1=(Spinner)findViewById(R.id.how_spinner);
        s2=(Spinner)findViewById(R.id.how1_spinner);
        r1=(RadioGroup)findViewById(R.id.alcohol_rg);
        r2=(RadioGroup)findViewById(R.id.smoke_radio);
        rb1= (RadioButton) findViewById(R.id.alcoholic_radio);
        rb2= (RadioButton) findViewById(R.id.non_alcoholic_radio);
        rb3= (RadioButton) findViewById(R.id.no);
        rb4= (RadioButton) findViewById(R.id.yes);

        smokeL= (LinearLayout) findViewById(R.id.smokeL);
        alcoholL= (LinearLayout) findViewById(R.id.alcoholL);
        rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb1.isChecked()){
                    alcoholL.setVisibility(View.VISIBLE);
                }
            }
        });
        rb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb2.isChecked()){
                    alcoholL.setVisibility(View.GONE);
                }
            }
        });
        rb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb3.isChecked()){
                    smokeL.setVisibility(View.GONE);
                }
            }
        });
        rb4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb4.isChecked()){
                    smokeL.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void getValues() {
        vegan=c2.isChecked();
        veg=c1.isChecked();
        nonVeg=c3.isChecked();
        id=r1.getCheckedRadioButtonId();
        id1=r2.getCheckedRadioButtonId();
        rb1=(RadioButton)findViewById(id);
        rb2=(RadioButton)findViewById(id1);
        radioAlcohol=rb1.getText().toString();
        radioSmoking=rb2.getText().toString();
        howOftenAlcohol=s1.getSelectedItem().toString();
        howOftenSmoking=s2.getSelectedItem().toString();
        /*Log.e("harshal", String.valueOf(radioAlcohol+radioSmoking+howOftenAlcohol+howOftenSmoking));
        Log.e("harshal", String.valueOf(vegan));
        Log.e("harshal", String.valueOf(nonVeg) );
        Log.e("harshal", String.valueOf(veg) );
        */

        new UpdatePasswordTask().execute();
    }


    class UpdatePasswordTask extends AsyncTask<String, String, String> {

        ProgressDialog dialog = new ProgressDialog(UserType.this);

        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject postDataParams = new JSONObject();
                String file="uploadtype.php";
                postDataParams.put("veg",veg );
                postDataParams.put("nonveg",nonVeg);
                postDataParams.put("vegan",vegan);
                postDataParams.put("alcohol",radioAlcohol);
                postDataParams.put("howoften",howOftenAlcohol);
                postDataParams.put("smoking",radioSmoking);
                postDataParams.put("howoftensmoking",howOftenSmoking);
                postDataParams.put("email",UserDatabase.getEmail(db));

                Log.e("params", postDataParams.toString());
                return(SendData.sendData(file,postDataParams));
            } catch (Exception e) {
                return new String("Exception: " + e.toString());
            }
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait...");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();
            Intent i =new Intent(getApplicationContext(),MedHistory.class);
            startActivity(i);
        }
    }
    public void seeCheck(View view) {
        Log.e("Check", "seeCheck: "+view);
        switch (view.getId()) {
            case R.id.veg_check_box:
                if (c1.isChecked()) {
                    c1.setButtonDrawable(R.drawable.veg);
                    c1.setChecked(true);
                    c3.setButtonDrawable(R.drawable.nullveg);
                    c3.setChecked(false);
                } else {
                    c1.setChecked(false);
                }
                break;
            case R.id.non_veg_check_box:
                if (c3.isChecked()) {
                    c3.setButtonDrawable(R.drawable.nonveg);
                    c3.setChecked(true);
                    c1.setButtonDrawable(R.drawable.nullveg);
                    c1.setChecked(false);
                } else {

                    c3.setChecked(false);
                }
                break;
            case R.id.vegan_check_box:
                if (c2.isChecked()) {
                    c2.setButtonDrawable(R.drawable.vegan);
                    c2.setChecked(true);
                } else {
                    c2.setButtonDrawable(R.drawable.nullvegan);
                    c2.setChecked(false);
                }
        }
        // Check which radio button was clicked
       // Log.e("Checks", String.valueOf(c1.isChecked())+String.valueOf(c2.isChecked())+String.valueOf(c3.isChecked()));
    }

}




