package in.leshop.local_e_shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.os.Parcelable;
import android.os.Bundle;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import in.leshop.util.AddressProviderService;
import in.leshop.util.AddressResultReceiver;
import in.leshop.util.Constants;
import in.leshop.util.LocationProvider;

import static in.leshop.local_e_shop.LeShopApp.getApplicationInstance;
public class LeLaunch extends Activity implements LocationProvider.LocationCallback ,AddressResultReceiver.AddressReceiver {

    private String TAG = "LeLaunch";
    private AddressResultReceiver mResultReceiver;
    private LocationProvider mLocationProvider;
    Location mLocation;
    boolean mAddressFetched;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_le_launch);
        mLocation = null;
        mAddressFetched = false;
        mLocationProvider = new LocationProvider(this, this);
        mResultReceiver = new AddressResultReceiver(new Handler());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isConnected = false;
        boolean isCurAddressAvailable = false;
        int checkConnectivity = 0;
       Toast.makeText(this,"Checking internet connectivity",Toast.LENGTH_SHORT);
       while (checkConnectivity < 1000) {
            isConnected = isConnected();
            if(isConnected)
                break;
            checkConnectivity++;
        }
        if (isConnected) {
            Log.e(TAG, "Internet Connectivity is there");
            /*Intent intent = new Intent(this, MainPageNavigation.class); */
        }
        else {
            Log.e(TAG, "Internet Connectivity is not there");
            Intent intent = new Intent(this, OutOfCoverage.class);
            startActivity(intent);
        }
        Toast.makeText(this,"Fetching current location",Toast.LENGTH_SHORT).show();
        mLocationProvider.connect();
        mResultReceiver.setAddressReceiver(this);
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

    @Override
    protected void onPause() {
        super.onPause();
        mLocationProvider.disconnect();
        mResultReceiver.setAddressReceiver(null);
    }
    @Override
    public void handleNewLocation(Location location) {
        if((mLocation == null) || (mAddressFetched == false)) {
            mLocation = location;
            Log.i(TAG, "Handle New Location Called");
            fetchAddress(mLocation);
        }
        else {
            Log.i(TAG, "Location already available");
        }
    }


    @Override
    public void onReceiveAddress(int resultCode, Parcelable address) {
        if(resultCode == Constants.FAILURE_RESULT) {
            /*TODO: Launch error Activity */
            Log.e(TAG, "Sorry, Could not locate your current location");
        }
        else {
            Address mAddress = (Address) address;
            Log.i(TAG, "your current location located");
            getApplicationInstance().setCurAddress(mAddress);
            mAddressFetched = true;
            if(!getApplicationInstance().getAddressDb().hasDeliveryAddresses()) {
                Log.i(TAG,"Starting No delivery Address Page activity");
                Toast.makeText(this,"Launching Add address page",Toast.LENGTH_SHORT);
                Intent intent = new Intent(this, AddDeliveryAddress.class);
                startActivity(intent);
            }
            else {
                Log.i(TAG,"Starting Main Page activity");
                Toast.makeText(this,"Launching Main page page",Toast.LENGTH_SHORT);
                Intent intent = new Intent(this, MainPageNavigation.class);
                startActivity(intent);
            }
            finish();
        }
    }

    private void fetchAddress(Location loc) {
        Intent intent = new Intent(this, AddressProviderService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, loc);
        Log.i(TAG,"Starting address service");
        startService(intent);
    }
}
