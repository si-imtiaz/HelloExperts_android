package com.HelloExperts.HelloExpert;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;



import android.app.ProgressDialog;

import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.HelloExperts.HelloExpert.R;
import com.hmomeni.progresscircula.ProgressCircula;
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


public class Question_view extends AppCompatActivity {
    private TextView lock_and_answer,already_locked_answer_now,question_status,account_suspended_notification,answer_description_heading;
    private ProgressDialog progressDialog;

    private String HttpUrl;
    private int q_id;
    SharedPreferences dashboard_preference;
    String customers_id;
    WebView question_description,answer_description;
    LinearLayout already_locked_layout,main_loader_question_view_bg;
    private String if_already_locked_question;
    private int current_question_id;
    private String from_answers;
    public String customers_tokken;
    public TextView timer_text,terms_and_conditions_heading_note,error_select_terms,notification_text;
    public ProgressCircula round_progress;
    public Button answer_now,answer_now_cancel;
    public ImageView terms_and_conditions_close_btn;
    public RelativeLayout progress_section,terms_and_conditions_section;
    public LinearLayout question_section;

    public CheckBox terms_and_conditions_1,terms_and_conditions_2,terms_and_conditions_3,terms_and_conditions_4,terms_and_conditions_5;
    private AVLoadingIndicatorView recent_loader;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_view);


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

        TypefaceUtils.overrideFont(getApplicationContext(), "SERIF", "fonts/Lato-Light.ttf");
        this.assignLocalVariables();
        Bundle b1= getIntent().getExtras();
        if(b1.getBoolean("notification_from")){

            if(!dashboard_preference.contains("customers_id")){
                Intent new_intent = new Intent(getApplicationContext(),login.class);
                new_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(new_intent);
                finish();
            }
        }

        this.authenticateUserProgressDialogue();
        this.getExpertQuestionNotification();
        this.authenticateUserProgressDialogue();
        this.sendDataForAuthentication();





        this.already_locked_answer_now.setClickable(true);
        String status = b1.getString("status");
        if(b1.getBoolean("This_is_already_locked_question")){
            this.lock_and_answer.setVisibility(View.GONE);


        }
        if(b1.getString("from_recent").equals("true")){
            from_answers = "true";
            if(status.equals("In-Progress")){
                this.question_status.setText("Submitted");
                this.question_status.setBackgroundResource(R.drawable.answer_in_progress_background);
            }else if(status.equals("Rejected")){
                    this.question_status.setText("Rejected");
                    this.question_status.setBackgroundResource(R.drawable.answer_rejected_background);
            }else if(status.equals("Accpeted")){
                this.question_status.setText("Accepted");
                this.question_status.setBackgroundResource(R.drawable.answer_accepted_background);
            }
            this.question_status.setVisibility(View.VISIBLE);
        }
        if(b1.getBoolean("show_status")){
            from_answers = "true";
          if(b1.getString("status").equals("Accepted")){
               this.question_status.setText("Accepted");
               this.question_status.setBackgroundResource(R.drawable.answer_accepted_background);
            }else if(b1.getString("status").equals("Submitted")){
                this.question_status.setText("Submitted");
                this.question_status.setBackgroundResource(R.drawable.answer_in_progress_background);
            }
            else if(b1.getString("status").equals("Rejected")){
                this.question_status.setText("Rejected");
                this.question_status.setBackgroundResource(R.drawable.answer_rejected_background);
            }
            this.question_status.setVisibility(View.VISIBLE);
        }

        this.lock_and_answer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Checkconnectivity checkconnectivity = new Checkconnectivity();
                if(checkconnectivity.hasInternetConnection(getApplicationContext())){
                    Animation animation = AnimationUtils.loadAnimation(Question_view.this, R.anim.slide_question_bar);
                    terms_and_conditions_section.startAnimation(animation);
                    terms_and_conditions_section.setVisibility(View.VISIBLE);
                    terms_and_conditions_section.setAlpha(0.75f);
                    question_section.setVisibility(View.GONE);
                    lock_and_answer.setVisibility(View.GONE);

                }else{
                    Toast.makeText(Question_view.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }


            }
        });
        this.already_locked_answer_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(already_locked_answer_now.getText().toString().toLowerCase().equals("kindly try another")){

                    Intent intent = new Intent(Question_view.this,dashboard.class);
                    SharedPreferences sp=getSharedPreferences("Login", 0);
                    SharedPreferences.Editor Ed=sp.edit();
                    Ed.putInt("navigation_checked_item_id",R.id.nav_all_questions);
                    Ed.commit();
                    startActivity(intent);

                }else if(already_locked_answer_now.getText().toString().toLowerCase().equals("answer now")){


                    Bundle question_info = new Bundle();
                    question_info.putInt("question_id",Integer.parseInt(if_already_locked_question));
                    Intent question_view_intent = new Intent(Question_view.this,Answer_submission.class);
                    question_view_intent.putExtras(question_info);
                    startActivity(question_view_intent);
                }

            }
        });

        this.answer_now.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Checkconnectivity checkconnectivity = new Checkconnectivity();
                if(checkconnectivity.hasInternetConnection(getApplicationContext())){

                    if( terms_and_conditions_1.isChecked() &&
                            terms_and_conditions_2.isChecked() &&
                            terms_and_conditions_3.isChecked() &&
                            terms_and_conditions_4.isChecked() &&
                            terms_and_conditions_5.isChecked()){

                        terms_and_conditions_section.setVisibility(View.GONE);
                        question_section.setVisibility(View.GONE);
                        lock_and_answer.setVisibility(View.GONE);
                        progress_section.setVisibility(View.VISIBLE);

                        new CountDownTimer(6000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                timer_text.setText( Integer.toString((int) (millisUntilFinished / 1000)));
                            }

                            public void onFinish() {

                                lockQuestionForExpert();
                            }
                        }.start();
                    }else{
                        error_select_terms.setVisibility(View.VISIBLE);
                    }

                }else{
                    Toast.makeText(Question_view.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }




            }
        });

        this.terms_and_conditions_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                terms_and_conditions_section.setVisibility(View.GONE);
                progress_section.setVisibility(View.GONE);
                question_section.setVisibility(View.VISIBLE);
                lock_and_answer.setVisibility(View.VISIBLE);
            }
        });
        this.answer_now_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                terms_and_conditions_section.setVisibility(View.GONE);
                progress_section.setVisibility(View.GONE);
                question_section.setVisibility(View.VISIBLE);
                lock_and_answer.setVisibility(View.VISIBLE);
            }
        });




    }

    public void setCurrent_question_id(int current_question_id) {
        this.current_question_id = current_question_id;
    }
    public int getCurrent_question_id() {
        return current_question_id;
    }
    private void assignLocalVariables(){
        this.answer_description_heading = (TextView)findViewById(R.id.answer_description_heading);
        this.question_status = (TextView)findViewById(R.id.question_status);
        this.lock_and_answer = (TextView) findViewById(R.id.lock_and_answer);
        this.lock_and_answer.setTypeface(null,Typeface.BOLD);
        this.account_suspended_notification = (TextView)findViewById(R.id.account_suspended_notification);
        this.account_suspended_notification.setTypeface(null,Typeface.BOLD);
        this.terms_and_conditions_heading_note = (TextView)findViewById(R.id.terms_and_conditions_heading_note);
        this.timer_text = (TextView)findViewById(R.id.timer_text);
        this.round_progress = (ProgressCircula)findViewById(R.id.progressBar);
        this.terms_and_conditions_heading_note.setTypeface(null, Typeface.BOLD);
        /* Start Question Description Web view */
        this.question_description = (WebView) findViewById(R.id.question_description);
        this.question_description.getSettings().setBuiltInZoomControls(true);
        this.question_description.setVerticalScrollBarEnabled(true);
        this.question_description.setHorizontalScrollBarEnabled(true);


        /* End Question Description Web view */

        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setCanceledOnTouchOutside(false);
        this.HttpUrl = getResources().getString(R.string.about_yourself_api_url);
        this.dashboard_preference=getApplicationContext().getSharedPreferences("Login",0);
        int temp_id = dashboard_preference.getInt("customers_id",0);
        customers_tokken = dashboard_preference.getString("customers_tokken",null);
        customers_id = Integer.toString(temp_id);
        this.already_locked_layout = (LinearLayout)findViewById(R.id.already_locked_layout);
        this.if_already_locked_question = "0";
        this.already_locked_answer_now =(TextView)findViewById(R.id.already_locked_answer_now);
        this.from_answers = "false";

        /* Start Answer Description Web view */
        this.answer_description = (WebView)findViewById(R.id.answer_description);
        this.answer_description.getSettings().setBuiltInZoomControls(true);
        this.answer_description.setVerticalScrollBarEnabled(true);
        this.answer_description.setHorizontalScrollBarEnabled(true);



        /*  End Answer Description Web view */

        this.answer_now = (Button)findViewById(R.id.answer_now);
        this.answer_now_cancel = (Button)findViewById(R.id.answer_now_cancel);
        this.terms_and_conditions_close_btn = (ImageView)findViewById(R.id.terms_and_conditions_close_btn);
        this.progress_section = (RelativeLayout)findViewById(R.id.progress_section);
        this.terms_and_conditions_section = (RelativeLayout)findViewById(R.id.terms_and_conditions_section);
        this.question_section = (LinearLayout) findViewById(R.id.question_section);
        this.terms_and_conditions_1 = (CheckBox)findViewById(R.id.terms_and_conditions_1);
        this.terms_and_conditions_2 = (CheckBox)findViewById(R.id.terms_and_conditions_2);
        this.terms_and_conditions_3 = (CheckBox)findViewById(R.id.terms_and_conditions_3);
        this.terms_and_conditions_4 = (CheckBox)findViewById(R.id.terms_and_conditions_4);
        this.terms_and_conditions_5 = (CheckBox)findViewById(R.id.terms_and_conditions_5);
        this.error_select_terms = (TextView)findViewById(R.id.error_select_terms);
        this.notification_text = (TextView)findViewById(R.id.notification_text);
        main_loader_question_view_bg = (LinearLayout)findViewById(R.id.main_loader_question_view_bg);
        recent_loader= (AVLoadingIndicatorView)findViewById(R.id.recent_loader);
        recent_loader.show();


    }
    private void authenticateUserProgressDialogue(){

        this.main_loader_question_view_bg.setVisibility(View.VISIBLE);

    }
    private void hideProgressBar(){

        this.main_loader_question_view_bg.setVisibility(View.GONE);
    }
    public void setIf_already_locked_question(String if_already_locked_question) {
        this.if_already_locked_question = if_already_locked_question;
    }
    public String getIf_already_locked_question() {
        return if_already_locked_question;
    }
    private void sendDataForAuthentication() {
        Bundle b = getIntent().getExtras();
        q_id = b.getInt("question_id");


        this.setCurrent_question_id(q_id);


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
                params.put("get_question_detail", "true");
                params.put("customers_id", customers_id);
                params.put("from_answers", from_answers );
                params.put("customers_tokken",customers_tokken);
                params.put("question_id", Integer.toString(getCurrent_question_id()));

                return params;
            }
        };
        queue.add(postRequest);
    }
    private void parseData(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("login_status").equals("false") &&
                    jsonObject.getString("data").equals("go_to_login")) {
                Toast.makeText(getApplicationContext(), "Error : You must login first to continue", Toast.LENGTH_LONG).show();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("Login", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit(); // commit changes
                finish();
                startActivity(new Intent(getApplicationContext(), login.class));
            }

            String q_description = jsonObject.getString("question_description");
            String q_price = jsonObject.getString("question_price");
            String posted_time = jsonObject.getString("posted_ago");
            String skills_required = jsonObject.getString("skill_required");
            String already_locked_question_id = jsonObject.getString("already_locked_question_id");
            Boolean dont_show_answer_now = jsonObject.getBoolean("dont_show_answer_now");
            String a_description = jsonObject.getString("answer_description");
            Integer expert_account_status = jsonObject.getInt("expert_account_status");




            if(expert_account_status == 2){
                this.lock_and_answer.setVisibility(View.GONE);
                this.account_suspended_notification.setVisibility(View.VISIBLE);
            }

            if(!TextUtils.isEmpty(a_description)){
                this.already_locked_layout.setVisibility(View.GONE);
                this.lock_and_answer.setVisibility(View.GONE);
                answer_description.loadDataWithBaseURL("", a_description.trim(),  "text/html", "UTF-8", "");
                answer_description.setVisibility(View.VISIBLE);
                answer_description_heading.setVisibility(View.VISIBLE);
            }else{
                ViewGroup.LayoutParams layoutParams = question_description.getLayoutParams();

                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;


                question_description.setLayoutParams(layoutParams);
            }


            if(dont_show_answer_now){
                this.already_locked_layout.setVisibility(View.GONE);

            }else {

                if (!already_locked_question_id.equals("0")) {
                    setIf_already_locked_question(already_locked_question_id);
                    this.already_locked_layout.setVisibility(View.VISIBLE);
                    lock_and_answer.setEnabled(false);
                    lock_and_answer.setBackgroundResource(R.drawable.locked_grey_btn);
                }

            }

            final String mimeType = "text/html";
            final String encoding = "UTF-8";

            question_description.loadDataWithBaseURL("",   q_description.trim(), mimeType, encoding, "");


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void lockQuestionForExpert(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext()); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();


                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getInt("type") == 3){
                                Intent answer_submission = new Intent(Question_view.this,Answer_submission.class);
                                Bundle b = new Bundle();
                                b.putInt("question_id",q_id);
                                answer_submission.putExtras(b);
                                startActivity(answer_submission);
                            }else if(jsonObject.getInt("type") == 2){
                                String msg = jsonObject.getString("msj");
                                msg  = Html.fromHtml(msg).toString();
                                progress_section.setVisibility(View.GONE);
                                question_section.setVisibility(View.VISIBLE);
                                lock_and_answer.setVisibility(View.GONE);
                                already_locked_layout.setVisibility(View.VISIBLE);
                                String[] separated = msg.split("Kindly");
                                notification_text.setText(separated[0]);
                                already_locked_answer_now.setText("Kindly try another");

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
                params.put("locked_question", "true");
                params.put("customers_id", customers_id);
                params.put("question_id", Integer.toString(getCurrent_question_id()));

                return params;
            }
        };
        queue.add(postRequest);
    }
    private void getExpertQuestionNotification(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext()); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();


                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("this_question_is_locked").equals("true")){

                                Bundle b1 = new Bundle();
                                b1.putInt("question_id",Integer.parseInt(jsonObject.getString("redirected_link_question_id")));
                                Intent jump = new Intent(Question_view.this,Answer_submission.class);
                                jump.putExtras(b1);
                                startActivity(jump);
                            }else  if(jsonObject.getString("this_question_is_locked").equals("false")){
                                if(jsonObject.getString("notification_class").equals("hidden")){

                                    already_locked_layout.setVisibility(View.GONE);

                                }else  if(jsonObject.getString("notification_class").equals("")){
                                    already_locked_layout.setVisibility(View.VISIBLE);
                                    notification_text.setText(jsonObject.getString("text"));
                                    if(jsonObject.getString("redirected_link_question_id").equals("question_listing")){
                                        already_locked_answer_now.setText("Kindly try another");

                                    }else if(Pattern.compile( "[0-9]" ).matcher( jsonObject.getString("redirected_link_question_id")).find()){

                                        already_locked_answer_now.setText("Answer Now");
                                        setIf_already_locked_question(jsonObject.getString("redirected_link_question_id"));

                                    }else  if(jsonObject.getString("redirected_link_question_id").equals("false")){

                                        already_locked_answer_now.setVisibility(View.GONE);
                                    }

                                    lock_and_answer.setVisibility(View.GONE);

                                }
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
                params.put("get_expert_locked_notification", "true");
                params.put("customers_id", customers_id);
                params.put("question_id", Integer.toString(getCurrent_question_id()));

                return params;
            }
        };
        queue.add(postRequest);
    }
    @Override
    public void onBackPressed() {

        CheckIfAlreadySignInClass checkIfAlreadySignInClass = new CheckIfAlreadySignInClass(Question_view.this,HttpUrl,dashboard_preference);
        checkIfAlreadySignInClass.checkIfAlreadySignedIn(dashboard_preference);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh_dashboard, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.refresh_dashboard:

                finish();
                startActivity(getIntent());


                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


}
