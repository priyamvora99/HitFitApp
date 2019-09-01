package com.example.priyam.databaselogin;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.util.regex.Pattern;
public class Signup extends AppCompatActivity {
    public EditText email,otp,pwd,cpwd;
    Button signup;
    TextView tvm,tvm2;
    String e;
    String o,p,cp;
    SQLiteDatabase db;
    TextInputLayout el,pl,ol,cpl;
    int flag=0;
    Pattern regex = Pattern.compile("[$&+,:;=\\\\?#|/'<>^*()%!-]");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Sign Up");
        db= openOrCreateDatabase("MyTemp.db", Context.MODE_PRIVATE, null);
        getId();
    }
    private void getId() {
        email=(EditText)findViewById(R.id.emailS);
        tvm2= (TextView) findViewById(R.id.tvm1);
        tvm= (TextView) findViewById(R.id.tvmail);
        otp=(EditText)findViewById(R.id.otp);
        pwd=(EditText)findViewById(R.id.password);
        cpwd=(EditText)findViewById(R.id.cpassword);
        signup=(Button)findViewById(R.id.btnSign);
        el= (TextInputLayout) findViewById(R.id.emaill);
        ol= (TextInputLayout) findViewById(R.id.otpL);
        pl= (TextInputLayout) findViewById(R.id.passL);
        cpl= (TextInputLayout) findViewById(R.id.cpassL);
    }
    public void signUp(View v) {
        try {
            if (otp.getVisibility() == View.INVISIBLE) {
                int i;
                i = 0;
                e = email.getText().toString().trim();
                if(e.length()>0) {
                    if (!e.contains("@")) i = 1;
                    if (!e.contains(".")) i = 1;
                    if (regex.matcher(e).find()) {
                        i=1;
                        Log.e("Chars", "SPECIAL CHARS FOUND");
                    }
                    if (e.indexOf(".")==e.indexOf("@")+1) i=1;
                    if (e.charAt(e.length() - 1) == '@' || e.charAt(e.length() - 1) == '.') i = 1;
                    if (i == 0) {
                        new SendPostRequest().execute();
                    } else {
                        CoordinatorLayout c= (CoordinatorLayout) findViewById(R.id.coordinate);
                        final Snackbar snackbar=Snackbar.make(c,"Enter valid email",Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ff0000"));
                        snackbar.show();
                    }
                }
                else{
                    final Snackbar snackbar=Snackbar.make(signup,"Enter Email",Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ff0000"));
                    snackbar.show();
                    Toast.makeText(getApplicationContext(), "Enter Email",
                            Toast.LENGTH_LONG).show();
                }
            }
            else {
                o = otp.getText().toString().trim();
                p = pwd.getText().toString().trim();
                cp = cpwd.getText().toString().trim();
                if (p.equals(cp)) {
                    if (p.length()<=7){
                        Toast.makeText(getApplicationContext(), "Password Minimum Length is 8",
                                Toast.LENGTH_LONG).show();
                    }else{
                        new SendPostRequest().execute();
                    }
                } else {
                    final Snackbar snackbar=Snackbar.make(signup,"Password Mismatch",Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ff0000"));
                    snackbar.show();
                    Toast.makeText(getApplicationContext(), "Password Mismatch",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),e.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }
    public class SendPostRequest extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(Signup.this);
        protected void onPreExecute(){
            this.dialog.setMessage("Please wait");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }
            protected String doInBackground(String... arg0) {
                try {
                    String file="register.php";
                    JSONObject postDataParams = new JSONObject();
                    if(flag==0) {
                        postDataParams.put("email", e);
                        flag=1;
                    }else{
                        postDataParams.put("otp", o);
                        postDataParams.put("pwd", p);
                    }
                    return (SendData.sendData(file,postDataParams));
                }
                catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }
            }
            @Override
            protected void onPostExecute(String result) {
                dialog.dismiss();
                if(result.contains("Exception:") || result.contains("Message could not be sent")) {
                    Toast.makeText(getApplicationContext(),"An error occurred!\n Please try again! ",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), result,
                            Toast.LENGTH_LONG).show();
                    if (result.equals("Registered Successfully") && otp.getVisibility() == View.VISIBLE) {
                        UserDatabase.insertLogin(e, db);
                        Intent i = new Intent(getApplicationContext(), GetBasicInfo.class);
                        startActivity(i);
                    }
                    else if (result.equals("You are Registered Customer !")) {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    } else {
                        Log.e("visible", String.valueOf(tvm2.getVisibility()));
                        setInvisibleVisible();
                    }
                }
            }
    }
    private void setInvisibleVisible() {
        email.setVisibility(View.INVISIBLE);
        el.setVisibility(View.INVISIBLE);
        tvm.setVisibility(View.INVISIBLE);
        tvm2.setVisibility(View.VISIBLE);
        otp.setVisibility(View.VISIBLE);
        pwd.setVisibility(View.VISIBLE);
        cpwd.setVisibility(View.VISIBLE);
        ol.setVisibility(View.VISIBLE);
        pl.setVisibility(View.VISIBLE);
        cpl.setVisibility(View.VISIBLE);
        signup.setText("Set Password");
    }
}
/*
final Snackbar snackbar=Snackbar.make(login,"Enter valid email",Snackbar.LENGTH_INDEFINITE);
                        snackbar.setActionTextColor(Color.parseColor("#ff0000"));
                        snackbar.setAction("Dismiss", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbar.dismiss();
                            }
                        });
                        snackbar.show();
 */