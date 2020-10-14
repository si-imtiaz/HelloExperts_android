package com.HelloExperts.HelloExpert;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.HelloExperts.HelloExpert.util.NotificationUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.HelloExperts.HelloExpert.R;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.ViewGroup;

import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;


public class dashboard extends AppCompatActivity {
    public int version_code;
    private FirebaseAnalytics mFirebaseAnalytics;
    private AppBarConfiguration mAppBarConfiguration;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    public CircleImageView customers_profile_picture;
    private AsyncTask download_profile_image;
    private int customer_id;
    private boolean go_to_play_store;
    private String customers_tokken;
    private String HttpUrl;
    SharedPreferences dashboard_preference;
    private NavigationView navigationView;

    private String locked_question_id_if_exist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        HttpUrl = getResources().getString(R.string.about_yourself_api_url);

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



        dashboard_preference=this.getSharedPreferences("Login",0);
        if(!dashboard_preference.contains("not_now_update")) {
            getLatestVersionCode();
        }
         if(dashboard_preference.getBoolean("Splash_screen_displayed",false)){
             if (getIntent().getExtras() != null) {
                if (!getIntent().getExtras().containsKey("activity_name")) {
                    CheckIfAlreadySignInClass checkIfAlreadySignInClass = new CheckIfAlreadySignInClass(this,HttpUrl,dashboard_preference);
                    checkIfAlreadySignInClass.checkIfAlreadySignedIn(dashboard_preference);
                    return;
                }
             }else{
                 CheckIfAlreadySignInClass checkIfAlreadySignInClass = new CheckIfAlreadySignInClass(this,HttpUrl,dashboard_preference);
                 checkIfAlreadySignInClass.checkIfAlreadySignedIn(dashboard_preference);
                return;
             }
         }else{
            Intent splash = new Intent(this,Splash.class);
            splash.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(splash);
            return;
        }
         setContentView(R.layout.activity_dashboard);
         Toolbar toolbar = findViewById(R.id.toolbar);
        navigationView = (NavigationView)findViewById(R.id.nav_view);


        this.customer_id = dashboard_preference.getInt("customers_id",0);


        FirebaseCrashlytics.getInstance().setUserId(Integer.toString(this.customer_id));
        FirebaseApp.initializeApp(this);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();


                sendRegTokenToServer(Integer.toString(customer_id),newToken);
                Log.e("newToken",newToken);

            }
        });


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getExtras().getString("message");
                    Integer question_id = intent.getExtras().getInt("question_id");


                }

            }
        };
        displayFirebaseRegId();



        TypefaceUtils.overrideFont(getApplicationContext(), "SERIF", "fonts/Lato-Light.ttf");

        setSupportActionBar(toolbar);

        DrawerLayout drawer =(DrawerLayout)findViewById(R.id.drawer_layout);

        Menu menuNav = navigationView.getMenu();




        MenuItem complainsItem = menuNav.findItem(R.id.nav_complains);
        MenuItem nav_add_withdraw_payment_source = menuNav.findItem(R.id.nav_add_withdraw_payment_source);
        MenuItem logoutItem = menuNav.findItem(R.id.nav_sign_out);
        MenuItem lockedQuestionItem = menuNav.findItem(R.id.nav_locked_question);
        MenuItem RecentActivityItem = menuNav.findItem(R.id.nav_Recent_activity);



        TextView bottom_fixed_notification = (TextView) findViewById(R.id.bottom_fixed_notification);

        RelativeLayout bottom_fixed_notification_section = (RelativeLayout)findViewById(R.id.bottom_fixed_notification_section);








        complainsItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                startActivity(new Intent(getApplicationContext(),contact_us.class));
                return true;
            }
        });
        nav_add_withdraw_payment_source.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.withdraw_payment_url)));
                startActivity(browserIntent);
                return true;
            }
        });

        logoutItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("Login", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit(); // commit changes
                clearFromLog();

                return true;
            }
        });
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
              R.id.nav_new_questions, R.id.nav_Recent_activity,
                R.id.nav_all_questions, R.id.nav_all_answers,R.id.nav_locked_question, R.id.nav_suspension_policy,R.id.nav_expert_answering_guideline,R.id.nav_expert_faq,R.id.nav_alerts_setting)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        //////////////////////////////////////////////
        View nav_bar_layout = navigationView.inflateHeaderView(R.layout.nav_header_dashboard);
        TextView customers_name = (TextView)nav_bar_layout.findViewById(R.id.user_name);
        TextView customers_email_address = (TextView)nav_bar_layout.findViewById(R.id.user_email);
        TextView customers_pending_payment = (TextView)nav_bar_layout.findViewById(R.id.pending_payments);
        TextView experts_score = (TextView)nav_bar_layout.findViewById(R.id.experts_score);
        TextView customers_withdraw_payment = (TextView)nav_bar_layout.findViewById(R.id.withdraw_payments);
        TextView customers_withdraw_status = (TextView)nav_bar_layout.findViewById(R.id.withdraw_status);
        TextView withdraw_heading_and_date = (TextView)nav_bar_layout.findViewById(R.id.withdraw_heading_and_date);
        RelativeLayout withdraw_section = (RelativeLayout)nav_bar_layout.findViewById(R.id.withdraw_section);

        TextView customers_bonus_payment = (TextView)nav_bar_layout.findViewById(R.id.bonus_payments);


        RelativeLayout probation_answers_count_section = (RelativeLayout)nav_bar_layout.findViewById(R.id.probation_answers_count_section);
        TextView submitted_answers_count = (TextView)nav_bar_layout.findViewById(R.id.submitted_answers_count);
        TextView approved_answers_count = (TextView)nav_bar_layout.findViewById(R.id.approved_answers_count);
        String verification_status = dashboard_preference.getString("customers_verification_status",null);
        String expert_account_suspend_till_date = dashboard_preference.getString("expert_account_suspend_till_date",null);
        Integer expert_account_status = dashboard_preference.getInt("expert_account_status",0);
        String locked_question_id_if_exist_temp = dashboard_preference.getString("locked_question_id_if_exist",null);



        if(!expert_account_suspend_till_date.equalsIgnoreCase("false")){
            if(expert_account_status == 2){
                bottom_fixed_notification_section.setVisibility(View.VISIBLE);
                bottom_fixed_notification.setText("Account Suspended till: "+expert_account_suspend_till_date);
            }
        }
        if(!locked_question_id_if_exist_temp.equals("false")){
            setLocked_question_id_if_exist(locked_question_id_if_exist_temp);

            lockedQuestionItem.setVisible(true);
            lockedQuestionItem.setChecked(false);
            RecentActivityItem.setChecked(true);


        }


        if(verification_status.equals("2")){
            customers_pending_payment.setVisibility(View.GONE);
            experts_score.setVisibility(View.GONE);

            probation_answers_count_section.setVisibility(View.VISIBLE);

            Integer expert_probation_answers_count = dashboard_preference.getInt("expert_probation_answers_count",0);
            Integer expert_probation_submitted_count = dashboard_preference.getInt("expert_probation_submitted_count",0);
            Integer expert_probation_accepted_count = dashboard_preference.getInt("expert_probation_accepted_count",0);

            submitted_answers_count.setText("Submitted Answers:  "+expert_probation_submitted_count+"/"+expert_probation_answers_count);
            approved_answers_count.setText("Approved Answers:  "+expert_probation_accepted_count+"/"+expert_probation_answers_count);

        }else if(verification_status.equals("1")){
            customers_pending_payment.setTypeface(null,Typeface.BOLD);
            experts_score.setTypeface(null,Typeface.BOLD);
            customers_pending_payment.setText("Earned Money : "+ dashboard_preference.getString("customers_pending_payment",null));
            experts_score.setText("Expert Score : "+dashboard_preference.getString("experts_score",null)+"%");

            if(dashboard_preference.getBoolean("customers_withdraw_payment_flag",false)){

                customers_withdraw_payment.setTypeface(null, Typeface.BOLD);
                withdraw_heading_and_date.setTypeface(null, Typeface.BOLD);

                View view = nav_bar_layout.findViewById(R.id.nav_header_main_layout);
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

                layoutParams.height = 650 ;


                view.setLayoutParams(layoutParams);

                withdraw_section.setVisibility(View.VISIBLE);
                customers_withdraw_payment.setVisibility(View.VISIBLE);
                customers_withdraw_status.setVisibility(View.VISIBLE);
                withdraw_heading_and_date.setVisibility(View.VISIBLE);

                customers_withdraw_payment.setText("Earnings : "+
                        dashboard_preference.getString("customers_withdraw_payment",null));

                withdraw_heading_and_date.setText(dashboard_preference.getString("customers_withdraw_date_heading",null)+" "+
                        dashboard_preference.getString("customers_withdraw_datetime",null));
                customers_withdraw_status.setText(dashboard_preference.getString("customers_withdraw_status",null));

                if(dashboard_preference.getBoolean("customers_bonus_amount_flag",false)){

                    View view1 = nav_bar_layout.findViewById(R.id.nav_header_main_layout);
                    ViewGroup.LayoutParams layoutParams1 = view.getLayoutParams();
                    layoutParams.height =700 ;
                    view.setLayoutParams(layoutParams);

                    customers_bonus_payment.setTypeface(null,Typeface.BOLD);
                    customers_bonus_payment.setVisibility(View.VISIBLE);
                    customers_bonus_payment.setText("Bonus Amount : "+ dashboard_preference.getString("customers_bonus_amount",null));

                }

            }


        }


        customers_profile_picture = (CircleImageView)nav_bar_layout.findViewById(R.id.user_image);
        customers_name.setText("Hi "+dashboard_preference.getString("customers_name", null));
        customers_email_address.setText(dashboard_preference.getString("customers_email_address", null));

        String picture_path =dashboard_preference.getString("customers_profile_picture",null);
        String url_for_image = getResources().getString(R.string.expert_images_url);
        if(!picture_path.equals("")){

            url_for_image += picture_path;
            download_profile_image = new DownloadTask().execute(stringToURL(url_for_image));
        }



        if(dashboard_preference.contains("navigation_checked_item_id")){
            navigationView.getMenu().performIdentifierAction(dashboard_preference.getInt("navigation_checked_item_id",0),0);
        }


        lockedQuestionItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Bundle b = new Bundle();
                b.putInt("question_id",Integer.parseInt(getLocked_question_id_if_exist()));
                Intent directJumpToLockedQuestion = new Intent(getApplicationContext(),Answer_submission.class);
                directJumpToLockedQuestion.putExtras(b);
                startActivity(directJumpToLockedQuestion);
                return true;
            }
        });






}




    public void setLocked_question_id_if_exist(String locked_question_id_if_exist) {
        this.locked_question_id_if_exist = locked_question_id_if_exist;
    }

    public String getLocked_question_id_if_exist() {
        return locked_question_id_if_exist;
    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
    protected URL stringToURL(String urlString){
        try{
            URL url = new URL(urlString);
            return url;
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }

    private class DownloadTask extends AsyncTask<URL,Void,Bitmap> {
        // Before the tasks execution
        protected void onPreExecute(){
            // Display the progress dialog on async task start

        }
        // Do the task in background/non UI thread
        protected Bitmap doInBackground(URL...urls){
            URL url = urls[0];
            HttpURLConnection connection = null;

            try{
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
                return bmp;

            }catch(IOException e){
                e.printStackTrace();
            }finally{
                // Disconnect the http url connection
                connection.disconnect();
            }
            return null;
        }

        // When all async task done
        protected void onPostExecute(Bitmap result){

            if(result!=null){

                customers_profile_picture.setImageBitmap(result);
            }
        }
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
                params.put("customers_id", Integer.toString(customer_id));
                return params;
            }
        };
        queue.add(postRequest);
    }

    public void getLatestVersionCode(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext()); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            version_code = jsonObject.getInt("version_code");
                            if(version_code!= -1){
                                PackageInfo pinfo = null;

                                    pinfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
                                    int versionNumber = pinfo.versionCode;
                                    if(versionNumber < version_code){

                                        final Dialog temp = new Dialog(dashboard.this);
                                        temp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        temp.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        temp.setContentView(R.layout.need_udpate);
                                        temp.setCancelable(false);
                                        TextView not_now = (TextView)temp.findViewById(R.id.not_now);
                                        TextView update = (TextView)temp.findViewById(R.id.update);
                                        if(jsonObject.getInt("update_type") == 1){
                                            not_now.setVisibility(View.GONE);
                                        }else{
                                            not_now.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    temp.dismiss();
                                                    SharedPreferences sp=getSharedPreferences("Login", 0);
                                                    SharedPreferences.Editor Ed=sp.edit();
                                                    Ed.putBoolean("not_now_update",true);
                                                    Ed.commit();
                                                }
                                            });
                                        }
                                        update.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                                try {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                } catch (android.content.ActivityNotFoundException anfe) {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                                }
                                            }
                                        });
                                        temp.show();



                                    }

                            }


                        } catch (JSONException | PackageManager.NameNotFoundException e) {
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
                params.put("get_version_code", "true");
                return params;
            }
        };
        queue.add(postRequest);
    }


    public void sendRegTokenToServer(final String expert_id, final String reg_id){
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
                params.put("register_device_id", "true");
                params.put("customers_id", expert_id);
                params.put("device_token", reg_id);
                return params;
            }
        };
        queue.add(postRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);



        Log.d("dekho", "Firebase reg id: " + regId);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh_dashboard, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_dashboard:

                SharedPreferences sp=getSharedPreferences("Login", 0);
                SharedPreferences.Editor Ed=sp.edit();
                Ed.putInt("navigation_checked_item_id",navigationView.getCheckedItem().getItemId());
                Ed.commit();
               CheckIfAlreadySignInClass checkIfAlreadySignInClass = new CheckIfAlreadySignInClass(this,HttpUrl,dashboard_preference);
                checkIfAlreadySignInClass.checkIfAlreadySignedIn(this.dashboard_preference);


                // User chose the "Settings" item, show the app settings UI...

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }




}
