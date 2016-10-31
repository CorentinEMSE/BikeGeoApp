package corentinulysse.bikegeoapp;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ulysse on 27/10/2016.
 */

/**
 * Classe gérant les requêtes http issues de l'activité FavoritesActivity
 */
public class FavoriteHttpRequest {

    /*
    Initialisations
     */
    private List<StationsVelib> stationData;

    /**
     * Constructeur
     */
    public FavoriteHttpRequest() {
        this.stationData =  new ArrayList<>();
    }


    /**
     * Méthode lancant la requete http avec comme parametre :
     * @param requestQueue la file de requête
     * @param activity la FavoritesActivity voulant cette requete
     * @param URL url de la requete
     */
    public void LaunchHttpRequest(RequestQueue requestQueue, final FavoritesActivity activity, String URL) {


        stationData.clear(); // mise a jour de la liste


/*
Requete Json
 */
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray jsonArray = response.getJSONArray("features"); // On récupère les données après le champ "Features" dans notre json

                            Gson gson = new GsonBuilder().create();

                            for(int i = 0; i < jsonArray.length(); ++i){ // Parcours des différents tableaux "properties" dans "features"

                                JSONObject features = jsonArray.getJSONObject(i); // Selection d'un tableau "properties" parmi tous les "features"
                                String properties = features.getString("properties"); // Parcours des éléments dans la liste Properties du fichier json

                                StationsVelib stationRecup = gson.fromJson(properties, StationsVelib.class);

                                getStationList().add(stationRecup); // Ajout de la station dans la liste des stations à afficher
                            }



                            Toast.makeText(activity, "Données actualisées", Toast.LENGTH_SHORT).show();
                            activity.httpRequestReceived(true);//Envoi à l'activité la notification de la réception de la requete
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() { // Erreur dans la requete

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        activity.httpRequestReceived(false);
                        Toast.makeText(activity, "Impossible de récupérer les données, veuillez retourner à l'écran précédent et réessayer", Toast.LENGTH_SHORT).show();

                    }
                });

        requestQueue.add(jsObjRequest); // Lancement de la requete
    }

    /**
     * Obtenir la liste de stations de la classe FavoritesHttpRequest
     * @return La liste de stations
     */
    public List<StationsVelib> getStationList() {
        return stationData;
    }
}
