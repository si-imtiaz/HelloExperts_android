package com.HelloExperts.HelloExpert;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
//import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.HelloExperts.HelloExpert.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import cn.iwgang.countdownview.CountdownView;
import in.nashapp.androidsummernote.Summernote;


public class Answer_submission extends AppCompatActivity  implements View.OnClickListener{
    SwipeRefreshLayout refresh_answer_submission;
    TextView image_loading,text_percentage;
    int i;
    double uploadingTime;
    RelativeLayout image_uploading;
    double upStreamSpeed,downStreamSpeed;
    private static final boolean SHOW_SPEED_IN_BITS = false;
    private TrafficSpeedMeasurer mTrafficSpeedMeasurer;
    CountdownView question_submission_timer,skip_question_timer;
    Summernote answer_submission_editor;
    Button skip_question,skip_cancel,skip_confirm,review_and_submit_answer;
    RelativeLayout question_section,skip_question_section,answer_section,question_no_available_section,skip_question_timeout_section;
    ImageView skip_question_close_btn,skip_question_timeout_close_btn;
    Button question_close_btn;
    RadioGroup all_radio_buttons;
    EditText other_reason;
    TextView bold_select,note_bold_select,error_select_option_skip,hiddenQuestion_heading,skip_question_text,try_another_question;
    String HttpUrl;
    String customers_tokken;
    String customers_id;

    public successfully_uploaded_dialog access_answer;
    String expert_temp_answer;
    String file_path,upload_path;
    LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
    SharedPreferences answer_submission_shared;
    SharedPreferences.Editor answer_submission_shared_Ed;
    WebView hiddenQuestion;
    private String final_html_answer;
    private LinearLayout main_loader_answer_submission_bg;
    private AVLoadingIndicatorView recent_loader;
    private int customer_id;
    Dialog error_msg;
    RelativeLayout rootView;
    LinearLayout review_and_skip_section;
    ArrayList<String> files = new ArrayList<>();
    private ProgressBar pDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_submission);

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
        assignLocalVariables();
        answer_submission_editor.setText("");
        // call on click functions
        allOnClickFunctions();
        authenticateUserProgressDialogue();
        getQuestionLockTimeAndAnswerIfInEdit(Integer.parseInt(customers_id),getIntent().getExtras().getInt("question_id"));
        getSkipOptions();

      this.answer_submission_editor.setRequestCodeforFilepicker(5);

        this.answer_submission_editor.requestFocus();
        rootView = (RelativeLayout)findViewById(R.id.rootView);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new
            ViewTreeObserver.OnGlobalLayoutListener() {
             @Override
             public void onGlobalLayout() {
             Rect r = new Rect();
             rootView.getWindowVisibleDisplayFrame(r);
            int screenHeight = rootView.getRootView().getHeight();
            int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) {
                    review_and_skip_section.setVisibility(View.GONE);

                } else {

                    review_and_skip_section.setVisibility(View.VISIBLE);
                }

             }
        });


        this.review_and_submit_answer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Checkconnectivity checkconnectivity = new Checkconnectivity();
                if(checkconnectivity.hasInternetConnection(getApplicationContext())){
                    check_if_string_empty(answer_submission_editor.getText());
                }else{
                    Toast.makeText(Answer_submission.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }



            }
        });

        this.grantExternalPermission();






    }

    public String getExpert_temp_answer() {
        return expert_temp_answer;
    }

    public void setExpert_temp_answer(String expert_temp_answer) {
        this.expert_temp_answer = expert_temp_answer;
    }
    public void check_if_string_empty(final String answer_html){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext()); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(!jsonObject.getBoolean("isEmpty")){
                                setFinal_html_answer(answer_submission_editor.getText());
                                authenticateUserProgressDialogue();
                                reviewAndSubmitAnswer(customers_id,Integer.toString(getIntent().getExtras().getInt("question_id")),
                                        getFinal_html_answer());
                            }else{
                                error_msg = new Dialog(Answer_submission.this);
                                error_msg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                error_msg.setContentView(R.layout.error_alert);
                                TextView btn_ok=(TextView)error_msg.findViewById(R.id.btn_ok);
                                TextView message=(TextView)error_msg.findViewById(R.id.message);
                                message.setText("Answer cannot be empty");
                                error_msg.show();
                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        error_msg.dismiss();
                                    }
                                });
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
                params.put("check_empty_string", "true");
                params.put("answer_html", answer_html);

                return params;
            }
        };
        queue.add(postRequest);
    }



    public String getFinal_html_answer() {
        return final_html_answer;
    }

    public void hideQuestionView(View view){

        question_section.setVisibility(View.GONE);
        question_close_btn.setVisibility(View.GONE);
        hiddenQuestion_heading.setVisibility(View.GONE);
        answer_section.setVisibility(View.VISIBLE);
     //  refresh_answer_submission.setVisibility(View.VISIBLE);

    }

    public void setFinal_html_answer(String final_html_answer) {
        this.final_html_answer = final_html_answer;
    }
    public void reviewAndSubmitAnswer(final String expert_id,final String question_id,final String answer_html){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext()); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("submitted").equals("true")){
                                Bundle b = new Bundle();
                                b.putInt("question_id",Integer.parseInt(question_id));
                                Intent edit_answer_intent  = new Intent(Answer_submission.this,Final_answer_preview.class);
                                edit_answer_intent.putExtras(b);
                                startActivity(edit_answer_intent);
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
                params.put("review_and_submit_answer", "true");
                params.put("question_id", question_id);
                params.put("customer_id", expert_id);
                params.put("answer_html", answer_html);



                return params;
            }
        };
        queue.add(postRequest);
    }
    public void grantExternalPermission(){

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        5);
            }
        }
    }

    private ITrafficSpeedListener mStreamSpeedListener = new ITrafficSpeedListener() {

        @Override
        public void onTrafficSpeedMeasured(final double upStream, final double downStream) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                        upStreamSpeed = Utils.parseSpeed(upStream, SHOW_SPEED_IN_BITS);
                         downStreamSpeed = Utils.parseSpeed(downStream, SHOW_SPEED_IN_BITS);

                   // mTextView.setText("Up Stream Speed: " + upStreamSpeed + "\n" + "Down Stream Speed: " + downStreamSpeed);
                }
            });
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTrafficSpeedMeasurer.stopMeasuring();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTrafficSpeedMeasurer.removeListener(mStreamSpeedListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTrafficSpeedMeasurer.registerListener(mStreamSpeedListener);
    }



        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        answer_submission_editor.onActivityResult(requestCode, resultCode, intent);

            if(requestCode == 5 && resultCode == RESULT_OK){
                Checkconnectivity checkconnectivity = new Checkconnectivity();
                if(checkconnectivity.hasInternetConnection(getApplicationContext())){

                    this.review_and_submit_answer.setEnabled(false);

                    setFinal_html_answer(answer_submission_editor.getText());

                    Uri Uripath = intent.getData();


                    RealPathUtil realPathUtil = new RealPathUtil();

                    String  path = realPathUtil.getRealPath(getApplicationContext(),Uripath);


                    File f = new File(path);

                    long fileSizeInBytes = f.length();
                    //      Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                    long fileSizeInKB = fileSizeInBytes / 1024;
                    //  Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                    long fileSizeInMB = fileSizeInKB / 1024;

                    if (fileSizeInMB >= 10) {
                        this.review_and_submit_answer.setEnabled(true);
                        setFinal_html_answer(getExpert_temp_answer());

                        answer_submission_editor.setText(getFinal_html_answer());

                        Toast.makeText(getApplicationContext(), "Sorry can't upload more than 10 mb file ", Toast.LENGTH_LONG).show();
                    } else {
                        // uploadingTime = (fileSizeInMB*8/upStreamSpeed)/60;
                        //   authenticateUserProgressDialogue();
                        file_path = f.getAbsolutePath();
                        if(fileSizeInMB >= 2){
                            file_path = resize(file_path,f.getName());
                        }
                        files.add(file_path);
                        if(files.size()>0){
                            uploadFiles();
                        }
                        //   new Answer_submission.UploadFileAsync().execute("");
                    }
                }else{
                    hideProgressBar();
                    setFinal_html_answer(getExpert_temp_answer());
                    answer_submission_editor.setText(getFinal_html_answer());
                    Toast.makeText(Answer_submission.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 33: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void allOnClickFunctions(){

//        this.refresh_answer_submission.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                finish();
//                startActivity(getIntent());
//            }
//        });

        this.question_submission_timer.setOnCountdownIntervalListener(1000, new CountdownView.OnCountdownIntervalListener() {
            @Override
            public void onInterval(CountdownView cv, long remainTime) {
                if(remainTime < 1000){
                    skip_question_section.setVisibility(View.GONE);
                    question_section.setVisibility(View.GONE);
                    question_close_btn.setVisibility(View.GONE);
                    hiddenQuestion_heading.setVisibility(View.GONE);

                    answer_section.setVisibility(View.GONE);
                  //  refresh_answer_submission.setVisibility(View.GONE);
                    skip_question_timeout_section.setVisibility(View.GONE);

                    Animation animation = AnimationUtils.loadAnimation(Answer_submission.this, R.anim.slide_question_bar);
                    question_no_available_section.startAnimation(animation);
                    question_no_available_section.setVisibility(View.VISIBLE);
                    question_no_available_section.setAlpha(0.75f);

                    widthDrawQuestion("0",customers_id,Integer.toString(getIntent().getExtras().getInt("question_id")),false);

                }

            }
        });
        this.skip_question_timer.setOnCountdownIntervalListener(1000, new CountdownView.OnCountdownIntervalListener() {
            @Override
            public void onInterval(CountdownView cv, long remainTime) {
                if(remainTime < 1000){

                    skip_question.setText("Can't answer");
                    skip_question_text.setVisibility(View.GONE);
                    skip_question_section.setVisibility(View.GONE);
                    question_section.setVisibility(View.GONE);
                    question_close_btn.setVisibility(View.GONE);
                    hiddenQuestion_heading.setVisibility(View.GONE);

                    answer_section.setVisibility(View.GONE);
                  //  refresh_answer_submission.setVisibility(View.GONE);
                    question_no_available_section.setVisibility(View.GONE);

                    Animation animation = AnimationUtils.loadAnimation(Answer_submission.this, R.anim.slide_question_bar);
                    skip_question_timeout_section.startAnimation(animation);
                    skip_question_timeout_section.setVisibility(View.VISIBLE);
                    skip_question_timeout_section.setAlpha(0.75f);

                }

            }
        });

        this.skip_question.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Checkconnectivity checkconnectivity = new Checkconnectivity();
                if(checkconnectivity.hasInternetConnection(getApplicationContext())){

                    answer_section.setVisibility(View.GONE);
                   // refresh_answer_submission.setVisibility(View.GONE);
                    Animation animation = AnimationUtils.loadAnimation(Answer_submission.this, R.anim.slide_question_bar);
                    skip_question_section.startAnimation(animation);
                    skip_question_section.setVisibility(View.VISIBLE);
                    skip_question_section.setAlpha(0.75f);

                }else{
                    Toast.makeText(Answer_submission.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });

        this.skip_question_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                skip_question_section.setVisibility(View.GONE);
                answer_section.setVisibility(View.VISIBLE);
           //     refresh_answer_submission.setVisibility(View.VISIBLE);

            }
        });
        this.skip_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skip_question_section.setVisibility(View.GONE);
                answer_section.setVisibility(View.VISIBLE);
          //      refresh_answer_submission.setVisibility(View.VISIBLE);
            }
        });
        this.skip_question_timeout_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skip_question_timeout_section.setVisibility(View.GONE);
                answer_section.setVisibility(View.VISIBLE);
          //      refresh_answer_submission.setVisibility(View.VISIBLE);
            }
        });

        this.skip_confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Checkconnectivity checkconnectivity = new Checkconnectivity();
                if(checkconnectivity.hasInternetConnection(getApplicationContext())){
                    int selectedRadioButtonID = all_radio_buttons.getCheckedRadioButtonId();
                    if(selectedRadioButtonID == 0){
                        if(other_reason.getText().toString().equals("")){
                            other_reason.setError("At least enter the reason");
                        }else{
                            error_select_option_skip.setVisibility(View.GONE);
                            authenticateUserProgressDialogue();
                            widthDrawQuestion(other_reason.getText().toString(),customers_id,Integer.toString(getIntent().getExtras().getInt("question_id")),true);

                        }
                    }else if(selectedRadioButtonID > 0){
                        RadioButton radioButton = (RadioButton) findViewById(selectedRadioButtonID);

                        authenticateUserProgressDialogue();
                        widthDrawQuestion(Integer.toString(selectedRadioButtonID),customers_id,Integer.toString(getIntent().getExtras().getInt("question_id")),true);
                    }else{
                        error_select_option_skip.setVisibility(View.VISIBLE);

                    }
                }else{
                    Toast.makeText(Answer_submission.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        this.try_another_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp=getSharedPreferences("Login", 0);
                SharedPreferences.Editor Ed=sp.edit();
                Ed.putInt("navigation_checked_item_id",R.id.nav_all_questions);
                Ed.commit();

                CheckIfAlreadySignInClass checkIfAlreadySignInClass = new CheckIfAlreadySignInClass(Answer_submission.this,HttpUrl,answer_submission_shared);
                checkIfAlreadySignInClass.checkIfAlreadySignedIn(answer_submission_shared);
            }
        });

    }


    @Override
    public void onBackPressed() {
        if (this.question_section.getVisibility() == View.VISIBLE) {
            long remainTime = this.question_submission_timer.getRemainTime();
            if(remainTime < 1000){

                SharedPreferences sp=getSharedPreferences("Login", 0);
                SharedPreferences.Editor Ed=sp.edit();
                Ed.putInt("navigation_checked_item_id",R.id.nav_all_questions);
                Ed.commit();

                CheckIfAlreadySignInClass checkIfAlreadySignInClass = new CheckIfAlreadySignInClass(Answer_submission.this,HttpUrl,answer_submission_shared);
                checkIfAlreadySignInClass.checkIfAlreadySignedIn(answer_submission_shared);
            }else{
                question_section.setVisibility(View.GONE);
                question_close_btn.setVisibility(View.GONE);
                hiddenQuestion_heading.setVisibility(View.GONE);

                answer_section.setVisibility(View.VISIBLE);
          //      refresh_answer_submission.setVisibility(View.VISIBLE);
            }


        }else if (this.skip_question_section.getVisibility() == View.VISIBLE) {

            skip_question_section.setVisibility(View.GONE);
            answer_section.setVisibility(View.VISIBLE);
         //   refresh_answer_submission.setVisibility(View.VISIBLE);
        }else if(this.skip_question_timeout_section.getVisibility() == View.VISIBLE){
            skip_question_timeout_section.setVisibility(View.GONE);
            answer_section.setVisibility(View.VISIBLE);
        //    refresh_answer_submission.setVisibility(View.VISIBLE);
        }else if(this.question_no_available_section.getVisibility() == View.VISIBLE){
            CheckIfAlreadySignInClass checkIfAlreadySignInClass = new CheckIfAlreadySignInClass(Answer_submission.this,HttpUrl,answer_submission_shared);
            checkIfAlreadySignInClass.checkIfAlreadySignedIn(answer_submission_shared);
        }else {
            CheckIfAlreadySignInClass checkIfAlreadySignInClass = new CheckIfAlreadySignInClass(Answer_submission.this,HttpUrl,answer_submission_shared);
            checkIfAlreadySignInClass.checkIfAlreadySignedIn(answer_submission_shared);
        }


    }
    public void addRadioButtons(int number) {
        this.all_radio_buttons.setOrientation(LinearLayout.VERTICAL);

        Object[] keys = map.keySet().toArray();

        for (int i = 0; i < number; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(Integer.parseInt(keys[i].toString()));
            radioButton.setText(map.get(keys[i]));
            radioButton.setOnClickListener(this);
            radioButton.setTypeface(Typeface.SANS_SERIF);
            radioButton.setTextColor(Color.parseColor("#414042"));
            radioButton.setTextSize(15);
            all_radio_buttons.addView(radioButton);
        }
        RadioButton otherRadioButton = new RadioButton(this);
        otherRadioButton.setId(0);
        otherRadioButton.setText("Other");
        otherRadioButton.setTextColor(Color.parseColor("#414042"));
        otherRadioButton.setOnClickListener(this);
        otherRadioButton.setTypeface(Typeface.SANS_SERIF);
        otherRadioButton.setTextSize(16);
        all_radio_buttons.addView(otherRadioButton);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == 0){
        this.other_reason.setVisibility(View.VISIBLE);
        }else{
            if(this.other_reason.getVisibility() == View.VISIBLE){
                this.other_reason.setVisibility(View.GONE);

            }
            this.error_select_option_skip.setVisibility(View.GONE);
        }
    }
    public void assignLocalVariables() {
        this.image_uploading = (RelativeLayout)findViewById(R.id.image_uploading);
        this.text_percentage = (TextView)findViewById(R.id.text_percentage);
        this.expert_temp_answer = "";
        this.pDialog = (ProgressBar)findViewById(R.id.progressBar);
        this.pDialog.setClickable(false);
        this.image_uploading.setClickable(false);
        access_answer = new successfully_uploaded_dialog(Answer_submission.this);
        access_answer.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        access_answer.setCanceledOnTouchOutside(false);

      //  this.refresh_answer_submission = (SwipeRefreshLayout) findViewById(R.id.refresh_answer_submission);
        this.review_and_skip_section = (LinearLayout) findViewById(R.id.review_and_skip_section);
        this.answer_submission_editor = (Summernote) findViewById(R.id.answer_submission_editor);
        this.question_submission_timer = (CountdownView) findViewById(R.id.answer_submission_time);
        this.skip_question_timer = (CountdownView) findViewById(R.id.skip_question_timer);

        this.skip_question = (Button) findViewById(R.id.skip_question);
        this.skip_cancel = (Button) findViewById(R.id.skip_cancel);
        this.skip_confirm = (Button) findViewById(R.id.skip_confirm);
        this.review_and_submit_answer = (Button) findViewById(R.id.review_and_submit_answer);
        this.answer_section = (RelativeLayout) findViewById(R.id.answer_section);
        this.question_section = (RelativeLayout) findViewById(R.id.question_section);
        this.skip_question_section = (RelativeLayout) findViewById(R.id.skip_question_section);
        this.question_no_available_section = (RelativeLayout) findViewById(R.id.question_no_available_section);
        this.skip_question_timeout_section = (RelativeLayout) findViewById(R.id.skip_question_timeout_section);
        this.skip_question_timeout_close_btn = (ImageView) findViewById(R.id.skip_question_timeout_close_btn);
        this.question_close_btn = (Button) findViewById(R.id.question_close_btn);
        this.skip_question_close_btn = (ImageView) findViewById(R.id.skip_question_close_btn);
        this.all_radio_buttons = (RadioGroup) findViewById(R.id.all_radio_buttons);
        this.other_reason = (EditText) findViewById(R.id.other_reason);
        this.bold_select = (TextView) findViewById(R.id.bold_select);
        this.bold_select.setTypeface(null, Typeface.BOLD);
        this.note_bold_select = (TextView) findViewById(R.id.note_bold_select);
        this.error_select_option_skip = (TextView) findViewById(R.id.error_select_option_skip);
        this.hiddenQuestion_heading = (TextView) findViewById(R.id.hiddenQuestion_heading);
        this.try_another_question = (TextView) findViewById(R.id.try_another_question);

        this.try_another_question.setPaintFlags(this.try_another_question.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        this.note_bold_select.setTypeface(null, Typeface.BOLD);
        this.HttpUrl = getResources().getString(R.string.about_yourself_api_url);
        this.answer_submission_shared = getApplicationContext().getSharedPreferences("Login", 0);
        int temp_id = this.answer_submission_shared.getInt("customers_id", 0);
        customers_tokken = this.answer_submission_shared.getString("customers_tokken", null);
        customers_id = Integer.toString(temp_id);
        this.answer_submission_shared_Ed = this.answer_submission_shared.edit();
        this.hiddenQuestion = (WebView) findViewById(R.id.hiddenQuestion);
        this.hiddenQuestion.getSettings().setBuiltInZoomControls(true);
        this.hiddenQuestion.setVerticalScrollBarEnabled(true);
        this.hiddenQuestion.setHorizontalScrollBarEnabled(true);

        hiddenQuestion.setVerticalScrollBarEnabled(true);
        hiddenQuestion.setHorizontalScrollBarEnabled(true);

        main_loader_answer_submission_bg = (LinearLayout) findViewById(R.id.main_loader_answer_submission_bg);
        recent_loader = (AVLoadingIndicatorView) findViewById(R.id.recent_loader);
        recent_loader.show();
        this.skip_question_text = (TextView) findViewById(R.id.skip_question_text);

        mTrafficSpeedMeasurer = new TrafficSpeedMeasurer(TrafficSpeedMeasurer.TrafficType.ALL);
        mTrafficSpeedMeasurer.startMeasuring();

        this.image_loading = (TextView) findViewById(R.id.image_loading);
        i = 0;

        this.answer_submission_editor.loadUrl("file:///android_asset/summernote_local.html");

    }
    private void authenticateUserProgressDialogue(){

        this.main_loader_answer_submission_bg.setVisibility(View.VISIBLE);

    }

    private void hideProgressBar(){

        this.main_loader_answer_submission_bg.setVisibility(View.GONE);
    }

    private void getSkipOptions(){
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext()); // this = context
            StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            hideProgressBar();
                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                for(int i =0 ; i < jsonArray.length() ; i++){
                                    map.put(jsonArray.getJSONObject(i).getString("skip_reason_id"),jsonArray.getJSONObject(i).getString("skip_reason_text"));

                                }

                               addRadioButtons(map.size());

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
                    params.put("get_question_skip_options", "true");


                    return params;
                }
            };
            queue.add(postRequest);

    }
    private void getQuestionLockTimeAndAnswerIfInEdit(final int customer_id,final int question_id) {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext()); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getInt("differences_question_time_out") <= 0){



                                skip_question_section.setVisibility(View.GONE);
                                question_section.setVisibility(View.GONE);
                                question_close_btn.setVisibility(View.GONE);
                                hiddenQuestion_heading.setVisibility(View.GONE);

                                answer_section.setVisibility(View.GONE);
                              //  refresh_answer_submission.setVisibility(View.GONE);
                                skip_question_timeout_section.setVisibility(View.GONE);
                                question_no_available_section.setVisibility(View.VISIBLE);
                                answer_submission_shared_Ed.putString("skip_message_displayed","false");
                                answer_submission_shared_Ed.commit(); // commit changes
                                widthDrawQuestion("0",Integer.toString(customer_id),Integer.toString(question_id),false);

                            }else{

                                question_submission_timer.start(jsonObject.getInt("differences_question_time_out"));
                                if(jsonObject.getInt("difference_question_skip_time_out") <= 0){


                                    if(!answer_submission_shared.contains("skip_message_displayed")){

                                        answer_submission_shared_Ed.putString("skip_message_displayed","true");
                                        answer_submission_shared_Ed.commit(); // commit changes


                                        skip_question_section.setVisibility(View.GONE);
                                        question_section.setVisibility(View.GONE);
                                        question_close_btn.setVisibility(View.GONE);
                                        hiddenQuestion_heading.setVisibility(View.GONE);

                                        answer_section.setVisibility(View.GONE);
                                  //      refresh_answer_submission.setVisibility(View.GONE);
                                        question_no_available_section.setVisibility(View.GONE);
                                        skip_question_timeout_section.setVisibility(View.VISIBLE);

                                    }

                                    skip_question.setText("Can't answer");
                                    skip_question_timer.setVisibility(View.GONE);
                                    skip_question_text.setVisibility(View.GONE);

                                }else{

                                    skip_question_timer.start(jsonObject.getInt("difference_question_skip_time_out"));
                                }
                            }


                            if(jsonObject.getString("expert_answer_edit_state").equals("true")){

                                setExpert_temp_answer(jsonObject.getString("expert_temp_answer"));

                                answer_submission_editor.setText(jsonObject.getString("expert_temp_answer"));
                            }
                            if(jsonObject.getString("expert_question_flag").equals("true")){


                                String question_description_detail =jsonObject.getString("expert_question_description");
                                hiddenQuestion.loadDataWithBaseURL("", question_description_detail,  "text/html", "UTF-8", "");
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
                params.put("get_all_answer_submission_data", "true");
                params.put("customer_id", Integer.toString(customer_id));
                params.put("question_id", Integer.toString(question_id));


                return params;
            }
        };
        queue.add(postRequest);
    }

    private void widthDrawQuestion(final String withdrawReason,final String customer_id,final String question_id,final Boolean suspended_flag) {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext()); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("suspended").equals("true") &&
                                suspended_flag){

                                CheckIfAlreadySignInClass checkIfAlreadySignInClass = new CheckIfAlreadySignInClass(Answer_submission.this,HttpUrl,answer_submission_shared);
                                checkIfAlreadySignInClass.checkIfAlreadySignedIn(answer_submission_shared);
                            }else if(jsonObject.getString("suspended").equals("true") &&
                                    !suspended_flag){
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
                        Toast.makeText(getApplicationContext(),"Error2 : Something went wrong. Please try again",Toast.LENGTH_LONG).show();
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


//    private class UploadFileAsync extends AsyncTask<String, Integer, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            upload_docs(file_path,"image_param");
//
//
//
//            return "Executed";
//        }
//        protected void upload_docs(String file,String document_request_type){
//            try {
//                String sourceFileUri = file;
//
//                HttpURLConnection conn = null;
//                DataOutputStream dos = null;
//                String lineEnd = "\r\n";
//                String twoHyphens = "--";
//                String boundary = "*****";
//                int bytesRead, bytesAvailable, bufferSize;
//                byte[] buffer;
//                int maxBufferSize = 5 * 1024 * 1024;
//                File sourceFile = new File(sourceFileUri);
//
//                if (sourceFile.isFile()) {
//
//                    try {
//                        String upLoadServerUri = getResources().getString(R.string.about_yourself_api_url);
//
//                        // open a URL connection to the Servlet
//                        FileInputStream fileInputStream = new FileInputStream(
//                                sourceFile);
//                        URL url = new URL(upLoadServerUri);
//
//                        // Open a HTTP connection to the URL
//                        conn = (HttpURLConnection) url.openConnection();
//                        conn.setDoInput(true); // Allow Inputs
//                        conn.setDoOutput(true); // Allow Outputs
//                        conn.setUseCaches(false); // Don't use a Cached Copy
//                        conn.setRequestMethod("POST");
//                        conn.setRequestProperty("Connection", "Keep-Alive");
//                        conn.setRequestProperty("ENCTYPE",
//                                "multipart/form-data");
//                        conn.setRequestProperty("Content-Type",
//                                "multipart/form-data;boundary=" + boundary);
//                        conn.setRequestProperty(document_request_type, sourceFileUri);
//                        dos = new DataOutputStream(conn.getOutputStream());
//
//                        dos.writeBytes(twoHyphens + boundary + lineEnd);
//                        dos.writeBytes("Content-Disposition: form-data; name=\""+document_request_type+"\";filename=\""
//                                + sourceFileUri + "\"" + lineEnd);
//                        dos.writeBytes(lineEnd);
//
//                        // create a buffer of maximum size
//                        bytesAvailable = fileInputStream.available();
//
//                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                        buffer = new byte[bufferSize];
//
//                        // read file and write it into form...
//                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
//                        while (bytesRead > 0) {
//
//                            dos.write(buffer, 0, bufferSize);
//                            bytesAvailable = fileInputStream.available();
//                            bufferSize = Math
//                                    .min(bytesAvailable, maxBufferSize);
//                            bytesRead = fileInputStream.read(buffer, 0,
//                                    bufferSize);
//
//                        }
//
//                        // send multipart form data necesssary after file
//                        // data...
//                        dos.writeBytes(lineEnd);
//                        dos.writeBytes(twoHyphens + boundary + twoHyphens
//                                + lineEnd);
//
//
//                        // Responses from the server (code and message)
//                        int serverResponseCode = conn.getResponseCode();
//                        upload_path = conn.getHeaderField("upload_path");
//
//
//
//                        if (serverResponseCode == 200) {
//
//                            // messageText.setText(msg);
//                            //Toast.makeText(ctx, "File Upload Complete.",
//                            //      Toast.LENGTH_SHORT).show();
//
//                            // recursiveDelete(mDirectory1);
//
//                        }
//                        //
//
//                        // close the streams //
//                        fileInputStream.close();
//                        dos.flush();
//                        dos.close();
//                        hideProgressBar();
//
//                    } catch (Exception e) {
//
//
//                        e.printStackTrace();
//
//                    }
//
//
//                } // End else block
//
//
//            } catch (Exception ex) {
//
//
//                ex.printStackTrace();
//            }
//        }
//
//
//        @Override
//        protected void onPostExecute(String result) {
//
//            Checkconnectivity checkconnectivity = new Checkconnectivity();
//            if(checkconnectivity.hasInternetConnection(getApplicationContext())){
//                upload_path = upload_path.replace("175px","350px");
//
//                setFinal_html_answer(getFinal_html_answer()+" "+upload_path);
//                answer_submission_editor.setText(getFinal_html_answer());
//                review_and_submit_answer.setEnabled(true);
//
//            }else{
//                hideProgressBar();
//
//                setFinal_html_answer(getExpert_temp_answer());
//                answer_submission_editor.setText(getFinal_html_answer());
//                Toast.makeText(Answer_submission.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
//
//
//            }
//
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);
//            Toast.makeText(Answer_submission.this, "imitaz"+values[0], Toast.LENGTH_SHORT).show();
//
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.see_question, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.see_question_fixed:

                answer_section.setVisibility(View.GONE);
             //   refresh_answer_submission.setVisibility(View.GONE);
                skip_question_section.setVisibility(View.GONE);

                question_no_available_section.setVisibility(View.GONE);
                skip_question_timeout_section.setVisibility(View.GONE);

                Animation animation = AnimationUtils.loadAnimation(Answer_submission.this, R.anim.slide_question_bar);
                question_section.startAnimation(animation);
                question_section.setVisibility(View.VISIBLE);
                question_close_btn.setVisibility(View.VISIBLE);
                hiddenQuestion_heading.setVisibility(View.VISIBLE);
                question_section.setAlpha(0.75f);

                // User chose the "Settings" item, show the app settings UI...

                return true;
            case R.id.refresh_activity:

                finish();
                startActivity(getIntent());


                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public String resize(String file_path,String file_name){

        Bitmap b= BitmapFactory.decodeFile(file_path);
        Bitmap out = Bitmap.createScaledBitmap(b, 700, 1000, false);

        ExifInterface ei = null;
        try {
            ei = new ExifInterface(file_path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                out = rotateImage(out, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                out = rotateImage(out, 180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                out = rotateImage(out, 270);
                break;
            default:
                break;
        }



        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File file = new File(directory, "file_name_resize" + ".jpg");

        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            out.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            b.recycle();
            out.recycle();
        } catch (Exception e) {}

        return file.getAbsolutePath();
    }


private static Bitmap rotateImage(Bitmap img,int degree) {


    Matrix matrix = new Matrix();
    matrix.postRotate(degree);
    Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
    return rotatedImg;
}
/* Retrofit upload Files */
    public void uploadFiles(){

    File[] filesToUpload = new File[files.size()];
    for(int i=0; i< files.size(); i++){
        filesToUpload[i] = new File(files.get(i));

    }
    showProgress("Uploading media ...");
    FileUploader fileUploader = new FileUploader(getApplicationContext());
    fileUploader.uploadFiles("/index.php?main_page=expert_post_requests", "image_param", filesToUpload, new FileUploader.FileUploaderCallback() {
        @Override
        public void onError() {
            if (pDialog.getVisibility() == View.VISIBLE) {
                pDialog.setVisibility(View.GONE);
                text_percentage.setVisibility(View.GONE);
                image_uploading.setVisibility(View.GONE);
            }
        }

        @Override
        public void onFinish(String[] responses) {

            String str = "";
            for(int i=0; i< responses.length; i++){
                str = responses[i];
                
            }

            if(!str.equals("")){
                Checkconnectivity checkconnectivity = new Checkconnectivity();
                if(checkconnectivity.hasInternetConnection(getApplicationContext())){
                    hideProgress();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(str);
                        upload_path = jsonObject.getString("upload_path");
                        upload_path = upload_path.replace("175px","350px");
                         setFinal_html_answer(getFinal_html_answer()+" "+upload_path);
                          answer_submission_editor.setText(getFinal_html_answer());

                           review_and_submit_answer.setEnabled(true);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }else{
                    if (pDialog.getVisibility() == View.VISIBLE){
                        pDialog.setVisibility(View.GONE);
                        text_percentage.setVisibility(View.GONE);
                        image_uploading.setVisibility(View.GONE);
                    }

                    setFinal_html_answer(getExpert_temp_answer());
                    answer_submission_editor.setText(getFinal_html_answer());
                    Toast.makeText(Answer_submission.this, "No Internet Connection", Toast.LENGTH_SHORT).show();


                }
            }else{
                if (pDialog.getVisibility() == View.VISIBLE){
                    pDialog.setVisibility(View.GONE);
                    text_percentage.setVisibility(View.GONE);
                    image_uploading.setVisibility(View.GONE);
                }
            //    Toast.makeText(Answer_submission.this, "Something went wrong. Please try again later.", Toast.LENGTH_SHORT).show();
                setFinal_html_answer(getFinal_html_answer());
                answer_submission_editor.setText(getFinal_html_answer());
            }

        }

        @Override
        public void onProgressUpdate(int currentpercent, int totalpercent, int filenumber) {
            updateProgress(totalpercent,"Uploading image ","");
        }
    });
}

    public void updateProgress(int val, String title, String msg){

        pDialog.setProgress(val);
        text_percentage.setText(Integer.toString(val)+"%");
    }

    public void showProgress(String str){
        try{

            if (pDialog.getVisibility() == View.VISIBLE){
                pDialog.setVisibility(View.GONE);
                text_percentage.setVisibility(View.GONE);
                image_uploading.setVisibility(View.GONE);
            }

            pDialog.setVisibility(View.VISIBLE);
            text_percentage.setVisibility(View.VISIBLE);
            image_uploading.setVisibility(View.VISIBLE);


        }catch (Exception e){

        }
    }

    public void hideProgress(){
        try{
            if (pDialog.getVisibility() == View.VISIBLE){
                pDialog.setVisibility(View.GONE);
                text_percentage.setVisibility(View.GONE);
                image_uploading.setVisibility(View.GONE);
                access_answer.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        access_answer.dismiss();
                    }
                }, 2000);
            }

        }catch (Exception e){

        }

    }
}
