package com.semicolon.Halan.Services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Delta on 17/04/2018.
 */

public class NetworkConnection {
    private static NetworkConnection instance=null;

    private NetworkConnection()
    {
        //constructor
    }
    public static synchronized NetworkConnection getInstance ()
    {
        if (instance==null)
        {
            instance = new NetworkConnection();
        }
        return instance;
    }

    public boolean getConnection(Context context)
    {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager!=null)
        {
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();

            if (networkInfo!=null && networkInfo.isAvailable() && networkInfo.isConnected())
            {
                return true;
            }
        }
        return false;
    }
}
