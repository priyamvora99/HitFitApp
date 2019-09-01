package com.example.priyam.databaselogin;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.john.waveview.WaveView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Random;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SQLiteDatabase db;
    String name,email;
    WaveView waveView;
    TextView emailT,nameT,tipname,tipnumber;
    Context mcontext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        db= openOrCreateDatabase("MyTemp.db", Context.MODE_PRIVATE, null);
        initialization();
        mcontext=getApplicationContext();
        String tipno[]=mcontext.getResources().getStringArray(R.array.tipnumber);
        String tip[]=mcontext.getResources().getStringArray(R.array.tipname);

        tipname=(TextView)findViewById(R.id.tip);
        tipnumber=(TextView)findViewById(R.id.tipno);
        Random r = new Random();
        int i1 = r.nextInt(17-0+1)+0;
        tipnumber.setText(tipno[i1]);
        tipname.setText(tip[i1]);

    }
    public void initialization(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        name=UserDatabase.getName(db);
        email=UserDatabase.getEmail(db);
        nameT= (TextView) findViewById(R.id.nameT);
        emailT= (TextView) findViewById(R.id.emailT);
        waveView= (WaveView) findViewById(R.id.wave_view);
        waveView.setProgress(60);
    }
    public void waterMonitor(View v){
        Intent i=new Intent(getApplicationContext(),WaterMonitor.class);
        startActivity(i);
    }
    public void exercise(View v){
        Intent i=new Intent(getApplicationContext(),Exercise.class);
        startActivity(i);
    }
    public void foodMonitor(View v){
        Intent i=new Intent(getApplicationContext(),FoodMonitor.class);
        startActivity(i);
    }
    public void stepMonitor(View v){
        Intent i=new Intent(getApplicationContext(),StepMonitor.class);
        startActivity(i);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        nameT= (TextView) findViewById(R.id.nameT);
        emailT= (TextView) findViewById(R.id.emailT);
        nameT.setText(name);
        emailT.setText(email);
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent i=new Intent(getApplicationContext(),UserProfile.class);
            startActivity(i);
        } else if (id == R.id.nav_gallery) {
            Intent i=new Intent(getApplicationContext(),Reminders.class);
            startActivity(i);
        } else if (id == R.id.nav_slideshow) {
            if(backUpDataBase(getApplicationContext())) {
                db.execSQL("delete from Login");
                db.execSQL("delete from Steps;");
                db.execSQL("delete from FoodData;");
                db.execSQL("delete from waterGraph;");
                db.execSQL("delete from FoodItems;");
                db.execSQL("delete from Search;");
                Toast.makeText(getApplicationContext(), "Logged Out Successfully", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean backUpDataBase(Context context)
    {
        try {
                String currentDBPath = db.getPath();
                File currentDB = new File(currentDBPath);
            File data = Environment.getDataDirectory();
                File file = new File(data.getPath()+"/data/com.example.priyam.databaselogin/HitFit/");
                if (!file.exists()) {
                    file.mkdir();
                    Log.e("not","exist");
                }
                File dbFile = new File(file,UserDatabase.getEmail(db)+".db");
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(dbFile).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                return true;
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            return false;
        }
    }
}


