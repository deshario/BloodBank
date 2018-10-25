package com.deshario.bloodbank.Configs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.deshario.bloodbank.MainActivity;

/**
 * Created by Deshario on 2/10/2018.
 */

public class ConnectionReceiver extends BroadcastReceiver {

    MainActivity mainActivity; //a reference to activity's context

    public ConnectionReceiver(MainActivity context){
        mainActivity = context;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {

        if (intent.getExtras() != null){
            final ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnectedOrConnecting()){
                mainActivity.ConnectedCallback("Connected : "+networkInfo.getTypeName());
            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)){
                mainActivity.DisconnectedCallback();
            }
        }
    }

}