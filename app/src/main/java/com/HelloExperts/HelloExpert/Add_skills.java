package com.HelloExperts.HelloExpert;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.HelloExperts.HelloExpert.R;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;


import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;


public class Add_skills extends AppCompatActivity {
    private LinearLayout main_layout,selected_skills_layout,main_loader_add_skills_bg;
    AVLoadingIndicatorView recent_loader;
    private ScrollView selected_scroll;
    private CheckBox checkBox;
    private TextView save_and_continue_to_iv;
    private List<String> name_skills;
    private TextView btn_ok,message;
    private ProgressDialog progressDialog;
    private TextView second_level_skills,selected;
    private String current_category_id;
    private List<String> parent_skills_name,previously_selected;
    private List<String> id_skills,id_all_skills;
    private Dialog error_msg;
    private String HttpUrl;

    SharedPreferences dashboard_preference;
    int customers_id;
    ImageView sign_out_from_page_iii;
    private MaterialSpinner main_skills_categories;
    private LinkedHashMap<String, String> child_skills,selected_skills;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_skills);



        this.dashboard_preference=getApplicationContext().getSharedPreferences("Login",0);
        this.customers_id = dashboard_preference.getInt("customers_id",0);
        error_msg = new Dialog(this);
        error_msg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sign_out_from_page_iii = (ImageView)findViewById(R.id.sign_out_from_page_iii);
        error_msg.setContentView(R.layout.error_alert);
        save_and_continue_to_iv = (TextView)findViewById(R.id.save_and_continue_to_iv);
        progressDialog = new ProgressDialog(this);

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

        parent_skills_name = new ArrayList<>();
        HttpUrl = getResources().getString(R.string.about_yourself_api_url);
        main_skills_categories = (MaterialSpinner)findViewById(R.id.main_skills_categories);
        main_layout = (LinearLayout) findViewById(R.id.main_layout);
        selected_skills_layout = (LinearLayout) findViewById(R.id.selected_skills);
        selected_scroll = (ScrollView) findViewById(R.id.selected_scroll);
        name_skills = new ArrayList<>();
        id_all_skills = new ArrayList<>();
        previously_selected = new ArrayList<>();
        id_skills = new ArrayList<>();
        child_skills = new LinkedHashMap<String, String>();
        selected_skills = new LinkedHashMap<String, String>();

        main_loader_add_skills_bg = (LinearLayout)findViewById(R.id.main_loader_add_skills_bg);
        recent_loader = (AVLoadingIndicatorView)findViewById(R.id.recent_loader);
        recent_loader.show();

        get_previous_skills();

        authenticateUserProgressDialogue();
        getAllMainSkills();

        sign_out_from_page_iii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Logout successfully",Toast.LENGTH_LONG).show();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("Login", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit(); // commit changes
                clearFromLog();
                finish();
                startActivity(new Intent(getApplicationContext(),login.class));
            }
        });
        main_skills_categories.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                child_skills.clear();

                main_layout.removeAllViews();
                child_skills = new LinkedHashMap<String, String>();

                authenticateUserProgressDialogue();
                setCurrent_category_id(id_skills.get(position));
                getReleventSkills(id_skills.get(position));

            }
        });
        save_and_continue_to_iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Checkconnectivity checkconnectivity = new Checkconnectivity();
                if(!checkconnectivity.hasInternetConnection(getApplicationContext())){
                    Toast.makeText(Add_skills.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }else {
                    if (id_all_skills.size() > 10) {
                        btn_ok = (TextView) error_msg.findViewById(R.id.btn_ok);
                        message = (TextView) error_msg.findViewById(R.id.message);
                        message.setText("Can't Select more than 10 skills");
                        error_msg.show();
                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                error_msg.dismiss();
                            }
                        });
                    } else if (id_all_skills.size() == 0) {
                        btn_ok = (TextView) error_msg.findViewById(R.id.btn_ok);
                        message = (TextView) error_msg.findViewById(R.id.message);
                        message.setText("Please Select at least 1 skill");
                        error_msg.show();
                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                error_msg.dismiss();
                            }
                        });
                    } else {
                        authenticateUserProgressDialogue();
                        save_all_skills();

                    }
                }
            }
        });

    }
    public String getCurrent_category_id() {
        return current_category_id;
    }


    private void authenticateUserProgressDialogue(){

        this.main_loader_add_skills_bg.setVisibility(View.VISIBLE);

    }

    private void hideProgressBar(){

        this.main_loader_add_skills_bg.setVisibility(View.GONE);
    }



    public void setCurrent_category_id(String current_category_id) {
        this.current_category_id = current_category_id;
    }
    View.OnClickListener getOnClickDoSomething(final CheckBox button) {
        return new View.OnClickListener() {
            public void onClick(View v) {

                if(!(button.isChecked())){
                    id_all_skills.remove(Integer.toString(button.getId()));

                    TextView temp = (TextView)findViewById(button.getId());
                    selected_skills_layout.removeView(temp);
                    if(id_all_skills.size() == 0){
                        selected_scroll.setVisibility(View.GONE);
                    }

                }else{

                    id_all_skills.add(Integer.toString(button.getId()));
                    selected = new TextView(getApplicationContext());
                    selected.setId(button.getId());
                    selected.setText(button.getText().toString());
                    selected.setTextColor(Color.parseColor("#ffffff"));
                    selected.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_close_black_24dp, 0);
                    selected.setPadding(15,15,15,15);
                    selected.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(10, 10, 10, 10);
                    selected.setLayoutParams(params);
                    selected.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnformate));
                    selected.setOnClickListener(getOnClickDoSomethingSelected(selected));
                    selected_scroll.setVisibility(View.VISIBLE);
                    selected_skills_layout.addView(selected);
                }


            }
        };
    }
    View.OnClickListener getOnClickDoSomethingSelected(final TextView button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                id_all_skills.remove(Integer.toString(button.getId()));
                selected_skills_layout.removeView(button);
                if(id_all_skills.size() == 0){
                    selected_scroll.setVisibility(View.GONE);
                }
                child_skills.clear();
                main_layout.removeAllViews();
                child_skills = new LinkedHashMap<String, String>();
                authenticateUserProgressDialogue();
                getReleventSkills(getCurrent_category_id());

            }
        };
    }
    public void getAllMainSkills(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext()); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i =0 ;i<jsonArray.length();i++){
                                id_skills.add(jsonArray.getJSONObject(i).getString("category_id"));
                                name_skills.add(jsonArray.getJSONObject(i).getString("category_name"));
                            }
                            main_skills_categories.setItems(name_skills);
                            setCurrent_category_id(id_skills.get(0));
                            getReleventSkills(getCurrent_category_id());

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
                params.put("get_main_category_list", "true");
                return params;
            }
        };
        queue.add(postRequest);
    }
    public void getReleventSkills(final String category_id){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext()); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if(jsonArray.getJSONObject(0).getString("skills_child").equals("false")){
                               for (int i =0 ;i<jsonArray.length();i++){
                                    child_skills.put(jsonArray.getJSONObject(i).getString("skills_id"), jsonArray.getJSONObject(i).getString("skills_name"));
                                }
                                Set<?> set = child_skills.entrySet();
                                // Get an iterator
                                Iterator<?> iterator = set.iterator();
                                // Display elements
                                while (iterator.hasNext()) {
                                    @SuppressWarnings("rawtypes")
                                    Map.Entry me = (Map.Entry) iterator.next();
                                    checkBox = new CheckBox(getApplicationContext());
                                    checkBox.setId(Integer.parseInt(me.getKey().toString()));
                                    if(id_all_skills.contains(me.getKey().toString())){
                                        checkBox.setChecked(true);
                                    }
                                    checkBox.setText(me.getValue().toString());
                                    checkBox.setButtonTintList(getResources().getColorStateList(R.color.btn_color));
                                    checkBox.setOnClickListener(getOnClickDoSomething(checkBox));
                                    main_layout.addView(checkBox);
                                }
                            }else if(jsonArray.getJSONObject(0).getString("skills_child").equals("true")){


                                int i = 0, j = 1;
                                 for( ;i<jsonArray.length();){
                                     child_skills.clear();
                                     second_level_skills = new TextView(getApplicationContext());
                                     second_level_skills.setTypeface(null, Typeface.BOLD);
                                     second_level_skills.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                                     second_level_skills.setTextColor(Color.parseColor("#000000"));
                                     second_level_skills.setId(Integer.parseInt(jsonArray.getJSONObject(i).getString("skills_id")));
                                    second_level_skills.setText(jsonArray.getJSONObject(i).getString("skills_name"));
                                    main_layout.addView(second_level_skills);
                                    for( int k = 0 ; k < Integer.parseInt(jsonArray.getJSONObject(i).getString("child_count")) ; k++) {

                                        child_skills.put(jsonArray.getJSONObject(j).getString("skills_id"), jsonArray.getJSONObject(j).getString("skills_name"));
                                        j++;
                                    }
                                    i = j;
                                    j++;

                                    Set<?> set = child_skills.entrySet();
                                    Iterator<?> iterator = set.iterator();
                                    while (iterator.hasNext()) {
                                        @SuppressWarnings("rawtypes")
                                        Map.Entry me = (Map.Entry) iterator.next();
                                        checkBox = new CheckBox(getApplicationContext());
                                        checkBox.setId(Integer.parseInt(me.getKey().toString()));
                                        if(id_all_skills.contains(me.getKey().toString())){
                                            checkBox.setChecked(true);
                                        }
                                        checkBox.setText(me.getValue().toString());
                                        checkBox.setButtonTintList(getResources().getColorStateList(R.color.btn_color));
                                        checkBox.setOnClickListener(getOnClickDoSomething(checkBox));
                                        main_layout.addView(checkBox);
                                    }

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
                        hideProgressBar();

                        Toast.makeText(getApplicationContext(),"Error : Something went wrong. Please try again adas",Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("get_skills_child", "true");
                params.put("category_id", category_id);
                return params;
            }
        };
        queue.add(postRequest);
    }
    public void save_all_skills(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext()); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();


                        startActivity(new Intent(Add_skills.this,Education_verification.class));


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
                params.put("save_all_skills", "true");
                params.put("customers_id", Integer.toString(customers_id));
                for(int i = 0 ;i < id_all_skills.size() ; i++){
                    params.put("all_skills"+Integer.toString(i), id_all_skills.get(i));
                }

                return params;
            }
        };
        queue.add(postRequest);
    }
    public void get_previous_skills(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext()); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();
                        id_all_skills.clear();

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i =0 ; i < jsonArray.length() ; i++){
                                id_all_skills.add(jsonArray.getJSONObject(i).getString("skill_id"));
                                previously_selected.add(jsonArray.getJSONObject(i).getString("skill_name"));
                            }


                           if(id_all_skills.size() == 0){
                                selected_scroll.setVisibility(View.GONE);
                            }else{
                                selected_scroll.setVisibility(View.VISIBLE);
                            }
                            for(int i = 0 ; i < id_all_skills.size(); i++){
                                selected = new TextView(getApplicationContext());
                                selected.setId(Integer.parseInt(id_all_skills.get(i)));
                                selected.setText(previously_selected.get(i));
                                selected.setTextColor(Color.parseColor("#ffffff"));
                                selected.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_close_black_24dp, 0);
                                selected.setPadding(8,8,8,8);
                                selected.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );
                                params.setMargins(10, 10, 10, 10);
                                selected.setLayoutParams(params);
                                selected.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnformate));
                                selected.setOnClickListener(getOnClickDoSomethingSelected(selected));
                                selected_scroll.setVisibility(View.VISIBLE);
                                selected_skills_layout.addView(selected);
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
                params.put("get_previous_skilled", "true");
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


}
