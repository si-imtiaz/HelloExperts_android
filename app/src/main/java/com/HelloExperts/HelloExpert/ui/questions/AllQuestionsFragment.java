package com.HelloExperts.HelloExpert.ui.questions;

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
import android.widget.RelativeLayout;
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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.HelloExperts.HelloExpert.Categories_data;
import com.HelloExperts.HelloExpert.MySingleton;
import com.HelloExperts.HelloExpert.Question_Categories_data;
import com.HelloExperts.HelloExpert.Questions_categories_adapter;
import com.HelloExperts.HelloExpert.R;
import com.HelloExperts.HelloExpert.login;
import com.innovattic.rangeseekbar.RangeSeekBar;
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

public class AllQuestionsFragment extends Fragment {

    private String start,limit,customer_currency;
    public RangeSeekBar rangeSeekBar;
    public RelativeLayout range_labels,filter_section;
    public int min_p,max_p;

    public Boolean getPrice_flag() {
        return price_flag;
    }

    public void setPrice_flag(Boolean price_flag) {
        this.price_flag = price_flag;
    }

    public Boolean price_flag;


    public int min;

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int max;
    TextView Left_label,Right_label;
    private AllQuestionsViewModel ViewModel;
    private SwipeRefreshLayout refresh_all_questions;
    private  String HttpUrl;
    public String getOrder_by() {
        return order_by;
    }

    public void setOrder_by(String order_by) {
        this.order_by = order_by;
    }

    private String order_by;

    private  List<String> temp_list;
    private View root;

    public String customers_id,customers_verification_status;
    private Button try_again;
    private TextView no_internet_text,no_record_text;
    private ImageView no_internet_symbol,sort_asc,sort_desc;
    private RecyclerView recyclerView_all_question;
    private SharedPreferences dashboard_preference;
    private ImageView no_record_found;
    private RecyclerView.Adapter adapter;
    private Questions_categories_adapter questions_categories_adapter;
    private ArrayList<Question_Categories_data> question_categories_data;
    private String category_name;
    public int all_project_count;
    private MaterialSpinner question_categories;
    private Boolean itShouldLoadMore;
    public String customers_tokken;
    private LinearLayout main_loader_all_questions_activity_bg;
    private AVLoadingIndicatorView recent_loader;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ViewModel =
                ViewModelProviders.of(this).get(AllQuestionsViewModel.class);
        root = inflater.inflate(R.layout.fragment_all_questions, container, false);

        SharedPreferences sp=this.getActivity().getSharedPreferences("Login", 0);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.putInt("navigation_checked_item_id",R.id.nav_all_questions);
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
        this.recyclerView_all_question.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView_all_question.setHasFixedSize(true);
        this.recyclerView_all_question.setAdapter(this.questions_categories_adapter);

        authenticateUserProgressDialogue();
        getQuestionCategories(false);
        getMinMaxPrices();

        setStart("0");
        setLimit("10");
        sendDataForAuthentication(false);
        recyclerView_all_question.addOnScrollListener(new RecyclerView.OnScrollListener() {

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {

                        if (itShouldLoadMore) {
                            setStart(Integer.toString(Integer.parseInt(getLimit())+Integer.parseInt(getStart())));
                            setLimit(getLimit());
                            authenticateUserProgressDialogue();
                            if(getPrice_flag()){
                                sendDataForAuthentication(true);
                            }else{
                                sendDataForAuthentication(false);
                            }
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
                
                question_categories_data = new ArrayList<>();
                questions_categories_adapter = new Questions_categories_adapter(question_categories_data);
                recyclerView_all_question.setAdapter(questions_categories_adapter);
                sendDataForAuthentication(false);


            }
        });

        sort_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticateUserProgressDialogue();
                setOrder_by("desc");
                setStart("0");
                setLimit("10");
                question_categories_data = new ArrayList<>();
                questions_categories_adapter = new Questions_categories_adapter(question_categories_data);
                recyclerView_all_question.setAdapter(questions_categories_adapter);
                if(getPrice_flag()){
                    sendDataForAuthentication(true);
                }else{
                    sendDataForAuthentication(false);
                }
            }
        });
        sort_asc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticateUserProgressDialogue();
                setOrder_by("asc");
                setStart("0");
                setLimit("10");
                question_categories_data = new ArrayList<>();
                questions_categories_adapter = new Questions_categories_adapter(question_categories_data);
                recyclerView_all_question.setAdapter(questions_categories_adapter);
                if(getPrice_flag()){
                    sendDataForAuthentication(true);
                }else{
                    sendDataForAuthentication(false);
                }

            }
        });
        try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ftr = getFragmentManager().beginTransaction();
                ftr.detach(AllQuestionsFragment.this).attach(AllQuestionsFragment.this).commit();
            }
        });
        refresh_all_questions.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                authenticateUserProgressDialogue();
                temp_list.clear();
                getQuestionCategories(false);
                setStart("0");
                setLimit("10");

                question_categories.setItems(temp_list);
                question_categories_data = new ArrayList<>();
                questions_categories_adapter = new Questions_categories_adapter(question_categories_data);
                recyclerView_all_question.setAdapter(questions_categories_adapter);
                sendDataForAuthentication(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh_all_questions.setRefreshing(false);
                    }
                },3000);
            }
        });

        this.rangeSeekBar.setSeekBarChangeListener(new RangeSeekBar.SeekBarChangeListener() {
            @Override
            public void onStartedSeeking() {

            }

            @Override
            public void onStoppedSeeking() {
                setPrice_flag(true);

                authenticateUserProgressDialogue();
                temp_list.clear();
                getQuestionCategories(true);
                setStart("0");
                setLimit("10");

                question_categories.setItems(temp_list);
                question_categories_data = new ArrayList<>();
                questions_categories_adapter = new Questions_categories_adapter(question_categories_data);
                recyclerView_all_question.setAdapter(questions_categories_adapter);
                sendDataForAuthentication(true);

            }

            @Override
            public void onValueChanged(int i, int i1) {

                int value_min = min_p +i;
                Left_label.setText(customer_currency+" "+value_min);
                setMin(value_min);


                if(i1 <= (max_p-min_p)) {
                    int value_max = min_p + i1;
                    Right_label.setText(customer_currency+" "+value_max);

                    setMax(value_max);
                }

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
        this.setPrice_flag(false);
        setMin(0);
        setMax(0);
        this.min_p = 0;
        this.max_p = 0;
        this.recyclerView_all_question  = (RecyclerView)root.findViewById(R.id.all_questions_recyclerView);
        this.rangeSeekBar = (RangeSeekBar)root.findViewById(R.id.rangeSeekBar);
        this.Left_label = (TextView)root.findViewById(R.id.Left_label);
        this.Right_label = (TextView)root.findViewById(R.id.Right_label);

        this.HttpUrl = getResources().getString(R.string.about_yourself_api_url);
        this.question_categories = (MaterialSpinner)root.findViewById(R.id.question_categories);
        this.temp_list = new ArrayList<>();

        this.all_project_count = 0;
        this.try_again = (Button)root.findViewById(R.id.try_again);
        this.no_internet_symbol=(ImageView)root.findViewById(R.id.no_connection_symbol);
        this.no_internet_text = (TextView)root.findViewById(R.id.no_connection_text);
        this.dashboard_preference=getActivity().getSharedPreferences("Login",0);
        int temp_id = dashboard_preference.getInt("customers_id",0);
        customers_tokken = dashboard_preference.getString("customers_tokken",null);
        customers_verification_status = dashboard_preference.getString("customers_verification_status",null);
        this.customers_id = Integer.toString(temp_id);
        this.refresh_all_questions = (SwipeRefreshLayout)root.findViewById(R.id.refresh_all_questions);
        this.category_name = "All Categories";
        this.question_categories_data = new ArrayList<>();
        this.questions_categories_adapter = new Questions_categories_adapter(this.question_categories_data);
        this.itShouldLoadMore = true;
        this.start = "0";
        this.limit = "10";
        main_loader_all_questions_activity_bg = (LinearLayout)root.findViewById(R.id.main_loader_all_questions_activity_bg);
        recent_loader= (AVLoadingIndicatorView)root.findViewById(R.id.recent_loader);
        recent_loader.show();
        // load_all_questions = (ProgressBar)root.findViewById(R.id.load_all_questions);
        this.sort_asc = (ImageView)root.findViewById(R.id.sort_asc);
        this.sort_desc = (ImageView)root.findViewById(R.id.sort_desc);
        this.no_record_found = (ImageView)root.findViewById(R.id.no_record_found);
        this.no_record_text = (TextView)root.findViewById(R.id.no_record_text);
        this.range_labels = (RelativeLayout) root.findViewById(R.id.range_labels);
        this.filter_section = (RelativeLayout) root.findViewById(R.id.filter_section);
        this.setOrder_by("-1");
        if(dashboard_preference.contains("customers_withdraw_payment")){
            String[] each_string = dashboard_preference.getString("customers_withdraw_payment",null).split(" ");
            this.customer_currency = each_string[each_string.length-1];
            this.filter_section.setVisibility(View.VISIBLE);
            this.range_labels.setVisibility(View.VISIBLE);
            this.rangeSeekBar.setVisibility(View.VISIBLE);
        }else{
            this.customer_currency = "";
            this.filter_section.setVisibility(View.GONE);
            this.range_labels.setVisibility(View.GONE);
            this.rangeSeekBar.setVisibility(View.GONE);
        }
        this.Left_label.setText(this.customer_currency+" "+getMin());
        this.Right_label.setText(this.customer_currency+" "+getMax());

    }
    private void authenticateUserProgressDialogue(){
        this.main_loader_all_questions_activity_bg.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){

        this.main_loader_all_questions_activity_bg.setVisibility(View.GONE);
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

    private void getQuestionCategories(final Boolean price_flag){
        customers_tokken = "";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();

                        try{
                            all_project_count =0;
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
                            }else {
                             hideProgressBar();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    all_project_count += Integer.parseInt(jsonArray.getJSONObject(i).getString("category_count"));
                                }
                                Categories_data categories_data = new Categories_data("All Categories", all_project_count, 0);
                                temp_list.add(categories_data.getCategory_name() + " (" + categories_data.getCategory_count() + ")");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject student = jsonArray.getJSONObject(i);
                                    String question_category_name = student.getString("category_name");
                                    String question_category_count = student.getString("category_count");
                                    String question_category_id = student.getString("category_id");
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
                params.put("get_questions_categories","true");
                params.put("customers_tokken",customers_tokken);
                if(price_flag){
                    params.put("min",Integer.toString(getMin()));
                    params.put("max",Integer.toString(getMax()));
                }
                return params;
            }

        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    private void getMinMaxPrices(){



        customers_tokken = "";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();

                        try{

                            JSONObject jsonObject = new JSONObject(response);
                            min_p = jsonObject.getInt("min");
                            max_p = jsonObject.getInt("max");
                            Left_label.setText(customer_currency+" "+min_p);
                            Right_label.setText(customer_currency+" "+max_p);
                            rangeSeekBar.setMax((max_p-min_p)/1);

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
                params.put("get_min_max","true");
                params.put("customers_tokken",customers_tokken);
                return params;
            }

        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    private void sendDataForAuthentication(final boolean price_flag) {

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
                params.put("get_questions_via_categories","true");
                params.put("customer_id",customers_id);
                params.put("category_name", get_category_name());
                if(price_flag){
                    params.put("min",Integer.toString(getMin()));
                    params.put("max",Integer.toString(getMax()));
                }
                if(!getOrder_by().equals("-1")){
                    params.put("order_by",getOrder_by());
                }
                params.put("start",getStart());
                params.put("limit",getLimit());
                return params;
            }

        };
        queue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    private void parseData(String response){
        try{


            JSONArray jsonArray = new JSONArray(response);

            int i=0;

            if(jsonArray.length() > 0) {
                this.no_record_found.setVisibility(View.GONE);
                this.no_record_text.setVisibility(View.GONE);
                for (; i < jsonArray.length(); i++) {
                    // Get current json object

                    JSONObject category_questions = jsonArray.getJSONObject(i);

                    String question = category_questions.getString("question_category_wise_description");

                    String date = category_questions.getString("question_category_time");

                    String price = category_questions.getString("question_category_price");

                    if (customers_verification_status.equals("2")) {
                        price = " ";
                    }

                    String skill = category_questions.getString("question_category_name");
                    String question_id = category_questions.getString("question_id");

                    this.question_categories_data.add(new Question_Categories_data(question, date, price, skill, Integer.parseInt(question_id)));
                    questions_categories_adapter.notifyDataSetChanged();

                }



            }else{

                this.recyclerView_all_question.setVisibility(View.GONE);
                this.no_record_found.setVisibility(View.VISIBLE);
                this.no_record_text.setVisibility(View.VISIBLE);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

    }

}