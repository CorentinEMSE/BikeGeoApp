package corentinulysse.bikegeoapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Corentin on 30/10/2016.
 */

public class FavoritesStations {

    public static final String APP = "BIKEGEOAPP";
    public static final String FAVORITES = "STATION_Favorites";

    public FavoritesStations(){
        super();
    }

//    public StationsVelib getOneFavorite(Context context,int position){
//        return getFavorites(context).get(position);
//    }

    public static ArrayList<StationsVelib> getFavorites(Context context){
        SharedPreferences sharedPref;
       List<StationsVelib> favorites;

        sharedPref=context.getApplicationContext().getSharedPreferences(APP,Context.MODE_PRIVATE);

        if (sharedPref.contains(FAVORITES)){
            String jsonFavorites = sharedPref.getString(FAVORITES,null);
            Gson gson = new Gson();
            StationsVelib[] favoritesStation = gson.fromJson(jsonFavorites,StationsVelib[].class);
            favorites= Arrays.asList(favoritesStation);
            favorites=new ArrayList<StationsVelib>(favorites);
        }
        else
        {
            return null;
        }
        return (ArrayList<StationsVelib>) favorites;
    }

    public static void addFavorite(Context context, StationsVelib station){
        List<StationsVelib> favorites = getFavorites(context);
        if(favorites == null)
            favorites = new ArrayList<StationsVelib>();
        favorites.add(station);
        saveFavorites(context, favorites);
//        Toast.makeText(context
//                , "La station a été ajoutée en tant que favorite"
//                , Toast.LENGTH_LONG).show();
    }

    public static void removeAllFavorite(Context context){
        ArrayList<StationsVelib> favorites = getFavorites(context);
        if(favorites != null) {
            favorites.clear();
            saveFavorites(context,favorites);
//            Toast.makeText(context
//                    , "Favoris supprimés"
//                    , Toast.LENGTH_LONG).show();
        }
    }

    public static void removeFavorite(Context context, StationsVelib station){
        ArrayList<StationsVelib> favorites = getFavorites(context);
        StationsVelib stationToDelete=null;
        if(favorites != null){

            for(int i=0; i<favorites.size();++i){
                if(favorites.get(i).getAddress().equals(station.getAddress())){
                    stationToDelete=favorites.get(i);
                }
            }
         if(stationToDelete!=null) {
             favorites.remove(stationToDelete);
             saveFavorites(context, favorites);
//             Toast.makeText(context
//                     , "Piste favorie supprimée"
//                     , Toast.LENGTH_LONG).show();
         }
            else{
//             Toast.makeText(context
//                     , "Piste favorie non supprimée"
//                     , Toast.LENGTH_LONG).show();
         }
        }
    }

    public static void saveFavorites(Context context, List<StationsVelib> favorites){
        SharedPreferences sharedPref;
        SharedPreferences.Editor editor;

        sharedPref = context.getApplicationContext().getSharedPreferences(APP, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        Gson gson = new Gson();
        editor.putString(FAVORITES, gson.toJson(favorites));
        editor.commit();

    }

    public static boolean isStationInFavorites(StationsVelib station,Context context){
        ArrayList<StationsVelib> favorites = getFavorites(context);

        boolean isInFavorite = false;

        if(favorites != null){
            isInFavorite=searchStation(favorites,station);
        }
        else{

        }
        return isInFavorite;
    }

    private static boolean searchStation(ArrayList<StationsVelib> favorites, StationsVelib station){
        boolean retour=false;
        for(int i=0; i<favorites.size();++i){
            if(favorites.get(i).getAddress().equals(station.getAddress())){
                retour=true;
                return retour;
            }
        }
        return retour;
    }
}
