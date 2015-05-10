package in.leshop.database;

import android.content.ContentValues;
import android.location.Address;
import android.location.Location;

import in.leshop.local_e_shop.R;

/**
 * Created by Mudits on 4/21/2015.
 */
public class DeliveryAddress {

    private Address mAddr;
    private String mTag;
    private String mClientName;
    private String mHouseNo;
    private String mUserId;

    public DeliveryAddress(Address addr, String tag, String name, String houseNo,String userId) {
        mAddr = addr;
        mTag = tag;
        mClientName = name;
        mHouseNo = houseNo;
        mUserId = userId;
        assert(!userId.isEmpty());
    }

    public String getUserId() {return mUserId;}

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
        String ret = null;
        if((getClientName() != null) && (!getClientName().isEmpty()))
         ret = getClientName() + ",";
        if((getTag() !=null) && !getTag().isEmpty())
         ret = "@" + getTag() + ",";
        if(getHouseNo() != null)
            ret = ret + getHouseNo() + ",";
        if((getAddress().getPremises() != null) && !getAddress().getPremises().isEmpty())
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
