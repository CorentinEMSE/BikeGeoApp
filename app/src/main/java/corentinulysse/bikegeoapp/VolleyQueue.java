package corentinulysse.bikegeoapp;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Classe gérant la file de requête
 */
public class VolleyQueue {
    private static RequestQueue ourInstance;

    public static RequestQueue getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = Volley.newRequestQueue(context);
        }
        return ourInstance;
    }

    private VolleyQueue() {
    }
}
