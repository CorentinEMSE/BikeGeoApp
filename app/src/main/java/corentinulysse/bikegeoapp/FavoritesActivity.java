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

/**
 * Activité gérant l'affichage des stations favorites
 */
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





    private SwipeRefreshLayout swipeRefreshLayout;//Rafraichissement


    /**
     * A la création de l'activité
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);


        /*
        Récupération de la listview que l'on souhaite modifier dans le layout
         */
        mListView = (ListView) findViewById(favorites_listView);

        /*
        Initialisation de la toolbar avec le bouton de retour
         */

        Toolbar toolbar = (Toolbar) findViewById(R.id.favorites_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /*
        Initialisation de la requête http correspondant aux favoris
         */
        mRequestQueue = VolleyQueue.getInstance(FavoritesActivity.this);
        mHttpRequest = new FavoriteHttpRequest();


        /*
        Permet d'ajouter un listener pour surveiller le clic sur la liste
         */
        clickList();

        /*
        Implémentation de l'outit de refresh
         */
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.favorites_swiperefresh);


        /*
        Mise à jour de la listview
         */
        manageFragment();



        swipeRefreshLayout.setOnRefreshListener(this);//On rend disponible la fonction de rafraichissment

        clickable=true;




    }

    /**
     * Gestion du clic sur un item de la liste pour lancer l'activité détail
     */
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

    /**
     * OnRefresh on initialise le swiperefreshlayout puis on fait une requête http
     */
    @Override
    public void onRefresh() {
        clickable=false; //On n'autorise plus pendant le refresh à cliquer sur la lsite
        setSwipeRefreshLayoutTrue();
        mHttpRequest.LaunchHttpRequest(mRequestQueue, FavoritesActivity.this, URL);


    }

    /**
     * Méthode appelant le swiperefreshlayout
     */
    public void setSwipeRefreshLayoutTrue(){
        swipeRefreshLayout.setRefreshing(true);//On lance l'animation

    }

    /**
     * Méthode enlevant le swiperefreshlayout
     */
    public void setSwipeRefreshLayoutFalse(){
        swipeRefreshLayout.setRefreshing(false);//On stoppe l'animation

    }


    /**
     * Méthode appelée lorsque la requête http est reçue
     * @param requestReceived
     */
    public void httpRequestReceived(boolean requestReceived) {

        if (requestReceived) {

            stationDataReq = mHttpRequest.getStationList();//Recuperation de la liste des Stations de la requete
            refreshFavorites(); //On met à jour les favoris
            manageFragment(); //On met à jour l'affichage de ceux-ci
            clickable=true; //On autorise de nouveau le fait de pouvoir cliquer sur la liste
           setSwipeRefreshLayoutFalse();

        }
        return;
    }

    /**
     * Méthode permettant de mettre à jour les favoris, voir dans NavigationActivity la même méthode pour plus de commentaires
     */
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

    }


    /**
     * Méthode permettant de mettre à jour l'affichage des favoris
     */
    public void manageFragment(){
        ArrayList<StationsVelib> list2 = FavoritesStations.getFavorites(getApplicationContext());
        mList = list2;
        if(mList.size() > 0) {

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

    /**
     * Méthode initialisant le menu (toolbar)
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_favorite,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Méthode gérant l'appui sur un item de la toolbar par l'utilisateur
     * @param item Soit retour à l'activité précédente, soit suppression de tous les favoris
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home: //bouton retour à l'activité précédente
                FavoritesActivity.this.finish();
                return super.onOptionsItemSelected(item);

            case R.id.action_settings: //Option permettant de supprimer tous les favoris
                FavoritesStations.removeAllFavorite(getApplicationContext());
                manageFragment();
                mAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext()
                        , "Favoris supprimés"/* et notifés"*/
                        , Toast.LENGTH_LONG).show();



                return true;
            default: //If we got here, the user's action was not recognized.
                //Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }


}
