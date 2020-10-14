package com.HelloExperts.HelloExpert;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.HelloExperts.HelloExpert.R;

import java.util.HashMap;
import java.util.Map;

public class Congrats extends AppCompatActivity {
    TextView go_to_dashboard;
    ImageView sign_out_from_page_vi;
    SharedPreferences dashboard_preference;
    private  int customers_id;
    String HttpUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congrats);




        go_to_dashboard = (TextView)findViewById(R.id.go_to_dashboard);
        sign_out_from_page_vi = (ImageView) findViewById(R.id.sign_out_from_page_vi);
        this.dashboard_preference=getApplicationContext().getSharedPreferences("Login",0);
        this.customers_id = dashboard_preference.getInt("customers_id",0);
        HttpUrl = getResources().getString(R.string.about_yourself_api_url);

        sign_out_from_page_vi.setOnClickListener(new View.OnClickListener() {
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

        go_to_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckIfAlreadySignInClass checkIfAlreadySignInClass = new CheckIfAlreadySignInClass(Congrats.this,HttpUrl,dashboard_preference);
                checkIfAlreadySignInClass.checkIfAlreadySignedIn(dashboard_preference);
            }
        });
    }
    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh_sign_up_completion, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_completion:

                CheckIfAlreadySignInClass checkIfAlreadySignInClass = new CheckIfAlreadySignInClass(this,HttpUrl,dashboard_preference);
                checkIfAlreadySignInClass.checkIfAlreadySignedIn(dashboard_preference);


                // User chose the "Settings" item, show the app settings UI...

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


}
