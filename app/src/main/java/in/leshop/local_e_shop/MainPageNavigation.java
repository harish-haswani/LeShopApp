package in.leshop.local_e_shop;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import in.leshop.database.DeliveryAddress;
import static in.leshop.local_e_shop.LeShopApp.getApplicationInstance;


public class MainPageNavigation extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */

    private static final String TAG = "LeShop-MainNavigation";
    SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_navigation);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        search = (SearchView) findViewById(R.id.searchView1);
        search.setIconified(false);
        int id = search.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
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
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                //Code Goes for Home
                break;
            case 2:

                break;
            case 3:

                break;
        }
    }


    public void restoreActionBar() {
       /* ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main_page_navigation, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
            Intent intent;
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
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
        } else if (id == R.id.delivery_address) {
            //change location code goes here
            Intent intent = new Intent(this, AddDeliveryAddress.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpActionBar();
    }

    private void setUpActionBar() {
        Log.i(TAG, "Setting up Action bar");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        ArrayList<String> tags = getApplicationInstance().getAddressDb().getAllTags();
        if (getApplicationInstance().getUsingCurrentLocation() || ((tags == null) || tags.isEmpty())) {
            actionBar.setTitle(getApplicationInstance().getCurAddress().getSubLocality());
            actionBar.setSubtitle(getApplicationInstance().getCurAddress().getLocality());
        } else {
            DeliveryAddress addr = getApplicationInstance().getCurDeliveryAddress();
            actionBar.setTitle(addr.getTag());
            actionBar.setSubtitle(addr.getAddress().getLocality());
        }
        if (actionBar != null) {
            actionBar.setIcon(R.mipmap.cart_icon);
        } else {
            Log.e(TAG, "Action bar is null");
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_page_navigation, container, false);
            return rootView;
        }


        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainPageNavigation) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
