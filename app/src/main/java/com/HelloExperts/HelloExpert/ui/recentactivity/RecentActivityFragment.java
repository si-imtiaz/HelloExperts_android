package com.HelloExperts.HelloExpert.ui.recentactivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentTransaction;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.HelloExperts.HelloExpert.HttpTrustManager;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.HelloExperts.HelloExpert.CheckIfAlreadySignInClass;
import com.HelloExperts.HelloExpert.MySingleton;
import com.HelloExperts.HelloExpert.R;
import com.HelloExperts.HelloExpert.Recent_activity_adapter;
import com.HelloExperts.HelloExpert.Recent_activity_data;
import com.HelloExperts.HelloExpert.dashboard;
import com.HelloExperts.HelloExpert.login;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class RecentActivityFragment extends Fragment {

    private RecentActivityViewModel ViewModel;
    private SwipeRefreshLayout refresh_new_questions;
    private  String HttpUrl;
    private View root;
    private String customers_id;
    private String customers_tokken;
    private Button try_again;
    private TextView no_internet_text;
    private ImageView no_internet_symbol;
    private RecyclerView recyclerView;
    SharedPreferences dashboard_preference;
    RecyclerView.Adapter adapter;
    LinearLayout have_a_look,main_loader_recent_activity_bg;
    TextView no_recent_activity,check_new_posted;
    AVLoadingIndicatorView recent_loader;


    /* feedback_dialog */
    Boolean star_rating_set;
    Boolean display_feed_flag;
    Boolean dont_show_textbox;
    Dialog feed_back;
    Boolean check_feed_back_flag;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ViewModel =
                ViewModelProviders.of(this).get(RecentActivityViewModel.class);
         root = inflater.inflate(R.layout.fragment_recent_activity, container, false);

        SharedPreferences sp=this.getActivity().getSharedPreferences("Login", 0);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.putInt("navigation_checked_item_id",R.id.nav_Recent_activity);
        Ed.commit();



        //HttpTrustManager.allowAllSSL(); // allow all SSL
        try {
            HttpsURLConnection.setDefaultSSLSocketFactory(HttpTrustManager.getSocketFactory(getContext().getApplicationContext()));
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
        authenticateUserProgressDialogue();

        sendDataForAuthentication();

        if(check_feed_back_flag){
            getFeedBackDetails(customers_id);
        }


        try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ftr = getFragmentManager().beginTransaction();
                ftr.detach(RecentActivityFragment.this).attach(RecentActivityFragment.this).commit();
            }
        });
        refresh_new_questions.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                authenticateUserProgressDialogue();
                sendDataForAuthentication();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh_new_questions.setRefreshing(false);
                    }
                },3000);
            }
        });
        have_a_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sp=getActivity().getSharedPreferences("Login", 0);
                SharedPreferences.Editor Ed=sp.edit();
                Ed.putInt("navigation_checked_item_id",R.id.nav_new_questions);
                Ed.commit();
                startActivity(new Intent(getActivity(), dashboard.class));

            }
        });


        return root;
    }
    private void assignLocalVariables(){
        this.star_rating_set = false;
        this.dont_show_textbox = false;
        this.feed_back = new Dialog(getActivity());
        this.display_feed_flag = true;
        recyclerView  = (RecyclerView)root.findViewById(R.id.recent_activity_recyclerView);
        HttpUrl = getResources().getString(R.string.about_yourself_api_url);
        try_again = (Button)root.findViewById(R.id.try_again);
        no_internet_symbol=(ImageView)root.findViewById(R.id.no_connection_symbol);
        no_internet_text = (TextView)root.findViewById(R.id.no_connection_text);
        dashboard_preference=getActivity().getSharedPreferences("Login",0);
        int temp_id = dashboard_preference.getInt("customers_id",0);
        customers_id = Integer.toString(temp_id);
        customers_tokken = dashboard_preference.getString("customers_tokken",null);
        refresh_new_questions = (SwipeRefreshLayout)root.findViewById(R.id.refresh_new_questions);
        check_feed_back_flag = dashboard_preference.getBoolean("check_feed_back_flag",false);
        have_a_look = (LinearLayout)root.findViewById(R.id.have_a_look);
        check_new_posted = (TextView)root.findViewById(R.id.check_new_posted);
        no_recent_activity = (TextView)root.findViewById(R.id.no_recent_activity);
        main_loader_recent_activity_bg = (LinearLayout)root.findViewById(R.id.main_loader_recent_activity_bg);
        recent_loader= (AVLoadingIndicatorView)root.findViewById(R.id.recent_loader);
        recent_loader.show();

    }
    private void authenticateUserProgressDialogue(){

        this.main_loader_recent_activity_bg.setVisibility(View.VISIBLE);

    }

    private void hideProgressBar(){

        this.main_loader_recent_activity_bg.setVisibility(View.GONE);
    }
    private void sendDataForAuthentication() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        authenticateUserProgressDialogue();
                        parseData(response);

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        error.printStackTrace();

                        hideProgressBar();
                        try_again.setVisibility(View.VISIBLE);
                        no_internet_symbol.setVisibility(View.VISIBLE);
                        no_internet_text.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(),"Error: Something went wrong. Please try again.",Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String,String>();

                params.put("get_recent_activities","true");
                params.put("customers_id",customers_id);
                params.put("customers_tokken",customers_tokken);
                return params;
            }

        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void parseData(String response){
        try{
            hideProgressBar();
            List<Recent_activity_data> complete_list = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(response);

            if(jsonArray.getJSONObject(0).getString("login_status").equals("false") &&
               jsonArray.getJSONObject(0).getString("data").equals("go_to_login")){
                Toast.makeText(getActivity(),"Error : You must login first to continue",Toast.LENGTH_LONG).show();
                SharedPreferences pref = getActivity().getSharedPreferences("Login", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit(); // commit changes
                getActivity().finish();
                startActivity(new Intent(getActivity(), login.class));
            }else if(jsonArray.getJSONObject(0).getString("login_status").equals("true") &&
               jsonArray.getJSONObject(0).getString("data").equals("true")){

                for (int i = 0; i < jsonArray.length(); i++) {
                    // Get current json object
                    JSONObject student = jsonArray.getJSONObject(i);

                    String question = student.getString("question");
                    String date = student.getString("date");
                    String status = student.getString("status");
                    String question_id = student.getString("question_id");
                    Recent_activity_data recent_activity_data = new Recent_activity_data(question, date, status,Integer.parseInt(question_id));
                    complete_list.add(recent_activity_data);

                    if (i + 1 == jsonArray.length()) {

                        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setHasFixedSize(true);
                        adapter = new Recent_activity_adapter(complete_list);
                        recyclerView.setAdapter(adapter);

                    }


                }
            }else{
                recyclerView.setVisibility(View.GONE);
                no_recent_activity.setVisibility(View.VISIBLE);
                have_a_look.setVisibility(View.VISIBLE);
                check_new_posted.setVisibility(View.VISIBLE);
            }




        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    public void getFeedBackDetails(final String customer_id){

        RequestQueue queue = Volley.newRequestQueue(getActivity()); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            display_feed_flag = jsonObject.getBoolean("is_feed_back_display");
                            if(display_feed_flag){
                                submit_feed_back();
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
                        Toast.makeText(getContext(),"Error : Something went wrong. Please try again",Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("get_feed_back_detail", "true");
                params.put("customer_id", customer_id);
                return params;
            }
        };
        queue.add(postRequest);
        postRequest.setRetryPolicy(new DefaultRetryPolicy(20000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }


    public void submit_feed_back(){


        feed_back.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        feed_back.requestWindowFeature(Window.FEATURE_NO_TITLE);
        feed_back.setContentView(R.layout.feedback);

        final RelativeLayout feedback_section = (RelativeLayout)feed_back.findViewById(R.id.feedback_section);
        final RelativeLayout star_rating_section = (RelativeLayout)feed_back.findViewById(R.id.star_rating_section);


        TextView submit_to_feed_back=(TextView)feed_back.findViewById(R.id.submit_to_feed_back);
        final TextView next_to_feed_back=(TextView)feed_back.findViewById(R.id.next_to_feed_back);
        TextView not_now_feedback=(TextView)feed_back.findViewById(R.id.not_now_feedback);
        TextView cancel_submit_feedback=(TextView)feed_back.findViewById(R.id.cancel_submit_feedback);

        final EditText text_feedback = (EditText)feed_back.findViewById(R.id.text_feedback);

        final RatingBar ratingBar = (RatingBar)feed_back.findViewById(R.id.ratingBar);

        next_to_feed_back.setTypeface(null, Typeface.BOLD);
        not_now_feedback.setTypeface(null,Typeface.BOLD);
        cancel_submit_feedback.setTypeface(null,Typeface.BOLD);
        submit_to_feed_back.setTypeface(null,Typeface.BOLD);


        feed_back.show();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if(b){
                    star_rating_set = true;
                    if(v < 1.0){
                        ratingBar.setRating(1.0f);
                    }
                    if(v <= 3.0){
                        dont_show_textbox = true;
                        next_to_feed_back.setText("Next");

                    }else{
                        dont_show_textbox = false;
                        next_to_feed_back.setText("Submit");
                    }
                }

            }
        });
        cancel_submit_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feed_back.dismiss();
                String feed_back_text;
                if(text_feedback.getText().toString().equals("")){
                    feed_back_text = "";
                }else{
                    feed_back_text = text_feedback.getText().toString();
                }
                authenticateUserProgressDialogue();
                send_feed_back_data_to_server(customers_id,Float.toString(ratingBar.getRating()),feed_back_text);

            }
        });
        submit_to_feed_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String feed_back_text;
                if(text_feedback.getText().toString().equals("")){
                    text_feedback.setError("Cannot be empty");
                }else{
                    feed_back_text = text_feedback.getText().toString();
                    authenticateUserProgressDialogue();
                    send_feed_back_data_to_server(customers_id,Float.toString(ratingBar.getRating()),feed_back_text);
                    feed_back.dismiss();
                }



            }
        });
        next_to_feed_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(star_rating_set){

                    if(dont_show_textbox){
                        star_rating_section.setVisibility(View.GONE);
                        feedback_section.setVisibility(View.VISIBLE);
                    }else{
                        authenticateUserProgressDialogue();
                        send_feed_back_data_to_server(customers_id,Float.toString(ratingBar.getRating()),"");
                    }

                }else{
                    Toast.makeText(getActivity(), "Give Rating First", Toast.LENGTH_SHORT).show();
                }


            }
        });
        not_now_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feed_back.dismiss();

            }
        });
    }

    public void send_feed_back_data_to_server(final String customer_id,final String rating,final String feed_back){

        RequestQueue queue = Volley.newRequestQueue(getActivity()); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();
                        redirectedToRecentActivity();

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //error
                        hideProgressBar();
                        Toast.makeText(getContext(),"Error : Something went wrong. Please try again",Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("submit_expert_feedback", "true");
                params.put("customers_id", customer_id);
                params.put("rating", rating);
                params.put("feed_back", feed_back);

                return params;
            }
        };
        queue.add(postRequest);
        postRequest.setRetryPolicy(new DefaultRetryPolicy(20000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }
    public void redirectedToRecentActivity(){
        SharedPreferences sp=getActivity().getSharedPreferences("Login", 0);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.putInt("navigation_checked_item_id",R.id.nav_Recent_activity);
        Ed.commit();

        CheckIfAlreadySignInClass checkIfAlreadySignInClass = new CheckIfAlreadySignInClass(getActivity(),HttpUrl,dashboard_preference);
        checkIfAlreadySignInClass.checkIfAlreadySignedIn(dashboard_preference);
    }



}