package in.leshop.local_e_shop;

import android.app.Application;
import android.location.Address;
import android.util.Log;

import com.google.android.gms.location.LocationServices;

import in.leshop.database.DeliveryAddress;
import in.leshop.database.LocationStorage;

/**
 * Created by Mudits on 4/21/2015.
 */
public class LeShopApp extends Application {
    private static final String TAG = "LeShopApp";

    public static LeShopApp getS_thisInst() {
        return s_thisInst;
    }

    private static LeShopApp s_thisInst;
    private LocationStorage m_addressDb;
    private Address mCurAddress;
    private DeliveryAddress mCurDeliveryAddress;
    private boolean mUsingCurLocation;

    @Override
    public void onCreate() {
        super.onCreate();
        s_thisInst = this;
        mCurAddress = null;
        mUsingCurLocation = false;
        setDeliveryAddress(getAddressDb().getDefaultDeliveryAddress());
    }


    static public LeShopApp getApplicationInstance() {
        return s_thisInst;
    }

    public synchronized LocationStorage getAddressDb() {
        if (m_addressDb == null)
            m_addressDb = new LocationStorage(this);
        return m_addressDb;
    }

    public void setUsingCurrentLocation(boolean val) {
        mUsingCurLocation = val;
    }

    public boolean getUsingCurrentLocation() {
        return mUsingCurLocation;
    }

    public DeliveryAddress getCurDeliveryAddress() { return mCurDeliveryAddress;}

    public void setDeliveryAddress(DeliveryAddress addr) {
        mCurDeliveryAddress = addr;
        if(mCurDeliveryAddress != null)
          Log.i(TAG,"Update Delivery Address " + mCurDeliveryAddress.toString());
    }

    public void setCurAddress(Address curAddress) {
        mCurAddress = curAddress;
    }

    public Address getCurAddress() {
        return mCurAddress;
    }

    public String curUser() {return "sharma_mudit2k1@yahoo.com";}
}
