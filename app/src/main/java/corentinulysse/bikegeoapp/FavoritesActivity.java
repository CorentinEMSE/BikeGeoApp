package corentinulysse.bikegeoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.List;

import static corentinulysse.bikegeoapp.R.id.favorites_listView;

public class FavoritesActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private static String URL = "http://opendata.paris.fr/explore/dataset/stations-velib-disponibilites-en-temps-reel/download/?format=geojson&timezone=Europe/Berlin";


    private List<StationsVelib> mList;
    private FrameLayout frame;
    private ListFragment listFragment;
    private ListView mListView;
    private ArrayList<ListSample> list = new ArrayList<>();//Entrée du SampleAdapter
    private ListSampleAdapter mAdapter;
    private boolean clickable;
    private List<StationsVelib> stationDataReq;

    private RequestQueue mRequestQueue;
    private FavoriteHttpRequest mHttpRequest;




//    private Interface mTunnel;
    private SwipeRefreshLayout swipeRefreshLayout;//Rafraichissement



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);



        mListView = (ListView) findViewById(favorites_listView);


        Toolbar toolbar = (Toolbar) findViewById(R.id.favorites_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mRequestQueue = VolleyQueue.getInstance(FavoritesActivity.this);
        mHttpRequest = new FavoriteHttpRequest();

//        frame = (FrameLayout) findViewById(R.id.favorites_frame);

//        View mView = findViewById(R.layout.fragment_list);


        clickList();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.favorites_swiperefresh);



        manageFragment();



        swipeRefreshLayout.setOnRefreshListener(this);//On rend disponible la fonction de rafraichissment

        clickable=true;




    }

    public void clickList() {//Gestion du clic sur un item de la liste

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(clickable) {
                    //Toast.makeText(getActivity(), "Item : "+mDatalist.get(position).getName(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), DetailsActivity.class);
                    intent.putExtra("stationS", FavoritesStations.getFavorites(getApplicationContext()).get(position));


                    startActivity(intent);
                }

            }
        });
    }

    @Override
    public void onRefresh() {
        clickable=false;
        setSwipeRefreshLayoutTrue();
        mHttpRequest.LaunchHttpRequest(mRequestQueue, FavoritesActivity.this, URL);

//        mTunnel.sendHttpRequestFromFragment();
    }

    public void setSwipeRefreshLayoutTrue(){
        swipeRefreshLayout.setRefreshing(true);//On lance l'animation

    }

    public void setSwipeRefreshLayoutFalse(){
        swipeRefreshLayout.setRefreshing(false);//On stoppe l'animation

    }

//    public void listfragmentOnHttpRequestReceived(){
////       mTunnel.refreshFavorites();
//        manageFragment();
////        mAdapter.notifyDataSetChanged();//On actualise l'adapter
//        clickable=true;
//        setSwipeRefreshLayoutFalse();
//    }

    public void httpRequestReceived(boolean requestReceived) {

        if (requestReceived) {

            stationDataReq = mHttpRequest.getStationList();//Recuperation de la liste des Stations de la requete
            refreshFavorites();
            manageFragment();
            clickable=true;
           setSwipeRefreshLayoutFalse();

        }
        return;
    }

    public void refreshFavorites() {
        ArrayList<StationsVelib> temp = FavoritesStations.getFavorites(getApplicationContext());
        FavoritesStations.removeAllFavorite(getApplicationContext());
        if(temp!=null) {
            for (StationsVelib station : temp) {
                for (int i = 0; i < stationDataReq.size(); ++i) {
                    if (station.getName().equals(stationDataReq.get(i).getName())) {
                        FavoritesStations.addFavorite(getApplicationContext(), stationDataReq.get(i));
                    }
                }
            }
        }
//        Toast.makeText(getApplicationContext()
//                , "Favoris actualisés"
//                , Toast.LENGTH_LONG).show();
    }



    public void manageFragment(){
        ArrayList<StationsVelib> list2 = FavoritesStations.getFavorites(getApplicationContext());
        mList = list2;
        if(mList.size() > 0) {
//
            list = new ArrayList<>();//Initialisation de list pour l'affichage
            if(!mList.isEmpty()) {//Si la liste n'est pas vide
                for (StationsVelib station : mList) { // Parcours des stations de velib dans la list récupérée
                    ListSample item = new ListSample(
                            station.getStatus(),
                            station.getBike_stands(),
                            station.getAvailable_bike_stands(),
                            station.getAvailable_bikes(),
                            station.getName(),
                            station.getPosition());

                    list.add(item);
                }
            }
            else {
                Toast.makeText(FavoritesActivity.this, "La liste récupérée est vide", Toast.LENGTH_SHORT).show();
            }

            mAdapter= new ListSampleAdapter(this, list);
            mListView.setAdapter(mAdapter);

        }
        else {
            list = new ArrayList<>();
            mAdapter= new ListSampleAdapter(this, list);
            mListView.setAdapter(mAdapter);

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_favorite,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                FavoritesActivity.this.finish();
                return super.onOptionsItemSelected(item);

            case R.id.action_settings:
                FavoritesStations.removeAllFavorite(getApplicationContext());
                manageFragment();
                mAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext()
                        , "Favoris supprimés"/* et notifés"*/
                        , Toast.LENGTH_LONG).show();



                return true;
//            case R.id.action_favorite:
//                //User chose the "Favorite" action, mark the current item as favorite...
//                return true;
            default: //If we got here, the user's action was not recognized.
                //Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }


}
