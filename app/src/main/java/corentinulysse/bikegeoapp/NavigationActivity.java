package corentinulysse.bikegeoapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.List;


public class NavigationActivity extends AppCompatActivity implements Interface {// extends AppCompatActivity  {

    private static String URL = "http://opendata.paris.fr/explore/dataset/stations-velib-disponibilites-en-temps-reel/download/?format=geojson&timezone=Europe/Berlin";

    private ArrayList<ListSample> list;//Entrée du SampleAdapter
    private List<StationsVelib> stationDataReq;

    private RequestQueue mRequestQueue;
    private HttpRequest mHttpRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        stationDataReq=new ArrayList<>();


        mRequestQueue =  VolleyQueue.getInstance(NavigationActivity.this);
        mHttpRequest = new HttpRequest();

        mHttpRequest.LaunchHttpRequest(mRequestQueue, NavigationActivity.this, URL);

        stationDataReq=mHttpRequest.getStationList();



//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });


    }

    public HttpRequest getHttpRequest() {
        return mHttpRequest;
    }

    public void httpRequestReceived(boolean requestReceived){

        if(requestReceived){
            stationDataReq = mHttpRequest.getStationList();//Recuperation de la liste des Stations de la requete

            if(!stationDataReq.isEmpty()) {//Si la liste des stations recup est non vide
//                list = new ArrayList<>();
//                for (StationsVelib station : stationDataReq){ // Parcours des stations de velib dans la list récupérée
//                    ListSample item = new ListSample(
//                            station.getStatus(),
//                            station.getBike_stands(),
//                            station.getAvailable_bike_stands(),
//                            station.getAvailable_bikes(),
//                            station.getAddress(),
//                            station.getPosition());
//
//                    list.add(item);
//                }

            }
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

//        // Get the ViewPager and set it's PagerAdapter so that it can display items
//        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
//        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(),
//                NagivationActivity.this));
//
//        // Give the TabLayout the ViewPager
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
//        tabLayout.setupWithViewPager(viewPager);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), NavigationActivity.this);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        }
        return;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_settings:
                //User chose the "Settings" item, show the app settings UI...
                return true;
            case R.id.action_favorite:
                //User chose the "Favorite" action, mark the current item as favorite...
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
        //if(mPermissionInternet == true) {
            final NavigationActivity activity = this;
            getHttpRequest().LaunchHttpRequest(mRequestQueue, activity, URL);
    }


}
