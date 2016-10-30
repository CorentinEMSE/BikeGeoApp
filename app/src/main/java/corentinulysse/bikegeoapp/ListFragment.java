package corentinulysse.bikegeoapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
public class ListFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private Interface mTunnel;
    public static ListView mListView;
    private List<StationsVelib> mDatalist;
    private ArrayList<ListSample> list = new ArrayList<>();//Entrée du SampleAdapter
    private int mPage;

   private OnFragmentInteractionListener mListener;

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

        View view = inflater.inflate(R.layout.fragment_list, container, false);
//        TextView textView = (TextView) view; //Fonctionne seulement si la vue est composée seulement d'un textView. Sinon il faudra voir comment récuperer/modifier le texte
//        textView.setText("Ici sera la liste");
        mListView = (ListView) view.findViewById(listView);

        mDatalist = mTunnel.getStationList();
            list = new ArrayList<>();
            for (StationsVelib station : mDatalist){ // Parcours des stations de velib dans la list récupérée
                ListSample item = new ListSample(
                        station.getStatus(),
                        station.getBike_stands(),
                        station.getAvailable_bike_stands(),
                        station.getAvailable_bikes(),
                        station.getAddress(),
                        station.getPosition());

                list.add(item);
            }

        ListSampleAdapter adapter= new ListSampleAdapter(getActivity(), list);
        mListView.setAdapter(adapter);

        return view;
    }

//    public void setA(ListSampleAdapter adapter) {
//        listview.setAdapter(adapter);
//    }



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
            mTunnel = (Interface) context;
        }
        catch(ClassCastException e){
            throw new ClassCastException((getActivity().toString()+"must implement HttpRequest"));
        }
    }

    public void onRefresh() {
        mTunnel.sendHttpRequestFromFragment();
    }



//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

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
