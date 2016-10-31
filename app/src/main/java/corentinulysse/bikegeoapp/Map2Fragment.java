package corentinulysse.bikegeoapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;
import static corentinulysse.bikegeoapp.R.id.map;

/**
 * Fragment gérant l'affichage des stations sur une googlemap
 */
public class Map2Fragment extends Fragment implements OnMapReadyCallback, android.location.LocationListener {

    private LocationManager lm;
    private LocationListener locationListener;

    private double latitude;
    private double longitude;
    private double altitude;
    private float accuracy;

    private Interface mTunnel;

    private List<StationsVelib> mDataListe;
    private List<String> mMarkerUniciteListe; // sert a verifier l'unicite des markers affiches par nom de voie. Objectif : réduction du nombre de markers affiches


    private int mTargetItem; //Index de l'item sur lequel la map est centrée par défaut
    private LatLng mTargetMarker; //marker de l'item cible

    private MapView mMapView;
    private GoogleMap mGoogleMap;

    private ProgressBar mProgressBar;
    private TextView mMessageChargement;


    /**
     * Constructeur
     */
    public Map2Fragment() {

    }


    /**
     * Lorsque la vue est crée
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /*
        Initialisations
         */

        View rootView = inflater.inflate(R.layout.fragment_map2, container, false);

        mTargetItem = 0;
        mDataListe = mTunnel.getStationList();


        double[] tempInit = mDataListe.get(mTargetItem).getPosition();
        mTargetMarker = new LatLng(tempInit[0], tempInit[1]);


        /*
        Message et progressbar s'affichant pendant le chargement de la carte si nécessaire
         */

        mMessageChargement = (TextView) rootView.findViewById(R.id.f_Map2Fragment_messageMapChargement);
        mMessageChargement.setText("La carte est en cours de chargement ...");

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.f_Map2Fragment_progressBarMap);
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setIndeterminate(false);
        mProgressBar.setProgress(0);
        mProgressBar.setMax(mDataListe.size());



        return rootView;
    }


    /**
     * Lorsque la vue est crée on place un SupportMapFragment, soit une google map, dans notre fragment
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);


    }

    /**
     * Lorsque la carte est prête
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        /*
        Ajout du bouton de zoom et du compass indiquant le nord
         */

        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);


    /**
     * Ajout d'un listener afin de contrôler si l'utilisateur clique sur un marker, dans ce cas on ouvre une Details Activity
     */
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String name=marker.getTitle();
                StationsVelib stationATransmettre=null;
                for (int i = 0; i < mDataListe.size(); ++i) { //Recherche de la station à transmettre dans la liste des stations grâce au nom du marker
                    StationsVelib temp=mDataListe.get(i);
                    if (name.equals(temp.getName())){
                        stationATransmettre=temp; //On a trouvé la station dont on veut des détails
                    }
                }
                if(stationATransmettre!=null) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), DetailsActivity.class);
                    intent.putExtra("stationS", stationATransmettre);


                    startActivity(intent); //Ouverture de l'activity détails
                }
            }
        });




        //For showing a move to my location button
        if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }


        mGoogleMap.setMyLocationEnabled(true);


        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

        for (StationsVelib dataCourante : mDataListe) { //Ajout des markers correspondant à la liste
            LatLng latCourante = new LatLng(dataCourante.getPosition()[0], dataCourante.getPosition()[1]);




            mGoogleMap.addMarker(new MarkerOptions().position(latCourante).title(dataCourante.getName()).snippet("Adresse :" + dataCourante.getAddress()));

        }

        /*
        On enleve les messages/progressbar de chargement
         */
        mMessageChargement.setText("");
        mProgressBar.setVisibility(View.GONE);


        /*
        Mise à jour de la position caméra
         */
        CameraPosition cameraPosition = new CameraPosition.Builder().target(mTargetMarker).zoom(15).build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }

    /**
     * OnAttach, récupération du tunnel
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mTunnel = (Interface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException((getActivity().toString() + "must implement HttpRequest"));
        }
    }


    /**
     * OnResume pour les mise à jour de localisation
     */
    @Override
    public void onResume() {
        super.onResume();
        super.onResume();
            lm = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
            if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, (android.location.LocationListener) this);
        if(lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, (android.location.LocationListener) this);


    }

    /**
     * OnPause pour arreter les mises à jour de localisation
     */
    @Override
    public void onPause() {
        super.onPause();

        if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        lm.removeUpdates((android.location.LocationListener) this);
    }

    /**
     * Pour savoir si une connexion GPS ou Network est disponible
     * @param provider Network ou GPS
     */
    @Override
    public void onProviderEnabled(String provider){
        String msg = String.format(getResources().getString(R.string.provider_enabled), provider);
        Toast.makeText(this.getContext(), msg, Toast.LENGTH_SHORT).show();

    }


    /**
     * Pour savoir si une connexion GPS ou Network n'est plus disponible
     * @param provider GPS ou Network
     */
    @Override
    public void onProviderDisabled(String provider){
        String msg = String.format(getResources().getString(R.string.provider_disabled), provider);
        Toast.makeText(this.getContext(), msg, Toast.LENGTH_SHORT).show();

    }

    /**
     * Si le status change
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
        Toast.makeText(this.getContext(), msg, Toast.LENGTH_SHORT).show();

    }

    /**
     * Si la localisation change
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
        Toast.makeText(this.getContext(), msg, Toast.LENGTH_LONG).show();

    }

    /**
     * Lorsque l'on reçoit le résultat d'une requête http (demandée par listFragment par exemple), on modifie la map pour correspondre à cette mise à jour
     */
    public void map2fragmentOnHttpRequestReceived(){

        manageFragment();


    }

    /**
     * Mise à jour de la map avec la nouvelle liste de stations mise à jour suite à la requete
     */
    public void manageFragment(){


        //On dit que la map change
        mMessageChargement.setText("La carte est en cours de chargement ...");

        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setIndeterminate(false);
        mProgressBar.setProgress(0);
        mProgressBar.setMax(mDataListe.size());

        mTargetItem = 0;
        mDataListe = mTunnel.getStationList();


        double[] tempInit = mDataListe.get(mTargetItem).getPosition();
        mTargetMarker = new LatLng(tempInit[0], tempInit[1]);

        mGoogleMap.clear(); //On enlève tous les précédents markers

        /*
        On ajoute les marker de chaque station vélib qui sont maintenant mis à jour
         */

        for (StationsVelib dataCourante : mDataListe) {
            LatLng latCourante = new LatLng(dataCourante.getPosition()[0], dataCourante.getPosition()[1]);


            mGoogleMap.addMarker(new MarkerOptions().position(latCourante).title(dataCourante.getName()).snippet("Vélibs disponibles : " + dataCourante.getAvailable_bikes()+" et supports disponibles : "+dataCourante.getAvailable_bike_stands()));

        }

        /*
        On indique que le chargment de la carte est terminé
         */
        mMessageChargement.setText("");
        mProgressBar.setVisibility(View.GONE);


    /*
    On recentre la caméra sur le premier élément de la liste (donc le premier élément de listFragment aussi)
     */
        CameraPosition cameraPosition = new CameraPosition.Builder().target(mTargetMarker).zoom(15).build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        Toast.makeText(getActivity(), "Map mise à jour", Toast.LENGTH_SHORT).show();
    }




}



