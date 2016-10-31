package corentinulysse.bikegeoapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.gms.location.LocationListener;

import java.util.ArrayList;
import java.util.List;


public class NavigationActivity extends AppCompatActivity implements Interface, LocationListener {// extends AppCompatActivity  {

    private static String URL = "http://opendata.paris.fr/explore/dataset/stations-velib-disponibilites-en-temps-reel/download/?format=geojson&timezone=Europe/Berlin";

    private ArrayList<ListSample> list;//Entrée du SampleAdapter
    private List<StationsVelib> stationDataReq;

    private RequestQueue mRequestQueue;
    private HttpRequest mHttpRequest;
    private boolean mFirstRequest = false;
    private PagerAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1
            );

        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 2);
        }


        mFirstRequest = true;
        stationDataReq = new ArrayList<>();


        mRequestQueue = VolleyQueue.getInstance(NavigationActivity.this);
        mHttpRequest = new HttpRequest();

        mHttpRequest.LaunchHttpRequest(mRequestQueue, NavigationActivity.this, URL);

        stationDataReq = mHttpRequest.getStationList();





    }

    public HttpRequest getHttpRequest() {
        return mHttpRequest;
    }

    public void httpRequestReceived(boolean requestReceived) {

        if (requestReceived) {

            stationDataReq = mHttpRequest.getStationList();//Recuperation de la liste des Stations de la requete
            refreshFavorites();

            if (mFirstRequest == false) {

                adapter.pagerAdapterHttpRequestReceived();
            }


            if (mFirstRequest) {
                TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
                tabLayout.addTab(tabLayout.newTab());
                tabLayout.addTab(tabLayout.newTab());
                tabLayout.addTab(tabLayout.newTab());
                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

                final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
                adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), NavigationActivity.this);
                viewPager.setAdapter(adapter);
                tabLayout.setupWithViewPager(viewPager);

                mFirstRequest = false;
            }
        }
        return;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                //User chose the "Settings" item, show the app settings UI...
                Intent intent = new Intent();
                intent.setClass(NavigationActivity.this, FavoritesActivity.class);


                startActivity(intent);

                return true;

            default: //If we got here, the user's action was not recognized.
                //Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public List<StationsVelib> getStationList() {

        return stationDataReq;
    }

    @Override
    public void sendHttpRequestFromFragment() {
        final NavigationActivity activity = this;
        getHttpRequest().LaunchHttpRequest(mRequestQueue, activity, URL);
    }

    @Override
    public void onLocationChanged(Location location) {

    }


    public void refreshFavorites() {
        ArrayList<StationsVelib> temp = FavoritesStations.getFavorites(getApplicationContext());
        List<StationsVelib> temp2 = getStationList();
        FavoritesStations.removeAllFavorite(getApplicationContext());
        if(temp!=null) {
            for (StationsVelib station : temp) {
                for (int i = 0; i < temp2.size(); ++i) {
                    if (station.getName().equals(temp2.get(i).getName())) {
                        FavoritesStations.addFavorite(getApplicationContext(), temp2.get(i));
                    }
                }
            }
        }
        Toast.makeText(getApplicationContext()
                , "Favoris actualisés"
                , Toast.LENGTH_LONG).show();
    }
}
