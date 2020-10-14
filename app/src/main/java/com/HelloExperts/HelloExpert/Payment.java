package com.HelloExperts.HelloExpert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.HelloExperts.HelloExpert.R;

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


public class Payment extends AppCompatActivity {
    Bundle activity_status;
    private String activity_status_name;
    private ProgressDialog progressDialog;
    private TextView section_name;
    EditText paypal_name,paypal_email;
    private String  HttpUrl ;
    TextView continue_to_add_payment_method;
    LinearLayout add_payment_method_section,view_withdraw_payment_methods_section;
    private RecyclerView withdraw_methods_recycler_view;
    SharedPreferences dashboard_preference;
    private RecyclerView.Adapter withdraw_payment_methods_adapter;
    private List<Withdraw_methods_list_data> complete_list = new ArrayList<>();
    private String customers_id;
    Dialog error_msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        dashboard_preference=getApplicationContext().getSharedPreferences("Login",0);
        int temp_id = dashboard_preference.getInt("customers_id",0);
        //   customers_tokken = dashboard_preference.getString("customers_tokken",null);
        customers_id = Integer.toString(temp_id);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        HttpUrl = getResources().getString(R.string.about_yourself_api_url);
        error_msg = new Dialog(this);


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


        activity_status = getIntent().getExtras();
        activity_status_name = activity_status.getString("activity");

        section_name = (TextView)findViewById(R.id.section_name);
        continue_to_add_payment_method = (TextView) findViewById(R.id.continue_to_add_payment_method);
        add_payment_method_section = (LinearLayout) findViewById(R.id.add_payment_method_section);
        view_withdraw_payment_methods_section =(LinearLayout)findViewById(R.id.view_withdraw_payment_methods_section);
        paypal_email = (EditText)findViewById(R.id.paypal_email);
        paypal_name = (EditText)findViewById(R.id.paypal_account_name);


        if(activity_status_name.equals("add_withdraw_method")){

        }else if(activity_status_name.equals("my_withdraw_method")){

            add_payment_method_section.setVisibility(View.GONE);
            view_withdraw_payment_methods_section.setVisibility(View.VISIBLE);

            withdraw_methods_recycler_view =(RecyclerView)findViewById(R.id.withdraw_methods_recycler_view);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            withdraw_methods_recycler_view.setLayoutManager(manager);
            withdraw_methods_recycler_view.setHasFixedSize(true);
            authenticateUserProgressDialogue();
            getDataForWithdrawalMethod();

        }else if(activity_status_name.equals("withdraw_pending_earning")){



        }else if(activity_status_name.equals("my_withdraw_history")){


        }

        continue_to_add_payment_method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                authenticateUserProgressDialogue();
                sendDataForAddingWithdrawalMethod();

            }
        });


    }
    private void authenticateUserProgressDialogue(){
        progressDialog.setTitle("Authenticate User...");
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMax(100);
        progressDialog.show();

    }
    private  void sendDataForAddingWithdrawalMethod(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                           if(jsonObject.getString("type").equals("false")){
                                error_msg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                error_msg.setContentView(R.layout.error_alert);
                                TextView btn_ok=(TextView)error_msg.findViewById(R.id.btn_ok);
                                TextView message=(TextView)error_msg.findViewById(R.id.message);
                                message.setText(jsonObject.getString("message"));
                                error_msg.show();
                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        error_msg.dismiss();
                                    }
                                });
                            }else if(jsonObject.getString("type").equals("true")){
                               Bundle activity = new Bundle();
                               activity.putString("activity","my_withdraw_method");
                               Intent payment_intent = new Intent(getApplicationContext(),Payment.class);
                               payment_intent.putExtras(activity);
                               startActivity(payment_intent);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        error.printStackTrace();
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Error: Something went wrong. Please try again.",Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String,String>();

                params.put("add_payment_method","true");
                params.put("customers_id",customers_id);
                params.put("paypal_email",paypal_email.getText().toString());
                params.put("paypal_name",paypal_name.getText().toString());
                return params;
            }

        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private  void getDataForWithdrawalMethod(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if(jsonArray.length() > 0){
                                for(int i =0;i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String account_id = jsonObject.getString("account_id");
                                    String account_name = jsonObject.getString("Account_title");
                                    String source_name = jsonObject.getString("Source_Name");
                                    String paypal_email = jsonObject.getString("PayPal_Email");
                                    String status = jsonObject.getString("status");
                                    Withdraw_methods_list_data withdraw_methods_list_data1 =
                                            new Withdraw_methods_list_data(Integer.parseInt(account_id),account_name,source_name,paypal_email,status);
                                    complete_list.add(withdraw_methods_list_data1);
                                    withdraw_payment_methods_adapter = new Withdraw_methods_list_adapter(complete_list);
                                    withdraw_methods_recycler_view.setAdapter(withdraw_payment_methods_adapter);

                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        error.printStackTrace();
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Error: Something went wrong. Please try again.",Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String,String>();
                params.put("view_withdraw_method","true");
                params.put("customers_id",customers_id);
                return params;
            }

        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
