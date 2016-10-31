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

/**
 * Activité qui gère les requetes http et les trois fragments affichant les informations concernant le traitement de ces requetes
 */
public class NavigationActivity extends AppCompatActivity implements Interface, LocationListener {


    /*
    Initialisations
     */
    private static String URL = "http://opendata.paris.fr/explore/dataset/stations-velib-disponibilites-en-temps-reel/download/?format=geojson&timezone=Europe/Berlin";

    private ArrayList<ListSample> list;//Entrée du SampleAdapter
    private List<StationsVelib> stationDataReq;

    private RequestQueue mRequestQueue;
    private HttpRequest mHttpRequest;
    private boolean mFirstRequest = false;
    private PagerAdapter adapter = null;

    /**
     * A la création de l'activité
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        /*
        Ajout de la toolbar
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /**
         * Demande des permissions internet et gps si nécessaire
         */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        } else { //Ici GPS
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1
            );

        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) { //Ici internet

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 2);
        }

        /*
        Initialisations suite
         */

        mFirstRequest = true;
        stationDataReq = new ArrayList<>();


        mRequestQueue = VolleyQueue.getInstance(NavigationActivity.this);
        mHttpRequest = new HttpRequest();

        /*
        Lancement de la premiere requete HTTP
         */

        mHttpRequest.LaunchHttpRequest(mRequestQueue, NavigationActivity.this, URL);

        /*
        Réupération des données de la première requête
         */

        stationDataReq = mHttpRequest.getStationList();





    }

    /**
     * Méthode permettant de récupérer la requete http
     * @return
     */
    public HttpRequest getHttpRequest() {
        return mHttpRequest;
    }

    /**
     * Méthode appelée lorsque la requete est reçue par NavigationActivity
     * @param requestReceived
     */
    public void httpRequestReceived(boolean requestReceived) {

        if (requestReceived) {

            stationDataReq = mHttpRequest.getStationList();//Recuperation de la liste des Stations de la requete
            refreshFavorites(); //On met à jour les favoris aussi

            if (mFirstRequest == false) {

                adapter.pagerAdapterHttpRequestReceived(); //On met à jour ListFragment en passant par son adapter si ce n'est pas la première requete (en cas de premiere requete c'est mis à jour dans la foulée)
            }

    /*
    Initialisation du TabLayout et du viewPager correspondant - Permet de passer d'un fragment à l'autre en faisant glisser l'écran
     */
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


    /**
     * Création du menu/toolbar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Lorsqu'on clique sur un élément de la toolbar
     * @param item sur lequel on clique
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: //Si l'utilisateur souhaite aller dans les favoris
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

    /**
     * Méthode permettant de récuperer la liste des stationsVelib de NavigationActivity
     * @return la liste de stationsVelib
     */
    @Override
    public List<StationsVelib> getStationList() {

        return stationDataReq;
    }

    /**
     * Permet de lancer une requete http depuis un fragment de navigationActivity
     */
    @Override
    public void sendHttpRequestFromFragment() {
        final NavigationActivity activity = this;
        getHttpRequest().LaunchHttpRequest(mRequestQueue, activity, URL);
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    /**
     * Méthode permettant de rafraichir les favoris lorsque l'on veut rafraichir les données
     */
    public void refreshFavorites() {
        ArrayList<StationsVelib> temp = FavoritesStations.getFavorites(getApplicationContext()); //On récupere les favoris dans une variable temporaire
        List<StationsVelib> temp2 = getStationList(); //On récupere la nouvelle liste issue de la requete
        FavoritesStations.removeAllFavorite(getApplicationContext()); //Suppression de tous les favoris
        if(temp!=null) {
            for (StationsVelib station : temp) {
                for (int i = 0; i < temp2.size(); ++i) {
                    if (station.getName().equals(temp2.get(i).getName())) {
                        FavoritesStations.addFavorite(getApplicationContext(), temp2.get(i)); //On ajoute les favoris mis à jours à la place des anciens favoris
                    }
                }
            }
        }
        Toast.makeText(getApplicationContext()
                , "Favoris actualisés"
                , Toast.LENGTH_LONG).show();
    }
}
