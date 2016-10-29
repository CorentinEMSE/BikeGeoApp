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

public class ListSampleAdapter extends ArrayAdapter<ListSample> {


    public ListSampleAdapter(Context context, List<ListSample> statVelibs) {
        super(context, 0, statVelibs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.data_line, parent, false);
        TextView address = (TextView) itemView.findViewById(R.id.a_n_address);
        TextView position_long = (TextView) itemView.findViewById(R.id.a_n_position_long);
        TextView position_lat = (TextView) itemView.findViewById(R.id.a_n_position_lat);
        TextView status = (TextView) itemView.findViewById(R.id.a_n_status);
        TextView available_bike_stands = (TextView) itemView.findViewById(R.id.a_n_available_bike_stands);
        TextView bike_stands = (TextView) itemView.findViewById(R.id.a_n_bike_stands);
        TextView available_bikes = (TextView) itemView.findViewById(R.id.a_n_available_bikes);
        ImageView velib = (ImageView) itemView.findViewById(R.id.a_n_velib);
        ImageView borne = (ImageView) itemView.findViewById(R.id.a_n_borne);

        ListSample sampleData = getItem(position);

        address.setText(sampleData.getAddress());
        position_long.setText("Longitude : " +sampleData.getPosition()[0]);
        position_lat.setText("Latitude : " +sampleData.getPosition()[1]);

        if (sampleData.getStatus().equals("OPEN")) {
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

        if (sampleData.getAvailable_bike_stands()==0) {
            available_bike_stands.setText("Plus de places...");
        }
        else {
            available_bike_stands.setText(sampleData.getAvailable_bike_stands() + " places");
        }
        bike_stands.setText("Nombre d'emplacements: " +sampleData.getBike_stands());

        if (sampleData.getAvailable_bikes()==0) {
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
