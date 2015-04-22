package in.leshop.local_e_shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.os.HandlerThread;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

public class LeLaunch extends Activity {

    private String TAG = "LeLaunch";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_le_launch);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isConnected = false;
        int checkConnectivity = 0;
        while (checkConnectivity < 1000) {
            isConnected = isConnected();
            if(isConnected)
                break;
            checkConnectivity++;
        }
        if (isConnected) {
            Log.e(TAG, "Internet Connectivity is there");
            Intent intent = new Intent(this, MainPage.class);
            startActivity(intent);
        }
        else {
            Log.e(TAG, "Internet Connectivity is not there");
            Intent intent = new Intent(this, OutOfCoverage.class);
            startActivity(intent);
        }
        finish();
    }

    private boolean isConnected() {
        boolean isConnected = false;
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = (activeNetworkInfo != null && activeNetworkInfo.isConnected());
        if(!isConnected) {
            SupplicantState supState;
            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            supState = wifiInfo.getSupplicantState();
            if(supState.equals(SupplicantState.ASSOCIATED))
                isConnected=true;
        }
        return isConnected;
    }
}
