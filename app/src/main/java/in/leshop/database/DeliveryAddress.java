package in.leshop.database;

import android.content.ContentValues;
import android.location.Address;
import android.location.Location;

/**
 * Created by Mudits on 4/21/2015.
 */
public class DeliveryAddress {

    private Address mAddr;
    private String mTag;
    private String mClientName;
    private String mHouseNo;

    DeliveryAddress(Address addr,String tag,String name, String houseNo) {
        mAddr = addr;
        mTag = tag;
        mClientName = name;
        mHouseNo = houseNo;
    }


    public Address getAddress() {
        return mAddr;
    }

    public String getTag() {
        return mTag;
    }

    public String getClientName () {
        return mClientName;
    }

    public String getHouseNo() {
        return mHouseNo;
    }

    public String toString() {
        String ret = getClientName() + ",";
        if(getHouseNo() != null)
            ret = ret + getHouseNo() + ",";
        if(getAddress().getPremises() != null)
            ret = ret + getAddress().getPremises() + ",";
        if(getAddress().getSubLocality() != null) {
            ret = ret+getAddress().getPremises() + ",";
        }
        if(getAddress().getLocality()!=null) {
            ret = ret+getAddress().getLocality() + ",";
        }
        if(getAddress().getAdminArea() != null) {
            ret = ret+getAddress().getAdminArea() ;
        }

        if(getAddress().getPostalCode() != null) {
            ret =  "," + ret+getAddress().getPostalCode();
        }
        return ret;
    }

}
