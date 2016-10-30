package corentinulysse.bikegeoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static corentinulysse.bikegeoapp.R.id.favorites_listView;

public class FavoritesActivity extends AppCompatActivity {

    private List<StationsVelib> mList;
    private FrameLayout frame;
    private ListFragment listFragment;
    private ListView mListView;
    private ArrayList<ListSample> list = new ArrayList<>();//Entrée du SampleAdapter
    private ListSampleAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        mListView = (ListView) findViewById(favorites_listView);


        Toolbar toolbar = (Toolbar) findViewById(R.id.favorites_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        frame = (FrameLayout) findViewById(R.id.favorites_frame);


        manageFragment();







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
            //TODO ERREUR
        }

        //TODO METTRE LE FRAGMENT DANS LE FRAME LAYOUT

//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.favorites_frame,listFragment);
//        ft.addToBackStack(null);
//        ft.commit();
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
                        , "Favoris supprimés et notifés"
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
