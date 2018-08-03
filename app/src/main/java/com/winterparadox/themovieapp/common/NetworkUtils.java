package com.winterparadox.themovieapp.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkRequest;

public class NetworkUtils {

    public static boolean isConnected (Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService (Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo ();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting ();
    }

    public static void registerConnectivityCallback (Context context, ConnectivityManager
            .NetworkCallback callBack) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService (Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder ();

        connectivityManager.registerNetworkCallback (builder.build (), callBack);
    }

    public static void unregisterConnectivityCallback (Context context, ConnectivityManager
            .NetworkCallback callBack) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService (Context.CONNECTIVITY_SERVICE);

        connectivityManager.unregisterNetworkCallback (callBack);
    }
}
