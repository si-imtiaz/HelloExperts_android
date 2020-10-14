package com.HelloExperts.HelloExpert;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Complains extends AppCompatActivity {
    private int customers_id;
    private String customers_email_address;
    TextView email_for_complaint,submit_complaint;
    ScrollView send_complain_section;
    LinearLayout after_submission_complaint;
    TextView user_complaint_ticket_id;
    public ProgressDialog progressDialog;
    private String HttpUrl;
    private Pattern emailPattern;
    private String regularExpressionForEmail;
    EditText subject_for_complaint,description_for_complaint;
    LinearLayout main_loader_complaints_bg;
    AVLoadingIndicatorView recent_loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complains);


        assignLocalVariables();
        submit_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email_for_complaint.getText().toString().trim().equals("")){
                    email_for_complaint.setError("Email Required");
                }else if(!emailPattern.matcher(email_for_complaint.getText().toString()).matches()){
                    email_for_complaint.setError("Enter Valid email");
                }else if(subject_for_complaint.getText().toString().trim().equals("")){
                    subject_for_complaint.setError("Subject Required");
                }else if(subject_for_complaint.getText().toString().length() < 5){
                    subject_for_complaint.setError("Complaint subject cannot be less than 5 characters");
                }else if(description_for_complaint.getText().toString().trim().equals("")){
                    description_for_complaint.setError("Description Required");
                } else if(description_for_complaint.getText().toString().length() < 50){
                    description_for_complaint.setError("Description cannot be less than 50 character");
                }else{
                    submit_complaint.setVisibility(View.GONE);
                    send_complain_section.setVisibility(View.GONE);
                    after_submission_complaint.setVisibility(View.VISIBLE);
                    authenticateUserProgressDialogue();
                    sendDataForAuthentication();
                }


            }
        });
        SharedPreferences sp1=this.getSharedPreferences("Login",0);
        customers_id=sp1.getInt("customers_id", 0);
        if(Integer.toString(customers_id)!=null){
            customers_email_address=sp1.getString("customers_email_address", null);
            email_for_complaint.setText(customers_email_address);

        }


    }
    private void assignLocalVariables() {
        HttpUrl = getResources().getString(R.string.about_yourself_api_url);
        progressDialog = new ProgressDialog(this);
        description_for_complaint = (EditText)findViewById(R.id.description_for_complaint);
        subject_for_complaint = (EditText)findViewById(R.id.subject_for_complaint);
        email_for_complaint = (TextView)findViewById(R.id.email_for_complaint);
        submit_complaint = (TextView) findViewById(R.id.submit_complaint);
        send_complain_section = (ScrollView)findViewById(R.id.send_complain_section);
        after_submission_complaint = (LinearLayout)findViewById(R.id.after_submission_complaint);
        user_complaint_ticket_id = (TextView)findViewById(R.id.user_complaint_ticket_id);
        regularExpressionForEmail = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        emailPattern = Pattern.compile(regularExpressionForEmail);

        main_loader_complaints_bg = (LinearLayout)findViewById(R.id.main_loader_complaints_bg);
        recent_loader = (AVLoadingIndicatorView)findViewById(R.id.recent_loader);
        recent_loader.show();

    }
    private void authenticateUserProgressDialogue(){

        this.main_loader_complaints_bg.setVisibility(View.VISIBLE);

    }

    private void hideProgressBar(){

        this.main_loader_complaints_bg.setVisibility(View.GONE);
    }
    private void sendDataForAuthentication() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext()); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            user_complaint_ticket_id.setText("ticket id is\n"+jsonObject.getString("ticket_id"));


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
                params.put("complaint_email", email_for_complaint.getText().toString().trim());
                params.put("complaint_subject", subject_for_complaint.getText().toString().trim());
                params.put("complaint_description",description_for_complaint.getText().toString().trim());
                params.put("submit_complaint","true");
                return params;
            }
        };
        queue.add(postRequest);
    }

}
