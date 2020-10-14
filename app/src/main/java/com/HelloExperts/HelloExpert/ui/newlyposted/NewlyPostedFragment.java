package com.HelloExperts.HelloExpert.ui.newlyposted;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.HelloExperts.HelloExpert.HttpTrustManager;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.HelloExperts.HelloExpert.MySingleton;
import com.HelloExperts.HelloExpert.Newly_questions_adapter;
import com.HelloExperts.HelloExpert.Newly_questions_data;
import com.HelloExperts.HelloExpert.R;
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

public class NewlyPostedFragment extends Fragment {

    private NewlyPostedViewModel newlyPostedViewModel;
    private SwipeRefreshLayout refresh_new_questions;
    private  String HttpUrl;
    private View root;
    private ProgressDialog progressDialog;
    public String customers_id,customers_verification_status;
    private Button try_again;
    private TextView no_internet_text;
    private ImageView no_internet_symbol;
    private RecyclerView recyclerView;
    SharedPreferences dashboard_preference;
    RecyclerView.Adapter adapter;
    public String customers_tokken;
    private LinearLayout main_loader_newly_posted_questions_bg;

    private AVLoadingIndicatorView recent_loader;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newlyPostedViewModel =
                ViewModelProviders.of(this).get(NewlyPostedViewModel.class);
         root = inflater.inflate(R.layout.fragment_newly_posted, container, false);
        SharedPreferences sp=this.getActivity().getSharedPreferences("Login", 0);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.putInt("navigation_checked_item_id",R.id.nav_new_questions);
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
        try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ftr = getFragmentManager().beginTransaction();
                ftr.detach(NewlyPostedFragment.this).attach(NewlyPostedFragment.this).commit();
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
        newlyPostedViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });




        return root;
    }
    private void assignLocalVariables(){
        recyclerView  = (RecyclerView)root.findViewById(R.id.newly_questions_recyclerView);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        HttpUrl = getResources().getString(R.string.about_yourself_api_url);
        try_again = (Button)root.findViewById(R.id.try_again);
        no_internet_symbol=(ImageView)root.findViewById(R.id.no_connection_symbol);
        no_internet_text = (TextView)root.findViewById(R.id.no_connection_text);
        dashboard_preference=getActivity().getSharedPreferences("Login",0);
        int temp_id = dashboard_preference.getInt("customers_id",0);
        customers_tokken = dashboard_preference.getString("customers_tokken",null);
        customers_verification_status = dashboard_preference.getString("customers_verification_status",null);
        customers_id = Integer.toString(temp_id);
        refresh_new_questions = (SwipeRefreshLayout)root.findViewById(R.id.refresh_new_questions);
        main_loader_newly_posted_questions_bg = (LinearLayout)root.findViewById(R.id.main_loader_newly_posted_questions_bg);
        recent_loader= (AVLoadingIndicatorView)root.findViewById(R.id.recent_loader);
        recent_loader.show();
    }
    private void sendDataForAuthentication() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


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
                      Toast.makeText(getContext(),"Error: Something went wrong. Please try again. ",Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String,String>();

               params.put("get_newly_posted_questions","true");
               params.put("customers_id",customers_id);
               params.put("customers_tokken",customers_tokken);
                return params;
            }

        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    private void authenticateUserProgressDialogue(){

        this.main_loader_newly_posted_questions_bg.setVisibility(View.VISIBLE);

    }

    private void hideProgressBar(){

        this.main_loader_newly_posted_questions_bg.setVisibility(View.GONE);
    }
    private void parseData(String response){
        try{


            List<Newly_questions_data> complete_list = new ArrayList<>();
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
                    jsonArray.getJSONObject(0).getString("data").equals("true")) {
                hideProgressBar();
                for (int i = 0; i < jsonArray.length(); i++) {
                    // Get current json object
                    JSONObject student = jsonArray.getJSONObject(i);

                    String question = student.getString("question");
                    String date = student.getString("date");
                    String price = student.getString("price");
                    if(customers_verification_status.equals("2")){
                        price = " ";
                    }
                    String question_id = student.getString("question_id");
                    Newly_questions_data newly_questions_data = new Newly_questions_data(question, date, price, Integer.parseInt(question_id));
                    complete_list.add(newly_questions_data);


                    if (i + 1 == jsonArray.length()) {
                        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setHasFixedSize(true);
                        adapter = new Newly_questions_adapter(complete_list);
                        recyclerView.setAdapter(adapter);

                    }


                }
            }




        }catch (JSONException e){
            e.printStackTrace();
        }

    }


}