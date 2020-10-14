package com.HelloExperts.HelloExpert;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.util.Util;

public class Checkconnectivity {



    public boolean hasInternetConnection(final Context context) {
//        final ConnectivityManager connectivityManager = (ConnectivityManager)context.
//                getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        final Network network = connectivityManager.getActiveNetwork();
//        final NetworkCapabilities capabilities = connectivityManager
//                .getNetworkCapabilities(network);
//
//        return capabilities != null
//                && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public int getuploadingspeed(final Context context) {
        final ConnectivityManager cm = (ConnectivityManager)context.
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());

        int upSpeedMbps = nc.getLinkDownstreamBandwidthKbps();

        return upSpeedMbps;

    }



}