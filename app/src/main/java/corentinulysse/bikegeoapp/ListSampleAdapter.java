package corentinulysse.bikegeoapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Adapter permettant d'afficher des éléments ListSample dans une listView comme celle de ListFragment
 */
public class ListSampleAdapter extends ArrayAdapter<ListSample> {

    /**
     * Constructeur
     * @param context
     * @param statVelibs liste de stations déjà formatées par ListSample
     */
    public ListSampleAdapter(Context context, List<ListSample> statVelibs) {
        super(context, 0, statVelibs);
    }

    /**
     * Obtenir la vue
     * @param position dans la listview
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*
        On prend les éléments du data_line layout que l'on souhaite modifier
         */
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.data_line, parent, false);
        TextView name = (TextView) itemView.findViewById(R.id.a_n_name);
        TextView position_long = (TextView) itemView.findViewById(R.id.a_n_position_long);
        TextView position_lat = (TextView) itemView.findViewById(R.id.a_n_position_lat);
        TextView status = (TextView) itemView.findViewById(R.id.a_n_status);
        TextView available_bike_stands = (TextView) itemView.findViewById(R.id.a_n_available_bike_stands);
        TextView bike_stands = (TextView) itemView.findViewById(R.id.a_n_bike_stands);
        TextView available_bikes = (TextView) itemView.findViewById(R.id.a_n_available_bikes);
        ImageView velib = (ImageView) itemView.findViewById(R.id.a_n_velib);
        ImageView borne = (ImageView) itemView.findViewById(R.id.a_n_borne);

        ListSample sampleData = getItem(position);

        name.setText(sampleData.getName());
        position_long.setText("Longitude : " +sampleData.getPosition()[0]);
        position_lat.setText("Latitude : " +sampleData.getPosition()[1]);

        if (sampleData.getStatus().equals("OPEN")) { //Suivant l'état de la station on l'affiche d'une couleur différente
            status.setTextColor(Color.parseColor("#04B404"));
            status.setText("OUVERTE");
        }
        else {
            status.setTextColor(Color.RED);
            status.setText("FERMÉE");
            available_bike_stands.setVisibility(View.GONE);
            available_bikes.setVisibility(View.GONE);
            velib.setVisibility(View.GONE);
            borne.setVisibility(View.GONE);
        }

        if (sampleData.getAvailable_bike_stands()==0) { //On regarde s'il reste des places pour garer les vlibs
            available_bike_stands.setText("Plus de places...");
        }
        else {
            available_bike_stands.setText(sampleData.getAvailable_bike_stands() + " places");
        }
        bike_stands.setText("Nombre d'emplacements: " +sampleData.getBike_stands());

        if (sampleData.getAvailable_bikes()==0) { //On regarde s'il reste des vlibs
            available_bikes.setText("Aucun Velib'...");
        }
        else {
            available_bikes.setText(sampleData.getAvailable_bikes() + " libres");
        }

        velib.setImageResource(R.drawable.velib);
        borne.setImageResource(R.drawable.borne);

        return itemView;
    }
}
