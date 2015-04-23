package in.leshop.local_e_shop;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


public class MainPage extends Activity {

    private static final String TAG = "LeShop-Main";
    SearchView search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        ActionBar actionBar = getActionBar();
        if(actionBar!=null) {
            actionBar.setIcon(R.mipmap.cart_icon);
        } else {
            Log.e(TAG, "Action bar is null");
        }
        search=(SearchView) findViewById(R.id.searchView);
        search.setIconified(false);
        int id = search.getContext().getResources().getIdentifier("android:id/search_src_text",null,null);
        TextView textView = (TextView) search.findViewById(id);
        textView.setTextColor(Color.WHITE);
        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                CharSequence toBeSearched = ((SearchView) v).getQuery();
                Log.e(TAG, "SerachView Has focus = " + hasFocus);
                if (!hasFocus) {
                    Toast.makeText(getBaseContext(), String.valueOf(toBeSearched),
                            Toast.LENGTH_SHORT).show();
                    // tobeSearched String will be Queried To Server
                    // Query Code goes here
                }


            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            //Login code goes here
            return true;
        } else if (id == R.id.action_track_order) {
            //Login code goes here
            return true;
        } else if (id == R.id.action_invite_friends) {
            //Track order code goes here
            return true;
        } else if (id == R.id.action_contact_us) {
            //Contact us code goes here
            return true;
        } else if (id == R.id.action_policies) {
            //Policies code goes here
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        Log.e(TAG, "onBackPressed");
        super.onBackPressed();
    }
}
