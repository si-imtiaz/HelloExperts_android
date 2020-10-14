package com.HelloExperts.HelloExpert.ui.alertSetting.alertSettings;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.HelloExperts.HelloExpert.HttpTrustManager;
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

public class AlertSettingsFragment extends Fragment {

    private AlertSettingsViewModel ViewModel;

    private TextView switch_heading;
    private Switch alert_switch;
    private SharedPreferences dashboard_sahred;
    View root;
    private String HttpUrl,customers_tokken,customers_id;
    private AVLoadingIndicatorView recent_loader;
    private LinearLayout main_loader_alert_notification_activity_bg;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ViewModel =
                ViewModelProviders.of(this).get(AlertSettingsViewModel.class);
        root = inflater.inflate(R.layout.fragment_alert_settings, container, false);


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
        Ed.putInt("navigation_checked_item_id",R.id.nav_alerts_setting);
        Ed.commit();

        assignLocalVariables();

        authenticateUserProgressDialogue();
        getAlertStatus();
        allOnClickFunctions();


        return root;
    }
    public void assignLocalVariables() {

        this.HttpUrl = getResources().getString(R.string.about_yourself_api_url);
        this.dashboard_sahred = getActivity().getSharedPreferences("Login", 0);
        int temp_id = this.dashboard_sahred.getInt("customers_id", 0);
        customers_tokken = this.dashboard_sahred.getString("customers_tokken", null);
        customers_id = Integer.toString(temp_id);
        switch_heading = (TextView) root.findViewById(R.id.switch_heading);
        switch_heading.setTypeface(null, Typeface.BOLD);
        alert_switch = (Switch) root.findViewById(R.id.alert_switch);
        this.main_loader_alert_notification_activity_bg = (LinearLayout)root.findViewById(R.id.main_loader_alert_notification_activity_bg);
        this.recent_loader = (AVLoadingIndicatorView)root.findViewById(R.id.recent_loader);
        recent_loader.show();
    }

    private void allOnClickFunctions(){
        alert_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) //Line A
            {
                if(isChecked){
                    authenticateUserProgressDialogue();
                    updateExpertAlertNotificationStatus("true");
                }else{
                    authenticateUserProgressDialogue();
                    updateExpertAlertNotificationStatus("false");
                }
            }
        });
    }
    private void authenticateUserProgressDialogue(){

        this.main_loader_alert_notification_activity_bg.setVisibility(View.VISIBLE);

    }

    private void hideProgressBar(){

        this.main_loader_alert_notification_activity_bg.setVisibility(View.GONE);
    }

    public void getAlertStatus(){

        RequestQueue queue = Volley.newRequestQueue(getActivity()); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    hideProgressBar();
                   try{
                        JSONObject jsonObject = new JSONObject(response);

                        if(jsonObject.getString("status").equals("checked")){
                            alert_switch.setChecked(true);
                        }else if(jsonObject.getString("status").equals("unchecked")){
                            alert_switch.setChecked(false);
                        }

                    } catch (
                    JSONException e) {
                        e.printStackTrace();
                    }


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity(),"Error : Something went wrong. Please try again",Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("getAlertStatus", "true");
                params.put("customers_id", customers_id);
                return params;
            }
        };
        queue.add(postRequest);
    }
    public void updateExpertAlertNotificationStatus(final String check_status){

        RequestQueue queue = Volley.newRequestQueue(getActivity()); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        hideProgressBar();

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity(),"Error : Something went wrong. Please try again",Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("update_alert_status", "true");
                params.put("check_status", check_status);
                params.put("customers_id", customers_id);
                return params;
            }
        };
        queue.add(postRequest);
    }

}