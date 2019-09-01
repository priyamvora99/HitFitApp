package com.example.priyam.databaselogin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {
    EditText email,password;
    Button login;
    TextView textView,textView1;
    String e,p;
    boolean network=false;
    SQLiteDatabase db;
    int i;
    Thread t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_main);
            setTitle("Login");
            t=new Thread(){
                @Override
                public void run(){
                    try {
                        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        while(true) {
                            if (cm.getActiveNetworkInfo()!=null) {
                                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                                Log.e(netInfo.getTypeName(), netInfo.isConnected() + "");
                                Log.e("netT",network+"");
                                network = netInfo.isConnected();
                            }else{
                                network=false;
                                Log.e("netF",network+"");
                            }
                            sleep(3000);
                            Log.e("net",network+"");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                    }
                }
            };
            t.start();
            textView= (TextView) findViewById(R.id.textView3);
            textView1= (TextView) findViewById(R.id.fp);
            db= openOrCreateDatabase("MyTemp.db", Context.MODE_PRIVATE, null);
            email=(EditText)findViewById(R.id.userEmail);
            password=(EditText)findViewById(R.id.userPassword);
            login=(Button)findViewById(R.id.loginButton);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start the Signup activity
                    Intent intent = new Intent(getApplicationContext(), Signup.class);
                    startActivity(intent);
                }
            });
            textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start the Signup activity
                    Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                    startActivity(intent);
                }
            });
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), e.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public  void onPause(){
        super.onPause();
        finish();
    }
    public void onLoginClick(View v){
        i=0;
        if (network) {
            try {
                e = email.getText().toString().trim();
                p = password.getText().toString().trim();
                if (e.length() > 0) {
                    if (!e.contains("@")) i = 1;
                    if (!e.contains(".")) i = 1;
                    if (e.indexOf(".") == e.indexOf("@") + 1) i = 1;
                    if (e.charAt(e.length() - 1) == '@' || e.charAt(e.length() - 1) == '.') i = 1;
                    if (i == 0) {
                        new SendPostRequest().execute();
                    } else {
                        if (i == 1)
                            Toast.makeText(getApplicationContext(), "Enter Valid Email",
                                    Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Enter Email",
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Enter Email",
                        Toast.LENGTH_LONG).show();
            }
        }else
            Toast.makeText(getApplicationContext(),"No Internet Available",Toast.LENGTH_LONG).show();
    }
    public class SendPostRequest extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        protected void onPreExecute(){
            this.dialog.setMessage("Authenticating...");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }
        protected String doInBackground(String... arg0) {
            try {
                String file="login.php";
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("email", e);
                postDataParams.put("password", p);
                Log.e("params",postDataParams.toString());
                return(SendData.sendData(file,postDataParams));
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }
        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            Log.e("onPostExecute: ",result );
            if(result.contains("Welcome:")){
                UserDatabase.insertLogin(e,db);
                Intent i= new Intent(getApplicationContext(),GetBasicInfo.class);
                startActivity(i);
            }
            else if(result.contains("WelcomeBack:")){
                //File file = new File("/storage/emulated/0/HitFit");
                File data = Environment.getDataDirectory();
                //Log.e("pathD",data.getPath());
                File file = new File(data.getPath()+"/data/com.example.priyam.databaselogin/HitFit/");
                if (!file.exists()) {
                    file.mkdir();
                    //Log.e("folder","notfound");
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    UserDatabase.insertLogin(e,db);
                    new SendPostRequestData().execute();
                }else {
                    Boolean flag=false;
                    if (file.listFiles() != null) {
//                        Log.e("files", "found");
                        if (file.listFiles().length > 0) {
                            Log.e("count",file.listFiles().length+"");
                            File user[] = file.listFiles();
                            for (i = 0; i < user.length; i++) {
                                Log.e("path",user[i].getPath());
                                if (user[i].getPath().contains(e)) {
                                    if (restoreDb(user[i])) {
                                        flag=true;
                                        //Toast.makeText(getApplicationContext(), "Restored", Toast.LENGTH_LONG).show();
                                    }
                                    break;
                                }
                            }
                            if (!flag){
                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                UserDatabase.insertLogin(e, db);
                                new SendPostRequestData().execute();
                            }
                        } else {
                            Log.e("files", "notfound");
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            UserDatabase.insertLogin(e, db);
                            new SendPostRequestData().execute();
                        }
                    }
                }
                Intent i= new Intent(getApplicationContext(),Dashboard.class);
                startActivity(i);
            }
            else{
                Toast.makeText(getApplicationContext(), result,
                        Toast.LENGTH_LONG).show();
                password.setText(null);
                email.setText(null);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //t.stop();
        t.interrupt();
    }

    public class SendPostRequestData extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        protected void onPreExecute() {
            this.dialog.setMessage("Fetching Details..");
            this.dialog.show();
        }
        protected String doInBackground(String... arg0) {
            String file="getUserValues.php";
            String res="";
            try {
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("email", e);
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
    public boolean restoreDb(File importFile){

        File exportFile = new File(db.getPath());
        //File importFile = new File("/storage/emulated/0/hitfit/data.db");

        try {
            //exportFile.createNewFile();
            copyFile(importFile, exportFile);
            return true;
        } catch (IOException e) {
            Log.e("restore",e.toString());
            e.printStackTrace();
            return false;
        }
    }

    private static void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        }catch (Exception e) {
            Log.e("copyfle",e.toString());
        }finally
        {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
    }
}