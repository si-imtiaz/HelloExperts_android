package com.HelloExperts.HelloExpert;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


public class Education_verification extends AppCompatActivity {
    private MaterialSpinner user_id_doc_1_type,user_skill_doc_1_type,user_skill_doc_2_type;
    private TextView save_and_continue_to_v,attach_id_doc_1,attach_id_doc_2,attach_skill_doc_1,attach_skill_doc_2,attached_id_doc_1_name,attached_id_doc_2_name,attached_skill_doc_1_name,attached_skill_doc_2_name;
    private Intent intent;
    private String file_path;
    private Boolean id_1,id_2,edu_1,edu_2;
    private String HttpUrl;
    SharedPreferences dashboard_preference;

    String upload_path;
    private  int customers_id;
    ImageView sign_out_from_page_iv;
    LinearLayout main_loader_education_verification_bg;
    AVLoadingIndicatorView recent_loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_education_verification);
        id_1 = false;
        id_2 = false;
        edu_1 = false;
        edu_2 = false;

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

        user_id_doc_1_type = (MaterialSpinner)findViewById(R.id.user_id_doc_1_type);
        user_skill_doc_1_type = (MaterialSpinner)findViewById(R.id.user_skill_doc_1_type);
        user_skill_doc_2_type = (MaterialSpinner)findViewById(R.id.user_skill_doc_2_type);

        HttpUrl = getResources().getString(R.string.about_yourself_api_url);
        this.dashboard_preference=getApplicationContext().getSharedPreferences("Login",0);
        this.customers_id = dashboard_preference.getInt("customers_id",0);
        sign_out_from_page_iv = (ImageView)findViewById(R.id.sign_out_from_page_iv);
        attach_id_doc_1 = (TextView)findViewById(R.id.attach_id_doc_1);
        attach_id_doc_2 = (TextView)findViewById(R.id.attach_id_doc_2);
        attach_skill_doc_1 = (TextView)findViewById(R.id.attach_skill_doc_1);
        attach_skill_doc_2 = (TextView)findViewById(R.id.attach_skill_doc_2);
        save_and_continue_to_v = (TextView)findViewById(R.id.save_and_continue_to_v);

        attached_id_doc_1_name = (TextView)findViewById(R.id.attached_id_doc_1_name);
        attached_id_doc_2_name = (TextView)findViewById(R.id.attached_id_doc_2_name);
        attached_skill_doc_1_name = (TextView)findViewById(R.id.attached_skill_doc_1_name);
        attached_skill_doc_2_name = (TextView)findViewById(R.id.attached_skill_doc_2_name);
        main_loader_education_verification_bg = (LinearLayout)findViewById(R.id.main_loader_education_verification_bg);
        recent_loader = (AVLoadingIndicatorView)findViewById(R.id.recent_loader);
        recent_loader.show();

        user_id_doc_1_type.setItems("-- Select --","Passport","Government Issued ID","Driving License","Birth Certificate");
        user_skill_doc_1_type.setItems("-- Select --","Student ID","Transcript","Degree","Clearance Certificate","Experience Certificate");
        user_skill_doc_2_type.setItems("-- Select --","Student ID","Transcript","Degree","Clearance Certificate","Experience Certificate");

        user_id_doc_1_type.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                user_id_doc_1_type.setError(null);
                user_id_doc_1_type.clearFocus();
            }
        });
        user_skill_doc_1_type.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                if(!user_skill_doc_1_type.equals("-- Select -- ") && user_skill_doc_1_type.getText().toString().equals(user_skill_doc_2_type.getText().toString())){
                    error_message("Both document cannot be of same type.");
                    user_skill_doc_1_type.setSelectedIndex(0);
                }else{

                    user_skill_doc_1_type.setError(null);
                    user_skill_doc_1_type.clearFocus();
                }

            }
        });

        user_skill_doc_2_type.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                if(!user_skill_doc_2_type.equals("-- Select -- ") && user_skill_doc_1_type.getText().toString().equals(user_skill_doc_2_type.getText().toString())){
                    error_message("Both document cannot be of same type.");
                    user_skill_doc_2_type.setSelectedIndex(0);

                }else{

                    user_skill_doc_2_type.setError(null);
                    user_skill_doc_2_type.clearFocus();
                }


            }
        });
        sign_out_from_page_iv.setOnClickListener(new View.OnClickListener() {
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
        save_and_continue_to_v.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Checkconnectivity checkconnectivity = new Checkconnectivity();
                if(!checkconnectivity.hasInternetConnection(getApplicationContext())){
                    Toast.makeText(Education_verification.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }else {
                    authenticateUserProgressDialogue();
                    if (!attach_id_doc_1.getText().toString().toLowerCase().equals("update file")) {
                        error_message("Please upload identity verification document");
                        hideProgressBar();
                    } else if (!attach_id_doc_2.getText().toString().toLowerCase().equals("update file")) {
                        error_message("Please upload Standing picture with above attachment");
                        hideProgressBar();
                    } else if (!attach_skill_doc_1.getText().toString().toLowerCase().equals("update file")) {
                        error_message("Please upload skill document one picture");
                        hideProgressBar();
                    } else if (!attach_skill_doc_2.getText().toString().toLowerCase().equals("update file")) {
                        error_message("Please upload skill document two picture");
                        hideProgressBar();
                    } else {
                        sendData("", "goto_next_v", "", "");
                        startActivity(new Intent(Education_verification.this,About_yourself.class));
                    }

                }

            }
        });


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                return;
            }
        }

        enable_button();





    }

    public void error_message(String error_msg_text){
        final Dialog error_msg = new Dialog(Education_verification.this);
        error_msg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        error_msg.setContentView(R.layout.error_alert);
        TextView btn_ok=(TextView)error_msg.findViewById(R.id.btn_ok);
        TextView message=(TextView)error_msg.findViewById(R.id.message);
        message.setText(error_msg_text);
        error_msg.show();
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error_msg.dismiss();
            }
        });
    }
    private void enable_button() {

        attach_id_doc_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Checkconnectivity checkconnectivity = new Checkconnectivity();
                if(!checkconnectivity.hasInternetConnection(getApplicationContext())){
                    Toast.makeText(Education_verification.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }else {
                    if (user_id_doc_1_type.getText().toString().equals("-- Select --")) {
                        user_id_doc_1_type.setError("Select one of them");
                    } else {
                        user_id_doc_1_type.setError(null);
                        user_id_doc_1_type.clearFocus();
                        id_1 = true;
                        id_2 = false;
                        edu_1 = false;
                        edu_2 = false;

                        Intent intent = new Intent();
                        intent.setType("*/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select File"), 10);


//                    new MaterialFilePicker()
//
//                            .withActivity(Education_verification.this)
//                            .withRequestCode(10)
//                            .start();
                    }
                }


            }
        });

        attach_id_doc_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Checkconnectivity checkconnectivity = new Checkconnectivity();
                if(!checkconnectivity.hasInternetConnection(getApplicationContext())){
                    Toast.makeText(Education_verification.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }else {

                    id_1 = false;
                    id_2 = true;
                    edu_1 = false;
                    edu_2 = false;


                    Intent intent = new Intent();
                    intent.setType("*/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select File"), 10);
                }

//
//                    new MaterialFilePicker()
//                            .withActivity(Education_verification.this)
//                            .withRequestCode(10)
//                            .start();


            }
        });
        attach_skill_doc_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Checkconnectivity checkconnectivity = new Checkconnectivity();
                if(!checkconnectivity.hasInternetConnection(getApplicationContext())){
                    Toast.makeText(Education_verification.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }else {
                    if (user_skill_doc_1_type.getText().toString().equals("-- Select --")) {
                        user_skill_doc_1_type.setError("");
                    } else {
                        user_skill_doc_1_type.setError(null);
                        user_skill_doc_1_type.clearFocus();
                        id_1 = false;
                        id_2 = false;
                        edu_1 = true;
                        edu_2 = false;

                        Intent intent = new Intent();
                        intent.setType("*/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select File"), 10);

//                    new MaterialFilePicker()
//                            .withActivity(Education_verification.this)
//                            .withRequestCode(10)
//                            .start();
                    }
                }

            }
        });
        attach_skill_doc_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Checkconnectivity checkconnectivity = new Checkconnectivity();
                if(!checkconnectivity.hasInternetConnection(getApplicationContext())){
                    Toast.makeText(Education_verification.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }else {
                    if (user_skill_doc_2_type.getText().toString().equals("-- Select --")) {
                        user_skill_doc_2_type.setError("");
                    } else {
                        user_skill_doc_2_type.setError(null);
                        user_skill_doc_2_type.clearFocus();
                        id_1 = false;
                        id_2 = false;
                        edu_1 = false;
                        edu_2 = true;


                        Intent intent = new Intent();
                        intent.setType("*/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select File"), 10);


//                    new MaterialFilePicker()
//                            .withActivity(Education_verification.this)
//                            .withRequestCode(10)
//                            .start();

                    }
                }

            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            enable_button();
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
            }
        }
    }
    private void authenticateUserProgressDialogue(){

        this.main_loader_education_verification_bg.setVisibility(View.VISIBLE);

    }

    private void hideProgressBar(){

        this.main_loader_education_verification_bg.setVisibility(View.GONE);
    }

    public void sendData(final String upload_path, final String post_request, final String doc_type, final String doc_type_name){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext()); // this = context
        StringRequest postRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        hideProgressBar();
                        if(doc_type_name.equals("update_id_1")){

                            attach_id_doc_1.setText("Update File");
                        }else if(doc_type_name.equals("update_id_2")){
                            attach_id_doc_2.setText("Update File");
                        }else if(doc_type_name.equals("update_edu_1")){
                            attach_skill_doc_1.setText("Update File");
                        }else if(doc_type_name.equals("update_edu_2")){
                            attach_skill_doc_2.setText("Update File");
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
                params.put("upload_path",upload_path);
                params.put(doc_type_name,doc_type);
                params.put(post_request, "true");
                return params;
            }
        };
        queue.add(postRequest);
    }

    @SuppressLint("NewApi")
    public static String getPathAPI19(Context context, Uri uri) {
        String filePath = "";
        String fileId = DocumentsContract.getDocumentId(uri);
        // Split at colon, use second item in the array
        String id = fileId.split(":")[1];
        String[] column = {MediaStore.Images.Media.DATA};
        String selector = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, selector, new String[]{id}, null);
        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

        Checkconnectivity checkconnectivity = new Checkconnectivity();
        if(!checkconnectivity.hasInternetConnection(getApplicationContext())){
            Toast.makeText(Education_verification.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }else {
            if (requestCode == 10 && resultCode == RESULT_OK) {

                Uri uri = data.getData();

                RealPathUtil realPathUtil = new RealPathUtil();


                File f = new File(realPathUtil.getRealPath(getApplicationContext(), uri));


                long fileSizeInBytes = f.length();
                // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                long fileSizeInKB = fileSizeInBytes / 1024;
                //  Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                long fileSizeInMB = fileSizeInKB / 1024;

                if (fileSizeInMB > 20) {
                    Toast.makeText(getApplicationContext(), "Sorry can't upload more than 20 mb file ", Toast.LENGTH_LONG).show();
                } else {
                    authenticateUserProgressDialogue();
                    file_path = f.getAbsolutePath();

                    if (id_1) {
                        attached_id_doc_1_name.setText(f.getName());
                    }
                    if (id_2) {

                        attached_id_doc_2_name.setText(f.getName());
                    }
                    if (edu_1) {
                        attached_skill_doc_1_name.setText(f.getName());
                    }
                    if (edu_2) {
                        attached_skill_doc_2_name.setText(f.getName());
                    }

                    new UploadFileAsync().execute("");


                }

            }
        }


        }



    private class UploadFileAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if(id_1){
                upload_docs(file_path,"file_id_1");
            }
            if(id_2){
                upload_docs(file_path,"file_id_2");
            }
            if(edu_1){
                upload_docs(file_path,"file_edu_1");
            }
            if(edu_2){
                upload_docs(file_path,"file_edu_2");
            }

            return "Executed";
        }
        protected void upload_docs(String file,String document_request_type){
            try {
                String sourceFileUri = file;

                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                File sourceFile = new File(sourceFileUri);

                if (sourceFile.isFile()) {

                    try {
                        String upLoadServerUri = getResources().getString(R.string.about_yourself_api_url);

                        // open a URL connection to the Servlet
                        FileInputStream fileInputStream = new FileInputStream(
                                sourceFile);
                        URL url = new URL(upLoadServerUri);

                        // Open a HTTP connection to the URL
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true); // Allow Inputs
                        conn.setDoOutput(true); // Allow Outputs
                        conn.setUseCaches(false); // Don't use a Cached Copy
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("ENCTYPE",
                                "multipart/form-data");
                        conn.setRequestProperty("Content-Type",
                                "multipart/form-data;boundary=" + boundary);
                        conn.setRequestProperty(document_request_type, sourceFileUri);
                        dos = new DataOutputStream(conn.getOutputStream());

                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\""+document_request_type+"\";filename=\""
                                + sourceFileUri + "\"" + lineEnd);
                        dos.writeBytes(lineEnd);

                        // create a buffer of maximum size
                        bytesAvailable = fileInputStream.available();

                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        buffer = new byte[bufferSize];

                        // read file and write it into form...
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                        while (bytesRead > 0) {

                            dos.write(buffer, 0, bufferSize);
                            bytesAvailable = fileInputStream.available();
                            bufferSize = Math
                                    .min(bytesAvailable, maxBufferSize);
                            bytesRead = fileInputStream.read(buffer, 0,
                                    bufferSize);

                        }

                        // send multipart form data necesssary after file
                        // data...
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + twoHyphens
                                + lineEnd);


                        // Responses from the server (code and message)
                        int serverResponseCode = conn.getResponseCode();
                         upload_path = conn.getHeaderField("upload_path");




                        if (serverResponseCode == 200) {



                        }


                        // close the streams //
                        fileInputStream.close();
                        dos.flush();
                        dos.close();
                        hideProgressBar();

                    } catch (Exception e) {


                        e.printStackTrace();

                    }


                } // End else block


            } catch (Exception ex) {


                ex.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(String result) {


            if(id_1){
                sendData(upload_path,"upload_id_1",user_id_doc_1_type.getText().toString(),"update_id_1");
            }
            if(id_2){
                sendData(upload_path,"upload_id_2","Picture with provided identity","update_id_2");
            }
            if(edu_1){
                sendData(upload_path,"upload_edu_1",user_skill_doc_1_type.getText().toString(),"update_edu_1");
            }
            if(edu_2){
                sendData(upload_path,"upload_edu_2",user_skill_doc_2_type.getText().toString(),"update_edu_2");
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {

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
                params.put("customers_id", Integer.toString(customers_id));
                return params;
            }
        };
        queue.add(postRequest);
    }



}
