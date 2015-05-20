package in.leshop.local_e_shop;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Locale;

import in.leshop.database.DeliveryAddress;

import static in.leshop.local_e_shop.LeShopApp.getApplicationInstance;


public class AddDeliveryAddress extends ActionBarActivity {
    private static final String TAG = AddDeliveryAddress.class.getSimpleName();
    ArrayList<String> mSpinnerData;
    EditText mName;
    EditText mHouseNo;
    EditText mSociety;
    EditText mArea;
    EditText mCity;
    EditText mDist;
    EditText mState;
    EditText mPinCode;
    EditText mPhone;
    EditText mLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery_address);
        getSupportActionBar().setTitle(getResources().getString(R.string.Add_Delivery_Address));
        mName = (EditText) findViewById(R.id.nameTB);
        mHouseNo = (EditText) findViewById(R.id.houseNoTB);
        mSociety = (EditText) findViewById(R.id.societyTB);
        mArea = (EditText) findViewById(R.id.areaTB);
        mCity = (EditText) findViewById(R.id.cityTB);
        mDist = (EditText) findViewById(R.id.distTB);
        mState = (EditText) findViewById(R.id.stateTB);
        mPinCode = (EditText) findViewById(R.id.pincodeTB);
        mPhone = (EditText) findViewById(R.id.phoneTB);
        mLabel = (EditText) findViewById(R.id.LabelTB);
        setDefaults();
    }


    public void saveButtonClicked(View v) {
        View checkFailed = checkMandatoryFields();
       if( checkFailed == null) {
           Address mAddr = getApplicationInstance().getCurAddress();
           Address laddr = new Address(Locale.getDefault());
           laddr.setPhone(String.valueOf(mPhone.getText()));
           laddr.setAdminArea(String.valueOf(mState.getText()));
           laddr.setLocality(String.valueOf(mCity.getText()));
           laddr.setSubLocality(String.valueOf(mArea.getText()));
           if(mAddr == null) {
               laddr.setCountryName("India");
               laddr.setLatitude(28.57);
               laddr.setLongitude(77.3);
           } else {
               laddr.setCountryName(mAddr.getCountryName());
               laddr.setLatitude(mAddr.getLatitude());
               laddr.setLongitude(mAddr.getLongitude());
           }
           laddr.setPostalCode(String.valueOf(mPinCode.getText()));
           laddr.setFeatureName(String.valueOf(mSociety.getText()));

           DeliveryAddress addr = new DeliveryAddress(laddr,String.valueOf(mLabel.getText()),String.valueOf(mName.getText())
                   ,String.valueOf(mHouseNo.getText()),getApplicationInstance().curUser());
           getApplicationInstance().getAddressDb().insertDeliveryAddress(addr);
           getApplicationInstance().setDeliveryAddress(addr);
           Log.i(TAG,"Saving Address:" + addr.toString());
           launchMainPage();
       }
       else {
          if(checkFailed.requestFocus()) {
              InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
              imm.showSoftInput(checkFailed, InputMethodManager.SHOW_IMPLICIT);
          }
       }
    }

    public void useCurrentLocationButtonClicked(View v) {
        //TODO: Again compute current location. For now taking location
        //computed at the time of launching app
        getApplicationInstance().setUsingCurrentLocation(true);
        launchMainPage();
    }


    private void launchMainPage() {
        Log.i(TAG, "Starting Main Page activity");
        Intent intent = new Intent(this, MainPageNavigation.class);
        startActivity(intent);
        finish();
    }

    private View checkMandatoryFields() {
        if (mName.getText().length() == 0) {
            return mName;
        }

        if (mHouseNo.getText().length() == 0) {
            return mHouseNo;
        }
        if (mArea.getText().length() == 0) {
            return mArea;
        }
        if (mCity.getText().length() == 0) {
            return mCity;
        }
        if (mPhone.getText().length() == 0) {
            return mPhone;
        }

        if(mLabel.getText().length() == 0) {
            return mLabel;
        }
        return null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_no_delivery_address, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setDefaults () {
        Address mAddr = getApplicationInstance().getCurAddress();
        if(mAddr == null) {
            mArea.setText("");
            mSociety.setText("");
            mDist.setText("");
            mCity.setText("");
            mState.setText("");
            mPinCode.setText("");
        }else {
            if (mAddr.getSubLocality() != null)
                mArea.setText(mAddr.getSubLocality());
            if ((mAddr.getFeatureName() != null))
                mSociety.setText(mAddr.getFeatureName());
            if (mAddr.getSubAdminArea() != null)
                mDist.setText(mAddr.getSubAdminArea());
            mCity.setText(mAddr.getLocality());
            mState.setText(mAddr.getAdminArea());
            mPinCode.setText(mAddr.getPostalCode());
        }
    }


}
