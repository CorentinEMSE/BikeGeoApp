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

/**
 * Classe gérant les favoris grâce à SharedPreferences
 */
public class FavoritesStations {
    /*
    Initialisations
     */

    public static final String APP = "BIKEGEOAPP";
    public static final String FAVORITES = "STATION_Favorites";

    /**
     * Constructeur de FavoritesStations
     */
    public FavoritesStations(){
        super();
    }


    /**
     * Méthode permettant de récuperer les favoris
     * @param context
     * @return la liste des stations favorites
     */
    public static ArrayList<StationsVelib> getFavorites(Context context){
        SharedPreferences sharedPref;
       List<StationsVelib> favorites;

        sharedPref=context.getApplicationContext().getSharedPreferences(APP,Context.MODE_PRIVATE);

        if (sharedPref.contains(FAVORITES)){ //S'il y a des favoris
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

    /**
     * Méthode permettant d'ajouter une station aux favoris
     * @param context
     * @param station à ajouter aux favoris
     */
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

    /**
     * Méthode permettant d'effacer tous les favoris
     * @param context
     */
    public static void removeAllFavorite(Context context){
        ArrayList<StationsVelib> favorites = getFavorites(context);
        if(favorites != null) {
            favorites.clear(); //Enlever tous les favoris
            saveFavorites(context,favorites);
//            Toast.makeText(context
//                    , "Favoris supprimés"
//                    , Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Méthode permettant d'enlever une station des favoris
     * @param context
     * @param station à enlever des favoris
     */
    public static void removeFavorite(Context context, StationsVelib station){
        ArrayList<StationsVelib> favorites = getFavorites(context);
        StationsVelib stationToDelete=null;
        if(favorites != null){ //S'il y a des favoris

            for(int i=0; i<favorites.size();++i){ //On regarde tous les favoris
                if(favorites.get(i).getAddress().equals(station.getAddress())){ //Afin de trouver celui qu'on veut supprimer
                    stationToDelete=favorites.get(i);
                }
            }
         if(stationToDelete!=null) {
             favorites.remove(stationToDelete); //On supprime
             saveFavorites(context, favorites);
//             Toast.makeText(context
//                     , "La station a été supprimée des favoris"
//                     , Toast.LENGTH_LONG).show();
         }
            else{
//             Toast.makeText(context
//                     , "La station n'a pas pu être supprimée"
//                     , Toast.LENGTH_LONG).show();
         }
        }
    }

    /**
     * Méthode permettant de sauvegarder les favoris
     * @param context
     * @param favorites que l'on souhaite sauvegarder
     */
    public static void saveFavorites(Context context, List<StationsVelib> favorites){
        SharedPreferences sharedPref;
        SharedPreferences.Editor editor;

        sharedPref = context.getApplicationContext().getSharedPreferences(APP, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        Gson gson = new Gson();
        editor.putString(FAVORITES, gson.toJson(favorites));
        editor.commit();

    }

    /**
     * Méthode permettant de savoir si une station est dans les favoris
     * @param station dont on cherche à savoir si elle est dans les favoris
     * @param context
     * @return true si ça appartient au favoris, false sinon
     */
    public static boolean isStationInFavorites(StationsVelib station,Context context){
        ArrayList<StationsVelib> favorites = getFavorites(context);

        boolean isInFavorite = false;

        if(favorites != null){ //Si on a des favoris
            isInFavorite=searchStation(favorites,station);
        }
        else{

        }
        return isInFavorite;
    }

    /**
     * Recherche une station dans les favoris
     * @param favorites : les favoris
     * @param station : station à chercher
     * @return true si la station est dans les favoris
     */
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
