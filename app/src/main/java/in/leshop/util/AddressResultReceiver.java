package in.leshop.util;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.util.Log;

public class AddressResultReceiver extends ResultReceiver {
    private static final String TAG = AddressResultReceiver.class.getSimpleName();
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
            Log.d(TAG, "Address result received");
            mReceiver.onReceiveAddress(resultCode, resultData.getParcelable(Constants.RESULT_DATA_KEY));
        }
        else
            Log.e(TAG,"No Address receiver set");
    }
}
