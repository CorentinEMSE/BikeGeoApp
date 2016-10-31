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


    public Map2Fragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_map2, container, false);

        mTargetItem = 0;
        mDataListe = mTunnel.getStationList();


        double[] tempInit = mDataListe.get(mTargetItem).getPosition();
        mTargetMarker = new LatLng(tempInit[0], tempInit[1]);

        // mMapView = (MapView)rootView.findViewById()

        mMessageChargement = (TextView) rootView.findViewById(R.id.f_Map2Fragment_messageMapChargement);
        mMessageChargement.setText("La carte est en cours de chargement ...");

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.f_Map2Fragment_progressBarMap);
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setIndeterminate(false);
        mProgressBar.setProgress(0);
        mProgressBar.setMax(mDataListe.size());

        //public final class MapsInitializer extends Object
        // Use this class to initialize the Google Maps Android API if features need to be used before obtaining a map. It must be called because some classes such as BitmapDescriptorFactory and CameraUpdateFactory need to be initialized.
        //If you are using MapFragment or MapView and have already obtained a (non-null) GoogleMap by calling getMapAsync() on either of these classes and waiting for the onMapReady(GoogleMap map) callback, then you do not need to worry about this class.

        //MapsInitializer.initialize(getActivity().getApplicationContext());

//        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,this);

        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);


        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String name=marker.getTitle();
                StationsVelib stationATransmettre=null;
                for (int i = 0; i < mDataListe.size(); ++i) {
                    StationsVelib temp=mDataListe.get(i);
                    if (name.equals(temp.getName())){
                        stationATransmettre=temp;
                    }
                }
                if(stationATransmettre!=null) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), DetailsActivity.class);
                    intent.putExtra("stationS", stationATransmettre);


                    startActivity(intent);
                }
            }
        });


        //mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }


//        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }

        //For showing a move to my location button
        if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            // return;
        }

//        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
//
//        }
        mGoogleMap.setMyLocationEnabled(true);


        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

        for (StationsVelib dataCourante : mDataListe) {
            LatLng latCourante = new LatLng(dataCourante.getPosition()[0], dataCourante.getPosition()[1]);

            //TODO A voir si on ajoute un test sur l'unicité du marker

            //TODO Verifier getName fonctionne (Name ajouté il y a peu) et ajouter Name dans la liste
            mGoogleMap.addMarker(new MarkerOptions().position(latCourante).title(dataCourante.getName()).snippet("Adresse :" + dataCourante.getAddress()));

        }

        mMessageChargement.setText("");
        mProgressBar.setVisibility(View.GONE);

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mGoogleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //LatLng gardanne=new LatLng(-43.45,5.4667);
        //mGoogleMap.addMarker(new MarkerOptions().position(gardanne).title("Mines St Etienne").snippet("QG des ISMINs")/*.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_call_received_black_24dp))*/);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(mTargetMarker).zoom(15).build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        // mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(mTargetMarker));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mTunnel = (Interface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException((getActivity().toString() + "must implement HttpRequest"));
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        super.onResume();
            lm = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
            if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
//                return;
            }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, (android.location.LocationListener) this);
        if(lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, (android.location.LocationListener) this);


    }

    @Override
    public void onPause() {
        super.onPause();

        if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
//            return;
        }
        lm.removeUpdates((android.location.LocationListener) this);
    }

    @Override
    public void onProviderEnabled(String provider){
        String msg = String.format(getResources().getString(R.string.provider_enabled), provider);
        Toast.makeText(this.getContext(), msg, Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onProviderDisabled(String provider){
        String msg = String.format(getResources().getString(R.string.provider_disabled), provider);
        Toast.makeText(this.getContext(), msg, Toast.LENGTH_SHORT).show();

    }

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

    public void map2fragmentOnHttpRequestReceived(){
//       mTunnel.refreshFavorites();
        manageFragment();
//        mAdapter.notifyDataSetChanged();//On actualise l'adapter

    }

    public void manageFragment(){


        mMessageChargement.setText("La carte est en cours de chargement ...");

        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setIndeterminate(false);
        mProgressBar.setProgress(0);
        mProgressBar.setMax(mDataListe.size());

        mTargetItem = 0;
        mDataListe = mTunnel.getStationList();


        double[] tempInit = mDataListe.get(mTargetItem).getPosition();
        mTargetMarker = new LatLng(tempInit[0], tempInit[1]);

        mGoogleMap.clear();

        for (StationsVelib dataCourante : mDataListe) {
            LatLng latCourante = new LatLng(dataCourante.getPosition()[0], dataCourante.getPosition()[1]);

            //TODO A voir si on ajoute un test sur l'unicité du marker

            //TODO Verifier getName fonctionne (Name ajouté il y a peu) et ajouter Name dans la liste
            mGoogleMap.addMarker(new MarkerOptions().position(latCourante).title(dataCourante.getName()).snippet("Vélibs disponibles : " + dataCourante.getAvailable_bikes()+" et supports disponibles : "+dataCourante.getAvailable_bike_stands()));

        }

        mMessageChargement.setText("");
        mProgressBar.setVisibility(View.GONE);

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mGoogleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //LatLng gardanne=new LatLng(-43.45,5.4667);
        //mGoogleMap.addMarker(new MarkerOptions().position(gardanne).title("Mines St Etienne").snippet("QG des ISMINs")/*.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_call_received_black_24dp))*/);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(mTargetMarker).zoom(15).build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        Toast.makeText(getActivity(), "Map mise à jour", Toast.LENGTH_SHORT).show();
    }




}



//import android.support.v4.app.Fragment;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//public class Map2Fragment extends Fragment implements OnMapReadyCallback {
//    private GoogleMap mGoogleMap;
//    public static Map2Fragment newInstance() {
//        Map2Fragment fragment = new Map2Fragment();
//        return fragment;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_map2, null, false);
//
//        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//
//        return view;
//    }
//
//
//    /**
//     * Manipulates the map once available.
//     * This callback is triggered when the map is ready to be used.
//     * This is where we can add markers or lines, add listeners or move the camera. In this case,
//     * we just add a marker near Sydney, Australia.
//     * If Google Play services is not installed on the device, the user will be prompted to install
//     * it inside the SupportMapFragment. This method will only be triggered once the user has
//     * installed Google Play services and returned to the app.
//     */
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mGoogleMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mGoogleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }
//}