package com.docotel.latihanmvp2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkManager {
    private static NetworkManager sNetworkManager;
    private final Context mContext;

    private NetworkManager(Context context) {
        mContext = context;
    }

    public static NetworkManager get(Context context) {
        if (sNetworkManager == null) {
            sNetworkManager = new NetworkManager(context);
        }

        return sNetworkManager;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}

