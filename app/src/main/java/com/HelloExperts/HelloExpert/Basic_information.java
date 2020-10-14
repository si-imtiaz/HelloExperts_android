package com.HelloExperts.HelloExpert;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;

import android.app.Dialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;


import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Base64;

import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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

import java.io.ByteArrayOutputStream;

import java.io.File;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class Basic_information extends AppCompatActivity {
    TextView country_calling_code,upload_image_reg_btn,save_and_continue_to_ii,btn_ok,message;
    EditText first_name,last_name,address,zip_postal_code,mobile_number;
    MaterialSpinner user_country,user_finding_resources;
    CircleImageView image_reg;
    List<String> countries_list;
    boolean is_image_uploaded;
    List<String> countries_code_list;
    List<String> countries_id_list;
    List<String> country_state_list;
    List<String> state_city_list;
    RequestQueue queue;
    Bitmap bitmap_profile,bitmap_profile_39,bitmap_profile_90,bitmap_profile_240;
    ImageView sign_out_from_page_i;
    public String HttpUrl;
    ArrayAdapter<String> adapter_state,adapter_city;
    AutoCompleteTextView state,city;
    SharedPreferences dashboard_preference;
    LinearLayout main_loader_basic_info_bg;
    AVLoadingIndicatorView recent_loader;
    int customers_id;

    private String current_country_id;
    Bundle for_this_intent;


    Dialog error_msg ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_information);

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
               assignLocalvariables();
        getCountriesList();


        sign_out_from_page_i.setOnClickListener(new View.OnClickListener() {
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
        save_and_continue_to_ii.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Checkconnectivity checkconnectivity = new Checkconnectivity();
                if(checkconnectivity.hasInternetConnection(getApplicationContext())){
                    if(checkValidations()){
                        authenticateUserProgressDialogue();
                        getAllInputAndSend();
                    }
                }else{
                    Toast.makeText(Basic_information.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });


        user_country.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                country_calling_code.setText("+"+countries_code_list.get(position));
                setCurrent_country_id(countries_id_list.get(position));

                country_state_list.clear();
                getStatesList(getCurrent_country_id());

            }
        });
        state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                state_city_list.clear();
               getCitiesList(adapterView.getItemAtPosition(i).toString());
            }
        });
        upload_image_reg_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                Checkconnectivity checkconnectivity = new Checkconnectivity();
                if(checkconnectivity.hasInternetConnection(getApplicationContext())){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                }else{
                    Toast.makeText(Basic_information.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }



            }
        });

        this.grantExternalPermission();
    }
    public void grantExternalPermission(){

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
    public String getCurrent_country_id() {
        return current_country_id;
    }
    public void setCurrent_country_id(String current_country_id) {
        this.current_country_id = current_country_id;
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Checkconnectivity checkconnectivity = new Checkconnectivity();
        if (!checkconnectivity.hasInternetConnection(getApplicationContext())) {

            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        } else {

            if (requestCode == 1 && resultCode == RESULT_OK && data != null) {


                Uri uripath = data.getData();


                try {
                    RealPathUtil realPathUtil = new RealPathUtil();
                    File f = new File(realPathUtil.getRealPath(getApplicationContext(), uripath));

                    long fileSizeInBytes = f.length();
                    // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                    long fileSizeInKB = fileSizeInBytes / 1024;
                    //  Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                    long fileSizeInMB = fileSizeInKB / 1024;


                    if (fileSizeInMB >= 10) {
                        Toast.makeText(getApplicationContext(), "Sorry can't upload more than 10 mb picture ", Toast.LENGTH_LONG).show();
                    } else {

                        bitmap_profile = MediaStore.Images.Media.getBitmap(getContentResolver(), uripath);
                        ExifInterface ei = new ExifInterface(realPathUtil.getRealPath(getApplicationContext(), uripath));
                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                bitmap_profile = rotateImage(bitmap_profile, 90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                bitmap_profile = rotateImage(bitmap_profile, 180);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                bitmap_profile = rotateImage(bitmap_profile, 270);
                                break;
                            default:
                                break;
                        }

                        bitmap_profile_39 = Bitmap.createScaledBitmap(bitmap_profile, 39, 39, false);
                        bitmap_profile_90 = Bitmap.createScaledBitmap(bitmap_profile, 90, 90, false);
                        bitmap_profile_240 = Bitmap.createScaledBitmap(bitmap_profile, 240, 240, false);

                        image_reg.setImageBitmap(bitmap_profile);
                        authenticateUserProgressDialogue();
                        upload_image();
                        image_reg.setTag("profile_picture");

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    private static Bitmap rotateImage(Bitmap img,int degree) {


        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }


    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
        byte [] b=baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);

    }

    private void authenticateUserProgressDialogue(){

        this.main_loader_basic_info_bg.setVisibility(View.VISIBLE);

    }

    private void hideProgressBar(){

        this.main_loader_basic_info_bg.setVisibility(View.GONE);
    }

    private void assignLocalvariables(){
       setCurrent_country_id("");
        sign_out_from_page_i = (ImageView)findViewById(R.id.sign_out_from_page_i);
        queue= Volley.newRequestQueue(this); // this = context

        error_msg = new Dialog(this);
        error_msg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        error_msg.setContentView(R.layout.error_alert);
        country_calling_code = (TextView)findViewById(R.id.country_calling_code);
        upload_image_reg_btn = (TextView)findViewById(R.id.upload_image_reg_btn);
        save_and_continue_to_ii = (TextView)findViewById(R.id.save_and_continue_to_ii);

        for_this_intent = getIntent().getExtras();

        this.dashboard_preference=getApplicationContext().getSharedPreferences("Login",0);
        this.customers_id = dashboard_preference.getInt("customers_id",0);
        countries_list = new ArrayList<>();
        countries_code_list = new ArrayList<>();
        countries_id_list = new ArrayList<>();
        country_state_list = new ArrayList<>();
        state_city_list = new ArrayList<>();
//////////////////////////////////////////////////////////////////////////////////////////////////////////////


///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        HttpUrl = getResources().getString(R.string.about_yourself_api_url);
        first_name = (EditText)findViewById(R.id.first_name);
        last_name = (EditText)findViewById(R.id.last_name);
        address = (EditText)findViewById(R.id.address);
        state =(AutoCompleteTextView)findViewById(R.id.state);
        city = (AutoCompleteTextView) findViewById(R.id.city);
        zip_postal_code = (EditText)findViewById(R.id.zip_postal_code);
        mobile_number = (EditText)findViewById(R.id.mobile_number);

        user_country = (MaterialSpinner) findViewById(R.id.user_country);
        user_finding_resources = (MaterialSpinner) findViewById(R.id.user_finding_resources);

        image_reg = (CircleImageView)findViewById(R.id.image_reg);

        user_finding_resources.setItems("-- Select --","Google", "Facebook", "Twitter", "Youtube", "Instagram","Google+","Linked In","Blogs","School/College","Friends","Indeed","Quora");

        main_loader_basic_info_bg = (LinearLayout)findViewById(R.id.main_loader_basic_info_bg);
        recent_loader = (AVLoadingIndicatorView)findViewById(R.id.recent_loader);
        this.is_image_uploaded = false;
    }
    private Boolean checkValidations(){
        if(first_name.getText().toString().trim().equals("") ||
           last_name.getText().toString().trim().equals("") ||
           user_country.getText().toString().trim().equals("-- Select--") ||
           address.getText().toString().trim().equals("") ||
           state.getText().toString().trim().equals("") ||
           city.getText().toString().trim().equals("") ||
           zip_postal_code.getText().toString().trim().equals("") ||
           mobile_number.getText().toString().trim().equals("") ||
           user_finding_resources.getText().toString().trim().equals("-- Select --")){

             btn_ok=(TextView)error_msg.findViewById(R.id.btn_ok);
             message=(TextView)error_msg.findViewById(R.id.message);
            message.setText("Fields cannot be empty");
            error_msg.show();
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    error_msg.dismiss();
                }
            });
        return false;
        }else{
            if(!is_image_uploaded){

                btn_ok=(TextView)error_msg.findViewById(R.id.btn_ok);
                message=(TextView)error_msg.findViewById(R.id.message);
                message.setText("You must have to Upload Image");
                error_msg.show();
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        error_msg.dismiss();
                    }
                });
                return false;
            }
            return true;
        }

    }
    private void getCountriesList(){
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray_complete = new JSONArray(response);
                            JSONArray jsonObject_user_country = jsonArray_complete.getJSONArray(0);
                            JSONArray jsonObject_all_country = jsonArray_complete.getJSONArray(1);
                            for(int i = 0; i < jsonObject_all_country.length();i++){
                                countries_list.add(jsonObject_all_country.getJSONObject(i).getString("country_name"));
                                countries_code_list.add(jsonObject_all_country.getJSONObject(i).getString("country_calling_code"));
                                countries_id_list.add(jsonObject_all_country.getJSONObject(i).getString("country_id"));
                            }
                            user_country.setItems(countries_list);
                            user_country.setText(jsonObject_user_country.getJSONObject(0).getString("country_name"));
                            country_calling_code.setText("+"+jsonObject_user_country.getJSONObject(0).getString("country_calling_code"));
                            setCurrent_country_id(jsonObject_user_country.getJSONObject(0).getString("country_id"));
                            getStatesList(jsonObject_user_country.getJSONObject(0).getString("country_id"));

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
    private void getStatesList(final String country_id){
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray_complete = new JSONArray(response);
                            for(int i = 0; i < jsonArray_complete.length();i++){
                                country_state_list.add(jsonArray_complete.getJSONObject(i).getString("state_name"));
                            }
                            adapter_state = new ArrayAdapter<String>(getApplicationContext(),
                                    R.layout.iconic_search, R.id.name, country_state_list);
                            state.setAdapter(adapter_state);


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
                params.put("get_states_list", "true");
                return params;
            }
        };
        queue.add(postRequest);
    }
    private void getCitiesList(final String state_name){

        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray_complete = new JSONArray(response);

                            for(int i = 0; i < jsonArray_complete.length();i++){
                                state_city_list.add(jsonArray_complete.getJSONObject(i).getString("city_name"));
                            }
                            adapter_city = new ArrayAdapter<String>(getApplicationContext(),
                                    R.layout.iconic_search, R.id.name, state_city_list);
                            city.setAdapter(adapter_city);


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
                params.put("state_name",state_name);
                params.put("country_id",getCurrent_country_id());
                params.put("get_cities_list", "true");
                return params;
            }
        };
        queue.add(postRequest);
    }
    private void upload_image(){

        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();
                        try {
                            image_reg.setImageBitmap(bitmap_profile);
                            JSONObject jsonObject = new JSONObject(response);

                            is_image_uploaded = true;


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
                params.put("image_name",BitMapToString(bitmap_profile));
                params.put("image_name_39",BitMapToString(bitmap_profile_39));
                params.put("image_name_90",BitMapToString(bitmap_profile_90));
                params.put("image_name_240",BitMapToString(bitmap_profile_240));
                params.put("upload_image", "true");
                return params;
            }
        };
        queue.add(postRequest);
        postRequest.setRetryPolicy(new DefaultRetryPolicy(50000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    private void getAllInputAndSend(){
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();

                    Intent intent = new Intent(getApplicationContext(),Education_experience.class);

                    startActivity(intent);

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
                params.put("insert_basic_info_data","true");
                params.put("set_rejection","true");
                params.put("first_name",first_name.getText().toString());
                params.put("last_name",last_name.getText().toString());
                params.put("address",address.getText().toString());
                params.put("phone_number",mobile_number.getText().toString());
                params.put("country",user_country.getText().toString());
                params.put("finding_source",user_finding_resources.getText().toString());
                params.put("zip_code",zip_postal_code.getText().toString());
                params.put("state",state.getText().toString());
                params.put("city",city.getText().toString());
                return params;
            }
        };
        queue.add(postRequest);

    }

    @Override
    public void onBackPressed() {
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


}
