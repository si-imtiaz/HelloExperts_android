package com.HelloExperts.HelloExpert;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CheckIfAlreadySignInClass {

    SharedPreferences sp1;
    private int customer_id;
    private String customers_tokken;
    private String HttpUrl;
    Context ctx;

    public CheckIfAlreadySignInClass(Context ctx,String HttpUrl,SharedPreferences sp1_shared){
        this.ctx = ctx;
        this.HttpUrl=HttpUrl;
        this.sp1 = sp1_shared;

    }

    public void checkIfAlreadySignedIn(final SharedPreferences sp1_shared){
        final String[] expected_date = new String[1];

        customer_id=sp1.getInt("customers_id", 0);
        customers_tokken = sp1.getString("customers_tokken", null);

        if(customers_tokken!=null){

            RequestQueue queue = Volley.newRequestQueue(this.ctx); // this = context
            StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                final JSONObject jsonObject = new JSONObject(response);


                                if(jsonObject.getString("login_status").equals("not_verified")) {
                                    int completion_level = jsonObject.getInt("completion_level");
                                    if(completion_level == 5){
                                        expected_date[0] = jsonObject.getString("expected_date");
                                    }
                                    if(completion_level == 0){


                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                ctx.startActivity(new Intent(ctx,Basic_information.class));
                                            }
                                        }, 2000);


                                    }else if(completion_level == 1){


                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                ctx.startActivity(new Intent(ctx,Education_experience.class));
                                            }
                                        }, 2000);


                                    }else if(completion_level == 2){


                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                ctx.startActivity(new Intent(ctx,Add_skills.class));
                                            }
                                        }, 2000);


                                    }else if(completion_level == 3){


                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                ctx.startActivity(new Intent(ctx,Education_verification.class));
                                            }
                                        }, 2000);


                                    }else if(completion_level == 4){


                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                ctx.startActivity(new Intent(ctx,About_yourself.class));
                                            }
                                        }, 2000);


                                    }else if(completion_level == 5){


                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                Bundle b = new Bundle();
                                                b.putString("expected_date", expected_date[0]);
                                                Intent finish_intent= new Intent(ctx.getApplicationContext(),Sign_up_finish.class);
                                                finish_intent.putExtras(b);
                                                ctx.startActivity(finish_intent);
                                            }
                                        }, 2000);


                                    }

                                }else if(jsonObject.getString("login_status").equals("true")){


                                    SharedPreferences sp= sp1_shared;
                                    SharedPreferences.Editor Ed=sp.edit();

                                    String response_string = jsonObject.getString("customers_id");
                                    int customers_id = Integer.parseInt(response_string);

                                    String customers_name = jsonObject.getString("customers_name");
                                    String customers_email_address = jsonObject.getString("customers_email_address");
                                    String customers_profile_picture = jsonObject.getString("customers_profile_picture");

                                    String customers_tokken = jsonObject.getString("customers_tokken");
                                    String customers_verification_status = jsonObject.getString("customers_verification_status");
                                    Integer expert_account_status = jsonObject.getInt("expert_account_status");
                                    String expert_account_suspend_till_date = jsonObject.getString("expert_account_suspend_till_date");
                                    String locked_question_id_if_exist = jsonObject.getString("locked_question_id_if_exist");


                                    if(customers_verification_status.equals("1")){
                                        String customers_pending_payment = jsonObject.getString("customers_pending_payment");
                                        String experts_score = jsonObject.getString("experts_score");

                                        Boolean customers_withdraw_payment_flag = jsonObject.getBoolean("customers_withdraw_payment_flag");
                                        String customers_withdraw_payment = jsonObject.getString("customers_withdraw_payment");
                                        String customers_withdraw_status = jsonObject.getString("customers_withdraw_status");
                                        String customers_withdraw_date_heading = jsonObject.getString("customers_withdraw_date_heading");
                                        String customers_withdraw_datetime = jsonObject.getString("customers_withdraw_datetime");
                                        Boolean customers_bonus_amount_flag = jsonObject.getBoolean("customers_bonus_amount_flag");
                                        String customers_bonus_amount = jsonObject.getString("customers_bonus_amount");

                                        Ed.putString("customers_pending_payment",customers_pending_payment);
                                        Ed.putString("experts_score",experts_score);

                                        Ed.putBoolean("customers_withdraw_payment_flag",customers_withdraw_payment_flag);
                                        Ed.putString("customers_withdraw_payment",customers_withdraw_payment);
                                        Ed.putString("customers_withdraw_status",customers_withdraw_status);
                                        Ed.putString("customers_withdraw_date_heading",customers_withdraw_date_heading);
                                        Ed.putString("customers_withdraw_datetime",customers_withdraw_datetime);
                                        Ed.putBoolean("customers_bonus_amount_flag",customers_bonus_amount_flag);
                                        Ed.putString("customers_bonus_amount",customers_bonus_amount);




                                    }else if(customers_verification_status.equals("2")){
                                        Integer expert_probation_answers_count = jsonObject.getInt("expert_probation_answers_count");
                                        Integer expert_probation_submitted_count = jsonObject.getInt("expert_probation_submitted_count");
                                        Integer expert_probation_accepted_count = jsonObject.getInt("expert_probation_accepted_count");
                                        Ed.putInt("expert_probation_answers_count",expert_probation_answers_count);
                                        Ed.putInt("expert_probation_submitted_count",expert_probation_submitted_count);
                                        Ed.putInt("expert_probation_accepted_count",expert_probation_accepted_count);
                                    }

                                    Ed.putBoolean("Splash_screen_displayed",true);
                                    Ed.putString("locked_question_id_if_exist",locked_question_id_if_exist);
                                    Ed.putString("expert_account_suspend_till_date",expert_account_suspend_till_date);
                                    Ed.putInt("expert_account_status",expert_account_status);
                                    Ed.putString("customers_email_address",customers_email_address);
                                    Ed.putString("customers_name",customers_name);
                                    Ed.putString("customers_profile_picture",customers_profile_picture);
                                    Ed.putInt("customers_id",customers_id);
                                    Ed.putString("customers_tokken",customers_tokken);
                                    Ed.putString("customers_verification_status",customers_verification_status);

                                    if(ctx.getClass().getSimpleName().equals("Final_answer_preview")){
                                        Ed.putBoolean("check_feed_back_flag",true);
                                    }else{
                                        Ed.putBoolean("check_feed_back_flag",false);
                                    }

                                    Ed.commit();

                                    Intent answer_submission = new Intent(ctx,dashboard.class);
                                    Bundle b = new Bundle();
                                    answer_submission.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    b.putString("activity_name","in_session");
                                    answer_submission.putExtras(b);
                                    ctx.startActivity(answer_submission);



                                }else if(jsonObject.getString("login_status").equals("false")){
                                    Intent redirect_to_login = new Intent(ctx,login.class);
                                    ctx.startActivity(redirect_to_login);
                                    ((Activity) ctx).finish();

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


                            Toast.makeText(ctx,"Error : No Internet connection",Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("customers_id",Integer.toString(customer_id));
                    params.put("customers_tokken", customers_tokken);
                    params.put("check_if_already_in_session", "true");


                    return params;
                }
            };
            queue.add(postRequest);
            postRequest.setRetryPolicy(new DefaultRetryPolicy(20000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        }else{

            Intent redirect_to_login = new Intent(ctx,login.class);
            ctx.startActivity(redirect_to_login);
            ((Activity) ctx).finish();
            Toast.makeText(ctx,"No credential saved",Toast.LENGTH_LONG).show();


        }


    }
}
