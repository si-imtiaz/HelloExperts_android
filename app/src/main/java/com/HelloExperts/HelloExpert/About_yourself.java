package com.HelloExperts.HelloExpert;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.Map;

public class About_yourself extends AppCompatActivity {
    EditText user_interest_hobbies,user_education_experience_description;
    TextView save_and_continue_to_vi;
    SharedPreferences dashboard_preference;
    LinearLayout main_loader_about_yourself_bg;
    AVLoadingIndicatorView recent_loader;
    ProgressDialog progressDialog;
    private  int customers_id;
    public String HttpUrl;
    ImageView sign_out_from_page_v;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_yourself);



        assignLocalVariables();
        save_and_continue_to_vi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Checkconnectivity checkconnectivity = new Checkconnectivity();
                if(!checkconnectivity.hasInternetConnection(getApplicationContext())){
                    Toast.makeText(About_yourself.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }else {
                    if (user_interest_hobbies.getText().toString().length() < 30 || user_education_experience_description.getText().toString().length() < 30) {

                        displayErrorMsg("Profile description and Brief introduction cannot be less than 30 characters.");
                    }


                    if (user_interest_hobbies.getText().toString().length() >= 30 &&
                            user_education_experience_description.getText().toString().length() >= 30) {
                        authenticateUserProgressDialogue();
                        submit_about_yourself();
                    }
                }
            }
        });
        sign_out_from_page_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Logout successfully",Toast.LENGTH_LONG).show();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("Login", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit(); // commit changes
                clearFromLog();

            }
        });

        user_education_experience_description.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (user_education_experience_description.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });
        user_interest_hobbies.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (user_interest_hobbies.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });

    }
    public void assignLocalVariables(){


        sign_out_from_page_v = (ImageView)findViewById(R.id.sign_out_from_page_v);
        user_interest_hobbies = (EditText)findViewById(R.id.user_interest_hobbies);
        user_education_experience_description = (EditText)findViewById(R.id.user_education_experience_description);
        save_and_continue_to_vi = (TextView)findViewById(R.id.save_and_continue_to_vi);
        progressDialog = new ProgressDialog(this);
        this.progressDialog.setCanceledOnTouchOutside(false);
        this.dashboard_preference=getApplicationContext().getSharedPreferences("Login",0);
        this.customers_id = dashboard_preference.getInt("customers_id",0);
        HttpUrl = getResources().getString(R.string.about_yourself_api_url);

        main_loader_about_yourself_bg  = (LinearLayout)findViewById(R.id.main_loader_about_yourself_bg);
        recent_loader = (AVLoadingIndicatorView)findViewById(R.id.recent_loader);
        recent_loader.show();



    }
    private void authenticateUserProgressDialogue(){

        this.main_loader_about_yourself_bg.setVisibility(View.VISIBLE);

    }

    private void hideProgressBar(){

        this.main_loader_about_yourself_bg.setVisibility(View.GONE);
    }
    public void submit_about_yourself(){



        RequestQueue queue = Volley.newRequestQueue(getApplicationContext()); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("profile_approved").equals("true")){

                                startActivity(new Intent(About_yourself.this,Congrats.class));

                            }

                        }catch(JSONException e){
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(),"Error : Something went wrong. Please try again",Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("save_about_yourself", "true");
                params.put("hobbies", user_interest_hobbies.getText().toString());
                params.put("description", user_education_experience_description.getText().toString());
                params.put("customers_id", Integer.toString(customers_id));
                return params;
            }
        };
        queue.add(postRequest);
    }
    public void clearFromLog(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext()); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        finish();
                        startActivity(new Intent(getApplicationContext(),login.class));

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(),"Error : Something went wrong. Please try again",Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("clearLog", "true");
                params.put("customers_id", Integer.toString(customers_id));
                return params;
            }
        };
        queue.add(postRequest);
    }
    public void displayErrorMsg(String message_text){
        final Dialog error_msg = new Dialog(this);
        error_msg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        error_msg.setContentView(R.layout.error_alert);
        TextView btn_ok=(TextView)error_msg.findViewById(R.id.btn_ok);
        TextView message=(TextView)error_msg.findViewById(R.id.message);
        message.setText(message_text);
        error_msg.show();
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error_msg.dismiss();
            }
        });


    }


}

