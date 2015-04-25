package in.leshop.local_e_shop;

import android.app.Application;

import com.google.android.gms.location.LocationServices;

import in.leshop.database.LocationStorage;

/**
 * Created by Mudits on 4/21/2015.
 */
public class LeShopApp extends Application {
    private static LeShopApp s_thisInst;
    private LocationStorage m_addressDb;

    @Override
    public void onCreate() {
        super.onCreate();
        s_thisInst = this;
    }

    static public LeShopApp getApplicationInstance() {
          return s_thisInst;
    }

   public synchronized LocationStorage getAddressDb() {
        if(m_addressDb == null)
            m_addressDb = new LocationStorage(this);
       return m_addressDb;
    }

}
