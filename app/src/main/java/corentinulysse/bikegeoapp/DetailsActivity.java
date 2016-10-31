package corentinulysse.bikegeoapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Activité gérant l'affichage des détails d'une station : Des informations utiles ainsi qu'une carte n'affichant que cette station.
 */
public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback, android.location.LocationListener {


    private LocationManager lm;
    private LocationListener locationListener;

    private double latitude;
    private double longitude;
    private double altitude;
    private float accuracy;




    private StationsVelib mStation;
    private Menu mMenu;
    private GoogleMap mGoogleMap;


    /**
     * Executé à la création de l'activité
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        /*Initialisation des données */

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.details_toolbar);
        setSupportActionBar(toolbar);

        /*Ajoute la flèche de retour à l'activité précédente*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        TextView name = (TextView) findViewById(R.id.a_d_name);
        TextView status = (TextView) findViewById(R.id.a_d_status);
        ImageView velib = (ImageView) findViewById(R.id.a_d_velib);
        ImageView borne = (ImageView) findViewById(R.id.a_d_borne);
        TextView available_bikes = (TextView) findViewById(R.id.a_d_available_bikes);
        TextView available_bike_stands = (TextView) findViewById(R.id.a_d_available_bike_stands);
        TextView address = (TextView) findViewById(R.id.a_d_address);

        /*Récupération des données*/
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mStation = (StationsVelib) getIntent().getSerializableExtra("stationS"); //Obtaining data
        }



        //Nom
        name.setText(mStation.getName());
        //Statut de la station
        if (mStation.getStatus().equals("OPEN")) {
            status.setTextColor(Color.parseColor("#04B404"));
            status.setText("OUVERTE");
        }
        else {
            status.setTextColor(Color.RED);
            status.setText("FERMÉE");
        }
        //Images
        velib.setImageResource(R.drawable.velib);
        borne.setImageResource(R.drawable.borne);
        //Velib dispos
        available_bikes.setText(""+mStation.getAvailable_bikes());
        //Places dispos
        available_bike_stands.setText(""+mStation.getAvailable_bike_stands());
        //Adresse de la station
        address.setText(mStation.getAddress());

    }

    /**
     * Initialisation du menu (toolbar)
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail,menu);

        mMenu=menu;
        MenuItem favoritesMenu = mMenu.findItem(R.id.detail_action_favorite);
        final boolean isFavorite = FavoritesStations.isStationInFavorites(mStation,getApplicationContext());

        if(isFavorite){ //On met l'icone de la toolbar en noir si l'élément est déjà dans les favoris, en blanc sinon
            favoritesMenu.setIcon(R.drawable.ic_favorite_black_48dp);
        }
        else{
            favoritesMenu.setIcon(R.drawable.ic_favorite_border_black_48dp);
        }


        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Executé quand on selectionne un élément de la toolbar
     * @param item sur lequel on clique
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home: //Bouton retour
                DetailsActivity.this.finish();
                return super.onOptionsItemSelected(item);
            case R.id.detail_action_settings: //Accès aux favoris
                //User chose the "Settings" item, show the app settings UI...
                Intent intent = new Intent();
                intent.setClass(DetailsActivity.this, FavoritesActivity.class);
                startActivity(intent);
                return true;
            case R.id.detail_action_favorite: //Mettre ou enlever la station des favoris

                final boolean isFavorite= FavoritesStations.isStationInFavorites(mStation,getApplicationContext());

                if(isFavorite){ //Si c'est déjà dans les favoris on l'enlève, sinon on l'ajoute
                    //Remove Favorite
                    FavoritesStations.removeFavorite(getApplicationContext(),mStation);
                    item.setIcon(R.drawable.ic_favorite_border_black_48dp);
                    Toast.makeText(getApplicationContext()
                            , "La station a été supprimée des favoris"
                            , Toast.LENGTH_LONG).show();

                }
                else{
                    //Add Favorite
                    FavoritesStations.addFavorite(getApplicationContext(),mStation);
                    item.setIcon(R.drawable.ic_favorite_black_48dp);
                    Toast.makeText(getApplicationContext()
                            , "La station a été ajoutée en tant que favorite"
                            , Toast.LENGTH_LONG).show();


                }


                //User chose the "Favorite" action, mark the current item as favorite...
                return true;
            default: //If we got here, the user's action was not recognized.
                //Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Quand la carte est prête
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        /*
        Initialisation des variables
         */
        mGoogleMap = googleMap;

        /*
        Activation des boutons de zoom et du compas indiquant le nord
         */

        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);


        //For showing a move to my location button
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }

        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);


        /*
        Initialisation position de l'objet
         */
        LatLng latCourante = new LatLng(mStation.getPosition()[0], mStation.getPosition()[1]);



        /*
        Ajout du marker sur la map
         */
        mGoogleMap.addMarker(new MarkerOptions().position(latCourante).title(mStation.getName()));


        /*
        Déplacement caméra
         */
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latCourante).zoom(15).build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));



    }

    /**
     * OnResume : mise à jour de la localisation
     */
    @Override
    public void onResume() {
        super.onResume();
        super.onResume();
        lm = (LocationManager)getSystemService(LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, (android.location.LocationListener) this);
        if(lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, (android.location.LocationListener) this);


    }

    /**
     * OnPause : on arrête les mise à jour de localisation
     */
    @Override
    public void onPause() {
        super.onPause();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        lm.removeUpdates((android.location.LocationListener) this);
    }

    /**
     * Des qu'on active GPS ou Network, ça l'indique
     * @param provider GPS ou Network
     */
    @Override
    public void onProviderEnabled(String provider){
        String msg = String.format(getResources().getString(R.string.provider_enabled), provider);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    /**
     * Des qu'on désactive GPS ou Network, ça l'indique
     * @param provider GPS ou Network
     */
    @Override
    public void onProviderDisabled(String provider){
        String msg = String.format(getResources().getString(R.string.provider_disabled), provider);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    /**
     * Quand le status change
     * @param provider
     * @param status
     * @param extras
     */
    @Override
    public void onStatusChanged(String provider, int status,Bundle extras ){
        String newStatus = "";
        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                newStatus = "OUT_OF_SERVICE";
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                newStatus = "TEMPORARILY_UNAVAILABLE";
                break;
            case LocationProvider.AVAILABLE:
                newStatus = "AVAILABLE";
                break;
        }

        String msg = String.format(getResources().getString(R.string.provider_new_status), provider,newStatus);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    /**
     * Quand la localisation change
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        altitude = location.getAltitude();
        accuracy = location.getAccuracy();

        String msg = String.format(
                getResources().getString(R.string.new_location), String.valueOf(latitude),
                String.valueOf(longitude),String.valueOf(altitude), String.valueOf(accuracy));
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

    }

}
