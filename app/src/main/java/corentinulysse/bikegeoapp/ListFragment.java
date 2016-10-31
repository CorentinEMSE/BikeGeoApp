package corentinulysse.bikegeoapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static corentinulysse.bikegeoapp.R.id.listView;


/**
 * Fragment affichant la liste des stations de vlib
 */
public class ListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    /*
    Initialisation
     */
    public static final String ARG_PAGE = "ARG_PAGE";
    private Interface mTunnel;//Interface de communication
    public static ListView mListView;
    private List<StationsVelib> mDatalist;//Liste de station reçu de la requete depuis NavigationActivity
    private ArrayList<ListSample> list = new ArrayList<>();//Entrée du SampleAdapter
    private int mPage;
    private SwipeRefreshLayout swipeRefreshLayout;//Rafraichissement

    private ListSampleAdapter mAdapter;
    private int i = 0;

    private boolean clickable;

    /**
     * Constructeur
     */
    public ListFragment() {
        //keep empty
    }


    /**
     * Permet d'ajouter une nouvelle instance de listFragment
     * @param page position dans le tablayout
     * @return
     */
    public static ListFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ListFragment fragment = new ListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * A la création du fragment
     * @param savedInstanceState
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPage=getArguments().getInt(ARG_PAGE);
        }
    }

    /**
     * Lorsque le fragment est crée, on initalise le swiperefreshlayout et on affiche la premièe liste dans le listview grace à manageFragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View mView = inflater.inflate(R.layout.fragment_list, container, false);

        mListView = (ListView) mView.findViewById(listView);
        /*
        Ajout d'un listener pour savoir quand on clique sur un item de la liste
         */
        clickList(mView);
        swipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.f_n_swiperefresh);

        manageFragment();

        swipeRefreshLayout.setOnRefreshListener(this);//On rend disponible la fonction de rafraichissment
        clickable=true;
        return mView;
    }

    /**
     * Gestion du clic sur un item de la liste pour lancer l'activité détail
     * @param mView
     */
    public void clickList(View mView) {//Gestion du clic sur un item de la liste

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(clickable) {

                    Intent intent = new Intent();
                    intent.setClass(getActivity(), DetailsActivity.class);
                    intent.putExtra("stationS", mDatalist.get(position));


                    startActivity(intent);
                }

            }
        });
    }


/*
Permet d'initialiser le tunnel
 */
    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        try {
            mTunnel = (Interface) context;//On créé l'interface pour partager les méthodes
        }
        catch(ClassCastException e){
            throw new ClassCastException((getActivity().toString()+"must implement HttpRequest"));
        }
    }

    /**
     * Lorsque l'utilisateur souhaite emttre à jour les données, cette méthode est appelée
     */
    public void onRefresh() {//Fonction de rafraichissement quand on swipe vers le bas
        clickable=false; //On ne peut plus cliquer sur les éléments de la liste
        setSwipeRefreshLayoutTrue(); //On appelle le swiperefreshlayout
        mTunnel.sendHttpRequestFromFragment();//On relance une requete http

    }

    /**
     * Permet de mettre à jour l'affichage sur la listview en traitant les données issues de la requete que l'on affiche grace à un adapter
     */
    public void manageFragment(){
        mDatalist = mTunnel.getStationList();//Récupération de la liste de stations issue de la requete
        list = new ArrayList<>();//Initialisation de list pour l'affichage
        if(!mDatalist.isEmpty()) {//Si la liste n'est pas vide
            for (StationsVelib station : mDatalist) { // Parcours des stations de velib dans la list récupérée
                ListSample item = new ListSample(
                        station.getStatus(),
                        station.getBike_stands(),
                        station.getAvailable_bike_stands(),
                        station.getAvailable_bikes(),
                        station.getName(),
                        station.getPosition());

                list.add(item);
                i=i+1;
            }
            Log.d("NombredeStations : ", ""+i);//Nombre de stations
        }
        else {
            Toast.makeText(getActivity(), "La liste récupérée est vide", Toast.LENGTH_SHORT).show();
        }

        mAdapter= new ListSampleAdapter(getActivity(), list); //Déclaration de l'adapter avec la list précédemment obtenue
        mListView.setAdapter(mAdapter); //On affecte cet adapter à la listview pour pouvoir afficher
    }

    /**
     * Méthode affichant le swipeRefreshLayout
     */
    public void setSwipeRefreshLayoutTrue(){
        swipeRefreshLayout.setRefreshing(true);//On lance l'animation

    }

    /**
     * Méthode enlevant le swipeRefreshLayout
     */
    public void setSwipeRefreshLayoutFalse(){
        swipeRefreshLayout.setRefreshing(false);//On stoppe l'animation

    }

    /**
     * Méthode appelée lorsque l'on reçoit la requete http
     */
    public void listfragmentOnHttpRequestReceived(){
        manageFragment(); //On met a jour l'affichage des données
        clickable=true; //On redonne la possibilité de cliquer sur la liste
        setSwipeRefreshLayoutFalse(); //On enleve le swipeRefreshLayout
    }




}
