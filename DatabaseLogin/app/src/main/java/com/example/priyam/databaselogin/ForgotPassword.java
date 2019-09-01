package com.example.priyam.databaselogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class ForgotPassword extends AppCompatActivity {

    private EditText mEmailEditText;
    private Button mSendButton;
    public EditText otp,pwd,cpwd;
    TextView tvm,tvm2;
    String e;
    String o,p,cp;
    TextInputLayout el,pl,ol,cpl;
    String email;
    int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getId();
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (otp.getVisibility() == View.INVISIBLE) {
                        email = mEmailEditText.getText().toString();
                        new SendPostRequest().execute();
                    } else {
                        o = otp.getText().toString();
                        flag=1;
                        p=pwd.getText().toString();
                        cp = cpwd.getText().toString();
                        if(p.equals(cp))
                        new SendPostRequest().execute();
                        else{
                            Toast.makeText(getApplicationContext(), "Password Mismatch",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
                catch(Exception e){
                    Toast.makeText(getApplicationContext(), e.toString(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getId() {
        tvm2= (TextView) findViewById(R.id.tvm1);
        tvm= (TextView) findViewById(R.id.tvmail);
        otp=(EditText)findViewById(R.id.otp);
        pwd=(EditText)findViewById(R.id.password);
        cpwd=(EditText)findViewById(R.id.cpassword);
        el= (TextInputLayout) findViewById(R.id.emaill);
        ol= (TextInputLayout) findViewById(R.id.otpL);
        pl= (TextInputLayout) findViewById(R.id.passL);
        cpl= (TextInputLayout) findViewById(R.id.cpassL);
        mEmailEditText = (EditText) findViewById(R.id.email_et);
        mSendButton = (Button) findViewById(R.id.btnSend);
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(ForgotPassword.this);

        protected void onPreExecute(){
            this.dialog.setMessage("Please wait...");
            this.dialog.show();
        }
        protected String doInBackground(String... arg0) {
            try {
                String file="sendemaiil.php";
                JSONObject postDataParams = new JSONObject();
                if (flag==0) {
                    postDataParams.put("email", email);
                }
                else{
                    postDataParams.put("otp", o);
                    postDataParams.put("pwd", cp);
                }
                Log.e("params",postDataParams.toString());
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
            if(result.equals("Mail has been sent")){
                tvm2.setVisibility(View.VISIBLE);
                Log.e("visible", String.valueOf(tvm2.getVisibility()));
                mEmailEditText.setVisibility(View.INVISIBLE);
                el.setVisibility(View.INVISIBLE);
                ol.setVisibility(View.VISIBLE);
                pl.setVisibility(View.VISIBLE);
                cpl.setVisibility(View.VISIBLE);
                tvm.setVisibility(View.INVISIBLE);
                otp.setVisibility(View.VISIBLE);
                pwd.setVisibility(View.VISIBLE);
                cpwd.setVisibility(View.VISIBLE);
                mSendButton.setText("Set Password");
            }
            else{
                Intent i= new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        }
    }
}