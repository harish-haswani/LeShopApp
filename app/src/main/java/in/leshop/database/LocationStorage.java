package in.leshop.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

/**
 * Created by Mudits on 4/19/2015.
 */
public class LocationStorage extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "my_addresses_db";
    private static final String TABLE_ADDRESS = "delivery_address";
    private static final String TABLE_TAGS ="address_tags";
    /* Column names */
    private static final String KEY_ADDRESS_ID = "Address_Id";
    private static final String KEY_USER_ID = "User_Id";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_TAG = "tag";
    private static final String KEY_NAME = "name";
    private static final String KEY_HOUSE_NO = "house_no";
    private static final String KEY_SOCIETY_NAME = "society";
    private static final String KEY_AREA = "area";
    private static final String KEY_CITY = "city";
    private static final String KEY_STATE = "state";
    private static final String KEY_PIN_CODE = "pin_code";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_PHONE = "phone_no";
    private static final String KEY_NUM_DELIVERIES = "num_deliveries";
    private static final ColumnInfo [] addressTableInfo = getAddressTableColumns();
    private static final String TAG = LocationStorage.class.getSimpleName() ;

    private static class ColumnInfo {
        private String mName;
        private String mType;
        private String mKey;
        public ColumnInfo(String name,String type,String key) {
            mName = name;
            mType = type;
            mKey = key;
        }
        String createColumn() {
          return mName + " " + mType + " " + mKey;
        }
    }

    static ColumnInfo [] getAddressTableColumns() {
        ColumnInfo ret[] = new ColumnInfo[14];
        ret[0] = new ColumnInfo(KEY_TAG,"TEXT ","PRIMARY_KEY");
        ret[1] = new ColumnInfo(KEY_LONGITUDE,"TEXT","");
        ret[2] = new ColumnInfo(KEY_LATITUDE,"TEXT","");
        ret[3] = new ColumnInfo(KEY_NAME,"TEXT","");
        ret[4] = new ColumnInfo(KEY_HOUSE_NO,"TEXT","");
        ret[5] = new ColumnInfo(KEY_SOCIETY_NAME,"TEXT","");
        ret[6] = new ColumnInfo(KEY_AREA,"TEXT","");
        ret[7] = new ColumnInfo(KEY_CITY,"TEXT","");
        ret[8] = new ColumnInfo(KEY_STATE,"TEXT","");
        ret[9] = new ColumnInfo(KEY_COUNTRY,"TEXT","");
        ret[10] = new ColumnInfo(KEY_PIN_CODE,"TEXT","");
        ret[11] = new ColumnInfo(KEY_PHONE,"TEXT","");
        ret[12] = new ColumnInfo(KEY_NUM_DELIVERIES,"INTEGER","");
        ret[13] = new ColumnInfo(KEY_USER_ID,"TEXT","");
        return ret;
    }

    /*
    static ColumnInfo [] getTagTableColumns() {
        ColumnInfo ret[] = new ColumnInfo[2];
        ret[0] = new ColumnInfo(KEY_ADDRESS_ID,"INTEGER","FOREIGN KEY");
        ret[1] = new ColumnInfo(KEY_USER_ID,"TEXT","");
        ret[2] = new ColumnInfo(KEY_TAG,"TEXT","collate nocase");
        return ret;
    } */


    private ContentValues getContentValues(DeliveryAddress addr) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, addr.getClientName());
        values.put(KEY_PHONE,addr.getAddress().getPhone());
        values.put(KEY_COUNTRY,addr.getAddress().getCountryName());
        values.put(KEY_AREA,addr.getAddress().getSubLocality());
        values.put(KEY_CITY,addr.getAddress().getLocality());
        values.put(KEY_LATITUDE, String.valueOf(addr.getAddress().getLatitude()));
        values.put(KEY_LONGITUDE,String.valueOf(addr.getAddress().getLongitude()));
        values.put(KEY_PIN_CODE,addr.getAddress().getPostalCode());
        values.put(KEY_HOUSE_NO,addr.getHouseNo());
        values.put(KEY_SOCIETY_NAME,addr.getAddress().getPremises());
        values.put(KEY_TAG,addr.getTag());
        values.put(KEY_STATE,addr.getAddress().getAdminArea());
        Log.i(TAG,"Content Values =" + values.toString());
        values.put(KEY_USER_ID,addr.getUserId());
        return values;
    }

    public LocationStorage(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private String createTableQuery(String TableName,ColumnInfo [] columnInfos, String Constraint) {
        String sql = "CREATE TABLE " + TableName + "(";
        for (int i = 0 ; i <columnInfos.length; ++i) {
            sql = sql + columnInfos[i].createColumn();
            if (i != (columnInfos.length -1))
               sql = sql +", ";
        }
        if(Constraint != null &&  !Constraint.isEmpty())
           sql = sql + Constraint;
        sql = sql + ")";
        Log.i(TAG,"CREATE TABLE " + sql);
        return sql;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query = createTableQuery(TABLE_ADDRESS,addressTableInfo,null);
        Log.i(TAG,query);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESS);
        // Create tables again
        onCreate(db);
    }

    public void insertDeliveryAddress(DeliveryAddress addr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = getContentValues(addr);
        long i = db.insert(TABLE_ADDRESS,null,values);
        if(i == -1)
           Log.e(TAG,"Failed to insert address in database");
        else
           Log.i(TAG,"Address added at row " + i);
        db.close();
    }


    public void updateDeliveryAddress (DeliveryAddress addr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = getContentValues(addr);
        db.update(TABLE_ADDRESS, values, KEY_TAG + " = ?",
                new String[] {addr.getTag()});
        db.close();
    }

    public void deleteDeliveryAddress(DeliveryAddress addr) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ADDRESS,KEY_TAG + "=?",new String[] {addr.getTag()});
        db.close();
    }

    private DeliveryAddress getDeliveryAddress(Cursor cursor) {
        if(cursor != null) {
            String lname = cursor.getString(cursor.getColumnIndex(KEY_NAME));
            String ltag = cursor.getString(cursor.getColumnIndex(KEY_TAG));
            String lhouseNo = cursor.getString(cursor.getColumnIndex(KEY_HOUSE_NO));
            String lUserid = cursor.getString(cursor.getColumnIndex(KEY_USER_ID));
            Address laddr = new Address(Locale.getDefault());
            laddr.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE))));
            laddr.setLongitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE))));
            laddr.setPhone(cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
            laddr.setAdminArea(cursor.getString(cursor.getColumnIndex(KEY_STATE)));
            Log.i(TAG,"setting cursor ="+ cursor.toString());
            Log.i(TAG,"setting locality ="+ cursor.getString(cursor.getColumnIndex(KEY_CITY)));
            laddr.setLocality(cursor.getString(cursor.getColumnIndex(KEY_CITY)));
            laddr.setSubLocality(cursor.getString(cursor.getColumnIndex(KEY_AREA)));
            laddr.setCountryName(cursor.getString(cursor.getColumnIndex(KEY_COUNTRY)));
            laddr.setPostalCode(cursor.getString(cursor.getColumnIndex(KEY_PIN_CODE)));
            laddr.setPremises(cursor.getString(cursor.getColumnIndex(KEY_SOCIETY_NAME)));
            return new DeliveryAddress(laddr,ltag,lname,lhouseNo,lUserid);
        }
        return null;
    }

    public DeliveryAddress getDeliveryAddress(String tag) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_ADDRESS,null,KEY_TAG + "=?",new String[] {tag},null,null,null,null);
        DeliveryAddress ret = null;
        if(cursor != null) {
            cursor.moveToFirst();
            ret = getDeliveryAddress(cursor);
        }
        db.close();
        return ret;
    }

    public List<DeliveryAddress> getAllDeliveryAddresses() {
        List<DeliveryAddress> deliveryAddresses = new ArrayList<DeliveryAddress>();
        String selectQuery = "SELECT  * FROM " + TABLE_ADDRESS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                // Adding contact to list
                deliveryAddresses.add(getDeliveryAddress(cursor));
            } while (cursor.moveToNext());
        }
        //Not many queries hence closing after every query
        db.close();
        return deliveryAddresses;
    }

    public ArrayList<String> getAllTags() {
        ArrayList<String> ret = new ArrayList<String>();
        String selectQuery = "SELECT " + KEY_TAG + " FROM " + TABLE_ADDRESS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery(selectQuery, null);
        if(cur != null && cur.moveToFirst()) {
            do {
                ret.add(cur.getString(cur.getColumnIndex(KEY_TAG)));
            } while (cur.moveToNext());
        }
        cur.close();
        return ret;
    }

    public DeliveryAddress getDefaultDeliveryAddress() {
        ArrayList<String> tags = getAllTags();
        if(!tags.isEmpty())
           return getDeliveryAddress(tags.get(0));
        return null;
    }

    public boolean hasDeliveryAddresses() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT COUNT(*) FROM " + TABLE_ADDRESS;
        boolean empty = true;
        Cursor cur = db.rawQuery(selectQuery, null);
        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getInt (0) == 0);
        }
        cur.close();
        if(empty)
          Log.i(TAG,"Database Does not have any delivery address stored");
        else
          Log.i(TAG,"Database have delivery addresses stored");
        return !empty;
    }

}
