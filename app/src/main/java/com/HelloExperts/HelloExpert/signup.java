package com.HelloExperts.HelloExpert;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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

public class signup extends AppCompatActivity {
   private EditText signup_email,signup_password;
    private LinearLayout validation_checks,main_loader_sign_up_view_bg;
    AVLoadingIndicatorView recent_loader;
    boolean hasUppercase;
    boolean hasLowercase;
    boolean isAtLeast6;
    public String HttpUrl;
    public ProgressDialog progressDialog;
    boolean hasNumber;
    String regulerExpressionForEmailregistration;
    private Button one_capital,one_small,one_number,six_char;
    private TextView sign_up,terms_and_conditions,privacy_policy,sign_from_sign_up;
    private Boolean show_hide_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


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

        signup_email=(EditText)findViewById(R.id.signup_email);
        signup_password=(EditText)findViewById(R.id.signup_password);
        validation_checks=(LinearLayout)findViewById(R.id.validation_checks);
        one_capital=(Button)findViewById(R.id.one_capital);
        one_small=(Button)findViewById(R.id.one_small);
        one_number=(Button)findViewById(R.id.one_number);
        six_char=(Button)findViewById(R.id.six_char);
        sign_up=(TextView)findViewById(R.id.sign_up);
        terms_and_conditions=(TextView)findViewById(R.id.terms_and_conditions);
        privacy_policy=(TextView) findViewById(R.id.privacy_policy);
        sign_from_sign_up =(TextView)findViewById(R.id.sign_from_sign_up);
        main_loader_sign_up_view_bg = (LinearLayout)findViewById(R.id.main_loader_sign_up_view_bg);
        recent_loader = (AVLoadingIndicatorView)findViewById(R.id.recent_loader);
        show_hide_password = false;
        recent_loader.show();

        HttpUrl = getResources().getString(R.string.about_yourself_api_url);
        progressDialog = new ProgressDialog(this);
        this.progressDialog.setCanceledOnTouchOutside(false);
        sign_from_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(),login.class));

            }
        });
        regulerExpressionForEmailregistration = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        terms_and_conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(signup_email.getText().toString().equals("")){
                    signup_email.setError("Email is empty");
                }else if(!Pattern.compile(regulerExpressionForEmailregistration).matcher(signup_email.getText().toString().trim()).matches()){

                    signup_email.setError("Enter valid email");
                }
                if(signup_password.getText().toString().equals("")){
                    signup_password.setError("Password is empty");
                    validation_checks.setVisibility(View.VISIBLE);
                }else {

                    if (hasLowercase && hasUppercase && hasNumber && isAtLeast6) {
                        authenticateUserProgressDialogue();
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("Login", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.clear();
                        editor.commit(); // commit changes


                        RegisterUser();
                    } else {
                        signup_password.setError("Enter valid password");
                    }
                }
            }
        });

        signup_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_UP) {

                    if(event.getRawX() >= signup_password.getRight() - signup_password.getTotalPaddingRight()) {
                        // your action here
                        if(!show_hide_password){
                            show_hide_password = true;
                            signup_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            signup_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.locked_question, 0, R.drawable.show_password_icon, 0);
                        }else{

                            show_hide_password = false;
                            signup_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            signup_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.locked_question, 0, R.drawable.hide, 0);
                        }

                        return true;
                    }
                }
                return false;
            }
        });

        signup_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if(signup_email.getText().toString().equals("")){
                        signup_email.setError("Email is empty");
                    }else if(!Pattern.compile(regulerExpressionForEmailregistration).matcher(signup_email.getText().toString().trim()).matches()){

                        signup_email.setError("Enter valid email");
                    }

                }
            }
        });
        signup_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                   validation_checks.setVisibility(View.VISIBLE);
                }else{
                    validation_checks.setVisibility(View.GONE);
                }
            }
        });
        signup_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                hasUppercase = !signup_password.getText().toString().equals(signup_password.getText().toString().toLowerCase());
                hasLowercase = !signup_password.getText().toString().equals(signup_password.getText().toString().toUpperCase());
                isAtLeast6   = signup_password.getText().toString().length() >= 6;//Checks for at least 8 characters
                hasNumber = Pattern.compile( "[0-9]" ).matcher(signup_password.getText().toString()).find();

                one_capital.setBackground(getResources().getDrawable(R.drawable.circle_shape_red));
                one_small.setBackground(getResources().getDrawable(R.drawable.circle_shape_red));
                one_number.setBackground(getResources().getDrawable(R.drawable.circle_shape_red));
                six_char.setBackground(getResources().getDrawable(R.drawable.circle_shape_red));
                if(hasUppercase){
                    one_capital.setBackground(getResources().getDrawable(R.drawable.circle_shape_btn_color));

                }if(hasLowercase){
                    one_small.setBackground(getResources().getDrawable(R.drawable.circle_shape_btn_color));

                }
                if(hasNumber){
                    one_number.setBackground(getResources().getDrawable(R.drawable.circle_shape_btn_color));

                }
                if(isAtLeast6){
                    six_char.setBackground(getResources().getDrawable(R.drawable.circle_shape_btn_color));

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void RegisterUser(){

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext()); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                           if(jsonObject.getInt("isEmailExistStatus") == 0){

                               signup_email.setError("Email Already Exists");

                           }else if(jsonObject.getInt("isEmailExistStatus") == 1){
                               SharedPreferences sp=getSharedPreferences("Login", 0);
                               SharedPreferences.Editor Ed=sp.edit();

                               Ed.putBoolean("Splash_screen_displayed",true);
                               Ed.putInt("customers_id",jsonObject.getInt("customers_id"));
                               Ed.putString("customers_tokken",jsonObject.getString("customers_tokken"));
                               Ed.putString("customers_email_address",jsonObject.getString("customers_email_address"));
                               Ed.commit();
                               Intent intent = new Intent(getApplicationContext(),Basic_information.class);
                               startActivity(intent);

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
                params.put("email", signup_email.getText().toString().trim());
                params.put("password", signup_password.getText().toString());
                params.put("sign_up", "true");


                return params;
            }
        };
        queue.add(postRequest);
    }

    private void authenticateUserProgressDialogue(){

        this.main_loader_sign_up_view_bg.setVisibility(View.VISIBLE);

    }

    private void hideProgressBar(){

        this.main_loader_sign_up_view_bg.setVisibility(View.GONE);
    }


}
