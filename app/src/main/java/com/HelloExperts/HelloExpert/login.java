package com.HelloExperts.HelloExpert;

import android.app.Dialog;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.os.Handler;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import android.widget.EditText;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;

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

public class login extends AppCompatActivity
{
    private TextView sign_in,register,forgot_password;

    private EditText email,password;
    public String HttpUrl;
    private String regularExpressionForEmail;
    private Pattern emailPattern;
    String expected_date;

    private LinearLayout main_loader_login_view_bg;
    private AVLoadingIndicatorView recent_loader;
    public access_granted_dialog access;
    private Boolean show_hide_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



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



        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_UP) {

                    if(event.getRawX() >= password.getRight() - password.getTotalPaddingRight()) {
                        // your action here
                        if(!show_hide_password){
                            show_hide_password = true;
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.locked_question, 0, R.drawable.show_password_icon, 0);
                        }else{

                            show_hide_password = false;
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.locked_question, 0, R.drawable.hide, 0);
                        }

                        return true;
                    }
                }
                return false;
            }
        });
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if(!emailPattern.matcher(email.getText().toString().trim()).matches()){
                        email.setError("Enter valid email");
                    }

                }
            }
        });
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailPattern.matcher(email.getText().toString().trim()).matches()){
                    authenticateUserProgressDialogue();
                    sendDataForAuthentication();
                }else{
                    email.setError("Enter valid email");
                }
                if(password.getText().toString().equals("")){
                    password.setError("Password can't be empty");
                }


            }


        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sign_up_page = new Intent(login.this, forgotPassword.class);
                startActivity(sign_up_page);
            }
        });
        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent sign_up_page = new Intent(login.this, signup.class);
                startActivity(sign_up_page);
            }
        });


    }
    private void authenticateUserProgressDialogue(){

        this.main_loader_login_view_bg.setVisibility(View.VISIBLE);

    }

    private void hideProgressBar(){

        this.main_loader_login_view_bg.setVisibility(View.GONE);
    }


    private void assignLocalVariables(){
        show_hide_password = false;

        expected_date = "";
        register = (TextView)findViewById(R.id.register);
        forgot_password = (TextView)findViewById(R.id.forgot_password);
        sign_in = (TextView)findViewById(R.id.sign_in);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);

        regularExpressionForEmail = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        emailPattern = Pattern.compile(regularExpressionForEmail);
        HttpUrl = getResources().getString(R.string.about_yourself_api_url);
        access = new access_granted_dialog(login.this);
        access.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        access.setCanceledOnTouchOutside(false);
        main_loader_login_view_bg = (LinearLayout)findViewById(R.id.main_loader_login_view_bg);
        recent_loader= (AVLoadingIndicatorView)findViewById(R.id.recent_loader);
        recent_loader.show();


    }
    private void sendDataForAuthentication() {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext()); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();
                        parseData(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressBar();
                        Toast.makeText(getApplicationContext(),"Error: Something went wrong. Please try again",Toast.LENGTH_LONG).show();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email.getText().toString().trim());
                params.put("password", password.getText().toString());
                params.put("sign_in", "true");


                return params;
            }
        };
        queue.add(postRequest);
        postRequest.setRetryPolicy(new DefaultRetryPolicy(20000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    private void parseData(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String login_status = jsonObject.getString("login_status");
            if(login_status.equals("false")){
                final Dialog error_msg = new Dialog(this);
                error_msg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                error_msg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                error_msg.setContentView(R.layout.error_alert);
                TextView btn_ok=(TextView)error_msg.findViewById(R.id.btn_ok);
                TextView message=(TextView)error_msg.findViewById(R.id.message);
                message.setText("Email or Password is incorrect");
                error_msg.show();
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        error_msg.dismiss();
                    }
                });
            }else{

                SharedPreferences sp=getSharedPreferences("Login", 0);
                SharedPreferences.Editor Ed=sp.edit();
                Ed.putInt("customers_id",Integer.parseInt(jsonObject.getString("customers_id")));
                Ed.putString("customers_tokken",jsonObject.getString("customers_tokken"));
                Ed.putString("customers_email_address",jsonObject.getString("customers_email_address"));
                Ed.putBoolean("Splash_screen_displayed",true);
                Ed.commit();
                if(jsonObject.getString("login_status").equals("not_verified")) {
                    int completion_level = jsonObject.getInt("completion_level");
                    if(completion_level == 5){
                        expected_date = jsonObject.getString("expected_date");
                    }
                    if(completion_level == 0){
                        access.show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                access.dismiss();
                                startActivity(new Intent(getApplicationContext(),Basic_information.class));
                            }
                        }, 2000);


                    }else if(completion_level == 1){
                        access.show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                access.dismiss();
                                startActivity(new Intent(getApplicationContext(),Education_experience.class));
                            }
                        }, 2000);


                    }else if(completion_level == 2){
                        access.show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                access.dismiss();
                                startActivity(new Intent(getApplicationContext(),Add_skills.class));
                            }
                        }, 2000);

                    }else if(completion_level == 3){
                        access.show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                access.dismiss();
                                startActivity(new Intent(getApplicationContext(),Education_verification.class));
                            }
                        }, 2000);


                    }else if(completion_level == 4){
                        access.show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                access.dismiss();
                                startActivity(new Intent(getApplicationContext(),About_yourself.class));
                            }
                        }, 2000);


                    }else if(completion_level == 5){

                        access.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                access.dismiss();
                                Bundle b = new Bundle();
                                b.putString("expected_date",expected_date);
                                Intent finish_intent= new Intent(getApplicationContext(),Sign_up_finish.class);
                                finish_intent.putExtras(b);
                                startActivity(finish_intent);
                            }
                        }, 2000);


                    }

                }

///////////////////////////////////////////////////////////////////////////////////
                String response_string = jsonObject.getString("customers_id");
                int customers_id = Integer.parseInt(response_string);

                String customers_name = jsonObject.getString("customers_name");
                String customers_email_address = jsonObject.getString("customers_email_address");
                String customers_profile_picture = jsonObject.getString("customers_profile_picture");

                String customers_tokken = jsonObject.getString("customers_tokken");
                String customers_verification_status = jsonObject.getString("customers_verification_status");
                Integer expert_account_status = jsonObject.getInt("expert_account_status");
                String expert_account_suspend_till_date = jsonObject.getString("expert_account_suspend_till_date");
                String locked_question_id_if_exist = jsonObject.getString("locked_question_id_if_exist");



                if(customers_verification_status.equals("1")){
                    String customers_pending_payment = jsonObject.getString("customers_pending_payment");
                    String experts_score = jsonObject.getString("experts_score");

                    Boolean customers_withdraw_payment_flag = jsonObject.getBoolean("customers_withdraw_payment_flag");
                    String customers_withdraw_payment = jsonObject.getString("customers_withdraw_payment");
                    String customers_withdraw_status = jsonObject.getString("customers_withdraw_status");
                    String customers_withdraw_date_heading = jsonObject.getString("customers_withdraw_date_heading");
                    String customers_withdraw_datetime = jsonObject.getString("customers_withdraw_datetime");
                    Boolean customers_bonus_amount_flag = jsonObject.getBoolean("customers_bonus_amount_flag");
                    String customers_bonus_amount = jsonObject.getString("customers_bonus_amount");


                    Ed.putString("customers_pending_payment",customers_pending_payment);
                    Ed.putString("experts_score",experts_score);

                    Ed.putBoolean("customers_withdraw_payment_flag",customers_withdraw_payment_flag);
                    Ed.putString("customers_withdraw_payment",customers_withdraw_payment);
                    Ed.putString("customers_withdraw_status",customers_withdraw_status);
                    Ed.putString("customers_withdraw_date_heading",customers_withdraw_date_heading);
                    Ed.putString("customers_withdraw_datetime",customers_withdraw_datetime);
                    Ed.putBoolean("customers_bonus_amount_flag",customers_bonus_amount_flag);
                    Ed.putString("customers_bonus_amount",customers_bonus_amount);


                }else if(customers_verification_status.equals("2")){
                    Integer expert_probation_answers_count = jsonObject.getInt("expert_probation_answers_count");
                    Integer expert_probation_submitted_count = jsonObject.getInt("expert_probation_submitted_count");
                    Integer expert_probation_accepted_count = jsonObject.getInt("expert_probation_accepted_count");
                    Ed.putInt("expert_probation_answers_count",expert_probation_answers_count);
                    Ed.putInt("expert_probation_submitted_count",expert_probation_submitted_count);
                    Ed.putInt("expert_probation_accepted_count",expert_probation_accepted_count);
                }


                Ed.putString("locked_question_id_if_exist",locked_question_id_if_exist);
                Ed.putString("customers_email_address",customers_email_address);
                Ed.putString("customers_password",password.getText().toString());
                Ed.putString("customers_name",customers_name);
                Ed.putString("customers_profile_picture",customers_profile_picture);
                Ed.putInt("customers_id",customers_id);
                Ed.putString("customers_tokken",customers_tokken);
                Ed.putString("customers_verification_status",customers_verification_status);
                Ed.putString("expert_account_suspend_till_date",expert_account_suspend_till_date);
                Ed.putInt("expert_account_status",expert_account_status);
                Ed.putBoolean("check_feed_back_flag",false);


                Ed.commit();
                access.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        access.dismiss();
                        Intent dashboard = new Intent(getApplicationContext(),dashboard.class);
                        Bundle b = new Bundle();
                        b.putString("activity_name","login");
                        dashboard.putExtras(b);
                        dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(dashboard);
                    }
                }, 2000);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }



}

