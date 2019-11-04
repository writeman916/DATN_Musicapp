package com.framgia.music_22.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionChecking {

    private Context mContext;

    public ConnectionChecking(Context context) {
        mContext = context;
    }

    public boolean isNetworkConnection() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }
}
