package corentinulysse.bikegeoapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String ARG_PAGE = "ARG_PAGE";
    private Interface mTunnel;//Interface de communication
    public static ListView mListView;
    private List<StationsVelib> mDatalist;//Liste de station reçu de la requete depuis NavigationActivity
    private ArrayList<ListSample> list = new ArrayList<>();//Entrée du SampleAdapter
    private int mPage;
    private SwipeRefreshLayout swipeRefreshLayout;//Rafraichissement
    private OnFragmentInteractionListener mListener;
    private ListSampleAdapter mAdapter;
    private int i = 0;

    public ListFragment() {
        //keep empty
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page Parameter 1.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ListFragment fragment = new ListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPage=getArguments().getInt(ARG_PAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     //return inflater.inflate(R.layout.fragment_list, container, false);

        View mView = inflater.inflate(R.layout.fragment_list, container, false);
//        TextView textView = (TextView) view; //Fonctionne seulement si la vue est composée seulement d'un textView. Sinon il faudra voir comment récuperer/modifier le texte
//        textView.setText("Ici sera la liste");
        mListView = (ListView) mView.findViewById(listView);
        clickList(mView);
        swipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.f_n_swiperefresh);

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

        mAdapter= new ListSampleAdapter(getActivity(), list);
        mListView.setAdapter(mAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);//On rend disponible la fonction de rafraichissment

        return mView;
    }

    public void clickList(View mView) {//Gestion du clic sur un item de la liste

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(), "Item : "+mDatalist.get(position).getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(getActivity(), DetailsActivity.class);
                intent.putExtra("stationS", mDatalist.get(position));

                startActivity(intent);
            }
        });
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

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

    public void onRefresh() {//Fonction de rafraichissement quand on swipe vers le bas
        swipeRefreshLayout.setRefreshing(true);//On lance l'animation
        mTunnel.sendHttpRequestFromFragment();//On relance une requete http
        mAdapter.notifyDataSetChanged();//On actualise l'adapter
        swipeRefreshLayout.setRefreshing(false);//On stoppe l'animation
    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
