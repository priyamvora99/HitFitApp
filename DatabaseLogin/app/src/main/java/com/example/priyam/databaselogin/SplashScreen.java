package com.example.priyam.databaselogin;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;


public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        db= openOrCreateDatabase("MyTemp.db", Context.MODE_PRIVATE, null);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    //Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    //startActivity(i);
                   int l=0;
                    if(UserDatabase.exists(db)){
                        l=UserDatabase.selectInfo(db);
                        if(l==0){
                            Intent i=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(i);
                        }
                        else{
                            if(UserDatabase.getName(db).equals("")) {
                                Intent i = new Intent(getApplicationContext(), GetBasicInfo.class);
                                startActivity(i);
                            }else{
                                Intent i = new Intent(getApplicationContext(), Dashboard.class);
                                startActivity(i);

                            }
                        }
                    }
                    else{
                        UserDatabase.makeTable(db);
                        Intent i=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(i);
                    }
                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}