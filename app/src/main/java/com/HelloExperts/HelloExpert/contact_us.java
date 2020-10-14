package com.HelloExperts.HelloExpert;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.HelloExperts.HelloExpert.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import customfonts.MyRegulerText;

public class contact_us extends AppCompatActivity {

    private MyRegulerText question_1,question_2,question_3,question_4,question_5,question_6,question_7,question_8
            ,question_9,question_10,question_11,question_12,question_13,question_14,question_15,question_16,
            question_ex_5,question_ex_6,question_ex_7,question_ex_8,question_ex_9,question_ex_10,question_ex_11,
            question_ex_12,question_ex_13,question_ex_14,question_ex_15,question_ex_16,question_ex_17,question_ex_18,
            question_ex_19,question_ex_20,question_ex_21,question_ex_22,question_ex_23,earning_problem,deactivate_account;

    private Dialog contact_us;
    String regulerExpressionForEmailregistration,HttpUrl;
    LinearLayout main_loader_contact_activity_bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        AVLoadingIndicatorView recent_loader;



        regulerExpressionForEmailregistration = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        HttpUrl = getResources().getString(R.string.about_yourself_api_url);
        main_loader_contact_activity_bg = (LinearLayout)findViewById(R.id.main_loader_contact_activity_bg);
        recent_loader = (AVLoadingIndicatorView)findViewById(R.id.recent_loader);

        earning_problem = (MyRegulerText)findViewById(R.id.earning_problem);
        deactivate_account = (MyRegulerText)findViewById(R.id.deactivate_account);


        question_1 = (MyRegulerText)findViewById(R.id.question_1);
        question_2 = (MyRegulerText)findViewById(R.id.question_2);
        question_3 = (MyRegulerText)findViewById(R.id.question_3);
        question_4 = (MyRegulerText)findViewById(R.id.question_4);
        question_5 = (MyRegulerText)findViewById(R.id.question_5);
        question_6 = (MyRegulerText)findViewById(R.id.question_6);
        question_7 = (MyRegulerText)findViewById(R.id.question_7);
        question_8 = (MyRegulerText)findViewById(R.id.question_8);
        question_9 = (MyRegulerText)findViewById(R.id.question_9);
        question_10 = (MyRegulerText)findViewById(R.id.question_10);
        question_11 = (MyRegulerText)findViewById(R.id.question_11);
        question_12 = (MyRegulerText)findViewById(R.id.question_12);
        question_13 = (MyRegulerText)findViewById(R.id.question_13);
        question_14 = (MyRegulerText)findViewById(R.id.question_14);
        question_15 = (MyRegulerText)findViewById(R.id.question_15);


        question_ex_5 = (MyRegulerText)findViewById(R.id.question_ex_5);
        question_ex_6 = (MyRegulerText)findViewById(R.id.question_ex_6);
        question_ex_7 = (MyRegulerText)findViewById(R.id.question_ex_7);
        question_ex_8 = (MyRegulerText)findViewById(R.id.question_ex_8);
        question_ex_9 = (MyRegulerText)findViewById(R.id.question_ex_9);
        question_ex_10 = (MyRegulerText)findViewById(R.id.question_ex_10);
        question_ex_11 = (MyRegulerText)findViewById(R.id.question_ex_11);
        question_ex_12 = (MyRegulerText)findViewById(R.id.question_ex_12);
        question_ex_13 = (MyRegulerText)findViewById(R.id.question_ex_13);
        question_ex_14 = (MyRegulerText)findViewById(R.id.question_ex_14);
        question_ex_15 = (MyRegulerText)findViewById(R.id.question_ex_15);
        question_ex_16 = (MyRegulerText)findViewById(R.id.question_ex_16);
        question_ex_17 = (MyRegulerText)findViewById(R.id.question_ex_17);
        question_ex_18 = (MyRegulerText)findViewById(R.id.question_ex_18);
        question_ex_19 = (MyRegulerText)findViewById(R.id.question_ex_19);
        question_ex_20 = (MyRegulerText)findViewById(R.id.question_ex_20);
        question_ex_21 = (MyRegulerText)findViewById(R.id.question_ex_21);
        question_ex_22 = (MyRegulerText)findViewById(R.id.question_ex_22);
        question_ex_23 = (MyRegulerText)findViewById(R.id.question_ex_23);




        question_1.setTypeface(null, Typeface.BOLD);
        question_2.setTypeface(null, Typeface.BOLD);
        question_3.setTypeface(null, Typeface.BOLD);
        question_4.setTypeface(null, Typeface.BOLD);
        question_5.setTypeface(null, Typeface.BOLD);
        question_6.setTypeface(null, Typeface.BOLD);
        question_7.setTypeface(null, Typeface.BOLD);
        question_8.setTypeface(null, Typeface.BOLD);
        question_9.setTypeface(null, Typeface.BOLD);
        question_10.setTypeface(null, Typeface.BOLD);
        question_11.setTypeface(null, Typeface.BOLD);
        question_12.setTypeface(null, Typeface.BOLD);
        question_13.setTypeface(null, Typeface.BOLD);
        question_14.setTypeface(null, Typeface.BOLD);
        question_15.setTypeface(null, Typeface.BOLD);



        question_ex_5.setTypeface(null, Typeface.BOLD);
        question_ex_6.setTypeface(null, Typeface.BOLD);
        question_ex_7.setTypeface(null, Typeface.BOLD);
        question_ex_8.setTypeface(null, Typeface.BOLD);
        question_ex_9.setTypeface(null, Typeface.BOLD);
        question_ex_10.setTypeface(null, Typeface.BOLD);
        question_ex_11.setTypeface(null, Typeface.BOLD);
        question_ex_12.setTypeface(null, Typeface.BOLD);
        question_ex_13.setTypeface(null, Typeface.BOLD);
        question_ex_14.setTypeface(null, Typeface.BOLD);
        question_ex_15.setTypeface(null, Typeface.BOLD);
        question_ex_16.setTypeface(null, Typeface.BOLD);
        question_ex_17.setTypeface(null, Typeface.BOLD);
        question_ex_18.setTypeface(null, Typeface.BOLD);
        question_ex_19.setTypeface(null, Typeface.BOLD);
        question_ex_20.setTypeface(null, Typeface.BOLD);
        question_ex_21.setTypeface(null, Typeface.BOLD);
        question_ex_22.setTypeface(null, Typeface.BOLD);
        question_ex_23.setTypeface(null, Typeface.BOLD);

        earning_problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit_contact_us("Earnings not updated.","My answer count and earnings are not being updated. Please review this matter.");
            }
        });
        deactivate_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit_contact_us("Close my account.","I no longer want to use my account, please deactivate it.");
            }
        });


    }

    public void submit_contact_us(final String subject, final String message){

        this.contact_us = new Dialog(this);
        contact_us.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        contact_us.requestWindowFeature(Window.FEATURE_NO_TITLE);
        contact_us.setContentView(R.layout.contact_us_faq);


        TextView submit_to_complain=(TextView)contact_us.findViewById(R.id.submit_to_complain);
        TextView close_email=(TextView)contact_us.findViewById(R.id.close_email);
        TextView email_title=(TextView)contact_us.findViewById(R.id.email_title);

        final EditText email_expert = (EditText)contact_us.findViewById(R.id.email_expert);


        submit_to_complain.setTypeface(null, Typeface.BOLD);
        close_email.setTypeface(null,Typeface.BOLD);
        email_title.setTypeface(null,Typeface.BOLD);


        contact_us.show();


        close_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contact_us.dismiss();

            }
        });
        submit_to_complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(email_expert.getText().toString().equals("")){
                    email_expert.setError("Enter Email address");
                }else if(!Pattern.compile(regulerExpressionForEmailregistration).matcher(email_expert.getText().toString()).matches()){

                    email_expert.setError("Enter valid email");
                }else{
                    authenticateUserProgressDialogue();
                   send_contact_data_to_server(email_expert.getText().toString(),subject,message);
                   contact_us.dismiss();

                }




            }
        });

    }

    public void send_contact_data_to_server(final String email,final String subject,final String message){

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext()); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();



                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //error
                        hideProgressBar();
                        Toast.makeText(getApplicationContext(),"Error : Something went wrong. Please try again",Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("submit_contact_us_faq", "true");
                params.put("email", email);
                params.put("subject", subject);
                params.put("message", message);

                return params;
            }
        };
        queue.add(postRequest);
        postRequest.setRetryPolicy(new DefaultRetryPolicy(20000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void authenticateUserProgressDialogue(){

        this.main_loader_contact_activity_bg.setVisibility(View.VISIBLE);

    }

    private void hideProgressBar(){

        this.main_loader_contact_activity_bg.setVisibility(View.GONE);
    }
}
