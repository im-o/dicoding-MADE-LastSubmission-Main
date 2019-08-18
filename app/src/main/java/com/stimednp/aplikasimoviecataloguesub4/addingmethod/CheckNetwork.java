package com.stimednp.aplikasimoviecataloguesub4.addingmethod;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by rivaldy on 8/3/2019.
 */

public class CheckNetwork {
    private static final String TAG = CheckNetwork.class.getSimpleName();
    public static int statusInternet;

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED)
                && (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED)) {
            Log.w(TAG, "No internet Connection");
            statusInternet = 0;//DISCONNECTED
            return true;
        } else {
            if ((connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                    || (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)) {
                Log.w(TAG, "Internet Connection Available");
                statusInternet = 1;//CONNECTED
                return true;
            } else {
                Log.w(TAG, "Internet Connection");
                statusInternet = 2;//CONNECTION
                return true;
            }
        }
    }
}
