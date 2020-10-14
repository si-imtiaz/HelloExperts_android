package com.HelloExperts.HelloExpert.ui.answers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.HelloExperts.HelloExpert.HttpTrustManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.HelloExperts.HelloExpert.All_answer_adapter;
import com.HelloExperts.HelloExpert.All_answers_data;
import com.HelloExperts.HelloExpert.Categories_data;
import com.HelloExperts.HelloExpert.MySingleton;
import com.HelloExperts.HelloExpert.R;
import com.HelloExperts.HelloExpert.login;
import com.jaredrummler.materialspinner.MaterialSpinner;
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

public class AllAnswersFragment extends Fragment {

    private String start,limit;
    private AllAnswersViewModel ViewModel;
    private SwipeRefreshLayout refresh_all_questions;
    private  String HttpUrl;
    List<String> temp_list;
    private View root;
    private ProgressDialog progressDialog;
    public String customers_id;
    private Button try_again;
    private TextView no_internet_text;
    private ImageView no_internet_symbol;
    private RecyclerView recyclerView_all_question;
    SharedPreferences dashboard_preference;
    RecyclerView.Adapter adapter;
    private All_answer_adapter all_answer_adapter;
    private ArrayList<All_answers_data> all_answers_data;
    private String category_name;
    int all_project_count;
    private MaterialSpinner question_categories;
    private Boolean itShouldLoadMore;
    public String customers_tokken;
    public TextView no_recent_answers;
    private LinearLayout main_loader_all_answers_activity_bg;
    private AVLoadingIndicatorView recent_loader;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ViewModel =
                ViewModelProviders.of(this).get(AllAnswersViewModel.class);
        root = inflater.inflate(R.layout.fragment_all_answers, container, false);

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


        SharedPreferences sp=this.getActivity().getSharedPreferences("Login", 0);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.putInt("navigation_checked_item_id",R.id.nav_all_answers);
        Ed.commit();

        assignLocalVariables();
        this.recyclerView_all_question.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView_all_question.setHasFixedSize(true);
        this.recyclerView_all_question.setAdapter(this.all_answer_adapter);
        authenticateUserProgressDialogue();
        getQuestionCategories();
        setStart("0");
        setLimit("10");
        sendDataForAuthentication();
        recyclerView_all_question.addOnScrollListener(new RecyclerView.OnScrollListener() {

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {

                        if (itShouldLoadMore) {
                            setStart(Integer.toString(Integer.parseInt(getLimit())+Integer.parseInt(getStart())));
                            setLimit(getLimit());
                            authenticateUserProgressDialogue();
                            sendDataForAuthentication();

                        }
                    }
                }
            }

        });
        question_categories.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                authenticateUserProgressDialogue();

                set_category_name(before(item," (").trim());
                setStart("0");
                setLimit("10");
                all_answers_data = new ArrayList<>();
                all_answer_adapter = new All_answer_adapter(all_answers_data);
                recyclerView_all_question.setAdapter(all_answer_adapter);
                sendDataForAuthentication();
            }
        });
        try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ftr = getFragmentManager().beginTransaction();
                ftr.detach(AllAnswersFragment.this).attach(AllAnswersFragment.this).commit();
            }
        });
        refresh_all_questions.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                authenticateUserProgressDialogue();
                temp_list.clear();
                getQuestionCategories();
                setStart("0");
                setLimit("10");

                question_categories.setItems(temp_list);

                all_answers_data = new ArrayList<>();
                all_answer_adapter = new All_answer_adapter(all_answers_data);
                recyclerView_all_question.setAdapter(all_answer_adapter);
                sendDataForAuthentication();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh_all_questions.setRefreshing(false);
                    }
                },3000);
            }
        });

        return root;
    }
    static String before(String value, String a) {
        // Return substring containing all characters before a string.
        int posA = value.indexOf(a);
        if (posA == -1) {
            return "";
        }
        return value.substring(0, posA);
    }
    private void assignLocalVariables(){
        this.recyclerView_all_question  = (RecyclerView)root.findViewById(R.id.all_answers_recyclerView);
        this.progressDialog = new ProgressDialog(getActivity());
        this.progressDialog.setCanceledOnTouchOutside(false);
        this.HttpUrl = getResources().getString(R.string.about_yourself_api_url);
        this.question_categories = (MaterialSpinner)root.findViewById(R.id.answer_categories);
        this.temp_list = new ArrayList<>();
        this.all_project_count = 0;
        this.try_again = (Button)root.findViewById(R.id.try_again);
        this.no_internet_symbol=(ImageView)root.findViewById(R.id.no_connection_symbol);
        this.no_internet_text = (TextView)root.findViewById(R.id.no_connection_text);
        this.dashboard_preference=getActivity().getSharedPreferences("Login",0);
        int temp_id = dashboard_preference.getInt("customers_id",0);
        customers_tokken = dashboard_preference.getString("customers_tokken",null);
        this.customers_id = Integer.toString(temp_id);
        this.refresh_all_questions = (SwipeRefreshLayout)root.findViewById(R.id.refresh_all_answers);
        this.category_name = "All Categories";
        this.all_answers_data = new ArrayList<>();
        this.all_answer_adapter = new All_answer_adapter(this.all_answers_data);
        this.itShouldLoadMore = true;
        this.start = "0";
        this.limit = "10";
        no_recent_answers = (TextView)root.findViewById(R.id.no_recent_answers);
        main_loader_all_answers_activity_bg = (LinearLayout) root.findViewById(R.id.main_loader_all_answers_activity_bg);
        recent_loader = (AVLoadingIndicatorView)root.findViewById(R.id.recent_loader);
        recent_loader.show();

        // load_all_questions = (ProgressBar)root.findViewById(R.id.load_all_questions);

    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getLimit() {
        return this.limit;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getStart() {
        return this.start;
    }

    private void set_category_name(String c){
        this.category_name = c;
    }
    private String get_category_name(){
        return this.category_name;
    }

    private void getQuestionCategories(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            all_project_count =0;
                            JSONArray jsonArray = new JSONArray(response);

                            if (jsonArray.getJSONObject(0).getString("login_status").equals("false") &&
                                    jsonArray.getJSONObject(0).getString("data").equals("go_to_login")) {
                                Toast.makeText(getActivity(), "Error : You must login first to continue", Toast.LENGTH_LONG).show();
                                SharedPreferences pref = getActivity().getSharedPreferences("Login", 0);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.clear();
                                editor.commit(); // commit changes
                                getActivity().finish();
                                startActivity(new Intent(getActivity(), login.class));
                            } else {
                                hideProgressBar();
                                if(jsonArray.getJSONObject(0).getString("data").equals("false")){
                                    no_recent_answers.setVisibility(View.VISIBLE);
                                    recyclerView_all_question.setVisibility(View.GONE);
                                }

                            for (int i = 0; i < jsonArray.length(); i++) {
                                all_project_count += Integer.parseInt(jsonArray.getJSONObject(i).getString("category_answer_count"));
                            }
                            Categories_data categories_data = new Categories_data("All Statuses", all_project_count, 0);
                            temp_list.add(categories_data.getCategory_name() + " (" + categories_data.getCategory_count() + ")");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject student = jsonArray.getJSONObject(i);
                                String question_category_name = student.getString("category_answer_name");
                                String question_category_count = student.getString("category_answer_count");
                                String question_category_id = student.getString("category_answer_id");
                                Categories_data categories_data1 = new Categories_data(question_category_name, Integer.parseInt(question_category_count), Integer.parseInt(question_category_id));
                                temp_list.add(categories_data1.getCategory_name() + " (" + categories_data1.getCategory_count() + ")");

                            }
                            question_categories.setItems(temp_list);

                        }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        error.printStackTrace();
                        hideProgressBar();
                        recyclerView_all_question.setVisibility(View.GONE);
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
                params.put("customer_id",customers_id);
                params.put("customers_tokken",customers_tokken);
                params.put("get_answers_categories","true");
                return params;
            }

        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
    private void authenticateUserProgressDialogue(){

        this.main_loader_all_answers_activity_bg.setVisibility(View.VISIBLE);

    }

    private void hideProgressBar(){

        this.main_loader_all_answers_activity_bg.setVisibility(View.GONE);
    }
    private void sendDataForAuthentication() {
        this.itShouldLoadMore = false;
        RequestQueue queue = Volley.newRequestQueue(getActivity()); // this = context
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // load_all_questions.setVisibility(View.GONE);
                        recyclerView_all_question.setVisibility(View.VISIBLE);
                        hideProgressBar();
                        itShouldLoadMore = true;
                        parseData(response);

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        error.printStackTrace();
                        //  load_all_questions.setVisibility(View.GONE);

                        hideProgressBar();
                        recyclerView_all_question.setVisibility(View.GONE);
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
                params.put("get_answers_via_categories","true");
                params.put("customer_id",customers_id);
                params.put("category_name",get_category_name());
                params.put("start",getStart());
                params.put("limit",getLimit());
                return params;
            }

        };
        queue.add(stringRequest);
    }
    private void parseData(String response){
        try{


            JSONArray jsonArray = new JSONArray(response);
            String price,date,status,question_id,verification;
            int i=0;

           // Toast.makeText(getActivity(),jsonArray.length()+" dsfsad",Toast.LENGTH_SHORT).show();
           for(;i<jsonArray.length();i++){
                // Get current json object

                JSONObject category_questions = jsonArray.getJSONObject(i);
                String question = category_questions.getString("question_category_wise_description");
                date = category_questions.getString("question_category_time");
                status = category_questions.getString("question_status_text");
                question_id = category_questions.getString("question_id");
                price = category_questions.getString("question_category_price");



               this.all_answers_data.add(new All_answers_data(question,date,status,price,Integer.parseInt(question_id)));
                all_answer_adapter.notifyDataSetChanged();

            }

        }catch (JSONException e){
            e.printStackTrace();
        }

    }

}