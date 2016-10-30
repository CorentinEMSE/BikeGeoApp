package corentinulysse.bikegeoapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private StationsVelib mStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Button boutonBack = (Button) findViewById(R.id.a_d_bBack);

        TextView name = (TextView) findViewById(R.id.a_d_name);
        TextView status = (TextView) findViewById(R.id.a_d_status);
        ImageView velib = (ImageView) findViewById(R.id.a_d_velib);
        ImageView borne = (ImageView) findViewById(R.id.a_d_borne);
        TextView available_bikes = (TextView) findViewById(R.id.a_d_available_bikes);
        TextView available_bike_stands = (TextView) findViewById(R.id.a_d_available_bike_stands);
        TextView address = (TextView) findViewById(R.id.a_d_address);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mStation = (StationsVelib) getIntent().getSerializableExtra("stationS"); //Obtaining data
        }
        Log.d("Coucou : ", mStation.toString());
        boutonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailsActivity.this.finish();
            }
        });

        //Nom
        name.setText(mStation.getName());
        //Statut de la station
        if (mStation.getStatus().equals("OPEN")) {
            status.setTextColor(Color.parseColor("#04B404"));
            status.setText("OUVERTE");
        }
        else {
            status.setTextColor(Color.RED);
            status.setText("FERMÉE");
        }
        //Images
        velib.setImageResource(R.drawable.velib);
        borne.setImageResource(R.drawable.borne);
        //Velib dispos
        available_bikes.setText(""+mStation.getAvailable_bikes());
        //Places dispos
        available_bike_stands.setText(""+mStation.getAvailable_bike_stands());
        //Adresse de la station
        address.setText(mStation.getAddress());

    }

}
