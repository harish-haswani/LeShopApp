package in.leshop.util;

import android.os.Handler;
import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.provider.SyncStateContract;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.leshop.local_e_shop.R;


public class AddressProviderService extends IntentService {

    private static final String TAG = AddressProviderService.class.getSimpleName();
    protected ResultReceiver mReceiver;

    public AddressProviderService() {
        super("AddressProviderService");
    }

    private void deliverResultToReceiver(int resultCode, Address message) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Log.i(TAG,"Address Intent Started");
            String errorMessage = "";
            mReceiver = intent.getParcelableExtra(Constants.RECEIVER);
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            // Get the location passed to this service through an extra.
            Location location = intent.getParcelableExtra(
                    Constants.LOCATION_DATA_EXTRA);

            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(
                        location.getLatitude(),
                        location.getLongitude(),
                        // In this sample, get just a single address.
                        1);
            } catch (IOException ioException) {
                // Catch network or other I/O problems.
                errorMessage = getString(R.string.service_not_available);
                Log.e(TAG, errorMessage, ioException);
            } catch (IllegalArgumentException illegalArgumentException) {
                // Catch invalid latitude or longitude values.
                errorMessage = getString(R.string.invalid_lat_long_used);
                Log.e(TAG, errorMessage + ". " +
                        "Latitude = " + location.getLatitude() +
                        ", Longitude = " +
                        location.getLongitude(), illegalArgumentException);
            }

            // Handle case where no address was found.
            if (addresses == null || addresses.size()  == 0) {
                if (errorMessage.isEmpty()) {
                    errorMessage = getString(R.string.no_address_found);
                    Log.e(TAG, errorMessage);
                }
                deliverResultToReceiver(Constants.FAILURE_RESULT, null);
            } else {
                Address address = addresses.get(0);
              /*  ArrayList<String> addressFragments = new ArrayList<String>();

                // Fetch the address lines using getAddressLine,
                // join them, and send them to the thread.
                for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    addressFragments.add(address.getAddressLine(i));
                }
                */
                Log.i(TAG, getString(R.string.address_found));
                deliverResultToReceiver(Constants.SUCCESS_RESULT,
                        address);
            }
        }

    }

}


class AddressResultReceiver extends ResultReceiver {

    private AddressReceiver mReceiver;
    public AddressResultReceiver(Handler handler) {
        super(handler);
    }

    public interface AddressReceiver {
        public void onReceiveAddress(int resultCode, Parcelable resultData);
    }

    public void setAddressReceiver(AddressReceiver addressReceiver) {
        mReceiver = addressReceiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        /* Pass on the result to receiver */
        if(mReceiver != null) {
            mReceiver.onReceiveAddress(resultCode, resultData.getParcelable(Constants.RESULT_DATA_KEY));
        }

    }
}
