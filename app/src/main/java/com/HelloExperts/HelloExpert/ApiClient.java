package com.HelloExperts.HelloExpert;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.HTTP;


public class ApiClient {
    public static final String BASE_URL = "https://www.helloexperts.com";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(RetrofitHttpTrustManager.getUnsafeOkHttpClient().build())
                    .build();
        }
        return retrofit;
    }
}