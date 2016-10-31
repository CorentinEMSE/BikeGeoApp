package corentinulysse.bikegeoapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mButtonNav = (Button) findViewById(R.id.a_m_bNav);
        Button mButtonYT = (Button) findViewById(R.id.a_m_bYT);
        ImageView velibLogo = (ImageView) findViewById(R.id.a_d_velibLogo);
        velibLogo.setImageResource(R.drawable.velib_logo);

        mButtonNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
                startActivity(intent);
            }
        });

        mButtonYT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=viNXFoKjoBo")));
            }
        });

    }

}
