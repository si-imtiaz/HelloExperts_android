package com.HelloExperts.HelloExpert;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import android.widget.LinearLayout;

import android.widget.RelativeLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import cn.iwgang.countdownview.CountdownView;

public class Final_answer_preview extends AppCompatActivity {

    boolean loadingFinished = true;
    boolean redirect = false;
    TextView question_description_heading,your_answer_heading,try_another_question_in_final;
    Button edit_answer,final_submit_answer;
    String HttpUrl;
    String customers_tokken;
    String customers_id;
    String question_string;
    String answer_string;
    CountdownView answer_submission_time_in_final;
    LinearLayout main_loader_final_preview_bg;
    AVLoadingIndicatorView recent_loader;
    SharedPreferences final_answer_shared;
    SharedPreferences.Editor final_answer_shared_Ed;
    WebView question_description,temp_answer;
    RelativeLayout question_no_available_section_in_final,final_answer_preview_section;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_answer_preview);



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

        // assign all local variables
        assignLocalvariables();


        // get question and answer detail
        authenticateUserProgressDialogue();


        getQuestionAndAnswerDescription(customers_id,Integer.toString(getIntent().getExtras().getInt("question_id")));

        // call on click functions
        onClickFuncitonsCall();

    }
    void onClickFuncitonsCall(){



        this.question_description.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                if (!loadingFinished) {
                    redirect = true;
                }

                loadingFinished = false;
                view.loadUrl(urlNewString);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                loadingFinished = false;
                //SHOW LOADING IF IT ISNT ALREADY VISIBLE
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(!redirect){
                    loadingFinished = true;
                }

                if(loadingFinished && !redirect){
                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        final_submit_answer.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.final_answer_btn) );
                    } else {
                        final_submit_answer.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.final_answer_btn));
                    }
                    final_submit_answer.setEnabled(true);
                } else{
                    redirect = false;
                }

            }
        });

        this.answer_submission_time_in_final.setOnCountdownIntervalListener(1000, new CountdownView.OnCountdownIntervalListener() {
            @Override
            public void onInterval(CountdownView cv, long remainTime) {
                if(remainTime < 1000){

                    final_answer_preview_section.setVisibility(View.GONE);
                    answer_submission_time_in_final.setVisibility(View.GONE);


                    Animation animation = AnimationUtils.loadAnimation(Final_answer_preview.this, R.anim.slide_question_bar);
                    question_no_available_section_in_final.startAnimation(animation);
                    question_no_available_section_in_final.setVisibility(View.VISIBLE);
                    question_no_available_section_in_final.setAlpha(0.75f);
                    answer_submission_time_in_final.setVisibility(View.GONE);

                    widthDrawQuestion("0",customers_id,Integer.toString(getIntent().getExtras().getInt("question_id")),false);

                }

            }
        });
        this.edit_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Bundle b = new Bundle();
            b.putInt("question_id",getIntent().getExtras().getInt("question_id"));
                Intent edit_answer_intent  = new Intent(Final_answer_preview.this,Answer_submission.class);
                edit_answer_intent.putExtras(b);
                startActivity(edit_answer_intent);
            }
        });
        this.final_submit_answer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Checkconnectivity checkconnectivity = new Checkconnectivity();
                if(checkconnectivity.hasInternetConnection(getApplicationContext())){
                    final_answer_shared_Ed.putString("skip_message_displayed","true");
                    final_answer_shared_Ed.commit(); // commit changes
                    authenticateUserProgressDialogue();
                    finalAnswerSubmission(Integer.toString(getIntent().getExtras().getInt("question_id")),customers_id,getAnswer_string());
                }else{
                    Toast.makeText(Final_answer_preview.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }


            }
        });
        this.try_another_question_in_final.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp=getSharedPreferences("Login", 0);
                SharedPreferences.Editor Ed=sp.edit();
                Ed.putInt("navigation_checked_item_id",R.id.nav_all_questions);
                Ed.putBoolean("check_feed_back_flag",true);
                Ed.commit();

                CheckIfAlreadySignInClass checkIfAlreadySignInClass = new CheckIfAlreadySignInClass(Final_answer_preview.this,HttpUrl,final_answer_shared);
                checkIfAlreadySignInClass.checkIfAlreadySignedIn(final_answer_shared);
            }
        });



    }


    public String getAnswer_string() {
        return answer_string;
    }

    public void setAnswer_string(String answer_string) {
        this.answer_string = answer_string;
    }

    public void finalAnswerSubmission(final String question_id,final String expert_id,final String answer_html){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext()); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("final_submitted").equals("true")){
                                SharedPreferences sp=getApplicationContext().getSharedPreferences("Login", 0);
                                SharedPreferences.Editor Ed=sp.edit();
                                Ed.putInt("navigation_checked_item_id",R.id.nav_Recent_activity);
                                Ed.commit();

                                CheckIfAlreadySignInClass checkIfAlreadySignInClass = new CheckIfAlreadySignInClass(Final_answer_preview.this,HttpUrl,final_answer_shared);
                                checkIfAlreadySignInClass.checkIfAlreadySignedIn(final_answer_shared);


                            }else{

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
                        //error
                        hideProgressBar();
                        Toast.makeText(getApplicationContext(),"Error: Something went wrong. Please try again",Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("final_submit_answer", "true");
                params.put("question_id", question_id);
                params.put("customer_id", expert_id);
                params.put("answer_html", answer_html);



                return params;
            }
        };
        queue.add(postRequest);

    }

    public void getQuestionAndAnswerDescription(final String expert_id, final String question_id){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext()); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            question_string = jsonObject.getString("question_description");
                            String question_description_detail = question_string;
                            question_description.loadDataWithBaseURL("", question_description_detail,  "text/html", "UTF-8", "");
                            setAnswer_string(jsonObject.getString("answer_description"));
                            String temp_answer_detail = getAnswer_string();
                            temp_answer.loadDataWithBaseURL("", temp_answer_detail,  "text/html", "UTF-8", "");

                            if(jsonObject.getInt("differences_question_time_out") <= 0){

                                final_answer_preview_section.setVisibility(View.GONE);
                                question_no_available_section_in_final.setVisibility(View.VISIBLE);
                                widthDrawQuestion("0",customers_id,Integer.toString(getIntent().getExtras().getInt("question_id")),false);

                            }else{
                                answer_submission_time_in_final.start(jsonObject.getInt("differences_question_time_out"));
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
                        //error
                        hideProgressBar();
                        Toast.makeText(getApplicationContext(),"Error: Something went wrong. Please try again",Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("get_question_and_answer_description", "true");
                params.put("question_id", question_id);
                params.put("customer_id", expert_id);



                return params;
            }
        };
        queue.add(postRequest);

    }
    private void widthDrawQuestion(final String withdrawReason,final String customer_id,final String question_id,final Boolean suspend_flag) {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext()); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("suspended").equals("true") &&
                                suspend_flag){

                                CheckIfAlreadySignInClass checkIfAlreadySignInClass = new CheckIfAlreadySignInClass(Final_answer_preview.this,HttpUrl,final_answer_shared);
                                checkIfAlreadySignInClass.checkIfAlreadySignedIn(final_answer_shared);
                            }else if(jsonObject.getString("suspended").equals("true") &&
                                    !suspend_flag){

                                return;
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
                params.put("skip_question_and_suspend_if_applied", "true");
                params.put("customer_id", customer_id);
                params.put("question_id", question_id);
                params.put("reason_id", withdrawReason);


                return params;
            }
        };
        queue.add(postRequest);
        postRequest.setRetryPolicy(new DefaultRetryPolicy(20000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    void assignLocalvariables(){



        this.HttpUrl = getResources().getString(R.string.about_yourself_api_url);
        this.question_description_heading = (TextView)findViewById(R.id.question_description_heading);
        this.your_answer_heading = (TextView)findViewById(R.id.your_answer_heading);
        this.try_another_question_in_final = (TextView)findViewById(R.id.try_another_question_in_final);

        this.try_another_question_in_final.setPaintFlags(this.try_another_question_in_final.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        this.question_no_available_section_in_final = (RelativeLayout)findViewById(R.id.question_no_available_section_in_final);
        this.final_answer_preview_section = (RelativeLayout)findViewById(R.id.final_answer_preview_section);

        this.answer_submission_time_in_final = (CountdownView)findViewById(R.id.answer_submission_time_in_final);

        this.question_description_heading.setTypeface(null, Typeface.BOLD);
        this.your_answer_heading.setTypeface(null, Typeface.BOLD);
        this.edit_answer = (Button)findViewById(R.id.edit_answer);
        this.final_submit_answer = (Button)findViewById(R.id.final_submit_answer);
        main_loader_final_preview_bg = (LinearLayout)findViewById(R.id.main_loader_final_preview_bg);
        recent_loader= (AVLoadingIndicatorView)findViewById(R.id.recent_loader);
        recent_loader.show();
        this.final_answer_shared=getApplicationContext().getSharedPreferences("Login", 0);
        int temp_id = this.final_answer_shared.getInt("customers_id",0);
        customers_tokken =this.final_answer_shared.getString("customers_tokken",null);
        customers_id = Integer.toString(temp_id);

        this.final_answer_shared_Ed = this.final_answer_shared.edit();
        this.question_description = (WebView)findViewById(R.id.question_description);
        this.temp_answer=(WebView)findViewById(R.id.temp_answer);

     //   this.question_description.getSettings().setBuiltInZoomControls(true);
      //  this.temp_answer.getSettings().setBuiltInZoomControls(true);
        this.final_submit_answer.setEnabled(false);


    }
    private void authenticateUserProgressDialogue(){

        this.main_loader_final_preview_bg.setVisibility(View.VISIBLE);

    }
    private void hideProgressBar(){

        this.main_loader_final_preview_bg.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {


        if (answer_submission_time_in_final.getVisibility() == View.GONE) {

            SharedPreferences sp = getSharedPreferences("Login", 0);
            SharedPreferences.Editor Ed = sp.edit();
            Ed.putInt("navigation_checked_item_id", R.id.nav_all_questions);
            Ed.commit();

            CheckIfAlreadySignInClass checkIfAlreadySignInClass = new CheckIfAlreadySignInClass(Final_answer_preview.this, HttpUrl, final_answer_shared);
            checkIfAlreadySignInClass.checkIfAlreadySignedIn(final_answer_shared);
        }else{
            Final_answer_preview.super.onBackPressed();
        }


    }


}
