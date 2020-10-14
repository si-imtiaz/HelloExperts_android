package com.HelloExperts.HelloExpert;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.HelloExperts.HelloExpert.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class forgotPassword extends AppCompatActivity {
    private TextView cancel_forotton_password,reset_forgotton_password,git_it_email_received,user_checked_email,customer_support;
    private EditText forgotten_email;
    private Pattern emailPattern;
    private String regularExpressionForEmail;
    private String HttpUrl;

    public ProgressDialog progressDialog;
    private LinearLayout getting_checked_email,getting_forgotten_email,main_loader_forget_password_bg;
    AVLoadingIndicatorView recent_loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //HttpTrustManager.allowAllSSL(); // allow all SSL
        try {
            HttpsURLConnection.setDefaultSSLSocketFactory(HttpTrustManager.getSocketFactory(getApplicationContext()));
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        assignLocalVariables();
        customer_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),contact_us.class));
            }
        });
        cancel_forotton_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        reset_forgotton_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(forgotten_email.getText().toString().trim().equals("")){
                    forgotten_email.setError("Email Required to reset the password");
                }else if(!emailPattern.matcher(forgotten_email.getText().toString()).matches()){
                    forgotten_email.setError("Enter Valid email");
                }else{
                    authenticateUserProgressDialogue();
                    sendDataForAuthentication();
                }

            }
        });
        git_it_email_received.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
    private void assignLocalVariables(){
        user_checked_email = (TextView)findViewById(R.id.user_checked_email);
        customer_support = (TextView)findViewById(R.id.customer_support);
        getting_checked_email = (LinearLayout)findViewById(R.id.getting_checked_email);
        getting_forgotten_email = (LinearLayout)findViewById(R.id.getting_forgotten_email);
        progressDialog = new ProgressDialog(this);
        reset_forgotton_password=(TextView)findViewById(R.id.reset_forgotton_password);
        git_it_email_received=(TextView)findViewById(R.id.got_it_email_received);
        cancel_forotton_password=(TextView)findViewById(R.id.cancel_forgotton);
        forgotten_email = (EditText)findViewById(R.id.forgotten_email);
        regularExpressionForEmail = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        emailPattern = Pattern.compile(regularExpressionForEmail);
        HttpUrl = getResources().getString(R.string.about_yourself_api_url);
        main_loader_forget_password_bg = (LinearLayout)findViewById(R.id.main_loader_forget_password_bg);
        recent_loader = (AVLoadingIndicatorView)findViewById(R.id.recent_loader);
        recent_loader.show();

    }
    private void sendDataForAuthentication() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext()); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String action = jsonObject.getString("action");
                            if(Integer.parseInt(action) == 0 ){
                                forgotten_email.setError("This Email doesn't exist");
                            }else if(Integer.parseInt(action) == 1 ){
                                user_checked_email.setText(forgotten_email.getText().toString().trim());
                                getting_forgotten_email.setVisibility(View.GONE);
                                getting_checked_email.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressBar();
                        Toast.makeText(getApplicationContext(),"Error : Something went wrong. Please try again",Toast.LENGTH_LONG).show();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", forgotten_email.getText().toString().trim());
                params.put("forgot_password", "true");


                return params;
            }
        };
        queue.add(postRequest);
    }
    private void authenticateUserProgressDialogue(){

        this.main_loader_forget_password_bg.setVisibility(View.VISIBLE);

    }

    private void hideProgressBar(){

        this.main_loader_forget_password_bg.setVisibility(View.GONE);
    }



}
