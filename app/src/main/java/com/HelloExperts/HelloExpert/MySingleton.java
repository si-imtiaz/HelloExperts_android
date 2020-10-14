package com.HelloExperts.HelloExpert;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    private static MySingleton mySingleton;
    private Context mContext;
    private RequestQueue requestQueue;

    public MySingleton(Context mContext){
        this.mContext = mContext;
        this.requestQueue = getRequestqueue();
    }

    public RequestQueue getRequestqueue(){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return requestQueue;
    }
    public static synchronized MySingleton getInstance(Context context){
        if(mySingleton == null){
        mySingleton = new MySingleton(context);
        }
        return mySingleton;
    }
    public<T> void addToRequestQueue(Request<T> request){
        requestQueue.add(request);
    }

}
