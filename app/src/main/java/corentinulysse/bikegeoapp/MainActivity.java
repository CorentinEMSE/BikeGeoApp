package corentinulysse.bikegeoapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Activité de base où l'utilisateur peut choisir de se renseigner sur ce qu'est un vlib ou lancer l'application en elle-même
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Initialisation des deux boutons et de l'image de l'application
         */
        Button mButtonNav = (Button) findViewById(R.id.a_m_bNav);
        Button mButtonYT = (Button) findViewById(R.id.a_m_bYT);
        ImageView velibLogo = (ImageView) findViewById(R.id.a_d_velibLogo);
        velibLogo.setImageResource(R.drawable.velib_logo);

        mButtonNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //On lance l'activité NavigationActivity qui gère vraiment l'application : Obtention de la liste de stations, calculs et affichage de ce que l'on souhaite.
                Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
                startActivity(intent);
            }
        });

        mButtonYT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Si l'utilisateur souhaite se renseigner sur les vélibs, ouverture de youtube.
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=viNXFoKjoBo")));
            }
        });

    }

}
