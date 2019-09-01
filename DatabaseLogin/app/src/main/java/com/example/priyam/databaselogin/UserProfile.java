package com.example.priyam.databaselogin;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UserProfile extends AppCompatActivity implements View.OnClickListener {
    Cursor c;
    SQLiteDatabase db;
    TextView userName,cardViewE,cardViewP;
    LinearLayout basic,med;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        db=openOrCreateDatabase("MyTemp.db", Context.MODE_PRIVATE,null);
        getId();
        setValues();
    }

    public void getId() {
        userName=(TextView)findViewById(R.id.userN);
        cardViewE=(TextView)findViewById(R.id.cardViewEmail);
        cardViewP=(TextView)findViewById(R.id.cardViewPhoneNumber);
        basic=(LinearLayout)findViewById(R.id.linearUpdateBasic);
        med=(LinearLayout)findViewById(R.id.updateMedical);
        basic.setOnClickListener(this);
        med.setOnClickListener(this);
    }
    public void setValues(){
        userName.setText(UserDatabase.getName(db));

        cardViewE.setText(UserDatabase.getEmail(db));
        cardViewP.setText(UserDatabase.getPhone(db));

    }

    @Override
    public void onClick(View v) {
        if(v==basic){
            Intent i=new Intent(getApplicationContext(),UpdateBasic.class);
            startActivity(i);
            Toast.makeText(getApplicationContext(),"Basic",Toast.LENGTH_LONG).show();
        }else{

            Intent i=new Intent(getApplicationContext(),UpdateMedical.class);
            startActivity(i);
            Toast.makeText(getApplicationContext(),"Med",Toast.LENGTH_LONG).show();
        }
    }
}
