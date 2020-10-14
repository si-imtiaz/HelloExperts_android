package com.HelloExperts.HelloExpert;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Education_experience extends AppCompatActivity {
    private ImageView expand_education_details,expand_professional_details,expand_certification_details,sign_out_from_page_ii;
    private LinearLayout education_detail_area,professional_detail_area,certification_detail_area,main_loader_education_experience_bg;
    AVLoadingIndicatorView recent_loader;
    private TextView edu_success,prof_success,cert_success,btn_ok,message,save_and_continue_to_iii,cancel_add_education,cancel_add_professional,cancel_add_certification,add_education_details,add_professional_details,add_certification_details;
    private MaterialSpinner user_degree_country,user_degree_title,user_year_of_enrollment,user_year_of_graduation,user_year_of_joining,user_year_of_leaving,user_year_of_certification;
    private AutoCompleteTextView user_university_name;
    private SharedPreferences dashboard_preference;
    EditText user_degree_name,user_organization,user_job_description,user_designation,user_certification_title,user_certification_organization,user_certification_description;
    private int customers_id;
    private CheckBox is_still_studying_here,is_still_doing_job_here;
    private ArrayAdapter<String> adapter_university;
    private String HttpUrl;
    private List<String> university_list;
    private List<String> countries_list;
    private List<String> countries_id_list;
    private String starting_year,starting_year_job;
    private Dialog error_msg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_experience);


        assignLocalvariables();

        authenticateUserProgressDialogue();
        check_if_data_already_exist_education();
        check_if_data_already_exist_profession();

        check_if_data_already_exist_certification();
        cancel_add_education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                education_detail_area.setVisibility(View.GONE);
                expand_education_details.setVisibility(View.VISIBLE);
            }
        });
        expand_education_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expand_education_details.setVisibility(View.GONE);
                education_detail_area.setVisibility(View.VISIBLE);
            }
        });

        cancel_add_professional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                professional_detail_area.setVisibility(View.GONE);
                expand_professional_details.setVisibility(View.VISIBLE);
            }
        });
        expand_professional_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expand_professional_details.setVisibility(View.GONE);
                professional_detail_area.setVisibility(View.VISIBLE);
            }
        });
        cancel_add_certification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                certification_detail_area.setVisibility(View.GONE);
                expand_certification_details.setVisibility(View.VISIBLE);
            }
        });
        expand_certification_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expand_certification_details.setVisibility(View.GONE);
                certification_detail_area.setVisibility(View.VISIBLE);
            }
        });
        add_education_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        add_professional_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        add_certification_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        add_education_details.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Checkconnectivity checkconnectivity = new Checkconnectivity();
                if (!checkconnectivity.hasInternetConnection(getApplicationContext())) {

                    Toast.makeText(Education_experience.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }else {
                    if (is_still_studying_here.isChecked()) {
                        if (user_degree_title.getText().toString().trim().equals("-- Select --") ||
                                user_degree_name.getText().toString().trim().equals("") ||
                                user_degree_country.getText().toString().trim().equals("-- Select --") ||
                                user_university_name.getText().toString().trim().equals("") ||
                                user_year_of_enrollment.getText().toString().trim().equals("-- Select --")
                        ) {
                            btn_ok = (TextView) error_msg.findViewById(R.id.btn_ok);
                            message = (TextView) error_msg.findViewById(R.id.message);
                            message.setText("You must have to add your Education details");
                            error_msg.show();
                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    error_msg.dismiss();
                                }
                            });
                        } else {
                            authenticateUserProgressDialogue();
                            getAllEducationAndSend();
                        }
                    } else {
                        if (user_degree_title.getText().toString().trim().equals("-- Select --") ||
                                user_degree_name.getText().toString().trim().equals("") ||
                                user_degree_country.getText().toString().trim().equals("") ||
                                user_university_name.getText().toString().trim().equals("") ||
                                user_year_of_enrollment.getText().toString().trim().equals("-- Select --") ||
                                user_year_of_graduation.getText().toString().trim().equals("-- Select --")
                        ) {
                            btn_ok = (TextView) error_msg.findViewById(R.id.btn_ok);
                            message = (TextView) error_msg.findViewById(R.id.message);
                            message.setText("You must have to add your Education details");
                            error_msg.show();
                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    error_msg.dismiss();
                                }
                            });
                        } else {
                            authenticateUserProgressDialogue();
                            getAllEducationAndSend();
                        }
                    }
                }
            }
        });
        add_professional_details.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Checkconnectivity checkconnectivity = new Checkconnectivity();
                if (!checkconnectivity.hasInternetConnection(getApplicationContext())) {

                    Toast.makeText(Education_experience.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }else {
                    if (is_still_doing_job_here.isChecked()) {
                        if (user_designation.getText().toString().trim().equals("") ||
                                user_organization.getText().toString().trim().equals("") ||
                                user_job_description.getText().toString().trim().equals("") ||
                                user_year_of_joining.getText().toString().trim().equals("-- Select --")
                        ) {
                            btn_ok = (TextView) error_msg.findViewById(R.id.btn_ok);
                            message = (TextView) error_msg.findViewById(R.id.message);
                            message.setText("You must have to add your Experience details");
                            error_msg.show();
                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    error_msg.dismiss();
                                }
                            });
                        } else {
                            authenticateUserProgressDialogue();
                            getAllProfessionAndSend();
                        }
                    } else {
                        if (user_designation.getText().toString().trim().equals("") ||
                                user_organization.getText().toString().trim().equals("") ||
                                user_job_description.getText().toString().trim().equals("") ||
                                user_year_of_joining.getText().toString().trim().equals("-- Select --") ||
                                user_year_of_leaving.getText().toString().trim().equals("-- Select --")
                        ) {
                            btn_ok = (TextView) error_msg.findViewById(R.id.btn_ok);
                            message = (TextView) error_msg.findViewById(R.id.message);
                            message.setText("You must have to add your Experience details");
                            error_msg.show();
                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    error_msg.dismiss();
                                }
                            });
                        } else {
                            authenticateUserProgressDialogue();
                            getAllProfessionAndSend();
                        }
                    }
                }
            }
        });
        add_certification_details.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Checkconnectivity checkconnectivity = new Checkconnectivity();
                if (!checkconnectivity.hasInternetConnection(getApplicationContext())) {

                    Toast.makeText(Education_experience.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }else {
                    if (user_certification_title.getText().toString().trim().equals("") ||
                            user_certification_organization.getText().toString().trim().equals("") ||
                            user_certification_description.getText().toString().trim().equals("") ||
                            user_year_of_certification.getText().toString().trim().equals("-- Select --")
                    ) {
                        btn_ok = (TextView) error_msg.findViewById(R.id.btn_ok);
                        message = (TextView) error_msg.findViewById(R.id.message);
                        message.setText("You must have to add your Certification details");
                        error_msg.show();
                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                error_msg.dismiss();
                            }
                        });
                    } else {
                        authenticateUserProgressDialogue();
                        getAllCertificationAndSend();
                    }
                }

            }
        });
        sign_out_from_page_ii.setOnClickListener(new View.OnClickListener() {
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
        save_and_continue_to_iii.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Checkconnectivity checkconnectivity = new Checkconnectivity();
                if (!checkconnectivity.hasInternetConnection(getApplicationContext())) {

                    Toast.makeText(Education_experience.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }else {
                    if (edu_success.getVisibility() == View.GONE) {
                        btn_ok = (TextView) error_msg.findViewById(R.id.btn_ok);
                        message = (TextView) error_msg.findViewById(R.id.message);
                        message.setText("You must have to add your Educational details");
                        error_msg.show();
                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                error_msg.dismiss();
                            }
                        });
                    } else {
                        authenticateUserProgressDialogue();
                        save_and_goto_next();
                    }
                }

            }
        });

        getCountriesList();
        user_degree_title.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

            }
        });
        user_degree_country.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                university_list.clear();
                if(!countries_id_list.get(position).equals("0")){
                    getUniversitiesList(countries_id_list.get(position));
                }

            }
        });
        is_still_studying_here.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(is_still_studying_here.isChecked()){
                    user_year_of_graduation.setEnabled(false);
                    user_year_of_graduation.setText("");
                }else{
                    user_year_of_graduation.setEnabled(true);
                }

            }
        });
        user_year_of_enrollment.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                String year  = Integer.toString(Integer.parseInt(user_year_of_enrollment.getText().toString())+1);

                user_year_of_graduation.setItems(getYearList(year,Integer.toString(Calendar.getInstance().get(Calendar.YEAR))));


            }
        });
        user_year_of_graduation.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

            }
        });

        user_year_of_joining.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                String year  = user_year_of_joining.getText().toString();

                user_year_of_leaving.setItems(getYearList(year,Integer.toString(Calendar.getInstance().get(Calendar.YEAR))));


            }
        });


        is_still_doing_job_here.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(is_still_doing_job_here.isChecked()){
                    user_year_of_leaving.setText("");
                    user_year_of_leaving.setEnabled(false);
                }else{
                    user_year_of_leaving.setEnabled(true);
                }

            }
        });
        user_year_of_enrollment.setItems(getYearList(starting_year,Integer.toString(Calendar.getInstance().get(Calendar.YEAR))));
        user_year_of_graduation.setItems(getYearList(starting_year,Integer.toString(Calendar.getInstance().get(Calendar.YEAR))));
        user_year_of_joining.setItems(getYearList(starting_year_job,Integer.toString(Calendar.getInstance().get(Calendar.YEAR))));
        user_year_of_leaving.setItems(getYearList(starting_year_job,Integer.toString(Calendar.getInstance().get(Calendar.YEAR))));
        user_year_of_certification.setItems(getYearList(starting_year_job,Integer.toString(Calendar.getInstance().get(Calendar.YEAR))));
    }
    public void assignLocalvariables(){
        sign_out_from_page_ii = (ImageView)findViewById(R.id.sign_out_from_page_ii);
        edu_success = (TextView)findViewById(R.id.education_added);
        prof_success = (TextView)findViewById(R.id.profession_added);
        cert_success = (TextView)findViewById(R.id.certification_added);

        save_and_continue_to_iii = (TextView)findViewById(R.id.save_and_continue_to_iii);
        this.dashboard_preference=getApplicationContext().getSharedPreferences("Login",0);
        this.customers_id = dashboard_preference.getInt("customers_id",0);
        HttpUrl = getResources().getString(R.string.about_yourself_api_url);
        university_list = new ArrayList<>();
        countries_list =new ArrayList<>();
        countries_id_list = new ArrayList<>();
        starting_year = "1980";
        starting_year_job ="1950";
        error_msg = new Dialog(this);
        error_msg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        error_msg.setContentView(R.layout.error_alert);
      //////////////////////////////////////////////////////////////////////////////////////
        user_degree_name =(EditText)findViewById(R.id.user_degree_name);
        is_still_studying_here = (CheckBox)findViewById(R.id.is_still_studying_here);
        user_university_name = (AutoCompleteTextView)findViewById(R.id.user_university_name);
        user_degree_country = (MaterialSpinner)findViewById(R.id.user_degree_country);
        user_degree_title = (MaterialSpinner)findViewById(R.id.user_degree_title);
        user_year_of_graduation = (MaterialSpinner)findViewById(R.id.user_year_of_graduation);
        user_year_of_enrollment = (MaterialSpinner)findViewById(R.id.user_year_of_enrollment);
        add_education_details = (TextView)findViewById(R.id.add_education_details);
    ////////////////////////////////////////////////////////////////////////////////////////////
        is_still_doing_job_here = (CheckBox)findViewById(R.id.is_still_doing_job_here);
        user_designation = (EditText) findViewById(R.id.user_designation);
        user_organization =(EditText)findViewById(R.id.user_organization);
        user_job_description =(EditText)findViewById(R.id.user_job_description);
        user_year_of_joining = (MaterialSpinner)findViewById(R.id.user_year_of_joining);
        user_year_of_leaving = (MaterialSpinner)findViewById(R.id.user_year_of_leaving);
        add_professional_details = (TextView)findViewById(R.id.add_professional_details);
    ///////////////////////////////////////////////////////////////////////////////////////////////////

        user_certification_title = (EditText) findViewById(R.id.user_certification_title);
        user_certification_organization =(EditText)findViewById(R.id.user_certification_organization);
        user_certification_description =(EditText)findViewById(R.id.user_certification_description);
        user_year_of_certification = (MaterialSpinner)findViewById(R.id.user_year_of_certification);
        add_certification_details = (TextView)findViewById(R.id.add_certification_details);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        expand_education_details = (ImageView)findViewById(R.id.expand_education_details);
        expand_professional_details = (ImageView)findViewById(R.id.expand_professional_details);
        expand_certification_details = (ImageView)findViewById(R.id.expand_certification_details);
        education_detail_area = (LinearLayout)findViewById(R.id.education_detail_area);
        professional_detail_area = (LinearLayout)findViewById(R.id.professional_detail_area);
        certification_detail_area = (LinearLayout)findViewById(R.id.certification_detail_area);
        cancel_add_education = (TextView)findViewById(R.id.cancel_add_education);
        cancel_add_professional = (TextView)findViewById(R.id.cancel_add_professional);
        cancel_add_certification = (TextView)findViewById(R.id.cancel_add_certification);
       ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        user_degree_title.setItems("-- Select --","High School Diploma", "Associate", "Bachelors (BS)", "Bachelors (BA)", "Bachelors","Post-bachelor","Masters","Masters (MA)","Masters (MS)","MBA","Specialist","Doctorate");

        main_loader_education_experience_bg = (LinearLayout) findViewById(R.id.main_loader_education_experience_bg);
        recent_loader = (AVLoadingIndicatorView)findViewById(R.id.recent_loader);
        recent_loader.show();

    }

    public List<String> getYearList(String starting_year,String last_year){
        List<String> enrollment_years = new ArrayList<>();
        enrollment_years.add("-- Select --");
        for(int i = Integer.parseInt(starting_year); i <= Integer.parseInt(last_year);i++ ){
            enrollment_years.add(Integer.toString(i));
        }

        return enrollment_years;

    }


    private void getCountriesList(){
        RequestQueue queue= Volley.newRequestQueue(this); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray_complete = new JSONArray(response);
                            JSONArray jsonObject_all_country = jsonArray_complete.getJSONArray(1);
                            countries_list.add("-- Select --");
                            countries_id_list.add("0");
                            for(int i = 0; i < jsonObject_all_country.length();i++){
                                countries_list.add(jsonObject_all_country.getJSONObject(i).getString("country_name"));
                                countries_id_list.add(jsonObject_all_country.getJSONObject(i).getString("country_id"));
                            }
                            user_degree_country.setItems(countries_list);
                            getUniversitiesList(jsonObject_all_country.getJSONObject(0).getString("country_id"));

                        } catch (JSONException e) {
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
                params.put("customers_id",Integer.toString(customers_id));
                params.put("get_countries_list", "true");
                return params;
            }
        };
        queue.add(postRequest);
    }
    private void getUniversitiesList(final String country_id){

        RequestQueue queue= Volley.newRequestQueue(this); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray_complete = new JSONArray(response);
                            for(int i = 0; i < jsonArray_complete.length();i++){
                                university_list.add(jsonArray_complete.getJSONObject(i).getString("university_name"));
                            }
                            adapter_university = new ArrayAdapter<String>(getApplicationContext(),
                                    R.layout.iconic_search, R.id.name, university_list);
                            user_university_name.setAdapter(adapter_university);

                        } catch (JSONException e) {
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
                params.put("customers_id",Integer.toString(customers_id));
                params.put("country_id",country_id);
                params.put("get_universities_list_country_wise", "true");
                return params;
            }
        };
        queue.add(postRequest);
    }
    private void getAllEducationAndSend(){
        RequestQueue queue= Volley.newRequestQueue(this); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();
                        education_detail_area.setVisibility(View.GONE);
                        expand_education_details.setVisibility(View.GONE);
                        edu_success.setVisibility(View.VISIBLE);


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
                params.put("customers_id",Integer.toString(customers_id));
                params.put("save_education_information","true");
                params.put("country",user_degree_country.getText().toString());
                params.put("degree_name",user_degree_name.getText().toString());
                params.put("degree_level",user_degree_title.getText().toString());
                params.put("university",user_university_name.getText().toString());
                params.put("enrollment_year",user_year_of_enrollment.getText().toString());
                params.put("graduation_year",user_year_of_graduation.getText().toString());
                return params;
            }
        };
        queue.add(postRequest);

    }
    private void getAllProfessionAndSend(){
        RequestQueue queue= Volley.newRequestQueue(this); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();

                        professional_detail_area.setVisibility(View.GONE);
                        expand_professional_details.setVisibility(View.GONE);
                        prof_success.setVisibility(View.VISIBLE);

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
                params.put("customers_id",Integer.toString(customers_id));
                params.put("save_professional_information","true");
                params.put("designation",user_designation.getText().toString());
                params.put("organization",user_organization.getText().toString());
                params.put("job_description",user_job_description.getText().toString());
                params.put("joining_year",user_year_of_joining.getText().toString());
                params.put("leaving_year",user_year_of_leaving.getText().toString());
                return params;
            }
        };
        queue.add(postRequest);

    }
    private void getAllCertificationAndSend(){
        RequestQueue queue= Volley.newRequestQueue(this); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();

                        certification_detail_area.setVisibility(View.GONE);
                        expand_certification_details.setVisibility(View.GONE);
                        cert_success.setVisibility(View.VISIBLE);

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
                params.put("customers_id",Integer.toString(customers_id));
                params.put("save_Certification_information","true");
                params.put("certification_title",user_certification_title.getText().toString());
                params.put("certification_organization",user_certification_organization.getText().toString());
                params.put("certification_description",user_certification_description.getText().toString());
                params.put("certification_year",user_year_of_certification.getText().toString());
                return params;
            }
        };
        queue.add(postRequest);

    }
    private void authenticateUserProgressDialogue(){

        this.main_loader_education_experience_bg.setVisibility(View.VISIBLE);

    }

    private void hideProgressBar(){

        this.main_loader_education_experience_bg.setVisibility(View.GONE);
    }

    private void check_if_data_already_exist_education(){
        RequestQueue queue= Volley.newRequestQueue(this); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("response").equals("already_added")){
                                education_detail_area.setVisibility(View.GONE);
                                expand_education_details.setVisibility(View.GONE);
                                edu_success.setVisibility(View.VISIBLE);
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
                params.put("customers_id",Integer.toString(customers_id));
                params.put("check_if_data_exist","true");
                return params;
            }
        };
        queue.add(postRequest);

    }
    private void check_if_data_already_exist_profession(){
        RequestQueue queue= Volley.newRequestQueue(this); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("response").equals("already_added")){
                                professional_detail_area.setVisibility(View.GONE);
                                expand_professional_details.setVisibility(View.GONE);
                                prof_success.setVisibility(View.VISIBLE);
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
                params.put("customers_id",Integer.toString(customers_id));
                params.put("check_if_data_exist_professional","true");
                return params;
            }
        };
        queue.add(postRequest);

    }
    private void check_if_data_already_exist_certification(){
        RequestQueue queue= Volley.newRequestQueue(this); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("response").equals("already_added")){
                                certification_detail_area.setVisibility(View.GONE);
                                expand_certification_details.setVisibility(View.GONE);
                                cert_success.setVisibility(View.VISIBLE);
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
                params.put("customers_id",Integer.toString(customers_id));
                params.put("check_if_data_exist_certification","true");
                return params;
            }
        };
        queue.add(postRequest);

    }
    private void save_and_goto_next(){
        RequestQueue queue= Volley.newRequestQueue(this); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();
                        startActivity(new Intent(getApplicationContext(),Add_skills.class));
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
                params.put("customers_id",Integer.toString(customers_id));
                params.put("save_progress","true");
                params.put("set_rejection","true");
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

}
